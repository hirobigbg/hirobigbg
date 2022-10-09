package net.softsociety.issho.project.service;

import java.util.ArrayList;
import java.util.List;

import net.softsociety.issho.notice.domain.Notice;
import net.softsociety.issho.plans.domain.Plans;
import net.softsociety.issho.project.domain.ProjectMember;
import net.softsociety.issho.project.domain.Projects;
import net.softsociety.issho.task.domain.Task;
import net.softsociety.issho.util.PageNavigator;

public interface ProjectService {

	int domainCheck(String prj_domain);

	void newProject(Projects projects);

	void grantPM(ProjectMember pjmb);

	Projects searchOne(String prj_domain);
	
	//관리자 페이지 프로젝트 리스트
	ArrayList<Projects> listProjects(PageNavigator navi, String searchWord);

	Projects getProjectsInfo(String domain);
	
	//* 신승훈 메인창 목록 (최신 공지사항)
	List<Notice> selectMainNotice(String prj_domain);
	//* 신승훈 메인창 목록 (내 일정)
	List<Plans> selectMainPlan(String userid);
	//* 신승훈 메인창 목록 (할당 태스크 현황, 담당 태스크 리스트)
	List<Task> selectMainMysender(String prj_domain, String userid);
	//* 신승훈 메인창 목록 (담당 태스크 리스트)
	List<Task> selectMainMyStaff(String prj_domain, String userid);

	
	



}
