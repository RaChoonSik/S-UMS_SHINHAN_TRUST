/**
 * 작성자 : 김상진
 * 작성일시 : 2021.08.04
 * 설명 : 테스트 발송 결과 VO
 */
package kr.co.sict.ums.ems.cam.vo;

public class SendTestLogVO {
	private int retryCnt;
	private String sendDt;
	private String sendRcode;
	private String sendMessage;
	private String custEm;
	private String sendRcodeDtl;
	public int getRetryCnt() {
		return retryCnt;
	}
	public void setRetryCnt(int retryCnt) {
		this.retryCnt = retryCnt;
	}
	public String getSendDt() {
		return sendDt;
	}
	public void setSendDt(String sendDt) {
		this.sendDt = sendDt;
	}
	public String getSendRcode() {
		return sendRcode;
	}
	public void setSendRcode(String sendRcode) {
		this.sendRcode = sendRcode;
	}
	public String getSendMessage() {
		return sendMessage;
	}
	public void setSendMessage(String sendMessage) {
		this.sendMessage = sendMessage;
	}
	public String getCustEm() {
		return custEm;
	}
	public void setCustEm(String custEm) {
		this.custEm = custEm;
	}
	public String getSendRcodeDtl() {
		return sendRcodeDtl;
	}
	public void setSendRcodeDtl(String sendRcodeDtl) {
		this.sendRcodeDtl = sendRcodeDtl;
	}
}
