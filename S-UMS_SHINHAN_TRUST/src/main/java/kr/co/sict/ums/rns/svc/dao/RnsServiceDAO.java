/**
 * 작성자 : 김상진
 * 작성일시 : 2021.09.01
 * 설명 : RNS 서비스 관리 데이터 처리
 */
package kr.co.sict.ums.rns.svc.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.ems.apr.dao.SecuApprovalLineMapper;
import kr.co.sict.ums.ems.apr.vo.SecuApprovalLineVO;
import kr.co.sict.ums.rns.svc.vo.RnsAttachVO;
import kr.co.sict.ums.rns.svc.vo.RnsMailQueueTestVO;
import kr.co.sict.ums.rns.svc.vo.RnsMailQueueVO;
import kr.co.sict.ums.rns.svc.vo.RnsRecipientInfoVO;
import kr.co.sict.ums.rns.svc.vo.RnsServiceVO;
import kr.co.sict.ums.rns.svc.vo.RnsWebAgentVO;
import kr.co.sict.ums.rns.svc.vo.RnsSecuApprovalLineVO;

@Repository
public class RnsServiceDAO implements RnsServiceMapper {
	@Autowired
	private SqlSession sqlSessionRns;

	@Override
	public List<RnsServiceVO> getServiceList(RnsServiceVO rnsAutoSendVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).getServiceList(rnsAutoSendVO);
	}

	@Override
	public int insertServiceInfo(RnsServiceVO rnsAutoSendVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).insertServiceInfo(rnsAutoSendVO);
	}

	@Override
	public int getCurrServiceTid() throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).getCurrServiceTid();
	}

	@Override
	public int insertWebAgent(RnsWebAgentVO rnsWebAgentVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).insertWebAgent(rnsWebAgentVO);
	}

	@Override
	public int insertAttachInfo(RnsAttachVO rnsAttachVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).insertAttachInfo(rnsAttachVO);
	}

	@Override
	public int updateServiceStatus(RnsServiceVO rnsServiceVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).updateServiceStatus(rnsServiceVO);
	}

	@Override
	public RnsServiceVO getServiceInfo(int tid) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).getServiceInfo(tid);
	}
	
	@Override
	public RnsServiceVO getServiceInfoByEai(String eaiCampNo) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).getServiceInfoByEai(eaiCampNo);
	}

	@Override
	public List<RnsAttachVO> getAttachList(int tid) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).getAttachList(tid);
	}

	@Override
	public int insertMailQueue(RnsMailQueueVO queueVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).insertMailQueue(queueVO);
	}

	@Override
	public int insertMailQueueTest(RnsMailQueueTestVO testVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).insertMailQueueTest(testVO);
	}

	@Override
	public RnsWebAgentVO getWebAgentInfo(int tid) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).getWebAgentInfo(tid);
	}

	@Override
	public List<RnsServiceVO> getServiceTestList(RnsServiceVO serviceVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).getServiceTestList(serviceVO);
	}

	@Override
	public List<RnsRecipientInfoVO> getServiceTestResultList(RnsRecipientInfoVO recipientInfoVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).getServiceTestResultList(recipientInfoVO);
	}

	@Override
	public int updateServiceInfo(RnsServiceVO rnsAutoSendVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).updateServiceInfo(rnsAutoSendVO);
	}

	@Override
	public int updateServicePartInfo(RnsServiceVO rnsAutoSendVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).updateServicePartInfo(rnsAutoSendVO);
	}

	@Override
	public int deleteAttachInfo(int tid) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).deleteAttachInfo(tid);
	}

	@Override
	public int deleteWebAgent(int tid) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).deleteWebAgent(tid);
	}

	@Override
	public long getCurrMailQueueId() throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).getCurrMailQueueId();
	}

	@Override
	public int getNextSubId(long mid) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).getNextSubId(mid);
	}
	
	@Override
	public RnsRecipientInfoVO getRecipientInfo(RnsRecipientInfoVO recipientVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).getRecipientInfo(recipientVO);
	}
	
	@Override
	public RnsMailQueueVO getMailQueue(RnsRecipientInfoVO recipientVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).getMailQueue(recipientVO);
	}
	
	@Override
	public int insertRecipientInfo(RnsRecipientInfoVO recipientVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).insertRecipientInfo(recipientVO);
	}
	
	@Override
	public int insertReSendMailQueue(RnsMailQueueVO queueVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).insertReSendMailQueue(queueVO);
	}	
	
	@Override
	public List<RnsSecuApprovalLineVO> getRnsApprovalLineList(int taskNo) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).getRnsApprovalLineList(taskNo);
	}

	@Override
	public int insertRnsApprovalLine(RnsSecuApprovalLineVO rnsAapprovalLineVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).insertRnsApprovalLine(rnsAapprovalLineVO);
	}

	@Override
	public int updateRnsApprovalLine(RnsSecuApprovalLineVO rnsAapprovalLineVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).updateRnsApprovalLine(rnsAapprovalLineVO);
	}

	@Override
	public int deleteRnsApprovalLine(int tid) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).deleteRnsApprovalLine(tid);
	}

	@Override
	public int updateRnsServiceAprStep(RnsSecuApprovalLineVO rnsAapprovalLineVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).updateRnsServiceAprStep(rnsAapprovalLineVO);
	}

	@Override
	public int updateRnsServiceAprStepNext(RnsSecuApprovalLineVO rnsAapprovalLineVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).updateRnsServiceAprStepNext(rnsAapprovalLineVO);
	}

	@Override
	public int updateRnsServiceWorkStatus(RnsServiceVO rnsServiceVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).updateRnsServiceWorkStatus(rnsServiceVO);
	}
	
	@Override
	public int updateSubmitApproval(RnsServiceVO rnsServiceVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).updateSubmitApproval(rnsServiceVO);
	}

	@Override
	public int updateSubmitApprovalLine(RnsServiceVO rnsServiceVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).updateSubmitApprovalLine(rnsServiceVO);
	}

	@Override
	public String getFirstApprUserId(int tid) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).getFirstApprUserId(tid);
	}
	
	@Override
	public List<RnsSecuApprovalLineVO> nextRnsAprStep(RnsSecuApprovalLineVO rnsAapprovalLineVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).nextRnsAprStep(rnsAapprovalLineVO);
	}
	
	@Override
	public List<RnsSecuApprovalLineVO> nowRnsAprStep(RnsSecuApprovalLineVO rnsAapprovalLineVO) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).nowRnsAprStep(rnsAapprovalLineVO);
	}
	
	@Override
	public int getCountRequestKey(String requestKey) throws Exception {
		return sqlSessionRns.getMapper(RnsServiceMapper.class).getCountRequestKey(requestKey);
	}
	
}
