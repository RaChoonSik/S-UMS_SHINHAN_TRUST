/**
 * 작성자 : 김상진
 * 작성일시 : 2021.08.04
 * 설명 : 통계분석 세부에러 VO
 */
package kr.co.sict.ums.ems.ana.vo;

public class MailErrorVO {
	private String step1;		// 1단계
	private String step2;		// 2단계
	private String step3;		// 3단계
	private String nmStep1;		// 1단계명
	private String nmStep2;		// 2단계명
	private String nmStep3;		// 3단계명
	private int cntStep1;		// 2단계수
	private int cntStep2;		// 2단계수
	private int cntStep3;		// 3단계수
	private String domain;		// 도메인
	private int step1Rowspan;	// 1단계그룹핑수
	private int step2Rowspan;	// 2단계그룹핑수
	public String getStep1() {
		return step1;
	}
	public void setStep1(String step1) {
		this.step1 = step1;
	}
	public String getStep2() {
		return step2;
	}
	public void setStep2(String step2) {
		this.step2 = step2;
	}
	public String getStep3() {
		return step3;
	}
	public void setStep3(String step3) {
		this.step3 = step3;
	}
	public String getNmStep1() {
		return nmStep1;
	}
	public void setNmStep1(String nmStep1) {
		this.nmStep1 = nmStep1;
	}
	public String getNmStep2() {
		return nmStep2;
	}
	public void setNmStep2(String nmStep2) {
		this.nmStep2 = nmStep2;
	}
	public String getNmStep3() {
		return nmStep3;
	}
	public void setNmStep3(String nmStep3) {
		this.nmStep3 = nmStep3;
	}
	public int getCntStep1() {
		return cntStep1;
	}
	public void setCntStep1(int cntStep1) {
		this.cntStep1 = cntStep1;
	}
	public int getCntStep2() {
		return cntStep2;
	}
	public void setCntStep2(int cntStep2) {
		this.cntStep2 = cntStep2;
	}
	public int getCntStep3() {
		return cntStep3;
	}
	public void setCntStep3(int cntStep3) {
		this.cntStep3 = cntStep3;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public int getStep1Rowspan() {
		return step1Rowspan;
	}
	public void setStep1Rowspan(int step1Rowspan) {
		this.step1Rowspan = step1Rowspan;
	}
	public int getStep2Rowspan() {
		return step2Rowspan;
	}
	public void setStep2Rowspan(int step2Rowspan) {
		this.step2Rowspan = step2Rowspan;
	}
}
