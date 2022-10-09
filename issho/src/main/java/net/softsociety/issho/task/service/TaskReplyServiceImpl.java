package net.softsociety.issho.task.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.issho.task.dao.TaskDAO;
import net.softsociety.issho.task.dao.TaskReplyDAO;
import net.softsociety.issho.task.domain.TaskReply;
import net.softsociety.issho.task.domain.TaskReplyDetail;

@Slf4j
@Transactional
@Service
public class TaskReplyServiceImpl implements TaskReplyService{
	
	@Autowired
	private TaskReplyDAO taskReplyDAO;
	
	// 댓글 목록 조회
	@Override
	public ArrayList<TaskReplyDetail> listReply(int task_seq){
		ArrayList<TaskReplyDetail> commentList = taskReplyDAO.listReply(task_seq);
		return commentList;

	}
	

	// 댓글 등록
	 
  	@Override 
  	public int writeReply(TaskReply taskReply) { 
		  int result = taskReplyDAO.insertReply(taskReply); 
		  return result;
	  
	  }
	  
	/* 
	 * @Override public PageNavigator getReplyPageNavi( int pagePerGroup, int
	 * countPerPage, int page, int task_seq) {
	 * 
	 * int total = taskDAO.countReply(task_seq); PageNavigator navi = new
	 * PageNavigator(pagePerGroup, countPerPage, page, total);
	 * 
	 * return navi; }
	 * 
	 * 
	 * // 댓글 수정
	 * 
	 * @Override public int updateReply(TaskReply taskReply) { 
	 * int result =	taskDAO.updateReply(taskReply);
	 * return result;
	 * 
	 * }
	 */ 
	  
	 // 댓글 삭제
	  
	  @Override public int deleteReply(TaskReply taskReply) { 
		  int result = taskReplyDAO.deleteReply(taskReply); 
		  return result;
	  
	  }
	 
}
