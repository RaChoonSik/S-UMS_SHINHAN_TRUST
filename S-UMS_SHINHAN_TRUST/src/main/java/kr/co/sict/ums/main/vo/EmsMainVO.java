/**
 * 작성자 : 박찬용
 * 작성일시 : 2022.01.26
 * 설명 : 이메일 시스템 메인 VO
 */

package kr.co.sict.ums.main.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class EmsMainVO extends CommonVO {
	private String searchYmd;		// 조회년월일(yyyymmdd)
	private String searchYm;		// 조회년월(yyyymm)
	private String userId;			// 사용자ID
	private String userNm;			// 사용자이름
	private String orgCd;			// 조직코드
	private String orgKorNm;		// 한글조직명
	private String jobGb;			// 직책코드
	private String jobNm;			// 직책코드명		
	private int cnt202;				// 결제진행
	private int cnt203;				// 결제반려
	private int cnt204;				// 결제완료
	private int cntTot;				// 총발송
	private int cntSucc;			// 발송성공
	private int cntFail;			// 발송실패
	private int opnTot;				// 수신응답
	private String sendRcode;		// 대분류
	private String sendRcodeNm;		// 대분류명
	private int cntErr;				// 오류건수
	private String custDomain;		// 도메인명
	private int campNo;				// 캠페인번호	
	private String campNm;			// 캠페인명
	private String times;			// 시간대
	private int cntTask;			// 발송메일수
	private String days;			// 일자별 	
	private String sendHh24mi;		//발송일시      
	private String taskNm;			//메일명        
	private String sendMode;		//메일유형      
	private String sendModeNm;		//메일유형명    	      
	private String segNo;			//수신자그룹코드
	private String segNm;			//수신자그룹명  	      
	private String workStatusNm;	//발송상태      
	private String compliancdYn;	//준법심의      
	private String workStatus;		//발송상태코드  
	private String taskNo;			//TASK_NO       
	private String subTaskNo;		//SUB_TASK_NO   
	private int mindate202;			//결재진행최소일자
	private int maxdate202;			//결재진행최종일자
	private int mindate2034;		//결재반려/완료 최소일자
	private int maxdate2034;		//결재반려/완료 최종일자
	private String searchPf;		//이전,앞	
	private int serviceGb;			//메뉴 1dept 코드
	private String cdNm;			//메뉴 1dept 코드명
	private String pmenuId;			//메뉴 2dept 코드
	private String pmenuNm;			//메뉴 2dept 코드명
	private String menuId;			//메뉴 3dept 코드
	private String menuNm;			//메뉴 3dept 코드명
	private String sourcePath;		//메뉴 URL
	private String qmenuYn;			//퀵메뉴 여부
	private String searchBoardDt;	//발송일정 날짜
	private String searchBoardPf;	//발송일정 이전 다음
	private String[] arrDelQmenu; 	// 일괄적용 퀵메뉴
	private int deptNo;				//사용자 그룹
	private String approvalProcAppYn;
	private String uilang;			// 언어권
	private String adminYn;			// 관리자여부
	
	public String getSearchYmd() {
		return searchYmd;
	}
	public void setSearchYmd(String searchYmd) {
		this.searchYmd = searchYmd;
	}
	public String getSearchYm() {
		return searchYm;
	}
	public void setSearchYm(String searchYm) {
		this.searchYm = searchYm;
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
	public String getOrgCd() {
		return orgCd;
	}
	public void setOrgCd(String orgCd) {
		this.orgCd = orgCd;
	}
	public String getOrgKorNm() {
		return orgKorNm;
	}
	public void setOrgKorNm(String orgKorNm) {
		this.orgKorNm = orgKorNm;
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
	public int getCnt202() {
		return cnt202;
	}
	public void setCnt202(int cnt202) {
		this.cnt202 = cnt202;
	}
	public int getCnt203() {
		return cnt203;
	}
	public void setCnt203(int cnt203) {
		this.cnt203 = cnt203;
	}
	public int getCnt204() {
		return cnt204;
	}
	public void setCnt204(int cnt204) {
		this.cnt204 = cnt204;
	}
	public int getCntTot() {
		return cntTot;
	}
	public void setCntTot(int cntTot) {
		this.cntTot = cntTot;
	}
	public int getCntSucc() {
		return cntSucc;
	}
	public void setCntSucc(int cntSucc) {
		this.cntSucc = cntSucc;
	}
	public int getCntFail() {
		return cntFail;
	}
	public void setCntFail(int cntFail) {
		this.cntFail = cntFail;
	}
	public int getOpnTot() {
		return opnTot;
	}
	public void setOpnTot(int opnTot) {
		this.opnTot = opnTot;
	}
	public String getSendRcode() {
		return sendRcode;
	}
	public void setSendRcode(String sendRcode) {
		this.sendRcode = sendRcode;
	}
	public String getSendRcodeNm() {
		return sendRcodeNm;
	}
	public void setSendRcodeNm(String sendRcodeNm) {
		this.sendRcodeNm = sendRcodeNm;
	}
	public int getCntErr() {
		return cntErr;
	}
	public void setCntErr(int cntErr) {
		this.cntErr = cntErr;
	}
	public String getCustDomain() {
		return custDomain;
	}
	public void setCustDomain(String custDomain) {
		this.custDomain = custDomain;
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
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	public int getCntTask() {
		return cntTask;
	}
	public void setCntTask(int cntTask) {
		this.cntTask = cntTask;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getSendHh24mi() {
		return sendHh24mi;
	}
	public void setSendHh24mi(String sendHh24mi) {
		this.sendHh24mi = sendHh24mi;
	}
	public String getTaskNm() {
		return taskNm;
	}
	public void setTaskNm(String taskNm) {
		this.taskNm = taskNm;
	}
	public String getSendMode() {
		return sendMode;
	}
	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}
	public String getSendModeNm() {
		return sendModeNm;
	}
	public void setSendModeNm(String sendModeNm) {
		this.sendModeNm = sendModeNm;
	}
	public String getSegNo() {
		return segNo;
	}
	public void setSegNo(String segNo) {
		this.segNo = segNo;
	}
	public String getSegNm() {
		return segNm;
	}
	public void setSegNm(String segNm) {
		this.segNm = segNm;
	}
	public String getWorkStatusNm() {
		return workStatusNm;
	}
	public void setWorkStatusNm(String workStatusNm) {
		this.workStatusNm = workStatusNm;
	}
	public String getCompliancdYn() {
		return compliancdYn;
	}
	public void setCompliancdYn(String compliancdYn) {
		this.compliancdYn = compliancdYn;
	}
	public String getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}
	public String getTaskNo() {
		return taskNo;
	}
	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}
	public String getSubTaskNo() {
		return subTaskNo;
	}
	public void setSubTaskNo(String subTaskNo) {
		this.subTaskNo = subTaskNo;
	}
	public int getMindate202() {
		return mindate202;
	}
	public void setMindate202(int mindate202) {
		this.mindate202 = mindate202;
	}
	public int getMaxdate202() {
		return maxdate202;
	}
	public void setMaxdate202(int maxdate202) {
		this.maxdate202 = maxdate202;
	}
	public int getMindate2034() {
		return mindate2034;
	}
	public void setMindate2034(int mindate2034) {
		this.mindate2034 = mindate2034;
	}
	public int getMaxdate2034() {
		return maxdate2034;
	}
	public void setMaxdate2034(int maxdate2034) {
		this.maxdate2034 = maxdate2034;
	}
	public String getSearchPf() {
		return searchPf;
	}
	public void setSearchPf(String searchPf) {
		this.searchPf = searchPf;
	}
	public int getServiceGb() {
		return serviceGb;
	}
	public void setServiceGb(int serviceGb) {
		this.serviceGb = serviceGb;
	}
	public String getCdNm() {
		return cdNm;
	}
	public void setCdNm(String cdNm) {
		this.cdNm = cdNm;
	}
	public String getPmenuId() {
		return pmenuId;
	}
	public void setPmenuId(String pmenuId) {
		this.pmenuId = pmenuId;
	}
	public String getPmenuNm() {
		return pmenuNm;
	}
	public void setPmenuNm(String pmenuNm) {
		this.pmenuNm = pmenuNm;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getMenuNm() {
		return menuNm;
	}
	public void setMenuNm(String menuNm) {
		this.menuNm = menuNm;
	}
	public String getSourcePath() {
		return sourcePath;
	}
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
	public String getQmenuYn() {
		return qmenuYn;
	}
	public void setQmenuYn(String qmenuYn) {
		this.qmenuYn = qmenuYn;
	}
	public String getSearchBoardDt() {
		return searchBoardDt;
	}
	public void setSearchBoardDt(String searchBoardDt) {
		this.searchBoardDt = searchBoardDt;
	}
	public String getSearchBoardPf() {
		return searchBoardPf;
	}
	public void setSearchBoardPf(String searchBoardPf) {
		this.searchBoardPf = searchBoardPf;
	}
	public String[] getArrDelQmenu() {
		return arrDelQmenu;
	}
	public void setArrDelQmenu(String[] arrDelQmenu) {
		this.arrDelQmenu = arrDelQmenu;
	}
	public int getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(int deptNo) {
		this.deptNo = deptNo;
	}
	public String getApprovalProcAppYn() {
		return approvalProcAppYn;
	}
	public void setApprovalProcAppYn(String approvalProcAppYn) {
		this.approvalProcAppYn = approvalProcAppYn;
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
