package net.softsociety.issho.manager.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvitationMember {

	public InvitationMember(String membInv_recipient, String prj_domain) {
		this.membInv_recipient = membInv_recipient;
		this.prj_domain = prj_domain;

	}

	private String membInv_seq;
	private String prj_domain;
	private String membInv_recipient;
	private int membInv_accept;
}
