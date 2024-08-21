/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.22
 * 설명 : 메일 Task VO
 */
package kr.co.sict.ums.ems.cam.vo;

import java.util.List;

import kr.co.sict.ums.com.vo.CommonVO;

public class TaskVO extends CommonVO {
	private int taskNo;				// 주업무번호
	private String taskNm;			// 주업무명
	private int subTaskNo;			// 보조업무번호
	private String mailTitle;		// 메일제목
	private String mailFromNm;		// 발송자명
	private String mailFromEm;		// 발송자이메일
	private String replyToEm;		// 회신이메일
	private String returnEm;		// 리턴이메일
	private String exeUserId;		// 실행담당자아이디
	private int deptNo;				// 사용자그룹(부서)번호
	private String deptNm;			// 사용자그룹(부서)명
	private String userId;			// 사용자아이디
	private String userNm;			// 사용자명
	private int attCnt;				// 첨부파일건수
	private String sendDt;			// 예약일시
	private String endDt;			// 발송종료시간
	private String sendRepeat;		// 발송유형(일회:000,정기:001)
	private String sendRepeatNm;	// 발송유형명
	private String sendTermLoop;	// 정기발송주기
	private String sendTermLoopTy;	// 정기발송주기타입
	private String sendTermLoopTyNm;	// 정기발송주기타입명
	private String sendTermEndDt;	// 정기발송종료일
	private String workStatus;		// 발송상태
	private String workStatusNm;	// 발송상태명
	private String workStatusDtl;	// 발송상태상세
	private int respLog;			// 수신확인종료일
	private int socketTimeout;		// 소켓연결시간
	private int connPerCnt;			// 커넥션당발송수
	private int retryCnt;			// 재발송수
	private String sendMode;		// 발송모드(가상발송,실발송)
	private String headerEnc;		// 헤더인코딩
	private String bodyEnc;			// 바디인코딩
	private String charset;			// 문자셋
	private int campNo;				// 캠페인번호
	private String campNm;			// 캠페인명
	private String campTy;			// 캠페인유형
	private String campTyNm;		// 캠페인유형명
	private String status;			// 상태(주업무)
	private String statusNm;		// 상태명(주업무)
	private String subStatus;		// 상태(보조업무)
	private String subStatusNm;		// 상태명(보조업무)
	private int sendUnitCost;		// 발송단가
	private String contFlPath;		// 컨텐츠경로
	private String contTy;			// 컨텐츠유형
	private String planUserId;		// 기획담당자아이디
	private String channel;			// 채널
	private String recoStatus;		// 승인여부
	private String idMerge;			// 
	private String nmMerge;			// 수신자머지키
	private String respYn;			// 수신확인여부
	private String linkYn;			// 링크여부
	private int surveyno;			// 설문번호
	private String targetGrpTy;		// 발송대상유형
	private String regId;			// 등록자아이디
	private String regDt;			// 등록일시
	private String upId;			// 수정자아이디
	private String upDt;			// 수정일시
	
	private String sendYmd;			// 발송일자년월일
	private String sendHour;		// 발송일자시간
	private String sendMin;			// 발송일자분
	private String isSendTerm;		// 단기/정기 구분
	private String respEndDt;		// 응답종료날짜
	private String sendTestYn;		// 테스트발송여부
	private String sendTestEm;		// 테스트수신이메일
	private int sendTestTaskNo;		// 테스트주업무번호
	private int sendTestSubTaskNo;	// 테스트보조업무전호
	private String sendIp;			// 발송IP번호
	private String testSendId;		// 발송IP번호
	
	private String rtyTyp;			//재발송유형
	private int rtyTaskNo;			//재발송캠페인업무정의등록번호
	private int rtySubTaskNo;		//재발송보조업무번호
	private String rtyCode;			//재발송대상코드 
	private String rtyCodes;		//재발송대상코드(대용량 시리즈)
	
	private int dbConnNo;			// DB번호
	private int segNo;				// 수신자그룹(발송대상)번호
	private String segNoc;			// 번호가 캐릭터로 사용되는경우 segno + mergekey
	private String segNm;			// 수신자그룹명
	private String selectSql;		// SELECT
	private String fromSql;			// FROM
	private String whereSql;		// WHERE
	private String oderbySql;		// ORDER BY
	private String mergeKey;		// 머지키
	private String mergeCol;		// 머지컬럼
	private String segFlPath;		// 수신자그룹 파일
	private String segCreateTy; 	// 수신자그룹 성유형	

	//첨부파일
	private String attachNm;		// 첨부파일명
	private String attachPath;		// 첨부파일경로
	
	// 추가정보
	private int searchDeptNo;					// 검색사용자그룹번호
	private String searchUserId;				// 검색사용자아이디
	private String searchStartDt;				// 검색시작일
	private String searchEndDt;					// 검색종료일
	private String searchTaskNm;				// 검색메일명
	private int searchCampNo;					// 검색캠페인번호
	private int searchSegNo;					// 검색수신자그룹번호
	private String searchSendRepeat;			// 검색단기/정기구분
	private String searchStatus;				// 검색상태
	private String searchWorkStatus;			// 검색발송상태
	private List<String> searchWorkStatusList;	// 검색발송상태목록
	private String adminYn;						// 관리자여부
	private String uilang;						// 언어권
	private String composerValue;				// 메일작성내용
	private String campInfo;					// 캠페인정보
	private int testCnt;						// 테스트건수
	private String taskNos;						// 주업무번호멀티선택
	private String subTaskNos;					// 보조업무번호멀티선택
	private int mailCnt;						// 등록메일건수
	private int totCnt;							// 총건수
	private int sucCnt;							// 성공건수
	private int failCnt;						// 실패건수
	private int apprCnt;
	
	private String approvalLineYn;		// 발송결재라인해당여부
	private String approvalProcYn;		// 발송결재라인진행여부
	private String approvalProcAppYn;	// 발송결재라인실승인여부
	private String webAgentUrl;			// 웹에이전트URL
	private String webAgentAttachYn;	// 웹에이전트첨부여부
	private String secuAttTyp;			// 웹에이전트첨부유형
	private String sndTpeGb;			// 발송결과유형구분(발송결과종별)
	private String mailMktGb;			// 마케팅수신유형
	private String mailMktNm;			// 마케팅수신유형명
	private String apprUserId;			// 결재자ID
	
	//재전송위한 추가정보
	private String bizkey;				// 재발송정보 처리하기 위한 BizKey
	private String custId;				// 재발송정보 처리하기 위한 고객ID
	private String custEm;				// 재발송정보 처리하기 위한 고객이메일
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
	public String getMailTitle() {
		return mailTitle;
	}
	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
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
	public String getReplyToEm() {
		return replyToEm;
	}
	public void setReplyToEm(String replyToEm) {
		this.replyToEm = replyToEm;
	}
	public String getReturnEm() {
		return returnEm;
	}
	public void setReturnEm(String returnEm) {
		this.returnEm = returnEm;
	}
	public String getExeUserId() {
		return exeUserId;
	}
	public void setExeUserId(String exeUserId) {
		this.exeUserId = exeUserId;
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
	public int getAttCnt() {
		return attCnt;
	}
	public void setAttCnt(int attCnt) {
		this.attCnt = attCnt;
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
	public int getRespLog() {
		return respLog;
	}
	public void setRespLog(int respLog) {
		this.respLog = respLog;
	}
	public int getSocketTimeout() {
		return socketTimeout;
	}
	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
	public int getConnPerCnt() {
		return connPerCnt;
	}
	public void setConnPerCnt(int connPerCnt) {
		this.connPerCnt = connPerCnt;
	}
	public int getRetryCnt() {
		return retryCnt;
	}
	public void setRetryCnt(int retryCnt) {
		this.retryCnt = retryCnt;
	}
	public String getSendMode() {
		return sendMode;
	}
	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}
	public String getHeaderEnc() {
		return headerEnc;
	}
	public void setHeaderEnc(String headerEnc) {
		this.headerEnc = headerEnc;
	}
	public String getBodyEnc() {
		return bodyEnc;
	}
	public void setBodyEnc(String bodyEnc) {
		this.bodyEnc = bodyEnc;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
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
	public String getSubStatus() {
		return subStatus;
	}
	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}
	public String getSubStatusNm() {
		return subStatusNm;
	}
	public void setSubStatusNm(String subStatusNm) {
		this.subStatusNm = subStatusNm;
	}
	public int getSendUnitCost() {
		return sendUnitCost;
	}
	public void setSendUnitCost(int sendUnitCost) {
		this.sendUnitCost = sendUnitCost;
	}
	public String getContFlPath() {
		return contFlPath;
	}
	public void setContFlPath(String contFlPath) {
		this.contFlPath = contFlPath;
	}
	public String getContTy() {
		return contTy;
	}
	public void setContTy(String contTy) {
		this.contTy = contTy;
	}
	public String getPlanUserId() {
		return planUserId;
	}
	public void setPlanUserId(String planUserId) {
		this.planUserId = planUserId;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getRecoStatus() {
		return recoStatus;
	}
	public void setRecoStatus(String recoStatus) {
		this.recoStatus = recoStatus;
	}
	public String getIdMerge() {
		return idMerge;
	}
	public void setIdMerge(String idMerge) {
		this.idMerge = idMerge;
	}
	public String getNmMerge() {
		return nmMerge;
	}
	public void setNmMerge(String nmMerge) {
		this.nmMerge = nmMerge;
	}
	public String getRespYn() {
		return respYn;
	}
	public void setRespYn(String respYn) {
		this.respYn = respYn;
	}
	public String getLinkYn() {
		return linkYn;
	}
	public void setLinkYn(String linkYn) {
		this.linkYn = linkYn;
	}
	public int getSurveyno() {
		return surveyno;
	}
	public void setSurveyno(int surveyno) {
		this.surveyno = surveyno;
	}
	public String getTargetGrpTy() {
		return targetGrpTy;
	}
	public void setTargetGrpTy(String targetGrpTy) {
		this.targetGrpTy = targetGrpTy;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
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
	public String getUpDt() {
		return upDt;
	}
	public void setUpDt(String upDt) {
		this.upDt = upDt;
	}
	public String getSendYmd() {
		return sendYmd;
	}
	public void setSendYmd(String sendYmd) {
		this.sendYmd = sendYmd;
	}
	public String getSendHour() {
		return sendHour;
	}
	public void setSendHour(String sendHour) {
		this.sendHour = sendHour;
	}
	public String getSendMin() {
		return sendMin;
	}
	public void setSendMin(String sendMin) {
		this.sendMin = sendMin;
	}
	public String getIsSendTerm() {
		return isSendTerm;
	}
	public void setIsSendTerm(String isSendTerm) {
		this.isSendTerm = isSendTerm;
	}
	public String getRespEndDt() {
		return respEndDt;
	}
	public void setRespEndDt(String respEndDt) {
		this.respEndDt = respEndDt;
	}
	public String getSendTestYn() {
		return sendTestYn;
	}
	public void setSendTestYn(String sendTestYn) {
		this.sendTestYn = sendTestYn;
	}
	public String getSendTestEm() {
		return sendTestEm;
	}
	public void setSendTestEm(String sendTestEm) {
		this.sendTestEm = sendTestEm;
	}
	public int getSendTestTaskNo() {
		return sendTestTaskNo;
	}
	public void setSendTestTaskNo(int sendTestTaskNo) {
		this.sendTestTaskNo = sendTestTaskNo;
	}
	public int getSendTestSubTaskNo() {
		return sendTestSubTaskNo;
	}
	public void setSendTestSubTaskNo(int sendTestSubTaskNo) {
		this.sendTestSubTaskNo = sendTestSubTaskNo;
	}
	public String getSendIp() {
		return sendIp;
	}
	public void setSendIp(String sendIp) {
		this.sendIp = sendIp;
	}
	public String getTestSendId() {
		return testSendId;
	}
	public void setTestSendId(String testSendId) {
		this.testSendId = testSendId;
	}
	public String getRtyTyp() {
		return rtyTyp;
	}
	public void setRtyTyp(String rtyTyp) {
		this.rtyTyp = rtyTyp;
	}
	public int getRtyTaskNo() {
		return rtyTaskNo;
	}
	public void setRtyTaskNo(int rtyTaskNo) {
		this.rtyTaskNo = rtyTaskNo;
	}
	public int getRtySubTaskNo() {
		return rtySubTaskNo;
	}
	public void setRtySubTaskNo(int rtySubTaskNo) {
		this.rtySubTaskNo = rtySubTaskNo;
	}
	public String getRtyCode() {
		return rtyCode;
	}
	public void setRtyCode(String rtyCode) {
		this.rtyCode = rtyCode;
	}
	public String getRtyCodes() {
		return rtyCodes;
	}
	public void setRtyCodes(String rtyCodes) {
		this.rtyCodes = rtyCodes;
	}
	public int getDbConnNo() {
		return dbConnNo;
	}
	public void setDbConnNo(int dbConnNo) {
		this.dbConnNo = dbConnNo;
	}
	public int getSegNo() {
		return segNo;
	}
	public void setSegNo(int segNo) {
		this.segNo = segNo;
	}
	public String getSegNoc() {
		return segNoc;
	}
	public void setSegNoc(String segNoc) {
		this.segNoc = segNoc;
	}
	public String getSegNm() {
		return segNm;
	}
	public void setSegNm(String segNm) {
		this.segNm = segNm;
	}
	public String getSelectSql() {
		return selectSql;
	}
	public void setSelectSql(String selectSql) {
		this.selectSql = selectSql;
	}
	public String getFromSql() {
		return fromSql;
	}
	public void setFromSql(String fromSql) {
		this.fromSql = fromSql;
	}
	public String getWhereSql() {
		return whereSql;
	}
	public void setWhereSql(String whereSql) {
		this.whereSql = whereSql;
	}
	public String getOderbySql() {
		return oderbySql;
	}
	public void setOderbySql(String oderbySql) {
		this.oderbySql = oderbySql;
	}
	public String getMergeKey() {
		return mergeKey;
	}
	public void setMergeKey(String mergeKey) {
		this.mergeKey = mergeKey;
	}
	public String getMergeCol() {
		return mergeCol;
	}
	public void setMergeCol(String mergeCol) {
		this.mergeCol = mergeCol;
	}
	public String getSegFlPath() {
		return segFlPath;
	}
	public void setSegFlPath(String segFlPath) {
		this.segFlPath = segFlPath;
	}
	public String getSegCreateTy() {
		return segCreateTy;
	}
	public void setSegCreateTy(String segCreateTy) {
		this.segCreateTy = segCreateTy;
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
	public int getSearchSegNo() {
		return searchSegNo;
	}
	public void setSearchSegNo(int searchSegNo) {
		this.searchSegNo = searchSegNo;
	}
	public String getSearchSendRepeat() {
		return searchSendRepeat;
	}
	public void setSearchSendRepeat(String searchSendRepeat) {
		this.searchSendRepeat = searchSendRepeat;
	}
	public String getSearchStatus() {
		return searchStatus;
	}
	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}
	public String getSearchWorkStatus() {
		return searchWorkStatus;
	}
	public void setSearchWorkStatus(String searchWorkStatus) {
		this.searchWorkStatus = searchWorkStatus;
	}
	public List<String> getSearchWorkStatusList() {
		return searchWorkStatusList;
	}
	public void setSearchWorkStatusList(List<String> searchWorkStatusList) {
		this.searchWorkStatusList = searchWorkStatusList;
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
	public String getComposerValue() {
		return composerValue;
	}
	public void setComposerValue(String composerValue) {
		this.composerValue = composerValue;
	}
	public String getCampInfo() {
		return campInfo;
	}
	public void setCampInfo(String campInfo) {
		this.campInfo = campInfo;
	}
	public int getTestCnt() {
		return testCnt;
	}
	public void setTestCnt(int testCnt) {
		this.testCnt = testCnt;
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
	public int getMailCnt() {
		return mailCnt;
	}
	public void setMailCnt(int mailCnt) {
		this.mailCnt = mailCnt;
	}
	public int getTotCnt() {
		return totCnt;
	}
	public void setTotCnt(int totCnt) {
		this.totCnt = totCnt;
	}
	public int getSucCnt() {
		return sucCnt;
	}
	public void setSucCnt(int sucCnt) {
		this.sucCnt = sucCnt;
	}
	public int getFailCnt() {
		return failCnt;
	}
	public void setFailCnt(int failCnt) {
		this.failCnt = failCnt;
	}
	public int getApprCnt() {
		return apprCnt;
	}
	public void setApprCnt(int apprCnt) {
		this.apprCnt = apprCnt;
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
	public String getSndTpeGb() {
		return sndTpeGb;
	}
	public void setSndTpeGb(String sndTpeGb) {
		this.sndTpeGb = sndTpeGb;
	}
	public String getMailMktGb() {
		return mailMktGb;
	}
	public void setMailMktGb(String mailMktGb) {
		this.mailMktGb = mailMktGb;
	}
	public String getMailMktNm() {
		return mailMktNm;
	}
	public void setMailMktNm(String mailMktNm) {
		this.mailMktNm = mailMktNm;
	}
	public String getApprUserId() {
		return apprUserId;
	}
	public void setApprUserId(String apprUserId) {
		this.apprUserId = apprUserId;
	}
	public String getBizkey() {
		return bizkey;
	}
	public void setBizkey(String bizkey) {
		this.bizkey = bizkey;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getCustEm() {
		return custEm;
	}
	public void setCustEm(String custEm) {
		this.custEm = custEm;
	}
}
