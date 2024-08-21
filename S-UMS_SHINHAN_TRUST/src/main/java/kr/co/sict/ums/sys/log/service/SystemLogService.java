/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 시스템 서비스 인터페이스==>시스템 로그 관리 서비스 인터페이스
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : 패키지명수정 및 sys관련 분리에 의한 소스 변경  kr.co.sict.ums.sys.service ==> kr.co.sict.ums.sys.log.service
 *                로그관련  외의 함수 및 Import 제거 
 */
package kr.co.sict.ums.sys.log.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import kr.co.sict.ums.sys.log.vo.ActionLogVO;
import kr.co.sict.ums.sys.log.vo.LoginHistVO;

@Service
public interface SystemLogService {
 
	/**
	 * 사용자 로그인 이력 조회
	 * @param loginHistVO
	 * @return
	 * @throws Exception
	 */
	public List<LoginHistVO> getLoginHistList(LoginHistVO loginHistVO) throws Exception;
	
	 
	/**
	 * 사용자활동로그 이력 조회
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
	
	/**
	 * 사용자활동로그 등록(전달 항목 축소)
	 * @param request
	 * @param session
	 * @param actionLogVO
	 * @return
	 * @throws Exception
	 */
	public int insertActionLog(HttpServletRequest request, HttpSession session, ActionLogVO actionLogVO) throws Exception;
}
