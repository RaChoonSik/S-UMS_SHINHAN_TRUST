/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 공통 인터셉터 정의(로그인 체크)
 */
package kr.co.sict.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.co.sict.ums.ems.apr.service.SecuApprovalLineService;
import kr.co.sict.ums.main.service.MainService;
import kr.co.sict.ums.sys.acc.vo.SysMenuVO;
import kr.co.sict.ums.sys.log.service.SystemLogService;
import kr.co.sict.ums.sys.log.vo.ActionLogVO;

public class CommonSessionInterceptor extends HandlerInterceptorAdapter {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private SystemLogService systemService;
	
	@Autowired
	private MainService mainService;
	
	@Autowired
	private SecuApprovalLineService apprService;

	@SuppressWarnings("unchecked")
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String contextRoot = request.getContextPath();
		String requestUri = request.getRequestURI();
		
		logger.debug("## preHandle requestUri = " + requestUri);
		
		boolean result = false;
		HttpSession session = request.getSession();
		
		// 사용자 세션 체크
		if(session.getAttribute("NEO_USER_ID") == null || "".equals((String)session.getAttribute("NEO_USER_ID"))) {
			if(requestUri.indexOf("/index.ums") >= 0 || requestUri.indexOf("/service.ums") >= 0) {
				response.sendRedirect(contextRoot + "/lgn/lgnP.ums");
			} else {
				response.sendRedirect(contextRoot + "/lgn/timeout.ums");
			}
			result = false;
		} else {
			// Disable browser caching
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			
			// 메뉴ID/상위메뉴ID 확인
			String pMenuId = (String)request.getParameter("pMenuId");
			String menuId = (String)request.getParameter("menuId");
			if(pMenuId == null || "".equals(pMenuId)) {
				pMenuId = (String)session.getAttribute("NEO_P_MENU_ID");
			}
			if(menuId == null || "".equals(menuId)) {
				menuId = (String)session.getAttribute("NEO_MENU_ID");
			}
			
			// 페이지 권한 확인
			if(!(requestUri.indexOf("index.ums") >= 0 || requestUri.indexOf("service.ums") >= 0)) {		// index.ums, service.ums 페이지는 제외
				SysMenuVO sysMenu = mainService.getSourcePathMenu(requestUri);
				// 시스템메뉴로 등록된 URI인 경우
				if(sysMenu != null) {
					pMenuId = sysMenu.getParentmenuId();
					menuId = sysMenu.getMenuId();
					
					// 사용자 권한 체크
					/*
					 * SysMenuVO menuVO = new SysMenuVO(); 
					 * menuVO.setSourcePath(requestUri);
					 * menuVO.setUserId((String)session.getAttribute("NEO_USER_ID")); 
					 * SysMenuVO userMenu = mainService.getUserMenuAuth(menuVO);
					 */
					//09.23 세션에서 페이지 권한 가져오는것으로 변경
					List<SysMenuVO> menuList = (List<SysMenuVO>) session.getAttribute("NEO_MENU_LIST");
					SysMenuVO userMenuVO = new SysMenuVO();
					boolean hasMenuAuth = false; 
					for (int idx = 0; idx < menuList.size(); idx++) {
						userMenuVO = (SysMenuVO) menuList.get(idx);
						if (requestUri.equals(userMenuVO.getSourcePath())){
							hasMenuAuth = true;
						}
					}
					
					// 페이지권한 O
					if(hasMenuAuth) {
						// 사용자 활동이력 등록(Success)
						ActionLogVO logVO = new ActionLogVO();
						logVO.setStatusGb("Success");
						logVO.setContentType("001"); // 공통코드(C112) = 001:MENU:메뉴접근
						logVO.setContent(menuId);
						logVO.setContentPath(requestUri);
						logVO.setExtrYn("N");
						logVO.setMobilYn("N");
						systemService.insertActionLog(request, session, logVO);
						
					// 페이지권한 X
					} else {
						// 사용자 활동이력 등록(Failure:권한없음)
						ActionLogVO logVO = new ActionLogVO();
						logVO.setStatusGb("Failure");
						logVO.setContentType("001"); // 공통코드(C112) = 001:MENU:메뉴접근
						logVO.setContent(menuId);
						logVO.setContentPath(requestUri);
						logVO.setMessage("메뉴 권한 없음.");
						logVO.setExtrYn("N");
						logVO.setMobilYn("N");
						systemService.insertActionLog(request, session, logVO);
						
						response.sendRedirect(contextRoot + "/err/access.ums");
						result = false;
					}
				}
			}
			
			// 단기메일,정기메일 권한확인(등록/수정:좌측메뉴 외 접근통제:신규발송/일정)
			if(requestUri.indexOf("mailAddP.ums") >= 0 || requestUri.indexOf("taskAddP.ums") >= 0 || requestUri.indexOf("mailUpdateP.ums") >= 0 && requestUri.indexOf("taskUpdateP.ums") >= 0) {
				// 사용자 권한 체크
				SysMenuVO menuVO = new SysMenuVO();
				if(requestUri.indexOf("mailAddP.ums") >= 0 || requestUri.indexOf("mailUpdateP.ums") >= 0) {
					menuVO.setSourcePath("/ems/cam/mailListP.ums");
				} else {
					menuVO.setSourcePath("/ems/cam/taskListP.ums");
				}
				menuVO.setUserId((String)session.getAttribute("NEO_USER_ID"));
				//SysMenuVO userMenu =  mainService.getUserMenuAuth(menuVO);
				//09.23 세션에서 페이지 권한 가져오는것으로 변경
				List<SysMenuVO> menuList = (List<SysMenuVO>) session.getAttribute("NEO_MENU_LIST");
				SysMenuVO userMenuVO = new SysMenuVO();
				boolean hasMenuAuth = false; 
				String menuSoucePath = menuVO.getSourcePath();
				for (int idx = 0; idx < menuList.size(); idx++) {
					userMenuVO = (SysMenuVO) menuList.get(idx);
					if (menuSoucePath.equals(userMenuVO.getSourcePath())){
						hasMenuAuth = true;
					}
				}
				
				// 페이지권한 O
				if(hasMenuAuth) {
					// 사용자 활동이력 등록(Success)
					ActionLogVO logVO = new ActionLogVO();
					logVO.setStatusGb("Success");
					logVO.setContentType("001"); // 공통코드(C112) = 001:MENU:메뉴접근
					logVO.setContent(menuId);
					logVO.setContentPath(requestUri);
					logVO.setExtrYn("N");
					logVO.setMobilYn("N");
					systemService.insertActionLog(request, session, logVO);
					
				// 페이지권한 X
				} else {
					// 사용자 활동이력 등록(Failure:권한없음)
					ActionLogVO logVO = new ActionLogVO();
					logVO.setStatusGb("Failure");
					logVO.setContentType("001"); // 공통코드(C112) = 001:MENU:메뉴접근
					logVO.setContent(menuId);
					logVO.setContentPath(requestUri);
					logVO.setMessage("메뉴 권한 없음.");
					logVO.setExtrYn("N");
					logVO.setMobilYn("N");
					systemService.insertActionLog(request, session, logVO);
					
					response.sendRedirect(contextRoot + "/err/access.ums");
					result = false;
				}
			}
			
			// 메일결재건수 조회
			request.setAttribute("apprMailCnt",apprService.getApprCount((String)session.getAttribute("NEO_USER_ID")));
			
			session.setAttribute("NEO_P_MENU_ID", pMenuId);
			session.setAttribute("NEO_MENU_ID", menuId);
			
			result = true;
		}
		
		return result;
	}

}
