/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.22
 * 설명 : 캠페인관리 서비스 인터페이스
 */
package kr.co.sict.ums.ems.cam.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.sict.ums.ems.ana.vo.SendLogVO;
import kr.co.sict.ums.ems.apr.vo.SecuApprovalLineVO;
import kr.co.sict.ums.ems.cam.vo.ApprovalOrgVO;
import kr.co.sict.ums.ems.cam.vo.AttachVO;
import kr.co.sict.ums.ems.cam.vo.CampaignVO;
import kr.co.sict.ums.ems.cam.vo.EmailClinicVO;
import kr.co.sict.ums.ems.cam.vo.LinkVO;
import kr.co.sict.ums.ems.cam.vo.SendTestLogVO;
import kr.co.sict.ums.ems.cam.vo.TaskVO;
import kr.co.sict.ums.ems.cam.vo.TestUserVO;
import kr.co.sict.ums.ems.cam.vo.WebAgentVO;
import kr.co.sict.ums.sys.acc.vo.UserVO;
import kr.co.sict.util.PropertiesUtil;

@Service
public interface CampaignService {
	/**
	 * 캠페인 목록 조회
	 * @param campaignVO
	 * @return
	 * @throws Exception
	 */
	public List<CampaignVO> getCampaignList(CampaignVO campaignVO) throws Exception;
	
	/**
	 * 캠페인 + rns 서비스 목록 조회
	 * @param campaignVO
	 * @return
	 * @throws Exception
	 */
	public List<CampaignVO> getCampaignRnsList(CampaignVO campaignVO) throws Exception;
	
	/**
	 * 메일 목록 조회
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public List<TaskVO> getMailList(TaskVO taskVO) throws Exception;
	
	/**
	 * 메일 목록 조회(단기메일 - 일회성)
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public List<TaskVO> getMailListOnetime(TaskVO taskVO) throws Exception;
	
	/**
	 * 메일 목록 조회(단기메일 - 정기성)
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public List<TaskVO> getMailListRepeat(TaskVO taskVO) throws Exception;
	
	/**
	 * 메일 목록 조회(통합-단기,정기한건만)
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public List<TaskVO> getMailListUnion(TaskVO taskVO) throws Exception;
	
	/**
	 * 캠페인 정보 조회
	 * @param campaignVO
	 * @return
	 * @throws Exception
	 */
	public CampaignVO getCampaignInfo(CampaignVO campaignVO) throws Exception;
	
	/**
	 * 캠페인 정보 등록
	 * @param campaignVO
	 * @return
	 * @throws Exception
	 */
	public int insertCampaignInfo(CampaignVO campaignVO) throws Exception;
	
	/**
	 * 캠페인 정보 수정
	 * @param campaingVO
	 * @return
	 * @throws Exception
	 */
	public int updateCampaignInfo(CampaignVO campaingVO) throws Exception;
	
	/**
	 * 캠페인 주업무 및 보조업무 승인처리(발송대기 -> 발송승인)
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int updateMailAdmit(TaskVO taskVO) throws Exception;
	
	/**
	 * 메일 정보를 등록한다.(주업무 -> 보조업무 -> 첨부파일 -> 링크)
	 * @param taskVO
	 * @param attachList
	 * @param linkList
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int insertMailInfo(TaskVO taskVO, List<AttachVO> attachList, List<LinkVO> linkList) throws Exception;
	
	/**
	 * 캠페인 주업무 및 보조업무 상태변경(사용중지, 삭제)
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int updateMailStatus(TaskVO taskVO) throws Exception;
	
	/**
	 * 메일 정보를 복사한다.(주업무, 보조업무, 첨부파일 복사)
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int copyMailInfo(TaskVO taskVO, PropertiesUtil properties) throws Exception;
	
	/**
	 * 테스트 사용자 목록 조회
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<TestUserVO> getTestUserList(String userId) throws Exception;
	
	/**
	 * 테스트 사용자 정보 등록
	 * @param testUserVO
	 * @return
	 * @throws Exception
	 */
	public int insertTestUserInfo(TestUserVO testUserVO) throws Exception;
	
	/**
	 * 테스트 사용자 정보 수정
	 * @param testUserVO
	 * @return
	 * @throws Exception
	 */
	public int updateTestUserInfo(TestUserVO testUserVO) throws Exception;
	
	/**
	 * 테스트 사용자 정보 삭제
	 * @param testUserVO
	 * @return
	 * @throws Exception
	 */
	public int deleteTestUserInfo(TestUserVO testUserVO) throws Exception;
	
	/**
	 * 테스트메일 발송 정보 등록
	 * @param testUserVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int sendTestMail(TestUserVO testUserVO, HttpSession session) throws Exception;
	
	/**
	 * 메일을 재발송 한다.(주업무, 보조업무, 첨부파일)
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int reSendMail(TaskVO taskVO, HttpSession session) throws Exception;
	
	/**
	 * 메일 정보(캠페인 주업무,보조업무) 정보 조회
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public TaskVO getMailInfo(TaskVO taskVO) throws Exception;
	
	/**
	 * 첨부파일 목록 조회
	 * @param taskNo
	 * @return
	 * @throws Exception
	 */
	public List<AttachVO> getAttachList(int taskNo) throws Exception;
	
	/**
	 * 메일 정보를 수정한다.(주업무 -> 보조업무 -> 첨부파일 -> 링크)
	 * @param taskVO
	 * @param attachList
	 * @param linkList
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int updateMailInfo(TaskVO taskVO, List<AttachVO> attachList, List<LinkVO> linkList) throws Exception;
	
	/**
	 * 메일 일자 정보를 수정한다.
	 * @param taskVO
	 * @param attachList
	 * @param linkList
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int updateMailInfoDate(TaskVO taskVO) throws Exception;
	
	/**
	 * 테스트발송상세 목록 조회
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public List<TaskVO> getMailTestTaskList(TaskVO taskVO) throws Exception;
	
	/**
	 * 테스트발송결과 목록 조회
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public List<SendTestLogVO> getMailTestSendLogList(TaskVO taskVO) throws Exception;
	
	/**
	 * 정기발송 최소 최대 기간 조회 
	 * @param taskNo
	 * @return
	 * @throws Exception
	 */
	public TaskVO getMailPeriod(int taskNo) throws Exception;
	
	/**
	 * 캠페인별 메일발송목록 조회
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public List<TaskVO> getCampMailList(TaskVO taskVO) throws Exception;
	
	
	
	
	
	
	/************* 발송결재팝업창 구성 ***************/
	/**
	 * 최상위 조직 목록을 조회한다.
	 * @return
	 * @throws Exception
	 */
	public List<ApprovalOrgVO> getOrgListLvl1() throws Exception;
	
	/**
	 * 하위 조직 목록을 조회한다.
	 * @param orgCd
	 * @return
	 * @throws Exception
	 */
	public List<ApprovalOrgVO> getOrgListChild(String upOrgCd) throws Exception;
	
	/**
	 * 조직에 속한 사용자 목록 조회
	 * @param orgCd
	 * @return
	 * @throws Exception
	 */
	public List<UserVO> getUserListOrg(String orgCd) throws Exception;
	
	/**
	 * 사용자명 검색으로 사용자 목록 조회
	 * @param userNm
	 * @return
	 * @throws Exception
	 */
	public List<UserVO> getUserListSearch(String userNm) throws Exception;
	
	
	
	/**
	 * 웹에이전트 정보 조회
	 * @param webAgentVO
	 * @return
	 * @throws Exception
	 */
	public WebAgentVO getWebAgentInfo(int taskNo) throws Exception;
	
	/**
	 * 웹에이전트 정보 등록
	 * @param webAgentVO
	 * @return
	 * @throws Exception
	 */
	public int insertWebAgent(WebAgentVO webAgentVO) throws Exception;
	
	/**
	 * 웹에이전트 정보 수정
	 * @param webAgentVO
	 * @return
	 * @throws Exception
	 */
	public int updateWebAgent(WebAgentVO webAgentVO) throws Exception;
	
	/**
	 * 웹에이전트 정보 삭제
	 * @param webAgentVO
	 * @return
	 * @throws Exception
	 */
	public int deleteWebAgent(WebAgentVO webAgentVO) throws Exception;
	
	/**
	 * 발송결과유형구분(발송결과종별) 조회
	 * @param taskNo
	 * @return
	 * @throws Exception
	 */
	public EmailClinicVO getEmailClinicInfo(int taskNo) throws Exception;
	
	/**
	 * 발송결과유형구분(발송결과종별) 등록
	 * @param emailClinicVO
	 * @return
	 * @throws Exception
	 */
	public int insertEmailClinic(EmailClinicVO emailClinicVO) throws Exception;
	
	/**
	 * 발송결과유형구분(발송결과종별) 등록
	 * @param emailClinicVO
	 * @return
	 * @throws Exception
	 */
	public int updateEmailClinic(EmailClinicVO emailClinicVO) throws Exception;
	
	/**
	 * 발송결과유형구분(발송결과종별) 등록
	 * @param emailClinicVO
	 * @return
	 * @throws Exception
	 */
	public int deleteEmailClinic(EmailClinicVO emailClinicVO) throws Exception;
	
	/**
	 * 발송결재라인 목록 조회
	 * @param taskNo
	 * @return
	 * @throws Exception
	 */
	public List<SecuApprovalLineVO> getApprovalLineList(int taskNo) throws Exception;
	
	/**
	 * 발송결재라인 정보 등록
	 * @param approvalLineVO
	 * @return
	 * @throws Exception
	 */
	public int insertApprovalLine(SecuApprovalLineVO approvalLineVO) throws Exception;

	/**
	 * 발송결재라인 정보 수정
	 * @param approvalLineVO
	 * @return
	 * @throws Exception
	 */
	public int updateApprovalLine(SecuApprovalLineVO approvalLineVO) throws Exception;
	
	/**
	 * 발송결재라인 정보 삭제
	 * @param approvalLineVO
	 * @return
	 * @throws Exception
	 */
	public int deleteApprovalLine(int taskNo) throws Exception;
	
	/**
	 * 보안결재 상신 처리
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int updateSubmitApproval(TaskVO taskVO) throws Exception;
	
	/**
	 * 테스트발송 목록 조회
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public List<TaskVO> getMailTestList(TaskVO taskVO) throws Exception;
	
	/**
	 * 정기메일 차수별 발송목록 조회
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public List<TaskVO> getSubTaskList(TaskVO taskVO) throws Exception;
	
	/**
	 * 테스트메일발송결과 조회
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public List<SendLogVO> getMailTestResultList(TaskVO taskVO) throws Exception;
	
	/**
	 * 첫번째 결재자 ID 조회
	 * @param taskNo
	 * @return
	 * @throws Exception
	 */
	public String getFirstApprUserId(int taskNo) throws Exception;
	
	/**
	 * 결재취소처리
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int cancelMailApproval(TaskVO taskVO) throws Exception;
	
	/**
	 * 메일 발송 이력 조회
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public List<TaskVO> getMailSendHist(TaskVO taskVO) throws Exception;
	
	/**
	 * 키값 기준 갯수 
	 * @param requestKey
	 * @return
	 * @throws Exception
	 */
	public int getCountRequestKey(String requestKey) throws Exception;
}
