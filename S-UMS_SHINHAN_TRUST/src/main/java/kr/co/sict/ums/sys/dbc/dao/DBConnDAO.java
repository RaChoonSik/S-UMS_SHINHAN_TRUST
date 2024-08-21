/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 시스템 데이터 처리==>데이터베이스 연결관리 데이터 처리
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : 패키지명수정 및 sys관련 분리에 의한 소스 변경  kr.co.sict.ums.sys.dao ==> kr.co.sict.ums.sys.dbc.dao
 *                데이테베이스 연결관리 외의 함수제거
 */
package kr.co.sict.ums.sys.dbc.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.sys.dbc.vo.DbConnGrpPermVO;
import kr.co.sict.ums.sys.dbc.vo.DbConnPermVO;
import kr.co.sict.ums.sys.dbc.vo.DbConnUserVO;
import kr.co.sict.ums.sys.dbc.vo.DbConnVO;
import kr.co.sict.ums.sys.dbc.vo.MetaColumnVO;
import kr.co.sict.ums.sys.dbc.vo.MetaJoinVO;
import kr.co.sict.ums.sys.dbc.vo.MetaOperatorVO;
import kr.co.sict.ums.sys.dbc.vo.MetaTableVO;
import kr.co.sict.ums.sys.dbc.vo.MetaValueVO;

@Repository
public class DBConnDAO implements DBConnMapper {
	@Autowired
	private SqlSession sqlSessionEms;

	@Override
	public List<DbConnVO> getDbConnList(DbConnVO dbConnVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).getDbConnList(dbConnVO);
	}

	@Override
	public int insertDbConnInfo(DbConnVO dbConnVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).insertDbConnInfo(dbConnVO);
	}

	@Override
	public DbConnVO getDbConnInfo(DbConnVO dbConnVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).getDbConnInfo(dbConnVO);
	}

	@Override
	public List<DbConnUserVO> getDbConnUserList(DbConnUserVO dbConnUserVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).getDbConnUserList(dbConnUserVO);
	}	

	@Override
	public int updateDbConnInfo(DbConnVO dbConnVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).updateDbConnInfo(dbConnVO);
	}
	
	@Override
	public int deleteDbConnInfo(DbConnVO dbConnVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).deleteDbConnInfo(dbConnVO);
	}	
	
	@Override
	public List<DbConnPermVO> getDbConnPermList(DbConnPermVO dbConnPermVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).getDbConnPermList(dbConnPermVO);
	}
	
	@Override
	public int insertDbConnPermInfo(DbConnPermVO dbConnPermVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).insertDbConnPermInfo(dbConnPermVO);
	}

	@Override
	public int deleteDbConnPermInfo(int dbConnNo) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).deleteDbConnPermInfo(dbConnNo);
	}
	
	@Override
	public List<DbConnUserVO> getDbConnDeptList(DbConnUserVO dbConnUserVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).getDbConnDeptList(dbConnUserVO);
	}
	
	@Override
	public List<DbConnGrpPermVO> getDbConnGrpPermList(DbConnGrpPermVO dbConnGrpPermVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).getDbConnGrpPermList(dbConnGrpPermVO);
	}
	
	@Override
	public int insertDbConnGrpPermInfo(DbConnGrpPermVO dbConnGrpPermVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).insertDbConnGrpPermInfo(dbConnGrpPermVO);
	}

	@Override
	public int deleteDbConnGrpPermInfo(int dbConnNo) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).deleteDbConnGrpPermInfo(dbConnNo);
	}

	@Override
	public int getMetaTableSeq() throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).getMetaTableSeq();
	}
	
	@Override
	public List<MetaTableVO> getMetaTableList(DbConnVO dbConnVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).getMetaTableList(dbConnVO);
	}
	
	@Override
	public MetaTableVO getMetaTableInfo(MetaTableVO metaTableVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).getMetaTableInfo(metaTableVO);
	}
	
	@Override
	public int insertMetaTableInfo(MetaTableVO metaTableVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).insertMetaTableInfo(metaTableVO);
	}

	@Override
	public int updateMetaTableInfo(MetaTableVO metaTableVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).updateMetaTableInfo(metaTableVO);
	}
	
	@Override
	public int deleteMetaTableOperator(MetaTableVO metaTableVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).deleteMetaTableOperator(metaTableVO);
	}

	@Override
	public int deleteMetaTableValue(MetaTableVO metaTableVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).deleteMetaTableValue(metaTableVO);
	}

	@Override
	public int deleteMetaTableColumn(MetaTableVO metaTableVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).deleteMetaTableColumn(metaTableVO);
	}

	@Override
	public int deleteMetaTableInfo(MetaTableVO metaTableVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).deleteMetaTableInfo(metaTableVO);
	}

	@Override
	public List<MetaColumnVO> getMetaColumnList(MetaColumnVO metaColumnVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).getMetaColumnList(metaColumnVO);
	}
	
	@Override
	public MetaColumnVO getMetaColumnInfo(MetaColumnVO metaColumnVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).getMetaColumnInfo(metaColumnVO);
	}	
	
	@Override
	public int getMetaColumnNo(MetaColumnVO metaColumnVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).getMetaColumnNo(metaColumnVO);
	}

	@Override
	public int insertMetaColumnInfo(MetaColumnVO metaColumnVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).insertMetaColumnInfo(metaColumnVO);
	}
	
	@Override
	public int updateMetaColumnInfo(MetaColumnVO metaColumnVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).updateMetaColumnInfo(metaColumnVO);
	}

	@Override
	public int deleteMetaColumnOperator(int colNo) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).deleteMetaColumnOperator(colNo);
	}

	@Override
	public int deleteMetaColumnValue(int colNo) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).deleteMetaColumnValue(colNo);
	}

	@Override
	public int deleteMetaColumnInfo(int colNo) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).deleteMetaColumnInfo(colNo);
	}
	
	@Override
	public List<MetaOperatorVO> getMetaOperatorList(MetaOperatorVO metaOperatorVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).getMetaOperatorList(metaOperatorVO);
	}

	@Override
	public int insertMetaOperatorInfo(MetaOperatorVO metaOperatorVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).insertMetaOperatorInfo(metaOperatorVO);
	}
	
	@Override
	public List<MetaValueVO> getMetaValueList(MetaValueVO metaValueVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).getMetaValueList(metaValueVO);
	}
	
	@Override
	public int insertMetaValueInfo(MetaValueVO metaValueVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).insertMetaValueInfo(metaValueVO);
	}
	
	@Override
	public int updateMetaValueInfo(MetaValueVO metaValueVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).updateMetaValueInfo(metaValueVO);
	}
	
	@Override
	public int deleteMetaValueInfo(MetaValueVO metaValueVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).deleteMetaValueInfo(metaValueVO);
	}
	
	@Override
	public List<MetaJoinVO> getMetaJoinList(MetaJoinVO metaJoinVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).getMetaJoinList(metaJoinVO);
	}
	
	@Override
	public int insertMetaJoinInfo(MetaJoinVO metaJoinVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).insertMetaJoinInfo(metaJoinVO);
	}
	
	@Override
	public int updateMetaJoinInfo(MetaJoinVO metaJoinVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).updateMetaJoinInfo(metaJoinVO);
	}

	@Override
	public int deleteMetaJoinInfo(MetaJoinVO metaJoinVO) throws Exception {
		return sqlSessionEms.getMapper(DBConnMapper.class).deleteMetaJoinInfo(metaJoinVO);
	}
}
