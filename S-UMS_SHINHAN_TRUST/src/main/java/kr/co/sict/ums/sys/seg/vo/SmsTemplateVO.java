/**
 * 작성자 : 김준희
 * 작성일시 : 2022.06.23
 * 설명 : SmsTemplateVO
 */
package kr.co.sict.ums.sys.seg.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class SmsTemplateVO extends CommonVO {
	
	//SMS 템플릿 항목
	private String tempCd;		//템플릿코드
	private String tempNm;		//템플릿명
	private String tempDesc;	//템플릿설명
	private String tempSubject;	//템플릿제목
	private String tempContent;	//템플릿콘텐츠
	private String mergeItem;	//머지항목
	private String gubun;		//전송유형:C115
	private int campNo;			//캠페인번호
	private String campNm;		//캠페인이름
	private int segNo;			//세그먼트등록번호
	private String segNoc;		//번호가 캐릭터로 사용되는경우 segno + mergekey
	private String segNm;		//세그먼트등록이름
	private String status;		//STATUS
	private String statusNm;	//STATUS이름
	private int deptNo;			//부서코드
	private String deptNm;		//부서이름
	private String user;		//사용자
	private String userId;		//사용자아이디
	private String userNm;		//사용자
	private String regId;		//등록자아이디
	private String regNm;		//등록자명
	private String regDt;		//등록일자
	private String upId;		//수정자
	private String upNm;		//수정자명
	private String upDt;		//수정일자

	//검색
	private String uilang;			// 언어권
	private String searchStartDt;	// 검색 시작날짜
	private String searchEndDt;		// 검색 종료날짜
	private String searchStatus;	// 검색 상태
	private String searchTempNm;	// 검색 템플릿명
	private String searchTempCd;	// 검색 템플릿코드
	private int searchDeptNo;		// 검색 부서코드
	private String searchUserId;	// 검색 사용자아이디
	private String searchGubun;		// 검색 구분
	
	//추가정보
	private int mappKakaoTempCnt; 	//매핑되어있는 카카오 알림톡 갯수
	private String mappKakaoTempNm;	//매핑되어있는 카카오 알림톡 최근 1건 이름 
	private int mappInfoInit;		//매핑되어 있는 카카오 알림톡 정보 초기화여부 0 : 아니오 / 1 :초기화
	
	public String getTempCd() {
		return tempCd;
	}
	public void setTempCd(String tempCd) {
		this.tempCd = tempCd;
	}
	public String getTempNm() {
		return tempNm;
	}
	public void setTempNm(String tempNm) {
		this.tempNm = tempNm;
	}
	public String getTempDesc() {
		return tempDesc;
	}
	public void setTempDesc(String tempDesc) {
		this.tempDesc = tempDesc;
	}
	public String getTempSubject() {
		return tempSubject;
	}
	public void setTempSubject(String tempSubject) {
		this.tempSubject = tempSubject;
	}
	public String getTempContent() {
		return tempContent;
	}
	public void setTempContent(String tempContent) {
		this.tempContent = tempContent;
	}
	public String getMergeItem() {
		return mergeItem;
	}
	public void setMergeItem(String mergeItem) {
		this.mergeItem = mergeItem;
	}
	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	public int getCampNo() {
		return campNo;
	}
	public void setCampNo(int campNo) {
		this.campNo = campNo;
	}
	public String getCampNm() {
		return campNm;
	}
	public void setCampNm(String campNm) {
		this.campNm = campNm;
	}
	public int getSegNo() {
		return segNo;
	}
	public void setSegNo(int segNo) {
		this.segNo = segNo;
	}
	public String getSegNoc() {
		return segNoc;
	}
	public void setSegNoc(String segNoc) {
		this.segNoc = segNoc;
	}
	public String getSegNm() {
		return segNm;
	}
	public void setSegNm(String segNm) {
		this.segNm = segNm;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusNm() {
		return statusNm;
	}
	public void setStatusNm(String statusNm) {
		this.statusNm = statusNm;
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
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
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
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public String getRegNm() {
		return regNm;
	}
	public void setRegNm(String regNm) {
		this.regNm = regNm;
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
	public String getUpNm() {
		return upNm;
	}
	public void setUpNm(String upNm) {
		this.upNm = upNm;
	}
	public String getUpDt() {
		return upDt;
	}
	public void setUpDt(String upDt) {
		this.upDt = upDt;
	}
	public String getUilang() {
		return uilang;
	}
	public void setUilang(String uilang) {
		this.uilang = uilang;
	}
	public String getSearchStartDt() {
		return searchStartDt;
	}
	public void setSearchStartDt(String searchStartDt) {
		this.searchStartDt = searchStartDt;
	}
	public String getSearchEndDt() {
		return searchEndDt;
	}
	public void setSearchEndDt(String searchEndDt) {
		this.searchEndDt = searchEndDt;
	}
	public String getSearchStatus() {
		return searchStatus;
	}
	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}
	public String getSearchTempNm() {
		return searchTempNm;
	}
	public void setSearchTempNm(String searchTempNm) {
		this.searchTempNm = searchTempNm;
	}
	public String getSearchTempCd() {
		return searchTempCd;
	}
	public void setSearchTempCd(String searchTempCd) {
		this.searchTempCd = searchTempCd;
	}
	public int getSearchDeptNo() {
		return searchDeptNo;
	}
	public void setSearchDeptNo(int searchDeptNo) {
		this.searchDeptNo = searchDeptNo;
	}
	public String getSearchUserId() {
		return searchUserId;
	}
	public void setSearchUserId(String searchUserId) {
		this.searchUserId = searchUserId;
	}
	public String getSearchGubun() {
		return searchGubun;
	}
	public void setSearchGubun(String searchGubun) {
		this.searchGubun = searchGubun;
	}
	public int getMappKakaoTempCnt() {
		return mappKakaoTempCnt;
	}
	public void setMappKakaoTempCnt(int mappKakaoTempCnt) {
		this.mappKakaoTempCnt = mappKakaoTempCnt;
	}
	public String getMappKakaoTempNm() {
		return mappKakaoTempNm;
	}
	public void setMappKakaoTempNm(String mappKakaoTempNm) {
		this.mappKakaoTempNm = mappKakaoTempNm;
	}
	public int getMappInfoInit() {
		return mappInfoInit;
	}
	public void setMappInfoInit(int mappInfoInit) {
		this.mappInfoInit = mappInfoInit;
	}
}
