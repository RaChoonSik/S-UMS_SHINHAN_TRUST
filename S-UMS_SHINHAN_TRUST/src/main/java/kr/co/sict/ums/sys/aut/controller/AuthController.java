/**
 * 작성자 : 김준희
 * 작성일시 : 2021.08.26
 * 설명 :권한관리 Controller
 */
package kr.co.sict.ums.sys.aut.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.co.sict.ums.com.service.CodeService;
import kr.co.sict.ums.com.vo.CodeVO;

import kr.co.sict.ums.sys.acc.service.AccountService;
import kr.co.sict.ums.sys.aut.service.AuthService;
import kr.co.sict.ums.sys.aut.vo.MenuUserMappVO;
import kr.co.sict.ums.sys.acc.vo.OrganizationVO;
import kr.co.sict.ums.sys.acc.vo.SysMenuVO;
import kr.co.sict.ums.sys.acc.vo.UserOrgVO;
import kr.co.sict.ums.sys.acc.vo.UserProgVO;
import kr.co.sict.ums.sys.acc.vo.UserVO; 
import kr.co.sict.util.Code; 
import kr.co.sict.util.PageUtil;  
import kr.co.sict.util.EncryptUtil;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Controller
@RequestMapping("/sys/aut")
public class AuthController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private AccountService accountService;	
	
	@Autowired
	private AuthService authService;

	@Autowired
	private PropertiesUtil properties;
 
	
	/******************************메뉴권한관리******************************/
	/**
	 * 메뉴 권한 관리 화면을 출력한다.
	 * @param deptVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/menuAuthListP")	
	public String goMenuAuthListP(@ModelAttribute MenuUserMappVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {		
		 
		// 부서 조회
		OrganizationVO orgVO = new OrganizationVO();
		orgVO.setLvlVal("1");
		orgVO.setUseYn("Y");
		List<OrganizationVO> orgList = null;
		try {
			orgList = accountService.getOrgListView(orgVO);

		} catch (Exception e) {
			logger.error("accountService.getOrgListView error = " + e);
		}
		model.addAttribute("orgList", orgList); // 부서(최상위)항목
		return "sys/aut/menuAuthListP";
	}
	 
	
	/**
	 * 공통설정을 제외한 메뉴를 조회한다
	 * @param orgVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getMenuList")
	public ModelAndView getMenuList(@ModelAttribute MenuUserMappVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {		
 
		List<MenuUserMappVO> userMenuList = null; 
		try { 
			userMenuList = authService.getMenuList(searchVO);
		} catch (Exception e) {
			logger.error("authService.getMenuList error = " + e);
		} 
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userMenuList", userMenuList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}
	
	/**
	 * 부서에 소속된 사용자 조회
	 * @param orgVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/userList")
	public ModelAndView getUserList(@ModelAttribute MenuUserMappVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getUserList getOrgCd = " + searchVO.getOrgCd());
		logger.debug("getUserList getUserNm = " + searchVO.getUserNm());
		 
		List<UserVO> userList = new ArrayList<UserVO>();
		UserVO userVO = new UserVO();
		 
 
		userVO.setOrgCd(searchVO.getOrgCd());
		userVO.setUserNm(searchVO.getUserNm());
		userVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		try {
			userList = accountService.getOrgUserList(userVO);
		} catch (Exception e) {
			logger.error("authService.getOrgUserList error = " + e);
		}
		 
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userList", userList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}
	
	/**
	 * 메뉴별 사용자 조회
	 * @param orgVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getMenuUserList")
	public ModelAndView getMenuUserList(@ModelAttribute MenuUserMappVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getMenuUserList getMenuIds = " + searchVO.getMenuIds()); 		
		 
		searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		List<MenuUserMappVO> calcMenuUserList = new ArrayList<MenuUserMappVO>();
		
		String menuIds; 
		String[] arrMenuIds = null; 
		
		if (searchVO.getMenuIds().length() > 1) {
			menuIds = searchVO.getMenuIds();
			menuIds = menuIds.substring(0, menuIds.length() - 1);
			arrMenuIds = menuIds.split(",");
			searchVO.setArrMenuIds(arrMenuIds);
		}
		
		String menuId = "";
		String[][] arrMenuUser = new String [arrMenuIds.length][2];
		
		String curVal = ""; 
		String nextVal = "";
		String equalYn = "Y"; 
		try {
			calcMenuUserList = authService.getMenuUserList(searchVO);
			for (int i = 0; i < arrMenuIds.length ; i++) {
				menuId = arrMenuIds[i];
				arrMenuUser[i][0] = menuId;
				arrMenuUser[i][1] = "";
				for(int j=0 ; j < calcMenuUserList.size(); j ++) {
					curVal = calcMenuUserList.get(j).getMenuId();
					if(menuId.equals(curVal)) {
						arrMenuUser[i][1] = arrMenuUser[i][1] +  calcMenuUserList.get(j).getUserId();
					}
				}
			}
			
			curVal = arrMenuUser[0][1]; 
			nextVal = "";
			equalYn = "Y";
			
			for (int i = 1 ; i <  arrMenuUser.length ; i++) {
				nextVal = arrMenuUser[i][1];				
				if(curVal.equals(nextVal)) {
					if (equalYn.equals("Y")){
						equalYn ="Y";
					} else {
						equalYn ="N";
					}
				} else {
					equalYn ="N";
				}
				
				curVal = nextVal;				
			}
		} catch (Exception e) {
			logger.error("authService.getMenuUserList error = " + e);
		}
		 
		//최종적으로 보낼 사용자 리스트 재쿼리 함 
		try {
			//호출하는 함수 바꿔야함 
			calcMenuUserList = authService.getMenuUserSimple(searchVO);
		} catch (Exception e) {
			logger.error("authService.getMenuUserList error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("menuUserList", calcMenuUserList);
		map.put("enable", equalYn);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}
	
	/**
	 * 메뉴 권한 부여 정보를  수정한다. (삭제 후 등록)
	 * @param deptVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return/
	 */
	@RequestMapping(value="/menuAuthUpdate")
	public ModelAndView updateMenuAuth(@ModelAttribute MenuUserMappVO menuUserMappVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		logger.debug("updateMenuAuth getMenuIds = " + menuUserMappVO.getMenuIds());
		logger.debug("updateMenuAuth getUserIds = " + menuUserMappVO.getUserIds());
 
		String[] arrMenuIds = null;
		String[] arrUserIds = null;
		
		if (menuUserMappVO.getMenuIds() == null || menuUserMappVO.getMenuIds().equals("")) {
			return null; 
		} else {
			arrMenuIds =  menuUserMappVO.getMenuIds().split(",");		
		}
		
		if (menuUserMappVO.getUserIds() != null && !menuUserMappVO.getUserIds().equals("")) {
			arrUserIds =  menuUserMappVO.getUserIds().split(",");		
		}
 
		menuUserMappVO.setRegId((String)session.getAttribute("NEO_USER_ID"));
		menuUserMappVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
 
		 
		int result = 0 ;
		try {
			menuUserMappVO.setArrMenuIds(arrMenuIds);
			result = authService.deleteMenuUserAuth(menuUserMappVO);
			logger.info("autService.deleteMenuUserAuth count= " + result);
			if (result >= 0  ) {
				if (  arrUserIds == null || arrUserIds.length == 0 ) {
					logger.info("autService.updateMenuAuth No Add User!");
				}
				for (int i = 0; i < arrUserIds.length; i++) {
					menuUserMappVO.setUserId(arrUserIds[i]);
					for (int j = 0; j < arrMenuIds.length; j++) {
						menuUserMappVO.setMenuId(arrMenuIds[j]);
						try {						
							if (result > -1 ) {
								result = authService.insertUserAuth(menuUserMappVO);
								if(result < 0 ) {
									logger.error("autService.updateMenuAuth [ins]error = userId : " + arrUserIds[i] + "/ menuId" + arrMenuIds[j] );
								}
							} else {
								logger.error("autService.updateMenuAuth [del]error = userId : " + arrUserIds[i] + "/ menuId" + arrMenuIds[j] );
							} 
						} catch (Exception e) {
							logger.error("autService.updateMenuAuth error = " + e);
						}
					}
				}
			} 
		} catch (Exception e) {
			logger.error("autService.deleteMenuAuth error = " + e);
		}
	 
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(result > 0) {
			map.put("result","Success");
		} else {
			map.put("result","Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 메뉴 권한 부여 정보를 삭제한다
	 * @param menuUserMappVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */		
	@RequestMapping(value="/menuAuthDelete")
	public ModelAndView deleteMenuAuth(@ModelAttribute MenuUserMappVO menuUserMappVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("deleteMenuAuth getMenuIds = " + menuUserMappVO.getMenuIds());
		logger.debug("deleteMenuAuth getUserIds = " + menuUserMappVO.getUserIds());
 
		 
		if (menuUserMappVO.getMenuIds() == null || menuUserMappVO.getMenuIds().equals("")) {
			return null; 
		}
		
		if (menuUserMappVO.getUserIds() == null || menuUserMappVO.getUserIds().equals("")) {
			return null; 
		}
		
		menuUserMappVO.setRegId((String)session.getAttribute("NEO_USER_ID"));
		menuUserMappVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
	
		String menuIds;
		String userIds; 
		String[] arrMenuIds = null;
		String[] arrUserIds = null;
		
		if (menuUserMappVO.getMenuIds().length() > 1) {
			menuIds = menuUserMappVO.getMenuIds();
			menuIds = menuIds.substring(0, menuIds.length() - 1);
			arrMenuIds = menuIds.split(",");			
		}
		
		if (menuUserMappVO.getUserIds().length() > 1) {
			userIds = menuUserMappVO.getUserIds();
			userIds = userIds.substring(0, userIds.length() - 1);
			arrUserIds = userIds.split(",");
		} 
		 
		int result = 0 ;
		try {
			for (int i = 0; i < arrUserIds.length; i++) {
				menuUserMappVO.setUserId(arrUserIds[i]);
				for (int j = 0; j < arrMenuIds.length; j++) {
					menuUserMappVO.setMenuId(arrMenuIds[j]);
					try {
						result = authService.deleteMenuUserAuth(menuUserMappVO);
						if (result < 0 ) {
							logger.error("autService.deleteMenuAuth [del]error = userId : " + arrUserIds[i] + "/ menuId" + arrMenuIds[j] );
						} 
					} catch (Exception e) {
						logger.error("autService.deleteMenuAuth error = " + e);
					}
				}
				
			}			
		} catch (Exception e) {
			logger.error("autService.deleteMenuAuth error = " + e);
		}
	 
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(result > 0) {
			map.put("result","Success");
		} else {
			map.put("result","Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	} 	
	
	/******************************사용자권한관리******************************/
	/**
	 * 사용자 권한 관리 화면을 출력한다.
	 * @param deptVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/userAuthListP")	
	public String goUserAuthListP(@ModelAttribute MenuUserMappVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {		
		
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		logger.debug("goUserAuthListP getUilang = " + searchVO.getUilang());
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		searchVO.setPage(page);	
		
		// 사용자 상태 코드 목록을 조회한다.
		CodeVO status = new CodeVO();
		status.setUilang((String)session.getAttribute("NEO_UILANG"));
		status.setCdGrp("C010");
		status.setUseYn("Y");
		
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(status);
		} catch(Exception e) {
			logger.error("codeService.getCodeList error[C010] = " + e);
		} 

		// 코드그룹목록(코드성) 조회 --  사용자 그룹 
		CodeVO dept = new CodeVO();
		dept.setUilang(searchVO.getUilang());
		dept.setStatus("000");
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(dept);
		} catch(Exception e) {
			logger.error("codeService.getDeptList[DEPT]error = " + e);
		}
	 
		// model에 코드 목록 추가
		model.addAttribute("searchVO", searchVO);		// 검색 항목
		model.addAttribute("statusList", statusList);	// 사용자 상태 코드 목록
		model.addAttribute("deptList", deptList);		// 사용자 그룹  코드 목록
		
		return "sys/aut/userAuthListP";
	}
	
	/**
	 * 사용자 권한 목록을 조회한다.
	 * @param menuUserMappVO
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/userAuthList")
	public String goUserAuthList(@ModelAttribute MenuUserMappVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {		
		logger.debug("goUserAuthList getSearchUserId = " + searchVO.getSearchUserId());
		logger.debug("goUserAuthList getSearchUserNm = " + searchVO.getSearchUserNm());
		logger.debug("goUserAuthList getSearchStatus = " + searchVO.getSearchStatus());
		logger.debug("goUserAuthList getSearchDeptNo = " + searchVO.getSearchDeptNo());
		
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		// 사용자 권한 목록 조회
		List<MenuUserMappVO> orgUserAuthList = null;
		List<MenuUserMappVO> userAuthList = new ArrayList<MenuUserMappVO>();
		
		try {
			orgUserAuthList = authService.getUserAuthList(searchVO);
		} catch(Exception e) {
			logger.error("autService.getUserAuthList error = " + e);
		}
		// 등록일시 포맷 수정
		
		if(orgUserAuthList != null) {
			String formatRegDt =""; 
			for(MenuUserMappVO nMenuUserMappVO:orgUserAuthList) {
				if(nMenuUserMappVO.getRegDt() != null && !"".equals(nMenuUserMappVO.getRegDt())) { 
					formatRegDt = StringUtil.getFDate(nMenuUserMappVO.getRegDt(), Code.DT_FMT2);
					logger.error("formatRegDt. : " + formatRegDt);
					formatRegDt = formatRegDt.substring(0,10);
					nMenuUserMappVO.setRegDt(formatRegDt);
				}
				userAuthList.add(nMenuUserMappVO);
			}
		}
		
		if(userAuthList != null && userAuthList.size() > 0) {
			totalCount = userAuthList.get(0).getTotalCount();
		}
		
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("userAuthList", userAuthList);	// 사용자 권한 목록
		model.addAttribute("pageUtil", pageUtil);			// 페이징
		
		return "sys/aut/userAuthList";
	}
 
	/**
	 * 사용자의 권한 목록 조회
	 * @param orgVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getUserAuthInfo")
	public ModelAndView getUserAuthInfo(@ModelAttribute MenuUserMappVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getUserAuthInfo getUserId = " + searchVO.getUserId());

		if (searchVO.getUserId() == null || searchVO.getUserId().equals("")) {
			return null; 
		}

		List<MenuUserMappVO> userAuthList = null;
		try {
			userAuthList = authService.getUserAuthInfo(searchVO);
		} catch (Exception e) {
			logger.error("authService.getUserAuthInfo error = " + e);
		}
		 
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userAuthList", userAuthList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}
	
	/**
	 * 사용자의 권한 목록 조회
	 * @param orgVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getUserMenuInfo")
	public ModelAndView getUserMenuInfo(@ModelAttribute MenuUserMappVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getUserMenuInfo getUserId = " + searchVO.getUserId());

		if (searchVO.getUserId() == null || searchVO.getUserId().equals("")) { 
			searchVO.setUserId((String)session.getAttribute("NEO_USER_ID"));
		}

		List<MenuUserMappVO> userMenuList = null;
		List<MenuUserMappVO> userAuthInfo = null;
		try { 
			userMenuList = authService.getUserMenuInfo(searchVO);
			userAuthInfo = authService.getUserAuthInfo(searchVO);
			
			String menuId = "" ; 
			for(int i =0; i < userMenuList.size() ; i ++) {
				menuId = userMenuList.get(i).getMenuId();
				userMenuList.get(i).setMappYn("N");
				for(int j=0; j < userAuthInfo.size(); j ++) {
					if(menuId.equals(userAuthInfo.get(j).getMenuId())){
						userMenuList.get(i).setMappYn("Y");
					}
				}
			}
		} catch (Exception e) {
			logger.error("authService.getUserMenuInfo error = " + e);
		} 
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userMenuList", userMenuList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}
	
	/**
	 * 사용자의 권한 목록 조회
	 * @param orgVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getUserAccessMenuInfo")
	public ModelAndView getUserAccessMenuInfo(@ModelAttribute MenuUserMappVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getUserAccessMenuInfo getUserId = " + searchVO.getUserId());

		if (searchVO.getUserId() == null || searchVO.getUserId().equals("")) {
			return null; 
		}

		List<MenuUserMappVO> userAuthList = null;
		try {
			userAuthList = authService.getUserAccessMenuInfo(searchVO);
		} catch (Exception e) {
			logger.error("authService.getUserAccessMenuInfo error = " + e);
		} 
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userAuthList", userAuthList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}	
	
	
	/**
	 * 사용자 메뉴권한 정보를 조회하고 화면을 출력한다
	 * @param menuUserMappVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/userAuthUpdateP")
	public String goUserAuthUpdateP(@ModelAttribute MenuUserMappVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {		

		logger.debug("userAuthUpdateP getUserId = " + searchVO.getUserId());
		   
		UserVO searchUserInfo = new UserVO();
		UserVO userInfo = null;
		
		searchUserInfo.setUserId(searchVO.getUserId());
		searchUserInfo.setUilang((String)session.getAttribute("NEO_UILANG"));
		try {
			userInfo = accountService.getUserInfo(searchUserInfo);
		} catch(Exception e) {
			logger.error("accountService.getUserInfo error = " + e);
		}
 
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("userInfo", userInfo);			// 사용자정보 조회 결과 
		
		return "sys/aut/userAuthUpdateP";
	} 
	
	/**
	 * 사용자메뉴정보를 수정한다. (삭제 후 등록)
	 * @param deptVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return/
	 */
	@RequestMapping(value="/userAuthUpdate")
	public ModelAndView updateUserAuth(@ModelAttribute MenuUserMappVO menuUserMappVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		logger.debug("userAuthUpdate getMenuIds = " + menuUserMappVO.getMenuIds());
		logger.debug("userAuthUpdate getUserID = " + menuUserMappVO.getUserId());
 
		menuUserMappVO.setRegId((String)session.getAttribute("NEO_USER_ID"));
		menuUserMappVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
	
		int result = 0 ;
		try {
			result = authService.deleteUserAuth(menuUserMappVO);
		} catch (Exception e) {
			logger.error("autService.deleteUserAuth error = " + e);
		}
	
		String[] menuIds = menuUserMappVO.getMenuIds().split(",");

		for (int i = 0; i < menuIds.length; i++) {
			menuUserMappVO.setMenuId(menuIds[i]);
			try {
				result = authService.insertUserAuth(menuUserMappVO);
			} catch (Exception e) {
				logger.error("autService.insertUserAuth error = " + e);
			}
		} 
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(result > 0) {
			map.put("result","Success");
		} else {
			map.put("result","Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 사용자 권한 정보를 삭제한다
	 * @param menuUserMappVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */		
	@RequestMapping(value="/userAuthDelete")
	public ModelAndView deleteUserAuth(@ModelAttribute MenuUserMappVO menuUserMappVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("deleteUserAuth getUserIds      = " + menuUserMappVO.getUserIds());

		String[] userId = menuUserMappVO.getUserIds().split(",");
		
		int result = 0;
		
		for (int i = 0; i < userId.length; i++) {
			menuUserMappVO.setUserId(userId[i]);		
			try {
				result = authService.deleteUserAuth(menuUserMappVO);
			} catch (Exception e) {
				logger.error("accountService.deleteUserAuth error = " + e);
			} 
		}
 
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(result > 0) {
			map.put("result","Success");
		} else {
			map.put("result","Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	} 
}
