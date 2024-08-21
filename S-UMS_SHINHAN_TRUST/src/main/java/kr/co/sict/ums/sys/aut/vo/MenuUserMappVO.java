/**
 * 작성자 : 김준희
 * 작성일시 : 2021.08.26
 * 설명 : 사용자메뉴 권한  VO  
 */
package kr.co.sict.ums.sys.aut.vo;
 
import kr.co.sict.ums.com.vo.CommonVO;

public class MenuUserMappVO extends CommonVO {
		
	private String menuId; 
	private String menuNm;
	private String userId;
	private String regId;
	private String regDt;
	private String userNm;
	private String status;
	private String statusNm;
	private String deptCd;
	private String deptNm;
	private String orgCd;
	private String orgNm;
	private String menuCount;
	private String positionGb;
	private String positionNm;
	private String jobGb;
	private String jobNm;
	private int menuLvlVal;	
	private String functionYn;
	private String functionNm;
	private String regNm;		 
	private String upNm;		 
	
	private String parentMenuId; 
	private int childCnt; 
	private String arrMenuIds[];
	private String arrUserIds[];
	
	private String searchUserId;
	private String searchUserNm;
	private String searchStatus;
	private String searchDeptNo;
	private String menuIds;
	private String userIds;
	private String uilang;
	
	private String lv1Id;
	private String lv2Id;
	private String lv3Id;
	private String lv1Nm;
	private String lv2Nm;
	private String lv3Nm;
	private String mappYn; 
	
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getMenuNm() {
		return menuNm;
	}
	public void setMenuNm(String menuNm) {
		this.menuNm = menuNm;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
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
	public String getDeptCd() {
		return deptCd;
	}
	public void setDeptCd(String deptCd) {
		this.deptCd = deptCd;
	}
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
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
	public String getMenuCount() {
		return menuCount;
	}
	public void setMenuCount(String menuCount) {
		this.menuCount = menuCount;
	}
	public String getPositionGb() {
		return positionGb;
	}
	public void setPositionGb(String positionGb) {
		this.positionGb = positionGb;
	}
	public String getPositionNm() {
		return positionNm;
	}
	public void setPositionNm(String positionNm) {
		this.positionNm = positionNm;
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
	public int getMenuLvlVal() {
		return menuLvlVal;
	}
	public void setMenuLvlVal(int menuLvlVal) {
		this.menuLvlVal = menuLvlVal;
	}
	public String getFunctionYn() {
		return functionYn;
	}
	public void setFunctionYn(String functionYn) {
		this.functionYn = functionYn;
	}
	public String getFunctionNm() {
		return functionNm;
	}
	public void setFunctionNm(String functionNm) {
		this.functionNm = functionNm;
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
	public String getParentMenuId() {
		return parentMenuId;
	}
	public void setParentMenuId(String parentMenuId) {
		this.parentMenuId = parentMenuId;
	}
	public int getChildCnt() {
		return childCnt;
	}
	public void setChildCnt(int childCnt) {
		this.childCnt = childCnt;
	}
	public String[] getArrMenuIds() {
		return arrMenuIds;
	}
	public void setArrMenuIds(String[] arrMenuIds) {
		this.arrMenuIds = arrMenuIds;
	}
	public String[] getArrUserIds() {
		return arrUserIds;
	}
	public void setArrUserIds(String[] arrUserIds) {
		this.arrUserIds = arrUserIds;
	}
	public String getSearchUserId() {
		return searchUserId;
	}
	public void setSearchUserId(String searchUserId) {
		this.searchUserId = searchUserId;
	}
	public String getSearchUserNm() {
		return searchUserNm;
	}
	public void setSearchUserNm(String searchUserNm) {
		this.searchUserNm = searchUserNm;
	}
	public String getSearchStatus() {
		return searchStatus;
	}
	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}
	public String getSearchDeptNo() {
		return searchDeptNo;
	}
	public void setSearchDeptNo(String searchDeptNo) {
		this.searchDeptNo = searchDeptNo;
	}
	public String getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
	public String getUserIds() {
		return userIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	public String getUilang() {
		return uilang;
	}
	public void setUilang(String uilang) {
		this.uilang = uilang;
	}
	public String getLv1Id() {
		return lv1Id;
	}
	public void setLv1Id(String lv1Id) {
		this.lv1Id = lv1Id;
	}
	public String getLv2Id() {
		return lv2Id;
	}
	public void setLv2Id(String lv2Id) {
		this.lv2Id = lv2Id;
	}
	public String getLv3Id() {
		return lv3Id;
	}
	public void setLv3Id(String lv3Id) {
		this.lv3Id = lv3Id;
	}
	public String getLv1Nm() {
		return lv1Nm;
	}
	public void setLv1Nm(String lv1Nm) {
		this.lv1Nm = lv1Nm;
	}
	public String getLv2Nm() {
		return lv2Nm;
	}
	public void setLv2Nm(String lv2Nm) {
		this.lv2Nm = lv2Nm;
	}
	public String getLv3Nm() {
		return lv3Nm;
	}
	public void setLv3Nm(String lv3Nm) {
		this.lv3Nm = lv3Nm;
	}
	public String getMappYn() {
		return mappYn;
	}
	public void setMappYn(String mappYn) {
		this.mappYn = mappYn;
	}
	 

}
