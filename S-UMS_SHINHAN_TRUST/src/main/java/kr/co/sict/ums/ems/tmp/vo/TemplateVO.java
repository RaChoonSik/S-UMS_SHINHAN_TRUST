/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.26
 * 설명 : 템플릿 관리 VO
 */
package kr.co.sict.ums.ems.tmp.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class TemplateVO extends CommonVO {
	private int tempNo;			// 템플릿 등록번호
	private String tempNm;		// 템플릿명
	private String tempDesc;	// 템플릿설명
	private String tempFlPath;	// 템플릿파일경로
	private String status;		// 상태
	private String statusNm;	// 상태명
	private String channel;		// 채널
	private int deptNo;			// 부서번호
	private String deptNm;		// 부서명
	private String userId;		// 사용자아이디
	private String userNm;		// 사용자명
	private String regId;		// 등록자
	private String regNm;		// 등록자명
	private String regDt;		// 등록일자
	private String upId;		// 수정자
	private String upNm;		// 수정자명
	private String upDt;		// 수정일자
	
	//검색
	private String searchTempNm;	// 템플릿명 검색
	private String searchStatus;	// 상태 검색
	private String searchStartDt;	// 검색시작일
	private String searchEndDt;		// 검색종료일
	private int searchDeptNo;		// 부서번호 검색
	private String searchUserId;	// 사용자아이디 검색
	private String searchChannel;	// 채널 검색
	private String uilang;			// 언어권
	private String adminYn;			// 관리자여부
	
	// 추가정보
	private String tempVal;			// 템플릿 작성내용		
	
	public int getTempNo() {
		return tempNo;
	}
	public void setTempNo(int tempNo) {
		this.tempNo = tempNo;
	}
	public String getTempNm() {
		return tempNm;
	}
	public void setTempNm(String tempNm) {
		this.tempNm = tempNm;
	}
	public String getTempDesc() {
		return tempDesc;
	}
	public void setTempDesc(String tempDesc) {
		this.tempDesc = tempDesc;
	}
	public String getTempFlPath() {
		return tempFlPath;
	}
	public void setTempFlPath(String tempFlPath) {
		this.tempFlPath = tempFlPath;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusNm() {
		return statusNm;
	}
	public void setStatusNm(String statusNm) {
		this.statusNm = statusNm;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
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
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public String getRegNm() {
		return regNm;
	}
	public void setRegNm(String regNm) {
		this.regNm = regNm;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	public String getUpId() {
		return upId;
	}
	public void setUpId(String upId) {
		this.upId = upId;
	}
	public String getUpNm() {
		return upNm;
	}
	public void setUpNm(String upNm) {
		this.upNm = upNm;
	}
	public String getUpDt() {
		return upDt;
	}
	public void setUpDt(String upDt) {
		this.upDt = upDt;
	}
	public String getSearchTempNm() {
		return searchTempNm;
	}
	public void setSearchTempNm(String searchTempNm) {
		this.searchTempNm = searchTempNm;
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
	public String getSearchChannel() {
		return searchChannel;
	}
	public void setSearchChannel(String searchChannel) {
		this.searchChannel = searchChannel;
	}
	public String getUilang() {
		return uilang;
	}
	public void setUilang(String uilang) {
		this.uilang = uilang;
	}
	public String getAdminYn() {
		return adminYn;
	}
	public void setAdminYn(String adminYn) {
		this.adminYn = adminYn;
	}
	public String getTempVal() {
		return tempVal;
	}
	public void setTempVal(String tempVal) {
		this.tempVal = tempVal;
	}
}
