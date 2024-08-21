/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.06
 * 설명 : 메인화면 처리
 */
package kr.co.sict.ums.main.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mp.util.Code;

import kr.co.sict.ums.main.service.MainService;
import kr.co.sict.ums.sys.acc.vo.SysMenuVO;
import kr.co.sict.ums.sys.log.service.SystemLogService;
import kr.co.sict.ums.sys.log.vo.ActionLogVO;
import kr.co.sict.util.EncryptUtil;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;
import kr.co.sict.ums.sys.acc.vo.ServiceVO;

@Controller
@RequestMapping(value = "/")
public class MainController {
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private MainService mainService;

	@Autowired
	private SystemLogService systemService;

	@Autowired
	private PropertiesUtil properties;
	
	/**
	 * 메인화면
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String goIndex(Model model, HttpServletRequest req, HttpServletResponse res, HttpSession session) {
		logger.debug("## goIndex Start.");

		// 로그인 여부 세션확인
		String userId = (String) session.getAttribute("NEO_USER_ID");		
		if (userId == null || "".equals(userId)) { // 로그인 화면으로 이동
			return "lgn/lgnP";
		} else { 
			return "index";
		}
	}

	/**
	 * 메인화면
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/service")
	public String goService(Model model, HttpServletRequest req, HttpServletResponse res, HttpSession session) {
		logger.debug("## goService Start.");
		String userId = (String) session.getAttribute("NEO_USER_ID");
 
		List<ServiceVO> userServiceList = null;
		ServiceVO userService = new ServiceVO();
		String licenseKey = ""; 
		String decLicenseKey = "";
		String domainKey = ""; 
		try {
			userServiceList = mainService.getUserService(userId);
			if(userServiceList != null) {
				for(int i = 0 ; i < userServiceList.size() ; i ++) {
					userService = userServiceList.get(i);
					licenseKey  = userService.getLicenseKey();
					/*
					decLicenseKey= EncryptUtil.getJasyptDecryptedString(properties.getProperty("JASYPT.ALGORITHM"), properties.getProperty("LICENSE.KEYSTRING"), licenseKey);
					*/
					decLicenseKey= EncryptUtil.getJasyptDecryptedUnFixString(properties.getProperty("JASYPT.ALGORITHM"), properties.getProperty("LICENSE.KEYSTRING"), licenseKey);
					
					domainKey = userService.getCustDomain()  + "+" +  userService.getServiceNm();
					if(!decLicenseKey.substring(0, domainKey.length()).equals(domainKey)) {
						userServiceList.get(i).setPayYn(0);
					} else {
						decLicenseKey = decLicenseKey.replace(domainKey, "");
						
						SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
						Date expireDate = null;
						try {
							expireDate = mSimpleDateFormat.parse(StringUtil.getFDate(decLicenseKey.substring(1, 9), Code.DT_FMT2));
							Date currentDate = new Date();
							int compare = expireDate.compareTo( currentDate );  
							if(compare >  0  ) {
								userServiceList.get(i).setPayYn(1);
							} else {
								userServiceList.get(i).setPayYn(0);
							}  
						} catch (Exception e) {
							logger.error("mainService.getUserService error[Expire Date illegal] = " + e);
							userServiceList.get(i).setPayYn(0);
						}
					} 
				}
			}
				
		} catch (Exception e) {
			logger.error("mainService.getUserService error[C009] = " + e);
		} 
		
		model.addAttribute("userServiceList", userServiceList);
		
		return "service";
	}

	/**
	 * EMS 메인화면
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/ems/index")
	public String goEmsMain(Model model, HttpServletRequest req, HttpServletResponse res, HttpSession session) {
		logger.debug("## goEmsMain Start.");

		// 기본 접속 화면 설정(사용자 서비스별 메뉴 권한 확인)
		SysMenuVO menuInfo = null;
		
		try {
			SysMenuVO menu = new SysMenuVO();
			menu.setUserId((String) session.getAttribute("NEO_USER_ID"));
			menu.setServiceGb(10);
			menuInfo = mainService.getServiceUserMenu(menu);
		} catch (Exception e) {
			logger.error("mainService.getServiceUserMenu error = " + e);
		}

		if (menuInfo == null) {
			// 기본 접속 메뉴가 없을 경우
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Failure");
			logVO.setContent("EMS");
			logVO.setContentPath("EMS");
			insertServiceActionLog(req, session, logVO);

			return "err/access";
		} else {
			// 기본 접속 메뉴가 있는 경우
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContent("EMS");
			logVO.setContentPath("EMS");
			insertServiceActionLog(req, session, logVO);

			model.addAttribute("menuInfo", menuInfo);
			return "ems/index";
		}
	}

	/**
	 * RNS 메인화면
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/rns/index")
	public String goRnsMain(Model model, HttpServletRequest req, HttpServletResponse res, HttpSession session) {
		logger.debug("## goRnsMain Start.");

		// 기본 접속 화면 설정(사용자 서비스별 메뉴 권한 확인)
		SysMenuVO menuInfo = null;
		
		try {
			SysMenuVO menu = new SysMenuVO();
			menu.setUserId((String) session.getAttribute("NEO_USER_ID"));
			menu.setServiceGb(20);
			menuInfo = mainService.getServiceUserMenu(menu);
		} catch (Exception e) {
			logger.error("mainService.getServiceUserMenu error = " + e);
		}

		if (menuInfo == null) {
			// 기본 접속 메뉴가 없을 경우
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Failure");
			logVO.setContent("RNS");
			logVO.setContentPath("RNS");
			insertServiceActionLog(req, session, logVO);

			return "err/access";
		} else {
			// 기본 접속 메뉴가 있는 경우
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContent("RNS");
			logVO.setContentPath("RNS");
			insertServiceActionLog(req, session, logVO);

			model.addAttribute("menuInfo", menuInfo);
			return "rns/index";
		}
	}
	
	/**
	 * SMS 메인화면
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/sms/index")
	public String goSmsMain(Model model, HttpServletRequest req, HttpServletResponse res, HttpSession session) {
		logger.debug("## goSmsMain Start.");

		// 기본 접속 화면 설정(사용자 서비스별 메뉴 권한 확인)
		SysMenuVO menuInfo = null;
		
		try {
			SysMenuVO menu = new SysMenuVO();
			menu.setUserId((String) session.getAttribute("NEO_USER_ID"));
			menu.setServiceGb(30);
			menuInfo = mainService.getServiceUserMenu(menu);
		} catch (Exception e) {
			logger.error("mainService.getServiceUserMenu error = " + e);
		}

		if (menuInfo == null) {
			// 기본 접속 메뉴가 없을 경우
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Failure");
			logVO.setContent("SMS");
			logVO.setContentPath("SMS");
			insertServiceActionLog(req, session, logVO);

			return "err/access";
		} else {
			// 기본 접속 메뉴가 있는 경우
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContent("SMS");
			logVO.setContentPath("SMS");
			insertServiceActionLog(req, session, logVO);

			model.addAttribute("menuInfo", menuInfo);
			return "sms/index";
		}
	}
	
	/**
	 * PUSH 메인화면
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/push/index")
	public String goPushMain(Model model, HttpServletRequest req, HttpServletResponse res, HttpSession session) {
		logger.debug("## goPushMain Start.");
		
		// 기본 접속 화면 설정(사용자 서비스별 메뉴 권한 확인)
		SysMenuVO menuInfo = null;
		
		try {
			SysMenuVO menu = new SysMenuVO();
			menu.setUserId((String) session.getAttribute("NEO_USER_ID"));
			menu.setServiceGb(40);
			menuInfo = mainService.getServiceUserMenu(menu);
		} catch (Exception e) {
			logger.error("mainService.getServiceUserMenu error = " + e);
		}
		
		if (menuInfo == null) {
			// 기본 접속 메뉴가 없을 경우
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Failure");
			logVO.setContent("PUSH");
			logVO.setContentPath("PUSH");
			insertServiceActionLog(req, session, logVO);
			
			return "err/access";
		} else {
			// 기본 접속 메뉴가 있는 경우
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContent("PUSH");
			logVO.setContentPath("PUSH");
			insertServiceActionLog(req, session, logVO);
			
			model.addAttribute("menuInfo", menuInfo);
			return "push/index";
		}
	}
	
	/**
	 * 공통설정 메인화면
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/sys/index")
	public String goSysMain(Model model, HttpServletRequest req, HttpServletResponse res, HttpSession session) {
		logger.debug("## goSysMain Start.");

		// 기본 접속 화면 설정(사용자 서비스별 메뉴 권한 확인)
		SysMenuVO menuInfo = null;
		
		try {
			SysMenuVO menu = new SysMenuVO();
			menu.setUserId((String) session.getAttribute("NEO_USER_ID"));
			menu.setServiceGb(99);
			menuInfo = mainService.getServiceUserMenu(menu);
		} catch (Exception e) {
			logger.error("mainService.getServiceUserMenu error = " + e);
		}

		if (menuInfo == null) {
			// 기본 접속 메뉴가 없을 경우
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Failure");
			logVO.setContent("SYS");
			logVO.setContentPath("SYS");
			insertServiceActionLog(req, session, logVO);

			return "err/access";
		} else {
			// 기본 접속 메뉴가 있는 경우
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContent("SYS");
			logVO.setContentPath("SYS");
			insertServiceActionLog(req, session, logVO);

			model.addAttribute("menuInfo", menuInfo);
			return "sys/index";
		}
	}

	public void insertServiceActionLog(HttpServletRequest req, HttpSession session, ActionLogVO actionLogVO) {
		if ("Success".equals(actionLogVO.getStatusGb())) {
			actionLogVO.setContentType("003"); // 003:SERVICE:서비스접근
			actionLogVO.setExtrYn("N");
			actionLogVO.setMobilYn("N");
		} else {
			actionLogVO.setStatusGb("Failure");
			actionLogVO.setContentType("003");
			actionLogVO.setMessage("기본 접속 메뉴가 없음.");
			actionLogVO.setExtrYn("N");
			actionLogVO.setMobilYn("N");
		}

		try {
			systemService.insertActionLog(req, session, actionLogVO);
		} catch (Exception e) {
			logger.error("systemService.insertActionLog error = " + e);
		}
	} 
 
}
