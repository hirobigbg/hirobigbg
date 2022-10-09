package net.softsociety.issho.manager.service;

import java.util.ArrayList;

import net.softsociety.issho.manager.domain.DriveFile;
import net.softsociety.issho.manager.domain.Helper;
import net.softsociety.issho.manager.domain.InvitationMember;

import net.softsociety.issho.manager.domain.MemberTemp;
import net.softsociety.issho.manager.domain.TaskCnt;
import net.softsociety.issho.manager.domain.TaskCntDone;
import net.softsociety.issho.manager.domain.TaskState;
import net.softsociety.issho.member.domain.Members;
import net.softsociety.issho.util.PageNavigator;


public interface ManagerService {
	
	// 관리자 페이지 리스트
	public ArrayList<Members> listManager(String domain,
			PageNavigator navi,String searchWord);
	
	//페이지 정보 생성
	public PageNavigator getPageNavigator(
			int pagePerGroup, int countPerPage, 
			int page,String searchWord);

	//구성원 페이지 멤버 정보 불러오기
	public Members getMemberInfo(String domain);
	
	//초대테이블에 초대인 메일 추가생성
	public void insertAttendant(Members members);

	//초대 메일 중복 확인
	public int invitationIdSearchOne(InvitationMember invitationMember);


	//드라이브 파일 리스트
	public ArrayList<DriveFile> listDriveFile(PageNavigator navi, String searchWord);

	public int insertDrive(DriveFile driveFile);

	public DriveFile readDriveFile(int driveFile_seq);

	public Members listManager(String damain,String email);
	
	//업무관리 리스트
	public ArrayList<MemberTemp> listWork(String prj_domain, PageNavigator navi, String searchWord);
	
	public int editMembRight(Members members);

	public int editPMRight(Members members);

	public TaskCnt taskCnt(String memEmail);

	public TaskCntDone taskCntDone(String memEmail);

	public ArrayList<Members> listInvitation(String prj_domain, PageNavigator navi, String searchWord);

	public TaskState taskState(String memEmail);

	public ArrayList<MemberTemp> listWork(String email);

	public ArrayList<MemberTemp> taskStateMap(String memEmail);

	public ArrayList<MemberTemp> memberName(String memb_name);

	public void insertHelper(Helper helper);

	public void insertInvitaion(InvitationMember invitation);


	


	
	
}
