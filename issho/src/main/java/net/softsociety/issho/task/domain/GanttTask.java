package net.softsociety.issho.task.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GanttTask {
	private int task_seq;
	private String task_startdate;
	private String task_enddate;
	private String exp_startdate;
	private String exp_enddate;
}
