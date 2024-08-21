/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.12
 * 설명 : DB Connection VO
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : sys.vo -->sys.dbc.vo 
 */
package kr.co.sict.ums.sys.dbc.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class DbConnVO extends CommonVO {
	private int dbConnNo;			// DB Connection번호
	private String dbConnNm;		// DB Connection명
	private String dbTy;			// DBMS 유형
	private String dbTyNm;			// DBMS 유형명
	private String dbDriver;		// DB Driver
	private String dbUrl;			// DB 연결 URL
	private String loginId;			// 로그인 아이디
	private String loginPwd;		// 비밀번호
	private String dbCharSet;		// 문자셋
	private String dbCharSetNm;		// 문자셋명
	private String dbConnDesc;		// 설명
	private String status;			// 상태
	private String statusNm;		// 상태명
	private String regId;			// 등록자
	private String regDt;			// 등록일자
	private String upId;			// 수정자
	private String upDt;			// 수정일자
	private String regNm;		// 등록자이름
	private String upNm;		// 수정자이름	
	
	// 검색
	private String searchDbConnNo;	// 검색CONNECTION뻔호
	private String searchDbConnNm;	// 검색CONNECTION명
	private String searchDbTy;		// 검색DB유형
	private String searchStatus;	// 검색상태코드
	private String uilang;			// 언어권
	private String dbConnNos;		// DB Connection번호 여러개 
	
	// 추가정보
	private String userId;			// 사용자아이디
	private String adminYn;			// 관리자여부
	private int deptNo;          // 사용자부서번호
	
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
	public String getDbTy() {
		return dbTy;
	}
	public void setDbTy(String dbTy) {
		this.dbTy = dbTy;
	}
	public String getDbTyNm() {
		return dbTyNm;
	}
	public void setDbTyNm(String dbTyNm) {
		this.dbTyNm = dbTyNm;
	}
	public String getDbDriver() {
		return dbDriver;
	}
	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}
	public String getDbUrl() {
		return dbUrl;
	}
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getLoginPwd() {
		return loginPwd;
	}
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}
	public String getDbCharSet() {
		return dbCharSet;
	}
	public void setDbCharSet(String dbCharSet) {
		this.dbCharSet = dbCharSet;
	}
	public String getDbCharSetNm() {
		return dbCharSetNm;
	}
	public void setDbCharSetNm(String dbCharSetNm) {
		this.dbCharSetNm = dbCharSetNm;
	}
	public String getDbConnDesc() {
		return dbConnDesc;
	}
	public void setDbConnDesc(String dbConnDesc) {
		this.dbConnDesc = dbConnDesc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusNm() {
		return statusNm;
	}
	public void setStatusNm(String statusNm) {
		this.statusNm = statusNm;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	public String getUpId() {
		return upId;
	}
	public void setUpId(String upId) {
		this.upId = upId;
	}
	public String getUpDt() {
		return upDt;
	}
	public void setUpDt(String upDt) {
		this.upDt = upDt;
	}
	public String getRegNm() {
		return regNm;
	}
	public void setRegNm(String regNm) {
		this.regNm = regNm;
	}
	public String getUpNm() {
		return upNm;
	}
	public void setUpNm(String upNm) {
		this.upNm = upNm;
	}
	public String getSearchDbConnNo() {
		return searchDbConnNo;
	}
	public void setSearchDbConnNo(String searchDbConnNo) {
		this.searchDbConnNo = searchDbConnNo;
	}
	public String getSearchDbConnNm() {
		return searchDbConnNm;
	}
	public void setSearchDbConnNm(String searchDbConnNm) {
		this.searchDbConnNm = searchDbConnNm;
	}
	public String getSearchDbTy() {
		return searchDbTy;
	}
	public void setSearchDbTy(String searchDbTy) {
		this.searchDbTy = searchDbTy;
	}
	public String getSearchStatus() {
		return searchStatus;
	}
	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}
	public String getUilang() {
		return uilang;
	}
	public void setUilang(String uilang) {
		this.uilang = uilang;
	}
	public String getDbConnNos() {
		return dbConnNos;
	}
	public void setDbConnNos(String dbConnNos) {
		this.dbConnNos = dbConnNos;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAdminYn() {
		return adminYn;
	}
	public void setAdminYn(String adminYn) {
		this.adminYn = adminYn;
	}
	public int getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(int deptNo) {
		this.deptNo = deptNo;
	}
 
}
