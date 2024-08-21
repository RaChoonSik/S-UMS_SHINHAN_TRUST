/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 코드 VO
 */
package kr.co.sict.ums.com.vo;

public class CodeVO {
	private String cdGrp;	// 코드그룹ID
	private String cd;		// 코드
	private String cdNm;	// 코드명
	private String uilang;	// 언어권
	private String cdDtl;	// 코드설명
	private String useYn;	// 사용여부
	
	// 추가정보
	private String status;	// 상태
	private int deptNo;		// 부서번호
	private String deptNm;	// 부서명
	private String userId;	// 사용자아이디
	private String userNm;	// 사용자명
	
	public String getCdGrp() {
		return cdGrp;
	}
	public void setCdGrp(String cdGrp) {
		this.cdGrp = cdGrp;
	}
	public String getCd() {
		return cd;
	}
	public void setCd(String cd) {
		this.cd = cd;
	}
	public String getCdNm() {
		return cdNm;
	}
	public void setCdNm(String cdNm) {
		this.cdNm = cdNm;
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
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
}
