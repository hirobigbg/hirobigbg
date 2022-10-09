package net.softsociety.issho.notice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notice {
	int		notice_seq;
	String	prj_domain;
	String	notice_name;
	String	notice_writer;
	String	notice_contents;
	String	notice_ogFilr;
	String	notice_saveFile;
	String	notice_date;
	int		notice_hits;
}
