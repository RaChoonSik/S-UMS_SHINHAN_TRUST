/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : DB 연결 작업 처리.
 */
package kr.co.sict.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import kr.co.sict.ums.sys.seg.vo.SegmentMemberVO;
import kr.co.sict.ums.sys.seg.vo.SegmentVO;
import kr.co.sict.ums.sys.dbc.vo.MetaColumnVO; 

public class DBUtil {
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * DB Connection을 생성한다.
	 * @param dbDriver
	 * @param dbUrl
	 * @param loginId
	 * @param loginPwd
	 * @return
	 * @throws Exception
	 */
	public Connection getConnection(String dbDriver, String dbUrl, String loginId, String loginPwd) throws Exception {
		Connection conn = null;
		
		Class.forName(dbDriver);
		conn = DriverManager.getConnection(dbUrl, loginId, loginPwd);
		
		return conn;
	}
	
	/**
	 * DB Connection을 생성한다.(DataSouce 이용)
	 * @param dataSourceName
	 * @return
	 * @throws Exception
	 */
	public Connection getConnection(String dataSourceName) throws Exception {
		Connection conn = null;
		
		Context ctx = new InitialContext();
		DataSource ds = (DataSource) ctx.lookup(dataSourceName);
		conn = ds.getConnection();
		
		return conn;
	}
	
	/**
	 * DB별 테이블 목록을 조회한다.
	 * @param dbTy
	 * @param dbDriver
	 * @param dbUrl
	 * @param loginId
	 * @param loginPwd
	 * @return
	 */
	public List<String> getRealTableList(String dbTy, String dbDriver, String dbUrl, String loginId, String loginPwd) {
		logger.debug("getRealTableList dbTy = " + dbTy);
		List<String> tableList = new ArrayList<String>();
		
		String sql = "";
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rss = null;
		try {
			conn = getConnection(dbDriver, dbUrl, loginId, loginPwd);
			
			if(Code.DB_VENDOR_ORACLE.equals(dbTy)) {	// Oracle
				sql =  "SELECT OBJECT_NAME TABLE_NAME 			";
				sql += "  FROM USER_OBJECTS 					";
				sql += " WHERE OBJECT_TYPE IN ('TABLE','VIEW') 	";
				sql += "   AND NOT OBJECT_NAME LIKE '%NEO_%' 	";
				sql += "   AND NOT OBJECT_NAME LIKE 'TS_%'		";
				sql += "   AND STATUS = 'VALID' 				";
				
				sql += " ORDER BY OBJECT_NAME 					";
			} else if(Code.DB_VENDOR_MSSQL.equals(dbTy)) {		//MSSQL
				sql =  "SELECT A.NAME TABLE_NAME 				";
				sql += "  FROM SYSOBJECTS A, SYSUSERS B 		";
				sql += " WHERE A.UID = B.UID 					";
				sql += "   AND A.TYPE IN ('U','V') 				";
				sql += "   AND A.NAME NOT LIKE '%NEO_%'			";
				sql += "   AND A.NAME NOT LIKE 'TS_%' 			";
				sql += " ORDER BY A.NAME 						";
			} else if(Code.DB_VENDOR_INFORMIX.equals(dbTy)) {	//INFORMIX
				sql =  "SELECT TABNAME TABLE_NAME 				";
				sql += "  FROM SYSTABLES 						";
				sql += " WHERE TABTYPE IN ('T','V') 			";
				sql += "   AND TABNAME NOT LIKE '%NEO_%' 		";
				sql += "   AND TABNAME NOT LIKE 'TS_%' 			";
				sql += "   AND TABID >= 100 					";
				sql += " ORDER BY TABNAME 						";
			} else if(Code.DB_VENDOR_SYBASE.equals(dbTy)) {	//SYBASE
				sql =  "SELECT name TABLE_NAME 					";
				sql += "  FROM sysobjects 						";
				sql += " WHERE type IN ('U','V') 				";
				sql += "   AND name NOT LIKE '%NEO_%' 			";
				sql += "   AND name NOT LIKE 'TS_%' 			";
				sql += " ORDER BY name 							";
			} else if(Code.DB_VENDOR_MYSQL.equals(dbTy)) {		//MYSQL
				String schema = "";
				if(dbUrl.indexOf("?") > 0) {
					dbUrl = dbUrl.substring(0,dbUrl.indexOf("?"));
				}
				schema = dbUrl.split("/")[dbUrl.split("/").length-1];
				
				sql =  "SELECT TABLE_NAME						";
				sql += "  FROM INFORMATION_SCHEMA.TABLES		";
				sql += " WHERE TABLE_SCHEMA = '" + schema + "'	";
				sql += "   AND TABLE_NAME NOT LIKE '%NEO_%' 	";
				sql += "   AND TABLE_NAME NOT LIKE 'TS_%' 		";
				sql += " ORDER BY TABLE_NAME					";
			} else if(Code.DB_VENDOR_CUBRID.equals(dbTy)) {		//CUBRID
				sql =  "SELECT class_name AS TABLE_NAME 		";
				sql += "  FROM db_class 						";
				sql += " WHERE class_name  NOT LIKE '%neo_%' 	";
				sql += "  AND class_name NOT LIKE 'ts_%' 		";
				sql += "  AND class_name NOT LIKE 'db_%' 		";
			} else if(Code.DB_VENDOR_UNISQL.equals(dbTy)) {	//UNISQL
			} else if(Code.DB_VENDOR_INGRES.equals(dbTy)) {	//INGRES
			} else if(Code.DB_VENDOR_DB2.equals(dbTy)) {		//DB2
				sql =  "SELECT TABNAME TABLE_NAME 				";
				sql += "  FROM SYSCAT.TABLES ";
				sql += " WHERE TABSCHEMA = '" +loginId.toUpperCase() + "' ";
				sql += "   AND TABNAME NOT LIKE '%NEO_%' 		";
				sql += "   AND TABNAME NOT LIKE 'TS_%' 		";
				sql += " ORDER BY TABNAME 						";
			} else if(Code.DB_VENDOR_POSTGRES.equals(dbTy)) {	//POSTGRES
				sql =  "SELECT TABLENAME TABLE_NAME 				";
				sql += "  FROM PG_TABLES ";
				sql += " WHERE UPPER(TABLEOWNER) = '" +loginId.toUpperCase() + "' ";
				sql += "   AND UPPER(TABLENAME)NOT LIKE '%NEO_%' 		";
				sql += "   AND UPPER(TABLENAME) NOT LIKE 'TS_%' 		";
				sql += " ORDER BY TABLENAME 						";
			} else {
				sql = "";
			}
			
			pstm = conn.prepareStatement(sql);
			rss = pstm.executeQuery();
			while(rss.next()) {
				tableList.add(rss.getString("TABLE_NAME").toUpperCase());
			}
		} catch(Exception e) {
			System.out.println("getRealTableList error = " + e);
		} finally {
			if(rss != null) try { rss.close(); } catch(Exception ex) {}
			if(pstm != null) try { pstm.close(); } catch(Exception ex) {}
			if(conn != null) try { conn.close(); } catch(Exception ex) {}
		}
		
		return tableList;
	}
	
	/**
	 * DB별 테이블의 컬럼 목록을 조회한다.
	 * @param dbTy
	 * @param dbDriver
	 * @param dbUrl
	 * @param loginId
	 * @param loginPwd
	 * @param tblNm
	 * @return
	 */
	public List<MetaColumnVO> getRealColumnList(String dbTy, String dbDriver, String dbUrl, String loginId, String loginPwd, String tblNm) {
		logger.debug("getRealTableList dbTy = " + dbTy);
		logger.debug("getRealTableList tblNm = " + tblNm);
		List<MetaColumnVO> columnList = new ArrayList<MetaColumnVO>();
		
		String sql = "";
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rss = null;
		try {
			conn = getConnection(dbDriver, dbUrl, loginId, loginPwd);
			
			if(Code.DB_VENDOR_ORACLE.equals(dbTy)) {			//오라클
				sql =  " SELECT TABLE_NAME TBL_NM,          ";
				sql += "		   COLUMN_NAME COL_NM,		";
				sql += "		   DATA_TYPE COL_DATA_TY,	";
				sql += "		   '' COL_DATA_TY_JDBC		";
				sql += "   FROM USER_TAB_COLUMNS			";
				sql += "	 WHERE TABLE_NAME = '" + tblNm + "'";
			} else if(Code.DB_VENDOR_MSSQL.equals(dbTy)) {		//MSSQL
				sql =  " SELECT B.NAME TBL_NM, 				";
				sql += "		   A.NAME COL_NM,			";
				sql += "		   C.NAME COL_DATA_TY,		";
				sql += "		   '' COL_DATA_TY_JDBC		";
				sql += "   FROM SYSCOLUMNS A,				";
				sql += "	       SYSOBJECTS B,			";
				sql += "		   SYSTYPES C				";
				sql += "	 WHERE A.ID = B.ID				";
				sql += "	   AND B.NAME = '" + tblNm + "'	";
				sql += "	   AND A.XTYPE = C.XUSERTYPE";
			} else if(Code.DB_VENDOR_INFORMIX.equals(dbTy)) {	//INFORMIX
				sql =  " SELECT B.TABNAME TBL_NM, 			";
				sql += "		   A.COLNAME COL_NM,		";
				sql += "		   A.COLTYPE COL_DATA_TY,	";
				sql += "		   '' COL_DATA_TY_JDBC		";
				sql += "   FROM SYSCOLUMNS A,				";
				sql += "	       SYSTABLES B				";
				sql += "	 WHERE A.TABID = B.TABID		";
				sql += "	   AND B.TABNAME = '" + tblNm + "'";
			} else if(Code.DB_VENDOR_SYBASE.equals(dbTy)) {	//SYBASE
				sql =  " SELECT B.name TBL_NM, 				";
				sql += "		   A.name COL_NM,			";
				sql += "		   C.name COL_DATA_TY,		";
				sql += "		   '' COL_DATA_TY_JDBC		";
				sql += "   FROM syscolumns A,				";
				sql += "	       sysobjects B,			";
				sql += "		   systypes C				";
				sql += "	 WHERE A.id = B.id				";
				sql += "	   AND B.name = '" + tblNm + "'	";
				sql += "	   AND A.usertype = C.usertype	";
			} else if(Code.DB_VENDOR_MYSQL.equals(dbTy)) {		//MYSQL
				String schema = "";
				if(dbUrl.indexOf("?") > 0) {
					dbUrl = dbUrl.substring(0,dbUrl.indexOf("?"));
				}
				schema = dbUrl.split("/")[dbUrl.split("/").length-1];

				sql =  "SELECT TABLE_NAME TBL_NM,			";
				sql += "		COLUMN_NAME COL_NM,			";
				sql += "		DATA_TYPE COL_DATA_TY,		";
				sql += "		'' COL_DATA_TY_JDBC			";
				sql += "  FROM INFORMATION_SCHEMA.COLUMNS	";
				sql += " WHERE TABLE_SCHEMA = '" + schema + "'	";
				sql += "   AND TABLE_NAME = '" + tblNm + "'	";
				sql += " ORDER BY ORDINAL_POSITION			";
			} else if(Code.DB_VENDOR_CUBRID.equals(dbTy)) {		//CUBRID
				sql =  " SELECT class_name AS TBL_NM, attr_name AS COL_NM, [DATA_TYPE] AS COL_DATA_TY, '' COL_DATA_TY_JDBC ";
				sql += "	from db_attribute 		     	         ";
				sql += "	 WHERE class_name ='" + tblNm + "'";
			} else if(Code.DB_VENDOR_UNISQL.equals(dbTy)) {	//UNISQL
			} else if(Code.DB_VENDOR_INGRES.equals(dbTy)) {	//INGRES
			} else if(Code.DB_VENDOR_DB2.equals(dbTy)) {		//DB2
				sql =  " SELECT TABNAME TBL_NM,				";
				sql += "   	   COLNAME COL_NM,				";
				sql += "		   TYPENAME COL_DATA_TY,	";
				sql += "		   '' COL_DATA_TY_JDBC		";
				sql += "	  FROM SYSCAT.COLUMNS			";
				sql += "  WHERE TABSCHEMA = '" + loginId.toUpperCase() + "'	";
				sql += "	   AND TABNAME = '" + tblNm + "'";
			} else if(Code.DB_VENDOR_POSTGRES.equals(dbTy)) {	//POSTGRES
				sql =  " SELECT table_name TBL_NM,				";
				sql += "   	   column_name COL_NM,				";
				sql += "		   data_type COL_DATA_TY,	";
				sql += "		   '' COL_DATA_TY_JDBC		";
				sql += "	  FROM information_schema.\"columns\"  ";
				sql += "  WHERE UPPER(TABLENAME) =UPPER( '" + tblNm + "' )	";  
			} else {
				sql = "";
			}
			
			logger.debug("getRealColumnList sql = " + sql);
			
			
			/*
			pstm = conn.prepareStatement(sql);
			rss = pstm.executeQuery();
			
			while(rss.next()) {
				MetaColumnVO metaColumn = new MetaColumnVO();
				metaColumn.setTblNm(rss.getString("TBL_NM").toUpperCase());
				metaColumn.setColNm(rss.getString("COL_NM").toUpperCase());
				metaColumn.setColDataTy(rss.getString("COL_DATA_TY").toUpperCase());
				metaColumn.setColDataTyJdbc(rss.getString("COL_DATA_TY_JDBC"));
				
				columnList.add(metaColumn);
			}
			*/
			
			sql = "SELECT * FROM " + tblNm + " WHERE 1 = 2 ";
			
			pstm = conn.prepareStatement(sql);
			rss = pstm.executeQuery();
			ResultSetMetaData metaData = rss.getMetaData();
			int colcnt = metaData.getColumnCount();
			for(int cnt = 1; cnt <= colcnt; cnt++) {
				MetaColumnVO metaColumn = new MetaColumnVO();
				metaColumn.setTblNm(tblNm);
				metaColumn.setColNm(metaData.getColumnName(cnt).toUpperCase());
				metaColumn.setColDataTy(metaData.getColumnTypeName(cnt));
				metaColumn.setColDataTyJdbc(Integer.toString(metaData.getColumnType(cnt)));
				columnList.add(metaColumn);
			} 
		} catch(Exception e) {
			System.out.println("getRealColumnList error = " + e);
		} finally {
			if(rss != null) try { rss.close(); } catch(Exception ex) {}
			if(pstm != null) try { pstm.close(); } catch(Exception ex) {}
			if(conn != null) try { conn.close(); } catch(Exception ex) {}
		}
		
		return columnList;
	}
	
	/**
	 * 쿼리 실행 데이터 건수를 조회한다.
	 * @param dbDriver
	 * @param dbUrl
	 * @param loginId
	 * @param loginPwd
	 * @param segmentInfo
	 * @return
	 */
	public int getSegmentCount(String dbDriver, String dbUrl, String loginId, String loginPwd, SegmentVO segmentInfo) {
		logger.debug("getMemberList query     = " + segmentInfo.getQuery());
		logger.debug("getMemberList selectSql = " + segmentInfo.getSelectSql());
		logger.debug("getMemberList fromSql   = " + segmentInfo.getFromSql());
		logger.debug("getMemberList whereSql  = " + segmentInfo.getWhereSql());
		
		String query      = StringUtil.setUpperString(segmentInfo.getQuery());
		String selectSql  = StringUtil.setUpperString(segmentInfo.getSelectSql());
		String fromSql    = StringUtil.setUpperString(segmentInfo.getFromSql());
		String whereSql   = StringUtil.setUpperString(segmentInfo.getWhereSql());
		
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rss = null;
		
		String sql = "";
		if("002".equals(segmentInfo.getCreateTy())) {	// 직접SQL입력
			sql  = "SELECT COUNT(*) TOT_CNT ";
			sql += "  FROM ( ";
			sql += query;
			sql += "       ) NEO_DUAL";
		} else {
			String fromUnder = "FROM " + fromSql + " ";
			if(whereSql != null && whereSql.length() > 0) {
				fromUnder += "WHERE " + whereSql + " ";
			}
			if(selectSql != null && selectSql.toUpperCase().indexOf("DISTINCT") != -1) {
				sql = "SELECT " + selectSql + " " + fromUnder;
				sql = "SELECT COUNT(*) TOT_CNT FROM (" + sql.trim() + ")";
			} else {
				sql = "SELECT COUNT(*) TOT_CNT " + fromUnder;
			}
		}
		
		logger.debug("getSegmentCount sql = " + sql);
		
		int totCnt = 0;
		try {
			conn = getConnection(dbDriver, dbUrl, loginId, loginPwd);
			pstm = conn.prepareStatement(sql);
			rss = pstm.executeQuery();
			if(rss.next()) {
				totCnt = rss.getInt("TOT_CNT");
			}
		} catch(Exception e) {
			logger.error("getSegmentCount error = " + e);
		} finally {
			if(rss != null) try { rss.close(); } catch(Exception e) {}
			if(pstm != null) try { pstm.close(); } catch(Exception e) {}
			if(conn != null) try { conn.close(); } catch(Exception e) {}
		}
		
		return totCnt;
	}
	
	/**
	 * 쿼리 실행 데이터 건수를 조회한다.(데이터 소스 이용)
	 * @param dataSourceName
	 * @param segmentInfo
	 * @return
	 */
	public int getSegmentCount(String dataSourceName, SegmentVO segmentInfo) {
		logger.debug("getMemberList query     = " + segmentInfo.getQuery());
		logger.debug("getMemberList selectSql = " + segmentInfo.getSelectSql());
		logger.debug("getMemberList fromSql   = " + segmentInfo.getFromSql());
		logger.debug("getMemberList whereSql  = " + segmentInfo.getWhereSql());
		
		String query      = StringUtil.setUpperString(segmentInfo.getQuery());
		String selectSql  = StringUtil.setUpperString(segmentInfo.getSelectSql());
		String fromSql    = StringUtil.setUpperString(segmentInfo.getFromSql());
		String whereSql   = StringUtil.setUpperString(segmentInfo.getWhereSql());
		
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rss = null;
		
		String sql = "";
		if("002".equals(segmentInfo.getCreateTy())) {	// 직접SQL입력
			sql  = "SELECT COUNT(*) TOT_CNT ";
			sql += "  FROM ( ";
			sql += query;
			sql += "       ) NEO_DUAL";
		} else {
			String fromUnder = "FROM " + fromSql + " ";
			if(whereSql != null && whereSql.length() > 0) {
				fromUnder += "WHERE " + whereSql + " ";
			}
			if(selectSql != null && selectSql.toUpperCase().indexOf("DISTINCT") != -1) {
				sql = "SELECT " + selectSql + " " + fromUnder;
				sql = "SELECT COUNT(*) TOT_CNT FROM (" + sql.trim() + ")";
			} else {
				sql = "SELECT COUNT(*) TOT_CNT " + fromUnder;
			}
		}
		
		logger.debug("getSegmentCount sql = " + sql);
		
		int totCnt = 0;
		try {
			conn = getConnection(dataSourceName);
			pstm = conn.prepareStatement(sql);
			rss = pstm.executeQuery();
			if(rss.next()) {
				totCnt = rss.getInt("TOT_CNT");
			}
		} catch(Exception e) {
			totCnt = -1;
			logger.error("getSegmentCount error = " + e);
		} finally {
			if(rss != null) try { rss.close(); } catch(Exception e) {}
			if(pstm != null) try { pstm.close(); } catch(Exception e) {}
			if(conn != null) try { conn.close(); } catch(Exception e) {}
		}
		
		return totCnt;
	}
	
	/**
	 * SQL 실행결과 목록을 조회한다.
	 * @param dbDriver
	 * @param dbUrl
	 * @param loginId
	 * @param loginPwd
	 * @param segmentInfo
	 * @return
	 */
	public SegmentMemberVO getMemberList(String dbDriver, String dbUrl, String loginId, String loginPwd, SegmentVO segmentInfo) {
		logger.debug("getMemberList query     = " + segmentInfo.getQuery());
		logger.debug("getMemberList selectSql = " + segmentInfo.getSelectSql());
		logger.debug("getMemberList fromSql   = " + segmentInfo.getFromSql());
		logger.debug("getMemberList whereSql  = " + segmentInfo.getWhereSql());
		
		String query      = StringUtil.setUpperString(segmentInfo.getQuery());
		String selectSql  = StringUtil.setUpperString(segmentInfo.getSelectSql());
		String fromSql    = StringUtil.setUpperString(segmentInfo.getFromSql());
		String whereSql   = StringUtil.setUpperString(segmentInfo.getWhereSql());
		String orderbySql = StringUtil.setUpperString(segmentInfo.getOrderbySql());
		
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rss = null;
		
		SegmentMemberVO memberVO = new SegmentMemberVO();
		List<HashMap<String,String>> memberList = new ArrayList<HashMap<String, String>>();
		
		String cSql = "";
		String sql = "";
        if("002".equals(segmentInfo.getCreateTy())) {		//직접 SQL 입력
        	sql = query;
        	cSql = "SELECT COUNT(*) " + query.substring(query.indexOf("FROM"), query.length());
        } else {

	        // Legacy DB에서 회원정보를 추출함.
        	sql  = "SELECT " +  selectSql + " ";
        	sql += "FROM " + fromSql + " ";
        	if(whereSql != null && !"".equals(whereSql)) {
            	if(segmentInfo.getValue() != null && !"".equals(segmentInfo.getValue())) {
            		sql += "WHERE " + whereSql + " AND " + segmentInfo.getSearch() + " LIKE '%" + segmentInfo.getValue() + "%' ";
            	} else {
            		sql += "WHERE " + whereSql + " ";
            	}
        	} else {
            	if(segmentInfo.getValue() != null && !"".equals(segmentInfo.getValue())) {
            		sql += "WHERE " + segmentInfo.getSearch() + " LIKE '%" + segmentInfo.getValue() + "%' ";
            	}
        	}
        	if(orderbySql != null && !"".equals(orderbySql.trim())) {
        		sql += "ORDER BY " + orderbySql;
        	}

        	String fromUnder = "FROM " + fromSql + " ";
        	if(whereSql != null && !"".equals(whereSql)) {
            	if(segmentInfo.getValue() != null && !"".equals(segmentInfo.getValue())) {
            		fromUnder += "WHERE " + whereSql + " AND " + segmentInfo.getSearch() + " LIKE '%" + segmentInfo.getValue() + "%' ";
            	} else {
            		fromUnder += "WHERE " + whereSql + " ";
            	}
        	} else {
            	if(segmentInfo.getValue() != null && !"".equals(segmentInfo.getValue())) {
            		fromUnder += "WHERE " + segmentInfo.getSearch() + " LIKE '%" + segmentInfo.getValue() + "%' ";
            	}
        	}

        	if(selectSql.toUpperCase().indexOf("DISTINCT") != -1) {
            	cSql = "SELECT COUNT(DISTINCT *)  " + fromUnder;
        	} else {
            	cSql = "SELECT COUNT(*) " + fromUnder.trim();
        	}
        }
        
        logger.debug("getMemberList sql = " + sql);
        logger.debug("getMemberList cSql = " + cSql);
        logger.debug("getMemberList mergeKey = " + segmentInfo.getMergeKey());
        
        try {
        	conn = getConnection(dbDriver, dbUrl, loginId, loginPwd);
        	
        	// 전체건수 조회
        	pstm = conn.prepareStatement(cSql);
        	rss = pstm.executeQuery();
        	if(rss.next()) {
            	logger.debug("getMemberList Total Count data found!!");
        		memberVO.setTotalCount(rss.getInt(1));
        	} else {
        		memberVO.setTotalCount(0);
        	}
        	rss.close();
        	pstm.close();
        	
        	// 데이터 상세 조회
        	pstm = conn.prepareStatement(sql);
        	rss = pstm.executeQuery();
        	while(rss.next()) {
        		int idx = 1;
        		HashMap<String,String> data = new HashMap<String,String>();
        		StringTokenizer mkey = new StringTokenizer(segmentInfo.getMergeKey(), ",");
        		while(mkey.hasMoreTokens()) {
        			String tmpMkey = mkey.nextToken();
        			data.put(tmpMkey, rss.getString(idx));
        			idx++;
        		}
        		memberList.add(data);
        	}
        	
        	memberVO.setMemberList(memberList);
        	
        } catch(Exception e) {
        	logger.error("getMemberList error = " + e);
        } finally {
			if(rss != null) try { rss.close(); } catch(Exception ex) {}
			if(pstm != null) try { pstm.close(); } catch(Exception ex) {}
			if(conn != null) try { conn.close(); } catch(Exception ex) {}
        }
		
		return memberVO;
	}
	
	/**
	 * SQL 실행결과 목록을 조회한다.(데이터 소스 이용)
	 * @param dataSourceName
	 * @param segmentInfo
	 * @return
	 */
	public SegmentMemberVO getMemberList(String dataSourceName, SegmentVO segmentInfo) {
		logger.debug("getMemberList query     = " + segmentInfo.getQuery());
		logger.debug("getMemberList selectSql = " + segmentInfo.getSelectSql());
		logger.debug("getMemberList fromSql   = " + segmentInfo.getFromSql());
		logger.debug("getMemberList whereSql  = " + segmentInfo.getWhereSql());
		
		String query      = StringUtil.setUpperString(segmentInfo.getQuery());
		String selectSql  = StringUtil.setUpperString(segmentInfo.getSelectSql());
		String fromSql    = StringUtil.setUpperString(segmentInfo.getFromSql());
		String whereSql   = StringUtil.setUpperString(segmentInfo.getWhereSql());
		String orderbySql = StringUtil.setUpperString(segmentInfo.getOrderbySql());
		
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rss = null;
		
		SegmentMemberVO memberVO = new SegmentMemberVO();
		List<HashMap<String,String>> memberList = new ArrayList<HashMap<String, String>>();
		
		String cSql = "";
		String sql = "";
        if("002".equals(segmentInfo.getCreateTy())) {		//직접 SQL 입력
        	sql = query;
        	cSql = "SELECT COUNT(*) " + query.substring(query.indexOf("FROM"), query.length());
        } else {

	        // Legacy DB에서 회원정보를 추출함.
        	sql  = "SELECT " +  selectSql + " ";
        	sql += "FROM " + fromSql + " ";
        	if(whereSql != null && !"".equals(whereSql)) {
            	if(segmentInfo.getValue() != null && !"".equals(segmentInfo.getValue())) {
            		sql += "WHERE " + whereSql + " AND " + segmentInfo.getSearch() + " LIKE '%" + segmentInfo.getValue() + "%' ";
            	} else {
            		sql += "WHERE " + whereSql + " ";
            	}
        	} else {
            	if(segmentInfo.getValue() != null && !"".equals(segmentInfo.getValue())) {
            		sql += "WHERE " + segmentInfo.getSearch() + " LIKE '%" + segmentInfo.getValue() + "%' ";
            	}
        	}
        	if(orderbySql != null && !"".equals(orderbySql.trim())) {
        		sql += "ORDER BY " + orderbySql;
        	}

        	String fromUnder = "FROM " + fromSql + " ";
        	if(whereSql != null && !"".equals(whereSql)) {
            	if(segmentInfo.getValue() != null && !"".equals(segmentInfo.getValue())) {
            		fromUnder += "WHERE " + whereSql + " AND " + segmentInfo.getSearch() + " LIKE '%" + segmentInfo.getValue() + "%' ";
            	} else {
            		fromUnder += "WHERE " + whereSql + " ";
            	}
        	} else {
            	if(segmentInfo.getValue() != null && !"".equals(segmentInfo.getValue())) {
            		fromUnder += "WHERE " + segmentInfo.getSearch() + " LIKE '%" + segmentInfo.getValue() + "%' ";
            	}
        	}

        	if(selectSql.toUpperCase().indexOf("DISTINCT") != -1) {
            	cSql = "SELECT COUNT(DISTINCT *)  " + fromUnder;
        	} else {
            	cSql = "SELECT COUNT(*) " + fromUnder.trim();
        	}
        }
        
        logger.debug("getMemberList sql = " + sql);
        logger.debug("getMemberList cSql = " + cSql);
        logger.debug("getMemberList mergeKey = " + segmentInfo.getMergeKey());
        
        try {
        	conn = getConnection(dataSourceName);
        	
        	// 전체건수 조회
        	pstm = conn.prepareStatement(cSql);
        	rss = pstm.executeQuery();
        	if(rss.next()) {
        		memberVO.setTotalCount(rss.getInt(1));
        	} else {
        		memberVO.setTotalCount(0);
        	}
        	rss.close();
        	pstm.close();
        	
        	// 데이터 상세 조회
        	pstm = conn.prepareStatement(sql);
        	rss = pstm.executeQuery();
        	while(rss.next()) {
        		int idx = 1;
        		HashMap<String,String> data = new HashMap<String,String>();
        		StringTokenizer mkey = new StringTokenizer(segmentInfo.getMergeKey(), ",");
        		while(mkey.hasMoreTokens()) {
        			String tmpMkey = mkey.nextToken();
        			data.put(tmpMkey, rss.getString(idx));
        			idx++;
        		}
        		memberList.add(data);
        	}
        	
        	memberVO.setMemberList(memberList);
        	
        } catch(Exception e) {
        	logger.error("getMemberList error = " + e);
        } finally {
			if(rss != null) try { rss.close(); } catch(Exception ex) {}
			if(pstm != null) try { pstm.close(); } catch(Exception ex) {}
			if(conn != null) try { conn.close(); } catch(Exception ex) {}
        }
		
		return memberVO;
	}
	
	/**
	 * SQL 직접 입력 테스트
	 * @param dbDriver
	 * @param dbUrl
	 * @param loginId
	 * @param loginPwd
	 * @param segmentInfo
	 * @return
	 */
	public SegmentMemberVO getDirectSqlTest(String dbDriver, String dbUrl, String loginId, String loginPwd, SegmentVO segmentInfo) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rss = null;
		ResultSetMetaData mrs = null;
		
		SegmentMemberVO memberVO = new SegmentMemberVO();
		
		String sql = segmentInfo.getQuery();
		logger.debug("getDirectSqlTest sql = " + sql);
		
		try {
			conn = getConnection(dbDriver, dbUrl, loginId, loginPwd);
			
			pstm = conn.prepareStatement(sql);
			rss = pstm.executeQuery();
			mrs = rss.getMetaData();
			
			String mergeKey = "";
			int cols = mrs.getColumnCount();
			for(int i=1;i<=cols;i++) {
				// oracle : getColumnName(i)과 getColumnLabel(i)이 알리아스명
				// mariadb: getColumnName(i)은 원래 컬러명 getColumnLabel(i)이 알리아스명
				//dbDriver : org.mariadb.jdbc.Driver
				if(dbDriver.indexOf("maria") > 0 ) {
					if(i == 1) {
						mergeKey += mrs.getColumnLabel(i).toUpperCase();
					} else {
						mergeKey += "," + mrs.getColumnLabel(i).toUpperCase();
					}
				} else {
					if(i == 1) {
						mergeKey += mrs.getColumnName(i).toUpperCase();
					} else {
						mergeKey += "," + mrs.getColumnName(i).toUpperCase();
					}
				}
			}
			
			memberVO.setMergeKey(mergeKey);
			memberVO.setResult(true);
		} catch(Exception e) {
			logger.error("getDirectSqlTest error = " + e);
			memberVO.setMessage(e.getMessage());
			memberVO.setResult(false);
		} finally {
			if(rss != null) try { rss.close(); } catch(Exception e) {}
			if(pstm != null) try { pstm.close(); } catch(Exception e) {}
			if(conn != null) try { conn.close(); } catch(Exception e) {}
		}
		
		return memberVO;
	}
	
	/**
	 * SQL 직접 입력 테스트(데이터 소스 이용)
	 * @param dataSourceName
	 * @param segmentInfo
	 * @return
	 */
	public SegmentMemberVO getDirectSqlTest(String dataSourceName, SegmentVO segmentInfo) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rss = null;
		ResultSetMetaData mrs = null;
		
		SegmentMemberVO memberVO = new SegmentMemberVO();
		
		String sql = segmentInfo.getQuery();
		logger.debug("getDirectSqlTest sql = " + sql);
		
		try {
			conn = getConnection(dataSourceName);
			
			pstm = conn.prepareStatement(sql);
			rss = pstm.executeQuery();
			mrs = rss.getMetaData();
			
			String mergeKey = "";
			int cols = mrs.getColumnCount();
			for(int i=1;i<=cols;i++) {
				// oracle : getColumnName(i)과 getColumnLabel(i)이 알리아스명
				// mariadb: getColumnName(i)은 원래 컬러명 getColumnLabel(i)이 알리아스명
				//dataSourceNAme : java:/comp/env/MPV31ForMaria
				if(dataSourceName.indexOf("Maria") > 0 ) {
					if(i == 1) {
						mergeKey += mrs.getColumnLabel(i).toUpperCase();
					} else {
						mergeKey += "," + mrs.getColumnLabel(i).toUpperCase();
					}
				} else {
					if(i == 1) {
						mergeKey += mrs.getColumnName(i).toUpperCase();
					} else {
						mergeKey += "," + mrs.getColumnName(i).toUpperCase();
					}
				}
			}
			
			memberVO.setMergeKey(mergeKey);
			memberVO.setResult(true);
		} catch(Exception e) {
			logger.error("getDirectSqlTest error = " + e);
			memberVO.setMessage(e.getMessage());
			memberVO.setResult(false);
		} finally {
			if(rss != null) try { rss.close(); } catch(Exception e) {}
			if(pstm != null) try { pstm.close(); } catch(Exception e) {}
			if(conn != null) try { conn.close(); } catch(Exception e) {}
		}
		
		return memberVO;
	}
	
	/*
	public static void main(String[] args) {
		
		DBUtil util = new DBUtil();
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rss = null;
		try {
			conn = util.getConnection("oracle.jdbc.OracleDriver","jdbc:oracle:thin:@localhost:1521:XE","ums2","amway11!");
			//conn = util.getConnection("com.mysql.cj.jdbc.Driver","jdbc:mysql://127.0.0.1:3306/UMS?useUnicode=true&characterEncoding=utf8","tester","tester");
			
			pstm = conn.prepareStatement("SELECT * FROM NEO_CD WHERE 1 = 2");
			
			rss = pstm.executeQuery();
			ResultSetMetaData metaData = rss.getMetaData();
			int colcnt = metaData.getColumnCount();
			for(int cnt = 1; cnt <= colcnt; cnt++) {
				String COL_NAME = metaData.getColumnName(cnt).toUpperCase();	
				String COL_TYPE = metaData.getColumnTypeName(cnt);	
				String COL_RAW_TYPE = Integer.toString(metaData.getColumnType(cnt));	
				String COL_SIZE = Integer.toString(metaData.getColumnDisplaySize(cnt));
				
				System.out.println(COL_NAME + ", " + COL_TYPE + ", " + COL_RAW_TYPE + ", " + COL_SIZE);
			}
			
		} catch(Exception e) {
			System.out.println("error = " + e);
		} finally {
			if(rss != null) try { rss.close(); } catch(Exception e) {}
			if(pstm != null) try { pstm.close(); } catch(Exception e) {}
			if(conn != null) try { conn.close(); } catch(Exception e) {}
		}
	}
	*/
}
