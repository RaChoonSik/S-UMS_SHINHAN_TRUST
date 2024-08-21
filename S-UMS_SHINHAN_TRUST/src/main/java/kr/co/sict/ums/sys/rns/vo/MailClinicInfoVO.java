 /**
 * 작성자 : 박찬용
 * 작성일시 : 2022.01.21
 * 설명 : 메일클리닉 일정 관리 VO
 */
package kr.co.sict.ums.sys.rns.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class MailClinicInfoVO extends CommonVO {
	private String clsY;     //마감년
	private String clsM;     //마감월
	private String clsYm;    //마감년월
	private String clsDt;    //마감일시
	private int clsErrCnt;   //마감오류횟수
	private int clsStrMmCnt; //마감시작개월
	private int clsEndMmCnt; //마감종료개월 
		
	private String insertMonth; //일괄적용월
	private String delMonth; //일괄삭제월
	
	private String upId;
	private String upNm;
	private String upDt;
	private String regId;
	private String regNm;
	private String regDt;
	
	private String[] arrDelMonth; // 일괄적용 비대상월
	
	public String getClsYm() {
		return clsYm;
	}
	public void setClsYm(String clsYm) {
		this.clsYm = clsYm;
	}
	public String getClsDt() {
		return clsDt;
	}
	public void setClsDt(String clsDt) {
		this.clsDt = clsDt;
	}
	public int getClsErrCnt() {
		return clsErrCnt;
	}
	public void setClsErrCnt(int clsErrCnt) {
		this.clsErrCnt = clsErrCnt;
	}
	public int getClsStrMmCnt() {
		return clsStrMmCnt;
	}
	public void setClsStrMmCnt(int clsStrMmCnt) {
		this.clsStrMmCnt = clsStrMmCnt;
	}
	public int getClsEndMmCnt() {
		return clsEndMmCnt;
	}
	public void setClsEndMmCnt(int clsEndMmCnt) {
		this.clsEndMmCnt = clsEndMmCnt;
	}	 
	public String getUpId() {
		return upId;
	}
	public void setUpId(String upId) {
		this.upId = upId;
	}
	public String getUpNm() {
		return upNm;
	}
	public void setUpNm(String upNm) {
		this.upNm = upNm;
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
	public String getRegNm() {
		return regNm;
	}
	public void setRegNm(String regNm) {
		this.regNm = regNm;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}	
	
	public String getInsertMonth() {
		return insertMonth;
	}
	public void setInsertMonth(String insertMonth) {
		this.insertMonth = insertMonth;
	}
	public String getDelMonth() {
		return delMonth;
	}
	public void setDelMonth(String delMonth) {
		this.delMonth = delMonth;
	}
	
	public String[] getArrDelMonth() {
		return arrDelMonth;
	}
	public void setArrDelMonth(String[] arrDelMonth) {
		this.arrDelMonth = arrDelMonth;
	}
	public String getClsY() {
		return clsY;
	}
	public void setClsY(String clsY) {
		this.clsY = clsY;
	}
	public String getClsM() {
		return clsM;
	}
	public void setClsM(String clsM) {
		this.clsM = clsM;
	}
	
}
