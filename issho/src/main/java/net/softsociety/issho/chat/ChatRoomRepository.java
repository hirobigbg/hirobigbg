package net.softsociety.issho.chat;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.issho.chat.dao.ChatDAO;
import net.softsociety.issho.chat.domain.Chatrooms;

/**
 * @brief 다중 채팅방이므로 (여러) 채팅방을 관리해줄 자바 클래스.
 * @author 윤영혜
 *
 */
@Slf4j
@Service
public class ChatRoomRepository {

	// 다중 채팅방 구현을 위한 map. 채팅방 고유번호(난수)와 chatroom 정보를 저장한다.
	public static Map<String, ChatRoom> chatRoomMap = new HashMap<String, ChatRoom>();

	// static : home컨트롤러에서 model로 사용하기 위함.
	public static Collection<ChatRoom> chatRooms;

	@Autowired
	ChatDAO chatdao;

	/**
	 * 새로운 채팅방 오픈
	 * 
	 */
	public String newChatRoom(String chatroom_name, String prj_domain) {
		
		log.debug("newChatRoom 실행");
		
		// 고유한 난수를 생성한다. hashmap의 key 역할을 해줄 것임.
		String uuid = UUID.randomUUID().toString();
		//
		ChatRoom chatRoom = new ChatRoom(uuid);
		// 채팅방 목록에 추가
		chatRoomMap.put(chatRoom.getId(), chatRoom);

		// chatroom DB에 insert
		Chatrooms chroom = new Chatrooms(uuid, chatroom_name, prj_domain);

		// dao 호출
		chatdao.openNewChat(chroom);

		return uuid;

		// chatRooms = chatRoomMap.values();
	}

	/**
	 * 기존 채팅방 들어가기
	 * 
	 * uuid 받아와서 새로운 채팅방 생성한 뒤 현 채팅방 목록에 추가
	 * 
	 * @param id
	 * @return
	 */
	public void enterChatRoom(String chatroom_seq) {
		log.debug("enterChat 실행됨");

		ChatRoom chatRoom = new ChatRoom(chatroom_seq);
		// 채팅방 목록에 추가
		chatRoomMap.put(chatRoom.getId(), chatRoom);
		
		log.debug("chatRoomMap : {}", chatRoomMap);

		// chatRooms = chatRoomMap.values();
	}

	/**
	 * 열려 있는 채팅방 참가.
	 * @param roomId
	 */
	public void enterOpenedChatRoom(String roomId) {

		log.debug("enterOpenedChat 실행됨");
		
		net.softsociety.issho.chat.ChatRoom chatroom = chatRoomMap.get(roomId);

		log.debug("chatroom : {}", chatroom);

		// chatRooms = chatRoomMap.values();
	}

	public ChatRoom getChatRoom(String id) {

		// 채팅방 목록에 추가
		log.debug("chat id : {}", id);
		log.debug("chatRoomMap get:{}", chatRoomMap);

		return chatRoomMap.get(id);
	}

	public Map<String, ChatRoom> getChatRooms() {
		return chatRoomMap;
	}

	public void remove(WebSocketSession session) {
		this.chatRooms.parallelStream().forEach(chatRoom -> chatRoom.remove(session));
	}

}
