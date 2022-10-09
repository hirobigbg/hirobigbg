package net.softsociety.issho.project.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Projects {
	
	private String prj_domain;
	private String prj_name;
	private String prj_date;
	private boolean prj_enabled;	//1(true) : 활성화 0(false) : 비활성화

}
