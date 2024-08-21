/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.26
 * 설명 : 템플릿관리 서비스 구현
 */
package kr.co.sict.ums.rns.tmp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.rns.tmp.dao.RnsTemplateDAO;
import kr.co.sict.ums.rns.tmp.vo.RnsTemplateVO;

@Service
public class RnsTemplateServiceImpl implements RnsTemplateService {

	@Autowired
	private RnsTemplateDAO rnsTemplateDAO;
	
	@Override
	public List<RnsTemplateVO> getTemplateList(RnsTemplateVO templateVO) throws Exception {
		return rnsTemplateDAO.getTemplateList(templateVO);
	}

	@Override
	public RnsTemplateVO getTemplateInfo(RnsTemplateVO templateVO) throws Exception {
		return rnsTemplateDAO.getTemplateInfo(templateVO);
	}

	@Override
	public int insertTemplateInfo(RnsTemplateVO templateVO) throws Exception {
		return rnsTemplateDAO.insertTemplateInfo(templateVO);
	}

	@Override
	public int updateTemplateInfo(RnsTemplateVO templateVO) throws Exception {
		return rnsTemplateDAO.updateTemplateInfo(templateVO);
	}
	
}
