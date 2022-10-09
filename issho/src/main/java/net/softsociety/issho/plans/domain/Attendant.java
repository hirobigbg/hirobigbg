package net.softsociety.issho.plans.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Attendant {
	
	private int plan_seq;
	private String memb_name;
	private String memb_mail;
	private int att_whether; //참석여부 DEFAULT 0
	
	public Attendant(int plan_seq, String memb_mail, int att_whether) {
		this.plan_seq = plan_seq;
		this.memb_mail = memb_mail;
		this.att_whether = att_whether;
	}
}
