 /* 작성자 : 김준희
 * 작성일시 : 2022.04.01
 * 설명 : PUSH통계분석 서비스 인터페이스
 */
package kr.co.sict.ums.push.ana.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.sict.ums.push.ana.vo.PushSendLogVO;

@Service
public interface PushAnalysisService {
	
	/**
	 * 푸시통계분석
	 * @param PushVO
	 * @return
	 */
	public List<PushSendLogVO> getPushList(PushSendLogVO pushSendLogVO) throws Exception;
	
	/**
	 * 푸시통계상세정보
	 * @param PushVO
	 * @return
	 */
	public PushSendLogVO getPushInfo(PushSendLogVO pushSendLogVO) throws Exception;
	
	/**
	 * 캠페인별 푸시통계분석
	 * @param PushVO
	 * @return
	 */
	public List<PushSendLogVO> getCampPushList(PushSendLogVO pushSendLogVO) throws Exception;
	
    /**
     * 푸시통계분석
     * @param PushVO
     * @return
     */
    public List<PushSendLogVO> getPushCampList(PushSendLogVO pushSendLogVO) throws Exception;
    
}
