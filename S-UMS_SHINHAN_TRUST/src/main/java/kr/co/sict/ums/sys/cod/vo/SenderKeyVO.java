/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 코드 VO
 */
package kr.co.sict.ums.sys.cod.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class SenderKeyVO extends CommonVO {
	private String senderKeyId;	// 발송키 아이디
	private String senderKey;	// 발송키 
	private String useYn;		// 사용여부
	private String delYn;		// 삭제여부
	private String upId;		// 수정자
	private String upDt;		// 수정일
	private String regId;		// 등록자
	private String regDt;		// 등록일
	private String regNm;		// 등록자이름
	private String upNm;		// 수정자이
	
	// 추가정보
	private String senderKeyIds;// 발송키리스트
	
	
	public String getSenderKeyId() {
		return senderKeyId;
	}

	public void setSenderKeyId(String senderKeyId) {
		this.senderKeyId = senderKeyId;
	}

	public String getSenderKey() {
		return senderKey;
	}

	public void setSenderKey(String senderKey) {
		this.senderKey = senderKey;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	public String getDelYn() {
		return delYn;
	}

	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}

	public String getUpId() {
		return upId;
	}

	public void setUpId(String upId) {
		this.upId = upId;
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

	public String getRegDt() {
		return regDt;
	}

	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	public String getRegNm() {
		return regNm;
	}

	public void setRegNm(String regNm) {
		this.regNm = regNm;
	}

	public String getUpNm() {
		return upNm;
	}

	public void setUpNm(String upNm) {
		this.upNm = upNm;
	}

	public String getSenderKeyIds() {
		return senderKeyIds;
	}

	public void setSenderKeyIds(String senderKeyIds) {
		this.senderKeyIds = senderKeyIds;
	}
	
	
	
}
