package net.softsociety.issho.manager.util;

import lombok.Data;
/*
 * 전체 글개수 : 125개
 * 페이지당 글 수 : 10개
 * 그룹당 페이지 이동 링크 수 : 5
 * 
 * 총페이지수 : 13
 * 1페이지의글 : 0행부터 10개
 * 2페이지의글 : 10행부터 10개
 * 현재 페이지는 : ?
 * 
 * 현재 그룹의 페이지 이동 링크 : 1 ~ 5
 */

/**
 * 게시판 페이징 처리 클래스
 */
@Data
public class PageNavigator {
	//페이지 관련 정보 
	private int countPerPage;		//페이지당 글목록 수
	private int pagePerGroup;		//그룹당 페이지 수 
	private int currentPage;		//현재 페이지 (최근 글이 1부터 시작)
	private int totalRecordsCount;	//전체 글 수
	private int totalPageCount;		//전체 페이지 수
	private int currentGroup;		//현재 그룹 (최근 그룹이 0부터 시작)
	private int startPageGroup;		//현재 그룹의 첫 페이지
	private int endPageGroup;		//현재 그룹의 마지막 페이지
	private int startRecord;		//전체 레코드 중 현재 페이지 첫 글의 위치 (0부터 시작)
	
	/*
	 * 생성자
	 */
	public PageNavigator(int pagePerGroup, int countPerPage, int currentPage, int totalRecordsCount) {
		//그룹당 페이지 수, 페이지당 글 수, 현재 페이지, 전체 글 수를 전달받음
		this.countPerPage = countPerPage;
		this.pagePerGroup = pagePerGroup;
		this.totalRecordsCount = totalRecordsCount;
		
		//전체 페이지 수
		totalPageCount = (totalRecordsCount + countPerPage - 1) / countPerPage;

		//전달된 현재 페이지가 1보다 작으면 현재페이지를 1페이지로 지정
		if (currentPage < 1)	currentPage = 1;
		//전달된 현재 페이지가 마지막 페이지보다 크면 현재페이지를 마지막 페이지로 지정
		if (currentPage > totalPageCount)	currentPage = totalPageCount;
		
		this.currentPage = currentPage;

		//현재 그룹
		currentGroup = (currentPage - 1) / pagePerGroup;
		
		//현재 그룹의 첫 페이지
		startPageGroup = currentGroup * pagePerGroup + 1;
		//현재 그룹의 첫 페이지가 1보다 작으면 1로 처리
		startPageGroup = startPageGroup < 1 ? 1 : startPageGroup;
		//현재 그룹의 마지막 페이지
		
		endPageGroup = startPageGroup + pagePerGroup - 1;
		//현재 그룹의 마지막 페이지가 전체 페이지 수보다 작으면 전체페이지 수를 마지막으로.
		endPageGroup = endPageGroup < totalPageCount ? endPageGroup : totalPageCount;

		//전체 레코드 중 현재 페이지 첫 글의 위치
		startRecord = (currentPage - 1) * countPerPage;			
	}
}
