package net.softsociety.issho.plans.service;

import java.util.ArrayList;
import java.util.List;

import net.softsociety.issho.plans.domain.Attendant;
import net.softsociety.issho.plans.domain.Plans;

public interface PlansService {
	
	// 신승훈 * 일정 서비스 홈 화면 ( 저장된 일정 불러오기 ) 
	public ArrayList<Plans> selectPlansByMemMail(String username);
	// 신승훈 * 일정 저장
	public int savePlan(Plans plans, String memberMail, List<String> attendees);
	// 신승훈 * 일정 수정 (현재 일정의 참석자 전부 셀렉)
	public List<Attendant> selectattendants(int plan_seq);
	// 신승훈 * 일정 수정 
	public int updatePlan(Plans plans, List<String> delete_att, List<String> insert_att);
	// 신승훈 * 일정 삭제 
	public int deletePlan(Plans plans);
	// 신승훈 * 일정 참석 여부 설정(ajax)
	public void ifYouWillAttend(String plan_seq, String username, String att_whether);
	


}
