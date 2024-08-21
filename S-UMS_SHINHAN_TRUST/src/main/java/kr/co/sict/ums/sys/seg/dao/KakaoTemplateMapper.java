/**
 * 작성자 : 김준희
 * 작성일시 : 2022.02.24
 * 설명 : 카카오템플릿관리 매퍼
 */
package kr.co.sict.ums.sys.seg.dao;

import java.util.List;

import kr.co.sict.ums.sys.seg.vo.KakaoTemplateVO;

public interface KakaoTemplateMapper {
	
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
	 * 카카오 알림톡 템플릿 코드 갯수 
	 * @param kakaoTemplateVO
	 * @return
	 * @throws Exception
	 */
	public int getKakaoTemplateCodeCount(String tempCd) throws Exception;
	
	/**
	 * 카카오알림톡 API 템플릿 머지 정보 조회
	 * @param kakaoTemplateVO
	 * @return
	 * @throws Exception
	 */
	public List<KakaoTemplateVO> getApiKakaoTemplateMergeList(KakaoTemplateVO kakaoTemplateVO) throws Exception;
	
	/**
	 * 카카오알림톡 API 템플릿 머지 정보 등록 
	 * @param kakaoTemplateVO
	 * @return
	 * @throws Exception
	 */
	public int insertApiKakaoTemplateMerge(KakaoTemplateVO kakaoTemplateVO) throws Exception;

	/**
	 * 카카오알림톡 API 템플릿 머지 정보 삭제
	 * @param kakaoTemplateVO
	 * @return
	 * @throws Exception
	 */
	public int deleteApiKakaoTemplateMerge(KakaoTemplateVO kakaoTemplateVO) throws Exception;
	
	/**
	 * 카카오알림톡 API 템플릿 MAPP 리스트 조회
	 * @param String
	 * @return
	 * @throws Exception
	 */
	public List<KakaoTemplateVO> getApiKakaoTemplateMappList(String mappTempCd) throws Exception;
}
