package net.softsociety.issho.notice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
	int noticeCmt_seq;
	int notice_seq;
	String noticeCmt_writer;
	String noticeCmt_contents;
	String noticeCmt_date;
}