package kr.co.sict.util; 

/**
 * <pre>
 * 프로그램유형 : Util(java)
 * 프로그램명   : Code.java
 * Version      : 1.0
 * 작성일       : 2002/10/20
 * 작성자       : 오범석
 * 수정일       : 
 * 수정자       : 
 * 설명         : 코드 정보를 저장한다.
 *
 * 프로젝트명   : 표준콤포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class Code {

	/* =======================================================
			날짜 포맷 문자 설정
	   ======================================================= */
	/** 날짜 생성 포맷 (년월일시분초) 	*/
	public static final int TM_YMDHMS = 1;	
	/** 날짜 생성 포맷 (월일시분초)		*/
	public static final int TM_MDHMS = 2;
	/** 날짜 생성 포맷 (일시분초) 		*/
	public static final int TM_DHMS = 3;
	/** 날짜 생성 포맷 (시분초) 			*/
	public static final int TM_HMS = 4;
	/** 날짜 생성 포맷 (분초) 			*/
	public static final int TM_MS = 5;
	/** 날짜 생성 포맷 (초)				*/
	public static final int TM_S = 6;
	/** 날짜 생성 포맷 년월일)			*/
	public static final int TM_YMD = 7;	
	/** 날짜 생성 포맷 (년월일시분)		*/
	public static final int TM_YMDHM = 8;

	/** 날짜 생성 포맷 (년)		*/
	public static final int TM_Y = 9;
	/** 날짜 생성 포맷 (월)		*/
	public static final int TM_M = 10;
	/** 날짜 생성 포맷 (일)		*/
	public static final int TM_D = 11;
	/** 날짜 생성 포맷 (시)		*/
	public static final int TM_H = 12;
	/** 날짜 생성 포맷 (분)		*/
	public static final int TM_MI = 13;
	
	/** 날짜 생성 포맷 (년월)		*/
	public static final int TM_YM = 14;
	/** 날짜 생성 포맷 (년월일시)		*/
	public static final int TM_YMDH = 15;
	
	/** 날짜 생성 포맷 (년월일시분초밀리세컨)	*/
	public static final int TM_YMDHMSM = 16;

	/** 요일 (일요일) */
	public static final int SUNDAY = 0;
	/** 요일 (월요일) */
	public static final int MONDAY = 1;
	/** 요일 (회요일) */
	public static final int TUESDAY = 2;
	/** 요일 (수요일) */
	public static final int WEDNESDAY = 3; 
	/** 요일 (목요일) */
	public static final int THURSDAY = 4;
	/** 요일 (금요일) */
	public static final int FRIDAY = 5;
	/** 요일 (토요일) */
	public static final int SATURDAY = 6;
	
	/* =======================================================
			날짜 출력 포맷 설정
	   ======================================================= */
	/** 날짜출력포맷 XXXX/XX/XX			*/
	public static final int DT_FMT1 = 1;
	/** 날짜출력포맷 XXXX-XX-XX			*/
	public static final int DT_FMT2 = 2;
	/** 날짜출력포맷 XXXX년 XX월 XX일	*/
	public static final int DT_KOR = 3;


	/* =======================================================
			일반 로그 출력 관련 변수
	   ======================================================= */
	/**	에러 로그 출력		*/
	public static final int ERR_LOG = 1;
	/**	Debug 로그 출력		*/
	public static final int DEBUG_LOG = 2;
	/** 에러, Debug 로그 출력 	*/
	public static final int ALL_LOG = 3; 

	/* =======================================================
			DB Pool 로그 출력 관련 변수
	   ======================================================= */
	/**	에러 로그 출력		*/
	public static final int DBPOOL_ERR_LOG = 1;
	/**	메세지 로그 출력		*/
	public static final int DBPOOL_MSG_LOG = 2;
	/** 에러, 메세지 로그 출력 	*/
	public static final int DBPOOL_ALL_LOG = 3; 
	/** 메세지 로그 유형 */
	public static final int DBPOOL_GET = 1;
	public static final int DBPOOL_RETURN = 2;
  
	/* =======================================================
			플랫폼 구분 변수
	   ======================================================= */
	/**	Unix 플랫폼			*/
	public static final int UNIX = 0; 
	/**	Window 플랫폼		*/
	public static final int WINDOW = 1;

	/* =======================================================
			데이터베이스 컬럼 타입 변수
	   ======================================================= */
	/**	데이터베이스 컬럼 타입 INT형		*/
	public static final int DB_INT = 1; 
	/**	데이터베이스 컬럼 타입 VARCHAR형	*/
	public static final int DB_VARCHAR = 2;
	/**	데이터베이스 컬럼 타입 LONG형		*/
	public static final int DB_LONG = 3; 
	/**	데이터베이스 컬럼 타입 FLOAT형		*/
	public static final int DB_FLOAT = 4; 
	/**	데이터베이스 컬럼 타입 DOUBLE형		*/
	public static final int DB_DOUBLE = 5; 
	/**	데이터베이스 컬럼 타입 LONG 또는 TEXT 형	*/
	public static final int DB_LVARCHAR = 6; 

	/* =======================================================
			쿼리 컬럼에 대한 메타정보 순번
	   ======================================================= */
	/**	컬럼 메타정보 크기					*/
	public static final int DB_META_SIZE = 5; 
	/**	컬럼명								*/
	public static final int DB_META_COL_NM = 0; 
	/**	컬럼타입							*/
	public static final int DB_META_COL_TYPE = 1;
	/**	컬럼타입(int)						*/
	public static final int DB_META_COL_RAW_TYPE = 2;
	/**	컬럼사이즈							*/
	public static final int DB_META_COL_SIZE = 3; 
	/**	컬럼의 테이블							*/
	public static final int DB_META_TNAME = 4; 

	/* =======================================================
			쿼리 실행 유형
	   ======================================================= */
	/**	쿼리 유형 SELECT(상세정보)			*/
	public static final String SQL_TYPE_SELECT_INFO = "0"; 
	/**	쿼리 유형 SELECT(리스트정보)		*/
	public static final String SQL_TYPE_SELECT_LIST = "1"; 
	/**	쿼리 유형 SELECT(전체리스트정보)	*/
	public static final String SQL_TYPE_SELECT_ALIST = "2"; 
	/**	쿼리 유형 COUNT						*/
	public static final String SQL_TYPE_COUNT = "3"; 
	/**	쿼리 유형 INSERT					*/
	public static final String SQL_TYPE_INSERT = "4"; 
	/**	쿼리 유형 UPDATE					*/
	public static final String SQL_TYPE_UPDATE = "5"; 
	/**	쿼리 유형 DELETE					*/
	public static final String SQL_TYPE_DELETE = "6"; 
	/**	쿼리 유형 PROCEDURE					*/
	public static final String SQL_TYPE_PROCEDURE = "7"; 

	/* =======================================================
			통계 정보 유형
	   ======================================================= */
	/**	시도 횟수 통계 정보			*/
	public static final int STAT_TRY = 0; 
	/**	성공 횟수 통계 정보			*/
	public static final int STAT_SUCCESS = 1; 
	/**	실패 횟수 통계 정보			*/
	public static final int STAT_FAIL = 2; 

	/* =======================================================
			메일 전송 로그 관련
	   ======================================================= */
	/** 메일 전송 에러로그			*/
	public static final int MAIL_ER_LOG = 1;
	/** 메일 전송 전송성공로그		*/
	public static final int MAIL_TR_LOG = 2;

	/* =======================================================
			Filler 위치 관련
	   ======================================================= */
	/** 앞쪽 필러		*/
	public static final int PREFIX_FILLER = 0;
	/** 뒷쪽 필러		*/
	public static final int SUFFIX_FILLER = 1;

	/* =======================================================
			DB Vendor 리스트
	   ======================================================= */
	/** DB Vendor : ORACLE		*/
	public static final String DB_VENDOR_ORACLE = "000";
	/** DB Vendor : MSSQL		*/
	public static final String DB_VENDOR_MSSQL = "001";
	/** DB Vendor : INFORMIX	*/
	public static final String DB_VENDOR_INFORMIX = "002"; 
	/** DB Vendor : SYBASE		*/
	public static final String DB_VENDOR_SYBASE = "003";
	/** DB Vendor : MYSQL		*/
	public static final String DB_VENDOR_MYSQL = "004";
	/** DB Vendor : UNISQL		*/
	public static final String DB_VENDOR_UNISQL = "005";
	/** DB Vendor : INGRES		*/
	public static final String DB_VENDOR_INGRES = "006";
	/** DB Vendor : IBM DB2		*/
	public static final String DB_VENDOR_DB2 = "007";
	/** DB Vendor : POSTGRES	*/
	public static final String DB_VENDOR_POSTGRES = "008";
	/** DB Vendor : CUBRID	*/
	public static final String DB_VENDOR_CUBRID = "009";

	/* =======================================================
			환경값 COPY값에 타입
	   ======================================================= */
	/** 주석		*/
	public static final String ENV_COMMENT = "C";
	/** 환경값		*/
	public static final String ENV_VALUE = "V";

	/* =======================================================
			문자 출력 형식 포맷 정보
	   ======================================================= */
	/** 형식(3-6-5)		*/
	public static final int FORMAT_A = 0;
	/** 금액형식(100,000)		*/
	public static final int FORMAT_M = 1;
	/** 날짜형식(2004/01/11)		*/
	public static final int FORMAT_D = 2;
	/** 시간형식(01:11:24)		*/
	public static final int FORMAT_T = 3;
	/** 주민번호(6-7)		*/
	public static final int FORMAT_R = 4;
	/** 형식(4-5-4)		*/
	public static final int FORMAT_P = 5;
	/** 형식(4-4-4)		*/
	public static final int FORMAT_K = 6;
	/** 우편번호		*/
	public static final int FORMAT_Z = 7;
	/** 사업자번호(3-2-5)		*/
	public static final int FORMAT_O = 8;
	/** 형식(6-7)		*/
	public static final int FORMAT_C = 9;

	/* =======================================================
			쿠키 암호화 방법 정보
	   ======================================================= */
	/** 없음			*/
	public static final int COOKIE_CRYPT_NONE = 0;
	/** BASE64			*/
	public static final int COOKIE_CRYPT_BASE64 = 1;

	/* =======================================================
			세션 암호화 방법 정보
	   ======================================================= */
	/** 없음			*/
	public static final int SESSION_CRYPT_NONE = 0;
	/** BASE64			*/
	public static final int SESSION_CRYPT_BASE64 = 1;

	/* =======================================================
			UI 언어권 정보
	   ======================================================= */
	/** 언어권 배열 저장 크기	*/
	public static final int UI_LANG_SIZE = 4;

	/** 한국어	*/
	public static final String UI_LANG_KOR = "000";
	/** 영어	*/
	public static final String UI_LANG_ENG = "001";
	/** 일어	*/
	public static final String UI_LANG_JPN = "002";
	/** 중국어	*/
	public static final String UI_LANG_CHI = "003";

	/** 한국어 배열 저장 위치	*/
	public static final int UI_LANG_KOR_POS = 0;
	/** 영어 배열 저장 위치	*/
	public static final int UI_LANG_ENG_POS = 1;
	/** 일어 배열 저장 위치	*/
	public static final int UI_LANG_JPN_POS = 2;
	/** 중국어 배열 저장 위치	*/
	public static final int UI_LANG_CHI_POS = 3;
	
	/* =======================================================
			수신 로그 출력 레벨(주기)
	   ======================================================= */
	/** 년단위 */
	public static final int RESP_LOG_YEAR = 1;
	/** 월단위	*/
	public static final int RESP_LOG_MONTH = 2;
	/** 일단위	*/
	public static final int RESP_LOG_DAY = 3;
	/** 시간위	*/
	public static final int RESP_LOG_HOUR = 4;
	
	/* =======================================================
			랜덤 문자열 출력 유형 
	======================================================= */
	/** 숫자 */
	public static final int RAND_TYPE_A = 1;
	/** 숫자 + 문자 */
	public static final int RAND_TYPE_B = 2;
	/** 숫자 + 문자 + 특수문자*/
	public static final int RAND_TYPE_C = 3; 

}
