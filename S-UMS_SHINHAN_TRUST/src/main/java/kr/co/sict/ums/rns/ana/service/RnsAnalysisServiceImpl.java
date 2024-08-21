/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.22
 * 설명 : RNS 통계 구현
 */
package kr.co.sict.ums.rns.ana.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.com.vo.CodeVO;
import kr.co.sict.ums.rns.ana.dao.RnsAnalysisDAO;
import kr.co.sict.ums.rns.ana.dao.RnsAnalysisMapper;
import kr.co.sict.ums.rns.ana.vo.RnsAnaMonthVO;
import kr.co.sict.ums.rns.ana.vo.RnsAnaServiceVO;
import kr.co.sict.ums.rns.ana.vo.RnsAnaDomainVO;
import kr.co.sict.ums.rns.ana.vo.RnsAnaMailSendResultVO; 
import kr.co.sict.ums.rns.ana.vo.RnsAnaDetailLogVO;

  
@Service
public class RnsAnalysisServiceImpl implements RnsAnalysisService {
	
	@Autowired
	private RnsAnalysisDAO rnsAnalysisDAO;

	@Override
	public List<RnsAnaMonthVO> getMonthList(RnsAnaMonthVO rnsAnaMonthVO) throws Exception {
		return rnsAnalysisDAO.getMonthList(rnsAnaMonthVO);
	} 

	@Override
	public List<RnsAnaServiceVO> getServiceList(RnsAnaServiceVO rnsAnaServiceVO) throws Exception {
		return rnsAnalysisDAO.getServiceList(rnsAnaServiceVO);
	} 

	@Override
	public List<RnsAnaDomainVO> getDomainList(RnsAnaDomainVO rnsAnaDomainVO) throws Exception {
		return rnsAnalysisDAO.getDomainList(rnsAnaDomainVO);
	} 

	@Override
	public List<RnsAnaDetailLogVO> getReceiverList(RnsAnaDetailLogVO rnsAnaDetailLogVO) throws Exception {
		return rnsAnalysisDAO.getReceiverList(rnsAnaDetailLogVO);
	} 

	@Override
	public List<RnsAnaDetailLogVO> getMailList(RnsAnaDetailLogVO rnsAnaDetailLogVO) throws Exception {
		return rnsAnalysisDAO.getMailList(rnsAnaDetailLogVO);
	} 

	@Override
	public List<RnsAnaDetailLogVO> getDetailLogList(RnsAnaDetailLogVO rnsAnaDetailLogVO) throws Exception {
		return rnsAnalysisDAO.getDetailLogList(rnsAnaDetailLogVO);
	} 
	
	@Override
	public List<RnsAnaMailSendResultVO> getMailSendResultList(RnsAnaMailSendResultVO rnsAnaMailSendResultVO) throws Exception {
		return rnsAnalysisDAO.getMailSendResultList(rnsAnaMailSendResultVO);
	} 
	
	@Override
	public List<CodeVO> getServiceCodeList() throws Exception {
		return rnsAnalysisDAO.getServiceCodeList();
	}
	
	@Override
	public List<String> getSenderList() throws Exception {
		return rnsAnalysisDAO.getSenderList();
	}		

	@Override
	public List<CodeVO> getRcodeList(CodeVO codeVO) throws Exception {
		return rnsAnalysisDAO.getRcodeList(codeVO);
	}	

}
