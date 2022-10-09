package net.softsociety.issho.plans.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import net.softsociety.issho.member.domain.Members;
import net.softsociety.issho.plans.domain.Attendant;
import net.softsociety.issho.plans.domain.Plans;
import net.softsociety.issho.project.domain.ProjectMember;
import net.softsociety.issho.project.domain.Projects;

@Mapper
public interface PlansDAO {
	// 신승훈 * 일정 서비스 홈 화면 ( 저장된 일정 불러오기 ) 
	public ArrayList<Plans> selectPlansByMemMail(String memberMail);
	// 신승훈 * 일정 저장
	public int savePlan(Plans plans);
	// 신승훈 * 일정 수정 (현재 일정의 참석자 전부 셀렉)
	public List<Attendant> selectattendants(int plan_seq);
	// 신승훈 * 일정 수정 
	public int updatePlan(Plans plans);
	// 신승훈 * 일정 삭제 
	public int deletePlan(Plans plans);
	// 신승훈 * 수정시 삭제할 참석자
	public void deleteAttendant(Attendant attendant);
	// 신승훈 * 수정시 추가할 참석자
	public int insertAttendant(Attendant attendant);
	// 신승훈 * 일정 참석 여부 설정(ajax)
	public void ifYouWillAttend(Attendant attendant);
	//* 신승훈 메인창 목록 (내 일정)
	public List<Plans> selectMainPlan(String userid);
	
}
