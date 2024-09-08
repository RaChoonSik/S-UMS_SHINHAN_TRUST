/**
 * 작성자 : 김준희
 * 작성일시 : 2021.10.13
 * 설명 : 캠페인관리 서비스 인터페이스
 */
package kr.co.sict.ums.sms.cam.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.sict.ums.sms.ana.vo.SmsSendLogVO;
import kr.co.sict.ums.sms.cam.vo.SmsAttachVO;
import kr.co.sict.ums.sms.cam.vo.SmsCampaignVO;
import kr.co.sict.ums.sms.cam.vo.SmsPhoneVO;
import kr.co.sict.ums.sms.cam.vo.SmsStgVO;
import kr.co.sict.ums.sms.cam.vo.SmsVO;
import kr.co.sict.util.PropertiesUtil;

@Service
public interface SmsCampaignService {
 
    /**
     * 캠페인 목록(문자)조회
     * @param campaignVO
     * @return
     * @throws Exception
     */
    public List<SmsCampaignVO> getCampaignList(SmsCampaignVO smsCampaignVO) throws Exception; 
    
    
    /**
     * 캠페인 정보 조회
     * @param smsCampaignVO
     * @return
     * @throws Exception
     */
    public SmsCampaignVO getCampaignInfo(SmsCampaignVO smsCampaignVO) throws Exception;
    
    
    /**
     * 캠페인 정보 등록
     * @param smsCampaignVO
     * @return
     * @throws Exception
     */
    public int insertCampaignInfo(SmsCampaignVO smsCampaignVO) throws Exception;
    
    
    /**
     * 캠페인 정보 수정
     * @param smsCampaignVO
     * @return
     * @throws Exception
     */
    public int updateCampaignInfo(SmsCampaignVO smsCampaignVO) throws Exception;
    
	/**
	 * SMS 목록 조회
	 * @param smsVO
	 * @return
	 * @throws Exception
	 */
	public List<SmsVO> getSmsList(SmsVO smsVO) throws Exception;
	
	/**
	 * SMS 정보를 등록
	 * @param smsVO
	 * @param attachList
	 * @param linkList
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int insertSmsInfo(SmsVO smsVO, List<SmsAttachVO> smsAttachVO) throws Exception;
	
	/**
	 * SMS 문자상태 상태변경(정상, 삭제)
	 * @param smsVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int updateSmsStatus(SmsVO smsVO) throws Exception;
	
	/**
	 * SMS 상태 상태변경(발송대기, 발송승인, 발송요청, 발송완료)
	 * @param smsVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int updateSmsStatusAdmit(SmsVO smsVO) throws Exception;

	/**
	 * SMS 복사
	 * @param smsVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int copySmsInfo(SmsVO smsVO, PropertiesUtil properties, String serverUrl) throws Exception;
	
	/**
	 * 카카오알림톡 복사
	 * @param smsVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int copyKakaoInfo(SmsVO smsVO) throws Exception;
	
	/**
	 * SMS 정보 조회
	 * @param smsVO
	 * @return
	 * @throws Exception
	 */
	public SmsVO getSmsInfo(SmsVO smsVO) throws Exception;
  
	/**
	 * SMS 정보를 수정한다
	 * @param smsVO  
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int updateSmsInfo(SmsVO smsVO, List<SmsAttachVO> smsAttachVO) throws Exception;

	
	/**
	 * SMS 발송 대상 핸드폰 번호 조회
	 * @param smsVO
	 * @return 
	 * @throws Exception
	 */
	public List<SmsPhoneVO> getSmsPhoneList(SmsVO smsVO) throws Exception;	 
	
	/**
	 *  SMS 발송 대상 핸드폰 번호 등록 
	 * @param smsPhoneVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int insertSmsPhone(SmsPhoneVO smsPhoneVO) throws Exception;
		
	/**
	 *  SMS 발송 대상 핸드폰 번호 복사
	 * @param smsVO
	 * @return
	 * @throws Exception
	 */	
	@Transactional(value="transactionManager")
	public int copySmsPhone(SmsVO smsVO) throws Exception;
	
	/**
	 *  SMS 발송 대상 핸드폰 번호 삭제
	 * @param smsVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int deleteSmsPhone(SmsVO smsVO) throws Exception;
	
	/**
	 * 첨부파일 목록 조회
	 * @param taskNo
	 * @return
	 * @throws Exception
	 */
	public List<SmsAttachVO> getSmsAttachList(SmsVO smsVO) throws Exception;

	/**
	 * 첨부파일 등록 
	 * @param smsVO
	 * @param attachList
	 * @param linkList
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int insertSmsAttachInfo(SmsAttachVO smsAttachVO) throws Exception;
	
	/**
	 * 첨부파일 등록 삭제
	 * @param smsVO
	 * @param attachList
	 * @param linkList
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int deleteSmsAttachInfo(SmsVO smsVO) throws Exception;
	
	/**
	 *  카카오알림톡 템플릿 머지 정보 조회
	 * @param smsVO
	 * @return 
	 * @throws Exception
	 */
	public List<SmsVO> getKakaoTemplateMergeList(SmsVO smsVO) throws Exception;	 
	
	/**
	 *  카카오알림톡 템플릿 머지 정보 등록 
	 * @param smsVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int insertKakaoTemplateMerge(SmsVO smsVO) throws Exception;
		
	/**
	 *  카카오알림톡 템플릿 머지 정보 복사
	 * @param smsVO
	 * @return
	 * @throws Exception
	 */	
	@Transactional(value="transactionManager")
	public int copyKakaoTemplateMerge(SmsVO smsVO) throws Exception;
	
	/**
	 *  카카오알림톡 템플릿 머지 정보 삭제
	 * @param smsVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int deleteKakaoTemplateMerge(SmsVO smsVO) throws Exception;
	
	/**
	 * SMS STG 정보를 등록
	 * @param smsVO
	 * @param attachList
	 * @param linkList
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int insertSmsStg(SmsStgVO smsStgVO) throws Exception;
	
	/**
	 * SMS STG 목록 조회
	 * @param smsVO
	 * @return
	 * @throws Exception
	 */
	public List<SmsStgVO> getSmsStgList(SmsStgVO smsStgVO) throws Exception;

	/**
	 * 키값 기준 갯수 
	 * @param requestKey
	 * @return
	 * @throws Exception
	 */
	public int getCountRequestKey(String requestKey) throws Exception;

}

