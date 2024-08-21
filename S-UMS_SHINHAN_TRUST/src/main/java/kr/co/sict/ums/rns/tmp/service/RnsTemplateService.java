/**
 * 작성자 : 김상진
 * 작성일시 : 2021.08.21
 * 설명 : RNS 템플릿관리 서비스 인터페이스
 */
package kr.co.sict.ums.rns.tmp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.sict.ums.rns.tmp.vo.RnsTemplateVO;

@Service
public interface RnsTemplateService {
	/**
	 * RNS 템플릿 목록 조회
	 * @param templateVO
	 * @return
	 * @throws Exception
	 */
	public List<RnsTemplateVO> getTemplateList(RnsTemplateVO templateVO) throws Exception;
	
	/**
	 * RNS 템플릿 정보 조회
	 * @param templateVO
	 * @return
	 * @throws Exception
	 */
	public RnsTemplateVO getTemplateInfo(RnsTemplateVO templateVO) throws Exception;
	
	/**
	 * RNS 템플릿 정보 등록
	 * @param templateVO
	 * @return
	 * @throws Exception
	 */
	public int insertTemplateInfo(RnsTemplateVO templateVO) throws Exception;
	
	/**
	 * RNS 템플릿 정보 수정
	 * @param templateVO
	 * @return
	 * @throws Exception
	 */
	public int updateTemplateInfo(RnsTemplateVO templateVO) throws Exception;
}
