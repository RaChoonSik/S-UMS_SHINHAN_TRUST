/**
 * 작성자 : 김준희
 * 작성일시 : 2022.02.24
 * 설명 : 카카오템플릿관리  데이터 처리
 */
package kr.co.sict.ums.sys.seg.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.sys.seg.vo.KakaoTemplateVO;

@Repository
public class KakaoTemplateDAO implements KakaoTemplateMapper {
	@Autowired
	private SqlSession sqlSessionEms;
	
	@Override
	public List<KakaoTemplateVO> getKakaoTemplateList(KakaoTemplateVO kakaoTemplateVO) throws Exception {
		return sqlSessionEms.getMapper(KakaoTemplateMapper.class).getKakaoTemplateList(kakaoTemplateVO);
	}
	
	@Override
	public int insertKakaoTemplateInfo(KakaoTemplateVO kakaoTemplateVO) throws Exception {
		return sqlSessionEms.getMapper(KakaoTemplateMapper.class).insertKakaoTemplateInfo(kakaoTemplateVO);
	}
	
	@Override
	public KakaoTemplateVO getKakaoTemplateInfo(String tempCd) throws Exception {
		return sqlSessionEms.getMapper(KakaoTemplateMapper.class).getKakaoTemplateInfo(tempCd);
	}
	
	@Override
	public int updateKakaoTemplateInfo(KakaoTemplateVO kakaoTemplateVO) throws Exception {
		return sqlSessionEms.getMapper(KakaoTemplateMapper.class).updateKakaoTemplateInfo(kakaoTemplateVO);
	}
	
	@Override
	public int getKakaoTemplateCodeCount(String tempCd) throws Exception {
		return sqlSessionEms.getMapper(KakaoTemplateMapper.class).getKakaoTemplateCodeCount(tempCd);
	}
	
	@Override
	public List<KakaoTemplateVO> getApiKakaoTemplateMergeList(KakaoTemplateVO kakaoTemplateVO) throws Exception {
		return sqlSessionEms.getMapper(KakaoTemplateMapper.class).getApiKakaoTemplateMergeList(kakaoTemplateVO);
	}
	
	
	@Override
	public int insertApiKakaoTemplateMerge(KakaoTemplateVO kakaoTemplateVO) throws Exception {
		return sqlSessionEms.getMapper(KakaoTemplateMapper.class).insertApiKakaoTemplateMerge(kakaoTemplateVO);
	}
	
	
	@Override
	public int deleteApiKakaoTemplateMerge(KakaoTemplateVO kakaoTemplateVO) throws Exception {
		return sqlSessionEms.getMapper(KakaoTemplateMapper.class).deleteApiKakaoTemplateMerge(kakaoTemplateVO);
	}
	
	@Override
	public List<KakaoTemplateVO> getApiKakaoTemplateMappList(String mappTempCd) throws Exception {
		return sqlSessionEms.getMapper(KakaoTemplateMapper.class).getApiKakaoTemplateMappList(mappTempCd);
	}
}
