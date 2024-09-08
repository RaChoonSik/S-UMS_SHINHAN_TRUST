/**
 * 작성자 : 김준희
 * 작성일시 : 2021.10.13
 * 설명 : 캠페인관리 Controller
 */
package kr.co.sict.ums.sms.cam.controller;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONArray;
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
import kr.co.sict.ums.sys.seg.service.KakaoTemplateService;
import kr.co.sict.ums.ems.seg.service.SegmentService;
import kr.co.sict.ums.sys.seg.service.SmsTemplateService;
import kr.co.sict.ums.sms.cam.service.SmsCampaignService;
import kr.co.sict.ums.sms.cam.vo.SmsAttachVO;
import kr.co.sict.ums.sms.cam.vo.SmsCampaignVO;
import kr.co.sict.ums.sms.cam.vo.SmsPhoneVO;
import kr.co.sict.ums.sms.cam.vo.SmsStgVO;
import kr.co.sict.ums.sms.cam.vo.SmsVO;
import kr.co.sict.ums.sys.acc.vo.UserVO;
import kr.co.sict.ums.sys.cod.vo.SenderKeyVO;
import kr.co.sict.ums.sys.cod.service.UserCodeService;
import kr.co.sict.ums.sys.dbc.service.DBConnService;
import kr.co.sict.ums.sys.dbc.vo.DbConnVO;
import kr.co.sict.ums.sys.seg.vo.KakaoTemplateVO;
import kr.co.sict.ums.ems.seg.vo.SegmentVO;
import kr.co.sict.ums.sys.seg.vo.SmsTemplateVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.PageUtil;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Controller
@RequestMapping("/sms/cam")
public class SmsCampaignController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private CodeService codeService;
		
	@Autowired
	private SmsCampaignService smsCampaignService;
	
	@Autowired
	private SegmentService segmentService;
	
	@Autowired
	private KakaoTemplateService kakaoTemplateService;
	
	@Autowired
	private SmsTemplateService smsTemplateService;

	@Autowired
	private AccountService systemService;
	

	@Autowired
	private UserCodeService userCodeService;
	
	@Autowired
	private CryptoService cryptoService;
	
	@Autowired
	private PropertiesUtil properties;

	
	/****************************************** 믄자 발송 캠페인 관리 ******************************************/
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
	public String goCampListP(@ModelAttribute SmsCampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goCampListP searchCampNm  = " + searchVO.getSearchCampNm());
		logger.debug("goCampListP searchCampTy  = " + searchVO.getSearchCampTy());
		logger.debug("goCampListP searchStatus  = " + searchVO.getSearchStatus());
		logger.debug("goCampListP searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goCampListP searchEndDt   = " + searchVO.getSearchEndDt());
		logger.debug("goCampListP searchDeptNo  = " + searchVO.getSearchDeptNo());
		logger.debug("goCampListP searchUserId  = " + searchVO.getSearchUserId());
		
		// 검색 기본값 설정
		if(searchVO.getSearchStartDt() == null || "".equals(searchVO.getSearchStartDt())) {
			searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-3, "M", "yyyyMMdd"));
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
		
		return "sms/cam/campListP";
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
	public String goCampList(@ModelAttribute SmsCampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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
		List<SmsCampaignVO> campaignList  = null;
		try {
			campaignList = smsCampaignService.getCampaignList(searchVO);
		} catch(Exception e) {
			logger.error("smsCampaignService.getCampaignSmsList error = " + e);
		}
		
		if(campaignList != null && campaignList.size() > 0) {
			totalCount = campaignList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);

		model.addAttribute("searchVO", searchVO);			// 검색 항목
		model.addAttribute("campaignList", campaignList);	// 캠페인 목록
		model.addAttribute("pageUtil", pageUtil);			// 페이징
		
		return "sms/cam/campList";
	}
	
	/**
	 * 캠페인 정보 조회
	 * @param SmsCampaignVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campInfo")
	public ModelAndView getCampInfo(@ModelAttribute SmsCampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getCampInfo campNo      = " + searchVO.getCampNo());
		
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		SmsCampaignVO smsCampaignVO = null;
		try {
			smsCampaignVO = smsCampaignService.getCampaignInfo(searchVO);
		} catch(Exception e) {
			logger.error("smsCampaignService.getCampaignInfo error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("campaign", smsCampaignVO);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 캠페인 정보 등록 화면을 출력한다.
	 * @param SmsCampaignVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campAddP")
	public String goCampAddP(@ModelAttribute SmsCampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("campTyList", campTyList);		// 캠페인목적 목록
		model.addAttribute("statusList", statusList);		// 캠페인상태 목록
		model.addAttribute("deptList", deptList);			// 부서 목록
		model.addAttribute("userList", userList);			// 사용자 목록
		
		return "sms/cam/campAddP";
	}
	
	/**
	 * 캠페인 정보 등록
	 * @param SmsCampaignVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campAdd")
	public ModelAndView insertCampInfo(@ModelAttribute SmsCampaignVO SmsCampaignVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("insertCampInfo campNm      = " + SmsCampaignVO.getCampNm());
		logger.debug("insertCampInfo campDesc      = " + SmsCampaignVO.getCampDesc());
		logger.debug("insertCampInfo campTy      = " + SmsCampaignVO.getCampTy());
		logger.debug("insertCampInfo status      = " + SmsCampaignVO.getStatus());
		logger.debug("insertCampInfo deptNo      = " + SmsCampaignVO.getDeptNo());
		logger.debug("insertCampInfo userId      = " + SmsCampaignVO.getUserId());
		SmsCampaignVO.setRegId((String)session.getAttribute("NEO_USER_ID"));
		SmsCampaignVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		int result = 0;
		try {
			result = smsCampaignService.insertCampaignInfo(SmsCampaignVO);
		} catch(Exception e) {
			logger.error("smsCampaignService.insertCampaignInfo error = " + e);
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
	 * @param SmsCampaignVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campUpdateP")
	public String goCampUpdateP(@ModelAttribute SmsCampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goCampUpdateP campNo = " + searchVO.getCampNo());
		
		// 캠페인 정보 조회
		SmsCampaignVO campInfo = null;
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		try {
			campInfo = smsCampaignService.getCampaignInfo(searchVO);
		} catch(Exception e) {
			logger.error("smsCampaignService.getCampaignInfo error = " + e);
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
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("campInfo", campInfo);			// 캠페인 정보
		model.addAttribute("campTyList", campTyList);		// 캠페인목적 목록
		model.addAttribute("statusList", statusList);		// 캠페인상태 목록
		model.addAttribute("deptList", deptList);			// 부서 목록
		model.addAttribute("userList", userList);			// 사용자 목록
		
		return "sms/cam/campUpdateP";
	}
	
	/**
	 * 캠페인 정보 수정
	 * @param SmsCampaignVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campUpdate")
	public ModelAndView updateCampInfo(@ModelAttribute SmsCampaignVO SmsCampaignVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateCampInfo campNo      = " + SmsCampaignVO.getCampNo());
		logger.debug("updateCampInfo campNm      = " + SmsCampaignVO.getCampNm());
		logger.debug("updateCampInfo campDesc    = " + SmsCampaignVO.getCampDesc());
		logger.debug("updateCampInfo campTy      = " + SmsCampaignVO.getCampTy());
		logger.debug("updateCampInfo status      = " + SmsCampaignVO.getStatus());
		logger.debug("updateCampInfo deptNo      = " + SmsCampaignVO.getDeptNo());
		logger.debug("updateCampInfo userId      = " + SmsCampaignVO.getUserId());
		
		SmsCampaignVO.setUpId((String)session.getAttribute("NEO_USER_ID"));
		SmsCampaignVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		int result = 0;
		try {
			result = smsCampaignService.updateCampaignInfo(SmsCampaignVO);
		} catch(Exception e) {
			logger.error("smsCampaignService.updateCampaignInfo error = " + e);
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
	
	/****************************************** 믄자 발송 관리 ******************************************/
	/**
	 * 믄자발송 등록현황 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/smsListP")
	public String goSmsListP(@ModelAttribute SmsVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSmsListP searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goSmsListP searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("goSmsListP searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goSmsListP searchTaskNm     = " + searchVO.getSearchTaskNm());
		logger.debug("goSmsListP searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goSmsListP searchStatus     = " + searchVO.getSearchStatus());
		logger.debug("goSmsListP searchSmsStatus  = " + searchVO.getSearchSmsStatus());
		logger.debug("goSmsListP searchGubun      = " + searchVO.getSearchGubun());
	 
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
		
		// 전송유형 목록
		CodeVO gubun = new CodeVO();
		gubun.setUilang((String)session.getAttribute("NEO_UILANG"));
		gubun.setCdGrp("C119");
		gubun.setCd("000");
		gubun.setUseYn("Y");
		List<CodeVO> gubunList = null;
		try {
			gubunList = codeService.getCodeListByUpCd(gubun);
		} catch(Exception e) {
			logger.error("codeService.getCodeListByUpCd[C119] error = " + e);
		}
		
		// 캠페인 목록
		SmsCampaignVO smsCampVO = new SmsCampaignVO();
		smsCampVO.setStatus("000");
		smsCampVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		smsCampVO.setDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<SmsCampaignVO> smsCampList = null;
		try {
			smsCampList = codeService.getCampaignSmsList(smsCampVO);
		} catch(Exception e) {
			logger.error("codeService.getCampaignSmsList error = " + e);
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
		
		// 발송상태코드 목록
		CodeVO statusVO = new CodeVO();
		statusVO.setUilang("000");
		statusVO.setCdGrp("C118");
		statusVO.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(statusVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList error = " + e);
		}
		
		// 문자상태코드 목록
		CodeVO smsStatus = new CodeVO();
		smsStatus.setUilang((String)session.getAttribute("NEO_UILANG"));
		smsStatus.setCdGrp("C116");
		smsStatus.setUseYn("Y");
		List<CodeVO> smsStatusList = null;
		try {
			smsStatusList = codeService.getCodeList(smsStatus);
		} catch(Exception e) {
			logger.error("codeService.getCodeList error = " + e);
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
		model.addAttribute("gubunList", gubunList);				// 문자전송유형
		model.addAttribute("smsCampList", smsCampList);			// 캠페인목록
		model.addAttribute("deptList", deptList);				// 사용자그룹목록
		model.addAttribute("userList", userList);				// 사용자목록
		model.addAttribute("statusList", statusList);			// 문자발송상태목록
		model.addAttribute("smsStatusList",smsStatusList);		// 문자상태목록 
		model.addAttribute("reasonList", reasonList);			// 조회사유
		
		return "sms/cam/smsListP";
	}
	
	/**
	 * SMS발송 등록현황 목록을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/smsList")
	public String goSmsList(@ModelAttribute SmsVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSmsList searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goSmsList searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("goSmsList searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goSmsList searchTaskNm     = " + searchVO.getSearchTaskNm());
		logger.debug("goSmsList searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goSmsList searchStatus     = " + searchVO.getSearchStatus());
		logger.debug("goSmsList searchSmsStatus  = " + searchVO.getSearchSmsStatus());
		logger.debug("goSmsList searchGubun      = " + searchVO.getSearchGubun());
		
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

		// 문자 목록 조회
		List<SmsVO> smsList = null;
		try {
			smsList = smsCampaignService.getSmsList(searchVO);
		} catch(Exception e) {
			logger.error("smsCampaignService.getSmsList error = " + e);
		}
		
		if(smsList != null && smsList.size() > 0) {
			totalCount = smsList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		
		model.addAttribute("searchVO", searchVO);	// 검색 항목
		model.addAttribute("smsList", smsList);	// 문자발송 목록
		model.addAttribute("pageUtil", pageUtil);	// 페이징
		
		return "sms/cam/smsList";
	}
	/**
	 * 문자발송 등록 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/smsAddP")
	public String goSmsAddP(@ModelAttribute SmsVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSmsAddP searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goSmsAddP searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("goSmsAddP searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goSmsAddP searchTaskNm     = " + searchVO.getSearchTaskNm());
		logger.debug("goSmsAddP searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goSmsAddP searchStatus     = " + searchVO.getSearchStatus());
		logger.debug("goSmsAddP searchSmsStatus  = " + searchVO.getSearchSmsStatus());
		logger.debug("goSmsAddP searchGubun      = " + searchVO.getSearchGubun());
		
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
		
		// 전송유형 목록
		CodeVO gubun = new CodeVO();
		gubun.setUilang((String)session.getAttribute("NEO_UILANG"));
		gubun.setCdGrp("C119");
		gubun.setCd("000");
		gubun.setUseYn("Y");
		List<CodeVO> gubunList = null;
		try {
			gubunList = codeService.getCodeListByUpCd(gubun);
		} catch(Exception e) {
			logger.error("codeService.getCodeListByUpCd[C119] error = " + e);
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
		
		// 발송상태코드 목록
		CodeVO statusVO = new CodeVO();
		statusVO.setUilang("000");
		statusVO.setCdGrp("C118");
		statusVO.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(statusVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList error = " + e);
		}
		
		// 문자상태코드 목록
		CodeVO smsStatus = new CodeVO();
		smsStatus.setUilang((String)session.getAttribute("NEO_UILANG"));
		smsStatus.setCdGrp("C116");
		smsStatus.setUseYn("Y");
		List<CodeVO> smsStatusList = null;
		try {
			smsStatusList = codeService.getCodeList(smsStatus);
		} catch(Exception e) {
			logger.error("codeService.getCodeList error = " + e);
		}
		
		// 문자무료수신거부 목록
		CodeVO legal = new CodeVO();
		legal.setUilang((String)session.getAttribute("NEO_UILANG"));
		legal.setCdGrp("C117");
		legal.setCd("000");
		legal.setUseYn("Y");
		
		String smsLegalCf ="";
		try {
			CodeVO smsLegal = codeService.getCodeInfo(legal);
			if(smsLegal != null && !"".equals(smsLegal.getCdNm())) {
				smsLegalCf = smsLegal.getCdNm();
			} else {//설정->수신설정
				smsLegalCf = "080-000-0000";
			}
		} catch (Exception e) {
			logger.error("codeService.getCodeInfo[C117] error = " + e);
		}
		
		// 캠페인 목록 조회
		SmsCampaignVO smsCamp = new SmsCampaignVO();
		smsCamp.setUilang((String)session.getAttribute("NEO_UILANG"));
		smsCamp.setSearchStatus("000");
		smsCamp.setPage(1);
		smsCamp.setRows(10000);
		smsCamp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		
		List<SmsCampaignVO> smsCampList = null;
		try {
			smsCampList = smsCampaignService.getCampaignList(smsCamp);
		} catch(Exception e) {
			logger.error("smsCampaignService.getCampaignList error = " + e);
		}
		
		// 수신자그룹(발송대상) 목록
		SegmentVO seg = new SegmentVO();
		seg.setUilang((String)session.getAttribute("NEO_UILANG"));
		seg.setSearchStatus("000");
		seg.setPage(1);
		seg.setRows(10000);
		seg.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		seg.setSmsuseYn("Y");
		
		List<SegmentVO> segList = null;
		try {
			segList = segmentService.getSegmentList(seg);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentList error = " + e);
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
		
		// 사용자정보
		UserVO userInfo = new UserVO();
		userInfo.setUilang((String)session.getAttribute("NEO_UILANG"));
		userInfo.setUserId((String)session.getAttribute("NEO_USER_ID"));
		try {
			userInfo = systemService.getUserInfo(userInfo);
		} catch(Exception e) {
			logger.error("systemService.getUserInfo error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("gubunList", gubunList);			// 문자전송유형
		model.addAttribute("smsCampList", smsCampList);		// 캠페인목록
		model.addAttribute("deptList", deptList);			// 부서 목록
		model.addAttribute("userList", userList);			// 사용자목록
		model.addAttribute("userInfo", userInfo);			// 사용자정보
		model.addAttribute("mergeList", mergeList);			// 수신자정보머지키코드
		model.addAttribute("statusList", statusList);		// 발송상태
		model.addAttribute("smsStatusList", smsStatusList);	// 문자상태
		model.addAttribute("reasonList", reasonList);		// 조회사유
		model.addAttribute("segList", segList);				// 발송대상(세그먼트) 목록
		model.addAttribute("smsLegalCf", smsLegalCf);		// 무료수신거부 전화번호
		model.addAttribute("imgUploadPath", properties.getProperty("IMG.SMS_UPLOAD_PATH"));	// 무료수신거부 전화번호

		return "sms/cam/smsAddP";
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
	@RequestMapping(value="/smsAdd")
	public String goSmsAdd(@ModelAttribute SmsVO smsVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailAdd userId        = " + smsVO.getUserId());
		logger.debug("goMailAdd deptNo        = " + smsVO.getDeptNo());
		logger.debug("goMailAdd segNoc        = " + smsVO.getSegNoc());
		logger.debug("goMailAdd sendTelNo     = " + smsVO.getSendTelno());
		logger.debug("goMailAdd campNo        = " + smsVO.getCampNo());
		
		logger.debug("goMailAdd sendYmd       = " + smsVO.getSendYmd());
		logger.debug("goMailAdd sendHour      = " + smsVO.getSendHour());
		logger.debug("goMailAdd sendMin       = " + smsVO.getSendMin());
		logger.debug("goMailAdd smsName       = " + smsVO.getSmsName());
		logger.debug("goMailAdd gubun         = " + smsVO.getGubun());
		logger.debug("goMailAdd taskNm        = " + smsVO.getTaskNm());
		logger.debug("goMailAdd smsStatus     = " + smsVO.getSmsStatus());
		logger.debug("goMailAdd status        = " + smsVO.getStatus());
		logger.debug("goMailAdd sendNm        = " + smsVO.getSendNm());
		logger.debug("goMailAdd sendTyp       = " + smsVO.getSendTyp());
		logger.debug("goMailAdd smsPhones     = " + smsVO.getSmsPhones());	
		logger.debug("goMailAdd legalYn       = " + smsVO.getLegalYn());
		logger.debug("goMailAdd smsMessage    = " + smsVO.getSmsMessage());
		logger.debug("goMailAdd attachNm      = " + smsVO.getAttachNm());
		logger.debug("goMailAdd attachPath    = " + smsVO.getAttachPath());
		
		List<SmsAttachVO> smsAttachList = new ArrayList<SmsAttachVO>();		// 첨부파일 목록
		
		// 파일 사이즈 체크
		if(smsVO.getAttachPath() != null && !"".equals(smsVO.getAttachPath())) {
			File attachFile = null;
			String basePath = properties.getProperty("IMG.SMS_UPLOAD_PATH");
			
			long fileSize = 0;
			String[] fileNm = smsVO.getAttachNm().split(",");
			String[] filePath = smsVO.getAttachPath().split(",");
			
			for(int i=0;i<filePath.length;i++) {
				attachFile = new File(basePath + "/" + filePath[i]);
				fileSize += attachFile.length();
				
				SmsAttachVO smsAttach = new SmsAttachVO();
				smsAttach.setAttNm(fileNm[i]);
				smsAttach.setAttFlPath(filePath[i]);
				smsAttach.setAttFlSize(attachFile.length());
				smsAttach.setAttPriviewPath("");
				
				smsAttachList.add(smsAttach);
			}
			
			// 합계 파일 사이즈가 10MB 이상인 경우 중지 
			if(fileSize > 600) {	// 600KB
				model.addAttribute("result","Fail");
				model.addAttribute("FILE_SIZE","EXCESS");
				return "sms/cam/smsAdd";
			}
		}
		
		// 기본값 설정 
		if(smsVO.getDeptNo() == 0) smsVO.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));						// 부서번호
		if(StringUtil.isNull(smsVO.getUserId())) smsVO.setUserId((String)session.getAttribute("NEO_USER_ID"));		// 사용자아이디	
		
		smsVO.setSmsStatus("000"); //문자상태 기본값  정상 
		smsVO.setStatus("000");//문자발송상태 기본값 발송대기 //문자발송상태 기본값 발송대기 
		if(!StringUtil.isNull(smsVO.getSegNoc())) smsVO.setSegNo(Integer.parseInt( smsVO.getSegNoc().substring(0, smsVO.getSegNoc().indexOf("|")) ));// 세그먼트번호(발송대상그룹)
		
		//1.즉시 여부에 따른 발송시간 입력
		if("000".equals(smsVO.getSendTyp())){
			smsVO.setSendDate(StringUtil.getDate(Code.TM_YMDHMS));
			smsVO.setSendYm(StringUtil.getDate(Code.TM_YM));
		} else {
			if(StringUtil.isNull(smsVO.getSendYmd())) smsVO.setSendYmd("0000-00-00");									// 예약시간(예약일)
			String sendHour = StringUtil.setTwoDigitsString(smsVO.getSendHour());										// 예약시간(시)
			String sendMin = StringUtil.setTwoDigitsString(smsVO.getSendMin());											// 예약시간(분)
			String sendSec = "00";																						// 예약시간(초)
			smsVO.setSendDate( smsVO.getSendYmd().replaceAll("\\.", "") + sendHour + sendMin + sendSec );				// 예약일시
			smsVO.setSendYm(smsVO.getSendYmd().replaceAll("\\.", "").substring(0,6));
		}
		
		//2.등록자 정보 입력 
		smsVO.setRegId((String) session.getAttribute("NEO_USER_ID"));
		smsVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		//3.메시지아이디 만들기
		smsVO.setMsgid(smsVO.getCampNo()+"||");
		//4.Keygen 만들기 
		smsVO.setKeygen(StringUtil.getDate(Code.TM_YMDHMSM));
		//5.컬럼 암호화 설정
		smsVO.setSendTelno( cryptoService.getEncrypt("SEND_TELNO",smsVO.getSendTelno()) );
		
		//6.SMS Message 만들기
		String smsMessageHead = "";
		String smsMessageTail = "";
		String smsMessage ="";
		if(smsVO.getLegalYn()!= null && "Y".equals(smsVO.getLegalYn())) {
			smsMessageHead ="(광고)";
			smsMessageTail = "무료수신거부 " + smsVO.getLegalCf();
		}
		smsMessageHead = smsMessageHead + smsVO.getSmsName();
		smsMessage = smsMessageHead + "\n" + smsVO.getSmsMessage();
		if (!"".equals(smsMessageTail)) {
			smsMessage = smsMessageHead + "\n" + smsVO.getSmsMessage() + "\n" + smsMessageTail;
		}
		
		smsVO.setSmsMessage(smsMessage);
		logger.debug("smsMessage [" + smsMessage + "]");
		
		int result = 0;
	 
		try {
			result = smsCampaignService.insertSmsInfo(smsVO, smsAttachList);
		} catch(Exception e) {
			logger.error("smsCampaignService.insertSmsInfo error = " + e);
		}

		//추가 번호 있을 경우 입력
		if(result > 0) {
			try {
				if(smsAttachList != null && smsAttachList.size() > 0) {
					for(SmsAttachVO smsAttach:smsAttachList) {
						smsAttach.setMsgid(smsVO.getMsgid());
						smsAttach.setKeygen(smsVO.getKeygen());
						result += smsCampaignService.insertSmsAttachInfo(smsAttach);
					}
				}
			} catch(Exception e) {
				logger.error("smsCampaignService.insertSmsAttachInfo error = " + e);
			}
		}

		
		if(result > 0) {
			try {
				if (smsVO.getSmsPhones().length() > 1) {
					String[] arrSmsPhone = smsVO.getSmsPhones().split(",");
					for (int i = 0; i < arrSmsPhone.length; i++) {
						SmsPhoneVO smsPhoneVO = new SmsPhoneVO();
						smsPhoneVO.setKeygen(smsVO.getKeygen());
						smsPhoneVO.setMsgid(smsVO.getMsgid());
						arrSmsPhone[i] = arrSmsPhone[i].replace("-", "");
						smsPhoneVO.setPhone(cryptoService.getEncrypt("PHONE",arrSmsPhone[i]) ); 
						result = smsCampaignService.insertSmsPhone(smsPhoneVO);
					}
				}
				
			} catch(Exception e) {
				logger.error("smsCampaignService.insertSmsPhone error = " + e);
			}
		}
		
		// 결과값		
		if (result > 0) {
			model.addAttribute("result","Success");
			model.addAttribute("msgid", smsVO.getMsgid());
			model.addAttribute("keygen", smsVO.getKeygen());
			model.addAttribute("campNo", smsVO.getCampNo());
		} else {
			model.addAttribute("result","Fail");
		}
		
		return "sms/cam/smsAdd";
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
	@RequestMapping(value="/smsAddApi")
	public String goSmsAddApi(@ModelAttribute SmsVO smsVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailAdd sendTelNo     = " + smsVO.getSendTelno());
		logger.debug("goMailAdd sendNm        = " + smsVO.getSendNm());
		logger.debug("goMailAdd taskNm        = " + smsVO.getTaskNm());		
		logger.debug("goMailAdd smsName       = " + smsVO.getSmsName());
		logger.debug("goMailAdd smsMessage    = " + smsVO.getSmsMessage());
		logger.debug("goMailAdd legalYn       = " + smsVO.getLegalYn());
		logger.debug("goMailAdd smsPhones     = " + smsVO.getSmsPhones());	
		
		//API기 때문에 USER_ID, DEPT_NO, CAMPUS_NO는 임의로 설정해줘야함  (나중에 바꿀생각하고 변경하자 )
		//기본값 설정 
		smsVO.setUserId("choonsik");
		smsVO.setDeptNo(3);
		smsVO.setCampNo(3);
		smsVO.setSmsStatus("000"); //문자상태 정상으로 설정 
		smsVO.setStatus("000"); //문자 발송상태 는 발송대기로 설정 
		smsVO.setGubun("001"); //문자 발송은 예약으로 함 (즉시로 하고 싶으면 일자를 그렇게 넣어주면됨 )
		//정보 조합해서 처리해야할것 
		//sendTyp 은 문자열 조합해서 사이즈 확인해서 SMS 또는 LMS를 넣으면 됨 
		smsVO.setSendTyp("001"); //문자 발송은 예약으로 함 (즉시로 하고 싶으면 일자를 그렇게 넣어주면됨 )
		//sendDate로 보내도록 하고  
		//구분은 즉시, 발송예약을 하도록 하고..
		 
		//1.등록자 정보 입력 
		smsVO.setRegId(smsVO.getUserId());
		smsVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		//2.메시지아이디 만들기
		smsVO.setMsgid(smsVO.getCampNo()+"||");
		//3.Keygen 만들기 
		smsVO.setKeygen(StringUtil.getDate(Code.TM_YMDHMSM));
		//4.컬럼 암호화 설정
		smsVO.setSendTelno( cryptoService.getEncrypt("SEND_TELNO",smsVO.getSendTelno()) );
		
		int result = 0;
		
		List<SmsAttachVO> smsAttachList = new ArrayList<SmsAttachVO>();// 첨부파일 목록
		
		try {
			result = smsCampaignService.insertSmsInfo(smsVO, smsAttachList);
		} catch(Exception e) {
			logger.error("smsCampaignService.insertSmsInfo error = " + e);
		}
		
		if(result > 0) {
			try {
				if (smsVO.getSmsPhones().length() > 1) {
					String[] arrSmsPhone = smsVO.getSmsPhones().split(",");
					for (int i = 0; i < arrSmsPhone.length; i++) {
						SmsPhoneVO smsPhoneVO = new SmsPhoneVO();
						smsPhoneVO.setKeygen(smsVO.getKeygen());
						smsPhoneVO.setMsgid(smsVO.getMsgid());
						arrSmsPhone[i] = arrSmsPhone[i].replace("-", "");
						smsPhoneVO.setPhone(cryptoService.getEncrypt("PHONE",arrSmsPhone[i]) ); 
						result = smsCampaignService.insertSmsPhone(smsPhoneVO);
					}
				}
				
			} catch(Exception e) {
				logger.error("smsCampaignService.insertSmsPhone error = " + e);
			}
		}
		
		// 결과값		
		if (result > 0) {
			model.addAttribute("result","Success"); 
		} else {
			model.addAttribute("result","Fail");
		}
		
		return "sms/cam/smsAddApi";
	}
	
	/**
	 * 문자 정보 수정 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/smsUpdateP")
	public String goSmsUpdateP(@ModelAttribute SmsVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSmsUpdateP getMsgid    = " + searchVO.getMsgid());
		logger.debug("goSmsUpdateP getKeygen   = " + searchVO.getKeygen()); 
 
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
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
		
		// 전송유형 목록
		CodeVO gubun = new CodeVO();
		gubun.setUilang((String)session.getAttribute("NEO_UILANG"));
		gubun.setCdGrp("C119");
		gubun.setCd("000");
		gubun.setUseYn("Y");
		List<CodeVO> gubunList = null;
		try {
			gubunList = codeService.getCodeListByUpCd(gubun);
		} catch(Exception e) {
			logger.error("codeService.getCodeListByUpCd[C119] error = " + e);
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
		
		// 발송상태코드 목록
		CodeVO statusVO = new CodeVO();
		statusVO.setUilang("000");
		statusVO.setCdGrp("C118");
		statusVO.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(statusVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList error = " + e);
		}
		
		// 문자상태코드 목록
		CodeVO smsStatus = new CodeVO();
		smsStatus.setUilang((String)session.getAttribute("NEO_UILANG"));
		smsStatus.setCdGrp("C116");
		smsStatus.setUseYn("Y");
		List<CodeVO> smsStatusList = null;
		try {
			smsStatusList = codeService.getCodeList(smsStatus);
		} catch(Exception e) {
			logger.error("codeService.getCodeList error = " + e);
		}
		
		// 문자무료수신거부 목록
		CodeVO legal = new CodeVO();
		legal.setUilang((String)session.getAttribute("NEO_UILANG"));
		legal.setCdGrp("C117");
		legal.setCd("000");
		legal.setUseYn("Y");
		
		String smsLegalCf ="";
		try {
			CodeVO smsLegal = codeService.getCodeInfo(legal);
			if(smsLegal != null && !"".equals(smsLegal.getCdNm())) {
				smsLegalCf = smsLegal.getCdNm();
			} else {//설정->수신설정
				smsLegalCf = "080-000-0000";
			}
		} catch (Exception e) {
			logger.error("codeService.getCodeInfo[C117] error = " + e);
		}
		
		// 캠페인 목록 조회
		SmsCampaignVO camp = new SmsCampaignVO();
		camp.setUilang((String)session.getAttribute("NEO_UILANG"));
		camp.setSearchStatus("000");
		camp.setPage(1);
		camp.setRows(10000);
		camp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<SmsCampaignVO> smsCampList = null;
		try {
			smsCampList = smsCampaignService.getCampaignList(camp);
		} catch(Exception e) {
			logger.error("smsCampaignService.getCampaignList error = " + e);
		}
		
		// 수신자그룹(발송대상) 목록
		SegmentVO seg = new SegmentVO();
		seg.setUilang((String)session.getAttribute("NEO_UILANG"));
		seg.setSearchStatus("000");
		seg.setPage(1);
		seg.setRows(10000);
		seg.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		seg.setSearchSmsuseYn("Y");
		
		List<SegmentVO> segList = null;
		try {
			segList = segmentService.getSegmentList(seg);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentList error = " + e);
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
		
		// 사용자정보
		UserVO userInfo = new UserVO();
		userInfo.setUilang((String)session.getAttribute("NEO_UILANG"));
		userInfo.setUserId((String)session.getAttribute("NEO_USER_ID"));
		try {
			userInfo = systemService.getUserInfo(userInfo);
		} catch(Exception e) {
			logger.error("systemService.getUserInfo error = " + e);
		}
		
		// 문자 상세정보 조회
		SmsVO smsInfo = new SmsVO(); 
		try {
			smsInfo = smsCampaignService.getSmsInfo(searchVO);
		} catch(Exception e) {
			logger.error("smsCampaignService.getMailInfo error = " + e);
		}
		
		// 문자 수신 번호 조회 
		List<SmsPhoneVO> oldSmsPhoneList = new ArrayList<SmsPhoneVO>();
		List<SmsPhoneVO> smsPhoneList = new ArrayList<SmsPhoneVO>();
		String smsPhone = ""; 
		try {
			oldSmsPhoneList = smsCampaignService.getSmsPhoneList(searchVO);
			if (oldSmsPhoneList.size() > 0 ) {
				for(int i =0 ; i < oldSmsPhoneList.size(); i++) {
					SmsPhoneVO tempSmsVO = new SmsPhoneVO();
					tempSmsVO = oldSmsPhoneList.get(i);
					smsPhone = cryptoService.getDecrypt("PHONE", tempSmsVO.getPhone());
					smsPhone = StringUtil.getPhone(smsPhone);
					tempSmsVO.setPhone(smsPhone);
					smsPhoneList.add(tempSmsVO);
				}
			}
		} catch(Exception e) {
			logger.error("smsCampaignService.getSmsPhoneList error = " + e);
		}
		// 첨부파일 목록		
		List<SmsAttachVO> smsAttachList = new ArrayList<SmsAttachVO>();
		try {
			if (smsInfo != null) {
				smsAttachList = smsCampaignService.getSmsAttachList(searchVO);
			}
			//
		} catch(Exception e) {
			logger.error("smsCampaignService.getSmsAttachList error = " + e);
		}
		
		//SMSMessage 분리
		
		String smsMessageFull = smsInfo.getSmsMessage();
		logger.debug("smsMessageFull [" + smsMessageFull  + "]");
		String smsMessageHead = "";
		String smsMessageTail = "";
		String smsMessage ="";
		if(smsInfo.getLegalYn()!= null && "Y".equals(smsInfo.getLegalYn())) {
			smsMessageHead ="(광고)";
			smsMessageTail ="무료수신거부 " + smsInfo.getLegalCf();
		}
		smsMessageHead = smsMessageHead + smsInfo.getSmsName();
		smsMessageFull = smsMessageFull.replace(smsMessageHead, "");
		smsMessageFull = smsMessageFull.replace(smsMessageTail, "");
		smsMessage = smsMessageFull.trim();
		
		logger.debug("smsMessageFull [" + smsMessage  + "]");
		smsInfo.setSmsMessage(smsMessage);
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("gubunList", gubunList);			// 문자전송유형
		model.addAttribute("smsCampList", smsCampList);		// 캠페인목록
		model.addAttribute("deptList", deptList);			// 부서 목록
		model.addAttribute("userList", userList);			// 사용자목록
		model.addAttribute("userInfo", userInfo);			// 사용자정보
		model.addAttribute("mergeList", mergeList);			// 수신자정보머지키코드
		model.addAttribute("statusList", statusList);		// 발송상태
		model.addAttribute("smsStatusList", smsStatusList);	// 문자상태		
		model.addAttribute("segList", segList);				// 발송대상(세그먼트) 목록
		model.addAttribute("reasonList", reasonList);		// 수신자그룹 조회 사유
		model.addAttribute("smsPhoneList", smsPhoneList);	// 수신자핸드폰번호리스트
		model.addAttribute("smsInfo", smsInfo);				// 문자정보
		model.addAttribute("smsAttachList", smsAttachList);	// 문자정보
		model.addAttribute("smsLegalCf", smsLegalCf);		// 무료수신거부 전화번호		
		model.addAttribute("imgUploadPath", properties.getProperty("IMG.SMS_UPLOAD_PATH"));	//첨부파일 경로
		return "sms/cam/smsUpdateP";
	}
	 
	/**
	 * 문자 발송 정보를 수정한다.
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/smsUpdate")	
	public String goSmsUpdate(@ModelAttribute SmsVO smsVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {		
		logger.debug("goSmsUpdate msgid        = " + smsVO.getMsgid());
		logger.debug("goSmsUpdate keygen        = " + smsVO.getKeygen());		
		logger.debug("goSmsUpdate userId        = " + smsVO.getUserId());
		logger.debug("goSmsUpdate deptNo        = " + smsVO.getDeptNo());
		logger.debug("goSmsUpdate segNoc         = " + smsVO.getSegNoc());
		logger.debug("goSmsUpdate sendTelNo     = " + smsVO.getSendTelno());
		logger.debug("goSmsUpdate campNo      = " + smsVO.getCampNo());
		logger.debug("goSmsUpdate sendYmd       = " + smsVO.getSendYmd());
		logger.debug("goSmsUpdate sendHour      = " + smsVO.getSendHour());
		logger.debug("goSmsUpdate sendMin       = " + smsVO.getSendMin());
		logger.debug("goSmsUpdate smsName       = " + smsVO.getSmsName());
		logger.debug("goSmsUpdate gubun         = " + smsVO.getGubun());
		logger.debug("goSmsUpdate taskNm        = " + smsVO.getTaskNm());
		logger.debug("goSmsUpdate smsStatus     = " + smsVO.getSmsStatus());
		logger.debug("goSmsUpdate status        = " + smsVO.getStatus());
		logger.debug("goSmsUpdate sendNm        = " + smsVO.getSendNm());
		logger.debug("goSmsUpdate sendTyp       = " + smsVO.getSendTyp());
		logger.debug("goSmsUpdate smsPhones     = " + smsVO.getSmsPhones());
		logger.debug("goSmsUpdate legalYn       = " + smsVO.getLegalYn());
		logger.debug("goSmsUpdate smsMessage    = " + smsVO.getSmsMessage());
		logger.debug("goSmsUpdate attachNm      = " + smsVO.getAttachNm());
		logger.debug("goSmsUpdate attachPath    = " + smsVO.getAttachPath()); 
		
		List<SmsAttachVO> smsAttachList = new ArrayList<SmsAttachVO>();		// 첨부파일 목록
		
		// 파일 사이즈 체크
		if(smsVO.getAttachPath() != null && !"".equals(smsVO.getAttachPath())) {
			File attachFile = null;
			String basePath = properties.getProperty("IMG.SMS_UPLOAD_PATH") ;
			
			long fileSize = 0;
			String[] fileNm = smsVO.getAttachNm().split(",");
			String[] filePath = smsVO.getAttachPath().split(",");
			
			for(int i=0;i<filePath.length;i++) {
				attachFile = new File(basePath + "/" + filePath[i]);
				fileSize += attachFile.length();
				
				SmsAttachVO smsAttach = new SmsAttachVO();
				smsAttach.setMsgid(smsVO.getMsgid());
				smsAttach.setKeygen(smsVO.getKeygen());
				smsAttach.setAttNm(fileNm[i]);
				smsAttach.setAttFlPath(filePath[i]);
				smsAttach.setAttFlSize(attachFile.length());
				smsAttach.setAttPriviewPath("");
				
				smsAttachList.add(smsAttach);
			}
			
			// 합계 파일 사이즈가 10MB 이상인 경우 중지 
			if(fileSize > 600) {	// 600KB
				model.addAttribute("result","Fail");
				model.addAttribute("FILE_SIZE","EXCESS");
				return "sms/cam/smsUpdate";
			}
		}
		
		// 기본값 설정 
		if(smsVO.getDeptNo() == 0) smsVO.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));						// 부서번호
		if(StringUtil.isNull(smsVO.getUserId())) smsVO.setUserId((String)session.getAttribute("NEO_USER_ID"));		// 사용자아이디		
		if(!StringUtil.isNull(smsVO.getSegNoc())) smsVO.setSegNo(Integer.parseInt( smsVO.getSegNoc().substring(0, smsVO.getSegNoc().indexOf("|")) ));// 세그먼트번호(발송대상그룹)
		
		//1.즉시 여부에 따른 발송시간 입력
		if("000".equals(smsVO.getSendTyp())){
			smsVO.setSendDate(StringUtil.getDate(Code.TM_YMDHMS));
			smsVO.setSendYm(StringUtil.getDate(Code.TM_YM));
		} else {
			if(StringUtil.isNull(smsVO.getSendYmd())) smsVO.setSendYmd("0000.00.00");									// 예약시간(예약일)
			String sendHour = StringUtil.setTwoDigitsString(smsVO.getSendHour());										// 예약시간(시)
			String sendMin = StringUtil.setTwoDigitsString(smsVO.getSendMin());											// 예약시간(분)
			String sendSec = "00";																						// 예약시간(초)
			smsVO.setSendDate( smsVO.getSendYmd().replaceAll("\\.", "") + sendHour + sendMin + sendSec );				// 예약일시
			smsVO.setSendYm(smsVO.getSendYmd().replaceAll("\\.", "").substring(0,6));
		}

		//5.컬럼 암호화 설정
		smsVO.setSendTelno( cryptoService.getEncrypt("SEND_TELNO",smsVO.getSendTelno()) );		
	 
		//6.SMS Message 만들기
		String smsMessageHead = "";
		String smsMessageTail = "";
		String smsMessage ="";
		if(smsVO.getLegalYn()!= null && "Y".equals(smsVO.getLegalYn())) {
			smsMessageHead ="(광고)";
			smsMessageTail = "무료수신거부 " + smsVO.getLegalCf();
		}
		smsMessageHead = smsMessageHead + smsVO.getSmsName();
		smsMessage = smsMessageHead + "\n" + smsVO.getSmsMessage();
		if (!"".equals(smsMessageTail)) {
			smsMessage = smsMessageHead + "\n" + smsVO.getSmsMessage() + "\n" + smsMessageTail;
		}
		
		smsVO.setSmsMessage(smsMessage);
		logger.debug("smsMessage [" + smsMessage + "]");
		
		int infoResult = 0;
		int result = 0;
	 
		try {
			infoResult = smsCampaignService.updateSmsInfo(smsVO, smsAttachList);
		} catch(Exception e) {
			logger.error("smsCampaignService.updateSmsInfo error = " + e);
		}
		
		//추가 번호 있을 경우 입력  
		if(infoResult > 0) {
			try {
				result = smsCampaignService.deleteSmsPhone(smsVO);	
				if (result >= 0) {
					try {
						
						if (smsVO.getSmsPhones().length() > 1) {
							String[] arrSmsPhone = smsVO.getSmsPhones().split(",");
							for (int i = 0; i < arrSmsPhone.length; i++) {
								SmsPhoneVO smsPhoneVO = new SmsPhoneVO(); 						
								smsPhoneVO.setKeygen(smsVO.getKeygen());
								smsPhoneVO.setMsgid(smsVO.getMsgid());
								arrSmsPhone[i] = arrSmsPhone[i].replace("-", "");
								smsPhoneVO.setPhone(cryptoService.getEncrypt("PHONE",arrSmsPhone[i]) ); 
								result = smsCampaignService.insertSmsPhone(smsPhoneVO);		
							}
						} 
					} catch (Exception e) {
						logger.error("smsCampaignService.insertSmsPhone error = " + e);
					}
				}
			} catch(Exception e) {
				logger.error("smsCampaignService.insertSmsPhone error = " + e);
			}
		}
		
		if(infoResult > 0) {
			try {
				result = smsCampaignService.deleteSmsAttachInfo(smsVO);	
				if(result >= 0) { 
					try {
						if(smsAttachList != null && smsAttachList.size() > 0) {
							for(SmsAttachVO smsAttach:smsAttachList) {
								result +=smsCampaignService.insertSmsAttachInfo(smsAttach);
							}
						}
					} catch(Exception e) {
						logger.error("smsCampaignService.insertSmsAttachInfo error = " + e);
					}
				}
			} catch(Exception e) {
				logger.error("smsCampaignService.deleteSmsAttachInfo error = " + e);
			}
		} 
		
		if (infoResult > 0 &&  result >= 0 ) {
			model.addAttribute("result","Success");
		} else {
			model.addAttribute("result","Success");
		}
		
		return "sms/cam/smsUpdate";
	}
	
	/**
	 * 문자 발송 정보를 삭제한다
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/smsDelete")
	public ModelAndView updateSmsStatus(@ModelAttribute SmsVO smsVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateSmsStatus selKeygens    = " + smsVO.getSelKeygens()); 
		logger.debug("updateSmsStatus msgid         = " + smsVO.getMsgid());
		logger.debug("updateSmsStatus keygen        = " + smsVO.getKeygen());
		logger.debug("updateSmsStatus smsStatus     = " + smsVO.getSmsStatus());
		
		int result = -1;
		String[] targetInfo;
		String[] targetSms = null;
		List<SmsVO> targetSmsVOList  = new ArrayList<SmsVO>(); 
		
		
		//조회화면에서 일괄적으로 삭제 하는 경우 		
		if ( smsVO.getSelKeygens() != null &&  !"".equals(smsVO.getSelKeygens())) {
			targetSms = smsVO.getSelKeygens().split(",");
			
			if (targetSms !=null && targetSms.length > 0 ) {
				
				try {
					for (int i = 0 ; i < targetSms.length; i ++) {
						SmsVO targetSmsVO = new SmsVO();
						targetInfo = targetSms[i].split(":");
						targetSmsVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
						targetSmsVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
						targetSmsVO.setMsgid(targetInfo[0]);
						targetSmsVO.setKeygen(targetInfo[1]);
						targetSmsVO.setSmsStatus(smsVO.getSmsStatus());
						targetSmsVOList.add(targetSmsVO);
					}
				} catch(Exception e) {
					logger.error("smsCampaignService.updateSmsStatus error = " + e);
				}  
			}
		} else {
			SmsVO targetSmsVO = new SmsVO();
			targetSmsVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
			targetSmsVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
			targetSmsVO.setSmsStatus(smsVO.getSmsStatus());
			targetSmsVO.setMsgid(smsVO.getMsgid());
			targetSmsVO.setKeygen(smsVO.getKeygen());
			targetSmsVOList.add(targetSmsVO);
		}
		
		try {
			if (targetSmsVOList != null && targetSmsVOList.size() > 0 ) {
				for(int i=0; i < targetSmsVOList.size(); i++) { 
					result = smsCampaignService.updateSmsStatus(targetSmsVOList.get(i));
				} 
			}
		} catch(Exception e) {
			logger.error("smsCampaignService.updateSmsStatus error = " + e);
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
	 * 문자 발송 정보를 승인한다
	 * @param smsVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/smsAdmit")
	public ModelAndView smsAdmit(@ModelAttribute SmsVO smsVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateSmsStatus selKeygens    = " + smsVO.getSelKeygens()); 
		logger.debug("updateSmsStatus msgid         = " + smsVO.getMsgid());
		logger.debug("updateSmsStatus keygen        = " + smsVO.getKeygen());
		logger.debug("updateSmsStatus smsStatus     = " + smsVO.getSmsStatus());

		smsVO.setSelKeygens("");
		
		int result = -1;
		String[] targetInfo;
		String[] targetSms = null;
		List<SmsVO> targetSmsVOList  = new ArrayList<SmsVO>(); 
		
		//조회화면에서 일괄적으로 승인 하는 경우
		if ( smsVO.getSelKeygens() != null &&  !"".equals(smsVO.getSelKeygens())) {
			targetSms = smsVO.getSelKeygens().split(",");
			
			if (targetSms !=null && targetSms.length > 0 ) {
				try {
					for (int i = 0 ; i < targetSms.length; i ++) {
						targetInfo = targetSms[i].split(":");
						SmsVO targetSmsVO = new SmsVO();
						targetSmsVO.setMsgid(targetInfo[0]);
						targetSmsVO.setKeygen(targetInfo[1]);
						targetSmsVO.setStatus("001");
						targetSmsVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
						targetSmsVOList.add(targetSmsVO);
					}
				} catch(Exception e) {
					logger.error("smsCampaignService.updateSmsStatusAdmit error = " + e);
				}  
			}
		} else {
			SmsVO targetSmsVO = new SmsVO();
			targetSmsVO.setStatus("001");
			targetSmsVO.setMsgid(smsVO.getMsgid());
			targetSmsVO.setKeygen(smsVO.getKeygen());
			targetSmsVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
			targetSmsVOList.add(targetSmsVO);
		}
		
		try {
			if (targetSmsVOList != null && targetSmsVOList.size() > 0 ) {
				for(int i=0; i < targetSmsVOList.size(); i++) { 
					result = smsCampaignService.updateSmsStatusAdmit(targetSmsVOList.get(i));
				} 
			}
		} catch(Exception e) {
			logger.error("smsCampaignService.updateSmsStatusAdmit error = " + e);
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
	 * 문자를 복사한다.(주업무, 보조업무, 첨부파일)
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/smsCopy")
	public ModelAndView copySmsInfo(@ModelAttribute SmsVO smsVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("copySmsInfo selKeygens    = " + smsVO.getSelKeygens()); 
		logger.debug("copySmsInfo msgid         = " + smsVO.getMsgid());
		logger.debug("copySmsInfo keygen        = " + smsVO.getKeygen());
		logger.debug("copySmsInfo smsStatus     = " + smsVO.getSmsStatus());
		logger.debug("copySmsInfo gubun         = " + smsVO.getGubun());

		int result = -1;
		String[] targetInfo;
		String[] targetSms = null;
		List<SmsVO> targetSmsVOList  = new ArrayList<SmsVO>(); 
	
		String serverUrl = request.getServletContext().getRealPath("/");
		//조회화면에서 일괄적으로 복사 하는 경우 		
		if ( smsVO.getSelKeygens() != null &&  !"".equals(smsVO.getSelKeygens())) {
			targetSms = smsVO.getSelKeygens().split(",");
			
			if (targetSms !=null && targetSms.length > 0 ) {
				try {
					for (int i = 0 ; i < targetSms.length; i ++) {
						SmsVO targetSmsVO = new SmsVO();
						targetInfo = targetSms[i].split(":");
						targetSmsVO.setMsgid(targetInfo[0]);
						targetSmsVO.setKeygen(targetInfo[1]);
						targetSmsVOList.add(targetSmsVO);
					}
				} catch(Exception e) {
					logger.error("smsCampaignService.copySmsInfo error = " + e);
				}
			}
		} else {
			SmsVO targetSmsVO = new SmsVO();
			targetSmsVO.setMsgid(smsVO.getMsgid());
			targetSmsVO.setKeygen(smsVO.getKeygen());
			targetSmsVOList.add(targetSmsVO);
		} 
		
		try {
			if (targetSmsVOList != null && targetSmsVOList.size() > 0 ) {
				for(int i=0; i < targetSmsVOList.size(); i++) {
					result = smsCampaignService.copySmsInfo(targetSmsVOList.get(i), properties, serverUrl);
				} 
			}
		} catch(Exception e) {
			logger.error("smsCampaignService.copySmsInfo error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		if(result >= 0) {
			map.put("result", "Success");
		} else {
			map.put("result", "Fail");
		}
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
	
	
	/****************************************** KAKAO 알림톡 발송 관리 ******************************************/
	/**
	 * KAKAO 알림톡 발송 등록현황 화면을 출력한다.
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/kakaoListP")
	public String goKakaoListP(@ModelAttribute SmsVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goKakaoListP searchStartDt     = " + searchVO.getSearchStartDt());
		logger.debug("goKakaoListP searchEndDt       = " + searchVO.getSearchEndDt());
		logger.debug("goKakaoListP searchDeptNo      = " + searchVO.getSearchDeptNo());
		logger.debug("goKakaoListP searchTaskNm      = " + searchVO.getSearchTaskNm());
		logger.debug("goKakaoListP searchCampNo      = " + searchVO.getSearchCampNo());
		logger.debug("goKakaoListP searchStatus      = " + searchVO.getSearchStatus());
		logger.debug("goKakaoListP searchKakaoStatus = " + searchVO.getSearchSmsStatus());
		logger.debug("goKakaoListP searchTempCd      = " + searchVO.getSearchTempCd());
		
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

		// 전송유형 목록
		CodeVO gubun = new CodeVO();
		gubun.setUilang((String) session.getAttribute("NEO_UILANG"));
		gubun.setCdGrp("C119");
		gubun.setCd("001");
		gubun.setUseYn("Y");
		List<CodeVO> gubunList = null;
		try {
			gubunList = codeService.getCodeListByUpCd(gubun);
		} catch (Exception e) {
			logger.error("codeService.getCodeListByUpCd[C119] error = " + e);
		}
		
		// 캠페인 목록
		SmsCampaignVO campVO = new SmsCampaignVO();
		campVO.setStatus("000");
		campVO.setAdminYn((String) session.getAttribute("NEO_ADMIN_YN"));
		campVO.setDeptNo("Y".equals((String) session.getAttribute("NEO_ADMIN_YN")) ? 0: (int) session.getAttribute("NEO_DEPT_NO"));
		List<SmsCampaignVO> campList = null;
		try {
			campList = codeService.getCampaignSmsList(campVO);
		} catch (Exception e) {
			logger.error("codeService.getCampaignSmsList error = " + e);
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
		userVO.setDeptNo("Y".equals((String) session.getAttribute("NEO_ADMIN_YN")) ? 0: (int) session.getAttribute("NEO_DEPT_NO"));
		userVO.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch (Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		// 발송상태코드 목록
		CodeVO statusVO = new CodeVO();
		statusVO.setUilang("000");
		statusVO.setCdGrp("C118");
		statusVO.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(statusVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error = " + e);
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
		
		// 카카오 알림톡 상태코드 목록
		CodeVO kakaoStatus = new CodeVO();
		kakaoStatus.setUilang((String) session.getAttribute("NEO_UILANG"));
		kakaoStatus.setCdGrp("C116");
		kakaoStatus.setUseYn("Y");
		List<CodeVO> kakaoStatusList = null;
		try {
			kakaoStatusList = codeService.getCodeList(kakaoStatus);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error = " + e);
		}
		
		// 카카오 알림톡 템플릿 목록 조회
		CodeVO kakaoTemplate = new CodeVO();
		List<CodeVO> kakaoTemplateList = null;
		kakaoTemplate.setStatus("000");
		
		try {
			kakaoTemplateList = codeService.getKakaoTemplateList(kakaoTemplate);
		} catch (Exception e) {
			logger.error("codeService.getKakaoTemplateList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO); 				// 검색항목
		model.addAttribute("gubunList", gubunList); 			// 카카오 알림톡 전송유형
		model.addAttribute("campList", campList); 				// 캠페인목록
		model.addAttribute("deptList", deptList); 				// 사용자그룹목록
		model.addAttribute("userList", userList); 				// 사용자목록
		model.addAttribute("statusList", statusList); 			// 카카오 알림톡 발송상태목록
		model.addAttribute("reasonList", reasonList);			// 조회사유
		model.addAttribute("kakaoStatusList", kakaoStatusList); // 카카오 알림톡 상태목록
		model.addAttribute("kakaoTemplateList", kakaoTemplateList); // 카카오 알림톡 템플릿목록

		return "sms/cam/kakaoListP";
	}
	
	/**
	 * KAKAO 알림톡 발송 등록현황 목록을 조회한다.
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/kakaoList")
	public String goKakaoList(@ModelAttribute SmsVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goKakaoList searchStartDt     = " + searchVO.getSearchStartDt());
		logger.debug("goKakaoList searchEndDt       = " + searchVO.getSearchEndDt());
		logger.debug("goKakaoList searchDeptNo      = " + searchVO.getSearchDeptNo());
		logger.debug("goKakaoList searchTaskNm      = " + searchVO.getSearchTaskNm());
		logger.debug("goKakaoList searchCampNo      = " + searchVO.getSearchCampNo());
		logger.debug("goKakaoList searchStatus      = " + searchVO.getSearchStatus());
		logger.debug("goKakaoList searchKakaoStatus = " + searchVO.getSearchSmsStatus());
		logger.debug("goKakaoList searchTempCd      = " + searchVO.getSearchTempCd());
		//logger.debug("goKakaoList searchSmsName     = " + searchVO.getSearchSmsName());
		
		// 검색 기본값 설정
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		searchVO.setAdminYn((String) session.getAttribute("NEO_ADMIN_YN"));
		searchVO.setSearchGubun("004");
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage() - 1) * searchVO.getRows());
		int totalCount = 0;
		
		// 카카오 알림톡 목록 조회
		List<SmsVO> kakaoList = null;
		try {
			kakaoList = smsCampaignService.getSmsList(searchVO);
		} catch (Exception e) {
			logger.error("smsCampaignService.getSmsList error = " + e);
		}

		if (kakaoList != null && kakaoList.size() > 0) {
			totalCount = kakaoList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);

		model.addAttribute("searchVO", searchVO); // 검색 항목
		model.addAttribute("kakaoList", kakaoList); // 카카오 알림톡 발송 목록
		model.addAttribute("pageUtil", pageUtil); // 페이징

		return "sms/cam/kakaoList";
	}
	
	/**
	 * 카카오 알림톡 발송 등록 화면을 출력한다.
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/kakaoAddP")
	public String goKakaoAddP(@ModelAttribute SmsVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goKakaoAddP searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goKakaoAddP searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("goKakaoAddP searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goKakaoAddP searchTaskNm     = " + searchVO.getSearchTaskNm());
		logger.debug("goKakaoAddP searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goKakaoAddP searchStatus     = " + searchVO.getSearchStatus());
		logger.debug("goKakaoAddP searchGubun      = " + searchVO.getSearchGubun());

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

		// 전송유형 목록
		CodeVO gubun = new CodeVO();
		gubun.setUilang((String) session.getAttribute("NEO_UILANG"));
		gubun.setCdGrp("C119");
		gubun.setCd("000");
		gubun.setUseYn("Y");
		List<CodeVO> gubunList = null;
		try {
			gubunList = codeService.getCodeListByUpCd(gubun);
		} catch (Exception e) {
			logger.error("codeService.getCodeListByUpCd[C119] error = " + e);
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
		userVO.setDeptNo("Y".equals((String) session.getAttribute("NEO_ADMIN_YN")) ? 0
				: (int) session.getAttribute("NEO_DEPT_NO"));
		userVO.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch (Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}

		// 발송상태코드 목록
		CodeVO statusVO = new CodeVO();
		statusVO.setUilang("000");
		statusVO.setCdGrp("C118");
		statusVO.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(statusVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error = " + e);
		}

		// 카카오 알림톡 상태코드 목록
		CodeVO kakaoStatus = new CodeVO();
		kakaoStatus.setUilang((String) session.getAttribute("NEO_UILANG"));
		kakaoStatus.setCdGrp("C116");
		kakaoStatus.setUseYn("Y");
		List<CodeVO> kakaoStatusList = null;
		try {
			kakaoStatusList = codeService.getCodeList(kakaoStatus);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error = " + e);
		}
		
		// 카카오 알림톡 무료수신거부 목록
		CodeVO legal = new CodeVO();
		legal.setUilang((String) session.getAttribute("NEO_UILANG"));
		legal.setCdGrp("C117");
		legal.setCd("002");
		legal.setUseYn("Y");
		
		String kakaoLegalCf ="";
		try {
			CodeVO kakaoLegal = codeService.getCodeInfo(legal);
			if(kakaoLegal != null && !"".equals(kakaoLegal.getCdNm())) {
				kakaoLegalCf = kakaoLegal.getCdNm();
			} else {//설정->수신설정
				kakaoLegalCf = "설정된 수신거부 경로가 없습니다";
			}
		} catch (Exception e) {
			logger.error("codeService.getCodeInfo[C117] error = " + e);
		}
		
		// 캠페인 목록 조회
		SmsCampaignVO campVO = new SmsCampaignVO();
		campVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		campVO.setSearchStatus("000");
		campVO.setPage(1);
		campVO.setRows(10000);
		campVO.setSearchDeptNo("Y".equals((String) session.getAttribute("NEO_ADMIN_YN")) ? 0: (int) session.getAttribute("NEO_DEPT_NO"));
		List<SmsCampaignVO> campList = null;
		try {
			campList = smsCampaignService.getCampaignList(campVO);
		} catch (Exception e) {
			logger.error("smsCampaignService.getCampaignList error = " + e);
		}
		
		// 수신자그룹(발송대상) 목록
		SegmentVO seg = new SegmentVO();
		seg.setUilang((String) session.getAttribute("NEO_UILANG"));
		seg.setSearchStatus("000");
		seg.setPage(1);
		seg.setRows(10000);
		seg.setSearchDeptNo("Y".equals((String) session.getAttribute("NEO_ADMIN_YN")) ? 0: (int) session.getAttribute("NEO_DEPT_NO"));
		seg.setSmsuseYn("Y");
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
		
		// 카카오 알림톡 템플릿 목록 조회
		KakaoTemplateVO search = new KakaoTemplateVO();;
		List<KakaoTemplateVO> kakaoTemplateList = null;
		search.setUilang((String) session.getAttribute("NEO_UILANG"));
		search.setSearchStartDt(null);
		search.setSearchEndDt(null);
		search.setSearchStatus("000");
		search.setStartRow(0);
		search.setRows(100000);

		try {
			kakaoTemplateList = kakaoTemplateService.getKakaoTemplateList(search);
		} catch (Exception e) {
			logger.error("segmentService.getKakaoTemplateList error = " + e);
		}

		model.addAttribute("searchVO", searchVO); // 검색항목
		model.addAttribute("gubunList", gubunList); // 카카오 알림톡 전송유형
		model.addAttribute("campList", campList); // 캠페인목록
		model.addAttribute("deptList", deptList); // 부서 목록
		model.addAttribute("userList", userList); // 사용자목록
		model.addAttribute("userInfo", userInfo); // 사용자정보
		model.addAttribute("mergeList", mergeList); // 수신자정보머지키코드
		model.addAttribute("statusList", statusList); // 발송상태
		model.addAttribute("kakaoStatusList", kakaoStatusList); // 카카오 알림톡 상태
		model.addAttribute("reasonList", reasonList); // 조회사유
		model.addAttribute("segList", segList); // 발송대상(세그먼트) 목록
		model.addAttribute("kakaoLegalCf", kakaoLegalCf); // 무료수신거부 설정방법
		model.addAttribute("imgUploadPath", properties.getProperty("IMG.SMS_UPLOAD_PATH")); // 이미지첨부파일경
		model.addAttribute("kakaoTemplateList", kakaoTemplateList); // 카카오 알림톡 템플릿 정보

		return "sms/cam/kakaoAddP";
	}
	
	/**
	 * 카카오 알림톡 정보 수정 화면을 출력한다.
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/kakaoUpdateP")
	public String goKakaoUpdateP(@ModelAttribute SmsVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goKakaoUpdateP getMsgid     = " + searchVO.getMsgid());
		logger.debug("goKakaoUpdateP getKeygen    = " + searchVO.getKeygen());
		logger.debug("goKakaoUpdateP searchTempCd = " + searchVO.getSearchTempCd());

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

		// 전송유형 목록
		CodeVO gubun = new CodeVO();
		gubun.setUilang((String) session.getAttribute("NEO_UILANG"));
		gubun.setCdGrp("C119");
		gubun.setCd("000");
		gubun.setUseYn("Y");
		List<CodeVO> gubunList = null;
		try {
			gubunList = codeService.getCodeListByUpCd(gubun);
		} catch (Exception e) {
			logger.error("codeService.getCodeListByUpCd[C119] error = " + e);
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
		userVO.setDeptNo("Y".equals((String) session.getAttribute("NEO_ADMIN_YN")) ? 0: (int) session.getAttribute("NEO_DEPT_NO"));
		userVO.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch (Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		// 발송상태코드 목록
		CodeVO statusVO = new CodeVO();
		statusVO.setUilang("000");
		statusVO.setCdGrp("C118");
		statusVO.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(statusVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error = " + e);
		}
		
		// 카카오 알림톡 상태코드 목록
		CodeVO kakaoStatus = new CodeVO();
		kakaoStatus.setUilang((String) session.getAttribute("NEO_UILANG"));
		kakaoStatus.setCdGrp("C116");
		kakaoStatus.setUseYn("Y");
		List<CodeVO> kakaoStatusList = null;
		try {
			kakaoStatusList = codeService.getCodeList(kakaoStatus);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error = " + e);
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
		
		// 캠페인 목록 조회
		SmsCampaignVO camp = new SmsCampaignVO();
		camp.setUilang((String) session.getAttribute("NEO_UILANG"));
		camp.setSearchStatus("000");
		camp.setPage(1);
		camp.setRows(10000);
		camp.setSearchDeptNo("Y".equals((String) session.getAttribute("NEO_ADMIN_YN")) ? 0: (int) session.getAttribute("NEO_DEPT_NO"));
		List<SmsCampaignVO> campList = null;
		try {
			campList = smsCampaignService.getCampaignList(camp);
		} catch (Exception e) {
			logger.error("smsCampaignService.getCampaignList error = " + e);
		}
		
		// 수신자그룹(발송대상) 목록
		SegmentVO seg = new SegmentVO();
		seg.setUilang((String) session.getAttribute("NEO_UILANG"));
		seg.setSearchStatus("000");
		seg.setPage(1);
		seg.setRows(10000);
		seg.setSearchDeptNo("Y".equals((String) session.getAttribute("NEO_ADMIN_YN")) ? 0: (int) session.getAttribute("NEO_DEPT_NO"));
		seg.setSearchSmsuseYn("Y");
		List<SegmentVO> segList = null;
		try {
			segList = segmentService.getSegmentList(seg);
		} catch (Exception e) {
			logger.error("segmentService.getSegmentList error = " + e);
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
		
		// 카카오 알림톡 상세정보 조회
		SmsVO kakaoInfo = new SmsVO();
		try {
			kakaoInfo = smsCampaignService.getSmsInfo(searchVO);
		} catch (Exception e) {
			logger.error("smsCampaignService.getMailInfo error = " + e);
		}
		
		// 카카오 알림톡 수신번호 조회
		List<SmsPhoneVO> oldKakaoPhoneList = new ArrayList<SmsPhoneVO>();
		List<SmsPhoneVO> kakaoPhoneList = new ArrayList<SmsPhoneVO>();
		String smsPhone = "";
		try {
			oldKakaoPhoneList = smsCampaignService.getSmsPhoneList(searchVO);
			if (oldKakaoPhoneList.size() > 0) {
				for (int i = 0; i < oldKakaoPhoneList.size(); i++) {
					SmsPhoneVO tempSmsVO = new SmsPhoneVO();
					tempSmsVO = oldKakaoPhoneList.get(i);
					smsPhone = cryptoService.getDecrypt("PHONE", tempSmsVO.getPhone());
					smsPhone = StringUtil.getPhone(smsPhone);
					tempSmsVO.setPhone(smsPhone);
					kakaoPhoneList.add(tempSmsVO);
				}
			}
		} catch (Exception e) {
			logger.error("smsCampaignService.getSmsPhoneList error = " + e);
		}
		
		// 카카오 알림톡 템플릿 매핑 정보 조회
		/*
		List<SmsVO> kakaoTemplateMergeList = new ArrayList<SmsVO>();
		if ( kakaoInfo.getTempCd() != null &&  !"".equals(kakaoInfo.getTempCd())) {
			KakaoTemplateVO kakaoTemplateInfo = null;
			try {
				kakaoTemplateInfo = kakaoTemplateService.getKakaoTemplateInfo(kakaoInfo.getTempCd()); 
			} catch(Exception e) {
				logger.error("kakaoTemplateService.getKakaoTemplateInfo error = " + e);
			}
			
			String[] templateMergeItem = null;
			
			if ( kakaoTemplateInfo.getMergyItem() != null &&  !"".equals(kakaoTemplateInfo.getMergyItem())) {
				templateMergeItem = kakaoTemplateInfo.getMergyItem().split(",");
				if ( templateMergeItem != null && templateMergeItem.length > 0 ) {

					for (int i = 0 ; i < templateMergeItem.length ; i++) {
						SmsVO kakaoTemplateMerge = new SmsVO();
						kakaoTemplateMerge.setKakaoCol(templateMergeItem[i]);
						kakaoTemplateMergeList.add(kakaoTemplateMerge);
					}
					
					List<SmsVO> orgkakaoTemplateMergeList = new ArrayList<SmsVO>();
					try {
						orgkakaoTemplateMergeList = smsCampaignService.getKakaoTemplateMergeList(kakaoInfo); 
					} catch(Exception e) {
						logger.error("smsCampaignService.getKakaoTemplateMergeList error = " + e);
					}
					if ( orgkakaoTemplateMergeList != null && orgkakaoTemplateMergeList.size() > 0 ) {
						String mergeCol = "";
						String kakaoCol = "";
						for (int j= 0; j < kakaoTemplateMergeList.size(); j++) {
							kakaoCol = orgkakaoTemplateMergeList.get(j).getKakaoCols();
							mergeCol = orgkakaoTemplateMergeList.get(j).getKakaoMergeCol();
							for (int k= 0; j < kakaoTemplateMergeList.size(); k++) { 
								if(kakaoTemplateMergeList.get(k).getKakaoCol().equals(kakaoCol)) {
									kakaoTemplateMergeList.get(k).setKakaoMergeCol(mergeCol);
								}
							}
						}
					}
				}
			}
		}
		*/
		
		// 카카오 알림톡 템플릿 목록 조회
		KakaoTemplateVO search = new KakaoTemplateVO();;
		List<KakaoTemplateVO> kakaoTemplateList = null;
		search.setUilang((String) session.getAttribute("NEO_UILANG"));
		search.setSearchStartDt(null);
		search.setSearchEndDt(null);
		search.setSearchStatus("000");
		search.setStartRow(0);
		search.setRows(100000);
		
		try {
			kakaoTemplateList = kakaoTemplateService.getKakaoTemplateList(search);
		} catch (Exception e) {
			logger.error("segmentService.getKakaoTemplateList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO); 								// 검색항목
		model.addAttribute("gubunList", gubunList); 							// 카카오 알림톡 전송유형
		model.addAttribute("campList", campList); 								// 캠페인목록
		model.addAttribute("deptList", deptList); 								// 부서 목록
		model.addAttribute("userList", userList); 								// 사용자목록
		model.addAttribute("userInfo", userInfo); 								// 사용자정보
		model.addAttribute("mergeList", mergeList); 							// 수신자정보머지키코드
		model.addAttribute("statusList", statusList); 							// 발송상태
		model.addAttribute("reasonList", reasonList);							// 조회사유
		model.addAttribute("kakaoStatusList", kakaoStatusList); 				// 카카오 알림톡 상태
		model.addAttribute("segList", segList); 								// 발송대상(세그먼트) 목록
		model.addAttribute("kakaoPhoneList", kakaoPhoneList); 					// 수신자핸드폰번호리스트
		model.addAttribute("kakaoInfo", kakaoInfo); 							// 카카오 알림톡 정보
		model.addAttribute("kakaoTemplateList", kakaoTemplateList); 			// 카카오 템플릿 리스트
		//model.addAttribute("kakaoTemplateMergeList", kakaoTemplateMergeList); 	// 카카오 템플릿 머지 매핑 리스트
		
		return "sms/cam/kakaoUpdateP";
	}
	
	/**
	 * 카카오발송 정보를 등록한다. (신규등록)
	 * 
	 * @param kakaoVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/kakaoAdd")
	public String goKakaoAdd(@ModelAttribute SmsVO kakaoVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goKakaoAdd userId        = " + kakaoVO.getUserId());
		logger.debug("goKakaoAdd deptNo        = " + kakaoVO.getDeptNo());
		logger.debug("goKakaoAdd segNoc        = " + kakaoVO.getSegNoc());
		logger.debug("goKakaoAdd sendTelNo     = " + kakaoVO.getSendTelno());
		logger.debug("goKakaoAdd campNo        = " + kakaoVO.getCampNo());
		logger.debug("goKakaoAdd sendYmd       = " + kakaoVO.getSendYmd());
		logger.debug("goKakaoAdd sendHour      = " + kakaoVO.getSendHour());
		logger.debug("goKakaoAdd sendMin       = " + kakaoVO.getSendMin());
		logger.debug("goKakaoAdd smsName       = " + kakaoVO.getSmsName());
		logger.debug("goKakaoAdd taskNm        = " + kakaoVO.getTaskNm());
		logger.debug("goKakaoAdd sendNm        = " + kakaoVO.getSendNm());
		logger.debug("goKakaoAdd sendTyp       = " + kakaoVO.getSendTyp());
		logger.debug("goKakaoAdd kakaoPhones   = " + kakaoVO.getSmsPhones());
		logger.debug("goKakaoAdd kakaoMessage  = " + kakaoVO.getSmsMessage());
		logger.debug("goKakaoUpdate kakaoMergeCols = " + kakaoVO.getKakaoMergeCols());
		
		List<SmsAttachVO> kakaoAttachList = new ArrayList<SmsAttachVO>(); // 첨부파일 목록
		
		// 기본값 설정
		if (kakaoVO.getDeptNo() == 0) {
			kakaoVO.setDeptNo((int) session.getAttribute("NEO_DEPT_NO")); // 부서번호
		}
		if (StringUtil.isNull(kakaoVO.getUserId())) {
			kakaoVO.setUserId((String) session.getAttribute("NEO_USER_ID")); // 사용자아이디
		}
		
		kakaoVO.setGubun("004"); 	 // 메시지 구분 = 알림톡
		kakaoVO.setSmsStatus("000"); // 카카오 알림톡 상태 기본값 = 정상
		kakaoVO.setStatus("000");	 // 카카오 알림톡 발송상태 기본값 = 발송대기 
		
		if (!StringUtil.isNull(kakaoVO.getSegNoc()))
			kakaoVO.setSegNo(Integer.parseInt(kakaoVO.getSegNoc().substring(0, kakaoVO.getSegNoc().indexOf("|"))));// 세그먼트번호(발송대상그룹)
		
		if ("000".equals(kakaoVO.getSendTyp())) {
			kakaoVO.setSendDate(StringUtil.getDate(Code.TM_YMDHMS));
			kakaoVO.setSendYm(StringUtil.getDate(Code.TM_YM));
		} else {
			if (StringUtil.isNull(kakaoVO.getSendYmd()))
				kakaoVO.setSendYmd("0000-00-00"); // 예약시간(예약일)
			String sendHour = StringUtil.setTwoDigitsString(kakaoVO.getSendHour()); // 예약시간(시)
			String sendMin = StringUtil.setTwoDigitsString(kakaoVO.getSendMin()); // 예약시간(분)
			String sendSec = "00"; // 예약시간(초)
			kakaoVO.setSendDate(kakaoVO.getSendYmd().replaceAll("\\.", "") + sendHour + sendMin + sendSec); // 예약일시
			kakaoVO.setSendYm(kakaoVO.getSendYmd().replaceAll("\\.", "").substring(0, 6));
		}
		
		kakaoVO.setRegId((String) session.getAttribute("NEO_USER_ID"));
		kakaoVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		kakaoVO.setMsgid(kakaoVO.getCampNo() + "||");
		kakaoVO.setKeygen(StringUtil.getDate(Code.TM_YMDHMSM));
		kakaoVO.setSendTelno(cryptoService.getEncrypt("SEND_TELNO", kakaoVO.getSendTelno()));
		
		int result = 0;
		
		try {
			result = smsCampaignService.insertSmsInfo(kakaoVO, kakaoAttachList);
		} catch (Exception e) {
			logger.error("smsCampaignService.insertSmsInfo error = " + e);
		}
		
		String[] targetInfo = null;
		String[] targetKakaoMerge = null;  
		// 카카오 템플릿 머지 정보 입력 
		if (result > 0) {
			try {
				if (smsCampaignService.deleteKakaoTemplateMerge(kakaoVO) >= 0) {
					if ( kakaoVO.getKakaoMergeCols() != null &&  !"".equals(kakaoVO.getKakaoMergeCols())) {
						targetKakaoMerge = kakaoVO.getKakaoMergeCols().split(",");
						if (targetKakaoMerge !=null && targetKakaoMerge.length > 0 ) {
							try {
								for (int i = 0 ; i < targetKakaoMerge.length; i ++) {
									SmsVO targetKakaoTemplateMerge = new SmsVO();
									String strKakaoMerge = targetKakaoMerge[i];
									targetInfo = strKakaoMerge.split("\\|");
									targetKakaoTemplateMerge.setMsgid(kakaoVO.getMsgid());
									targetKakaoTemplateMerge.setKeygen(kakaoVO.getKeygen());
									targetKakaoTemplateMerge.setRegDt(kakaoVO.getRegDt());
									targetKakaoTemplateMerge.setRegId(kakaoVO.getRegId());
									targetKakaoTemplateMerge.setKakaoCol(targetInfo[0]);
									targetKakaoTemplateMerge.setKakaoMergeCol( targetInfo[1]);
									result = smsCampaignService.insertKakaoTemplateMerge(targetKakaoTemplateMerge);
								}
							} catch(Exception e) {
								logger.error("smsCampaignService.insertKakaoTemplateMerge error = " + e);
							}
						}
					}
				}
			} catch(Exception e) {
				logger.error("smsCampaignService.deleteKakaoTemplateMerge error = " + e);
			}
		}
		
		if (result > 0) {
			try {
				if (kakaoVO.getSmsPhones().length() > 1) {
					String[] arrKakaoPhone = kakaoVO.getSmsPhones().split(",");
					for (int i = 0; i < arrKakaoPhone.length; i++) {
						SmsPhoneVO smsPhoneVO = new SmsPhoneVO();
						smsPhoneVO.setKeygen(kakaoVO.getKeygen());
						smsPhoneVO.setMsgid(kakaoVO.getMsgid());
						arrKakaoPhone[i] = arrKakaoPhone[i].replace("-", "");
						smsPhoneVO.setPhone(cryptoService.getEncrypt("PHONE", arrKakaoPhone[i]));
						result = smsCampaignService.insertSmsPhone(smsPhoneVO);
					}
				}
				
			} catch (Exception e) {
				logger.error("smsCampaignService.insertSmsPhone error = " + e);
			}
		}
		
		// 결과값
		if (result > 0) {
			model.addAttribute("result", "Success");
			model.addAttribute("msgid", kakaoVO.getMsgid());
			model.addAttribute("keygen", kakaoVO.getKeygen());
			model.addAttribute("campNo", kakaoVO.getCampNo());
		} else {
			model.addAttribute("result", "Fail");
		}
		
		return "sms/cam/kakaoAdd";
	}
	
	/**
	 * 카카오 알림톡 발송 정보를 수정한다.
	 * 
	 * @param kakaoVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/kakaoUpdate")
	public ModelAndView goKakaoUpdate(@ModelAttribute SmsVO kakaoVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goKakaoUpdate msgid          = " + kakaoVO.getMsgid());
		logger.debug("goKakaoUpdate keygen         = " + kakaoVO.getKeygen());
		logger.debug("goKakaoUpdate userId         = " + kakaoVO.getUserId());
		logger.debug("goKakaoUpdate deptNo         = " + kakaoVO.getDeptNo());
		logger.debug("goKakaoUpdate segNoc         = " + kakaoVO.getSegNoc());
		logger.debug("goKakaoUpdate sendTelNo      = " + kakaoVO.getSendTelno());
		logger.debug("goKakaoUpdate campNo         = " + kakaoVO.getCampNo());
		logger.debug("goKakaoUpdate sendYmd        = " + kakaoVO.getSendYmd());
		logger.debug("goKakaoUpdate sendHour       = " + kakaoVO.getSendHour());
		logger.debug("goKakaoUpdate sendMin        = " + kakaoVO.getSendMin());
		logger.debug("goKakaoUpdate smsName        = " + kakaoVO.getSmsName());
		logger.debug("goKakaoUpdate gubun          = " + kakaoVO.getGubun());
		logger.debug("goKakaoUpdate taskNm         = " + kakaoVO.getTaskNm());
		logger.debug("goKakaoUpdate kakaoStatus    = " + kakaoVO.getSmsStatus());
		logger.debug("goKakaoUpdate status         = " + kakaoVO.getStatus());
		logger.debug("goKakaoUpadte sendNm         = " + kakaoVO.getSendNm());
		logger.debug("goKakaoUpdate sendTyp        = " + kakaoVO.getSendTyp());
		logger.debug("goKakaoUpdate kakaoPhones    = " + kakaoVO.getSmsPhones());
		logger.debug("goKakaoUpdate kakaoMessage   = " + kakaoVO.getSmsMessage()); 
		logger.debug("goKakaoUpdate kakaoMergeCols = " + kakaoVO.getKakaoMergeCols());
		
		List<SmsAttachVO> kakaoAttachList = new ArrayList<SmsAttachVO>(); // 첨부파일 목록
		kakaoVO.setGubun("004");
		// 기본값 설정
		if (kakaoVO.getDeptNo() == 0)
			kakaoVO.setDeptNo((int) session.getAttribute("NEO_DEPT_NO")); // 부서번호
		if (StringUtil.isNull(kakaoVO.getUserId()))
			kakaoVO.setUserId((String) session.getAttribute("NEO_USER_ID")); // 사용자아이디
		if (!StringUtil.isNull(kakaoVO.getSegNoc()))
			kakaoVO.setSegNo(Integer.parseInt(kakaoVO.getSegNoc().substring(0, kakaoVO.getSegNoc().indexOf("|"))));// 세그먼트번호(발송대상그룹)
		
		// 1.즉시 여부에 따른 발송시간 입력
		if ("000".equals(kakaoVO.getSendTyp())) {
			kakaoVO.setSendDate(StringUtil.getDate(Code.TM_YMDHMS));
			kakaoVO.setSendYm(StringUtil.getDate(Code.TM_YM));
		} else {
			if (StringUtil.isNull(kakaoVO.getSendYmd()))
				kakaoVO.setSendYmd("0000.00.00"); // 예약시간(예약일)
			String sendHour = StringUtil.setTwoDigitsString(kakaoVO.getSendHour()); // 예약시간(시)
			String sendMin = StringUtil.setTwoDigitsString(kakaoVO.getSendMin()); // 예약시간(분)
			String sendSec = "00"; // 예약시간(초)
			kakaoVO.setSendDate(kakaoVO.getSendYmd().replaceAll("\\.", "") + sendHour + sendMin + sendSec); // 예약일시
			kakaoVO.setSendYm(kakaoVO.getSendYmd().replaceAll("\\.", "").substring(0, 6));
		}
		// 2.컬럼 암호화 설정
		kakaoVO.setSendTelno(cryptoService.getEncrypt("SEND_TELNO", kakaoVO.getSendTelno()));
		
		int infoResult = 0;
		int result = 0;
		
		try {
			infoResult = smsCampaignService.updateSmsInfo(kakaoVO, kakaoAttachList);
		} catch (Exception e) {
			logger.error("smsCampaignService.updateSmsInfo error = " + e);
		}
		
		String[] targetInfo = null;
		String[] targetKakaoMerge = null;  
		// 카카오 템플릿 머지 정보 입력 
		if (infoResult > 0) {
			try {
				if (smsCampaignService.deleteKakaoTemplateMerge(kakaoVO) >= 0) {
					if ( kakaoVO.getKakaoMergeCols() != null &&  !"".equals(kakaoVO.getKakaoMergeCols())) {
						targetKakaoMerge = kakaoVO.getKakaoMergeCols().split(",");
						if (targetKakaoMerge !=null && targetKakaoMerge.length > 0 ) {
							try {
								for (int i = 0 ; i < targetKakaoMerge.length; i ++) {
									SmsVO targetKakaoTemplateMerge = new SmsVO();
									String strKakaoMerge = targetKakaoMerge[i];
									targetInfo = strKakaoMerge.split("\\|");
									targetKakaoTemplateMerge.setMsgid(kakaoVO.getMsgid());
									targetKakaoTemplateMerge.setKeygen(kakaoVO.getKeygen());
									targetKakaoTemplateMerge.setRegDt(kakaoVO.getRegDt());
									targetKakaoTemplateMerge.setRegId(kakaoVO.getRegId());
									targetKakaoTemplateMerge.setKakaoCol(targetInfo[0]);
									targetKakaoTemplateMerge.setKakaoMergeCol( targetInfo[1]);
									infoResult = smsCampaignService.insertKakaoTemplateMerge(targetKakaoTemplateMerge);
								}
							} catch(Exception e) {
								logger.error("smsCampaignService.insertKakaoTemplateMerge error = " + e);
							}
						}
					}
				}
			} catch(Exception e) {
				logger.error("smsCampaignService.deleteKakaoTemplateMerge error = " + e);
			}
		}
		// 추가 번호 있을 경우 입력
		if (infoResult > 0) {
			try {
				result = smsCampaignService.deleteSmsPhone(kakaoVO);
				if (result >= 0) {
					try {
						
						if (kakaoVO.getSmsPhones().length() > 1) {
							String[] arrKakaoPhone = kakaoVO.getSmsPhones().split(",");
							for (int i = 0; i < arrKakaoPhone.length; i++) {
								SmsPhoneVO smsPhoneVO = new SmsPhoneVO();
								smsPhoneVO.setKeygen(kakaoVO.getKeygen());
								smsPhoneVO.setMsgid(kakaoVO.getMsgid());
								arrKakaoPhone[i] = arrKakaoPhone[i].replace("-", "");
								smsPhoneVO.setPhone(cryptoService.getEncrypt("PHONE", arrKakaoPhone[i]));
								result = smsCampaignService.insertSmsPhone(smsPhoneVO);
							}
						}
					} catch (Exception e) {
						logger.error("smsCampaignService.insertKakaoPhone error = " + e);
					}
				}
			} catch (Exception e) {
				logger.error("smsCampaignService.insertKakaoPhone error = " + e);
			}
		}
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();

		if (infoResult > 0 && result >= 0) {
			map.put("result", "Success");
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}
	
	
	/**
	 * 카카오알림톡 복사한다.(주업무, 템플릿머지정보, 추가수신번호)
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/kakaoCopy")
	public ModelAndView copyKakaoInfo(@ModelAttribute SmsVO smsVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) { 
		logger.debug("copyKakaoInfo msgid          = " + smsVO.getMsgid());
		logger.debug("copyKakaoInfo keygen         = " + smsVO.getKeygen());
		
		int result = -1;
		String[] targetInfo;
		String[] targetSms = null;
		List<SmsVO> targetSmsVOList  = new ArrayList<SmsVO>();
		
		//조회화면에서 일괄적으로 복사 하는 경우
		if ( smsVO.getSelKeygens() != null &&  !"".equals(smsVO.getSelKeygens())) {
			targetSms = smsVO.getSelKeygens().split(",");
			
			if (targetSms !=null && targetSms.length > 0 ) {
				try {
					for (int i = 0 ; i < targetSms.length; i ++) {
						SmsVO targetSmsVO = new SmsVO();
						targetInfo = targetSms[i].split(":");
						targetSmsVO.setMsgid(targetInfo[0]);
						targetSmsVO.setKeygen(targetInfo[1]);
						targetSmsVOList.add(targetSmsVO);
					}
				} catch(Exception e) {
					logger.error("smsCampaignService.copyKakaoInfo error = " + e);
				}
			}
		} else {
			SmsVO targetSmsVO = new SmsVO();
			targetSmsVO.setMsgid(smsVO.getMsgid());
			targetSmsVO.setKeygen(smsVO.getKeygen());
			targetSmsVOList.add(targetSmsVO);
		} 
		
		try {
			if (targetSmsVOList != null && targetSmsVOList.size() > 0 ) {
				for(int i=0; i < targetSmsVOList.size(); i++) {
					result = smsCampaignService.copyKakaoInfo(targetSmsVOList.get(i));
				} 
			}
		} catch(Exception e) {
			logger.error("smsCampaignService.copyKakaoInfo error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		if(result >= 0) {
			map.put("result", "Success");
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 카카오 알림톡 정보 수정 화면을 출력한다.
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/kakaoTemplateMerge")
	public String goKakaoTemplateMerge(@ModelAttribute SmsVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goKakaoTemplateMerge msgid     = " + searchVO.getMsgid());
		logger.debug("goKakaoTemplateMerge keygen    = " + searchVO.getKeygen());
		logger.debug("goKakaoTemplateMerge tempCd    = " + searchVO.getTempCd()); 
		logger.debug("goKakaoTemplateMerge segNo     = " + searchVO.getSegNo());
		
		List<SmsVO> kakaoTemplateMergeList = new ArrayList<SmsVO>();
		
		// 카카오 알림톡 템플릿 매핑 정보 조회
		KakaoTemplateVO kakaoTemplateInfo = null;
		try {
			kakaoTemplateInfo = kakaoTemplateService.getKakaoTemplateInfo(searchVO.getTempCd()); 
		} catch(Exception e) {
			logger.error("kakaoTemplateService.getKakaoTemplateInfo error = " + e);
		}
		
		SegmentVO segmentInfo = null;
		SegmentVO searchSegmet = new SegmentVO();
		searchSegmet.setUilang("000");
		searchSegmet.setSegNo(searchVO.getSegNo());
		
		List<String> segMergeList = new ArrayList<String>();
		
		try {
			segmentInfo = segmentService.getSegmentInfo(searchSegmet);
			if (segmentInfo != null && !"".equals(segmentInfo.getMergeCol())) {
				String[] segMergeItem = null;
				segMergeItem = segmentInfo.getMergeCol().split(",");
				for (int i = 0 ; i < segMergeItem.length ; i++) {
					String segMerge = segMergeItem[i];
					segMergeList.add(segMerge);
				}
				
			}
		} catch(Exception e) {
			logger.error("kakaoTemplateService.getKakaoTemplateInfo error = " + e);
		}
		
		String[] templateMergeItem = null;
		
		if ( kakaoTemplateInfo.getMergyItem() != null &&  !"".equals(kakaoTemplateInfo.getMergyItem())) {
			templateMergeItem = kakaoTemplateInfo.getMergyItem().split(",");
			if ( templateMergeItem != null && templateMergeItem.length > 0 ) {
				for (int i = 0 ; i < templateMergeItem.length ; i++) {
					SmsVO kakaoTemplateMerge = new SmsVO();
					kakaoTemplateMerge.setKakaoCol(templateMergeItem[i]);
					kakaoTemplateMergeList.add(kakaoTemplateMerge);
				}
				
			}
		}
		
		if (searchVO.getOrgTempCd() != null && !"".equals(searchVO.getOrgTempCd()) && (searchVO.getOrgSegNo() != 0)) {
			if ((searchVO.getTempCd().equals(searchVO.getOrgTempCd()) && ( searchVO.getSegNo() == searchVO.getOrgSegNo()))) {
				List<SmsVO> orgkakaoTemplateMergeList = new ArrayList<SmsVO>();
				try {
					orgkakaoTemplateMergeList = smsCampaignService.getKakaoTemplateMergeList(searchVO); 
				} catch(Exception e) {
					logger.error("smsCampaignService.getKakaoTemplateMergeList error = " + e);
				}
				if ( orgkakaoTemplateMergeList != null && orgkakaoTemplateMergeList.size() > 0 ) {
					String mergeCol = "";
					String kakaoCol = "";
					for (int j= 0; j < orgkakaoTemplateMergeList.size(); j++) {
						if( orgkakaoTemplateMergeList.get(j) != null) {
							if ( orgkakaoTemplateMergeList.get(j).getKakaoCol() != null && !"".equals( orgkakaoTemplateMergeList.get(j).getKakaoCol() )) {
								kakaoCol = orgkakaoTemplateMergeList.get(j).getKakaoCol();
							} else {
								kakaoCol = "";
							}
							if ( orgkakaoTemplateMergeList.get(j).getKakaoMergeCol() != null && !"".equals( orgkakaoTemplateMergeList.get(j).getKakaoMergeCol() )) {
								mergeCol = orgkakaoTemplateMergeList.get(j).getKakaoMergeCol();
							}else {
								mergeCol = "";
							}
							
							for (int k= 0; k < kakaoTemplateMergeList.size(); k++) { 
								if( !"".equals(kakaoCol) && kakaoTemplateMergeList.get(k).getKakaoCol().equals(kakaoCol)) {
									kakaoTemplateMergeList.get(k).setKakaoMergeCol(mergeCol);
								}
							}
						}
					}
				}  
			}
		}
		
		model.addAttribute("segMergeList", segMergeList); 					  // 수신자그룹의 머지리스트 
		model.addAttribute("kakaoTemplateMergeList", kakaoTemplateMergeList); // 카카오 템플릿 머지 매핑 리스트
		
		return "sms/cam/kakaoTemplateMergeList";
	}
	
	
	/****************************************** SMS 발송 내부 팝업 처리 ******************************************/
	/**
	 * SMS 등록/수정 캠페인 목록(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popCampList")
	public String goPopCampList(@ModelAttribute SmsCampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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
		List<SmsCampaignVO> campList = null;
		try {
			campList = smsCampaignService.getCampaignList(searchVO);
		} catch(Exception e) {
			logger.error("smsCampaignService.getCampaignList error = " + e);
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
		
		return "sms/cam/pop/popCampList";
	}
	
	/**
	 * SMS 등록/수정 캠페인 등록(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popCampAdd")
	public String goPopCampAdd(@ModelAttribute SmsCampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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
		
		return "sms/cam/pop/popCampAdd";
	}
	
	/**
	 * SMS 등록/수정 캠페인 수정(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popCampUpdate")
	public String goPopCampUpdate(@ModelAttribute SmsCampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopCampUpdate campNo        = " + searchVO.getCampNo());
		
		// 캠페인 정보 조회
		SmsCampaignVO campInfo = null;
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		try {
			campInfo = smsCampaignService.getCampaignInfo(searchVO);
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
		
		return "sms/cam/pop/popCampUpdate";
	}
		
	/**
	 * SMS 등록/수정 수신자그룹 목록(팝업)
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
		searchVO.setSearchSmsuseYn("Y");
		
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
		
		return "sms/cam/pop/popSegList";
	}
	
	/**
	 * SMS 등록/수정 수신자그룹 등록:파일(팝업)
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
		
		return "sms/cam/pop/popSegAddFile";
	}
	
	/**
	 * SMS 등록/수정 수신자그룹 수정:파일(팝업)
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
		
		return "sms/cam/pop/popSegUpdateFile";
	}
	
	/**
	 * SMS 등록/수정 수신자그룹 등록:SQL(팝업)
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
		
		return "sms/cam/pop/popSegAddSql";
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
		
		return "sms/cam/pop/popSegUpdateSql";
	}
	/**
	 * API : smsList
	 * @param params
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return 
	 */
	@RequestMapping(value="/api/smsList")
	public void goApiSmsList(@RequestBody Map<String, Object> params, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
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
		
		SmsVO smsVO = new SmsVO();
		int deptNo = 0;
		if (json.has("deptNo")) {
			deptNo = StringUtil.setNullToInt(json.get("deptNo").toString());
			smsVO.setSearchDeptNo(deptNo);
		} else {
			sReturnValue = "Fail";
			sErrorCode = "E001";
			sErrorMessage += "[소속정보(deptNo)] ";
		}
		
		if (json.has("startDt")) {
			if ("".equals(StringUtil.setNullToString(json.get("startDt").toString()))) {
				sReturnValue = "Fail";
				sErrorCode = "E001";
				sErrorMessage += "[검색시작일(startDt)] ";
			} else {
				smsVO.setSearchStartDt(StringUtil.setNullToString(json.get("startDt").toString()));
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
				smsVO.setSearchEndDt(StringUtil.setNullToString(json.get("endDt").toString()));
			}
		} else {
			sReturnValue = "Fail";
			sErrorCode = "E001";
			sErrorMessage += "[검색종료일(endDt)] ";
		}
		
		if (json.has("status")) {
			smsVO.setSearchStatus(StringUtil.setNullToString(json.get("status").toString()));
		}
		if (json.has("taskNm")) {
			smsVO.setSearchTaskNm(StringUtil.setNullToString(json.get("taskNm").toString()));
		}
		String gubun = "";
		if (json.has("gubun")) {
			gubun = StringUtil.setNullToString(json.get("gubun").toString());
			if("004".equals(gubun)||"".equals(gubun)) {
				sReturnValue = "Fail";
				sErrorCode = "E002";
				sErrorMessage += "[전송유형(gubun)] ";
			} else {
				smsVO.setSearchGubun(gubun);
			}
		} else {
			sReturnValue = "Fail";
			sErrorCode = "E001";
			sErrorMessage += "[전송유형(gubun)] ";
		}
		
		if (json.has("smsStatus")) {
			smsVO.setSearchSmsStatus(StringUtil.setNullToString(json.get("smsStatus").toString()));
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
		
		smsVO.setPage(page);
		smsVO.setRows(rows);
		smsVO.setStartRow(( page-1) * rows);
		smsVO.setUilang("000");
		
		int resultCount = 0;
		int totalCount = 0;
		String resultData = "";
		
		if ("0000".equals(sErrorCode)) {
			
			List<SmsVO> smsList = null;
			try {
				smsList = smsCampaignService.getSmsList(smsVO);
				if(smsList == null || smsList.size() == 0) {
					sReturnValue ="Success";
					sErrorMessage= "조회된 내역이 없습니다";
					resultData = "data";
					resultCount = 0; 
				} else {
					resultCount =  smsList.size();
					String resultString = "";
					String smsMessage =""; 
					for (int m = 0; m < smsList.size(); m++)
					{
						SmsVO sObjectVO = (SmsVO) smsList.get(m);
						resultString += String.format("{ ", "");
						resultString += String.format("\"userId\":\"%s\",", StringUtil.setNullToString(sObjectVO.getUserId()));
						resultString += String.format("\"deptNo\":\"%s\",", sObjectVO.getDeptNo());
						resultString += String.format("\"deptNm\":\"%s\",", StringUtil.setNullToString(sObjectVO.getDeptNm()));
						resultString += String.format("\"segNo\":\"%s\",", sObjectVO.getSegNo());
						resultString += String.format("\"segNm\":\"%s\",", StringUtil.setNullToString(sObjectVO.getSegNm()));
						resultString += String.format("\"campNo\":\"%s\",", sObjectVO.getCampNo());
						resultString += String.format("\"campNm\":\"%s\",", StringUtil.setNullToString(sObjectVO.getCampNm()));
						resultString += String.format("\"sendDate\":\"%s\",", StringUtil.setNullToString(sObjectVO.getSendDate()));
						resultString += String.format("\"smsName\":\"%s\",", StringUtil.setNullToString(sObjectVO.getSmsName()));
						
						smsMessage = sObjectVO.getSmsMessage().replaceAll("\r", "").replaceAll("\n", "");
						resultString += String.format("\"smsMessage\":\"%s\",", smsMessage);
						resultString += String.format("\"status\":\"%s\",", StringUtil.setNullToString(sObjectVO.getStatus()));
						resultString += String.format("\"statusNm\":\"%s\",", StringUtil.setNullToString(sObjectVO.getStatusNm()));
						resultString += String.format("\"gubun\":\"%s\",", StringUtil.setNullToString(sObjectVO.getGubun()));
						resultString += String.format("\"gubunNm\":\"%s\",", StringUtil.setNullToString(sObjectVO.getGubunNm()));
						resultString += String.format("\"smsStatus\":\"%s\",", StringUtil.setNullToString(sObjectVO.getSmsStatus()));
						resultString += String.format("\"smsStatusNm\":\"%s\"", StringUtil.setNullToString(sObjectVO.getSmsStatusNm()));
						resultString += String.format("},", "");
					
						totalCount = sObjectVO.getTotalCount();
					}
					resultData = String.format("%s", (resultCount > 0 ? resultString.trim().substring(0, resultString.length() - 1) : ""));
				}
			} catch (Exception e) {
				logger.error("smsCampaignService.getSmsList error = " + e);
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
	 * API : sendSmsList (파일처리)
	 * @param params
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @apiNote This Api, for  Huge(over 10,000) items 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/api/sendSmsList")
	public void goApiSendSmsList(@RequestBody Map<String, Object> params, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("===========sendSmsList=====================");
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
		
		SmsStgVO smsStgHeader = new SmsStgVO();
		String requestkey = ""; 
		String templatecode = ""; 
		String senderphonenumber = ""; 
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
						smsStgHeader.setRequestkey(requestkey);
		
						try {
							int requestKeyCount =  smsCampaignService.getCountRequestKey(requestkey);
							if (requestKeyCount > 0 ) {
								sResultmessage = "[ 이미 사용된 키값임 (" + requestkey +")]";
								sResultcode = "E007";
							}
						} catch (Exception e) {
							logger.error("kakaoTemplateService.getKakaoTemplateInfo error = " + e);
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
		
		//2023-04-14 센터키 추가
		if(json.has("bizclient.sender_key ")) {
			requestoption = StringUtil.setNullToString(json.get("bizclient.sender_key ").toString());
			if("".equals(requestoption)) {
				sResultmessage += "[ 발송 센더 키 누락 (bizclient.sender_key )]";
				sResultcode = "E001";  
			} 
		} else {
			sResultmessage += "[ 발송 센더 키  누락 (bizclient.sender_key )]";
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
		
		if(json.has("templatecode")) {
			templatecode = StringUtil.setNullToString(json.get("templatecode").toString());
			if("".equals(templatecode)) {
				sResultmessage += "[ 템플릿코드 누락 (templatecode)]";
				sResultcode = "E001";  
			}else {
				smsStgHeader.setTemplatecode(templatecode);
			}
		} else {
			sResultmessage += "[ 템플릿코드 누락 (templatecode)]";
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
			}else {
				smsStgHeader.setSenderphonenumber(senderphonenumber);
			}
		} else {
			sResultmessage += "[ 발신자번호 누락 (senderphonenumber)]";
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
			}else {
				smsStgHeader.setPagenumber(json.get("pagenumber").toString());
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
			}else {
				smsStgHeader.setTotalpagenumber(json.get("totalpagenumber").toString());
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
			}else {
				smsStgHeader.setPagesize(json.get("pagesize").toString());
			}
		} else {
			sResultmessage += "[ 데이터건수 (pagesize)]";
			sResultcode = "E001";
		}
		
		if ("000".equals(sResultcode)) {
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
				
				if ("000".equals(sResultcode)) {
					try {
						SmsCampaignVO campaignVO = new SmsCampaignVO();
						campaignVO.setCampNo(campaignno);
						campaignVO.setUilang("000");
						SmsCampaignVO campaignInfo =  smsCampaignService.getCampaignInfo(campaignVO);
						if (campaignInfo == null) {
							sResultmessage = "[ 등록되지 않은 캠페인 번호입니다 (" + campaignno +")]";
							sResultcode = "E010";
						}
					} catch (Exception e) {
						logger.error("campaignService.getCampaignInfo error = " + e);
						sResultmessage = "[ 등록되지 않은 캠페인 번호입니다 (" + campaignno +")]";
						sResultcode = "E010";
					}
				}
			} catch (Exception e) {
				logger.error("codeService.getSmsTemplate error = " + e);
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
				logger.error("SmsCampaignController.goApiSendSmsList Send Return error = " + e);
			} 
		} else {
			//파일로 만든다 ..
			//디렉토리있는지 확인
			String basePath = properties.getProperty("FILE.UPLOAD_PATH") + "/api/sms";
			String filePath = basePath + "/" + requestkey + "_" + pagenumber + "_"+ totalpagenumber + ".tmp";
			logger.debug("goApiSendSmsList filePath = " + filePath);
			
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
				logger.error("goApiSendSmsList file write error = " + e);
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
				logger.error("SmsCampaignController.goApiSendSmsList Send Return error = " + e);
			}
		}
	}
	
	
	/**
	 * API : kakaoList 
	 * @param params
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return 
	 */
	@RequestMapping(value="/api/kakaoList")
	public void goApiKakaoList(@RequestBody Map<String, Object> params, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
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
		
		SmsVO kakaoVO = new SmsVO();
		int deptNo = 0;
		if (json.has("deptNo")) {
			deptNo = StringUtil.setNullToInt(json.get("deptNo").toString());
			kakaoVO.setSearchDeptNo(deptNo);
		} else {
			sReturnValue = "Fail";
			sErrorCode = "E001";
			sErrorMessage += "[소속정보(deptNo)] ";
		}
		
		if (json.has("startDt")) {
			if ("".equals(StringUtil.setNullToString(json.get("startDt").toString()))) {
				sReturnValue = "Fail";
				sErrorCode = "E001";
				sErrorMessage += "[검색시작일(startDt)] ";
			} else {
				kakaoVO.setSearchStartDt(StringUtil.setNullToString(json.get("startDt").toString()));
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
				kakaoVO.setSearchEndDt(StringUtil.setNullToString(json.get("endDt").toString()));
			}
		} else {
			sReturnValue = "Fail";
			sErrorCode = "E001";
			sErrorMessage += "[검색종료일(endDt)] ";
		}
		
		if (json.has("status")) {
			kakaoVO.setSearchStatus(StringUtil.setNullToString(json.get("status").toString()));
		}
		
		String gubun = "";
		if (json.has("gubun")) {
			gubun = StringUtil.setNullToString(json.get("gubun").toString());
			if(!"004".equals(gubun) || "".equals(gubun)) {
				sReturnValue = "Fail";
				sErrorCode = "E002";
				sErrorMessage += "[전송유형(gubun)] ";
			} else {
				kakaoVO.setSearchGubun(gubun);
			}
		} else {
			sReturnValue = "Fail";
			sErrorCode = "E001";
			sErrorMessage += "[전송유형(gubun)] ";
		}
		
		if (json.has("smsStatus")) {
			kakaoVO.setSearchSmsStatus(StringUtil.setNullToString(json.get("smsStatus").toString()));
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
		
		kakaoVO.setPage(page);
		kakaoVO.setRows(rows);
		kakaoVO.setStartRow(( page-1) * rows);
		kakaoVO.setUilang("000");
		
		int resultCount = 0;
		int totalCount = 0;
		String resultData = "";
		
		if ("0000".equals(sErrorCode)) {
			
			List<SmsVO> kakaoList = null;
			try {
				kakaoList = smsCampaignService.getSmsList(kakaoVO);
				if(kakaoList == null || kakaoList.size() == 0) {
					sReturnValue ="Success";
					sErrorMessage= "조회된 내역이 없습니다";
					resultData = "data";
					resultCount = 0; 
				} else {
					resultCount =  kakaoList.size();
					String resultString = "";
					String kakaoMessage =""; 
					for (int m = 0; m < kakaoList.size(); m++)
					{
						SmsVO sObjectVO = (SmsVO) kakaoList.get(m);
						resultString += String.format("{ ", "");
						resultString += String.format("\"userId\":\"%s\",", StringUtil.setNullToString(sObjectVO.getUserId()));
						resultString += String.format("\"deptNo\":\"%s\",", sObjectVO.getDeptNo());
						resultString += String.format("\"deptNm\":\"%s\",", StringUtil.setNullToString(sObjectVO.getDeptNm()));
						resultString += String.format("\"segNo\":\"%s\",", sObjectVO.getSegNo());
						resultString += String.format("\"segNm\":\"%s\",", StringUtil.setNullToString(sObjectVO.getSegNm()));
						resultString += String.format("\"campNo\":\"%s\",", sObjectVO.getCampNo());
						resultString += String.format("\"campNm\":\"%s\",", StringUtil.setNullToString(sObjectVO.getCampNm()));
						resultString += String.format("\"sendDate\":\"%s\",", StringUtil.setNullToString(sObjectVO.getSendDate()));
						resultString += String.format("\"smsName\":\"%s\",", StringUtil.setNullToString(sObjectVO.getSmsName()));
						
						kakaoMessage = sObjectVO.getSmsMessage().replaceAll("\r", "").replaceAll("\n", "");
						resultString += String.format("\"kakaoMessage\":\"%s\",", kakaoMessage);
						resultString += String.format("\"status\":\"%s\",", StringUtil.setNullToString(sObjectVO.getStatus()));
						resultString += String.format("\"statusNm\":\"%s\",", StringUtil.setNullToString(sObjectVO.getStatusNm()));
						resultString += String.format("\"gubun\":\"%s\",", StringUtil.setNullToString(sObjectVO.getGubun()));
						resultString += String.format("\"gubunNm\":\"%s\",", StringUtil.setNullToString(sObjectVO.getGubunNm()));
						resultString += String.format("\"smsStatus\":\"%s\",", StringUtil.setNullToString(sObjectVO.getSmsStatus()));
						resultString += String.format("\"smsStatusNm\":\"%s\"", StringUtil.setNullToString(sObjectVO.getSmsStatusNm()));
						resultString += String.format("},", "");
					
						totalCount = sObjectVO.getTotalCount();
					}
					resultData = String.format("%s", (resultCount > 0 ? resultString.trim().substring(0, resultString.length() - 1) : ""));
				}
			} catch (Exception e) {
				logger.error("smsCampaignService.getSmsList error = " + e);
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
	 * API : SendKakao
	 * @param params
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @apiNote This Api, cannot use a merge item 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/api/sendKakao")
	public void goApiSendKakao(@RequestBody Map<String, Object> params, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
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
		SmsVO kakaoVO = new SmsVO();
		String gubun = "";
		if(json.has("gubun")) {
			gubun = StringUtil.setNullToString(json.get("gubun").toString());
			if(!"004".equals(gubun)) {
				sReturnValue = "Fail";
				sErrorCode = "E002";
				sErrorMessage += "[ 전송유형 (gubun)] ";
			}else {
				kakaoVO.setGubun(gubun);
			}
		} else {
			sReturnValue = "Fail";
			sErrorCode = "E001";
			sErrorMessage += "[ 전송유형 (gubun)] ";
		}
		
		int deptNo = 0; 
		if(json.has("deptNo")) { // deptNo 가 존재하지 않을 시 
			deptNo = StringUtil.setNullToInt(json.get("deptNo").toString());
			if(deptNo == 0 ) {
				sReturnValue = "Fail";
				sErrorCode = "E001";
				sErrorMessage += "[소속정보(deptNo)] ";
			} else {
				kakaoVO.setDeptNo(deptNo);
			}
		} else {
			sReturnValue = "Fail";
			sErrorCode = "E001";
			sErrorMessage += "[소속정보(deptNo)] ";
		}
		
		String userId= "";
		if(json.has("userId")) {
			if ("".equals(StringUtil.setNullToString(json.get("userId").toString()))) {
				sReturnValue = "Fail";
				sErrorCode = "E001";
				sErrorMessage += "[사용자정보(userId)] ";
			} else {
				userId = StringUtil.setNullToString(json.get("userId").toString());
				kakaoVO.setUserId(userId);
			}
		} else {
			sReturnValue = "Fail";
			sErrorCode = "E001";
			sErrorMessage += "[사용자정보(userId)] ";
		}
		
		if(json.has("segNo")) {
			kakaoVO.setSegNo(StringUtil.setNullToInt(json.get("segNo").toString()));
		} else {
			sReturnValue = "Fail";
			sErrorCode = "E001";
			sErrorMessage += "[수신자그룹번호(segNo)]";
		}
		
		if(json.has("campNo")) {
			kakaoVO.setCampNo(StringUtil.setNullToInt(json.get("campNo").toString()));
		} else {
			sReturnValue = "Fail";
			sErrorCode = "E001";
			sErrorMessage += "[캠페인번호(campNo)]";
		}
		
		kakaoVO.setMsgid(kakaoVO.getCampNo()+"||");
		kakaoVO.setKeygen(StringUtil.getDate(Code.TM_YMDHMS));
		
		String sendTelno = "";
		if(json.has("sendTelno")) {
			sendTelno = StringUtil.setNullToString(json.get("sendTelno").toString());
			kakaoVO.setSendTelno(cryptoService.getEncrypt("SEND_TELNO",sendTelno));
		}
		
		String kakaoMessageHead = "";
		String kakaoMessageTail = "";
		String kakaoMessage ="";
		String legalYn = "";
		
		if(json.has("legalYn")) {
			legalYn = StringUtil.setNullToString(json.get("legalYn").toString());
			if(!"".equals(legalYn) && "Y".equals(legalYn)) {
				kakaoMessageHead = "(광고)";
				if(json.has("legalCf")) {
					kakaoVO.setLegalCf(StringUtil.setNullToString(json.get("legalCf").toString()));
					kakaoMessageTail = "무료수신거부 " + kakaoVO.getLegalCf() ;
				}
			}
		}
		
		if(json.has("smsName")) {
			kakaoVO.setSmsName(StringUtil.setNullToString(json.get("smsName").toString()));
		}
		if(json.has("smsMessage")) {
			kakaoMessage = StringUtil.setNullToString(json.get("smsMessage").toString());
		}
		
		kakaoMessageHead = kakaoMessageHead + kakaoVO.getSmsName(); 
		kakaoMessage = kakaoMessageHead + "\n" + kakaoMessage;
		if(!"".equals(kakaoMessageTail)) {
			kakaoMessage = kakaoMessageHead + "\n" + kakaoMessage + "\n" + kakaoMessageTail;
		}
		
		kakaoVO.setSmsMessage(kakaoMessage);
		logger.debug("kakaoMessage [" + kakaoMessage + "]");
		
		if(json.has("taskNm")) {
			kakaoVO.setTaskNm(StringUtil.setNullToString(json.get("taskNm").toString()));
		} else {
			sReturnValue = "Fail";
			sErrorCode = "E001";
			sErrorMessage += "[알림톡명(taskNm)]";
			
		}
		if(json.has("sendNm")) {
			kakaoVO.setSendNm(StringUtil.setNullToString(json.get("sendNm").toString()));
		}
		if(json.has("tempCd")) {
			kakaoVO.setTempCd(StringUtil.setNullToString(json.get("tempCd").toString()));
		}
		if(json.has("validYn")) {
			String validYn = StringUtil.setNullToString(json.get("validYn").toString());
			if(!"".equals(validYn) && "Y".equals(validYn)) {
				kakaoVO.setValidYn(validYn);
			} else {
				kakaoVO.setValidYn("N");
			}
		} else {
			kakaoVO.setValidYn("N");
		}
		
		if(json.has("sendTyp")) {
			if(StringUtil.isNull(json.get("sendTyp").toString())){
				kakaoVO.setSendTyp("000");
			} else {
				kakaoVO.setSendTyp(json.get("sendTyp").toString());
			}
		}
		
		if ("000".equals(kakaoVO.getSendTyp())) {
			kakaoVO.setSendDate(StringUtil.getDate(Code.TM_YMDHMS));
			kakaoVO.setSendYm(StringUtil.getDate(Code.TM_YM));
		} else {
			String sendDate = "";
			if(json.has("sendDate")) {
				sendDate = json.get("sendDate").toString();
				if(StringUtil.isNull(sendDate)) {
					sReturnValue = "Fail";
					sErrorCode = "E001";
					sErrorMessage += "[발송일시(sendDate)]";
				} else {
					kakaoVO.setSendDate(sendDate);	// 예약일시
					kakaoVO.setSendYm(sendDate.substring(0,6));	
				}
			} else {
				sReturnValue = "Fail";
				sErrorCode = "E001";
				sErrorMessage += "[발송일시(sendDate)]";
			}
		}
		kakaoVO.setSmsStatus("000");	//문자상태 기본값  정상 
		kakaoVO.setStatus("001");		//문자발송상태 기본값 발송승인 
		kakaoVO.setRegId(userId);
		kakaoVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		List<SmsPhoneVO> kakaoPhoneList = new ArrayList<SmsPhoneVO>();
		ArrayList<Map<String, Object>> kakaoArrList = (ArrayList) json.get("phoneList");
		if(kakaoArrList.size() > 0) {
			for (int i = 0 ; i <kakaoArrList.size(); i++ ) {
				SmsPhoneVO addKakaoPhone = new SmsPhoneVO(); 
				
				for(Map.Entry<String, Object> entry : kakaoArrList.get(i).entrySet()) {
					String addKey = entry.getKey();
					String addValue = entry.getValue().toString();
					if(addKey.equals("phone")) {
						addKakaoPhone.setPhone(cryptoService.getDecrypt("PHONE",addValue));
					}
				}
				kakaoPhoneList.add(addKakaoPhone);
			}
		}
		
		List<SmsAttachVO> kakaoAttachList = new ArrayList<SmsAttachVO>();		// 첨부파일 목록
		
		if ("0000".equals(sErrorCode)) {
			int result = 0; 
			try {
				result = smsCampaignService.insertSmsInfo(kakaoVO, kakaoAttachList);
			} catch (Exception e) {
				logger.error("smsCampaignService.insertSmsInfo error = " + e);
				sErrorMessage = e.getMessage();
			}
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
	
	/**
	 * API : sendKakaoList (FILE 처리) 
	 * @param params
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @apiNote This Api, for  Huge(over 10,000) items 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/api/sendKakaoList")
	public void goApiSendKakaoList(@RequestBody Map<String, Object> params, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("===========sendKakaoList=====================");
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
		String senderKey ="";
		String templatecode = ""; 
		String senderphonenumber = "";
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
							int requestKeyCount =  smsCampaignService.getCountRequestKey(requestkey);
							if (requestKeyCount > 0 ) {
								sResultmessage = "[ 이미 사용된 키값임 (" + requestkey +")]";
								sResultcode = "E007";
							}
						} catch (Exception e) {
							logger.error("kakaoTemplateService.getKakaoTemplateInfo error = " + e);
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
		
		if(json.has("senderkey")) {
			senderKey = StringUtil.setNullToString(json.get("senderkey").toString());
			if("".equals(senderKey)) {
				sResultmessage += "[ 발송키 누락 (senderkey)]";
				sResultcode = "E001";
			} else {
				if(senderKey.contains("_")){
					sResultmessage += "[ 발송키 사용불가문자(_) 포함 (senderkey)]";
					sResultcode = "E011";
				} else {
					 
					try {
						
						SenderKeyVO senderKeyVO = new SenderKeyVO();
						senderKeyVO.setSenderKey(senderKey);
						senderKeyVO.setUseYn("Y");
						senderKeyVO.setDelYn("N");
						
						int senderKeyCount =  userCodeService.getSenderKeyExist(senderKeyVO);
						if (senderKeyCount < 1 ) {
							sResultmessage = "[ 등록되지 않은 발송키입니다 (" + senderKey +")]";
							sResultcode = "E011";
						}
					} catch (Exception e) {
						logger.error("userCodeService.getSenderKeyExist error = " + e);
						sResultmessage = "[ 발송 키 조회 오류 (" + senderKey +")]";
						sResultcode = "E006";
					} 
				}
			}
		} else {
			sResultcode = "E001";
			sResultmessage += "[ 발송키 누락 (senderKey)]";
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
		
		if(json.has("sendduedatatime")) {
			sendduedatatime = StringUtil.setNullToString(json.get("sendduedatatime").toString());
			if("".equals(sendduedatatime)) {
				sResultmessage += "[ 발송 예정 일시 (sendduedatatime)]";
				sResultcode = "E001";  
			} 
			if (!StringUtil.isValidDateString(json.get("sendduedatatime").toString())) {
				sResultmessage += "[ 발송 예정 일시 데이터 오류(sendduedatatime)]";
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
		
		if ("000".equals(sResultcode)) {
			try {
				KakaoTemplateVO kakaoTemplateVO = kakaoTemplateService.getKakaoTemplateInfo(templatecode);
				if (kakaoTemplateVO == null) {
					sResultmessage = "[ 템플릿정보 없음 (" + templatecode +")]";
					sResultcode = "E003";
				} else {
					if ("".equals(kakaoTemplateVO.getTempContent())) {
						sResultmessage = "[ 템플릿정보 없음 (" + templatecode +")]";
						sResultcode = "E003";
					}
				}
				
				if ("000".equals(sResultcode)) {
					try {
						SmsCampaignVO campaignVO = new SmsCampaignVO();
						campaignVO.setCampNo(campaignno);
						campaignVO.setUilang("000");
						SmsCampaignVO campaignInfo =  smsCampaignService.getCampaignInfo(campaignVO);
						if (campaignInfo == null) {
							sResultmessage = "[ 등록되지 않은 캠페인 번호입니다 (" + campaignno +")]";
							sResultcode = "E010";
						}
					} catch (Exception e) {
						logger.error("campaignService.getCampaignInfo error = " + e);
						sResultmessage = "[ 등록되지 않은 캠페인 번호입니다 (" + campaignno +")]";
						sResultcode = "E010";
					}
				}
				
			} catch (Exception e) {
				logger.error("kakaoTemplateService.getKakaoTemplateInfo error = " + e);
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
				logger.error("SmsCampaignController.goApiSendKakaoList Send Return error = " + e);
			} 
		} else { 
			String basePath = properties.getProperty("FILE.UPLOAD_PATH") + "/api/kakao";
			String filePath = basePath + "/" + requestkey + "_" + pagenumber + "_"+ totalpagenumber + ".tmp";
			logger.debug("goApiSendKakaoList filePath = " + filePath);
			 
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
				logger.error("goApiSendKakaoList file write error = " + e);
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
				logger.error("SmsCampaignController.goApiSendKakaoList Send Return error = " + e);
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
		
		SmsCampaignVO smsCampVO = new SmsCampaignVO();
		
		int deptNo = 0;
		if (json.has("deptNo")) {
			deptNo = StringUtil.setNullToInt(json.get("deptNo").toString());
			smsCampVO.setSearchDeptNo(deptNo);
		} else {
			smsCampVO.setSearchDeptNo(deptNo);
		}
		
		if (json.has("startDt")) {
			if ("".equals(StringUtil.setNullToString(json.get("startDt").toString()))) {
				sReturnValue = "Fail";
				sErrorCode = "E001";
				sErrorMessage += "[검색시작일(startDt)] ";
			} else {
				smsCampVO.setSearchStartDt(StringUtil.setNullToString(json.get("startDt").toString()));
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
				smsCampVO.setSearchEndDt(StringUtil.setNullToString(json.get("endDt").toString()));
			}
		} else {
			sReturnValue = "Fail";
			sErrorCode = "E001";
			sErrorMessage += "[검색종료일(endDt)] ";
		}
		
		if (json.has("status")) {
			smsCampVO.setSearchStatus(StringUtil.setNullToString(json.get("status").toString()));
		}
		
		if (json.has("campTy")) {
			smsCampVO.setSearchCampTy(StringUtil.setNullToString(json.get("campTy").toString()));
		}
		
		if (json.has("campNm")) {
			smsCampVO.setSearchCampNm(StringUtil.setNullToString(json.get("campNm").toString()));
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
		
		smsCampVO.setPage(page);
		smsCampVO.setRows(rows);
		smsCampVO.setStartRow(( page-1) * rows);
		smsCampVO.setUilang("000");
		
		int resultCount = 0;
		int totalCount = 0;
		String resultData = "";
		
		if ("0000".equals(sErrorCode)) {
			
			List<SmsCampaignVO> campaignList = null;
			try {
				campaignList = smsCampaignService.getCampaignList(smsCampVO);
				
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
						SmsCampaignVO sObjectVO = (SmsCampaignVO) campaignList.get(m);
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
						resultString += String.format("\"campSmsCnt\":\"%s\"", sObjectVO.getCampSmsCnt());
						
						resultString += String.format("},", "");
						
						totalCount = sObjectVO.getTotalCount();
					}
					resultData = String.format("%s", (resultCount > 0 ? resultString.trim().substring(0, resultString.length() - 1) : ""));
				}
			} catch (Exception e) {
				logger.error("smsCampaignService.getCampaignList error = " + e);
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
