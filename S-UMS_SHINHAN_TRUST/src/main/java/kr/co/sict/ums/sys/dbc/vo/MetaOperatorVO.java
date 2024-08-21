/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.15
 * 설명 : 메타 관계식 VO
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : sys.vo -->sys.dbc.vo 
 */
package kr.co.sict.ums.sys.dbc.vo;

public class MetaOperatorVO {
	private int operNo;			// 관계식번호
	private int colNo;			// 컬럼번호
	private String colNm;		// 컬럼명
	private String operCd;		// 관계식코드
	private String operNm;		// 관계식명
	private int tblNo;			// 테이블번호
	private String tblNm;			// 테이블명
	
	// 추가정보
	private String uilang;		// 언어권
	private String cdDtl;		// 코드상세
	private String operAlias;	// 코드상세(별칭)
	public int getOperNo() {
		return operNo;
	}
	public void setOperNo(int operNo) {
		this.operNo = operNo;
	}
	public int getColNo() {
		return colNo;
	}
	public void setColNo(int colNo) {
		this.colNo = colNo;
	}
	public String getColNm() {
		return colNm;
	}
	public void setColNm(String colNm) {
		this.colNm = colNm;
	}
	public String getOperCd() {
		return operCd;
	}
	public void setOperCd(String operCd) {
		this.operCd = operCd;
	}
	public String getOperNm() {
		return operNm;
	}
	public void setOperNm(String operNm) {
		this.operNm = operNm;
	}
	public int getTblNo() {
		return tblNo;
	}
	public void setTblNo(int tblNo) {
		this.tblNo = tblNo;
	}
	public String getTblNm() {
		return tblNm;
	}
	public void setTblNm(String tblNm) {
		this.tblNm = tblNm;
	}
	public String getUilang() {
		return uilang;
	}
	public void setUilang(String uilang) {
		this.uilang = uilang;
	}
	public String getCdDtl() {
		return cdDtl;
	}
	public void setCdDtl(String cdDtl) {
		this.cdDtl = cdDtl;
	}
	public String getOperAlias() {
		return operAlias;
	}
	public void setOperAlias(String operAlias) {
		this.operAlias = operAlias;
	}
	
 
}
