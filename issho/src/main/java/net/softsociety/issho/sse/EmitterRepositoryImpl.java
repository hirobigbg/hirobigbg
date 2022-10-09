package net.softsociety.issho.sse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@NoArgsConstructor
public class EmitterRepositoryImpl implements EmitterRepository {
	
	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
	
	private final Map<String, Object> eventCache = new ConcurrentHashMap<>();
	
	
	@Override
	public SseEmitter save(String id, SseEmitter sseEmitter) {

		emitters.put(id, sseEmitter);
		
		return sseEmitter;
	}

	@Override
	public void deleteById(String id) {

		emitters.remove(id);

	}

	@Override
	public void saveEventCache(String key, Alarm alarm) {
		
		eventCache.put(key, alarm);
		
		log.debug("saveEventCache");
		
	}

	@Override
	public Map<String, SseEmitter> findAllEmitterStartWithByMember(String id) {

		return emitters.entrySet().stream()
				.filter(entry -> entry.getKey().startsWith(id))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	@Override
	public Map<String, Object> findAllEventCacheStartWithByMemberId(String valueOf) {

		return eventCache.entrySet().stream()
				.filter(entry -> entry.getKey().startsWith(valueOf))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

}
