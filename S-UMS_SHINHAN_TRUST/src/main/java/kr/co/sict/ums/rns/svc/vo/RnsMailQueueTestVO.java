/**
 * 작성자 : 김상진
 * 작성일시 : 2021.09.02
 * 설명 : RNS 자동메일 서비스 테스트 VO
 */
package kr.co.sict.ums.rns.svc.vo;

public class RnsMailQueueTestVO {
	private long mid;			// 메세지ID
	private String useYn;		// 사용여부
	private String regId;		// 생성자ID
	private String regDt;		// 생성일시
	private String upId;		// 수정자ID
	private String upDt;		// 수정일시
	public long getMid() {
		return mid;
	}
	public void setMid(long mid) {
		this.mid = mid;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
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
}
