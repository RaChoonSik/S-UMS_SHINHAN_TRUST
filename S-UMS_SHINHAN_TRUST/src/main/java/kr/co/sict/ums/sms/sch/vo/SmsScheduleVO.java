/**
 * 작성자 : 김준희
 * 작성일시 : 2021.10.12
 * 설명 : Schedule VO
 */
package kr.co.sict.ums.sms.sch.vo;

import java.util.List;

import kr.co.sict.ums.com.vo.CommonVO;

public class SmsScheduleVO extends CommonVO {
	
	//주 또는 일 결과값용
	private String msgid;			// 메시지아이디
	private String keygen;			// 키값	
	private String smsName;			// 메일제목
	private String sendDate;		// 발송 시분초 
	private String taskNm;			// 문자명
	private String segNm;			// 수신자그룹명
	private String campNm;			// 캠페인명
	private String status;			// 코드값(C118) 000, 001 : 예약 002: 진행 003:완료
	private String statusNm;   		// 문자발송상태명 (발송대기/발송승인/발송중/발송완료 )
	private String gubun;   		// 004: 카카오톡, 그외 :문자
	
	private int weekDay;			// 요일 0: 일요일 ~ 6 : 토요일
	private String displayYmd;		// 날짜 표현 목 7/21 	 	
	private String scheduleDesc;	// 문자명[수신자그룹](등록자)
	 	
	private String dayIndex;		// 결과값 표현위한 인덱스
	private int schCnt;				// 예약 
	private int ingCnt;				// 진행중 
	private int sucCnt;				// 발송성공
	private int endCnt;				// 완료건
	private int totCnt;				// 전체건수 
	
	private String schDesc;			// 예약 
	private String ingDesc;			// 진행중  
	private String sucDesc;			// 완료 
	private String endDesc;			// 완료
	
	//월집계용 
	private int statusCnt;			// 메일 발송 상태별 집계
	private String today;			//오늘여부
	private String displayYmdFull;	// 일자별
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
	public String getSmsName() {
		return smsName;
	}
	public void setSmsName(String smsName) {
		this.smsName = smsName;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getTaskNm() {
		return taskNm;
	}
	public void setTaskNm(String taskNm) {
		this.taskNm = taskNm;
	}
	public String getSegNm() {
		return segNm;
	}
	public void setSegNm(String segNm) {
		this.segNm = segNm;
	}
	public String getCampNm() {
		return campNm;
	}
	public void setCampNm(String campNm) {
		this.campNm = campNm;
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
	public int getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
	}
	public String getDisplayYmd() {
		return displayYmd;
	}
	public void setDisplayYmd(String displayYmd) {
		this.displayYmd = displayYmd;
	}
	public String getScheduleDesc() {
		return scheduleDesc;
	}
	public void setScheduleDesc(String scheduleDesc) {
		this.scheduleDesc = scheduleDesc;
	}
	public String getDayIndex() {
		return dayIndex;
	}
	public void setDayIndex(String dayIndex) {
		this.dayIndex = dayIndex;
	}
	public int getSchCnt() {
		return schCnt;
	}
	public void setSchCnt(int schCnt) {
		this.schCnt = schCnt;
	}
	public int getIngCnt() {
		return ingCnt;
	}
	public void setIngCnt(int ingCnt) {
		this.ingCnt = ingCnt;
	}
	public int getSucCnt() {
		return sucCnt;
	}
	public void setSucCnt(int sucCnt) {
		this.sucCnt = sucCnt;
	}
	public int getEndCnt() {
		return endCnt;
	}
	public void setEndCnt(int endCnt) {
		this.endCnt = endCnt;
	}
	public int getTotCnt() {
		return totCnt;
	}
	public void setTotCnt(int totCnt) {
		this.totCnt = totCnt;
	}
	public String getSchDesc() {
		return schDesc;
	}
	public void setSchDesc(String schDesc) {
		this.schDesc = schDesc;
	}
	public String getIngDesc() {
		return ingDesc;
	}
	public void setIngDesc(String ingDesc) {
		this.ingDesc = ingDesc;
	}
	public String getSucDesc() {
		return sucDesc;
	}
	public void setSucDesc(String sucDesc) {
		this.sucDesc = sucDesc;
	}
	public String getEndDesc() {
		return endDesc;
	}
	public void setEndDesc(String endDesc) {
		this.endDesc = endDesc;
	}
	public int getStatusCnt() {
		return statusCnt;
	}
	public void setStatusCnt(int statusCnt) {
		this.statusCnt = statusCnt;
	}
	public String getToday() {
		return today;
	}
	public void setToday(String today) {
		this.today = today;
	}
	public String getDisplayYmdFull() {
		return displayYmdFull;
	}
	public void setDisplayYmdFull(String displayYmdFull) {
		this.displayYmdFull = displayYmdFull;
	}
}
