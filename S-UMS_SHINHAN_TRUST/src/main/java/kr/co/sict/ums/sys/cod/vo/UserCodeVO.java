/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.16
 * 설명 : 캠페인 목적관리(코드) VO ==> 사용자에 의한 코드 관리 VO
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : sys.vo -->sys.cod.vo 
 */
package kr.co.sict.ums.sys.cod.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class UserCodeVO extends CommonVO {
	private String cdGrp;		// 코드그룹
	private String cdGrpNm;		// 코드그룹명
	private String cd;			// 코드
	private String cdNm;		// 코드명
	private String uilang;		// 언어권
	private String uilangNm;	// 언어권명
	private String cdDtl;		// 코드설명
	private String useYn;		// 사용여부
	private String sysYn;		// 시스템여부
	private String upCd;		// 상위코드
	private String upCdNm;		// 상위코드명
	private String upId;		// 수정자
	private String upDt;		// 수정일
	private String regId;		// 등록자
	private String regDt;		// 등록일	
	private String regNm;		// 등록자이름
	private String upNm;		// 수정자이름
	private int sortNo;			// 정렬순번 
	
	// 검색
	private String searchCdGrpNm;	// 코드그룹명 검색
	private String searchCdGrp;		// 코드그룹 검색
	private String searchUiLang;	// 언어 검색
	private String cds;				// 코드리스트
	public String getCdGrp() {
		return cdGrp;
	}
	public void setCdGrp(String cdGrp) {
		this.cdGrp = cdGrp;
	}
	public String getCdGrpNm() {
		return cdGrpNm;
	}
	public void setCdGrpNm(String cdGrpNm) {
		this.cdGrpNm = cdGrpNm;
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
	public String getUilangNm() {
		return uilangNm;
	}
	public void setUilangNm(String uilangNm) {
		this.uilangNm = uilangNm;
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
	public String getSysYn() {
		return sysYn;
	}
	public void setSysYn(String sysYn) {
		this.sysYn = sysYn;
	}
	public String getUpCd() {
		return upCd;
	}
	public void setUpCd(String upCd) {
		this.upCd = upCd;
	}
	public String getUpCdNm() {
		return upCdNm;
	}
	public void setUpCdNm(String upCdNm) {
		this.upCdNm = upCdNm;
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
	public String getSearchCdGrpNm() {
		return searchCdGrpNm;
	}
	public void setSearchCdGrpNm(String searchCdGrpNm) {
		this.searchCdGrpNm = searchCdGrpNm;
	}
	public String getSearchCdGrp() {
		return searchCdGrp;
	}
	public void setSearchCdGrp(String searchCdGrp) {
		this.searchCdGrp = searchCdGrp;
	}
	public String getSearchUiLang() {
		return searchUiLang;
	}
	public void setSearchUiLang(String searchUiLang) {
		this.searchUiLang = searchUiLang;
	}
	public String getCds() {
		return cds;
	}
	public void setCds(String cds) {
		this.cds = cds;
	}
	public int getSortNo() {
		return sortNo;
	}
	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}
	
 
}
