/**
 * 작성자 : 김준희
 * 작성일시 : 2021.10.13
 * 설명 : 메일 Sms VO
 */
package kr.co.sict.ums.sms.cam.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class SmsVO extends CommonVO {
	private String msgid;			//메시지아이디
	private String keygen;			//메시지키
	private String userId;			//사용자아이디
	private String userNm;			//사용자명
	private int deptNo;				//사용자그릅코드
	private String deptNm;			//사용자그룹(부서)명
	private int segNo;				//수신자그룹(발송대상)번호
	private String segNoc;			//번호가 캐릭터로 사용되는경우 segno + mergekey
	private String segNm;			//수신자그룹명
	private String sendTelno;		//발송자연락처
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
	private String legalYn;			//광고여부 
	private String legalCf;			//광고수신거부번호
	private String taskNm;			//문자명
	private String smsStatus;		//문자상태
	private String smsStatusNm;		//문자상태명 
	private String sendNm;			//발송자이름
	private String sendTyp;			// C120
	private String plainUserId;		//현재 미사용
	private String tempCd;   		//카카오템플릿코드
	private String validYn;			//유효성검토여부
	private String regId;			//등록자아이디
	private String regDt;			//등록일시
	private String requestKey;		//API요청키 
	private String upId;			//수정자아이디
	private String upDt;			//수정일시
	
	//카카오템플릿 매핑 정보 
	private String kakaoMergeCol;	//매핑된 수신자그룹의 머지 (실제테이블MERGE_COL, Segment의 MergeCol과의 혼동 회피위함)
	private String kakaoCol;		//매핑된 카카오템플릿의 머지
	private String kakaoMergeCols;	//매핑된 수신자그룹의 머지 (실제테이블MERGE_COL, Segment의 MergeCol과의 혼동 회피위함)
	private String kakaoCols;		//매핑된 카카오템플릿의 머지
	private String mergeItem;		//카카오 템플릿에 등록된 템플릿 머지 
	// 수신핸드폰 번호
	private String smsPhones;		//수시핸드폰번호리스트
	private String selKeygens;		//선택한리스트	
	private String sendYmd;			//예약일자
	private String sendHour;		//예약시간
	private String sendMin;			//예약분
	
	// 검색조건
	private int searchDeptNo;			// 검색사용자그룹번호
	private String searchUserId;		// 검색사용자아이디
	private String searchStartDt;		// 검색시작일
	private String searchEndDt;			// 검색종료일
	private String searchTaskNm;		// 검색문자명
	private int searchCampNo;			// 검색캠페인번호
	private String searchStatus;		// 검색메시지상태
	private String searchSmsStatus;		// 검색문자상태
	private String searchGubun;			// 검색전송유형 
	private String searchGrantMenuId;	// 문자발송건한확인
	private String searchSmsName;		// 검색문자명
	private String searchTempCd;		// 검색카카오템플릿명
	
	//추가정보
	private String adminYn;		// 관리자여부
	private String uilang;		// 언어권
	private String attachNm;	// 파일명 
	private String attachPath;	// 파일경로
	private String orgTempCd;	// 저장되어있는 TempCd
	private int orgSegNo;	// 저장되어있는 SegNo
	
	//비즈뿌리오 sms전송 컬럼(BIZ_MSG)
	private int msgType; //SMS:0, LMS:5
	private String cmid; // 키값
	private String destPhone; // 수신전화번호
	private String sendPhone; // 발신전화번호
	private String msgBody; // 메세지내용
	
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
	public String getTaskNm() {
		return taskNm;
	}
	public void setTaskNm(String taskNm) {
		this.taskNm = taskNm;
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
	public String getPlainUserId() {
		return plainUserId;
	}
	public void setPlainUserId(String plainUserId) {
		this.plainUserId = plainUserId;
	}
	public String getTempCd() {
		return tempCd;
	}
	public void setTempCd(String tempCd) {
		this.tempCd = tempCd;
	}
	public String getValidYn() {
		return validYn;
	}
	public void setValidYn(String validYn) {
		this.validYn = validYn;
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
	public String getRequestKey() {
		return requestKey;
	}
	public void setRequestKey(String requestKey) {
		this.requestKey = requestKey;
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
	public String getKakaoMergeCol() {
		return kakaoMergeCol;
	}
	public void setKakaoMergeCol(String kakaoMergeCol) {
		this.kakaoMergeCol = kakaoMergeCol;
	}
	public String getKakaoCol() {
		return kakaoCol;
	}
	public void setKakaoCol(String kakaoCol) {
		this.kakaoCol = kakaoCol;
	}
	public String getKakaoMergeCols() {
		return kakaoMergeCols;
	}
	public void setKakaoMergeCols(String kakaoMergeCols) {
		this.kakaoMergeCols = kakaoMergeCols;
	}
	public String getKakaoCols() {
		return kakaoCols;
	}
	public void setKakaoCols(String kakaoCols) {
		this.kakaoCols = kakaoCols;
	}
	public String getMergeItem() {
		return mergeItem;
	}
	public void setMergeItem(String mergeItem) {
		this.mergeItem = mergeItem;
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
	public String getSearchGrantMenuId() {
		return searchGrantMenuId;
	}
	public void setSearchGrantMenuId(String searchGrantMenuId) {
		this.searchGrantMenuId = searchGrantMenuId;
	}
	public String getSearchSmsName() {
		return searchSmsName;
	}
	public void setSearchSmsName(String searchSmsName) {
		this.searchSmsName = searchSmsName;
	}
	public String getSearchTempCd() {
		return searchTempCd;
	}
	public void setSearchTempCd(String searchTempCd) {
		this.searchTempCd = searchTempCd;
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
	public String getOrgTempCd() {
		return orgTempCd;
	}
	public void setOrgTempCd(String orgTempCd) {
		this.orgTempCd = orgTempCd;
	}
	public int getOrgSegNo() {
		return orgSegNo;
	}
	public void setOrgSegNo(int orgSegNo) {
		this.orgSegNo = orgSegNo;
	}
	public int getMsgType() {
		return msgType;
	}
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	public String getCmid() {
		return cmid;
	}
	public void setCmid(String cmid) {
		this.cmid = cmid;
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
	public String getMsgBody() {
		return msgBody;
	}
	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}
	
	
}
