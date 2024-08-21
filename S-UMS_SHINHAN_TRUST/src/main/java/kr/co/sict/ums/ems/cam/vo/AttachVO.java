/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.29
 * 설명 : 첨부파일 VO
 */
package kr.co.sict.ums.ems.cam.vo;

public class AttachVO {
	private int attNo;			// 첨부번호
	private String attNm;		// 첨부파일명
	private String attFlPath;	// 첨부파일경로
	private long attFlSize;		// 첨부파일사이즈
	private String attFlTy;		// 첨부파일유형
	private int taskNo;			// 업무번호
	private String encryptYn;	// 암호화여부
	private String encryptTy;	// 암호화유형
	private String encryptKey;	// 암호키
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
	public int getTaskNo() {
		return taskNo;
	}
	public void setTaskNo(int taskNo) {
		this.taskNo = taskNo;
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
}
