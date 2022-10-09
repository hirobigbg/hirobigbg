package net.softsociety.issho.sse;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SSEService {

	private static final long DEFAULT_TIMEOUT = 60L * 1000 * 60; // 한 시간 에미터 유지

	private final EmitterRepository emitterRepository;

	public SSEService(EmitterRepository emitterRepository) {
		this.emitterRepository = emitterRepository;
	}

	public SseEmitter subscribe(String userId, String lastEventId) {

		String id = userId + "_" + System.currentTimeMillis();
		
		SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));
		
		emitter.onCompletion(()-> emitterRepository.deleteById(id));
		emitter.onTimeout(() -> emitterRepository.deleteById(id));
		
		Alarm alarm = new Alarm();
		alarm.setContent("subscribe 성공");
		
		sendToClient(emitter, id, alarm);
		
		if(!lastEventId.isEmpty()) {
			Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(userId));
			events.entrySet().stream()
					.filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
					.forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
		}
		
		
		return emitter;
	}
	
	public void send(String receiverId, String content, String url) {
		
		Alarm alarm = new Alarm(receiverId, content, url);
		String id = String.valueOf(receiverId);
		
		log.debug("send 도착");
		log.debug("alarm 객체 : {}", alarm);
		
		Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByMember(id);
		log.debug("sseEmitter : {}", sseEmitters);
		
		sseEmitters.forEach((key, emitter) -> {
			
			emitterRepository.saveEventCache(key, alarm);
			
			sendToClient(emitter, key, alarm);
			
		});
		
	}
	

	private void sendToClient(SseEmitter emitter, String id, Object alarm) {

		log.debug("sendToClient() called");

		try {
		emitter.send(SseEmitter.event()
					.id(id)
					.name("sse")
					.data(alarm));
		} catch(IOException exception) {
			emitterRepository.deleteById(id);
		}
		
	}

}
