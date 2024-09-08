/**
 * 작성자 : 김준희
 * 작성일시 : 2021.07.17
 * 설명 : 발송대상(세그먼트)관리 데이터 처리
 */
package kr.co.sict.ums.ems.seg.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.ems.seg.vo.SegmentVO;
import kr.co.sict.ums.sys.dbc.vo.DbConnVO;

@Repository
public class SegmentDAO implements SegmentMapper {
	@Autowired
	private SqlSession sqlSessionEms;

	@Override
	public List<SegmentVO> getSegmentList(SegmentVO segmentVO) throws Exception {
		return sqlSessionEms.getMapper(SegmentMapper.class).getSegmentList(segmentVO);
	}
	
	@Override
	public int insertSegmentInfo(SegmentVO segmentVO) throws Exception {
		return sqlSessionEms.getMapper(SegmentMapper.class).insertSegmentInfo(segmentVO);
	}
	
	@Override
	public int insertRetrySegmentInfo(SegmentVO segmentVO) throws Exception {
		return sqlSessionEms.getMapper(SegmentMapper.class).insertRetrySegmentInfo(segmentVO);
	}
	
	@Override
	public int insertRealSegmentInfo(SegmentVO segmentVO) throws Exception {
		return sqlSessionEms.getMapper(SegmentMapper.class).insertRealSegmentInfo(segmentVO);
	}
	
	@Override
	public int updateSegmentInfo(SegmentVO segmentvO) throws Exception {
		return sqlSessionEms.getMapper(SegmentMapper.class).updateSegmentInfo(segmentvO);
	}
	
	
	@Override
	public int deleteRetrySegmentInfo(SegmentVO segmentvO) throws Exception {
		return sqlSessionEms.getMapper(SegmentMapper.class).deleteRetrySegmentInfo(segmentvO);
	}
		
	@Override
	public int deleteRealSegmentInfo(SegmentVO segmentvO) throws Exception {
		return sqlSessionEms.getMapper(SegmentMapper.class).deleteRealSegmentInfo(segmentvO);
	}
	
	@Override
	public int getSegmentNo() throws Exception {
		return sqlSessionEms.getMapper(SegmentMapper.class).getSegmentNo();
	}
	
	@Override
	public int getRetrySegmentInfoCount(SegmentVO segmentvO) throws Exception {
		return sqlSessionEms.getMapper(SegmentMapper.class).getRetrySegmentInfoCount(segmentvO);
	}
	
	@Override
	public int getRealSegmentInfoCount(SegmentVO segmentvO) throws Exception {
		return sqlSessionEms.getMapper(SegmentMapper.class).getRealSegmentInfoCount(segmentvO);
	}
	
	@Override
	public List<DbConnVO> getDbConnList(DbConnVO dbConnVO) throws Exception {
		return sqlSessionEms.getMapper(SegmentMapper.class).getDbConnList(dbConnVO);
	}

	@Override
	public int updateSegmentStatus(SegmentVO segmentVO) throws Exception {
		return sqlSessionEms.getMapper(SegmentMapper.class).updateSegmentStatus(segmentVO);
	}

	@Override
	public SegmentVO getSegmentInfo(SegmentVO segmentVO) throws Exception {
		return sqlSessionEms.getMapper(SegmentMapper.class).getSegmentInfo(segmentVO);
	}

	@Override
	public int insertSegmentRealInfo(SegmentVO segmentVO) throws Exception {
		return sqlSessionEms.getMapper(SegmentMapper.class).insertSegmentRealInfo(segmentVO);
	}
	
	@Override
	public int insertSegmentRetryInfo(SegmentVO segmentVO) throws Exception {
		return sqlSessionEms.getMapper(SegmentMapper.class).insertSegmentRetryInfo(segmentVO);
	}
	
	@Override
	public int updateSegmentRealInfo(SegmentVO segmentVO) throws Exception {
		return sqlSessionEms.getMapper(SegmentMapper.class).updateSegmentRealInfo(segmentVO);
	}
	
	@Override
	public int updateSegmentRetryInfo(SegmentVO segmentVO) throws Exception {
		return sqlSessionEms.getMapper(SegmentMapper.class).updateSegmentRetryInfo(segmentVO);
	}	
		
	@Override
	public int getSegmentRealCount(SegmentVO segmentVO) throws Exception {
		return sqlSessionEms.getMapper(SegmentMapper.class).getSegmentRealCount(segmentVO);
	}
	
	@Override
	public int getSegmentRetryCount(SegmentVO segmentVO) throws Exception {
		return sqlSessionEms.getMapper(SegmentMapper.class).getSegmentRetryCount(segmentVO);
	}

}
