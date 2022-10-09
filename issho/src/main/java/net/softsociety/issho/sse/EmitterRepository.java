package net.softsociety.issho.sse;

import java.util.Map;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {

	SseEmitter save(String id, SseEmitter sseEmitter);

	void deleteById(String id);

	void saveEventCache(String key, Alarm alarm);

	Map<String, SseEmitter> findAllEmitterStartWithByMember(String id);

	Map<String, Object> findAllEventCacheStartWithByMemberId(String valueOf);

}
