/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.22
 * 설명 : RNS 수신자별 통계 VO
 */
package kr.co.sict.ums.rns.ana.vo;

public class RnsAnaReceiverVO {
	private int mid;
	private int subid;
	private int tid;
	private String rid;
	private String rname;
	private String rmail;
	private String rcode;
	private String stime;
	private String rsdate;
	private String tname;

	//검색조건
	private String searchStdDt;		// 검색시작일
	private String searchEndDt;		// 검색종료일
	private String searchRmail;		// 이메일주소
	private String searchRname;		// 수신자명
	private String searchRid;				// 수신자ID
	private String searchRcode;		// 발송결과
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public int getSubid() {
		return subid;
	}
	public void setSubid(int subid) {
		this.subid = subid;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public String getRname() {
		return rname;
	}
	public void setRname(String rname) {
		this.rname = rname;
	}
	public String getRmail() {
		return rmail;
	}
	public void setRmail(String rmail) {
		this.rmail = rmail;
	}
	public String getRcode() {
		return rcode;
	}
	public void setRcode(String rcode) {
		this.rcode = rcode;
	}
	public String getStime() {
		return stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
	public String getRsdate() {
		return rsdate;
	}
	public void setRsdate(String rsdate) {
		this.rsdate = rsdate;
	}
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
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
	public String getSearchRmail() {
		return searchRmail;
	}
	public void setSearchRmail(String searchRmail) {
		this.searchRmail = searchRmail;
	}
	public String getSearchRname() {
		return searchRname;
	}
	public void setSearchRname(String searchRname) {
		this.searchRname = searchRname;
	}
	public String getSearchRid() {
		return searchRid;
	}
	public void setSearchRid(String searchRid) {
		this.searchRid = searchRid;
	}
	public String getSearchRcode() {
		return searchRcode;
	}
	public void setSearchRcode(String searchRcode) {
		this.searchRcode = searchRcode;
	}
	
  
}
