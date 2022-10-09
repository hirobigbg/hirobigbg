package net.softsociety.issho.member.domain;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Members{
	
	private String memb_mail;
	private String memb_name;
	private String memb_pwd;
	private String memb_ogfile;
	private String memb_savedfile;
	private String memb_work;
	private String memb_phone;
	private boolean enabled;	//1 : 사용가능, 0 : 불가능ㄴ
	private String rolename;
	
	//지윤 멤버리스트
	private String prj_domain;
	private String pjmb_right;
	
	//지윤 초대관련
	private String membInv_accept;
	
	
}
