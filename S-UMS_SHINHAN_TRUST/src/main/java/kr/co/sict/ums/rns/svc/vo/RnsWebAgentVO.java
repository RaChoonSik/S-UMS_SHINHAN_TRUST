/**
 * 작성자 : 김상진
 * 작성일시 : 2021.09.01
 * 설명 : RNS 웹에이전트 VO
 */
package kr.co.sict.ums.rns.svc.vo;

public class RnsWebAgentVO {
	private int tid;			// 서비스ID
	private int attNo;			// 첨부번호
	private String sourceUrl;	// 소스경로
	private String secuAttYn;	// 보안첨부여부
	private String secuAttTyp;	// 보안첨부타입
	private String regId;		// 생성자ID
	private String regDt;		// 생성일시
	private String upId;		// 수정자ID
	private String upDt;		// 수정일시
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public int getAttNo() {
		return attNo;
	}
	public void setAttNo(int attNo) {
		this.attNo = attNo;
	}
	public String getSourceUrl() {
		return sourceUrl;
	}
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	public String getSecuAttYn() {
		return secuAttYn;
	}
	public void setSecuAttYn(String secuAttYn) {
		this.secuAttYn = secuAttYn;
	}
	public String getSecuAttTyp() {
		return secuAttTyp;
	}
	public void setSecuAttTyp(String secuAttTyp) {
		this.secuAttTyp = secuAttTyp;
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
