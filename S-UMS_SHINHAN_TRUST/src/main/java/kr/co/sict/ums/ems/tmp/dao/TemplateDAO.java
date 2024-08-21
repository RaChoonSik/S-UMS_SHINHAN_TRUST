/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.26
 * 설명 : 템플릿관리 데이터 처리
 */
package kr.co.sict.ums.ems.tmp.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.ems.tmp.vo.TemplateVO;

@Repository
public class TemplateDAO implements TemplateMapper {
	@Autowired
	private SqlSession sqlSessionEms;

	@Override
	public List<TemplateVO> getTemplateList(TemplateVO templateVO) throws Exception {
		return sqlSessionEms.getMapper(TemplateMapper.class).getTemplateList(templateVO);
	}

	@Override
	public TemplateVO getTemplateInfo(TemplateVO templateVO) throws Exception {
		return sqlSessionEms.getMapper(TemplateMapper.class).getTemplateInfo(templateVO);
	}

	@Override
	public int insertTemplateInfo(TemplateVO templateVO) throws Exception {
		return sqlSessionEms.getMapper(TemplateMapper.class).insertTemplateInfo(templateVO);
	}

	@Override
	public int updateTemplateInfo(TemplateVO templateVO) throws Exception {
		return sqlSessionEms.getMapper(TemplateMapper.class).updateTemplateInfo(templateVO);
	}

}
