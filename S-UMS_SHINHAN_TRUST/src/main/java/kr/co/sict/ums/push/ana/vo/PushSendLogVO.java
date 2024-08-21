/**
 * Push 관련 주 테이블 => neo_pushmessage   
 * 
 * 작성자 : 김준희
 * 작성일시 : 2022.04.01
 * 설명 : Push Send Log VO
 */
package kr.co.sict.ums.push.ana.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class PushSendLogVO extends CommonVO {
	private String pushmessageId;		// PUSH 아이디 (PKey) => 필수
	private String userId;				//사용자아이디 => 필수
	private String userNm;				//사용자명
	private int deptNo;					//사용자그릅코드 => 필수
	private String deptNm;				//사용자그룹(부서)명
	private String regId;				//등록자아이디
	private String regDt;				//등록일시
	private int segNo;					//수신자그룹(발송대상)번호
	private String segNm;				//수신자그룹명
	private String sendTelno;			//발송자연락처
	private int campNo;					//캠페인번호
	private String campNm;				//캠페인명
	private String oCode;				//소속코드
	private String pCode;				//사원코드
	private String sendYm;				//발송년월
	private String sendDt;				//발송년월일시분  
	private String pushName;			//메시지제목
	private String pushMessage;			//메시지내용  
	private String status;				//메시지 상태 C009 (정상 / 사용중지 / 삭제) 
	private String statusNm;			//메시지상태명
	private String pushGubun;			//PUSH 유형
	private String pushGubunNm;			//PUSH 유형명
	private String filePath01;			//첨부파일 1 (이미지)
	private String filePath02;			//첨부파일 2 (이미지)
	private String callUri;				//ANDROID LINK URL
	private String callUriIos;			//IOS LINK URL
	private String osGubun;				//OS구분
	private String pushTitle;			//PUSH제목
	private int retryCnt;				//재시도 횟수
	private int pushAlarmday;			//알람일수
	private String smsYn;				//SMS전송여부
	private String sendRepeat;			//SEND_REPEAT
	private String sendTermLoop;		//SEND_TERM_LOOP
	private String sendTermEndDt;		//SEND_TERM_END_DT
	private String sendTermLoopTy;		//SEND_TERM_LOOP_TY
	private String legalYn;				//광고여부 
	private String taskNm;				//문자명
	private String sendNm;				//발송자이름
	private String sendTyp;				//발송유형(즉시, 예약)
	private String callUrlTyp;			//URL 링크 유형 C123 (WEB LINK 000 / WEB OPEN 001 / WEB VIDEO 002)
	private String sendDate;			//발송 시분초 (sysdate)
	private String sendEndDt;			//발송 시분초 (sysdate)

	private String workStatus;			//발송상태C118 (001:대기 003:성공 004:실패) 
	private String upId;				//수정자  
	private String upDt;				//수정일자
	private String rsltCd;				//발송결과 0실패 -1 성공

	private int sendTotCnt;				//총건수
	private int succCnt;				//성공
	private int failCnt;				//실패
	private int totalCnt;				//총건수
	private int succAnd;				//android 성공
	private int failAnd;				//android 실패
	private int succIos;				//ios 성공
	private int failIos;				//ios 실패
	private String succAndPer;			//android 성공 퍼센트
	private String failAndPer;			//android 실패 퍼센트
	private String succIosPer;			//ios 성공 퍼센트
	private String failIosPer;			//ios 실패 퍼센트	
	
	
	// 수신핸드폰 번호
	private String sendYmd;				//예약일자
	private String sendHour;			//예약시간
	private String sendMin;				//예약분
	
	// 검색조건
	private int searchDeptNo;			//검색사용자그룹번호
	private String searchUserId;		//검색사용자아이디
	private String searchStartDt;		//검색시작일
	private String searchEndDt;			//검색종료일
	private String searchPushName;		//검색문자명
	private int searchCampNo;			//검색캠페인번호
	private String searchPushGubun;		//검색전송유형
	private String searchStatus;		//검색메시지상태
	
	private String adminYn;				//관리자여부
	private String uilang;				//언어권
	private String attachNm;			//파일명 
	private String attachPath;			//파일경로  
	
	private int pushCount;				//캠페인별 PUSH 발송건수
	private String campRegDt;			//캠페인등록일
	private String pushRegDt;			//PUSH 등록일 
	private String workStatusNm;		//발송상태 
	private String searchCampNm;		//검색캠페인명
	private String searchCallGubun;		//호출페이지 구분(캠페인별에서 목록인지 기본목록인지 구분 C / P )
	
	public String getPushmessageId() {
		return pushmessageId;
	}
	public void setPushmessageId(String pushmessageId) {
		this.pushmessageId = pushmessageId;
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
	public String getSendTelno() {
		return sendTelno;
	}
	public void setSendTelno(String sendTelno) {
		this.sendTelno = sendTelno;
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
	public String getoCode() {
		return oCode;
	}
	public void setoCode(String oCode) {
		this.oCode = oCode;
	}
	public String getpCode() {
		return pCode;
	}
	public void setpCode(String pCode) {
		this.pCode = pCode;
	}
	public String getSendYm() {
		return sendYm;
	}
	public void setSendYm(String sendYm) {
		this.sendYm = sendYm;
	}
	public String getSendDt() {
		return sendDt;
	}
	public void setSendDt(String sendDt) {
		this.sendDt = sendDt;
	}
	public String getPushName() {
		return pushName;
	}
	public void setPushName(String pushName) {
		this.pushName = pushName;
	}
	public String getPushMessage() {
		return pushMessage;
	}
	public void setPushMessage(String pushMessage) {
		this.pushMessage = pushMessage;
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
	public String getPushGubun() {
		return pushGubun;
	}
	public void setPushGubun(String pushGubun) {
		this.pushGubun = pushGubun;
	}
	public String getPushGubunNm() {
		return pushGubunNm;
	}
	public void setPushGubunNm(String pushGubunNm) {
		this.pushGubunNm = pushGubunNm;
	}
	public String getFilePath01() {
		return filePath01;
	}
	public void setFilePath01(String filePath01) {
		this.filePath01 = filePath01;
	}
	public String getFilePath02() {
		return filePath02;
	}
	public void setFilePath02(String filePath02) {
		this.filePath02 = filePath02;
	}
	public String getCallUri() {
		return callUri;
	}
	public void setCallUri(String callUri) {
		this.callUri = callUri;
	}
	public String getCallUriIos() {
		return callUriIos;
	}
	public void setCallUriIos(String callUriIos) {
		this.callUriIos = callUriIos;
	}
	public String getOsGubun() {
		return osGubun;
	}
	public void setOsGubun(String osGubun) {
		this.osGubun = osGubun;
	}
	public String getPushTitle() {
		return pushTitle;
	}
	public void setPushTitle(String pushTitle) {
		this.pushTitle = pushTitle;
	}
	public int getRetryCnt() {
		return retryCnt;
	}
	public void setRetryCnt(int retryCnt) {
		this.retryCnt = retryCnt;
	}
	public int getPushAlarmday() {
		return pushAlarmday;
	}
	public void setPushAlarmday(int pushAlarmday) {
		this.pushAlarmday = pushAlarmday;
	}
	public String getSmsYn() {
		return smsYn;
	}
	public void setSmsYn(String smsYn) {
		this.smsYn = smsYn;
	}
	public String getSendRepeat() {
		return sendRepeat;
	}
	public void setSendRepeat(String sendRepeat) {
		this.sendRepeat = sendRepeat;
	}
	public String getSendTermLoop() {
		return sendTermLoop;
	}
	public void setSendTermLoop(String sendTermLoop) {
		this.sendTermLoop = sendTermLoop;
	}
	public String getSendTermEndDt() {
		return sendTermEndDt;
	}
	public void setSendTermEndDt(String sendTermEndDt) {
		this.sendTermEndDt = sendTermEndDt;
	}
	public String getSendTermLoopTy() {
		return sendTermLoopTy;
	}
	public void setSendTermLoopTy(String sendTermLoopTy) {
		this.sendTermLoopTy = sendTermLoopTy;
	}
	public String getLegalYn() {
		return legalYn;
	}
	public void setLegalYn(String legalYn) {
		this.legalYn = legalYn;
	}
	public String getTaskNm() {
		return taskNm;
	}
	public void setTaskNm(String taskNm) {
		this.taskNm = taskNm;
	}
	public String getSendNm() {
		return sendNm;
	}
	public void setSendNm(String sendNm) {
		this.sendNm = sendNm;
	}
	public String getSendTyp() {
		return sendTyp;
	}
	public void setSendTyp(String sendTyp) {
		this.sendTyp = sendTyp;
	}
	public String getCallUrlTyp() {
		return callUrlTyp;
	}
	public void setCallUrlTyp(String callUrlTyp) {
		this.callUrlTyp = callUrlTyp;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getSendEndDt() {
		return sendEndDt;
	}
	public void setSendEndDt(String sendEndDt) {
		this.sendEndDt = sendEndDt;
	}
	public String getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
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
	public String getRsltCd() {
		return rsltCd;
	}
	public void setRsltCd(String rsltCd) {
		this.rsltCd = rsltCd;
	}
	public int getSendTotCnt() {
		return sendTotCnt;
	}
	public void setSendTotCnt(int sendTotCnt) {
		this.sendTotCnt = sendTotCnt;
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
	public int getSuccAnd() {
		return succAnd;
	}
	public void setSuccAnd(int succAnd) {
		this.succAnd = succAnd;
	}
	public int getFailAnd() {
		return failAnd;
	}
	public void setFailAnd(int failAnd) {
		this.failAnd = failAnd;
	}
	public int getSuccIos() {
		return succIos;
	}
	public void setSuccIos(int succIos) {
		this.succIos = succIos;
	}
	public int getFailIos() {
		return failIos;
	}
	public void setFailIos(int failIos) {
		this.failIos = failIos;
	}
	public String getSuccAndPer() {
		return succAndPer;
	}
	public void setSuccAndPer(String succAndPer) {
		this.succAndPer = succAndPer;
	}
	public String getFailAndPer() {
		return failAndPer;
	}
	public void setFailAndPer(String failAndPer) {
		this.failAndPer = failAndPer;
	}
	public String getSuccIosPer() {
		return succIosPer;
	}
	public void setSuccIosPer(String succIosPer) {
		this.succIosPer = succIosPer;
	}
	public String getFailIosPer() {
		return failIosPer;
	}
	public void setFailIosPer(String failIosPer) {
		this.failIosPer = failIosPer;
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
	public String getSearchPushName() {
		return searchPushName;
	}
	public void setSearchPushName(String searchPushName) {
		this.searchPushName = searchPushName;
	}
	public int getSearchCampNo() {
		return searchCampNo;
	}
	public void setSearchCampNo(int searchCampNo) {
		this.searchCampNo = searchCampNo;
	}
	public String getSearchPushGubun() {
		return searchPushGubun;
	}
	public void setSearchPushGubun(String searchPushGubun) {
		this.searchPushGubun = searchPushGubun;
	}
	public String getSearchStatus() {
		return searchStatus;
	}
	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
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
	public int getPushCount() {
		return pushCount;
	}
	public void setPushCount(int pushCount) {
		this.pushCount = pushCount;
	}
	public String getCampRegDt() {
		return campRegDt;
	}
	public void setCampRegDt(String campRegDt) {
		this.campRegDt = campRegDt;
	}
	public String getPushRegDt() {
		return pushRegDt;
	}
	public void setPushRegDt(String pushRegDt) {
		this.pushRegDt = pushRegDt;
	}
	public String getWorkStatusNm() {
		return workStatusNm;
	}
	public void setWorkStatusNm(String workStatusNm) {
		this.workStatusNm = workStatusNm;
	}
	public String getSearchCampNm() {
		return searchCampNm;
	}
	public void setSearchCampNm(String searchCampNm) {
		this.searchCampNm = searchCampNm;
	}
	public String getSearchCallGubun() {
		return searchCallGubun;
	}
	public void setSearchCallGubun(String searchCallGubun) {
		this.searchCallGubun = searchCallGubun;
	}
}
