package net.softsociety.issho.task.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.issho.member.service.MemberService;
import net.softsociety.issho.task.domain.GanttTask;
import net.softsociety.issho.task.domain.Task;
import net.softsociety.issho.task.domain.Taskstaff;
import net.softsociety.issho.task.service.TaskService;

@Controller
@Slf4j
@RequestMapping("**/task")
public class DashboardController {

	@Autowired
	MemberService memservice;

	@Autowired
	TaskService taskservice;

	/**
	 * 
	 * 칸반보드(전체)
	 * 
	 * @param request : 현재 도메인 얻기 위함
	 * @param model   : 태스크 리스트, 프로젝트 멤버 리스트, 유저 정보, 도메인
	 * @param user    : 현재 유저 세션
	 * @return 칸반보드 리스트 html
	 * 
	 * @author 윤영혜
	 */
	@GetMapping("kanban")
	public String kanban(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails user) {

		String calledValue = request.getServletPath();
		String[] splitedUrl = calledValue.split("/");
		String prj_domain = splitedUrl[1];

		List<Task> list = taskservice.SelectAlltask1(prj_domain);

		List<Taskstaff> pjmb = taskservice.projectMembers(prj_domain);
		log.debug("pjmb : {}", pjmb);

		model.addAttribute("list", list);
		model.addAttribute("user", user);
		model.addAttribute("domain", prj_domain);
		model.addAttribute("members", pjmb);

		log.debug("list : {}", list);

		return "taskView/task_kanban";
	}

	/**
	 * 칸반보드(내가 할당한 업무)
	 * 
	 * @param request : 현재 도메인 얻기 위함
	 * @param model   : 태스크 리스트, 프로젝트 멤버 리스트, 유저 정보, 도메인
	 * @param user    : 현재 유저 세션
	 * @return 칸반보드 리스트 html
	 * 
	 * @author 윤영혜
	 */

	@GetMapping("myAllocateKanban")
	public String myAllocate(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails user) {

		String calledValue = request.getServletPath();
		String[] splitedUrl = calledValue.split("/");
		String prj_domain = splitedUrl[1];

		String id = user.getUsername();
		Map<String, String> map = new HashMap<>();

		map.put("prj_domain", prj_domain);
		map.put("memb_mail", id);

		List<Task> list = taskservice.myAllocate(map);

		List<Taskstaff> pjmb = taskservice.projectMembers(prj_domain);
		log.debug("pjmb : {}", pjmb);

		model.addAttribute("list", list);
		model.addAttribute("user", user);
		model.addAttribute("domain", prj_domain);
		model.addAttribute("members", pjmb);

		log.debug("list : {}", list);

		return "taskView/task_MyAllocateKanban";
	}

	/**
	 * 칸반보드(내가 담당 중 업무)
	 * 
	 * * @param request : 현재 도메인 얻기 위함
	 * 
	 * @param model : 태스크 리스트, 프로젝트 멤버 리스트, 유저 정보, 도메인
	 * @param user  : 현재 유저 세션
	 * @return 칸반보드 리스트 html
	 * 
	 * @author 윤영혜
	 */
	@GetMapping("myChargedKanban")
	public String myCharged(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails user) {

		String calledValue = request.getServletPath();
		String[] splitedUrl = calledValue.split("/");
		String prj_domain = splitedUrl[1];

		String id = user.getUsername();
		Map<String, String> map = new HashMap<>();

		map.put("prj_domain", prj_domain);
		map.put("memb_mail", id);

		List<Task> list = taskservice.myCharged(map);

		List<Taskstaff> pjmb = taskservice.projectMembers(prj_domain);
		log.debug("pjmb : {}", pjmb);

		model.addAttribute("list", list);
		model.addAttribute("user", user);
		model.addAttribute("domain", prj_domain);
		model.addAttribute("members", pjmb);

		log.debug("list : {}", list);

		return "taskView/task_MyChargedKanban";
	}

	/**
	 * 칸반보드 상태 변경 ajax
	 * 
	 * @param task_state
	 * @param task_seq
	 */

	@PostMapping("changeState")
	@ResponseBody
	public void changeState(String task_state, String task_seq) {

		log.debug("task_state : {}", task_state);
		log.debug("task_seq : {}", task_seq);

		Map<String, String> map = new HashMap<>();

		map.put("task_state", task_state);
		map.put("task_seq", task_seq);

		taskservice.changeState(map);
	}

	@GetMapping("gantt")
	public String gantt(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails user) {

		String calledValue = request.getServletPath();
		String[] splitedUrl = calledValue.split("/");
		String prj_domain = splitedUrl[1];

		List<Task> list = taskservice.SelectAlltask1(prj_domain);

		List<Taskstaff> pjmb = taskservice.projectMembers(prj_domain);
		
		log.debug("pjmb : {}", pjmb);

		model.addAttribute("list", list);
		model.addAttribute("user", user);
		model.addAttribute("domain", prj_domain);
		model.addAttribute("members", pjmb);

		log.debug("list : {}", list);

		model.addAttribute("list", list);

		return "taskView/task_gantt";
	}
	
	@PostMapping("/changeGantt")
	@ResponseBody
	public void changeGantt(GanttTask task) {
		
		log.debug("ganttTask 객체 : {}", task);
		
		taskservice.changeDate(task);
	}

}
