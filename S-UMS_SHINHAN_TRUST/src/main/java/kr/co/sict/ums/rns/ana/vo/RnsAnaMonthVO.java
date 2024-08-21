/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.22
 * 설명 : RNS 월별통계 VO
 */
package kr.co.sict.ums.rns.ana.vo;

public class RnsAnaMonthVO {
	private int days;				// 일자(숫자)
	private int send;				// 발송건수
	private int success;			// 성공건수
	private int failed;				// 실패건수
	private int opened;				// 오픈건수
	private String displaySuccess;   // 표기되는 성공(%)
	private String displayFailed;    // 표기되는 실패(%)
	private String displayOpened;    // 표기되는 오픈(%)
	private String displayDays;      // 표기되는 일자 내용		
	
	//검색조건
	private String searchYm;		// 검색년월 
	private String searchStatus;	// 검색상태
	private String searchService;	// 검색서비스
	private String searchSname;		// 발송자명
	
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public int getSend() {
		return send;
	}
	public void setSend(int send) {
		this.send = send;
	}
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public int getFailed() {
		return failed;
	}
	public void setFailed(int failed) {
		this.failed = failed;
	}
	public int getOpened() {
		return opened;
	}
	public void setOpened(int opened) {
		this.opened = opened;
	}
	public String getDisplaySuccess() {
		return displaySuccess;
	}
	public void setDisplaySuccess(String displaySuccess) {
		this.displaySuccess = displaySuccess;
	}
	public String getDisplayFailed() {
		return displayFailed;
	}
	public void setDisplayFailed(String displayFailed) {
		this.displayFailed = displayFailed;
	}
	public String getDisplayOpened() {
		return displayOpened;
	}
	public void setDisplayOpened(String displayOpened) {
		this.displayOpened = displayOpened;
	}
	public String getDisplayDays() {
		return displayDays;
	}
	public void setDisplayDays(String displayDays) {
		this.displayDays = displayDays;
	}
	public String getSearchYm() {
		return searchYm;
	}
	public void setSearchYm(String searchYm) {
		this.searchYm = searchYm;
	}
	public String getSearchStatus() {
		return searchStatus;
	}
	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}
	public String getSearchService() {
		return searchService;
	}
	public void setSearchService(String searchService) {
		this.searchService = searchService;
	}
	public String getSearchSname() {
		return searchSname;
	}
	public void setSearchSname(String searchSname) {
		this.searchSname = searchSname;
	}
	 
}
