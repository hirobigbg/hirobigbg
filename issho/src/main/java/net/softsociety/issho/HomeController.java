package net.softsociety.issho;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.issho.manager.domain.DriveFile;
import net.softsociety.issho.notice.domain.Notice;
import net.softsociety.issho.plans.domain.Plans;
import net.softsociety.issho.project.domain.Projects;
import net.softsociety.issho.project.service.ProjectService;
import net.softsociety.issho.task.domain.Task;
import net.softsociety.issho.task.domain.Taskfile;
import net.softsociety.issho.task.service.TaskService;

@Slf4j
@Controller
public class HomeController {

	@Autowired
	ProjectService pjservice;
  
	/**
	 * @brief 메인화면 이동
	 * @author 윤영혜
	 */
	@GetMapping({ "", "/" })
	public String home() {
		return "home";
	}

	@GetMapping("sidebar")
	public String footer() {
		return "fragments/sidebar";
	}

	/**
	 * 로그인 폼 이동
	 * 
	 * @return
	 */
	@GetMapping("/loginForm")
	public String loginForm(/* @PathVariable String prj_domain */) {
		return "member/member_login";
	}

	/**
	 * 도메인 중복 여부 체크 ajax
	 * 
	 * @param prj_domain
	 * @return
	 */

	@ResponseBody
	@PostMapping("/domainCheck")
	public int domainCheck(String prj_domain) {

		int result = pjservice.domainCheck(prj_domain);

		log.debug("도메인 : {}", prj_domain);

		log.debug("결과 : {}", result);

		return result;
	}

	/**
	 * 멤버가 아닌 프로젝트에 접근한 경우
	 * 
	 * @return
	 */
	@GetMapping("/wrongPath")
	public String wrongPath() {

		return "wrongPath";
	}

	/**
	 * 
	 * @param prj_domain
	 * @return
	 */
	@GetMapping("/{prj_domain}/main")
	public String enterPj(@PathVariable String prj_domain, 
			Model model,
			HttpSession session,
			@AuthenticationPrincipal UserDetails user) {
		log.debug("HomeController [enterPj] Start");
		log.debug("prj_domain : {}", prj_domain);
		
		session.setAttribute("prj_domain", prj_domain);

		Projects project = pjservice.searchOne(prj_domain);

		model.addAttribute("project", project);
		
		//* 신승훈 메인창 목록 (최신 공지사항, 내 일정, 할당 태스크 현황, 담당 태스크 리스트)
		List<Notice> MainNoticelist = pjservice.selectMainNotice(prj_domain);
		log.debug("HomeController [enterPj] MainNoticelist : {}", MainNoticelist);
		model.addAttribute("MainNoticelist", MainNoticelist);
		
		
		List<Plans> MainPlanlist = pjservice.selectMainPlan(user.getUsername());
		log.debug("HomeController [enterPj] MainPlanlist : {}", MainPlanlist);
		model.addAttribute("MainPlanlist", MainPlanlist);

		List<Task> MainMysenderlist = pjservice.selectMainMysender(prj_domain, user.getUsername()); 
		log.debug("HomeController [enterPj] MainMysenderlist : {}", MainMysenderlist); 
		model.addAttribute("MainMysenderlist", MainMysenderlist);
		
		List<Task> MainMyStafflist = pjservice.selectMainMyStaff(prj_domain, user.getUsername()); 
		log.debug("HomeController [enterPj] MainMyStafflist : {}", MainMyStafflist); 
		model.addAttribute("MainMyStafflist", MainMyStafflist);		
		
		log.debug("HomeController [enterPj] End");
		return "project/project_main";
	}
}
