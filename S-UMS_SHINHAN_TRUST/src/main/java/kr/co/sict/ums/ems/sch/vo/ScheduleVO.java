/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.08
 * 설명 : Schedule VO
 */
package kr.co.sict.ums.ems.sch.vo;

import java.util.List;

import kr.co.sict.ums.com.vo.CommonVO;

public class ScheduleVO extends CommonVO {
	
	//주 또는 일 결과값용  
	private String sendDt;			// 일자
	private String displayYmd;		// 날짜 표현 목 7/21 
	private int weekDay;			// 요일 0: 일요일 ~ 6 : 토요일 	
	private int taskNo;				// 주업무번호
	private int subTaskNo;			// 보조업무번호	
	private String sendRepeat;		// 발송유형(일회:000,정기:001)
	private String sendTimeRepeat;  // 시간 + [단기]
	private String mailTitle;		// 메일제목
	private String scheduleDesc;	// TASK명[수신자그룹](등록자)
	private String workStatusNm;   	// 발송상태명 
	private String workStatus;		// 코드값에 따라 예약. 진행중 ,완료 여부 표기 
	private String dayIndex;		// 결과값 표현위한 인덱스
	private int schCnt;			// 예약 
	private int ingCnt;			// 진행중 
	private int sucCnt;			// 성공
	private int failCnt;		// 실패
	private int endCnt;			// 완료건
	private int appCnt;			// 결재대기
	private int aigCnt;			// 결재진행
	private int rejCnt;			// 결재반려
	private int afnCnt;			// 결재완료
	private int totCnt;			// 전체건수
	private int approvalCnt;	// 결재관련상태인것 건수 
	
	private String schDesc;			// 예약 
	private String ingDesc;			// 진행중 
	private String sucDesc;			// 성공
	private String failDesc;		// 실패
	private String endDesc;			// 완료
	private String appDesc;			// 결재대기
	private String aigDesc;			// 결재진행
	private String rejDesc;			// 결재반려
	private String afnDesc;			// 결재완료
	private String approvalDesc;	// 결재관련상태인것
	
	private String approvalProcAppYn;	// 발송결재라인실승인여부
	
	//월집계용 
	private int workStatusCnt;		// 메일 발송 상태별 집계
	private String today;			//오늘여부
	private String displayYmdFull;	// 일자별
	public String getSendDt() {
		return sendDt;
	}
	public void setSendDt(String sendDt) {
		this.sendDt = sendDt;
	}
	public String getDisplayYmd() {
		return displayYmd;
	}
	public void setDisplayYmd(String displayYmd) {
		this.displayYmd = displayYmd;
	}
	public int getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
	}
	public int getTaskNo() {
		return taskNo;
	}
	public void setTaskNo(int taskNo) {
		this.taskNo = taskNo;
	}
	public int getSubTaskNo() {
		return subTaskNo;
	}
	public void setSubTaskNo(int subTaskNo) {
		this.subTaskNo = subTaskNo;
	}
	public String getSendRepeat() {
		return sendRepeat;
	}
	public void setSendRepeat(String sendRepeat) {
		this.sendRepeat = sendRepeat;
	}
	public String getSendTimeRepeat() {
		return sendTimeRepeat;
	}
	public void setSendTimeRepeat(String sendTimeRepeat) {
		this.sendTimeRepeat = sendTimeRepeat;
	}
	public String getMailTitle() {
		return mailTitle;
	}
	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}
	public String getScheduleDesc() {
		return scheduleDesc;
	}
	public void setScheduleDesc(String scheduleDesc) {
		this.scheduleDesc = scheduleDesc;
	}
	public String getWorkStatusNm() {
		return workStatusNm;
	}
	public void setWorkStatusNm(String workStatusNm) {
		this.workStatusNm = workStatusNm;
	}
	public String getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
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
	public int getAppCnt() {
		return appCnt;
	}
	public void setAppCnt(int appCnt) {
		this.appCnt = appCnt;
	}
	public int getAigCnt() {
		return aigCnt;
	}
	public void setAigCnt(int aigCnt) {
		this.aigCnt = aigCnt;
	}
	public int getRejCnt() {
		return rejCnt;
	}
	public void setRejCnt(int rejCnt) {
		this.rejCnt = rejCnt;
	}
	public int getAfnCnt() {
		return afnCnt;
	}
	public void setAfnCnt(int afnCnt) {
		this.afnCnt = afnCnt;
	}
	public int getTotCnt() {
		return totCnt;
	}
	public void setTotCnt(int totCnt) {
		this.totCnt = totCnt;
	}
	public int getApprovalCnt() {
		return approvalCnt;
	}
	public void setApprovalCnt(int approvalCnt) {
		this.approvalCnt = approvalCnt;
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
	public String getAppDesc() {
		return appDesc;
	}
	public void setAppDesc(String appDesc) {
		this.appDesc = appDesc;
	}
	public String getAigDesc() {
		return aigDesc;
	}
	public void setAigDesc(String aigDesc) {
		this.aigDesc = aigDesc;
	}
	public String getRejDesc() {
		return rejDesc;
	}
	public void setRejDesc(String rejDesc) {
		this.rejDesc = rejDesc;
	}
	public String getAfnDesc() {
		return afnDesc;
	}
	public void setAfnDesc(String afnDesc) {
		this.afnDesc = afnDesc;
	}
	public String getApprovalDesc() {
		return approvalDesc;
	}
	public void setApprovalDesc(String approvalDesc) {
		this.approvalDesc = approvalDesc;
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
	public String getApprovalProcAppYn() {
		return approvalProcAppYn;
	}
	public void setApprovalProcAppYn(String approvalProcAppYn) {
		this.approvalProcAppYn = approvalProcAppYn;
	}
}
