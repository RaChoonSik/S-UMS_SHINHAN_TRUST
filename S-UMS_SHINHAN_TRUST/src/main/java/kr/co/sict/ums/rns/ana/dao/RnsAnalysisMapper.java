/**
 * 작성자 : 김상진
 * 작성일시 : 2021.09.01
 * 설명 : RNS 서비스 관리 매퍼
 */
package kr.co.sict.ums.rns.ana.dao;

import java.util.List;

import kr.co.sict.ums.rns.ana.vo.RnsAnaMonthVO;
import kr.co.sict.ums.rns.ana.vo.RnsAnaServiceVO;
import kr.co.sict.ums.rns.ana.vo.RnsAnaDomainVO;
import kr.co.sict.ums.rns.ana.vo.RnsAnaMailSendResultVO; 
import kr.co.sict.ums.com.vo.CodeVO;
import kr.co.sict.ums.rns.ana.vo.RnsAnaDetailLogVO;

public interface RnsAnalysisMapper {
	/**
	 * 월별통계 조회
	 * @return
	 * @throws Exception
	 */
	public List<RnsAnaMonthVO> getMonthList(RnsAnaMonthVO rnsAnaMonthVO) throws Exception;
	
	/**
	 * 서비스별 통계 조회
	 * @return
	 * @throws Exception
	 */
	public List<RnsAnaServiceVO> getServiceList(RnsAnaServiceVO rnsAnaServiceVO) throws Exception;
	
	/**
	 * 도메인별통계 조회
	 * @return
	 * @throws Exception
	 */
	public List<RnsAnaDomainVO> getDomainList(RnsAnaDomainVO rnsAnaDomainVO) throws Exception;
	
	/**
	 * 수신자별 통계 조회
	 * @return
	 * @throws Exception
	 */
	public List<RnsAnaDetailLogVO> getReceiverList(RnsAnaDetailLogVO rnsAnaDetailLogVO) throws Exception;
	
	/**
	 * 메일별 통계 조회
	 * @return
	 * @throws Exception
	 */
	public List<RnsAnaDetailLogVO> getMailList(RnsAnaDetailLogVO rnsAnaDetailLogVO) throws Exception;
	
	/**
	 * 상세로그 조회
	 * @return
	 * @throws Exception
	 */
	public List<RnsAnaDetailLogVO> getDetailLogList(RnsAnaDetailLogVO rnsAnaDetailLogVO) throws Exception;
	
	/**
	 * 메일 발송 상세내역 조회
	 * @return
	 * @throws Exception
	 */
	public List<RnsAnaMailSendResultVO> getMailSendResultList(RnsAnaMailSendResultVO rnsAnaMailSendResultVO) throws Exception;
		
	
	/**
	 * 검색조건 :  서비스리스트 조회
	 * @return
	 * @throws Exception
	 */
	public List<CodeVO> getServiceCodeList() throws Exception; 

	/**
	 * 검색조건 :  발신자리스트 조회
	 * @return
	 * @throws Exception
	 */
	public List<String> getSenderList() throws Exception;		
	
	/**
	 * 검색조건 :  RCODE 리스트 조회
	 * @return
	 * @throws Exception
	 */ 
	public List<CodeVO> getRcodeList(CodeVO codeVO) throws Exception;
 
}
