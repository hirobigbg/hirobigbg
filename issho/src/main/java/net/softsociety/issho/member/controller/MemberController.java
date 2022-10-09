package net.softsociety.issho.member.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import net.softsociety.issho.manager.domain.DriveFile;
import net.softsociety.issho.manager.service.ManagerService;
import net.softsociety.issho.manager.util.PageNavigator;
import net.softsociety.issho.member.dao.MemberDAO;
import net.softsociety.issho.member.domain.Members;
import net.softsociety.issho.member.service.MemberService;
import net.softsociety.issho.util.FileService;

@lombok.extern.slf4j.Slf4j
/**
 * @brief 멤버 관련 컨트롤러 : 회원가입
 * @author 윤영혜
 *
 */

@Controller
@RequestMapping("/member")
public class MemberController {

	@Autowired
	MemberService memService;

	@Autowired
	ServletContext servletContext;

	@Autowired
	MemberDAO memDao;

	@Autowired
	private WebApplicationContext webApplicationContext;

	// 여기서부터 김지윤 작성
	@Autowired
	ManagerService manService;

	// 게시판 목록의 페이지당 글 수
	@Value("${user.manager.members.page}")
	int countPerPage;

	// 게시판 목록의 페이지 이동 링크 수
	@Value("${user.manager.members.group}")
	int pagePerGroup;

	// 첨부파일 저장할 경로
	@Value("${spring.servlet.multipart.location}")
	String uploadPath;

	/**
	 * 
	 * 로그인 폼 이동
	 */
	@GetMapping("/join")
	public String join() {

		return "member/member_join";
	}

	/**
	 * 입력값 받아서 회원가입 로직 수행
	 * 
	 * @return
	 */
	@PostMapping("/join")
	public String join(Members members, @RequestParam MultipartFile upload) {

		log.debug("전달받은 객체 : {}", members);

		String realpath = servletContext.getRealPath("/resources");

		log.debug(realpath);

		// resources의 상위 디렉토리까지의 경로가 저장됨.
		// String webRoot = webApplicationContext.getServletContext().getRealPath("/");
		// String imgRoot = webRoot + "resources/savedImg";

		// log.debug("webRoot : {}", webRoot);
		// log.debug("imgRoot : {}", imgRoot);

//		if (upload != null && !upload.isEmpty()) {
//			String savedfile = FileService.saveFile(upload, realpath + "/upload");
//			members.setMemb_ogfile(upload.getOriginalFilename());
//			members.setMemb_savedfile(savedfile);
//		}

		/*
		 * if(upload != null && !upload.isEmpty()) { try { String absolutePath = new
		 * ClassPathResource(uploadPath).getFile().getAbsolutePath();
		 * log.debug("absolutePath : {}", absolutePath); String savedfile =
		 * FileService.saveFile(upload, absolutePath);
		 * members.setMemb_ogfile(upload.getOriginalFilename());
		 * members.setMemb_savedfile(savedfile); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } }
		 */

		if (upload != null && !upload.isEmpty()) {

			/*
			 * String absolutePath = new
			 * ClassPathResource(uploadPath).getFile().getAbsolutePath();
			 * log.debug("absolutePath : {}", absolutePath);
			 */
			log.debug("uploadPath : {}", uploadPath);
			String savedfile = FileService.saveFile(upload, uploadPath);
			members.setMemb_ogfile(upload.getOriginalFilename());
			members.setMemb_savedfile(savedfile);

		}

		log.debug("업로드 처리후 : {}", members);

		memService.memberJoin(members);

		return "redirect:/";
	}

	/**
	 * 아이디 중복체크
	 * 
	 * @param memb_mail
	 * @return 결과값
	 */
	@ResponseBody
	@PostMapping("/idCheck")
	public int idCheck(String memb_mail) {

		log.debug("이메일 : {}", memb_mail);

		int result = memService.idSearchOne(memb_mail);

		log.debug("idCheck 결과 : {}", result);

		return result;
	}

	/**
	 * 로그인 폼 이동
	 * 
	 * @return
	 */
	@GetMapping("/loginForm")
	public String loginForm() {
		return "member/member_login";
	}

	/*
	public String memSearch(String memb_mail) {

		log.debug("memSearch mail : {}", memb_mail);

		Members member = memDao.getUserById(memb_mail);

		log.debug("member : {}", member);

		log.debug("member.getMemb_name() : {} ", member.getMemb_name());

		return "member/addressBook";
	}
	*/
	
	@RequestMapping(value = "/memSearch",produces = "application/text; charset=utf8")
	@ResponseBody
	public String memSearch(String memb_mail) {
		
		log.debug("memSearch 진입");
		
		log.debug("memSearch mail : {}", memb_mail);
		
		Members member = memDao.getUserById(memb_mail);
		
		log.debug("membSearch Memb 객체 : {}", member);
		
		return member.getMemb_name();

	}
	
	@PostMapping(value = "/memSearchObject")
	@ResponseBody
	public Members memSearchObject(String memb_mail) {
		
		log.debug("memSearch 진입");
		
		log.debug("memSearch mail : {}", memb_mail);
		
		Members member = memDao.getUserById(memb_mail);
		
		log.debug("membSearch Memb 객체 : {}", member);
		
		return member;
	}

	@PostMapping("/memSearchByIdName")
	@ResponseBody
	public List<Members> memSearchByIdName(String searchWord) {

		log.debug("memSearchByIdName 진입");
		
		log.debug("searchWord : {}", searchWord);

		List<Members> list = memDao.memSearchByIdName(searchWord);

		return list;

	}

}
