/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.26
 * 설명 : 캠페인관리 Controller
 */
package kr.co.sict.ums.ems.cam.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import kr.co.sict.ums.com.service.CodeService;
import kr.co.sict.ums.com.service.CryptoService;
import kr.co.sict.ums.com.vo.CodeVO;
import kr.co.sict.ums.ems.ana.vo.SendLogVO;
import kr.co.sict.ums.ems.apr.vo.SecuApprovalLineVO;
import kr.co.sict.ums.ems.cam.service.CampaignService;
import kr.co.sict.ums.ems.cam.vo.ApprovalOrgVO;
import kr.co.sict.ums.ems.cam.vo.AttachVO;
import kr.co.sict.ums.ems.cam.vo.CampaignVO;
import kr.co.sict.ums.ems.cam.vo.LinkVO;
import kr.co.sict.ums.ems.cam.vo.SendTestLogVO;
import kr.co.sict.ums.ems.cam.vo.TaskVO;
import kr.co.sict.ums.ems.cam.vo.TestUserVO;
import kr.co.sict.ums.ems.cam.vo.WebAgentVO;
import kr.co.sict.ums.ems.seg.service.SegmentService;
import kr.co.sict.ums.sys.seg.service.SmsTemplateService;
import kr.co.sict.ums.ems.seg.vo.SegmentMemberVO;
import kr.co.sict.ums.ems.seg.vo.SegmentVO;
import kr.co.sict.ums.sys.seg.vo.SmsTemplateVO;
import kr.co.sict.ums.ems.tmp.service.TemplateService;
import kr.co.sict.ums.ems.tmp.vo.TemplateVO;
import kr.co.sict.ums.rns.svc.service.RnsServiceService;
import kr.co.sict.ums.rns.svc.vo.RnsServiceVO;
import kr.co.sict.ums.sys.acc.service.AccountService;
import kr.co.sict.ums.sys.acc.vo.UserVO;
import kr.co.sict.ums.sys.dbc.service.DBConnService;
import kr.co.sict.ums.sys.dbc.vo.DbConnVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.DBUtil;
import kr.co.sict.util.EncryptUtil;
import kr.co.sict.util.PageUtil;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;
import kr.co.sict.util.CrossScriptingFilter;

@Controller
@RequestMapping("/ems/cam")
public class CampaignController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private CampaignService campaignService;
	
	@Autowired
	private SegmentService segmentService;
	
	//@Autowired
	//private SmsTemplateService smsTemplateService;
	
	@Autowired
	private RnsServiceService rnsServiceService;
	
	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private AccountService systemService;
	
	@Autowired
	private CryptoService cryptoService;
	
	@Autowired
	private PropertiesUtil properties;
	
	@Autowired
	private DBConnService dbConnService;
	
	
	/**
	 * 캠페인관리 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campListP")
	public String goCampListP(@ModelAttribute CampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goCampListP searchCampNm  = " + searchVO.getSearchCampNm());
		logger.debug("goCampListP searchCampTy  = " + searchVO.getSearchCampTy());
		logger.debug("goCampListP searchStatus  = " + searchVO.getSearchStatus());
		logger.debug("goCampListP searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goCampListP searchEndDt   = " + searchVO.getSearchEndDt());
		logger.debug("goCampListP searchDeptNo  = " + searchVO.getSearchDeptNo());
		logger.debug("goCampListP searchUserId  = " + searchVO.getSearchUserId());
		
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
		searchVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		
		// 캠페인목적 목록
		CodeVO campTy = new CodeVO();
		campTy.setUilang((String)session.getAttribute("NEO_UILANG"));
		campTy.setCdGrp("C004");
		campTy.setUseYn("Y");
		List<CodeVO> campTyList = null;
		try {
			campTyList = codeService.getCodeList(campTy);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C004] error = " + e);
		}
		
		// 캠페인상태 목록
		CodeVO status = new CodeVO();
		status.setUilang((String)session.getAttribute("NEO_UILANG"));
		status.setCdGrp("C014");
		status.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(status);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C014] error = " + e);
		}
		
		// 부서목록(코드성) 조회
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
		
		
		model.addAttribute("searchVO", searchVO);			// 검색 항목
		model.addAttribute("campTyList", campTyList);		// 캠페인목적 목록
		model.addAttribute("statusList", statusList);		// 캠페인상태 목록
		model.addAttribute("deptList", deptList);			// 부서 목록
		model.addAttribute("userList", userList);			// 사용자 목록
		
		return "ems/cam/campListP";
	}
	
	/**
	 * 캠페인 목록 조회
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campList")
	public String goCampList(@ModelAttribute CampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goCampList searchCampNm  = " + searchVO.getSearchCampNm());
		logger.debug("goCampList searchCampTy  = " + searchVO.getSearchCampTy());
		logger.debug("goCampList searchStatus  = " + searchVO.getSearchStatus());
		logger.debug("goCampList searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goCampList searchEndDt   = " + searchVO.getSearchEndDt());
		logger.debug("goCampList searchDeptNo  = " + searchVO.getSearchDeptNo());
		logger.debug("goCampList searchUserId  = " + searchVO.getSearchUserId());
		

		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		/* 권한별 제어 제거
		if(searchVO.getSearchDeptNo() == 0) {
			if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo(0);
			} else {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		*/
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		/* 권한별 제어 제거
		searchVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		*/
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		// 캠페인 목록 조회
		List<CampaignVO> campaignList  = null;
		try {
			campaignList = campaignService.getCampaignList(searchVO);
		} catch(Exception e) {
			logger.error("campaignService.getCampaignList error = " + e);
		}
		
		
		if(campaignList != null && campaignList.size() > 0) {
			totalCount = campaignList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);

		model.addAttribute("searchVO", searchVO);			// 검색 항목
		model.addAttribute("campaignList", campaignList);	// 캠페인 목록
		model.addAttribute("pageUtil", pageUtil);			// 페이징
		
		return "ems/cam/campList";
	}

	
	/**
	 * 캠페인 정보 조회
	 * @param campaignVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campInfo")
	public ModelAndView getCampInfo(@ModelAttribute CampaignVO campaignVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getCampInfo campNo      = " + campaignVO.getCampNo());
		
		campaignVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		try {
			campaignVO = campaignService.getCampaignInfo(campaignVO);
		} catch(Exception e) {
			logger.error("campaignService.getCampaignInfo error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("campaign", campaignVO);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 캠페인 정보 등록
	 * @param campaignVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campAdd")
	public ModelAndView insertCampInfo(@ModelAttribute CampaignVO campaignVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("insertCampInfo campNm      = " + campaignVO.getCampNm());
		logger.debug("insertCampInfo campDesc      = " + campaignVO.getCampDesc());
		logger.debug("insertCampInfo campTy      = " + campaignVO.getCampTy());
		logger.debug("insertCampInfo status      = " + campaignVO.getStatus());
		logger.debug("insertCampInfo deptNo      = " + campaignVO.getDeptNo());
		logger.debug("insertCampInfo userId      = " + campaignVO.getUserId());
		campaignVO.setRegId((String)session.getAttribute("NEO_USER_ID"));
		campaignVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		int result = 0;
		try {
			result = campaignService.insertCampaignInfo(campaignVO);
		} catch(Exception e) {
			logger.error("campaignService.insertCampaignInfo error = " + e);
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
	 * 캠페인 정보 등록 화면을 출력한다.
	 * @param campaignVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campAddP")
	public String goCampAddP(@ModelAttribute CampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goCampAddP getSearchDeptNo = " + searchVO.getSearchDeptNo());
		
		// 캠페인목적 목록
		CodeVO campTy = new CodeVO();
		campTy.setUilang((String)session.getAttribute("NEO_UILANG"));
		campTy.setCdGrp("C004");
		campTy.setUseYn("Y");
		List<CodeVO> campTyList = null;
		try {
			campTyList = codeService.getCodeList(campTy);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C004] error = " + e);
		}
		
		// 캠페인상태 목록
		CodeVO status = new CodeVO();
		status.setUilang((String)session.getAttribute("NEO_UILANG"));
		status.setCdGrp("C014");
		status.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(status);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C014] error = " + e);
		}
		
		// 부서목록(코드성) 조회
		CodeVO dept = new CodeVO();
		dept.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(dept);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 사용자 목록 조회
		int deptNo = "Y".equals((String)session.getAttribute("NEO_ADMIN_YN")) ? 0 : (int)session.getAttribute("NEO_DEPT_NO");
		CodeVO user = new CodeVO();
		user.setDeptNo(deptNo);
		user.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(user);
		} catch(Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 캠페인목적 목록
		model.addAttribute("campTyList", campTyList);		// 캠페인목적 목록
		model.addAttribute("statusList", statusList);		// 캠페인상태 목록
		model.addAttribute("deptList", deptList);			// 부서 목록
		model.addAttribute("userList", userList);			// 사용자 목록
		
		return "ems/cam/campAddP";
	}
	
	/**
	 * 캠페인 정보 수정 화면을 출력한다.
	 * @param campaignVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campUpdateP")
	public String goCampUpdateP(@ModelAttribute CampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goCampUpdateP campNo = " + searchVO.getCampNo());
		
		// 캠페인 정보 조회
		CampaignVO campInfo = null;
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		try {
			campInfo = campaignService.getCampaignInfo(searchVO);
		} catch(Exception e) {
			logger.error("campaignService.getCampaignInfo error = " + e);
		}
		
		// 캠페인목적 목록
		CodeVO campTy = new CodeVO();
		campTy.setUilang((String)session.getAttribute("NEO_UILANG"));
		campTy.setCdGrp("C004");
		campTy.setUseYn("Y");
		List<CodeVO> campTyList = null;
		try {
			campTyList = codeService.getCodeList(campTy);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C004] error = " + e);
		}
		
		// 캠페인상태 목록
		CodeVO status = new CodeVO();
		status.setUilang((String)session.getAttribute("NEO_UILANG"));
		status.setCdGrp("C014");
		status.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(status);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C014] error = " + e);
		}
		
		// 부서목록(코드성) 조회
		CodeVO dept = new CodeVO();
		dept.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(dept);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 사용자 목록 조회
		int deptNo = campInfo==null?(int)session.getAttribute("NEO_DEPT_NO"):campInfo.getDeptNo();
		//int deptNo = "Y".equals((String)session.getAttribute("NEO_ADMIN_YN")) ? 0 : (int)session.getAttribute("NEO_DEPT_NO");
		CodeVO user = new CodeVO();
		user.setDeptNo(deptNo);
		user.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(user);
		} catch(Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("campInfo", campInfo);			// 캠페인 정보
		model.addAttribute("campTyList", campTyList);		// 캠페인목적 목록
		model.addAttribute("statusList", statusList);		// 캠페인상태 목록
		model.addAttribute("deptList", deptList);			// 부서 목록
		model.addAttribute("userList", userList);			// 사용자 목록
		
		return "ems/cam/campUpdateP";
	}
	
	/**
	 * 캠페인 정보 수정
	 * @param campaignVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campUpdate")
	public ModelAndView updateCampInfo(@ModelAttribute CampaignVO campaignVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateCampInfo campNo      = " + campaignVO.getCampNo());
		logger.debug("updateCampInfo campNm      = " + campaignVO.getCampNm());
		logger.debug("updateCampInfo campDesc      = " + campaignVO.getCampDesc());
		logger.debug("updateCampInfo campTy      = " + campaignVO.getCampTy());
		logger.debug("updateCampInfo status      = " + campaignVO.getStatus());
		logger.debug("updateCampInfo deptNo      = " + campaignVO.getDeptNo());
		logger.debug("updateCampInfo userId      = " + campaignVO.getUserId());
		campaignVO.setUpId((String)session.getAttribute("NEO_USER_ID"));
		campaignVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		int result = 0;
		try {
			result = campaignService.updateCampaignInfo(campaignVO);
		} catch(Exception e) {
			logger.error("campaignService.updateCampaignInfo error = " + e);
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
	 * 메일 관리 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailMainP")
	public String goMailMain(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailMain searchTaskNm     = " + searchVO.getSearchTaskNm());
		logger.debug("goMailMain searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goMailMain searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goMailMain searchUserId     = " + searchVO.getSearchUserId());
		logger.debug("goMailMain searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goMailMain searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("goMailMain searchStatus     = " + searchVO.getSearchStatus());
		logger.debug("goMailMain searchWorkStatus = " + searchVO.getSearchWorkStatus());
		logger.debug("goMailMain campNo           = " + searchVO.getCampNo());
		
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
		if(searchVO.getSearchStatus() == null || "".equals(searchVO.getSearchStatus())) searchVO.setSearchStatus("000");
		if(searchVO.getSearchDeptNo() == 0) {
			if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo(0);
			} else {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		searchVO.setPage(searchVO.getPage()==0?1:searchVO.getPage());
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		searchVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		
		// 캠페인 목록 조회
		CampaignVO campVO = new CampaignVO();
		campVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		campVO.setSearchStatus("000");
		campVO.setPage(1);
		campVO.setRows(10000);
		campVO.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<CampaignVO> campaignList = null;
		try {
			campaignList = campaignService.getCampaignList(campVO);
		} catch(Exception e) {
			logger.error("campaignService.getCampaignList error = " + e);
		}
		
		// 부서목록(코드성) 조회
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
		
		// 메일상태 목록 조회
		CodeVO status = new CodeVO();
		status.setUilang((String)session.getAttribute("NEO_UILANG"));
		status.setCdGrp("C023");
		status.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(status);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C023] error = " + e);
		}

		model.addAttribute("searchVO", searchVO);			// 검색 항목
		model.addAttribute("campaignList", campaignList);	// 캠페인 목록
		model.addAttribute("deptList", deptList);			// 부서 목록
		model.addAttribute("userList", userList);			// 사용자 목록
		model.addAttribute("statusList", statusList);		// 메일상태 목록
		
		return "ems/cam/mailMainP";
	}
	

	/**
	 * 단기메일 목록 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailListP")
	public String goMailListP(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailListP searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goMailListP searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("goMailListP searchTaskNm     = " + searchVO.getSearchTaskNm());
		logger.debug("goMailListP searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goMailListP searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goMailListP searchUserId     = " + searchVO.getSearchUserId());
		logger.debug("goMailListP searchStatus     = " + searchVO.getSearchStatus());
		logger.debug("goMailListP searchWorkStatus = " + searchVO.getSearchWorkStatus());
		logger.debug("goMailListP campNo           = " + searchVO.getCampNo());
		
		// 검색 기본값 설정
		if(StringUtil.isNull(searchVO.getSearchStartDt())) {
			searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-1, "M", "yyyyMMdd"));
		} else {
			searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		}
		if(StringUtil.isNull(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		}
		if(searchVO.getSearchDeptNo() == 0) {
			if(!"Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		searchVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		
		// 캠페인 목록
		CampaignVO campVO = new CampaignVO();
		campVO.setStatus("000");
		campVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		campVO.setDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<CampaignVO> campList = null;
		try {
			campList = codeService.getCampaignList(campVO);
		} catch(Exception e) {
			logger.error("codeService.getCampaignList error = " + e);
		}
		
		// 사용자그룹(부서) 목록
		CodeVO deptVO = new CodeVO();
		deptVO.setStatus("000");
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(deptVO);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 사용자 목록
		CodeVO userVO = new CodeVO();
		userVO.setDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		userVO.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch(Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		// 메일상태 목록
		CodeVO statusVO = new CodeVO();
		statusVO.setUilang("000");
		statusVO.setCdGrp("C023");
		statusVO.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(statusVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList error = " + e);
		}
		
		// 발송상태 목록
		CodeVO workStatusVO = new CodeVO();
		workStatusVO.setUilang("000");
		workStatusVO.setCdGrp("C101");
		workStatusVO.setUseYn("Y");
		List<CodeVO> workStatusList = null;
		try {
			workStatusList = codeService.getWorkStatusList(workStatusVO);
		} catch(Exception e) {
			logger.error("codeService.getWorkStatusList error = " + e);
		}
		
		// 고객정보 조회사유코드 조회
		CodeVO reasonVO = new CodeVO();
		reasonVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		reasonVO.setCdGrp("C102");
		reasonVO.setUseYn("Y");
		List<CodeVO> reasonList = null;
		try {
			reasonList = codeService.getCodeList(reasonVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C102] error = " + e);
		}

		model.addAttribute("searchVO", searchVO);				// 검색항목
		model.addAttribute("campList", campList);				// 캠페인목록
		model.addAttribute("deptList", deptList);				// 사용자그룹목록
		model.addAttribute("userList", userList);				// 사용자목록
		model.addAttribute("statusList", statusList);			// 메일상태목록
		model.addAttribute("workStatusList", workStatusList);	// 발송상태목록
		model.addAttribute("reasonList", reasonList);			// 고객정보 조회사유코드
		
		
		return "ems/cam/mailListP";
	}
	
	/**
	 * 단기메일 목록을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailList")
	public String goMailList(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailList searchTaskNm     = " + searchVO.getSearchTaskNm());
		logger.debug("goMailList searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goMailList searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goMailList searchUserId     = " + searchVO.getSearchUserId());
		logger.debug("goMailList searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goMailList searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("goMailList searchStatus     = " + searchVO.getSearchStatus());
		logger.debug("goMailList searchWorkStatus = " + searchVO.getSearchWorkStatus());
		logger.debug("goMailList campNo           = " + searchVO.getCampNo());
		
		// 검색 기본값 설정
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		searchVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		List<String> workStatusList = new ArrayList<String>();
		if(searchVO.getSearchWorkStatus() != null) {
			String[] workStatus = searchVO.getSearchWorkStatus().split(",");
			for(int i=0;i<workStatus.length;i++) {
				workStatusList.add(workStatus[i]);
			}
		}
		searchVO.setSearchWorkStatusList(workStatusList);
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;

		// 메일 목록 조회
		List<TaskVO> mailList = null;
		try {
			mailList = campaignService.getMailListOnetime(searchVO);
		} catch(Exception e) {
			logger.error("campaignService.getMailListOnetime error = " + e);
		}
		
		if(mailList != null && mailList.size() > 0) {
			totalCount = mailList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		pageUtil.setSubmitFunc("goPageNumMail");
		
		model.addAttribute("searchVO", searchVO);	// 검색 항목
		model.addAttribute("mailList", mailList);	// 메일 목록
		model.addAttribute("pageUtil", pageUtil);	// 페이징
		
		return "ems/cam/mailList";
	}
	
	/**
	 * 정기메일 등록현황 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskListP")
	public String goTaskListP(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTaskListP searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goTaskListP searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("goTaskListP searchTaskNm     = " + searchVO.getSearchTaskNm());
		logger.debug("goTaskListP searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goTaskListP searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goTaskListP searchUserId     = " + searchVO.getSearchUserId());
		logger.debug("goTaskListP searchStatus     = " + searchVO.getSearchStatus());
		logger.debug("goTaskListP campNo           = " + searchVO.getCampNo());
		
		// 검색 기본값 설정
		if(StringUtil.isNull(searchVO.getSearchStartDt())) {
			searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-1, "M", "yyyyMMdd"));
		} else {
			searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		}
		if(StringUtil.isNull(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		}
		if(searchVO.getSearchDeptNo() == 0) {
			if(!"Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		searchVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		
		// 메일유형 목록
		CodeVO sendRepeat = new CodeVO();
		sendRepeat.setUilang((String)session.getAttribute("NEO_UILANG"));
		sendRepeat.setCdGrp("C017");
		sendRepeat.setUseYn("Y");
		List<CodeVO> repeatList = null;
		try {
			repeatList = codeService.getCodeList(sendRepeat);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C021] error = " + e);
		}
		
		// 캠페인 목록
		CampaignVO campVO = new CampaignVO();
		campVO.setStatus("000");
		campVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		campVO.setDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<CampaignVO> campList = null;
		try {
			campList = codeService.getCampaignList(campVO);
		} catch(Exception e) {
			logger.error("codeService.getCampaignList error = " + e);
		}
		
		// 사용자그룹(부서) 목록
		CodeVO deptVO = new CodeVO();
		deptVO.setStatus("000");
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(deptVO);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 사용자 목록
		CodeVO userVO = new CodeVO();
		userVO.setDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		userVO.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch(Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		// 메일상태 목록
		CodeVO statusVO = new CodeVO();
		statusVO.setUilang("000");
		statusVO.setCdGrp("C023");
		statusVO.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(statusVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList error = " + e);
		}
		
		// 발송상태코드 목록
		CodeVO wStatus = new CodeVO();
		wStatus.setUilang((String)session.getAttribute("NEO_UILANG"));
		wStatus.setCdGrp("C101");
		wStatus.setUseYn("Y");
		List<CodeVO> workStatusList = null;
		try {
			workStatusList = codeService.getWorkStatusList(wStatus);
		} catch(Exception e) {
			logger.error("codeService.getWorkStatusList error = " + e);
		}
		
		// 고객정보 조회사유코드 조회
		CodeVO reasonVO = new CodeVO();
		reasonVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		reasonVO.setCdGrp("C102");
		reasonVO.setUseYn("Y");
		List<CodeVO> reasonList = null;
		try {
			reasonList = codeService.getCodeList(reasonVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C102] error = " + e);
		}

		model.addAttribute("searchVO", searchVO);				// 검색항목
		model.addAttribute("repeatList", repeatList);			// 메일유형목록
		model.addAttribute("campList", campList);				// 캠페인목록
		model.addAttribute("deptList", deptList);				// 사용자그룹목록
		model.addAttribute("userList", userList);				// 사용자목록
		model.addAttribute("statusList", statusList);			// 메일상태목록
		model.addAttribute("workStatusList", workStatusList);	// 발송상태목록
		model.addAttribute("reasonList", reasonList);			// 고객정보 조회사유코드
		
		return "ems/cam/taskListP";
	}
	
	/**
	 * 정기메일 등록현황 목록을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskList")
	public String goTaskList(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTaskList searchTaskNm      = " + searchVO.getSearchTaskNm());
		logger.debug("goTaskList searchCampNo      = " + searchVO.getSearchCampNo());
		logger.debug("goTaskList searchDeptNo      = " + searchVO.getSearchDeptNo());
		logger.debug("goTaskList searchUserId      = " + searchVO.getSearchUserId());
		logger.debug("goTaskList searchStartDt     = " + searchVO.getSearchStartDt());
		logger.debug("goTaskList searchEndDt       = " + searchVO.getSearchEndDt());
		logger.debug("goTaskList searchStatus      = " + searchVO.getSearchStatus());
		logger.debug("goTaskList campNo            = " + searchVO.getCampNo());
		logger.debug("goTaskListP searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goTaskListP searchSendRepeat = " + searchVO.getSearchSendRepeat());
		
		// 검색 기본값 설정
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		searchVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;

		// 메일 목록 조회
		List<TaskVO> mailList = null;
		try {
			mailList = campaignService.getMailListRepeat(searchVO);
		} catch(Exception e) {
			logger.error("campaignService.getMailListOnetime error = " + e);
		}
		
		if(mailList != null && mailList.size() > 0) {
			totalCount = mailList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		pageUtil.setSubmitFunc("goPageNumMail");
		
		model.addAttribute("searchVO", searchVO);	// 검색 항목
		model.addAttribute("mailList", mailList);	// 메일 목록
		model.addAttribute("pageUtil", pageUtil);	// 페이징
		
		return "ems/cam/taskList";
	}
	
	/**
	 * 메일을 발송승인 상태로 변경한다. 
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailAdmit")
	public ModelAndView updateMailAdmit(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateMailAdmit taskNo    = " + taskVO.getTaskNo());
		logger.debug("updateMailAdmit subTaskNo = " + taskVO.getSubTaskNo());
		taskVO.setRecoStatus("001");	// 승인여부:승인
		taskVO.setWorkStatus("001");	// 발송상태:발송승인
		taskVO.setExeUserId((String)session.getAttribute("NEO_USER_ID"));

		
		int result = 0;
		try {
			result = campaignService.updateMailAdmit(taskVO);
		} catch(Exception e) {
			logger.error("campaignService.updateMailAdmit error = " + e);
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
	 * 단기메일 등록 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailAddP")
	public String goMailAddP(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailAddP searchTaskNm     = " + searchVO.getSearchTaskNm());
		logger.debug("goMailAddP searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goMailAddP searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goMailAddP searchUserId     = " + searchVO.getSearchUserId());
		logger.debug("goMailAddP searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goMailAddP searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("goMailAddP searchStatus     = " + searchVO.getSearchStatus());
		logger.debug("goMailAddP searchWorkStatus = " + searchVO.getSearchWorkStatus());
		
		// 수신자정보머지키코드 목록
		CodeVO merge = new CodeVO();
		merge.setUilang((String)session.getAttribute("NEO_UILANG"));
		merge.setCdGrp("C001");
		merge.setUseYn("Y");
		List<CodeVO> mergeList = null;
		try {
			mergeList = codeService.getCodeList(merge);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C001] error = " + e);
		}
		
		// 채널코드 목록
		CodeVO channel = new CodeVO();
		channel.setUilang((String)session.getAttribute("NEO_UILANG"));
		channel.setCdGrp("C002");
		channel.setUseYn("Y");
		List<CodeVO> channelList = null;
		try {
			channelList = codeService.getCodeList(channel);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C002] error = " + e);
		}
		
		// 발송주기타입코드 목록
		CodeVO period = new CodeVO();
		period.setUilang((String)session.getAttribute("NEO_UILANG"));
		period.setCdGrp("C019");
		period.setUseYn("Y");
		List<CodeVO> periodList = null;
		try {
			periodList = codeService.getCodeList(period);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C019] error = " + e);
		}
		
		// 인코딩타입코드 목록
		CodeVO encoding = new CodeVO();
		encoding.setUilang((String)session.getAttribute("NEO_UILANG"));
		encoding.setCdGrp("C021");
		encoding.setUseYn("Y");
		List<CodeVO> encodingList = null;
		try {
			encodingList = codeService.getCodeList(encoding);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C021] error = " + e);
		}
		
		// 코드그룹목록(코드성) 조회 -- 매일문자셋
		CodeVO charset = new CodeVO();
		charset.setUilang((String)session.getAttribute("NEO_UILANG"));
		charset.setCdGrp("C022");
		charset.setUseYn("Y");
		List<CodeVO> charsetList = null;
		try {
			charsetList = codeService.getCodeList(charset);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[022] error = " + e);
		}
		
		// 사용자정보
		UserVO userInfo = new UserVO();
		userInfo.setUilang((String)session.getAttribute("NEO_UILANG"));
		userInfo.setUserId((String)session.getAttribute("NEO_USER_ID"));
		try {
			userInfo = systemService.getUserInfo(userInfo);
		} catch(Exception e) {
			logger.error("systemService.getUserInfo error = " + e);
		}
		
		// 캠페인 목록 조회
		CampaignVO camp = new CampaignVO();
		camp.setUilang((String)session.getAttribute("NEO_UILANG"));
		camp.setSearchStatus("000");
		camp.setPage(1);
		camp.setRows(10000);
		camp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<CampaignVO> campList = null;
		try {
			campList = campaignService.getCampaignList(camp);
		} catch(Exception e) {
			logger.error("campaignService.getCampaignList error = " + e);
		}
		
		// 수신자그룹(발송대상) 목록
		SegmentVO seg = new SegmentVO();
		seg.setUilang((String)session.getAttribute("NEO_UILANG"));
		seg.setSearchStatus("000");
		seg.setPage(1);
		seg.setRows(10000);
		seg.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		seg.setEmsuseYn("Y");
		List<SegmentVO> segList = null;
		try {
			segList = segmentService.getSegmentList(seg);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentList error = " + e);
		}
		
		// 템플릿 목록
		TemplateVO temp = new TemplateVO();
		temp.setUilang((String)session.getAttribute("NEO_UILANG"));
		temp.setSearchStatus("000");
		temp.setPage(1);
		temp.setRows(10000);
		temp.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		temp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<TemplateVO> tempList = null;
		try {
			tempList = templateService.getTemplateList(temp);
		} catch(Exception e) {
			logger.error("templateService.getTemplateList error = " + e);
		}
		
		
		// 캠페인정보
		CampaignVO campaignInfo = new CampaignVO();
		if(searchVO.getCampNo() != 0) {
			campaignInfo.setUilang((String)session.getAttribute("NEO_UILANG"));
			campaignInfo.setCampNo(searchVO.getCampNo());
			try {
				campaignInfo = campaignService.getCampaignInfo(campaignInfo);
			} catch(Exception e) {
				logger.error("campaignService.getCampaignInfo error = " + e);
			}
		}
		
		// 부서목록(코드성) 조회
		CodeVO dept = new CodeVO();
		dept.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(dept);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 발송결과종별 목록
		CodeVO sndTpeVO = new CodeVO();
		sndTpeVO.setUilang("000");
		sndTpeVO.setCdGrp("C107");
		sndTpeVO.setUseYn("Y");
		List<CodeVO> sndTpeList = null;
		try {
			sndTpeList = codeService.getWorkStatusList(sndTpeVO);
		} catch(Exception e) {
			logger.error("codeService.getWorkStatusList error = " + e);
		}
		
		// 고객정보 조회사유코드 조회
		CodeVO reasonVO = new CodeVO();
		reasonVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		reasonVO.setCdGrp("C102");
		reasonVO.setUseYn("Y");
		List<CodeVO> reasonList = null;
		try {
			reasonList = codeService.getCodeList(reasonVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C102] error = " + e);
		}
		
		// 마케팅수신동의유형
		CodeVO mktGb = new CodeVO();
		mktGb.setUilang((String)session.getAttribute("NEO_UILANG"));
		mktGb.setCdGrp("C114");
		mktGb.setUseYn("Y");
		List<CodeVO> mktGbList = null;
		try {
			mktGbList = codeService.getCodeList(mktGb);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C114] error = " + e);
		}
		
		// 최상위 조직목록 조회
		List<ApprovalOrgVO> orgList = null;
		try {
			orgList = campaignService.getOrgListLvl1();
		} catch(Exception e) {
			logger.error("campaignService.getOrgListLvl1 error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("mergeList", mergeList);			// 수신자정보머지키코드
		model.addAttribute("channelList", channelList);		// 채널코드
		model.addAttribute("periodList", periodList);		// 발송주기타입코드
		model.addAttribute("encodingList", encodingList);	// 인코딩타입코드
		model.addAttribute("userInfo", userInfo);			// 사용자정보
		model.addAttribute("campList", campList);			// 캠페인목록
		model.addAttribute("segList", segList);				// 발송대상(세그먼트) 목록
		model.addAttribute("tempList", tempList);			// 템플릿 목록
		model.addAttribute("deptList", deptList);			// 부서 목록
		model.addAttribute("sndTpeList", sndTpeList);		// 발송결과종별 목록
		model.addAttribute("reasonList", reasonList);		// 고객정보 조회사유코드
		model.addAttribute("mktGbList", mktGbList);			// 마케팅수신동의유형
		model.addAttribute("orgList", orgList);				// 최상위 조직 목록
		if(searchVO.getCampNo() != 0) {
			model.addAttribute("campaignInfo", campaignInfo);			// 캠페인정보
		}
		
		// 설정파일내용
		model.addAttribute("MP_FULL_URL", properties.getProperty("MP_FULL_URL"));
		model.addAttribute("SOCKET_TIME_OUT", properties.getProperty("SOCKET_TIME_OUT"));
		model.addAttribute("CONN_PER_CNT", properties.getProperty("CONN_PER_CNT"));
		model.addAttribute("RETRY_CNT", properties.getProperty("RETRY_CNT"));
		model.addAttribute("MAIL_ENCODING", properties.getProperty("MAIL_ENCODING"));
		
		//환경설정 관련 정보 조회용 항목 추가 
		String userInfoCharSetNm = "코드정보없음";
		String mailInfoCharSetNm = "코드정보없음";
		String mailInfoHeaderEncNm = "코드정보없음";
		String mailInfoBodyEncNm = "코드정보없음";
		String mailEncodingNm = "코드정보없음";
		
		if(userInfo !=null && !"".equals(userInfo.getCharsetNm())) {
			userInfoCharSetNm = userInfo.getCharsetNm();
		}
		
		if(encodingList != null && encodingList.size() >0 ) {
			for(int i = 0; i < encodingList.size() ; i++) {
				CodeVO encodingInfo  = new CodeVO();
				encodingInfo = (CodeVO) encodingList.get(i);
				if(encodingInfo.getCd().equals(properties.getProperty("MAIL_ENCODING"))){
					mailEncodingNm = encodingInfo.getCdNm();
				}
			}
		}
		
		model.addAttribute("MAIL_ENCODING_NM", mailEncodingNm);
		model.addAttribute("userInfo_charset_nm", userInfoCharSetNm);
		model.addAttribute("mailInfo_charset_nm", mailInfoCharSetNm);
		model.addAttribute("mailInfo_headerEnc_nm", mailInfoHeaderEncNm);
		model.addAttribute("mailInfo_bodyEnc_nm", mailInfoBodyEncNm);
		
		return "ems/cam/mailAddP";
	}
	
	/**
	 * 정기메일 등록 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskAddP")
	public String goTaskAddP(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTaskAddP searchTaskNm     = " + searchVO.getSearchTaskNm());
		logger.debug("goTaskAddP searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goTaskAddP searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goTaskAddP searchUserId     = " + searchVO.getSearchUserId());
		logger.debug("goTaskAddP searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goTaskAddP searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("goTaskAddP searchStatus     = " + searchVO.getSearchStatus());
		logger.debug("goTaskAddP searchWorkStatus = " + searchVO.getSearchWorkStatus());
		
		// 수신자정보머지키코드 목록
		CodeVO merge = new CodeVO();
		merge.setUilang((String)session.getAttribute("NEO_UILANG"));
		merge.setCdGrp("C001");
		merge.setUseYn("Y");
		List<CodeVO> mergeList = null;
		try {
			mergeList = codeService.getCodeList(merge);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C001] error = " + e);
		}
		
		// 채널코드 목록
		CodeVO channel = new CodeVO();
		channel.setUilang((String)session.getAttribute("NEO_UILANG"));
		channel.setCdGrp("C002");
		channel.setUseYn("Y");
		List<CodeVO> channelList = null;
		try {
			channelList = codeService.getCodeList(channel);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C002] error = " + e);
		}
		
		// 발송주기타입코드 목록
		CodeVO period = new CodeVO();
		period.setUilang((String)session.getAttribute("NEO_UILANG"));
		period.setCdGrp("C019");
		period.setUseYn("Y");
		List<CodeVO> periodList = null;
		try {
			periodList = codeService.getCodeList(period);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C019] error = " + e);
		}
		
		// 인코딩타입코드 목록
		CodeVO encoding = new CodeVO();
		encoding.setUilang((String)session.getAttribute("NEO_UILANG"));
		encoding.setCdGrp("C021");
		encoding.setUseYn("Y");
		List<CodeVO> encodingList = null;
		try {
			encodingList = codeService.getCodeList(encoding);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C021] error = " + e);
		}
		
		// 코드그룹목록(코드성) 조회 -- 매일문자셋
		CodeVO charset = new CodeVO();
		charset.setUilang((String)session.getAttribute("NEO_UILANG"));
		charset.setCdGrp("C022");
		charset.setUseYn("Y");
		List<CodeVO> charsetList = null;
		try {
			charsetList = codeService.getCodeList(charset);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[022] error = " + e);
		}
		
		// 사용자정보
		UserVO userInfo = new UserVO();
		userInfo.setUilang((String)session.getAttribute("NEO_UILANG"));
		userInfo.setUserId((String)session.getAttribute("NEO_USER_ID"));
		try {
			userInfo = systemService.getUserInfo(userInfo);
		} catch(Exception e) {
			logger.error("systemService.getUserInfo error = " + e);
		}
		
		// 캠페인 목록 조회
		CampaignVO camp = new CampaignVO();
		camp.setUilang((String)session.getAttribute("NEO_UILANG"));
		camp.setSearchStatus("000");
		camp.setPage(1);
		camp.setRows(10000);
		camp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<CampaignVO> campList = null;
		try {
			campList = campaignService.getCampaignList(camp);
		} catch(Exception e) {
			logger.error("campaignService.getCampaignList error = " + e);
		}
		
		// 수신자그룹(발송대상) 목록
		SegmentVO seg = new SegmentVO();
		seg.setUilang((String)session.getAttribute("NEO_UILANG"));
		seg.setSearchStatus("000");
		seg.setPage(1);
		seg.setRows(10000);
		seg.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		seg.setEmsuseYn("Y");
		List<SegmentVO> segList = null;
		try {
			segList = segmentService.getSegmentList(seg);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentList error = " + e);
		}
		
		// 템플릿 목록
		TemplateVO temp = new TemplateVO();
		temp.setUilang((String)session.getAttribute("NEO_UILANG"));
		temp.setSearchStatus("000");
		temp.setPage(1);
		temp.setRows(10000);
		temp.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		temp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<TemplateVO> tempList = null;
		try {
			tempList = templateService.getTemplateList(temp);
		} catch(Exception e) {
			logger.error("templateService.getTemplateList error = " + e);
		}
		
		// 캠페인정보
		CampaignVO campaignInfo = new CampaignVO();
		if(searchVO.getCampNo() != 0) {
			campaignInfo.setUilang((String)session.getAttribute("NEO_UILANG"));
			campaignInfo.setCampNo(searchVO.getCampNo());
			try {
				campaignInfo = campaignService.getCampaignInfo(campaignInfo);
			} catch(Exception e) {
				logger.error("campaignService.getCampaignInfo error = " + e);
			}
		}
		
		// 부서목록(코드성) 조회
		CodeVO dept = new CodeVO();
		dept.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(dept);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 발송결과종별 목록
		CodeVO sndTpeVO = new CodeVO();
		sndTpeVO.setUilang("000");
		sndTpeVO.setCdGrp("C107");
		sndTpeVO.setUseYn("Y");
		List<CodeVO> sndTpeList = null;
		try {
			sndTpeList = codeService.getWorkStatusList(sndTpeVO);
		} catch(Exception e) {
			logger.error("codeService.getWorkStatusList error = " + e);
		}
		
		// 마케팅수신동의유형
		CodeVO mktGb = new CodeVO();
		mktGb.setUilang((String)session.getAttribute("NEO_UILANG"));
		mktGb.setCdGrp("C114");
		mktGb.setUseYn("Y");
		List<CodeVO> mktGbList = null;
		try {
			mktGbList = codeService.getCodeList(mktGb);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C114] error = " + e);
		}
		
		// 고객정보 조회사유코드 조회
		CodeVO reasonVO = new CodeVO();
		reasonVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		reasonVO.setCdGrp("C102");
		reasonVO.setUseYn("Y");
		List<CodeVO> reasonList = null;
		try {
			reasonList = codeService.getCodeList(reasonVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C102] error = " + e);
		}
		
		// 최상위 조직목록 조회
		List<ApprovalOrgVO> orgList = null;
		try {
			orgList = campaignService.getOrgListLvl1();
		} catch(Exception e) {
			logger.error("campaignService.getOrgListLvl1 error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("mergeList", mergeList);			// 수신자정보머지키코드
		model.addAttribute("channelList", channelList);		// 채널코드
		model.addAttribute("periodList", periodList);		// 발송주기타입코드
		model.addAttribute("encodingList", encodingList);	// 인코딩타입코드
		model.addAttribute("userInfo", userInfo);			// 사용자정보
		model.addAttribute("campList", campList);			// 캠페인목록
		model.addAttribute("segList", segList);				// 발송대상(세그먼트) 목록
		model.addAttribute("tempList", tempList);			// 템플릿 목록
		model.addAttribute("deptList", deptList);			// 부서 목록
		model.addAttribute("sndTpeList", sndTpeList);		// 발송결과종별 목록
		model.addAttribute("mktGbList", mktGbList);			// 마케팅수신동의유형
		model.addAttribute("reasonList", reasonList);		// 고객정보 조회사유코드
		model.addAttribute("orgList", orgList);				// 최상위 조직 목록
		if(searchVO.getCampNo() != 0) {
			model.addAttribute("campaignInfo", campaignInfo);			// 캠페인정보
		}
		
		
		// 설정파일내용
		model.addAttribute("MP_FULL_URL", properties.getProperty("MP_FULL_URL"));
		model.addAttribute("SOCKET_TIME_OUT", properties.getProperty("SOCKET_TIME_OUT"));
		model.addAttribute("CONN_PER_CNT", properties.getProperty("CONN_PER_CNT"));
		model.addAttribute("RETRY_CNT", properties.getProperty("RETRY_CNT"));
		model.addAttribute("MAIL_ENCODING", properties.getProperty("MAIL_ENCODING"));
		model.addAttribute("DEFAULT_DOMAIN", properties.getProperty("IMG.DEFAULT_DOMAIN"));
		
		//환경설정 관련 정보 조회용 항목 추가 
		String userInfoCharSetNm = "코드정보없음";
		String mailInfoCharSetNm = "코드정보없음";
		String mailInfoHeaderEncNm = "코드정보없음";
		String mailInfoBodyEncNm = "코드정보없음";
		String mailEncodingNm = "코드정보없음";
		
		if(userInfo !=null && !"".equals(userInfo.getCharsetNm())) {
			userInfoCharSetNm = userInfo.getCharsetNm();
		}
		
		if(encodingList != null && encodingList.size() >0 ) {
			for(int i = 0; i < encodingList.size() ; i++) {
				CodeVO encodingInfo  = new CodeVO();
				encodingInfo = (CodeVO) encodingList.get(i);
				if(encodingInfo.getCd().equals(properties.getProperty("MAIL_ENCODING"))){
					mailEncodingNm = encodingInfo.getCdNm();
				}
			}
		}
		
		model.addAttribute("MAIL_ENCODING_NM", mailEncodingNm);
		model.addAttribute("userInfo_charset_nm", userInfoCharSetNm);
		model.addAttribute("mailInfo_charset_nm", mailInfoCharSetNm);
		model.addAttribute("mailInfo_headerEnc_nm", mailInfoHeaderEncNm);
		model.addAttribute("mailInfo_bodyEnc_nm", mailInfoBodyEncNm);
		
		return "ems/cam/taskAddP";
	}
	
	/**
	 * 메일 정보를 등록한다.
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailAdd")
	public String goMailAdd(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailAdd deptNo     = " + taskVO.getDeptNo());
		logger.debug("goMailAdd userId     = " + taskVO.getUserId());
		logger.debug("goMailAdd campInfo   = " + taskVO.getCampInfo());
		logger.debug("goMailAdd segNo      = " + taskVO.getSegNo());
		logger.debug("goMailAdd contTy     = " + taskVO.getContTy());
		logger.debug("goMailAdd linkYn     = " + taskVO.getLinkYn());
		logger.debug("goMailAdd attachNm   = " + taskVO.getAttachNm());
		logger.debug("goMailAdd attachPath = " + taskVO.getAttachPath());
		
		//XSS Filter 주석 막음 
		/*
		if (CrossScriptingFilter.existScript(request, taskVO.getComposerValue())) {
			model.addAttribute("result","filter");  
			return "ems/cam/mailAdd";
		}
		*/
		
		List<AttachVO> attachList = new ArrayList<AttachVO>();		// 첨부파일 목록
		List<LinkVO> linkList = new ArrayList<LinkVO>();			// 링크 목록
		
		// 파일 사이즈 체크
		if(taskVO.getAttachPath() != null && !"".equals(taskVO.getAttachPath())) {
			File attachFile = null;
			String basePath = properties.getProperty("FILE.UPLOAD_PATH") + "/attach";
			
			long fileSize = 0;
			String[] fileNm = taskVO.getAttachNm().split(",");
			String[] filePath = taskVO.getAttachPath().split(",");
			taskVO.setAttCnt(filePath.length);		// 첨부파일 수 설정
			for(int i=0;i<filePath.length;i++) {
				attachFile = new File(basePath + "/" + filePath[i]);
				fileSize += attachFile.length();
				
				AttachVO attach = new AttachVO();
				attach.setAttNm(fileNm[i]);
				attach.setAttFlPath("attach/" + filePath[i]);
				attach.setAttFlSize(attachFile.length());
				
				attachList.add(attach);
			}
			
			// 합계 파일 사이즈가 10MB 이상인 경우 중지 
			if(fileSize > 10485760) {	// 10MB이상
				model.addAttribute("result","Fail");
				model.addAttribute("FILE_SIZE","EXCESS");
				return "ems/cam/mailAdd";
			}
		}

		// 기본값 설정
		if(StringUtil.isNull(taskVO.getCampTy())) taskVO.setCampTy("");												// 캠페인유형
		if(StringUtil.isNull(taskVO.getNmMerge())) taskVO.setNmMerge("");											// $:nm_merge:$형태로 넘어옴
		if(StringUtil.isNull(taskVO.getIdMerge())) taskVO.setIdMerge("");											// $:id_merge:$형태로 넘어옴
		if(StringUtil.isNull(taskVO.getChannel())) taskVO.setChannel("000");										// 채널코드
		if(StringUtil.isNull(taskVO.getContTy())) taskVO.setContTy("000");											// 편집모드(기본 HTML형식)
		
		if(taskVO.getDeptNo() == 0) taskVO.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));						// 부서번호
		if(StringUtil.isNull(taskVO.getUserId())) taskVO.setUserId((String)session.getAttribute("NEO_USER_ID"));	// 사용자아이디
		if(StringUtil.isNull(taskVO.getPlanUserId())) taskVO.setPlanUserId("");
		if(!StringUtil.isNull(taskVO.getSegNoc())) taskVO.setSegNo(Integer.parseInt( taskVO.getSegNoc().substring(0, taskVO.getSegNoc().indexOf("|")) ));		// 세그먼트번호(발송대상그룹)
		if(StringUtil.isNull(taskVO.getRespYn())) taskVO.setRespYn("0");											// 수신확인
		if(StringUtil.isNull(taskVO.getTaskNm())) taskVO.setTaskNm("");												// 메일명
		if(StringUtil.isNull(taskVO.getMailTitle())) taskVO.setMailTitle("");										// 메일제목
		if(StringUtil.isNull(taskVO.getSendYmd())) taskVO.setSendYmd("0000.00.00");									// 예약시간(예약일)
		String sendHour = StringUtil.setTwoDigitsString(taskVO.getSendHour());										// 예약시간(시)
		String sendMin = StringUtil.setTwoDigitsString(taskVO.getSendMin());										// 예약시간(분)
		taskVO.setSendDt( taskVO.getSendYmd().replaceAll("\\.", "") + sendHour + sendMin );							// 예약일시
		taskVO.setRespEndDt("999999999999");
		if(StringUtil.isNull(taskVO.getIsSendTerm())) taskVO.setIsSendTerm("N");									// 정기발송체크여부
		if(StringUtil.isNull(taskVO.getSendTermEndDt())) taskVO.setSendTermEndDt("0000.00.00");						// 정기발송종료일
		if(StringUtil.isNull(taskVO.getSendTermLoop())) taskVO.setSendTermLoop("");									// 정기발송주기
		if(StringUtil.isNull(taskVO.getSendTermLoopTy())) taskVO.setSendTermLoopTy("");								// 정기발송주기유형
		if(StringUtil.isNull(taskVO.getSendTestYn())) taskVO.setSendTestYn("N");
		if(StringUtil.isNull(taskVO.getSendTestEm())) taskVO.setSendTestEm("");
		if(StringUtil.isNull(taskVO.getComposerValue())) taskVO.setComposerValue("");								// 메일내용
		if("Y".equals(taskVO.getIsSendTerm())) {
			taskVO.setSendTermEndDt( taskVO.getSendTermEndDt().replaceAll("\\.", "") + "2359" );						// 정기발송종료일
			taskVO.setSendRepeat("001");
		} else {
			taskVO.setSendTermEndDt("");
			taskVO.setSendTermLoop("");
			taskVO.setSendTermLoopTy("");
			taskVO.setSendRepeat("000");
		}
		if(StringUtil.isNull(taskVO.getLinkYn())) taskVO.setLinkYn("N");											// 링크클릭
		taskVO.setRegId((String)session.getAttribute("NEO_USER_ID"));
		taskVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		if(StringUtil.isNull(taskVO.getWebAgentAttachYn())) taskVO.setWebAgentAttachYn("N");						// WebAgent 첨부파일로 지정
		
		if(StringUtil.isNull(taskVO.getSendMode())) taskVO.setSendMode("");
		if(StringUtil.isNull(taskVO.getMailFromNm())) taskVO.setMailFromNm("");
		if(StringUtil.isNull(taskVO.getMailFromEm())) taskVO.setMailFromEm("");
		if(StringUtil.isNull(taskVO.getReplyToEm())) taskVO.setReplyToEm("");
		if(StringUtil.isNull(taskVO.getReturnEm())) taskVO.setReturnEm("");
		
		if(StringUtil.isNull(taskVO.getHeaderEnc())) taskVO.setHeaderEnc("");
		if(StringUtil.isNull(taskVO.getBodyEnc())) taskVO.setBodyEnc("");
		if(StringUtil.isNull(taskVO.getCharset())) taskVO.setCharset("");
		
		taskVO.setStatus("000");
		taskVO.setRecoStatus("000");
		if(StringUtil.isNull(taskVO.getApprUserId())) {
			taskVO.setWorkStatus("000");
		} else {
			taskVO.setWorkStatus("201");
		}
		
		taskVO.setSendIp(properties.getProperty("SEND_IP"));
		
		// 링크클릭 체크한 경우( 광주 프로젝트에서 빠짐 )
		if("Y".equals(taskVO.getLinkYn()) && "000".equals(taskVO.getContTy())) {
			// 링크 클릭 알리아싱 처리(mailAliasParser.jsp 내용 처리)
			List<LinkVO> dataList = null;

			// 수신자정보머지키코드 목록
			CodeVO merge = new CodeVO();
			merge.setUilang((String)session.getAttribute("NEO_UILANG"));
			merge.setCdGrp("C001");
			merge.setUseYn("Y");
			List<CodeVO> mergeList = null;
			try {
				mergeList = codeService.getCodeList(merge);
			} catch(Exception e) {
				logger.error("codeService.getCodeList[C001] error = " + e);
			}
			
			try {
				// 링크 클릭 알리아스 처리
				dataList = mailAliasParser(taskVO, mergeList, properties);
			} catch(Exception e) {
				logger.error("campaignService.mailAliasParser error = "+ e);
			}
			
			if(dataList != null && dataList.size() > 0) {
				for(LinkVO data:dataList) {
					LinkVO link = new LinkVO();
					
					link.setLinkTy(data.getLinkTy());
					link.setLinkUrl(data.getLinkUrl());
					link.setLinkNm(data.getLinkNm());
					link.setLinkNo(data.getLinkNo());
					link.setRegId((String)session.getAttribute("NEO_USER_ID"));
					link.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
					linkList.add(link);
				}
			}
		}

		// 수신확인 클릭한 경우
		if("Y".equals(taskVO.getRespYn())) {
			// send_dt와 p_resp_log를 이용하여 수신 종료 응답시간을 구한다.
			if(taskVO.getRespLog() != 0) { // respLog : 수신확인추적기간
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
				cal.set(Integer.parseInt(taskVO.getSendYmd().substring(0,4)), Integer.parseInt(taskVO.getSendYmd().substring(5,7))-1, Integer.parseInt(taskVO.getSendYmd().substring(8,10)));
				cal.add(Calendar.DATE, taskVO.getRespLog());
				taskVO.setRespEndDt(fmt.format(cal.getTime()) +  StringUtil.setTwoDigitsString(taskVO.getSendHour()) + StringUtil.setTwoDigitsString(taskVO.getSendMin()));
			} else {
				taskVO.setRespEndDt("999999999999");
			}
			
			String tmpCompose = taskVO.getComposerValue();
			int pos = tmpCompose.toLowerCase().indexOf("</body>");
			
			String strResponse = "<!--NEO__RESPONSE__START-->";
			//2022.11.16 수신확인 관련 수정 CAMP_TY과 CAMP_NO 순서바꿈
			//strResponse += "<img src=\""+properties.getProperty("RESPONSE_URL")+"?$:RESP_END_DT:$|000|"+taskVO.getIdMerge()+"|$:TASK_NO:$|$:SUB_TASK_NO:$|$:DEPT_NO:$|$:USER_ID:$|$:CAMP_TY:$|$:CAMP_NO:$\" width=0 height=0 border=0>";
			strResponse += "<img src=\""+properties.getProperty("RESPONSE_URL")+"?$:RESP_END_DT:$|000|"+taskVO.getIdMerge()+"|$:TASK_NO:$|$:SUB_TASK_NO:$|$:DEPT_NO:$|$:USER_ID:$|$:CAMP_NO:$|$:CAMP_TY:$\" width=0 height=0 border=0>";
			strResponse += "<!--NEO__RESPONSE__END-->";
			if(pos == -1) taskVO.setComposerValue(strResponse + taskVO.getComposerValue()) ;
			else taskVO.setComposerValue( strResponse + taskVO.getComposerValue().substring(0, pos) + taskVO.getComposerValue().substring(pos));
		}
		
		// 컬럼 암호화 설정
		taskVO.setMailFromEm( cryptoService.getEncrypt("MAIL_FROM_EM",taskVO.getMailFromEm()) );
		taskVO.setReplyToEm( cryptoService.getEncrypt("REPLY_TO_EM",taskVO.getReplyToEm()) );
		taskVO.setReturnEm( cryptoService.getEncrypt("RETURN_EM",taskVO.getReturnEm()) );
		
		// 메일 작성내용 파일로 생성(파일 생성 전 디렉토리 생성)
		taskVO.getComposerValue();
		String contFlPath = "content/" + (String)session.getAttribute("NEO_USER_ID") + "/" + StringUtil.getDate(Code.TM_YMDHMSM) + ".tmp";
		taskVO.setContFlPath(contFlPath);
		String basePath = properties.getProperty("FILE.UPLOAD_PATH");
		String filePath = basePath + "/" + contFlPath;
		String dirPath = basePath + "/content/" + (String)session.getAttribute("NEO_USER_ID");
		File fileDir = new File(dirPath);
		if(!fileDir.exists()) {
			fileDir.mkdir();
		}
		
		FileOutputStream fos = null;
		OutputStreamWriter writer = null;
		try {
			File contentFile = new File(filePath);
			fos = new FileOutputStream(contentFile);
			writer = new OutputStreamWriter(fos, "UTF-8");
			writer.write(taskVO.getComposerValue());
			writer.flush();
		} catch(Exception e) {
			logger.error("mailAdd File Write Error = " + e);
		} finally {
			if(writer != null) try { writer.close(); } catch(Exception e) {}
			if(fos != null) try { fos.close(); } catch(Exception e) {}
		}
		
		int result = 0;
		// 메일 정보 등록(NEO_TASK, NEO_SUBTASK, NEO_ATTACH, NEO_LINK), 추가(웹에이전트, 발송결재라인)
		try {
			result = campaignService.insertMailInfo(taskVO, attachList, linkList);
		} catch(Exception e) {
			logger.error("campaignService.insertMailInfo error = " + e);
		}
		
		if(result > 0) {
			model.addAttribute("result","Success");
			/*
			 * model.addAttribute("sendRepeat", taskVO.getSendRepeat());
			 * model.addAttribute("taskNo", taskVO.getTaskNo());
			 * model.addAttribute("subTaskNo", taskVO.getSubTaskNo());
			 */
		} else {
			model.addAttribute("result","Fail");
		}
		
		return "ems/cam/mailAdd";
	}
	
	/**
	 * 메일 정보 수정 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailUpdateP")
	public String goMailUpdateP(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailUpdateP searchTaskNm     = " + searchVO.getSearchTaskNm());
		logger.debug("goMailUpdateP searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goMailUpdateP searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goMailUpdateP searchUserId     = " + searchVO.getSearchUserId());
		logger.debug("goMailUpdateP searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goMailUpdateP searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("goMailUpdateP searchStatus     = " + searchVO.getSearchStatus());
		logger.debug("goMailUpdateP searchWorkStatus = " + searchVO.getSearchWorkStatus());
		logger.debug("goMailUpdateP taskNo           = " + searchVO.getTaskNo());
		logger.debug("goMailUpdateP subTaskNo        = " + searchVO.getSubTaskNo());
		
		// 수신자정보머지키코드 목록
		CodeVO merge = new CodeVO();
		merge.setUilang((String)session.getAttribute("NEO_UILANG"));
		merge.setCdGrp("C001");
		merge.setUseYn("Y");
		List<CodeVO> mergeList = null;
		try {
			mergeList = codeService.getCodeList(merge);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C001] error = " + e);
		}
		
		// 채널코드 목록
		CodeVO channel = new CodeVO();
		channel.setUilang((String)session.getAttribute("NEO_UILANG"));
		channel.setCdGrp("C002");
		channel.setUseYn("Y");
		List<CodeVO> channelList = null;
		try {
			channelList = codeService.getCodeList(channel);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C002] error = " + e);
		}
		
		// 발송주기타입코드 목록
		CodeVO period = new CodeVO();
		period.setUilang((String)session.getAttribute("NEO_UILANG"));
		period.setCdGrp("C019");
		period.setUseYn("Y");
		List<CodeVO> periodList = null;
		try {
			periodList = codeService.getCodeList(period);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C019] error = " + e);
		}
		
		// 메일 상세정보 조회
		TaskVO mailInfo = new TaskVO();
		mailInfo.setTaskNo(searchVO.getTaskNo());
		mailInfo.setSubTaskNo(searchVO.getSubTaskNo());
		try {
			mailInfo = campaignService.getMailInfo(mailInfo);
		} catch(Exception e) {
			logger.error("campaignService.getMailInfo error = " + e);
		}
		
		// 캠페인정보
		CampaignVO campaignInfo = new CampaignVO();
		campaignInfo.setUilang((String)session.getAttribute("NEO_UILANG"));
		campaignInfo.setCampNo(mailInfo.getCampNo());
		try {
			campaignInfo = campaignService.getCampaignInfo(campaignInfo);
		} catch(Exception e) {
			logger.error("campaignService.getCampaignInfo error = " + e);
		}
		
		// 캠페인 목록 조회
		CampaignVO camp = new CampaignVO();
		camp.setUilang((String)session.getAttribute("NEO_UILANG"));
		camp.setSearchStatus("000");
		camp.setPage(1);
		camp.setRows(10000);
		camp.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		camp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<CampaignVO> campList = null;
		try {
			campList = campaignService.getCampaignList(camp);
		} catch(Exception e) {
			logger.error("campaignService.getCampaignList error = " + e);
		}
		
		// 수신자그룹(발송대상) 목록
		SegmentVO seg = new SegmentVO();
		seg.setUilang((String)session.getAttribute("NEO_UILANG"));
		seg.setSearchStatus("000");
		seg.setPage(1);
		seg.setRows(10000);
		seg.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		seg.setEmsuseYn("Y");
		List<SegmentVO> segList = null;
		try {
			segList = segmentService.getSegmentList(seg);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentList error = " + e);
		}
		
		// 템플릿 목록
		TemplateVO temp = new TemplateVO();
		temp.setUilang((String)session.getAttribute("NEO_UILANG"));
		temp.setSearchStatus("000");
		temp.setPage(1);
		temp.setRows(10000);
		temp.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		temp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<TemplateVO> tempList = null;
		try {
			tempList = templateService.getTemplateList(temp);
		} catch(Exception e) {
			logger.error("templateService.getTemplateList error = " + e);
		}
		
		// 첨부파일 목록
		List<AttachVO> attachList = null;
		try {
			attachList = campaignService.getAttachList(searchVO.getTaskNo());
		} catch(Exception e) {
			logger.error("campaignService.getAttachList error = " + e);
		}
		
		// 부서목록(코드성) 조회
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
		user.setDeptNo(mailInfo.getDeptNo());
		user.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(user);
		} catch(Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		// 발송결재라인 목록
		List<SecuApprovalLineVO> apprLineList = null;
		try {
			apprLineList = campaignService.getApprovalLineList(searchVO.getTaskNo());
		} catch(Exception e) {
			logger.error("campaignService.getApprovalLineList error = " + e);
		}
		
		// 웹에이전트 정보
		WebAgentVO webAgent = null;
		try {
			webAgent = campaignService.getWebAgentInfo(searchVO.getTaskNo());
		} catch(Exception e) {
			logger.error("campaignService.getWebAgentInfo error = " + e);
		}
		
		// 발송결과종별 목록
		CodeVO sndTpeVO = new CodeVO();
		sndTpeVO.setUilang("000");
		sndTpeVO.setCdGrp("C107");
		sndTpeVO.setUseYn("Y");
		List<CodeVO> sndTpeList = null;
		try {
			sndTpeList = codeService.getWorkStatusList(sndTpeVO);
		} catch(Exception e) {
			logger.error("codeService.getWorkStatusList error = " + e);
		}
		
		// 마케팅수신동의유형
		CodeVO mktGb = new CodeVO();
		mktGb.setUilang((String)session.getAttribute("NEO_UILANG"));
		mktGb.setCdGrp("C114");
		mktGb.setUseYn("Y");
		List<CodeVO> mktGbList = null;
		try {
			mktGbList = codeService.getCodeList(mktGb);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C114] error = " + e);
		}
		
		// 고객정보 조회사유코드 조회
		CodeVO reasonVO = new CodeVO();
		reasonVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		reasonVO.setCdGrp("C102");
		reasonVO.setUseYn("Y");
		List<CodeVO> reasonList = null;
		try {
			reasonList = codeService.getCodeList(reasonVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C102] error = " + e);
		}
		
		// 최상위 조직목록 조회
		List<ApprovalOrgVO> orgList = null;
		try {
			orgList = campaignService.getOrgListLvl1();
		} catch(Exception e) {
			logger.error("campaignService.getOrgListLvl1 error = " + e);
		}
		
		// 메일 내용
		String compVal = getContFileText(mailInfo.getContFlPath());
		
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("mergeList", mergeList);			// 수신자정보머지키코드
		model.addAttribute("channelList", channelList);		// 채널코드
		model.addAttribute("periodList", periodList);		// 발송주기타입코드
		model.addAttribute("mailInfo", mailInfo);			// 메일상세정보
		model.addAttribute("campaignInfo", campaignInfo);	// 캠페인정보
		model.addAttribute("campList", campList);			// 캠페인목록
		model.addAttribute("segList", segList);				// 발송대상(세그먼트) 목록
		model.addAttribute("tempList", tempList);			// 템플릿 목록
		model.addAttribute("attachList", attachList);		// 첨부파일 목록
		model.addAttribute("deptList", deptList);			// 부서 목록
		model.addAttribute("userList", userList);			// 사용자 목록
		model.addAttribute("webAgent", webAgent);			// 웹에이전트 목록
		model.addAttribute("apprLineList", apprLineList);	// 발송결재라인 목록
		model.addAttribute("sndTpeList", sndTpeList);		// 발송결과종별 목록
		model.addAttribute("mktGbList", mktGbList);			// 마케팅수신동의유형
		model.addAttribute("reasonList", reasonList);		// 조회사유코드 목록
		model.addAttribute("orgList", orgList);				// 조직목록
		
		model.addAttribute("compVal", compVal);				// 메일 내용
		
		return "ems/cam/mailUpdateP";
	}
	
	/**
	 * 메일 정보 수정 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskUpdateP")
	public String goTaskUpdateP(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTaskUpdateP searchTaskNm     = " + searchVO.getSearchTaskNm());
		logger.debug("goTaskUpdateP searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goTaskUpdateP searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goTaskUpdateP searchUserId     = " + searchVO.getSearchUserId());
		logger.debug("goTaskUpdateP searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goTaskUpdateP searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("goTaskUpdateP searchStatus     = " + searchVO.getSearchStatus());
		logger.debug("goTaskUpdateP searchWorkStatus = " + searchVO.getSearchWorkStatus());
		logger.debug("goTaskUpdateP searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goTaskUpdateP searchSendRepeat = " + searchVO.getSearchSendRepeat());
		logger.debug("goTaskUpdateP subTaskNo        = " + searchVO.getSubTaskNo());
		logger.debug("goTaskUpdateP taskNo           = " + searchVO.getTaskNo());
		logger.debug("goTaskUpdateP subTaskNo        = " + searchVO.getSubTaskNo());
		
		
		
		// 수신자정보머지키코드 목록
		CodeVO merge = new CodeVO();
		merge.setUilang((String)session.getAttribute("NEO_UILANG"));
		merge.setCdGrp("C001");
		merge.setUseYn("Y");
		List<CodeVO> mergeList = null;
		try {
			mergeList = codeService.getCodeList(merge);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C001] error = " + e);
		}
		
		// 채널코드 목록
		CodeVO channel = new CodeVO();
		channel.setUilang((String)session.getAttribute("NEO_UILANG"));
		channel.setCdGrp("C002");
		channel.setUseYn("Y");
		List<CodeVO> channelList = null;
		try {
			channelList = codeService.getCodeList(channel);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C002] error = " + e);
		}
		
		// 발송주기타입코드 목록
		CodeVO period = new CodeVO();
		period.setUilang((String)session.getAttribute("NEO_UILANG"));
		period.setCdGrp("C019");
		period.setUseYn("Y");
		List<CodeVO> periodList = null;
		try {
			periodList = codeService.getCodeList(period);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C019] error = " + e);
		}
		
		// 인코딩타입코드 목록
		CodeVO encoding = new CodeVO();
		encoding.setUilang((String)session.getAttribute("NEO_UILANG"));
		encoding.setCdGrp("C021");
		encoding.setUseYn("Y");
		List<CodeVO> encodingList = null;
		try {
			encodingList = codeService.getCodeList(encoding);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C021] error = " + e);
		} 
		
		// 코드그룹목록(코드성) 조회 -- 매일문자셋
		CodeVO charset = new CodeVO();
		charset.setUilang((String)session.getAttribute("NEO_UILANG"));
		charset.setCdGrp("C022");
		charset.setUseYn("Y");
		List<CodeVO> charsetList = null;
		try {
			charsetList = codeService.getCodeList(charset);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[022] error = " + e);
		}
		
		// 메일 상세정보 조회
		TaskVO mailInfo = new TaskVO();
		mailInfo.setTaskNo(searchVO.getTaskNo());
		mailInfo.setSubTaskNo(searchVO.getSubTaskNo());
		try {
			mailInfo = campaignService.getMailInfo(mailInfo);
		} catch(Exception e) {
			logger.error("campaignService.getMailInfo error = " + e);
		}
		
		// 캠페인정보
		CampaignVO campaignInfo = new CampaignVO();
		campaignInfo.setUilang((String)session.getAttribute("NEO_UILANG"));
		campaignInfo.setCampNo(mailInfo.getCampNo());
		try {
			campaignInfo = campaignService.getCampaignInfo(campaignInfo);
		} catch(Exception e) {
			logger.error("campaignService.getCampaignInfo error = " + e);
		}
		
		// 캠페인 목록 조회
		CampaignVO camp = new CampaignVO();
		camp.setUilang((String)session.getAttribute("NEO_UILANG"));
		camp.setSearchStatus("000");
		camp.setPage(1);
		camp.setRows(10000);
		camp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<CampaignVO> campList = null;
		try {
			campList = campaignService.getCampaignList(camp);
		} catch(Exception e) {
			logger.error("campaignService.getCampaignList error = " + e);
		}
		
		// 수신자그룹(발송대상) 목록
		SegmentVO seg = new SegmentVO();
		seg.setUilang((String)session.getAttribute("NEO_UILANG"));
		seg.setSearchStatus("000");
		seg.setPage(1);
		seg.setRows(10000);
		seg.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		seg.setEmsuseYn("Y");
		List<SegmentVO> segList = null;
		try {
			segList = segmentService.getSegmentList(seg);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentList error = " + e);
		}
		
		// 템플릿 목록
		TemplateVO temp = new TemplateVO();
		temp.setUilang((String)session.getAttribute("NEO_UILANG"));
		temp.setSearchStatus("000");
		temp.setPage(1);
		temp.setRows(10000);
		temp.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		temp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		seg.setEmsuseYn("Y");
		List<TemplateVO> tempList = null;
		try {
			tempList = templateService.getTemplateList(temp);
		} catch(Exception e) {
			logger.error("templateService.getTemplateList error = " + e);
		}
		
		// 첨부파일 목록
		List<AttachVO> attachList = null;
		try {
			attachList = campaignService.getAttachList(searchVO.getTaskNo());
		} catch(Exception e) {
			logger.error("campaignService.getAttachList error = " + e);
		}
		
		// 부서목록(코드성) 조회
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
		user.setDeptNo(mailInfo.getDeptNo());
		user.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(user);
		} catch(Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		// 발송결재라인 목록
		List<SecuApprovalLineVO> apprLineList = null;
		try {
			apprLineList = campaignService.getApprovalLineList(searchVO.getTaskNo());
		} catch(Exception e) {
			logger.error("campaignService.getApprovalLineList error = " + e);
		}
		
		// 웹에이전트 정보
		WebAgentVO webAgent = null;
		try {
			webAgent = campaignService.getWebAgentInfo(searchVO.getTaskNo());
		} catch(Exception e) {
			logger.error("campaignService.getWebAgentInfo error = " + e);
		}
		
		// 발송결과종별 목록
		CodeVO sndTpeVO = new CodeVO();
		sndTpeVO.setUilang("000");
		sndTpeVO.setCdGrp("C107");
		sndTpeVO.setUseYn("Y");
		List<CodeVO> sndTpeList = null;
		try {
			sndTpeList = codeService.getWorkStatusList(sndTpeVO);
		} catch(Exception e) {
			logger.error("codeService.getWorkStatusList error = " + e);
		}
		
		// 마케팅수신동의유형
		CodeVO mktGb = new CodeVO();
		mktGb.setUilang((String)session.getAttribute("NEO_UILANG"));
		mktGb.setCdGrp("C114");
		mktGb.setUseYn("Y");
		List<CodeVO> mktGbList = null;
		try {
			mktGbList = codeService.getCodeList(mktGb);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C114] error = " + e);
		}
		
		// 고객정보 조회사유코드 조회
		CodeVO reasonVO = new CodeVO();
		reasonVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		reasonVO.setCdGrp("C102");
		reasonVO.setUseYn("Y");
		List<CodeVO> reasonList = null;
		try {
			reasonList = codeService.getCodeList(reasonVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C102] error = " + e);
		}
		
		// 최상위 조직목록 조회
		List<ApprovalOrgVO> orgList = null;
		try {
			orgList = campaignService.getOrgListLvl1();
		} catch(Exception e) {
			logger.error("campaignService.getOrgListLvl1 error = " + e);
		}
		
		// 메일 내용
		String compVal = getContFileText(mailInfo.getContFlPath());
		
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("mergeList", mergeList);			// 수신자정보머지키코드
		model.addAttribute("channelList", channelList);		// 채널코드
		model.addAttribute("periodList", periodList);		// 발송주기타입코드
		model.addAttribute("mailInfo", mailInfo);			// 메일상세정보
		model.addAttribute("campaignInfo", campaignInfo);	// 캠페인정보
		model.addAttribute("campList", campList);			// 캠페인목록
		model.addAttribute("segList", segList);				// 발송대상(세그먼트) 목록
		model.addAttribute("tempList", tempList);			// 템플릿 목록
		model.addAttribute("attachList", attachList);		// 첨부파일 목록
		model.addAttribute("deptList", deptList);			// 부서 목록
		model.addAttribute("userList", userList);			// 사용자 목록
		model.addAttribute("webAgent", webAgent);			// 웹에이전트 목록
		model.addAttribute("apprLineList", apprLineList);	// 발송결재라인 목록
		model.addAttribute("sndTpeList", sndTpeList);		// 발송결과종별 목록
		model.addAttribute("mktGbList", mktGbList);			// 마케팅수신동의유형
		model.addAttribute("reasonList", reasonList);		// 조회사유코드 목록
		model.addAttribute("orgList", orgList);				// 조직목록
		model.addAttribute("DEFAULT_DOMAIN", properties.getProperty("IMG.DEFAULT_DOMAIN"));		
		
		//환경설정 관련 정보 조회용 항목 추가 
		String userInfoCharSetNm = "코드정보없음";
		String mailInfoCharSetNm = "코드정보없음";
		String mailInfoHeaderEncNm = "코드정보없음";
		String mailInfoBodyEncNm = "코드정보없음";
		String mailEncodingNm = "코드정보없음";
 
		if(charsetList != null && charsetList.size() >0 ) {
			for(int i = 0; i < charsetList.size() ; i++) {
				CodeVO charSetInfo  = new CodeVO();
				charSetInfo = (CodeVO) charsetList.get(i);
				if(charSetInfo.getCd().equals(mailInfo.getCharset())){
					mailInfoCharSetNm = charSetInfo.getCdNm();
				}
			}
		}
		
		if(encodingList != null && encodingList.size() >0 ) {
			for(int i = 0; i < encodingList.size() ; i++) {
				CodeVO encodingInfo  = new CodeVO();
				encodingInfo = (CodeVO) encodingList.get(i);
				if(encodingInfo.getCd().equals(mailInfo.getHeaderEnc())){
					mailInfoHeaderEncNm = encodingInfo.getCdNm();
				}
				if(encodingInfo.getCd().equals(mailInfo.getBodyEnc())){
					mailInfoBodyEncNm = encodingInfo.getCdNm();
				}
			}
		}
		
		model.addAttribute("MAIL_ENCODING_NM", mailEncodingNm);
		model.addAttribute("userInfo_charset_nm", userInfoCharSetNm);
		model.addAttribute("mailInfo_charset_nm", mailInfoCharSetNm);
		model.addAttribute("mailInfo_headerEnc_nm", mailInfoHeaderEncNm);
		model.addAttribute("mailInfo_bodyEnc_nm", mailInfoBodyEncNm);
		
		return "ems/cam/taskUpdateP";
	}
	
	/**
	 * 메일 정보를 수정한다.
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailUpdate")
	public String goMailUpdate(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailUpdate taskNo     = " + taskVO.getTaskNo());
		logger.debug("goMailUpdate subTaskNo  = " + taskVO.getSubTaskNo());
		logger.debug("goMailUpdate deptNo     = " + taskVO.getDeptNo());
		logger.debug("goMailUpdate userId     = " + taskVO.getUserId());
		logger.debug("goMailUpdate campInfo   = " + taskVO.getCampInfo());
		logger.debug("goMailUpdate segNo      = " + taskVO.getSegNo());
		logger.debug("goMailUpdate contTy     = " + taskVO.getContTy());
		logger.debug("goMailUpdate linkYn     = " + taskVO.getLinkYn());
		logger.debug("goMailUpdate attachNm   = " + taskVO.getAttachNm());
		logger.debug("goMailUpdate attachPath = " + taskVO.getAttachPath());
		logger.debug("goMailUpdate sendTermEndDt = " + taskVO.getSendTermEndDt());
		
		//XSS 필터 
		/*
		if (CrossScriptingFilter.existScript(request, taskVO.getComposerValue())) {
			model.addAttribute("result","filter");  
			return "ems/cam/mailUpdate";
		}
		*/
		
		List<AttachVO> attachList = new ArrayList<AttachVO>();		// 첨부파일 목록
		List<LinkVO> linkList = new ArrayList<LinkVO>();			// 링크 목록
		
		// 메일 업데이트시 상태 변경(상태:정상[000], 발송상태:발송대기[000]or결재대기[201]
		String res = "";
		try {
			res = mailUpdateProcess(taskVO, model, attachList, linkList, session);
		} catch(Exception e) {
			logger.error("mailUpdateProcess error = " + e);
		}
		
		// 합계 파일 사이즈가 10MB 이상인 경우 중지 
		if("FILE_SIZE".equals(res)) {
			model.addAttribute("result","Fail");
			model.addAttribute("FILE_SIZE","EXCESS");
			return "ems/cam/mailUpdate";
		}
		
		int result = 0;
		if("Success".equals(res)) {
			// 메일 정보 수정(NEO_TASK, NEO_SUBTASK, NEO_ATTACH, NEO_LINK)
			try {
				result = campaignService.updateMailInfo(taskVO, attachList, linkList);
			} catch(Exception e) {
				logger.error("campaignService.updateMailInfo error = " + e);
			}
		}
		
		if(result > 0) {
			model.addAttribute("result","Success");
			model.addAttribute("sendRepeat", taskVO.getSendRepeat());
		} else {
			model.addAttribute("result","Fail");
		}
		
		return "ems/cam/mailUpdate";
	}
	
	/**
	 * 메일 정보 일자 수정 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskUpdateDateP")
	public String goTaskUpdateDateP(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTaskUpdateDateP searchTaskNm      = " + searchVO.getSearchTaskNm());
		logger.debug("goTaskUpdateDateP searchCampNo      = " + searchVO.getSearchCampNo());
		logger.debug("goTaskUpdateDateP searchDeptNo      = " + searchVO.getSearchDeptNo());
		logger.debug("goTaskUpdateDateP searchUserId      = " + searchVO.getSearchUserId());
		logger.debug("goTaskUpdateDateP searchStartDt     = " + searchVO.getSearchStartDt());
		logger.debug("goTaskUpdateDateP searchEndDt       = " + searchVO.getSearchEndDt());
		logger.debug("goTaskUpdateDateP searchStatus      = " + searchVO.getSearchStatus());
		logger.debug("goTaskUpdateDateP searchWorkStatus  = " + searchVO.getSearchWorkStatus());
		logger.debug("goTaskUpdateDateP searchCampNo      = " + searchVO.getSearchCampNo());
		logger.debug("goTaskUpdateDateP searchSendRepeat  = " + searchVO.getSearchSendRepeat());		
		logger.debug("goTaskUpdateDateP taskNo            = " + searchVO.getTaskNo());
		logger.debug("goTaskUpdateDateP subTaskNo         = " + searchVO.getSubTaskNo());
		logger.debug("goTaskUpdateDateP approvalProcAppYn = " + searchVO.getApprovalProcAppYn());
		
		// 수신자정보머지키코드 목록
		CodeVO merge = new CodeVO();
		merge.setUilang((String)session.getAttribute("NEO_UILANG"));
		merge.setCdGrp("C001");
		merge.setUseYn("Y");
		List<CodeVO> mergeList = null;
		try {
			mergeList = codeService.getCodeList(merge);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C001] error = " + e);
		}
		
		// 채널코드 목록
		CodeVO channel = new CodeVO();
		channel.setUilang((String)session.getAttribute("NEO_UILANG"));
		channel.setCdGrp("C002");
		channel.setUseYn("Y");
		List<CodeVO> channelList = null;
		try {
			channelList = codeService.getCodeList(channel);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C002] error = " + e);
		}
		
		// 발송주기타입코드 목록
		CodeVO period = new CodeVO();
		period.setUilang((String)session.getAttribute("NEO_UILANG"));
		period.setCdGrp("C019");
		period.setUseYn("Y");
		List<CodeVO> periodList = null;
		try {
			periodList = codeService.getCodeList(period);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C019] error = " + e);
		}
		
		// 인코딩타입코드 목록
		CodeVO encoding = new CodeVO();
		encoding.setUilang((String)session.getAttribute("NEO_UILANG"));
		encoding.setCdGrp("C021");
		encoding.setUseYn("Y");
		List<CodeVO> encodingList = null;
		try {
			encodingList = codeService.getCodeList(encoding);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C021] error = " + e);
		} 
		
		// 코드그룹목록(코드성) 조회 -- 매일문자셋
		CodeVO charset = new CodeVO();
		charset.setUilang((String)session.getAttribute("NEO_UILANG"));
		charset.setCdGrp("C022");
		charset.setUseYn("Y");
		List<CodeVO> charsetList = null;
		try {
			charsetList = codeService.getCodeList(charset);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[022] error = " + e);
		}
		
		// 메일 상세정보 조회
		TaskVO mailInfo = new TaskVO();
		mailInfo.setTaskNo(searchVO.getTaskNo());
		mailInfo.setSubTaskNo(searchVO.getSubTaskNo());
		try {
			mailInfo = campaignService.getMailInfo(mailInfo);
		} catch(Exception e) {
			logger.error("campaignService.getMailInfo error = " + e);
		}
		
		// 캠페인정보
		CampaignVO campaignInfo = new CampaignVO();
		campaignInfo.setUilang((String)session.getAttribute("NEO_UILANG"));
		campaignInfo.setCampNo(mailInfo.getCampNo());
		try {
			campaignInfo = campaignService.getCampaignInfo(campaignInfo);
		} catch(Exception e) {
			logger.error("campaignService.getCampaignInfo error = " + e);
		}
		
		// 캠페인 목록 조회
		CampaignVO camp = new CampaignVO();
		camp.setUilang((String)session.getAttribute("NEO_UILANG"));
		camp.setSearchStatus("000");
		camp.setPage(1);
		camp.setRows(10000);
		camp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<CampaignVO> campList = null;
		try {
			campList = campaignService.getCampaignList(camp);
		} catch(Exception e) {
			logger.error("campaignService.getCampaignList error = " + e);
		}
		
		// 수신자그룹(발송대상) 목록
		SegmentVO seg = new SegmentVO();
		seg.setUilang((String)session.getAttribute("NEO_UILANG"));
		seg.setSearchStatus("000");
		seg.setPage(1);
		seg.setRows(10000);
		seg.setEmsuseYn("Y");
		seg.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<SegmentVO> segList = null;
		try {
			segList = segmentService.getSegmentList(seg);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentList error = " + e);
		}
		
		// 템플릿 목록
		TemplateVO temp = new TemplateVO();
		temp.setUilang((String)session.getAttribute("NEO_UILANG"));
		temp.setSearchStatus("000");
		temp.setPage(1);
		temp.setRows(10000);
		temp.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		temp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<TemplateVO> tempList = null;
		try {
			tempList = templateService.getTemplateList(temp);
		} catch(Exception e) {
			logger.error("templateService.getTemplateList error = " + e);
		}
		
		// 첨부파일 목록
		List<AttachVO> attachList = null;
		try {
			attachList = campaignService.getAttachList(searchVO.getTaskNo());
		} catch(Exception e) {
			logger.error("campaignService.getAttachList error = " + e);
		}
		
		// 부서목록(코드성) 조회
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
		user.setDeptNo(mailInfo.getDeptNo());
		user.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(user);
		} catch(Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		// 발송결재라인 목록
		List<SecuApprovalLineVO> apprLineList = null;
		try {
			apprLineList = campaignService.getApprovalLineList(searchVO.getTaskNo());
		} catch(Exception e) {
			logger.error("campaignService.getApprovalLineList error = " + e);
		}
		
		// 웹에이전트 정보
		WebAgentVO webAgent = null;
		try {
			webAgent = campaignService.getWebAgentInfo(searchVO.getTaskNo());
		} catch(Exception e) {
			logger.error("campaignService.getWebAgentInfo error = " + e);
		}
		
		// 발송결과종별 목록
		CodeVO sndTpeVO = new CodeVO();
		sndTpeVO.setUilang("000");
		sndTpeVO.setCdGrp("C107");
		sndTpeVO.setUseYn("Y");
		List<CodeVO> sndTpeList = null;
		try {
			sndTpeList = codeService.getWorkStatusList(sndTpeVO);
		} catch(Exception e) {
			logger.error("codeService.getWorkStatusList error = " + e);
		}
		
		// 마케팅수신동의유형
		CodeVO mktGb = new CodeVO();
		mktGb.setUilang((String)session.getAttribute("NEO_UILANG"));
		mktGb.setCdGrp("C114");
		mktGb.setUseYn("Y");
		List<CodeVO> mktGbList = null;
		try {
			mktGbList = codeService.getCodeList(mktGb);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C114] error = " + e);
		}
		
		// 고객정보 조회사유코드 조회
		CodeVO reasonVO = new CodeVO();
		reasonVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		reasonVO.setCdGrp("C102");
		reasonVO.setUseYn("Y");
		List<CodeVO> reasonList = null;
		try {
			reasonList = codeService.getCodeList(reasonVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C102] error = " + e);
		}
		
		// 최상위 조직목록 조회
		List<ApprovalOrgVO> orgList = null;
		try {
			orgList = campaignService.getOrgListLvl1();
		} catch(Exception e) {
			logger.error("campaignService.getOrgListLvl1 error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("mergeList", mergeList);			// 수신자정보머지키코드
		model.addAttribute("channelList", channelList);		// 채널코드
		model.addAttribute("periodList", periodList);		// 발송주기타입코드
		model.addAttribute("mailInfo", mailInfo);			// 메일상세정보
		model.addAttribute("campaignInfo", campaignInfo);	// 캠페인정보
		model.addAttribute("campList", campList);			// 캠페인목록
		model.addAttribute("segList", segList);				// 발송대상(세그먼트) 목록
		model.addAttribute("tempList", tempList);			// 템플릿 목록
		model.addAttribute("attachList", attachList);		// 첨부파일 목록
		model.addAttribute("deptList", deptList);			// 부서 목록
		model.addAttribute("userList", userList);			// 사용자 목록
		model.addAttribute("webAgent", webAgent);			// 웹에이전트 목록
		model.addAttribute("apprLineList", apprLineList);	// 발송결재라인 목록		
		model.addAttribute("sndTpeList", sndTpeList);		// 발송결과종별 목록
		model.addAttribute("mktGbList", mktGbList);			// 마케팅수신동의유형
		model.addAttribute("reasonList", reasonList);		// 조회사유코드 목록
		model.addAttribute("orgList", orgList);				// 조직목록
		model.addAttribute("DEFAULT_DOMAIN", properties.getProperty("IMG.DEFAULT_DOMAIN"));
		
		//환경설정 관련 정보 조회용 항목 추가 
		String userInfoCharSetNm = "코드정보없음";
		String mailInfoCharSetNm = "코드정보없음";
		String mailInfoHeaderEncNm = "코드정보없음";
		String mailInfoBodyEncNm = "코드정보없음";
		String mailEncodingNm = "코드정보없음";
 
		if(charsetList != null && charsetList.size() >0 ) {
			for(int i = 0; i < charsetList.size() ; i++) {
				CodeVO charSetInfo  = new CodeVO();
				charSetInfo = (CodeVO) charsetList.get(i);
				if(charSetInfo.getCd().equals(mailInfo.getCharset())){
					mailInfoCharSetNm = charSetInfo.getCdNm();
				}
			}
		}
		
		if(encodingList != null && encodingList.size() >0 ) {
			for(int i = 0; i < encodingList.size() ; i++) {
				CodeVO encodingInfo  = new CodeVO();
				encodingInfo = (CodeVO) encodingList.get(i);
				if(encodingInfo.getCd().equals(mailInfo.getHeaderEnc())){
					mailInfoHeaderEncNm = encodingInfo.getCdNm();
				}
				if(encodingInfo.getCd().equals(mailInfo.getBodyEnc())){
					mailInfoBodyEncNm = encodingInfo.getCdNm();
				}
			}
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("MAIL_ENCODING_NM", mailEncodingNm);
		model.addAttribute("userInfo_charset_nm", userInfoCharSetNm);
		model.addAttribute("mailInfo_charset_nm", mailInfoCharSetNm);
		model.addAttribute("mailInfo_headerEnc_nm", mailInfoHeaderEncNm);
		model.addAttribute("mailInfo_bodyEnc_nm", mailInfoBodyEncNm);
		
		return "ems/cam/taskUpdateDateP";
	}
	
	/**
	 * 메일 일자 정보를 수정한다.
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailUpdateDate")
	public String goMailUpdateDate(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailUpdateDate taskNo         = " + taskVO.getTaskNo());
		logger.debug("goMailUpdateDate subTaskNo      = " + taskVO.getSubTaskNo());
		logger.debug("goMailUpdateDate sendYmd        = " + taskVO.getSendYmd());
		logger.debug("goMailUpdateDate sendTermEndDt  = " + taskVO.getSendTermEndDt());
		logger.debug("goMailUpdateDate sendTermEndDt  = " + taskVO.getSendHour());
		logger.debug("goMailUpdateDate sendTermEndDt  = " + taskVO.getSendMin());
		logger.debug("goMailUpdateDate isSendTerm     = " + taskVO.getIsSendTerm());
		logger.debug("goMailUpdateDate sendTermLoop   = " + taskVO.getSendTermLoop());
		logger.debug("goMailUpdateDate sendTermLoopTy = " + taskVO.getSendTermLoopTy());
		
		int result = 0;
		
		if(StringUtil.isNull(taskVO.getSendYmd())) taskVO.setSendYmd("0000.00.00");									// 예약시간(예약일)
		String sendHour = StringUtil.setTwoDigitsString(taskVO.getSendHour());										// 예약시간(시)
		String sendMin = StringUtil.setTwoDigitsString(taskVO.getSendMin());										// 예약시간(분)
		taskVO.setSendDt( taskVO.getSendYmd().replaceAll("\\.", "") + sendHour + sendMin );							// 예약일시
		taskVO.setRespEndDt("999999999999");
				if(StringUtil.isNull(taskVO.getSendTermEndDt())) taskVO.setSendTermEndDt("0000.00.00");						// 정기발송종료일
		if(StringUtil.isNull(taskVO.getSendTermLoop())) taskVO.setSendTermLoop("");									// 정기발송주기
		if(StringUtil.isNull(taskVO.getSendTermLoopTy())) taskVO.setSendTermLoopTy("");								// 정기발송주기유형
		if("Y".equals(taskVO.getIsSendTerm())) {
			taskVO.setSendTermEndDt( taskVO.getSendTermEndDt().replaceAll("\\.", "") + "2359" );						// 정기발송종료일
			taskVO.setSendRepeat("001");
		} else {
			taskVO.setSendTermEndDt("");
			taskVO.setSendTermLoop("");
			taskVO.setSendTermLoopTy("");
			taskVO.setSendRepeat("000");
		}
		
		logger.debug("updateMailInfoDate taskNo         = " + taskVO.getTaskNo());
		logger.debug("updateMailInfoDate subTaskNo      = " + taskVO.getSubTaskNo());
		logger.debug("updateMailInfoDate sendDt         = " + taskVO.getSendDt());
		logger.debug("updateMailInfoDate sendTermEndDt  = " + taskVO.getSendTermEndDt());
		logger.debug("updateMailInfoDate sendTermLoop   = " + taskVO.getSendTermLoop());
		logger.debug("updateMailInfoDate sendTermLoopTy = " + taskVO.getSendTermLoopTy());
		
		try {
			result = campaignService.updateMailInfoDate(taskVO);
		} catch(Exception e) {
			logger.error("campaignService.updateMailInfoDate error = " + e);
		}
		
		if(result > 0) {
			model.addAttribute("result","Success");
			model.addAttribute("sendRepeat", taskVO.getSendRepeat());
		} else {
			model.addAttribute("result","Fail");
		}
		
		return "ems/cam/mailUpdateDate";
	}

	
	/**
	 * 웹에이전트 팝업 화면을 출력한다.
	 * @return
	 */
	@RequestMapping(value="/mailWebAgentP")
	public String goMailWebAgent() {
		return "ems/cam/mailWebAgentP";
	}
	
	/**
	 * 환경설정 팝업 화면을 출력한다.
	 * @return
	 */
	@RequestMapping(value="/mailOptionP")
	public String goMailOption(Model model, HttpSession session) {
		
		// 인코딩타입코드 목록
		CodeVO encoding = new CodeVO();
		encoding.setUilang((String)session.getAttribute("NEO_UILANG"));
		encoding.setCdGrp("C021");
		encoding.setUseYn("Y");
		List<CodeVO> encodingList = null;
		try {
			encodingList = codeService.getCodeList(encoding);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C021] error = " + e);
		}
		
		// 문자셋코드 목록
		CodeVO charset = new CodeVO();
		charset.setUilang((String)session.getAttribute("NEO_UILANG"));
		charset.setCdGrp("C022");
		charset.setUseYn("Y");
		List<CodeVO> charsetList = null;
		try {
			charsetList = codeService.getCodeList(charset);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C022] error = " + e);
		}
		
		// 발송모드코드 목록
		CodeVO sendMode = new CodeVO();
		sendMode.setUilang((String)session.getAttribute("NEO_UILANG"));
		sendMode.setCdGrp("C020");
		sendMode.setUseYn("Y");
		List<CodeVO> sendModeList = null;
		try {
			sendModeList = codeService.getCodeList(sendMode);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C020] error = " + e);
		}
		
		model.addAttribute("encodingList", encodingList);	// 인코딩
		model.addAttribute("charsetList", charsetList);		// 문자셋
		model.addAttribute("sendModeList", sendModeList);	// 발송모드
		
		return "ems/cam/mailOptionP";
	}
	
	/**
	 * 수신거부 팝업 화면을 출력한다.
	 * @return
	 */
	@RequestMapping(value="/mailRejectP")
	public String goMailReject(Model model, HttpSession session) {
		
		// 수신자정보머지키코드 목록
		CodeVO merge = new CodeVO();
		merge.setUilang((String)session.getAttribute("NEO_UILANG"));
		merge.setCdGrp("C001");
		merge.setUseYn("Y");
		List<CodeVO> mergeList = null;
		try {
			mergeList = codeService.getCodeList(merge);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C001] error = " + e);
		}
		
		model.addAttribute("mergeList", mergeList);
		model.addAttribute("RES_REJECT_URL", properties.getProperty("RES_REJECT_URL"));
		
		return "ems/cam/mailRejectP";
	}
	
	/**
	 * 메일(캠페인 주업무, 보조업무) 상태를 업데이트 한다.
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailDelete")
	public ModelAndView updateMailStatus(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateMailStatus taskNos    = " + taskVO.getTaskNos());
		logger.debug("updateMailStatus subTaskNos = " + taskVO.getSubTaskNos());
		logger.debug("updateMailStatus status     = " + taskVO.getStatus());
		
		taskVO.setUpId((String)session.getAttribute("NEO_USER_ID"));
		taskVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));

		int result = 0;
		try {
			result = campaignService.updateMailStatus(taskVO);
		} catch(Exception e) {
			logger.error("campaignService.updateMailStatus error = " + e);
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
	 * 발송결재라인 결재정보 상신 처리(목록에서 처리)
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailSubmitApproval")
	public ModelAndView updateSubmitApproval(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateSubmitApproval taskNos        = " + taskVO.getTaskNos());
		logger.debug("updateSubmitApproval subTaskNos     = " + taskVO.getSubTaskNos());
		logger.debug("updateSubmitApproval status         = " + taskVO.getStatus());
		logger.debug("updateSubmitApproval sendTermEndDt  = " + taskVO.getSendTermEndDt());
		
		TaskVO submit = new TaskVO();
		submit.setTaskNo(Integer.parseInt(taskVO.getTaskNos()));
		submit.setSubTaskNo(Integer.parseInt(taskVO.getSubTaskNos()));
		submit.setWorkStatus(taskVO.getStatus());
		submit.setUpId((String)session.getAttribute("NEO_USER_ID"));
		submit.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));

		// 메일 상태 업데이트(결재진행)
		int result = 0;
		try {
			result = campaignService.updateSubmitApproval(submit);
		} catch(Exception e) {
			logger.error("campaignService.updateSubmitApproval error = " + e);
		}
		
		// 첫번째 결재자 정보 조회
		String apprUserId = "";
		try {
			apprUserId = campaignService.getFirstApprUserId(submit.getTaskNo());
		} catch(Exception e) {
			logger.error("campaignService.getFirstApprUserId error = " + e);
		}
		
		// 메일 정보 조회
		TaskVO mailInfo = new TaskVO();
		try {
			mailInfo = campaignService.getMailInfo(submit);
		} catch(Exception e) {
			logger.error("campaignService.getMailInfo error = " + e);
		}
		
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(result > 0) {
			map.put("result", "Success");
			map.put("apprUserId", apprUserId);
			map.put("mailTitle", mailInfo.getMailTitle());
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 발송결재라인 결재정보 상신 처리(수정화면에서 처리)
	 * [2021.10.25 상신 처리시 메일정보 수정처리 추가]
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailSubmitApprovalP")
	public ModelAndView updateSubmitApprovalP(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateSubmitApprovalP taskNo         = " + taskVO.getTaskNo());
		logger.debug("updateSubmitApprovalP subTaskNo      = " + taskVO.getSubTaskNo());
		logger.debug("updateSubmitApprovalP status         = " + taskVO.getStatus());
		logger.debug("updateSubmitApprovalP sendTermEndDt  = " + taskVO.getSendTermEndDt());
		
		// 메일 업데이트
		List<AttachVO> attachList = new ArrayList<AttachVO>();		// 첨부파일 목록
		List<LinkVO> linkList = new ArrayList<LinkVO>();			// 링크 목록

		String res = "";
		try {
			res = mailUpdateProcess(taskVO, model, attachList, linkList, session);
		} catch(Exception e) {
			logger.error("mailUpdateProcess error = " + e);
		}
		
		int result = 0;
		if("Success".equals(res)) {
			// 메일 정보 수정(NEO_TASK, NEO_SUBTASK, NEO_ATTACH, NEO_LINK)
			try {
				result += campaignService.updateMailInfo(taskVO, attachList, linkList);
			} catch(Exception e) {
				logger.error("campaignService.updateMailInfo error = " + e);
			}
			
			if(result == 0) {
				// jsonView 생성
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("result", "Fail");
				ModelAndView modelAndView = new ModelAndView("jsonView", map);
				
				return modelAndView;
			} 
			
			TaskVO submit = new TaskVO();
			submit.setTaskNo(taskVO.getTaskNo());
			submit.setSubTaskNo(taskVO.getSubTaskNo());
			submit.setWorkStatus("202");	// 결재진행
			submit.setUpId((String)session.getAttribute("NEO_USER_ID"));
			submit.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));

			// 메일 상태 업데이트(결재진행)
			try {
				result = campaignService.updateSubmitApproval(submit);
			} catch(Exception e) {
				logger.error("campaignService.updateSubmitApproval error = " + e);
			}
			
			// 첫번째 결재자 정보 조회
			String apprUserId = "";
			try {
				apprUserId = campaignService.getFirstApprUserId(submit.getTaskNo());
			} catch(Exception e) {
				logger.error("campaignService.getFirstApprUserId error = " + e);
			}
			
			// 메일 정보 조회
			TaskVO mailInfo = new TaskVO();
			try {
				mailInfo = campaignService.getMailInfo(submit);
			} catch(Exception e) {
				logger.error("campaignService.getMailInfo error = " + e);
			}
			
			
			// jsonView 생성
			HashMap<String, Object> map = new HashMap<String, Object>();
			if(result > 0) {
				map.put("result", "Success");
				map.put("apprUserId", apprUserId);
				map.put("mailTitle", mailInfo.getMailTitle());
			} else {
				map.put("result", "Fail");
			}
			ModelAndView modelAndView = new ModelAndView("jsonView", map);
			
			return modelAndView;
		} else {
			// jsonView 생성
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("result", "Fail");
			ModelAndView modelAndView = new ModelAndView("jsonView", map);
			
			return modelAndView;
		}
	}
	
	/**
	 * 메일을 복사한다.(주업무, 보조업무, 첨부파일)
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailCopy")
	public ModelAndView copyMailInfo(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("copyMailInfo taskNos    = " + taskVO.getTaskNos());
		logger.debug("copyMailInfo subTaskNos = " + taskVO.getSubTaskNos());
		
		taskVO.setTaskNo( Integer.parseInt(taskVO.getTaskNos()) );
		taskVO.setSubTaskNo( Integer.parseInt(taskVO.getSubTaskNos()) );
		taskVO.setUserId((String)session.getAttribute("NEO_USER_ID"));
		taskVO.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
		taskVO.setRegId((String)session.getAttribute("NEO_USER_ID"));
		
		int result = 0;
		try {
			result = campaignService.copyMailInfo(taskVO, properties);
		} catch(Exception e) {
			logger.error("campaignService.copyMailInfo error = " + e);
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
	 * 테스트메일 목록을 출력한다.
	 * @param testUserVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailTestListP")
	public String goMailTestListP(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailTestListP searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goMailTestListP searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("goMailTestListP searchTaskNm     = " + searchVO.getSearchTaskNm());
		logger.debug("goMailTestListP searchSendRepeat = " + searchVO.getSearchSendRepeat());
		
		// 검색 기본값 설정
		if(StringUtil.isNull(searchVO.getSearchStartDt())) {
			searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-1, "M", "yyyyMMdd"));
		} else {
			searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		}
		if(StringUtil.isNull(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		}
		
		// 발송유형 목록
		CodeVO sendRepeat = new CodeVO();
		sendRepeat.setUilang((String)session.getAttribute("NEO_UILANG"));
		sendRepeat.setCdGrp("C017");
		sendRepeat.setUseYn("Y");
		List<CodeVO> sendRepeatList = null;
		try {
			sendRepeatList = codeService.getCodeList(sendRepeat);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C021] error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);				// 검색항목
		model.addAttribute("sendRepeatList", sendRepeatList);	// 발송유형
		
		return "ems/cam/mailTestListP";
	}
	
	/**
	 * 테스트발송메일 목록을 조회한다.
	 * @param testUserVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailTestList")
	public String goTestUserList(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailTestListP searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goMailTestListP searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("goMailTestListP searchTaskNm     = " + searchVO.getSearchTaskNm());
		logger.debug("goMailTestListP searchSendRepeat = " + searchVO.getSearchSendRepeat());
		
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		searchVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		searchVO.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		List<TaskVO> mailTestList = null;
		try {
			mailTestList = campaignService.getMailTestList(searchVO);
		} catch(Exception e) {
			logger.error("campaignService.getMailTestList error = " + e);
		}
		
		if(mailTestList != null && mailTestList.size() > 0) {
			totalCount = mailTestList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("mailTestList", mailTestList);	// 테스트메일 목록
		model.addAttribute("pageUtil", pageUtil);			// 페이징
		
		return "ems/cam/mailTestList";
	}
	
	/**
	 * 테스트발송 팝업창을 출력한다.
	 * @param testUserVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailTestUserListP")
	public String goTestUserListP(@ModelAttribute TestUserVO testUserVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("mailTestListP taskNos    = " + testUserVO.getTaskNos());
		logger.debug("mailTestListP subTaskNos = " + testUserVO.getSubTaskNos());
		
		String userId = (String)session.getAttribute("NEO_USER_ID");
		List<TestUserVO> testUserList = null;
		try {
			testUserList = campaignService.getTestUserList(userId);
		} catch(Exception e) {
			logger.error("campaignService.getTestUserList error = " + e);
		}
		
		model.addAttribute("testUserVO", testUserVO);
		model.addAttribute("testUserList", testUserList);	// 테스트유저 목록
		
		return "ems/cam/mailTestUserListP";
	}
	
	/**
	 * 테스트발송 사용자 목록을 조회한다.
	 * @param testUserVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailTestUserList")
	public String goTestUserList(@ModelAttribute TestUserVO testUserVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTestUserList taskNos    = " + testUserVO.getTaskNos());
		logger.debug("goTestUserList subTaskNos = " + testUserVO.getSubTaskNos());
		
		String userId = (String)session.getAttribute("NEO_USER_ID");
		List<TestUserVO> testUserList = null;
		try {
			testUserList = campaignService.getTestUserList(userId);
		} catch(Exception e) {
			logger.error("campaignService.getTestUserList error = " + e);
		}
		
		model.addAttribute("testUserVO", testUserVO);
		model.addAttribute("testUserList", testUserList);	// 테스트유저 목록
		
		return "ems/cam/mailTestUserList";
	}
	
	/**
	 * 테스트 사용자 정보 등록
	 * @param testUserVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailTestUserAdd")
	public ModelAndView insertTestUserInfo(@ModelAttribute TestUserVO testUserVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("insertTestUserInfo testUserNm = " + testUserVO.getTestUserNm());
		logger.debug("insertTestUserInfo testUserEm = " + testUserVO.getTestUserEm());
		
		testUserVO.setUserId((String)session.getAttribute("NEO_USER_ID"));
		testUserVO.setTestUserEm( cryptoService.getEncrypt("TEST_USER_EM", testUserVO.getTestUserEm()) );
		int result = 0;
		try {
			result = campaignService.insertTestUserInfo(testUserVO);
		} catch(Exception e) {
			logger.error("campaignService.insertTestUserInfo error = " + e);
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
	 * 테스트 사용자 정보 수정
	 * @param testUserVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailTestUserUpdate")
	public ModelAndView updateTestUserInfo(@ModelAttribute TestUserVO testUserVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateTestUserInfo testUserNm = " + testUserVO.getTestUserNm());
		logger.debug("updateTestUserInfo testUserEm = " + testUserVO.getTestUserEm());
		logger.debug("updateTestUserInfo testUserNo = " + testUserVO.getTestUserNo());
		
		int result = 0;
		try {
			result = campaignService.updateTestUserInfo(testUserVO);
		} catch(Exception e) {
			logger.error("campaignService.updateTestUserInfo error = " + e);
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
	 * 테스트 사용자 정보 삭제
	 * @param testUserVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailTestUserDelete")
	public ModelAndView deleteTestUserInfo(@ModelAttribute TestUserVO testUserVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("deleteTestUserInfo testUserNos = " + testUserVO.getTestUserNos());
		
		int result = 0;
		try {
			if(testUserVO.getTestUserNos() != null && !"".equals(testUserVO.getTestUserNos())) {
				String[] userNo = testUserVO.getTestUserNos().split(",");
				for(int i=0;i<userNo.length;i++) {
					TestUserVO userVO = new TestUserVO();
					userVO.setTestUserNo(Integer.parseInt(userNo[i]));
					result += campaignService.deleteTestUserInfo(userVO);
				}
			}
			
		} catch(Exception e) {
			logger.error("campaignService.deleteTestUserInfo error = " + e);
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
	 * 테스트발송 등록
	 * @param testUserVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailTestSend")
	public ModelAndView sendTestMail(@ModelAttribute TestUserVO testUserVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("sendTestMail taskNos    = " + testUserVO.getTaskNos());
		logger.debug("sendTestMail subTaskNos = " + testUserVO.getSubTaskNos());
		logger.debug("sendTestMail testUserEm = " + testUserVO.getTestUserEm());
		
		testUserVO.setSendDt(StringUtil.getDate(Code.TM_YMDHM));
		testUserVO.setTestUserEm( cryptoService.getEncrypt("SEND_TEST_EM", testUserVO.getTestUserEm()) );
		
		int result = 0;
		try {
			result = campaignService.sendTestMail(testUserVO, session);
		} catch(Exception e) {
			logger.error("campaignService.sendTestMail error = " + e);
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
	
	/**********************************************************/
	
	/**
	 * 메일을 재발송한다.(주업무, 보조업무, 첨부파일) : 건별 (연계되는 SEG: NEO_SEGMENT_REAL) 
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailReSend")
	public ModelAndView reSendMail(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("reSendMail taskNo     = " + taskVO.getTaskNo());
		logger.debug("reSendMail subTaskNos = " + taskVO.getSubTaskNo());
		logger.debug("reSendMail custId     = " + taskVO.getCustId());
		logger.debug("reSendMail custEm     = " + taskVO.getCustEm());
		logger.debug("reSendMail bizkey     = " + taskVO.getBizkey());
		logger.debug("reSendMail rtyCode    = " + taskVO.getRtyCode());
		logger.debug("reSendMail rtyType    = " + taskVO.getRtyTyp());
		
		taskVO.setSendDt(StringUtil.getDate(Code.TM_YMDHM));
		taskVO.setMailFromEm(cryptoService.getDecrypt("MAIL_FROM_EM",(String)session.getAttribute("NEO_MAIL_FROM_EM")));
		
		
		int result = 0;
		try {
			result = campaignService.reSendMail(taskVO, session);
		} catch(Exception e) {
			logger.error("campaignService.reSendMail error = " + e);
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
	 * 메일을 재발송한다.(주업무, 보조업무, 첨부파일) : 일괄 (연계되는 SEG: NEO_SEGMENT_RETRY) 
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailReSendTask")
	public ModelAndView reSendMailTask(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("reSendMail taskNo    = " + taskVO.getTaskNo());
		logger.debug("reSendMail subTaskNos = " + taskVO.getSubTaskNo());
		logger.debug("reSendMail custId = " + taskVO.getCustId());
		logger.debug("reSendMail custEm = " + taskVO.getCustEm());
		logger.debug("reSendMail bizkey = " + taskVO.getBizkey());
		
		taskVO.setSendDt(StringUtil.getDate(Code.TM_YMDHM));
		taskVO.setMailFromEm(cryptoService.getDecrypt("MAIL_FROM_EM",(String)session.getAttribute("NEO_MAIL_FROM_EM")));
		
		int result = 0;
		try {
			result = campaignService.reSendMail(taskVO, session);
		} catch(Exception e) {
			logger.error("campaignService.reSendMail error = " + e);
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
	 * 메일업데이트 화면에서 발송승인 처리
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailUpdateAdmit")
	public String goMailUpdateAdmit(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		List<AttachVO> attachList = new ArrayList<AttachVO>();		// 첨부파일 목록
		List<LinkVO> linkList = new ArrayList<LinkVO>();			// 링크 목록
		
		//XSS필터
		/*
		if (CrossScriptingFilter.existScript(request, taskVO.getComposerValue())) {
			model.addAttribute("result","filter");  
			return "ems/cam/mailUpdateAdmit";
		}		
		*/
		
		String res = "";
		try {
			res = mailUpdateProcess(taskVO, model, attachList, linkList, session);
		} catch(Exception e) {
			logger.error("mailUpdateProcess error = " + e);
		}
		
		// 합계 파일 사이즈가 10MB 이상인 경우 중지 
		if("FILE_SIZE".equals(res)) {
			model.addAttribute("result","Fail");
			model.addAttribute("FILE_SIZE","EXCESS");
			return "ems/cam/mailUpdateAdmit";
		}
		
		int result = 0;
		if("Success".equals(res)) {
			// 메일 정보 수정(NEO_TASK, NEO_SUBTASK, NEO_ATTACH, NEO_LINK)
			try {
				result += campaignService.updateMailInfo(taskVO, attachList, linkList);
			} catch(Exception e) {
				logger.error("campaignService.updateMailInfo error = " + e);
			}
			
			if(result == 0) {
				model.addAttribute("result","Fail");
				return "ems/cam/mailUpdateAdmit";
			} 
			
			// 발송승인 처리
			taskVO.setRecoStatus("001");	// 승인여부:승인
			taskVO.setWorkStatus("001");	// 발송상태:발송승인
			taskVO.setExeUserId((String)session.getAttribute("NEO_USER_ID"));
			try {
				result += campaignService.updateMailAdmit(taskVO);
			} catch(Exception e) {
				logger.error("campaignService.updateMailAdmit error = " + e);
			}
		}
		
		if(result > 0) {
			model.addAttribute("result","Success");
			model.addAttribute("sendRepeat", taskVO.getSendRepeat());
		} else {
			model.addAttribute("result","Fail");
		}
		
		return "ems/cam/mailUpdateAdmit";
	}
	
	/**
	 * 메일 업데이트 처리
	 * @param taskVO
	 * @param model
	 * @param attachList
	 * @param linkList
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public String mailUpdateProcess(TaskVO taskVO, Model model, List<AttachVO> attachList, List<LinkVO> linkList, HttpSession session) throws Exception {
		// 파일 사이즈 체크
		if(taskVO.getAttachPath() != null && !"".equals(taskVO.getAttachPath())) {
			File attachFile = null;
			String basePath = properties.getProperty("FILE.UPLOAD_PATH") + "/attach";
			
			long fileSize = 0;
			String[] fileNm = taskVO.getAttachNm().split(",");
			String[] filePath = taskVO.getAttachPath().split(",");
			taskVO.setAttCnt(filePath.length);		// 첨부파일 수 설정
			for(int i=0;i<filePath.length;i++) {
				attachFile = new File(basePath + "/" + filePath[i]);
				fileSize += attachFile.length();
				
				AttachVO attach = new AttachVO();
				attach.setTaskNo(taskVO.getTaskNo());
				attach.setAttNm(fileNm[i]);
				attach.setAttFlPath("attach/" + filePath[i]);
				attach.setAttFlSize(attachFile.length());
				
				attachList.add(attach);
			}
			
			// 합계 파일 사이즈가 10MB 이상인 경우 중지 
			if(fileSize > 10485760) {	// 10MB이상
				model.addAttribute("result","Fail");
				model.addAttribute("FILE_SIZE","EXCESS");
				return "FILE_SIZE";
			}
		}
		
		// 기본값 설정
		if(taskVO.getDeptNo() == 0) taskVO.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));						// 부서번호
		if(StringUtil.isNull(taskVO.getUserId())) taskVO.setUserId((String)session.getAttribute("NEO_USER_ID"));	// 사용자아이디
		if(StringUtil.isNull(taskVO.getPlanUserId())) taskVO.setPlanUserId("");
		if(StringUtil.isNull(taskVO.getCampTy())) taskVO.setCampTy("");												// 캠페인유형
		if(StringUtil.isNull(taskVO.getNmMerge())) taskVO.setNmMerge("");											// $:nm_merge:$형태로 넘어옴
		if(StringUtil.isNull(taskVO.getIdMerge())) taskVO.setIdMerge("");											// $:id_merge:$형태로 넘어옴
		if(StringUtil.isNull(taskVO.getChannel())) taskVO.setChannel("000");										// 채널코드
		if(StringUtil.isNull(taskVO.getContTy())) taskVO.setContTy("000");											// 편집모드(기본 HTML형식)
		if(!StringUtil.isNull(taskVO.getSegNoc())) taskVO.setSegNo(Integer.parseInt( taskVO.getSegNoc().substring(0, taskVO.getSegNoc().indexOf("|")) ));		// 세그먼트번호(발송대상그룹)
		if(StringUtil.isNull(taskVO.getRespYn())) taskVO.setRespYn("0");											// 수신확인
		if(StringUtil.isNull(taskVO.getTaskNm())) taskVO.setTaskNm("");												// 메일명
		if(StringUtil.isNull(taskVO.getMailTitle())) taskVO.setMailTitle("");										// 메일제목
		if(StringUtil.isNull(taskVO.getSendYmd())) taskVO.setSendYmd("0000.00.00");									// 예약시간(예약일)
		String sendHour = StringUtil.setTwoDigitsString(taskVO.getSendHour());										// 예약시간(시)
		String sendMin = StringUtil.setTwoDigitsString(taskVO.getSendMin());										// 예약시간(분)
		taskVO.setSendDt( taskVO.getSendYmd().replaceAll("\\.", "") + sendHour + sendMin );							// 예약일시
		taskVO.setRespEndDt("999999999999");
		if(StringUtil.isNull(taskVO.getIsSendTerm())) taskVO.setIsSendTerm("N");									// 정기발송체크여부
		if(StringUtil.isNull(taskVO.getSendTermEndDt())) taskVO.setSendTermEndDt("0000.00.00");						// 정기발송종료일
		if(StringUtil.isNull(taskVO.getSendTermLoop())) taskVO.setSendTermLoop("");									// 정기발송주기
		if(StringUtil.isNull(taskVO.getSendTermLoopTy())) taskVO.setSendTermLoopTy("");								// 정기발송주기유형
		if(StringUtil.isNull(taskVO.getSendTestYn())) taskVO.setSendTestYn("N");
		if(StringUtil.isNull(taskVO.getSendTestEm())) taskVO.setSendTestEm("");
		if(StringUtil.isNull(taskVO.getComposerValue())) taskVO.setComposerValue("");								// 메일내용
		if("Y".equals(taskVO.getIsSendTerm())) {
			taskVO.setSendTermEndDt( taskVO.getSendTermEndDt().replaceAll("\\.", "") + "2359" );						// 정기발송종료일
			taskVO.setSendRepeat("001");
		} else {
			taskVO.setSendTermEndDt("");
			taskVO.setSendTermLoop("");
			taskVO.setSendTermLoopTy("");
			taskVO.setSendRepeat("000");
		}
		if(StringUtil.isNull(taskVO.getLinkYn())) taskVO.setLinkYn("N");											// 링크클릭
		taskVO.setUpId((String)session.getAttribute("NEO_USER_ID"));
		taskVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		if(StringUtil.isNull(taskVO.getWebAgentAttachYn())) taskVO.setWebAgentAttachYn("N");						// WebAgent 첨부파일로 지정
		
		if(StringUtil.isNull(taskVO.getSendMode())) taskVO.setSendMode("");
		if(StringUtil.isNull(taskVO.getMailFromNm())) taskVO.setMailFromNm("");
		if(StringUtil.isNull(taskVO.getMailFromEm())) taskVO.setMailFromEm("");
		if(StringUtil.isNull(taskVO.getReplyToEm())) taskVO.setReplyToEm("");
		if(StringUtil.isNull(taskVO.getReturnEm())) taskVO.setReturnEm("");
		
		if(StringUtil.isNull(taskVO.getHeaderEnc())) taskVO.setHeaderEnc("");
		if(StringUtil.isNull(taskVO.getBodyEnc())) taskVO.setBodyEnc("");
		if(StringUtil.isNull(taskVO.getCharset())) taskVO.setCharset("");
		
		
		taskVO.setStatus("000");
		taskVO.setRecoStatus("000");
		taskVO.setSendIp(properties.getProperty("SEND_IP"));
		// 발송결재라인정보가 있는경우 상태처리
		if(StringUtil.isNull(taskVO.getApprUserId())) {
			taskVO.setWorkStatus("000");
		} else {
			taskVO.setWorkStatus("201");
		}
		
		// 링크클릭 체크한 경우
		if("Y".equals(taskVO.getLinkYn())) {
			// 링크 클릭 알리아싱 처리(mailAliasParser.jsp 내용 처리) =============================================================================
			List<LinkVO> dataList = null;

			// 수신자정보머지키코드 목록
			CodeVO merge = new CodeVO();
			merge.setUilang((String)session.getAttribute("NEO_UILANG"));
			merge.setCdGrp("C001");
			merge.setUseYn("Y");
			List<CodeVO> mergeList = null;
			try {
				mergeList = codeService.getCodeList(merge);
			} catch(Exception e) {
				logger.error("codeService.getCodeList[C001] error = " + e);
			}
			
			try {
				// 링크 클릭 알리아스 처리
				dataList = mailAliasParser(taskVO, mergeList, properties);
			} catch(Exception e) {
				logger.error("campaignService.mailAliasParser error = "+ e);
			}
			
			if(dataList != null && dataList.size() > 0) {
				for(LinkVO data:dataList) {
					LinkVO link = new LinkVO();
					
					link.setLinkTy(data.getLinkTy());
					link.setLinkUrl(data.getLinkUrl());
					link.setLinkNm(data.getLinkNm());
					link.setLinkNo(data.getLinkNo());
					link.setRegId((String)session.getAttribute("NEO_USER_ID"));
					link.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
					linkList.add(link);
				}
			}
		}
		
		// 수신확인 클릭한 경우
		if("Y".equals(taskVO.getRespYn()) && "000".equals(taskVO.getContTy())) {
			// send_dt와 p_resp_log를 이용하여 수신 종료 응답시간을 구한다.
			if(taskVO.getRespLog() != 0) { // respLog : 수신확인추적기간
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
				cal.set(Integer.parseInt(taskVO.getSendYmd().substring(0,4)), Integer.parseInt(taskVO.getSendYmd().substring(5,7))-1, Integer.parseInt(taskVO.getSendYmd().substring(8,10)));
				cal.add(Calendar.DATE, taskVO.getRespLog());
				taskVO.setRespEndDt(fmt.format(cal.getTime()) +  StringUtil.setTwoDigitsString(taskVO.getSendHour()) + StringUtil.setTwoDigitsString(taskVO.getSendMin())) ;
			} else {
				taskVO.setRespEndDt("999999999999");
			}
			
			if(taskVO.getComposerValue().indexOf("<!--NEO__RESPONSE__START-->") == -1) {
				String tmpCompose = taskVO.getComposerValue();
				int pos = tmpCompose.toLowerCase().indexOf("</body>");
				
				String strResponse = "<!--NEO__RESPONSE__START-->";
				//strResponse += "<img src='"+properties.getProperty("RESPONSE_URL")+"?$:RESP_END_DT:$|000|"+taskVO.getIdMerge()+"|$:TASK_NO:$|$:SUB_TASK_NO:$|$:DEPT_NO:$|$:USER_ID:$|$:CAMP_TY:$|$:CAMP_NO:$' width=0 height=0 border=0>";
				//strResponse += "<img src=\""+properties.getProperty("RESPONSE_URL")+"?$:RESP_END_DT:$|000|"+taskVO.getIdMerge()+"|$:TASK_NO:$|$:SUB_TASK_NO:$|$:DEPT_NO:$|$:USER_ID:$|$:CAMP_TY:$|$:CAMP_NO:$\" width=0 height=0 border=0>";
				//2022.11.16 수신확인 관련 수정 CAMP_TY과 CAMP_NO 순서바꿈
				strResponse += "<img src=\""+properties.getProperty("RESPONSE_URL")+"?$:RESP_END_DT:$|000|"+taskVO.getIdMerge()+"|$:TASK_NO:$|$:SUB_TASK_NO:$|$:DEPT_NO:$|$:USER_ID:$|$:CAMP_NO:$|$:CAMP_TY:$\" width=0 height=0 border=0>";
				strResponse += "<!--NEO__RESPONSE__END-->";
				if(pos == -1) taskVO.setComposerValue(strResponse + taskVO.getComposerValue());
				else taskVO.setComposerValue( strResponse + taskVO.getComposerValue().substring(0, pos) + taskVO.getComposerValue().substring(pos));
			}
		} else {
			int startPos = taskVO.getComposerValue().indexOf("<!--NEO__RESPONSE__START-->");
			int endPos = taskVO.getComposerValue().indexOf("<!--NEO__RESPONSE__END-->");
			if(startPos != -1 && endPos != -1) {
				taskVO.setComposerValue( taskVO.getComposerValue().substring(0, startPos) + taskVO.getComposerValue().substring(endPos+"<!--NEO__RESPONSE__END-->".length()) );
			}
		}
		
		// 컬럼 암호화 설정
		taskVO.setMailFromEm( cryptoService.getEncrypt("MAIL_FROM_EM",taskVO.getMailFromEm()) );
		taskVO.setReplyToEm( cryptoService.getEncrypt("REPLY_TO_EM",taskVO.getReplyToEm()) );
		taskVO.setReturnEm( cryptoService.getEncrypt("RETURN_EM",taskVO.getReturnEm()) );

		// 파일을 수정한다.
		FileOutputStream fos = null;
		OutputStreamWriter writer = null;
		try {
			File contFile = new File(properties.getProperty("FILE.UPLOAD_PATH") + "/" + taskVO.getContFlPath());
			fos = new FileOutputStream(contFile);
			writer = new OutputStreamWriter(fos, "UTF-8");
			writer.write(taskVO.getComposerValue());
			writer.flush();
		} catch(Exception e) {
			logger.error("mailUpdate File Write Error = " + e);
		}
		
		return "Success";
	}
	
	/**
	 * 테스트발송상세정보 화면을 출력한다.
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailTestTaskP")
	public String goMailTestTaskP(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailTestTaskP sendTestTaskNo    = " + taskVO.getSendTestTaskNo());
		logger.debug("goMailTestTaskP sendTestSubTaskNo = " + taskVO.getSendTestSubTaskNo());
		taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 테스트발송 목록 조회
		List<TaskVO> testList = null;
		try {
			testList = campaignService.getMailTestTaskList(taskVO);
		} catch(Exception e) {
			logger.error("campaignService.getMailTestTaskList error = " + e);
		}
		
		model.addAttribute("taskVO", taskVO);
		model.addAttribute("testList", testList);
		
		return "ems/cam/mailTestTaskP";
	}
	
	/**
	 * 테스트발송결과로그 조회
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailTestSendLogP")
	public String goMailTestSendLogP(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailTestSendLogP taskNo    = " + taskVO.getTaskNo());
		logger.debug("goMailTestSendLogP subTaskNo = " + taskVO.getSubTaskNo());
		taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 테스트발송로그 목록 조회
		List<SendTestLogVO> logList = null;
		try {
			logList = campaignService.getMailTestSendLogList(taskVO);
		} catch(Exception e) {
			logger.error("campaignService.getMailTestSendLogList error = " + e);
		}
		
		model.addAttribute("taskVO", taskVO);
		model.addAttribute("testList", logList);
		
		return "ems/cam/mailTestSendLogP";
	}
	
	
	
	/**
	 * 파일의 내용을 읽는다.
	 * @param contFlPath
	 * @return
	 */
	public String getContFileText(String contFlPath) {
		logger.debug("getContFileText contFlPath  = " + contFlPath);
		
		FileInputStream input = null;
		InputStreamReader reader = null;
		BufferedReader bufferedReader = null;
		StringBuffer sb = new StringBuffer();
		try {
			String basePath = properties.getProperty("FILE.UPLOAD_PATH");
			String contPath = basePath + "/" + contFlPath;
			
			input = new FileInputStream(contPath);
			reader = new InputStreamReader(input,"UTF-8");
			bufferedReader = new BufferedReader(reader);
			String line = "";
			while((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
		} catch(Exception e) {
			logger.error("getContFileText error = " + e);
		} finally {
			if(bufferedReader != null) try { bufferedReader.close(); } catch(Exception e) {};
			if(reader != null) try { reader.close(); } catch(Exception e) {};
		}
		
		String fileContent = sb.toString().trim();
		fileContent = fileContent.replaceAll("\"", "'");
		fileContent = fileContent.replaceAll("\n", " ");
		fileContent = fileContent.replaceAll("\r", " ");
		
		return fileContent;
	}
	
	/**
	 * 메일 파일 읽기
	 * @param templateVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailFileView")
	public ModelAndView goMailFileView(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailFileView ContFlPath  = " + taskVO.getContFlPath());
		
		/*
		File file = null;
		FileReader fileReader = null;
		*/
		FileInputStream input = null;
		InputStreamReader reader = null;	
		BufferedReader bufferedReader = null;
		StringBuffer sb = new StringBuffer();
		try {
			String basePath = properties.getProperty("FILE.UPLOAD_PATH");
			String contFlPath = taskVO.getContFlPath();
			if(contFlPath !=null && ! "".equals(contFlPath)) {
				contFlPath = contFlPath.replace("..", "");
			}
			String contPath = basePath + "/" + contFlPath;
			
			//file = new File(contentsPath);
			//fileReader = new FileReader(file);
			input = new FileInputStream(contPath);
			reader = new InputStreamReader(input,"UTF-8");
			bufferedReader = new BufferedReader(reader);
			String line = "";
			while((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
		} catch(Exception e) {
			logger.error("goMailFileView error = " + e);
		} finally {
			if(bufferedReader != null) try { bufferedReader.close(); } catch(Exception e) {};
			if(reader != null) try { reader.close(); } catch(Exception e) {};
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("result", "Success");
		map.put("contVal", sb.toString().trim());
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 캠페인별 메일발송목록 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campMailListP")
	public String goCampMailListP(@ModelAttribute CampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goCampMailListP searchCampNm  = " + searchVO.getSearchCampNm());
		logger.debug("goCampMailListP searchCampTy  = " + searchVO.getSearchCampTy());
		logger.debug("goCampMailListP searchStatus  = " + searchVO.getSearchStatus());
		logger.debug("goCampMailListP searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goCampMailListP searchEndDt   = " + searchVO.getSearchEndDt());
		logger.debug("goCampMailListP searchDeptNo  = " + searchVO.getSearchDeptNo());
		logger.debug("goCampMailListP searchUserId  = " + searchVO.getSearchUserId());
		
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
		searchVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));

		
		// 캠페인 목록 조회
		CampaignVO campVO = new CampaignVO();
		campVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		campVO.setSearchStatus("000");
		campVO.setPage(1);
		campVO.setRows(10000);
		campVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		campVO.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));		List<CampaignVO> campaignList = null;
		try {
			campaignList = campaignService.getCampaignList(campVO);
		} catch(Exception e) {
			logger.error("campaignService.getCampaignList error = " + e);
		}
		
		// 부서목록(코드성) 조회
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
		
		// 메일유형 목록
		CodeVO sendRepeat = new CodeVO();
		sendRepeat.setUilang((String)session.getAttribute("NEO_UILANG"));
		sendRepeat.setCdGrp("C017");
		sendRepeat.setUseYn("Y");
		List<CodeVO> sendRepeatList = null;
		try {
			sendRepeatList = codeService.getCodeList(sendRepeat);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C021] error = " + e);
		}
		
		// 메일상태 목록 조회
		CodeVO status = new CodeVO();
		status.setUilang((String)session.getAttribute("NEO_UILANG"));
		status.setCdGrp("C023");
		status.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(status);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C023] error = " + e);
		}
		
		// 보조업무작업상태(발송상태)코드(결재포함)
		CodeVO wStatus = new CodeVO();
		wStatus.setUilang((String)session.getAttribute("NEO_UILANG"));
		wStatus.setCdGrp("C101");
		wStatus.setUseYn("Y");
		List<CodeVO> workStatusList = null;
		try {
			workStatusList = codeService.getWorkStatusList(wStatus);
		} catch(Exception e) {
			logger.error("codeService.getWorkStatusList error = " + e);
		}

		model.addAttribute("searchVO", searchVO);				// 검색 항목
		model.addAttribute("campaignList", campaignList);		// 캠페인 목록
		model.addAttribute("deptList", deptList);				// 부서 목록
		model.addAttribute("userList", userList);				// 사용자 목록
		model.addAttribute("repeatList", sendRepeatList);		// 단기/정기 목록
		model.addAttribute("statusList", statusList);			// 메일상태 목록
		model.addAttribute("workStatusList", workStatusList);	// 발송상태 목록
		
		return "ems/cam/campMailListP";
	}
	
	/**
	 * 캠페인별 메일발송목록을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campMailList")
	public String goCampMailList(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 검색 기본값 설정
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		searchVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		List<String> workStatusList = new ArrayList<String>();
		String[] workStatus = searchVO.getSearchWorkStatus().split(",");
		for(int i=0;i<workStatus.length;i++) {
			workStatusList.add(workStatus[i]);
		}
		searchVO.setSearchWorkStatusList(workStatusList);
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;

		// 캠페인별 메일 목록 조회
		List<TaskVO> campMailList = null;
		try {
			campMailList = campaignService.getCampMailList(searchVO);
		} catch(Exception e) {
			logger.error("campaignService.getCampMailList error = " + e);
		}
		
		if(campMailList != null && campMailList.size() > 0) {
			totalCount = campMailList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("searchVO", searchVO);			// 검색 항목
		model.addAttribute("campMailList", campMailList);	// 메일 목록
		model.addAttribute("pageUtil", pageUtil);			// 페이징

		
		return "ems/cam/campMailList";
	}

	
	/**
	 * 대상자보기(미리보기) 화면을 출력한다.
	 * @param segmentVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/getSegInfo")
	public ModelAndView getSegInfo(@ModelAttribute SegmentVO segmentVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getSegInfo segNo = " + segmentVO.getSegNo());
		
		try {
			segmentVO.setUilang((String)session.getAttribute("NEO_UILANG"));
			segmentVO = segmentService.getSegmentInfo(segmentVO);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentInfo error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("result", "Success");
		map.put("segmentVO", segmentVO);
		map.put("uploadPath", properties.getProperty("FILE.UPLOAD_PATH"));
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 대상자보기(미리보기) 화면을 출력한다.
	 * @param segmentVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/getMailView")
	public ModelAndView getMailViewInfo(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getMailViewInfo taskNo = " + searchVO.getTaskNo());
		
		// 메일 상세정보 조회
		TaskVO mailInfo = new TaskVO();
		mailInfo.setTaskNo(searchVO.getTaskNo());
		mailInfo.setSubTaskNo(searchVO.getSubTaskNo());
		try {
			mailInfo = campaignService.getMailInfo(mailInfo);
		} catch(Exception e) {
			logger.error("campaignService.getMailInfo error = " + e);
		} 
		 
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("result", "Success");
		map.put("mailInfo", mailInfo); 
		map.put("uploadPath", properties.getProperty("FILE.UPLOAD_PATH"));
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 발송결재라인 조직 및 사용자 목록 조회
	 * @param orgVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/getOrgUserList")
	public ModelAndView getOrgUserList(@ModelAttribute ApprovalOrgVO orgVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getOrgUserList upOrgCd = " + orgVO.getUpOrgCd());
		
		// 조직 목록
		List<ApprovalOrgVO> orgList = null;
		try {
			orgList = campaignService.getOrgListChild(orgVO.getUpOrgCd());
		} catch(Exception e) {
			logger.error("campaignService.getOrgListChild error = " + e);
		}
		
		// 사용자 목록
		List<UserVO> orgUserList = null;
		try {
			orgUserList = campaignService.getUserListOrg(orgVO.getUpOrgCd());
		} catch(Exception e) {
			logger.error("campaignService.getUserListOrg error = " + e);
		}
		
		String userId = (String)session.getAttribute("NEO_USER_ID");
		
		List<UserVO> userList = new ArrayList<UserVO>();
		if (!"ADMIN".equals(userId)) {
			
			if(orgUserList != null && orgUserList.size() > 0) {
				for(int i = 0; i < orgUserList.size() ; i++) {
					if(!userId.equals(orgUserList.get(i).getUserId())){
						//UserVO addUserVO = new UserVO();
						userList.add(orgUserList.get(i));
					}
				}
			}
		} else {
			userList = orgUserList;
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("orgList", orgList);
		map.put("userList", userList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 발송결재라인 사용자 검색 목록 조회
	 * @param userVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/getUserListSearch")
	public ModelAndView getUserListSearch(@ModelAttribute UserVO userVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getUserListSearch searchUserNm = " + userVO.getSearchUserNm());
		
		// 사용자 목록
		List<UserVO> orgUserList = null;
		try {
			orgUserList = campaignService.getUserListSearch(userVO.getSearchUserNm());
		} catch(Exception e) {
			logger.error("campaignService.getUserListSearch error = " + e);
		}
		
		String userId = (String)session.getAttribute("NEO_USER_ID");
				
		List<UserVO> userList = new ArrayList<UserVO>();
		if (!"ADMIN".equals(userId)) {
			
			if(orgUserList != null && orgUserList.size() > 0) {
				for(int i = 0; i < orgUserList.size() ; i++) {
					if(!userId.equals(orgUserList.get(i).getUserId())){
						//UserVO addUserVO = new UserVO();
						userList.add(orgUserList.get(i));
					}
				}
			}
		} else {
			userList = orgUserList;
		}
		
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userList", userList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 정기메일 등록현황 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/subTaskListP")
	public String goSubTaskListP(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSubTaskListP searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goSubTaskListP searchEndDt   = " + searchVO.getSearchEndDt());
		logger.debug("goSubTaskListP searchTaskNm  = " + searchVO.getSearchTaskNm());
		logger.debug("goSubTaskListP searchCampNo  = " + searchVO.getSearchCampNo());
		logger.debug("goSubTaskListP searchDeptNo  = " + searchVO.getSearchDeptNo());
		logger.debug("goSubTaskListP searchUserId  = " + searchVO.getSearchUserId());
		logger.debug("goSubTaskListP searchStatus  = " + searchVO.getSearchStatus());
		logger.debug("goSubTaskListP taskNo        = " + searchVO.getCampNo());
		
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		
		// 메일 목록 조회
		TaskVO mailPeriod = null;
		try {
			mailPeriod = campaignService.getMailPeriod(searchVO.getTaskNo());
		} catch(Exception e) {
			logger.error("campaignService.getMailPeriod error = " + e);
		}
		
		// 검색 기본값 설정
		TaskVO subSearchVO = new TaskVO();
		if (mailPeriod != null ) {
			subSearchVO.setSearchStartDt(mailPeriod.getSearchStartDt().substring(0,8));
			subSearchVO.setSearchEndDt(mailPeriod.getSearchEndDt().substring(0,8));
		} else {
			subSearchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-1, "M", "yyyyMMdd"));
			subSearchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		}
		
		model.addAttribute("searchVO", searchVO);			// 이전검색항목
		model.addAttribute("subSearchVO", subSearchVO);		// 현재검색항목
		
		return "ems/cam/subTaskListP";
	}
	
	/**
	 * 정기메일 등록현황 목록을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/subTaskList")
	public String goSubTaskList(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSubTaskList searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goSubTaskList searchEndDt   = " + searchVO.getSearchEndDt());
		logger.debug("goSubTaskList taskNo        = " + searchVO.getTaskNo());
		
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 검색 기본값 설정
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;

		// 메일 목록 조회
		List<TaskVO> mailList = null;
		try {
			mailList = campaignService.getSubTaskList(searchVO);
		} catch(Exception e) {
			logger.error("campaignService.getSubTaskList error = " + e);
		}
		
		if(mailList != null && mailList.size() > 0) {
			totalCount = mailList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("searchVO", searchVO);	// 검색 항목
		model.addAttribute("mailList", mailList);	// 메일 목록
		model.addAttribute("pageUtil", pageUtil);	// 페이징
		
		return "ems/cam/subTaskList";
	}
	

	/**
	 * 테스트메일 발송결과 목록 조회
	 * @param userVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailTestResultList")
	public String getMailTestResultList(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getMailTestResultList taskNo    = " + taskVO.getTaskNo());
		logger.debug("getMailTestResultList subTaskNo = " + taskVO.getSubTaskNo());
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(taskVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(taskVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		taskVO.setPage(page);
		taskVO.setRows(rows);
		int totalCount = 0;
		
		// 테스트메일 발송결과
		List<SendLogVO> sendLogList = null;
		try {
			sendLogList = campaignService.getMailTestResultList(taskVO);
		} catch(Exception e) {
			logger.error("campaignService.getMailTestResultList error = " + e);
		}
		
		if(sendLogList != null && sendLogList.size() > 0) {
			totalCount = sendLogList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, taskVO.getPage(), totalCount, rows);
		pageUtil.setSubmitFunc("goPageNumTest");
		
		model.addAttribute("sendLogList", sendLogList);	// 테스트메일 발송결과
		model.addAttribute("pageUtil", pageUtil);		// 페이징
		
		return "ems/cam/mailTestResultList";
	}
	
	/**
	 * 메일 내용 링크 파싱 처리
	 * @param taskVO
	 * @param mergeList
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	public List<LinkVO> mailAliasParser(TaskVO taskVO, List<CodeVO> mergeList, PropertiesUtil properties) throws Exception {
		int inc = 100;

		List<LinkVO> dataList = new ArrayList<LinkVO>();
		String composerValue = taskVO.getComposerValue();
		
		// ID머지명칭
		String ID = mergeList.get(2).getCdNm();
		
		if(inc >= 100) inc = 10;
		
		String tmpCV = "";
		StringBuffer returnValue = new StringBuffer();
		String TM = Long.toString(System.currentTimeMillis());
		
		try {
			composerValue = StringUtil.repStr(composerValue, "\r\n", "_NEOCR_");

			String strTag = "";				// <a ... <map.. 테그의 처음에서 끝까지 모든 값(<a href.....>내용</a>)
			String tmpHTag = "";
			String temp = "";
			int pos = 0;
			int s_pos = 0;
			int e_pos = 0;
			tmpCV = composerValue;

			String tempValue = "";
			while(true) {
				s_pos = tmpCV.indexOf("<!--NEO__LINKCLICK__START-->");
				if(s_pos != -1) {
					returnValue.append(tmpCV.substring(0, s_pos));

					e_pos = tmpCV.indexOf("<!--NEO__LINKCLICK__END-->");
					if(e_pos != -1) e_pos += 26;

					if(e_pos < s_pos) {
						returnValue.append(tmpCV.substring(0, e_pos));
						tmpCV = tmpCV.substring(e_pos);
						continue;
					}

					strTag = tmpCV.substring(s_pos, e_pos);
					tmpCV = tmpCV.substring(e_pos);

					tempValue ="<A href="+strTag.substring(strTag.lastIndexOf("|")+1, strTag.indexOf("<!--NEO__LINKCLICK__END-->"));

					returnValue.append(tempValue.replaceAll("\"", ""));
				} else {
					returnValue.append(tmpCV);
					break;
				}
			}

			tmpCV = returnValue.toString();

			returnValue = new StringBuffer();

			tmpCV = StringUtil.repStr(tmpCV,"<!--NEO__REJECT__START--><A","<!--NEO__REJECT__CONVERT__START-->");
			tmpCV = StringUtil.repStr(tmpCV,"</A><!--NEO__REJECT__END-->","<!--NEO__REJECT__CONVERT__END-->");

			//[LINK_TY, LINK_URL, LINK_NM, LINK_NO]
			while( (s_pos = tmpCV.toLowerCase().indexOf("<a ")) != -1) {

				LinkVO bodyData = new LinkVO();
				TM = Long.toString(System.currentTimeMillis());
				if(inc >= 100) inc = 10;

				returnValue.append(tmpCV.substring(0, s_pos));

				e_pos = tmpCV.toLowerCase().indexOf("</a>");
				if(e_pos != -1) e_pos += 4;

				if(e_pos < s_pos) {
					returnValue.append(tmpCV.substring(0, e_pos));
					tmpCV = tmpCV.substring(e_pos);
					continue;
				}

				strTag = tmpCV.substring(s_pos, e_pos);
				tmpCV = tmpCV.substring(e_pos);

				// 이미지 인지 텍스트 링크인지 처리
				if(strTag.toLowerCase().indexOf("<img ") != -1) tmpHTag = "000";    // 이미지(000)
				else tmpHTag = "001";                                               // 텍스트(001)
				bodyData.setLinkTy(tmpHTag);

				// 링크 URL 찾기
				pos = strTag.toLowerCase().indexOf("href=") + 5;
				temp = strTag.substring(pos, strTag.indexOf(">"));
				if(temp.indexOf(" ") != -1) temp = temp.substring(0, temp.indexOf(" "));
				if(temp.charAt(0) == '"' || temp.charAt(0) == '\'') temp = temp.substring(1, temp.length() - 1);

				if(temp.length() < 7 || !temp.substring(0, 7).toLowerCase().equals("http://")) {
					returnValue.append(strTag);
					continue;
				}

				bodyData.setLinkUrl(temp);

				// 링크 내용(이미지 또는 텍스트:a테그에 둘러 싸인 부분)
				tmpHTag = strTag.substring(strTag.indexOf(">") + 1, strTag.lastIndexOf("<"));
				bodyData.setLinkNm(tmpHTag);
				
				returnValue.append("<!--NEO__LINKCLICK__START-->"+
									StringUtil.repStr(strTag,
												temp,
												properties.getProperty("TRACKING_URL")+
												"?$:RESP_END_DT:$|002|$:"+ID+":$|$:TASK_NO:$|$:SUB_TASK_NO:$|$:DEPT_NO:$|$:USER_ID:$|$:CAMP_TY:$|$:CAMP_NO:$|"+
												TM+
												Integer.toString(inc) +
												"|" +
												temp)+
									"<!--NEO__LINKCLICK__END-->"
									);

				// 번호
				bodyData.setLinkNo(TM+Integer.toString(inc));
				inc++;
				dataList.add(bodyData);
            }

			// image map 테그 처리
			tmpCV = returnValue.toString() + tmpCV;
			returnValue = new StringBuffer();
			int mapNo = 0;
			String mapNm = "";

			while( (s_pos = tmpCV.toLowerCase().indexOf("<map ")) != -1 ) {
				returnValue.append(tmpCV.substring(0, s_pos));

				e_pos = tmpCV.toLowerCase().indexOf("</map>");
				if(e_pos != -1) e_pos += 6;

				if(e_pos < s_pos) {
					returnValue.append(tmpCV.substring(0, e_pos));
					tmpCV = tmpCV.substring(e_pos);
					continue;
				}

				strTag = tmpCV.substring(s_pos, e_pos);
				tmpCV = tmpCV.substring(e_pos);

				// 이미지 맵 이름 구하는 부분
				mapNm = strTag.substring(strTag.toLowerCase().indexOf("name=")+5, strTag.indexOf(">"));
				if(mapNm.indexOf(" ") != -1) mapNm = mapNm.substring(0, mapNm.indexOf(" "));
				if(mapNm.charAt(0) == '"' || mapNm.charAt(0) == '\'') mapNm = mapNm.substring(1, mapNm.length() - 1);

				strTag = strTag.substring(strTag.indexOf(">") + 1);
				mapNo = 0;

				returnValue.append("<MAP NAME="+mapNm+">");

				while( (s_pos = strTag.toLowerCase().indexOf("<area ")) != -1 ) {
					LinkVO bodyData = new LinkVO();
					TM = Long.toString(System.currentTimeMillis());
					if(inc >= 100) inc = 10;
					mapNo++;

					// 이미지 맵 처리
					tmpHTag = "003";       // 이미지 맵
					bodyData.setLinkTy(tmpHTag);

					// 링크 URL 찾기
					pos = strTag.toLowerCase().indexOf("href=") + 5;
					temp = strTag.substring(pos, strTag.indexOf(">"));
					if(temp.indexOf(" ") != -1) temp = temp.substring(0, temp.indexOf(" "));
					if(temp.charAt(0) == '"' || temp.charAt(0) == '\'') temp = temp.substring(1, temp.length() - 1);

					String strMap = strTag.substring(0,strTag.indexOf(">")+1);

					strTag = strTag.substring(strTag.indexOf(">") + 1);

					if(temp.length() < 7 || !temp.substring(0, 7).toLowerCase().equals("http://")) {
						returnValue.append(strTag);
						continue;
					}
					bodyData.setLinkUrl(temp);

					// 링크 내용
					tmpHTag = mapNm + Integer.toString(mapNo);
					bodyData.setLinkNm(tmpHTag);

					returnValue.append("<!--NEO__LINKCLICK__START-->"+
										StringUtil.repStr(strMap,
													temp,
													properties.getProperty("TRACKING_URL")+
													"?$:RESP_END_DT:$|002|$:"+ID+":$|$:TASK_NO:$|$:SUB_TASK_NO:$|$:DEPT_NO:$|$:USER_ID:$|$:CAMP_TY:$|$:CAMP_NO:$|"+
													TM+
													Integer.toString(inc) +
													"|" +
													temp) +
										"<!--NEO__LINKCLICK__END-->"
										);
					
					// 번호
					bodyData.setLinkNo(TM+Integer.toString(inc));
					inc++;
					dataList.add(bodyData);
                }
				returnValue.append("</MAP>");
            }


            temp = StringUtil.repStr(returnValue.toString() + tmpCV, "_NEOCR_", "\r\n");

			temp = StringUtil.repStr(temp,"<!--NEO__REJECT__CONVERT__START-->","<!--NEO__REJECT__START--><a");
			temp = StringUtil.repStr(temp,"<!--NEO__REJECT__CONVERT__END-->","</a><!--NEO__REJECT__END-->");

			composerValue = temp;
			
		} catch(Exception e) {
			logger.error("Mail Alias Parsing Error = " + e);
			composerValue = "";
		}
		
		taskVO.setComposerValue(composerValue);
		
		return dataList;
	}
	
	
	
	
	
	
	
	
	/****************************************** 메일작성 내부 팝업 처리 ******************************************/
	/**
	 * 메일 등록/수정 캠페인 목록(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popCampList")
	public String goPopCampList(@ModelAttribute CampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopCampList searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goPopCampList searchEndDt   = " + searchVO.getSearchEndDt());
		logger.debug("goPopCampList searchCampNm  = " + searchVO.getSearchCampNm());
		
		if(StringUtil.isNull(searchVO.getSearchStartDt())) {
			searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-1, "M", "yyyyMMdd"));
		} else {
			searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		}
		if(StringUtil.isNull(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		}
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		searchVO.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_POP")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		// 캠페인 목록
		List<CampaignVO> campList = null;
		try {
			campList = campaignService.getCampaignList(searchVO);
		} catch(Exception e) {
			logger.error("campaignService.getCampaignList error = " + e);
		}
		
		if(campList != null && campList.size() > 0) {
			totalCount = campList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		pageUtil.setSubmitFunc("goPageNumPopCamp");
		
		model.addAttribute("searchVO", searchVO);	// 검색항목
		model.addAttribute("campList", campList);	// 캠페인목록
		model.addAttribute("pageUtil", pageUtil);	// 페이징
		
		return "ems/cam/pop/popCampList";
	}
	
	/**
	 * 메일 등록/수정 캠페인 등록(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popCampAdd")
	public String goPopCampAdd(@ModelAttribute CampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopCampAdd searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goPopCampAdd searchEndDt   = " + searchVO.getSearchEndDt());
		logger.debug("goPopCampAdd searchCampNm  = " + searchVO.getSearchCampNm());
		
		// 캠페인목적 목록
		CodeVO campTy = new CodeVO();
		campTy.setUilang((String)session.getAttribute("NEO_UILANG"));
		campTy.setCdGrp("C004");
		campTy.setUseYn("Y");
		List<CodeVO> campTyList = null;
		try {
			campTyList = codeService.getCodeList(campTy);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C004] error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);		// 검색항목
		model.addAttribute("campTyList", campTyList);	// 캠페인목적
		
		return "ems/cam/pop/popCampAdd";
	}
	
	/**
	 * 메일 등록/수정 캠페인 수정(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popCampUpdate")
	public String goPopCampUpdate(@ModelAttribute CampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopCampUpdate campNo        = " + searchVO.getCampNo());
		
		// 캠페인 정보 조회
		CampaignVO campInfo = null;
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		try {
			campInfo = campaignService.getCampaignInfo(searchVO);
		} catch(Exception e) {
			logger.error("campaignService.getCampaignInfo error = " + e);
		}
		
		// 캠페인목적 목록
		CodeVO campTy = new CodeVO();
		campTy.setUilang((String)session.getAttribute("NEO_UILANG"));
		campTy.setCdGrp("C004");
		campTy.setUseYn("Y");
		List<CodeVO> campTyList = null;
		try {
			campTyList = codeService.getCodeList(campTy);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C004] error = " + e);
		}
		
		// 캠페인상태 목록
		CodeVO status = new CodeVO();
		status.setUilang((String)session.getAttribute("NEO_UILANG"));
		status.setCdGrp("C014");
		status.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(status);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C014] error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);		// 검색항목
		model.addAttribute("campInfo", campInfo);		// 캠페인정보
		model.addAttribute("campTyList", campTyList);	// 캠페인목적
		model.addAttribute("statusList", statusList);	// 캠페인상태
		
		return "ems/cam/pop/popCampUpdate";
	}
	
	/**
	 * 메일 등록/수정 템플릿 목록(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popTempList")
	public String goPopTempList(@ModelAttribute TemplateVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopTempList searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goPopTempList searchEndDt   = " + searchVO.getSearchEndDt());
		logger.debug("goPopTempList searchTempNm  = " + searchVO.getSearchTempNm());
		
		if(StringUtil.isNull(searchVO.getSearchStartDt())) {
			searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-1, "M", "yyyyMMdd"));
		} else {
			searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		}
		if(StringUtil.isNull(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		}
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		searchVO.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));		
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_POP")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		// 템플릿 목록
		List<TemplateVO> tempList = null;
		try {
			tempList = templateService.getTemplateList(searchVO);
		} catch(Exception e) {
			logger.error("templateService.getTemplateList error = " + e);
		}
		
		if(tempList != null && tempList.size() > 0) {
			totalCount = tempList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		pageUtil.setSubmitFunc("goPageNumPopTemp");
		
		model.addAttribute("searchVO", searchVO);	// 검색항목
		model.addAttribute("tempList", tempList);	// 템플릿목록
		model.addAttribute("pageUtil", pageUtil);	// 페이징
		
		return "ems/cam/pop/popTempList";
	}
	
	/**
	 * 메일 등록/수정 템플릿 등록(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popTempAdd")
	public String goPopTempAdd(@ModelAttribute TemplateVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopTempAdd searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goPopTempAdd searchEndDt   = " + searchVO.getSearchEndDt());
		logger.debug("goPopTempAdd searchTempNm  = " + searchVO.getSearchTempNm());
		
		model.addAttribute("searchVO", searchVO);		// 검색항목
		model.addAttribute("DEFAULT_DOMAIN", properties.getProperty("IMG.DEFAULT_DOMAIN"));
		
		return "ems/cam/pop/popTempAdd";
	}
	
	/**
	 * 메일 등록/수정 템플릿 수정(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popTempUpdate")
	public String goPopTempUpdate(@ModelAttribute TemplateVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopTempUpdate tempNo = " + searchVO.getTempNo());
		
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 템플릿 정보 조회
		TemplateVO tempInfo = null;
		try {
			tempInfo = templateService.getTemplateInfo(searchVO);
		} catch(Exception e) {
			logger.error("templateService.getTemplateInfo error = " + e);
		}
		
		// 템플릿상태 목록
		CodeVO status = new CodeVO();
		status.setUilang((String)session.getAttribute("NEO_UILANG"));
		status.setCdGrp("C016");
		status.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(status);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C016] error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);		// 검색항목
		model.addAttribute("tempInfo", tempInfo);		// 템플릿정보
		model.addAttribute("statusList", statusList);	// 템플릿상태
		model.addAttribute("DEFAULT_DOMAIN", properties.getProperty("IMG.DEFAULT_DOMAIN"));
		
		return "ems/cam/pop/popTempUpdate";
	}
	
	/****************************************** 상세로그 내부 팝업 처리 ******************************************/
	/**
	 * 상세로그 캠페인 RNS 서비스 목록(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popCampRnsList")
	public String goPopCampRnsList(@ModelAttribute CampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopCampRnsList searchStartDt	= " + searchVO.getSearchStartDt());
		logger.debug("goPopCampRnsList searchEndDt		= " + searchVO.getSearchEndDt());
		logger.debug("goPopCampRnsList searchCampNm		= " + searchVO.getSearchCampNm());
		logger.debug("goPopCampRnsList searchServiceGb  = " + searchVO.getSearchServiceGb());
		
		if(StringUtil.isNull(searchVO.getSearchStartDt())) {
			searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-3, "M", "yyyyMMdd"));
		} else {
			searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		}
		if(StringUtil.isNull(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		}
		if(StringUtil.isNull(searchVO.getSearchServiceGb())) {
			searchVO.setSearchServiceGb("10");
		}
		
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_POP")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		// 캠페인 목록
		List<CampaignVO> campList = null;
		try {
			campList = campaignService.getCampaignRnsList(searchVO);
		} catch(Exception e) {
			logger.error("campaignService.getCampaignRnsList error = " + e);
		}
		
		if(campList != null && campList.size() > 0) {
			totalCount = campList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		pageUtil.setSubmitFunc("goPageNumPopCampRns");
		
		model.addAttribute("searchVO", searchVO);	// 검색항목
		model.addAttribute("campList", campList);	// 캠페인목록
		model.addAttribute("pageUtil", pageUtil);	// 페이징
		
		return "ems/cam/pop/popCampRnsList";
	}
		
	/**
	 * 메일 등록/수정 수신자그룹 목록(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popSegList")
	public String goPopSegList(@ModelAttribute SegmentVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopSegList searchStartDt   = " + searchVO.getSearchStartDt());
		logger.debug("goPopSegList searchEndDt     = " + searchVO.getSearchEndDt());
		logger.debug("goPopSegList searchSegNm     = " + searchVO.getSearchSegNm());
		logger.debug("goPopSegList searchCreateTy  = " + searchVO.getSearchCreateTy());
		logger.debug("goPopSegList searchCreateTy = " + searchVO.getSearchCreateTy());
		
		if(StringUtil.isNull(searchVO.getSearchStartDt())) {
			searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-1, "M", "yyyyMMdd"));
		} else {
			searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		}
		if(StringUtil.isNull(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		}
		if(StringUtil.isNull(searchVO.getSearchStatus())) {
			searchVO.setSearchStatus("000");
		}
		
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		searchVO.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		searchVO.setSearchStatus("000");
		searchVO.setSearchEmsuseYn("Y");
		
		// 수신자그룹 생성 유형 코드 조회
		CodeVO createTy = new CodeVO();
		createTy.setUilang((String)session.getAttribute("NEO_UILANG"));
		createTy.setCdGrp("C013");	// 세그먼트 생성 유형
		createTy.setUseYn("Y");
		List<CodeVO> createTyList = null;
		try {
			createTyList = codeService.getCodeList(createTy);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C013] error = " + e);
		}
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_POP")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		// 수신자그룹 목록
		List<SegmentVO> segList = null;
		try {
			segList = segmentService.getSegmentList(searchVO);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentList error = " + e);
		}
		
		if(segList != null && segList.size() > 0) {
			totalCount = segList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		pageUtil.setSubmitFunc("goPageNumPopSeg");
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("createTyList", createTyList);	// 수신자그룹생성유형
		model.addAttribute("segList", segList);				// 수신자그룹목록
		model.addAttribute("pageUtil", pageUtil);			// 페이징
		
		return "ems/cam/pop/popSegList";
	}
	
	/**
	 * 메일 등록/수정 수신자그룹 등록:파일(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popSegAddFile")
	public String goPopSegAddFile(@ModelAttribute SegmentVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopSegAddFile searchStartDt  = " + searchVO.getSearchStartDt());
		logger.debug("goPopSegAddFile searchEndDt    = " + searchVO.getSearchEndDt());
		logger.debug("goPopSegAddFile searchSegNm    = " + searchVO.getSearchSegNm());
		logger.debug("goPopSegAddFile searchCreateTy = " + searchVO.getSearchCreateTy());
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		
		return "ems/cam/pop/popSegAddFile";
	}
	
	/**
	 * 메일 등록/수정 수신자그룹 수정:파일(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popSegUpdateFile")
	public String goPopSegUpdateFile(@ModelAttribute SegmentVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopSegUpdateFile segNo  = " + searchVO.getSegNo());
		
		// 수신자그룹 정보 조회
		SegmentVO segInfo = null;
		try {
			searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
			segInfo = segmentService.getSegmentInfo(searchVO);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentInfo error = " + e);
		}
		
		// 수신자그룹상태 코드 조회
		CodeVO status = new CodeVO();
		status.setUilang((String)session.getAttribute("NEO_UILANG"));
		status.setCdGrp("C023");	// 수신자그룹상태
		status.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(status);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C023] error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);		// 검색항목
		model.addAttribute("segInfo", segInfo);			// 수신자그룹정보
		model.addAttribute("statusList", statusList);	// 상태목록
		
		return "ems/cam/pop/popSegUpdateFile";
	}
	
	/**
	 * 메일 등록/수정 수신자그룹 등록:SQL(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popSegAddSql")
	public String goPopSegAddSql(@ModelAttribute SegmentVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopSegAddFile searchStartDt  = " + searchVO.getSearchStartDt());
		logger.debug("goPopSegAddFile searchEndDt    = " + searchVO.getSearchEndDt());
		logger.debug("goPopSegAddFile searchSegNm    = " + searchVO.getSearchSegNm());
		logger.debug("goPopSegAddFile searchCreateTy = " + searchVO.getSearchCreateTy());
		
		// DB Connection 목록 조회
		DbConnVO dbConnVO = new DbConnVO();
		dbConnVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		dbConnVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		dbConnVO.setUserId((String)session.getAttribute("NEO_USER_ID"));
		List<DbConnVO> dbConnList = null;
		try {
			dbConnList = segmentService.getDbConnList(dbConnVO);
		} catch(Exception e) {
			logger.error("segmentService.getDbConnList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("dbConnList", dbConnList);		// DB연결목록
		
		return "ems/cam/pop/popSegAddSql";
	}
	
	/**
	 * 메일 등록/수정 수신자그룹 수정:SQL(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popSegUpdateSql")
	public String goPopSegUpdateSql(@ModelAttribute SegmentVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopSegUpdateFile segNo  = " + searchVO.getSegNo());
		
		// 수신자그룹 정보 조회
		SegmentVO segInfo = null;
		try {
			searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
			segInfo = segmentService.getSegmentInfo(searchVO);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentInfo error = " + e);
		}
		
		// DB Connection 목록 조회
		DbConnVO dbConnVO = new DbConnVO();
		dbConnVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		dbConnVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		dbConnVO.setUserId((String)session.getAttribute("NEO_USER_ID"));
		List<DbConnVO> dbConnList = null;
		try {
			dbConnList = segmentService.getDbConnList(dbConnVO);
		} catch(Exception e) {
			logger.error("segmentService.getDbConnList error = " + e);
		}
		
		// 수신자그룹상태 코드 조회
		CodeVO status = new CodeVO();
		status.setUilang((String)session.getAttribute("NEO_UILANG"));
		status.setCdGrp("C023");	// 수신자그룹상태
		status.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(status);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C023] error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);		// 검색항목
		model.addAttribute("segInfo", segInfo);			// 수신자그룹정보
		model.addAttribute("dbConnList", dbConnList);	// Connection목록
		model.addAttribute("statusList", statusList);	// 상태목록
		
		return "ems/cam/pop/popSegUpdateSql";
	}

	/**
	 * 결재상태 정보 조회 (팝업)
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popApprStateList")
	public String goPopApprStateList(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopApprStateList taskNo          = " + taskVO.getTaskNo());
		logger.debug("goPopApprStateList subTaskNo       = " + taskVO.getSubTaskNo());
		logger.debug("goPopApprStateList userId          = " + taskVO.getUserId());
		logger.debug("goPopApprStateList workStatus      = " + taskVO.getWorkStatus());
		
		// 발송결재라인 목록
		List<SecuApprovalLineVO> apprLineListAll = null;
		try {
			apprLineListAll = campaignService.getApprovalLineList(taskVO.getTaskNo());
		} catch(Exception e) {
			logger.error("campaignService.getApprovalLineList error = " + e);
		}
		
		// 발송결재라인 목록 
		SecuApprovalLineVO firArrpLine = new SecuApprovalLineVO();
		SecuApprovalLineVO secArrpLine = new SecuApprovalLineVO();
		SecuApprovalLineVO thrArrpLine = new SecuApprovalLineVO();
		SecuApprovalLineVO forArrpLine = new SecuApprovalLineVO();
		
		if (apprLineListAll != null ) {
			for(int i = 0; i < apprLineListAll.size() ; i++) {
				if (apprLineListAll.get(i).getApprStep() == 1 ) {
					firArrpLine = apprLineListAll.get(i);
				}
				if (apprLineListAll.get(i).getApprStep() == 2 ) {
					secArrpLine = apprLineListAll.get(i);
				}
				if (apprLineListAll.get(i).getApprStep() == 3 ) {
					thrArrpLine = apprLineListAll.get(i);
				}
				if (apprLineListAll.get(i).getApprStep() == 4 ) {
					forArrpLine = apprLineListAll.get(i);
				}
			}
		}
		
		model.addAttribute("taskVO", taskVO);					// 메일정보
		model.addAttribute("firArrpLine", firArrpLine);			// 발송결재라인목록
		model.addAttribute("secArrpLine", secArrpLine);			// 발송결재라인목록
		model.addAttribute("thrArrpLine", thrArrpLine);			// 발송결재라인목록
		model.addAttribute("forArrpLine", forArrpLine);			// 발송결재라인목록
		
		return "ems/cam/pop/popApprStateList";
	}
	
	/**
	 * 메일 등록/수정 수신자그룹 등록:SQL(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popApprList")
	public String goPopApprList(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopApprList taskNo    = " + taskVO.getTaskNo());
		logger.debug("goPopApprList subTaskNo = " + taskVO.getSubTaskNo());
		logger.debug("goPopApprList userId    = " + taskVO.getUserId());
		
		// 요청자 정보 조회
		UserVO userInfo = new UserVO();
		userInfo.setUserId(taskVO.getUserId());
		userInfo.setUilang((String)session.getAttribute("NEO_UILANG"));
		try {
			userInfo = systemService.getUserInfoDetail(userInfo);
		} catch(Exception e) {
			logger.error("systemService.getUserInfoDetail error = " + e);
		}
		
		// 발송결재라인 목록
		List<SecuApprovalLineVO> apprLineList = null;
		try {
			apprLineList = campaignService.getApprovalLineList(taskVO.getTaskNo());
		} catch(Exception e) {
			logger.error("campaignService.getApprovalLineList error = " + e);
		}
		
		model.addAttribute("taskVO", taskVO);				// 메일정보
		model.addAttribute("userInfo", userInfo);			// 요청자정보
		model.addAttribute("apprLineList", apprLineList);	// 발송결재라인목록
		
		return "ems/cam/pop/popApprList";
	}
	
	
	/**
	 * 결재취소처리
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/apprCancel")
	public ModelAndView cancelMailApproval(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("cancelMailApproval taskNos    = " + taskVO.getTaskNos());
		logger.debug("cancelMailApproval subTaskNos = " + taskVO.getSubTaskNos());
		logger.debug("cancelMailApproval status     = " + taskVO.getStatus());
		
		taskVO.setUpId((String)session.getAttribute("NEO_USER_ID"));
		taskVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));

		int result = 0;
		try {
			result = campaignService.cancelMailApproval(taskVO);
		} catch(Exception e) {
			logger.error("campaignService.cancelMailApproval error = " + e);
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
	 * 웹에이전트 미리보기 수신자그룹 정보
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/webAgentPreview")
	public ModelAndView webAgentPreview(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("webAgentPreview segNoc      = " + taskVO.getSegNoc());
		logger.debug("webAgentPreview webAgentUrl = " + taskVO.getWebAgentUrl());
		
		if(!StringUtil.isNull(taskVO.getSegNoc())) taskVO.setSegNo(Integer.parseInt( taskVO.getSegNoc().substring(0, taskVO.getSegNoc().indexOf("|")) ));
		
		String webAgentUrl = "";
		try {
			webAgentUrl = taskVO.getWebAgentUrl();
			
			logger.debug("webAgentPreview webAgentUrl[1] = " + webAgentUrl);
			
			if(taskVO.getSegNo() != 0) {
				SegmentVO segInfo = new SegmentVO();
				segInfo.setSegNo(taskVO.getSegNo());
				segInfo.setUilang((String)session.getAttribute("NEO_UILANG"));
				segInfo = segmentService.getSegmentInfo(segInfo);
				
				if("003".equals(segInfo.getCreateTy())) {
					int totCount = 0;	// 회원총수
					int aliasCnt = 0;	// 알리아스수
					
					List<HashMap<String,String>> memList = new ArrayList<HashMap<String,String>>();
					List<String> memAlias = new ArrayList<String>();
					
					BufferedReader line = null;
					String tempStr = "";
					try {
						String tmpFlPath = properties.getProperty("FILE.UPLOAD_PATH") + "/" + segInfo.getSegFlPath();
						line = new BufferedReader(new InputStreamReader(new FileInputStream(tmpFlPath), "UTF-8"));
						while((tempStr = line.readLine()) != null) {
							if("".equals(tempStr.trim())) continue;
							StringTokenizer st = new StringTokenizer(tempStr, segInfo.getSeparatorChar());
							
							if(totCount == 0) {	// 첫줄의 알리아스를 읽어 셋팅한다.
								while(st.hasMoreTokens()) {
									memAlias.add(st.nextToken());
									aliasCnt++;
								}
							} else {
								if(tempStr == null || "".equals(tempStr) || totCount == 2) break;
								
								HashMap<String, String> unitInfo = new HashMap<String,String>();
			                    for(int cnt = 0; cnt < aliasCnt; cnt++) {
			                    	unitInfo.put((String)memAlias.get(cnt), st.nextToken());
			                    }
			                    memList.add(unitInfo);
							}
							totCount++;
						}
						
						if(memList != null && memList.size() > 0) {
							HashMap<String, String> memInfo = memList.get(0);
							for(String colTitle:memAlias) {
								if(webAgentUrl.indexOf("$:" + colTitle + ":$") >= 0) {
									webAgentUrl = webAgentUrl.replace("$:" + colTitle + ":$", cryptoService.getDecrypt(colTitle, memInfo.get(colTitle)));
								}
							}
						}
					} catch(Exception segFileEx) {
						logger.error("webAgentPreview segFileEx error = " + segFileEx);
					}

				} else {
					// DB Connection 정보를 조회한다.
					DbConnVO dbConnInfo = null;
					try {
						DbConnVO searchVO = new DbConnVO();
						searchVO.setDbConnNo(segInfo.getDbConnNo());
						searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
						dbConnInfo = dbConnService.getDbConnInfo(searchVO);
					} catch(Exception e) {
						logger.error("dbConnService.getDbConnInfo error = " + e);
					}
					
					SegmentMemberVO memberVO = null;
					List<HashMap<String,String>> memberList = null;
					// 대상자 수 조회
					if(dbConnInfo != null) {
						DBUtil dbUtil = new DBUtil();
						String dbDriver = dbConnInfo.getDbDriver();
						String dbUrl = dbConnInfo.getDbUrl();
						String loginId = dbConnInfo.getLoginId();
						String loginPwd = EncryptUtil.getJasyptDecryptedString(properties.getProperty("JASYPT.ALGORITHM"), properties.getProperty("JASYPT.KEYSTRING"), dbConnInfo.getLoginPwd());
						if(segInfo.getDbConnNo() == 0) {
							String dataSourceName = "";
							try {
								String realPath = request.getServletContext().getRealPath("/");
								Properties prop = new Properties();
								prop.load(new FileInputStream(realPath + "/WEB-INF/config/properties/db.properties"));
								dataSourceName = prop.getProperty("Ums.DataSourceName");
							} catch(Exception e) {
								logger.error("DataSourceName Read Error!!");
							}
							memberVO = dbUtil.getMemberList(dataSourceName, segInfo);
						} else {
							memberVO = dbUtil.getMemberList(dbDriver, dbUrl, loginId, loginPwd, segInfo);
						}
					}
					
					if(memberVO != null) {
						memberList = memberVO.getMemberList();
						if(memberList != null && memberList.size() > 0) {
							HashMap<String,String> memInfo = memberList.get(0);
							String[] mergeKey = segInfo.getMergeKey().split(",");
							
							for(String colTitle:mergeKey) {
								if(webAgentUrl.indexOf("$:" + colTitle + ":$") >= 0) {
									webAgentUrl = webAgentUrl.replace("$:" + colTitle + ":$", cryptoService.getDecrypt(colTitle, memInfo.get(colTitle)));
								}
							}
						}
					}

				}
			}
			
			String paramUrl = "";
			if(webAgentUrl.indexOf("?")>=0) {
				String tempParamUrl = webAgentUrl.substring(webAgentUrl.indexOf("?")+1);
				String[] paramStr = tempParamUrl.split("&");
				for(int i=0;i<paramStr.length;i++) {
					String param = paramStr[i];
					if(param.indexOf("=")>=0) {
						String key = param.split("=")[0];
						String val = param.split("=")[1];
						paramUrl += (i==0?"":"&") + key + "=" + java.net.URLEncoder.encode(val,"UTF-8");
					}
				}
				webAgentUrl = webAgentUrl.substring(0, webAgentUrl.indexOf("?")) + "?" + paramUrl;
			}

			logger.debug("webAgentPreview webAgentUrl[2] = " + webAgentUrl);
		} catch(Exception e) {
			logger.error("webAgentPreview error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(webAgentUrl != null && !"".equals(webAgentUrl)) {
			map.put("result", "Success");
			map.put("webAgentUrl", webAgentUrl);
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}

	/**
	 * 메일 내역 조회 (팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popMailSendHist")
	public String goPopMailSendHist(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopMailSendHist taskNo = " + searchVO.getTaskNo());
		logger.debug("goPopMailSendHist subTaskNo   = " + searchVO.getSubTaskNo()); 
		 
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_POP")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		// 캠페인 목록
		List<TaskVO> mailSendHist = null;
		try {
			mailSendHist = campaignService.getMailSendHist(searchVO);
		} catch(Exception e) {
			logger.error("campaignService.getMailSendHist error = " + e);
		}
		
		if(mailSendHist != null && mailSendHist.size() > 0) {
			totalCount = mailSendHist.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		pageUtil.setSubmitFunc("goPageNumPopMailHist");
		
		model.addAttribute("searchVO", searchVO);	// 검색항목
		model.addAttribute("mailSendHist", mailSendHist);	// 캠페인목록
		model.addAttribute("pageUtil", pageUtil);	// 페이징
		
		return "ems/cam/pop/popMailSendHist";
	}
	
	/**
	 * API 테스트
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/api/sendMail")
	public void goApiTest(@RequestBody Map<String, Object> params, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goApiTest userId = " + params.get("resultCode"));
		logger.debug("goApiTest ueerName   = " + params.get("resultMessage")); 
		logger.debug("goApiTest data   = " + params.get("resultValue"));
		
		
		String rValue ="success";
		
		try {
			sendResultJson(response, rValue, "0000", "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void sendResultJson(HttpServletResponse response, String sJsonString, String sErrorCode, String sErrorMessage) throws Exception {
		
		PrintWriter writer;
		String returnValue = "{ \"resultCode\":\"%s\", \"resultMessage\":\"%s\", \"resultValue\":{%s} }";
		
		if (sErrorCode == null){
			sErrorCode = "9999";
		}
		
		response.setContentType("text/plain; charset=UTF-8");
		
		writer = response.getWriter();
		writer.write(String.format(returnValue, sErrorCode, sErrorMessage, sJsonString));
		
		writer.flush();
		writer.close();
		
	}
	
	/**
	 * API : sendEmailList 
	 * @param params
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @apiNote This Api, for  Huge(over 10,000) items 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/api/sendEmailList")
	public void goApiSendEmailList(@RequestBody Map<String, Object> params, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("===========sendEmailList=====================");
		logger.debug("params : " + params);
		
		String sResultmessage ="";
		String sResultcode ="000"; 
		
		JSONObject json = new JSONObject(); 
		String key = "";
		Object value = null;
		for(Map.Entry<String, Object> entry : params.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();
			json.put(key, value); 
		}
		 
		String requestkey = ""; 
		String templatecode = ""; 
		String senderemail = "";
		int campaignno = 0;
		int pagenumber = 0;
		int totalpagenumber = 0;
		int pagesize = 0;
		
		String requestoption = "";
		String messagename = "";
		String sendduedatatime = "";
		
		if(json.has("requestkey")) {
			requestkey = StringUtil.setNullToString(json.get("requestkey").toString());
			if("".equals(requestkey)) {
				sResultmessage += "[ 메시지요청키 누락 (requestkey)]";
				sResultcode = "E001";
			} else {
				if(requestkey.contains("_")){
					sResultmessage += "[ 메시지요청키 사용불가문자(_) 포함 (requestkey)]";
					sResultcode = "E011";
				} else {
					if(requestkey.length() >28){
						sResultmessage += "[ 메시지요청키 길이제한(28) 초과 (requestkey)]";
						sResultcode = "E011";
					} else {
						try {
							int requestKeyCount =  campaignService.getCountRequestKey(requestkey);
							if (requestKeyCount > 0 ) {
								sResultmessage = "[ 이미 사용된 키값임 (" + requestkey +")]";
								sResultcode = "E007";
							}
						} catch (Exception e) {
							logger.error("campaignService.getCountRequestKey error = " + e);
							sResultmessage = "[ 사용된 키 조회 오류 (" + requestkey +")]";
							sResultcode = "E006";
						}
					}

				}
			}
		} else {
			sResultcode = "E001";
			sResultmessage += "[ 메시지요청키 누락 (requestkey)]";
		}
		
		if(json.has("templatecode")) {
			templatecode = StringUtil.setNullToString(json.get("templatecode").toString());
			if("".equals(templatecode)) {
				sResultmessage += "[ 템플릿코드 누락 (templatecode)]";
				sResultcode = "E001";  
			} 
		} else {
			sResultmessage += "[ 템플릿코드 누락 (templatecode)]";
			sResultcode = "E001";  
		}
		
		if(json.has("senderemail")) {
			senderemail = StringUtil.setNullToString(json.get("senderemail").toString());
			if("".equals(senderemail)) {
				sResultmessage += "[ 발신자이메일 누락 (senderemail)]";
				sResultcode = "E001";  
			} 
		} else {
			sResultmessage += "[ 발신자이메일 누락 (senderemail)]";
			sResultcode = "E001";  
		}
		
		if(json.has("campaignno")) {
			campaignno = StringUtil.setNullToInt(json.get("campaignno").toString());
			if(campaignno == 0) {
				sResultmessage += "[ 캠페인번호 (campaignno)]";
				sResultcode = "E001";
			}
		} else {
			sResultmessage += "[ 캠페인번호 (campaignno)]";
			sResultcode = "E001";
		}
		
		if(json.has("pagenumber")) {
			pagenumber = StringUtil.setNullToInt(json.get("pagenumber").toString());
			if(pagenumber == 0) {
				sResultmessage += "[ 페이지번호 누락 (pagenumber)]";
				sResultcode = "E001";  
			} 
		} else {
			sResultmessage += "[ 페이지번호 누락 (pagenumber)]";
			sResultcode = "E001";  

		}
		
		if(json.has("totalpagenumber")) {
			totalpagenumber = StringUtil.setNullToInt(json.get("totalpagenumber").toString());
			if(totalpagenumber == 0) {
				sResultmessage += "[ 전체페이지번호 (totalpagenumber)]";
				sResultcode = "E001";
			} 
		} else {
			sResultmessage += "[ 전체페이지번호 (totalpagenumber)]";
			sResultcode = "E001";
		}
		
		if(json.has("pagesize")) {
			pagesize = StringUtil.setNullToInt(json.get("pagesize").toString());
			if(pagesize == 0) {
				sResultmessage += "[ 데이터건수 (pagesize)]";
				sResultcode = "E001";
			} 
		} else {
			sResultmessage += "[ 데이터건수 (pagesize)]";
			sResultcode = "E001";
		}
		
		if(json.has("requestoption")) {
			requestoption = StringUtil.setNullToString(json.get("requestoption").toString());
			if("".equals(requestoption)) {
				sResultmessage += "[ 요청옵션 누락 (requestoption)]";
				sResultcode = "E001";  
			} 
		} else {
			sResultmessage += "[ 요청옵션 누락 (requestoption)]";
			sResultcode = "E001";  
		}
		
		if(json.has("messagename")) {
			messagename = StringUtil.setNullToString(json.get("messagename").toString());
			if("".equals(messagename)) {
				sResultmessage += "[ 메시지 대표 명칭 (messagename)]";
				sResultcode = "E001";  
			} 
		} else {
			sResultmessage += "[ 메시지 대표 명칭 (messagename)]";
			sResultcode = "E001";  
		}
		
		if(json.has("sendduedatatime")) {
			sendduedatatime = StringUtil.setNullToString(json.get("sendduedatatime").toString());
			if("".equals(sendduedatatime)) {
				sResultmessage += "[ 발송 예정 일시 (sendduedatatime)]";
				sResultcode = "E001";  
			}
			if (!StringUtil.isValidDateString(json.get("sendduedatatime").toString())) {
				sResultmessage += "[ 발송 예정 일시 데이터 오류 (sendduedatatime)]";
				sResultcode = "E008";  
			}
		} else {
			sResultmessage += "[ 발송 예정 일시 (sendduedatatime)]";
			sResultcode = "E001";  
		}
		
		if ("000".equals(sResultcode)) {
			try {
				// 서비스 정보 조회 By EaiCampNo(templatecode)
				RnsServiceVO rnsServiceVO = new RnsServiceVO();
				try {
					rnsServiceVO = rnsServiceService.getServiceInfoByEai(templatecode);
					if (rnsServiceVO == null ) {
						sResultmessage = "[ 템플릿정보 없음 (" + templatecode +")]";
						sResultcode = "E003";
					} else {
						if ( "".equals(rnsServiceVO.getEaiCampNo())) {
							sResultmessage = "[ 템플릿정보 없음 (" + templatecode +")]";
							sResultcode = "E003";
						}
					}
				} catch (Exception e) {
					logger.error("rnsServiceService.getServiceInfoByEai error = " + e);
				}
			} catch (Exception e) {
				logger.error("rnsServiceService.getSmsTemplate error = " + e);
				sResultmessage = "[ 서버 데이터 템플릿 정보 조회 오류 (" + templatecode +")]";
				sResultcode = "E006";
			}
		}
		
		if ("000".equals(sResultcode)) {
			ArrayList<Map<String, Object>> dataArrList = (ArrayList) json.get("data");
			if(dataArrList.size() != pagesize) {
				sResultmessage += "[ 데이터건수 불일치 (pagesize)]";
				sResultcode = "E001";
			}
		}
		
		if (!"000".equals(sResultcode)) {
			try {
				sendApiResultJson(response, requestkey, sResultcode, sResultmessage, pagenumber, totalpagenumber, pagesize);
			} catch (Exception e) { 
				logger.error("SmsCampaignController.goApiSendEmailList Send Return error = " + e);
			} 
		} else {
			//파일로 만든다 ..
			//디렉토리있는지 확인
			String basePath = properties.getProperty("FILE.UPLOAD_PATH") + "/api/neoemail";
			String filePath = basePath + "/" + requestkey + "_" + pagenumber + "_"+ totalpagenumber + ".tmp";
			logger.debug("goApiSendEmailList filePath = " + filePath);
			
			// 파일 쓰기
			FileOutputStream fos = null;
			OutputStreamWriter writer = null;
			try {
				File baseDir = new File(basePath);
				if(!baseDir.isDirectory()) {
					baseDir.mkdir();
				}
				 
				File oldfile = new File(filePath);
				if(oldfile.exists()) {
					oldfile.delete();
				}
				
				File file = new File(filePath);
				fos = new FileOutputStream(file);
				writer = new OutputStreamWriter(fos, "UTF-8");
				writer.write(json.toString());
				writer.flush();
			} catch(Exception e) {
				logger.error("goApiSendEmailList file write error = " + e);
			} finally {
				if(writer != null) try { writer.close(); } catch(Exception e) {};
				if(fos != null) try { fos.close(); } catch(Exception e) {};
			}
			
			File path = new File(basePath);
			final String pattern = requestkey +"_";
			
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.startsWith(pattern);
				}
			};
			
			String[] fileList = path.list(filter);
			if (fileList.length == totalpagenumber) {
				for (int k = 0 ; k<fileList.length ; k++) {
					String oldName = fileList[k];
					String newName = fileList[k].replace(".tmp", ".txt") ;
					
					Path file = Paths.get(basePath + "/" + oldName);
					Path newFile = Paths.get(basePath + "/" + newName);
					
					try 
					{
						Path newFilePath = Files.move(file, newFile);
						System.out.println(newFilePath);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				sendApiResultJson(response, requestkey, sResultcode, sResultmessage, pagenumber, totalpagenumber, pagesize);
			} catch (Exception e) { 
				logger.error("SmsCampaignController.goApiSendEmailList Send Return error = " + e);
			}
		}
	}
	
	public static void sendApiResultJson(HttpServletResponse response, String sRequestkey, String sResultcode, String sResultmessage, int nPagenumber, int nTotalpageNumber, int nPagesize) throws Exception {
		
		PrintWriter writer;
		String returnValue = "";
		String sPagenumber = Integer.toString(nPagenumber);
		String sTotalpageNumber = Integer.toString(nTotalpageNumber);
		String sPagesize = Integer.toString(nPagesize);
		
		returnValue = "{ \"requestkey\":\"%s\", \"resultcode\":\"%s\", \"resultmessage\":\"%s\", \"pagenumber\":\"%s\", \"totalpagenumber\":\"%s\", \"pagesize\":[%s]}"; 
		
		if (sResultcode == null){
			sResultcode = "9999";
		}
		if ("000".equals(sResultcode)) {
			sResultmessage ="Success";
		}
		
		response.setContentType("text/plain; charset=UTF-8");
		writer = response.getWriter();
		
		writer.write(String.format(returnValue, sRequestkey, sResultcode, sResultmessage, sPagenumber, sTotalpageNumber, sPagesize));
		
		writer.flush();
		writer.close();
	}
}


