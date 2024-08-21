/**
 * 작성자 : 김상진
 * 작성일시 : 2021.09.01
 * 설명 : RNS 메일 발송 VO
 */
package kr.co.sict.ums.rns.svc.vo;

import java.util.Date;

public class RnsMailQueueVO {
	private long mid;				// 메시지아이디
	private int subid;				// 재발송아이디
	private int tid;				// 서비스ID
	private String spos;			// 발송자소속코드
	private String sname;			// 발송자명
	private String smail;			// 발송자이메일
	private String sid;				// 발송자아이디
	private String rpos;			// 수신자그룹위치
	private String query;			// 대상자추출쿼리
	private String ctnpos;			// 컨텐츠위치
	private String subject;			// 메일제목
	private String contents;		// 컨텐츠템플릿
	private Date cdate;				// 예약생성일자
	private Date sdate;				// 메일발송일자
	private String status;			// 상태코드
	private String dbcode;			// DB연결코드
	private long refmid;			// 부모MID
	private int charset;			// 문자셋
	private String attachfile01;	// 첨부파일1
	private String attachfile02;	// 첨부파일2
	private String attachfile03;	// 첨부파일3
	private String attachfile04;	// 첨부파일4
	private String attachfile05;	// 첨부파일5
	
	private long rtyMid;			// 재발송메세지ID
	private int rtySubid;			// 재발송SUBID
	private String rtyTyp;			// 재발송유형
	
	public long getMid() {
		return mid;
	}
	public void setMid(long mid) {
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
	public String getSpos() {
		return spos;
	}
	public void setSpos(String spos) {
		this.spos = spos;
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
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getRpos() {
		return rpos;
	}
	public void setRpos(String rpos) {
		this.rpos = rpos;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getCtnpos() {
		return ctnpos;
	}
	public void setCtnpos(String ctnpos) {
		this.ctnpos = ctnpos;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public Date getCdate() {
		return cdate;
	}
	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}
	public Date getSdate() {
		return sdate;
	}
	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDbcode() {
		return dbcode;
	}
	public void setDbcode(String dbcode) {
		this.dbcode = dbcode;
	}
	public long getRefmid() {
		return refmid;
	}
	public void setRefmid(long refmid) {
		this.refmid = refmid;
	}
	public int getCharset() {
		return charset;
	}
	public void setCharset(int charset) {
		this.charset = charset;
	}
	public String getAttachfile01() {
		return attachfile01;
	}
	public void setAttachfile01(String attachfile01) {
		this.attachfile01 = attachfile01;
	}
	public String getAttachfile02() {
		return attachfile02;
	}
	public void setAttachfile02(String attachfile02) {
		this.attachfile02 = attachfile02;
	}
	public String getAttachfile03() {
		return attachfile03;
	}
	public void setAttachfile03(String attachfile03) {
		this.attachfile03 = attachfile03;
	}
	public String getAttachfile04() {
		return attachfile04;
	}
	public void setAttachfile04(String attachfile04) {
		this.attachfile04 = attachfile04;
	}
	public String getAttachfile05() {
		return attachfile05;
	}
	public void setAttachfile05(String attachfile05) {
		this.attachfile05 = attachfile05;
	}
	public long getRtyMid() {
		return rtyMid;
	}
	public void setRtyMid(long rtyMid) {
		this.rtyMid = rtyMid;
	}
	public int getRtySubid() {
		return rtySubid;
	}
	public void setRtySubid(int rtySubid) {
		this.rtySubid = rtySubid;
	}
	public String getRtyTyp() {
		return rtyTyp;
	}
	public void setRtyTyp(String rtyTyp) {
		this.rtyTyp = rtyTyp;
	}
}
