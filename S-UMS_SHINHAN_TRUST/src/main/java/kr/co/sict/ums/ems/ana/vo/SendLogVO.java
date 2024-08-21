/**
 * 작성자 : 김상진
 * 작성일시 : 2021.08.03
 * 설명 : 발송로그 VO
 */
package kr.co.sict.ums.ems.ana.vo;

import java.util.HashMap;
import java.util.List;

import kr.co.sict.ums.com.vo.CommonVO;

public class SendLogVO extends CommonVO {
	private int taskNo;		// 주업무번호
	private String taskNm;	// 주업무명
	private int subTaskNo;	// 보조업무번호
	private String step1;	// 1단계
	private String step2;	// 2단계
	private String step3;	// 3단계
	private String domain;	// 도메인
	
	private int campNo;			// 캠페인번호
	private String campNm;		// 캠페인명
	private String campTy;		// 캠페인목적
	private String campTyNm;	// 캠페인목적명
	private int segNo;			// 수신자그룹번호
	private String segNm;		// 수신자그룹명
	private String custDomain;	// 수신자도메인
	private String custEm;		// 이메일
	private String custId;		// 고객아이디
	private String custNm;		// 이름
	private String day;			// 일
	private String deniedType;	// 
	private int deptNo;			// 사용자그룹(부서)번호
	private String deptNm;		// 사용자그룹명
	private String domainCd;	// 도메인코드
	private String hour;		// 시
	private String month;		// 월
	private String year;		// 년
	private String rcodeStep1;	// 오류코드1
	private String rcodeStep2;	// 오류코드2
	private String rcodeStep3;	// 오류코드3
	private int retryCnt;		// 재발송시점카운트
	private int sendAmt;		// 발송횟수
	private String sendDt;		// 발송날짜
	private String endDt;		// 발송종료날짜
	private String sendMsg;		// 오류메시지
	private String sendRcode;	// 발송결과
	private String openDt;		// 
	private String targetGrpTy;	// 발송대상유형
	private String userId;		// 사용자아이디
	private String userNm;		// 사용자명
	private int attCnt;			// 첨부파일갯수
	
	private String sendTime;	// 
	private int sendCnt;		// 발송수
	private int succCnt;		// 성공수
	private int failCnt;		// 실패수
	private String failDesc;	// 실패사유
	
	private int rtyNo;		// 재발송여부
	// 검색
	private String searchStartDt;
	private String searchEndDt;
	private String searchUserId;
	private String searchUserNm;
	private String searchSendRcode;
	private String searchWorkStatus;
	private String searchSendRepeat;
	private String searchTaskNm;
	private int searchCampNo;
	private String searchCampNm;
	private String searchCampTy;
	private String searchStatus;
	private int searchSegNo;
	private int searchDeptNo;
	private String adminYn;
	private String searchCustId;
	private String searchCustNm;
	private String searchCustEm;
	private String searchKind;
	private String searchServiceGb;
	
	private String taskNos;
	private String subTaskNos;
	private String campNos;
	private String uilang;
	private List<HashMap<String, Integer>> joinList;
	private List<Integer> campList;
	
	// 추가정보
	private String mailFromNm;
	private String mailFromEm;
	private String sendRepeat;
	private String sendRepeatNm;
	private String mailTitle;
	private String sendRcodeNm;
	private String domainNm;
	private String serviceGb;
	private String webAgent;
	private String webAgentTyp;
	private String contFlPath;
	private String respAmt;
	private String bizkey;
	private int taskCount;
	private String campRegDt;
	private String taskRegDt;
	private String statusNm;
	private String workStatus;
	private String workStatusNm;
	private String workStatusDtl;
	private String sendTermStartDt; 
	private String sendTermEndDt;
	private int sendTermLoop;
	private String sendTermLoopTy;
	private String sendTermLoopTyNm;
	private int segRetry;
	private int segReal;
	private String segCreateTy;
	public int getTaskNo() {
		return taskNo;
	}
	public void setTaskNo(int taskNo) {
		this.taskNo = taskNo;
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
	public String getStep1() {
		return step1;
	}
	public void setStep1(String step1) {
		this.step1 = step1;
	}
	public String getStep2() {
		return step2;
	}
	public void setStep2(String step2) {
		this.step2 = step2;
	}
	public String getStep3() {
		return step3;
	}
	public void setStep3(String step3) {
		this.step3 = step3;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
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
	public String getCustDomain() {
		return custDomain;
	}
	public void setCustDomain(String custDomain) {
		this.custDomain = custDomain;
	}
	public String getCustEm() {
		return custEm;
	}
	public void setCustEm(String custEm) {
		this.custEm = custEm;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getCustNm() {
		return custNm;
	}
	public void setCustNm(String custNm) {
		this.custNm = custNm;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getDeniedType() {
		return deniedType;
	}
	public void setDeniedType(String deniedType) {
		this.deniedType = deniedType;
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
	public String getDomainCd() {
		return domainCd;
	}
	public void setDomainCd(String domainCd) {
		this.domainCd = domainCd;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getRcodeStep1() {
		return rcodeStep1;
	}
	public void setRcodeStep1(String rcodeStep1) {
		this.rcodeStep1 = rcodeStep1;
	}
	public String getRcodeStep2() {
		return rcodeStep2;
	}
	public void setRcodeStep2(String rcodeStep2) {
		this.rcodeStep2 = rcodeStep2;
	}
	public String getRcodeStep3() {
		return rcodeStep3;
	}
	public void setRcodeStep3(String rcodeStep3) {
		this.rcodeStep3 = rcodeStep3;
	}
	public int getRetryCnt() {
		return retryCnt;
	}
	public void setRetryCnt(int retryCnt) {
		this.retryCnt = retryCnt;
	}
	public int getSendAmt() {
		return sendAmt;
	}
	public void setSendAmt(int sendAmt) {
		this.sendAmt = sendAmt;
	}
	public String getSendDt() {
		return sendDt;
	}
	public void setSendDt(String sendDt) {
		this.sendDt = sendDt;
	}
	public String getEndDt() {
		return endDt;
	}
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}
	public String getSendMsg() {
		return sendMsg;
	}
	public void setSendMsg(String sendMsg) {
		this.sendMsg = sendMsg;
	}
	public String getSendRcode() {
		return sendRcode;
	}
	public void setSendRcode(String sendRcode) {
		this.sendRcode = sendRcode;
	}
	public String getOpenDt() {
		return openDt;
	}
	public void setOpenDt(String openDt) {
		this.openDt = openDt;
	}
	public String getTargetGrpTy() {
		return targetGrpTy;
	}
	public void setTargetGrpTy(String targetGrpTy) {
		this.targetGrpTy = targetGrpTy;
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
	public int getAttCnt() {
		return attCnt;
	}
	public void setAttCnt(int attCnt) {
		this.attCnt = attCnt;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
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
	public int getFailCnt() {
		return failCnt;
	}
	public void setFailCnt(int failCnt) {
		this.failCnt = failCnt;
	}
	public String getFailDesc() {
		return failDesc;
	}
	public void setFailDesc(String failDesc) {
		this.failDesc = failDesc;
	}
	public int getRtyNo() {
		return rtyNo;
	}
	public void setRtyNo(int rtyNo) {
		this.rtyNo = rtyNo;
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
	public String getSearchSendRcode() {
		return searchSendRcode;
	}
	public void setSearchSendRcode(String searchSendRcode) {
		this.searchSendRcode = searchSendRcode;
	}
	public String getSearchWorkStatus() {
		return searchWorkStatus;
	}
	public void setSearchWorkStatus(String searchWorkStatus) {
		this.searchWorkStatus = searchWorkStatus;
	}
	public String getSearchSendRepeat() {
		return searchSendRepeat;
	}
	public void setSearchSendRepeat(String searchSendRepeat) {
		this.searchSendRepeat = searchSendRepeat;
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
	public int getSearchSegNo() {
		return searchSegNo;
	}
	public void setSearchSegNo(int searchSegNo) {
		this.searchSegNo = searchSegNo;
	}
	public int getSearchDeptNo() {
		return searchDeptNo;
	}
	public void setSearchDeptNo(int searchDeptNo) {
		this.searchDeptNo = searchDeptNo;
	}
	public String getAdminYn() {
		return adminYn;
	}
	public void setAdminYn(String adminYn) {
		this.adminYn = adminYn;
	}
	public String getSearchCustId() {
		return searchCustId;
	}
	public void setSearchCustId(String searchCustId) {
		this.searchCustId = searchCustId;
	}
	public String getSearchCustNm() {
		return searchCustNm;
	}
	public void setSearchCustNm(String searchCustNm) {
		this.searchCustNm = searchCustNm;
	}
	public String getSearchCustEm() {
		return searchCustEm;
	}
	public void setSearchCustEm(String searchCustEm) {
		this.searchCustEm = searchCustEm;
	}
	public String getSearchKind() {
		return searchKind;
	}
	public void setSearchKind(String searchKind) {
		this.searchKind = searchKind;
	}
	public String getSearchServiceGb() {
		return searchServiceGb;
	}
	public void setSearchServiceGb(String searchServiceGb) {
		this.searchServiceGb = searchServiceGb;
	}
	public String getTaskNos() {
		return taskNos;
	}
	public void setTaskNos(String taskNos) {
		this.taskNos = taskNos;
	}
	public String getSubTaskNos() {
		return subTaskNos;
	}
	public void setSubTaskNos(String subTaskNos) {
		this.subTaskNos = subTaskNos;
	}
	public String getCampNos() {
		return campNos;
	}
	public void setCampNos(String campNos) {
		this.campNos = campNos;
	}
	public String getUilang() {
		return uilang;
	}
	public void setUilang(String uilang) {
		this.uilang = uilang;
	}
	public List<HashMap<String, Integer>> getJoinList() {
		return joinList;
	}
	public void setJoinList(List<HashMap<String, Integer>> joinList) {
		this.joinList = joinList;
	}
	public List<Integer> getCampList() {
		return campList;
	}
	public void setCampList(List<Integer> campList) {
		this.campList = campList;
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
	public String getMailTitle() {
		return mailTitle;
	}
	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}
	public String getSendRcodeNm() {
		return sendRcodeNm;
	}
	public void setSendRcodeNm(String sendRcodeNm) {
		this.sendRcodeNm = sendRcodeNm;
	}
	public String getDomainNm() {
		return domainNm;
	}
	public void setDomainNm(String domainNm) {
		this.domainNm = domainNm;
	}
	public String getServiceGb() {
		return serviceGb;
	}
	public void setServiceGb(String serviceGb) {
		this.serviceGb = serviceGb;
	}
	public String getWebAgent() {
		return webAgent;
	}
	public void setWebAgent(String webAgent) {
		this.webAgent = webAgent;
	}
	public String getWebAgentTyp() {
		return webAgentTyp;
	}
	public void setWebAgentTyp(String webAgentTyp) {
		this.webAgentTyp = webAgentTyp;
	}
	public String getContFlPath() {
		return contFlPath;
	}
	public void setContFlPath(String contFlPath) {
		this.contFlPath = contFlPath;
	}
	public String getRespAmt() {
		return respAmt;
	}
	public void setRespAmt(String respAmt) {
		this.respAmt = respAmt;
	}
	public String getBizkey() {
		return bizkey;
	}
	public void setBizkey(String bizkey) {
		this.bizkey = bizkey;
	}
	public int getTaskCount() {
		return taskCount;
	}
	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}
	public String getCampRegDt() {
		return campRegDt;
	}
	public void setCampRegDt(String campRegDt) {
		this.campRegDt = campRegDt;
	}
	public String getTaskRegDt() {
		return taskRegDt;
	}
	public void setTaskRegDt(String taskRegDt) {
		this.taskRegDt = taskRegDt;
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
	public String getWorkStatusDtl() {
		return workStatusDtl;
	}
	public void setWorkStatusDtl(String workStatusDtl) {
		this.workStatusDtl = workStatusDtl;
	}
	public String getSendTermStartDt() {
		return sendTermStartDt;
	}
	public void setSendTermStartDt(String sendTermStartDt) {
		this.sendTermStartDt = sendTermStartDt;
	}
	public String getSendTermEndDt() {
		return sendTermEndDt;
	}
	public void setSendTermEndDt(String sendTermEndDt) {
		this.sendTermEndDt = sendTermEndDt;
	}
	public int getSendTermLoop() {
		return sendTermLoop;
	}
	public void setSendTermLoop(int sendTermLoop) {
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
	public int getSegRetry() {
		return segRetry;
	}
	public void setSegRetry(int segRetry) {
		this.segRetry = segRetry;
	}
	public int getSegReal() {
		return segReal;
	}
	public void setSegReal(int segReal) {
		this.segReal = segReal;
	}
	public String getSegCreateTy() {
		return segCreateTy;
	}
	public void setSegCreateTy(String segCreateTy) {
		this.segCreateTy = segCreateTy;
	}
}