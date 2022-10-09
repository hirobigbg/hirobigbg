package net.softsociety.issho.manager.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import net.softsociety.issho.manager.domain.DriveFile;
import net.softsociety.issho.manager.domain.Helper;
import net.softsociety.issho.manager.domain.InvitationMember;
import net.softsociety.issho.manager.domain.MemberTemp;
import net.softsociety.issho.manager.domain.TaskCnt;
import net.softsociety.issho.manager.domain.TaskCntDone;
import net.softsociety.issho.manager.domain.TaskState;
import net.softsociety.issho.member.domain.Members;
import net.softsociety.issho.project.domain.ProjectMember;

@Mapper
public interface ManagerDAO {
	public ArrayList<Members> listMembers(
			HashMap<String, String> map,RowBounds rb);

	public int count(HashMap<String, String> map);

	public Members getMemberInfo(String domain);

	public void insertAttendant(Members members);

	public int invitationIdSearchOne(InvitationMember invitationMember);

	public ArrayList<DriveFile> listDriveFile(
			HashMap<String, String> map, RowBounds rb);

	public void updateAccept(InvitationMember invitation);

	public InvitationMember getOneObject(InvitationMember invitation);

	public int insertDrive(DriveFile driveFile);

	public DriveFile readDriveFile(int driveFile_seq);

	public Members listMembers2(HashMap<String, String> map);

	public ArrayList<MemberTemp> listWork(HashMap<String, String> map, RowBounds rb);

	public ArrayList<MemberTemp> listWork(String prj_domain);

	public int editMembRight(Members members);

	public int editPMRight(Members members);

	public TaskCnt taskCnt(String memEmail);

	public TaskCntDone taskCntDone(String memEmail);

	public ProjectMember getPrjMem(ProjectMember prjMem);

	public void insertPrjMem(ProjectMember prjMem);

	public ArrayList<Members> listInvitation(HashMap<String, String> map, RowBounds rb);

	public TaskState taskState(String memEmail);

	public ArrayList<MemberTemp> listWorkEx(String email);

	public ArrayList<MemberTemp> taskStateMap(String memEmail);

	public ArrayList<MemberTemp> mamberName(String memb_name);

	public void insertHelper(Helper helper);

	public void insertInvitaion(InvitationMember invitation);


}
