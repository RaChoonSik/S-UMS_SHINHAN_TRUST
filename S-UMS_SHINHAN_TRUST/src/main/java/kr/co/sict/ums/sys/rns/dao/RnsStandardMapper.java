/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.08
 * 설명 : 도메인 정보 관리
 */
package kr.co.sict.ums.sys.rns.dao;

import java.util.List;

import kr.co.sict.ums.sys.rns.vo.DomainInfoVO;

public interface RnsStandardMapper {
	/**
	 * 도메인 목록 조회
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
	 * @param domainInfoVO
	 * @return
	 * @throws Exception
	 */
	public int insertDomainInfo(DomainInfoVO domainInfoVO) throws Exception;
		
	/**
	 * 도메인 정보 수정
	 * @param domainInfoVO
	 * @return
	 * @throws Exception
	 */
	public int updateDomainInfo(DomainInfoVO domainInfoVO) throws Exception;
	
	/**
	 * 도메인 정보 삭제
	 * @param domainInfoVO
	 * @return
	 * @throws Exception
	 */
	public int deleteDomainInfo(DomainInfoVO domainInfoVO) throws Exception;	
}
