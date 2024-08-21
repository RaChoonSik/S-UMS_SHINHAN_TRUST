/**
 * 작성자 : 김상진
 * 작성일시 : 2021.09.01
 * 설명 : RNS 서비스 관리 매퍼
 */
package kr.co.sict.ums.rns.svc.dao;

import java.util.List;

import kr.co.sict.ums.rns.svc.vo.RnsSecuApprovalLineVO;
import kr.co.sict.ums.rns.svc.vo.RnsAttachVO;
import kr.co.sict.ums.rns.svc.vo.RnsMailQueueTestVO;
import kr.co.sict.ums.rns.svc.vo.RnsMailQueueVO;
import kr.co.sict.ums.rns.svc.vo.RnsRecipientInfoVO;
import kr.co.sict.ums.rns.svc.vo.RnsServiceVO;
import kr.co.sict.ums.rns.svc.vo.RnsWebAgentVO;

public interface RnsServiceMapper {
	
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
	 * 자동메일 서비스 등록
	 * @param rnsAutoSendVO
	 * @return
	 * @throws Exception
	 */
	public int insertServiceInfo(RnsServiceVO rnsAutoSendVO) throws Exception;
	
	/**
	 * 자동메일 서비스 수정
	 * @param rnsAutoSendVO
	 * @return
	 * @throws Exception
	 */
	public int updateServiceInfo(RnsServiceVO rnsAutoSendVO) throws Exception;
	
	/**
	 * 자동메일 서비스 부분 수정
	 * @param rnsAutoSendVO
	 * @return
	 * @throws Exception
	 */
	public int updateServicePartInfo(RnsServiceVO rnsAutoSendVO) throws Exception;	
	
	/**
	 * 자동메일 현재 서비스ID 조회
	 * @return
	 * @throws Exception
	 */
	public int getCurrServiceTid() throws Exception;
	
	/**
	 * 웹에이전트 정보 등록
	 * @param rnsWebAgentVO
	 * @return
	 * @throws Exception
	 */
	public int insertWebAgent(RnsWebAgentVO rnsWebAgentVO) throws Exception;
	
	/**
	 * 첨부파일 정보 등록
	 * @param rnsAttachVO
	 * @return
	 * @throws Exception
	 */
	public int insertAttachInfo(RnsAttachVO rnsAttachVO) throws Exception;
	
	/**
	 * 자동메일 서비스 상태 변경
	 * @param rnsServiceVO
	 * @return
	 * @throws Exception
	 */
	public int updateServiceStatus(RnsServiceVO rnsServiceVO) throws Exception;
	
	/**
	 * 자동메일 서비스 첨부파일 목록 조회
	 * @param tid
	 * @return
	 * @throws Exception
	 */
	public List<RnsAttachVO> getAttachList(int tid) throws Exception;

	/**
	 * 자동메일 테스트발송 정보 등록(MailQueue)
	 * @param queueVO
	 * @return
	 * @throws Exception
	 */
	public int insertMailQueue(RnsMailQueueVO queueVO) throws Exception;
	
	/**
	 * 자동메일 테스트발송 정보 등록(MailQueueTest)
	 * @param testVO
	 * @return
	 * @throws Exception
	 */
	public int insertMailQueueTest(RnsMailQueueTestVO testVO) throws Exception;
	
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
	 * MAILQUQUE ID 조회(MID_SEQ.CURRVAL)
	 * @return
	 * @throws Exception
	 */
	public long getCurrMailQueueId() throws Exception;
	
	/**
	 * MAILQUQUE SUB ID 조회
	 * @return
	 * @throws Exception
	 */
	public int getNextSubId(long mid) throws Exception;
	
	/**
	 * 자동메일 수신자그룹 정보 조회
	 * @param tid
	 * @return
	 * @throws Exception
	 */
	public RnsRecipientInfoVO getRecipientInfo(RnsRecipientInfoVO recipientVO) throws Exception;
	
	/**
	 * 자동메일 메일큐 정보 조회
	 * @param tid
	 * @return
	 * @throws Exception
	 */
	public RnsMailQueueVO getMailQueue(RnsRecipientInfoVO recipientVO) throws Exception;

	/**
	 * 자동메일 재발송 정보 등록(RnsRecipientInfoVO)
	 * @param queueVO
	 * @return
	 * @throws Exception
	 */
	public int insertRecipientInfo(RnsRecipientInfoVO recipientVO) throws Exception;
	
	/**
	 * 자동메일 재발송 정보 등록(MailQueue)
	 * @param queueVO
	 * @return
	 * @throws Exception
	 */
	public int insertReSendMailQueue(RnsMailQueueVO queueVO) throws Exception;	
	/**
	 * 첨부파일 정보 삭제
	 * @param tid
	 * @return
	 * @throws Exception
	 */
	public int deleteAttachInfo(int tid) throws Exception;
	
	/**
	 * 웹에이전트 정보 삭제
	 * @param tid
	 * @return
	 * @throws Exception
	 */
	public int deleteWebAgent(int tid) throws Exception;
	
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
	 * @param taskNo
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
	
	public List<RnsSecuApprovalLineVO> nextRnsAprStep(RnsSecuApprovalLineVO rnsAapprovalLineVO) throws Exception;
	
	public List<RnsSecuApprovalLineVO> nowRnsAprStep(RnsSecuApprovalLineVO rnsAapprovalLineVO) throws Exception;
	
	/**
	 * 키값 기준 갯수 
	 * @param keygen
	 * @return
	 * @throws Exception
	 */
	public int getCountRequestKey(String requestKey) throws Exception;
}
