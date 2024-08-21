/**
 * 작성자 : 김상진
 * 작성일시 : 2021.08.23
 * 설명 : 메일발송결재 Controller
 */
package kr.co.sict.ums.ems.apr.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
import kr.co.sict.ums.ems.apr.service.SecuApprovalLineService;
import kr.co.sict.ums.ems.apr.vo.SecuApprovalLineVO;
import kr.co.sict.ums.ems.cam.vo.AttachVO;
import kr.co.sict.ums.ems.cam.vo.CampaignVO;
import kr.co.sict.ums.ems.cam.vo.TaskVO;
import kr.co.sict.ums.ems.sch.vo.ScheduleVO;
import kr.co.sict.ums.rns.svc.service.RnsServiceService;
import kr.co.sict.ums.rns.svc.vo.RnsAttachVO;
import kr.co.sict.ums.rns.svc.vo.RnsSecuApprovalLineVO;
import kr.co.sict.ums.rns.svc.vo.RnsServiceVO;
import kr.co.sict.ums.sys.log.service.SystemLogService;
import kr.co.sict.ums.sys.log.vo.ActionLogVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.PageUtil;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Controller
@RequestMapping("/ems/apr")
public class SecuApprovalLineController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private SecuApprovalLineService approvalLineService;
	
	@Autowired
	private RnsServiceService rnsServiceService;
	
	@Autowired
	private SystemLogService logService;
	
	@Autowired
	private PropertiesUtil properties;
	
	/**
	 * 메일발송결재 목록 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailAprListP")
	public String getMailAprListP(@ModelAttribute SecuApprovalLineVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getMailAprListP searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("getMailAprListP searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("getMailAprListP searchTaskNm     = " + searchVO.getSearchTaskNm());
		logger.debug("getMailAprListP searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("getMailAprListP searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("getMailAprListP searchUserId     = " + searchVO.getSearchUserId());
		logger.debug("getMailAprListP searchWorkStatus = " + searchVO.getSearchWorkStatus());
		logger.debug("getMailAprListP searchAprDeptNo  = " + searchVO.getSearchAprDeptNo());
		logger.debug("getMailAprListP searchAprUserId  = " + searchVO.getSearchAprUserId());
		logger.debug("getMailAprListP topNotiYn        = " + searchVO.getTopNotiYn());
		
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
		if(searchVO.getSearchAprDeptNo() == 0) {
			if(!"Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchAprDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		if(StringUtil.isNull(searchVO.getTopNotiYn())) searchVO.setTopNotiYn("N");	// 상단 알림에서 넘어오는 경우 체크
		
		// 캠페인 목록
		CampaignVO campVO = new CampaignVO();
		campVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		campVO.setStatus("000");
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
		userVO.setDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN")) ? 0 : (int)session.getAttribute("NEO_DEPT_NO"));
		userVO.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch(Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		// 발송상태 목록
		CodeVO workStatusVO = new CodeVO();
		workStatusVO.setUilang("000");
		workStatusVO.setCdGrp("C101");
		workStatusVO.setUseYn("Y");
		List<CodeVO> orgWorkStatusList = null;
		List<CodeVO> workStatusList = new ArrayList<CodeVO>(); 
		
		try {
			orgWorkStatusList = codeService.getWorkStatusList(workStatusVO);
			for (int i= 0 ; i < orgWorkStatusList.size() ; i ++) {
				if (!"000".equals(orgWorkStatusList.get(i).getCd()) && !"201".equals(orgWorkStatusList.get(i).getCd())) {
					CodeVO addWorkStatusVO = new CodeVO();
					addWorkStatusVO.setCd( orgWorkStatusList.get(i).getCd());
					addWorkStatusVO.setCdNm(orgWorkStatusList.get(i).getCdNm());
					workStatusList.add(addWorkStatusVO);
				}
			 
			}
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
		model.addAttribute("workStatusList", workStatusList);	// 발송상태목록
		model.addAttribute("reasonList", reasonList);			// 조회사유코드목록
		
		return "ems/apr/mailAprListP";
	}
	
	/**
	 * 메일발송결재 목록을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailAprList")
	public String getMailAprList(@ModelAttribute SecuApprovalLineVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getMailAprListP searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("getMailAprListP searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("getMailAprListP searchTaskNm     = " + searchVO.getSearchTaskNm());
		logger.debug("getMailAprListP searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("getMailAprListP searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("getMailAprListP searchUserId     = " + searchVO.getSearchUserId());
		logger.debug("getMailAprListP searchWorkStatus = " + searchVO.getSearchWorkStatus());
		logger.debug("getMailAprListP searchAprDeptNo  = " + searchVO.getSearchAprDeptNo());
		logger.debug("getMailAprListP searchAprUserId  = " + searchVO.getSearchAprUserId());
		logger.debug("getMailAprListP searchSvcType    = " + searchVO.getSearchSvcType());
		
		// 검색 기본값 설정
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		searchVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		searchVO.setRegId((String)session.getAttribute("NEO_USER_ID"));
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));

		//메일구분에 따른 정보 추가  
		if ( !StringUtil.isNull(searchVO.getSearchSvcType()) && !"".equals(searchVO.getSearchSvcType()) ) {
			if ("00".equals(searchVO.getSearchSvcType())) {
				searchVO.setSearchSendRepeat("000");
				searchVO.setSearchSvcType("10");
			} else if ("10".equals(searchVO.getSearchSvcType())) {
				searchVO.setSearchSendRepeat("001");
				searchVO.setSearchSvcType("10");
			} else if ("20".equals(searchVO.getSearchSvcType())) {
				searchVO.setSearchSvcType("20");
			} else {
			}
		} else {
			searchVO.setSearchSendRepeat("");
			searchVO.setSearchSvcType("");
		}
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		// 메일발송결재 목록
		List<SecuApprovalLineVO> aprMailList = null;
		try {
			aprMailList = approvalLineService.getApprovalLineList(searchVO);
		} catch(Exception e) {
			logger.error("approvalLineService.getApprovalLineList error = " + e);
		}
		if(aprMailList != null && aprMailList.size() > 0) {
			totalCount = aprMailList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("searchVO", searchVO);				// 검색항목
		model.addAttribute("aprMailList", aprMailList);			// 메일발송결재목록
		model.addAttribute("pageUtil", pageUtil);				// 페이징
		
		return "ems/apr/mailAprList";
	}
	
	/**
	 * 메일발송결재 수정 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailAprUpdateP")
	public String goMailAprUpdateP(@ModelAttribute SecuApprovalLineVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailAprUpdateP taskNo    = " + searchVO.getTaskNo());
		logger.debug("goMailAprUpdateP subTaskNo = " + searchVO.getSubTaskNo());
		
		// 메일정보 조회
		SecuApprovalLineVO mailInfo = null;
		try {
			mailInfo = approvalLineService.getMailInfo(searchVO);
		} catch(Exception e) {
			logger.error("approvalLineService.getMailInfo error = " + e);
		}
		
		// 첨부파일목록 조회
		List<AttachVO> attachList = null;
		try {
			attachList = approvalLineService.getAttachList(searchVO.getTaskNo());
		} catch(Exception e) {
			logger.error("approvalLineService.getAttachList error = " + e);
		}
		
		// 메일별결재목록 조회
		List<SecuApprovalLineVO> apprLineList = null;
		String apprRegId = "";
		try {
			apprLineList = approvalLineService.getMailApprLineList(searchVO.getTaskNo());
			if(apprLineList != null && apprLineList.size() > 0) {
				apprRegId = apprLineList.get(0).getRegId();
				mailInfo.setRegId(apprRegId);
			}
		} catch(Exception e) {
			logger.error("approvalLineService.getMailApprLineList error = " + e);
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
		
		// 결재반려사유코드 조회
		CodeVO rejectVO = new CodeVO();
		rejectVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		rejectVO.setCdGrp("C104");
		rejectVO.setUseYn("Y");
		List<CodeVO> rejectList = null;
		try {
			rejectList = codeService.getCodeList(rejectVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C102] error = " + e);
		}
		
		String mailContent = getContFileText(mailInfo.getContFlPath(), true);
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("mailInfo", mailInfo);			// 메일정보
		model.addAttribute("attachList", attachList);		// 첨부파일목록
		model.addAttribute("apprLineList", apprLineList);	// 메일별결재목록
		model.addAttribute("mailContent", mailContent);		// 메일내용
		model.addAttribute("reasonList", reasonList);		// 조회사유코드
		model.addAttribute("rejectList", rejectList);		// 결재반려사유코드
		
		return "ems/apr/mailAprUpdateP";
	}

	/**
	 * 메일발송결재 결재라인을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailAprStepListP")
	public String mailAprStepListP(@ModelAttribute SecuApprovalLineVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("mailAprStepListP taskNo    = " + searchVO.getTaskNo());
		
		// 메일별결재목록 조회
		List<SecuApprovalLineVO> apprLineList = null;
		try {
			apprLineList = approvalLineService.getMailApprLineList(searchVO.getTaskNo());
		} catch(Exception e) {
			logger.error("approvalLineService.getMailApprLineList error = " + e);
		}
		
		model.addAttribute("apprLineList", apprLineList);	// 메일별결재목록
		
		return "ems/apr/mailAprStepListP";
	}
	
	/**
	 * 메일발송결재 결재라인을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailAprStepUpdate")
	public ModelAndView mailAprStepUpdate(@ModelAttribute SecuApprovalLineVO apprVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("mailAprStepUpdate taskNo      = " + apprVO.getTaskNo());
		logger.debug("mailAprStepUpdate subTaskNo   = " + apprVO.getSubTaskNo());
		logger.debug("mailAprStepUpdate apprStep    = " + apprVO.getApprStep());
		logger.debug("mailAprStepUpdate rsltCd      = " + apprVO.getRsltCd());
		logger.debug("mailAprStepUpdate rejectCd    = " + apprVO.getRejectCd());
		logger.debug("mailAprStepUpdate totalCouunt = " + apprVO.getTotalCount());
		
		apprVO.setUpId((String)session.getAttribute("NEO_USER_ID"));
		apprVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		apprVO.setApprDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		int result = 0;
		try {
			result = approvalLineService.updateMailAprStep(apprVO);
			
			// 결재로그
			ActionLogVO logVO = new ActionLogVO();
			if("002".equals(apprVO.getRsltCd())) { // 승인
				logVO.setStatusGb("Success");
				logVO.setContentType("004");	// 결재승인
				logVO.setContent("M1003004");
				logVO.setContentPath(request.getRequestURI());
				logVO.setContentRslt("002");	// 승인
				logVO.setMessage("000(정상결재)");
				logVO.setExtrYn("N");
				logVO.setRecCnt(0);
				logVO.setMobilYn("N");
			} else if("003".equals(apprVO.getRsltCd())) {	// 반려
				logVO.setStatusGb("Success");
				logVO.setContentType("004");	// 결재승인
				logVO.setContent("M1003004");
				logVO.setContentPath(request.getRequestURI());
				logVO.setContentRslt("003");	// 반려
				logVO.setMessage(apprVO.getRejectCd() + "(" + approvalLineService.getRejectNm(apprVO.getRejectCd()) + ")");
				logVO.setExtrYn("N");
				logVO.setRecCnt(0);
				logVO.setMobilYn("N");
			}
			logService.insertActionLog(request, session, logVO);
		} catch(Exception e) {
			logger.error("approvalLineService.updateMailAprStep error = " + e);
			
			// 결재로그
			ActionLogVO logVO = new ActionLogVO();
			if("002".equals(apprVO.getRsltCd())) { // 승인
				logVO.setStatusGb("Failure");
				logVO.setContentType("004");	// 결재승인
				logVO.setContent("M1003004");
				logVO.setContentPath("/ems/apr/mailAprUpdateP.ums");
				logVO.setContentRslt("002");	// 승인
				logVO.setMessage(e.getMessage());
				logVO.setExtrYn("N");
				logVO.setRecCnt(0);
				logVO.setMobilYn("N");
			} else if("003".equals(apprVO.getRsltCd())) {	// 반려
				logVO.setStatusGb("Failure");
				logVO.setContentType("004");	// 결재승인
				logVO.setContent("M1003004");
				logVO.setContentPath("/ems/apr/mailAprUpdateP.ums");
				logVO.setContentRslt("003");	// 반려
				logVO.setMessage(e.getMessage());
				logVO.setExtrYn("N");
				logVO.setRecCnt(0);
				logVO.setMobilYn("N");
			}
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception ex) {}
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
	
	/*RNS 실시간 이메일 결재처리 */
	/**
	 * 메일발송결재 수정 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/rnsAprUpdateP")
	public String goRnsAprUpdateP(@ModelAttribute SecuApprovalLineVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goRnsAprUpdateP taskNo    = " + searchVO.getTaskNo()); 
		
		int tid = searchVO.getTaskNo();
		// 메일정보 조회
		RnsServiceVO serviceInfo = null;
		try {
			serviceInfo = rnsServiceService.getServiceInfo(tid);
		} catch(Exception e) {
			logger.error("rnsServiceService.getServiceInfo error = " + e);
		}
		
		// 첨부파일목록 조회
		List<RnsAttachVO> attachList = null;
		try {
			attachList = rnsServiceService.getAttachList(tid);
		} catch(Exception e) {
			logger.error("rnsServiceService.getAttachList error = " + e);
		}
		
		// 메일별결재목록 조회
		List<RnsSecuApprovalLineVO> apprLineList = null;
		String apprRegId="";
		try {
			apprLineList = rnsServiceService.getRnsApprovalLineList(tid);
			if(apprLineList != null && apprLineList.size() > 0 ) {
				apprRegId = apprLineList.get(0).getRegId();
				serviceInfo.setRegId(apprRegId);
			}
		} catch(Exception e) {
			logger.error("rnsServiceService.getRnsApprovalLineList error = " + e);
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
		
		// 결재반려사유코드 조회
		CodeVO rejectVO = new CodeVO();
		rejectVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		rejectVO.setCdGrp("C104");
		rejectVO.setUseYn("Y");
		List<CodeVO> rejectList = null;
		try {
			rejectList = codeService.getCodeList(rejectVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C102] error = " + e);
		}
		
		String mailContent = getContFileText(serviceInfo.getContentsPath(), false);
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("serviceInfo", serviceInfo);		// 메일정보
		model.addAttribute("attachList", attachList);		// 첨부파일목록
		model.addAttribute("apprLineList", apprLineList);	// 메일별결재목록
		model.addAttribute("mailContent", mailContent);		// 메일내용
		model.addAttribute("reasonList", reasonList);		// 조회사유코드
		model.addAttribute("rejectList", rejectList);		// 결재반려사유코드
		
		return "ems/apr/rnsAprUpdateP";
	}	
	
	/**
	 * RNS 실시간 이메일발송결재 결재라인을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/rnsAprStepListP")
	public String rnsAprStepListP(@ModelAttribute RnsSecuApprovalLineVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("rnsAprStepListP Tid    = " + searchVO.getTid());
 
		
		// 메일별결재목록 조회
		List<RnsSecuApprovalLineVO> apprLineList = null;
		try {
			apprLineList = rnsServiceService.getRnsApprovalLineList(searchVO.getTid());
		} catch(Exception e) {
			logger.error("rnsServiceService.getRnsApprovalLineList error = " + e);
		}
		
		
		model.addAttribute("apprLineList", apprLineList);	// 메일별결재목록
		
		return "ems/apr/rnsAprStepListP";
	}
	
	/**
	 * RNS 실시간 이메일발송결재 결재라인을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/rnsAprStepUpdate")
	public ModelAndView rnsAprStepUpdate(@ModelAttribute RnsSecuApprovalLineVO rnsApprVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("rnsAprStepUpdate tid         = " + rnsApprVO.getTid());
		logger.debug("rnsAprStepUpdate apprStep    = " + rnsApprVO.getApprStep());
		logger.debug("rnsAprStepUpdate rsltCd      = " + rnsApprVO.getRsltCd());
		logger.debug("rnsAprStepUpdate rejectCd    = " + rnsApprVO.getRejectCd());
		logger.debug("rnsAprStepUpdate totalCouunt = " + rnsApprVO.getTotalCount());
		
		rnsApprVO.setUpId((String)session.getAttribute("NEO_USER_ID"));
		rnsApprVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		rnsApprVO.setApprDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		int result = 0;
		try { 
		 
			//result = approvalLineService.updateMailAprStep(rnsApprVO);
			result = rnsServiceService.updateRnsServiceAprStep(rnsApprVO);
			
			// 결재로그
			ActionLogVO logVO = new ActionLogVO();
			if("002".equals(rnsApprVO.getRsltCd())) { // 승인
				logVO.setStatusGb("Success");
				logVO.setContentType("004");	// 결재승인
				logVO.setContent("M1003004");
				logVO.setContentPath(request.getRequestURI());
				logVO.setContentRslt("002");	// 승인
				logVO.setMessage("000(정상결재)");
				logVO.setExtrYn("N");
				logVO.setRecCnt(0);
				logVO.setMobilYn("N");
			} else if("003".equals(rnsApprVO.getRsltCd())) {	// 반려
				logVO.setStatusGb("Success");
				logVO.setContentType("004");	// 결재승인
				logVO.setContent("M1003004");
				logVO.setContentPath(request.getRequestURI());
				logVO.setContentRslt("003");	// 반려
				logVO.setMessage(rnsApprVO.getRejectCd() + "(" + approvalLineService.getRejectNm(rnsApprVO.getRejectCd()) + ")");
				logVO.setExtrYn("N");
				logVO.setRecCnt(0);
				logVO.setMobilYn("N");
			}
			logService.insertActionLog(request, session, logVO);
		} catch(Exception e) {
			logger.error("rnsServiceService.updateRnsServiceAprStep error = " + e);
			
			// 결재로그
			ActionLogVO logVO = new ActionLogVO();
			if("002".equals(rnsApprVO.getRsltCd())) { // 승인
				logVO.setStatusGb("Failure");
				logVO.setContentType("004");	// 결재승인
				logVO.setContent("M1003004");
				logVO.setContentPath("/ems/apr/rnsAprUpdateP.ums");
				logVO.setContentRslt("002");	// 승인
				logVO.setMessage(e.getMessage());
				logVO.setExtrYn("N");
				logVO.setRecCnt(0);
				logVO.setMobilYn("N");
			} else if("003".equals(rnsApprVO.getRsltCd())) {	// 반려
				logVO.setStatusGb("Failure");
				logVO.setContentType("004");	// 결재승인
				logVO.setContent("M1003004");
				logVO.setContentPath("/ems/apr/rnsAprUpdateP.ums");
				logVO.setContentRslt("003");	// 반려
				logVO.setMessage(e.getMessage());
				logVO.setExtrYn("N");
				logVO.setRecCnt(0);
				logVO.setMobilYn("N");
			}
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception ex) {}
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
	 * 파일의 내용을 읽는다.
	 * @param contFlPath
	 * @return
	 */
	public String getContFileText(String contFlPath, boolean needServerPath) {
		logger.debug("getContFileText contFlPath  = " + contFlPath);
		
		FileInputStream input = null;
		InputStreamReader reader = null;
		BufferedReader bufferedReader = null;
		StringBuffer sb = new StringBuffer();
		try {
			
			String basePath = "";
			String contPath = "";
			if (needServerPath) {
				basePath = properties.getProperty("FILE.UPLOAD_PATH");
				contPath = basePath + "/" + contFlPath;
			} else {
				contPath = contFlPath;
			}
			
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
}
