package net.softsociety.issho.task.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
	
	//업무 테이블 attributes
	private int task_seq;
	private String prj_domain;
	private String task_name;
	private String task_content;
	private String task_sender;		//업무할당자
	private String task_rank;		//업무 중요도 0 : 낮음, 1 : 보통, 2 : 높음
	private String task_state;		//업무 상태 0 : 진행전, 1: 진행중, 3: 진행완료, 4 : 보류
	private String task_inputdate;
	private String task_startdate;
	private String task_enddate;
	private String exp_startdate;
	private String exp_enddate;
	private int ptask_num;
	
	//* 신승훈 조회, 즐겨찾기 관련 attributes
	private String memb_name;
	private String bookmark_memb_mail;
	private List<Taskstaff> taskStaffList;
	//평균 수행도 attributes
	private int performAvg;
		
	//담당자 attributes
	private String memb_mail;
	private int tsuper_perform;
	
	//파일 관련 attributes
	private int tfile_seq;
	private String tfile_ogfile;
	private String tfile_savefile;

	//김지윤 관리자 프로젝트 태스크 참가자
	private String taskMembList;

	
}
