/**
 * 작성자 : 김준희
 * 작성일시 : 2022.02.15
 * 설명 : RNS 금지어 VO
 */
package kr.co.sict.ums.rns.svc.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RnsProhibitWordVO {
	private int tid;				// 주업무번호
	private String contentTyp;		// 컨텐츠타입:000(메일제목), 001(메일본문)
	private int prohibitCnt;		// 금착어건수
	private String prohibitDescString;	// 금칙어
	private Map<String, Object> prohibitDesc;	// 금칙어
	private String regId;			// 생성자ID
	private String regDt;			// 생성일시
	
	//조회용 
	private int prohibitTitleCnt;					// 제목 : 금지어 건수 
	private String prohibitTitleDesc;	// 제목 : 금지어 항목 
	private int prohibitTextCnt;					// 본문 : 금지어 건수 
	private String prohibitTextDesc;	// 본문 : 금지어 항목  
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public String getContentTyp() {
		return contentTyp;
	}
	public void setContentTyp(String contentTyp) {
		this.contentTyp = contentTyp;
	}
	public int getProhibitCnt() {
		return prohibitCnt;
	}
	public void setProhibitCnt(int prohibitCnt) {
		this.prohibitCnt = prohibitCnt;
	}
	public String getProhibitDescString() {
		return prohibitDescString;
	}
	public void setProhibitDescString(String prohibitDescString) {
		this.prohibitDescString = prohibitDescString;
	}
	public Map<String, Object> getProhibitDesc() {
		return prohibitDesc;
	}
	public void setProhibitDesc(Map<String, Object> prohibitDesc) {
		this.prohibitDesc = prohibitDesc;
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
	public int getProhibitTitleCnt() {
		return prohibitTitleCnt;
	}
	public void setProhibitTitleCnt(int prohibitTitleCnt) {
		this.prohibitTitleCnt = prohibitTitleCnt;
	}
	public String getProhibitTitleDesc() {
		return prohibitTitleDesc;
	}
	public void setProhibitTitleDesc(String prohibitTitleDesc) {
		this.prohibitTitleDesc = prohibitTitleDesc;
	}
	public int getProhibitTextCnt() {
		return prohibitTextCnt;
	}
	public void setProhibitTextCnt(int prohibitTextCnt) {
		this.prohibitTextCnt = prohibitTextCnt;
	}
	public String getProhibitTextDesc() {
		return prohibitTextDesc;
	}
	public void setProhibitTextDesc(String prohibitTextDesc) {
		this.prohibitTextDesc = prohibitTextDesc;
	}
}
