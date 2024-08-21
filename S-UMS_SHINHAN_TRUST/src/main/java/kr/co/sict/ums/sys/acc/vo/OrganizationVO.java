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

public class OrganizationVO extends CommonVO {
	private String orgCd;
	private String orgKorNm;
	private String orgEngNm;
	private String upOrgCd;
	private String upOrgNm;
	private String orgLeaderId;
	private String orgtypGb;
	private String lvlVal;
	private String useYn;
	private String upId;
	private String upDt;
	private String regId;
	private String regDt;
	private String orgNm;
	private int childCnt;		// 하위조직수
	private String regNm;		// 등록자이름
	private String upNm;		// 수정자이름	

	// 검색	
	private String searchOrgCd;			// 검색부서코드
	private String searchOrgNm;			// 검색부서명
	private String searchUpOrgCd;		// 상위부서코드
	private String searchLvlVal;		// 등급
	private String uilang;				// 언어권
	private String orgCds;				// 부서코드리스트	
	private String arrSearchOrgCds[];
	public String getOrgCd() {
		return orgCd;
	}
	public void setOrgCd(String orgCd) {
		this.orgCd = orgCd;
	}
	public String getOrgKorNm() {
		return orgKorNm;
	}
	public void setOrgKorNm(String orgKorNm) {
		this.orgKorNm = orgKorNm;
	}
	public String getOrgEngNm() {
		return orgEngNm;
	}
	public void setOrgEngNm(String orgEngNm) {
		this.orgEngNm = orgEngNm;
	}
	public String getUpOrgCd() {
		return upOrgCd;
	}
	public void setUpOrgCd(String upOrgCd) {
		this.upOrgCd = upOrgCd;
	}
	public String getUpOrgNm() {
		return upOrgNm;
	}
	public void setUpOrgNm(String upOrgNm) {
		this.upOrgNm = upOrgNm;
	}
	public String getOrgLeaderId() {
		return orgLeaderId;
	}
	public void setOrgLeaderId(String orgLeaderId) {
		this.orgLeaderId = orgLeaderId;
	}
	public String getOrgtypGb() {
		return orgtypGb;
	}
	public void setOrgtypGb(String orgtypGb) {
		this.orgtypGb = orgtypGb;
	}
	public String getLvlVal() {
		return lvlVal;
	}
	public void setLvlVal(String lvlVal) {
		this.lvlVal = lvlVal;
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
	public String getOrgNm() {
		return orgNm;
	}
	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}
	public int getChildCnt() {
		return childCnt;
	}
	public void setChildCnt(int childCnt) {
		this.childCnt = childCnt;
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
	public String getSearchOrgCd() {
		return searchOrgCd;
	}
	public void setSearchOrgCd(String searchOrgCd) {
		this.searchOrgCd = searchOrgCd;
	}
	public String getSearchOrgNm() {
		return searchOrgNm;
	}
	public void setSearchOrgNm(String searchOrgNm) {
		this.searchOrgNm = searchOrgNm;
	}
	public String getSearchUpOrgCd() {
		return searchUpOrgCd;
	}
	public void setSearchUpOrgCd(String searchUpOrgCd) {
		this.searchUpOrgCd = searchUpOrgCd;
	}
	public String getSearchLvlVal() {
		return searchLvlVal;
	}
	public void setSearchLvlVal(String searchLvlVal) {
		this.searchLvlVal = searchLvlVal;
	}
	public String getUilang() {
		return uilang;
	}
	public void setUilang(String uilang) {
		this.uilang = uilang;
	}
	public String getOrgCds() {
		return orgCds;
	}
	public void setOrgCds(String orgCds) {
		this.orgCds = orgCds;
	}
	public String[] getArrSearchOrgCds() {
		return arrSearchOrgCds;
	}
	public void setArrSearchOrgCds(String[] arrSearchOrgCds) {
		this.arrSearchOrgCds = arrSearchOrgCds;
	}
	 
	
}
	