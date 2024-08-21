/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.26
 * 설명 : 템플릿관리 서비스 구현
 */
package kr.co.sict.ums.ems.tmp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.ems.tmp.dao.TemplateDAO;
import kr.co.sict.ums.ems.tmp.vo.TemplateVO;

@Service
public class TemplateServiceImpl implements TemplateService {

	@Autowired
	private TemplateDAO templateDAO;
	
	@Override
	public List<TemplateVO> getTemplateList(TemplateVO templateVO) throws Exception {
		return templateDAO.getTemplateList(templateVO);
	}

	@Override
	public TemplateVO getTemplateInfo(TemplateVO templateVO) throws Exception {
		return templateDAO.getTemplateInfo(templateVO);
	}

	@Override
	public int insertTemplateInfo(TemplateVO templateVO) throws Exception {
		return templateDAO.insertTemplateInfo(templateVO);
	}

	@Override
	public int updateTemplateInfo(TemplateVO templateVO) throws Exception {
		return templateDAO.updateTemplateInfo(templateVO);
	}
	
}
