/**
 * 작성자 : 김준희
 * 작성일시 : 2021.07.17
 * 설명 : 발송대상(세그먼트)관리 서비스 구현
 */
package kr.co.sict.ums.ems.seg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.ems.seg.dao.SegmentDAO;
import kr.co.sict.ums.ems.seg.vo.SegmentVO;
import kr.co.sict.ums.sys.dbc.vo.DbConnVO;

@Service
public class SegmentServiceImpl implements SegmentService {
	@Autowired
	private SegmentDAO segmentDAO;

	@Override
	public List<SegmentVO> getSegmentList(SegmentVO segmentVO) throws Exception {
		return segmentDAO.getSegmentList(segmentVO);
	}

	@Override
	public int insertSegmentInfo(SegmentVO segmentVO) throws Exception {
		int result = 0;
		
		/* mssql
		 * // 기본 쿼리 등록 result = segmentDAO.insertSegmentInfo(segmentVO);
		 * 
		 * if( result > 0 ) { int segNo = segmentDAO.getSegmentNo(); if(segNo != 0 ) {
		 * segmentVO.setSegNo(segNo);
		 * 
		 * if(segmentVO.getRetryQuery() != null &&
		 * !"".equals(segmentVO.getRetryQuery())) { result +=
		 * segmentDAO.insertRetrySegmentInfo(segmentVO); }
		 * 
		 * if(segmentVO.getRealQuery() != null && !"".equals(segmentVO.getRealQuery()))
		 * { result += segmentDAO.insertRealSegmentInfo(segmentVO); } } }
		 */
		
		// 기본 쿼리 등록 
		result = segmentDAO.insertSegmentInfo(segmentVO);
		if(segmentVO.getSegNo() != 0 ) {
			
			if( result > 0 ) {
				if(segmentVO.getRetryQuery() != null &&  !"".equals(segmentVO.getRetryQuery())) {
					result += segmentDAO.insertRetrySegmentInfo(segmentVO);
				}
				
				if(segmentVO.getRealQuery() != null &&  !"".equals(segmentVO.getRealQuery())) {
					result += segmentDAO.insertRealSegmentInfo(segmentVO);
				}
			} 
		} 
		return result;
	}
	
	@Override
	public int updateSegmentInfo(SegmentVO segmentvO) throws Exception {
		
		int result = 0;
		
		// 기본 쿼리 등록 
		result = segmentDAO.updateSegmentInfo(segmentvO);
		
		if( result > 0 ) {
			if ( segmentDAO.getRetrySegmentInfoCount(segmentvO)  > 0 ) {
				result += segmentDAO.deleteRetrySegmentInfo(segmentvO);
			}
			if(segmentvO.getRetryQuery() != null &&  !"".equals(segmentvO.getRetryQuery())) {
				result += segmentDAO.insertRetrySegmentInfo(segmentvO);
			}
			
			if ( segmentDAO.getRealSegmentInfoCount(segmentvO)  > 0 ) {
				result += segmentDAO.deleteRealSegmentInfo(segmentvO);
			}
			if(segmentvO.getRealQuery() != null &&  !"".equals(segmentvO.getRealQuery())) {
				result += segmentDAO.insertRealSegmentInfo(segmentvO);
			}
		}
		return result;
	}

	@Override
	public List<DbConnVO> getDbConnList(DbConnVO dbConnVO) throws Exception {
		return segmentDAO.getDbConnList(dbConnVO);
	}

	@Override
	public int updateSegmentStatus(SegmentVO segmentVO) throws Exception {
		int result = 0;
		if(segmentVO.getSegNos() != null && !"".equals(segmentVO.getSegNos())) {
			String[] segNo = segmentVO.getSegNos().split(",");
			for(int i=0;i<segNo.length;i++) {
				SegmentVO segment = new SegmentVO();
				
				segment.setSegNo(Integer.parseInt(segNo[i]));
				segment.setStatus(segmentVO.getStatus());
				segment.setUpId(segmentVO.getUpId());
				segment.setUpDt(segmentVO.getUpDt());
				
				result += segmentDAO.updateSegmentStatus(segment);
			}
		} else {
			SegmentVO segment = new SegmentVO();
			
			segment.setSegNo(segmentVO.getSegNo());
			segment.setStatus(segmentVO.getStatus());
			segment.setUpId(segmentVO.getUpId());
			segment.setUpDt(segmentVO.getUpDt());
			
			result += segmentDAO.updateSegmentStatus(segment);
		}
		return result;
	}

	@Override
	public SegmentVO getSegmentInfo(SegmentVO segmentVO) throws Exception {
		return segmentDAO.getSegmentInfo(segmentVO);
	}

}
