/**
 * 작성자 : 김상진
 * 작성일시 : 2021.08.04
 * 설명 : 통계분석 도메인별 VO
 */
package kr.co.sict.ums.ems.ana.vo;

public class MailDomainVO {
	private String domainNm;	// 도메인명
	private int sendCnt;		// 발송건수
	private int succCnt;		// 성공건수
	private int errorCnt;		// 에러건수
	public String getDomainNm() {
		return domainNm;
	}
	public void setDomainNm(String domainNm) {
		this.domainNm = domainNm;
	}
	public int getSendCnt() {
		return sendCnt;
	}
	public void setSendCnt(int sendCnt) {
		this.sendCnt = sendCnt;
	}
	public int getSuccCnt() {
		return succCnt;
	}
	public void setSuccCnt(int succCnt) {
		this.succCnt = succCnt;
	}
	public int getErrorCnt() {
		return errorCnt;
	}
	public void setErrorCnt(int errorCnt) {
		this.errorCnt = errorCnt;
	}
}
