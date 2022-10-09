package net.softsociety.issho.notice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDetail extends Comment {
	String notice_name;
	String memb_name;
}