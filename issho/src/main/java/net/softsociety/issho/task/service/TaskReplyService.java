package net.softsociety.issho.task.service;

import java.util.ArrayList;

import net.softsociety.issho.notice.domain.Comment;
import net.softsociety.issho.notice.domain.CommentDetail;
import net.softsociety.issho.notice.domain.Notice;
import net.softsociety.issho.notice.domain.NoticeDetail;
import net.softsociety.issho.task.domain.Task;
import net.softsociety.issho.task.domain.TaskReply;
import net.softsociety.issho.task.domain.TaskReplyDetail;
import net.softsociety.issho.util.PageNavigator;

public interface TaskReplyService {
		// 댓글 목록 조회
	public ArrayList<TaskReplyDetail> listReply(int task_seq);
	
	 // 댓글 등록 
	public int writeReply(TaskReply taskReply);
	
	 /* 
	 * PageNavigator getReplyPageNavi(int pagePerGroup, int countPerPage, int page,
	 * int task_seq);
	 * 
	 * // 댓글 수정 public int updateReply(TaskReply taskReply);
	 */
	
	 // 댓글 삭제 
	 public int deleteReply(TaskReply taskReply);
	 
}
