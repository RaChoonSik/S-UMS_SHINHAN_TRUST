/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 시스템 서비스 구현==>시스템 로그관리 서비스 구현
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : 패키지명수정 및 sys관련 분리에 의한 소스 변경  kr.co.sict.ums.sys.service ==> kr.co.sict.ums.sys.log.service
 *               로그인히스토리 외의 함수 제거  나중에 각종 로그 정보 추가해야함 
 */
package kr.co.sict.ums.sys.log.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.sys.log.dao.SystemLogDAO;
import kr.co.sict.ums.sys.log.vo.ActionLogVO;
import kr.co.sict.ums.sys.log.vo.LoginHistVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.StringUtil;

@Service
public class SystemLogServiceImpl implements SystemLogService {
	@Autowired
	private SystemLogDAO systemLogDAO;
 
	@Override
	public List<LoginHistVO> getLoginHistList(LoginHistVO loginHistVO) throws Exception {
		return systemLogDAO.getLoginHistList(loginHistVO);
	}

	 
	@Override
	public List<ActionLogVO> getActionLogList(ActionLogVO actionLogVO) throws Exception {
		return systemLogDAO.getActionLogList(actionLogVO);
	}
		
	@Override
	public int insertActionLog(ActionLogVO actionLogVO) throws Exception {
		return systemLogDAO.insertActionLog(actionLogVO);
	}

	@Override
	public int insertActionLog(HttpServletRequest request, HttpSession session, ActionLogVO actionLogVO) throws Exception {
		actionLogVO.setLogDt( StringUtil.getDate(Code.TM_YMDHMSM) );		// 로그일시
		actionLogVO.setSessionId( session.getId() );						// 세션ID
		actionLogVO.setIpAddr( request.getRemoteAddr() );					// IP주소
		actionLogVO.setUserId((String)session.getAttribute("NEO_USER_ID"));	// 사용자ID
		actionLogVO.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));	// 사용자그룹
		actionLogVO.setOrgCd((String)session.getAttribute("NEO_ORG_CD"));	// 조직코드

		return systemLogDAO.insertActionLog(actionLogVO);
	}  
}
