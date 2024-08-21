/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.15
 * 설명 : 메타 관계값 VO
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : sys.vo -->sys.dbc.vo 
 */
package kr.co.sict.ums.sys.dbc.vo;

public class MetaValueVO {
	private int valueNo;		// 관계값번호
	private String valueNm;		// 관계값명
	private String valueAlias;	// 관계값별칭
	private int colNo;			// 컬럼번호
	private String colNm;		// 컬럼명
	
	public int getValueNo() {
		return valueNo;
	}
	public void setValueNo(int valueNo) {
		this.valueNo = valueNo;
	}
	public String getValueNm() {
		return valueNm;
	}
	public void setValueNm(String valueNm) {
		this.valueNm = valueNm;
	}
	public String getValueAlias() {
		return valueAlias;
	}
	public void setValueAlias(String valueAlias) {
		this.valueAlias = valueAlias;
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
}
