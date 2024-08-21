/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.08
 * 설명 : RNS도메인정보 VO
 */
package kr.co.sict.ums.sys.rns.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class DomainInfoVO extends CommonVO {
	private int domainId;
	private String domainName;
	private String upId;
	private String upNm;
	private String upDt;
	private String regId;
	private String regNm;
	private String regDt;
	
	// 검색
	private String searchDomainName; // 검색 도메인명 
	private String domainIds; 		 // 도메인ID 리스트
	private int[] arrDomainId; 	 // 도메인ID 리스트 배열
	private String uilang;		//언어권
	public int getDomainId() {
		return domainId;
	}
	public void setDomainId(int domainId) {
		this.domainId = domainId;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getUpId() {
		return upId;
	}
	public void setUpId(String upId) {
		this.upId = upId;
	}
	public String getUpNm() {
		return upNm;
	}
	public void setUpNm(String upNm) {
		this.upNm = upNm;
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
	public String getRegNm() {
		return regNm;
	}
	public void setRegNm(String regNm) {
		this.regNm = regNm;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	public String getSearchDomainName() {
		return searchDomainName;
	}
	public void setSearchDomainName(String searchDomainName) {
		this.searchDomainName = searchDomainName;
	}
	public String getDomainIds() {
		return domainIds;
	}
	public void setDomainIds(String domainIds) {
		this.domainIds = domainIds;
	}
	public int[] getArrDomainId() {
		return arrDomainId;
	}
	public void setArrDomainId(int[] arrDomainId) {
		this.arrDomainId = arrDomainId;
	}
	public String getUilang() {
		return uilang;
	}
	public void setUilang(String uilang) {
		this.uilang = uilang;
	}	
}
