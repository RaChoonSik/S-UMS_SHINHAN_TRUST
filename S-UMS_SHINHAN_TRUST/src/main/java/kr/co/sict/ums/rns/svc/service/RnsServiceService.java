/**
 * 작성자 : 김상진
 * 작성일시 : 2021.09.01
 * 설명 : RNS 서비스 관리 서비스 인터페이스
 */
package kr.co.sict.ums.rns.svc.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.sict.ums.rns.svc.vo.RnsSecuApprovalLineVO;
import kr.co.sict.ums.rns.svc.vo.RnsAttachVO;
import kr.co.sict.ums.rns.svc.vo.RnsMailQueueTestVO;
import kr.co.sict.ums.rns.svc.vo.RnsMailQueueVO;
import kr.co.sict.ums.rns.svc.vo.RnsRecipientInfoVO;
import kr.co.sict.ums.rns.svc.vo.RnsServiceVO;
import kr.co.sict.ums.rns.svc.vo.RnsWebAgentVO;
import kr.co.sict.util.PropertiesUtil;

@Service
public interface RnsServiceService {

	/**
	 * 자동메일 서비스목록 조회
	 * @return
	 * @throws Exception
	 */
	public List<RnsServiceVO> getServiceList(RnsServiceVO rnsAutoSendVO) throws Exception;
	
	/**
	 * 자동메일 서비스 정보 조회
	 * @param tid
	 * @return
	 * @throws Exception
	 */
	public RnsServiceVO getServiceInfo(int tid) throws Exception;
	
	/**
	 * 자동메일 서비스 정보 조회 EaiCampNo기준
	 * @param tid
	 * @return
	 * @throws Exception
	 */
	public RnsServiceVO getServiceInfoByEai(String eaiCampNo) throws Exception;
	
	/**
	 * 자동메일 서비스 정보 등록
	 * @param rnsAutoSendVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int insertServiceInfo(RnsServiceVO rnsAutoSendVO, List<RnsAttachVO> attachList) throws Exception;
	
	/**
	 * 자동메일 서비스 정보 수정
	 * @param rnsAutoSendVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int updateServiceInfo(RnsServiceVO rnsAutoSendVO, List<RnsAttachVO> attachList) throws Exception;
	
	/**
	 * 자동메일 서비스 부분 정보 수정
	 * @param rnsAutoSendVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int updateServicePartInfo(RnsServiceVO rnsAutoSendVO) throws Exception;	
	
	/**
	 * 자동메일 서비스 상태 변경
	 * @param rnsServiceVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int updateServiceStatus(RnsServiceVO rnsServiceVO) throws Exception;
	
	/**
	 * 자동메일 서비스 첨부파일 목록 조회
	 * @param tid
	 * @return
	 * @throws Exception
	 */
	public List<RnsAttachVO> getAttachList(int tid) throws Exception;
	
	/**
	 * 자동메일 서비스 테스트 발송 등록
	 * @param recipientList
	 * @param queueList
	 * @param testVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int serviceTestSend(List<RnsRecipientInfoVO> recipientList, RnsMailQueueVO queue, RnsMailQueueTestVO testVO) throws Exception;
	
	/**
	 * 자동메일 서비스 재발송 등록
	 * @param recipientList
	 * @param queueList
	 * @param testVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int serviceReSend(RnsRecipientInfoVO recipientVO) throws Exception;
	
	/**
	 * 자동메일 서비스 복사
	 * @param serviceVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int serviceCopy(RnsServiceVO serviceVO, PropertiesUtil properties) throws Exception;
	
	/**
	 * 자동메일 서비스 웹에이전트 정보 조회
	 * @param tid
	 * @return
	 * @throws Exception
	 */
	public RnsWebAgentVO getWebAgentInfo(int tid) throws Exception;
	
	/**
	 * 자동메일 서비스 테스트메일 발송목록 조회
	 * @param serviceVO
	 * @return
	 * @throws Exception
	 */
	public List<RnsServiceVO> getServiceTestList(RnsServiceVO serviceVO) throws Exception;
	
	/**
	 * 자동메일 서비스 테스트메일 발송결과목록 조회
	 * @param recipientInfoVO
	 * @return
	 * @throws Exception
	 */
	public List<RnsRecipientInfoVO> getServiceTestResultList(RnsRecipientInfoVO recipientInfoVO) throws Exception;
	
	
	/**
	 * 실시간 메일 발송결재라인 목록 조회
	 * @param taskNo
	 * @return
	 * @throws Exception
	 */
	public List<RnsSecuApprovalLineVO> getRnsApprovalLineList(int tid) throws Exception;
	
	/**
	 * 실시간 메일 발송결재라인 정보 등록
	 * @param approvalLineVO
	 * @return
	 * @throws Exception
	 */
	public int insertRnsApprovalLine(RnsSecuApprovalLineVO rnsApprovalLineVO) throws Exception;

	/**
	 * 실시간 메일 발송결재라인 정보 수정
	 * @param approvalLineVO
	 * @return
	 * @throws Exception
	 */
	public int updateRnsApprovalLine(RnsSecuApprovalLineVO rnsApprovalLineVO) throws Exception;
	
	/**
	 * 실시간 메일 발송결재라인 정보 삭제
	 * @param approvalLineVO
	 * @return
	 * @throws Exception
	 */
	public int deleteRnsApprovalLine(int tid) throws Exception;
	
	/**
	 * 실시간 메일 결재라인 스탭 상태 변경
	 * @param approvalLineVO
	 * @return
	 * @throws Exception
	 */
	public int updateRnsServiceAprStep(RnsSecuApprovalLineVO rnsApprovalLineVO) throws Exception;
	
	/**
	 * 실시간 메일 결재라인 스탭 상태 변경(다음 스탭 상태 요청으로 변경)
	 * @param approvalLineVO
	 * @return
	 * @throws Exception
	 */
	public int updateRnsServiceAprStepNext(RnsSecuApprovalLineVO rnsApprovalLineVO) throws Exception;
	
	/**
	 * 실시간 메일 발송상태 변경
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int updateRnsServiceWorkStatus(RnsServiceVO rnsServiceVO) throws Exception;
	
	/**
	 * 보안결재 메일상신 처리
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */	
	public int updateSubmitApproval(RnsServiceVO rnsServiceVO) throws Exception;
	 
	/**
	 * 보안결재라인 요청상태 변경
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int updateSubmitApprovalLine(RnsServiceVO rnsServiceVO) throws Exception;
	
	/**
	 * 첫번째 결재자 ID 조회
	 * @param taskNo
	 * @return
	 * @throws Exception
	 */	
	public String getFirstApprUserId(int tid) throws Exception;
	
	/**
	 * 결재취소처리
	 * @param rnsServiceVO
	 * @return
	 * @throws Exception
	 */	
	public int cancelRnsApproval(RnsServiceVO rnsServiceVO) throws Exception;
	
	/**
	 * 키값 기준 갯수 
	 * @param requestKey
	 * @return
	 * @throws Exception
	 */
	public int getCountRequestKey(String requestKey) throws Exception;
}
