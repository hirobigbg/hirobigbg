package net.softsociety.issho.notice.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.issho.notice.domain.Comment;
import net.softsociety.issho.notice.domain.CommentDetail;
import net.softsociety.issho.notice.domain.Notice;
import net.softsociety.issho.notice.domain.NoticeDetail;
import net.softsociety.issho.notice.service.NoticeService;
import net.softsociety.issho.project.domain.Projects;
import net.softsociety.issho.project.service.ProjectService;
import net.softsociety.issho.util.FileService;
import net.softsociety.issho.util.PageNavigator;


@Slf4j
@RequestMapping("**/notice")
@Controller
public class NoticeController {
	
	 //게시판 목록의 페이지당 글 수
	@Value("${user.manager.members.page}")
	int countPerPage;
	
	//게시판 목록의 페이지 이동 링크 수
	@Value("${user.manager.members.group}")
	int pagePerGroup;

	//게시판 첨부파일 업로드 경로
	@Value("${spring.servlet.multipart.location}")
	String uploadPath;

	@Autowired
	NoticeService noticeService;
	
	@Autowired
	ProjectService projectService;
	
	//prj_seq는 나중에 반드시 프로젝트에 공통적으로 관리하고 읽을 수 있어야 함
	String prj_domain;
	
	@GetMapping("noticeList")
	public String noticeList(HttpServletRequest request, @RequestParam(name = "page", defaultValue = "1") int page,
								String prj_domain, String type , String searchWord,  Model model, 
								@AuthenticationPrincipal UserDetails user) {
			log.debug("----- 진입 GET : notice/noticeList");
			log.debug("-------------------- USER  : {}", user.getUsername());
			log.debug("----- PARAM: {} | {} | {}", page, type, searchWord);
			
			// prj_domain 설정
			String calledValue = request.getServletPath();
			String[] splitedUrl = calledValue.split("/");
			prj_domain = splitedUrl[1];
			
			log.debug("----- 찾아낸 prj_domain : {}", prj_domain);
			
			//project 정보 받아오기
			Projects project = projectService.searchOne(prj_domain);
			String prj_name = project.getPrj_name(); 
			log.debug("----- 찾아낸 prj_name : {}", prj_name);

			PageNavigator navi = noticeService.getNoticePageNavi(pagePerGroup, countPerPage, page, type, searchWord, prj_domain);
			log.debug("----- PageNavigator | {}", navi);
			
			ArrayList<NoticeDetail> noticeList = noticeService.listNotice(navi, type, searchWord, prj_domain);
			log.debug("----- 검색된 noticeList : {}", noticeList);
			
			//!!!! 나중에 prj_domain는 받아와서 반드시 채워주어야 함!!!!
			model.addAttribute("prj_domain", prj_domain); 
			model.addAttribute("prj_name", prj_name); 

			model.addAttribute("navi", navi);
			if(!noticeList.isEmpty())
				model.addAttribute("noticeList", noticeList);
			model.addAttribute("type", type);
			model.addAttribute("searchWord", searchWord);
			
			log.debug("----- 호출 : noticeView/noticeList");	
			return "/noticeView/noticeList";
	}
	
	@GetMapping("readNotice")
	public String readNotice(
			@RequestParam(name="notice_seq", defaultValue = "0") int notice_seq, 
			Model model) { 

		log.debug("----- 진입 GET : notice/readNotice");
		log.debug("----- PARAM: {} ", notice_seq);

		NoticeDetail noticeDetail = noticeService.readNotice(notice_seq, true);
		if (noticeDetail == null) {
			return "redirect:noticeList"; //글이 없으면 목록으로
//			return "redirect:/notice/noticeList"; //글이 없으면 목록으로
		}
		
		//현재 글에 달린 리플들
		ArrayList<CommentDetail> commentList = noticeService.listComment(notice_seq);

		//결과를 모델에 담아서 HTML에서 출력
		model.addAttribute("noticeDetail", noticeDetail);
		model.addAttribute("commentList", commentList);
		
		log.debug("----- noticeDetail : {} ", noticeDetail);
		log.debug("----- commentList : {} ", commentList);
			
		log.debug("----- 호출 : notice/readNotice");
		return "/noticeView/readNotice";
	}	

	@GetMapping("fileDownload")
	public String fileDownload(int notice_seq, Model model, HttpServletResponse response) {
		//전달된 글 번호로 글 정보 조회
		NoticeDetail noticeDetail = noticeService.readNotice(notice_seq, false);
		
		//원래의 파일명
		String originalfile = new String(noticeDetail.getNotice_ogFilr());
		try {
			response.setHeader("Content-Disposition", " attachment;filename="+ URLEncoder.encode(originalfile, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//저장된 파일 경로
		String fullPath = uploadPath + "/" + noticeDetail.getNotice_saveFile();
		
		//서버의 파일을 읽을 입력 스트림과 클라이언트에게 전달할 출력스트림
		FileInputStream filein = null;
		ServletOutputStream fileout = null;
		
		try {
			filein = new FileInputStream(fullPath);
			fileout = response.getOutputStream();
			
			//Spring의 파일 관련 유틸 이용하여 출력
			FileCopyUtils.copy(filein, fileout);
			
			filein.close();
			fileout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		log.debug("----- 호출 : redirect:/");	
		return "redirect:/";
	}	
	
	@PostMapping("writeNotice")
	public String writeNotice(
			Notice notice
			, @AuthenticationPrincipal UserDetails user
			, MultipartFile upload, Model model) {
		
//		notice.setPrj_domain(prj_domain);
		
		log.debug("----- 진입 POST : notice/writeNotice");
		log.debug("저장할 글정보 : {}", notice);
		log.debug("파일 업로드 경로: {}", uploadPath);
		log.debug("파일 정보: {}", upload);
		
		notice.setNotice_writer(user.getUsername());
		
		//첨부파일이 있는 경우 지정된 경로에 저장하고, 원본 파일명과 저장된 파일명을 Board객체에 세팅
		if (upload != null && !upload.isEmpty()) {
			String savedfile = FileService.saveFile(upload, uploadPath);
			notice.setNotice_ogFilr(upload.getOriginalFilename());
			notice.setNotice_saveFile(savedfile);
		}
		
		int result = noticeService.writeNotice(notice);
		
		model.addAttribute("prj_domain", notice.getPrj_domain()); 
		log.debug("prj_domain : {}", notice.getPrj_domain());

		log.debug("----- 호출 : notice/noticeList");	
//OK		return "redirect:/" + notice.getPrj_domain() +"/notice/noticeList";
		return "redirect:noticeList";
	}

	@GetMapping("writeNotice")
	public String writeNotice(String prj_domain, Model model) {
		log.debug("----- 진입 GET : notice/writeNotice");
		
		model.addAttribute("prj_domain", prj_domain);
		log.debug("prj_domain 정보 : {}", prj_domain);
		
		log.debug("----- 호출 : noticeView/writeForm");	
		return "/noticeView/writeForm";
	}

	@GetMapping("updateNotice")
	public String updateNotice(int notice_seq, Model model) {
		log.debug("----- 진입 GET : notice/updateNotice");

		log.debug("----- PARAM: {} ", notice_seq);

		NoticeDetail noticeDetail = noticeService.readNotice(notice_seq, true);
		if (noticeDetail == null) {
			return "redirect:noticeList"; //글이 없으면 목록으로
		}
		
		//결과를 모델에 담아서 HTML에서 출력
		model.addAttribute("noticeDetail", noticeDetail);
		log.debug("----- noticeDetail: {} ", noticeDetail);

		log.debug("----- 호출 : /noticeView/writeForm");

		return "/noticeView/writeForm";
	}

	@PostMapping("updateNotice")
	public String updateNotice(
			Notice notice
			, @AuthenticationPrincipal UserDetails user
			, MultipartFile upload) {

		log.debug("----- 진입 GET : notice/updateNotice");
		
		log.debug("저장할 글정보 : {}", notice);
		log.debug("파일 정보: {}", upload);
		
		//작성자 아이디 추가
		notice.setNotice_writer(user.getUsername());
		
		Notice oldNotice = null;
		String oldSavefile = null;
		String newSavefile = null;
		
		//첨부파일이 있는 경우 기존파일 삭제 후 새 파일 저장
		if (upload != null && !upload.isEmpty()) {
			oldNotice = noticeService.readNotice(notice.getNotice_seq(), false);
			oldSavefile = (oldNotice == null) ? null : oldNotice.getNotice_saveFile();
			
			newSavefile = FileService.saveFile(upload, uploadPath);
			notice.setNotice_ogFilr(upload.getOriginalFilename());
			notice.setNotice_saveFile(newSavefile);
			log.debug("새파일:{}, 구파일:{}", newSavefile, oldSavefile);
		}
		
		int result = noticeService.updateNotice(notice);
		
		//글 수정 성공 and 첨부된 파일이 있는 경우 파일도 삭제
		if (result == 1 && newSavefile != null) {
			FileService.deleteFile(uploadPath + "/" + oldSavefile);
		}
		
		log.debug("----- 호출 : notice/readNotice?notice_seq=notice.getNotice_seq()");
//		return "redirect:notice/readNotice?notice_seq=" + notice.getNotice_seq();
		return "redirect:readNotice?notice_seq=" + notice.getNotice_seq();
	}

	@GetMapping ("deleteNotice")
	public String deleteNotice(int notice_seq, @AuthenticationPrincipal UserDetails user) {

		log.debug("----- 진입 GET : notice/deleteNotice");
		
		//해당 번호의 글 정보 조회
		Notice notice = noticeService.readNotice(notice_seq, false);
		
		if (notice == null) {
			return "redirect:noticeList";
		}
		
		//첨부된 파일명 확인
		String savedfile = notice.getNotice_saveFile();
		
		log.debug("notice 작성자 정보 : {}", notice.getNotice_writer());
		//로그인 아이디를 notice객체에 저장
		notice.setNotice_writer(user.getUsername());
		log.debug("로그인 유저 정보 : {}", user);
		
		//글 삭제
		int result = noticeService.deleteNotice(notice);
		
		//글 삭제 성공 and 첨부된 파일이 있는 경우 파일도 삭제
		if (result == 1 && savedfile != null) {
			FileService.deleteFile(uploadPath + "/" + savedfile);
		}

		log.debug("----- 호출 : /notice/noticeList");
		return "redirect:noticeList";
	}

	/*
	 * @PostMapping("writeComment") public String write	ent(Notice notice,
	 * Comment comment, @AuthenticationPrincipal UserDetails user) {
	 * 
	 * log.debug("----- 진입 POST : notice/writeComment");
	 * comment.setNoticeCmt_writer(user.getUsername());
	 * 
	 * log.debug("저장할 댓글정보 : {}", comment); int result =
	 * noticeService.writeComment(comment);
	 * 
	 * log.debug("----- 호출 : notice/noticeList"); return
	 * "redirect:/notice/readNotice?notice_seq=" + notice.getNotice_seq(); }
	 */		
	@ResponseBody
	@PostMapping("writeComment")
	public int writeComment(Comment comment, @AuthenticationPrincipal UserDetails user) {
		
		log.debug("-------- AJAX 진입 POST : /notice/writeComment");	
		comment.setNoticeCmt_writer(user.getUsername());
		
		log.debug("저장할 댓글정보 : {}", comment); 
		int result = noticeService.writeComment(comment);
		
		log.debug("----- AJAX 저장결과 : {}", result); 
		return  result;
	}
	
	@ResponseBody
	@PostMapping("updateComment")
	public int updateComment(Comment comment, @AuthenticationPrincipal UserDetails user) {
		log.debug("-------- AJAX 진입 POST : /notice/updateComment");	
		comment.setNoticeCmt_writer(user.getUsername());
		
		log.debug("삭제할 댓글정보 : {}", comment); 
		int result = noticeService.updateComment(comment);
		
		log.debug("----- AJAX 수정결과 : {}", result); 
		return  result;
	}

	@ResponseBody
	@GetMapping("deleteComment")
	public int deleteComment(Comment comment, @AuthenticationPrincipal UserDetails user) {
		log.debug("-------- AJAX 진입 GET : /notice/deleteComment");	
		comment.setNoticeCmt_writer(user.getUsername());
		
		log.debug("삭제할 댓글정보 : {}", comment); 
		int result = noticeService.deleteComment(comment);
		
		log.debug("----- AJAX 삭제결과 : {}", result); 
		return  result;
	}
}
