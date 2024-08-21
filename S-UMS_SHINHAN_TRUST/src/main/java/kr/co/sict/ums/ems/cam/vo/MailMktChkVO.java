package kr.co.sict.ums.ems.cam.vo;

public class MailMktChkVO {
	private int taskNo;			// 주업무번호
	private String mailMktGb;	// 마케팅수신유형
	private String mailMktNm;	// 마케팅수신유형명
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
