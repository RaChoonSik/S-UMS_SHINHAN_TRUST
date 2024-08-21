/**
 * 작성자 : 김준희
 * 작성일시 : 2022.02.24
 * 설명 : 카카오템플릿관리 서비스 인터페이스
 */
package kr.co.sict.ums.sys.seg.service;

import java.util.List;

import org.springframework.stereotype.Service; 

import kr.co.sict.ums.sys.seg.vo.KakaoTemplateVO;

@Service
public interface KakaoTemplateService {

	/**
	 * 카카오 알림톡 템플릿 정보 등록
	 * @param kakaoTemplateVO
	 * @return
	 * @throws Exception
	 */
	public int insertKakaoTemplateInfo(KakaoTemplateVO kakaoTemplateVO) throws Exception;
	
	/**
	 * 카카오 알림톡 템플릿 목록 조회
	 * @param kakaoTemplateVO
	 * @return
	 * @throws Exception
	 */
	public List<KakaoTemplateVO> getKakaoTemplateList(KakaoTemplateVO kakaoTemplateVO) throws Exception;
	
	/**
	 * 카카오 알림톡 템플릿 정보 조회
	 * @param kakaoTemplateVO
	 * @return
	 * @throws Exception
	 */
	public KakaoTemplateVO getKakaoTemplateInfo(String tempCd) throws Exception;
	
	/**
	 * 카카오 알림톡 템플릿 정보 수정
	 * @param kakaoTemplateVO
	 * @return
	 * @throws Exception
	 */
	public int updateKakaoTemplateInfo(KakaoTemplateVO kakaoTemplateVO) throws Exception;
	
	/**
	 * 카카오 알림톡 템플릿 코드 중복 체크
	 * @param kakaoTemplateVO
	 * @return
	 * @throws Exception
	 */
	public int getKakaoTemplateCodeCount(String tempCd) throws Exception;
	
	/**
	 *  카카오알림톡 API 템플릿 머지 정보 조회
	 * @param kakaoTemplateVO
	 * @return 
	 * @throws Exception
	 */
	public List<KakaoTemplateVO> getApiKakaoTemplateMergeList(KakaoTemplateVO kakaoTemplateVO) throws Exception;	
	
	/**
	 *  카카오알림톡 API 템플릿 머지 정보 삭제
	 * @param kakaoTemplateVO
	 * @return
	 * @throws Exception
	 */
	public int deleteApiKakaoTemplateMerge(KakaoTemplateVO kakaoTemplateVO) throws Exception;
		
	/**
	 *  카카오알림톡 API 템플릿 머지 정보 등록 
	 * @param kakaoTemplateVO
	 * @return
	 * @throws Exception
	 */
	public int insertApiKakaoTemplateMerge(KakaoTemplateVO kakaoTemplateVO) throws Exception;
}
