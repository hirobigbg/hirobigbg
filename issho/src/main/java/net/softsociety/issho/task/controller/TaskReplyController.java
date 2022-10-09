package net.softsociety.issho.task.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.issho.member.dao.MemberDAO;
import net.softsociety.issho.member.service.MemberService;
import net.softsociety.issho.notice.domain.Notice;
import net.softsociety.issho.task.domain.TaskReply;
import net.softsociety.issho.task.domain.TaskReplyDetail;
import net.softsociety.issho.task.service.TaskReplyService;
import net.softsociety.issho.task.service.TaskService;
import net.softsociety.issho.util.FileService;

@Controller
@Slf4j
@RequestMapping("taskRpl")
public class TaskReplyController {
	
	@Autowired
	MemberService memservice;
	
	@Autowired
	TaskService taskservice;
	
	@Autowired
	TaskReplyService taskReplyService;
	
	//게시판 첨부파일 업로드 경로
	@Value("${spring.servlet.multipart.location}")
	String uploadPath;

	@Autowired
	MemberDAO memDao;

	
	@GetMapping("replyList")
	public String replyList(int task_seq , @AuthenticationPrincipal UserDetails user, Model model) {
		log.debug("----- 진입 GET : taskRpl/replyList");
		log.debug("-------------------- USER  : {}", user.getUsername());
		log.debug("----- PARAM:  {} ", task_seq);
		
		//PageNavigator navi = noticeService.getNoticePageNavi(pagePerGroup, countPerPage, page, type, searchWord, prj_domain);
		//log.debug("----- PageNavigator | {}", navi);
		
		model.addAttribute("task_seq", task_seq); 

		ArrayList<TaskReplyDetail> replyList = taskReplyService.listReply(task_seq);
		log.debug("----- 검색된 replyList : {}", replyList);
		
		//model.addAttribute("navi", navi);
		if(!replyList.isEmpty()) model.addAttribute("replyList", replyList);
				
		log.debug("----- 호출 : taskView/readReply");	
		return "/taskView/readReply";
	}	
	
//	@ResponseBody
	@PostMapping("writeReply")
	public String writeReply(
			TaskReply taskReply
			, @AuthenticationPrincipal UserDetails user
			, MultipartFile uploadFile) {
		
		log.debug("----- 진입 POST : taskRpl/taskReply");
		log.debug("저장할 글정보 : {}", taskReply);
		log.debug("파일 업로드 경로: {}", uploadPath);
		log.debug("파일 정보: {}", uploadFile);
		
		taskReply.setTreply_writer(user.getUsername());
		
		//첨부파일이 있는 경우 지정된 경로에 저장하고, 원본 파일명과 저장된 파일명을 Board객체에 세팅
		if (uploadFile != null && !uploadFile.isEmpty()) {
			String savedfile = FileService.saveFile(uploadFile, uploadPath);
			taskReply.setTreply_ogFile(uploadFile.getOriginalFilename());
			taskReply.setTreply_saveFile(savedfile);
		}
		
		int result = taskReplyService.writeReply(taskReply);
		
		log.debug("----- 호출 : taskRpl/replyList");	
		return "redirect:/taskRpl/replyList?task_seq=" + taskReply.getTask_seq();
	}
/*	
	@ResponseBody
	@PostMapping("writeReply")
	public int writeReply(TaskReply taskReply, @AuthenticationPrincipal UserDetails user) {
		
		log.debug("-------- AJAX 진입 POST : /taskRpl/writeReply");	
		taskReply.setTreply_writer(user.getUsername());
		
		log.debug("저장할 댓글정보 : {}", taskReply); 
		int result = taskReplyService.writeReply(taskReply);
		
		log.debug("----- AJAX 저장결과 : {}", result); 
		return  result;
	}
*/
	//홍상혁 미완성 9/28
//	@ResponseBody
//	@PostMapping("updateReply")
//	public int updateReply(TaskReply taskReply, @AuthenticationPrincipal UserDetails user) {
//		log.debug("-------- AJAX 진입 POST : /notice/updateComment");	
//		taskReply.setTreply_writer(user.getUsername());
//		
//		log.debug("삭제할 댓글정보 : {}", taskReply); 
//		int result = taskservice.updateReply(taskReply);
//		
//		log.debug("----- AJAX 수정결과 : {}", result); 
//		return  result;
//	}

	@ResponseBody
	@GetMapping("deleteReply")
	public int deleteReply(TaskReply taskReply, @AuthenticationPrincipal UserDetails user) {
		log.debug("----- PARAM:  {} ", taskReply);
		
		log.debug("-------- AJAX 진입 GET : /taskRpl/deleteReply");	
		taskReply.setTreply_writer(user.getUsername());
		
		log.debug("삭제할 댓글정보 : {}", taskReply); 
		int result = taskReplyService.deleteReply(taskReply);
		
		log.debug("----- AJAX 삭제결과 : {}", result); 
		return  result;
	}


}
