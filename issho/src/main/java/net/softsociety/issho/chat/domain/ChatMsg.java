package net.softsociety.issho.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMsg {

	public ChatMsg(String chatroom_seq, String chatmsg_recipient, String chatmsg_content) {
		// TODO Auto-generated constructor stub
		this.chatroom_seq = chatroom_seq;
		this.chatmsg_recipient = chatmsg_recipient;
		this.chatmsg_content = chatmsg_content;

	}

	private String chatmsg_seq;
	private String chatroom_seq;
	private String chatmsg_recipient;
	private String chatmsg_sendDate;
	private String chatmsg_content;
}
