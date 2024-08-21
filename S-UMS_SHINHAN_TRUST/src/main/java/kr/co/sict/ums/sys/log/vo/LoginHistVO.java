/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.12
 * 설명 : 로그인 이력 VO
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : sys.vo -->sys.log.vo 
 */
package kr.co.sict.ums.sys.log.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class LoginHistVO extends CommonVO {
/*
LGN_HST_ID	NUMBER
DEPT_NO	NUMBER
USER_ID	VARCHAR2(30 BYTE)
LGN_DT	CHAR(14 BYTE)
LGN_IP	VARCHAR2(20 BYTE)
 */
	private int lgnHstId;				// 일련번호
	private int deptNo;					// 부서번호
	private String deptNm;				// 부서명
	private String userId;				// 사용자ID
	private String userNm;				// 사용자명
	private String lgnDt;				// 로그인시간
	private String lgnIp;				// 로그인IP
	
	// 검색
	private String searchDeptNm;		// 검색부서명
	private String searchUserId;		// 검색사용자ID
	private String searchLgnStdDt;		// 검색시작일
	private String searchLgnEndDt;		// 검색종료일
	
	public int getLgnHstId() {
		return lgnHstId;
	}
	public void setLgnHstId(int lgnHstId) {
		this.lgnHstId = lgnHstId;
	}
	public int getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(int deptNo) {
		this.deptNo = deptNo;
	}
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	public String getLgnDt() {
		return lgnDt;
	}
	public void setLgnDt(String lgnDt) {
		this.lgnDt = lgnDt;
	}
	public String getLgnIp() {
		return lgnIp;
	}
	public void setLgnIp(String lgnIp) {
		this.lgnIp = lgnIp;
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
	public String getSearchLgnStdDt() {
		return searchLgnStdDt;
	}
	public void setSearchLgnStdDt(String searchLgnStdDt) {
		this.searchLgnStdDt = searchLgnStdDt;
	}
	public String getSearchLgnEndDt() {
		return searchLgnEndDt;
	}
	public void setSearchLgnEndDt(String searchLgnEndDt) {
		this.searchLgnEndDt = searchLgnEndDt;
	}
}
