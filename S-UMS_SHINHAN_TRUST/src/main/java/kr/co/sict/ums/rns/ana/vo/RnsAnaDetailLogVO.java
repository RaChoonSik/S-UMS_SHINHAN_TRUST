/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.22
 * 설명 : RNS 상세로그 VO
 */
package kr.co.sict.ums.rns.ana.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class RnsAnaDetailLogVO  extends CommonVO {
	private int mid;
	private int subid;
	private int tid; 
	private String sdate;
	private String cdate;
	private String rsdate;
	private String tnm;
	private String subject;
	private String rid;
	private String rname;
	private String rmail;
	private String sid;
	private String sname;
	private String smail;
	private String refmid;
	private String status;
	private String rcode;
	private String uilang; 
	private String deptNm;
	private String contents;
	private int attchCnt; 

	//검색조건
	private String searchMid;		// 검색시작일 
	private String searchStdDt;		// 검색시작일
	private String searchEndDt;		// 검색종료일
	private String searchStatus;	// 검색상태
	private String searchService;	// 검색상태
	private String searchRcode;		// 검색상태
	private String searchSname;		// 발송자명
	private String searchRname;		// 수신자명
	private String searchRmail;		// 수신자이메일주소
	private String searchRid;		// 수신자아이디
	private String searchTid;		// 서비스별
	
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
	public String getSdate() {
		return sdate;
	}
	public void setSdate(String sdate) {
		this.sdate = sdate;
	}
	public String getCdate() {
		return cdate;
	}
	public void setCdate(String cdate) {
		this.cdate = cdate;
	}
	public String getRsdate() {
		return rsdate;
	}
	public void setRsdate(String rsdate) {
		this.rsdate = rsdate;
	}
	public String getTnm() {
		return tnm;
	}
	public void setTnm(String tnm) {
		this.tnm = tnm;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
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
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getSmail() {
		return smail;
	}
	public void setSmail(String smail) {
		this.smail = smail;
	}
	public String getRefmid() {
		return refmid;
	}
	public void setRefmid(String refmid) {
		this.refmid = refmid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRcode() {
		return rcode;
	}
	public void setRcode(String rcode) {
		this.rcode = rcode;
	}
	public String getUilang() {
		return uilang;
	}
	public void setUilang(String uilang) {
		this.uilang = uilang;
	}
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public int getAttchCnt() {
		return attchCnt;
	}
	public void setAttchCnt(int attchCnt) {
		this.attchCnt = attchCnt;
	}
	public String getSearchMid() {
		return searchMid;
	}
	public void setSearchMid(String searchMid) {
		this.searchMid = searchMid;
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
	public String getSearchService() {
		return searchService;
	}
	public void setSearchService(String searchService) {
		this.searchService = searchService;
	}
	public String getSearchRcode() {
		return searchRcode;
	}
	public void setSearchRcode(String searchRcode) {
		this.searchRcode = searchRcode;
	}
	public String getSearchSname() {
		return searchSname;
	}
	public void setSearchSname(String searchSname) {
		this.searchSname = searchSname;
	}
	public String getSearchRname() {
		return searchRname;
	}
	public void setSearchRname(String searchRname) {
		this.searchRname = searchRname;
	}
	public String getSearchRmail() {
		return searchRmail;
	}
	public void setSearchRmail(String searchRmail) {
		this.searchRmail = searchRmail;
	}
	public String getSearchRid() {
		return searchRid;
	}
	public void setSearchRid(String searchRid) {
		this.searchRid = searchRid;
	}
	public String getSearchTid() {
		return searchTid;
	}
	public void setSearchTid(String searchTid) {
		this.searchTid = searchTid;
	}
	
	
}
