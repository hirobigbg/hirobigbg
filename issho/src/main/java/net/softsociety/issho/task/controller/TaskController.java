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

	// ????????? * ????????? ????????? ???????????? ??? ???
	@Value("${user.task.page}")
	int countPerPage;

	// ????????? * ????????? ????????? ????????? ?????? ?????? ???
	@Value("${user.task.group}")
	int pagePerGroup;

	// ????????? * ????????? ???????????? ????????? ??????
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
	 * ????????? ????????? ??????
	 * 
	 * @param user
	 * @param model
	 * @param domain
	 * @return
	 * 
	 * @author ?????????, ?????????
	 */

	@GetMapping("/taskList")
	public String taskList(HttpServletRequest request, @AuthenticationPrincipal UserDetails user,
			@RequestParam(name = "page", defaultValue = "1") int page, String searchWord, Model model, String domain, String isMentioned) {
		log.debug("TaskController [taskList] Start");
		

		String id = user.getUsername();

		// ????????? * prj_domain ??????
		String calledValue = request.getServletPath();
		String[] splitedUrl = calledValue.split("/");
		String prj_domain = splitedUrl[1];

		// ????????? : ?????? ?????? ??????
		Members member = memDao.getUserById(id);
		ArrayList<Members> pjMemList = memservice.searchPjMem(prj_domain);

		model.addAttribute("member", member);
		model.addAttribute("list", pjMemList);

		Map<String, String> map = new HashMap<>();
		map.put("prj_domain", prj_domain);
		if (searchWord != null)
			map.put("searchWord", searchWord);

		// ????????? * ????????? ??????
		int totalCount = taskservice.countAllBoard(map);
		PageNavigator navi = new PageNavigator(pagePerGroup, countPerPage, page, totalCount);
		log.debug("navi : {}", navi);
		model.addAttribute("navi", navi);

		// ????????? * ?????? ?????? ??? ????????? ????????? ?????? ??????
		List<Task> tasklist = taskservice.SelectAlltask(navi, prj_domain, searchWord);
		log.debug("TaskController [taskList] tasklist : {}", tasklist);

		List<Taskstaff> pjmb = taskservice.projectMembers(prj_domain);
		log.debug("TaskController [taskList] pjmb : {}", pjmb);

		model.addAttribute("tasklist", tasklist);
		model.addAttribute("page", page);
		model.addAttribute("pjmb", pjmb);

		// ?????????
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

	// ????????? * ????????? ????????????
	@ResponseBody
	@PostMapping("/showTaskModal")
	public Map<String, Object> showTaskModal(String taskSeq) {
		log.debug("TaskController [showTaskModal] Start");
		
		log.debug("taskSeq : {}", taskSeq);

		Task showTask = taskservice.selectTaskByTaskSeq(Integer.parseInt(taskSeq));
		log.debug("TaskController [showTaskModal] showTask : {}", showTask);

		ArrayList<Taskstaff> showTaskstaff = taskservice.selectStaff(Integer.parseInt(taskSeq));
		log.debug("TaskController [showTaskModal] showTaskstaff : {}", showTaskstaff);

		// ????????? * ???????????? ???????????? ??????
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

	// ????????? * ????????? ?????? ??????
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
			log.debug("searchWord??? ??????! : {}", searchWord);
		}

		int page = Integer.parseInt(param.get("page"));
		log.debug("page : {}", page);

		Map<String, String> orderList = new HashMap<>();
		orderList.put("prj_domain", prj_domain);
		// Map??? ???????????? task_sender=task_sender, task_staff=task_staff ???????????? ???????????? ??? ????????????
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

		// ????????? * ??????
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

	// ????????? * ????????????
	@ResponseBody
	@PostMapping("/bookmark")
	public boolean bookmark(@RequestParam String taskSeq, @AuthenticationPrincipal UserDetails user) {
		log.debug("TaskController [bookmark] Start");
		log.debug("TaskController [bookmark] taskSeq : {}", taskSeq);

		Bookmark bookmark = new Bookmark();
		bookmark.setTask_seq(Integer.parseInt(taskSeq));
		bookmark.setMemb_mail(user.getUsername());

		try {
			// ????????? * ???????????? ??????
			taskservice.insertBookmark(bookmark);
			log.debug("TaskController [bookmark] End");
			return true;
		} catch (Exception e) {
			log.debug(e.getMessage());
			// ????????? * ???????????? ??????
			taskservice.deleteBookmark(bookmark);
			log.debug("TaskController [bookmark] End");
			return false;
		}
	}

	// ????????? * ????????? ?????? ajax
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

	// ????????? * ???????????? ?????? ajax
	@ResponseBody
	@PostMapping("/stateChange")
	public Task stateChange(@RequestParam int task_seq, @AuthenticationPrincipal UserDetails user) throws Exception {
		log.debug("TaskController [stateChange] Start");

		Task task = taskservice.stateChange(task_seq, user.getUsername());
		if (task == null)
			throw new Exception("TaskController [stateChange] ??????");

		log.debug("TaskController [stateChange] End");
		return task;
	}
	
	/**
	 * ????????? ???????????? ??????
	 * 
	 * @param user
	 * @param task
	 * @param staffs
	 *
	 * @author ?????????
	 */

	@PostMapping("/addNewTask")
	public String newProject(HttpServletRequest request, @AuthenticationPrincipal UserDetails user, Task task,
			String memList2, @RequestParam List<MultipartFile> uploads) {

		String calledValue = request.getServletPath();
		String[] splitedUrl = calledValue.split("/");
		String prj_domain = splitedUrl[1];

		task.setPrj_domain(prj_domain);

		// ????????? ?????? ??????s
		String[] staffList = null;

		log.debug("task ?????? : {}", task);
		log.debug("staffs : {}", memList2);
		log.debug("uploads : {}", uploads);

		// ????????? ????????? ????????????
		String id = user.getUsername();

		// sender??? ????????? ????????????
		task.setTask_sender(id);

		// ?????? ?????? ??? : ?????? ???????????? ???????????? ????????????
		if ((task.getTask_state()).equals("1")) {
			task.setTask_startdate(task.getExp_startdate());
			task.setExp_startdate("");
		} else if ((task.getTask_state()).equals("3")) {
			// ?????? ????????? ??? : ?????? ???????????? ???????????? ????????????
			task.setTask_enddate(task.getExp_enddate());
			task.setExp_enddate("");
		}

		// ???????????? ????????? ???????????? ???????????? ???????????????, ???????????? ?????? ?????? split?????? ?????????.
		if (memList2.length() == 0 || memList2.equals("")) {

			log.debug("memList2.length == 0");

			memList2 = id;

			log.debug("staffs : {}", memList2);

		} else {
			staffList = memList2.split(",");

			log.debug("staffs : {}", memList2);
		}

		// ????????? ??????
		taskservice.addNewTask(task);
		log.debug("????????? task:{}", task);

		// ?????? ???????????? ?????? ????????? ??????
		if (staffList == null) {
			Taskstaff staff = new Taskstaff(task.getTask_seq(), memList2, 0);
			taskservice.addStaffs(staff);
		} else {
			for (int i = 0; i < staffList.length; i++) {
				Taskstaff staff = new Taskstaff(task.getTask_seq(), staffList[i], 0);
				log.debug("staff ?????? : {} ", staff);
				taskservice.addStaffs(staff);
			}
		}

		log.debug("uploads : {}", uploads.size());

		// ?????? ?????? ??????
		if (uploads != null && !uploads.isEmpty()) {
			log.debug("?????? ?????? ?????? ??????");
			for (int i = 0; i < uploads.size(); i++) {
				log.debug("upload ?????? : {}", uploads.get(i));
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
		log.debug("task ?????? ?????? ??? : {}", task);
		return "redirect:/" + prj_domain + "/task/taskList";
	}

	// * ????????? ???????????? ????????????
	@GetMapping(value = "/download")
	public String getDownload(@RequestParam("fileSeq") String tfileSeq, HttpServletResponse response) {

		log.debug("TaskController [getDownload] Start");
		// * ????????? ???????????? ????????????
		Taskfile file = taskservice.selectTaskFileByTfileSeq(tfileSeq);

		// ????????? ?????????
		String originalfile = new String(file.getTfile_ogfile());
		try {
			response.setHeader("Content-Disposition",
					" attachment;filename=" + URLEncoder.encode(originalfile, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// ????????? ?????? ??????
		String fullPath = uploadPath + "/" + file.getTfile_savefile();

		log.debug("fullPath: " + fullPath);

		// ????????? ????????? ?????? ?????? ???????????? ????????????????????? ????????? ???????????????
		FileInputStream filein = null;
		ServletOutputStream fileout = null;

		try {
			filein = new FileInputStream(fullPath);
			fileout = response.getOutputStream();

			// Spring??? ?????? ?????? ?????? ???????????? ??????
			FileCopyUtils.copy(filein, fileout);

			filein.close();
			fileout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.debug("TaskController [getDownload] End");
		return null;
	}

	// ?????? ??????
	@PostMapping("/insertComment")
	@ResponseBody
	public void insertComment(@AuthenticationPrincipal UserDetails user, TaskReply reply, String mentionTo) {

		String id = user.getUsername();

		reply.setTreply_writer(id);

		log.debug("?????? ?????? : {}", reply);
		log.debug("mentionTo : {}", mentionTo);

		taskDao.insertComment(reply);

		log.debug("insert ??? ?????? ?????? : {}", reply);

		if (mentionTo.length() != 0 || !mentionTo.equals("")) {
			Mention mention = new Mention(0, reply.getTreply_seq(), mentionTo);

			taskDao.insertMention(mention);

			log.debug("mention ?????? : {}", mention);
			
			Members member = memDao.getUserById(mentionTo);
			
			log.debug("member : {}", member);
			
					
			 sseService.send(mentionTo, member.getMemb_name() + "mentioned you : " + reply.getTreply_content() , Integer.toString(reply.getTask_seq()));
		}
	}
}
