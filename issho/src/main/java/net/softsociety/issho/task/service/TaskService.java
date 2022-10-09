package net.softsociety.issho.task.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.softsociety.issho.task.domain.Bookmark;
import net.softsociety.issho.task.domain.GanttTask;
import net.softsociety.issho.task.domain.Task;
import net.softsociety.issho.task.domain.Taskfile;
import net.softsociety.issho.task.domain.Taskstaff;
import net.softsociety.issho.util.PageNavigator;


public interface TaskService {
	
	// 신승훈 * 태스크 전체 검색
	public ArrayList<Task> SelectAlltask(PageNavigator navi, String domain, String searchWord);
	// 신승훈 * 태스크 상세보기
	public Task selectTaskByTaskSeq(int taskSeq);
	// 신승훈 * 태스크 조건 조회
	public List<Task> selectTaskAll(PageNavigator navi, Map<String, String> orderList);
	// 신승훈 * 태스크 네비
	public int countAllBoard(Map<String, String> map);
	// 신승훈 * 즐겨찾기 추가
	public void insertBookmark(Bookmark bookmark) throws Exception;
	// 신승훈 * 즐겨찾기 삭제
	public void deleteBookmark(Bookmark bookmark);
	// 신승훈 * 테스크 스태프 리스트 조회
	public ArrayList<Taskstaff> selectStaff(int parseInt);
	// 신승훈 * 수행도 변경 ajax
	public Taskstaff updatePerform(Taskstaff taskStaff);
	// 신승훈 * 진행상태 변경 ajax
	public Task stateChange(int task_seq, String usrid);
	// 신승훈 * 상세보기 첨부파일 확인
	public List<Taskfile> selectTaskFile(String taskSeq);
	
	public Taskfile selectTaskFileByTfileSeq(String fileSeq);


	//새로운 태스크 추가
	public void addNewTask(Task task);

	//새로운 태스크에 대한 staff 추가
	public void addStaffs(Taskstaff staff);

	//새로운 태스크에 대한 file 추가
	public void addFiles(Taskfile taskfile);

	public List<Task> myAllocate(Map<String, String> map);

	public List<Task> myCharged(Map<String, String> map);

	public void changeState(Map<String, String> map);

	public List<Taskstaff> projectMembers(String prj_domain);

	public ArrayList<Task> SelectAlltaskMG(String prj_domain, PageNavigator navi, String searchWord);

	public void changeDate(GanttTask task);
	
	public List<Task> SelectAlltask1(String prj_domain); 
	




}
