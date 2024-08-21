 /* 작성자 : 김준희
 * 작성일시 : 2022.04.01
 * 설명 : 푸시통계분석 매퍼
 */
package kr.co.sict.ums.push.ana.dao;

import java.util.List;

import kr.co.sict.ums.push.ana.vo.PushSendLogVO;

public interface PushAnalysisMapper {
	
	/**
	 * 푸시 통계 분석 
	 * @param PushVO
	 * @return
	 */
	public List<PushSendLogVO> getPushList(PushSendLogVO pushSendLogVO) throws Exception;
	
	/**
	 * 푸시상세정보 
	 * @param PushVO
	 * @return
	 */
	public PushSendLogVO getPushInfo(PushSendLogVO pushSendLogVO) throws Exception;
	
	/**
	 * 푸시 통계 분석 
	 * @param PushVO
	 * @return
	 */
	public List<PushSendLogVO> getCampPushList(PushSendLogVO pushSendLogVO) throws Exception;
	
    /**
     * 푸시 통계 분석 
     * @param PushVO
     * @return
     */
    public List<PushSendLogVO> getPushCampList(PushSendLogVO pushSendLogVO) throws Exception;
    
}