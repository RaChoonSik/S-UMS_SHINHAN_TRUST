/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.26
 * 설명 : 템플릿관리 서비스 인터페이스
 */
package kr.co.sict.ums.ems.tmp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.sict.ums.ems.tmp.vo.TemplateVO;

@Service
public interface TemplateService {
	/**
	 * 템플릿 목록 조회
	 * @param templateVO
	 * @return
	 * @throws Exception
	 */
	public List<TemplateVO> getTemplateList(TemplateVO templateVO) throws Exception;
	
	/**
	 * 템플릿 정보 조회
	 * @param templateVO
	 * @return
	 * @throws Exception
	 */
	public TemplateVO getTemplateInfo(TemplateVO templateVO) throws Exception;
	
	/**
	 * 템플릿 정보 등록
	 * @param templateVO
	 * @return
	 * @throws Exception
	 */
	public int insertTemplateInfo(TemplateVO templateVO) throws Exception;
	
	/**
	 * 템플릿 정보 수정
	 * @param templateVO
	 * @return
	 * @throws Exception
	 */
	public int updateTemplateInfo(TemplateVO templateVO) throws Exception;
}
