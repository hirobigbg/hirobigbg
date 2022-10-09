package net.softsociety.issho.plans.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.issho.member.domain.Members;
import net.softsociety.issho.plans.dao.PlansDAO;
import net.softsociety.issho.plans.domain.Attendant;
import net.softsociety.issho.plans.domain.Plans;

@Transactional
@Service
@Slf4j
public class PlansServiceImpl implements PlansService{

	@Autowired
	private PlansDAO plansDAO;
	
	// 신승훈 * 일정 서비스 홈 화면 ( 저장된 일정 불러오기 ) 
	@Override
	public ArrayList<Plans> selectPlansByMemMail(String username) {
		log.debug("PlansServiceImpl [selectPlansByMemMail] Start");
		
		ArrayList<Plans> result = plansDAO.selectPlansByMemMail(username);
		log.debug("PlansServiceImpl [selectPlansByMemMail] result : {}", result);
		
		log.debug("PlansServiceImpl [selectPlansByMemMail] End");
		return result;
	}
	
	// 신승훈 * 일정 저장
	@Override
	public int savePlan(Plans plans, String memberMail, List<String> attendees) {
		log.debug("PlansServiceImpl [savePlan] Start");
		
		int result = plansDAO.savePlan(plans);
		
		if(result != 1)
			return 0;
		
		result = plansDAO.insertAttendant(new Attendant(plans.getPlan_seq(), memberMail, 1));
		if(result != 1)
			return 0;
		
		for(String attendeeMail : attendees) {
			plansDAO.insertAttendant(new Attendant(plans.getPlan_seq(), attendeeMail, 0));
		}
		
		log.debug("PlansServiceImpl [savePlan] End");
		return result;
	}
	
	// 신승훈 * 일정 수정 (현재 일정의 참석자 전부 셀렉)
	@Override
	public List<Attendant> selectattendants(int plan_seq) {
		log.debug("PlansServiceImpl [selectattendants] Start");
		
		List<Attendant> result = plansDAO.selectattendants(plan_seq);
		log.debug("PlansServiceImpl [selectattendants] result : {}", result);
		
		log.debug("PlansServiceImpl [selectattendants] End");
		return result;
	}
	
	// 신승훈 * 일정 수정
	@Override
	public int updatePlan(Plans plans, List<String> deleteMembList, List<String> insertMembList) {
		log.debug("PlansServiceImpl [updatePlan] Start");
		
		int result = plansDAO.updatePlan(plans);
		
		if(result != 1)
			return 0;
		
		// 일정 수정시 삭제할 멤버 리스트
		log.debug("PlansServiceImpl [updatePlan] deleteMembList : {}", deleteMembList);
		for(String deleteMember : deleteMembList) {
			plansDAO.deleteAttendant(new Attendant(plans.getPlan_seq(), deleteMember, 0));
		}
		
		// 일정 수정시 추가할 멤버 리스트
		log.debug("PlansServiceImpl [updatePlan] insertAttendant : {}", insertMembList);
		for(String insertMember : insertMembList) {
			plansDAO.insertAttendant(new Attendant(plans.getPlan_seq(), insertMember, 0));
		}
		
		log.debug("PlansServiceImpl [updatePlan] End");
		return result;
	}

	// 신승훈 * 일정 삭제 
	@Override
	public int deletePlan(Plans plans) {
		log.debug("PlansServiceImpl [deletePlan] Start");
		
		int result = plansDAO.deletePlan(plans);
		
		log.debug("PlansServiceImpl [deletePlan] End");
		return result;
	}

	// 신승훈 * 일정 참석 여부 설정(ajax)
	@Override
	public void ifYouWillAttend(String plan_seq, String username, String att_whether) {
		log.debug("PlansServiceImpl [ifYouWillAttend] Start");
		
		if(att_whether.equals("true")) 
			att_whether = "1";
		else 
			att_whether = "0";
		
		Attendant attendant = new Attendant();
		
		attendant.setPlan_seq(Integer.parseInt(plan_seq));
		attendant.setMemb_mail(username);
		attendant.setAtt_whether(Integer.parseInt(att_whether));
		
		log.debug("PlansServiceImpl [ifYouWillAttend] attendant : {}", attendant);
		plansDAO.ifYouWillAttend(attendant);
		
		log.debug("PlansServiceImpl [ifYouWillAttend] End");
	}

}
