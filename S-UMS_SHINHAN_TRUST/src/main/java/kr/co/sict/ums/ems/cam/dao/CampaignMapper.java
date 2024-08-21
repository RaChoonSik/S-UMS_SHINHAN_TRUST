/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.22
 * 설명 : 캠페인관리 매퍼
 */
package kr.co.sict.ums.ems.cam.dao;

import java.util.List;

import kr.co.sict.ums.ems.ana.vo.SendLogVO;
import kr.co.sict.ums.ems.apr.vo.SecuApprovalLineVO;
import kr.co.sict.ums.ems.cam.vo.ApprovalOrgVO;
import kr.co.sict.ums.ems.cam.vo.AttachVO;
import kr.co.sict.ums.ems.cam.vo.CampaignVO;
import kr.co.sict.ums.ems.cam.vo.EmailClinicVO;
import kr.co.sict.ums.ems.cam.vo.LinkVO;
import kr.co.sict.ums.ems.cam.vo.MailMktChkVO;
import kr.co.sict.ums.ems.cam.vo.SendTestLogVO;
import kr.co.sict.ums.ems.cam.vo.TaskVO;
import kr.co.sict.ums.ems.cam.vo.TestUserVO;
import kr.co.sict.ums.ems.cam.vo.WebAgentVO;
import kr.co.sict.ums.sys.acc.vo.UserVO;

public interface CampaignMapper {
	/**
	 * 캠페인 목록 조회
	 * @param campaignVO
	 * @return
	 * @throws Exception
	 */
	public List<CampaignVO> getCampaignList(CampaignVO campaignVO) throws Exception;
	
	/**
	 * 캠페인+ rns 서비스 목록 조회
	 * @param campaignVO
	 * @return
	 * @throws Exception
	 */
	public List<CampaignVO> getCampaignRnsList(CampaignVO campaignVO) throws Exception;
	
	/**
	 * 캠페인 이메일 서비스 목록 조회
	 * @param campaignVO
	 * @return
	 * @throws Exception
	 */
	public List<CampaignVO> getCampaignEmsOnlyList(CampaignVO campaignVO) throws Exception;
	
	/**
	 * RNS 서비스 목록 조회
	 * @param campaignVO
	 * @return
	 * @throws Exception
	 */
	public List<CampaignVO> getCampaignRnsOnlyList(CampaignVO campaignVO) throws Exception;	
	
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
	 * 캠페인 주업무 발송승인
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int updateTaskStatusAdmit(TaskVO taskVO) throws Exception;
	
	/**
	 * 캠페인 보조업무 발송승인
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int updateSubTaskStatusAdmit(TaskVO taskVO) throws Exception;
	
	/**
	 * 주업무 정보 등록
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int insertTaskInfo(TaskVO taskVO) throws Exception;
	
	/**
	 * 등록한 주업무 번호 조회
	 * @return
	 * @throws Exception
	 */
	public int getTaskNo() throws Exception;
	
	/**
	 * 보조업무 번호 조회
	 * @param taskNo
	 * @return
	 * @throws Exception
	 */
	public int getSubTaskNo(int taskNo) throws Exception;
	
	/**
	 * 보조업무 정보 등록
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int insertSubTaskInfo(TaskVO taskVO) throws Exception;
	
	/**
	 * 첨부파일 정보 등록
	 * @param attachVO
	 * @return
	 * @throws Exception
	 */
	public int insertAttachInfo(AttachVO attachVO) throws Exception;
	
	/**
	 * 링크 정보 등록
	 * @param linkVO
	 * @return
	 * @throws Exception
	 */
	public int insertLinkInfo(LinkVO linkVO) throws Exception;
	
	/**
	 * 캠페인 주업무 상태변경(사용중지, 삭제)
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int updateTaskStatus(TaskVO taskVO) throws Exception;
	
	/**
	 * 캠페인 보조업무 상태변경(사용중지, 삭제)
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int updateSubTaskStatus(TaskVO taskVO) throws Exception;
	
	/**
	 * 캠페인 주업무 정보 조회
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	public TaskVO getTaskInfo(int seq) throws Exception;
	
	/**
	 * 캠페인 보조업무 정보 조회
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public TaskVO getSubTaskInfo(TaskVO taskVO) throws Exception;
	
	/**
	 * 캠페인 주업무 등록(복사용)
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int insertTaskInfoForCopy(TaskVO taskVO) throws Exception;
	
	/**
	 * 캠페인 보조업무 등록(복사용)
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int insertSubTaskInfoForCopy(TaskVO taskVO) throws Exception;
	
	/**
	 * 첨부파일 목록 조회
	 * @param taskNo
	 * @return
	 * @throws Exception
	 */
	public List<AttachVO> getAttachList(int taskNo) throws Exception;
	
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
	 * 캠페인 주업무 등록(테스트발송용)
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int insertTaskInfoForTestSend(TaskVO taskVO) throws Exception;
	
	/**
	 * 캠페인 보조업무 등록(테스트발송용)
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int insertSubTaskInfoForTestSend(TaskVO taskVO) throws Exception;
	
	
	/**
	 * 캠페인 주업무 등록(재발송)
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int insertTaskInfoForReSend(TaskVO taskVO) throws Exception;
	
	/**
	 * 캠페인 보조업무 등록(재발송)
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int insertSubTaskInfoForReSend(TaskVO taskVO) throws Exception;	
	
	/**
	 * 메일 정보(캠페인 주업무,보조업무) 정보 조회
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public TaskVO getMailInfo(TaskVO taskVO) throws Exception;
	
	/**
	 * 메일(캠페인 주업무) 정보 수정
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int updateTaskInfo(TaskVO taskVO) throws Exception;
	
	/**
	 * 메일(캠페인 보조업무) 정보 수정
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int updateSubTaskInfo(TaskVO taskVO) throws Exception;
	
	/**
	 * 메일(캠페인 주업무) 일자 정보 수정
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int updateTaskInfoDate(TaskVO taskVO) throws Exception;
	
	/**
	 * 메일(캠페인 보조업무) 일자 정보 수정
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int updateSubTaskInfoDate(TaskVO taskVO) throws Exception;	
	
	/**
	 * 첨부파일 정보 삭제
	 * @param taskNo
	 * @return
	 * @throws Exception
	 */
	public int deleteAttachInfo(int taskNo) throws Exception;
	
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
	 * 캠페인별 메일발송목록 조회
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public List<TaskVO> getCampMailList(TaskVO taskVO) throws Exception;
	
	
	
	
	/************************************************************ 발송결재팝업창 구성 ************************************************************/
	/**
	 * 최상위 조직 목록 조회
	 * @return
	 * @throws Exception
	 */
	public List<ApprovalOrgVO> getOrgListLvl1() throws Exception;
	
	/**
	 * 하위 조직 목록 조회
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
	 * @param taskNo
	 * @return
	 * @throws Exception
	 */
	public int deleteApprovalLine(int taskNo) throws Exception;
	
	/**
	 * 보안결재 메일상신 처리
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int updateSubmitApproval(TaskVO taskVO) throws Exception;
	
	/**
	 * 보안결재라인 요청상태 변경
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int updateSubmitApprovalLine(TaskVO taskVO) throws Exception;
	
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
	 * 정기발송 최소 최대 기간 조회 
	 * @param taskNo
	 * @return
	 * @throws Exception
	 */
	public TaskVO getMailPeriod(int taskNo) throws Exception;
	
	/**
	 * 마케팅수신동의유형 조회
	 * @param taskNo
	 * @return
	 * @throws Exception
	 */
	public MailMktChkVO getMailMktChkInfo(int taskNo) throws Exception;
	
	/**
	 * 마케팅수신동의유형 등록
	 * @param mailMktChkVO
	 * @return
	 * @throws Exception
	 */
	public int insertMailMktChkInfo(MailMktChkVO mailMktChkVO) throws Exception;
	
	/**
	 * 마케팅수신동의유형 수정
	 * @param mailMktChkVO
	 * @return
	 * @throws Exception
	 */
	public int updateMailMktChkInfo(MailMktChkVO mailMktChkVO) throws Exception;
	
	/**
	 * 마케팅수신동의유형 삭제
	 * @param mailMktChkVO
	 * @return
	 * @throws Exception
	 */
	public int deleteMailMktChkInfo(int taskNo) throws Exception;
	
	/**
	 * 첫번째 결재자 ID 조회
	 * @param taskNo
	 * @return
	 * @throws Exception
	 */
	public String getFirstApprUserId(int taskNo) throws Exception;
	
	/**
	 * 메일 발송 이력 조회
	 * @param campaignVO
	 * @return
	 * @throws Exception
	 */
	public List<TaskVO> getMailSendHist(TaskVO taskVO) throws Exception;
	
	/**
	 * 키값 기준 갯수 
	 * @param keygen
	 * @return
	 * @throws Exception
	 */
public int getCountRequestKey(String requestKey) throws Exception;	
}
