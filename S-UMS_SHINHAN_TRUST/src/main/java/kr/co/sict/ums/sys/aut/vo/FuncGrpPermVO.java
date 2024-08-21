/**
 * 작성자 : 손장호
 * 작성일시 : 2022.01.13
 * 설명 : 그룹 권한 (사용자 그룹)
 */
package kr.co.sict.ums.sys.aut.vo;

public class FuncGrpPermVO {
	
	private String funcCd;	    
	private int deptNo;			// 부서번호
	private String deptNm;		// 부서명
	private String permYn;		// 권한여부
	private int arrDeptNos[];
	private String deptNos;
	private String uilang;
	
	public String getFuncCd() {
		return funcCd;
	}
	public void setFuncCd(String funcCd) {
		this.funcCd = funcCd;
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
