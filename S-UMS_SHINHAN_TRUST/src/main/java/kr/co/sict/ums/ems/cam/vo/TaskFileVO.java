/**
 * 작성자 : 박인권
 * 작성일시 : 2024.05.16
 * 설명 : 메일 Task File VO
 */
package kr.co.sict.ums.ems.cam.vo;



public class TaskFileVO {
	private int taskNo;				// 주업무번호
	private byte[] contFile;		// 컨텐츠파일 
	private String regId;			// 등록자아이디
	private String regDt;			// 등록일시
	private String upId;			// 수정자아이디
	private String upDt;			// 수정일시
	private int newTaskNo;
	
	public int getTaskNo() {
		return taskNo;
	}
	public void setTaskNo(int taskNo) {
		this.taskNo = taskNo;
	}
	public byte[] getContFile() {
		return contFile;
	}
	public void setContFile(byte[] contFile) {
		this.contFile = contFile;
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
	public int getNewTaskNo() {
		return newTaskNo;
	}
	public void setNewTaskNo(int newTaskNo) {
		this.newTaskNo = newTaskNo;
	}
	
	
	
	
}
