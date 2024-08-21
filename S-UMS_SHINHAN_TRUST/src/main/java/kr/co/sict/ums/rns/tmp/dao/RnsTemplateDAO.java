/**
 * 작성자 : 김상진
 * 작성일시 : 2021.08.21
 * 설명 : RNS 템플릿관리 데이터 처리
 */
package kr.co.sict.ums.rns.tmp.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.rns.tmp.vo.RnsTemplateVO;

@Repository
public class RnsTemplateDAO implements RnsTemplateMapper {
	@Autowired
	private SqlSession sqlSessionRns;

	@Override
	public List<RnsTemplateVO> getTemplateList(RnsTemplateVO templateVO) throws Exception {
		return sqlSessionRns.getMapper(RnsTemplateMapper.class).getTemplateList(templateVO);
	}

	@Override
	public RnsTemplateVO getTemplateInfo(RnsTemplateVO templateVO) throws Exception {
		return sqlSessionRns.getMapper(RnsTemplateMapper.class).getTemplateInfo(templateVO);
	}

	@Override
	public int insertTemplateInfo(RnsTemplateVO templateVO) throws Exception {
		return sqlSessionRns.getMapper(RnsTemplateMapper.class).insertTemplateInfo(templateVO);
	}

	@Override
	public int updateTemplateInfo(RnsTemplateVO templateVO) throws Exception {
		return sqlSessionRns.getMapper(RnsTemplateMapper.class).updateTemplateInfo(templateVO);
	}

}
