/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 시스템 매퍼==>시스템 로그  매퍼
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : 패키지명수정 및 sys관련 분리에 의한 소스 변경  kr.co.sict.ums.sys.dao ==> kr.co.sict.ums.sys.log.dao
 *                계정관리외의 항목제거 
 */
package kr.co.sict.ums.sys.log.dao;

import java.util.List;

import kr.co.sict.ums.sys.log.vo.ActionLogVO;
import kr.co.sict.ums.sys.log.vo.LoginHistVO;

public interface SystemLogMapper {
	/**
	 * 사용자 로그인 이력 조회
	 * @param loginHistVO
	 * @return
	 * @throws Exception
	 */
	public List<LoginHistVO> getLoginHistList(LoginHistVO loginHistVO) throws Exception;
	
	/**
	 * 사용자활동로그 조회
	 * @param actionLogVO
	 * @return
	 * @throws Exception
	 */
	public List<ActionLogVO> getActionLogList(ActionLogVO actionLogVO) throws Exception;
		
	/**
	 * 사용자활동로그 등록
	 * @param actionLogVO
	 * @return
	 * @throws Exception
	 */
	public int insertActionLog(ActionLogVO actionLogVO) throws Exception;
}
