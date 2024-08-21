/**
 * 작성자 : 김상진
 * 작성일시 : 2021.08.12
 * 설명 : 기간별누적분석 VO
 */
package kr.co.sict.ums.ems.ana.vo;

public class PeriodSummVO {
	// 검색
	private int searchCampNo;		// 캠페인번호
	private String searchCampNm;	// 캠페인명
	private int searchDeptNo;		// 부서번호
	private String searchDeptNm;	// 부서명
	private String searchUserId;	// 사용자아이디
	private String searchUserNm;	// 사용자명
	private String searchStartDt;	// 검색시작일
	private String searchEndDt;		// 검색종료일
	
	private String ymd;			// 년월일
	private String weekNm;		// 요일명
	private String domainNm;	// 도메인명
	private String deptNm;		// 부서명(그룹명)
	private String userNm;		// 사용자명
	private String campNm;		// 캠페인명
	private int sendCnt;		// 발송수
	private int succCnt;		// 성공수
	private int errCnt;			// 실패수
	private int openCnt;		// 오픈수
	private int validCnt;		// 유효오픈수
	private int clickCnt;		// 클릭수
	private int blockCnt;		// 수신거부
	private String uilang;		// 언어권
	public int getSearchCampNo() {
		return searchCampNo;
	}
	public void setSearchCampNo(int searchCampNo) {
		this.searchCampNo = searchCampNo;
	}
	public String getSearchCampNm() {
		return searchCampNm;
	}
	public void setSearchCampNm(String searchCampNm) {
		this.searchCampNm = searchCampNm;
	}
	public int getSearchDeptNo() {
		return searchDeptNo;
	}
	public void setSearchDeptNo(int searchDeptNo) {
		this.searchDeptNo = searchDeptNo;
	}
	public String getSearchDeptNm() {
		return searchDeptNm;
	}
	public void setSearchDeptNm(String searchDeptNm) {
		this.searchDeptNm = searchDeptNm;
	}
	public String getSearchUserId() {
		return searchUserId;
	}
	public void setSearchUserId(String searchUserId) {
		this.searchUserId = searchUserId;
	}
	public String getSearchUserNm() {
		return searchUserNm;
	}
	public void setSearchUserNm(String searchUserNm) {
		this.searchUserNm = searchUserNm;
	}
	public String getSearchStartDt() {
		return searchStartDt;
	}
	public void setSearchStartDt(String searchStartDt) {
		this.searchStartDt = searchStartDt;
	}
	public String getSearchEndDt() {
		return searchEndDt;
	}
	public void setSearchEndDt(String searchEndDt) {
		this.searchEndDt = searchEndDt;
	}
	public String getYmd() {
		return ymd;
	}
	public void setYmd(String ymd) {
		this.ymd = ymd;
	}
	public String getWeekNm() {
		return weekNm;
	}
	public void setWeekNm(String weekNm) {
		this.weekNm = weekNm;
	}
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	public String getCampNm() {
		return campNm;
	}
	public void setCampNm(String campNm) {
		this.campNm = campNm;
	}
	public int getSendCnt() {
		return sendCnt;
	}
	public void setSendCnt(int sendCnt) {
		this.sendCnt = sendCnt;
	}
	public int getSuccCnt() {
		return succCnt;
	}
	public void setSuccCnt(int succCnt) {
		this.succCnt = succCnt;
	}
	public String getDomainNm() {
		return domainNm;
	}
	public void setDomainNm(String domainNm) {
		this.domainNm = domainNm;
	}
	public int getErrCnt() {
		return errCnt;
	}
	public void setErrCnt(int errCnt) {
		this.errCnt = errCnt;
	}
	public int getOpenCnt() {
		return openCnt;
	}
	public void setOpenCnt(int openCnt) {
		this.openCnt = openCnt;
	}
	public int getValidCnt() {
		return validCnt;
	}
	public void setValidCnt(int validCnt) {
		this.validCnt = validCnt;
	}
	public int getClickCnt() {
		return clickCnt;
	}
	public void setClickCnt(int clickCnt) {
		this.clickCnt = clickCnt;
	}
	public int getBlockCnt() {
		return blockCnt;
	}
	public void setBlockCnt(int blockCnt) {
		this.blockCnt = blockCnt;
	}
	public String getUilang() {
		return uilang;
	}
	public void setUilang(String uilang) {
		this.uilang = uilang;
	}
}
