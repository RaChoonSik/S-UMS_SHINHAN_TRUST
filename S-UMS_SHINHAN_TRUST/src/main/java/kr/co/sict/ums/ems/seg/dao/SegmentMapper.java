/**
 * 작성자 : 김준희
 * 작성일시 : 2021.07.17
 * 설명 : 발송대상(세그먼트)관리 매퍼
 */
package kr.co.sict.ums.ems.seg.dao;

import java.util.List;

import kr.co.sict.ums.ems.seg.vo.SegmentVO;
import kr.co.sict.ums.sys.dbc.vo.DbConnVO;

public interface SegmentMapper {
	/**
	 * 발송대상(세그먼트) 목록 조회
	 * @param segmentVO
	 * @return
	 * @throws Exception
	 */
	public List<SegmentVO> getSegmentList(SegmentVO segmentVO) throws Exception;
	
	/**
	 * 발송대상(세그먼트) 정보 등록
	 * @param segmentVO
	 * @return
	 * @throws Exception
	 */
	public int insertSegmentInfo(SegmentVO segmentVO) throws Exception;
	
	/**
	 * 재발송 발송대상(세그먼트) 정보 등록
	 * @param segmentVO
	 * @return
	 * @throws Exception
	 */
	public int insertRetrySegmentInfo(SegmentVO segmentVO) throws Exception;
	
	/**
	 * 실시간 발송대상(세그먼트) 정보 등록
	 * @param segmentVO
	 * @return
	 * @throws Exception
	 */
	public int insertRealSegmentInfo(SegmentVO segmentVO) throws Exception;	
	
	/**
	 * 등록한 발송대상(세그먼트)  번호 조회
	 * @return
	 * @throws Exception
	 */
	public int getSegmentNo() throws Exception;
	
	/**
	 * 재발송 쿼리 존재 여부 
	 * @param segmentvO
	 * @return
	 * @throws Exception
	 */
	public int getRetrySegmentInfoCount(SegmentVO segmentvO) throws Exception;
	
	/**
	 * 실시간 쿼리 존재 여부 
	 * @param segmentvO
	 * @return
	 * @throws Exception
	 */
	public int getRealSegmentInfoCount(SegmentVO segmentvO) throws Exception;
	
	
	/**
	 * 재발송 쿼리 존재 여부 
	 * @param segmentvO
	 * @return
	 * @throws Exception
	 */
	public int deleteRetrySegmentInfo(SegmentVO segmentvO) throws Exception;
	
	/**
	 * 실시간 쿼리 존재 여부 
	 * @param segmentvO
	 * @return
	 * @throws Exception
	 */
	public int deleteRealSegmentInfo(SegmentVO segmentvO) throws Exception;	
		
	/**
	 * 발송대상(세그먼트) 정보 수정
	 * @param segmentvO
	 * @return
	 * @throws Exception
	 */
	public int updateSegmentInfo(SegmentVO segmentvO) throws Exception;
	
	/**
	 * 발송대상(세그먼트) 상태 수정(중지,삭제)
	 * @param segmentVO
	 * @return
	 * @throws Exception
	 */
	public int updateSegmentStatus(SegmentVO segmentVO) throws Exception;
	
	/**
	 * 권한 있는 DB연결 목록 조회
	 * @param dbConnVO
	 * @return
	 * @throws Exception
	 */
	public List<DbConnVO> getDbConnList(DbConnVO dbConnVO) throws Exception;
	
	/**
	 * 발송대상(세그먼트) 정보 조회
	 * @param segmentVO
	 * @return
	 * @throws Exception
	 */
	public SegmentVO getSegmentInfo(SegmentVO segmentVO) throws Exception;
	
	/**
	 * 실시간 발송대상(세그먼트) 정보 등록
	 * @param segmentVO
	 * @return
	 * @throws Exception
	 */
	public int insertSegmentRealInfo(SegmentVO segmentVO) throws Exception;
	
	/**
	 * 재발송 발송대상(세그먼트) 정보 등록
	 * @param segmentVO
	 * @return
	 * @throws Exception
	 */
	public int insertSegmentRetryInfo(SegmentVO segmentVO) throws Exception;
	
	/**
	 * 실시간 발송대상(세그먼트) 정보 수정
	 * @param segmentVO
	 * @return
	 * @throws Exception
	 */
	public int updateSegmentRealInfo(SegmentVO segmentVO) throws Exception;
	
	/**
	 * 재발송 발송대상(세그먼트) 정보 수정
	 * @param segmentVO
	 * @return
	 * @throws Exception
	 */
	public int updateSegmentRetryInfo(SegmentVO segmentVO) throws Exception;
	/**
	 * 실시간 발송대상(세그먼트) 존재여부 
	 * @param ifBasicVO
	 * @return
	 * @throws Exception
	 */
	public int getSegmentRealCount(SegmentVO segmentVO) throws Exception;
	
	/**
	 * 재발송 발송대상(세그먼트) 존재여부 
	 * @param ifBasicVO
	 * @return
	 * @throws Exception
	 */
	public int getSegmentRetryCount(SegmentVO segmentVO) throws Exception;

}
