/**
 * 작성자 : 김상진
 * 작성일시 : 2021.08.06
 * 설명 : 시스템 메뉴 VO
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : sys.vo -->sys.acc.vo
 * 수정자 : 김상진
 * 수정일시 : 2021.08.25
 * 수정내역 : userId 추가
 */
package kr.co.sict.ums.sys.acc.vo;

import java.io.Serializable;
import java.sql.Date;

public class SysMenuVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String menuId;			// 메뉴ID
	private String menuNm;			// 메뉴명
	private int menulvlVal;			// 메뉴레벨
	private String parentmenuId;	// 상위메뉴ID
	private int sortSno;			// 정렬순번
	private int serviceGb;			// 서비스구분
	private String descTxt;			// 비고
	private String functionYn;		// 기능항목여부
	private String sourcePath;		// 소스경로
	private String useYn;			// 사용여부
	private String createId;		// 생성자ID
	private Date createDt;			// 생성일시
	private String modifyId;		// 수정자ID
	private Date modifyDt;			// 수정일시
	private String userId;			// 사용자ID
	private String regNm;		// 등록자이름
	private String upNm;		// 수정자이름
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
	public int getMenulvlVal() {
		return menulvlVal;
	}
	public void setMenulvlVal(int menulvlVal) {
		this.menulvlVal = menulvlVal;
	}
	public String getParentmenuId() {
		return parentmenuId;
	}
	public void setParentmenuId(String parentmenuId) {
		this.parentmenuId = parentmenuId;
	}
	public int getSortSno() {
		return sortSno;
	}
	public void setSortSno(int sortSno) {
		this.sortSno = sortSno;
	}
	public int getServiceGb() {
		return serviceGb;
	}
	public void setServiceGb(int serviceGb) {
		this.serviceGb = serviceGb;
	}
	public String getDescTxt() {
		return descTxt;
	}
	public void setDescTxt(String descTxt) {
		this.descTxt = descTxt;
	}
	public String getFunctionYn() {
		return functionYn;
	}
	public void setFunctionYn(String functionYn) {
		this.functionYn = functionYn;
	}
	public String getSourcePath() {
		return sourcePath;
	}
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public Date getCreateDt() {
		return createDt;
	}
	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}
	public String getModifyId() {
		return modifyId;
	}
	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}
	public Date getModifyDt() {
		return modifyDt;
	}
	public void setModifyDt(Date modifyDt) {
		this.modifyDt = modifyDt;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
 
}
