package kr.co.sict.ums.push.ana.controller;

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

import kr.co.sict.ums.com.service.CodeService;
import kr.co.sict.ums.com.service.CryptoService;
import kr.co.sict.ums.com.vo.CodeVO;
import kr.co.sict.ums.push.ana.service.PushAnalysisService;
import kr.co.sict.ums.push.ana.vo.PushSendLogVO;
import kr.co.sict.ums.push.cam.service.PushCampaignService;
import kr.co.sict.ums.push.cam.vo.PushCampaignVO;
import kr.co.sict.util.PageUtil;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Controller
@RequestMapping("/push/ana")
public class PushAnalysisController {
	
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private CodeService codeService;

	@Autowired
	private PushAnalysisService pushAnalysisService;
	
	@Autowired
	private PushCampaignService pushCampaignService;
	
	@Autowired
	private CryptoService cryptoService;
	
	@Autowired
	private PropertiesUtil properties;
	
	/**
	 * PUSH발송 현황 화면을 출력한다.
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pushListP")
	public String goPushListP(@ModelAttribute PushSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPushListP searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goPushListP searchUserId     = " + searchVO.getSearchUserId());
		logger.debug("goPushListP searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goPushListP searchEndtDt     = " + searchVO.getSearchEndDt());
		logger.debug("goPushListP searchPushGubun  = " + searchVO.getSearchPushGubun());
		logger.debug("goPushListP searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goPushListP searchPushName   = " + searchVO.getSearchPushName());
		
		// 검색 기본값 설정
		if(searchVO.getSearchStartDt() == null || "".equals(searchVO.getSearchStartDt())) {
			searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-1, "M", "yyyyMMdd"));
		} else {
			searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		}
		if(searchVO.getSearchEndDt() == null || "".equals(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		}
		if(searchVO.getSearchDeptNo() == 0) {
			if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo(0);
			} else {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 전송유형 목록
		CodeVO gubun = new CodeVO();
		gubun.setUilang((String)session.getAttribute("NEO_UILANG"));
		gubun.setCdGrp("C124");
		gubun.setUseYn("Y"); 
		List<CodeVO> pushGubunList = null;
		try {
			pushGubunList = codeService.getCodeList(gubun);
		} catch(Exception e) {
			logger.error("codeService.getCodeListByUpCd[C124] error = " + e);
		}
		
		// 캠페인 목록 조회
		PushCampaignVO camp = new PushCampaignVO();
		camp.setUilang((String)session.getAttribute("NEO_UILANG"));
		camp.setSearchStatus("000");
		camp.setPage(1);
		camp.setRows(10000);
		camp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<PushCampaignVO> pushCampList = null;
		try {
			pushCampList = pushCampaignService.getCampaignList(camp);
		} catch(Exception e) {
			logger.error("pushCampaignService.getCampaignList error = " + e);
		}
		
		// 부서 목록 조회
		CodeVO dept = new CodeVO();
		dept.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(dept);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 사용자 목록 조회
		CodeVO user = new CodeVO();
		user.setDeptNo(searchVO.getSearchDeptNo());
		user.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(user);
		} catch(Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("pushGubunList", pushGubunList);	// PUSH구분유형
		model.addAttribute("pushCampList",pushCampList);	// 캠페인목록
		model.addAttribute("deptList", deptList);			// 부서
		model.addAttribute("userList", userList);			// 사용자
		
		return "push/ana/pushListP";
	}
	
	/**
	 * PUSH발송 현황 목록을 조회 력한다.
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/pushList")
	public String goPushList(@ModelAttribute PushSendLogVO searchVO, Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		logger.debug("goPushList searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goPushList searchUserId     = " + searchVO.getSearchUserId());
		logger.debug("goPushList searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goPushList searchEndtDt     = " + searchVO.getSearchEndDt());
		logger.debug("goPushList searchPushGubun  = " + searchVO.getSearchPushGubun());
		logger.debug("goPushList searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goPushList searchPushName   = " + searchVO.getSearchPushName());
		
		// 검색 기본값 설정
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		if(searchVO.getSearchStartDt() == null || "".equals(searchVO.getSearchStartDt())) {
			searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-1, "M", "yyyyMMdd"));
		} else {
			searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		}
		
		if(searchVO.getSearchEndDt() == null || "".equals(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		}
		
		if(searchVO.getSearchDeptNo() == 0) {
			if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo(0);
			} else {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		// PUSH발송 내역 조회
		List<PushSendLogVO> pushSendList = null;
		try {
			pushSendList = pushAnalysisService.getPushList(searchVO);
		} catch(Exception e) {
			logger.error("pushAnalysisService.getPushList error = " + e);
		}
		
		if(pushSendList != null && pushSendList.size() > 0) {
			totalCount = pushSendList.get(0).getTotalCount();
		}
		
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("pushSendList", pushSendList);	// 항목
		model.addAttribute("pageUtil", pageUtil);			// 페이징
		
		return "push/ana/pushList";
	}
	
	/**
	 * PUSH 통계발송분석 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pushAnalListP")
	public String goPushAnalListP(@ModelAttribute PushSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPushAnalListP pushmessageId  = " + searchVO.getPushmessageId());
		 
		
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG")); 
		searchVO.setAdminYn((String) session.getAttribute("NEO_ADMIN_YN"));
		
		PushSendLogVO pushLogInfo = null; 
		try {
			pushLogInfo = pushAnalysisService.getPushInfo(searchVO);
		} catch(Exception e) {
			logger.error("pushAnalysisService.getPushInfo error = " + e);
		} 

		// 페이지 설정
		searchVO.setPage(1);
		searchVO.setRows(9999999);
		
		model.addAttribute("searchVO", searchVO);			// 검색조건
		model.addAttribute("pushLogInfo", pushLogInfo);		// PUSH 발송 정보
		return "push/ana/pushAnalListP";
	}
	
	
	/**
	 * PUSH발송 현황 화면을 출력한다.
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campPushListP")
	public String goCampPushListP(@ModelAttribute PushSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goCampPushListP searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goCampPushListP searchUserId     = " + searchVO.getSearchUserId());
		logger.debug("goCampPushListP searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goCampPushListP searchEndtDt     = " + searchVO.getSearchEndDt());		
		logger.debug("goCampPushListP searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goCampPushListP searchPushName   = " + searchVO.getSearchPushName());
		
		// 검색 기본값 설정
		if(searchVO.getSearchStartDt() == null || "".equals(searchVO.getSearchStartDt())) {
			searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-1, "M", "yyyyMMdd"));
		} else {
			searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		}
		if(searchVO.getSearchEndDt() == null || "".equals(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		}
		if(searchVO.getSearchDeptNo() == 0) {
			if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo(0);
			} else {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 캠페인 목록 조회
		PushCampaignVO camp = new PushCampaignVO();
		camp.setUilang((String)session.getAttribute("NEO_UILANG"));
		camp.setSearchStatus("000");
		camp.setPage(1);
		camp.setRows(10000);
		camp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<PushCampaignVO> pushCampList = null;
		try {
			pushCampList = pushCampaignService.getCampaignList(camp);
		} catch(Exception e) {
			logger.error("pushCampaignService.getCampaignList error = " + e);
		}
		
		// 부서 목록 조회
		CodeVO dept = new CodeVO();
		dept.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(dept);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 사용자 목록 조회
		CodeVO user = new CodeVO();
		user.setDeptNo(searchVO.getSearchDeptNo());
		user.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(user);
		} catch(Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("pushCampList",pushCampList);	// 캠페인목록
		model.addAttribute("deptList", deptList);			// 부서
		model.addAttribute("userList", userList);			// 사용자
		
		return "push/ana/campPushListP";
	}
	
	/**
	 * PUSH발송 현황 목록을 조회 력한다.
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/campPushList")
	public String goCampPushList(@ModelAttribute PushSendLogVO searchVO, Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		logger.debug("goCampPushList searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goCampPushList searchUserId     = " + searchVO.getSearchUserId());
		logger.debug("goCampPushList searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goCampPushList searchEndtDt     = " + searchVO.getSearchEndDt());
		logger.debug("goCampPushList searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goCampPushList searchPushName   = " + searchVO.getSearchPushName());
		
		// 검색 기본값 설정
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		if(searchVO.getSearchStartDt() == null || "".equals(searchVO.getSearchStartDt())) {
			searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-1, "M", "yyyyMMdd"));
		} else {
			searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		}
		
		if(searchVO.getSearchEndDt() == null || "".equals(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		}
		
		if(searchVO.getSearchDeptNo() == 0) {
			if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo(0);
			} else {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		// PUSH발송 내역 조회
		List<PushSendLogVO> campPushList = null;
		try {
			campPushList = pushAnalysisService.getCampPushList(searchVO);
		} catch(Exception e) {
			logger.error("pushAnalysisService.getPushList error = " + e);
		}
		
		if(campPushList != null && campPushList.size() > 0) {
			totalCount = campPushList.get(0).getTotalCount();
		}
		
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("campPushList", campPushList);	// 항목
		model.addAttribute("pageUtil", pageUtil);			// 페이징
		
		return "push/ana/campPushList";
	}

	/**
	 * 캠페인별 발송이력 -> PUSH발송 현황 화면을 출력한다.
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pushSendListP")
	public String goPushSendListP(@ModelAttribute PushSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPushSendListP searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goPushSendListP searchUserId     = " + searchVO.getSearchUserId());
		logger.debug("goPushSendListP searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goPushSendListP searchEndtDt     = " + searchVO.getSearchEndDt());
		logger.debug("goPushSendListP searchPushGubun  = " + searchVO.getSearchPushGubun());
		logger.debug("goPushSendListP searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goPushSendListP searchPushName   = " + searchVO.getSearchPushName());
		
		// 검색 기본값 설정
		if(searchVO.getSearchStartDt() == null || "".equals(searchVO.getSearchStartDt())) {
			searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-1, "M", "yyyyMMdd"));
		} else {
			searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		}
		if(searchVO.getSearchEndDt() == null || "".equals(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		}
		if(searchVO.getSearchDeptNo() == 0) {
			if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo(0);
			} else {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 전송유형 목록
		CodeVO gubun = new CodeVO();
		gubun.setUilang((String)session.getAttribute("NEO_UILANG"));
		gubun.setCdGrp("C124");
		gubun.setUseYn("Y"); 
		List<CodeVO> pushGubunList = null;
		try {
			pushGubunList = codeService.getCodeList(gubun);
		} catch(Exception e) {
			logger.error("codeService.getCodeListByUpCd[C124] error = " + e);
		}
		
		// 캠페인 목록 조회
		PushCampaignVO camp = new PushCampaignVO();
		camp.setUilang((String)session.getAttribute("NEO_UILANG"));
		camp.setSearchStatus("000");
		camp.setPage(1);
		camp.setRows(10000);
		camp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<PushCampaignVO> pushCampList = null;
		try {
			pushCampList = pushCampaignService.getCampaignList(camp);
		} catch(Exception e) {
			logger.error("pushCampaignService.getCampaignList error = " + e);
		}
		
		// 부서 목록 조회
		CodeVO dept = new CodeVO();
		dept.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(dept);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 사용자 목록 조회
		CodeVO user = new CodeVO();
		user.setDeptNo(searchVO.getSearchDeptNo());
		user.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(user);
		} catch(Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("pushGubunList", pushGubunList);	// PUSH구분유형
		model.addAttribute("pushCampList",pushCampList);	// 캠페인목록
		model.addAttribute("deptList", deptList);			// 부서
		model.addAttribute("userList", userList);			// 사용자
		
		return "push/ana/pushSendListP";
	}
	
	/**
	 * 캡페인별 발송이력 -> PUSH발송 현황 목록을 조회 력한다.
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/pushSendList")
	public String goPushSendList(@ModelAttribute PushSendLogVO searchVO, Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		logger.debug("goPushSendList searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goPushSendList searchUserId     = " + searchVO.getSearchUserId());
		logger.debug("goPushSendList searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goPushSendList searchEndtDt     = " + searchVO.getSearchEndDt());
		logger.debug("goPushSendList searchPushGubun  = " + searchVO.getSearchPushGubun());
		logger.debug("goPushSendList searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goPushSendList searchPushName   = " + searchVO.getSearchPushName());
		
		// 검색 기본값 설정
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		if(searchVO.getSearchStartDt() == null || "".equals(searchVO.getSearchStartDt())) {
			searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-1, "M", "yyyyMMdd"));
		} else {
			searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		}
		
		if(searchVO.getSearchEndDt() == null || "".equals(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		}
		
		if(searchVO.getSearchDeptNo() == 0) {
			if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo(0);
			} else {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		// PUSH발송 내역 조회
		List<PushSendLogVO> pushSendList = null;
		try {
			pushSendList = pushAnalysisService.getPushList(searchVO);
		} catch(Exception e) {
			logger.error("pushAnalysisService.getPushList error = " + e);
		}
		
		if(pushSendList != null && pushSendList.size() > 0) {
			totalCount = pushSendList.get(0).getTotalCount();
		}
		
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("pushSendList", pushSendList);	// 항목
		model.addAttribute("pageUtil", pageUtil);			// 페이징
		
		return "push/ana/pushSendList";
	}

    /**
     * PUSH발송 현황 화면을 출력한다.
     * 
     * @param searchVO
     * @param model
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping(value="/pushCampListP")
    public String goPushCampListP(@ModelAttribute PushSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        logger.debug("goPushCampListP searchDeptNo     = " + searchVO.getSearchDeptNo());
        logger.debug("goPushCampListP searchUserId     = " + searchVO.getSearchUserId());
        logger.debug("goPushCampListP searchStartDt    = " + searchVO.getSearchStartDt());
        logger.debug("goPushCampListP searchEndtDt     = " + searchVO.getSearchEndDt());
        logger.debug("goPushCampListP searchPushGubun  = " + searchVO.getSearchPushGubun());
        logger.debug("goPushCampListP searchCampNo     = " + searchVO.getSearchCampNo());
        logger.debug("goPushCampListP searchPushName   = " + searchVO.getSearchPushName());
        
        // 검색 기본값 설정
        if(searchVO.getSearchStartDt() == null || "".equals(searchVO.getSearchStartDt())) {
            searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-1, "M", "yyyyMMdd"));
        } else {
            searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
        }
        if(searchVO.getSearchEndDt() == null || "".equals(searchVO.getSearchEndDt())) {
            searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
        } else {
            searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
        }
        if(searchVO.getSearchDeptNo() == 0) {
            if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
                searchVO.setSearchDeptNo(0);
            } else {
                searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
            }
        }
        searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
        
        // 전송유형 목록
        CodeVO gubun = new CodeVO();
        gubun.setUilang((String)session.getAttribute("NEO_UILANG"));
        gubun.setCdGrp("C124");
        gubun.setUseYn("Y"); 
        List<CodeVO> pushGubunList = null;
        try {
            pushGubunList = codeService.getCodeList(gubun);
        } catch(Exception e) {
            logger.error("codeService.getCodeListByUpCd[C124] error = " + e);
        }
        
        // 캠페인 목록 조회
        PushCampaignVO camp = new PushCampaignVO();
        camp.setUilang((String)session.getAttribute("NEO_UILANG"));
        camp.setSearchStatus("000");
        camp.setPage(1);
        camp.setRows(10000);
        camp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
        List<PushCampaignVO> pushCampList = null;
        try {
            pushCampList = pushCampaignService.getCampaignList(camp);
        } catch(Exception e) {
            logger.error("pushCampaignService.getCampaignList error = " + e);
        }
        
        // 부서 목록 조회
        CodeVO dept = new CodeVO();
        dept.setStatus("000"); // 정상
        List<CodeVO> deptList = null;
        try {
            deptList = codeService.getDeptList(dept);
        } catch(Exception e) {
            logger.error("codeService.getDeptList error = " + e);
        }
        
        // 사용자 목록 조회
        CodeVO user = new CodeVO();
        user.setDeptNo(searchVO.getSearchDeptNo());
        user.setStatus("000");
        List<CodeVO> userList = null;
        try {
            userList = codeService.getUserList(user);
        } catch(Exception e) {
            logger.error("codeService.getUserList error = " + e);
        }
        
        model.addAttribute("searchVO", searchVO);           // 검색항목
        model.addAttribute("pushGubunList", pushGubunList); // PUSH구분유형
        model.addAttribute("pushCampList",pushCampList);    // 캠페인목록
        model.addAttribute("deptList", deptList);           // 부서
        model.addAttribute("userList", userList);           // 사용자
        
        return "push/ana/pushCampListP";
    }
    
    /**
     * PUSH발송 현황 목록을 조회 력한다.
     * 
     * @param searchVO
     * @param model
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping(value = "/pushCampList")
    public String goPushCampList(@ModelAttribute PushSendLogVO searchVO, Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
        logger.debug("goPushCampList searchDeptNo     = " + searchVO.getSearchDeptNo());
        logger.debug("goPushCampList searchUserId     = " + searchVO.getSearchUserId());
        logger.debug("goPushCampList searchStartDt    = " + searchVO.getSearchStartDt());
        logger.debug("goPushCampList searchEndtDt     = " + searchVO.getSearchEndDt());
        logger.debug("goPushCampList searchPushGubun  = " + searchVO.getSearchPushGubun());
        logger.debug("goPushCampList searchCampNo     = " + searchVO.getSearchCampNo());
        logger.debug("goPushCampList searchPushName   = " + searchVO.getSearchPushName());
        
        // 검색 기본값 설정
        searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
        
        if(searchVO.getSearchStartDt() == null || "".equals(searchVO.getSearchStartDt())) {
            searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-1, "M", "yyyyMMdd"));
        } else {
            searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
        }
        
        if(searchVO.getSearchEndDt() == null || "".equals(searchVO.getSearchEndDt())) {
            searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
        } else {
            searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
        }
        
        if(searchVO.getSearchDeptNo() == 0) {
            if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
                searchVO.setSearchDeptNo(0);
            } else {
                searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
            }
        }
        searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
        
        // 페이지 설정
        int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
        int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
        searchVO.setPage(page);
        searchVO.setRows(rows);
        searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
        int totalCount = 0;
        
        // PUSH발송 내역 조회
        List<PushSendLogVO> pushSendList = null;
        try {
            pushSendList = pushAnalysisService.getPushCampList(searchVO);
        } catch(Exception e) {
            logger.error("pushAnalysisService.getPushCampList error = " + e);
        }
        
        if(pushSendList != null && pushSendList.size() > 0) {
            totalCount = pushSendList.get(0).getTotalCount();
        }
        
        PageUtil pageUtil = new PageUtil();
        pageUtil.init(request, searchVO.getPage(), totalCount, rows);
        
        model.addAttribute("searchVO", searchVO);           // 검색항목
        model.addAttribute("pushSendList", pushSendList);   // 항목
        model.addAttribute("pageUtil", pageUtil);           // 페이징
        
        return "push/ana/pushCampList";
    }
    
    
    /**
     * PUSH발송 현황 화면을 출력한다.
     * 
     * @param searchVO
     * @param model
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping(value="/pushCampSendListP")
    public String goPushCampSendListP(@ModelAttribute PushSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        logger.debug("goPushCampSendListP searchDeptNo     = " + searchVO.getSearchDeptNo());
        logger.debug("goPushCampSendListP searchUserId     = " + searchVO.getSearchUserId());
        logger.debug("goPushCampSendListP searchStartDt    = " + searchVO.getSearchStartDt());
        logger.debug("goPushCampSendListP searchEndtDt     = " + searchVO.getSearchEndDt());
        logger.debug("goPushCampSendListP searchPushGubun  = " + searchVO.getSearchPushGubun());
        logger.debug("goPushCampSendListP searchCampNo     = " + searchVO.getSearchCampNo());
        logger.debug("goPushCampSendListP searchPushName   = " + searchVO.getSearchPushName());
        
        // 검색 기본값 설정
        if(searchVO.getSearchStartDt() == null || "".equals(searchVO.getSearchStartDt())) {
            searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-1, "M", "yyyyMMdd"));
        } else {
            searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
        }
        if(searchVO.getSearchEndDt() == null || "".equals(searchVO.getSearchEndDt())) {
            searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
        } else {
            searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
        }
        if(searchVO.getSearchDeptNo() == 0) {
            if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
                searchVO.setSearchDeptNo(0);
            } else {
                searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
            }
        }
        searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
        
        // 전송유형 목록
        CodeVO gubun = new CodeVO();
        gubun.setUilang((String)session.getAttribute("NEO_UILANG"));
        gubun.setCdGrp("C124");
        gubun.setUseYn("Y"); 
        List<CodeVO> pushGubunList = null;
        try {
            pushGubunList = codeService.getCodeList(gubun);
        } catch(Exception e) {
            logger.error("codeService.getCodeListByUpCd[C124] error = " + e);
        }
        
        // 캠페인 목록 조회
        PushCampaignVO camp = new PushCampaignVO();
        camp.setUilang((String)session.getAttribute("NEO_UILANG"));
        camp.setSearchStatus("000");
        camp.setPage(1);
        camp.setRows(10000);
        camp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
        List<PushCampaignVO> pushCampList = null;
        try {
            pushCampList = pushCampaignService.getCampaignList(camp);
        } catch(Exception e) {
            logger.error("pushCampaignService.getCampaignList error = " + e);
        }
        
        // 부서 목록 조회
        CodeVO dept = new CodeVO();
        dept.setStatus("000"); // 정상
        List<CodeVO> deptList = null;
        try {
            deptList = codeService.getDeptList(dept);
        } catch(Exception e) {
            logger.error("codeService.getDeptList error = " + e);
        }
        
        // 사용자 목록 조회
        CodeVO user = new CodeVO();
        user.setDeptNo(searchVO.getSearchDeptNo());
        user.setStatus("000");
        List<CodeVO> userList = null;
        try {
            userList = codeService.getUserList(user);
        } catch(Exception e) {
            logger.error("codeService.getUserList error = " + e);
        }
        
        model.addAttribute("searchVO", searchVO);           // 검색항목
        model.addAttribute("pushGubunList", pushGubunList); // PUSH구분유형
        model.addAttribute("pushCampList",pushCampList);    // 캠페인목록
        model.addAttribute("deptList", deptList);           // 부서
        model.addAttribute("userList", userList);           // 사용자
        
        return "push/ana/pushCampSendListP";
    }
    
    /**
     * PUSH발송 현황 목록을 조회 력한다.
     * 
     * @param searchVO
     * @param model
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping(value = "/pushCampSendList")
    public String goPushCampSendList(@ModelAttribute PushSendLogVO searchVO, Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
        logger.debug("goPushCampSendList searchDeptNo     = " + searchVO.getSearchDeptNo());
        logger.debug("goPushCampSendList searchUserId     = " + searchVO.getSearchUserId());
        logger.debug("goPushCampSendList searchStartDt    = " + searchVO.getSearchStartDt());
        logger.debug("goPushCampSendList searchEndtDt     = " + searchVO.getSearchEndDt());
        logger.debug("goPushCampSendList searchPushGubun  = " + searchVO.getSearchPushGubun());
        logger.debug("goPushCampSendList searchCampNo     = " + searchVO.getSearchCampNo());
        logger.debug("goPushCampSendList searchPushName   = " + searchVO.getSearchPushName());
        
        // 검색 기본값 설정
        searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
        
        if(searchVO.getSearchStartDt() == null || "".equals(searchVO.getSearchStartDt())) {
            searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-1, "M", "yyyyMMdd"));
        } else {
            searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
        }
        
        if(searchVO.getSearchEndDt() == null || "".equals(searchVO.getSearchEndDt())) {
            searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
        } else {
            searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
        }
        
        if(searchVO.getSearchDeptNo() == 0) {
            if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
                searchVO.setSearchDeptNo(0);
            } else {
                searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
            }
        }
        searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
        
        // 페이지 설정
        int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
        int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
        searchVO.setPage(page);
        searchVO.setRows(rows);
        searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
        int totalCount = 0;
        
        // PUSH발송 내역 조회
        List<PushSendLogVO> pushSendList = null;
        try {
            pushSendList = pushAnalysisService.getPushList(searchVO);
        } catch(Exception e) {
            logger.error("pushAnalysisService.getPushList error = " + e);
        }
        
        if(pushSendList != null && pushSendList.size() > 0) {
            totalCount = pushSendList.get(0).getTotalCount();
        }
        
        PageUtil pageUtil = new PageUtil();
        pageUtil.init(request, searchVO.getPage(), totalCount, rows);
        
        model.addAttribute("searchVO", searchVO);           // 검색항목
        model.addAttribute("pushSendList", pushSendList);   // 항목
        model.addAttribute("pageUtil", pageUtil);           // 페이징
        
        return "push/ana/pushCampSendList";
    }
}
