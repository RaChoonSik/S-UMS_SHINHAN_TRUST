/**
 * 작성자 : 김준희
 * 작성일시 : 2022.04.02
 * 설명 : Push VO
 */
package kr.co.sict.ums.push.cam.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class PushVO extends CommonVO {
	private int pushmessageId;		// PUSH 아이디
	private String userId;				//사용자아이디
	private String userNm;				//사용자명
	private int deptNo;					//사용자그릅코드
	private String deptNm;				//사용자그룹(부서)명
	private int segNo;					//수신자그룹(발송대상)번호
	private String segNoc;				//번호가 캐릭터로 사용되는경우 segno + mergekey
	private String segNm;				//수신자그룹명
	private String sendTelno;			//발송자연락처
	private int campNo;					//캠페인번호
	private String campNm;				//캠페인명
	private String pushName;			//메시지제목
	private String pushMessage;			//메시지내용 =>
	private String pushGubun;			//PUSH유형 (C124)
	private String pushGubunNm;			//PUSH유형명
	private String fileNm;			//첨부파일명 1 (이미지)
	private String filePath;			//첨부파일 1 (이미지)
	private long fileSize;				//첨부파일사이즈 1 (이미지)
	private String callUri;				//ANDROID LINK URL
	private String callUriIos;			//IOS LINK URL
	private String osGubun;				//OS구분
	private String osGubunNm;			//OS구분명
	private String pushTitle;			//PUSH제목
	private int retryCnt;				//재시도 횟수
	private int pushAlarmday;			//알람일수
	private String smsYn;				//SMS전송여부
	private String legalYn;				//광고여부
	private String legalCf;				//광고안내메시지
	private String sendRepeat;			//SEND_REPEAT
	private String sendTermLoop;		//SEND_TERM_LOOP
	private String sendTermEndDt;		//SEND_TERM_END_DT
	private String sendTermLoopTy;		//SEND_TERM_LOOP_TY
	private String sendDt;				//발송일시
	private String sendEndDt;			//발송종료일시 
	private String workStatus;			//발송상태 C009 (정상 / 사용중지 / 삭제) 
	private String workStatusNm;		//발송상태명 C009 (정상 / 사용중지 / 삭제)
	private String status;				//메시지상태 (C101) 
	private String statusNm;			//메시지상태명
	private String callUrlTyp;			//URL 링크 유형 C123 (WEB LINK 000 / WEB OPEN 001 / WEB VIDEO 002)	
	private String sendTyp;				//발송유형 C120 (즉시발송 000 / 예약발송 001 / 실시간 002)
	private String sendTypNm;			//발송유형 C120 (즉시발송 000 / 예약발송 001 / 실시간 002)
	private String regId;				//등록자아이디
	private String regDt;				//등록일시
	private String upId;				//수정자  
	private String upDt;				//수정일자
	
	private String pushmessageIds;		//선택한리스트	
	private String sendYmd;				//예약일자
	private String sendHour;			//예약시간
	private String sendMin;				//예약분
	
	// 검색조건
	private int searchDeptNo;			//검색사용자그룹번호
	private String searchUserId;		//검색사용자아이디
	private String searchStartDt;		//검색시작일
	private String searchEndDt;			//검색종료일
	private int searchCampNo;			//검색캠페인번호
	private String searchStatus;		//검색메시지상태
	private String searchWorkStatus;	//검색메시지발송상태
	private String searchPushGubun;		//검색전송유형 
	private String searchGrantMenuId;	//문자발송건한확인
	private String searchPushName;		//검색제목
	
	private String adminYn;				//관리자여부
	private String uilang;				//언어권
	private String attachNm;			//파일명 
	private String attachPath;			//파일경로 
	private long attachfileSize;		//첨부파일사이즈 
	public int getPushmessageId() {
		return pushmessageId;
	}
	public void setPushmessageId(int pushmessageId) {
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
	public String getFileNm() {
		return fileNm;
	}
	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
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
	public String getOsGubunNm() {
		return osGubunNm;
	}
	public void setOsGubunNm(String osGubunNm) {
		this.osGubunNm = osGubunNm;
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
	public String getLegalYn() {
		return legalYn;
	}
	public void setLegalYn(String legalYn) {
		this.legalYn = legalYn;
	}
	public String getLegalCf() {
		return legalCf;
	}
	public void setLegalCf(String legalCf) {
		this.legalCf = legalCf;
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
	public String getSendDt() {
		return sendDt;
	}
	public void setSendDt(String sendDt) {
		this.sendDt = sendDt;
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
	public String getWorkStatusNm() {
		return workStatusNm;
	}
	public void setWorkStatusNm(String workStatusNm) {
		this.workStatusNm = workStatusNm;
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
	public String getCallUrlTyp() {
		return callUrlTyp;
	}
	public void setCallUrlTyp(String callUrlTyp) {
		this.callUrlTyp = callUrlTyp;
	}
	public String getSendTyp() {
		return sendTyp;
	}
	public void setSendTyp(String sendTyp) {
		this.sendTyp = sendTyp;
	}
	public String getSendTypNm() {
		return sendTypNm;
	}
	public void setSendTypNm(String sendTypNm) {
		this.sendTypNm = sendTypNm;
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
	public String getPushmessageIds() {
		return pushmessageIds;
	}
	public void setPushmessageIds(String pushmessageIds) {
		this.pushmessageIds = pushmessageIds;
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
	public int getSearchCampNo() {
		return searchCampNo;
	}
	public void setSearchCampNo(int searchCampNo) {
		this.searchCampNo = searchCampNo;
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
	public String getSearchPushGubun() {
		return searchPushGubun;
	}
	public void setSearchPushGubun(String searchPushGubun) {
		this.searchPushGubun = searchPushGubun;
	}
	public String getSearchGrantMenuId() {
		return searchGrantMenuId;
	}
	public void setSearchGrantMenuId(String searchGrantMenuId) {
		this.searchGrantMenuId = searchGrantMenuId;
	}
	public String getSearchPushName() {
		return searchPushName;
	}
	public void setSearchPushName(String searchPushName) {
		this.searchPushName = searchPushName;
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
	public long getAttachfileSize() {
		return attachfileSize;
	}
	public void setAttachfileSize(long attachfileSize) {
		this.attachfileSize = attachfileSize;
	}
}
