/**
 * 작성자 : 김준희
 * 작성일시 : 2022.06.32
 * 설명 : Sms Stg VO
 */
package kr.co.sict.ums.sms.cam.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class SmsStgVO extends CommonVO {
	private String requestkey;					//메시지 요청 그룹 키
	private String requestoption;				//1 : 서비스 생성 , 그외 데이터 연동
	private String messagename;					//메시지 대표 명칭
	private String templatecode;				//메시지 템플릿 번호
	private String campaigncode;				//캠페인코드
	private String sendduedatatime;				//발송 예정일시
	private String senderphonenumber;			//발신자번호
	private String pagenumber;					//페이지 수
	private String totalpagenumber;				//총 페이지수
	private String pagesize;					//페이지 멤버수
	private String memberno;					//회원번호
	private String membername;					//회원명
	private String receivephonenumber;			//전화번호
	private String receiveemail;				//이메일
	private String receivedeviceno;				//디바이스번호
	private String encryptionkey;				//보안비밀번호
	private String businesskey;					//데이터업무키
	private String bankaccount;					//은행계좌
	private String bankname;					//은행명
	private String subject;						//제목
	private String context;						//내용
	private String depositduedatetime;			//입금기한
	private String depositorname;				//입금자명
	private String distributorname;				//판매원 이름
	private String licenseexpireddatecontext;	//자격 유지 신청일 문구
	private String moneyamount;					//금액
	private String orderdatecontext;			//제품 주문일 문구
	private String ordermonth;					//반품대상 주문 월
	private String orderno;						//주문번호
	private String storename;					//판매점명
	private String storeownerusername;			//판매점장명
	private String temporarypassword;			//임시비밀번호
	private String verificationcode;			//인증번호
	private String withdrawaldatecontext;		//자동탈퇴 예정일 문구
	private String status;//상태
	private String regId;//등록자
	private String regDt;//등록일자
	public String getRequestkey() {
		return requestkey;
	}
	public void setRequestkey(String requestkey) {
		this.requestkey = requestkey;
	}
	public String getRequestoption() {
		return requestoption;
	}
	public void setRequestoption(String requestoption) {
		this.requestoption = requestoption;
	}
	public String getMessagename() {
		return messagename;
	}
	public void setMessagename(String messagename) {
		this.messagename = messagename;
	}
	public String getTemplatecode() {
		return templatecode;
	}
	public void setTemplatecode(String templatecode) {
		this.templatecode = templatecode;
	}
	public String getCampaigncode() {
		return campaigncode;
	}
	public void setCampaigncode(String campaigncode) {
		this.campaigncode = campaigncode;
	}
	public String getSendduedatatime() {
		return sendduedatatime;
	}
	public void setSendduedatatime(String sendduedatatime) {
		this.sendduedatatime = sendduedatatime;
	}
	public String getSenderphonenumber() {
		return senderphonenumber;
	}
	public void setSenderphonenumber(String senderphonenumber) {
		this.senderphonenumber = senderphonenumber;
	}
	public String getPagenumber() {
		return pagenumber;
	}
	public void setPagenumber(String pagenumber) {
		this.pagenumber = pagenumber;
	}
	public String getTotalpagenumber() {
		return totalpagenumber;
	}
	public void setTotalpagenumber(String totalpagenumber) {
		this.totalpagenumber = totalpagenumber;
	}
	public String getPagesize() {
		return pagesize;
	}
	public void setPagesize(String pagesize) {
		this.pagesize = pagesize;
	}
	public String getMemberno() {
		return memberno;
	}
	public void setMemberno(String memberno) {
		this.memberno = memberno;
	}
	public String getMembername() {
		return membername;
	}
	public void setMembername(String membername) {
		this.membername = membername;
	}
	public String getReceivephonenumber() {
		return receivephonenumber;
	}
	public void setReceivephonenumber(String receivephonenumber) {
		this.receivephonenumber = receivephonenumber;
	}
	public String getReceiveemail() {
		return receiveemail;
	}
	public void setReceiveemail(String receiveemail) {
		this.receiveemail = receiveemail;
	}
	public String getReceivedeviceno() {
		return receivedeviceno;
	}
	public void setReceivedeviceno(String receivedeviceno) {
		this.receivedeviceno = receivedeviceno;
	}
	public String getEncryptionkey() {
		return encryptionkey;
	}
	public void setEncryptionkey(String encryptionkey) {
		this.encryptionkey = encryptionkey;
	}
	public String getBusinesskey() {
		return businesskey;
	}
	public void setBusinesskey(String businesskey) {
		this.businesskey = businesskey;
	}
	public String getBankaccount() {
		return bankaccount;
	}
	public void setBankaccount(String bankaccount) {
		this.bankaccount = bankaccount;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getDepositduedatetime() {
		return depositduedatetime;
	}
	public void setDepositduedatetime(String depositduedatetime) {
		this.depositduedatetime = depositduedatetime;
	}
	public String getDepositorname() {
		return depositorname;
	}
	public void setDepositorname(String depositorname) {
		this.depositorname = depositorname;
	}
	public String getDistributorname() {
		return distributorname;
	}
	public void setDistributorname(String distributorname) {
		this.distributorname = distributorname;
	}
	public String getLicenseexpireddatecontext() {
		return licenseexpireddatecontext;
	}
	public void setLicenseexpireddatecontext(String licenseexpireddatecontext) {
		this.licenseexpireddatecontext = licenseexpireddatecontext;
	}
	public String getMoneyamount() {
		return moneyamount;
	}
	public void setMoneyamount(String moneyamount) {
		this.moneyamount = moneyamount;
	}
	public String getOrderdatecontext() {
		return orderdatecontext;
	}
	public void setOrderdatecontext(String orderdatecontext) {
		this.orderdatecontext = orderdatecontext;
	}
	public String getOrdermonth() {
		return ordermonth;
	}
	public void setOrdermonth(String ordermonth) {
		this.ordermonth = ordermonth;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getStorename() {
		return storename;
	}
	public void setStorename(String storename) {
		this.storename = storename;
	}
	public String getStoreownerusername() {
		return storeownerusername;
	}
	public void setStoreownerusername(String storeownerusername) {
		this.storeownerusername = storeownerusername;
	}
	public String getTemporarypassword() {
		return temporarypassword;
	}
	public void setTemporarypassword(String temporarypassword) {
		this.temporarypassword = temporarypassword;
	}
	public String getVerificationcode() {
		return verificationcode;
	}
	public void setVerificationcode(String verificationcode) {
		this.verificationcode = verificationcode;
	}
	public String getWithdrawaldatecontext() {
		return withdrawaldatecontext;
	}
	public void setWithdrawaldatecontext(String withdrawaldatecontext) {
		this.withdrawaldatecontext = withdrawaldatecontext;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
}
