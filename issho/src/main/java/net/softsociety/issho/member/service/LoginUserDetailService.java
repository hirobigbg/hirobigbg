package net.softsociety.issho.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.issho.member.dao.MemberDAO;
import net.softsociety.issho.member.domain.LoginUser;
import net.softsociety.issho.member.domain.Members;
import net.softsociety.issho.project.domain.ProjectMember;

/**
 * UserDetailsService를 구현하여 로그인 프로세스를 커스터마이징한다.
 *
 */
@Slf4j
@Service
public class LoginUserDetailService implements UserDetailsService {

	@Autowired
	MemberDAO dao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		log.debug("username : {}", username);

		// 로그인폼에 입력한 아이디를 이용하여 DB를 검색한다.
		Members member = dao.getUserById(username);

		// 검색결과가 없으면 UsernameNotFoundException 예외를 발생시킨다
		if (member == null) {
			throw new UsernameNotFoundException(username);
		}

		// 도메인의 초대 멤버 여부 확인	
		ProjectMember pjmem = dao.getProjectMemById(username);
		
		LoginUser user = new LoginUser();
		user.setMember(member);
		user.setAuth(pjmem.getPjmb_right());
		
		return user;
	}

}
