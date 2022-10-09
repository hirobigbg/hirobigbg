package net.softsociety.issho.notice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDetail extends Notice {
	String	memb_name;
	String	prj_name;	
}
