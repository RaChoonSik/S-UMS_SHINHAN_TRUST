/**
 * 작성자 : 김상진
 * 작성일시 : 2021.09.01
 * 설명 : RNS 첨부파일 VO
 */
package kr.co.sict.ums.rns.svc.vo;

public class RnsAttachVO {
	private int tid;			// 서비스ID
	private int attNo;			// 첨부번호
	private String attNm;		// 첨부파일명
	private String attFlPath;	// 첨부파일경로
	private long attFlSize;		// 첨부파일사이즈
	private String attFlTy;		// 첨부파일유형
	private String encryptYn;	// 암호화여부
	private String encryptTy;	// 암호화유형
	private String encryptKey;	// 암호키
	private String useYn;		// 사용여부
	private String regId;		// 생성자ID
	private String regDt;		// 생성일시
	private String upId;		// 수정자ID
	private String upDt;		// 수정일시
	public int getAttNo() {
		return attNo;
	}
	public void setAttNo(int attNo) {
		this.attNo = attNo;
	}
	public String getAttNm() {
		return attNm;
	}
	public void setAttNm(String attNm) {
		this.attNm = attNm;
	}
	public String getAttFlPath() {
		return attFlPath;
	}
	public void setAttFlPath(String attFlPath) {
		this.attFlPath = attFlPath;
	}
	public long getAttFlSize() {
		return attFlSize;
	}
	public void setAttFlSize(long attFlSize) {
		this.attFlSize = attFlSize;
	}
	public String getAttFlTy() {
		return attFlTy;
	}
	public void setAttFlTy(String attFlTy) {
		this.attFlTy = attFlTy;
	}
	public String getEncryptYn() {
		return encryptYn;
	}
	public void setEncryptYn(String encryptYn) {
		this.encryptYn = encryptYn;
	}
	public String getEncryptTy() {
		return encryptTy;
	}
	public void setEncryptTy(String encryptTy) {
		this.encryptTy = encryptTy;
	}
	public String getEncryptKey() {
		return encryptKey;
	}
	public void setEncryptKey(String encryptKey) {
		this.encryptKey = encryptKey;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
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
