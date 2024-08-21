/**
 * 작성자 : 김상진
 * 작성일시 : 2021.08.24
 * 설명 : 메일클리닉 VO
 */
package kr.co.sict.ums.ems.cam.vo;

public class EmailClinicVO {
	private int taskNo;			// 주업무번호
	private String sndTpeGb;	// 발송결과유형구분
	private String regId;		// 생성자ID
	private String regDt;		// 생성일시
	private String upId;		// 수정자ID
	private String upDt;		// 수정일시
	public int getTaskNo() {
		return taskNo;
	}
	public void setTaskNo(int taskNo) {
		this.taskNo = taskNo;
	}
	public String getSndTpeGb() {
		return sndTpeGb;
	}
	public void setSndTpeGb(String sndTpeGb) {
		this.sndTpeGb = sndTpeGb;
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
}
