 /* 작성자 : 김준희
 * 작성일시 : 2021.10.13
 * 설명 : 메일 Sms VO
 */
package kr.co.sict.ums.sms.ana.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class SmsMsgListVO extends CommonVO {
	private int campNo;			    // 캠페인 등록번호
	private String campNm;			//캠페인명
	private String msgid;			//메시지아이디
	private String keygen;			//메시지키
	private String smsName;			//메시지제목
	private String smsMessage;		//메시지내용
	private int deptNo;				//사용자그룹번호
	private String deptNm;			//사용자그룹(부서)명
	private String userId;			//사용자아이디
	private String userNm;			//사용자명
	private String gubun;			//전송유형
	private String gubunNm;			//전송유형명
	private int succCnt;			//성공
	private int failCnt;			//실패
	private int totalCnt;			//총건수
	
	
	private String searchCampNm;	// 캠페인명 검색
	private String searchCampTy;	// 캠페인목적 검색
	private String searchStatus;	// 상태 검색
	private String searchStartDt;	// 검색 시작일
	private String searchEndDt;		// 검색 종료일
	private int searchDeptNo;		// 사용자그룹 검색
	private String searchUserId;	// 사용자 검색
	private String searchSmsName;	// 검색문자명
	
	private String adminYn;			// 관리자여부
	private String uilang;			// 언어권
	
	public String getSearchSmsName() {
		return searchSmsName;
	}
	public void setSearchSmsName(String searchSmsName) {
		this.searchSmsName = searchSmsName;
	}
	public String getMsgid() {
		return msgid;
	}
	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}
	public String getKeygen() {
		return keygen;
	}
	public void setKeygen(String keygen) {
		this.keygen = keygen;
	}
	
	public String getSmsName() {
		return smsName;
	}
	public void setSmsName(String smsName) {
		this.smsName = smsName;
	}
	
	public String getSmsMessage() {
		return smsMessage;
	}
	public void setSmsMessage(String smsMessage) {
		this.smsMessage = smsMessage;
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
	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	public String getGubunNm() {
		return gubunNm;
	}
	public void setGubunNm(String gubunNm) {
		this.gubunNm = gubunNm;
	}
	public int getSuccCnt() {
		return succCnt;
	}
	public void setSuccCnt(int succCnt) {
		this.succCnt = succCnt;
	}
	public int getFailCnt() {
		return failCnt;
	}
	public void setFailCnt(int failCnt) {
		this.failCnt = failCnt;
	}
	public int getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	public String getAdminYn() {
		return adminYn;
	}
	public void setAdminYn(String adminYn) {
		this.adminYn = adminYn;
	}
	public String getUilang() {
		return uilang;
	}
	public void setUilang(String uilang) {
		this.uilang = uilang;
	}
	public int getCampNo() {
		return campNo;
	}
	public void setCampNo(int campNo) {
		this.campNo = campNo;
	}
	public String getCampNm() {
		return campNm;
	}
	public void setCampNm(String campNm) {
		this.campNm = campNm;
	}
	
	
	public String getSearchCampNm() {
		return searchCampNm;
	}
	public void setSearchCampNm(String searchCampNm) {
		this.searchCampNm = searchCampNm;
	}
	public String getSearchCampTy() {
		return searchCampTy;
	}
	public void setSearchCampTy(String searchCampTy) {
		this.searchCampTy = searchCampTy;
	}
	public String getSearchStatus() {
		return searchStatus;
	}
	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
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
	public int getSearchDeptNo() {
		return searchDeptNo;
	}
	public void setSearchDeptNo(int searchDeptNo) {
		this.searchDeptNo = searchDeptNo;
	}
	public String getSearchUserId() {
		return searchUserId;
	}
	public void setSearchUserId(String searchUserId) {
		this.searchUserId = searchUserId;
	}
}
