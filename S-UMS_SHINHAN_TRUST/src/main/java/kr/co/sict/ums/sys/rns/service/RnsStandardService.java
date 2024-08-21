/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.08
 * 설명 : 도메인 정보 관리
 */
package kr.co.sict.ums.sys.rns.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.sict.ums.sys.rns.vo.DomainInfoVO;

@Service
public interface RnsStandardService {
 
	/**
	 * 도메인 리스트 조회
	 * @param domainInfoVO
	 * @return
	 * @throws Exception
	 */
	public List<DomainInfoVO> getDomainList(DomainInfoVO domainInfoVO) throws Exception;
	 
	/**
	* 도메인 정보 조회
	* @param domainInfoVO
	* @return
	* @throws Exception
	*/	
	public DomainInfoVO getDomainInfo(DomainInfoVO domainInfoVO) throws Exception;

	/**
	 * 도메인 정보 등록
	 * @param actionLogVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")	
	public int insertDomainInfo(DomainInfoVO domainInfoVO) throws Exception;
	
	/**
	 * 도메인 정보 수정
	 * @param actionLogVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")	
	public int updateDomainInfo(DomainInfoVO domainInfoVO) throws Exception;

	/**
	 * 도메인 정보 삭제
	 * @param actionLogVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")	
	public int deleteDomainInfo(DomainInfoVO domainInfoVO) throws Exception;
 
}
