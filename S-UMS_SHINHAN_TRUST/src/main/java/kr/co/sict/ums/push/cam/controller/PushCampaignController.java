/**
 * 작성자 : 김준희
 * 작성일시 : 2022.04.02
 * 설명 : PUSH 관련 Controller 
 */
package kr.co.sict.ums.push.cam.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import kr.co.sict.ums.com.vo.CodeVO;
import kr.co.sict.ums.com.service.CodeService;
import kr.co.sict.ums.com.service.CryptoService;
import kr.co.sict.ums.sys.acc.service.AccountService;
import kr.co.sict.ums.ems.seg.service.SegmentService;
import kr.co.sict.ums.sys.seg.service.SmsTemplateService;
import kr.co.sict.ums.push.cam.service.PushCampaignService;
import kr.co.sict.ums.push.cam.vo.PushCampaignVO;
import kr.co.sict.ums.push.cam.vo.PushVO;
import kr.co.sict.ums.rns.svc.vo.RnsServiceVO;
import kr.co.sict.ums.sms.cam.vo.SmsPhoneVO;
import kr.co.sict.ums.sms.cam.vo.SmsVO;
import kr.co.sict.ums.sys.acc.vo.UserVO;
import kr.co.sict.ums.sys.dbc.vo.DbConnVO;
import kr.co.sict.ums.ems.seg.vo.SegmentVO;
import kr.co.sict.ums.sys.seg.vo.SmsTemplateVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.PageUtil;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Controller
@RequestMapping("/push/cam")
public class PushCampaignController {
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private CodeService codeService;

	@Autowired
	private PushCampaignService pushCampaignService;
	
	@Autowired
	private SmsTemplateService smsTemplateService;
	
	@Autowired
	private SegmentService segmentService;

	@Autowired
	private AccountService systemService;

	@Autowired
	private CryptoService cryptoService;

	@Autowired
	private PropertiesUtil properties;

	/****************************************** PUSH 발송 캠페인 관리 ******************************************/
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
	public String goCampListP(@ModelAttribute PushCampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goCampListP searchCampNm  = " + searchVO.getSearchCampNm());
		logger.debug("goCampListP searchCampTy  = " + searchVO.getSearchCampTy());
		logger.debug("goCampListP searchStatus  = " + searchVO.getSearchStatus());
		logger.debug("goCampListP searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goCampListP searchEndDt   = " + searchVO.getSearchEndDt());
		logger.debug("goCampListP searchDeptNo  = " + searchVO.getSearchDeptNo());
		logger.debug("goCampListP searchUserId  = " + searchVO.getSearchUserId());
		// 검색 기본값 설정
		if (searchVO.getSearchStartDt() == null || "".equals(searchVO.getSearchStartDt())) {
			searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-3, "M", "yyyyMMdd"));
		} else {
			searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		}
		if (searchVO.getSearchEndDt() == null || "".equals(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		}
		if (searchVO.getSearchDeptNo() == 0) {
			if ("Y".equals((String) session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo(0);
			} else {
				searchVO.setSearchDeptNo((int) session.getAttribute("NEO_DEPT_NO"));
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
		
		return "push/cam/campListP";
	}
	
	/**
	 * 캠페인 목록 조회
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campList")
	public String goCampList(@ModelAttribute PushCampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goCampList searchCampNm  = " + searchVO.getSearchCampNm());
		logger.debug("goCampList searchCampTy  = " + searchVO.getSearchCampTy());
		logger.debug("goCampList searchStatus  = " + searchVO.getSearchStatus());
		logger.debug("goCampList searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goCampList searchEndDt   = " + searchVO.getSearchEndDt());
		logger.debug("goCampList searchDeptNo  = " + searchVO.getSearchDeptNo());
		logger.debug("goCampList searchUserId  = " + searchVO.getSearchUserId());
		
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		if(searchVO.getSearchDeptNo() == 0) {
			if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo(0);
			} else {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		searchVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		// 캠페인 목록 조회
		List<PushCampaignVO> campaignList = null;
		try {
			campaignList = pushCampaignService.getCampaignList(searchVO);
		} catch(Exception e) {
			logger.error("pushCampaignService.getCampaignPushList error = " + e);
		}
		
		if(campaignList != null && campaignList.size() > 0) {
			totalCount = campaignList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("searchVO", searchVO);			// 검색 항목
		model.addAttribute("campaignList", campaignList);	// 캠페인 목록
		model.addAttribute("pageUtil", pageUtil);			// 페이징
		
		return "push/cam/campList";
	}

	/**
	 * 캠페인 정보 조회
	 * @param PushCampaignVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campInfo")
	public ModelAndView getCampInfo(@ModelAttribute PushCampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getCampInfo campNo      = " + searchVO.getCampNo());

		searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		PushCampaignVO pushCampaignVO = null;
		try {
			pushCampaignVO = pushCampaignService.getCampaignInfo(searchVO);
		} catch(Exception e) {
			logger.error("pushCampaignService.getCampaignInfo error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("campaign", pushCampaignVO);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 캠페인 정보 등록 화면을 출력한다.
	 * @param PushCampaignVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campAddP")
	public String goCampAddP(@ModelAttribute PushCampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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
		int deptNo = "Y".equals((String)session.getAttribute("NEO_ADMIN_YN")) ? 0 : (int) session.getAttribute("NEO_DEPT_NO");
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
		model.addAttribute("campTyList", campTyList);		// 캠페인목적 목록
		model.addAttribute("statusList", statusList);		// 캠페인상태 목록
		model.addAttribute("deptList", deptList);			// 부서 목록
		model.addAttribute("userList", userList);			// 사용자 목록

		return "push/cam/campAddP";
	}

	/**
	 * 캠페인 정보 등록
	 * @param PushCampaignVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campAdd")
	public ModelAndView insertCampInfo(@ModelAttribute PushCampaignVO PushCampaignVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("insertCampInfo campNm      = " + PushCampaignVO.getCampNm());
		logger.debug("insertCampInfo campDesc      = " + PushCampaignVO.getCampDesc());
		logger.debug("insertCampInfo campTy      = " + PushCampaignVO.getCampTy());
		logger.debug("insertCampInfo status      = " + PushCampaignVO.getStatus());
		logger.debug("insertCampInfo deptNo      = " + PushCampaignVO.getDeptNo());
		logger.debug("insertCampInfo userId      = " + PushCampaignVO.getUserId());
		PushCampaignVO.setRegId((String)session.getAttribute("NEO_USER_ID"));
		PushCampaignVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));

		int result = 0;
		try {
			result = pushCampaignService.insertCampaignInfo(PushCampaignVO);
		} catch(Exception e) {
			logger.error("pushCampaignService.insertCampaignInfo error = " + e);
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
	 * 캠페인 정보 수정 화면을 출력한다.
	 * @param PushCampaignVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campUpdateP")
	public String goCampUpdateP(@ModelAttribute PushCampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goCampUpdateP campNo = " + searchVO.getCampNo());

		// 캠페인 정보 조회
		PushCampaignVO campInfo = null;
		searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		try {
			campInfo = pushCampaignService.getCampaignInfo(searchVO);
		} catch(Exception e) {
			logger.error("pushCampaignService.getCampaignInfo error = " + e);
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
		int deptNo = "Y".equals((String) session.getAttribute("NEO_ADMIN_YN")) ? 0: (int) session.getAttribute("NEO_DEPT_NO");
		CodeVO user = new CodeVO();
		user.setDeptNo(deptNo);
		user.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(user);
		} catch(Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}

		model.addAttribute("searchVO", searchVO); // 검색항목
		model.addAttribute("campInfo", campInfo); // 캠페인 정보
		model.addAttribute("campTyList", campTyList); // 캠페인목적 목록
		model.addAttribute("statusList", statusList); // 캠페인상태 목록
		model.addAttribute("deptList", deptList); // 부서 목록
		model.addAttribute("userList", userList); // 사용자 목록

		return "push/cam/campUpdateP";
	}

	/**
	 * 캠페인 정보 수정
	 * @param PushCampaignVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/campUpdate")
	public ModelAndView updateCampInfo(@ModelAttribute PushCampaignVO PushCampaignVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateCampInfo campNo      = " + PushCampaignVO.getCampNo());
		logger.debug("updateCampInfo campNm      = " + PushCampaignVO.getCampNm());
		logger.debug("updateCampInfo campDesc      = " + PushCampaignVO.getCampDesc());
		logger.debug("updateCampInfo campTy      = " + PushCampaignVO.getCampTy());
		logger.debug("updateCampInfo status      = " + PushCampaignVO.getStatus());
		logger.debug("updateCampInfo deptNo      = " + PushCampaignVO.getDeptNo());
		logger.debug("updateCampInfo userId      = " + PushCampaignVO.getUserId());
		PushCampaignVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		PushCampaignVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));

		int result = 0;
		try {
			result = pushCampaignService.updateCampaignInfo(PushCampaignVO);
		} catch (Exception e) {
			logger.error("pushCampaignService.updateCampaignInfo error = " + e);
		}

		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}
	
	/****************************************** PUSH 발송 관리 ******************************************/
	/**
	 * PUSH 발송 목록 화면에서 사용하게될 조회쪽 Data / 목록쪽으로 넘길 Data Controller
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/pushListP")
	public String goPushListP(@ModelAttribute PushVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPushListP searchStartDt    = " + searchVO.getSearchStartDt()); // 데이터 필요
		logger.debug("goPushListP searchEndDt      = " + searchVO.getSearchEndDt()); // 데이터 필요
		logger.debug("goPushListP searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goPushListP searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goPushListP searchStatus     = " + searchVO.getSearchStatus());
		logger.debug("goPushListP searchWorkStatus = " + searchVO.getSearchWorkStatus());
		logger.debug("goPushListP searchPushGubun  = " + searchVO.getSearchPushGubun());

		// 검색 기본값 설정
		if (StringUtil.isNull(searchVO.getSearchStartDt())) {
			searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-1, "M", "yyyyMMdd"));
		} else {
			searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		}
		if (StringUtil.isNull(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		}
		if (searchVO.getSearchDeptNo() == 0) {
			if (!"Y".equals((String) session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo((int) session.getAttribute("NEO_DEPT_NO"));
			}
		}
		searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		searchVO.setAdminYn((String) session.getAttribute("NEO_ADMIN_YN"));
		
		// 캠페인 목록 (공통)
		PushCampaignVO pushCampVO = new PushCampaignVO();
		pushCampVO.setStatus("000");
		pushCampVO.setAdminYn((String) session.getAttribute("NEO_ADMIN_YN"));
		pushCampVO.setDeptNo("Y".equals((String) session.getAttribute("NEO_ADMIN_YN")) ? 0 : (int) session.getAttribute("NEO_DEPT_NO"));
		
		List<PushCampaignVO> pushCampList = null;
		try {
			pushCampList = codeService.getCampaignPushList(pushCampVO);
		} catch (Exception e) {
			logger.error("codeService.getCampaignPushList error = " + e);
		}
		
		// 사용자그룹(부서) 목록
		CodeVO deptVO = new CodeVO();
		deptVO.setStatus("000");
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(deptVO);
		} catch (Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 사용자명 목록 
		CodeVO userVO = new CodeVO();
		userVO.setDeptNo("Y".equals((String) session.getAttribute("NEO_ADMIN_YN")) ? 0 : (int) session.getAttribute("NEO_DEPT_NO"));
		userVO.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch (Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		// PUSH 상태코드 목록
		CodeVO statusVO = new CodeVO();
		statusVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		statusVO.setCdGrp("C009");
		statusVO.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(statusVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[C009] error = " + e);
		}
		
		// PUSH 발송  상태코드 목록
		CodeVO workStatus = new CodeVO();
		workStatus.setUilang((String) session.getAttribute("NEO_UILANG"));
		workStatus.setCdGrp("C118");
		workStatus.setUseYn("Y");
		List<CodeVO> workStatusList = null;
		try {
			workStatusList = codeService.getCodeList(workStatus);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[C118] error = " + e);
		}
		
		// PUSH 구분
		CodeVO pushGubunVO = new CodeVO();
		workStatus.setUilang((String) session.getAttribute("NEO_UILANG"));
		pushGubunVO.setCdGrp("C124");
		pushGubunVO.setUseYn("Y");
		List<CodeVO> pushGubunList = null;
		try {
			pushGubunList = codeService.getCodeList(pushGubunVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[C124] error = " + e);
		}
		
		// 고객정보 조회사유코드 조회 (발송 고객 점검 000 / 결재 고객 점검 001 / 기타 099)
		CodeVO reasonVO = new CodeVO();
		reasonVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		reasonVO.setCdGrp("C102");
		reasonVO.setUseYn("Y");
		List<CodeVO> reasonList = null;
		try {
			reasonList = codeService.getCodeList(reasonVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[C102] error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO); 				// 검색항목
		model.addAttribute("pushCampList", pushCampList); 		// 캠페인목록
		model.addAttribute("deptList", deptList); 				// 사용자그룹목록
		model.addAttribute("userList", userList);				// 사용자목록
		model.addAttribute("statusList", statusList); 			// PUSH 발송상태목록
		model.addAttribute("workStatusList", workStatusList); 	// PUSH 상태목록
		model.addAttribute("pushGubunList", pushGubunList); 	// PUSH 전송유형
		model.addAttribute("reasonList", reasonList); 			// 수신자그룹조회 사유
		
		return "push/cam/pushListP";
	}
	
	/**
	 *  PUSH 발송 등록현황 목록을 조회한다	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/pushList")
	public String goPushList(@ModelAttribute PushVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPushList searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goPushList searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("goPushList searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goPushList searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goPushList searchStatus     = " + searchVO.getSearchStatus());
		logger.debug("goPushList searchWorkStatus = " + searchVO.getSearchWorkStatus());
		logger.debug("goPushList searchPushGubun  = " + searchVO.getSearchPushGubun());
		
		// 검색 기본값 설정
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		searchVO.setAdminYn((String) session.getAttribute("NEO_ADMIN_YN"));

		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage() - 1) * searchVO.getRows());
		int totalCount = 0;

		// PUSH 목록 조회
		List<PushVO> pushList = null;
		try {
			pushList = pushCampaignService.getPushList(searchVO);
		} catch (Exception e) {
			logger.error("pushCampaignService.getPushList error = " + e);
		}

		if (pushList != null && pushList.size() > 0) {
			totalCount = pushList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);

		model.addAttribute("searchVO", searchVO); // 검색 항목
		model.addAttribute("pushList", pushList); // PUSH발송 목록
		model.addAttribute("pageUtil", pageUtil); // 페이징

		return "push/cam/pushList";
	}

	/**
	 * PUSH 발송 신규등록 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/pushAddP")
	public String goPushAddP(@ModelAttribute PushVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPushAddP searchStartDt   = " + searchVO.getSearchStartDt());
		logger.debug("goPushAddP searchEndDt     = " + searchVO.getSearchEndDt());
		logger.debug("goPushAddP searchDeptNo    = " + searchVO.getSearchDeptNo());
		logger.debug("goPushAddP searchCampNo    = " + searchVO.getSearchCampNo());
		logger.debug("goPushAddP searchStatus    = " + searchVO.getSearchStatus());
		logger.debug("goPushAddP searchPushGubun = " + searchVO.getSearchPushGubun());
		
		// 수신자정보머지키코드 목록
		CodeVO merge = new CodeVO();
		merge.setUilang((String) session.getAttribute("NEO_UILANG"));
		merge.setCdGrp("C001");
		merge.setUseYn("Y");
		List<CodeVO> mergeList = null;
		try {
			mergeList = codeService.getCodeList(merge);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[C001] error = " + e);
		}
		
		// 사용자그룹(부서) 목록
		CodeVO deptVO = new CodeVO();
		deptVO.setStatus("000");
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(deptVO);
		} catch (Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 사용자 목록
		CodeVO userVO = new CodeVO();
		userVO.setDeptNo("Y".equals((String) session.getAttribute("NEO_ADMIN_YN")) ? 0 : (int) session.getAttribute("NEO_DEPT_NO"));
		userVO.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch (Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		// 발송상태코드 목록 (발송대기 / 발송승인 / 발송중 ....)
		CodeVO workStatus = new CodeVO();
		workStatus.setUilang((String) session.getAttribute("NEO_UILANG"));
		workStatus.setCdGrp("C118");
		workStatus.setUseYn("Y");
		List<CodeVO> workStatusList = null;
		try {
			workStatusList = codeService.getCodeList(workStatus);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error = " + e);
		}
		
		// 상태코드 목록 (정상 000 / 사용중지 001 / 삭제 002)
		CodeVO status = new CodeVO();
		status.setUilang((String) session.getAttribute("NEO_UILANG"));
		status.setCdGrp("C009");
		status.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(status);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error = " + e);
		}
		
		// PUSH 무료수신거부 안내
		CodeVO legal = new CodeVO();
		legal.setUilang((String) session.getAttribute("NEO_UILANG"));
		legal.setCdGrp("C117");
		legal.setCd("002");
		legal.setUseYn("Y");
		
		String pushLegalCf ="";
		try {
			CodeVO pushLegal = codeService.getCodeInfo(legal);
			if(pushLegal != null && !"".equals(pushLegal.getCdNm())) {
				pushLegalCf = pushLegal.getCdNm();
			} else {//설정->수신설정
				pushLegalCf = "설정된 수신거부 경로가 없습니다";
			}
		} catch (Exception e) {
			logger.error("codeService.getCodeInfo[C117] error = " + e);
		}
		
		// 캠페인 목록 조회
		PushCampaignVO pushCamp = new PushCampaignVO();
		pushCamp.setUilang((String) session.getAttribute("NEO_UILANG"));
		pushCamp.setSearchStatus("000");
		pushCamp.setPage(1);
		pushCamp.setRows(10000);
		pushCamp.setSearchDeptNo("Y".equals((String) session.getAttribute("NEO_ADMIN_YN")) ? 0 : (int) session.getAttribute("NEO_DEPT_NO"));
		List<PushCampaignVO> pushCampList = null;
		try {
			pushCampList = pushCampaignService.getCampaignList(pushCamp);
		} catch (Exception e) {
			logger.error("pushCampaignService.getCampaignList error = " + e);
		}
		
		// 수신자그룹(발송대상) 목록 
		SegmentVO seg = new SegmentVO();
		seg.setUilang((String) session.getAttribute("NEO_UILANG"));
		seg.setSearchStatus("000");
		seg.setPage(1);
		seg.setRows(10000);
		seg.setSearchDeptNo("Y".equals((String) session.getAttribute("NEO_ADMIN_YN")) ? 0: (int) session.getAttribute("NEO_DEPT_NO"));
		seg.setPushuseYn("Y");
		
		List<SegmentVO> segList = null;
		try {
			segList = segmentService.getSegmentList(seg);
		} catch (Exception e) {
			logger.error("segmentService.getSegmentList error = " + e);
		}
		
		// 고객정보 조회사유코드 조회
		CodeVO reasonVO = new CodeVO();
		reasonVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		reasonVO.setCdGrp("C102");
		reasonVO.setUseYn("Y");
		List<CodeVO> reasonList = null;
		try {
			reasonList = codeService.getCodeList(reasonVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[C102] error = " + e);
		}
		
		// 사용자정보 (전달받은 사용자 정보 user_id)
		UserVO userInfo = new UserVO();
		userInfo.setUilang((String) session.getAttribute("NEO_UILANG"));
		userInfo.setUserId((String) session.getAttribute("NEO_USER_ID"));
		try {
			userInfo = systemService.getUserInfo(userInfo);
		} catch (Exception e) {
			logger.error("systemService.getUserInfo error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);	 							// 검색항목
		model.addAttribute("pushCampList", pushCampList); 						// 캠페인목록
		model.addAttribute("deptList", deptList); 								// 부서 목록
		model.addAttribute("userList", userList); 								// 사용자목록
		model.addAttribute("userInfo", userInfo); 								// 사용자정보
		model.addAttribute("mergeList", mergeList); 							// 수신자정보머지키코드
		model.addAttribute("statusList", statusList); 							// 상태
		model.addAttribute("workStatusList", workStatusList);					// 발송상태
		model.addAttribute("reasonList", reasonList); 							// 조회사유
		model.addAttribute("segList", segList); 								// 발송대상(세그먼트) 목록
		model.addAttribute("pushLegalCf", pushLegalCf); 						// 수신거부 설정 방법안내
		model.addAttribute("appLogo", properties.getProperty("PUSH.APP_LOGO"));	// Push 앱 로고 
		model.addAttribute("appName", properties.getProperty("PUSH.APP_NAME"));	// Push 앱 이름
		model.addAttribute("imgUploadPushPath", properties.getProperty("IMG.PUSH_UPLOAD_PATH"));// Push 첨부 이미지경로
		model.addAttribute("DEFAULT_DOMAIN", properties.getProperty("IMG.DEFAULT_DOMAIN"));		// 이미지미리보기위한 Domain URL
		
		return "push/cam/pushAddP";
	}
	
	/**
	 * PUSH 정보를 등록한다.
	 * @param pushVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/pushAdd")
	public String goPushAdd(@ModelAttribute PushVO pushVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPushAdd userId        = " + pushVO.getUserId());
		logger.debug("goPushAdd deptNo        = " + pushVO.getDeptNo());
		logger.debug("goPushAdd segNoc        = " + pushVO.getSegNoc());
		logger.debug("goPushAdd campNo        = " + pushVO.getCampNo());
		logger.debug("goPushAdd sendYmd       = " + pushVO.getSendYmd());
		logger.debug("goPushAdd sendHour      = " + pushVO.getSendHour());
		logger.debug("goPushAdd sendMin       = " + pushVO.getSendMin());
		logger.debug("goPushAdd sendTyp       = " + pushVO.getSendTyp());
		logger.debug("goPushAdd pushName      = " + pushVO.getPushName());
		logger.debug("goPushAdd pushGubun     = " + pushVO.getPushGubun());
		logger.debug("goPushAdd status        = " + pushVO.getStatus());
		logger.debug("goPushAdd pushTitle     = " + pushVO.getPushTitle());
		logger.debug("goPushAdd pushMessage   = " + pushVO.getPushMessage());
		logger.debug("goPushAdd attachNm      = " + pushVO.getAttachNm());
		logger.debug("goPushAdd attachPath    = " + pushVO.getAttachPath());
		logger.debug("goPushAdd workStatus    = " + pushVO.getWorkStatus());
		logger.debug("goPushAdd legalYn       = " + pushVO.getLegalYn());
		logger.debug("goPushAdd legalCf       = " + pushVO.getLegalCf());
		logger.debug("goPushAdd smsYn         = " + pushVO.getSmsYn());
		logger.debug("goPushAdd callUrlTyp    = " + pushVO.getCallUrlTyp());
		logger.debug("goPushAdd AndroidURL    = " + pushVO.getCallUri());
		logger.debug("goPushAdd IOSURL        = " + pushVO.getCallUriIos());
		
		// 기본값 설정
		if (pushVO.getDeptNo() == 0) pushVO.setDeptNo((int) session.getAttribute("NEO_DEPT_NO")); 					// 부서번호
		if (StringUtil.isNull(pushVO.getUserId())) pushVO.setUserId((String) session.getAttribute("NEO_USER_ID"));  // 사용자아이디
		
		pushVO.setWorkStatus("000"); // PUSH발송상태 기본값 발송대기
		pushVO.setStatus("000");     //PUSH 상태 
		
		if (!StringUtil.isNull(pushVO.getSegNoc())) pushVO.setSegNo(Integer.parseInt(pushVO.getSegNoc().substring(0, pushVO.getSegNoc().indexOf("|"))));// 세그먼트번호(발송대상그룹)
		
		//1.즉시 여부에 따른 발송시간 입력
		if("000".equals(pushVO.getSendTyp())){
			pushVO.setSendDt(StringUtil.getDate(Code.TM_YMDHMS));
		} else {
			if(StringUtil.isNull(pushVO.getSendYmd())) pushVO.setSendYmd("0000-00-00");					// 예약시간(예약일)
			String sendHour = StringUtil.setTwoDigitsString(pushVO.getSendHour());						// 예약시간(시)
			String sendMin = StringUtil.setTwoDigitsString(pushVO.getSendMin());						// 예약시간(분)
			String sendSec = "00";																		// 예약시간(초)
			pushVO.setSendDt( pushVO.getSendYmd().replaceAll("\\.", "") + sendHour + sendMin + sendSec );// 예약일시
		}
		
		//1.첨부파일 등록
		if(pushVO.getAttachPath() != null && !"".equals(pushVO.getAttachPath())) {
			File attachFile = null;
			String basePath = properties.getProperty("IMG.PUSH_UPLOAD_PATH");
			
			long fileSize = 0;
			String[] fileNm = pushVO.getAttachNm().split(",");
			String[] filePath = pushVO.getAttachPath().split(",");
			
			//for(int i=0;i<filePath.length;i++) {
			for(int i=0;i< 1 ;i++) {
				attachFile = new File(basePath + "/" + filePath[i]);
				fileSize = attachFile.length();
				pushVO.setFileNm(fileNm[i]);
				pushVO.setFilePath(filePath[i]);
				pushVO.setFileSize(fileSize);
			}
		}
		
		// 2.등록자 정보 입력
		pushVO.setRegId((String) session.getAttribute("NEO_USER_ID"));
		pushVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		// 3. 광고 체크 했을때와 아니었을떄의 추가 문자 처리 
		String pushMessageHead = "";
		String pushMessageTail = "";
		String pushMessage = "";
		if (pushVO.getLegalYn() != null && "Y".equals(pushVO.getLegalYn())) {
			pushMessageHead = "(광고)";
			// 수신거부 번호
			pushMessageTail = pushVO.getLegalCf();
		}
		pushMessageHead = pushMessageHead + pushVO.getPushTitle();
		pushMessage = pushMessageHead + "\n" + pushVO.getPushMessage();
		if (!"".equals(pushMessageTail)) {
			pushMessage = pushMessageHead + "\n" + pushVO.getPushMessage() + "\n" + pushMessageTail;
		}

		pushVO.setPushMessage(pushMessage);
		
		logger.debug("goPushAdd pushMessage [" + pushMessage + "]");
		
		// 5. URL Type (String 으로 저장 Code 저장 000 => webLink 001 => webOpen 002 => webVideo)
		if(pushVO.getCallUrlTyp() != null) {
			pushVO.setCallUrlTyp(pushVO.getCallUrlTyp().trim());
		}
		
		// 6. Android URL (앞뒤공백 제거)
		if(pushVO.getCallUri() != null ) {
			pushVO.setCallUri(pushVO.getCallUri().trim());
		}
		
		// 7. IOS URL (앞뒤 공백 제거) 
		if(pushVO.getCallUriIos() != null ) {
			pushVO.setCallUriIos(pushVO.getCallUriIos().trim());
		}
		
		int result = 0; 
		try {
			result = pushCampaignService.insertPushInfo(pushVO);
		} catch (Exception e) {
			logger.error("pushCampaignService.insertPushInfo error = " + e);
		}
		logger.debug("insertPushInfo new pushmessageId [" + pushVO.getPushmessageId() + "]");
		// 결과값
		if (result > 0) {
			model.addAttribute("result", "Success");
			model.addAttribute("pushmessageId", pushVO.getPushmessageId());
		} else {
			model.addAttribute("result", "Fail");
		}
		
		return "push/cam/pushAdd";
	}

	/**
	 * PUSH 정보 수정 화면을 출력한다.
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/pushUpdateP")
	public String goPushUpdateP(@ModelAttribute PushVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		logger.debug("goPushUpdateP getPushmessageId   = " + searchVO.getPushmessageId());
		
		searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		// 수신자정보머지키코드 목록
		CodeVO merge = new CodeVO();
		merge.setUilang((String) session.getAttribute("NEO_UILANG"));
		merge.setCdGrp("C001");
		merge.setUseYn("Y");
		List<CodeVO> mergeList = null;
		try {
			mergeList = codeService.getCodeList(merge);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[C001] error = " + e);
		}
 
		// 사용자그룹(부서) 목록
		CodeVO deptVO = new CodeVO();
		deptVO.setStatus("000");
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(deptVO);
		} catch (Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 사용자 목록
		CodeVO userVO = new CodeVO();
		userVO.setDeptNo("Y".equals((String) session.getAttribute("NEO_ADMIN_YN")) ? 0 : (int) session.getAttribute("NEO_DEPT_NO"));
		userVO.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch (Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		// 발송상태코드 목록 (발송대기 / 발송승인 / 발송중 ....)
		CodeVO workStatus = new CodeVO();
		workStatus.setUilang((String) session.getAttribute("NEO_UILANG"));
		workStatus.setCdGrp("C118");
		workStatus.setUseYn("Y");
		List<CodeVO> workStatusList = null;
		try {
			workStatusList = codeService.getCodeList(workStatus);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error = " + e);
		}
		
		// 상태코드 목록 (정상 000 / 사용중지 001 / 삭제 002)
		CodeVO status = new CodeVO();
		status.setUilang((String) session.getAttribute("NEO_UILANG"));
		status.setCdGrp("C009");
		status.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(status);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error = " + e);
		}
		
		// PUSH 무료수신거부 안내
		CodeVO legal = new CodeVO();
		legal.setUilang((String) session.getAttribute("NEO_UILANG"));
		legal.setCdGrp("C117");
		legal.setCd("002");
		legal.setUseYn("Y");
		
		String pushLegalCf ="";
		try {
			CodeVO pushLegal = codeService.getCodeInfo(legal);
			if(pushLegal != null && !"".equals(pushLegal.getCdNm())) {
				pushLegalCf = pushLegal.getCdNm();
			} else {//설정->수신설정
				pushLegalCf = "설정된 수신거부 경로가 없습니다";
			}
		} catch (Exception e) {
			logger.error("codeService.getCodeInfo[C117] error = " + e);
		}
		
		// 캠페인 목록 조회
		PushCampaignVO pushCamp = new PushCampaignVO();
		pushCamp.setUilang((String) session.getAttribute("NEO_UILANG"));
		pushCamp.setSearchStatus("000");
		pushCamp.setPage(1);
		pushCamp.setRows(10000);
		pushCamp.setSearchDeptNo("Y".equals((String) session.getAttribute("NEO_ADMIN_YN")) ? 0 : (int) session.getAttribute("NEO_DEPT_NO"));
		List<PushCampaignVO> pushCampList = null;
		try {
			pushCampList = pushCampaignService.getCampaignList(pushCamp);
		} catch (Exception e) {
			logger.error("pushCampaignService.getCampaignList error = " + e);
		}

		// 수신자그룹(발송대상) 목록 (Push 대상임)
		SegmentVO seg = new SegmentVO();
		seg.setUilang((String) session.getAttribute("NEO_UILANG"));
		seg.setSearchStatus("000");
		seg.setPage(1);
		seg.setRows(10000);
		seg.setSearchDeptNo("Y".equals((String) session.getAttribute("NEO_ADMIN_YN")) ? 0: (int) session.getAttribute("NEO_DEPT_NO"));
		seg.setSearchPushuseYn("Y");
		
		List<SegmentVO> segList = null;
		try {
			segList = segmentService.getSegmentList(seg);
		} catch (Exception e) {
			logger.error("segmentService.getSegmentList error = " + e);
		}
		
		// 고객정보 조회사유코드 조회
		CodeVO reasonVO = new CodeVO();
		reasonVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		reasonVO.setCdGrp("C102");
		reasonVO.setUseYn("Y");
		List<CodeVO> reasonList = null;
		try {
			reasonList = codeService.getCodeList(reasonVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[C102] error = " + e);
		}
		
		// 사용자정보
		UserVO userInfo = new UserVO();
		userInfo.setUilang((String) session.getAttribute("NEO_UILANG"));
		userInfo.setUserId((String) session.getAttribute("NEO_USER_ID"));
		try {
			userInfo = systemService.getUserInfo(userInfo);
		} catch (Exception e) {
			logger.error("systemService.getUserInfo error = " + e);
		}
		
		// PUSH 상세정보 조회
		PushVO pushInfo = new PushVO();
		try {
			pushInfo = pushCampaignService.getPushInfo(searchVO);
		} catch (Exception e) {
			logger.error("pushCampaignService.getPushInfo error = " + e);
		}
		
		//PUSH Message 분리
		String pushMessageFull = pushInfo.getPushMessage();
		logger.debug("pushMessageFull [" + pushMessageFull  + "]");
		String pushMessageHead = "";
		String pushMessageTail = "";
		String pushMessage ="";
		if(pushInfo.getLegalYn()!= null && "Y".equals(pushInfo.getLegalYn())) {
			pushMessageHead ="(광고)";
			pushMessageTail = pushInfo.getLegalCf();
		}
		pushMessageHead = pushMessageHead + pushInfo.getPushTitle() + "\n";
		pushMessageFull = pushMessageFull.replace(pushMessageHead, "");
		pushMessageFull = "\n" + pushMessageFull.replace(pushMessageTail, "");
		pushMessage = pushMessageFull.trim();
		
		logger.debug("pushMessageFull [" + pushMessage  + "]");
		pushInfo.setPushMessage(pushMessage);
		
		model.addAttribute("searchVO", searchVO);	 							// 검색항목
		model.addAttribute("pushInfo", pushInfo); // PUSH정보
		model.addAttribute("pushCampList", pushCampList); 						// 캠페인목록
		model.addAttribute("deptList", deptList); 								// 부서 목록
		model.addAttribute("userList", userList); 								// 사용자목록
		model.addAttribute("userInfo", userInfo); 								// 사용자정보
		model.addAttribute("mergeList", mergeList); 							// 수신자정보머지키코드
		model.addAttribute("statusList", statusList); 							// 상태
		model.addAttribute("workStatusList", workStatusList);					// 발송상태
		model.addAttribute("reasonList", reasonList); 							// 조회사유
		model.addAttribute("segList", segList); 								// 발송대상(세그먼트) 목록
		model.addAttribute("pushLegalCf", pushLegalCf); 						// 수신거부 설정 방법안내
		model.addAttribute("appLogo", properties.getProperty("PUSH.APP_LOGO"));	// Push 앱 로고 
		model.addAttribute("appName", properties.getProperty("PUSH.APP_NAME"));	// Push 앱 이름
		model.addAttribute("imgUploadPushPath", properties.getProperty("IMG.PUSH_UPLOAD_PATH"));// Push 첨부 이미지경로
		model.addAttribute("DEFAULT_DOMAIN", properties.getProperty("IMG.DEFAULT_DOMAIN"));		// 이미지미리보기위한 Domain URL		
		return "push/cam/pushUpdateP";
	}
	
	/**
	 * PUSH 발송 정보를 수정한다.
	 * @param pushVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/pushUpdate")
	public String goPushUpdate(@ModelAttribute PushVO pushVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPushUpdate pushmessageId = " + pushVO.getPushmessageId());
		logger.debug("goPushUpdate userId        = " + pushVO.getUserId());
		logger.debug("goPushUpdate deptNo        = " + pushVO.getDeptNo());
		logger.debug("goPushUpdate segNoc        = " + pushVO.getSegNoc());
		logger.debug("goPushUpdate campNo        = " + pushVO.getCampNo());
		logger.debug("goPushUpdate sendYmd       = " + pushVO.getSendYmd());
		logger.debug("goPushUpdate sendHour      = " + pushVO.getSendHour());
		logger.debug("goPushUpdate sendMin       = " + pushVO.getSendMin());
		logger.debug("goPushUpdate sendTyp       = " + pushVO.getSendTyp());
		logger.debug("goPushUpdate pushName      = " + pushVO.getPushName());
		logger.debug("goPushUpdate pushGubun     = " + pushVO.getPushGubun());
		logger.debug("goPushUpdate status        = " + pushVO.getStatus());
		logger.debug("goPushUpdate pushTitle     = " + pushVO.getPushTitle());
		logger.debug("goPushUpdate pushMessage   = " + pushVO.getPushMessage());
		logger.debug("goPushUpdate attachNm      = " + pushVO.getAttachNm());
		logger.debug("goPushUpdate attachPath    = " + pushVO.getAttachPath());
		logger.debug("goPushUpdate workStatus    = " + pushVO.getWorkStatus());
		logger.debug("goPushUpdate legalYn       = " + pushVO.getLegalYn());
		logger.debug("goPushUpdate legalCf       = " + pushVO.getLegalCf());
		logger.debug("goPushUpdate smsYn         = " + pushVO.getSmsYn());
		logger.debug("goPushUpdate callUrlTyp    = " + pushVO.getCallUrlTyp());
		logger.debug("goPushUpdate AndroidURL    = " + pushVO.getCallUri());
		logger.debug("goPushUpdate IOSURL        = " + pushVO.getCallUriIos());
		
		// 기본값 설정
		if (pushVO.getDeptNo() == 0) pushVO.setDeptNo((int) session.getAttribute("NEO_DEPT_NO")); 					// 부서번호
		if (StringUtil.isNull(pushVO.getUserId())) pushVO.setUserId((String) session.getAttribute("NEO_USER_ID"));	// 사용자아이디

		if (!StringUtil.isNull(pushVO.getSegNoc())) pushVO.setSegNo(Integer.parseInt(pushVO.getSegNoc().substring(0, pushVO.getSegNoc().indexOf("|"))));// 세그먼트번호(발송대상그룹)
		
		if (!StringUtil.isNull(pushVO.getSegNoc())) pushVO.setSegNo(Integer.parseInt(pushVO.getSegNoc().substring(0, pushVO.getSegNoc().indexOf("|"))));// 세그먼트번호(발송대상그룹)
		
		//1.즉시 여부에 따른 발송시간 입력
		if("000".equals(pushVO.getSendTyp())){
			pushVO.setSendDt(StringUtil.getDate(Code.TM_YMDHMS));
		} else {
			if(StringUtil.isNull(pushVO.getSendYmd())) pushVO.setSendYmd("0000-00-00");					// 예약시간(예약일)
			String sendHour = StringUtil.setTwoDigitsString(pushVO.getSendHour());						// 예약시간(시)
			String sendMin = StringUtil.setTwoDigitsString(pushVO.getSendMin());						// 예약시간(분)
			String sendSec = "00";																		// 예약시간(초)
			pushVO.setSendDt( pushVO.getSendYmd().replaceAll("\\.", "") + sendHour + sendMin + sendSec );// 예약일시
		}
		
		//1.첨부파일 등록
		if(pushVO.getAttachPath() != null && !"".equals(pushVO.getAttachPath())) {
			File attachFile = null;
			String basePath = properties.getProperty("IMG.PUSH_UPLOAD_PATH");
			
			long fileSize = 0;
			String[] fileNm = pushVO.getAttachNm().split(",");
			String[] filePath = pushVO.getAttachPath().split(",");
			
			//for(int i=0;i<filePath.length;i++) {
			for(int i=0;i< 1 ;i++) {
				attachFile = new File(basePath + "/" + filePath[i]);
				fileSize = attachFile.length();
				pushVO.setFileNm(fileNm[i]);
				pushVO.setFilePath(filePath[i]);
				pushVO.setFileSize(fileSize);
			}
		}
		
		// 2.수정자 정보 입력
		pushVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		pushVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		// 3. 광고 체크 했을때와 아니었을떄의 추가 문자 처리 
		String pushMessageHead = "";
		String pushMessageTail = "";
		String pushMessage = "";
		if (pushVO.getLegalYn() != null && "Y".equals(pushVO.getLegalYn())) {
			pushMessageHead = "(광고)";
			// 수신거부 번호
			pushMessageTail = pushVO.getLegalCf();
		}
		pushMessageHead = pushMessageHead + pushVO.getPushTitle();
		pushMessage = pushMessageHead + "\n" + pushVO.getPushMessage();
		if (!"".equals(pushMessageTail)) {
			pushMessage = pushMessageHead + "\n" + pushVO.getPushMessage() + "\n" + pushMessageTail;
		}

		pushVO.setPushMessage(pushMessage);
		
		logger.debug("goPushAdd pushMessage [" + pushMessage + "]");
		
		// 5. URL Type (String 으로 저장 Code 저장 000 => webLink 001 => webOpen 002 => webVideo)
		if(pushVO.getCallUrlTyp() != null) {
			pushVO.setCallUrlTyp(pushVO.getCallUrlTyp().trim());
		}
		
		// 6. Android URL (앞뒤공백 제거)
		if(pushVO.getCallUri() != null ) {
			pushVO.setCallUri(pushVO.getCallUri().trim());
		}
		
		// 7. IOS URL (앞뒤 공백 제거) 
		if(pushVO.getCallUriIos() != null ) {
			pushVO.setCallUriIos(pushVO.getCallUriIos().trim());
		}
		
		int result = 0; 
		try {
			result = pushCampaignService.updatePushInfo(pushVO);
		} catch (Exception e) {
			logger.error("pushCampaignService.updatePushInfo error = " + e);
		}
		
		// 결과값
		if (result > 0) {
			model.addAttribute("result", "Success");

		} else {
			model.addAttribute("result", "Fail");
		}
		return "push/cam/pushUpdate";
	}
	
	/**
	 * PUSH 상태를 업데이트 한다
	 * @param pushVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/pushDelete")
	public ModelAndView updatePushStatus(@ModelAttribute PushVO pushVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updatePushStatus pushmessageIds = " + pushVO.getPushmessageIds());
		logger.debug("updatePushStatus pushmessageId  = " + pushVO.getPushmessageId());
		logger.debug("updatePushStatus pushStatus     = " + pushVO.getStatus());
		
		int result = -1;
		String[] targetPush = null;
		List<PushVO> targetPushVOList  = new ArrayList<PushVO>(); 
		
		//조회화면에서 일괄적으로 삭제 하는 경우 		
		if (pushVO.getPushmessageIds() != null &&  !"".equals(pushVO.getPushmessageIds())) {
			targetPush = pushVO.getPushmessageIds().split(",");
			
			if (targetPush !=null && targetPush.length > 0 ) {
				try {
					for (int i = 0 ; i < targetPush.length; i ++) {
						
						if (!StringUtil.isNull(targetPush[i])) {
							int messageId = Integer.parseInt(targetPush[i]);
							PushVO targetPushVO = new PushVO();
							targetPushVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
							targetPushVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
							targetPushVO.setPushmessageId(messageId);
							targetPushVO.setStatus(pushVO.getStatus());
							targetPushVOList.add(targetPushVO);
						}
					}
				} catch(Exception e) {
					logger.error("pushCampaignService.updatePushStatus error = " + e);
				}  
			}
		} else {
			PushVO targetPushVO = new PushVO();
			targetPushVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
			targetPushVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
			targetPushVO.setPushmessageId(pushVO.getPushmessageId());
			targetPushVO.setStatus(pushVO.getStatus());
			targetPushVOList.add(targetPushVO);
		}
		
		try {
			if (targetPushVOList != null && targetPushVOList.size() > 0 ) {
				for(int i=0; i < targetPushVOList.size(); i++) { 
					result = pushCampaignService.updatePushStatus(targetPushVOList.get(i));
				} 
			}
		} catch(Exception e) {
			logger.error("pushCampaignService.updatePushStatus error = " + e);
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
	 * PUSH 상태를 업데이트 한다
	 * @param pushVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/pushAdmit")
	public ModelAndView updatePushAdmit(@ModelAttribute PushVO pushVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {		
		logger.debug("updatePushAdmit pushmessageIds = " + pushVO.getPushmessageIds());
		logger.debug("updatePushAdmit pushmessageId  = " + pushVO.getPushmessageId());
		logger.debug("updatePushAdmit workStatus     = " + pushVO.getWorkStatus());
		
		int result = -1;
		String[] targetPush = null;
		List<PushVO> targetPushVOList  = new ArrayList<PushVO>(); 
		
		//조회화면에서 일괄적으로 승인 하는 경우
		if (pushVO.getPushmessageIds() != null &&  !"".equals(pushVO.getPushmessageIds())) {
			targetPush = pushVO.getPushmessageIds().split(",");
			
			if (targetPush !=null && targetPush.length > 0 ) {
				try {
					for (int i = 0 ; i < targetPush.length; i ++) {
						
						if (!StringUtil.isNull(targetPush[i])) {
							int messageId = Integer.parseInt(targetPush[i]);
							PushVO targetPushVO = new PushVO();
							targetPushVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
							targetPushVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
							targetPushVO.setPushmessageId(messageId);
							targetPushVO.setWorkStatus("001");
							targetPushVOList.add(targetPushVO);
						}
					}
				} catch(Exception e) {
					logger.error("pushCampaignService.updatePushStatus error = " + e);
				}  
			}
		} else {
			PushVO targetPushVO = new PushVO();
			targetPushVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
			targetPushVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
			targetPushVO.setPushmessageId(pushVO.getPushmessageId());
			targetPushVO.setWorkStatus("001");
			targetPushVOList.add(targetPushVO);
		}
		
		try {
			if (targetPushVOList != null && targetPushVOList.size() > 0 ) {
				for(int i=0; i < targetPushVOList.size(); i++) { 
					result = pushCampaignService.updatePushStatusAdmit(targetPushVOList.get(i));
				} 
			}
		} catch(Exception e) {
			logger.error("pushCampaignService.updatePushStatus error = " + e);
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
	 * PUSH를 복사한다
	 * @param pushVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/pushCopy")
	public ModelAndView copyPushInfo(@ModelAttribute PushVO pushVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("copyPushInfo pushmessageIds = " + pushVO.getPushmessageIds());
		logger.debug("copyPushInfo pushmessageId  = " + pushVO.getPushmessageId());
		logger.debug("copyPushInfo status         = " + pushVO.getStatus());

		int result = -1;
		String[] targetPush = null;
		List<PushVO> targetPushVOList = new ArrayList<PushVO>();

		String serverUrl = request.getServletContext().getRealPath("/");

		if (pushVO.getPushmessageIds() != null && !"".equals(pushVO.getPushmessageIds())) {
			targetPush = pushVO.getPushmessageIds().split(",");
			if (targetPush != null && targetPush.length > 0) {
				try {
					for (int i = 0; i < targetPush.length; i++) {
						if (!StringUtil.isNull(targetPush[i])) {
							int messageId = Integer.parseInt(targetPush[i]);
							PushVO targetPushVO = new PushVO();
							targetPushVO.setPushmessageId(messageId);
							targetPushVOList.add(targetPushVO);
						}
					}
				} catch (Exception e) {
					logger.error("pushCampaignService.copyPushInfo error = " + e);
				}
			}
		} else {
			PushVO targetPushVO = new PushVO();
			targetPushVO.setPushmessageId(pushVO.getPushmessageId());
			targetPushVOList.add(targetPushVO);
		}

		try {
			if (targetPushVOList != null && targetPushVOList.size() > 0) {
				for (int i = 0; i < targetPushVOList.size(); i++) {
					result = pushCampaignService.copyPushInfo(targetPushVOList.get(i), properties, serverUrl);
				}
			}
		} catch (Exception e) {
			logger.error("pushCampaignService.copyPushInfo error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		if (result >= 0) {
			map.put("result", "Success");
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}

	/****************************************** PUSH 발송 내부 팝업 처리 ******************************************/
	/**
	 * PUSH 등록/수정 캠페인 목록(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popCampList")
	public String goPopCampList(@ModelAttribute PushCampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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
		List<PushCampaignVO> campList = null;
		try {
			campList = pushCampaignService.getCampaignList(searchVO);
		} catch(Exception e) {
			logger.error("pushCampaignService.getCampaignList error = " + e);
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
		
		return "push/cam/pop/popCampList";
	}
	
	/**
	 * PUSH 등록/수정 캠페인 등록(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popCampAdd")
	public String goPopCampAdd(@ModelAttribute PushCampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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
		
		return "push/cam/pop/popCampAdd";
	}
	
	/**
	 * PUSH 등록/수정 캠페인 수정(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popCampUpdate")
	public String goPopCampUpdate(@ModelAttribute PushCampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopCampUpdate campNo        = " + searchVO.getCampNo());
		
		// 캠페인 정보 조회
		PushCampaignVO campInfo = null;
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		try {
			campInfo = pushCampaignService.getCampaignInfo(searchVO);
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
		
		return "push/cam/pop/popCampUpdate";
	}
	
	/**
	 * Push 등록/수정 수신자그룹 목록(팝업)
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
		searchVO.setSearchPushuseYn("Y");
		
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
		
		return "push/cam/pop/popSegList";
	}
	
	/**
	 * PUSH 등록/수정 수신자그룹 등록:파일(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popSegAddFile")
	public String goPopSegAddFile(@ModelAttribute SegmentVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopSegAddFile ..........");
		logger.debug("goPopSegAddFile searchStartDt  = " + searchVO.getSearchStartDt());
		logger.debug("goPopSegAddFile searchEndDt    = " + searchVO.getSearchEndDt());
		logger.debug("goPopSegAddFile searchSegNm    = " + searchVO.getSearchSegNm());
		logger.debug("goPopSegAddFile searchCreateTy = " + searchVO.getSearchCreateTy());
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		
		return "push/cam/pop/popSegAddFile";
	}
	
	/**
	 * PUSH 등록/수정 수신자그룹 수정:파일(팝업)
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
		
		return "push/cam/pop/popSegUpdateFile";
	}
	
	/**
	 * PUSH 등록/수정 수신자그룹 등록:SQL(팝업)
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
		
		return "push/cam/pop/popSegAddSql";
	}
	
	/**
	 * SMS 등록/수정 수신자그룹 수정:SQL(팝업)
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
		
		return "push/cam/pop/popSegUpdateSql";
	}

	/**
	 * Url 미리보기 
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/urlPreview")
	public ModelAndView urlPreview(@ModelAttribute PushVO pushVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("urlPreview callUriTyp = " + pushVO.getCallUrlTyp());
		logger.debug("urlPreview callUri    = " + pushVO.getCallUri());
		logger.debug("urlPreview callUriIos = " + pushVO.getCallUriIos());
		
		String uri = "";
		String uriIos = "";
		try {
			uri = pushVO.getCallUri();
			
			logger.debug("urlPreview callUri[1] = " + uri);
			
			String paramUrl = "";
			if(uri.indexOf("?")>=0) {
				String tempParamUrl = uri.substring(uri.indexOf("?")+1);
				String[] paramStr = tempParamUrl.split("&");
				for(int i=0;i<paramStr.length;i++) {
					String param = paramStr[i];
					if(param.indexOf("=")>=0) {
						String key = param.split("=")[0];
						String val = param.split("=")[1];
						paramUrl += (i==0?"":"&") + key + "=" + java.net.URLEncoder.encode(val,"UTF-8");
					}
				}
				uri = uri.substring(0, uri.indexOf("?")) + "?" + paramUrl;
			}
			
			logger.debug("urlPreview callUri[2] = " + uri);
			
			uriIos = pushVO.getCallUriIos();
			logger.debug("urlPreview callUriIos[1] = " + uriIos);
			
			String paramUrlIos = "";
			if(uriIos.indexOf("?")>=0) {
				String tempParamUrl = uriIos.substring(uriIos.indexOf("?")+1);
				String[] paramStr = tempParamUrl.split("&");
				for(int i=0;i<paramStr.length;i++) {
					String param = paramStr[i];
					if(param.indexOf("=")>=0) {
						String key = param.split("=")[0];
						String val = param.split("=")[1];
						paramUrlIos += (i==0?"":"&") + key + "=" + java.net.URLEncoder.encode(val,"UTF-8");
					}
				}
				uriIos = uriIos.substring(0, uri.indexOf("?")) + "?" + paramUrlIos;
			}
			
			logger.debug("urlPreview callUriIos[2] = " + uriIos);
			
		} catch(Exception e) {
			logger.error("urlPreview error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(uri != null && !"".equals(uri)) {
			map.put("result", "Success");
			map.put("uri", uri);
			map.put("uriIos", uriIos);
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	/**
	 * API : pushList
	 * @param params
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return 
	 */
	@RequestMapping(value="/api/sendPushList")
	public void goApiSendPushList(@RequestBody Map<String, Object> params, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		logger.debug("===========sendPushList=====================");
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
		String senderphonenumber = "";
		int campaignnumber = 999999;
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
							int requestKeyCount =  pushCampaignService.getCountRequestKey(requestkey);
							if (requestKeyCount > 0 ) {
								sResultmessage = "[ 이미 사용된 키값임 (" + requestkey +")]";
								sResultcode = "E007";
							}
						} catch (Exception e) {
							logger.error("kakaoTemplateService.getCountRequestKey error = " + e);
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
		
		if(json.has("senderphonenumber")) {
			senderphonenumber = StringUtil.setNullToString(json.get("senderphonenumber").toString());
			if("".equals(senderphonenumber)) {
				sResultmessage += "[ 발신자번호 누락 (senderphonenumber)]";
				sResultcode = "E001";  
			}
		} else {
			sResultmessage += "[ 발신자번호 누락 (senderphonenumber)]";
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
		/*
		try {
			SmsTemplateVO tempMergeVO = smsTemplateService.getSmsTemplate(templatecode);
			if (tempMergeVO == null) {
				sResultmessage = "[ 템플릿정보 없음 (" + templatecode +")]";
				sResultcode = "E003";
			} else {
				if ("".equals(tempMergeVO.getTempContent())) {
					sResultmessage = "[ 템플릿정보 없음 (" + templatecode +")]";
					sResultcode = "E003";
				}
			}
			
		} catch (Exception e) {
			logger.error("codeService.getSmsTemplate error = " + e);
			sResultmessage = "[ 서버 데이터 템플릿 정보 조회 오류 (" + templatecode +")]";
			sResultcode = "E006";
		}
		*/
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
				logger.error("PushCampaignController.goApiPushList Send Return error = " + e);
			} 
		} else {
			//파일로 만든다 ..
			//디렉토리있는지 확인
			String basePath = properties.getProperty("FILE.UPLOAD_PATH") + "/api/neopush";
			String filePath = basePath + "/" + requestkey + "_" + pagenumber + "_"+ totalpagenumber + ".tmp";
			logger.debug("goApiPushList filePath = " + filePath);
			
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
				logger.error("goApiPushList file write error = " + e);
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
				logger.error("PushCampaignController.goApiPushList Send Return error = " + e);
			}
		}
	}
	
	/**
	 * API : campList
	 * @param params
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return 
	 */
	@RequestMapping(value="/api/campList")
	public void goApiCampList(@RequestBody Map<String, Object> params, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		String sReturnValue ="Success";
		String sErrorCode ="0000"; 
		String sErrorMessage="";
		
		JSONObject json = new JSONObject();
		String key = "";
		Object value = null;
		for(Map.Entry<String, Object> entry : params.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();
			json.put(key, value);
		}
		
		PushCampaignVO pushCampVO = new PushCampaignVO();
		
		int deptNo = 0;
		if (json.has("deptNo")) {
			deptNo = StringUtil.setNullToInt(json.get("deptNo").toString());
			pushCampVO.setSearchDeptNo(deptNo);
		} else {
			pushCampVO.setSearchDeptNo(deptNo);
		}
		
		if (json.has("startDt")) {
			if ("".equals(StringUtil.setNullToString(json.get("startDt").toString()))) {
				sReturnValue = "Fail";
				sErrorCode = "E001";
				sErrorMessage += "[검색시작일(startDt)] ";
			} else {
				pushCampVO.setSearchStartDt(StringUtil.setNullToString(json.get("startDt").toString()));
			}
		} else {
			sReturnValue = "Fail";
			sErrorCode = "E001";
			sErrorMessage += "[검색시작일(startDt)] ";
		}
		
		if (json.has("endDt")) {
			if ("".equals(StringUtil.setNullToString(json.get("endDt").toString()))) {
				sReturnValue = "Fail";
				sErrorCode = "E001";
				sErrorMessage += "[검색종료일(endDt)] ";
			} else {
				pushCampVO.setSearchEndDt(StringUtil.setNullToString(json.get("endDt").toString()));
			}
		} else {
			sReturnValue = "Fail";
			sErrorCode = "E001";
			sErrorMessage += "[검색종료일(endDt)] ";
		}
		
		if (json.has("status")) {
			pushCampVO.setSearchStatus(StringUtil.setNullToString(json.get("status").toString()));
		}
		
		if (json.has("campTy")) {
			pushCampVO.setSearchCampTy(StringUtil.setNullToString(json.get("campTy").toString()));
		}
		
		if (json.has("campNm")) {
			pushCampVO.setSearchCampNm(StringUtil.setNullToString(json.get("campNm").toString()));
		}
		
		int page = 0;
		int rows = 0; 
		
		if (json.has("page")) {
			page = StringUtil.setNullToInt(json.get("page").toString());
			if (page <= 0) {
				page = 1;
			} 
		} else {
			page =1;
		}
		
		if (json.has("rows")) {
			rows = StringUtil.setNullToInt(json.get("rows").toString());
			if (rows <= 0) {
				rows = 10;
			}
		} else {
			rows = 10;
		}
		
		pushCampVO.setPage(page);
		pushCampVO.setRows(rows);
		pushCampVO.setStartRow(( page-1) * rows);
		pushCampVO.setUilang("000");
		
		int resultCount = 0;
		int totalCount = 0;
		String resultData = "";
		
		if ("0000".equals(sErrorCode)) {
			
			List<PushCampaignVO> campaignList = null;
			try {
				campaignList = pushCampaignService.getCampaignList(pushCampVO);
				if(campaignList == null || campaignList.size() == 0) {
					sReturnValue ="Success";
					sErrorMessage= "조회된 내역이 없습니다";
					resultData = "";
					resultCount = 0; 
				} else {
					resultCount =  campaignList.size();
					String resultString = "";
					
					for (int m = 0; m < campaignList.size(); m++)
					{
						PushCampaignVO sObjectVO = (PushCampaignVO) campaignList.get(m);
						resultString += String.format("{ ", "");
						resultString += String.format("\"userId\":\"%s\",", StringUtil.setNullToString(sObjectVO.getUserId()));
						resultString += String.format("\"userNm\":\"%s\",", StringUtil.setNullToString(sObjectVO.getUserNm()));
						resultString += String.format("\"deptNo\":\"%s\",", sObjectVO.getDeptNo());
						resultString += String.format("\"deptNm\":\"%s\",", StringUtil.setNullToString(sObjectVO.getDeptNm()));
						resultString += String.format("\"campNo\":\"%s\",", sObjectVO.getCampNo());
						resultString += String.format("\"campNm\":\"%s\",", StringUtil.setNullToString(sObjectVO.getCampNm())); 
						resultString += String.format("\"status\":\"%s\",", StringUtil.setNullToString(sObjectVO.getStatus()));
						resultString += String.format("\"statusNm\":\"%s\",", StringUtil.setNullToString(sObjectVO.getStatusNm())); 
						resultString += String.format("\"campTy\":\"%s\",", StringUtil.setNullToString(sObjectVO.getCampTy()));
						resultString += String.format("\"campTyNm\":\"%s\",", StringUtil.setNullToString(sObjectVO.getCampTyNm()));
						resultString += String.format("\"campPushCnt\":\"%s\"", sObjectVO.getCampPushCnt());
						
						resultString += String.format("},", "");
						
						totalCount = sObjectVO.getTotalCount();
					}
					resultData = String.format("%s", (resultCount > 0 ? resultString.trim().substring(0, resultString.length() - 1) : ""));
				}
			} catch (Exception e) {
				logger.error("pushCampaignService.getCampaignList error = " + e);
				sReturnValue ="Fail";
				sErrorCode = "E006";
				sErrorMessage = e.getMessage();
			}
		}
		
		try {
			sendResultJson(response, sReturnValue, sErrorCode, sErrorMessage, resultCount, totalCount, resultData);
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	/**
	 * API : SendPush
	 * @param params
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @apiNote This Api, cannot use a merge item 
	 */
	@RequestMapping(value="/api/sendPush")
	public void goApiSendPush(@RequestBody Map<String, Object> params, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		String sReturnValue ="Success";
		String sErrorCode ="0000"; 
		String sErrorMessage="";
		
		JSONObject json = new JSONObject();
		String key = "";
		Object value = null;
		for(Map.Entry<String, Object> entry : params.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();
			json.put(key, value);
		}
		
		PushVO pushVO = new PushVO();
		int deptNo = 0;
		if (json.has("deptNo")) {
			deptNo = StringUtil.setNullToInt(json.get("deptNo").toString());
			if(deptNo == 0 ) {
				sReturnValue = "Fail";
				sErrorCode = "E001";
				sErrorMessage += "[소속정보(deptNo)] ";
			} else {
				pushVO.setDeptNo(deptNo);
			}
		} else {
			sReturnValue = "Fail";
			sErrorCode = "E001";
			sErrorMessage += "[소속정보(deptNo)] ";
		}
		
		String userId= "";
		if (json.has("userId")) {
			if ("".equals(StringUtil.setNullToString(json.get("userId").toString()))) {
				sReturnValue = "Fail";
				sErrorCode = "E001";
				sErrorMessage += "[사용자정보(userId)] ";
			} else {
				userId = StringUtil.setNullToString(json.get("userId").toString());
				pushVO.setUserId(userId);
			}
		} else {
			sReturnValue = "Fail";
			sErrorCode = "E001";
			sErrorMessage += "[사용자정보(userId)] ";
		}
		
		if (json.has("segNo")) {
			pushVO.setSegNo(StringUtil.setNullToInt(json.get("segNo").toString()));
		} else {
			sReturnValue = "Fail";
			sErrorCode = "E001";
			sErrorMessage += "[수신자그룹(segNo)] ";
		}
		
		if (json.has("campNo")) {
			pushVO.setCampNo(StringUtil.setNullToInt(json.get("campNo").toString()));
		}
		
		if (json.has("pushName")) {
			pushVO.setPushName(StringUtil.setNullToString(json.get("pushName").toString()));
		}
		
		if (json.has("pushMessage")) {
			pushVO.setPushMessage(StringUtil.setNullToString(json.get("pushMessage").toString()));
		}
		
		if (json.has("pushGubun")) {
			pushVO.setPushGubun(StringUtil.setNullToString(json.get("pushGubun").toString()));
		}
		
		if (json.has("callUrlTyp") && !"".equals(StringUtil.setNullToString(json.get("callUrlTyp").toString()))) {
			pushVO.setCallUrlTyp(StringUtil.setNullToString(json.get("callUrlTyp").toString()));
		}
		
		if (json.has("callUri") && !"".equals(StringUtil.setNullToString(json.get("callUri").toString()))) {
			pushVO.setCallUri(StringUtil.setNullToString(json.get("callUri").toString()));
		}
		
		if (json.has("callUriIos") && !"".equals(StringUtil.setNullToString(json.get("callUriIos").toString()))) {
			pushVO.setCallUriIos(StringUtil.setNullToString(json.get("callUriIos").toString()));
		}
		
		if (json.has("pushTitle")) {
			pushVO.setPushTitle(StringUtil.setNullToString(json.get("pushTitle").toString()));
		}
		
		if (json.has("smsYn")) {
			if("".equals(StringUtil.setNullToString(json.get("smsYn").toString()))){
				pushVO.setSmsYn("N");
			} else {
				pushVO.setSmsYn(StringUtil.setNullToString(json.get("smsYn").toString()));
			}
		}
		
		String pushMessageHead = "";
		String pushMessageTail = "";
		String pushMessage = "";
		String legalYn = ""; 
		
		if (json.has("legalYn")) {
			legalYn = StringUtil.setNullToString(json.get("legalYn").toString());
			
			if (!"".equals(legalYn) && "Y".equals(legalYn)) {
				pushMessageHead = "(광고)";
				if (json.has("legalCf")) {
					pushMessageTail = StringUtil.setNullToString(json.get("legalCf").toString());
				}
				
			}
			pushMessageHead = pushMessageHead + pushVO.getPushTitle();
			pushMessage = pushMessageHead + "\n" + pushVO.getPushMessage();
			if (!"".equals(pushMessageTail)) {
				pushMessage = pushMessageHead + "\n" + pushVO.getPushMessage() + "\n" + pushMessageTail;
			}
			pushVO.setPushMessage(pushMessage);
		}
		
		if (json.has("sendTyp")) {
			if(StringUtil.isNull(json.get("sendTyp").toString())){
				pushVO.setSendTyp("000");
			} else {
				pushVO.setSendTyp(json.get("sendTyp").toString());
			}
		}
		
		if ("000".equals(pushVO.getSendTyp())) {
			pushVO.setSendDt(StringUtil.getDate(Code.TM_YMDHMS));
		} else {
			String sendDate = "";
			if (json.has("sendDt")) {
				sendDate = json.get("sendDt").toString();
				if(StringUtil.isNull(sendDate)) {
					sReturnValue = "Fail";
					sErrorCode = "E001";
					sErrorMessage += "[발송일시(sendDt)] ";
				} else {
					pushVO.setSendDt(sendDate);	// 예약일시
				}
			} else {
				sReturnValue = "Fail";
				sErrorCode = "E001";
				sErrorMessage += "[발송일시(sendDt)] ";
			}
		}
		
		pushVO.setWorkStatus("001");	//PUSH발송상태 기본값 발송승인
		pushVO.setStatus("000");		//PUSH상태 기본값 정상
		pushVO.setRegId(userId);
		pushVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		pushVO.setPushmessageId(0);
		
		if ("0000".equals(sErrorCode)) {
			int result = 0; 
			try {
				result = pushCampaignService.insertPushInfo(pushVO);
			} catch (Exception e) {
				logger.error("pushCampaignService.insertPushInfo error = " + e);
				sErrorMessage = e.getMessage();
			}
			
			logger.debug("insertPushInfo new pushmessageId [" + pushVO.getPushmessageId() + "]");
			// 결과값
			if (result > 0) {
				sReturnValue ="Success";
				sErrorCode = "0000";
				sErrorMessage= "";
			} else {
				sReturnValue ="Fail";
				sErrorCode = "E009";
			}
		}
		
		try {
			sendResultJson(response, sReturnValue, sErrorCode, sErrorMessage, 0, 0, "");
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	public static void sendResultJson(HttpServletResponse response, String sJsonString, String sErrorCode, String sErrorMessage, int nResultCount, int nTotalCount, String sResultData) throws Exception {
		
		PrintWriter writer;
		String returnValue = "";
		
		if (nTotalCount > 0 ) {
			returnValue = "{ \"resultCode\":\"%s\", \"resultMessage\":\"%s\", \"resultValue\":\"%s\", \"resultCount\":\"%s\", \"totalCount\":\"%s\", \"resultData\":[%s]}"; 
		} else {
			returnValue = "{ \"resultCode\":\"%s\", \"resultMessage\":\"%s\", \"resultValue\":\"%s\"}";
		}
		
		if (sErrorCode == null){
			sErrorCode = "9999";
		}
		
		response.setContentType("text/plain; charset=UTF-8");
		writer = response.getWriter();
		
		if (nTotalCount > 0 ) {
			writer.write(String.format(returnValue, sErrorCode, sErrorMessage, sJsonString, nResultCount, nTotalCount, sResultData));
		} else {
			writer.write(String.format(returnValue, sErrorCode, sErrorMessage, sJsonString));
		}
		
		
		writer.flush();
		writer.close();
		
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
