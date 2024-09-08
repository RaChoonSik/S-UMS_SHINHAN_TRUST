 /* 작성자 : 김준희
 * 작성일시 : 2021.10.13
 * 설명 : SMS 발송내역 조회 VO
 */
package kr.co.sict.ums.sms.ana.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class SmsSendLogVO extends CommonVO {
	private String msgid;			//메시지아이디
	private String keygen;			//메시지키
	private String userId;			//사용자아이디
	private String userNm;			//사용자명
	private int deptNo;				//사용자그릅코드
	private String deptNm;			//사용자그룹(부서)명
	private String regId;			//등록자아이디
	private String regDt;			//등록일시
	private int segNo;				//수신자그룹번호
	private String segNm;			//수신자그룹명
	private String sendTelNo;		//발송자연락처
	private int campNo;				//캠페인번호
	private String campNm;			//캠페인명
	private String oCode;			//소속코드
	private String pCode;			//사원코드
	private String sendYm;			//발송년월
	private String sendDate;		//발송년월일시분
	private String smsName;			//메시지제목
	private String smsMessage;		//메시지내용
	private String status;			//메시지상태
	private String statusNm;		//메시지상태명
	private String gubun;			//전송유형
	private String gubunNm;			//전송유형명
	private String sendGubun;		//실제전송유형
	private String sendGubunNm;		//실제전송유형명
	private String filePath;		//파일경로
	private String filePath2;		//파일경로
	private String legalYn;			//광고여부
	private String legalCf;			//광고수신거부번호
	private String smsStatus;		//문자상태
	private String smsStatusNm;		//문자상태명 
	private String taskNm;			//알림톡명
	private String tempNm;			//템플릿명
	private String tempCd;			//템플릿명
	private String tempContent;		//템플릿명
	private String validYn;			//유효성
	private String sendStartDt;		//시작일시
	private String sendEndDt;		//종료일시
	private String smsSendYn;		//sms 발송여부
	private int sendTotCnt;			//총건수
	private String rsltCd;			//발송결과 0실패 -1 성공
	private String id;				//sms 고객ID
	private String name;			//sms 고객이름
	private String phone;			//sms 고객 전화번호
	private String exeUserId;		//sms 발신자아이디
	private String exeUserNm;		//sms 발신자이름
	private String rcode;			//상세코드
	private String rname;			//상세코드 이름
	private String subject;			//smslog 메세지 제목
	private String msgBody;			//smslog 메세지 내용
	private String cmid;			//cmid

	private int succCnt;			//성공
	private int failCnt;			//실패
	private int totalCnt;			//총건수
	private int succSms;			//알림톡-SMS 성공
	private int failSms;			//알림톡-SMS  실패
	private String succPer;			//성공 퍼센트
	private String failPer;			//실패 퍼센트
	private String succSmsPer;		//알림톡-SMS 성공 퍼센트
	private String failSmsPer;		//알림톡- SMS 실패 퍼센트
	private String arrGubunNms[];	//메세지 구분
	
	// 수신핸드폰 번호
	private String smsPhones;		//수시핸드폰번호리스트
	private String selKeygens;		//선택한리스트
	private String sendYmd;			// 예약일자
	private String sendHour;		// 예약시간
	private String sendMin;			// 예약분
	
	private String attachFileList;	// 첨부파일리스트
	// 추가정보
	private int searchDeptNo;		// 검색사용자그룹번호
	private String searchUserId;	// 검색사용자아이디
	private String searchUserNm;	// 검색사용자명
	private String searchCustId;	// 검색고객아이디
	private String searchCustNm;	// 검색고객명
	private String searchCustPhone;	// 검색고객전화번호
	private String searchStartDt;	// 검색시작일
	private String searchEndDt;		// 검색종료일
	private String searchSmsName;	// 검색문자명
	private int searchCampNo;		// 검색캠페인번호
	private String searchStatus;	// 검색메시지상태
	private String searchSmsStatus;	// 검색문자상태
	private String searchGubun;		// 검색전송유형코드
	private String searchGubunNm;	// 검색전송유형이름 
	private String searchTaskNm;	// 검색문자명
	private String searchTempNm;	// 검색템플릿명
	private String searchTempCd;	// 검색템플릿코드
	private String searchCampNm;	// 검색캠페인명
	private String searchExeUserNm;	// 검색발신자명명
	private String searchIsInterface;// 화면등록/인터페이스등록 구분(Y/N)
	
	 
	private String adminYn;			// 관리자여부
	private String uilang;			// 언어권'
	
	private int smsCnt;					//캠페인별 SMS 발송건수
	private String campRegDt;			//캠페인등록일
	private String smsRegDt;			//SMS 등록일
	private String tempRegDt;			//템플릿 등록일
	private String searchCallGubun;		//호출페이지 구분(캠페인별에서 목록인지 기본목록인지 구분 C / P )

	private String sendTyp;          //발송유형
	private String sendTypNm;        //발송유형명
	
	private String destPhone;		//수신자phone
	private String sendPhone;		//발신자phone
	private String requestTime; 	//등록시간
	private String sendTime;		//발송기준시간
	private String reportTime;		//단말기수신시간
	private String msgType;			//데이터타입(sms:0/mms:5)
	private String callStatus; 
	private String callStatusNm;
    
	
	public String getMsgid() {
		return msgid;
	}
	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}
	public String getKeygen() {
		return keygen;
	}
	public void setKeygen(String keygen) {
		this.keygen = keygen;
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
	public String getSendTelNo() {
		return sendTelNo;
	}
	public void setSendTelNo(String sendTelNo) {
		this.sendTelNo = sendTelNo;
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
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getSmsName() {
		return smsName;
	}
	public void setSmsName(String smsName) {
		this.smsName = smsName;
	}
	public String getSmsMessage() {
		return smsMessage;
	}
	public void setSmsMessage(String smsMessage) {
		this.smsMessage = smsMessage;
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
	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	public String getGubunNm() {
		return gubunNm;
	}
	public void setGubunNm(String gubunNm) {
		this.gubunNm = gubunNm;
	}
	public String getSendGubun() {
		return sendGubun;
	}
	public void setSendGubun(String sendGubun) {
		this.sendGubun = sendGubun;
	}
	public String getSendGubunNm() {
		return sendGubunNm;
	}
	public void setSendGubunNm(String sendGubunNm) {
		this.sendGubunNm = sendGubunNm;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFilePath2() {
		return filePath2;
	}
	public void setFilePath2(String filePath2) {
		this.filePath2 = filePath2;
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
	public String getSmsStatus() {
		return smsStatus;
	}
	public void setSmsStatus(String smsStatus) {
		this.smsStatus = smsStatus;
	}
	public String getSmsStatusNm() {
		return smsStatusNm;
	}
	public void setSmsStatusNm(String smsStatusNm) {
		this.smsStatusNm = smsStatusNm;
	}
	public String getTaskNm() {
		return taskNm;
	}
	public void setTaskNm(String taskNm) {
		this.taskNm = taskNm;
	}
	public String getTempNm() {
		return tempNm;
	}
	public void setTempNm(String tempNm) {
		this.tempNm = tempNm;
	}
	public String getTempCd() {
		return tempCd;
	}
	public void setTempCd(String tempCd) {
		this.tempCd = tempCd;
	}
	public String getTempContent() {
		return tempContent;
	}
	public void setTempContent(String tempContent) {
		this.tempContent = tempContent;
	}
	public String getValidYn() {
		return validYn;
	}
	public void setValidYn(String validYn) {
		this.validYn = validYn;
	}
	public String getSendStartDt() {
		return sendStartDt;
	}
	public void setSendStartDt(String sendStartDt) {
		this.sendStartDt = sendStartDt;
	}
	public String getSendEndDt() {
		return sendEndDt;
	}
	public void setSendEndDt(String sendEndDt) {
		this.sendEndDt = sendEndDt;
	}
	public String getSmsSendYn() {
		return smsSendYn;
	}
	public void setSmsSendYn(String smsSendYn) {
		this.smsSendYn = smsSendYn;
	}
	public int getSendTotCnt() {
		return sendTotCnt;
	}
	public void setSendTotCnt(int sendTotCnt) {
		this.sendTotCnt = sendTotCnt;
	}
	public String getRsltCd() {
		return rsltCd;
	}
	public void setRsltCd(String rsltCd) {
		this.rsltCd = rsltCd;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getExeUserId() {
		return exeUserId;
	}
	public void setExeUserId(String exeUserId) {
		this.exeUserId = exeUserId;
	}
	public String getExeUserNm() {
		return exeUserNm;
	}
	public void setExeUserNm(String exeUserNm) {
		this.exeUserNm = exeUserNm;
	}
	public String getRcode() {
		return rcode;
	}
	public void setRcode(String rcode) {
		this.rcode = rcode;
	}
	public String getRname() {
		return rname;
	}
	public void setRname(String rname) {
		this.rname = rname;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMsgBody() {
		return msgBody;
	}
	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}
	public String getCmid() {
		return cmid;
	}
	public void setCmid(String cmid) {
		this.cmid = cmid;
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
	public int getSuccSms() {
		return succSms;
	}
	public void setSuccSms(int succSms) {
		this.succSms = succSms;
	}
	public int getFailSms() {
		return failSms;
	}
	public void setFailSms(int failSms) {
		this.failSms = failSms;
	}
	public String getSuccPer() {
		return succPer;
	}
	public void setSuccPer(String succPer) {
		this.succPer = succPer;
	}
	public String getFailPer() {
		return failPer;
	}
	public void setFailPer(String failPer) {
		this.failPer = failPer;
	}
	public String getSuccSmsPer() {
		return succSmsPer;
	}
	public void setSuccSmsPer(String succSmsPer) {
		this.succSmsPer = succSmsPer;
	}
	public String getFailSmsPer() {
		return failSmsPer;
	}
	public void setFailSmsPer(String failSmsPer) {
		this.failSmsPer = failSmsPer;
	}
	public String[] getArrGubunNms() {
		return arrGubunNms;
	}
	public void setArrGubunNms(String[] arrGubunNms) {
		this.arrGubunNms = arrGubunNms;
	}
	public String getSmsPhones() {
		return smsPhones;
	}
	public void setSmsPhones(String smsPhones) {
		this.smsPhones = smsPhones;
	}
	public String getSelKeygens() {
		return selKeygens;
	}
	public void setSelKeygens(String selKeygens) {
		this.selKeygens = selKeygens;
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
	public String getAttachFileList() {
		return attachFileList;
	}
	public void setAttachFileList(String attachFileList) {
		this.attachFileList = attachFileList;
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
	public String getSearchUserNm() {
		return searchUserNm;
	}
	public void setSearchUserNm(String searchUserNm) {
		this.searchUserNm = searchUserNm;
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
	public String getSearchCustPhone() {
		return searchCustPhone;
	}
	public void setSearchCustPhone(String searchCustPhone) {
		this.searchCustPhone = searchCustPhone;
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
	public String getSearchSmsName() {
		return searchSmsName;
	}
	public void setSearchSmsName(String searchSmsName) {
		this.searchSmsName = searchSmsName;
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
	public String getSearchSmsStatus() {
		return searchSmsStatus;
	}
	public void setSearchSmsStatus(String searchSmsStatus) {
		this.searchSmsStatus = searchSmsStatus;
	}
	public String getSearchGubun() {
		return searchGubun;
	}
	public void setSearchGubun(String searchGubun) {
		this.searchGubun = searchGubun;
	}
	public String getSearchGubunNm() {
		return searchGubunNm;
	}
	public void setSearchGubunNm(String searchGubunNm) {
		this.searchGubunNm = searchGubunNm;
	}
	public String getSearchTaskNm() {
		return searchTaskNm;
	}
	public void setSearchTaskNm(String searchTaskNm) {
		this.searchTaskNm = searchTaskNm;
	}
	public String getSearchTempNm() {
		return searchTempNm;
	}
	public void setSearchTempNm(String searchTempNm) {
		this.searchTempNm = searchTempNm;
	}
	public String getSearchTempCd() {
		return searchTempCd;
	}
	public void setSearchTempCd(String searchTempCd) {
		this.searchTempCd = searchTempCd;
	}
	public String getSearchCampNm() {
		return searchCampNm;
	}
	public void setSearchCampNm(String searchCampNm) {
		this.searchCampNm = searchCampNm;
	}
	public String getSearchExeUserNm() {
		return searchExeUserNm;
	}
	public void setSearchExeUserNm(String searchExeUserNm) {
		this.searchExeUserNm = searchExeUserNm;
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
	public int getSmsCnt() {
		return smsCnt;
	}
	public void setSmsCnt(int smsCnt) {
		this.smsCnt = smsCnt;
	}
	public String getCampRegDt() {
		return campRegDt;
	}
	public void setCampRegDt(String campRegDt) {
		this.campRegDt = campRegDt;
	}
	public String getSmsRegDt() {
		return smsRegDt;
	}
	public void setSmsRegDt(String smsRegDt) {
		this.smsRegDt = smsRegDt;
	}
	public String getTempRegDt() {
		return tempRegDt;
	}
	public void setTempRegDt(String tempRegDt) {
		this.tempRegDt = tempRegDt;
	}
	public String getSearchCallGubun() {
		return searchCallGubun;
	}
	public void setSearchCallGubun(String searchCallGubun) {
		this.searchCallGubun = searchCallGubun;
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
    public String getSearchIsInterface() {
		return searchIsInterface;
	}
	public void setSearchIsInterface(String searchIsInterface) {
		this.searchIsInterface = searchIsInterface;
	}
	public String getDestPhone() {
		return destPhone;
	}
	public void setDestPhone(String destPhone) {
		this.destPhone = destPhone;
	}
	public String getSendPhone() {
		return sendPhone;
	}
	public void setSendPhone(String sendPhone) {
		this.sendPhone = sendPhone;
	}
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getCallStatus() {
		return callStatus;
	}
	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}
	public String getCallStatusNm() {
		return callStatusNm;
	}
	public void setCallStatusNm(String callStatusNm) {
		this.callStatusNm = callStatusNm;
	}
	
}
