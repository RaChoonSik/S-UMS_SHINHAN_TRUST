/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 시스템 매퍼==>데이터베이스 연결 매퍼
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : 패키지명수정 및 sys관련 분리에 의한 소스 변경  kr.co.sict.ums.sys.dao ==> kr.co.sict.ums.sys.dbc.dao
 *                계정관리외의 항목제거 
 */
package kr.co.sict.ums.sys.dbc.dao;

import java.util.List;

import kr.co.sict.ums.sys.dbc.vo.DbConnGrpPermVO;
import kr.co.sict.ums.sys.dbc.vo.DbConnPermVO;
import kr.co.sict.ums.sys.dbc.vo.DbConnUserVO;
import kr.co.sict.ums.sys.dbc.vo.DbConnVO;
import kr.co.sict.ums.sys.dbc.vo.MetaColumnVO;
import kr.co.sict.ums.sys.dbc.vo.MetaJoinVO;
import kr.co.sict.ums.sys.dbc.vo.MetaOperatorVO;
import kr.co.sict.ums.sys.dbc.vo.MetaTableVO;
import kr.co.sict.ums.sys.dbc.vo.MetaValueVO;

public interface DBConnMapper {
 
	/**
	 * DB Connection 목록 조회
	 * @param dbConnVO
	 * @return
	 * @throws Exception
	 */
	public List<DbConnVO> getDbConnList(DbConnVO dbConnVO) throws Exception;
	
	/**
	 * DB Connection 정보 조회
	 * @param dbConnVO
	 * @return
	 * @throws Exception
	 */
	public DbConnVO getDbConnInfo(DbConnVO dbConnVO) throws Exception;
	
	/**
	 * DB Connection 사용자 정보 조회
	 * @param dbConnVO
	 * @return
	 * @throws Exception
	 */
	public List<DbConnUserVO> getDbConnUserList(DbConnUserVO dbConnUserVO) throws Exception;
	
	/**
	 * DB Connection 정보 등록
	 * @param dbConnVO
	 * @return
	 * @throws Exception
	 */
	public int insertDbConnInfo(DbConnVO dbConnVO) throws Exception;
	
	/**
	 * DB Connection 정보 수정
	 * @param dbConnVO
	 * @return
	 * @throws Exception
	 */
	public int updateDbConnInfo(DbConnVO dbConnVO) throws Exception;
	
	
	/**
	 * DB Connection 정보 수정
	 * @param dbConnVO
	 * @return
	 * @throws Exception
	 */
	public int deleteDbConnInfo(DbConnVO dbConnVO) throws Exception;
	
	/**
	 * DB 사용권한 목록 조회
	 * @param dbConnPermVO
	 * @return
	 * @throws Exception
	 */
	public List<DbConnPermVO> getDbConnPermList(DbConnPermVO dbConnPermVO) throws Exception;
	
	/**
	 * DB 사용권한 정보 등록
	 * @param dbConnPermVO
	 * @return
	 * @throws Exception
	 */
	public int insertDbConnPermInfo(DbConnPermVO dbConnPermVO) throws Exception;
	
	/**
	 * DB 사용권한 정보 삭제
	 * @param dbConnPermVO
	 * @return
	 * @throws Exception
	 */
	public int deleteDbConnPermInfo(int dbConnNo) throws Exception;
	
	/**
	 * DB Connection 그룹 정보 조회
	 * @param dbConnVO
	 * @return
	 * @throws Exception
	 */
	public List<DbConnUserVO> getDbConnDeptList(DbConnUserVO dbConnUserVO) throws Exception;
	
	/**
	 * DB 사용권한 그룹 목록 조회
	 * @param dbConnGrpPermVO
	 * @return
	 * @throws Exception
	 */
	public List<DbConnGrpPermVO> getDbConnGrpPermList(DbConnGrpPermVO dbConnGrpPermVO) throws Exception;
	
	/**
	 * DB 사용권한 그룹 정보 등록
	 * @param dbConnPermVO
	 * @return
	 * @throws Exception
	 */
	public int insertDbConnGrpPermInfo(DbConnGrpPermVO dbConnGrpPermVO) throws Exception;
	
	/**
	 * DB 사용권한 그룹 정보 삭제
	 * @param dbConnPermVO
	 * @return
	 * @throws Exception
	 */
	public int deleteDbConnGrpPermInfo(int dbConnNo) throws Exception;	
		
	/**
	 * 메타 테이블 목록 조회
	 * @param dbConnVO
	 * @return
	 * @throws Exception
	 */
	public List<MetaTableVO> getMetaTableList(DbConnVO dbConnVO) throws Exception;
	
	/**
	 * 메타 테이블 일련번호 조회
	 * @return
	 * @throws Exception
	 */
	public int getMetaTableSeq() throws Exception;
	
	/**
	 * 메타 테이블 정보 조회
	 * @param metaTableVO
	 * @return
	 * @throws Exception
	 */
	public MetaTableVO getMetaTableInfo(MetaTableVO metaTableVO) throws Exception;
	
	/**
	 * 메타 테이블 정보 등록
	 * @param metaTableVO
	 * @return
	 * @throws Exception
	 */
	public int insertMetaTableInfo(MetaTableVO metaTableVO) throws Exception;
	
	/**
	 * 메타 테이블 정보 수정
	 * @param metaTableVO
	 * @return
	 * @throws Exception
	 */
	public int updateMetaTableInfo(MetaTableVO metaTableVO) throws Exception;
	
	/**
	 * 메타 테이블 관계식 삭제
	 * @param metaTableVO
	 * @return
	 * @throws Exception
	 */
	public int deleteMetaTableOperator(MetaTableVO metaTableVO) throws Exception;
	
	/**
	 * 메타 테이블 관계값 삭제
	 * @param metaTableVO
	 * @return
	 * @throws Exception
	 */
	public int deleteMetaTableValue(MetaTableVO metaTableVO) throws Exception;
	
	/**
	 * 메타 테이블 컬럼 삭제
	 * @param metaTableVO
	 * @return
	 * @throws Exception
	 */
	public int deleteMetaTableColumn(MetaTableVO metaTableVO) throws Exception;
	
	/**
	 * 메타 테이블 정보 삭제
	 * @param metaTableVO
	 * @return
	 * @throws Exception
	 */
	public int deleteMetaTableInfo(MetaTableVO metaTableVO) throws Exception;
	
	/**
	 * 메타 컬럼 정보 조회
	 * @param metaColumnVO
	 * @return
	 * @throws Exception
	 */
	public List<MetaColumnVO> getMetaColumnList(MetaColumnVO metaColumnVO) throws Exception;
	
	/**
	 * 메타 컬럼 정보 조회
	 * @param metaColumnVO
	 * @return
	 * @throws Exception
	 */
	public MetaColumnVO getMetaColumnInfo(MetaColumnVO metaColumnVO) throws Exception;	
	
	/**
	 * 메타 컬럼 번호 조회
	 * @param metaColumnVO
	 * @return
	 * @throws Exception
	 */
	public int getMetaColumnNo(MetaColumnVO metaColumnVO) throws Exception;
	
	/**
	 * 메타 컬럼 정보 등록
	 * @param metaColumnVO
	 * @return
	 * @throws Exception
	 */
	public int insertMetaColumnInfo(MetaColumnVO metaColumnVO) throws Exception;
	
	/**
	 * 메타 컬럼 정보 수정
	 * @param metaColumnVO
	 * @return
	 * @throws Exception
	 */
	public int updateMetaColumnInfo(MetaColumnVO metaColumnVO) throws Exception;
	
	/**
	 * 메타 컬럼 관계식 삭제
	 * @param colNo
	 * @return
	 * @throws Exception
	 */
	public int deleteMetaColumnOperator(int colNo) throws Exception;
	
	/**
	 * 메타 컬럼 관계값 삭제
	 * @param colNo
	 * @return
	 * @throws Exception
	 */
	public int deleteMetaColumnValue(int colNo) throws Exception;
	
	/**
	 * 메타 컬럼 정보 삭제
	 * @param colNo
	 * @return
	 * @throws Exception
	 */
	public int deleteMetaColumnInfo(int colNo) throws Exception;
	
	/**
	 * 메타 관계식 목록 조회
	 * @param metaOperatorVO
	 * @return
	 * @throws Exception
	 */
	public List<MetaOperatorVO> getMetaOperatorList(MetaOperatorVO metaOperatorVO) throws Exception;
	
	/**
	 * 메타 관계식 등록
	 * @param metaOperatorVO
	 * @return
	 * @throws Exception
	 */
	public int insertMetaOperatorInfo(MetaOperatorVO metaOperatorVO) throws Exception;
	
	/**
	 * 메타 관계값 목록 조회
	 * @param metaValueVO
	 * @return
	 * @throws Exception
	 */
	public List<MetaValueVO> getMetaValueList(MetaValueVO metaValueVO) throws Exception;
	
	/**
	 * 메타 관계값 등록
	 * @param metaValueVO
	 * @return
	 * @throws Exception
	 */
	public int insertMetaValueInfo(MetaValueVO metaValueVO) throws Exception;
	
	/**
	 * 메타 관계값 수정
	 * @param metaValueVO
	 * @return
	 * @throws Exception
	 */
	public int updateMetaValueInfo(MetaValueVO metaValueVO) throws Exception;
	
	/**
	 * 메타 관계값 삭제
	 * @param metaValueVO
	 * @return
	 * @throws Exception
	 */
	public int deleteMetaValueInfo(MetaValueVO metaValueVO) throws Exception;
	
	/**
	 * 메타 조인 목록 조회
	 * @param metaJoinVO
	 * @return
	 * @throws Exception
	 */
	public List<MetaJoinVO> getMetaJoinList(MetaJoinVO metaJoinVO) throws Exception;
	
	/**
	 * 메타 조인 정보 등록
	 * @param metaJoinVO
	 * @return
	 * @throws Exception
	 */
	public int insertMetaJoinInfo(MetaJoinVO metaJoinVO) throws Exception;
	
	/**
	 * 메타 조인 정보 수정
	 * @param metaJoinVO
	 * @return
	 * @throws Exception
	 */
	public int updateMetaJoinInfo(MetaJoinVO metaJoinVO) throws Exception;
	
	/**
	 * 메타 조인 정보 삭제
	 * @param metaJoinVO
	 * @return
	 * @throws Exception
	 */
	public int deleteMetaJoinInfo(MetaJoinVO metaJoinVO) throws Exception;
}
