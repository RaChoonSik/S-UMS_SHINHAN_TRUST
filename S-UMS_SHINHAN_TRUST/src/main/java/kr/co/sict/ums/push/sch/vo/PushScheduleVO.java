/**
 * 작성자 : 김재환
 * 작성일시 : 2021.12.27
 * 설명 : Schedule VO
 */
package kr.co.sict.ums.push.sch.vo;

import java.util.List;

import kr.co.sict.ums.com.vo.CommonVO;

public class PushScheduleVO extends CommonVO {
	
	//주 또는 일 결과값용
	private int pushmessageId;	// pushmessageId
	private String pushName;		// PUSH 명
	private String sendDt;			// 발송 날짜 
	private String sendTime;		// 발송 시분 
	private String pushTitle;		// PUSH 제목
	private String segNm;			// 수신자그룹명
	private String campNm;			// 캠페인명
	private String workStatus;		// 코드값(C118) 000, 001 : 예약 002: 진행 003:완료
	private String workStatusNm;   	// 문자발송상태명 (발송대기/발송승인/발송중/발송완료 )
	
	private int weekDay;			// 요일 0: 일요일 ~ 6 : 토요일
	private String displayYmd;		// 날짜 표현 목 7/21 	 	
	private String scheduleDesc;	// PUSH명[수신자그룹](등록자)
	 	
	private String dayIndex;		// 결과값 표현위한 인덱스
	private int schCnt;				// 예약 
	private int ingCnt;				// 진행중 
	private int sucCnt;				// 발송성공
	private int failCnt;			// 실패건
	private int endCnt;				// 완료건
	private int totCnt;				// 전체건수 
	
	private String schDesc;			// 예약 
	private String ingDesc;			// 진행중  
	private String sucDesc;			// 성공완료
	private String failDesc;		// 실패완료
	private String endDesc;			// 완료
	
	//월집계용 
	private int workStatusCnt;			// 메일 발송 상태별 집계
	private String today;			//오늘여부
	private String displayYmdFull;	// 일자별
	public int getPushmessageId() {
		return pushmessageId;
	}
	public void setPushmessageId(int pushmessageId) {
		this.pushmessageId = pushmessageId;
	}
	public String getPushName() {
		return pushName;
	}
	public void setPushName(String pushName) {
		this.pushName = pushName;
	}
	public String getSendDt() {
		return sendDt;
	}
	public void setSendDt(String sendDt) {
		this.sendDt = sendDt;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getPushTitle() {
		return pushTitle;
	}
	public void setPushTitle(String pushTitle) {
		this.pushTitle = pushTitle;
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
	public int getFailCnt() {
		return failCnt;
	}
	public void setFailCnt(int failCnt) {
		this.failCnt = failCnt;
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
	public String getFailDesc() {
		return failDesc;
	}
	public void setFailDesc(String failDesc) {
		this.failDesc = failDesc;
	}
	public String getEndDesc() {
		return endDesc;
	}
	public void setEndDesc(String endDesc) {
		this.endDesc = endDesc;
	}
	public int getWorkStatusCnt() {
		return workStatusCnt;
	}
	public void setWorkStatusCnt(int workStatusCnt) {
		this.workStatusCnt = workStatusCnt;
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
