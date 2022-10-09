package net.softsociety.issho.member.service;

import java.util.ArrayList;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.softsociety.issho.manager.dao.ManagerDAO;
import net.softsociety.issho.manager.domain.InvitationMember;
import net.softsociety.issho.member.dao.MemberDAO;
import net.softsociety.issho.member.domain.Members;
import net.softsociety.issho.project.domain.ProjectMember;
import net.softsociety.issho.util.PageNavigator;

@Transactional
@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberDAO memDao;

	@Autowired
	private ManagerDAO membersDAO;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public ArrayList<Members> listMembers(Members members){
		
		ArrayList<Members> member = memDao.listMembers(members); 
		
		return member;
	}

	@Override
	public int idSearchOne(String memb_mail) {

		int result = memDao.idSearchOne(memb_mail);

		return result;
	}

	@Override
	public void memberJoin(Members members) {

		String encodedPassword = passwordEncoder.encode(members.getMemb_pwd());

		members.setMemb_pwd(encodedPassword);

		memDao.memberJoin(members);

	}

	@Override
	public ArrayList<Members> searchPjMem(String prj_domain) {
		// TODO Auto-generated method stub

		ArrayList<Members> pjMemList = memDao.searchPjMem(prj_domain);

		return pjMemList;

	}

	@Override
	public int deleteMember(Members members) {

		int result = memDao.deleteMember(members);

		return result;
	}

	@Override
	public InvitationMember enterProject(InvitationMember invitation) {

		InvitationMember member = membersDAO.getOneObject(invitation);

		// 수락 여부 업데이트
		if (member != null && member.getMembInv_accept() == 0) {
			membersDAO.updateAccept(invitation);
		}
		return member;
	}

	public PageNavigator getNoticePageNavi(int pagePerGroup, int countPerPage, int page, String type,
			String searchWord) {
		// TODO Auto-generated method stub
		return null;
	}

	//프로젝트 멤버 객체 얻어오기
	@Override
	public ProjectMember getPrjMem(ProjectMember prjMem) {

		ProjectMember prjmem = membersDAO.getPrjMem(prjMem);
		
		return prjmem;
	}

	@Override
	public void insertPrjMem(ProjectMember prjMem) {

		membersDAO.insertPrjMem(prjMem);
		
	}

}
