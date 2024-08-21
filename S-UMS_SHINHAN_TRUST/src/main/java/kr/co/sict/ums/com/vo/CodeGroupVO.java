/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 코드그룹 VO
 */
package kr.co.sict.ums.com.vo;

public class CodeGroupVO {
	private String cdGrp;		// 코드그룹
	private String cdGrpNm;		// 코드그룹명
	private String cdGrpDtl;	// 코드그룸설명
	private String uilang;		// 언어권
	private String useYn;		// 사용여부
	
	public String getCdGrp() {
		return cdGrp;
	}
	public void setCdGrp(String cdGrp) {
		this.cdGrp = cdGrp;
	}
	public String getCdGrpNm() {
		return cdGrpNm;
	}
	public void setCdGrpNm(String cdGrpNm) {
		this.cdGrpNm = cdGrpNm;
	}
	public String getCdGrpDtl() {
		return cdGrpDtl;
	}
	public void setCdGrpDtl(String cdGrpDtl) {
		this.cdGrpDtl = cdGrpDtl;
	}
	public String getUilang() {
		return uilang;
	}
	public void setUilang(String uilang) {
		this.uilang = uilang;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
}
