/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.21
 * 설명 : Segment Member VO
 */
package kr.co.sict.ums.sys.seg.vo;

import java.util.HashMap;
import java.util.List;

public class SegmentMemberVO {
	private int totalCount;								// 전체건수
	private List<HashMap<String,String>> memberList;	// 멤버목록
	private HashMap<String,String> member;				// 멤버정보
	private String mergeKey;							// 머지키
	private String message;								// 메시지
	boolean result;										// 결과
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public List<HashMap<String, String>> getMemberList() {
		return memberList;
	}
	public void setMemberList(List<HashMap<String, String>> memberList) {
		this.memberList = memberList;
	}
	public HashMap<String, String> getMember() {
		return member;
	}
	public void setMember(HashMap<String, String> member) {
		this.member = member;
	}
	public String getMergeKey() {
		return mergeKey;
	}
	public void setMergeKey(String mergeKey) {
		this.mergeKey = mergeKey;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
}
