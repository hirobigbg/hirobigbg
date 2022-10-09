package net.softsociety.issho.manager.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriveFile {
	private int driveFile_seq;
	private int driveFolder_seq2;
	private String driveFile_ogFile;
	private String driveFile_saveFile;
	private int driveFolder_seq;
	private String driveFile_writer;
	private String driveFile_type;
	private String driveFile_date;
}
