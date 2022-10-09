package net.softsociety.issho.task.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskReplyDetail extends TaskReply{
	String memb_name;
}
