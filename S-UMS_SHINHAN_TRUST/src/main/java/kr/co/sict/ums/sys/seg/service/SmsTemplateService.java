/**
 * 작성자 : 이혜민
 * 작성일시 : 2022.06.22
 * 설명 : SMS 템플릿 관리 서비스 인터페이스
 */
package kr.co.sict.ums.sys.seg.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.sict.ums.sys.seg.vo.SmsTemplateVO;

@Service
public interface SmsTemplateService {
	
	/**
	 * SMS 템플릿 목록 조회
	 * @param smsTemplateVO
	 * @return
	 * @throws Exception
	 */
	public List<SmsTemplateVO> getSmsTemplateList(SmsTemplateVO smsTemplateVO) throws Exception;
	
	/**
	 * SMS 템플릿 목록 간단 조회
	 * @param smsTemplateVO
	 * @return
	 * @throws Exception
	 */
	public List<SmsTemplateVO> getSmsTemplateSimpleList() throws Exception;
	/**
	 * SMS 템플릿 목록 정보 조회
	 * @param tempCd
	 * @return
	 * @throws Exception
	 */
	public SmsTemplateVO getSmsTemplateInfo(String tempCd) throws Exception;
	
	/**
	 * SMS 템플릿 목록 정보 등록
	 * @param smsTemplateVO
	 * @return
	 * @throws Exception
	 */
	public int insertSmsTemplateInfo(SmsTemplateVO smsTemplateVO) throws Exception;
	
	/**
	 * SMS 템플릿 코드 중복 조회
	 * @param tempCd
	 * @return
	 * @throws Exception
	 */
	public List<SmsTemplateVO> checkSmsTemplateCode(String tempCd) throws Exception;
	
	/**
	 * SMS 템플릿 목록 정보 수정
	 * @param SmsTemplateVO
	 * @return
	 * @throws Exception
	 */
	public int updateSmsTemplateInfo(SmsTemplateVO SmsTemplateVO) throws Exception;
	
	/**
	 * SMS 템플릿 상태 수정
	 * @param SmsTemplateVO
	 * @return
	 * @throws Exception
	 */
	public int updateSmsTemplateStatus(SmsTemplateVO SmsTemplateVO) throws Exception;
	
	/**
	 *  SMS 템플릿 머지 아이템 조회
	 * @param tempCd
	 * @return
	 * @throws Exception
	 */
	public SmsTemplateVO getSmsTemplate(String tempCd) throws Exception;
	/**
	 * API 템플릿 목록 조회
	 * @param smsTemplateVO
	 * @return
	 * @throws Exception
	 */
	public List<SmsTemplateVO> getApiTemplateList(SmsTemplateVO smsTemplateVO) throws Exception;
}
