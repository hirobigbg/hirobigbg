package net.softsociety.issho.chat.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.issho.chat.dao.ChatDAO;
import net.softsociety.issho.chat.domain.ChatMember;
import net.softsociety.issho.chat.domain.ChatMsg;
import net.softsociety.issho.chat.domain.Chatrooms;
import net.softsociety.issho.member.domain.Members;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService{
	
	@Autowired
	ChatDAO chatDao;

	@Override
	public List<Chatrooms> chatList(Map<String, String> map) {

		List<Chatrooms> list = chatDao.chatList(map);
		
		return list;
	}

	@Override
	public void addChatMem(String roomid, String id) {
		
		Chatrooms chatroom = new Chatrooms(roomid, id);
		
		chatDao.addChatMem(chatroom);
		
	}

	@Override
	public List<Members> chatMemList(String roomid) {
		
		List<Members> chatMemList = chatDao.chatMemList(roomid);
		
		log.debug("chatMemblist : {}", chatMemList);
		
		return chatMemList;
	}

	@Override
	public List<ChatMsg> chatMsgs(String roomid) {

		 List<ChatMsg> chatMsgs = chatDao.chatMsgs(roomid);
		
		return chatMsgs;
	}

	@Override
	public int insertMsg(ChatMsg msg) {
		
		int result = chatDao.insertMsg(msg);
		
		return result;
		
	}

	@Override
	public List<ChatMsg> recentMsgs() {

		List<ChatMsg> list = chatDao.recentMsgs();
		
		return list;
	}

	@Override
	public Chatrooms chatroomInfo(String roomid) {

		Chatrooms chatroom = chatDao.chatroomInfo(roomid);
		
		return chatroom;
	}

	@Override
	public void leaveChat(ChatMember chatmember) {
		
		chatDao.leaveChat(chatmember);
		
	}

}
