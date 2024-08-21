/**
 * 작성자 : 김준희
 * 작성일시 : 2021.11.06
 * 설명 : DB사용그룹권한 VO 
 */
package kr.co.sict.ums.sys.dbc.vo;

public class DbConnGrpPermVO {
	private int dbConnNo;		// DB연결 일련번호
	private String dbConnNm;	// DB연결명
	private int deptNo;			// 부서번호
	private String deptNm;		// 부서명
	private String permYn;		// 권한여부
	private int arrDeptNos[];
	private String deptNos;
	private String uilang;
	public int getDbConnNo() {
		return dbConnNo;
	}
	public void setDbConnNo(int dbConnNo) {
		this.dbConnNo = dbConnNo;
	}
	public String getDbConnNm() {
		return dbConnNm;
	}
	public void setDbConnNm(String dbConnNm) {
		this.dbConnNm = dbConnNm;
	}
	public int getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(int deptNo) {
		this.deptNo = deptNo;
	}
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public String getPermYn() {
		return permYn;
	}
	public void setPermYn(String permYn) {
		this.permYn = permYn;
	}
	public int[] getArrDeptNos() {
		return arrDeptNos;
	}
	public void setArrDeptNos(int[] arrDeptNos) {
		this.arrDeptNos = arrDeptNos;
	}
	public String getDeptNos() {
		return deptNos;
	}
	public void setDeptNos(String deptNos) {
		this.deptNos = deptNos;
	}
	public String getUilang() {
		return uilang;
	}
	public void setUilang(String uilang) {
		this.uilang = uilang;
	}
	

}
