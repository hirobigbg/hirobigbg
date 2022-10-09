package net.softsociety.issho.chat;


import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import groovy.util.logging.Slf4j;



/**
 * @brief view단에서 sock.send()가 실행되면 데이터가 오는 곳.
 * @author 윤영혜
 *
 */

@Slf4j
public class ChatHandler extends TextWebSocketHandler{
	
	ObjectMapper objectMapper = new ObjectMapper();
	ChatRoomRepository repository = new ChatRoomRepository();
	
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
		String payload = message.getPayload();
		ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
		ChatRoom chatRoom = repository.getChatRoom(chatMessage.getChatRoomId());
		chatRoom.handleMessage(session, chatMessage, objectMapper);
	}
	
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
		repository.remove(session);
	}
}

