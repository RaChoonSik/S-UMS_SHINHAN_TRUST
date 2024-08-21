/**
 * 작성자 : 김상진
 * 작성일시 : 2021.08.24
 * 설명 : 사용자활동로그 VO
 */
package kr.co.sict.ums.sys.log.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class ActionLogVO extends CommonVO {
	private String logDt;			// 로그일시
	private String sessionId;		// 세션ID
	private String ipAddr;			// IP주소
	private String userId;			// 사용자아이디
	private String userNm;			// 사용자명
	private int deptNo;				// 사용자그룹(부서)번호
	private String deptNm;			// 부서명
	private String orgCd;			// 조직코드
	private String orgNm;			// 조직명(ORG_KOR_NM)
	private String statusGb;		// 상태
	private String contentType;		// 컨텐츠유형
	private String contentTypeNm;		// 컨텐츠유형
	private String content;			// 컨텐츠
	private String contentPath;		// 컨텐츠경로
	private String contentRslt;		// 컨텐츠결과
	private String message;			// 메세지
	private String extrYn;			// 추출여부
	private int recCnt;				// 추출건수
	private String mobilYn;			// 모바일여부
	
	// 검색
	private String searchLogStdDt;		// 검색시작일
	private String searchLogEndDt;		// 검색종료일
	private String searchStatusGb;		// 검색상태
	private String searchContentType;	// 검색로그종
	private String searchOrgCd;			// 검색부서코드리스트
	private String searchDeptNo;		// 검색사용자그룹
	private String searchUserNm;		// 검색사용자명
	private String uilang;
	private String arrSearchOrgCds[];	// 부서 여러개
		
	public String getLogDt() {
		return logDt;
	}
	public void setLogDt(String logDt) {
		this.logDt = logDt;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	public int getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(int deptNo) {
		this.deptNo = deptNo;
	}
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public String getOrgCd() {
		return orgCd;
	}
	public void setOrgCd(String orgCd) {
		this.orgCd = orgCd;
	}
	public String getOrgNm() {
		return orgNm;
	}
	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}
	public String getStatusGb() {
		return statusGb;
	}
	public void setStatusGb(String statusGb) {
		this.statusGb = statusGb;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getContentTypeNm() {
		return contentTypeNm;
	}
	public void setContentTypeNm(String contentTypeNm) {
		this.contentTypeNm = contentTypeNm;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContentPath() {
		return contentPath;
	}
	public void setContentPath(String contentPath) {
		this.contentPath = contentPath;
	}
	public String getContentRslt() {
		return contentRslt;
	}
	public void setContentRslt(String contentRslt) {
		this.contentRslt = contentRslt;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getExtrYn() {
		return extrYn;
	}
	public void setExtrYn(String extrYn) {
		this.extrYn = extrYn;
	}
	public int getRecCnt() {
		return recCnt;
	}
	public void setRecCnt(int recCnt) {
		this.recCnt = recCnt;
	}
	public String getMobilYn() {
		return mobilYn;
	}
	public void setMobilYn(String mobilYn) {
		this.mobilYn = mobilYn;
	}
	public String getSearchLogStdDt() {
		return searchLogStdDt;
	}
	public void setSearchLogStdDt(String searchLogStdDt) {
		this.searchLogStdDt = searchLogStdDt;
	}
	public String getSearchLogEndDt() {
		return searchLogEndDt;
	}
	public void setSearchLogEndDt(String searchLogEndDt) {
		this.searchLogEndDt = searchLogEndDt;
	}
	public String getSearchStatusGb() {
		return searchStatusGb;
	}
	public void setSearchStatusGb(String searchStatusGb) {
		this.searchStatusGb = searchStatusGb;
	}
	public String getSearchContentType() {
		return searchContentType;
	}
	public void setSearchContentType(String searchContentType) {
		this.searchContentType = searchContentType;
	}
	public String getSearchOrgCd() {
		return searchOrgCd;
	}
	public void setSearchOrgCd(String searchOrgCd) {
		this.searchOrgCd = searchOrgCd;
	}
	public String getSearchDeptNo() {
		return searchDeptNo;
	}
	public void setSearchDeptNo(String searchDeptNo) {
		this.searchDeptNo = searchDeptNo;
	}
	public String getSearchUserNm() {
		return searchUserNm;
	}
	public void setSearchUserNm(String searchUserNm) {
		this.searchUserNm = searchUserNm;
	}
	public String getUilang() {
		return uilang;
	}
	public void setUilang(String uilang) {
		this.uilang = uilang;
	}
	public String[] getArrSearchOrgCds() {
		return arrSearchOrgCds;
	}
	public void setArrSearchOrgCds(String[] arrSearchOrgCds) {
		this.arrSearchOrgCds = arrSearchOrgCds;
	}
	
}
