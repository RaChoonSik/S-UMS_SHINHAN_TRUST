/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 시스템 관리 Controller==>사용자 코드 관리 Controller
 * 수정자 : 김준희
 * 작성일시 : 2021.08.09
 * 수정내역 : 패키지명수정 및 sys관련 분리에 의한 소스 변경 kr.co.sict.ums.sys.controller ==> kr.co.sict.ums.sys.cod.controller
 *                사용자코드 관리 기능 외의 항목제거 
 */
package kr.co.sict.ums.sys.cod.controller;

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
import kr.co.sict.ums.ems.cam.vo.CampaignVO;
import kr.co.sict.ums.sys.cod.service.UserCodeService;
import kr.co.sict.ums.sys.cod.vo.UserCodeGroupVO;
import kr.co.sict.ums.sys.cod.vo.UserCodeVO;
import kr.co.sict.ums.sys.cod.vo.SenderKeyVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.MessageUtil;
import kr.co.sict.util.PageUtil;  
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Controller
@RequestMapping("/sys/cod")
public class UserCodeController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private UserCodeService userCodeService;
	
	@Autowired
	private PropertiesUtil properties; 

	/**
	 * 코드 그룹 목록 화면을 출력한다
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/userCodeGroupListP")
	public String goUserCodeGroupListP(@ModelAttribute UserCodeGroupVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goUserCodeGroupList searchUserCodeGroupNm = " + searchVO.getSearchCdGrpNm());
		logger.debug("goUserCodeGroupList searchUserCodeGroup = " + searchVO.getSearchCdGrp());
	
		searchVO.setSearchUiLang((String)session.getAttribute("NEO_UILANG"));		
		logger.debug("goUserCodeGroupList getSearchUiLang = " + searchVO.getSearchUiLang());
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);	
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		// 코드그룹목록(코드성) 조회
		CodeVO cdGrp = new CodeVO();
		cdGrp.setUilang(searchVO.getSearchUiLang());
		List<CodeVO> cdGrpList = null;
		try {
			cdGrpList = codeService.getCodeGrpList(cdGrp);
		} catch(Exception e) {
			logger.error("codeService.getCodeGrpList error = " + e);
		}
		
		// 언어코드 목록
		CodeVO uiLang = new CodeVO();
		uiLang.setUilang(searchVO.getSearchUiLang());
		uiLang.setCdGrp("C025");
		uiLang.setUseYn("Y");
		List<CodeVO> uiLangList = null;
		try {
			uiLangList = codeService.getCodeList(uiLang);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C025] error = " + e);
		}
		
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("searchVO", searchVO);			// 검색 항목
		model.addAttribute("cdGrpList", cdGrpList);			// 코드그룹 검색 조건 항목
		model.addAttribute("uiLangList", uiLangList);		// 언어 항목
		
		return "sys/cod/userCodeGroupListP";
	}
	
	/**
	 * 코드 그룹 목록을 출력한다
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/userCodeGroupList")
	public String goUserCodeGroupList(@ModelAttribute UserCodeGroupVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goUserCodeGroupList searchUserCodeGroupNm = " + searchVO.getSearchCdGrpNm());
		logger.debug("goUserCodeGroupList searchUserCodeGroup = " + searchVO.getSearchCdGrp());
	
		searchVO.setSearchUiLang((String)session.getAttribute("NEO_UILANG"));		
		logger.debug("goUserCodeGroupList searchUserCodeGroup = " + searchVO.getSearchUiLang());
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		// 코드그룹 목록 조회
		List<UserCodeGroupVO> userCodeGroupList  = null;
		try {
			userCodeGroupList = userCodeService.getUserCodeGroupList(searchVO);
		} catch(Exception e) {
			logger.error("UserCodeService.getUserCodeGroupList error = " + e);
		}
		
		if(userCodeGroupList != null && userCodeGroupList.size() > 0) {
			totalCount = userCodeGroupList.get(0).getTotalCount();
		}
		
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);		
				
		model.addAttribute("userCodeGroupList", userCodeGroupList);// 코드그룹 목록
		model.addAttribute("pageUtil", pageUtil);	// 페이징
		model.addAttribute("searchVO", searchVO);
		return "sys/cod/userCodeGroupList";
	}
	
	/**
	 * 코드 그룹 신규 등록 화면 
	 * @param userCodeGroupVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/userCodeGroupAddP")
	public String goUserCodeGroupAddP(@ModelAttribute UserCodeGroupVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		  
		String userLang = (String)session.getAttribute("NEO_UILANG");
		// 코드그룹목록(코드성) 조회
		CodeVO cdGrp = new CodeVO();
		cdGrp.setUilang(userLang);
		List<CodeVO> cdGrpList = null;
		try {
			cdGrpList = codeService.getCodeGrpList(cdGrp);
		} catch(Exception e) {
			logger.error("codeService.getCodeGrpList error = " + e);
		}
		
		// 언어코드 목록
		CodeVO uiLang = new CodeVO();
		uiLang.setUilang(userLang);
		uiLang.setCdGrp("C025");
		uiLang.setUseYn("Y");
		List<CodeVO> uiLangList = null;
		try {
			uiLangList = codeService.getCodeList(uiLang);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C025] error = " + e);
		}
  		
		model.addAttribute("cdGrpList", cdGrpList);			// 코드그룹 검색 조건 항목
		model.addAttribute("uiLangList", uiLangList);		// 언어 항목
		
		return "sys/cod/userCodeGroupAddP";
	} 
	
	/**
	 * 코드 그룹 정보 조회 화면 
	 * @param userCodeGroupVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/userCodeGroupUpdateP")
	public String goUserCodeGroupUpdateP(@ModelAttribute UserCodeGroupVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goUserCodeGroupInfoP codeGroup      = " + searchVO.getCdGrp());
		
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));	
		searchVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		UserCodeGroupVO userCodeGroup  = null;
		try {
			userCodeGroup = userCodeService.getUserCodeGroupInfo(searchVO);
			if (userCodeGroup != null) {
				String formatDt = "";
				logger.debug("goUserCodeGroupInfoP getRegDt      = " + userCodeGroup.getRegDt()); 
				if ( userCodeGroup.getRegDt() != null && !"".equals(userCodeGroup.getRegDt())) {
					formatDt = StringUtil.getFDate(userCodeGroup.getRegDt(), Code.DT_FMT2);
					userCodeGroup.setRegDt(formatDt);
				}
				if ( userCodeGroup.getUpDt() != null && !"".equals(userCodeGroup.getUpDt())) {
					formatDt = StringUtil.getFDate(userCodeGroup.getUpDt(), Code.DT_FMT2);
					logger.debug("goUserCodeGroupInfoP formatDt      = " +  formatDt);
					userCodeGroup.setUpDt(formatDt);
				} 
			}
		} catch(Exception e) {
			logger.error("userCodeService.getUserCodeGroupInfo error = " + e);
		}
		
		// 코드그룹목록(코드성) 조회
		CodeVO cdGrp = new CodeVO();
		cdGrp.setUilang(searchVO.getUilang());
		List<CodeVO> cdGrpList = null;
		try {
			cdGrpList = codeService.getCodeGrpList(cdGrp);
		} catch(Exception e) {
			logger.error("codeService.getCodeGrpList error = " + e);
		}
		
		// 언어코드 목록
		CodeVO uiLang = new CodeVO();
		uiLang.setUilang(searchVO.getUilang());
		uiLang.setCdGrp("C025");
		uiLang.setUseYn("Y");
		List<CodeVO> uiLangList = null;
		try {
			uiLangList = codeService.getCodeList(uiLang);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C025] error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("userCodeGroup", userCodeGroup);	// 코드그룹 조회 결과
		model.addAttribute("cdGrpList", cdGrpList);			// 상위코드분류
		model.addAttribute("uiLangList", uiLangList);		// 언어 항목
		
		return "sys/cod/userCodeGroupUpdateP";
	} 
	
	/**
	 * 코드그룹 정보 추가
	 * @param userCodeGroupVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */	
	@RequestMapping(value="/userCodeGroupAdd")
	public ModelAndView insertUserCodeGroupInfo(@ModelAttribute UserCodeGroupVO userCodeGroupVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("insertUserCodeGroupInfo cdGrp = " + userCodeGroupVO.getCdGrp());		
		logger.debug("insertUserCodeGroupInfo cdGrpNm = " + userCodeGroupVO.getCdGrpNm());
		logger.debug("insertUserCodeGroupInfo cdGrpDtl = " + userCodeGroupVO.getCdGrpDtl());		
		logger.debug("insertUserCodeGroupInfo upCd = " + userCodeGroupVO.getUpCdGrp());
		logger.debug("insertUserCodeGroupInfo sysYn = " + userCodeGroupVO.getSysYn());
		logger.debug("insertUserCodeGroupInfo useYn = " + userCodeGroupVO.getUseYn());
						
		userCodeGroupVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		userCodeGroupVO.setRegId((String) session.getAttribute("NEO_USER_ID"));
		userCodeGroupVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		if ( userCodeGroupVO.getUilang() == null || "".equals((String)userCodeGroupVO.getUilang())) {
			userCodeGroupVO.setUilang("000");
		}
		
		//기본값 설정 (SYS_YN, USE_YN) 
		if(userCodeGroupVO.getSysYn() == null || "".equals((String)userCodeGroupVO.getSysYn())) {
			userCodeGroupVO.setSysYn("N");
		}
		if(userCodeGroupVO.getUseYn() == null || "".equals((String)userCodeGroupVO.getUseYn())) {
			userCodeGroupVO.setUseYn("N");
		}
		
		logger.debug("insertUserCodeGroupInfo Uilang = " + userCodeGroupVO.getUilang());
		logger.debug("insertUserCodeGroupInfo RegId = " + userCodeGroupVO.getRegId());
		logger.debug("insertUserCodeGroupInfo RegDt = " + userCodeGroupVO.getRegDt());
		
		int result = 0;
		try {
			result = userCodeService.insertUserCodeGroupInfo(userCodeGroupVO);
		} catch(Exception e) {
			logger.error("userCodeService.insertUserCodeGroupInfo error = " + e);
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
	
	/**
	 * 코드그룹 정보 수정
	 * @param userCodeGroupVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/userCodeGroupUpdate")
	public ModelAndView updateCodeGroupInfo(@ModelAttribute UserCodeGroupVO userCodeGroupVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateCodeGroupInfo cdGrp      = " + userCodeGroupVO.getCdGrp());
		logger.debug("updateCodeGroupInfo cdGrpNm      = " + userCodeGroupVO.getCdGrpNm());
		logger.debug("updateCodeGroupInfo cdGrpDtl     = " + userCodeGroupVO.getCdGrpDtl());
		logger.debug("updateCodeGroupInfo upCd 			= " + userCodeGroupVO.getUpCdGrp());
		logger.debug("updateCodeGroupInfo sysYn        = " + userCodeGroupVO.getSysYn());
		logger.debug("updateCodeGroupInfo useYn        = " + userCodeGroupVO.getUseYn());
		 	 
		userCodeGroupVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		userCodeGroupVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		userCodeGroupVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
 
		//기본값 설정 (SYS_YN, USE_YN) 
		if(userCodeGroupVO.getSysYn() == null || "".equals((String)userCodeGroupVO.getSysYn())) {
			userCodeGroupVO.setSysYn("N");
		}
		if(userCodeGroupVO.getUseYn() == null || "".equals((String)userCodeGroupVO.getUseYn())) {
			userCodeGroupVO.setUseYn("N");
		}
		
		userCodeGroupVO.setUpId((String)session.getAttribute("NEO_USER_ID"));
		userCodeGroupVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		int result = 0;
		try {
			result = userCodeService.updateUserCodeGroupInfo(userCodeGroupVO);
		} catch(Exception e) {
			logger.error("userCodeService.updateUserCodeGroupInfo error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(result > 0) {
			map.put("result", "Success");
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 코드 그룹 정보 삭제
	 * @param userCodeGroupVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */		
	@RequestMapping(value="/userCodeGroupDelete")
	public ModelAndView deleteUserCodeGroupInfo(@ModelAttribute UserCodeGroupVO userCodeGroupVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("deleteUserCodeGroupInfo cdGrps      = " + userCodeGroupVO.getCdGrps());
		
		userCodeGroupVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		userCodeGroupVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		userCodeGroupVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		if ( userCodeGroupVO.getUilang() == null || "".equals((String)userCodeGroupVO.getUilang())) {
			userCodeGroupVO.setUilang("000");
		}
				
		userCodeGroupVO.setUpId((String)session.getAttribute("NEO_USER_ID"));
		userCodeGroupVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		String[] cdGrp = userCodeGroupVO.getCdGrps().split(",");
		
		int result = 0;
		
		for (int i = 0; i < cdGrp.length; i++) {
			userCodeGroupVO.setCdGrp(cdGrp[i]);
			try {
				result = userCodeService.deleteUserCodeGroupInfo(userCodeGroupVO);
			} catch (Exception e) {
				logger.error("userCodeService.deleteUserCodeInfo error = " + e);
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
	
	/**
	 * 코드 관리 화면을 출력한다
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/userCodeListP")
	public String goUserCodeListP(@ModelAttribute UserCodeVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goUserCodeList searchUserCodeGroupNm = " + searchVO.getSearchCdGrpNm());
		logger.debug("goUserCodeList searchUserCodeGroup = " + searchVO.getSearchCdGrp());
	
		searchVO.setSearchUiLang((String)session.getAttribute("NEO_UILANG"));
		
		if ( searchVO.getUilang() == null || "".equals((String)searchVO.getUilang())) {
			searchVO.setUilang("000");
		}
  
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);		
		searchVO.setPage(page);		
 
		// 코드그룹목록(코드성) 조회
		UserCodeGroupVO cdGrp = new UserCodeGroupVO();
		cdGrp.setSearchUiLang(searchVO.getSearchUiLang());
		cdGrp.setPage(1);
		cdGrp.setRows(999);
		List<UserCodeGroupVO> cdGrpList = null;
		 
		try {
			cdGrpList =   userCodeService.getUserCodeGroupList(cdGrp);
		} catch(Exception e) {
			logger.error("userCodeService.getUserCodeGroupList error = " + e);
		}
		
		// 언어코드 목록
		CodeVO uiLang = new CodeVO();
		uiLang.setUilang(searchVO.getSearchUiLang());
		uiLang.setCdGrp("C025");
		uiLang.setUseYn("Y");
		List<CodeVO> uiLangList = null;
		try {
			uiLangList = codeService.getCodeList(uiLang);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C025] error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색 항목		
		model.addAttribute("cdGrpList", cdGrpList);			// 분류코드 검색 조건 항목 
		model.addAttribute("uiLangList", uiLangList);		// 언어 항목
		return "sys/cod/userCodeListP";
	}
	
	/**
	 * 코드 목록을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/userCodeList")
	public String goUserCodeList(@ModelAttribute UserCodeVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goUserCodeList searchUserCodeGroupNm = " + searchVO.getSearchCdGrpNm());
		logger.debug("goUserCodeList searchUserCodeGroup = " + searchVO.getSearchCdGrp());
	
		searchVO.setSearchUiLang((String)session.getAttribute("NEO_UILANG"));
 
		
		// 페이지 설정
		int page = 1;
		int rows = 9999999;
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		
		int totalCount = 0;
		
		// 코드 목록 조회
		List<UserCodeVO> userCodeList  = null;
		try {
			userCodeList = userCodeService.getUserCodeList(searchVO);
		} catch(Exception e) {
			logger.error("UserCodeService.getCodeInfo error = " + e);
		}
	 		
		if(userCodeList != null && userCodeList.size() > 0) {
			totalCount = userCodeList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("searchVO", searchVO);			// 검색 항목
		model.addAttribute("userCodeList", userCodeList);	// 코드그룹 내역 목록
		model.addAttribute("pageUtil", pageUtil);			// 페이징
		
		return "sys/cod/userCodeList";
	}
	
	/**
	 * 코드 신규 등록 화면 
	 * @param userCodeVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/userCodeAddP")
	public String goUserCodeAddP(@ModelAttribute UserCodeVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		  
		logger.debug("goUserCodeInfoP codeGroup      = " + searchVO.getCdGrp());		
		logger.debug("goUserCodeInfoP codeGroupNm    = " + searchVO.getCdGrpNm());
		
		String userLang = (String)session.getAttribute("NEO_UILANG");
		// 코드그룹목록(코드성) 조회
		CodeVO cdGrp = new CodeVO();
		cdGrp.setUilang(userLang);
		List<CodeVO> cdGrpList = null;
		cdGrp.setCdGrp(searchVO.getCdGrp());
		try {
			cdGrpList = codeService.getCodeGrpList(cdGrp); 
		} catch(Exception e) {
			logger.error("codeService.getCodeGrpList error = " + e);
		}
		
		// 언어코드 목록
		CodeVO uiLang = new CodeVO();
		uiLang.setUilang(userLang);
		uiLang.setCdGrp("C025");
		uiLang.setUseYn("Y");
		List<CodeVO> uiLangList = null;
		try {
			uiLangList = codeService.getCodeList(uiLang);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C025] error = " + e);
		}
  		
		model.addAttribute("cdGrpList", cdGrpList);			// 코드그룹 검색 조건 항목
		model.addAttribute("uiLangList", uiLangList);		// 언어 항목
		
		return "sys/cod/userCodeAddP";
	} 
	
	/**
	 * 코드 정보 조회 화면 
	 * @param userCodeVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/userCodeUpdateP")
	public String goUserCodeUpdateP(@ModelAttribute UserCodeVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goUserCodeInfoP codeGroup      = " + searchVO.getCdGrp());
		logger.debug("goUserCodeInfoP code           = " + searchVO.getCd());
		
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		UserCodeVO userCode  = null;
		try {
			userCode = userCodeService.getUserCodeInfo(searchVO);
			if (userCode != null) {
				String formatDt = ""; 
				if ( userCode.getRegDt() != null && !"".equals(userCode.getRegDt())) {
					formatDt = StringUtil.getFDate(userCode.getRegDt(), Code.DT_FMT2);
					userCode.setRegDt(formatDt);
				}
				if ( userCode.getUpDt() != null && !"".equals(userCode.getUpDt())) {
					formatDt = StringUtil.getFDate(userCode.getUpDt(), Code.DT_FMT2);
					logger.debug("goUserCodeGroupInfoP formatDt      = " +  formatDt);
					userCode.setUpDt(formatDt);
				} 
			}
			
		} catch(Exception e) {
			logger.error("userCodeService.getUserCodeInfo error = " + e);
		}
		
		// 해당 코드의 분류코드의 상위코드 조회
		//상위 코드 조회 
		String upCdGrp = "";
		try {
			upCdGrp = userCodeService.getUserCodeGroupUpCdGrp(searchVO);
			
		} catch(Exception e) {
			logger.error("userCodeService.getUserCodeGroupUpCdGrp error = " + e);
		}
		
		List<CodeVO> upCdList = null;
		
		if (upCdGrp != null || !"".equals(upCdGrp)){
			// 코드그룹목록(코드성) 조회
			CodeVO upCd = new CodeVO();
			upCd.setUilang(searchVO.getUilang());
			upCd.setCdGrp(upCdGrp);
			upCd.setUseYn("Y");
			try {
				upCdList = codeService.getCodeList(upCd);
			} catch(Exception e) {
				logger.error("codeService.getCodeList error = " + e);
			}
		} 
		
		// 언어코드 목록
		CodeVO uiLang = new CodeVO();
		uiLang.setUilang(searchVO.getUilang());
		uiLang.setCdGrp("C025");
		uiLang.setUseYn("Y");
		List<CodeVO> uiLangList = null;
		try {
			uiLangList = codeService.getCodeList(uiLang);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C025] error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("userCode", userCode);			// 코드 조회 결과
		model.addAttribute("upCdList", upCdList);			// 상위코드
		model.addAttribute("uiLangList", uiLangList);		// 언어 항목
		
		return "sys/cod/userCodeUpdateP";
	} 
	
	/**
	 * 코드그룹 정보 추가
	 * @param userCodeGroupVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */	
	@RequestMapping(value="/userCodeAdd")
	public ModelAndView insertUserCodeInfo(@ModelAttribute UserCodeVO userCodeVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("insertUserCodeInfo cd = " + userCodeVO.getCd());
		logger.debug("insertUserCodeInfo cdNm = " + userCodeVO.getCdNm());		
		logger.debug("insertUserCodeInfo cdGrp = " + userCodeVO.getCdGrp());
		logger.debug("insertUserCodeInfo upCd = " + userCodeVO.getUpCd());
		logger.debug("insertUserCodeInfo cdDtl = " + userCodeVO.getCdDtl());		
		logger.debug("insertUserCodeInfo useYn = " + userCodeVO.getUseYn());
						
		userCodeVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		userCodeVO.setRegId((String) session.getAttribute("NEO_USER_ID"));
		userCodeVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));				
		
		if ( userCodeVO.getUilang() == null || "".equals((String)userCodeVO.getUilang())) {
			userCodeVO.setUilang("000");
		}
		
		if(userCodeVO.getUseYn() == null || "".equals((String)userCodeVO.getUseYn())) {
			userCodeVO.setUseYn("N");
		}
		
		String result = "";
		try {
			result = userCodeService.insertUserCodeInfo(userCodeVO);
		} catch(Exception e) {
			logger.error("userCodeService.insertUserCodeInfo error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(Integer.parseInt(result) > 0 ) {
			map.put("result","Success");
			map.put("cd", result);
		} else {
			map.put("result","Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 코드그룹 정보 수정
	 * @param userCodeGroupVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/userCodeUpdate")
	public ModelAndView updateCodeInfo(@ModelAttribute UserCodeVO userCodeVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateCodeInfo cdNm      = " + userCodeVO.getCdNm());
		logger.debug("updateCodeInfo cdDtl     = " + userCodeVO.getCdDtl());
		logger.debug("updateCodeInfo upCd      = " + userCodeVO.getUpCd());
		logger.debug("updateCodeInfo useYn     = " + userCodeVO.getUseYn());
		 	 
		userCodeVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		userCodeVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		userCodeVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));				
		
		if ( userCodeVO.getUilang() == null || "".equals((String)userCodeVO.getUilang())) {
			userCodeVO.setUilang("000");
		}

		if(userCodeVO.getUseYn() == null || "".equals((String)userCodeVO.getUseYn())) {
			userCodeVO.setUseYn("N");
		}
		
		userCodeVO.setUpId((String)session.getAttribute("NEO_USER_ID"));
		userCodeVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		int result = 0;
		try {
			result = userCodeService.updateUserCodeInfo(userCodeVO);
		} catch(Exception e) {
			logger.error("userCodeService.updateUserCodeInfo error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(result > 0) {
			map.put("result", "Success");
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 코드그룹 정보 수정
	 * @param userCodeGroupVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/updateUserCodeSortNo")
	public ModelAndView updateUserCodeSortNo(@ModelAttribute UserCodeVO userCodeVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateCodeInfo cdGro      = " + userCodeVO.getCdGrp());
		logger.debug("updateCodeInfo cd     = " + userCodeVO.getCd());
		logger.debug("updateCodeInfo sortNo      = " + userCodeVO.getSortNo()); 
		 	 
		userCodeVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		userCodeVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		userCodeVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));				
		
		if ( userCodeVO.getUilang() == null || "".equals((String)userCodeVO.getUilang())) {
			userCodeVO.setUilang("000");
		} 
		
		userCodeVO.setUpId((String)session.getAttribute("NEO_USER_ID"));
		userCodeVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		int result = 0;
		try {
			result = userCodeService.updateUserCodeSortNo(userCodeVO);
		} catch(Exception e) {
			logger.error("userCodeService.updateUserCodeSortNo error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(result > 0) {
			map.put("result", "Success");
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}	
	
	/**
	 * 코드 정보 조회
	 * @param userCodeVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/userCodeInfo")
	public ModelAndView getUserCodeInfo(@ModelAttribute UserCodeVO userCodeVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getUserCodeInfo code      = " + userCodeVO.getCd());
		
		userCodeVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		try {
			userCodeVO = userCodeService.getUserCodeInfo(userCodeVO);
		} catch(Exception e) {
			logger.error("userCodeService.getUserCodeInfo error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userCode", userCodeVO);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 코드그룹에 연관된 상위코드의 코드목록 조회
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/userCodeListByCodeGroup")
	public ModelAndView goUserCodeListByCodeGroup(@ModelAttribute UserCodeVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goUserCodeList getCdGrp = " + searchVO.getCdGrp());
	
		searchVO.setSearchUiLang((String)session.getAttribute("NEO_UILANG"));
		
		if ( searchVO.getUilang() == null || "".equals((String)searchVO.getUilang())) {
			searchVO.setUilang("000");
		}
			
		//상위 코드 조회 
		String targetUpCdGrp = "";
		try {
			targetUpCdGrp = userCodeService.getUserCodeGroupUpCdGrp(searchVO);
		} catch(Exception e) {
			logger.error("userCodeService.getUserCodeGroupUpCdGrp error = " + e);
		}
		
		List<CodeVO> upCdGrpList = null;
		
		if (targetUpCdGrp != null || !"".equals(targetUpCdGrp)){
			// 코드그룹목록(코드성) 조회
			CodeVO upCdGrp = new CodeVO();
			upCdGrp.setUilang(searchVO.getSearchUiLang());
			upCdGrp.setCdGrp( targetUpCdGrp);
			upCdGrp.setUseYn("Y");
			try {
				upCdGrpList = codeService.getCodeList(upCdGrp);
			} catch(Exception e) {
				logger.error("codeService.getCodeGrpList error = " + e);
			}
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색 항목		
		model.addAttribute("upCdGrpList", upCdGrpList);			// 분류코드 검색 조건 항목 		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("upCdGrpList", upCdGrpList);

		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		return modelAndView;
	}  
	
	/**
	 * 코드 정보 삭제
	 * @param userCodeGroupVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/userCodeDelete")
	public ModelAndView deleteUserCodeInfo(@ModelAttribute UserCodeVO userCodeVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("deleteUserCodeInfo cdGrp      = " + userCodeVO.getCdGrp());
		logger.debug("deleteUserCodeInfo cds      = " + userCodeVO.getCds());
		
		if ( userCodeVO.getUilang() == null || "".equals((String)userCodeVO.getUilang())) {
			userCodeVO.setUilang("000");
		}
		userCodeVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		userCodeVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));				
				
		
		userCodeVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		userCodeVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		String[] cd = userCodeVO.getCds().split(",");
		
		int result = 0;
		
		for (int i = 0; i < cd.length; i++) {
			userCodeVO.setCd(cd[i]);
			try {
				result = userCodeService.deleteUserCodeInfo(userCodeVO);
			} catch (Exception e) {
				logger.error("userCodeService.deleteUserCodeInfo error = " + e);
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
	
	
	/***************API를 통한 발송요청시 발송 키 항목을 관리한다(해당 발송 키는 카카오알림톡에서만 사용된다)************************/
	/**
	 * 발송키 관리 화면을 출력한다
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/senderKeyListP")
	public String goSenderKeyListP(@ModelAttribute SenderKeyVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		return "sys/cod/senderKeyListP";
	}
	
	/**
	 * 발송키 목록을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/senderKeyList")
	public String goSenderKeyList(@ModelAttribute SenderKeyVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 페이지 설정
		int page = 1;
		int rows = 9999999;
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		
		int totalCount = 0;
		
		// 코드 목록 조회
		List<SenderKeyVO> senderKeyList  = null;
		try {
			senderKeyList = userCodeService.getSenderKeyList(searchVO);
		} catch(Exception e) {
			logger.error("UserCodeService.getSenderKeyList error = " + e);
		}
	 		
		if(senderKeyList != null && senderKeyList.size() > 0) {
			totalCount = senderKeyList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("searchVO", searchVO);			// 검색 항목
		model.addAttribute("senderKeyList", senderKeyList);	// 발송키 내역 목록
		model.addAttribute("pageUtil", pageUtil);			// 페이징
		
		return "sys/cod/senderKeyList";
	}
	
	/**
	 * 발송키 정보 조회
	 * @param senderKeyVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/senderKeyInfo")
	public ModelAndView getSenderKeyInfo(@ModelAttribute SenderKeyVO senderKeyVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getSenderKeyInfo senderKeyId      = " + senderKeyVO.getSenderKeyId());
		
		
		try {
			senderKeyVO = userCodeService.getSenderKeyInfo(senderKeyVO);
		} catch(Exception e) {
			logger.error("userCodeService.getSenderKeyInfo error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("senderKey", senderKeyVO);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	
		
	/**
	 * 발송키 정보 추가
	 * @param senderKeyVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */	
	@RequestMapping(value="/senderKeyAdd")
	public ModelAndView insertSenderKeyInfo(@ModelAttribute SenderKeyVO senderKeyVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("insertSenderKeyInfo senderKey = " + senderKeyVO.getSenderKey());	
		logger.debug("insertSenderKeyInfo useYn = " + senderKeyVO.getUseYn());
		
		senderKeyVO.setRegId((String) session.getAttribute("NEO_USER_ID"));
		senderKeyVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));	

		if(senderKeyVO.getUseYn() == null || "".equals((String)senderKeyVO.getUseYn())) {
			senderKeyVO.setUseYn("N");
		}
		
		int result = -1;
		try {
			result = userCodeService.insertSenderKeyInfo(senderKeyVO);
		} catch(Exception e) {
			logger.error("userCodeService.insertSenderKeyInfo error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(result > 0 ) {
			map.put("result","Success");
			map.put("senderKeyId", result);
		} else {
			map.put("result","Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 발송키 정보 수정
	 * @param senderKeyVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/senderKeyUpdate")
	public ModelAndView updateSenderKey(@ModelAttribute SenderKeyVO senderKeyVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateSenderKey senderKeyId = " + senderKeyVO.getSenderKeyId());
		logger.debug("updateSenderKey senderKey   = " + senderKeyVO.getSenderKey());
		logger.debug("updateSenderKey useYn       = " + senderKeyVO.getUseYn());
		 	 
		senderKeyVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		senderKeyVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		if(senderKeyVO.getUseYn() == null || "".equals((String)senderKeyVO.getUseYn())) {
			senderKeyVO.setUseYn("N");
		}
		
		senderKeyVO.setUpId((String)session.getAttribute("NEO_USER_ID"));
		senderKeyVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		int result = 0;
		try {
			result = userCodeService.updateSenderKeyInfo(senderKeyVO);
		} catch(Exception e) {
			logger.error("userCodeService.updateSenderKeyInfo error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(result > 0) {
			map.put("result", "Success");
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 발송키  정보 삭제
	 * @param senderKeyVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/senderKeyDelete")
	public ModelAndView deleteSenderKeyInfo(@ModelAttribute SenderKeyVO senderKeyVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("deleteSenderKeyInfo senderKeyIds = " + senderKeyVO.getSenderKeyIds());

		senderKeyVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		senderKeyVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		senderKeyVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		senderKeyVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		String[] senderKeyId = senderKeyVO.getSenderKeyIds().split(",");
		
		int result = 0;
		
		for (int i = 0; i < senderKeyId.length; i++) {
			senderKeyVO.setSenderKeyId(senderKeyId[i]);
			try {
				result = userCodeService.deleteSenderKeyInfo(senderKeyVO);
			} catch (Exception e) {
				logger.error("userCodeService.deleteSenderKeyInfo error = " + e);
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
