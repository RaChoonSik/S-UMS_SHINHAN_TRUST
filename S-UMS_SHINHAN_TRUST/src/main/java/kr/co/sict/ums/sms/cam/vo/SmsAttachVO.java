/**
 * 작성자 : 김준희
 * 작성일시 : 2021.10.30
 * 설명 : SMS 첨부파일 VO
 */
package kr.co.sict.ums.sms.cam.vo;

public class SmsAttachVO {
	private int attNo;				// 첨부번호
	private String attNm;			// 첨부파일명
	private String attFlPath;		// 첨부파일경로
	private long attFlSize;			// 첨부파일사이즈
	private String attFlTy;			// 첨부파일유형
	private String msgid;			// 메시지아이디
	private String keygen;      	// 키값
	private String encryptYn;		// 암호화여부
	private String encryptTy;		// 암호화유형
	private String encryptKey;		// 암호키
	private String attPriviewPath;	// 미리보기 전체경로
	
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
	public String getAttPriviewPath() {
		return attPriviewPath;
	}
	public void setAttPriviewPath(String attPriviewPath) {
		this.attPriviewPath = attPriviewPath;
	}

}
