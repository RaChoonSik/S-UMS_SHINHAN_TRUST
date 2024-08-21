/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.22
 * 설명 : Campaign VO
 */
package kr.co.sict.ums.ems.cam.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class CampaignVO extends CommonVO {
	private int campNo;			// 캠페인 등록번호
	private String campNm;		// 캠페인명
	private String campTy;		// 캠페인유형
	private String campTyNm;	// 캠페인유형명
	private String campDesc;	// 캠페인설명
	private String status;		// 상태
	private String statusNm;	// 상태명
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
	private String eaiCampNo;	// EAI캠페인번호
	private String requestKey;  // API 요청 키 
	// 검색
	private String searchCampNm;	// 캠페인명 검색
	private String searchCampTy;	// 캠페인목적 검색
	private String searchStatus;	// 상태 검색
	private String searchStartDt;	// 검색 시작일
	private String searchEndDt;		// 검색 종료일
	private int searchDeptNo;		// 사용자그룹 검색
	private String searchUserId;	// 사용자 검색
	private String searchServiceGb; // 검색 서비스 구분 : EMS캠페인과 RNS 서비스구분
	
	//추가정보
	private int campMailCnt;		// 캠페인발송메일수
	private String uilang;			// 언어권
	private String adminYn;			// 관리자여부
	private String campNos;			// 캠페인번호멀티선택용
	private String serviceGb;		// 서비스 구분 : EMS캠페인과 RNS 서비스구분
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
	public String getCampTy() {
		return campTy;
	}
	public void setCampTy(String campTy) {
		this.campTy = campTy;
	}
	public String getCampTyNm() {
		return campTyNm;
	}
	public void setCampTyNm(String campTyNm) {
		this.campTyNm = campTyNm;
	}
	public String getCampDesc() {
		return campDesc;
	}
	public void setCampDesc(String campDesc) {
		this.campDesc = campDesc;
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
	public String getEaiCampNo() {
		return eaiCampNo;
	}
	public void setEaiCampNo(String eaiCampNo) {
		this.eaiCampNo = eaiCampNo;
	}
	public String getRequestKey() {
		return requestKey;
	}
	public void setRequestKey(String requestKey) {
		this.requestKey = requestKey;
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
	public String getSearchServiceGb() {
		return searchServiceGb;
	}
	public void setSearchServiceGb(String searchServiceGb) {
		this.searchServiceGb = searchServiceGb;
	}
	public int getCampMailCnt() {
		return campMailCnt;
	}
	public void setCampMailCnt(int campMailCnt) {
		this.campMailCnt = campMailCnt;
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
	public String getCampNos() {
		return campNos;
	}
	public void setCampNos(String campNos) {
		this.campNos = campNos;
	}
	public String getServiceGb() {
		return serviceGb;
	}
	public void setServiceGb(String serviceGb) {
		this.serviceGb = serviceGb;
	}
	

}
