/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.06
 * 설명 : 사용자 로그인 Controller
 */
package kr.co.sict.ums.lgn.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.co.sict.ums.lgn.service.LoginService;
import kr.co.sict.ums.lgn.vo.LoginVO;
import kr.co.sict.ums.main.service.MainService;
import kr.co.sict.ums.sys.acc.vo.ServiceVO;
import kr.co.sict.ums.sys.acc.vo.SysMenuVO;
import kr.co.sict.ums.sys.acc.vo.UserVO;
import kr.co.sict.ums.sys.log.service.SystemLogService;
import kr.co.sict.ums.sys.log.vo.ActionLogVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.EncryptAccUtil;
import kr.co.sict.util.EncryptUtil;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Controller
@RequestMapping("/lgn")
public class LoginController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private MainService mainService;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private SystemLogService systemService;
	
	@Autowired
	private PropertiesUtil properties;	
	/**
	 * 로그인 화면을 출력한다.
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/lgnP")
	public String goLogin(Model model, HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("KEYSTRING", properties.getProperty("ACCOUNT.KEYSTRING"));
		logger.debug("## goLogin Form Start");
		
		return "lgn/lgnP";
	}
	/**
	 * 로그인 화면을 출력한다.
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/lgnAdmP")
	public String goLogin2(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		logger.debug("## goLogin Form Start");
		
		return "lgn/lgnP_old";
	}
	
	/**
	 * 사용자 로그인을 처리한다.
	 * @param loginVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	/* IAM 관련 정보 
	오류 :: {"code":"9001","message":"User r00107 is not found with password","data":null}
	정상 :: {
		"access_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX3R5cGUiOiJFTVBMT1lFRSIsInVzZXJfaWQiOjUsInVzZXJfbmFtZSI6IlIwMDEwNyIsInNjb3BlIjpbInJlYWQiXSwicm9sZXMiOiJST0xFX05PVEhJTkciLCJncm91cHMiOm51bGwsImV4cCI6MTY3MDk4NTQwNywidXNlcl9kaXNwbGF5X25hbWUiOiLquYDsg4HtmIQiLCJhdXRob3JpdGllcyI6WyJST0xFX05PVEhJTkciXSwianRpIjoiZjljYjUwZjAtOWNjMy00N2ZiLTg0N2YtODkxYjI4YmE0NjQ0IiwiY2xpZW50X2lkIjoiZDQ5YWE5ZWYtZTA1Yi00ZmQxLTg3YjUtYWJhNGZiOGI3OWY5In0.NOZo-9FJeJYQtI4QeY6PgVgZKsy1uJWsSRoDnIN33RI"
		,"token_type":"bearer"
		,"expires_in":299
		,"scope":"read"
		,"user_type":"EMPLOYEE"
		,"user_id":5
		,"user_name":"R00107"
		,"roles":"ROLE_NOTHING"
		,"groups":null
		,"user_display_name":"김상현"
		,"jti":"f9cb50f0-9cc3-47fb-847f-891b28ba4644"
	}
	prslt : 1 == 기존사용자 , 0 == 신규사용자, -1 == 처리오류 
	*/ 
	
	@RequestMapping(value="/lgn")
	public String loginProcess(@ModelAttribute LoginVO loginVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("## login process Start");
		
		// 아이디, 비밀번호 확인
		logger.debug("loginProcess pUserId = " + loginVO.getpUserId());
		logger.debug("loginProcess pUserPwd = " + loginVO.getpUserPwd());
		
		String authUse = properties.getProperty("IAM.USE_YN");
		String authKey = properties.getProperty("IAM.AUTH_KEY");
		String authUrl = properties.getProperty("IAM.AUTH_URL");
		String strAuthRes = "";  
		
		String cipherUserPwd = loginVO.getpUserPwd();
		String cipherUserId = loginVO.getpUserId();
		try {
			loginVO.setpUserId(EncryptAccUtil.getEncryptedSHA256(cipherUserId, properties.getProperty("ACCOUNT.KEYSTRING")));
			loginVO.setpUserPwd(EncryptAccUtil.getEncryptedSHA256(cipherUserPwd, properties.getProperty("ACCOUNT.KEYSTRING")));
		} catch(Exception e) { 
			logger.error("loginProcess EncryptAccUtil Error = " + e);
		}
		
		logger.debug("loginProcess pUserPwd Dec= " + loginVO.getpUserPwd());

		UserVO userVO = null;
		String userStatus = "999";
		String userId = null;
		String userNm = null;
		
		int prslt = 0;
		
		// 사용자 아이디, 비밀번호 체크
		try {
			userId = loginVO.getpUserId();
			
			if (authUse == null || !"Y".equals(authUse)) {
				
				if( !"admin".equals(userId.toLowerCase())) {
					
					int checkVal = loginService.checkAuthUser(loginVO);
					if (checkVal == 0) {
						model.addAttribute("result","E");
						
						ActionLogVO logVO = new ActionLogVO();
						logVO.setStatusGb("Failure");
						logVO.setContent("Login");
						logVO.setContentPath("/lgn/lgn.ums");
						logVO.setMessage("인증정보 불일치(sms인증)");
						insertLoginActionLog(request, session, logVO);
						return "lgn/lgnP";
					}
				}
				
				String encPasswd = EncryptUtil.getEncryptedSHA256(loginVO.getpUserPwd());
				loginVO.setpUserPwd(encPasswd); 
				userVO = loginService.isValidUser(loginVO);
			} else {
				if( "admin".equals(userId.toLowerCase())) {
					String encPasswd = EncryptUtil.getEncryptedSHA256(loginVO.getpUserPwd());
					loginVO.setpUserPwd(encPasswd); 
					userVO = loginService.isValidUser(loginVO);
				} else {
					// IAM 인증 
					strAuthRes = callRestApiPost(authUrl, authKey, loginVO.getpUserId(), loginVO.getpUserPwd());
					JSONParser parser = new JSONParser();
					JSONObject jsonObj = (JSONObject)parser.parse( strAuthRes );
						
					if( jsonObj.size() <= 3 ) {
							//인증실패 - > 오류메세지 IAM인증에 실패하였습니다.관리자에게 문의하세요.
							model.addAttribute("result","E");
							model.addAttribute("needChange", "E1");
							model.addAttribute("needUserId", "");
							
							ActionLogVO logVO = new ActionLogVO();
							logVO.setStatusGb("Failure");
							logVO.setContent("Login");
							logVO.setContentPath("/lgn/lgn.ums");
							if( null != jsonObj.get("message") && !"".equals(jsonObj.get("message"))) {
								logVO.setMessage("인증정보 불일치(" + jsonObj.get("code") + ":" + jsonObj.get("message") + ")");
							}
							insertLoginActionLog(request, session, logVO);
							return "lgn/lgnP";
					}
					//인증 결과 
					userId = (String) jsonObj.get("user_name");
					userNm = (String) jsonObj.get("user_display_name");
					
					userVO = new UserVO();
					userVO.setUserId(userId);
					userVO.setUserNm(userNm);
					
					//2. 로그인사용자확인 / 있을경우 통과 없을 경우 등록 
					loginService.procRimanUserInsert(userVO);
					prslt = userVO.getPrslt();
					if(  prslt >= 0 ) {  
						loginVO.setpUserId(userId);
						//3. 로그인 계정 정보 조회 
						userVO = loginService.isSSOUser(loginVO);
					}
					else { //사용자 등록or조회실패 - > 오류메세지 : UMS사용자 인증에 실패하였습니다.관리자에게 문의하세요.
						model.addAttribute("result","N");
						model.addAttribute("needChange", "E2");
						model.addAttribute("needUserId", "");
						
						ActionLogVO logVO = new ActionLogVO();
						logVO.setStatusGb("Failure");
						logVO.setContent("Login");
						logVO.setContentPath("/lgn/lgn.ums");
						if( null != jsonObj.get("message") && !"".equals(jsonObj.get("message"))) {
							logVO.setMessage("인증정보 불일치(IAM인증 후처리 프로시져오류)");
						}
						insertLoginActionLog(request, session, logVO);
						return "lgn/lgnP";
					}
				}
			}
		} catch(Exception e) {
			logger.error("loginService.isValidUser Error = " + e);
		}
			
		if(userVO != null ) {
			if(userVO.getStatus() != null && !"".equals(userVO.getStatus())) {
				userStatus = userVO.getStatus();
			}
		}		
		String retUrl = "lgn/lgn"; 
		if("000".equals(userStatus)) { 
			// 세션값 설정
			session.setAttribute("NEO_USER_ID", userVO.getUserId());		// 사용자ID
			session.setAttribute("NEO_USER_NM", userVO.getUserNm());		// 사용자명
			session.setAttribute("NEO_DEPT_NO", userVO.getDeptNo());		// 부서번호
			session.setAttribute("NEO_DEPT_NM", userVO.getDeptNm());		// 부서명
			session.setAttribute("NEO_TZ_CD", userVO.getTzCd());			// 타임존코드
			session.setAttribute("NEO_TZ_TERM", userVO.getTzTerm());		// 타임존시간차
			session.setAttribute("NEO_UILANG", userVO.getUilang());			// 언어권
			session.setAttribute("NEO_CHARSET", userVO.getCharset());		// 문자셋
			session.setAttribute("NEO_FONT", userVO.getFont());				// 폰트
			session.setAttribute("NEO_ORG_CD", userVO.getOrgCd());			// 조직코드
			session.setAttribute("NEO_ORG_NM", userVO.getOrgKorNm());		// 조직명
			session.setAttribute("NEO_JOB_GB", userVO.getJobGb());			// 직책코드
			session.setAttribute("NEO_JOB_NM", userVO.getJobNm());			// 직책명
			session.setAttribute("NEO_LINK", userVO.getLinkService());		// 사용자 링크 서비스
			
			session.setAttribute("NEO_MAIL_FROM_NM", userVO.getMailFromNm());	//발송자명 
			session.setAttribute("NEO_MAIL_FROM_EM", userVO.getMailFromEm());	// 발송자이메일주소
			session.setAttribute("NEO_USER_TEL", userVO.getUserTel());			//연락처
			session.setAttribute("NEO_USER_EM", userVO.getUserEm());			//이메일
			session.setAttribute("NEO_REPLY_TO_EM", userVO.getReplyToEm());		//회신이메일
			session.setAttribute("NEO_RETURN_EM", userVO.getReturnEm());		//return 이메일
			session.setAttribute("NEO_PER_PAGE", StringUtil.setNullToInt(userVO.getPerPage(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")))); // 사용자별 목록 조회 페이지
			
			session.setAttribute("NEO_USE_EMS", "N");			// EMS 사용여부
			session.setAttribute("NEO_USE_RNS", "N");			// RNS 사용여부
			session.setAttribute("NEO_USE_SMS", "N");			// SMS 사용여부
			session.setAttribute("NEO_USE_PUSH", "N");			// PUSH 사용여부
			 
			int [][] arrUserService = checkLicense(userVO.getUserId());
			
			int service = 0;
			int useYn = 0;
			for(int i=0; i < arrUserService.length ; i++) {
				service = arrUserService[i][0] ;
				switch(service) {
				case 10:
					useYn =  arrUserService[i][1] ;
					if (useYn == 1) {
						session.setAttribute("NEO_USE_EMS", "Y");			// EMS 사용여부
					}
					break;
				case 20: 
					useYn =  arrUserService[i][1] ;
					if (useYn == 1) {
						session.setAttribute("NEO_USE_RNS", "Y");			// RMS 사용여부
					}
					break;
				case 30: 
					useYn =  arrUserService[i][1] ;
					if (useYn == 1) {
						session.setAttribute("NEO_USE_SMS", "Y");			// SMS 사용여부
					}
					break;
				case 40: 
					useYn =  arrUserService[i][1] ;
					if (useYn == 1) {
						session.setAttribute("NEO_USE_PUSH", "Y");			// PUSH 사용여부
					}
					break;
				default: 
					break;
				}
			}
			
			if(userVO.getUseSYS() != null && !"".equals(userVO.getUseSYS()) && "1".equals(userVO.getUseSYS())) {
				session.setAttribute("NEO_USE_SYS", "Y");			// 공통설정사용권한여부
			} else {
				session.setAttribute("NEO_USE_SYS", "N");			// 공통설정사용권한여부
			}
			 
			// 관리자 여부
			if(userVO.getDeptNo() == 1) {
				session.setAttribute("NEO_ADMIN_YN", "Y");
			} else {
				session.setAttribute("NEO_ADMIN_YN", "N");
			}
			
			// 사용자 프로그램 사용권한 조회(데이터 등록 환경에 따라 쿼리 변동 가능성 있음)
			// 사용자 메뉴
			List<SysMenuVO> menuList = null;
			try {
				menuList = loginService.getUserMenuList(userVO.getUserId());
			} catch(Exception e) {
				logger.error("loginService.getUserMenuList Error = " + e);
			}
			// 세션에 사용가능 메뉴 목록 저장
			session.setAttribute("NEO_MENU_LIST", menuList);
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContent("Login");
			logVO.setContentPath("/lgn/lgn.ums");
			insertLoginActionLog(request, session, logVO); 
			
			model.addAttribute("result","Y"); 
			
			if ( userVO.getLinkService() > 0 ) {
				try {
					for(int i=0; i < arrUserService.length ; i++) {
						service = arrUserService[i][0] ;
						if (userVO.getLinkService() == service) {
							if (arrUserService[i][1] == 1) {
								if (userVO.getLinkService() == 10) {
									retUrl =  "redirect:/ems/index.ums";
								} else if (userVO.getLinkService() == 20) {
									retUrl =  "redirect:/rns/index.ums";
								}  else if (userVO.getLinkService() == 30) {
									retUrl =  "redirect:/sms/index.ums";
								}  else if (userVO.getLinkService() == 40) {
									retUrl =  "redirect:/push/index.ums";
								}
							}
						} 
					}
					
				} catch(Exception e) {
					logger.error("loginService.getUserMenuList sendRedirect Error = " + e);
				}
			} 
			
			return retUrl;
		} else {
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Failure");
			logVO.setContent("Login");
			logVO.setContentPath("/lgn/lgn.ums");
			insertLoginActionLog(request, session, logVO);
			
			if(userVO != null ) {
				model.addAttribute("result","E");
			} else {
				model.addAttribute("result","N");
			}
			return "lgn/lgnP";
		}
	}
	
	/**
	 * 초기화된 비밀번호 사용자 확인 
	 * 
	 * @param userVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/checkInitPwd")
	public ModelAndView checkInitPwd(@ModelAttribute LoginVO loginVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("checkInitPwd pUserId = " + loginVO.getpUserId());
		logger.debug("checkInitPwd pUserPwd = " + loginVO.getpUserPwd());
		
		//암호화된 내용 복호화  
		String cipherUserPwd = loginVO.getpUserPwd();
		String cipherUserId = loginVO.getpUserId();
		try {
			loginVO.setpUserId(EncryptAccUtil.getEncryptedSHA256(cipherUserId, properties.getProperty("ACCOUNT.KEYSTRING")));
			loginVO.setpUserPwd(EncryptAccUtil.getEncryptedSHA256(cipherUserPwd, properties.getProperty("ACCOUNT.KEYSTRING")));
		} catch(Exception e) { 
			logger.error("checkInitPwd EncryptAccUtil Error = " + e);
		}
		
		String encPasswd = EncryptUtil.getEncryptedSHA256(loginVO.getpUserPwd());
		loginVO.setpUserPwd(encPasswd);
		UserVO userVO = null; 
		String userId = loginVO.getpUserId();
		String authUse = properties.getProperty("IAM.USE_YN");
		
		try {
			if(!"Y".equals(authUse)) {
				userVO = loginService.isValidUser(loginVO);
			} else {
				if( "ADMIN".equals(userId.toUpperCase())) { 
					userVO = loginService.isValidUser(loginVO);
				}
			}
		} catch(Exception e) {
			logger.error("loginService.isValidUser Error = " + e);
		}
		//
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(!"Y".equals(authUse)) {
			if(userVO != null ) {
				map.put("result", "Success");
				if ("Y".equals(userVO.getPwInitYn())) {
					map.put("needChange", "Y");
				} else {
					
					//2단계인증 번호 생성 및 SMS발송
					try {
						loginService.sendSms(userVO);
					} catch (Exception e) {
						logger.error("loginService.sendSms Error = " + e);
					}
					
					map.put("needChange", "N");
					map.put("userTel", userVO.getUserTel());
				}
			} else {
				ActionLogVO logVO = new ActionLogVO();
				logVO.setStatusGb("Failure");
				logVO.setContent("Login");
				logVO.setContentPath("/lgn/lgn.ums");
				insertLoginActionLog(request, session, logVO);
				
				map.put("result", "Fail");
			}
		} else {
			if( "ADMIN".equals(userId.toUpperCase())) {  
				if(userVO != null ) {
					map.put("result", "Success");
					if ("Y".equals(userVO.getPwInitYn())) {
						map.put("needChange", "Y");
					} else {
						map.put("needChange", "N");
					}
				} else {
					ActionLogVO logVO = new ActionLogVO();
					logVO.setStatusGb("Failure");
					logVO.setContent("Login");
					logVO.setContentPath("/lgn/lgn.ums");
					insertLoginActionLog(request, session, logVO);
					
					map.put("result", "Fail");
				}
			}
		}
		
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		return modelAndView;
	}
	
	/**
	 * 사용자 로그인을 처리한다.
	 * @param loginVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/ssolgn")
	public String ssoLoginProcess(@ModelAttribute LoginVO loginVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("## login process Start");
		
		if(loginVO.getpUserId() == null || "".equals(loginVO.getpUserId())) {
			model.addAttribute("result","N");
			return "lgn/lgnP";
		}
		// 아이디, 비밀번호 확인
		logger.debug("loginProcess pUserId = " + loginVO.getpUserId()); 
		
		UserVO userVO = null;
		String userStatus = "999";
		// 사용자 아이디, 비밀번호 체크
		try {
			userVO = loginService.isSSOUser(loginVO);
		} catch(Exception e) {
			logger.error("loginService.isSSOUser Error = " + e);
		}
		
		if(userVO != null ) {
			if(userVO.getStatus() != null && !"".equals(userVO.getStatus())) {
				userStatus = userVO.getStatus();
			}
		}
		
		if("000".equals(userStatus)) {
			
			// 세션값 설정
			session.setAttribute("NEO_USER_ID", userVO.getUserId());		// 사용자ID
			session.setAttribute("NEO_USER_NM", userVO.getUserNm());		// 사용자명
			session.setAttribute("NEO_DEPT_NO", userVO.getDeptNo());		// 부서번호
			session.setAttribute("NEO_DEPT_NM", userVO.getDeptNm());		// 부서명
			session.setAttribute("NEO_TZ_CD", userVO.getTzCd());			// 타임존코드
			session.setAttribute("NEO_TZ_TERM", userVO.getTzTerm());		// 타임존시간차
			session.setAttribute("NEO_UILANG", userVO.getUilang());			// 언어권
			session.setAttribute("NEO_CHARSET", userVO.getCharset());		// 문자셋
			session.setAttribute("NEO_FONT", userVO.getFont());				// 폰트
			session.setAttribute("NEO_ORG_CD", userVO.getOrgCd());			// 조직코드
			session.setAttribute("NEO_ORG_NM", userVO.getOrgKorNm());		// 조직명
			session.setAttribute("NEO_JOB_GB", userVO.getJobGb());			// 직책코드
			session.setAttribute("NEO_JOB_NM", userVO.getJobNm());			// 직책명
			session.setAttribute("NEO_LINK", userVO.getLinkService());		// 사용자 링크 서비스
			
			session.setAttribute("NEO_MAIL_FROM_NM", userVO.getMailFromNm());	 //발송자명
			session.setAttribute("NEO_MAIL_FROM_EM", userVO.getMailFromEm());	 // 발송자이메일주소
			session.setAttribute("NEO_USER_TEL", userVO.getUserTel());			//연락처
			session.setAttribute("NEO_USER_EM", userVO.getUserEm());			//이메일
			session.setAttribute("NEO_REPLY_TO_EM", userVO.getReplyToEm());		//회신이메일
			session.setAttribute("NEO_RETURN_EM", userVO.getReturnEm());		 //return 이메일
			session.setAttribute("NEO_PER_PAGE", StringUtil.setNullToInt(userVO.getPerPage(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")))); // 사용자별 목록 조회 페이지
			
			session.setAttribute("NEO_USE_EMS", "N");			// EMS 사용여부
			session.setAttribute("NEO_USE_RNS", "N");			// RNS 사용여부
			session.setAttribute("NEO_USE_SMS", "N");			// SMS 사용여부
			session.setAttribute("NEO_USE_PUSH", "N");			//  PUSH 사용여부
			
			int [][] arrUserService = checkLicense(userVO.getUserId());
	  
			int service = 0;
			int useYn = 0;
			for(int i=0; i < arrUserService.length ; i++) {
				service = arrUserService[i][0] ;
				switch(service) {
				case 10:
					useYn =  arrUserService[i][1] ;
					if (useYn == 1) {
						session.setAttribute("NEO_USE_EMS", "Y");			// EMS 사용여부
					}
					break;
				case 20: 
					useYn =  arrUserService[i][1] ;
					if (useYn == 1) {
						session.setAttribute("NEO_USE_RNS", "Y");			// RMS 사용여부
					}
					break;
				case 30: 
					useYn =  arrUserService[i][1] ;
					if (useYn == 1) {
						session.setAttribute("NEO_USE_SMS", "Y");			// SMS 사용여부
					}
					break;
				case 40: 
					useYn =  arrUserService[i][1] ;
					if (useYn == 1) {
						session.setAttribute("NEO_USE_PUSH", "Y");			// PUSH 사용여부
					}
					break;
				default: 
					break;
				}
			}			
			if(userVO.getUseSYS() != null && !"".equals(userVO.getUseSYS()) && "1".equals(userVO.getUseSYS())) {
				session.setAttribute("NEO_USE_SYS", "Y");			// 공통설정사용권한여부
			} else {
				session.setAttribute("NEO_USE_SYS", "N");			// 공통설정사용권한여부
			} 
			  
			// 관리자 여부
			if(userVO.getDeptNo() == 1) {
				session.setAttribute("NEO_ADMIN_YN", "Y");
			} else {
				session.setAttribute("NEO_ADMIN_YN", "N");
			}
			
			// 사용자 프로그램 사용권한 조회(데이터 등록 환경에 따라 쿼리 변동 가능성 있음)
			// 사용자 메뉴
			List<SysMenuVO> menuList = null;
			try {
				menuList = loginService.getUserMenuList(userVO.getUserId());
			} catch(Exception e) {
				logger.error("loginService.getUserMenuList Error = " + e);
			}
			// 세션에 사용가능 메뉴 목록 저장
			session.setAttribute("NEO_MENU_LIST", menuList);
						 
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContent("SSOLogin");
			logVO.setContentPath("/lgn/ssolgn.ums");
			insertLoginActionLog(request, session, logVO);
			
			model.addAttribute("result","Y");
			
			String retUrl = "lgn/lgn" ;  
			
			if ( userVO.getLinkService() > 0 ) {
				try {
					for(int i=0; i < arrUserService.length ; i++) {
						service = arrUserService[i][0] ;
						if (userVO.getLinkService() == service) {
							if (arrUserService[i][1] == 1) {
								if (userVO.getLinkService() == 10) {
									retUrl =  "redirect:/ems/index.ums";
								} else if (userVO.getLinkService() == 20) {
									retUrl =  "redirect:/rns/index.ums";
								} else if (userVO.getLinkService() == 30) {
									retUrl =  "redirect:/sms/index.ums";
								} else if (userVO.getLinkService() == 40) {
									retUrl =  "redirect:/push/index.ums";
								}
							}
						} 
					}
				} catch(Exception e) {
					logger.error("loginService.getUserMenuList sendRedirect Error = " + e);				
				}
			}
			
			//retUrl =  "redirect:/ems/index.ums";
			return retUrl;
			
		} else {
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Failure");
			logVO.setContent("SSOLogin");
			logVO.setContentPath("/lgn/ssolgn.ums");
			session.setAttribute("NEO_USER_ID", loginVO.getpUserId());		// 사용자ID
			insertLoginActionLog(request, session, logVO);

			if(userVO != null ) {
				model.addAttribute("result","E");
			} else {
				model.addAttribute("result","N");
			}
			 
			//return "lgn/lgnP";
			return "/err/errorUser";
		}
	}
	
	/**
	 * 로그아웃 처리
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/logout")
	public String goLogout(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		ActionLogVO logVO = new ActionLogVO();
		logVO.setStatusGb("Success");
		logVO.setContent("LogOut");
		logVO.setContentPath("/lgn/logout.ums");
		insertLoginActionLog(request, session, logVO); 
		
		// 세션정보 초기화
		session.invalidate();
		return "lgn/logout";
	}
	
	/**
	 * 세션 타임아웃 처리
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/timeout")
	public String goSessionTimeout(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		String requestUri = request.getRequestURI();
		
		model.addAttribute("requestUri", requestUri);
		
		return "lgn/timeout";
	}
	
	public void insertLoginActionLog(HttpServletRequest req, HttpSession session, ActionLogVO actionLogVO) {
		
		try {
			if("Success".equals(actionLogVO.getStatusGb())) {
				actionLogVO.setContentType("000"); // 000: 인증
				actionLogVO.setExtrYn("N");
				actionLogVO.setMobilYn("N");
				systemService.insertActionLog(req, session, actionLogVO);
			} else {
				
				actionLogVO.setLogDt(StringUtil.getDate(Code.TM_YMDHMSM) );		// 로그일시
				actionLogVO.setSessionId( session.getId() );						// 세션ID
				actionLogVO.setIpAddr(req.getRemoteAddr() );					// IP주소
				actionLogVO.setUserId(req.getParameter("pUserId"));				// 사용자ID
				actionLogVO.setDeptNo(0);	// 사용자그룹
				actionLogVO.setOrgCd("");	// 조직코드
				
				actionLogVO.setStatusGb("Failure");  // 000: 인증
				actionLogVO.setContentType("000");
				if( "".equals(actionLogVO.getMessage()) || null == actionLogVO.getMessage() ) {
					actionLogVO.setMessage("인증정보 불일치");
				}
				actionLogVO.setExtrYn("N");
				actionLogVO.setMobilYn("N");
				
				systemService.insertActionLog(actionLogVO);
			}
		} catch(Exception e) {
			logger.error("systemService.insertActionLog error = " + e);
		} 
	}
	
	public int[][] checkLicense(String userId) {
		
		List<ServiceVO> userServiceList = null;
		ServiceVO userService = new ServiceVO();
		String licenseKey = ""; 
		String decLicenseKey = "";
		String domainKey = ""; 
		int [][] arrUserService = null;
		int arrIndex = 0 ; 
		try {
			userServiceList = mainService.getUserService(userId);
			if(userServiceList != null) {
				arrUserService = new int [userServiceList.size()][2];
				
				for(int i = 0 ; i < userServiceList.size() ; i ++) {
					userService = userServiceList.get(i);
					if ( userService.getUseYn().equals("1")) {
						arrUserService[arrIndex][0] = userService.getServiceGb();
						licenseKey  = userService.getLicenseKey();
						//decLicenseKey= EncryptUtil.getJasyptDecryptedString(properties.getProperty("JASYPT.ALGORITHM"),
						decLicenseKey= EncryptUtil.getJasyptDecryptedUnFixString(properties.getProperty("JASYPT.ALGORITHM"),
								properties.getProperty("LICENSE.KEYSTRING"), licenseKey);
						
						domainKey = userService.getCustDomain()  + "+" +  userService.getServiceNm();
						if(!decLicenseKey.substring(0, domainKey.length()).equals(domainKey)) {
							userServiceList.get(i).setPayYn(0);
							arrUserService[arrIndex][1] = 0;
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
									arrUserService[arrIndex][1] = 1;
								} else {
									userServiceList.get(i).setPayYn(0);
									arrUserService[arrIndex][1] = 0;
								}  
							} catch (Exception e) {
								logger.error("mainService.getUserService error[Expire Date illegal] = " + e);
								userServiceList.get(i).setPayYn(0);
								arrUserService[arrIndex][1] = 0;
							}
						}						 
						arrIndex += 1; 
					} 
				}
			}
				
		} catch (Exception e) {
			logger.error("mainService.getUserService error[C009] = " + e);
		} 
		
		return arrUserService;
	}	

	/**
	 * 리만 IAM API 인증 
	 * @param strURL
	 * @param strAuthKey
	 * @param strUserId
	 * @param strUserPw
	 * @return
	 */
	public String callRestApiPost(String strURL, String strAuthKey, String strUserId, String strUserPw) {
		logger.debug( "## callRestApiPost Start" );
		String rtnVal = ""; 
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		MediaType mediaType = MediaType.parse("text/plain");
//		logger.debug( "## new MultipartBody.Builder().setType(MultipartBody.FORM)" );
		RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("grant_type","password")//고정값
				.addFormDataPart("username", strUserId)
				.addFormDataPart("password", strUserPw)
				.addFormDataPart("scope","read")//고정값
				.addFormDataPart("client_id","d49aa9ef-e05b-4fd1-87b5-aba4fb8b79f9")//고정값
				.build();
//		logger.debug( "## new Request.Builder().url(strURL).method(\"POST\", body).addHeader(\"Authorization\", strAuthKey).build()" );
		Request request = new Request.Builder().url(strURL).method("POST", body).addHeader("Authorization", strAuthKey).build();
		try {
//			logger.debug( "## client.newCall(request).execute();" );
			Response response = client.newCall(request).execute();
			rtnVal = response.body().string();
//			logger.debug( rtnVal );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		logger.debug( "## callRestApiPost End" );
		return rtnVal;
	}
	
	/**
	 * 2단계 인증 확인
	 * 
	 * @param userVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/checkTwoFactor")
	public ModelAndView userCheckTwoFactor(@ModelAttribute LoginVO loginVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("userCheckTwoFactor userId = " + loginVO.getpUserId()); // 초기화된 비밀번호 변경할 사용자 ID 암호화 된것 
		logger.debug("userCheckTwoFactor TwoFactorCode = " + loginVO.getTwoFactorCode()); // 인증번호 
 
		int result = 0;
		
		String cipherUserId = loginVO.getpUserId();
 
		try {
			loginVO.setpUserId(EncryptAccUtil.getEncryptedSHA256(cipherUserId, properties.getProperty("ACCOUNT.KEYSTRING")));
		} catch (Exception e) {
			result = -9;
			logger.error("userCheckTwoFactor EncryptAccUtil Error = " + e);
		} 
		
		logger.debug("userUpdateInitPassword userId = " + loginVO.getpUserId()); // 사용자ID

		if ("".equals(loginVO.getTwoFactorCode())) {
			result = -9;
		} else {
			
			// 사용자 정보를 수정한다.
			try {
				result = loginService.isValidTwoFactor(loginVO);
			} catch (Exception e) {
				logger.error("loginService.isValidTwoFactor error = " + e);
			}
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");	
			map.put("message", "인증되었습니다.");
		} else {
			map.put("result", "Fail");
			if(result == -9) {
				map.put("message", "인증번호를 다시 확인하시기 바랍니다.");
			} else if(result == -8) {
				map.put("message", "인증번호가 만료되었습니다. 재발송 후 인증번호를 다시 입력하시기 바랍니다.");
			} else {
				map.put("message", "2차인증을 실패하였습니다. 다시 시도하여주세요. ");
			} 
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		return modelAndView;
	}
	
	/**
	 * 2단계인증번호 재발송
	 * 
	 * @param userVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/reSendTwofactor")
	public ModelAndView reSendTwofactor(@ModelAttribute LoginVO loginVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("reSendTwofactor pUserId = " + loginVO.getpUserId());
		
		//암호화된 내용 복호화  
		String cipherUserId = loginVO.getpUserId();
		try {
			loginVO.setpUserId(EncryptAccUtil.getEncryptedSHA256(cipherUserId, properties.getProperty("ACCOUNT.KEYSTRING")));
		} catch(Exception e) { 
			logger.error("reSendTwofactor EncryptAccUtil Error = " + e);
		}
		
		String userId = loginVO.getpUserId();
		UserVO userVO = new UserVO();
		userVO.setUserId(userId);
		//
		HashMap<String, Object> map = new HashMap<String, Object>();
		//2단계인증 번호 생성 및 SMS발송
		int result = 0;
		try {
			result = loginService.sendSms(userVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result > 0) {
			map.put("result", "Success");
		}else {
			map.put("result", "Fail");
		}
		
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		return modelAndView;
	}
}
