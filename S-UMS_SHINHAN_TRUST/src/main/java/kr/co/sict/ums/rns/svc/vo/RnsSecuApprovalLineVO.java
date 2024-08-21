/**
 * 작성자 : 김준희
 * 작성일시 : 2021.11.15
 * 설명 : RNS 실시간 이메일발송결재 VO
 */
package kr.co.sict.ums.rns.svc.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class RnsSecuApprovalLineVO extends CommonVO {
	private int tid;							// 서비스 ID
	private String tnm;			// 주업무명
	private int apprStep;			// 결재단계
	private String apprUserId;		// 결재자아이디
	private String apprUserNm;		// 결재자명
	private String orgCd;			// 조직코드
	private String orgNm;			// 조직명
	private String positionGb;		// 직급
	private String positionNm;		// 직급명
	private String jobGb;			// 직책
	private String jobNm;			// 직책명
	private String apprDt;			// 결재일시
	private String rsltCd;			// 결재결과코드
	private String rsltNm;			// 결재결과명
	private String rejectCd;		// 거절사유코드
	private String rejectNm;		// 거절사유명
	private String regId;			// 등록자아이디
	private String regNm;			// 등록자명
	private String regDt;			// 등록일시
	private String upId;			// 수정자아이디
	private String upNm;			// 수정자명
	private String upDt;			// 수정일시
	
	// 검색
	private String searchStartDt;		// 검색시작일(예약일시)
	private String searchEndDt;			// 검색종료일(예약일시)
	private String searchTaskNm;		// 검색메일명
	private int searchCampNo;			// 검색캠페인번호
	private int searchDeptNo;			// 검색사용자그룹(부서)번호
	private String searchUserId;		// 검색사용자아이디
	private String searchWorkStatus;	// 검색발송상태
	private int searchAprDeptNo;		// 승인예정그룹(부서)번호
	private String searchAprUserId;		// 승인예정자아이디
	
	// 목록
	private String sendDt;				// 예약일시
	private String userId;				// 사용자아이디
	private String userNm;				// 사용자명
	private int campNo;					// 캠페인번호
	private String campNm;				// 캠페인명
	private int segNo;					// 수신자그룹번호
	private String segNm;				// 수신자그룹명
	private String status;				// 상태
	private String statusNm;			// 상태명
	private String workStatus;			// 발송상태
	private String workStatusNm;		// 발송상태명
	
	// 추가정보
	private String uilang;				// 언어권
	private String adminYn;				// 관리자여부
	private String topNotiYn;			// TOP알림여부
	private String secuAttYn;			// 보안첨부여부
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public String getTnm() {
		return tnm;
	}
	public void setTnm(String tnm) {
		this.tnm = tnm;
	}
	public int getApprStep() {
		return apprStep;
	}
	public void setApprStep(int apprStep) {
		this.apprStep = apprStep;
	}
	public String getApprUserId() {
		return apprUserId;
	}
	public void setApprUserId(String apprUserId) {
		this.apprUserId = apprUserId;
	}
	public String getApprUserNm() {
		return apprUserNm;
	}
	public void setApprUserNm(String apprUserNm) {
		this.apprUserNm = apprUserNm;
	}
	public String getOrgCd() {
		return orgCd;
	}
	public void setOrgCd(String orgCd) {
		this.orgCd = orgCd;
	}
	public String getOrgNm() {
		return orgNm;
	}
	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}
	public String getPositionGb() {
		return positionGb;
	}
	public void setPositionGb(String positionGb) {
		this.positionGb = positionGb;
	}
	public String getPositionNm() {
		return positionNm;
	}
	public void setPositionNm(String positionNm) {
		this.positionNm = positionNm;
	}
	public String getJobGb() {
		return jobGb;
	}
	public void setJobGb(String jobGb) {
		this.jobGb = jobGb;
	}
	public String getJobNm() {
		return jobNm;
	}
	public void setJobNm(String jobNm) {
		this.jobNm = jobNm;
	}
	public String getApprDt() {
		return apprDt;
	}
	public void setApprDt(String apprDt) {
		this.apprDt = apprDt;
	}
	public String getRsltCd() {
		return rsltCd;
	}
	public void setRsltCd(String rsltCd) {
		this.rsltCd = rsltCd;
	}
	public String getRsltNm() {
		return rsltNm;
	}
	public void setRsltNm(String rsltNm) {
		this.rsltNm = rsltNm;
	}
	public String getRejectCd() {
		return rejectCd;
	}
	public void setRejectCd(String rejectCd) {
		this.rejectCd = rejectCd;
	}
	public String getRejectNm() {
		return rejectNm;
	}
	public void setRejectNm(String rejectNm) {
		this.rejectNm = rejectNm;
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
	public String getSearchTaskNm() {
		return searchTaskNm;
	}
	public void setSearchTaskNm(String searchTaskNm) {
		this.searchTaskNm = searchTaskNm;
	}
	public int getSearchCampNo() {
		return searchCampNo;
	}
	public void setSearchCampNo(int searchCampNo) {
		this.searchCampNo = searchCampNo;
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
	public String getSearchWorkStatus() {
		return searchWorkStatus;
	}
	public void setSearchWorkStatus(String searchWorkStatus) {
		this.searchWorkStatus = searchWorkStatus;
	}
	public int getSearchAprDeptNo() {
		return searchAprDeptNo;
	}
	public void setSearchAprDeptNo(int searchAprDeptNo) {
		this.searchAprDeptNo = searchAprDeptNo;
	}
	public String getSearchAprUserId() {
		return searchAprUserId;
	}
	public void setSearchAprUserId(String searchAprUserId) {
		this.searchAprUserId = searchAprUserId;
	}
	public String getSendDt() {
		return sendDt;
	}
	public void setSendDt(String sendDt) {
		this.sendDt = sendDt;
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
	public int getSegNo() {
		return segNo;
	}
	public void setSegNo(int segNo) {
		this.segNo = segNo;
	}
	public String getSegNm() {
		return segNm;
	}
	public void setSegNm(String segNm) {
		this.segNm = segNm;
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
	public String getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}
	public String getWorkStatusNm() {
		return workStatusNm;
	}
	public void setWorkStatusNm(String workStatusNm) {
		this.workStatusNm = workStatusNm;
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
	public String getTopNotiYn() {
		return topNotiYn;
	}
	public void setTopNotiYn(String topNotiYn) {
		this.topNotiYn = topNotiYn;
	}
	public String getSecuAttYn() {
		return secuAttYn;
	}
	public void setSecuAttYn(String secuAttYn) {
		this.secuAttYn = secuAttYn;
	}
}
