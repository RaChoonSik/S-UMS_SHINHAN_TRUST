/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 상단메뉴 VO
 */package kr.co.sict.ums.main.vo;

public class MenuVO {
	/*
	A.PROG_ID, A.PROG_NM, A.PROG_TARGET,
	  A.PROG_DOMAIN, A.USE_YN, A.PROG_SCRIPT,
	  A.UILANG
	  */
	private int progId;			// 프로그램아이디
	private String progNm;		// 프로그램명
	private String progTarget;	// 프로그램대상
	private String progDomain;	// 프로그램도메인
	private String useYn;		// 사용여부
	private String progScript;	// 수행스크립트
	private String uilang;		// 언어권
	
	public int getProgId() {
		return progId;
	}
	public void setProgId(int progId) {
		this.progId = progId;
	}
	public String getProgNm() {
		return progNm;
	}
	public void setProgNm(String progNm) {
		this.progNm = progNm;
	}
	public String getProgTarget() {
		return progTarget;
	}
	public void setProgTarget(String progTarget) {
		this.progTarget = progTarget;
	}
	public String getProgDomain() {
		return progDomain;
	}
	public void setProgDomain(String progDomain) {
		this.progDomain = progDomain;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getProgScript() {
		return progScript;
	}
	public void setProgScript(String progScript) {
		this.progScript = progScript;
	}
	public String getUilang() {
		return uilang;
	}
	public void setUilang(String uilang) {
		this.uilang = uilang;
	}
}
