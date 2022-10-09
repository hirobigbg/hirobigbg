package net.softsociety.issho.manager.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.issho.manager.dao.ManagerDAO;
import net.softsociety.issho.manager.domain.DriveFile;
import net.softsociety.issho.manager.domain.Helper;
import net.softsociety.issho.manager.domain.InvitationMember;

import net.softsociety.issho.manager.domain.MemberTemp;
import net.softsociety.issho.manager.domain.TaskCnt;
import net.softsociety.issho.manager.domain.TaskCntDone;
import net.softsociety.issho.manager.domain.TaskState;
import net.softsociety.issho.member.domain.Members;
import net.softsociety.issho.sse.SSEService;
import net.softsociety.issho.task.dao.TaskDAO;
import net.softsociety.issho.task.domain.Task;
import net.softsociety.issho.util.PageNavigator;

@Slf4j
@Service
public class ManagerServiceImpl implements ManagerService {

	@Autowired
	private ManagerDAO managerDAO;
	
	@Autowired
	private SSEService sseService;
	
	@Autowired
	private TaskDAO taskDAO;
	
	
	@Override
	public ArrayList<Members> listManager(String prj_domain,PageNavigator navi, String searchWord) {
		HashMap<String, String> map = new HashMap<>();
		map.put("searchWord", searchWord);
		map.put("prj_domain", prj_domain);
		log.debug("서비스단 도메인:" + prj_domain);
		RowBounds rb = new RowBounds(navi.getStartRecord(),navi.getCountPerPage());
		ArrayList<Members> list = managerDAO.listMembers(map, rb);
		
		return list;
	}

	@Override
	public PageNavigator getPageNavigator(
			int pagePerGroup, int countPerPage, int page,
			String searchWord) {
		
		//전체 글 개수	
		HashMap<String, String> map = new HashMap<>();
		map.put("searchWord", searchWord);
		int total = managerDAO.count(map);
		
		PageNavigator navi = new PageNavigator(pagePerGroup, countPerPage, page, total);
		return navi;
	}

	@Override
	public Members getMemberInfo(String domain) {
		
		Members members = managerDAO.getMemberInfo(domain);
		
		return members;
	}

	@Override
	public void insertAttendant(Members members) {
		managerDAO.insertAttendant(members);
	}

	@Override
	public int invitationIdSearchOne(InvitationMember invitationMember) {
		int result = managerDAO.invitationIdSearchOne(invitationMember);
		return result;
	}

	@Override
	public ArrayList<DriveFile> listDriveFile(PageNavigator navi, String searchWord) {
		HashMap<String, String> map = new HashMap<>();
		map.put("searchWord", searchWord);
		RowBounds rb = new RowBounds(navi.getStartRecord(),navi.getCountPerPage());
		ArrayList<DriveFile> list = managerDAO.listDriveFile(map, rb);
		
		return list;
	}

	@Override
	public int insertDrive(DriveFile driveFile) {
		return managerDAO.insertDrive(driveFile);
	}

	@Override
	public DriveFile readDriveFile(int driveFile_seq) {
		DriveFile driveFile = managerDAO.readDriveFile(driveFile_seq);
		return driveFile;
	}

	@Override
	public Members listManager(String domain,String email) {
		HashMap<String, String> map = new HashMap<>();
		map.put("domain", domain);
		map.put("email", email);
		Members members = managerDAO.listMembers2(map);
		return members;
	}

	@Override
	public ArrayList<MemberTemp> listWork(String prj_domain, PageNavigator navi, String searchWord) {
			HashMap<String, String> map = new HashMap<>();
			map.put("searchWord", searchWord);
			map.put("prj_domain", prj_domain);
			log.debug("서비스단 도메인:" + prj_domain);
			RowBounds rb = new RowBounds(navi.getStartRecord(),navi.getCountPerPage());
			ArrayList<MemberTemp> listWork = managerDAO.listWork(prj_domain);
		return listWork;
	}

	@Override
	public int editMembRight(Members members) {
		int result = managerDAO.editMembRight(members);
		return result;
	}
	
	@Override
	public int editPMRight(Members members) {
		int result = managerDAO.editPMRight(members);
		return result;
	}

	@Override
	public TaskCnt taskCnt(String memEmail) {
		TaskCnt taskCnt= managerDAO.taskCnt(memEmail);
		return taskCnt;
	}

	@Override
	public TaskCntDone taskCntDone(String memEmail) {
		TaskCntDone taskCntDone = managerDAO.taskCntDone(memEmail);
		return taskCntDone;
	}

	@Override
	public ArrayList<Members> listInvitation(String prj_domain, PageNavigator navi, String searchWord) {
		HashMap<String, String> map = new HashMap<>();
		map.put("searchWord", searchWord);
		map.put("prj_domain", prj_domain);
		log.debug("서비스단 도메인:" + prj_domain);
		RowBounds rb = new RowBounds(navi.getStartRecord(),navi.getCountPerPage());
		ArrayList<Members> list = managerDAO.listInvitation(map, rb);
		
		return list;
	}

	@Override
	public TaskState taskState(String memEmail) {
		TaskState taskState = managerDAO.taskState(memEmail);
		return taskState;
	}

	@Override
	public ArrayList<MemberTemp> listWork(String email) {
		ArrayList<MemberTemp> listWork = managerDAO.listWorkEx(email);
		return listWork;
	}
	
	@Override
	public ArrayList<MemberTemp> taskStateMap(String memEmail) {
		ArrayList<MemberTemp> MemberTemp = managerDAO.taskStateMap(memEmail);
		return MemberTemp;
	}

	@Override
	public ArrayList<MemberTemp> memberName(String memb_name) {
		ArrayList<MemberTemp> memberName = managerDAO.mamberName(memb_name);
		return memberName;
	}

	@Override
	public void insertHelper(Helper helper) {
		
		log.debug("insert helper 실행");
		
		managerDAO.insertHelper(helper);
		Task task= taskDAO.selectTaskByTaskSeq(helper.getTask_seq());
		sseService.send(helper.getMemb_mail(), task.getTask_name() + "-Request for Work", null);
		
	}

	@Override
	public void insertInvitaion(InvitationMember invitation) {
		managerDAO.insertInvitaion(invitation);
	}

	
	

}
