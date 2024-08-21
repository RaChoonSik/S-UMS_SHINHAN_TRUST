/**
 * 작성자 : 김상진
 * 작성일시 : 2021.08.03
 * 설명 : 응답로그 VO
 */
package kr.co.sict.ums.ems.ana.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class RespLogVO extends CommonVO {
	private String respTime;	// 응답시간
	private int succCnt;		// 성공수
	private int openCnt;		// 오픈수
	private int clickCnt;		// 클릭수
	private int validCnt;		// 유효오픈수
	private int blockCnt;		// 수신거부
	public String getRespTime() {
		return respTime;
	}
	public void setRespTime(String respTime) {
		this.respTime = respTime;
	}
	public int getSuccCnt() {
		return succCnt;
	}
	public void setSuccCnt(int succCnt) {
		this.succCnt = succCnt;
	}
	public int getOpenCnt() {
		return openCnt;
	}
	public void setOpenCnt(int openCnt) {
		this.openCnt = openCnt;
	}
	public int getClickCnt() {
		return clickCnt;
	}
	public void setClickCnt(int clickCnt) {
		this.clickCnt = clickCnt;
	}
	public int getValidCnt() {
		return validCnt;
	}
	public void setValidCnt(int validCnt) {
		this.validCnt = validCnt;
	}
	public int getBlockCnt() {
		return blockCnt;
	}
	public void setBlockCnt(int blockCnt) {
		this.blockCnt = blockCnt;
	}
}
