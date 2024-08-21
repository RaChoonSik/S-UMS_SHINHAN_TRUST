/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 시스템 서비스 인터페이스==>사용자 코드 관리 서비스 인터페이스
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : 패키지명수정 및 sys관련 분리에 의한 소스 변경  kr.co.sict.ums.sys.service ==> kr.co.sict.ums.sys.cod.service
 *                로그관련  외의 함수 및 Import 제거 
 */
package kr.co.sict.ums.sys.cod.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.sict.ums.sys.cod.vo.UserCodeVO;
import kr.co.sict.ums.sys.cod.vo.SenderKeyVO;
import kr.co.sict.ums.sys.cod.vo.UserCodeGroupVO;

@Service
public interface UserCodeService {

	/**
	 * 사용자 코드 그룹 목록 조회
	 * @param userCodeGroupVO
	 * @return
	 * @throws Exception
	 */
	public List<UserCodeGroupVO> getUserCodeGroupList(UserCodeGroupVO userCodeGroupVO) throws Exception;
	
	/**
	 * 사용자 코드 그룹 정보 조회
	 * @param userCodeGroupVO
	 * @return
	 * @throws Exception
	 */
	public UserCodeGroupVO getUserCodeGroupInfo(UserCodeGroupVO userCodeGroupVO) throws Exception;
	
	/**
	 * 사용자 코드 그룹 등록
	 * @param userCodeGroupVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int insertUserCodeGroupInfo(UserCodeGroupVO userCodeGroupVO) throws Exception;
	
	/**
	 * 사용자 코드 그룹 수정
	 * @param userCodeGroupVO
	 * @return
	 * @throws Exception
	 */
	public int updateUserCodeGroupInfo(UserCodeGroupVO userCodeGroupVO) throws Exception;
	
	/**
	 * 사용자 코드 그룹 삭제
	 * @param userCodeGroupVO
	 * @return
	 * @throws Exception
	 */
	public int deleteUserCodeGroupInfo(UserCodeGroupVO userCodeGroupVO) throws Exception;
	
	/**
	 * 사용자 코드 목록 조회
	 * @param userCodeVO
	 * @return
	 * @throws Exception
	 */
	public List<UserCodeVO> getUserCodeList(UserCodeVO userCodeVO) throws Exception;
	
	/**
	 * 사용자 코드 정보 조회
	 * @param userCodeVO
	 * @return
	 * @throws Exception
	 */
	public UserCodeVO getUserCodeInfo(UserCodeVO userCodeVO) throws Exception;
	
	/**
	 * 사용자 코드 그룹의 상위코드그룹조회
	 * @param userCodeGroupVO
	 * @return
	 * @throws Exception
	 */
	public String getUserCodeGroupUpCdGrp(UserCodeVO userCodeGroupVO) throws Exception;
	
	/**
	 * 사용자 코드 등록
	 * @param userCodeVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public String insertUserCodeInfo(UserCodeVO userCodeVO) throws Exception;
	
	/**
	 * 사용자 코드 수정
	 * @param userCodeVO
	 * @return
	 * @throws Exception
	 */
	public int updateUserCodeInfo(UserCodeVO userCodeVO) throws Exception;
	
	/**
	 * 사용자 코드 정렬순번 수정
	 * @param userCodeVO
	 * @return
	 * @throws Exception
	 */
	public int updateUserCodeSortNo(UserCodeVO userCodeVO) throws Exception;	
	
	/**
	 * 사용자 코드 삭제
	 * @param userCodeVO
	 * @return
	 * @throws Exception
	 */
	public int deleteUserCodeInfo(UserCodeVO userCodeVO) throws Exception;
	
	/**
	 * 발송키 목록 조회
	 * @param senderKeyVO
	 * @return
	 * @throws Exception
	 */
	public List<SenderKeyVO> getSenderKeyList(SenderKeyVO senderKeyVO) throws Exception;
	
	/**
	 * 발송키 존재여부 조회
	 * @param senderKeyVO
	 * @return
	 * @throws Exception
	 */
	public int getSenderKeyExist(SenderKeyVO senderKeyVO) throws Exception;	
	
	/**
	 * 발송키 정보 조회
	 * @param senderKeyVO
	 * @return
	 * @throws Exception
	 */
	public SenderKeyVO getSenderKeyInfo(SenderKeyVO senderKeyVO) throws Exception;
	
	/**
	 * 발송키 등록
	 * @param userCodeVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager")
	public int insertSenderKeyInfo(SenderKeyVO senderKeyVO) throws Exception;
	
	/**
	 * 발송키 수정
	 * @param userCodeVO
	 * @return
	 * @throws Exception
	 */
	public int updateSenderKeyInfo(SenderKeyVO senderKeyVO) throws Exception;
	
	/**
	 * 발송키 삭제
	 * @param userCodeVO
	 * @return
	 * @throws Exception
	 */
	public int deleteSenderKeyInfo(SenderKeyVO senderKeyVO) throws Exception;
	
}
