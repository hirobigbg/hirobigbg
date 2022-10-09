package net.softsociety.issho.notice.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import net.softsociety.issho.notice.domain.Notice;
import net.softsociety.issho.notice.domain.Comment;
import net.softsociety.issho.notice.domain.CommentDetail;
import net.softsociety.issho.notice.domain.NoticeDetail;


@Mapper
public interface NoticeDAO {
	
	// 공지사항 등록
	public int insertNotice(Notice notice);
	
	// 조건에 맞는 공지사항 갯수
	public int countNotice(HashMap<String, String> map);

	// 공지사항 1개 조회
	public NoticeDetail selectNotice(int notice_seq);

	// 공지사항 목록 조회
	public ArrayList<NoticeDetail> listNotice(HashMap<String, String> map, RowBounds rb);
	
	// 공지사항 삭제
	public int deleteNotice(Notice notice);
	
	// 공지사항 수정
	public int updateNotice(Notice notice);
	
	// 공지사항 조회수 증가
	public int updateHits(int notice_seq);

	// 댓글 등록
	public int writeComment(Comment comment);

	// 공지의 달린 댓글 수
	public int countComment(int notice_seq);

	// 댓글 목록 조회
	public ArrayList<CommentDetail> listComment(int notice_seq);
	
	// 댓글 수정
	public int updateComment(Comment comment);
	
	// 댓글 삭제
	public int deleteComment(Comment comment);
	
	//* 신승훈 메인창 목록 (최신 공지사항)
	public List<Notice> selectMainNotice(String prj_domain);
}
