/**
 * 작성자 : 김준희
 * 작성일시 : 2022.01.26
 * 설명 : IF Mail Info VO
 */
package kr.co.sict.ums.ems.cam.vo;

public class TestEaiUserVO {
	//IF_MAIL_INFO
	private String ifMakeDate;			//연계생성일
	private String ifCampId;			//연계캠페인아이디
	private int recevSeqno;				//수신순번
	private String ifId;				//연계고객ID
	private String ifName;				//연계고객명
	private String ifEmail;				//연계이메일주소
	private String sendResCd;			//발송결과코드
	private String sendResCdNm;			//발송결과코드명 ('000' 정상 / 이외 실패)
	private String bizkey;				//발송결과코드명 ('000' 정상 / 이외 실패)
	//검색정보
	private String searchIfCampId;		//검색용 캠페인아이디
	private String searchIfCampNm;		//검색용 캠페인명
	private String searchIfId;			//검색용 연계고객ID 
	private String searchIfName;		//검색용 연계고객명
	private String searchIfEmail;		//검색용 연계이메일
	private int searchIfCampNo;			//검색용 캠페인번호(숫자 CAMP_NO) 

	//테스트 사용자 정보 
	private int testEaiUserNo;			// 테스트사용자번호
	private String testEaiUserNm;		// 테스트사용자명
	private String testEaiUserEm;		// 테스트사용자이메일
	private String userEaiId;			// 사용자아이디
	private String testEaiCustId;		// 수신자고객ID
	
	// 추가정보
	private String taskNm;
	private String eaiTaskNos;			// 주업무번호
	private String eaiSubTaskNos;		// 보조업무번호
	private String testEaiEmail;		// 테스트이메일주소
	private String sendDt;				// 발송일시
	private String testEaiUserNos;		// 테스트사용자번호멀티
	
	
	public String getIfMakeDate() {
		return ifMakeDate;
	}
	public void setIfMakeDate(String ifMakeDate) {
		this.ifMakeDate = ifMakeDate;
	}
	public String getIfCampId() {
		return ifCampId;
	}
	public void setIfCampId(String ifCampId) {
		this.ifCampId = ifCampId;
	}
	public int getRecevSeqno() {
		return recevSeqno;
	}
	public void setRecevSeqno(int recevSeqno) {
		this.recevSeqno = recevSeqno;
	}
	public String getIfId() {
		return ifId;
	}
	public void setIfId(String ifId) {
		this.ifId = ifId;
	}
	public String getIfName() {
		return ifName;
	}
	public void setIfName(String ifName) {
		this.ifName = ifName;
	}
	public String getIfEmail() {
		return ifEmail;
	}
	public void setIfEmail(String ifEmail) {
		this.ifEmail = ifEmail;
	}
	public String getSendResCd() {
		return sendResCd;
	}
	public void setSendResCd(String sendResCd) {
		this.sendResCd = sendResCd;
	}
	public String getSendResCdNm() {
		return sendResCdNm;
	}
	public void setSendResCdNm(String sendResCdNm) {
		this.sendResCdNm = sendResCdNm;
	}
	public String getBizkey() {
		return bizkey;
	}
	public void setBizkey(String bizkey) {
		this.bizkey = bizkey;
	}
	public String getSearchIfCampId() {
		return searchIfCampId;
	}
	public void setSearchIfCampId(String searchIfCampId) {
		this.searchIfCampId = searchIfCampId;
	}
	public String getSearchIfCampNm() {
		return searchIfCampNm;
	}
	public void setSearchIfCampNm(String searchIfCampNm) {
		this.searchIfCampNm = searchIfCampNm;
	}
	public String getSearchIfId() {
		return searchIfId;
	}
	public void setSearchIfId(String searchIfId) {
		this.searchIfId = searchIfId;
	}
	public String getSearchIfName() {
		return searchIfName;
	}
	public void setSearchIfName(String searchIfName) {
		this.searchIfName = searchIfName;
	}
	public String getSearchIfEmail() {
		return searchIfEmail;
	}
	public void setSearchIfEmail(String searchIfEmail) {
		this.searchIfEmail = searchIfEmail;
	}
	public int getSearchIfCampNo() {
		return searchIfCampNo;
	}
	public void setSearchIfCampNo(int searchIfCampNo) {
		this.searchIfCampNo = searchIfCampNo;
	}
	public int getTestEaiUserNo() {
		return testEaiUserNo;
	}
	public void setTestEaiUserNo(int testEaiUserNo) {
		this.testEaiUserNo = testEaiUserNo;
	}
	public String getTestEaiUserNm() {
		return testEaiUserNm;
	}
	public void setTestEaiUserNm(String testEaiUserNm) {
		this.testEaiUserNm = testEaiUserNm;
	}
	public String getTestEaiUserEm() {
		return testEaiUserEm;
	}
	public void setTestEaiUserEm(String testEaiUserEm) {
		this.testEaiUserEm = testEaiUserEm;
	}
	public String getUserEaiId() {
		return userEaiId;
	}
	public void setUserEaiId(String userEaiId) {
		this.userEaiId = userEaiId;
	}
	public String getTestEaiCustId() {
		return testEaiCustId;
	}
	public void setTestEaiCustId(String testEaiCustId) {
		this.testEaiCustId = testEaiCustId;
	}
	public String getTaskNm() {
		return taskNm;
	}
	public void setTaskNm(String taskNm) {
		this.taskNm = taskNm;
	}
	public String getEaiTaskNos() {
		return eaiTaskNos;
	}
	public void setEaiTaskNos(String eaiTaskNos) {
		this.eaiTaskNos = eaiTaskNos;
	}
	public String getEaiSubTaskNos() {
		return eaiSubTaskNos;
	}
	public void setEaiSubTaskNos(String eaiSubTaskNos) {
		this.eaiSubTaskNos = eaiSubTaskNos;
	}
	public String getTestEaiEmail() {
		return testEaiEmail;
	}
	public void setTestEaiEmail(String testEaiEmail) {
		this.testEaiEmail = testEaiEmail;
	}
	public String getSendDt() {
		return sendDt;
	}
	public void setSendDt(String sendDt) {
		this.sendDt = sendDt;
	}
	public String getTestEaiUserNos() {
		return testEaiUserNos;
	}
	public void setTestEaiUserNos(String testEaiUserNos) {
		this.testEaiUserNos = testEaiUserNos;
	}
}
