package net.softsociety.issho.chat.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import net.softsociety.issho.chat.domain.ChatMember;
import net.softsociety.issho.chat.domain.ChatMsg;
import net.softsociety.issho.chat.domain.Chatrooms;
import net.softsociety.issho.member.domain.Members;

@Mapper
public interface ChatDAO {

	//현재 참여중인 채팅방 리스트
	List<Chatrooms> chatList(Map<String, String> map);

	//새로운 채팅방 오픈
	void openNewChat(Chatrooms chroom);

	//채팅 멤버 추가
	void addChatMem(Chatrooms chatroom);

	//채팅 멤버 리스트
	List<Members> chatMemList(String chatroom_seq);

	//메시지 목록
	List<ChatMsg> chatMsgs(String chatroom_seq);
	
	//메시지 인서트
	int insertMsg(ChatMsg msg);

	//최신 메시지 리스트
	List<ChatMsg> recentMsgs();

	Chatrooms chatroomInfo(String chatroom_seq);

	void leaveChat(ChatMember chatmember);

}
