package net.softsociety.issho.task.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Taskstaff {
	
	public Taskstaff(int task_seq, String memb_mail, int tsuper_perform) {
		this.task_seq = task_seq;
		this.memb_mail = memb_mail;
		this.tsuper_perform = tsuper_perform;
	}
	private int task_seq;
	private String memb_mail;
	private int tsuper_perform;

	
	//Not the columns of db
	private String memb_name;
	private String memb_ogfile;
}
