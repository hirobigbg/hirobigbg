package net.softsociety.issho.chat.service;

import java.util.List;
import java.util.Map;

import net.softsociety.issho.chat.domain.ChatMember;
import net.softsociety.issho.chat.domain.ChatMsg;
import net.softsociety.issho.chat.domain.Chatrooms;
import net.softsociety.issho.member.domain.Members;

public interface ChatService {

	List<Chatrooms> chatList(Map<String, String> map);

	void addChatMem(String roomid, String id);

	List<Members> chatMemList(String roomid);

	List<ChatMsg> chatMsgs(String roomid);

	int insertMsg(ChatMsg msg);

	List<ChatMsg> recentMsgs();

	Chatrooms chatroomInfo(String roomid);

	void leaveChat(ChatMember chatmember);

}
