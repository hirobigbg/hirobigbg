package net.softsociety.issho.project.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMember {

	private String prj_domain;
	private String memb_mail;
	private String pjmb_right;

	public ProjectMember(String prj_domain, String memb_mail) {
		this.prj_domain = prj_domain;
		this.memb_mail = memb_mail;
	}
}
