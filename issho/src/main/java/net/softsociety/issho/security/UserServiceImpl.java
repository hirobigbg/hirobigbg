package net.softsociety.issho.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Service;

import net.softsociety.issho.member.dao.MemberDAO;

@Service
public class UserServiceImpl implements UserService {
	
	//로그인 시 해당 프로젝트에 속해 있는 멤버인지 확인하여 소속이 아닐 경우 로그인 거부
	//초대 수락 여부 확인하여 수락 x -> 수락 o 변경
	//로그인 시 해당 프로젝트에 대한 권한이 별도로 있어 어떻게 처리해주어야 할까. . . .

	@Autowired
	private DataSource dataSource;

	@Autowired
	MemberDAO memDao;

	// 인증을 위한 쿼리
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				// 인증 (로그인)
				.usersByUsernameQuery("select memb_mail username, memb_pwd password, enabled " + "from members "
						+ "where memb_mail = ?")
				// 권한
				.authoritiesByUsernameQuery(
						"select memb_mail username, rolename role_name " + "from members " + "where memb_mail = ?");
	}

}
