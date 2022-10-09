package net.softsociety.issho.task.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.issho.member.dao.MemberDAO;
import net.softsociety.issho.member.domain.Members;
import net.softsociety.issho.member.service.MemberService;
import net.softsociety.issho.sse.SSEService;
import net.softsociety.issho.task.dao.TaskDAO;
import net.softsociety.issho.task.domain.Bookmark;
import net.softsociety.issho.task.domain.Mention;
import net.softsociety.issho.task.domain.Task;
import net.softsociety.issho.task.domain.TaskReply;
import net.softsociety.issho.task.domain.Taskfile;
import net.softsociety.issho.task.domain.Taskstaff;
import net.softsociety.issho.task.service.TaskService;
import net.softsociety.issho.util.FileService;
import net.softsociety.issho.util.PageNavigator;

@Controller
@Slf4j
@RequestMapping("**/task")
public class TaskController {

	// 신승훈 * 게시판 목록의 페이지당 글 수
	@Value("${user.task.page}")
	int countPerPage;

	// 신승훈 * 게시판 목록의 페이지 이동 링크 수
	@Value("${user.task.group}")
	int pagePerGroup;

	// 신승훈 * 테스크 첨부파일 업로드 경로
	@Value("${spring.servlet.multipart.location}")
	String uploadPath;

	@Autowired
	MemberService memservice;

	@Autowired
	TaskService taskservice;

	@Autowired
	MemberDAO memDao;

	@Autowired
	TaskDAO taskDao;

	@Autowired
	SSEService sseService;
	
	/**
	 * 태스크 리스트 이동
	 * 
	 * @param user
	 * @param model
	 * @param domain
	 * @return
	 * 
	 * @author 윤영혜, 신승훈
	 */

	@GetMapping("/taskList")
	public String taskList(HttpServletRequest request, @AuthenticationPrincipal UserDetails user,
			@RequestParam(name = "page", defaultValue = "1") int page, String searchWord, Model model, String domain, String isMentioned) {
		log.debug("TaskController [taskList] Start");
		

		String id = user.getUsername();

		// 신승훈 * prj_domain 설정
		String calledValue = request.getServletPath();
		String[] splitedUrl = calledValue.split("/");
		String prj_domain = splitedUrl[1];

		// 윤영혜 : 회원 정보 조회
		Members member = memDao.getUserById(id);
		ArrayList<Members> pjMemList = memservice.searchPjMem(prj_domain);

		model.addAttribute("member", member);
		model.addAttribute("list", pjMemList);

		Map<String, String> map = new HashMap<>();
		map.put("prj_domain", prj_domain);
		if (searchWord != null)
			map.put("searchWord", searchWord);

		// 신승훈 * 태스크 네비
		int totalCount = taskservice.countAllBoard(map);
		PageNavigator navi = new PageNavigator(pagePerGroup, countPerPage, page, totalCount);
		log.debug("navi : {}", navi);
		model.addAttribute("navi", navi);

		// 신승훈 * 메인 화면 첫 진입시 테스크 전체 검색
		List<Task> tasklist = taskservice.SelectAlltask(navi, prj_domain, searchWord);
		log.debug("TaskController [taskList] tasklist : {}", tasklist);

		List<Taskstaff> pjmb = taskservice.projectMembers(prj_domain);
		log.debug("TaskController [taskList] pjmb : {}", pjmb);

		model.addAttribute("tasklist", tasklist);
		model.addAttribute("page", page);
		model.addAttribute("pjmb", pjmb);

		// 윤영혜
		model.addAttribute("isMentioned", isMentioned);

		log.debug("TaskController [taskList] End");

		return "taskView/task_List";
	}

	@PostMapping("/newProject")
	public void newProject(@AuthenticationPrincipal UserDetails user, Task task, String staffs) {

		String id = user.getUsername();

		task.setTask_sender(id);

		return;
//		return "taskView/task_List";

	}

	// 신승훈 * 테스크 상세보기
	@ResponseBody
	@PostMapping("/showTaskModal")
	public Map<String, Object> showTaskModal(String taskSeq) {
		log.debug("TaskController [showTaskModal] Start");
		
		log.debug("taskSeq : {}", taskSeq);

		Task showTask = taskservice.selectTaskByTaskSeq(Integer.parseInt(taskSeq));
		log.debug("TaskController [showTaskModal] showTask : {}", showTask);

		ArrayList<Taskstaff> showTaskstaff = taskservice.selectStaff(Integer.parseInt(taskSeq));
		log.debug("TaskController [showTaskModal] showTaskstaff : {}", showTaskstaff);

		// 신승훈 * 상세보기 첨부파일 확인
		List<Taskfile> taskFileList = taskservice.selectTaskFile(taskSeq);
		log.debug("TaskController [showTaskModal] taskFileList : {}", taskFileList);

		List<TaskReply> replyList = taskDao.replyList();
		log.debug("TaskController [showTaskModal] replyList: {}", replyList);

		Map<String, Object> result = new HashMap<>();
		result.put("showTask", showTask);
		result.put("showStaff", showTaskstaff);
		result.put("taskFileList", taskFileList);
		result.put("replyList", replyList);

		log.debug("TaskController [showTaskModal] End");

		return result;
	}

	// 신승훈 * 테스크 조건 조회
	@ResponseBody
	@PostMapping("/selectTask")
	public Map<String, Object> selectTask(HttpServletRequest request, @RequestParam Map<String, String> param,
			@AuthenticationPrincipal UserDetails user) {

		log.debug("TaskController [selectTask] Start");
		log.debug("TaskController [selectTask] param : {}", param);

		String calledValue = request.getServletPath();
		String[] splitedUrl = calledValue.split("/");
		String prj_domain = splitedUrl[1];

		String searchWord = "";
		if (!param.get("searchWord").equals("")) {
			searchWord = param.get("searchWord");
			log.debug("searchWord가 있음! : {}", searchWord);
		}

		int page = Integer.parseInt(param.get("page"));
		log.debug("page : {}", page);

		Map<String, String> orderList = new HashMap<>();
		orderList.put("prj_domain", prj_domain);
		// Map을 사용하여 task_sender=task_sender, task_staff=task_staff 가있으면 아이디로 값 집어넣음
		if (param.containsKey("task_sender")) {
			orderList.put("task_sender", user.getUsername());
		}
		if (param.containsKey("task_staff")) {
			orderList.put("task_staff", user.getUsername());
		}
		if (param.containsKey("search_type")) {
			if (param.get("search_type").equals("bookmark"))
				orderList.put("bookmark", user.getUsername());
			else
				orderList.put(param.get("search_type"), param.get("search_type"));
		}
		if (!searchWord.equals(""))
			orderList.put("searchWord", searchWord);
		orderList.put("prj_domain", prj_domain);
		log.debug("TaskController [selectTask] orderList : {}", orderList);

		// 신승훈 * 네비
		int totalCount = taskservice.countAllBoard(orderList);
		PageNavigator navi = new PageNavigator(pagePerGroup, countPerPage, page, totalCount);
		log.debug("navi : {}", navi);

		List<Task> taskList = taskservice.selectTaskAll(navi, orderList);
		log.debug("TaskController [selectTask] taskList : {}", taskList);
		Map<String, Object> resultObj = new HashMap<>();
		resultObj.put("taskList", taskList);
		resultObj.put("page", page);
		resultObj.put("navi", navi);
		log.debug("TaskController [selectTask] End");

		return resultObj;
	}

	// 신승훈 * 즐겨찾기
	@ResponseBody
	@PostMapping("/bookmark")
	public boolean bookmark(@RequestParam String taskSeq, @AuthenticationPrincipal UserDetails user) {
		log.debug("TaskController [bookmark] Start");
		log.debug("TaskController [bookmark] taskSeq : {}", taskSeq);

		Bookmark bookmark = new Bookmark();
		bookmark.setTask_seq(Integer.parseInt(taskSeq));
		bookmark.setMemb_mail(user.getUsername());

		try {
			// 신승훈 * 즐겨찾기 추가
			taskservice.insertBookmark(bookmark);
			log.debug("TaskController [bookmark] End");
			return true;
		} catch (Exception e) {
			log.debug(e.getMessage());
			// 신승훈 * 즐겨찾기 삭제
			taskservice.deleteBookmark(bookmark);
			log.debug("TaskController [bookmark] End");
			return false;
		}
	}

	// 신승훈 * 수행도 변경 ajax
	@ResponseBody
	@PostMapping("/performChange")
	public Taskstaff performChange(@ModelAttribute Taskstaff taskStaff, @AuthenticationPrincipal UserDetails user) {
		log.debug("TaskController [performChange] Start");
		log.debug("TaskController [performChange] taskStaff : {}", taskStaff);

		taskStaff.setMemb_mail(user.getUsername());

		taskStaff = taskservice.updatePerform(taskStaff);

		log.debug("TaskController [performChange] End");
		return taskStaff;
	}

	// 신승훈 * 진행상태 변경 ajax
	@ResponseBody
	@PostMapping("/stateChange")
	public Task stateChange(@RequestParam int task_seq, @AuthenticationPrincipal UserDetails user) throws Exception {
		log.debug("TaskController [stateChange] Start");

		Task task = taskservice.stateChange(task_seq, user.getUsername());
		if (task == null)
			throw new Exception("TaskController [stateChange] 에러");

		log.debug("TaskController [stateChange] End");
		return task;
	}
	
	/**
	 * 새로운 프로젝트 추가
	 * 
	 * @param user
	 * @param task
	 * @param staffs
	 *
	 * @author 윤영혜
	 */

	@PostMapping("/addNewTask")
	public String newProject(HttpServletRequest request, @AuthenticationPrincipal UserDetails user, Task task,
			String memList2, @RequestParam List<MultipartFile> uploads) {

		String calledValue = request.getServletPath();
		String[] splitedUrl = calledValue.split("/");
		String prj_domain = splitedUrl[1];

		task.setPrj_domain(prj_domain);

		// 담당자 받을 배열s
		String[] staffList = null;

		log.debug("task 객체 : {}", task);
		log.debug("staffs : {}", memList2);
		log.debug("uploads : {}", uploads);

		// 할당자 아이디 받아오기
		String id = user.getUsername();

		// sender를 할당자 아이디로
		task.setTask_sender(id);

		// 진행 중일 때 : 예상 시작일을 시작일로 바꿔주자
		if ((task.getTask_state()).equals("1")) {
			task.setTask_startdate(task.getExp_startdate());
			task.setExp_startdate("");
		} else if ((task.getTask_state()).equals("3")) {
			// 진행 완료일 때 : 예상 종료일을 종료일로 바꿔주자
			task.setTask_enddate(task.getExp_enddate());
			task.setExp_enddate("");
		}

		// 담당자가 없다면 할당자를 담당자로 설정해주고, 담당자가 있는 경우 split하여 담는다.
		if (memList2.length() == 0 || memList2.equals("")) {

			log.debug("memList2.length == 0");

			memList2 = id;

			log.debug("staffs : {}", memList2);

		} else {
			staffList = memList2.split(",");

			log.debug("staffs : {}", memList2);
		}

		// 태스크 추가
		taskservice.addNewTask(task);
		log.debug("저장후 task:{}", task);

		// 해당 태스크에 대한 담당자 추가
		if (staffList == null) {
			Taskstaff staff = new Taskstaff(task.getTask_seq(), memList2, 0);
			taskservice.addStaffs(staff);
		} else {
			for (int i = 0; i < staffList.length; i++) {
				Taskstaff staff = new Taskstaff(task.getTask_seq(), staffList[i], 0);
				log.debug("staff 객체 : {} ", staff);
				taskservice.addStaffs(staff);
			}
		}

		log.debug("uploads : {}", uploads.size());

		// 파일 관련 처리
		if (uploads != null && !uploads.isEmpty()) {
			log.debug("파일 처리 진입 완료");
			for (int i = 0; i < uploads.size(); i++) {
				log.debug("upload 파일 : {}", uploads.get(i));
				if (uploads.get(i) != null && !uploads.get(i).isEmpty()) {
					log.debug("{}", uploads.get(i).getContentType());
					String savedfile = FileService.saveFile(uploads.get(i), uploadPath);
					Taskfile taskfile = new Taskfile(task.getTask_seq(), uploads.get(i).getOriginalFilename(),
							savedfile);

					log.debug("taskfile : {}", taskfile);
					taskservice.addFiles(taskfile);
				}
			}
		}
		log.debug("task 객체 처리 후 : {}", task);
		return "redirect:/" + prj_domain + "/task/taskList";
	}

	// * 신승훈 첨부파일 다운로드
	@GetMapping(value = "/download")
	public String getDownload(@RequestParam("fileSeq") String tfileSeq, HttpServletResponse response) {

		log.debug("TaskController [getDownload] Start");
		// * 신승훈 첨부파일 다운로드
		Taskfile file = taskservice.selectTaskFileByTfileSeq(tfileSeq);

		// 원래의 파일명
		String originalfile = new String(file.getTfile_ogfile());
		try {
			response.setHeader("Content-Disposition",
					" attachment;filename=" + URLEncoder.encode(originalfile, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 저장된 파일 경로
		String fullPath = uploadPath + "/" + file.getTfile_savefile();

		log.debug("fullPath: " + fullPath);

		// 서버의 파일을 읽을 입력 스트림과 클라이언트에게 전달할 출력스트림
		FileInputStream filein = null;
		ServletOutputStream fileout = null;

		try {
			filein = new FileInputStream(fullPath);
			fileout = response.getOutputStream();

			// Spring의 파일 관련 유틸 이용하여 출력
			FileCopyUtils.copy(filein, fileout);

			filein.close();
			fileout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.debug("TaskController [getDownload] End");
		return null;
	}

	// 댓글 작성
	@PostMapping("/insertComment")
	@ResponseBody
	public void insertComment(@AuthenticationPrincipal UserDetails user, TaskReply reply, String mentionTo) {

		String id = user.getUsername();

		reply.setTreply_writer(id);

		log.debug("댓글 객체 : {}", reply);
		log.debug("mentionTo : {}", mentionTo);

		taskDao.insertComment(reply);

		log.debug("insert 후 댓글 객체 : {}", reply);

		if (mentionTo.length() != 0 || !mentionTo.equals("")) {
			Mention mention = new Mention(0, reply.getTreply_seq(), mentionTo);

			taskDao.insertMention(mention);

			log.debug("mention 객체 : {}", mention);
			
			Members member = memDao.getUserById(mentionTo);
			
			log.debug("member : {}", member);
			
					
			 sseService.send(mentionTo, member.getMemb_name() + "mentioned you : " + reply.getTreply_content() , Integer.toString(reply.getTask_seq()));
		}
	}
}
