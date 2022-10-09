package net.softsociety.issho.plans.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plans {

	private Integer plan_seq;
	private String plan_name;
	private String plan_startDate;
	private String plan_endDate;
	private String plan_place;
	private Integer plan_priority; //중요도 DEFAULT 0 , 0,1,2(하,중,상)
	private String plan_content;
	private String memb_mail;
	
	//attendant 관련 속성 
	private Integer att_whether; //참석여부 DEFAULT 0

	private List<Attendant> attendantList;
}
