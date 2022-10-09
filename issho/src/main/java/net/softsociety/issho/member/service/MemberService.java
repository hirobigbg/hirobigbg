package net.softsociety.issho.member.service;

import java.util.ArrayList;
import net.softsociety.issho.manager.domain.InvitationMember;
import java.util.Map;
import net.softsociety.issho.member.domain.Members;
import net.softsociety.issho.project.domain.ProjectMember;
import net.softsociety.issho.util.PageNavigator;

public interface MemberService {

	public ArrayList<Members> listMembers(Members members);
	
	public int idSearchOne(String memb_mail);

	public void memberJoin(Members members);

	public ArrayList<Members> searchPjMem(String prj_domain);

	public int deleteMember(Members members);
	
	//초대여부 확인 후 수락 여부 변경해주는 메소드
	public InvitationMember enterProject(InvitationMember invitation);

	public PageNavigator getNoticePageNavi(int pagePerGroup, int countPerPage, int page, String type,
			String searchWord);

	//프로젝트 멤버 객체 얻어오기
	public ProjectMember getPrjMem(ProjectMember prjMem);

	//프로젝트 멤버 인서트
	public void insertPrjMem(ProjectMember prjMem);

}
