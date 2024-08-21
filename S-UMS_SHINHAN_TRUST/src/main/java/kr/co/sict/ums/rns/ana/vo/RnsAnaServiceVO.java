/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.22
 * 설명 : RNS 서비스별 통계 VO
 */
package kr.co.sict.ums.rns.ana.vo;

public class RnsAnaServiceVO {
	private int tid;				// 서비스 ID
	private String tnm;			// 서비스명
	private int send;				// 발송건수
	private int success;			// 성공건수
	private int failed;				// 실패건수
	private int reply;				// 응답건수
	private String displaySuccess;   // 표기되는 성공(%)
	private String displayFailed;    // 표기되는 실패(%)
	private String displayReply;     // 표기되는 응답(%)
			
	//검색조건
	private String searchStdDt;		// 검색시작일
	private String searchEndDt;		// 검색종료일
	private String searchStatus;		// 검색상태
	private String searchSname;		// 발송자명
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public String getTnm() {
		return tnm;
	}
	public void setTnm(String tnm) {
		this.tnm = tnm;
	}
	public int getSend() {
		return send;
	}
	public void setSend(int send) {
		this.send = send;
	}
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public int getFailed() {
		return failed;
	}
	public void setFailed(int failed) {
		this.failed = failed;
	}
	public int getReply() {
		return reply;
	}
	public void setReply(int reply) {
		this.reply = reply;
	}
	public String getDisplaySuccess() {
		return displaySuccess;
	}
	public void setDisplaySuccess(String displaySuccess) {
		this.displaySuccess = displaySuccess;
	}
	public String getDisplayFailed() {
		return displayFailed;
	}
	public void setDisplayFailed(String displayFailed) {
		this.displayFailed = displayFailed;
	}
	public String getDisplayReply() {
		return displayReply;
	}
	public void setDisplayReply(String displayReply) {
		this.displayReply = displayReply;
	}
	public String getSearchStdDt() {
		return searchStdDt;
	}
	public void setSearchStdDt(String searchStdDt) {
		this.searchStdDt = searchStdDt;
	}
	public String getSearchEndDt() {
		return searchEndDt;
	}
	public void setSearchEndDt(String searchEndDt) {
		this.searchEndDt = searchEndDt;
	}
	public String getSearchStatus() {
		return searchStatus;
	}
	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}
	public String getSearchSname() {
		return searchSname;
	}
	public void setSearchSname(String searchSname) {
		this.searchSname = searchSname;
	}
	
 
	 
}
