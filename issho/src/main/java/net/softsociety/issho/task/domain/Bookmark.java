package net.softsociety.issho.task.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {
	
	// * 신승훈 즐겨찾기 테이블 attributes
	private int Bookmark_seq;
	private int task_seq;
	private String memb_mail;

}
