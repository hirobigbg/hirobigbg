package net.softsociety.issho.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.issho.notice.dao.NoticeDAO;
import net.softsociety.issho.notice.domain.Notice;
import net.softsociety.issho.plans.dao.PlansDAO;
import net.softsociety.issho.plans.domain.Plans;
import net.softsociety.issho.project.dao.ProjectDAO;
import net.softsociety.issho.project.domain.ProjectMember;
import net.softsociety.issho.project.domain.Projects;
import net.softsociety.issho.task.dao.TaskDAO;
import net.softsociety.issho.task.domain.Task;
import net.softsociety.issho.util.PageNavigator;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService{

	@Autowired
	ProjectDAO pjDao;
	
	@Autowired
	NoticeDAO noticeDAO;
	
	@Autowired
	PlansDAO plansDAO;
	
	@Autowired
	TaskDAO taskDAO;
	
	@Override
	public int domainCheck(String prj_domain) {
		
		int result = pjDao.domainCheck(prj_domain);
		
		return result;
	}

	@Override
	public void newProject(Projects projects) {
	
		pjDao.newProject(projects);
		
	}

	@Override
	public void grantPM(ProjectMember pjmb) {
		
		pjDao.grantPM(pjmb);
		
	}

	@Override
	public Projects searchOne(String prj_domain) {

		Projects project = pjDao.searchOne(prj_domain);
		
		return project;
	}
	
	@Override
	public ArrayList<Projects> listProjects(PageNavigator navi, String searchWord) {
		HashMap<String, String> map = new HashMap<>();
		map.put("searchWord", searchWord);
		RowBounds rb = new RowBounds(navi.getStartRecord(),navi.getCountPerPage());
		ArrayList<Projects> list = pjDao.listProjects(map, rb);
		
		return list;
	}

	@Override
	public Projects getProjectsInfo(String domain) {
		
		Projects projects = pjDao.getProjectsInfo(domain);
		
		return projects;
	}
	
	
	  //* 신승훈 메인창 목록 (최신 공지사항)
	  @Override 
	  public List<Notice> selectMainNotice(String prj_domain) {
		log.debug("ProjectServiceImpl [selectMainNotice] Start");
		  
		List<Notice> list = noticeDAO.selectMainNotice(prj_domain);
		log.debug("ProjectServiceImpl selectMainNotice list : {}", list);
		  
	  	log.debug("ProjectServiceImpl [selectMainNotice] End"); 
	  	return list; 
	}
	 
 
	//* 신승훈 메인창 목록 (내 일정)
	@Override
	public List<Plans> selectMainPlan(String userid) {
		log.debug("ProjectServiceImpl [selectMainPlan] Start");
		
		List<Plans> list = plansDAO.selectMainPlan(userid);
		log.debug("ProjectServiceImpl selectMainPlan list : {}", list);
		
		log.debug("ProjectServiceImpl [selectMainPlan] End");
		return list;
	}
	
	//* 신승훈 메인창 목록 (할당 태스크 현황) 담당 태스크 리스트)
	@Override
	public List<Task> selectMainMysender(String prj_domain, String userid) {
		log.debug("ProjectServiceImpl [selectMainTask] Start");
		
		Map<String, String> domainAndUsrid = new HashMap<>();
		domainAndUsrid.put("prj_domain", prj_domain);
		domainAndUsrid.put("task_sender", userid);
		
		List<Task> list = taskDAO.selectMainMysender(domainAndUsrid);
		log.debug("ProjectServiceImpl selectMainTask list : {}", list);
		
		log.debug("ProjectServiceImpl [selectMainTask] End");
		return list;
	}
	
	//* 신승훈 메인창 목록 (담당 태스크 리스트)
	@Override
	public List<Task> selectMainMyStaff(String prj_domain, String userid) {
		log.debug("ProjectServiceImpl [selectMainMyStaff] Start");
		
		Map<String, String> domainAndUsrid = new HashMap<>();
		domainAndUsrid.put("prj_domain", prj_domain);
		domainAndUsrid.put("task_sender", userid);
		
		List<Task> list = taskDAO.selectMainMyStaff(domainAndUsrid);
		log.debug("ProjectServiceImpl selectMainMyStaff list : {}", list);
		
		log.debug("ProjectServiceImpl [selectMainMyStaff] End");
		return list;
	}
	
	
	
	
	
}
