/**
 * 작성자 : 김상진
 * 작성일시 : 2021.09.02
 * 설명 : RNS 발송 그룹 저장 VO
 */
package kr.co.sict.ums.rns.svc.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class RnsRecipientInfoVO extends CommonVO {
	private long mid;		// 메세지ID
	private int subid;		// 재발송아이디
	private int tid;		// 서비스ID
	private String rid;		// 수신자아이디
	private String rname;	// 수신자명
	private String rmail;	// 수신자이메일
	private String enckey;	// 첨부파일암호화키
	private String map1;	// 자동발송머지항목01
	private String map2;	// 자동발송머지항목02
	private String map3;	// 자동발송머지항목03
	private String map4;	// 자동발송머지항목04
	private String map5;	// 자동발송머지항목05
	private String map6;	// 자동발송머지항목06
	private String map7;	// 자동발송머지항목07
	private String map8;	// 자동발송머지항목08
	private String map9;	// 자동발송머지항목09
	private String map10;	// 자동발송머지항목10
	private String map11;	// 자동발송머지항목11
	private String map12;	// 자동발송머지항목12
	private String map13;	// 자동발송머지항목12
	private String map14;	// 자동발송머지항목12
	private String map15;	// 자동발송머지항목12
	
	// 추가정보
	private String rids;		// 수신자아이디 멀티선택
	private int succCnt;		// 성공건수
	private int failCnt;		// 실패건수
	private String failDesc;		// 실패사유
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
	public String getEnckey() {
		return enckey;
	}
	public void setEnckey(String enckey) {
		this.enckey = enckey;
	}
	public String getMap1() {
		return map1;
	}
	public void setMap1(String map1) {
		this.map1 = map1;
	}
	public String getMap2() {
		return map2;
	}
	public void setMap2(String map2) {
		this.map2 = map2;
	}
	public String getMap3() {
		return map3;
	}
	public void setMap3(String map3) {
		this.map3 = map3;
	}
	public String getMap4() {
		return map4;
	}
	public void setMap4(String map4) {
		this.map4 = map4;
	}
	public String getMap5() {
		return map5;
	}
	public void setMap5(String map5) {
		this.map5 = map5;
	}
	public String getMap6() {
		return map6;
	}
	public void setMap6(String map6) {
		this.map6 = map6;
	}
	public String getMap7() {
		return map7;
	}
	public void setMap7(String map7) {
		this.map7 = map7;
	}
	public String getMap8() {
		return map8;
	}
	public void setMap8(String map8) {
		this.map8 = map8;
	}
	public String getMap9() {
		return map9;
	}
	public void setMap9(String map9) {
		this.map9 = map9;
	}
	public String getMap10() {
		return map10;
	}
	public void setMap10(String map10) {
		this.map10 = map10;
	}
	public String getMap11() {
		return map11;
	}
	public void setMap11(String map11) {
		this.map11 = map11;
	}
	public String getMap12() {
		return map12;
	}
	public void setMap12(String map12) {
		this.map12 = map12;
	}
	public String getMap13() {
		return map13;
	}
	public void setMap13(String map13) {
		this.map13 = map13;
	}
	public String getMap14() {
		return map14;
	}
	public void setMap14(String map14) {
		this.map14 = map14;
	}
	public String getMap15() {
		return map15;
	}
	public void setMap15(String map15) {
		this.map15 = map15;
	}
	public String getRids() {
		return rids;
	}
	public void setRids(String rids) {
		this.rids = rids;
	}
	public int getSuccCnt() {
		return succCnt;
	}
	public void setSuccCnt(int succCnt) {
		this.succCnt = succCnt;
	}
	public int getFailCnt() {
		return failCnt;
	}
	public void setFailCnt(int failCnt) {
		this.failCnt = failCnt;
	}
	public String getFailDesc() {
		return failDesc;
	}
	public void setFailDesc(String failDesc) {
		this.failDesc = failDesc;
	}
}
