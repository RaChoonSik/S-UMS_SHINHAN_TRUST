/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.06
 * 설명 : 사용자 정보 VO
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : sys.vo -->sys.acc.vo 
 */
package kr.co.sict.ums.sys.acc.vo;

import java.io.Serializable;

public class UserServiceVO implements Serializable {
	private static final long serialVersionUID = 1L;
		
	private String emsYn;		// 프로그램명
	private String rnsYn;		// 사용자아이디
	private String smsYn;		// 사용자명
	private String tnsYn;		// 사용자명
	
	public String getEmsYn() {
		return emsYn;
	}
	public void setEmsYn(String emsYn) {
		this.emsYn = emsYn;
	}
	public String getRnsYn() {
		return rnsYn;
	}
	public void setRnsYn(String rnsYn) {
		this.rnsYn = rnsYn;
	}
	public String getSmsYn() {
		return smsYn;
	}
	public void setSmsYn(String smsYn) {
		this.smsYn = smsYn;
	}
	public String getTnsYn() {
		return tnsYn;
	}
	public void setTnsYn(String tnsYn) {
		this.tnsYn = tnsYn;
	}
 
}
