/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.22
 * 설명 : RNS 상세로그 VO
 */
package kr.co.sict.ums.rns.ana.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class RnsAnaMailSendResultVO  extends CommonVO {
	private int mid;
	private int subid;
	private int tid;
	private int refmid;
	private int send;
	private int success;
	private int ipChecking;
	
  
	//검색조건
	private int searchMid;   //검색 메시지 아이디 

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public int getSubid() {
		return subid;
	}

	public void setSubid(int subid) {
		this.subid = subid;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public int getRefmid() {
		return refmid;
	}

	public void setRefmid(int refmid) {
		this.refmid = refmid;
	}

	public int getSend() {
		return send;
	}

	public void setSend(int send) {
		this.send = send;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getIpChecking() {
		return ipChecking;
	}

	public void setIpChecking(int ipChecking) {
		this.ipChecking = ipChecking;
	}

	public int getSearchMid() {
		return searchMid;
	}

	public void setSearchMid(int searchMid) {
		this.searchMid = searchMid;
	}

 
	 
}
