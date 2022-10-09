package net.softsociety.issho.project.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import net.softsociety.issho.project.domain.ProjectMember;
import net.softsociety.issho.project.domain.Projects;

@Mapper
public interface ProjectDAO {

	int domainCheck(String prj_domain);

	void newProject(Projects projects);

	void grantPM(ProjectMember pjmb);

	Projects searchOne(String prj_domain);

	ArrayList<Projects> listProjects(HashMap<String, String> map, RowBounds rb);

	Projects getProjectsInfo(String domain);

}
