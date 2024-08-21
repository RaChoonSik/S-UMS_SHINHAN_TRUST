/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.29
 * 설명 : 링크(LINK) VO
 */
package kr.co.sict.ums.ems.cam.vo;

public class LinkVO {
	private String linkNo;		// 링크번호
	private String linkUrl;		// 링크경로
	private String linkNm;		// 링크이름
	private String aliasNm;		// 링크알리아스
	private int catNo;			// Category코드
	private String linkTy;		// 링크유형
	private String regId;		// 등록자
	private String regDt;		// 등록일자
	private String upId;		// 수정자
	private String upDt;		// 수정일자
	public String getLinkNo() {
		return linkNo;
	}
	public void setLinkNo(String linkNo) {
		this.linkNo = linkNo;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getLinkNm() {
		return linkNm;
	}
	public void setLinkNm(String linkNm) {
		this.linkNm = linkNm;
	}
	public String getAliasNm() {
		return aliasNm;
	}
	public void setAliasNm(String aliasNm) {
		this.aliasNm = aliasNm;
	}
	public int getCatNo() {
		return catNo;
	}
	public void setCatNo(int catNo) {
		this.catNo = catNo;
	}
	public String getLinkTy() {
		return linkTy;
	}
	public void setLinkTy(String linkTy) {
		this.linkTy = linkTy;
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
}
