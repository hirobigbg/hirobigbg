package net.softsociety.issho.task.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Taskfile {

	public Taskfile(int task_seq, String tfile_ogfile, String tfile_savefile) {
		this.task_seq = task_seq;
		this.tfile_ogfile = tfile_ogfile;
		this.tfile_savefile = tfile_savefile;
	}

	private int tfile_seq;
	private int task_seq;
	private String tfile_ogfile;
	private String tfile_savefile;

}
