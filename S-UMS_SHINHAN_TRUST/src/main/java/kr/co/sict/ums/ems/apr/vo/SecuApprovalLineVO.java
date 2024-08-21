/**
 * 작성자 : 김상진
 * 작성일시 : 2021.08.23
 * 설명 : 메일발송결재 VO
 */
package kr.co.sict.ums.ems.apr.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class SecuApprovalLineVO extends CommonVO {
	private int taskNo;				// 주업무번호
	private int tid;				// 주업무번호
	private String taskNm;			// 주업무명
	private int subTaskNo;			// 보조업무번호
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
	private String searchSendRepeat;	// 단기, 정기여부 (000, 001)
	private String searchSvcType;		// 이메일(00, 10), 실시간여부 (20)
	
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
	private String orgKorNm;			// 부서
	private String mktYn;				// 마케팅 동의여부 
	private String contFlPath;			// 파일경로
	private String mailTitle;			// 메일제목
	private String campTy;				 // 캠페인유형
	private String campTyNm;             // 캠페인 타입명
	private String webAgentAttachYn;     //
	private String sendMode;
	private String sendRepeat;			// 발송유형(일회:000,정기:001)
	private String sendRepeatNm;		// 발송유형명
	private String sendTermLoop;		// 정기발송주기
	private String sendTermLoopTy;		// 정기발송주기타입
	private String sendTermLoopTyNm;	// 정기발송주기타입명
	private String sendTermEndDt;		// 정기발송종료일
	private int respLog;				// 수신확인종료일	
	private int deptNo;					// 사용자그룹(부서)번호
	private String deptNm;				// 사용자그룹(부서)명
	private String mailFromNm;			// 발송자명
	private String mailFromEm;			// 발송자이메일
	private String respYn;				// 수신확인여부
	private String mailMktNm;			// 마케팅수신유형명
	
	// 추가정보
	private String uilang;				// 언어권
	private String adminYn;				// 관리자여부
	private String topNotiYn;			// TOP알림여부
	private String secuAttYn;			// 보안첨부여부
	private String svcType;				// 서비스구분 
	private String svcTypeNm;			// 서비스구분명
	public int getTaskNo() {
		return taskNo;
	}
	public void setTaskNo(int taskNo) {
		this.taskNo = taskNo;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public String getTaskNm() {
		return taskNm;
	}
	public void setTaskNm(String taskNm) {
		this.taskNm = taskNm;
	}
	public int getSubTaskNo() {
		return subTaskNo;
	}
	public void setSubTaskNo(int subTaskNo) {
		this.subTaskNo = subTaskNo;
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
	public String getSearchSendRepeat() {
		return searchSendRepeat;
	}
	public void setSearchSendRepeat(String searchSendRepeat) {
		this.searchSendRepeat = searchSendRepeat;
	}
	public String getSearchSvcType() {
		return searchSvcType;
	}
	public void setSearchSvcType(String searchSvcType) {
		this.searchSvcType = searchSvcType;
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
	public String getOrgKorNm() {
		return orgKorNm;
	}
	public void setOrgKorNm(String orgKorNm) {
		this.orgKorNm = orgKorNm;
	}
	public String getMktYn() {
		return mktYn;
	}
	public void setMktYn(String mktYn) {
		this.mktYn = mktYn;
	}
	public String getContFlPath() {
		return contFlPath;
	}
	public void setContFlPath(String contFlPath) {
		this.contFlPath = contFlPath;
	}
	public String getMailTitle() {
		return mailTitle;
	}
	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
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
	public String getWebAgentAttachYn() {
		return webAgentAttachYn;
	}
	public void setWebAgentAttachYn(String webAgentAttachYn) {
		this.webAgentAttachYn = webAgentAttachYn;
	}
	public String getSendMode() {
		return sendMode;
	}
	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}
	public String getSendRepeat() {
		return sendRepeat;
	}
	public void setSendRepeat(String sendRepeat) {
		this.sendRepeat = sendRepeat;
	}
	public String getSendRepeatNm() {
		return sendRepeatNm;
	}
	public void setSendRepeatNm(String sendRepeatNm) {
		this.sendRepeatNm = sendRepeatNm;
	}
	public String getSendTermLoop() {
		return sendTermLoop;
	}
	public void setSendTermLoop(String sendTermLoop) {
		this.sendTermLoop = sendTermLoop;
	}
	public String getSendTermLoopTy() {
		return sendTermLoopTy;
	}
	public void setSendTermLoopTy(String sendTermLoopTy) {
		this.sendTermLoopTy = sendTermLoopTy;
	}
	public String getSendTermLoopTyNm() {
		return sendTermLoopTyNm;
	}
	public void setSendTermLoopTyNm(String sendTermLoopTyNm) {
		this.sendTermLoopTyNm = sendTermLoopTyNm;
	}
	public String getSendTermEndDt() {
		return sendTermEndDt;
	}
	public void setSendTermEndDt(String sendTermEndDt) {
		this.sendTermEndDt = sendTermEndDt;
	}
	public int getRespLog() {
		return respLog;
	}
	public void setRespLog(int respLog) {
		this.respLog = respLog;
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
	public String getMailFromNm() {
		return mailFromNm;
	}
	public void setMailFromNm(String mailFromNm) {
		this.mailFromNm = mailFromNm;
	}
	public String getMailFromEm() {
		return mailFromEm;
	}
	public void setMailFromEm(String mailFromEm) {
		this.mailFromEm = mailFromEm;
	}
	public String getRespYn() {
		return respYn;
	}
	public void setRespYn(String respYn) {
		this.respYn = respYn;
	}
	public String getMailMktNm() {
		return mailMktNm;
	}
	public void setMailMktNm(String mailMktNm) {
		this.mailMktNm = mailMktNm;
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
	public String getSvcType() {
		return svcType;
	}
	public void setSvcType(String svcType) {
		this.svcType = svcType;
	}
	public String getSvcTypeNm() {
		return svcTypeNm;
	}
	public void setSvcTypeNm(String svcTypeNm) {
		this.svcTypeNm = svcTypeNm;
	}
}
