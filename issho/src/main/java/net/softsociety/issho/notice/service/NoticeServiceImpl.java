package net.softsociety.issho.notice.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.issho.notice.dao.NoticeDAO;
import net.softsociety.issho.notice.domain.Comment;
import net.softsociety.issho.notice.domain.CommentDetail;
import net.softsociety.issho.notice.domain.Notice;
import net.softsociety.issho.notice.domain.NoticeDetail;
import net.softsociety.issho.util.PageNavigator;


@Slf4j
@Transactional
@Service
public class NoticeServiceImpl implements NoticeService{
	
	@Autowired
	NoticeDAO noticeDAO;
	
	@Override
	public ArrayList<NoticeDetail> listNotice(PageNavigator navi, String type, String searchWord,String prj_domain){
		HashMap<String, String> map =new HashMap<>();
		map.put("type", type);
		map.put("searchWord", searchWord);
		map.put("prj_domain", prj_domain);
		
		RowBounds rb = new RowBounds(navi.getStartRecord(), navi.getCountPerPage());
		ArrayList<NoticeDetail> noticeList = noticeDAO.listNotice(map, rb); 
		
		return noticeList;		
	}

	
	@Override
	public PageNavigator getNoticePageNavi(
			int pagePerGroup, int countPerPage, int page, String type, String searchWord, String prj_domain) {
		
		HashMap<String, String> map = new HashMap<>();
		map.put("type", type);
		map.put("searchWord", searchWord);
		map.put("prj_domain", prj_domain);
		
		int total = noticeDAO.countNotice(map);
		PageNavigator navi = new PageNavigator(pagePerGroup, countPerPage, page, total);
		
		return navi;
	}
	
	//Notice 정보 읽어오기(Hit count는 증가하지 않음)
	@Override
	public NoticeDetail selectNotice(int notice_seq) {
		
		NoticeDetail noticeDetail = noticeDAO.selectNotice(notice_seq); 
		return noticeDetail;		
	}

	//Notice 정보 읽어오기(사용자가 읽을 때 Hit count는 증가)
	@Override
	public NoticeDetail readNotice(int notice_seq, boolean increaseHits) {
		int result;
		if(increaseHits)
			result = noticeDAO.updateHits(notice_seq);
		
		NoticeDetail notice = noticeDAO.selectNotice(notice_seq);
		return notice;
	}
	
	@Override
	public int writeNotice(Notice notice) {
		int result = noticeDAO.insertNotice(notice);
		return result;
	}

	public int deleteNotice(Notice notice) {
		int result = noticeDAO.deleteNotice(notice);
		return result;
	}

	public int updateNotice(Notice notice) {
		int result = noticeDAO.updateNotice(notice);
		return result;
	}

	@Override
	public int writeComment(Comment comment) {
		int result = noticeDAO.writeComment(comment);
		return result;
	}
	
	@Override
	public ArrayList<CommentDetail> listComment(int notice_seq){
		ArrayList<CommentDetail> commentList = noticeDAO.listComment(notice_seq);
		return commentList;
	}
	
	@Override
	public PageNavigator getCommentPageNavi(
			int pagePerGroup, int countPerPage, int page, int notice_seq) {
		
		int total = noticeDAO.countComment(notice_seq);
		PageNavigator navi = new PageNavigator(pagePerGroup, countPerPage, page, total);
		
		return navi;
	}

	@Override
	public int updateComment(Comment comment) {
		int result = noticeDAO.updateComment(comment);
		return result;
	}
	
	@Override
	public int deleteComment(Comment comment) {
		int result = noticeDAO.deleteComment(comment);
		return result;
	}

//	@Override
//	public int insertNotice(Notice notice) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
}
