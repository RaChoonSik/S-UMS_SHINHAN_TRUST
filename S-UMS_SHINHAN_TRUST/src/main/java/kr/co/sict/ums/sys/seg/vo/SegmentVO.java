/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.16
 * 설명 : Segment VO
 */
package kr.co.sict.ums.sys.seg.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class SegmentVO extends CommonVO {
	private int segNo;				// 세그먼트번호
	private String segNm;			// 세그먼트명
	private String segDesc;			// 세그먼트설명
	private int dbConnNo;			// DB Connection 번호
	private int dbConnNm;			// DB Connection 명
	private String createTy;		// 생성툴(유형)
	private String createTyNm;		// 생성툴(유형)명
	private String mergeKey;		// 머지알리아스명
	private String mergeCol;		// 머지컬럼명
	private String segFlPath;		// 파일경로
	private String srcWhere;		// 조건절 원본
	private String query;			// SQL문
	private String retryQuery;		// 재발송 SQL문
	private String realQuery;		// 실시간 SQL문
	private String separatorChar;	// 파일구분자(문자)
	private int totCnt;				// 총수신자
	private String selectSql;		// SELECT절
	private String fromSql;			// FROM절
	private String whereSql;		// WHERE절
	private String OrderbySql;		// ORDER BY절
	private String status;			// 상태
	private String statusNm;		// 상태명
	private int deptNo;				// 부서번호
	private String deptNm;			// 부서명
	private String userId;			// 사용자아이디
	private String userNm;			// 사용자명
	private String decode;			// ??
	private String emsuseYn;		// EMS사용여부
	private String smsuseYn;		// SMS사용여부
	private String pushuseYn;		// PUSH사용여부
	private String regId;			// 등록자
	private String regNm;			// 등록자명
	private String regDt;			// 등록일자
	private String upId;			// 수정자
	private String upNm;			// 수정자명
	private String upDt;			// 수정일자
	
	// 검색
	private String searchSegNm;		// 검색 세그먼트명
	private String searchCreateTy;	// 검색 생성툴
	private String searchStatus;	// 발송그룹상태
	private String searchStartDt;	// 검색시작일자
	private String searchEndDt;		// 검색종료일자
	private int searchDeptNo;		// 검색부서번호
	private String searchUserId;	// 검색사용자아이디
	private String searchEmsuseYn;	// 검색EMS사용여부
	private String searchSmsuseYn;	// 검색SMS사용여부
	private String searchPushuseYn;	// 검색PUSH사용여부
	private String uilang;			// 언어권
	private String value;			// 검색값
	private String search;			// 검색항목
	private String segNos;			// 세그먼트번호멀티
	private String testType;		// 테스트유형
	
	// 조회사유 ActionLog
	private String checkSearchReason;	// 조회사유등록여부
	private String searchReasonCd;		// 조회사유코드
	private String contentPath;			// 컨텐츠경로
	
	private int serviceGb;			//서비스구분			

	public int getSegNo() {
		return segNo;
	}
	public void setSegNo(int segNo) {
		this.segNo = segNo;
	}
	public String getSegNm() {
		return segNm;
	}
	public void setSegNm(String segNm) {
		this.segNm = segNm;
	}
	public String getSegDesc() {
		return segDesc;
	}
	public void setSegDesc(String segDesc) {
		this.segDesc = segDesc;
	}
	public int getDbConnNo() {
		return dbConnNo;
	}
	public void setDbConnNo(int dbConnNo) {
		this.dbConnNo = dbConnNo;
	}
	public int getDbConnNm() {
		return dbConnNm;
	}
	public void setDbConnNm(int dbConnNm) {
		this.dbConnNm = dbConnNm;
	}
	public String getCreateTy() {
		return createTy;
	}
	public void setCreateTy(String createTy) {
		this.createTy = createTy;
	}
	public String getCreateTyNm() {
		return createTyNm;
	}
	public void setCreateTyNm(String createTyNm) {
		this.createTyNm = createTyNm;
	}
	public String getMergeKey() {
		return mergeKey;
	}
	public void setMergeKey(String mergeKey) {
		this.mergeKey = mergeKey;
	}
	public String getMergeCol() {
		return mergeCol;
	}
	public void setMergeCol(String mergeCol) {
		this.mergeCol = mergeCol;
	}
	public String getSegFlPath() {
		return segFlPath;
	}
	public void setSegFlPath(String segFlPath) {
		this.segFlPath = segFlPath;
	}
	public String getSrcWhere() {
		return srcWhere;
	}
	public void setSrcWhere(String srcWhere) {
		this.srcWhere = srcWhere;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getRetryQuery() {
		return retryQuery;
	}
	public void setRetryQuery(String retryQuery) {
		this.retryQuery = retryQuery;
	}
	public String getRealQuery() {
		return realQuery;
	}
	public void setRealQuery(String realQuery) {
		this.realQuery = realQuery;
	}
	public String getSeparatorChar() {
		return separatorChar;
	}
	public void setSeparatorChar(String separatorChar) {
		this.separatorChar = separatorChar;
	}
	public int getTotCnt() {
		return totCnt;
	}
	public void setTotCnt(int totCnt) {
		this.totCnt = totCnt;
	}
	public String getSelectSql() {
		return selectSql;
	}
	public void setSelectSql(String selectSql) {
		this.selectSql = selectSql;
	}
	public String getFromSql() {
		return fromSql;
	}
	public void setFromSql(String fromSql) {
		this.fromSql = fromSql;
	}
	public String getWhereSql() {
		return whereSql;
	}
	public void setWhereSql(String whereSql) {
		this.whereSql = whereSql;
	}
	public String getOrderbySql() {
		return OrderbySql;
	}
	public void setOrderbySql(String orderbySql) {
		OrderbySql = orderbySql;
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
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	public String getDecode() {
		return decode;
	}
	public void setDecode(String decode) {
		this.decode = decode;
	}
	public String getEmsuseYn() {
		return emsuseYn;
	}
	public void setEmsuseYn(String emsuseYn) {
		this.emsuseYn = emsuseYn;
	}
	public String getSmsuseYn() {
		return smsuseYn;
	}
	public void setSmsuseYn(String smsuseYn) {
		this.smsuseYn = smsuseYn;
	}
	public String getPushuseYn() {
		return pushuseYn;
	}
	public void setPushuseYn(String pushuseYn) {
		this.pushuseYn = pushuseYn;
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
	public String getSearchSegNm() {
		return searchSegNm;
	}
	public void setSearchSegNm(String searchSegNm) {
		this.searchSegNm = searchSegNm;
	}
	public String getSearchCreateTy() {
		return searchCreateTy;
	}
	public void setSearchCreateTy(String searchCreateTy) {
		this.searchCreateTy = searchCreateTy;
	}
	public String getSearchStatus() {
		return searchStatus;
	}
	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
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
	public String getSearchEmsuseYn() {
		return searchEmsuseYn;
	}
	public void setSearchEmsuseYn(String searchEmsuseYn) {
		this.searchEmsuseYn = searchEmsuseYn;
	}
	public String getSearchSmsuseYn() {
		return searchSmsuseYn;
	}
	public void setSearchSmsuseYn(String searchSmsuseYn) {
		this.searchSmsuseYn = searchSmsuseYn;
	}
	public String getSearchPushuseYn() {
		return searchPushuseYn;
	}
	public void setSearchPushuseYn(String searchPushuseYn) {
		this.searchPushuseYn = searchPushuseYn;
	}
	public String getUilang() {
		return uilang;
	}
	public void setUilang(String uilang) {
		this.uilang = uilang;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public String getSegNos() {
		return segNos;
	}
	public void setSegNos(String segNos) {
		this.segNos = segNos;
	}
	public String getTestType() {
		return testType;
	}
	public void setTestType(String testType) {
		this.testType = testType;
	}
	public String getCheckSearchReason() {
		return checkSearchReason;
	}
	public void setCheckSearchReason(String checkSearchReason) {
		this.checkSearchReason = checkSearchReason;
	}
	public String getSearchReasonCd() {
		return searchReasonCd;
	}
	public void setSearchReasonCd(String searchReasonCd) {
		this.searchReasonCd = searchReasonCd;
	}
	public String getContentPath() {
		return contentPath;
	}
	public void setContentPath(String contentPath) {
		this.contentPath = contentPath;
	}
	public int getServiceGb() {
		return serviceGb;
	}
	public void setServiceGb(int serviceGb) {
		this.serviceGb = serviceGb;
	}
	
}
