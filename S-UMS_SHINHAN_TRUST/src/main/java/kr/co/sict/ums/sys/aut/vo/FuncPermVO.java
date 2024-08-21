/**
 * 작성자 : 손장호
 * 작성일시 : 2022.01.13
 * 설명 : 사용자 권한 (권한 매핑) 
 */
package kr.co.sict.ums.sys.aut.vo;

public class FuncPermVO {
	private String funcCd;
	private String userId;		// 사용자ID
	private String userNm;		// 사용자명
	private int deptNo;			// 부서번호
	private String deptNm;		// 부서명
	private String permYn;		// 권한여부
	private String orgCd;
	private String orgNm;
	private String jobGb;
	private String jobNm;
	private String arrUserIds[]; 
	private String userIds;
	private String deptNos;
	private String uilang;
	private int pagePerm;
	private int pageGrpPerm;
	
	public int getPagePerm() {
		return pagePerm;
	}
	public void setPagePerm(int pagePerm) {
		this.pagePerm = pagePerm;
	}
	public int getPageGrpPerm() {
		return pageGrpPerm;
	}
	public void setPageGrpPerm(int pageGrpPerm) {
		this.pageGrpPerm = pageGrpPerm;
	}
	public String getFuncCd() {
		return funcCd;
	}
	public void setFuncCd(String funcCd) {
		this.funcCd = funcCd;
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
	public String getOrgCd() {
		return orgCd;
	}
	public void setOrgCd(String orgCd) {
		this.orgCd = orgCd;
	}
	public String getOrgNm() {
		return orgNm;
	}
	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}
	public String getJobGb() {
		return jobGb;
	}
	public void setJobGb(String jobGb) {
		this.jobGb = jobGb;
	}
	public String getJobNm() {
		return jobNm;
	}
	public void setJobNm(String jobNm) {
		this.jobNm = jobNm;
	}
	public String[] getArrUserIds() {
		return arrUserIds;
	}
	public void setArrUserIds(String[] arrUserIds) {
		this.arrUserIds = arrUserIds;
	}
	public String getUserIds() {
		return userIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = userIds;
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
