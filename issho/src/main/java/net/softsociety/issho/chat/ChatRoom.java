package net.softsociety.issho.chat;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import net.softsociety.issho.chat.domain.ChatMsg;
import net.softsociety.issho.chat.service.ChatService;

/**
 * @brief 채팅방 입장, 메시지 전송, 채팅방 삭제 로직. chatRoomRepository 클래스의 chatRoomMap 변수를 채우기
 *        위한 클래스.
 * @author 윤영혜
 *
 */

public class ChatRoom {
	
	@Autowired
	ChatService chatService;

	// 채팅방 아이디
	String id;
	// 채팅방 참가자 세션을 모아둘 set.
	// set 안에서 sessions, 지금 들어온 session
	Set<WebSocketSession> sessions = new HashSet<>();

	public ChatRoom(String room_id) {
		this.id = room_id;
	}

	public String getId() {
		return id;
	}

	public Set<WebSocketSession> getSessions() {
		return sessions;
	}

	///

	public void handleMessage(WebSocketSession session, ChatMessage chatMessage, ObjectMapper objectMapper)
			throws JsonProcessingException {
		System.out.println("handlemsg 실행됨!!");
		if (chatMessage.getType().equals("JOIN")) {
			join(session);
			System.out.println("join 실행됨 : " + session);
		} else {
			send(session, chatMessage, objectMapper);
			System.out.println("handlemessage send session : " + session);
			System.out.println("handlemessage send chatMessage : " + chatMessage);
			System.out.println("handlemessage send chatMessage sender : " + chatMessage.sender);
			System.out.println("handlemessage send chatMessage message: " + chatMessage.message);
			System.out.println("handlemessage send objectMapper : " + objectMapper);
			
			
		}
	}

	private void join(WebSocketSession session) {

		System.out.println("Join 실행됨");

		sessions.add(session);

		System.out.println(sessions);
	}

	// sessions 변수에 있는 모든 사용자에게 메시지를 보냄.
	private <T> void send(WebSocketSession session, T messageObject, ObjectMapper objectMapper)
			throws JsonProcessingException {
		TextMessage message = new TextMessage(objectMapper.writeValueAsBytes(messageObject));

		//현재 로그인한...
		
		System.out.println("send 함수 실행됨");
		System.out.println("send 함수 session : " + session);
		System.out.println("send 함수 messageObject : " + messageObject);

		// parallelStream : 프로세서의 다중 코어를 활용하기 위한 자바 8 문법.
		sessions.parallelStream().forEach(eachSession -> {
			try {
				eachSession.sendMessage(message);
				
				if(eachSession == session) {
					System.out.println("sender session : " + message);
				}
				
				System.out.println("각 세션 : " + message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	public void remove(WebSocketSession target) {
		String targetId = target.getId();
		sessions.removeIf(session -> session.getId().equals(targetId));
	}
}
