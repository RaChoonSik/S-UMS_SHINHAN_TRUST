/**
 * 작성자 : 박인권
 * 작성일시 : 2024.05.14
 * 설명 : SegmentFile VO
 */
package kr.co.sict.ums.ems.seg.vo;


public class SegmentFileVO {
	private int segNo;				// 세그먼트번호
	private byte[] segFile;			// 수신자csv 파일
	private String compressYn;		// 압축여부
	private long segFlSize;			// 파일사이즈
	private String regId;			// 등록자
	private String regDt;			// 등록일자
	private String upId;			// 수정자
	private String upDt;			// 수정일자
	public int getSegNo() {
		return segNo;
	}
	public void setSegNo(int segNo) {
		this.segNo = segNo;
	}
	public byte[] getSegFile() {
		return segFile;
	}
	public void setSegFile(byte[] segFile) {
		this.segFile = segFile;
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
	public String getCompressYn() {
		return compressYn;
	}
	public void setCompressYn(String compressYn) {
		this.compressYn = compressYn;
	}
	public long getSegFlSize() {
		return segFlSize;
	}
	public void setSegFlSize(long segFlSize) {
		this.segFlSize = segFlSize;
	}
	
}
