package net.softsociety.issho.task.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import net.softsociety.issho.task.domain.TaskReply;
import net.softsociety.issho.task.domain.TaskReplyDetail;

@Mapper
public interface TaskReplyDAO {
	// 댓글 목록 조회
	public ArrayList<TaskReplyDetail> listReply(int task_seq);

	// 댓글 등록 
	public int insertReply(TaskReply taskReply);
	
	/*
	 * // 공지의 달린 댓글 수 public int countReply(int task_seq);
	 * 
	 * // 댓글 수정 public int updateReply(TaskReply taskReply);
	 */ 
	 
	// 댓글 삭제 
	public int deleteReply(TaskReply taskReply);
	 
}
