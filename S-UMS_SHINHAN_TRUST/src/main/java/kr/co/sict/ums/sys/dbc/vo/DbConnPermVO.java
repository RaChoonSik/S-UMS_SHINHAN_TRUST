/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.12
 * 설명 : DB사용권한 VO
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : sys.vo -->sys.dbc.vo 
 */
package kr.co.sict.ums.sys.dbc.vo;

public class DbConnPermVO {
	private int dbConnNo;		// DB연결 일련번호
	private String dbConnNm;	// DB연결명
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
