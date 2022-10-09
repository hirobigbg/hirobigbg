package net.softsociety.issho.manager.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCnt {
	String memb_mail;
	int taskCnt; 
}
