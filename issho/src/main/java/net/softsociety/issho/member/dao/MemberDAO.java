package net.softsociety.issho.member.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import net.softsociety.issho.member.domain.Members;
import net.softsociety.issho.project.domain.ProjectMember;

@Mapper
public interface MemberDAO {
	public ArrayList<Members> listMembers(Members members);

	public int idSearchOne(String memb_mail);

	public void memberJoin(Members members);

	public ArrayList<Members> searchPjMem(String prj_domain);

	public int deleteMember(Members members);

	public Members getUserById(String username);

	public ProjectMember getProjectMemById(String username);

	public List<Members> memSearchByIdName(String searchWord);

	
}
