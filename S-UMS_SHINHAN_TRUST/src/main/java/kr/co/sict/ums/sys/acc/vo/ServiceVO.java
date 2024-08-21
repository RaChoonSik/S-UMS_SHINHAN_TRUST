/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 코드 VO
 */
package kr.co.sict.ums.sys.acc.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class ServiceVO  extends CommonVO {
	private int serviceGb;		// 서비스구분
	private String custDomain;	// 고객도메인
	private String serviceNm;	// 서비스구분명
	private String licenseKey;	// 라이센스키
	private String useYn;		// 사용여부
	private String upId;		// 수정자ID
	private String upDt;		// 수정일시
	private String regId;		// 생성자ID
	private String regDt;       // 생성일시
	private String regNm;		// 등록자이름
	private String upNm;		// 수정자이름
	
	//기타 (외부 사용)
	private int    payYn;    // 접근 가능 여부 

	public int getServiceGb() {
		return serviceGb;
	}

	public void setServiceGb(int serviceGb) {
		this.serviceGb = serviceGb;
	}

	public String getCustDomain() {
		return custDomain;
	}

	public void setCustDomain(String custDomain) {
		this.custDomain = custDomain;
	}

	public String getServiceNm() {
		return serviceNm;
	}

	public void setServiceNm(String serviceNm) {
		this.serviceNm = serviceNm;
	}

	public String getLicenseKey() {
		return licenseKey;
	}

	public void setLicenseKey(String licenseKey) {
		this.licenseKey = licenseKey;
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

	public int getPayYn() {
		return payYn;
	}

	public void setPayYn(int payYn) {
		this.payYn = payYn;
	}
	 
}
 
