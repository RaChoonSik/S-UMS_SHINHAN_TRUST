/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.06
 * 설명 : 사용자 정보 VO
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : sys.vo -->sys.acc.vo 
 */
package kr.co.sict.ums.sys.acc.vo;

import java.io.Serializable;

public class UserOrgVO implements Serializable {
	private static final long serialVersionUID = 1L;
		
	private String userId;		// 사용자아이디
	private String orgCd;		// 조직코드
	private String positionGb;	// 직급
	private String jobGb;		// 직책
	private String upId;		// 수정자
	private String upDt;		// 수정일시
	private String regId;		// 등록자
	private String regDt;		// 등록일시
	private String regNm;		// 등록자이름
	private String upNm;		// 수정자이름
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOrgCd() {
		return orgCd;
	}
	public void setOrgCd(String orgCd) {
		this.orgCd = orgCd;
	}
	public String getPositionGb() {
		return positionGb;
	}
	public void setPositionGb(String positionGb) {
		this.positionGb = positionGb;
	}
	public String getJobGb() {
		return jobGb;
	}
	public void setJobGb(String jobGb) {
		this.jobGb = jobGb;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
 
}
