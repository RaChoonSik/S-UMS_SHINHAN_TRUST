/**
 * 작성자 : 김상진
 * 작성일시 : 2021.09.01
 * 설명 : RNS 서비스 관리 VO
 */
package kr.co.sict.ums.rns.svc.vo;

import java.util.Date;

import kr.co.sict.ums.com.vo.CommonVO;

public class RnsServiceVO extends CommonVO {
	private int tid;				// 서비스ID
	private String tnm;				// 서비스명
	private String tdesc;			// 비고
	private String sname;			// 발송자명
	private String smail;			// 발송자이메일
	private String sid;				// 발송자ID
	private String emailSubject;	// 이메일제목
	private String contentsPath;	// 컨텐츠경로
	private String contentsTyp;		// 컨텐츠타입
	private String contentsTypNm;	// 컨텐츠타입명
	private int attchCnt;			// 첨부파일수
	private String recvChkYn;		// 수신확인여부
	private String userId;			// 사용자ID
	private String userNm;			// 사용자명
	private int deptNo;				// 사용자그룹
	private String deptNm;			// 사용자그룹명
	private String useYn;			// 사용여부
	private String status;			// 상태
	private String statusNm;		// 상태명
	private String eaiCampNo;		// EAI 번호
	private String workStatus;		// 발송상태
	private String workStatusNm;	// 발송상태명
	private String regId;			// 생성자ID
	private String regNm;			// 생성자명
	private String regDt;			// 생성일시
	private String upId;			// 수정자ID
	private String upNm;			// 수정자명
	private String upDt;			// 수정일시
	
	// 추가정보
	private String orgKorNm;			// 부서
	private String webAgentUrl;			// 웹에이전트URL
	private String webAgentAttachYn;	// 웹에이전트첨부여부
	private String secuAttTyp;			// 웹에이전트첨부유형
	private String serviceContent;		// 메일내용
	private String attachNm;			// 첨부파일명
	private String attachPath;			// 첨부파일경로
	private String tids;				// 서비스ID멀티선택
	private Date sdate;					// 발송일시
	private long mid;					// 메세지ID
	private int succCnt;				// 성공건수
	private int failCnt;				// 실패건수
	
	//결재정보 추가 
	private String apprUserId;			// 결재자ID
	private String approvalLineYn;		// 발송결재라인해당여부
	private String approvalProcYn;		// 발송결재라인진행여부
	private String approvalProcAppYn;	// 발송결재라인실승인여부	
	// 검색
	private String searchStartDt;		// 검색시작일
	private String searchEndDt;			// 검색종료일
	private String searchTnm;			// 검색서비스명
	private int searchDeptNo;			// 검색사용자그룹
	private String searchUserId;		// 검색사용자ID
	private String searchContentsTyp;	// 검색컨텐츠타입
	private String searchStatus;		// 검색상태
	private String uilang;				// 언어권
	private String adminYn;				// 관리자여부
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
	public String getTdesc() {
		return tdesc;
	}
	public void setTdesc(String tdesc) {
		this.tdesc = tdesc;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getSmail() {
		return smail;
	}
	public void setSmail(String smail) {
		this.smail = smail;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getEmailSubject() {
		return emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
	public String getContentsPath() {
		return contentsPath;
	}
	public void setContentsPath(String contentsPath) {
		this.contentsPath = contentsPath;
	}
	public String getContentsTyp() {
		return contentsTyp;
	}
	public void setContentsTyp(String contentsTyp) {
		this.contentsTyp = contentsTyp;
	}
	public String getContentsTypNm() {
		return contentsTypNm;
	}
	public void setContentsTypNm(String contentsTypNm) {
		this.contentsTypNm = contentsTypNm;
	}
	public int getAttchCnt() {
		return attchCnt;
	}
	public void setAttchCnt(int attchCnt) {
		this.attchCnt = attchCnt;
	}
	public String getRecvChkYn() {
		return recvChkYn;
	}
	public void setRecvChkYn(String recvChkYn) {
		this.recvChkYn = recvChkYn;
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
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
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
	public String getEaiCampNo() {
		return eaiCampNo;
	}
	public void setEaiCampNo(String eaiCampNo) {
		this.eaiCampNo = eaiCampNo;
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
	public String getOrgKorNm() {
		return orgKorNm;
	}
	public void setOrgKorNm(String orgKorNm) {
		this.orgKorNm = orgKorNm;
	}
	public String getWebAgentUrl() {
		return webAgentUrl;
	}
	public void setWebAgentUrl(String webAgentUrl) {
		this.webAgentUrl = webAgentUrl;
	}
	public String getWebAgentAttachYn() {
		return webAgentAttachYn;
	}
	public void setWebAgentAttachYn(String webAgentAttachYn) {
		this.webAgentAttachYn = webAgentAttachYn;
	}
	public String getSecuAttTyp() {
		return secuAttTyp;
	}
	public void setSecuAttTyp(String secuAttTyp) {
		this.secuAttTyp = secuAttTyp;
	}
	public String getServiceContent() {
		return serviceContent;
	}
	public void setServiceContent(String serviceContent) {
		this.serviceContent = serviceContent;
	}
	public String getAttachNm() {
		return attachNm;
	}
	public void setAttachNm(String attachNm) {
		this.attachNm = attachNm;
	}
	public String getAttachPath() {
		return attachPath;
	}
	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}
	public String getTids() {
		return tids;
	}
	public void setTids(String tids) {
		this.tids = tids;
	}
	public Date getSdate() {
		return sdate;
	}
	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}
	public long getMid() {
		return mid;
	}
	public void setMid(long mid) {
		this.mid = mid;
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
	public String getApprUserId() {
		return apprUserId;
	}
	public void setApprUserId(String apprUserId) {
		this.apprUserId = apprUserId;
	}
	public String getApprovalLineYn() {
		return approvalLineYn;
	}
	public void setApprovalLineYn(String approvalLineYn) {
		this.approvalLineYn = approvalLineYn;
	}
	public String getApprovalProcYn() {
		return approvalProcYn;
	}
	public void setApprovalProcYn(String approvalProcYn) {
		this.approvalProcYn = approvalProcYn;
	}
	public String getApprovalProcAppYn() {
		return approvalProcAppYn;
	}
	public void setApprovalProcAppYn(String approvalProcAppYn) {
		this.approvalProcAppYn = approvalProcAppYn;
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
	public String getSearchTnm() {
		return searchTnm;
	}
	public void setSearchTnm(String searchTnm) {
		this.searchTnm = searchTnm;
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
	public String getSearchContentsTyp() {
		return searchContentsTyp;
	}
	public void setSearchContentsTyp(String searchContentsTyp) {
		this.searchContentsTyp = searchContentsTyp;
	}
	public String getSearchStatus() {
		return searchStatus;
	}
	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
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
	
}
