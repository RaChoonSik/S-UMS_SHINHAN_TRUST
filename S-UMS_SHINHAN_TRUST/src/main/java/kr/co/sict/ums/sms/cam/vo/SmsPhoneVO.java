/**
 * 작성자 : 김준희
 * 작성일시 : 2021.10.13
 * 설명 : 메일 SmsPhone VO
 */
package kr.co.sict.ums.sms.cam.vo;

import java.util.List;

import kr.co.sict.ums.com.vo.CommonVO;

public class SmsPhoneVO extends CommonVO {
	private String msgid;			//메시지아이디
	private String keygen;			//메시지키
	private String phone;			//핸드폰번호
  
	public String getMsgid() {
		return msgid;
	}
	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}
	public String getKeygen() {
		return keygen;
	}
	public void setKeygen(String keygen) {
		this.keygen = keygen;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}	
}
