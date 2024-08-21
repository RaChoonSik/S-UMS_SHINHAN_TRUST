package kr.co.sict.ums.lgn.vo;

public class LoginHistVO {
	public int lgnHistId;	// 로그인이력일련번호
	public int deptNo;		// 부서번호
	public String deptNm;	// 부서명
	public String userId;	// 사용자아이디
	public String userNm;	// 사용자명
	public String lgnDt;	// 로그인시간
	public String lgnIp;	// 로그인IP
	public int getLgnHistId() {
		return lgnHistId;
	}
	public void setLgnHistId(int lgnHistId) {
		this.lgnHistId = lgnHistId;
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
	public String getLgnDt() {
		return lgnDt;
	}
	public void setLgnDt(String lgnDt) {
		this.lgnDt = lgnDt;
	}
	public String getLgnIp() {
		return lgnIp;
	}
	public void setLgnIp(String lgnIp) {
		this.lgnIp = lgnIp;
	}
}
