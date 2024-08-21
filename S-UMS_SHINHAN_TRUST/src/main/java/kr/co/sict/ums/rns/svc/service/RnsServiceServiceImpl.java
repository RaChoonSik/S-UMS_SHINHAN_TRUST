/**
 * 작성자 : 김상진
 * 작성일시 : 2021.09.01
 * 설명 : RNS 서비스 관리 서비스 구현
 */
package kr.co.sict.ums.rns.svc.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.rns.svc.dao.RnsServiceDAO;
import kr.co.sict.ums.rns.svc.vo.RnsSecuApprovalLineVO;
import kr.co.sict.ums.rns.svc.vo.RnsAttachVO;
import kr.co.sict.ums.rns.svc.vo.RnsMailQueueTestVO;
import kr.co.sict.ums.rns.svc.vo.RnsMailQueueVO;
import kr.co.sict.ums.rns.svc.vo.RnsRecipientInfoVO;
import kr.co.sict.ums.rns.svc.vo.RnsServiceVO;
import kr.co.sict.ums.rns.svc.vo.RnsWebAgentVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Service
public class RnsServiceServiceImpl implements RnsServiceService {
	
	@Autowired
	private RnsServiceDAO rnsServiceDAO;

	@Override
	public List<RnsServiceVO> getServiceList(RnsServiceVO rnsAutoSendVO) throws Exception {
		return rnsServiceDAO.getServiceList(rnsAutoSendVO);
	}

	@Override
	public int insertServiceInfo(RnsServiceVO rnsAutoSendVO, List<RnsAttachVO> attachList) throws Exception {
		int result = 0;
		
		// 서비스 정보 등록
		result += rnsServiceDAO.insertServiceInfo(rnsAutoSendVO);
		
		// 서비스ID 조회
		int tid = rnsServiceDAO.getCurrServiceTid();

		// 첨부파일 등록
		if(attachList != null && attachList.size() > 0) {
			for(RnsAttachVO attach:attachList) {
				attach.setTid(tid);
				attach.setRegId(rnsAutoSendVO.getRegId());
				attach.setRegDt(rnsAutoSendVO.getRegDt());
				
				result += rnsServiceDAO.insertAttachInfo(attach);
			}
		}

		// 웹에이전트 둥록
		if(!StringUtil.isNull(rnsAutoSendVO.getWebAgentAttachYn())) {
			RnsWebAgentVO webAgent = new RnsWebAgentVO();
			webAgent.setTid(tid);
			webAgent.setAttNo(0);
			webAgent.setSourceUrl(rnsAutoSendVO.getWebAgentUrl());
			webAgent.setSecuAttTyp(rnsAutoSendVO.getSecuAttTyp());
			webAgent.setSecuAttYn(rnsAutoSendVO.getWebAgentAttachYn());
			webAgent.setRegId(rnsAutoSendVO.getRegId());
			webAgent.setRegDt(rnsAutoSendVO.getRegDt());
			
			result += rnsServiceDAO.insertWebAgent(webAgent);
		}
		
		// 발송결재라인정보 등록
		if(!StringUtil.isNull(rnsAutoSendVO.getApprUserId())) {
			String[] apprUserStr = rnsAutoSendVO.getApprUserId().split(",");
			for(int i=0;i<apprUserStr.length;i++) {
				//appUserId => userId|orgCd|posGb|jobGb
				String[] userInfo = apprUserStr[i].split("\\|");
				RnsSecuApprovalLineVO apprLine = new RnsSecuApprovalLineVO();
				for(int j=0;j<userInfo.length;j++) {
					apprLine.setTid(tid);
					apprLine.setApprStep(i+1);
					apprLine.setApprUserId(userInfo[0]);
					apprLine.setOrgCd(userInfo[1]);
					apprLine.setPositionGb(userInfo[2]);
					apprLine.setJobGb(userInfo[3]);
					apprLine.setRsltCd("000");
					apprLine.setRegId(rnsAutoSendVO.getRegId());
					apprLine.setRegDt(rnsAutoSendVO.getRegDt());
				}
				
				result += rnsServiceDAO.insertRnsApprovalLine(apprLine);
			}
		}		
		
		return result;
	}
	
	@Override
	public int updateServiceInfo(RnsServiceVO rnsAutoSendVO, List<RnsAttachVO> attachList) throws Exception {
		int result = 0;
		
		// 서비스 정보 수정
		result += rnsServiceDAO.updateServiceInfo(rnsAutoSendVO);
		
		// 기존 첨부파일 삭제
		result += rnsServiceDAO.deleteAttachInfo(rnsAutoSendVO.getTid());
		// 첨부파일 등록
		if(attachList != null && attachList.size() > 0) {
			for(RnsAttachVO attach:attachList) {
				attach.setRegId(rnsAutoSendVO.getUpId());
				attach.setRegDt(rnsAutoSendVO.getUpDt());
				
				result += rnsServiceDAO.insertAttachInfo(attach);
			}
		}

		// 기존 웹에이전트 정보 삭제
		result += rnsServiceDAO.deleteWebAgent(rnsAutoSendVO.getTid());
		// 웹에이전트 둥록
		if(!StringUtil.isNull(rnsAutoSendVO.getWebAgentAttachYn())) {
			RnsWebAgentVO webAgent = new RnsWebAgentVO();
			webAgent.setTid(rnsAutoSendVO.getTid());
			webAgent.setAttNo(0);
			webAgent.setSourceUrl(rnsAutoSendVO.getWebAgentUrl());
			webAgent.setSecuAttTyp(rnsAutoSendVO.getSecuAttTyp());
			webAgent.setSecuAttYn(rnsAutoSendVO.getWebAgentAttachYn());
			webAgent.setRegId(rnsAutoSendVO.getUpId());
			webAgent.setRegDt(rnsAutoSendVO.getUpDt());
			
			result += rnsServiceDAO.insertWebAgent(webAgent);
		}
		
		// 기존 발송결재라인 정보 삭제	
		result += rnsServiceDAO.deleteRnsApprovalLine(rnsAutoSendVO.getTid());
		// 발송결재라인정보 등록
		if(!StringUtil.isNull(rnsAutoSendVO.getApprUserId())) {
			String[] apprUserStr = rnsAutoSendVO.getApprUserId().split(",");
			for(int i=0;i<apprUserStr.length;i++) {
				//appUserId => userId|orgCd|posGb|jobGb
				String[] userInfo = apprUserStr[i].split("\\|");
				RnsSecuApprovalLineVO apprLine = new RnsSecuApprovalLineVO();
				for(int j=0;j<userInfo.length;j++) {
					apprLine.setTid(rnsAutoSendVO.getTid());
					apprLine.setApprStep(i+1);
					apprLine.setApprUserId(userInfo[0]);
					apprLine.setOrgCd(userInfo[1]);
					apprLine.setPositionGb(userInfo[2]);
					apprLine.setJobGb(userInfo[3]);
					apprLine.setRsltCd("000");
					apprLine.setRegId(rnsAutoSendVO.getUpId());
					apprLine.setRegDt(rnsAutoSendVO.getUpDt());
				}
				
				result += rnsServiceDAO.insertRnsApprovalLine(apprLine);
			} 
		}
 
		return result;
	}
	
	@Override
	public int updateServicePartInfo(RnsServiceVO rnsAutoSendVO) throws Exception {
		return rnsServiceDAO.updateServicePartInfo(rnsAutoSendVO);
	}
	
	@Override
	public int updateServiceStatus(RnsServiceVO rnsServiceVO) throws Exception {
		int result = 0;
		if(!StringUtil.isNull(rnsServiceVO.getTids())) {
			String[] tid = rnsServiceVO.getTids().split(",");
			for(int i=0;i<tid.length;i++) {
				RnsServiceVO serviceVO = new RnsServiceVO();
				serviceVO.setTid(Integer.parseInt(tid[i]));
				serviceVO.setStatus(rnsServiceVO.getStatus());
				serviceVO.setUpId(rnsServiceVO.getUpId());
				serviceVO.setUpDt(rnsServiceVO.getUpDt());
				
				result += rnsServiceDAO.updateServiceStatus(serviceVO);
			}
		}
		
		return result;
	}

	@Override
	public RnsServiceVO getServiceInfo(int tid) throws Exception {
		return rnsServiceDAO.getServiceInfo(tid);
	}
	
	@Override
	public RnsServiceVO getServiceInfoByEai(String eaiCampNo) throws Exception {
		return rnsServiceDAO.getServiceInfoByEai(eaiCampNo);
	}

	@Override
	public List<RnsAttachVO> getAttachList(int tid) throws Exception {
		return rnsServiceDAO.getAttachList(tid);
	}

	@Override
	public int serviceTestSend(List<RnsRecipientInfoVO> recipientList, RnsMailQueueVO queue, RnsMailQueueTestVO testVO) throws Exception {
		int result = 0;
		
		// MailQueue 등록
		result += rnsServiceDAO.insertMailQueue(queue);
		
		// MID 조회
		long mid = rnsServiceDAO.getCurrMailQueueId();
		
		// RecipientInfo 등록
		for(RnsRecipientInfoVO recipientVO : recipientList) {
			recipientVO.setMid(mid);
			result += rnsServiceDAO.insertRecipientInfo(recipientVO);
		}
		
		// MailQueueTest 등록
		testVO.setMid(mid);
		result += rnsServiceDAO.insertMailQueueTest(testVO);
		
		return result;
	}
	@Override
	public int serviceReSend(RnsRecipientInfoVO recipientVO) throws Exception {
		int result = 0;		
		
		RnsMailQueueVO addQueueVO = rnsServiceDAO.getMailQueue(recipientVO);
		RnsRecipientInfoVO addRecipientVO = rnsServiceDAO.getRecipientInfo(recipientVO);
		
		long rtyMid = addQueueVO.getRtyMid(); 
		int rtySubid  = addQueueVO.getRtySubid(); 
		//재발송 아님 
		if ( rtyMid == 0 ) {
			rtyMid = recipientVO.getMid();
			rtySubid = recipientVO.getSubid();
		}
		
		addQueueVO.setSubid(0);
		addQueueVO.setStatus("0");
		addQueueVO.setSpos("0");
		addQueueVO.setDbcode("0"); 
		addQueueVO.setRefmid(0);
		addQueueVO.setCharset(0);
		addQueueVO.setRtyTyp("000");
		addQueueVO.setRtyMid(rtyMid);
		addQueueVO.setRtySubid(rtySubid);
		
		result += rnsServiceDAO.insertReSendMailQueue(addQueueVO);
		long mid  = rnsServiceDAO.getCurrMailQueueId();
		
		addRecipientVO.setMid(mid);
		addRecipientVO.setSubid(0);
		result += rnsServiceDAO.insertRecipientInfo(addRecipientVO);
		
		return result;
	}
	
	@Override
	public int serviceCopy(RnsServiceVO serviceVO, PropertiesUtil properties) throws Exception {
		int result = 0;
		
		// 서비스 정보 조회
		RnsServiceVO serviceCopy = rnsServiceDAO.getServiceInfo(serviceVO.getTid());
		
		// Content 파일 복사
		if("1".equals(serviceCopy.getContentsTyp().trim())) {
			serviceCopy.setContentsPath(null);
		} else {
			String currDtm = StringUtil.getDate(Code.TM_YMDHMSM);
			String oldFullPath = serviceCopy.getContentsPath();
			String newFullPath = serviceCopy.getContentsPath().substring(0, serviceCopy.getContentsPath().lastIndexOf("/")+1) + currDtm + ".tmp";
			
			File oldFile = new File(oldFullPath);
			File newFile = new File(newFullPath);
			
			FileInputStream input = new FileInputStream(oldFile);
			FileOutputStream output = new FileOutputStream(newFile);
			
			byte[] buff = new byte[1024];
			int read;
			while((read = input.read(buff)) > 0) {
				output.write(buff, 0, read);
				output.flush();
			}
			input.close();
			output.close();

			serviceCopy.setContentsPath(newFullPath);
		}

		// 복사한 서비스 값 설정
		serviceCopy.setDeptNo(serviceVO.getDeptNo());
		serviceCopy.setUserId(serviceVO.getUserId());
		serviceCopy.setRegId(serviceVO.getRegId());
		serviceCopy.setRegDt(serviceVO.getRegDt());
		serviceCopy.setStatus("000");
		serviceCopy.setWorkStatus("201");
		serviceCopy.setTnm( serviceCopy.getTnm() + (serviceCopy.getTnm().indexOf(" - [copy]")> 0 ? "": " - [copy]") );
		serviceCopy.setEaiCampNo(null);
		// 서비스 등록
		result += rnsServiceDAO.insertServiceInfo(serviceCopy);
		
		// 신규 등록 서비스ID 조회
		int tid = rnsServiceDAO.getCurrServiceTid();
		
		// 첨부파일 목록 읽기
		List<RnsAttachVO> attachList = rnsServiceDAO.getAttachList(serviceVO.getTid());
		if(attachList != null && attachList.size() > 0) {
			for(RnsAttachVO attach : attachList) {
				
				// 첨부파일 복사
				String newAttachPath = "attach/" + System.currentTimeMillis() + "_" + attach.getAttNm().replaceAll(" ", "_");
				String oldFullAttachPath = properties.getProperty("FILE.UPLOAD_PATH") + "/" + attach.getAttFlPath();
				String newFullAttachPath = properties.getProperty("FILE.UPLOAD_PATH") + "/" + newAttachPath;
				
				File oldAttachFile = new File(oldFullAttachPath);
				File newAttachFile = new File(newFullAttachPath);
				
				FileInputStream inputAttach = new FileInputStream(oldAttachFile);
				FileOutputStream outputAttach = new FileOutputStream(newAttachFile);
				
				byte[] buff = new byte[1024];
				int read;
				while((read = inputAttach.read(buff)) > 0) {
					outputAttach.write(buff, 0, read);
					outputAttach.flush();
				}
				inputAttach.close();
				outputAttach.close();
				
				// 복사한 첨부파일 값 설정(설정하지 않은 경우 기존값 사용) 및 등록
				attach.setTid(tid);
				attach.setAttFlPath(newAttachPath);
				attach.setRegId(serviceVO.getRegId());
				attach.setRegDt(serviceVO.getRegDt());
				
				result += rnsServiceDAO.insertAttachInfo(attach);
			}
		}
		
		// WebAgent 읽기
		RnsWebAgentVO webAgent = rnsServiceDAO.getWebAgentInfo(serviceVO.getTid());
		if(webAgent != null) {
			webAgent.setTid(tid);
			webAgent.setRegId(serviceVO.getRegId());
			webAgent.setRegDt(serviceVO.getRegDt());
			
			result += rnsServiceDAO.insertWebAgent(webAgent);
		}
		
		//결재함 등록 
		List<RnsSecuApprovalLineVO> apprLineList = rnsServiceDAO.getRnsApprovalLineList(serviceVO.getTid());
		if(apprLineList != null && apprLineList.size() > 0) {
			for(RnsSecuApprovalLineVO apprLine:apprLineList) {
				apprLine.setTid(tid);
				apprLine.setApprDt(null);
				apprLine.setUpDt(null);
				apprLine.setUpId(null);
				apprLine.setRsltCd("000");
				apprLine.setRejectCd(null);
				apprLine.setRegId(serviceCopy.getRegId());
				apprLine.setRegDt(serviceCopy.getRegDt());
				result += rnsServiceDAO.insertRnsApprovalLine(apprLine);
			}
		}

		return result;
	}
	
	@Override
	public RnsWebAgentVO getWebAgentInfo(int tid) throws Exception {
		return rnsServiceDAO.getWebAgentInfo(tid);
	}
	
	@Override
	public List<RnsServiceVO> getServiceTestList(RnsServiceVO serviceVO) throws Exception {
		return rnsServiceDAO.getServiceTestList(serviceVO);
	}

	@Override
	public List<RnsRecipientInfoVO> getServiceTestResultList(RnsRecipientInfoVO recipientInfoVO) throws Exception {
		return rnsServiceDAO.getServiceTestResultList(recipientInfoVO);
	}

	@Override
	public List<RnsSecuApprovalLineVO> getRnsApprovalLineList(int tid) throws Exception {
		return rnsServiceDAO.getRnsApprovalLineList(tid);
	}

	@Override
	public int insertRnsApprovalLine(RnsSecuApprovalLineVO rnsApprovalLineVO) throws Exception {
		return rnsServiceDAO.insertRnsApprovalLine(rnsApprovalLineVO);
	}

	@Override
	public int updateRnsApprovalLine(RnsSecuApprovalLineVO rnsApprovalLineVO) throws Exception {
		return rnsServiceDAO.updateRnsApprovalLine(rnsApprovalLineVO);
	}

	@Override
	public int deleteRnsApprovalLine(int tid) throws Exception {
		return rnsServiceDAO.deleteRnsApprovalLine(tid);
	}
	
	@Override
	public int updateRnsServiceAprStep(RnsSecuApprovalLineVO rnsApprovalLineVO) throws Exception {
		int result = 0;		
		// 결재승인
		if("002".equals(rnsApprovalLineVO.getRsltCd())) {
			// 발송결재라인 상태수정
			result += rnsServiceDAO.updateRnsServiceAprStep(rnsApprovalLineVO);
			
			List<RnsSecuApprovalLineVO>  nextVO = rnsServiceDAO.nextRnsAprStep(rnsApprovalLineVO);
			
			if(nextVO != null && nextVO.size() > 0) {//다음 승인 처리 
				rnsApprovalLineVO.setApprStep(nextVO.get(0).getApprStep());
				rnsApprovalLineVO.setRsltCd("001"); 
				rnsApprovalLineVO.setApprDt("");
				result += rnsServiceDAO.updateRnsServiceAprStep(rnsApprovalLineVO);
				
			} 
			
			//마지막 승인 처리 
			RnsServiceVO serviceVO = new RnsServiceVO();
			serviceVO.setTid(rnsApprovalLineVO.getTid());
			serviceVO.setWorkStatus("204");	// 발송상태:결재완료
			result += rnsServiceDAO.updateRnsServiceWorkStatus(serviceVO);
		}
		// 결재반려
		if("003".equals(rnsApprovalLineVO.getRsltCd())) {
			
			List<RnsSecuApprovalLineVO>  nowVO = rnsServiceDAO.nowRnsAprStep(rnsApprovalLineVO);
			// 발송결재라인 상태수정
			result += rnsServiceDAO.updateRnsServiceAprStep(rnsApprovalLineVO);
			
			RnsServiceVO serviceVO = new RnsServiceVO();
			serviceVO.setTid(rnsApprovalLineVO.getTid());
			serviceVO.setWorkStatus("203");	// 발송상태:결재반려
			result += rnsServiceDAO.updateRnsServiceWorkStatus(serviceVO);
		}
		
		return result;
	}
	
	@Override
	public int updateRnsServiceAprStepNext(RnsSecuApprovalLineVO rnsApprovalLineVO) throws Exception {
		return rnsServiceDAO.updateRnsServiceAprStepNext(rnsApprovalLineVO);
	}

	@Override
	public int updateRnsServiceWorkStatus(RnsServiceVO rnsServiceVO) throws Exception {
		return rnsServiceDAO.updateRnsServiceWorkStatus(rnsServiceVO);
	}
	
	@Override
	public int updateSubmitApproval(RnsServiceVO rnsServiceVO) throws Exception {
		int result = 0;
		result += rnsServiceDAO.updateSubmitApproval(rnsServiceVO);
		result += rnsServiceDAO.updateSubmitApprovalLine(rnsServiceVO);
		return result;
	}
	
	@Override
	public int updateSubmitApprovalLine(RnsServiceVO rnsServiceVO) throws Exception {
		return rnsServiceDAO.updateSubmitApprovalLine(rnsServiceVO);
	}
	
	@Override
	public String getFirstApprUserId(int tid) throws Exception {
		return rnsServiceDAO.getFirstApprUserId(tid);
	}
	
	@Override
	public int cancelRnsApproval(RnsServiceVO rnsServiceVO) throws Exception {
		int result = 0;
		if(!StringUtil.isNull(rnsServiceVO.getTids())) {
			String[] tid = rnsServiceVO.getTids().split(",");
			
			for(int i=0;i<tid.length;i++) {
				RnsServiceVO sevice = new RnsServiceVO();
				sevice.setTid(Integer.parseInt(tid[i]));
				sevice.setStatus(rnsServiceVO.getStatus());
				sevice.setUpId(rnsServiceVO.getUpId());
				sevice.setUpDt(rnsServiceVO.getUpDt());
				sevice.setWorkStatus("201");
				
				result += rnsServiceDAO.updateRnsServiceWorkStatus(sevice);
				// 결재라인 의  RSLT = 000 으로 변경 
				RnsSecuApprovalLineVO secuapprovalLineVO = new RnsSecuApprovalLineVO();
				secuapprovalLineVO.setApprDt(sevice.getUpDt());
				secuapprovalLineVO.setRsltCd("000");
				secuapprovalLineVO.setRejectCd(null);
				secuapprovalLineVO.setUpId(sevice.getUpId());
				secuapprovalLineVO.setUpDt(sevice.getUpDt());
				secuapprovalLineVO.setTid(sevice.getTid());
				
				result += rnsServiceDAO.updateRnsApprovalLine(secuapprovalLineVO);
			}
		}
	
		return result;
	}
	
	@Override
	public int getCountRequestKey(String requestKey) throws Exception{
		return rnsServiceDAO.getCountRequestKey(requestKey);
	}
}
