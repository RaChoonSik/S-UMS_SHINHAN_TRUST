/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 시스템 서비스 인터페이스==>데이터베이스연결관리 서비스 인터페이스
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : 패키지명수정 및 sys관련 분리에 의한 소스 변경  kr.co.sict.ums.sys.service ==> kr.co.sict.ums.sys.dbc.service
 *                데이터베이스 연결 외의 함수 및 Import 제거 
 */
package kr.co.sict.ums.sys.dbc.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.sict.ums.sys.dbc.vo.DbConnGrpPermVO;
import kr.co.sict.ums.sys.dbc.vo.DbConnPermVO;
import kr.co.sict.ums.sys.dbc.vo.DbConnUserVO;
import kr.co.sict.ums.sys.dbc.vo.DbConnVO;
import kr.co.sict.ums.sys.dbc.vo.MetaColumnVO;
import kr.co.sict.ums.sys.dbc.vo.MetaJoinVO;
import kr.co.sict.ums.sys.dbc.vo.MetaOperatorVO;
import kr.co.sict.ums.sys.dbc.vo.MetaTableVO;
import kr.co.sict.ums.sys.dbc.vo.MetaValueVO;

@Service
public interface DBConnService {
	 
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
	 * DB Connection 정보 삭제
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
	 * DB 사용권한 정보 삭제
	 * @param dbConnPermVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int deleteDbConnPermInfo(int dbConnNo) throws Exception;
	
	/**
	 * DB 사용권한 정보 저장
	 * @param dbConnPermVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int insertDbConnPermInfo(DbConnPermVO dbConnPermVO) throws Exception;	
	
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
	 * DB 사용권한 그룹 정보 삭제
	 * @param dbConnGrpPermVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int deleteDbConnGrpPermInfo(int dbConnNo) throws Exception;
	
	/**
	 * DB 사용권한 그룹 정보 저장
	 * @param dbConnGrpPermVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int insertDbConnGrpPermInfo(DbConnGrpPermVO dbConnGrpPermVO) throws Exception;
	
	/**
	 * 메타 테이블 목록 조회
	 * @param dbConnVO
	 * @return
	 * @throws Exception
	 */
	public List<MetaTableVO> getMetaTableList(DbConnVO dbConnVO) throws Exception;
	
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
	@Transactional(value="transactionManager")
	public int insertMetaTableInfo(MetaTableVO metaTableVO) throws Exception;
	
	/**
	 * 메타 테이블 정보 수정
	 * @param metaTableVO
	 * @return
	 * @throws Exception
	 */
	public int updateMetaTableInfo(MetaTableVO metaTableVO) throws Exception;
	
	/**
	 * 메타 테이블 정보 삭제(관계식 삭제 -> 관계값 삭제 -> 메타컬럼 삭제 -> 메타테이블 삭제)
	 * @param metaTableVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
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
	 * 메타 컬럼 정보를 등록
	 * @param metaColumnVO
	 * @return
	 * @throws Exception
	 */
	public int insertMetaColumnInfo(MetaColumnVO metaColumnVO) throws Exception;
	
	/**
	 * 메타 컬럼 정보를 수정
	 * @param metaColumnVO
	 * @return
	 * @throws Exception
	 */
	public int updateMetaColumnInfo(MetaColumnVO metaColumnVO) throws Exception;
	
	/**
	 * 메타 컬럼 정보 삭제(관계식 삭제 -> 관계값 삭제 -> 메타컬럼 삭제)
	 * @param colNo
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int deleteMetaColumnInfo(int colNo) throws Exception;
	
	/**
	 * 메타 관계식 목록 조회
	 * @param metaOperatorVO
	 * @return
	 * @throws Exception
	 */
	public List<MetaOperatorVO> getMetaOperatorList(MetaOperatorVO metaOperatorVO) throws Exception;
	
	/**
	 * 메타 관계식 수정(삭제 후 등록)
	 * @param metaOperatorVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int updateMetaOperatorInfo(MetaOperatorVO metaOperatorVO) throws Exception;
	
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
	@Transactional(value="transactionManager")
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
