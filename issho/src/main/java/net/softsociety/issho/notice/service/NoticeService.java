package net.softsociety.issho.notice.service;


import java.util.ArrayList;

import net.softsociety.issho.notice.domain.Comment;
import net.softsociety.issho.notice.domain.CommentDetail;
import net.softsociety.issho.notice.domain.Notice;
import net.softsociety.issho.notice.domain.NoticeDetail;
import net.softsociety.issho.util.PageNavigator;

public interface NoticeService {
	
	public int writeNotice(Notice notice);
	
	public ArrayList<NoticeDetail> listNotice(PageNavigator navi, String type, String searchWord, String prj_domain);

	PageNavigator getNoticePageNavi(int pagePerGroup, int countPerPage, int page, String type, String searchWord, String prj_domain);

//	public int insertNotice(Notice notice);
	
	public NoticeDetail selectNotice(int notice_seq);
	
	public NoticeDetail readNotice(int notice_seq, boolean increaseHits);
	
	public int deleteNotice(Notice notice);
	
	public int updateNotice(Notice notice);
	
	public int writeComment(Comment comment);
	
	public ArrayList<CommentDetail> listComment(int notice_seq);
	
	PageNavigator getCommentPageNavi(int pagePerGroup, int countPerPage, int page, int notice_seq);
	
	public int updateComment(Comment comment);
	
	public int deleteComment(Comment comment);

}
