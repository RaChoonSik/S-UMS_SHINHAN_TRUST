/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.08
 * 설명 : 도메인 정보 관리
 */
package kr.co.sict.ums.sys.rns.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.sys.rns.dao.RnsStandardDAO;
import kr.co.sict.ums.sys.rns.vo.DomainInfoVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.StringUtil;

@Service
public class RnsStandardServiceImpl implements RnsStandardService {
	@Autowired
	private RnsStandardDAO rnsStandardDAO;
 
	@Override
	public List<DomainInfoVO> getDomainList(DomainInfoVO domainInfoVO) throws Exception {
		return rnsStandardDAO.getDomainList(domainInfoVO);
	}
	public DomainInfoVO getDomainInfo(DomainInfoVO domainInfoVO) throws Exception {
		return rnsStandardDAO.getDomainInfo(domainInfoVO);
	}
	@Override
	public int insertDomainInfo(DomainInfoVO domainInfoVO) throws Exception {
		return rnsStandardDAO.insertDomainInfo(domainInfoVO);
	}

	@Override 
	public int updateDomainInfo(DomainInfoVO domainInfoVO) throws Exception {
		return rnsStandardDAO.updateDomainInfo(domainInfoVO);
	}
	
	@Override 
	public int deleteDomainInfo(DomainInfoVO domainInfoVO) throws Exception {
		return rnsStandardDAO.deleteDomainInfo(domainInfoVO);
	}	
}
