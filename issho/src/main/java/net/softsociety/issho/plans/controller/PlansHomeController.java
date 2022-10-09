package net.softsociety.issho.plans.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.issho.member.domain.Members;
import net.softsociety.issho.member.service.MemberService;
import net.softsociety.issho.plans.domain.Attendant;
import net.softsociety.issho.plans.domain.Plans;
import net.softsociety.issho.plans.service.PlansService;
import net.softsociety.issho.util.ArrayOverlapDelete;



@Slf4j
@RequestMapping("**/plans")
@Controller
public class PlansHomeController {
	
	@Autowired
	PlansService plansService;
	
	@Autowired
	MemberService memberService;
	
	// 신승훈 * 일정 서비스 홈 화면 ( 저장된 일정 불러오기, 도메인 참석자(멤버리스트) 불러오기 )
	@GetMapping("plan")
	public String plan(HttpServletRequest request, Plans plans, Model model, @AuthenticationPrincipal UserDetails user) {
		log.debug("Controller [plan] Start");
		
		// 신승훈 * prj_domain 설정
		String calledValue = request.getServletPath();
		String[] splitedUrl = calledValue.split("/");
		String prj_domain = splitedUrl[1];
		
		// 신승훈 * 일정 서비스 홈 화면 ( 저장된 일정 불러오기 )
		ArrayList<Plans> attendantList = plansService.selectPlansByMemMail(user.getUsername());
		log.debug("Controller [plan] attendantList : {} ", attendantList);
		model.addAttribute("attendantlist", attendantList);
		
		// 신승훈 * 일정 서비스 홈 화면 ( 도메인 참석자(멤버리스트) 불러오기 )
		ArrayList<Members> memberlist = memberService.searchPjMem(prj_domain);
		log.debug("Controller [plan] attendees : {} ", memberlist);
		model.addAttribute("attendees", memberlist);
		
		log.debug("Controller [plan] End");
		return "/plansView/plans_home";
	}
	
	
	// 신승훈 * 일정 저장
	@PostMapping("savePlan")
	public String savePlan(HttpServletRequest request,
			@ModelAttribute Plans plans, @RequestParam(name = "attendees", required = false) List<String> attendees
			, @AuthenticationPrincipal UserDetails user
			) {
		log.debug("Controller [savePlan] Start");
		
		String calledValue = request.getServletPath();
		String[] splitedUrl = calledValue.split("/");
		String prj_domain = splitedUrl[1];
		
		
		if(attendees == null)
			attendees = new ArrayList<String>();
		log.debug("Controller [savePlan] param : {}", attendees);
		
		// datetime-local의 T제거
		plans.setPlan_startDate(plans.getPlan_startDate().replace("T", " "));
		plans.setPlan_endDate(plans.getPlan_endDate().replace("T", " "));
		plans.setMemb_mail(user.getUsername());
		
		log.debug("Controller [savePlan] plans : {}", plans);
		
		plansService.savePlan(plans, user.getUsername(), attendees);
		
		log.debug("Controller [savePlan] End");
		return "redirect:./plans/plan";
	}
	
	// 신승훈 * 일정 수정
	@PostMapping("updatePlan")
	public String updatePlan(
			@ModelAttribute Plans plans, @RequestParam(name = "attendees", required = false) List<String> attendees
			, @AuthenticationPrincipal UserDetails user
			) {
		log.debug("Controller [updatePlan] Start");
		log.debug("Controller [updatePlan] param attendees: {}", attendees);
		log.debug("Controller [updatePlan] param plans: {}", plans);
		
		// datetime-local의 T제거
		plans.setPlan_startDate(plans.getPlan_startDate().replace("T", " "));
		plans.setPlan_endDate(plans.getPlan_endDate().replace("T", " "));
		plans.setMemb_mail(user.getUsername());
		log.debug("Controller [updatePlan] plans : {}", plans);
		
		if(attendees == null)
			attendees = new ArrayList();

		// 현재 일정의 참석자 전부 셀렉
		List<Attendant> old_att = plansService.selectattendants(plans.getPlan_seq());
		log.debug("Controller [updatePlan] old_att : {}", old_att);
	
		// 업데이트로 받은 참석자 리스트랑 비교	
		List<String> old_attStr = new ArrayList();
		
		for(int i=old_att.size()-1; i>=0; i--) {
			if(old_att.get(i).getMemb_mail().equals(user.getUsername()))
				continue;
			old_attStr.add(old_att.get(i).getMemb_mail());
		}
		
		// ArrayOverlapDelete = target 리스트에서 source 리스트에 있는거 지워서 리턴
		log.debug(" Controller [updatePlan] 삭제할 참석자 : {}", ArrayOverlapDelete.arrayDelete(old_attStr, attendees));
		log.debug(" Controller [updatePlan] 추가할 참석자 : {}", ArrayOverlapDelete.arrayDelete(attendees, old_attStr));
		
		// delete_att = 삭제할 참석자, insert_att 추가할 참석자
		// ArrayOverlapDelete = target 리스트에서 source 리스트에 있는거 지워서 리턴
		List<String> delete_att = ArrayOverlapDelete.arrayDelete(old_attStr, attendees);
		List<String> insert_att = ArrayOverlapDelete.arrayDelete(attendees, old_attStr);
		
		plansService.updatePlan(plans, delete_att, insert_att);
		
		log.debug("Controller [updatePlan] End");
		return "redirect:./plans/plan";
	}
	
	// 신승훈 * 일정삭제
	@PostMapping("deletePlan")
	public String deletePlan(@ModelAttribute Plans plans
			, @AuthenticationPrincipal UserDetails user) {
		log.debug("Controller [deletePlan] Start");
		
		plansService.deletePlan(plans);
		
		log.debug("Controller [deletePlan] End");
		return "redirect:./plans/plan";
	}
	
	// 신승훈 * 일정 참석 여부 설정(ajax)
	@ResponseBody
	@PostMapping("/ifYouWillAttend")
	public void ifYouWillAttend(String plan_seq, String att_whether, @AuthenticationPrincipal UserDetails user) {
		log.debug("Controller [ifYouWillAttend] Start");
		log.debug("Controller [ifYouWillAttend] ifYouWillAttend : {}", att_whether);
		log.debug("Controller [ifYouWillAttend] plan_seq : {}", plan_seq);
		log.debug("Controller [ifYouWillAttend] user : {}", user.getUsername());
		
		plansService.ifYouWillAttend(plan_seq, user.getUsername(), att_whether);
		
		log.debug("Controller [ifYouWillAttend] End");
	}
	
	@ResponseBody
	@PostMapping("/planRenewal")
	public ArrayList<Plans> planRenewal(@AuthenticationPrincipal UserDetails user){
		log.debug("Controller [planRenewal] Start");
		
		ArrayList<Plans> attendantList = plansService.selectPlansByMemMail(user.getUsername());
		log.debug("Controller [planRenewal] attendantList : {} ", attendantList);
		
		log.debug("Controller [planRenewal] End");
		return attendantList;
	}
}

