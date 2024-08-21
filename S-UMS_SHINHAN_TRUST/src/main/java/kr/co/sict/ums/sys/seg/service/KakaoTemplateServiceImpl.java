/**
 * 작성자 : 김준희
 * 작성일시 : 2022.02.24
 * 설명 : 카카오템플릿관리 서비스 구현
 */
package kr.co.sict.ums.sys.seg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.sys.seg.dao.KakaoTemplateDAO;
import kr.co.sict.ums.sys.seg.vo.KakaoTemplateVO;

@Service
public class KakaoTemplateServiceImpl implements KakaoTemplateService {
	@Autowired
	private KakaoTemplateDAO kakaoTemplateDAO;
	
	@Override
	public int insertKakaoTemplateInfo(KakaoTemplateVO kakaoTemplateVO) throws Exception {
		if (getKakaoTemplateCodeCount(kakaoTemplateVO.getTempCd()) > 0 ) {
			return -2;
		} else {
			return kakaoTemplateDAO.insertKakaoTemplateInfo(kakaoTemplateVO);
		}
		
	}

	@Override
	public KakaoTemplateVO getKakaoTemplateInfo(String tempCd) throws Exception {
		return kakaoTemplateDAO.getKakaoTemplateInfo(tempCd);
	}
	
	@Override
	public int updateKakaoTemplateInfo(KakaoTemplateVO kakaoTemplateVO) throws Exception {
		return kakaoTemplateDAO.updateKakaoTemplateInfo(kakaoTemplateVO);
	}
	
	@Override
	public List<KakaoTemplateVO> getKakaoTemplateList(KakaoTemplateVO kakaoTemplateVO) throws Exception {
		return kakaoTemplateDAO.getKakaoTemplateList(kakaoTemplateVO);
	}
	
	@Override
	public int getKakaoTemplateCodeCount(String tempCd) throws Exception {
		return kakaoTemplateDAO.getKakaoTemplateCodeCount(tempCd);
	}
	
	public List<KakaoTemplateVO> getApiKakaoTemplateMergeList(KakaoTemplateVO kakaoTemplateVO) throws Exception {
		return kakaoTemplateDAO.getApiKakaoTemplateMergeList(kakaoTemplateVO);
	}
	
	@Override
	public int insertApiKakaoTemplateMerge(KakaoTemplateVO kakaoTemplateVO) throws Exception {
		return kakaoTemplateDAO.insertApiKakaoTemplateMerge(kakaoTemplateVO);
	}
	
	@Override
	public int deleteApiKakaoTemplateMerge(KakaoTemplateVO kakaoTemplateVO) throws Exception {
		return kakaoTemplateDAO.deleteApiKakaoTemplateMerge(kakaoTemplateVO);
	}
	

}
