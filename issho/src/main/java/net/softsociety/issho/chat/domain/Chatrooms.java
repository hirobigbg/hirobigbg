package net.softsociety.issho.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chatrooms {

	// chatroom
	private String chatroom_seq;
	private String chatroom_name;
	private String prj_domain;

	public Chatrooms(String chatroom_seq, String chatroom_name, String prj_domain) {
		this.chatroom_seq = chatroom_seq;
		this.chatroom_name = chatroom_name;
		this.prj_domain = prj_domain;
	}

	// chatmember
	private String chat_member;
	
	public Chatrooms(String chatroom_seq, String chat_member) {
		this.chatroom_seq = chatroom_seq;
		this.chat_member = chat_member;
	}

	// chatmsg
	private String chatmsg_recipient;
	private String chatmsg_sendDate;
	private String chatmsg_content;
}
