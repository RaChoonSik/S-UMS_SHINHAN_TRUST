/**
 * 작성자 : 김재환
 * 작성일시 : 2021.12.10
 * 설명 : KakaoTemplateVO
 */
package kr.co.sict.ums.sys.seg.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class KakaoTemplateVO extends CommonVO {
	
	//카카오 알림톡 템플릿 항목
	private String tempCd;			// 템플릿코드
	private String tempNm;			// 템플릿명
	private String tempDesc;		// 템플릿설명
	private String tempContent;		// 템플릿콘텐츠
	private String tempItem;		// 템플릿아이템
	private String mergyItem;		// 머지항목
	private String status;			// 상태
	private String statusNm;		// 상태명
	private int deptNo;				// 부서코드
	private String deptNm;			// 부서명
	private String userId;			// 사용자
	private String regId;			// 등록자아이디
	private String regDt;			// 등록일자
	private String upId;			// 수정자아이디
	private String upDt;			// 수정일자
	private String segNo;			// 순번
	private String userNm;			// 사용자명
	private String regNm;			// 등록자명
	private String upNm;			// 수정자명
	private String startDt;			// 시작날짜
	private String endDt;			// 종료날짜
	private String legalYn;			// 광고여부
	private String tempMappContent;	//매핑된 API 템플릿 컨텐츠
	private String weblinkYn; 		//웹링크여부 
	//API템플릿 매핑 정보 
	private String apiTempCd;		//매핑된 API 템플릿 코드 
	private String apiMergeCol;		//매핑된 API 템플릿의 머지(함수)항목
	private String apiKakaoCol;		//매핑된 API 템플릿과 매핑되는 카카오컬럼항목
	private String apiMergeCols;	//매핑된 API 템플릿과 매핑되는 카카오컬럼항목리스트 
	
	//검색
	private String uilang;			// 언어권
	private String searchStartDt;	// 검색 시작날짜
	private String searchEndDt;		// 검색 종료날짜
	private String searchStatus;	// 검색 상태
	private String searchTempNm;	// 검색 템플릿명
	private String searchTempCd;	// 검색 템플릿코드
	private int searchDeptNo;		// 검색 부서코드
	private String searchUserId;	// 검색 사용자아이디
	private String searchTaskNm;	// 검색 업무 이름
	private String searchCampNo;	// 검색 캠페인번호
	private String searchGubun;		// 검색 구분
	private String mappYn;			// 설정된 정보가져오기 여부
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
	public String getTempContent() {
		return tempContent;
	}
	public void setTempContent(String tempContent) {
		this.tempContent = tempContent;
	}
	public String getTempItem() {
		return tempItem;
	}
	public void setTempItem(String tempItem) {
		this.tempItem = tempItem;
	}
	public String getMergyItem() {
		return mergyItem;
	}
	public void setMergyItem(String mergyItem) {
		this.mergyItem = mergyItem;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getSegNo() {
		return segNo;
	}
	public void setSegNo(String segNo) {
		this.segNo = segNo;
	}
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	public String getRegNm() {
		return regNm;
	}
	public void setRegNm(String regNm) {
		this.regNm = regNm;
	}
	public String getUpNm() {
		return upNm;
	}
	public void setUpNm(String upNm) {
		this.upNm = upNm;
	}
	public String getStartDt() {
		return startDt;
	}
	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}
	public String getEndDt() {
		return endDt;
	}
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}
	public String getLegalYn() {
		return legalYn;
	}
	public void setLegalYn(String legalYn) {
		this.legalYn = legalYn;
	}
	public String getTempMappContent() {
		return tempMappContent;
	}
	public void setTempMappContent(String tempMappContent) {
		this.tempMappContent = tempMappContent;
	}
	public String getWeblinkYn() {
		return weblinkYn;
	}
	public void setWeblinkYn(String weblinkYn) {
		this.weblinkYn = weblinkYn;
	}
	public String getApiTempCd() {
		return apiTempCd;
	}
	public void setApiTempCd(String apiTempCd) {
		this.apiTempCd = apiTempCd;
	}
	public String getApiMergeCol() {
		return apiMergeCol;
	}
	public void setApiMergeCol(String apiMergeCol) {
		this.apiMergeCol = apiMergeCol;
	}
	public String getApiKakaoCol() {
		return apiKakaoCol;
	}
	public void setApiKakaoCol(String apiKakaoCol) {
		this.apiKakaoCol = apiKakaoCol;
	}
	public String getApiMergeCols() {
		return apiMergeCols;
	}
	public void setApiMergeCols(String apiMergeCols) {
		this.apiMergeCols = apiMergeCols;
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
	public String getSearchTaskNm() {
		return searchTaskNm;
	}
	public void setSearchTaskNm(String searchTaskNm) {
		this.searchTaskNm = searchTaskNm;
	}
	public String getSearchCampNo() {
		return searchCampNo;
	}
	public void setSearchCampNo(String searchCampNo) {
		this.searchCampNo = searchCampNo;
	}
	public String getSearchGubun() {
		return searchGubun;
	}
	public void setSearchGubun(String searchGubun) {
		this.searchGubun = searchGubun;
	}
	public String getMappYn() {
		return mappYn;
	}
	public void setMappYn(String mappYn) {
		this.mappYn = mappYn;
	}
}
