package net.softsociety.issho.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMember {
	
	String chatmember;
	String chatroom_seq;
}
