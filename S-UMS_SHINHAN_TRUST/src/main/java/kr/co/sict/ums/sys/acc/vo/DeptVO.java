/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 부서 정보 VO
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : sys.vo -->sys.acc.vo 
 */
package kr.co.sict.ums.sys.acc.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class DeptVO extends CommonVO {
	private int deptNo;			// 그룹코드,그룹번호
	private String deptNm;		// 그룹명
	private String deptDesc;	// 그룹설명
	private String status;		// 상태코드
	private String statusNm;	// 상태명
	private String useYn;		// 사용여부
	private String upId;		// 수정자	
	private String upDt;		// 수정일
	private String regId;		// 등록자
	private String regDt;		// 등록일
	private String regNm;		// 등록자이름
	private String upNm;		// 수정자이름
	private String dataAllYn;	// 대시보드전사데이터
	
	// 검색	
	private String searchDeptNm;	// 검색부서명
	private String searchStatus;	// 검색상태코드
	private String uilang;			// 언어권
	private int userDeptNo;			// 사용자그럽번호
	private String deptNos;		//사용자그룹 여러개
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
	public String getDeptDesc() {
		return deptDesc;
	}
	public void setDeptDesc(String deptDesc) {
		this.deptDesc = deptDesc;
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
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
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
	public String getSearchDeptNm() {
		return searchDeptNm;
	}
	public void setSearchDeptNm(String searchDeptNm) {
		this.searchDeptNm = searchDeptNm;
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
	public int getUserDeptNo() {
		return userDeptNo;
	}
	public void setUserDeptNo(int userDeptNo) {
		this.userDeptNo = userDeptNo;
	}
	public String getDeptNos() {
		return deptNos;
	}
	public void setDeptNos(String deptNos) {
		this.deptNos = deptNos;
	}
	public String getDataAllYn() {
		return dataAllYn;
	}
	public void setDataAllYn(String dataAllYn) {
		this.dataAllYn = dataAllYn;
	}
}
