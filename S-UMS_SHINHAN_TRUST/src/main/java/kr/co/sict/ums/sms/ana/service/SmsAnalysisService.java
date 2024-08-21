 /* 작성자 : 김준희
 * 작성일시 : 2022.03.31
 * 설명 : SMS통계분석 서비스 인터페이스
 */
package kr.co.sict.ums.sms.ana.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.sict.ums.sms.ana.vo.SmsSendLogVO;

@Service
public interface SmsAnalysisService {
	
	/**
	 * 카카오 통계 분석 리스트
	 * @param smsSendLogVO
	 * @return
	 */
	public List<SmsSendLogVO> getSmsList(SmsSendLogVO smsSendLogVO) throws Exception;
	
	/**
	 * 카카오 통계 분석
	 * @param smsSendLogVO
	 * @return
	 */
	public SmsSendLogVO getSmsInfo(SmsSendLogVO smsSendLogVO) throws Exception;
	
	/**
	 * 발송건수 성공 실패 팝업
	 * @param smsSendLogVO
	 * @return
	 */
	public List<SmsSendLogVO> getPopSmsSendList(SmsSendLogVO smsSendLogVO) throws Exception;

	/**
	 * 상세로그 리스트
	 * @param smsSendLogVO
	 * @return
	 */
	public List<SmsSendLogVO> getSmsSendLogList(SmsSendLogVO smsSendLogVO) throws Exception;
	/**
	 * SmsMessage
	 * @param smsSendLogVO
	 * @return
	 */
	public String getSmsMessage(SmsSendLogVO smsSendLogVO) throws Exception;
	/**
	 * 캠페인별 sms 분석
	 * @param smsSendLogVO
	 * @return
	 */
	public List<SmsSendLogVO> getCampSmsList(SmsSendLogVO smsSendLogVO) throws Exception;
	
    /**
     * 캠페인별 문자분석 리스트
     * @param smsSendLogVO
     * @return
     */
    public List<SmsSendLogVO> getSmsCampList(SmsSendLogVO smsSendLogVO) throws Exception;
    
}
