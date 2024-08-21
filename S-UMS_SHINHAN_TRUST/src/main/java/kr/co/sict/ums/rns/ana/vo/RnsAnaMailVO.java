/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.22
 * 설명 : RNS 메일별 통계 VO
 */
package kr.co.sict.ums.rns.ana.vo;

public class RnsAnaMailVO {
	private int mid;				//메시지아이디
	private int subid;			//재발송아이디
	private int tid;					//서비스아이디
	private String subject;	//메일제목
	private String smail;		//발송자 메일주소
	private String cdate;		//등록일시
	private String sdate;		//발송일시
	private String tname;	//서비스구분명
	private String reYn;		//재발송여부 subId 0 이면 재발송 아님 
	//검색조건
	private String searchStdDt;		// 검색시작일
	private String searchEndDt;		// 검색종료일 
	private String searchService;		// 검색상태 
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
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSmail() {
		return smail;
	}
	public void setSmail(String smail) {
		this.smail = smail;
	}
	public String getCdate() {
		return cdate;
	}
	public void setCdate(String cdate) {
		this.cdate = cdate;
	}
	public String getSdate() {
		return sdate;
	}
	public void setSdate(String sdate) {
		this.sdate = sdate;
	}
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public String getReYn() {
		return reYn;
	}
	public void setReYn(String reYn) {
		this.reYn = reYn;
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
	public String getSearchService() {
		return searchService;
	}
	public void setSearchService(String searchService) {
		this.searchService = searchService;
	}
  
	
}
