/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 코드 조회 서비스
 */
package kr.co.sict.ums.com.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.sict.ums.com.vo.CodeGroupVO;
import kr.co.sict.ums.com.vo.CodeVO;
import kr.co.sict.ums.ems.cam.vo.CampaignVO;
import kr.co.sict.ums.push.cam.vo.PushCampaignVO;
import kr.co.sict.ums.sms.cam.vo.SmsCampaignVO;

@Service
public interface CodeService {
	/**
	 * 코드 목록 조회
	 * @param codeVO
	 * @return
	 * @throws Exception
	 */
	public List<CodeVO> getCodeList(CodeVO codeVO) throws Exception;
	
	/**
	 * 코드 정보 조회
	 * @param codeVO
	 * @return
	 * @throws Exception
	 */
	public CodeVO getCodeInfo(CodeVO codeVO) throws Exception;
	
	/**
	 * 코드그룹 정보 조회
	 * @param codeGroupVO
	 * @return
	 * @throws Exception
	 */
	public CodeGroupVO getCodeGrpInfo(CodeGroupVO codeGroupVO) throws Exception;
	
	/**
	 * 코드그룹 목록 조회
	 * @param codeGroupVO
	 * @return
	 * @throws Exception
	 */
	public List<CodeVO> getCodeGrpList(CodeVO codeVO) throws Exception;
	
	/**
	 * 상윜 코드 기준 코드 목록 조회
	 * @param codeVO
	 * @return
	 * @throws Exception
	 */
	public List<CodeVO> getCodeListByUpCd(CodeVO codeVO) throws Exception;
	
	/**
	 * 타임존 목록 조회
	 * @param codeVO
	 * @return
	 * @throws Exception
	 */
	public List<CodeVO> getTimezoneList(CodeVO codeVO) throws Exception;
	
	/**
	 * 부서 목록 조회
	 * @param codeVO
	 * @return
	 * @throws Exception
	 */
	public List<CodeVO> getDeptList(CodeVO codeVO) throws Exception;
	
	/**
	 * 사용자 목록 조회
	 * @param codeVO
	 * @return
	 * @throws Exception
	 */
	public List<CodeVO> getUserList(CodeVO codeVO) throws Exception;
	
	/**
	 * 권한그룹 목록 조회
	 * @return
	 * @throws Exception
	 */
	public List<CodeVO> getAuthGroupList() throws Exception;
	
	/**
	 * 프로그램 목록 조회
	 * @param codeVO
	 * @return
	 * @throws Exception
	 */
	public List<CodeVO> getProgramList(CodeVO codeVO) throws Exception;
	
	/**
	 * 캠페인 목록 조회
	 * @param campaignVO
	 * @return
	 * @throws Exception
	 */
	public List<CampaignVO> getCampaignList(CampaignVO campaignVO) throws Exception;
	
	/**
	 * SMS 캠페인 목록 조회
	 * @param campaignVO
	 * @return
	 * @throws Exception
	 */
	public List<SmsCampaignVO> getCampaignSmsList(SmsCampaignVO smsCampaignVO) throws Exception;
	
	/**
	 * 보조업무작업상태(발송상태)코드 목록 조회
	 * @param codeVO
	 * @return
	 * @throws Exception
	 */
	public List<CodeVO> getWorkStatusList(CodeVO codeVO) throws Exception;
	
	/**
	 * PUSH 캠페인 목록 조회
	 * @param campaignVO
	 * @return
	 * @throws Exception
	 */
	public List<PushCampaignVO> getCampaignPushList(PushCampaignVO pushCampaignVO) throws Exception;
	
	/**
	 * RCode리스트 조회 
	 * @param codeVO
	 * @return
	 * @throws Exception
	 */
	public List<CodeVO> getRcodeList(CodeVO codeVO) throws Exception;
	
	/**
	 * 카카오템플릿 리스트 조회
	 * @param codeVO
	 * @return
	 * @throws Exception
	 */
	public List<CodeVO> getKakaoTemplateList(CodeVO codeVO) throws Exception;

}
