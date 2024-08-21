/**
 * 작성자 : 이혜민
 * 작성일시 : 2022.01.12
 * 설명 : 준법심의 결재라인 정보 VO
 */
package kr.co.sict.ums.sys.acc.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class ComplianceVO extends CommonVO{
	private int serviceGb;				//준법심의 결재라인 
	private int complianceNo;			//준법심의 No
	private String userId;				//사용자 아이디
	private String regId;				//등록자 아이디 
	private String regDt;				//등록 날짜
	
	private String userNm;				//사용자 이름
	private String orgCd;				//조직코드
	private String orgNm;				//조직이름
	private String positionGb;			//직급
	private String positionNm;			//직급명
	private String jobGb;				//직책
	private String jobNm;				//직책명
	
	private String userIds;
	private String uilang;
	
	public int getServiceGb() {
		return serviceGb;
	}
	public void setServiceGb(int serviceGb) {
		this.serviceGb = serviceGb;
	}
	public int getComplianceNo() {
		return complianceNo;
	}
	public void setComplianceNo(int complianceNo) {
		this.complianceNo = complianceNo;
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
}