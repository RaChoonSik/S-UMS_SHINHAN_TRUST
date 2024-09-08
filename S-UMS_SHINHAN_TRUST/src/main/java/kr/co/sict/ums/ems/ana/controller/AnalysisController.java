/**
 * 작성자 : 김상진
 * 작성일시 : 2021.08.02
 * 설명 : 통계/분석 Controller
 */
package kr.co.sict.ums.ems.ana.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.IndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.sict.ums.com.service.CodeService;
import kr.co.sict.ums.com.service.CryptoService;
import kr.co.sict.ums.com.vo.CodeVO;
import kr.co.sict.ums.ems.ana.service.AnalysisService;
import kr.co.sict.ums.ems.ana.vo.MailDomainVO;
import kr.co.sict.ums.ems.ana.vo.MailErrorVO;
import kr.co.sict.ums.ems.ana.vo.MailSummVO;
import kr.co.sict.ums.ems.ana.vo.PeriodSummVO;
import kr.co.sict.ums.ems.ana.vo.RespLogVO;
import kr.co.sict.ums.ems.ana.vo.SendLogVO;
import kr.co.sict.ums.ems.cam.service.CampaignService;
import kr.co.sict.ums.ems.cam.vo.CampaignVO;
import kr.co.sict.ums.ems.cam.vo.TaskVO;
import kr.co.sict.ums.ems.seg.service.SegmentService;
import kr.co.sict.ums.ems.seg.vo.SegmentVO;
import kr.co.sict.ums.sys.log.service.SystemLogService;
import kr.co.sict.ums.sys.log.vo.ActionLogVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.PageUtil;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Controller
@RequestMapping("/ems/ana")
public class AnalysisController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private AnalysisService analysisService;
	
	@Autowired
	private CampaignService campaignService;
	
	@Autowired
	private SegmentService segmentService;
	
	@Autowired
	private SystemLogService logService;
	
	@Autowired
	private CryptoService cryptoService;
	
	@Autowired
	private PropertiesUtil properties;
	
	/**
	 * 통계분석 단기메일분석(메일별분석) 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailListP")
	public String goMailListP(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		searchVO.setPage(page);
		
		// 캠페인 목록 조회
		List<CampaignVO> campList = null;
		CampaignVO camp = new CampaignVO();
		camp.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		camp.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
		camp.setStatus("000");
		try {
			campList = codeService.getCampaignList(camp);
		} catch(Exception e) {
			logger.error("codeService.getCampaignList error = " + e);
		}
		
		// 수신자그룹 목록 조회
		SegmentVO segVO = new SegmentVO();
		segVO.setPage(1);
		segVO.setRows(9999999);
		segVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		segVO.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		segVO.setEmsuseYn("Y");
		List<SegmentVO> segList = null;
		try {
			segList = segmentService.getSegmentList(segVO);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentList error = " + e);
		}
		
		// 사용자그룹(부서) 목록 조회
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
		
		model.addAttribute("searchVO", searchVO);	// 검색 항목
		model.addAttribute("campList", campList);	// 캠페인 목록
		model.addAttribute("segList", segList);		// 수신자그룹 목록
		model.addAttribute("deptList", deptList);	// 부서 목록
		model.addAttribute("userList", userList);	// 사용자 목록
		
		return "ems/ana/mailListP";
	}
	
	/**
	 * 통계분석 단기메일분석(메일별분석) 메일 목록을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailList")
	public String goMailList(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;

		// 메일 목록 조회
		List<TaskVO> mailList = null;
		try {
			mailList = analysisService.getMailList(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailList error = " + e);
		}
		
		if(mailList != null && mailList.size() > 0) {
			totalCount = mailList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("mailList", mailList);	// 메일 목록
		model.addAttribute("pageUtil", pageUtil);	// 페이징
		
		return "ems/ana/mailList";
	}
	
	/**
	 * 통계분석 단기메일분석(메일별분석) 통계분석 화면을 출력한다.
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailStatP")
	public String goMailStatP(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailStatP page          = " + searchVO.getPage());
		logger.debug("goMailStatP taskNo        = " + searchVO.getTaskNo());
		logger.debug("goMailStatP subTaskNo     = " + searchVO.getSubTaskNo());
		logger.debug("goMailStatP searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goMailStatP searchEndDt   = " + searchVO.getSearchEndDt());
		logger.debug("goMailStatP searchCampNo  = " + searchVO.getSearchCampNo());
		logger.debug("goMailStatP searchTaskNm  = " + searchVO.getSearchTaskNm());
		logger.debug("goMailStatP searchSegNo   = " + searchVO.getSearchSegNo());
		logger.debug("goMailStatP searchUserId  = " + searchVO.getSearchUserId());
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 메일 정보 조회
		TaskVO mailInfo = null;
		try {
			mailInfo = analysisService.getMailInfo(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailInfo error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);		// 검색조건
		model.addAttribute("mailInfo", mailInfo);		// 메일정보
		
		return "ems/ana/mailStatP";
	}

	
	
	
	/**
	 * 통계분석 단기메일분석 결과요약 화면을 출력한다.
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailSummP")
	public String goMailSumm(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailSumm taskNo = " + searchVO.getTaskNo());
		logger.debug("goMailSumm subTaskNo = " + searchVO.getSubTaskNo());
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 메일 정보 조회
		//TaskVO mailInfo = null;
		//try {
		//	mailInfo = analysisService.getMailInfo(searchVO);
		//} catch(Exception e) {
		//	logger.error("analysisService.getMailInfo error = " + e);
		//}
		
		// 발송결과 조회
		MailSummVO sendResult = null;
		try {
			sendResult = analysisService.getMailSummResult(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailSummResult error = " + e);
		}
		
		// 세부에러 조회
		List<MailErrorVO> detailList = null;
		try {
			detailList = analysisService.getMailSummDetail(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailSummDetail error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);		// 검색조건
		//model.addAttribute("mailInfo", mailInfo);		// 메일정보
		model.addAttribute("sendResult", sendResult);	// 발송결과
		model.addAttribute("detailList", detailList);	// 세부에러
		
		return "ems/ana/mailSummP";
	}
	
	/**
	 * 발송 실패 목록 조회
	 * @param sendLogVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/failListP")
	public String goFailListP(@ModelAttribute SendLogVO sendLogVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goFailListP taskNo    = " + sendLogVO.getTaskNo());
		logger.debug("goFailListP subTaskNo = " + sendLogVO.getSubTaskNo());
		logger.debug("goFailListP step1     = " + sendLogVO.getStep1());
		logger.debug("goFailListP step2     = " + sendLogVO.getStep2());
		logger.debug("goFailListP step3     = " + sendLogVO.getStep3());
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(sendLogVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(sendLogVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		sendLogVO.setPage(page);
		sendLogVO.setRows(rows);
		sendLogVO.setStartRow((sendLogVO.getPage()-1)*sendLogVO.getRows());
		int totalCount = 0;
		
		// 발송 실패 목록 조회
		List<SendLogVO> failList = null;
		try {
			failList = analysisService.getFailList(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getFailList error = " + e);
		}
		
		// 페이징 설정
		if(failList != null && failList.size() > 0) {
			totalCount = failList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, sendLogVO.getPage(), totalCount, rows);
		
		model.addAttribute("sendLogVO", sendLogVO);
		model.addAttribute("failList", failList);
		model.addAttribute("pageUtil", pageUtil);
		
		return "ems/ana/failListP";
	}
	
	/**
	 * 발송 실패 목록 엑셀 다운로드
	 * @param sendLogVO
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/failExcelList")
	public void goFailExcelList(@ModelAttribute SendLogVO sendLogVO, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("goFailExcelList taskNo    = " + sendLogVO.getTaskNo());
		logger.debug("goFailExcelList subTaskNo = " + sendLogVO.getSubTaskNo());
		logger.debug("goFailExcelList step1     = " + sendLogVO.getStep1());
		logger.debug("goFailExcelList step2     = " + sendLogVO.getStep2());
		
		// 페이지 설정
		int page = 1;
		int rows = 9999999;
		sendLogVO.setPage(page);
		sendLogVO.setRows(rows);
		
		// 발송 실패 목록 조회
		List<SendLogVO> failList = null;
		try {
			failList = analysisService.getFailList(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getFailList error = " + e);
		}

		try {
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			
			// 엑셀 헤더 생성 ( EMAIL | ID | NAME | SENDING DATE | MESSAGE )
			row = sheet.createRow(rowNum++);
			cell = row.createCell(0); cell.setCellStyle(headerStyle); cell.setCellValue("EMAIL");
			cell = row.createCell(1); cell.setCellStyle(headerStyle); cell.setCellValue("ID");
			cell = row.createCell(2); cell.setCellStyle(headerStyle); cell.setCellValue("NAME");
			cell = row.createCell(3); cell.setCellStyle(headerStyle); cell.setCellValue("SENDING DATE");
			cell = row.createCell(4); cell.setCellStyle(headerStyle); cell.setCellValue("MESSAGE");
			
			// 엑셀 내용 추가
			for(SendLogVO log:failList) {
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0); cell.setCellValue( log.getCustEm() );		// EMAIL
				cell = row.createCell(1); cell.setCellValue( log.getCustId() );		// ID
				cell = row.createCell(2); cell.setCellValue( log.getCustNm() );		// NAME
				cell = row.createCell(3); cell.setCellValue( StringUtil.getFDate(log.getSendDt()) );	// SENDING DATE
				cell = row.createCell(4); cell.setCellValue( log.getSendMsg() );	// MESSAGE
			}
			
			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=Error.xlsx");
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}

	/**
	 * 통계분석 단기메일분석 세부에러 화면을 출력한다.
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailErrorP")
	public String goMailErrorP(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailErrorP taskNo = " + taskVO.getTaskNo());
		logger.debug("goMailErrorP subTaskNo = " + taskVO.getSubTaskNo());
		taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 메일 정보 조회
		//TaskVO mailInfo = null;
		//try {
		//	mailInfo = analysisService.getMailInfo(taskVO);
		//} catch(Exception e) {
		//	logger.error("analysisService.getMailInfo error = " + e);
		//}
		
		// 세부에러 목록 조회
		List<MailErrorVO> errorList = null;
		try {
			errorList = analysisService.getMailErrorList(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailErrorList error = " + e);
		}
		
		model.addAttribute("taskVO", taskVO);			// 조건정보
		//model.addAttribute("mailInfo", mailInfo);		// 메일 정보
		model.addAttribute("errorList", errorList);		// 세부에러

		return "/ems/ana/mailErrorP";
	}
	
	/**
	 * 통계분석 단기메일분석 도메인별 화면을 출력한다.
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailDomainP")
	public String goMailDomainP(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailDomainP taskNo = " + taskVO.getTaskNo());
		logger.debug("goMailDomainP subTaskNo = " + taskVO.getSubTaskNo());
		taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 메일 정보 조회
		//TaskVO mailInfo = null;
		//try {
		//	mailInfo = analysisService.getMailInfo(taskVO);
		//} catch(Exception e) {
		//	logger.error("analysisService.getMailInfo error = " + e);
		//}
		
		// 도메인별 목록 조회
		List<MailDomainVO> domainList = null;
		try {
			domainList = analysisService.getMailDomainList(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailDomainList error = " + e);
		}
		
		model.addAttribute("taskVO", taskVO);			// 조건정보
		//model.addAttribute("mailInfo", mailInfo);		// 메일 정보
		model.addAttribute("domainList", domainList);	// 도메인별
		
		return "ems/ana/mailDomainP";
	}
	
	/**
	 * 통계분석 단기메일분석 발송시간별 화면을 출력한다.
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailSendP")
	public String goMailSendP(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailSendP taskNo = " + taskVO.getTaskNo());
		logger.debug("goMailSendP subTaskNo = " + taskVO.getSubTaskNo());
		taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 메일 정보 조회
		//TaskVO mailInfo = null;
		//try {
		//	mailInfo = analysisService.getMailInfo(taskVO);
		//} catch(Exception e) {
		//	logger.error("analysisService.getMailInfo error = " + e);
		//}
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(taskVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(taskVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		taskVO.setPage(page);
		taskVO.setRows(rows);
		taskVO.setStartRow((taskVO.getPage()-1)*taskVO.getRows());
		int totalCount = 0;

		// 발송시간별 목록 조회
		List<SendLogVO> sendList = null;
		try {
			sendList = analysisService.getMailSendHourList(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailSendHourList error = " + e);
		}
		
		// 페이징 설정
		if(sendList != null && sendList.size() > 0) {
			totalCount = sendList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, taskVO.getPage(), totalCount, rows);
		pageUtil.setSubmitFunc("goPageNumSend");
		
		// 발송시간별 합계 조회
		SendLogVO sendSum = null;
		try {
			sendSum = analysisService.getMailSendHourSum(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailSendHourList error = " + e);
		}
		
		model.addAttribute("taskVO", taskVO);			// 조건정보
		//model.addAttribute("mailInfo", mailInfo);		// 메일 정보
		model.addAttribute("sendList", sendList);		// 발송시간별 목록
		model.addAttribute("sendSum", sendSum);			// 발송시간별 합계
		model.addAttribute("pageUtil", pageUtil);		// 페이징
		
		return "ems/ana/mailSendP";
	}
	
	/**
	 * 통계분석 단기메일분석 응답시간별 화면을 출력한다.
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailRespP")
	public String goMailRespP(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailRespP taskNo = " + taskVO.getTaskNo());
		logger.debug("goMailRespP subTaskNo = " + taskVO.getSubTaskNo());
		taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 메일 정보 조회
		TaskVO mailInfo = null;
		try {
			mailInfo = analysisService.getMailInfo(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailInfo error = " + e);
		}
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(taskVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(taskVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		taskVO.setPage(page);
		taskVO.setRows(rows);
		taskVO.setStartRow((taskVO.getPage()-1)*taskVO.getRows());
		int totalCount = 0;

		// 응답시간별 목록 조회
		List<RespLogVO> respList = null;
		try {
			respList = analysisService.getMailRespHourList(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailRespHourList error = " + e);
		}
		
		// 페이징 설정
		if(respList != null && respList.size() > 0) {
			totalCount = respList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, taskVO.getPage(), totalCount, rows);
		pageUtil.setSubmitFunc("goPageNumResp");
		
		// 응답시간별 합계 조회
		RespLogVO respSum = null;
		try {
			respSum = analysisService.getMailRespHourSum(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailRespHourSum error = " + e);
		}
		
		model.addAttribute("taskVO", taskVO);			// 조건정보
		model.addAttribute("mailInfo", mailInfo);		// 메일 정보
		model.addAttribute("respList", respList);		// 응답시간별 목록
		model.addAttribute("respSum", respSum);			// 응답시간별 합계
		model.addAttribute("pageUtil", pageUtil);		// 페이징
		
		return "ems/ana/mailRespP";
	}
	
	/**
	 * 통계분석 단기메일분석 고객별 화면을 출력한다.
	 * @param sendLogVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailCustP")
	public String goMailCustP(@ModelAttribute SendLogVO sendLogVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailCustP taskNo       = " + sendLogVO.getTaskNo());
		logger.debug("goMailCustP subTaskNo    = " + sendLogVO.getSubTaskNo());
		logger.debug("goMailCustP searchCustId = " + sendLogVO.getSearchCustId());
		logger.debug("goMailCustP searchCustEm = " + sendLogVO.getSearchCustEm());
		logger.debug("goMailCustP searchCustNm = " + sendLogVO.getSearchCustNm());
		logger.debug("goMailCustP searchKind   = " + sendLogVO.getSearchKind());
		
		// 검색유형 초기값 설정(검색유형 선택 => 전체)
		if(StringUtil.isNull(sendLogVO.getSearchKind())) sendLogVO.setSearchKind("000");
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(sendLogVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(sendLogVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		sendLogVO.setPage(page);
		sendLogVO.setRows(rows);
		sendLogVO.setStartRow((sendLogVO.getPage()-1)*sendLogVO.getRows());
		int totalCount = 0;
		String plainCustEm = "";
		
		if(sendLogVO.getSearchCustEm() != null && !"".equals(sendLogVO.getSearchCustEm())) {
			plainCustEm = sendLogVO.getSearchCustEm();
			sendLogVO.setSearchCustEm(cryptoService.getEncrypt("CUST_EM", sendLogVO.getSearchCustEm()));
		}

		// 고객별 로그 목록 조회
		List<SendLogVO> custList = null;
		try {
			custList = analysisService.getCustLogList(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getCustLogList error = " + e);
		}
		
		// 페이징 설정
		if(custList != null && custList.size() > 0) {
			totalCount = custList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, sendLogVO.getPage(), totalCount, rows);
		pageUtil.setSubmitFunc("goPageNumCust");
		
		sendLogVO.setSearchCustEm(plainCustEm);
		model.addAttribute("sendLogVO", sendLogVO);		// 조건정보
		model.addAttribute("custList", custList);		// 고객별 목록
		model.addAttribute("pageUtil", pageUtil);		// 페이징
		
		return "ems/ana/mailCustP";
	}
	
	/**
	 * 통계분석 단기메일분석 고객별 화면을 출력한다.
	 * @param sendLogVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/logListP")
	public String goLogListP(@ModelAttribute SendLogVO sendLogVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("logListP taskNo       = " + sendLogVO.getTaskNo());
		logger.debug("logListP subTaskNo    = " + sendLogVO.getSubTaskNo());
		logger.debug("logListP searchCustId = " + sendLogVO.getSearchCustId());
		logger.debug("logListP searchCustEm = " + sendLogVO.getSearchCustEm());
		logger.debug("logListP searchCustNm = " + sendLogVO.getSearchCustNm());
		logger.debug("logListP searchKind   = " + sendLogVO.getSearchKind());
		
		// 검색유형 초기값 설정(검색유형 선택 => 전체)
		if(StringUtil.isNull(sendLogVO.getSearchKind())) sendLogVO.setSearchKind("000");
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(sendLogVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(sendLogVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		sendLogVO.setPage(page);
		sendLogVO.setRows(rows);
		sendLogVO.setStartRow((sendLogVO.getPage()-1)*sendLogVO.getRows());

		// 고객별 로그 목록 조회
		List<SendLogVO> custList = null;
		try {
			custList = analysisService.getCustLogList(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getCustLogList error = " + e);
		}
		
		// 페이징 설정
		int totalCount = 0;
		if(custList != null && custList.size() > 0) {
			totalCount = custList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, sendLogVO.getPage(), totalCount, rows);
		
		model.addAttribute("sendLogVO", sendLogVO);		// 조건정보
		model.addAttribute("custList", custList);		// 고객별 목록
		model.addAttribute("pageUtil", pageUtil);		// 페이징
		
		return "ems/ana/logListP";
	}
	
	
	/**
	 * 단기메일분석 병합분석 화면을 출력한다.
	 * @param sendLogVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailJoinP")
	public String goMailJoinP(@ModelAttribute SendLogVO sendLogVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailJoinP taskNos       = " + sendLogVO.getTaskNos());
		logger.debug("goMailJoinP subTaskNos    = " + sendLogVO.getSubTaskNos());
		sendLogVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		String[] taskNo = sendLogVO.getTaskNos().split(",");
		/* String[] subTaskNo = sendLogVO.getSubTaskNos().split(","); */
		List<HashMap<String, Integer>> joinList = new ArrayList<HashMap<String, Integer>>();
		for(int i=0;i<taskNo.length;i++) {
			HashMap<String, Integer> key = new HashMap<String, Integer>();
			key.put("taskNo", Integer.parseInt(taskNo[i]));
			/* key.put("subTaskNo", Integer.parseInt(subTaskNo[i])); */
			 key.put("subTaskNo", 1); 
			joinList.add(key);
		}
		sendLogVO.setJoinList(joinList);
		
		// 병합분석 메일정보 목록 조회
		List<TaskVO> mailList = null;
		try {
			mailList = analysisService.getJoinMailList(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinMailList error = " + e);
		}
		
		// 병합분석 발송결과 조회
		RespLogVO respLog = null;
		try {
			respLog = analysisService.getJoinSendResult(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinSendResult error = " + e);
		}
		
		// 병합분석 세부에러 목록 조회
		List<MailErrorVO> errorList = null;
		try {
			errorList = analysisService.getJoinErrorList(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinErrorList error = " + e);
		}
		
		model.addAttribute("sendLogVO", sendLogVO);		// 조건정보
		model.addAttribute("mailList", mailList);		// 병합분석 메일정보 목록
		model.addAttribute("respLog", respLog);			// 병합분석 발송결과
		model.addAttribute("errorList", errorList);		// 병합분석 세부에러 목록
		
		return "ems/ana/mailJoinP";
	}
	
	/**
	 * 단기메일분석 병합분석에서 메일명 클릭하면 나오는 팝업화면(메일분석 탭)
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailInfoP")
	public String goMailInfoP(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailInfoP taskNo       = " + taskVO.getTaskNo());
		logger.debug("goMailInfoP subTaskNo    = " + taskVO.getSubTaskNo());
		
		model.addAttribute("taskNo", taskVO.getTaskNo());			// TaskNo
		model.addAttribute("subTaskNo", 1);							// SubTaskNo
		//model.addAttribute("subTaskNo", taskVO.getSubTaskNo());	// SubTaskNo
		
		return "ems/ana/mailInfoP";
	}
	
	/**
	 * 통계분석 정기메일분석 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskListP")
	public String goTaskListP(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		searchVO.setPage(page);

		// 캠페인 목록 조회
		List<CampaignVO> campList = null;
		CampaignVO camp = new CampaignVO();
		camp.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		camp.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
		camp.setStatus("000");
		try {
			campList = codeService.getCampaignList(camp);
		} catch(Exception e) {
			logger.error("codeService.getCampaignList error = " + e);
		}
		
		// 수신자그룹 목록 조회
		SegmentVO segVO = new SegmentVO();
		segVO.setPage(1);
		segVO.setRows(9999999);
		segVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		segVO.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		segVO.setEmsuseYn("Y");
		List<SegmentVO> segList = null;
		try {
			segList = segmentService.getSegmentList(segVO);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentList error = " + e);
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
		
		model.addAttribute("searchVO", searchVO);	// 검색 항목
		model.addAttribute("campList", campList);	// 캠페인 목록
		model.addAttribute("segList", segList);		// 수신자그룹 목록
		model.addAttribute("deptList", deptList);	// 부서 목록
		model.addAttribute("userList", userList);	// 사용자 목록

		return "ems/ana/taskListP";
	}
	
	/**
	 * 통계분석 정기메일분석 메일 목록을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskList")
	public String goTaskList(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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

		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		// 정기메일 목록 조회
		List<TaskVO> taskList = null;
		try {
			taskList = analysisService.getTaskList(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailList error = " + e);
		}
		
		if(taskList != null && taskList.size() > 0) {
			totalCount = taskList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("searchVO", searchVO);	// 검색 항목
		model.addAttribute("taskList", taskList);	// 정기메일 목록
		model.addAttribute("pageUtil", pageUtil);	// 페이징

		return "ems/ana/taskList";
	}
	
	/**
	 * 통계분석 정기메일분석 차수별 분석 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskStepListP")
	public String goTaskStepListP(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		searchVO.setPage(page);

		// 캠페인 목록 조회
		List<CampaignVO> campList = null;
		CampaignVO camp = new CampaignVO();
		camp.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		camp.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
		camp.setStatus("000");
		try {
			campList = codeService.getCampaignList(camp);
		} catch(Exception e) {
			logger.error("codeService.getCampaignList error = " + e);
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
		
		model.addAttribute("searchVO", searchVO);	// 검색 항목
		model.addAttribute("campList", campList);	// 캠페인 목록
		model.addAttribute("deptList", deptList);	// 부서 목록
		model.addAttribute("userList", userList);	// 사용자 목록
		
		return "ems/ana/taskStepListP";
	}
	
	/**
	 * 통계분석 정기메일분석 차수별 목록 조회
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskStepList")
	public String goTaskStepList(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;

		// 메일 목록 조회
		List<TaskVO> mailList = null;
		try {
			mailList = analysisService.getTaskStepList(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskStepList error = " + e);
		}
		
		if(mailList != null && mailList.size() > 0) {
			totalCount = mailList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("mailList", mailList);	// 메일 목록
		model.addAttribute("pageUtil", pageUtil);	// 페이징
		
		return "ems/ana/taskStepList";
	}
	
	/**
	 * 통계분석 정기메일분석 차수별 통계분석 화면을 출력한다.
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskStepStatP")
	public String goTaskStepStatP(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTaskStepStatP page          = " + searchVO.getPage());
		logger.debug("goTaskStepStatP taskNo        = " + searchVO.getTaskNo());
		logger.debug("goTaskStepStatP subTaskNo     = " + searchVO.getSubTaskNo());
		logger.debug("goTaskStepStatP searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goTaskStepStatP searchEndDt   = " + searchVO.getSearchEndDt());
		logger.debug("goTaskStepStatP searchCampNo  = " + searchVO.getSearchCampNo());
		logger.debug("goTaskStepStatP searchTaskNm  = " + searchVO.getSearchTaskNm());
		logger.debug("goTaskStepStatP searchSegNo   = " + searchVO.getSearchSegNo());
		logger.debug("goTaskStepStatP searchUserId  = " + searchVO.getSearchUserId());
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 메일 정보 조회
		TaskVO mailInfo = null;
		try {
			mailInfo = analysisService.getMailInfo(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailInfo error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);		// 검색조건
		model.addAttribute("mailInfo", mailInfo);		// 메일정보
		
		return "ems/ana/taskStepStatP";
	}
	
	/**
	 * 통계분석 정기메일 통합 통계분석 화면을 출력한다.
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskStatP")
	public String goTaskStatP(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailStatP page          = " + searchVO.getPage());
		logger.debug("goMailStatP taskNo        = " + searchVO.getTaskNo());
		logger.debug("goMailStatP searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goMailStatP searchEndDt   = " + searchVO.getSearchEndDt());
		logger.debug("goMailStatP searchCampNo  = " + searchVO.getSearchCampNo());
		logger.debug("goMailStatP searchTaskNm  = " + searchVO.getSearchTaskNm());
		logger.debug("goMailStatP searchSegNo   = " + searchVO.getSearchSegNo());
		logger.debug("goMailStatP searchUserId  = " + searchVO.getSearchUserId());
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 메일 정보 조회
		TaskVO mailInfo = null;
		try {
			mailInfo = analysisService.getTaskInfo(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskInfo error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);		// 조건정보
		model.addAttribute("mailInfo", mailInfo);		// 메일 정보
		
		return "ems/ana/taskStatP";
	}
	
	/**
	 * 통계분석 정기메일분석 결과요약 화면을 출력한다.
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskSummP")
	public String goTaskSumm(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTaskSumm taskNo = " + taskVO.getTaskNo());
		logger.debug("goTaskSumm subTaskNo = " + taskVO.getSubTaskNo());
		taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		if(taskVO.getSubTaskNo() == 0) {
			taskVO.setSubTaskNo(1);
		}
		
		// 발송결과 조회
		MailSummVO sendResult = null;
		try {
			sendResult = analysisService.getTaskSummResult(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskSummResult error = " + e);
		}
		
		// 세부에러 조회
		List<MailErrorVO> detailList = null;
		try {
			detailList = analysisService.getTaskSummDetail(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskSummDetail error = " + e);
		}
		
		model.addAttribute("taskVO", taskVO);			// 조건정보
		model.addAttribute("sendResult", sendResult);	// 발송결과
		model.addAttribute("detailList", detailList);	// 세부에러
		
		return "ems/ana/taskSummP";
	}
	
	/**
	 * 통계분석 정기메일분석 세부에러 화면을 출력한다.
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskErrorP")
	public String goTaskErrorP(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("taskErrorP taskNo = " + taskVO.getTaskNo());
		logger.debug("taskErrorP subTaskNo = " + taskVO.getSubTaskNo());
		taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		if(taskVO.getSubTaskNo() == 0) {
			taskVO.setSubTaskNo(1);
		}
		
		
		// 세부에러 목록 조회
		List<MailErrorVO> errorList = null;
		try {
			errorList = analysisService.getTaskErrorList(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskErrorList error = " + e);
		}
		
		model.addAttribute("taskVO", taskVO);			// 조건정보
		model.addAttribute("errorList", errorList);		// 세부에러

		return "/ems/ana/taskErrorP";
	}
	
	/**
	 * 통계분석 정기메일분석 도메인별 화면을 출력한다.
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskDomainP")
	public String goTaskDomainP(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTaskDomainP taskNo = " + taskVO.getTaskNo());
		logger.debug("taskDomainP subTaskNo = " + taskVO.getSubTaskNo());
		taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		if(taskVO.getSubTaskNo() == 0) {
			taskVO.setSubTaskNo(1);
		}
		
		// 도메인별 목록 조회
		List<MailDomainVO> domainList = null;
		try {
			domainList = analysisService.getTaskDomainList(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskDomainList error = " + e);
		}
		
		model.addAttribute("taskVO", taskVO);			// 조건정보
		model.addAttribute("domainList", domainList);	// 도메인별
		
		return "ems/ana/taskDomainP";
	}
	
	/**
	 * 통계분석 정기메일분석 발송시간별 화면을 출력한다.
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskSendP")
	public String goTaskSendP(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTaskSendP taskNo = " + taskVO.getTaskNo());
		logger.debug("goTaskSendP subTaskNo = " + taskVO.getSubTaskNo());
		taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		if(taskVO.getSubTaskNo() == 0) {
			taskVO.setSubTaskNo(1);
		}
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(taskVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(taskVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		taskVO.setPage(page);
		taskVO.setRows(rows);
		taskVO.setStartRow((taskVO.getPage()-1)*taskVO.getRows());
		int totalCount = 0;

		// 발송시간별 목록 조회
		List<SendLogVO> sendList = null;
		try {
			sendList = analysisService.getTaskSendHourList(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskSendHourList error = " + e);
		}
		
		// 페이징 설정
		if(sendList != null && sendList.size() > 0) {
			totalCount = sendList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, taskVO.getPage(), totalCount, rows);
		pageUtil.setSubmitFunc("goPageNumSend");
		
		// 발송시간별 합계 조회
		SendLogVO sendSum = null;
		try {
			sendSum = analysisService.getTaskSendHourSum(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskSendHourSum error = " + e);
		}
		
		model.addAttribute("taskVO", taskVO);			// 조건정보
		model.addAttribute("sendList", sendList);		// 발송시간별 목록
		model.addAttribute("sendSum", sendSum);			// 발송시간별 합계
		model.addAttribute("pageUtil", pageUtil);		// 페이징
		
		return "ems/ana/taskSendP";
	}
	
	/**
	 * 통계분석 정기메일분석 응답시간별 화면을 출력한다.
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskRespP")
	public String goTaskRespP(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTaskRespP taskNo = " + taskVO.getTaskNo());
		logger.debug("goTaskRespP subTaskNo = " + taskVO.getSubTaskNo());
		taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		if(taskVO.getSubTaskNo() == 0) {
			taskVO.setSubTaskNo(1);
		}
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(taskVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(taskVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		taskVO.setPage(page);
		taskVO.setRows(rows);
		taskVO.setStartRow((taskVO.getPage()-1)*taskVO.getRows());
		int totalCount = 0;

		// 응답시간별 목록 조회
		List<RespLogVO> respList = null;
		try {
			respList = analysisService.getTaskRespHourList(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskRespHourList error = " + e);
		}
		
		// 페이징 설정
		if(respList != null && respList.size() > 0) {
			totalCount = respList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, taskVO.getPage(), totalCount, rows);
		pageUtil.setSubmitFunc("goPageNumResp");
		
		// 응답시간별 합계 조회
		RespLogVO respSum = null;
		try {
			respSum = analysisService.getTaskRespHourSum(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskRespHourSum error = " + e);
		}
		
		model.addAttribute("taskVO", taskVO);			// 조건정보
		model.addAttribute("respList", respList);		// 응답시간별 목록
		model.addAttribute("respSum", respSum);			// 응답시간별 합계
		model.addAttribute("pageUtil", pageUtil);		// 페이징
		
		return "ems/ana/taskRespP";
	}
	

	/**
	 * 통계분석 정기메일분석 고객별 화면을 출력한다.
	 * @param sendLogVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskCustP")
	public String goTaskCustP(@ModelAttribute SendLogVO sendLogVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTaskCustP taskNo       = " + sendLogVO.getTaskNo());
		logger.debug("goTaskCustP subTaskNo    = " + sendLogVO.getSubTaskNo());
		logger.debug("goTaskCustP searchCustId = " + sendLogVO.getSearchCustId());
		logger.debug("goTaskCustP searchCustEm = " + sendLogVO.getSearchCustEm());
		logger.debug("goTaskCustP searchCustNm = " + sendLogVO.getSearchCustNm());
		logger.debug("goTaskCustP searchKind   = " + sendLogVO.getSearchKind());
		
		// 검색유형 초기값 설정(검색유형 선택 => 전체)
		if(StringUtil.isNull(sendLogVO.getSearchKind())) sendLogVO.setSearchKind("000");
		
		if(sendLogVO.getSubTaskNo() == 0) {
			sendLogVO.setSubTaskNo(1);
		}
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(sendLogVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(sendLogVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		sendLogVO.setPage(page);
		sendLogVO.setRows(rows);
		sendLogVO.setStartRow((sendLogVO.getPage()-1)*sendLogVO.getRows());
		int totalCount = 0;
		String plainCustEm = "";
		
		if(sendLogVO.getSearchCustEm() != null && !"".equals(sendLogVO.getSearchCustEm())) {
			plainCustEm = sendLogVO.getSearchCustEm();
			sendLogVO.setSearchCustEm(cryptoService.getEncrypt("CUST_EM", sendLogVO.getSearchCustEm()));
		}

		// 고객별 로그 목록 조회
		List<SendLogVO> custList = null;
		try {
			custList = analysisService.getTaskCustLogList(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskCustLogList error = " + e);
		}
		
		// 페이징 설정
		if(custList != null && custList.size() > 0) {
			totalCount = custList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, sendLogVO.getPage(), totalCount, rows);
		pageUtil.setSubmitFunc("goPageNumCust");
		
		sendLogVO.setSearchCustEm(plainCustEm);
		model.addAttribute("sendLogVO", sendLogVO);		// 조건정보
		model.addAttribute("custList", custList);		// 고객별 목록
		model.addAttribute("pageUtil", pageUtil);		// 페이징
		
		return "ems/ana/taskCustP";
	}
	
	/**
	 * 정기메일분석 병합분석 화면을 출력한다.
	 * @param sendLogVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskJoinP")
	public String goTaskJoinP(@ModelAttribute SendLogVO sendLogVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTaskJoinP taskNos       = " + sendLogVO.getTaskNos());
		sendLogVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		String[] taskNo = sendLogVO.getTaskNos().split(",");
		List<HashMap<String, Integer>> joinList = new ArrayList<HashMap<String, Integer>>();
		for(int i=0;i<taskNo.length;i++) {
			HashMap<String, Integer> key = new HashMap<String, Integer>();
			key.put("taskNo", Integer.parseInt(taskNo[i]));
			joinList.add(key);
		}
		sendLogVO.setJoinList(joinList);
		
		// 병합분석 메일정보 목록 조회
		List<TaskVO> mailList = null;
		try {
			mailList = analysisService.getJoinTaskList(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinTaskList error = " + e);
		}
		
		// 병합분석 발송결과 조회
		RespLogVO respLog = null;
		try {
			respLog = analysisService.getJoinSendResultTask(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinSendResultTask error = " + e);
		}
		
		// 병합분석 세부에러 목록 조회
		List<MailErrorVO> errorList = null;
		try {
			errorList = analysisService.getJoinErrorListTask(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinErrorListTask error = " + e);
		}
		
		model.addAttribute("sendLogVO", sendLogVO);		// 조건정보
		model.addAttribute("mailList", mailList);		// 병합분석 메일정보 목록
		model.addAttribute("respLog", respLog);			// 병합분석 발송결과
		model.addAttribute("errorList", errorList);		// 병합분석 세부에러 목록
		
		return "ems/ana/taskJoinP";
	}
	
	/**
	 * 정기메일 차수별 병합분석 화면을 출력한다.
	 * @param sendLogVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskStepJoinP")
	public String goTaskStepJoinP(@ModelAttribute SendLogVO sendLogVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTaskStepJoinP taskNos       = " + sendLogVO.getTaskNos());
		logger.debug("goTaskStepJoinP subTaskNos    = " + sendLogVO.getSubTaskNos());
		sendLogVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		String[] taskNo = sendLogVO.getTaskNos().split(",");
		String[] subTaskNo = sendLogVO.getSubTaskNos().split(",");
		List<HashMap<String, Integer>> joinList = new ArrayList<HashMap<String, Integer>>();
		for(int i=0;i<taskNo.length;i++) {
			HashMap<String, Integer> key = new HashMap<String, Integer>();
			key.put("taskNo", Integer.parseInt(taskNo[i]));
			key.put("subTaskNo", Integer.parseInt(subTaskNo[i]));
			joinList.add(key);
		}
		sendLogVO.setJoinList(joinList);
		
		// 병합분석 메일정보 목록 조회
		List<TaskVO> mailList = null;
		try {
			mailList = analysisService.getJoinMailList(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinMailList error = " + e);
		}
		
		// 병합분석 발송결과 조회
		RespLogVO respLog = null;
		try {
			respLog = analysisService.getJoinSendResult(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinSendResult error = " + e);
		}
		
		// 병합분석 세부에러 목록 조회
		List<MailErrorVO> errorList = null;
		try {
			errorList = analysisService.getJoinErrorList(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinErrorList error = " + e);
		}
		
		model.addAttribute("sendLogVO", sendLogVO);		// 조건정보
		model.addAttribute("mailList", mailList);		// 병합분석 메일정보 목록
		model.addAttribute("respLog", respLog);			// 병합분석 발송결과
		model.addAttribute("errorList", errorList);		// 병합분석 세부에러 목록
		
		return "ems/ana/taskStepJoinP";
	}

	
	/**
	 * 정기메일분석 병합분석에서 메일명 클릭하면 나오는 팝업화면(정기메일분석 탭)
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskInfoP")
	public String goTaskInfoP(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTaskInfoP taskNo       = " + taskVO.getTaskNo());
		
		model.addAttribute("taskNo", taskVO.getTaskNo());			// TaskNo
		
		return "ems/ana/taskInfoP";
	}
	
	/**
	 * 통계분석 캠페인별분석 화면을 출력한다.
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
		logger.debug("goCampListP searchDeptNo  = " + searchVO.getSearchDeptNo());
		logger.debug("goCampListP searchUserId  = " + searchVO.getSearchUserId());
		logger.debug("goCampListP searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goCampListP searchEndDt   = " + searchVO.getSearchEndDt());
		
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
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		searchVO.setPage(page);
		
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
		
		model.addAttribute("searchVO", searchVO);		// 검색항목
		model.addAttribute("campTyList", campTyList);	// 캠페인목적
		model.addAttribute("statusList", statusList);	// 캠페인상태
		model.addAttribute("deptList", deptList);		// 부서
		model.addAttribute("userList", userList);		// 사용자
		
		return "ems/ana/campListP";
	}
	
	/**
	 * 통계분석 캠페인별분석 화면에서 캠페인 목록 조회
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
		logger.debug("goCampList searchDeptNo  = " + searchVO.getSearchDeptNo());
		logger.debug("goCampList searchUserId  = " + searchVO.getSearchUserId());
		logger.debug("goCampList searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goCampList searchEndDt   = " + searchVO.getSearchEndDt());
		
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
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;

		// 캠페인 목록 조회
		List<CampaignVO> campaignList = null;
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
		
		model.addAttribute("campaignList", campaignList);	// 캠페인
		model.addAttribute("pageUtil", pageUtil);			// 페이징
		model.addAttribute("searchVO", searchVO);			// 검색
		return "ems/ana/campList";
	}
	
	/**
	 * 통계분석 캠페인별분석 화면에서 캠페인 분석결과
	 * @param campVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campSummP")
	public String goCampSummP(@ModelAttribute CampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goCampSummP campNo = " + searchVO.getCampNo());
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 캠페인 정보 조회
		CampaignVO campInfo = null;
		try {
			campInfo = analysisService.getCampaignInfo(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getCampaignInfo error = " + e);
		}
		
		// 페이지 설정
		searchVO.setPage(1);
		searchVO.setRows(9999999);

		// 캠페인 메일 목록 조회
		List<MailSummVO> mailList = null;
		try {
			mailList = analysisService.getCampMailList(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getCampMailList error = " + e);
		}
		
		// 캠페인 메일 합계 조회
		MailSummVO mailTotal = null;
		try {
			mailTotal = analysisService.getCampMailTotal(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getCampMailTotal error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);		// 검색조건
		model.addAttribute("campInfo", campInfo);		// 캠페인 정보
		model.addAttribute("mailList", mailList);		// 캠페인 메일 목록
		model.addAttribute("mailTotal", mailTotal);		// 캠페인 메일 합계
		
		return "ems/ana/campSummP";
	}
	
	/**
	 * 통계분석 캠페인별분석 병합분석화면을 출력한다.
	 * @param campVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campJoinP")
	public String goCampJoinP(@ModelAttribute SendLogVO sendLogVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goCampJoinP campNos = " + sendLogVO.getCampNos());
		sendLogVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		String[] campNo = sendLogVO.getCampNos().split(",");
		List<Integer> campList = new ArrayList<Integer>();
		for(int i=0;i<campNo.length;i++) {
			campList.add(Integer.parseInt(campNo[i]));
		}
		sendLogVO.setCampList(campList);
		
		// 병합분석 캠페인정보 목록 조회
		List<TaskVO> campaignList = null;
		try {
			campaignList = analysisService.getJoinCampList(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinCampList error = " + e);
		}
		
		// 병합분석 캠페인 발송결과 조회
		RespLogVO respLog = null;
		try {
			respLog = analysisService.getJoinSendResultCamp(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinSendResultCamp error = " + e);
		}
		
		// 병합분석 캠페인 세부에러 목록 조회
		List<MailErrorVO> errorList = null;
		try {
			errorList = analysisService.getJoinErrorListCamp(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinErrorListCamp error = " + e);
		}
		
		model.addAttribute("sendLogVO", sendLogVO);			// 조건정보
		model.addAttribute("campaignList", campaignList);	// 병합분석 캠페인정보 목록
		model.addAttribute("respLog", respLog);				// 병합분석 캠페인 발송결과
		model.addAttribute("errorList", errorList);			// 병합분석 캠페인 세부에러 목록
		
		return "ems/ana/campJoinP";
	}
	
	/**
	 * 통계분석 기간별누적분석 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/summMainP")
	public String goSummMainP(@ModelAttribute PeriodSummVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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

		// 캠페인 목록 조회
		List<CampaignVO> campList = null;
		CampaignVO camp = new CampaignVO();
		camp.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		camp.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
		camp.setStatus("000");
		try {
			campList = codeService.getCampaignList(camp);
		} catch(Exception e) {
			logger.error("codeService.getCampaignList error = " + e);
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
		user.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
		user.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(user);
		} catch(Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);	// 검색 항목
		model.addAttribute("campList", campList);	// 캠페인 목록
		model.addAttribute("deptList", deptList);	// 부서 목록
		model.addAttribute("userList", userList);	// 사용자 목록
		
		return "ems/ana/summMainP";
	}
	
	/**
	 * 기간별누적분석 누적통계 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/summStatP")
	public String goSummStatP(@ModelAttribute PeriodSummVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSummStatP searchCampNo = " + searchVO.getSearchCampNo());
		logger.debug("goSummStatP searchCampNm = " + searchVO.getSearchCampNm());
		logger.debug("goSummStatP searchDeptNo = " + searchVO.getSearchDeptNo());
		logger.debug("goSummStatP searchDeptNm = " + searchVO.getSearchDeptNm());
		logger.debug("goSummStatP searchUserId = " + searchVO.getSearchUserId());
		logger.debug("goSummStatP searchUserNm = " + searchVO.getSearchUserNm());
		
		// 검색 기본값 설정
		if(StringUtil.isNull(searchVO.getSearchCampNm())) searchVO.setSearchCampNm("모든 캠페인");
		if(StringUtil.isNull(searchVO.getSearchDeptNm())) searchVO.setSearchDeptNm("모든 그룹");
		if(StringUtil.isNull(searchVO.getSearchUserNm())) searchVO.setSearchUserNm("모든 사용자");
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		
		model.addAttribute("searchVO", searchVO);		// 검색항목
		
		return "ems/ana/summStatP";
	}
	
	/**
	 * 기간별누적분석 월별탭 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/summMonthP")
	public String goSummMonthP(@ModelAttribute PeriodSummVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSummMonthP searchCampNo = " + searchVO.getSearchCampNo());
		logger.debug("goSummMonthP searchDeptNo = " + searchVO.getSearchDeptNo());
		logger.debug("goSummMonthP searchUserId = " + searchVO.getSearchUserId());
		
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		
		// 월별 데이터 조회
		List<PeriodSummVO> monthList = null;
		try {
			monthList = analysisService.getPeriodSummMonthList(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getPeriodSummMonthList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);		// 검색항목
		model.addAttribute("monthList", monthList);		// 월별데이터
		
		return "ems/ana/summMonthP";
	}
	
	/**
	 * 기간별누적분석 요일별탭 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/summWeekP")
	public String goSummWeekP(@ModelAttribute PeriodSummVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 요일별 데이터 조회
		List<PeriodSummVO> weekList = null;
		try {
			weekList = analysisService.getPeriodSummWeekList(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getPeriodSummWeekList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);		// 검색항목
		model.addAttribute("weekList", weekList);		// 요일별데이터

		
		return "ems/ana/summWeekP";
	}
	
	/**
	 * 기간별누적분석 일자별탭 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/summDateP")
	public String goSummDateP(@ModelAttribute PeriodSummVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		
		// 일자별 데이터 조회
		List<PeriodSummVO> dateList = null;
		try {
			dateList = analysisService.getPeriodSummDateList(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getPeriodSummDateList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);		// 검색항목
		model.addAttribute("dateList", dateList);		// 일자별데이터
		
		return "ems/ana/summDateP";
	}
	
	/**
	 * 기간별누적분석 도메인별탭 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/summDomainP")
	public String goSummDomainP(@ModelAttribute PeriodSummVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		
		// 도메인별 데이터 조회
		List<PeriodSummVO> domainList = null;
		try {
			domainList = analysisService.getPeriodSummDomainList(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getPeriodSummDomainList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);		// 검색항목
		model.addAttribute("domainList", domainList);	// 도메인별데이터
		
		return "ems/ana/summDomainP";
	}
	
	/**
	 * 기간별누적분석 그룹(부서)별탭 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/summDeptP")
	public String goSummDeptP(@ModelAttribute PeriodSummVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		
		// 그룹(부서)별 데이터 조회
		List<PeriodSummVO> deptList = null;
		try {
			deptList = analysisService.getPeriodSummDeptList(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getPeriodSummDeptList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);	// 검색항목
		model.addAttribute("deptList", deptList);	// 그룹(부서)별데이터
		
		return "ems/ana/summDeptP";
	}
	
	/**
	 * 기간별누적분석 사용자별탭 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/summUserP")
	public String goSummUserP(@ModelAttribute PeriodSummVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		
		// 사용자별 데이터 조회
		List<PeriodSummVO> userList = null;
		try {
			userList = analysisService.getPeriodSummUserList(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getPeriodSummUserList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);	// 검색항목
		model.addAttribute("userList", userList);	// 사용자별데이터
		
		return "ems/ana/summUserP";
	}
	
	/**
	 * 기간별누적분석 캠페인별탭 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/summCampP")
	public String goSummCampP(@ModelAttribute PeriodSummVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		
		// 캠페인별 데이터 조회
		List<PeriodSummVO> campList = null;
		try {
			campList = analysisService.getPeriodSummCampList(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getPeriodSummCampList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);	// 검색항목
		model.addAttribute("campList", campList);	// 캠페인별데이터
		
		return "ems/ana/summCampP";
	}
	
	/**
	 * 대량메일 상세 로그목록 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/detailLogListP")
	public String goDetailLogP(@ModelAttribute SendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 검색 기본값 설정
		if(StringUtil.isNull(searchVO.getSearchStartDt())) {
			searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-2, "D", "yyyyMMdd"));
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
		
		if(StringUtil.isNull(searchVO.getSearchServiceGb())) {
			searchVO.setSearchServiceGb("10");
		}
		
		// 발송결과코드 목록
		CodeVO result = new CodeVO();
		result.setUilang((String)session.getAttribute("NEO_UILANG"));
		result.setCdGrp("C035");
		result.setUseYn("Y");
		List<CodeVO> resultList = null;
		try {
			resultList = codeService.getCodeList(result);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C035] error = " + e);
		}
		
		// 발송상태코드 목록 
		CodeVO workStatus = new CodeVO();
		workStatus.setUilang((String)session.getAttribute("NEO_UILANG"));
		workStatus.setCdGrp("C101");
		workStatus.setUseYn("Y");
		List<CodeVO> workStatusList = null;
		try {
			workStatusList = codeService.getWorkStatusList(workStatus);
		} catch(Exception e) {
			logger.error("codeService.getWorkStatusList error = " + e);
		}
		
		// 발송유형코드 목록(단발성/정기성)
		CodeVO repeat = new CodeVO();
		repeat.setUilang((String)session.getAttribute("NEO_UILANG"));
		repeat.setCdGrp("C017");
		repeat.setUseYn("Y");
		List<CodeVO> repeatList = null;
		try {
			repeatList = codeService.getCodeList(repeat);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C017] error = " + e);
		}
		
		// 수신자그룹 목록 조회
		SegmentVO segVO = new SegmentVO();
		segVO.setPage(1);
		segVO.setRows(9999999);
		segVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		segVO.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		segVO.setEmsuseYn("Y");
		List<SegmentVO> segList = null;
		try {
			segList = segmentService.getSegmentList(segVO);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentList error = " + e);
		}

		
		model.addAttribute("searchVO", searchVO);				// 검색항목
		model.addAttribute("resultList", resultList);			// 발송결과코드
		model.addAttribute("workStatusList", workStatusList);	// 발송상태코드
		model.addAttribute("repeatList", repeatList);			// 발송유형코드
		model.addAttribute("segList", segList);					// 수신자그룹
		
		return "ems/ana/detailLogListP";
	}
	
	
	/**
	 * 대량메일 상세 로그목록 조회.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/detailLogList")
	public String goDetailLog(@ModelAttribute SendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goDetailLog searchStartDt     = " + searchVO.getSearchStartDt());
		logger.debug("goDetailLog searchEndDt       = " + searchVO.getSearchEndDt());
		logger.debug("goDetailLog searchCampNo      = " + searchVO.getSearchCampNo());
		logger.debug("goDetailLog searchCustId      = " + searchVO.getSearchCustId());
		logger.debug("goDetailLog searchCustNm      = " + searchVO.getSearchCustNm());
		logger.debug("goDetailLog searchCustEm      = " + searchVO.getSearchCustEm());
		logger.debug("goDetailLog searchSendRepeat  = " + searchVO.getSearchSendRepeat());
		logger.debug("goDetailLog searchWorkStatus  = " + searchVO.getSearchWorkStatus());
		logger.debug("goDetailLog searchUserNm      = " + searchVO.getSearchUserNm());
		logger.debug("goDetailLog searchServiceGb   = " + searchVO.getSearchServiceGb());
				
		// 검색 기본값 설정
		if(StringUtil.isNull(searchVO.getSearchStartDt())) {
			searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-2, "D", "yyyyMMdd"));
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
		if(!StringUtil.isNull(searchVO.getSearchCustEm())) {
			searchVO.setSearchCustEm(cryptoService.getEncrypt("CUST_EM", searchVO.getSearchCustEm()));
			logger.debug("goDetailLog Decrypt searchCustEm   = " + searchVO.getSearchCustEm());
		}
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		searchVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		List<SendLogVO> sendLogList = null;
		try {
			sendLogList = analysisService.getSendLogList(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getSendLogList error = " + e);
		}
		
		if(sendLogList != null && sendLogList.size() > 0) {
			totalCount = sendLogList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);

		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("sendLogList", sendLogList);		// 로그목록
		model.addAttribute("pageUtil", pageUtil);			// 페이징
		
		return "ems/ana/detailLogList";
	}
	
	
	
	/********************** EXCEL DOWNLOAD **********************/
	/**
	 * 통계분석 단기메일분석 결과요약 화면 EXCEL
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailSummExcel")
	public void goMailSummExcel(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailSummExcel taskNo = " + taskVO.getTaskNo());
		logger.debug("goMailSummExcel subTaskNo = " + taskVO.getSubTaskNo());
		taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 메일 정보 조회
		TaskVO mailInfo = null;
		try {
			mailInfo = analysisService.getMailInfo(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailInfo error = " + e);
		}
		
		// 발송결과 조회
		MailSummVO sendResult = null;
		try {
			sendResult = analysisService.getMailSummResult(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailSummResult error = " + e);
		}
		
		// 세부에러 조회
		List<MailErrorVO> detailList = null;
		try {
			detailList = analysisService.getMailSummDetail(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailSummDetail error = " + e);
		}
		
		try {
			String fileName = "MAIL_SUMM_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			if("001".equals(taskVO.getSendRepeat())) {
				fileName = "TASK_STEP_SUMM_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			}
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			
			// Data 탭
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			int i = 0;
			
			// 메일정보
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[메일분석]");
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송일시");
			cell = row.createCell(i++); cell.setCellValue( StringUtil.getFDate(mailInfo.getSendDt(), Code.DT_FMT2) + " ~ " + StringUtil.getFDate(mailInfo.getEndDt(), Code.DT_FMT2) );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신자그룹");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getSegNm() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인명");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampNm() );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인목적");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampTy() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("메일명");
			cell = row.createCell(i++); cell.setCellValue( "001".equals(mailInfo.getSendRepeat())?"["+mailInfo.getSubTaskNo()+"차] "+mailInfo.getTaskNm():mailInfo.getTaskNm() );
			
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// 백분율(%) 형식
			XSSFCellStyle perStyle = wb.createCellStyle();
			perStyle.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
			
			// [발송결과] 엑셀 헤더 생성 ( 대상수 | 성공수 | 실패수 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[발송결과]");

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("대상수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패수");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( sendResult.getSendCnt() );
			cell = row.createCell(i++); cell.setCellValue( sendResult.getSuccCnt() );
			cell = row.createCell(i++); cell.setCellValue( sendResult.getFailCnt() );

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "" );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSendCnt()==0?0:(sendResult.getSuccCnt()/sendResult.getSendCnt()) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSendCnt()==0?0:(sendResult.getFailCnt()/sendResult.getSendCnt()) );
			
			// 전체 실패건수
			int totFailCnt = sendResult.getFailCnt();
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// [반응결과] 엑셀 헤더 생성 ( 성공수 | 오픈수 | 유효오픈수 | 링크클릭수 | 수신거부 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[반응결과]");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("유효오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("링크클릭수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신거부");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "반응수" );
			cell = row.createCell(i++); cell.setCellValue( sendResult.getSuccCnt() );
			cell = row.createCell(i++); cell.setCellValue( sendResult.getOpenCnt() );
			cell = row.createCell(i++); cell.setCellValue( sendResult.getValidCnt() );
			cell = row.createCell(i++); cell.setCellValue( sendResult.getClickCnt() );
			cell = row.createCell(i++); cell.setCellValue( sendResult.getBlockCnt() );

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "성공대비" );
			cell = row.createCell(i++); cell.setCellValue( "" );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSuccCnt()==0?0:(sendResult.getOpenCnt()/sendResult.getSuccCnt()) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSuccCnt()==0?0:(sendResult.getValidCnt()/sendResult.getSuccCnt()) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSuccCnt()==0?0:(sendResult.getClickCnt()/sendResult.getSuccCnt()) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSuccCnt()==0?0:(sendResult.getBlockCnt()/sendResult.getSuccCnt()) );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "전체대비" );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSendCnt()==0?0:(sendResult.getSuccCnt()/sendResult.getSendCnt()) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSendCnt()==0?0:(sendResult.getOpenCnt()/sendResult.getSendCnt()) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSendCnt()==0?0:(sendResult.getValidCnt()/sendResult.getSendCnt()) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSendCnt()==0?0:(sendResult.getClickCnt()/sendResult.getSendCnt()) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSendCnt()==0?0:(sendResult.getBlockCnt()/sendResult.getSendCnt()) );
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// 세부에러 변수 설정
			int syntaxErrCnt = 0;
			int webAgentErrCnt = 0;
			int dbAgentErrCnt = 0;
			int mailBodyErrCnt = 0;
			int domainErrCnt = 0;
			int networkErrCnt = 0;
			int connectErrCnt = 0;
			int heloErrCnt = 0;
			int mailFromErrCnt = 0;
			int rcptToErrCnt = 0;
			int resetErrCnt = 0;
			int dataErrCnt = 0;
			int dotErrCnt = 0;
			int quitErrCnt = 0;
			int sumStep1Err = 0;
			int sumStep2Err = 0;
			int failCnt = 0;
			String step1 = "";
			String step2 = "";
			
			// 세부에러 값 설정
			for(MailErrorVO error:detailList) {
				step1 = error.getStep1();
				step2 = error.getStep2();
				failCnt = error.getCntStep2();
				if("001".equals(step1)) {
					sumStep1Err += failCnt;
					if("001".equals(step2)) {
						syntaxErrCnt = failCnt;
					} else if("002".equals(step2)) {
						webAgentErrCnt = failCnt;
					} else if("003".equals(step2)) {
						dbAgentErrCnt = failCnt;
					} else if("004".equals(step2)) {
						mailBodyErrCnt = failCnt;
					} else if("005".equals(step2)) {
						domainErrCnt = failCnt;
					} else if("006".equals(step2)) {
						networkErrCnt = failCnt;
					}
				} else if("002".equals(step1)) {
					sumStep2Err += failCnt;
					if("001".equals(step2)) {
						connectErrCnt = failCnt;
					} else if("002".equals(step2)) {
						heloErrCnt = failCnt;
					} else if("003".equals(step2)) {
						mailFromErrCnt = failCnt;
					} else if("004".equals(step2)) {
						rcptToErrCnt = failCnt;
					} else if("005".equals(step2)) {
						resetErrCnt = failCnt;
					} else if("006".equals(step2)) {
						dataErrCnt = failCnt;
					} else if("007".equals(step2)) {
						dotErrCnt = failCnt;
					} else if("008".equals(step2)) {
						quitErrCnt = failCnt;
					}
				}
			}
			
			// 합계 색상 지정
			XSSFCellStyle footerStyle = wb.createCellStyle();
			IndexedColorMap colorMap = wb.getStylesSource().getIndexedColors();
			footerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 합계 색상 지정(% 형식 추가)
			XSSFCellStyle footerStylePer = wb.createCellStyle();
			footerStylePer.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStylePer.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			footerStylePer.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
			
			// [세부에러 - 메일전송 전단계] 엑셀 헤더 생성 ( Syntax | Web Agent | DB Agent | Mail Body | Domain | Network | 합계 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[세부에러 - 메일전송 전단계]");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Syntax");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Web Agent");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("DB Agent");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Mail Body");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Domain");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Network");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("합계");

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( syntaxErrCnt );
			cell = row.createCell(i++); cell.setCellValue( webAgentErrCnt );
			cell = row.createCell(i++); cell.setCellValue( dbAgentErrCnt );
			cell = row.createCell(i++); cell.setCellValue( mailBodyErrCnt );
			cell = row.createCell(i++); cell.setCellValue( domainErrCnt );
			cell = row.createCell(i++); cell.setCellValue( networkErrCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumStep1Err );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(syntaxErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(webAgentErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(dbAgentErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(mailBodyErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(domainErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(networkErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( totFailCnt==0?0:(sumStep1Err/totFailCnt) );
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// [세부에러 - 메일발송 단계] 엑셀 헤더 생성 ( CONNECT | HELO | MAIL FROM | RCPT TO | RESET | DATA | DOT | QUIT | 합계 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[세부에러 - 메일발송 단계]");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("CONNECT");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("HELO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("MAIL FROM");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("RCPT TO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("RESET");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("DATA");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("DOT");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("QUIT");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("합계");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( connectErrCnt );
			cell = row.createCell(i++); cell.setCellValue( heloErrCnt );
			cell = row.createCell(i++); cell.setCellValue( mailFromErrCnt );
			cell = row.createCell(i++); cell.setCellValue( rcptToErrCnt );
			cell = row.createCell(i++); cell.setCellValue( resetErrCnt );
			cell = row.createCell(i++); cell.setCellValue( dataErrCnt );
			cell = row.createCell(i++); cell.setCellValue( dotErrCnt );
			cell = row.createCell(i++); cell.setCellValue( quitErrCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumStep2Err );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(connectErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(heloErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(mailFromErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(rcptToErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(resetErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(dataErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(dotErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(quitErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( totFailCnt==0?0:(sumStep2Err/totFailCnt) );

			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			if("001".equals(taskVO.getSendRepeat())) {
				logVO.setContent("M1004002");
			} else {
				logVO.setContent("M1004001");
			}
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(totFailCnt);
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}

	
	/**
	 * 통계분석 단기메일분석 세부에러 EXCEL
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="/mailErrorExcel")
	public void goMailErrorExcel(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailErrorExcel taskNo = " + taskVO.getTaskNo());
		logger.debug("goMailErrorExcel subTaskNo = " + taskVO.getSubTaskNo());
		taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 메일 정보 조회
		TaskVO mailInfo = null;
		try {
			mailInfo = analysisService.getMailInfo(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailInfo error = " + e);
		}
		
		// 세부에러 목록 조회
		List<MailErrorVO> errorList = null;
		try {
			errorList = analysisService.getMailErrorList(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailErrorList error = " + e);
		}

		try {
			String fileName = "MAIL_ERROR_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			if("001".equals(taskVO.getSendRepeat())) {
				fileName = "TASK_STEP_ERROR_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			}
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			
			// Data 탭
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			int i = 0;
			
			// 메일정보
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[메일분석]");
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송일시");
			cell = row.createCell(i++); cell.setCellValue( StringUtil.getFDate(mailInfo.getSendDt(), Code.DT_FMT2) + " ~ " + StringUtil.getFDate(mailInfo.getEndDt(), Code.DT_FMT2) );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신자그룹");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getSegNm() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인명");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampNm() );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인목적");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampTy() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("메일명");
			cell = row.createCell(i++); cell.setCellValue( "001".equals(mailInfo.getSendRepeat())?"["+mailInfo.getSubTaskNo()+"차] "+mailInfo.getTaskNm():mailInfo.getTaskNm() );
			
			
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++); 
			
			// 메일 에러 종류별 건수
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[메일 에러 종류별 건수]");
			
			// 엑셀 헤더 생성 ( 대분류건수 | 중분류건수 | 소분류건수 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("대분류건수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("중분류건수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("소분류건수");
			
			// 엑셀 내용 추가
			for(MailErrorVO error:errorList) {
				i = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(i++); cell.setCellValue( error.getNmStep1() + " : " + error.getCntStep1() );
				cell = row.createCell(i++); cell.setCellValue( error.getNmStep2() + " : " + error.getCntStep2() );
				if(StringUtil.isNull(error.getNmStep3())) {
					cell = row.createCell(i++); cell.setCellValue( error.getNmStep2() + " : " + error.getCntStep2() );
				} else {
					cell = row.createCell(i++); cell.setCellValue( error.getNmStep3() + " : " + error.getCntStep3() );
				}
			}
			
			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			if("001".equals(taskVO.getSendRepeat())) {
				logVO.setContent("M1004002");
			} else {
				logVO.setContent("M1004001");
			}
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(errorList == null?0:errorList.size());
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}
	
	/**
	 * 통계분석 단기메일분석 도메인별 EXCEL
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="/mailDomainExcel")
	public void goMailDomainExcel(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailDomainExcel taskNo = " + taskVO.getTaskNo());
		logger.debug("goMailDomainExcel subTaskNo = " + taskVO.getSubTaskNo());
		taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 메일 정보 조회
		TaskVO mailInfo = null;
		try {
			mailInfo = analysisService.getMailInfo(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailInfo error = " + e);
		}
		
		// 도메인별 목록 조회
		List<MailDomainVO> domainList = null;
		try {
			domainList = analysisService.getMailDomainList(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailDomainList error = " + e);
		}

		try {
			String fileName = "MAIL_DOMAIN_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			if("001".equals(taskVO.getSendRepeat())) {
				fileName = "TASK_STEP_DOMAIN_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			}
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			
			// Data 탭
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			int i = 0;
			
			// 메일정보
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[메일분석]");
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송일시");
			cell = row.createCell(i++); cell.setCellValue( StringUtil.getFDate(mailInfo.getSendDt(), Code.DT_FMT2) + " ~ " + StringUtil.getFDate(mailInfo.getEndDt(), Code.DT_FMT2) );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신자그룹");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getSegNm() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인명");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampNm() );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인목적");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampTy() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("메일명");
			cell = row.createCell(i++); cell.setCellValue( "001".equals(mailInfo.getSendRepeat())?"["+mailInfo.getSubTaskNo()+"차] "+mailInfo.getTaskNm():mailInfo.getTaskNm() );
			
			
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			
			// 목록
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[목록]");
			
			// 엑셀 헤더 생성 ( NO | 도메인명 | 발송수 | 성공수 | 실패수 | 발송단계에러 | 성공률 | 실패율 | 발생단계에러율 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("NO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("도메인명");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송단계에러");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공률");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패율");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발생단계에러율");
			
			// 백분율(%) 형식
			XSSFCellStyle perStyle = wb.createCellStyle();
			perStyle.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
			
			int sumSendCnt = 0;
			int sumSuccCnt = 0;
			int sumFailCnt = 0;
			int sumErrorCnt = 0;
			
			// 엑셀 내용 추가
			for(MailDomainVO domain:domainList) {
				sumSendCnt += domain.getSendCnt();
				sumSuccCnt += domain.getSuccCnt();
				sumFailCnt += domain.getSendCnt() - domain.getSuccCnt();
				sumErrorCnt += domain.getErrorCnt();
				
				i = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(i++); cell.setCellValue( rowNum - 7 );
				cell = row.createCell(i++); cell.setCellValue( domain.getDomainNm() );
				cell = row.createCell(i++); cell.setCellValue( domain.getSendCnt() );
				cell = row.createCell(i++); cell.setCellValue( domain.getSuccCnt() );
				cell = row.createCell(i++); cell.setCellValue( (domain.getSendCnt() - domain.getSuccCnt()) );
				cell = row.createCell(i++); cell.setCellValue( domain.getErrorCnt() );
				cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( domain.getSendCnt()==0?0:(domain.getSuccCnt()/domain.getSendCnt()) );
				cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( domain.getSendCnt()==0?0:((domain.getSendCnt() - domain.getSuccCnt())/domain.getSendCnt()) );
				cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( (domain.getSendCnt() - domain.getSuccCnt())==0?0:(domain.getErrorCnt()/(domain.getSendCnt() - domain.getSuccCnt())) );
			}
			
			// 합계 색상 지정
			XSSFCellStyle footerStyle = wb.createCellStyle();
			IndexedColorMap colorMap = wb.getStylesSource().getIndexedColors();
			footerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 합계 색상 지정(% 형식 추가)
			XSSFCellStyle footerStylePer = wb.createCellStyle();
			footerStylePer.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStylePer.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			footerStylePer.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( "합계" );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( "" );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumSendCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumSuccCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumFailCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumErrorCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( sumSendCnt==0?0:(sumSuccCnt/sumSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( sumSendCnt==0?0:(sumFailCnt/sumSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( sumSendCnt-sumSuccCnt==0?0:sumErrorCnt/(sumSendCnt-sumSuccCnt) );
			
			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			if("001".equals(taskVO.getSendRepeat())) {
				logVO.setContent("M1004002");
			} else {
				logVO.setContent("M1004001");
			}
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(domainList == null?0:domainList.size());
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}
	
	/**
	 * 통계분석 단기메일분석 발송시간별 EXCEL
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="/mailSendExcel")
	public void goMailSendExcel(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailSendExcel taskNo = " + taskVO.getTaskNo());
		logger.debug("goMailSendExcel subTaskNo = " + taskVO.getSubTaskNo());
		taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 메일 정보 조회
		TaskVO mailInfo = null;
		try {
			mailInfo = analysisService.getMailInfo(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailInfo error = " + e);
		}
		
		taskVO.setPage(1);
		taskVO.setRows(9999999);
		
		// 발송시간별 목록 조회
		List<SendLogVO> sendList = null;
		try {
			sendList = analysisService.getMailSendHourList(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailSendHourList error = " + e);
		}
		
		// 발송시간별 합계 조회
		SendLogVO sendSum = null;
		try {
			sendSum = analysisService.getMailSendHourSum(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailSendHourList error = " + e);
		}

		try {
			String fileName = "MAIL_SEND_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			if("001".equals(taskVO.getSendRepeat())) {
				fileName = "TASK_STEP_SEND_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			}
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			
			// Data 탭
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			int i = 0;
			
			// 메일정보
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[메일분석]");
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송일시");
			cell = row.createCell(i++); cell.setCellValue( StringUtil.getFDate(mailInfo.getSendDt(), Code.DT_FMT2) + " ~ " + StringUtil.getFDate(mailInfo.getEndDt(), Code.DT_FMT2) );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신자그룹");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getSegNm() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인명");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampNm() );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인목적");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampTy() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("메일명");
			cell = row.createCell(i++); cell.setCellValue( "001".equals(mailInfo.getSendRepeat())?"["+mailInfo.getSubTaskNo()+"차] "+mailInfo.getTaskNm():mailInfo.getTaskNm() );
			
			
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			
			// 목록
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[목록]");
			
			// 엑셀 헤더 생성 ( NO | 시간 | 발송수 | 성공수 | 실패수 | 성공률 | 실패율 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("NO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("시간");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공률");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패율");
			
			// 백분율(%) 형식
			XSSFCellStyle perStyle = wb.createCellStyle();
			perStyle.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
			
			// 엑셀 내용 추가
			if(sendList != null && sendList.size() > 0) {
				for(SendLogVO send:sendList) {
					i = 0;
					row = sheet.createRow(rowNum++);
					cell = row.createCell(i++); cell.setCellValue( rowNum - 7 );
					cell = row.createCell(i++); cell.setCellValue( send.getSendTime().substring(0,4)+"-"+send.getSendTime().substring(4,6)+"-"+send.getSendTime().substring(6,8)+" "+send.getSendTime().substring(8)+"시");
					cell = row.createCell(i++); cell.setCellValue( send.getSendCnt() );
					cell = row.createCell(i++); cell.setCellValue( send.getSuccCnt() );
					cell = row.createCell(i++); cell.setCellValue( (send.getSendCnt() - send.getSuccCnt()) );
					cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( send.getSendCnt()==0?0:(send.getSuccCnt()/send.getSendCnt()) );
					cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( send.getSendCnt()==0?0:((send.getSendCnt() - send.getSuccCnt())/send.getSendCnt()) );
				}
			}
			
			// 합계 색상 지정
			XSSFCellStyle footerStyle = wb.createCellStyle();
			IndexedColorMap colorMap = wb.getStylesSource().getIndexedColors();
			footerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 합계 색상 지정(% 형식 추가)
			XSSFCellStyle footerStylePer = wb.createCellStyle();
			footerStylePer.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStylePer.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			footerStylePer.setDataFormat(wb.createDataFormat().getFormat("0.00%"));

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( "합계" );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( "" );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sendSum.getSendCnt() );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sendSum.getSuccCnt() );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( (sendSum.getSendCnt() - sendSum.getSuccCnt()) );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( sendSum.getSendCnt()==0?0:(sendSum.getSuccCnt()/sendSum.getSendCnt()) );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( sendSum.getSendCnt()==0?0:((sendSum.getSendCnt() - sendSum.getSuccCnt())/sendSum.getSendCnt()) );
			
			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			if("001".equals(taskVO.getSendRepeat())) {
				logVO.setContent("M1004002");
			} else {
				logVO.setContent("M1004001");
			}
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(sendList == null?0:sendList.size());
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}
	
	/**
	 * 통계분석 단기메일분석 응답시간별 EXCEL
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="/mailRespExcel")
	public void goMailRespExcel(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailRespExcel taskNo = " + taskVO.getTaskNo());
		logger.debug("goMailRespExcel subTaskNo = " + taskVO.getSubTaskNo());
		taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 메일 정보 조회
		TaskVO mailInfo = null;
		try {
			mailInfo = analysisService.getMailInfo(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailInfo error = " + e);
		}
		
		taskVO.setPage(1);
		taskVO.setRows(9999999);
		
		// 응답시간별 목록 조회
		List<RespLogVO> respList = null;
		try {
			respList = analysisService.getMailRespHourList(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailRespHourList error = " + e);
		}
		
		// 응답시간별 합계 조회
		RespLogVO respSum = null;
		try {
			respSum = analysisService.getMailRespHourSum(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailRespHourSum error = " + e);
		}

		try {
			String fileName = "MAIL_RESP_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			if("001".equals(taskVO.getSendRepeat())) {
				fileName = "TASK_STEP_RESP_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			}
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			
			// Data 탭
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			int i = 0;
			
			// 메일정보
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[메일분석]");
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송일시");
			cell = row.createCell(i++); cell.setCellValue( StringUtil.getFDate(mailInfo.getSendDt(), Code.DT_FMT2) + " ~ " + StringUtil.getFDate(mailInfo.getEndDt(), Code.DT_FMT2) );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신자그룹");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getSegNm() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인명");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampNm() );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인목적");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampTy() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("메일명");
			cell = row.createCell(i++); cell.setCellValue( "001".equals(mailInfo.getSendRepeat())?"["+mailInfo.getSubTaskNo()+"차] "+mailInfo.getTaskNm():mailInfo.getTaskNm() );
			
			
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			
			// 목록
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[목록]");
			
			// 엑셀 헤더 생성 ( NO | 시간 | 오픈수 | 유효오픈수 | 클릭수 | 수신거부 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("NO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("시간");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("유효오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("클릭수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신거부");
			
			// 엑셀 내용 추가
			for(RespLogVO resp:respList) {
				i = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(i++); cell.setCellValue( rowNum - 7 );
				cell = row.createCell(i++); cell.setCellValue( resp.getRespTime().substring(0,4)+"-"+resp.getRespTime().substring(4,6)+"-"+resp.getRespTime().substring(6,8)+" "+resp.getRespTime().substring(8)+"시");
				cell = row.createCell(i++); cell.setCellValue( resp.getOpenCnt() );
				cell = row.createCell(i++); cell.setCellValue( resp.getValidCnt() );
				cell = row.createCell(i++); cell.setCellValue( resp.getClickCnt() );
				cell = row.createCell(i++); cell.setCellValue( resp.getBlockCnt() );
			}
			
			// 합계 색상 지정
			XSSFCellStyle footerStyle = wb.createCellStyle();
			IndexedColorMap colorMap = wb.getStylesSource().getIndexedColors();
			footerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( "합계" );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( "" );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( respSum.getOpenCnt() );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( respSum.getValidCnt() );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( respSum.getClickCnt() );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( respSum.getBlockCnt() );
			
			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			if("001".equals(taskVO.getSendRepeat())) {
				logVO.setContent("M1004002");
			} else {
				logVO.setContent("M1004001");
			}
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(respList == null?0:respList.size());
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}
	
	/**
	 * 통계분석 단기메일분석 고객별 EXCEL
	 * @param sendLogVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="/mailCustExcel")
	public void goMailCustExcel(@ModelAttribute SendLogVO sendLogVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailCustExcel taskNo       = " + sendLogVO.getTaskNo());
		logger.debug("goMailCustExcel subTaskNo    = " + sendLogVO.getSubTaskNo());
		logger.debug("goMailCustExcel searchCustId = " + sendLogVO.getSearchCustId());
		logger.debug("goMailCustExcel searchCustEm = " + sendLogVO.getSearchCustEm());
		logger.debug("goMailCustExcel searchCustNm = " + sendLogVO.getSearchCustNm());
		logger.debug("goMailCustExcel searchKind   = " + sendLogVO.getSearchKind());
		
		// 메일 정보 조회
		TaskVO mailInfo = null;
		try {
			TaskVO taskVO = new TaskVO();
			taskVO.setTaskNo(sendLogVO.getTaskNo());
			taskVO.setSubTaskNo(sendLogVO.getSubTaskNo());
			taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
			mailInfo = analysisService.getMailInfo(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getMailInfo error = " + e);
		}
		
		// 검색유형 초기값 설정(검색유형 선택 => 전체)
		if(StringUtil.isNull(sendLogVO.getSearchKind())) sendLogVO.setSearchKind("000");

		String plainCustEm = "";
		if(sendLogVO.getSearchCustEm() != null && !"".equals(sendLogVO.getSearchCustEm())) {
			plainCustEm = sendLogVO.getSearchCustEm();
			sendLogVO.setSearchCustEm(cryptoService.getEncrypt("CUST_EM", sendLogVO.getSearchCustEm()));
		}		
		
		// 페이지 설정
		sendLogVO.setPage(1);
		sendLogVO.setRows(99999999);
		sendLogVO.setStartRow(0);
		// 고객별 로그 목록 조회
		List<SendLogVO> custList = null;
		try {
			custList = analysisService.getCustLogList(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getCustLogList error = " + e);
		}

		try {
			String fileName = "MAIL_CUST_" + sendLogVO.getTaskNo() + "_" + sendLogVO.getSubTaskNo() + "_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			if("001".equals(sendLogVO.getSendRepeat())) {
				fileName = "TASK_STEP_CUST_" + sendLogVO.getTaskNo() + "_" + sendLogVO.getSubTaskNo() + "_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			}
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			
			// Data 탭
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			int i = 0;
			
			// 메일정보
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[메일분석]");
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송일시");
			cell = row.createCell(i++); cell.setCellValue( StringUtil.getFDate(mailInfo.getSendDt(), Code.DT_FMT2) + " ~ " + StringUtil.getFDate(mailInfo.getEndDt(), Code.DT_FMT2) );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신자그룹");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getSegNm() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인명");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampNm() );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인목적");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampTy() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("메일명");
			cell = row.createCell(i++); cell.setCellValue( "001".equals(mailInfo.getSendRepeat())?"["+mailInfo.getSubTaskNo()+"차] "+mailInfo.getTaskNm():mailInfo.getTaskNm() );
			
			
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// 조회
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[조회]");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("ID");
			cell = row.createCell(i++); cell.setCellValue( sendLogVO.getSearchCustId() );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("이메일");
			cell = row.createCell(i++); cell.setCellValue( plainCustEm );

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("고객명");
			cell = row.createCell(i++); cell.setCellValue( sendLogVO.getSearchCustNm() );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("검색유형");
			cell = row.createCell(i++); cell.setCellValue( getSearchKindNm(sendLogVO.getSearchKind()) );

			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// 목록
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[목록]");
			
			// 엑셀 헤더 생성 ( ID | EMAIL | NAME | 발송여부 | 발송시간 | 수신확인 | 수신거부자 | 수신시간 | Message )
			row = sheet.createRow(rowNum++);
			cell = row.createCell(0); cell.setCellStyle(headerStyle); cell.setCellValue("ID");
			cell = row.createCell(1); cell.setCellStyle(headerStyle); cell.setCellValue("EMAIL");
			cell = row.createCell(2); cell.setCellStyle(headerStyle); cell.setCellValue("NAME");
			cell = row.createCell(3); cell.setCellStyle(headerStyle); cell.setCellValue("발송여부");
			cell = row.createCell(4); cell.setCellStyle(headerStyle); cell.setCellValue("발송시간");
			cell = row.createCell(5); cell.setCellStyle(headerStyle); cell.setCellValue("수신확인");
			cell = row.createCell(6); cell.setCellStyle(headerStyle); cell.setCellValue("수신거부");
			cell = row.createCell(7); cell.setCellStyle(headerStyle); cell.setCellValue("수신시간");
			cell = row.createCell(8); cell.setCellStyle(headerStyle); cell.setCellValue("Message");
			
			// 엑셀 내용 추가
			for(SendLogVO log:custList) {
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0); cell.setCellValue( log.getCustId() );		// ID
				cell = row.createCell(1); cell.setCellValue( cryptoService.getDecrypt("CUST_EM", log.getCustEm()) ); 	// EMAIL
				cell = row.createCell(2); cell.setCellValue( log.getCustNm() );		// NAME
				cell = row.createCell(3); cell.setCellValue( "000".equals(log.getSendRcode())?"발송성공":"발송실패" );
				cell = row.createCell(4); cell.setCellValue( StringUtil.getFDate(log.getSendDt()) );
				cell = row.createCell(5); cell.setCellValue( StringUtil.isNull(log.getOpenDt())?"미확인":"수신확인" );
				cell = row.createCell(6); cell.setCellValue( "Y".equals(log.getDeniedType())?"수신거부":"수신허용" );
				cell = row.createCell(7); cell.setCellValue( StringUtil.getFDate(log.getOpenDt()) );
				cell = row.createCell(8); cell.setCellValue( log.getSendMsg() );
			}
			
			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			logVO.setContent("M1004001");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(custList == null?0:custList.size());
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}
	
	/**
	 * 통계분석 단기메일분석 병합분석 화면 EXCEL
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailJoinExcel")
	public void goMailJoinExcel(@ModelAttribute SendLogVO sendLogVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailJoinExcel taskNos    = " + sendLogVO.getTaskNos());
		logger.debug("goMailJoinExcel subTaskNos = " + sendLogVO.getSubTaskNos());
		sendLogVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		String[] taskNo = sendLogVO.getTaskNos().split(",");
		String[] subTaskNo = sendLogVO.getSubTaskNos().split(",");
		List<HashMap<String, Integer>> joinList = new ArrayList<HashMap<String, Integer>>();
		for(int i=0;i<taskNo.length;i++) {
			HashMap<String, Integer> key = new HashMap<String, Integer>();
			key.put("taskNo", Integer.parseInt(taskNo[i]));
			key.put("subTaskNo", Integer.parseInt(subTaskNo[i]));
			joinList.add(key);
		}
		sendLogVO.setJoinList(joinList);
		
		// 병합분석 메일정보 목록 조회
		List<TaskVO> mailList = null;
		try {
			mailList = analysisService.getJoinMailList(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinMailList error = " + e);
		}
		
		// 병합분석 발송결과 조회
		RespLogVO respLog = null;
		try {
			respLog = analysisService.getJoinSendResult(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinSendResult error = " + e);
		}
		
		// 병합분석 세부에러 목록 조회
		List<MailErrorVO> errorList = null;
		try {
			errorList = analysisService.getJoinErrorList(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinErrorList error = " + e);
		}
		
		
		try {
			String fileName = "MAIL_JOIN_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			
			// Data 탭
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			int i = 0;
			
			// 메일정보
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[단기메일 목록]");
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 백분율(%) 형식
			XSSFCellStyle perStyle = wb.createCellStyle();
			perStyle.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
			
			// 합계 색상 지정
			XSSFCellStyle footerStyle = wb.createCellStyle();
			IndexedColorMap colorMap = wb.getStylesSource().getIndexedColors();
			footerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 합계 색상 지정(% 형식 추가)
			XSSFCellStyle footerStylePer = wb.createCellStyle();
			footerStylePer.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStylePer.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			footerStylePer.setDataFormat(wb.createDataFormat().getFormat("0.00%"));

			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("NO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("메일명");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신자그룹");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인명");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("대상수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패수");
			
			int totSendCnt = 0;
			int totSuccCnt = 0;
			int totFailCnt = 0;
			
			for(TaskVO mail : mailList) {
				totSendCnt += mail.getTotCnt();
				totSuccCnt += mail.getSucCnt();
				totFailCnt += mail.getTotCnt() - mail.getSucCnt();
				
				i = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(i++); cell.setCellValue( mail.getTaskNo() );
				cell = row.createCell(i++); cell.setCellValue( mail.getTaskNm() );
				cell = row.createCell(i++); cell.setCellValue( mail.getSegNm() );
				cell = row.createCell(i++); cell.setCellValue( mail.getCampNm() );
				cell = row.createCell(i++); cell.setCellValue( mail.getTotCnt() );
				cell = row.createCell(i++); cell.setCellValue( mail.getSucCnt() );
				cell = row.createCell(i++); cell.setCellValue( mail.getTotCnt() - mail.getSucCnt() );
			}
			
			// 합계
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("합계");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( totSendCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( totSuccCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( totFailCnt );
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			
			// [발송결과] 엑셀 헤더 생성 ( 대상수 | 성공수 | 실패수 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[발송결과]");

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("총 대상수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("총 성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("총 실패수");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( totSendCnt );
			cell = row.createCell(i++); cell.setCellValue( totSuccCnt );
			cell = row.createCell(i++); cell.setCellValue( totFailCnt );

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "비율(%)" );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(totSuccCnt/totSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(totFailCnt/totSendCnt) );
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// [반응결과] 엑셀 헤더 생성 ( 성공수 | 오픈수 | 유효오픈수 | 링크클릭수 | 수신거부 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[반응결과]");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("유효오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("링크클릭수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신거부");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "반응수" );
			cell = row.createCell(i++); cell.setCellValue( totSuccCnt );
			cell = row.createCell(i++); cell.setCellValue( respLog.getOpenCnt() );
			cell = row.createCell(i++); cell.setCellValue( respLog.getValidCnt() );
			cell = row.createCell(i++); cell.setCellValue( respLog.getClickCnt() );
			cell = row.createCell(i++); cell.setCellValue( respLog.getBlockCnt() );

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "성공대비" );
			cell = row.createCell(i++); cell.setCellValue( "" );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSuccCnt==0?0:(respLog.getOpenCnt()/totSuccCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSuccCnt==0?0:(respLog.getValidCnt()/totSuccCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSuccCnt==0?0:(respLog.getClickCnt()/totSuccCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSuccCnt==0?0:(respLog.getBlockCnt()/totSuccCnt) );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "전체대비" );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(totSuccCnt/totSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(respLog.getOpenCnt()/totSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(respLog.getValidCnt()/totSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(respLog.getClickCnt()/totSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(respLog.getBlockCnt()/totSendCnt) );
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// 세부에러 변수 설정
			int syntaxErrCnt = 0;
			int webAgentErrCnt = 0;
			int dbAgentErrCnt = 0;
			int mailBodyErrCnt = 0;
			int domainErrCnt = 0;
			int networkErrCnt = 0;
			int connectErrCnt = 0;
			int heloErrCnt = 0;
			int mailFromErrCnt = 0;
			int rcptToErrCnt = 0;
			int resetErrCnt = 0;
			int dataErrCnt = 0;
			int dotErrCnt = 0;
			int quitErrCnt = 0;
			int sumStep1Err = 0;
			int sumStep2Err = 0;
			int failCnt = 0;
			String step1 = "";
			String step2 = "";
			
			// 세부에러 값 설정
			for(MailErrorVO error:errorList) {
				step1 = error.getStep1();
				step2 = error.getStep2();
				failCnt = error.getCntStep2();
				if("001".equals(step1)) {
					sumStep1Err += failCnt;
					if("001".equals(step2)) {
						syntaxErrCnt = failCnt;
					} else if("002".equals(step2)) {
						webAgentErrCnt = failCnt;
					} else if("003".equals(step2)) {
						dbAgentErrCnt = failCnt;
					} else if("004".equals(step2)) {
						mailBodyErrCnt = failCnt;
					} else if("005".equals(step2)) {
						domainErrCnt = failCnt;
					} else if("006".equals(step2)) {
						networkErrCnt = failCnt;
					}
				} else if("002".equals(step1)) {
					sumStep2Err += failCnt;
					if("001".equals(step2)) {
						connectErrCnt = failCnt;
					} else if("002".equals(step2)) {
						heloErrCnt = failCnt;
					} else if("003".equals(step2)) {
						mailFromErrCnt = failCnt;
					} else if("004".equals(step2)) {
						rcptToErrCnt = failCnt;
					} else if("005".equals(step2)) {
						resetErrCnt = failCnt;
					} else if("006".equals(step2)) {
						dataErrCnt = failCnt;
					} else if("007".equals(step2)) {
						dotErrCnt = failCnt;
					} else if("008".equals(step2)) {
						quitErrCnt = failCnt;
					}
				}
			}
			
			
			// [세부에러 - 메일전송 전단계] 엑셀 헤더 생성 ( Syntax | Web Agent | DB Agent | Mail Body | Domain | Network | 합계 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[세부에러 - 메일전송 전단계]");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Syntax");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Web Agent");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("DB Agent");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Mail Body");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Domain");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Network");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("합계");

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( syntaxErrCnt );
			cell = row.createCell(i++); cell.setCellValue( webAgentErrCnt );
			cell = row.createCell(i++); cell.setCellValue( dbAgentErrCnt );
			cell = row.createCell(i++); cell.setCellValue( mailBodyErrCnt );
			cell = row.createCell(i++); cell.setCellValue( domainErrCnt );
			cell = row.createCell(i++); cell.setCellValue( networkErrCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumStep1Err );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(syntaxErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(webAgentErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(dbAgentErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(mailBodyErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(domainErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(networkErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( totFailCnt==0?0:(sumStep1Err/totFailCnt) );
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// [세부에러 - 메일발송 단계] 엑셀 헤더 생성 ( CONNECT | HELO | MAIL FROM | RCPT TO | RESET | DATA | DOT | QUIT | 합계 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[세부에러 - 메일발송 단계]");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("CONNECT");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("HELO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("MAIL FROM");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("RCPT TO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("RESET");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("DATA");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("DOT");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("QUIT");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("합계");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( connectErrCnt );
			cell = row.createCell(i++); cell.setCellValue( heloErrCnt );
			cell = row.createCell(i++); cell.setCellValue( mailFromErrCnt );
			cell = row.createCell(i++); cell.setCellValue( rcptToErrCnt );
			cell = row.createCell(i++); cell.setCellValue( resetErrCnt );
			cell = row.createCell(i++); cell.setCellValue( dataErrCnt );
			cell = row.createCell(i++); cell.setCellValue( dotErrCnt );
			cell = row.createCell(i++); cell.setCellValue( quitErrCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumStep2Err );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(connectErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(heloErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(mailFromErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(rcptToErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(resetErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(dataErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(dotErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(quitErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( totFailCnt==0?0:(sumStep2Err/totFailCnt) );

			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			logVO.setContent("M1004001");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(totFailCnt);
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}
	
	
	
	/**
	 * 통계분석 단기메일분석 결과요약 화면 EXCEL
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskSummExcel")
	public void goTaskSummExcel(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTaskSummExcel taskNo = " + taskVO.getTaskNo());
		taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 메일 정보 조회
		TaskVO mailInfo = null;
		try {
			mailInfo = analysisService.getTaskInfo(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskInfo error = " + e);
		}
		
		// 발송결과 조회
		MailSummVO sendResult = null;
		try {
			sendResult = analysisService.getTaskSummResult(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskSummResult error = " + e);
		}
		
		// 세부에러 조회
		List<MailErrorVO> detailList = null;
		try {
			detailList = analysisService.getTaskSummDetail(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskSummDetail error = " + e);
		}
		
		try {
			String fileName = "TASK_SUMM_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			
			// Data 탭
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			int i = 0;
			
			// 메일정보
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[메일분석]");
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송일시");
			cell = row.createCell(i++); cell.setCellValue( StringUtil.getFDate(mailInfo.getSendDt(), Code.DT_FMT2) + " ~ " + StringUtil.getFDate(mailInfo.getEndDt(), Code.DT_FMT2) );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신자그룹");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getSegNm() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인명");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampNm() );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인목적");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampTy() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("메일명");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getTaskNm() );
			
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// 백분율(%) 형식
			XSSFCellStyle perStyle = wb.createCellStyle();
			perStyle.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
			
			// [발송결과] 엑셀 헤더 생성 ( 대상수 | 성공수 | 실패수 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[발송결과]");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("대상수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패수");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( sendResult.getSendCnt() );
			cell = row.createCell(i++); cell.setCellValue( sendResult.getSuccCnt() );
			cell = row.createCell(i++); cell.setCellValue( sendResult.getFailCnt() );

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "" );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSendCnt()==0?0:(sendResult.getSuccCnt()/sendResult.getSendCnt()) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSendCnt()==0?0:(sendResult.getFailCnt()/sendResult.getSendCnt()) );
			
			// 전체 실패건수
			int totFailCnt = sendResult.getFailCnt();
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// [반응결과] 엑셀 헤더 생성 ( 성공수 | 오픈수 | 유효오픈수 | 링크클릭수 | 수신거부 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[반응결과]");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("유효오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("링크클릭수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신거부");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "반응수" );
			cell = row.createCell(i++); cell.setCellValue( sendResult.getSuccCnt() );
			cell = row.createCell(i++); cell.setCellValue( sendResult.getOpenCnt() );
			cell = row.createCell(i++); cell.setCellValue( sendResult.getValidCnt() );
			cell = row.createCell(i++); cell.setCellValue( sendResult.getClickCnt() );
			cell = row.createCell(i++); cell.setCellValue( sendResult.getBlockCnt() );

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "성공대비" );
			cell = row.createCell(i++); cell.setCellValue( "" );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSuccCnt()==0?0:(sendResult.getOpenCnt()/sendResult.getSuccCnt()) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSuccCnt()==0?0:(sendResult.getValidCnt()/sendResult.getSuccCnt()) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSuccCnt()==0?0:(sendResult.getClickCnt()/sendResult.getSuccCnt()) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSuccCnt()==0?0:(sendResult.getBlockCnt()/sendResult.getSuccCnt()) );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "전체대비" );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSendCnt()==0?0:(sendResult.getSuccCnt()/sendResult.getSendCnt()) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSendCnt()==0?0:(sendResult.getOpenCnt()/sendResult.getSendCnt()) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSendCnt()==0?0:(sendResult.getValidCnt()/sendResult.getSendCnt()) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSendCnt()==0?0:(sendResult.getClickCnt()/sendResult.getSendCnt()) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( sendResult.getSendCnt()==0?0:(sendResult.getBlockCnt()/sendResult.getSendCnt()) );
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// 세부에러 변수 설정
			int syntaxErrCnt = 0;
			int webAgentErrCnt = 0;
			int dbAgentErrCnt = 0;
			int mailBodyErrCnt = 0;
			int domainErrCnt = 0;
			int networkErrCnt = 0;
			int connectErrCnt = 0;
			int heloErrCnt = 0;
			int mailFromErrCnt = 0;
			int rcptToErrCnt = 0;
			int resetErrCnt = 0;
			int dataErrCnt = 0;
			int dotErrCnt = 0;
			int quitErrCnt = 0;
			int sumStep1Err = 0;
			int sumStep2Err = 0;
			int failCnt = 0;
			String step1 = "";
			String step2 = "";
			
			// 세부에러 값 설정
			for(MailErrorVO error:detailList) {
				step1 = error.getStep1();
				step2 = error.getStep2();
				failCnt = error.getCntStep2();
				if("001".equals(step1)) {
					sumStep1Err += failCnt;
					if("001".equals(step2)) {
						syntaxErrCnt = failCnt;
					} else if("002".equals(step2)) {
						webAgentErrCnt = failCnt;
					} else if("003".equals(step2)) {
						dbAgentErrCnt = failCnt;
					} else if("004".equals(step2)) {
						mailBodyErrCnt = failCnt;
					} else if("005".equals(step2)) {
						domainErrCnt = failCnt;
					} else if("006".equals(step2)) {
						networkErrCnt = failCnt;
					}
				} else if("002".equals(step1)) {
					sumStep2Err += failCnt;
					if("001".equals(step2)) {
						connectErrCnt = failCnt;
					} else if("002".equals(step2)) {
						heloErrCnt = failCnt;
					} else if("003".equals(step2)) {
						mailFromErrCnt = failCnt;
					} else if("004".equals(step2)) {
						rcptToErrCnt = failCnt;
					} else if("005".equals(step2)) {
						resetErrCnt = failCnt;
					} else if("006".equals(step2)) {
						dataErrCnt = failCnt;
					} else if("007".equals(step2)) {
						dotErrCnt = failCnt;
					} else if("008".equals(step2)) {
						quitErrCnt = failCnt;
					}
				}
			}
			
			// 합계 색상 지정
			XSSFCellStyle footerStyle = wb.createCellStyle();
			IndexedColorMap colorMap = wb.getStylesSource().getIndexedColors();
			footerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 합계 색상 지정(% 형식 추가)
			XSSFCellStyle footerStylePer = wb.createCellStyle();
			footerStylePer.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStylePer.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			footerStylePer.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
			
			// [세부에러 - 메일전송 전단계] 엑셀 헤더 생성 ( Syntax | Web Agent | DB Agent | Mail Body | Domain | Network | 합계 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[세부에러 - 메일전송 전단계]");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Syntax");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Web Agent");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("DB Agent");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Mail Body");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Domain");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Network");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("합계");

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( syntaxErrCnt );
			cell = row.createCell(i++); cell.setCellValue( webAgentErrCnt );
			cell = row.createCell(i++); cell.setCellValue( dbAgentErrCnt );
			cell = row.createCell(i++); cell.setCellValue( mailBodyErrCnt );
			cell = row.createCell(i++); cell.setCellValue( domainErrCnt );
			cell = row.createCell(i++); cell.setCellValue( networkErrCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumStep1Err );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(syntaxErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(webAgentErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(dbAgentErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(mailBodyErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(domainErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(networkErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( totFailCnt==0?0:(sumStep1Err/totFailCnt) );
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// [세부에러 - 메일발송 단계] 엑셀 헤더 생성 ( CONNECT | HELO | MAIL FROM | RCPT TO | RESET | DATA | DOT | QUIT | 합계 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[세부에러 - 메일발송 단계]");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("CONNECT");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("HELO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("MAIL FROM");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("RCPT TO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("RESET");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("DATA");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("DOT");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("QUIT");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("합계");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( connectErrCnt );
			cell = row.createCell(i++); cell.setCellValue( heloErrCnt );
			cell = row.createCell(i++); cell.setCellValue( mailFromErrCnt );
			cell = row.createCell(i++); cell.setCellValue( rcptToErrCnt );
			cell = row.createCell(i++); cell.setCellValue( resetErrCnt );
			cell = row.createCell(i++); cell.setCellValue( dataErrCnt );
			cell = row.createCell(i++); cell.setCellValue( dotErrCnt );
			cell = row.createCell(i++); cell.setCellValue( quitErrCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumStep2Err );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(connectErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(heloErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(mailFromErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(rcptToErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(resetErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(dataErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(dotErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(quitErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( totFailCnt==0?0:(sumStep2Err/totFailCnt) );

			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			logVO.setContent("M1004002");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(totFailCnt);
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}
	
	/**
	 * 통계분석 정기메일분석 세부에러 EXCEL
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="/taskErrorExcel")
	public void goTaskErrorExcel(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTaskErrorExcel taskNo = " + taskVO.getTaskNo());
		taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 메일 정보 조회
		TaskVO mailInfo = null;
		try {
			mailInfo = analysisService.getTaskInfo(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskInfo error = " + e);
		}
		
		// 세부에러 목록 조회
		List<MailErrorVO> errorList = null;
		try {
			errorList = analysisService.getTaskErrorList(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskErrorList error = " + e);
		}

		try {
			String fileName = "TASK_ERROR_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			
			// Data 탭
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			int i = 0;
			
			// 메일정보
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[메일분석]");
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송일시");
			cell = row.createCell(i++); cell.setCellValue( StringUtil.getFDate(mailInfo.getSendDt(), Code.DT_FMT2) + " ~ " + StringUtil.getFDate(mailInfo.getEndDt(), Code.DT_FMT2) );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신자그룹");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getSegNm() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인명");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampNm() );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인목적");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampTy() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("메일명");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getTaskNm() );
			
			
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);

			
			// 메일 에러 종류별 건수
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[메일 에러 종류별 건수]");
			
			// 엑셀 헤더 생성 ( 대분류건수 | 중분류건수 | 소분류건수 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("대분류건수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("중분류건수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("소분류건수");
			
			// 엑셀 내용 추가
			for(MailErrorVO error:errorList) {
				i = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(i++); cell.setCellValue( error.getNmStep1() + " : " + error.getCntStep1() );
				cell = row.createCell(i++); cell.setCellValue( error.getNmStep2() + " : " + error.getCntStep2() );
				if(StringUtil.isNull(error.getNmStep3())) {
					cell = row.createCell(i++); cell.setCellValue( error.getNmStep2() + " : " + error.getCntStep2() );
				} else {
					cell = row.createCell(i++); cell.setCellValue( error.getNmStep3() + " : " + error.getCntStep3() );
				}
			}
			
			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			logVO.setContent("M1004002");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(errorList == null?0:errorList.size());
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}
	
	/**
	 * 통계분석 정기메일분석 도메인별 EXCEL
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="/taskDomainExcel")
	public void goTaskDomainExcel(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTaskDomainExcel taskNo = " + taskVO.getTaskNo());
		taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 메일 정보 조회
		TaskVO mailInfo = null;
		try {
			mailInfo = analysisService.getTaskInfo(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskInfo error = " + e);
		}
		
		// 도메인별 목록 조회
		List<MailDomainVO> domainList = null;
		try {
			domainList = analysisService.getTaskDomainList(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskDomainList error = " + e);
		}

		try {
			String fileName = "TASK_DOMAIN_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			
			// Data 탭
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			int i = 0;
			
			// 메일정보
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[메일분석]");
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송일시");
			cell = row.createCell(i++); cell.setCellValue( StringUtil.getFDate(mailInfo.getSendDt(), Code.DT_FMT2) + " ~ " + StringUtil.getFDate(mailInfo.getEndDt(), Code.DT_FMT2) );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신자그룹");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getSegNm() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인명");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampNm() );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인목적");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampTy() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("메일명");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getTaskNm() );
			
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// 목록
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[목록]");
			
			// 엑셀 헤더 생성 ( NO | 도메인명 | 발송수 | 성공수 | 실패수 | 발송단계에러 | 성공률 | 실패율 | 발생단계에러율 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("NO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("도메인명");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송단계에러");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공률");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패율");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발생단계에러율");
			
			// 백분율(%) 형식
			XSSFCellStyle perStyle = wb.createCellStyle();
			perStyle.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
			
			int sumSendCnt = 0;
			int sumSuccCnt = 0;
			int sumFailCnt = 0;
			int sumErrorCnt = 0;
			
			// 엑셀 내용 추가
			for(MailDomainVO domain:domainList) {
				sumSendCnt += domain.getSendCnt();
				sumSuccCnt += domain.getSuccCnt();
				sumFailCnt += domain.getSendCnt() - domain.getSuccCnt();
				sumErrorCnt += domain.getErrorCnt();
				
				i = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(i++); cell.setCellValue( rowNum - 7 );
				cell = row.createCell(i++); cell.setCellValue( domain.getDomainNm() );
				cell = row.createCell(i++); cell.setCellValue( domain.getSendCnt() );
				cell = row.createCell(i++); cell.setCellValue( domain.getSuccCnt() );
				cell = row.createCell(i++); cell.setCellValue( (domain.getSendCnt() - domain.getSuccCnt()) );
				cell = row.createCell(i++); cell.setCellValue( domain.getErrorCnt() );
				cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( domain.getSendCnt()==0?0:(domain.getSuccCnt()/domain.getSendCnt()) );
				cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( domain.getSendCnt()==0?0:((domain.getSendCnt() - domain.getSuccCnt())/domain.getSendCnt()) );
				cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( (domain.getSendCnt() - domain.getSuccCnt())==0?0:(domain.getErrorCnt()/(domain.getSendCnt() - domain.getSuccCnt())) );
			}
			
			// 합계 색상 지정
			XSSFCellStyle footerStyle = wb.createCellStyle();
			IndexedColorMap colorMap = wb.getStylesSource().getIndexedColors();
			footerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 합계 색상 지정(% 형식 추가)
			XSSFCellStyle footerStylePer = wb.createCellStyle();
			footerStylePer.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStylePer.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			footerStylePer.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( "합계" );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( "" );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumSendCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumSuccCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumFailCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumErrorCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( sumSendCnt==0?0:(sumSuccCnt/sumSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( sumSendCnt==0?0:(sumFailCnt/sumSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( sumSendCnt-sumSuccCnt==0?0:sumErrorCnt/(sumSendCnt-sumSuccCnt) );
			
			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			logVO.setContent("M1004002");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(domainList == null?0:domainList.size());
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}
	
	/**
	 * 통계분석 정기메일분석 발송시간별 EXCEL
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="/taskSendExcel")
	public void goTaskSendExcel(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTaskSendExcel taskNo = " + taskVO.getTaskNo());
		taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 메일 정보 조회
		TaskVO mailInfo = null;
		try {
			mailInfo = analysisService.getTaskInfo(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskInfo error = " + e);
		}
		
		taskVO.setPage(1);
		taskVO.setRows(9999999);
		
		// 발송시간별 목록 조회
		List<SendLogVO> sendList = null;
		try {
			sendList = analysisService.getTaskSendHourList(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskSendHourList error = " + e);
		}
		
		// 발송시간별 합계 조회
		SendLogVO sendSum = null;
		try {
			sendSum = analysisService.getTaskSendHourSum(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskSendHourSum error = " + e);
		}

		try {
			String fileName = "TASK_SEND_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			
			// Data 탭
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			int i = 0;
			
			// 메일정보
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[메일분석]");
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송일시");
			cell = row.createCell(i++); cell.setCellValue( StringUtil.getFDate(mailInfo.getSendDt(), Code.DT_FMT2) + " ~ " + StringUtil.getFDate(mailInfo.getEndDt(), Code.DT_FMT2) );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신자그룹");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getSegNm() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인명");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampNm() );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인목적");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampTy() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("메일명");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getTaskNm() );
			
			
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// 목록
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[목록]");
			
			// 엑셀 헤더 생성 ( NO | 시간 | 발송수 | 성공수 | 실패수 | 성공률 | 실패율 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("NO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("시간");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공률");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패율");
			
			// 백분율(%) 형식
			XSSFCellStyle perStyle = wb.createCellStyle();
			perStyle.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
			
			// 엑셀 내용 추가
			for(SendLogVO send:sendList) {
				i = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(i++); cell.setCellValue( rowNum - 7 );
				cell = row.createCell(i++); cell.setCellValue( send.getSendTime().substring(0,4)+"-"+send.getSendTime().substring(4,6)+"-"+send.getSendTime().substring(6,8)+" "+send.getSendTime().substring(8)+"시");
				cell = row.createCell(i++); cell.setCellValue( send.getSendCnt() );
				cell = row.createCell(i++); cell.setCellValue( send.getSuccCnt() );
				cell = row.createCell(i++); cell.setCellValue( (send.getSendCnt() - send.getSuccCnt()) );
				cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( send.getSendCnt()==0?0:(send.getSuccCnt()/send.getSendCnt()) );
				cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( send.getSendCnt()==0?0:((send.getSendCnt() - send.getSuccCnt())/send.getSendCnt()) );
			}
			
			// 합계 색상 지정
			XSSFCellStyle footerStyle = wb.createCellStyle();
			IndexedColorMap colorMap = wb.getStylesSource().getIndexedColors();
			footerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 합계 색상 지정(% 형식 추가)
			XSSFCellStyle footerStylePer = wb.createCellStyle();
			footerStylePer.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStylePer.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			footerStylePer.setDataFormat(wb.createDataFormat().getFormat("0.00%"));

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( "합계" );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( "" );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sendSum.getSendCnt() );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sendSum.getSuccCnt() );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( (sendSum.getSendCnt() - sendSum.getSuccCnt()) );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( sendSum.getSendCnt()==0?0:(sendSum.getSuccCnt()/sendSum.getSendCnt()) );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( sendSum.getSendCnt()==0?0:((sendSum.getSendCnt() - sendSum.getSuccCnt())/sendSum.getSendCnt()) );
			
			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			logVO.setContent("M1004002");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(sendList == null?0:sendList.size());
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}
	
	/**
	 * 통계분석 정기메일분석 응답시간별 EXCEL
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="/taskRespExcel")
	public void goTaskRespExcel(@ModelAttribute TaskVO taskVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTaskRespExcel taskNo = " + taskVO.getTaskNo());
		taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 메일 정보 조회
		TaskVO mailInfo = null;
		try {
			mailInfo = analysisService.getTaskInfo(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskInfo error = " + e);
		}
		
		taskVO.setPage(1);
		taskVO.setRows(9999999);
		
		// 응답시간별 목록 조회
		List<RespLogVO> respList = null;
		try {
			respList = analysisService.getTaskRespHourList(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskRespHourList error = " + e);
		}
		
		// 응답시간별 합계 조회
		RespLogVO respSum = null;
		try {
			respSum = analysisService.getTaskRespHourSum(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskRespHourSum error = " + e);
		}

		try {
			String fileName = "TASK_RESP_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			
			// Data 탭
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			int i = 0;
			
			// 메일정보
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[메일분석]");
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송일시");
			cell = row.createCell(i++); cell.setCellValue( StringUtil.getFDate(mailInfo.getSendDt(), Code.DT_FMT2) + " ~ " + StringUtil.getFDate(mailInfo.getEndDt(), Code.DT_FMT2) );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신자그룹");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getSegNm() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인명");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampNm() );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인목적");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getCampTy() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("메일명");
			cell = row.createCell(i++); cell.setCellValue( mailInfo.getTaskNm() );
			
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// 목록
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[목록]");
			
			// 엑셀 헤더 생성 ( NO | 시간 | 오픈수 | 유효오픈수 | 클릭수 | 수신거부 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("NO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("시간");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("유효오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("클릭수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신거부");
			
			// 엑셀 내용 추가
			for(RespLogVO resp:respList) {
				i = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(i++); cell.setCellValue( rowNum - 7 );
				cell = row.createCell(i++); cell.setCellValue( resp.getRespTime().substring(0,4)+"-"+resp.getRespTime().substring(4,6)+"-"+resp.getRespTime().substring(6,8)+" "+resp.getRespTime().substring(8)+"시");
				cell = row.createCell(i++); cell.setCellValue( resp.getOpenCnt() );
				cell = row.createCell(i++); cell.setCellValue( resp.getValidCnt() );
				cell = row.createCell(i++); cell.setCellValue( resp.getClickCnt() );
				cell = row.createCell(i++); cell.setCellValue( resp.getBlockCnt() );
			}
			
			// 합계 색상 지정
			XSSFCellStyle footerStyle = wb.createCellStyle();
			IndexedColorMap colorMap = wb.getStylesSource().getIndexedColors();
			footerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( "합계" );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( "" );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( respSum.getOpenCnt() );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( respSum.getValidCnt() );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( respSum.getClickCnt() );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( respSum.getBlockCnt() );
			
			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			logVO.setContent("M1004002");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(respList == null?0:respList.size());
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}
	
	/**
	 * 통계분석 정기메일분석 응답시간별 EXCEL
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="/taskCustExcel")
	public void goTaskCustExcel(@ModelAttribute SendLogVO sendLogVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailCustExcel taskNo       = " + sendLogVO.getTaskNo());
		logger.debug("goMailCustExcel subTaskNo    = " + sendLogVO.getSubTaskNo());
		logger.debug("goMailCustExcel searchCustId = " + sendLogVO.getSearchCustId());
		logger.debug("goMailCustExcel searchCustEm = " + sendLogVO.getSearchCustEm());
		logger.debug("goMailCustExcel searchCustNm = " + sendLogVO.getSearchCustNm());
		logger.debug("goMailCustExcel searchKind   = " + sendLogVO.getSearchKind());
		
		// 메일 정보 조회
		TaskVO taskInfo = null;
		try {
			TaskVO taskVO = new TaskVO();
			taskVO.setTaskNo(sendLogVO.getTaskNo());
			taskVO.setSubTaskNo(sendLogVO.getSubTaskNo());
			taskVO.setUilang((String)session.getAttribute("NEO_UILANG"));
			taskInfo = analysisService.getTaskInfo(taskVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskInfo error = " + e);
		}
		
		// 검색유형 초기값 설정(검색유형 선택 => 전체)
		if(StringUtil.isNull(sendLogVO.getSearchKind())) sendLogVO.setSearchKind("000");

		String plainCustEm = "";
		if(sendLogVO.getSearchCustEm() != null && !"".equals(sendLogVO.getSearchCustEm())) {
			plainCustEm = sendLogVO.getSearchCustEm();
			sendLogVO.setSearchCustEm(cryptoService.getEncrypt("CUST_EM", sendLogVO.getSearchCustEm()));
		}		
		
		// 페이지 설정
		sendLogVO.setPage(1);
		sendLogVO.setRows(99999999);
		
		// 고객별 로그 목록 조회
		List<SendLogVO> custList = null;
		try {
			custList = analysisService.getTaskCustLogList(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getTaskCustLogList error = " + e);
		}

		try {
			String fileName = "TASK_CUST_" + sendLogVO.getTaskNo() + "_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			if("001".equals(sendLogVO.getSendRepeat())) {
				fileName = "TASK_STEP_CUST_" + sendLogVO.getTaskNo() + "_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			}
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			
			// Data 탭
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			int i = 0;
			
			// 메일정보
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[메일분석]");
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송일시");
			cell = row.createCell(i++); cell.setCellValue( StringUtil.getFDate(taskInfo.getSendDt(), Code.DT_FMT2) + " ~ " + StringUtil.getFDate(taskInfo.getEndDt(), Code.DT_FMT2) );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신자그룹");
			cell = row.createCell(i++); cell.setCellValue( taskInfo.getSegNm() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인명");
			cell = row.createCell(i++); cell.setCellValue( taskInfo.getCampNm() );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인목적");
			cell = row.createCell(i++); cell.setCellValue( taskInfo.getCampTy() );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("메일명");
			cell = row.createCell(i++); cell.setCellValue("[정기메일]" + taskInfo.getTaskNm() );
			
			
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// 조회
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[조회]");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("ID");
			cell = row.createCell(i++); cell.setCellValue( sendLogVO.getSearchCustId() );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("이메일");
			cell = row.createCell(i++); cell.setCellValue( plainCustEm );

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("고객명");
			cell = row.createCell(i++); cell.setCellValue( sendLogVO.getSearchCustNm() );
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("검색유형");
			cell = row.createCell(i++); cell.setCellValue( getSearchKindNm(sendLogVO.getSearchKind()) );

			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// 목록
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[목록]");
			
			// 엑셀 헤더 생성 (차수| ID | EMAIL | NAME | 발송여부 | 발송시간 | 수신확인 | 수신거부자 | 수신시간 | Message )
			row = sheet.createRow(rowNum++);
			cell = row.createCell(0); cell.setCellStyle(headerStyle); cell.setCellValue("차수");
			cell = row.createCell(1); cell.setCellStyle(headerStyle); cell.setCellValue("ID");
			cell = row.createCell(2); cell.setCellStyle(headerStyle); cell.setCellValue("EMAIL");
			cell = row.createCell(3); cell.setCellStyle(headerStyle); cell.setCellValue("NAME");
			cell = row.createCell(4); cell.setCellStyle(headerStyle); cell.setCellValue("발송여부");
			cell = row.createCell(5); cell.setCellStyle(headerStyle); cell.setCellValue("발송시간");
			cell = row.createCell(6); cell.setCellStyle(headerStyle); cell.setCellValue("수신확인");
			cell = row.createCell(7); cell.setCellStyle(headerStyle); cell.setCellValue("수신거부");
			cell = row.createCell(8); cell.setCellStyle(headerStyle); cell.setCellValue("수신시간");
			cell = row.createCell(9); cell.setCellStyle(headerStyle); cell.setCellValue("Message");
			
			// 엑셀 내용 추가
			for(SendLogVO log:custList) {
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0); cell.setCellValue( log.getSubTaskNo() );	// 차수
				cell = row.createCell(1); cell.setCellValue( log.getCustId() );		// ID
				cell = row.createCell(2); cell.setCellValue( cryptoService.getDecrypt("CUST_EM", log.getCustEm()) ); 	// EMAIL
				cell = row.createCell(3); cell.setCellValue( log.getCustNm() );		// NAME
				cell = row.createCell(4); cell.setCellValue( "000".equals(log.getSendRcode())?"발송성공":"발송실패" );
				cell = row.createCell(5); cell.setCellValue( StringUtil.getFDate(log.getSendDt()) );
				cell = row.createCell(6); cell.setCellValue( StringUtil.isNull(log.getOpenDt())?"미확인":"수신확인" );
				cell = row.createCell(7); cell.setCellValue( "Y".equals(log.getDeniedType())?"수신거부":"수신허용" );
				cell = row.createCell(8); cell.setCellValue( StringUtil.getFDate(log.getOpenDt()) );
				cell = row.createCell(9); cell.setCellValue( log.getSendMsg() );
			}
			
			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			logVO.setContent("M1004001");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(custList == null?0:custList.size());
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}	
	
	/**
	 * 통계분석 정기메일분석 병합분석 화면 EXCEL
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskJoinExcel")
	public void goTaskJoinExcel(@ModelAttribute SendLogVO sendLogVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTaskJoinExcel taskNos    = " + sendLogVO.getTaskNos());
		sendLogVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		String[] taskNo = sendLogVO.getTaskNos().split(",");
		List<HashMap<String, Integer>> joinList = new ArrayList<HashMap<String, Integer>>();
		for(int i=0;i<taskNo.length;i++) {
			HashMap<String, Integer> key = new HashMap<String, Integer>();
			key.put("taskNo", Integer.parseInt(taskNo[i]));
			joinList.add(key);
		}
		sendLogVO.setJoinList(joinList);
		
		// 병합분석 메일정보 목록 조회
		List<TaskVO> mailList = null;
		try {
			mailList = analysisService.getJoinTaskList(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinTaskList error = " + e);
		}
		
		// 병합분석 발송결과 조회
		RespLogVO respLog = null;
		try {
			respLog = analysisService.getJoinSendResultTask(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinSendResultTask error = " + e);
		}
		
		// 병합분석 세부에러 목록 조회
		List<MailErrorVO> errorList = null;
		try {
			errorList = analysisService.getJoinErrorListTask(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinErrorListTask error = " + e);
		}
		
		try {
			String fileName = "TASK_JOIN_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			
			// Data 탭
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			int i = 0;
			
			// 메일정보
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[정기메일 목록]");
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 백분율(%) 형식
			XSSFCellStyle perStyle = wb.createCellStyle();
			perStyle.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
			
			// 합계 색상 지정
			XSSFCellStyle footerStyle = wb.createCellStyle();
			IndexedColorMap colorMap = wb.getStylesSource().getIndexedColors();
			footerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 합계 색상 지정(% 형식 추가)
			XSSFCellStyle footerStylePer = wb.createCellStyle();
			footerStylePer.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStylePer.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			footerStylePer.setDataFormat(wb.createDataFormat().getFormat("0.00%"));

			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("NO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("메일명");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신자그룹");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인명");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("대상수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패수");
			
			int totSendCnt = 0;
			int totSuccCnt = 0;
			int totFailCnt = 0;
			
			for(TaskVO mail : mailList) {
				totSendCnt += mail.getTotCnt();
				totSuccCnt += mail.getSucCnt();
				totFailCnt += mail.getTotCnt() - mail.getSucCnt();
				
				i = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(i++); cell.setCellValue( mail.getTaskNo() );
				cell = row.createCell(i++); cell.setCellValue( mail.getTaskNm() );
				cell = row.createCell(i++); cell.setCellValue( mail.getSegNm() );
				cell = row.createCell(i++); cell.setCellValue( mail.getCampNm() );
				cell = row.createCell(i++); cell.setCellValue( mail.getTotCnt() );
				cell = row.createCell(i++); cell.setCellValue( mail.getSucCnt() );
				cell = row.createCell(i++); cell.setCellValue( mail.getTotCnt() - mail.getSucCnt() );
			}
			
			// 합계
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("합계");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( totSendCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( totSuccCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( totFailCnt );
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			
			// [발송결과] 엑셀 헤더 생성 ( 대상수 | 성공수 | 실패수 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[발송결과]");

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("총 대상수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("총 성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("총 실패수");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( totSendCnt );
			cell = row.createCell(i++); cell.setCellValue( totSuccCnt );
			cell = row.createCell(i++); cell.setCellValue( totFailCnt );

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "비율(%)" );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(totSuccCnt/totSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(totFailCnt/totSendCnt) );
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// [반응결과] 엑셀 헤더 생성 ( 성공수 | 오픈수 | 유효오픈수 | 링크클릭수 | 수신거부 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[반응결과]");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("유효오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("링크클릭수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신거부");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "반응수" );
			cell = row.createCell(i++); cell.setCellValue( totSuccCnt );
			cell = row.createCell(i++); cell.setCellValue( respLog.getOpenCnt() );
			cell = row.createCell(i++); cell.setCellValue( respLog.getValidCnt() );
			cell = row.createCell(i++); cell.setCellValue( respLog.getClickCnt() );
			cell = row.createCell(i++); cell.setCellValue( respLog.getBlockCnt() );

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "성공대비" );
			cell = row.createCell(i++); cell.setCellValue( "" );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSuccCnt==0?0:(respLog.getOpenCnt()/totSuccCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSuccCnt==0?0:(respLog.getValidCnt()/totSuccCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSuccCnt==0?0:(respLog.getClickCnt()/totSuccCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSuccCnt==0?0:(respLog.getBlockCnt()/totSuccCnt) );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "전체대비" );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(totSuccCnt/totSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(respLog.getOpenCnt()/totSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(respLog.getValidCnt()/totSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(respLog.getClickCnt()/totSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(respLog.getBlockCnt()/totSendCnt) );
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// 세부에러 변수 설정
			int syntaxErrCnt = 0;
			int webAgentErrCnt = 0;
			int dbAgentErrCnt = 0;
			int mailBodyErrCnt = 0;
			int domainErrCnt = 0;
			int networkErrCnt = 0;
			int connectErrCnt = 0;
			int heloErrCnt = 0;
			int mailFromErrCnt = 0;
			int rcptToErrCnt = 0;
			int resetErrCnt = 0;
			int dataErrCnt = 0;
			int dotErrCnt = 0;
			int quitErrCnt = 0;
			int sumStep1Err = 0;
			int sumStep2Err = 0;
			int failCnt = 0;
			String step1 = "";
			String step2 = "";
			
			// 세부에러 값 설정
			for(MailErrorVO error:errorList) {
				step1 = error.getStep1();
				step2 = error.getStep2();
				failCnt = error.getCntStep2();
				if("001".equals(step1)) {
					sumStep1Err += failCnt;
					if("001".equals(step2)) {
						syntaxErrCnt = failCnt;
					} else if("002".equals(step2)) {
						webAgentErrCnt = failCnt;
					} else if("003".equals(step2)) {
						dbAgentErrCnt = failCnt;
					} else if("004".equals(step2)) {
						mailBodyErrCnt = failCnt;
					} else if("005".equals(step2)) {
						domainErrCnt = failCnt;
					} else if("006".equals(step2)) {
						networkErrCnt = failCnt;
					}
				} else if("002".equals(step1)) {
					sumStep2Err += failCnt;
					if("001".equals(step2)) {
						connectErrCnt = failCnt;
					} else if("002".equals(step2)) {
						heloErrCnt = failCnt;
					} else if("003".equals(step2)) {
						mailFromErrCnt = failCnt;
					} else if("004".equals(step2)) {
						rcptToErrCnt = failCnt;
					} else if("005".equals(step2)) {
						resetErrCnt = failCnt;
					} else if("006".equals(step2)) {
						dataErrCnt = failCnt;
					} else if("007".equals(step2)) {
						dotErrCnt = failCnt;
					} else if("008".equals(step2)) {
						quitErrCnt = failCnt;
					}
				}
			}
			
			
			// [세부에러 - 메일전송 전단계] 엑셀 헤더 생성 ( Syntax | Web Agent | DB Agent | Mail Body | Domain | Network | 합계 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[세부에러 - 메일전송 전단계]");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Syntax");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Web Agent");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("DB Agent");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Mail Body");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Domain");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Network");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("합계");

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( syntaxErrCnt );
			cell = row.createCell(i++); cell.setCellValue( webAgentErrCnt );
			cell = row.createCell(i++); cell.setCellValue( dbAgentErrCnt );
			cell = row.createCell(i++); cell.setCellValue( mailBodyErrCnt );
			cell = row.createCell(i++); cell.setCellValue( domainErrCnt );
			cell = row.createCell(i++); cell.setCellValue( networkErrCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumStep1Err );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(syntaxErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(webAgentErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(dbAgentErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(mailBodyErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(domainErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(networkErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( totFailCnt==0?0:(sumStep1Err/totFailCnt) );
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// [세부에러 - 메일발송 단계] 엑셀 헤더 생성 ( CONNECT | HELO | MAIL FROM | RCPT TO | RESET | DATA | DOT | QUIT | 합계 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[세부에러 - 메일발송 단계]");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("CONNECT");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("HELO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("MAIL FROM");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("RCPT TO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("RESET");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("DATA");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("DOT");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("QUIT");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("합계");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( connectErrCnt );
			cell = row.createCell(i++); cell.setCellValue( heloErrCnt );
			cell = row.createCell(i++); cell.setCellValue( mailFromErrCnt );
			cell = row.createCell(i++); cell.setCellValue( rcptToErrCnt );
			cell = row.createCell(i++); cell.setCellValue( resetErrCnt );
			cell = row.createCell(i++); cell.setCellValue( dataErrCnt );
			cell = row.createCell(i++); cell.setCellValue( dotErrCnt );
			cell = row.createCell(i++); cell.setCellValue( quitErrCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumStep2Err );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(connectErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(heloErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(mailFromErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(rcptToErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(resetErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(dataErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(dotErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(quitErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( totFailCnt==0?0:(sumStep2Err/totFailCnt) );

			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			logVO.setContent("M1004002");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(totFailCnt);
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}
	
	/**
	 * 통계분석 정기메일분석 차수별 병합분석 화면 EXCEL
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskStepJoinExcel")
	public void goTaskStepJoinExcel(@ModelAttribute SendLogVO sendLogVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goTaskStepJoinExcel taskNos    = " + sendLogVO.getTaskNos());
		logger.debug("goTaskStepJoinExcel subTaskNos = " + sendLogVO.getSubTaskNos());
		sendLogVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		String[] taskNo = sendLogVO.getTaskNos().split(",");
		String[] subTaskNo = sendLogVO.getSubTaskNos().split(",");
		List<HashMap<String, Integer>> joinList = new ArrayList<HashMap<String, Integer>>();
		for(int i=0;i<taskNo.length;i++) {
			HashMap<String, Integer> key = new HashMap<String, Integer>();
			key.put("taskNo", Integer.parseInt(taskNo[i]));
			key.put("subTaskNo", Integer.parseInt(subTaskNo[i]));
			joinList.add(key);
		}
		sendLogVO.setJoinList(joinList);
		
		// 병합분석 메일정보 목록 조회
		List<TaskVO> mailList = null;
		try {
			mailList = analysisService.getJoinMailList(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinMailList error = " + e);
		}
		
		// 병합분석 발송결과 조회
		RespLogVO respLog = null;
		try {
			respLog = analysisService.getJoinSendResult(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinSendResult error = " + e);
		}
		
		// 병합분석 세부에러 목록 조회
		List<MailErrorVO> errorList = null;
		try {
			errorList = analysisService.getJoinErrorList(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinErrorList error = " + e);
		}
		
		
		try {
			String fileName = "TASK_STEP_JOIN_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			
			// Data 탭
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			int i = 0;
			
			// 메일정보
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[정기메일 차수별목록]");
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 백분율(%) 형식
			XSSFCellStyle perStyle = wb.createCellStyle();
			perStyle.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
			
			// 합계 색상 지정
			XSSFCellStyle footerStyle = wb.createCellStyle();
			IndexedColorMap colorMap = wb.getStylesSource().getIndexedColors();
			footerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 합계 색상 지정(% 형식 추가)
			XSSFCellStyle footerStylePer = wb.createCellStyle();
			footerStylePer.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStylePer.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			footerStylePer.setDataFormat(wb.createDataFormat().getFormat("0.00%"));

			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("NO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("메일명");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("대상수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패수");
			
			int totSendCnt = 0;
			int totSuccCnt = 0;
			int totFailCnt = 0;
			
			for(TaskVO mail : mailList) {
				totSendCnt += mail.getTotCnt();
				totSuccCnt += mail.getSucCnt();
				totFailCnt += mail.getTotCnt() - mail.getSucCnt();
				
				i = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(i++); cell.setCellValue( mail.getTaskNo() );
				cell = row.createCell(i++); cell.setCellValue( "[" + mail.getSubTaskNo() + "차] " + mail.getTaskNm() );
				cell = row.createCell(i++); cell.setCellValue( mail.getTotCnt() );
				cell = row.createCell(i++); cell.setCellValue( mail.getSucCnt() );
				cell = row.createCell(i++); cell.setCellValue( mail.getTotCnt() - mail.getSucCnt() );
			}
			
			// 합계
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("합계");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( totSendCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( totSuccCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( totFailCnt );
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			
			// [발송결과] 엑셀 헤더 생성 ( 대상수 | 성공수 | 실패수 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[발송결과]");

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("총 대상수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("총 성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("총 실패수");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( totSendCnt );
			cell = row.createCell(i++); cell.setCellValue( totSuccCnt );
			cell = row.createCell(i++); cell.setCellValue( totFailCnt );

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "비율(%)" );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(totSuccCnt/totSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(totFailCnt/totSendCnt) );
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// [반응결과] 엑셀 헤더 생성 ( 성공수 | 오픈수 | 유효오픈수 | 링크클릭수 | 수신거부 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[반응결과]");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("유효오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("링크클릭수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신거부");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "반응수" );
			cell = row.createCell(i++); cell.setCellValue( totSuccCnt );
			cell = row.createCell(i++); cell.setCellValue( respLog.getOpenCnt() );
			cell = row.createCell(i++); cell.setCellValue( respLog.getValidCnt() );
			cell = row.createCell(i++); cell.setCellValue( respLog.getClickCnt() );
			cell = row.createCell(i++); cell.setCellValue( respLog.getBlockCnt() );

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "성공대비" );
			cell = row.createCell(i++); cell.setCellValue( "" );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSuccCnt==0?0:(respLog.getOpenCnt()/totSuccCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSuccCnt==0?0:(respLog.getValidCnt()/totSuccCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSuccCnt==0?0:(respLog.getClickCnt()/totSuccCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSuccCnt==0?0:(respLog.getBlockCnt()/totSuccCnt) );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "전체대비" );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(totSuccCnt/totSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(respLog.getOpenCnt()/totSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(respLog.getValidCnt()/totSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(respLog.getClickCnt()/totSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(respLog.getBlockCnt()/totSendCnt) );
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// 세부에러 변수 설정
			int syntaxErrCnt = 0;
			int webAgentErrCnt = 0;
			int dbAgentErrCnt = 0;
			int mailBodyErrCnt = 0;
			int domainErrCnt = 0;
			int networkErrCnt = 0;
			int connectErrCnt = 0;
			int heloErrCnt = 0;
			int mailFromErrCnt = 0;
			int rcptToErrCnt = 0;
			int resetErrCnt = 0;
			int dataErrCnt = 0;
			int dotErrCnt = 0;
			int quitErrCnt = 0;
			int sumStep1Err = 0;
			int sumStep2Err = 0;
			int failCnt = 0;
			String step1 = "";
			String step2 = "";
			
			// 세부에러 값 설정
			for(MailErrorVO error:errorList) {
				step1 = error.getStep1();
				step2 = error.getStep2();
				failCnt = error.getCntStep2();
				if("001".equals(step1)) {
					sumStep1Err += failCnt;
					if("001".equals(step2)) {
						syntaxErrCnt = failCnt;
					} else if("002".equals(step2)) {
						webAgentErrCnt = failCnt;
					} else if("003".equals(step2)) {
						dbAgentErrCnt = failCnt;
					} else if("004".equals(step2)) {
						mailBodyErrCnt = failCnt;
					} else if("005".equals(step2)) {
						domainErrCnt = failCnt;
					} else if("006".equals(step2)) {
						networkErrCnt = failCnt;
					}
				} else if("002".equals(step1)) {
					sumStep2Err += failCnt;
					if("001".equals(step2)) {
						connectErrCnt = failCnt;
					} else if("002".equals(step2)) {
						heloErrCnt = failCnt;
					} else if("003".equals(step2)) {
						mailFromErrCnt = failCnt;
					} else if("004".equals(step2)) {
						rcptToErrCnt = failCnt;
					} else if("005".equals(step2)) {
						resetErrCnt = failCnt;
					} else if("006".equals(step2)) {
						dataErrCnt = failCnt;
					} else if("007".equals(step2)) {
						dotErrCnt = failCnt;
					} else if("008".equals(step2)) {
						quitErrCnt = failCnt;
					}
				}
			}
			
			
			// [세부에러 - 메일전송 전단계] 엑셀 헤더 생성 ( Syntax | Web Agent | DB Agent | Mail Body | Domain | Network | 합계 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[세부에러 - 메일전송 전단계]");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Syntax");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Web Agent");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("DB Agent");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Mail Body");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Domain");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Network");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("합계");

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( syntaxErrCnt );
			cell = row.createCell(i++); cell.setCellValue( webAgentErrCnt );
			cell = row.createCell(i++); cell.setCellValue( dbAgentErrCnt );
			cell = row.createCell(i++); cell.setCellValue( mailBodyErrCnt );
			cell = row.createCell(i++); cell.setCellValue( domainErrCnt );
			cell = row.createCell(i++); cell.setCellValue( networkErrCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumStep1Err );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(syntaxErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(webAgentErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(dbAgentErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(mailBodyErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(domainErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(networkErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( totFailCnt==0?0:(sumStep1Err/totFailCnt) );
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// [세부에러 - 메일발송 단계] 엑셀 헤더 생성 ( CONNECT | HELO | MAIL FROM | RCPT TO | RESET | DATA | DOT | QUIT | 합계 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[세부에러 - 메일발송 단계]");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("CONNECT");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("HELO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("MAIL FROM");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("RCPT TO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("RESET");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("DATA");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("DOT");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("QUIT");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("합계");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( connectErrCnt );
			cell = row.createCell(i++); cell.setCellValue( heloErrCnt );
			cell = row.createCell(i++); cell.setCellValue( mailFromErrCnt );
			cell = row.createCell(i++); cell.setCellValue( rcptToErrCnt );
			cell = row.createCell(i++); cell.setCellValue( resetErrCnt );
			cell = row.createCell(i++); cell.setCellValue( dataErrCnt );
			cell = row.createCell(i++); cell.setCellValue( dotErrCnt );
			cell = row.createCell(i++); cell.setCellValue( quitErrCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumStep2Err );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(connectErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(heloErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(mailFromErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(rcptToErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(resetErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(dataErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(dotErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(quitErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( totFailCnt==0?0:(sumStep2Err/totFailCnt) );

			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			logVO.setContent("M1004002");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(totFailCnt);
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}

	/**
	 * 통계분석 정기메일분석 병합분석 화면 EXCEL
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campJoinExcel")
	public void goCampJoinExcel(@ModelAttribute SendLogVO sendLogVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goCampJoinExcel campNos = " + sendLogVO.getCampNos());
		sendLogVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		String[] campNo = sendLogVO.getCampNos().split(",");
		List<Integer> campList = new ArrayList<Integer>();
		for(int i=0;i<campNo.length;i++) {
			campList.add(Integer.parseInt(campNo[i]));
		}
		sendLogVO.setCampList(campList);
		
		// 병합분석 캠페인정보 목록 조회
		List<TaskVO> campaignList = null;
		try {
			campaignList = analysisService.getJoinCampList(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinCampList error = " + e);
		}
		
		// 병합분석 캠페인 발송결과 조회
		RespLogVO respLog = null;
		try {
			respLog = analysisService.getJoinSendResultCamp(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinSendResultCamp error = " + e);
		}
		
		// 병합분석 캠페인 세부에러 목록 조회
		List<MailErrorVO> errorList = null;
		try {
			errorList = analysisService.getJoinErrorListCamp(sendLogVO);
		} catch(Exception e) {
			logger.error("analysisService.getJoinErrorListCamp error = " + e);
		}
		
		try {
			String fileName = "CAMP_JOIN_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			
			// Data 탭
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			int i = 0;
			
			// 캠페인정보
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[캠페인 목록]");
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 백분율(%) 형식
			XSSFCellStyle perStyle = wb.createCellStyle();
			perStyle.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
			
			// 합계 색상 지정
			XSSFCellStyle footerStyle = wb.createCellStyle();
			IndexedColorMap colorMap = wb.getStylesSource().getIndexedColors();
			footerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 합계 색상 지정(% 형식 추가)
			XSSFCellStyle footerStylePer = wb.createCellStyle();
			footerStylePer.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStylePer.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			footerStylePer.setDataFormat(wb.createDataFormat().getFormat("0.00%"));

			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("NO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인명");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("대상수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패수");
			
			int totSendCnt = 0;
			int totSuccCnt = 0;
			int totFailCnt = 0;
			
			for(TaskVO camp : campaignList) {
				totSendCnt += camp.getTotCnt();
				totSuccCnt += camp.getSucCnt();
				totFailCnt += camp.getTotCnt() - camp.getSucCnt();
				
				i = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(i++); cell.setCellValue( camp.getCampNo() );
				cell = row.createCell(i++); cell.setCellValue( camp.getCampNm() );
				cell = row.createCell(i++); cell.setCellValue( camp.getTotCnt() );
				cell = row.createCell(i++); cell.setCellValue( camp.getSucCnt() );
				cell = row.createCell(i++); cell.setCellValue( camp.getTotCnt() - camp.getSucCnt() );
			}
			
			// 합계
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("합계");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( totSendCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( totSuccCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( totFailCnt );
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			
			// [발송결과] 엑셀 헤더 생성 ( 대상수 | 성공수 | 실패수 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[발송결과]");

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("총 대상수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("총 성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("총 실패수");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( totSendCnt );
			cell = row.createCell(i++); cell.setCellValue( totSuccCnt );
			cell = row.createCell(i++); cell.setCellValue( totFailCnt );

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "비율(%)" );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(totSuccCnt/totSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(totFailCnt/totSendCnt) );
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// [반응결과] 엑셀 헤더 생성 ( 성공수 | 오픈수 | 유효오픈수 | 링크클릭수 | 수신거부 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[반응결과]");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("유효오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("링크클릭수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신거부");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "반응수" );
			cell = row.createCell(i++); cell.setCellValue( totSuccCnt );
			cell = row.createCell(i++); cell.setCellValue( respLog.getOpenCnt() );
			cell = row.createCell(i++); cell.setCellValue( respLog.getValidCnt() );
			cell = row.createCell(i++); cell.setCellValue( respLog.getClickCnt() );
			cell = row.createCell(i++); cell.setCellValue( respLog.getBlockCnt() );

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "성공대비" );
			cell = row.createCell(i++); cell.setCellValue( "" );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSuccCnt==0?0:(respLog.getOpenCnt()/totSuccCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSuccCnt==0?0:(respLog.getValidCnt()/totSuccCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSuccCnt==0?0:(respLog.getClickCnt()/totSuccCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSuccCnt==0?0:(respLog.getBlockCnt()/totSuccCnt) );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( "전체대비" );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(totSuccCnt/totSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(respLog.getOpenCnt()/totSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(respLog.getValidCnt()/totSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(respLog.getClickCnt()/totSendCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totSendCnt==0?0:(respLog.getBlockCnt()/totSendCnt) );
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// 세부에러 변수 설정
			int syntaxErrCnt = 0;
			int webAgentErrCnt = 0;
			int dbAgentErrCnt = 0;
			int mailBodyErrCnt = 0;
			int domainErrCnt = 0;
			int networkErrCnt = 0;
			int connectErrCnt = 0;
			int heloErrCnt = 0;
			int mailFromErrCnt = 0;
			int rcptToErrCnt = 0;
			int resetErrCnt = 0;
			int dataErrCnt = 0;
			int dotErrCnt = 0;
			int quitErrCnt = 0;
			int sumStep1Err = 0;
			int sumStep2Err = 0;
			int failCnt = 0;
			String step1 = "";
			String step2 = "";
			
			// 세부에러 값 설정
			for(MailErrorVO error:errorList) {
				step1 = error.getStep1();
				step2 = error.getStep2();
				failCnt = error.getCntStep2();
				if("001".equals(step1)) {
					sumStep1Err += failCnt;
					if("001".equals(step2)) {
						syntaxErrCnt = failCnt;
					} else if("002".equals(step2)) {
						webAgentErrCnt = failCnt;
					} else if("003".equals(step2)) {
						dbAgentErrCnt = failCnt;
					} else if("004".equals(step2)) {
						mailBodyErrCnt = failCnt;
					} else if("005".equals(step2)) {
						domainErrCnt = failCnt;
					} else if("006".equals(step2)) {
						networkErrCnt = failCnt;
					}
				} else if("002".equals(step1)) {
					sumStep2Err += failCnt;
					if("001".equals(step2)) {
						connectErrCnt = failCnt;
					} else if("002".equals(step2)) {
						heloErrCnt = failCnt;
					} else if("003".equals(step2)) {
						mailFromErrCnt = failCnt;
					} else if("004".equals(step2)) {
						rcptToErrCnt = failCnt;
					} else if("005".equals(step2)) {
						resetErrCnt = failCnt;
					} else if("006".equals(step2)) {
						dataErrCnt = failCnt;
					} else if("007".equals(step2)) {
						dotErrCnt = failCnt;
					} else if("008".equals(step2)) {
						quitErrCnt = failCnt;
					}
				}
			}
			
			
			// [세부에러 - 메일전송 전단계] 엑셀 헤더 생성 ( Syntax | Web Agent | DB Agent | Mail Body | Domain | Network | 합계 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[세부에러 - 메일전송 전단계]");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Syntax");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Web Agent");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("DB Agent");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Mail Body");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Domain");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("Network");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("합계");

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( syntaxErrCnt );
			cell = row.createCell(i++); cell.setCellValue( webAgentErrCnt );
			cell = row.createCell(i++); cell.setCellValue( dbAgentErrCnt );
			cell = row.createCell(i++); cell.setCellValue( mailBodyErrCnt );
			cell = row.createCell(i++); cell.setCellValue( domainErrCnt );
			cell = row.createCell(i++); cell.setCellValue( networkErrCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumStep1Err );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(syntaxErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(webAgentErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(dbAgentErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(mailBodyErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(domainErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(networkErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( totFailCnt==0?0:(sumStep1Err/totFailCnt) );
			
			// 빈줄 삽입
			row = sheet.createRow(rowNum++);
			
			// [세부에러 - 메일발송 단계] 엑셀 헤더 생성 ( CONNECT | HELO | MAIL FROM | RCPT TO | RESET | DATA | DOT | QUIT | 합계 )
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue("[세부에러 - 메일발송 단계]");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("CONNECT");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("HELO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("MAIL FROM");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("RCPT TO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("RESET");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("DATA");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("DOT");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("QUIT");
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue("합계");
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellValue( connectErrCnt );
			cell = row.createCell(i++); cell.setCellValue( heloErrCnt );
			cell = row.createCell(i++); cell.setCellValue( mailFromErrCnt );
			cell = row.createCell(i++); cell.setCellValue( rcptToErrCnt );
			cell = row.createCell(i++); cell.setCellValue( resetErrCnt );
			cell = row.createCell(i++); cell.setCellValue( dataErrCnt );
			cell = row.createCell(i++); cell.setCellValue( dotErrCnt );
			cell = row.createCell(i++); cell.setCellValue( quitErrCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumStep2Err );
			
			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(connectErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(heloErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(mailFromErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(rcptToErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(resetErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(dataErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(dotErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( totFailCnt==0?0:(quitErrCnt/totFailCnt) );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( totFailCnt==0?0:(sumStep2Err/totFailCnt) );

			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			logVO.setContent("M1004003");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(totFailCnt);
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}

	
	
	/**
	 * 통계분석 기간별누적분석 월별 EXCEL
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="/summMonthExcel")
	public void goSummMonthExcel(@ModelAttribute PeriodSummVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSummMonthExcel searchCampNo = " + searchVO.getSearchCampNo());
		logger.debug("goSummMonthExcel searchCampNm = " + searchVO.getSearchCampNm());
		logger.debug("goSummMonthExcel searchDeptNo = " + searchVO.getSearchDeptNo());
		logger.debug("goSummMonthExcel searchDeptNm = " + searchVO.getSearchDeptNm());
		logger.debug("goSummMonthExcel searchUserId = " + searchVO.getSearchUserId());
		logger.debug("goSummMonthExcel searchUserNm = " + searchVO.getSearchUserNm());
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));

		// 월별 데이터 조회
		List<PeriodSummVO> monthList = null;
		try {
			monthList = analysisService.getPeriodSummMonthList(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getPeriodSummMonthList error = " + e);
		}

		try {
			String fileName = "SUMM_MONTH_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 엑셀 헤더 생성 ( 년월 | 발송수 | 성공수 | 실패수 | 오픈수 | 유효오픈수 | 클릭수 | 수신거부 )
			int i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("년월");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("유효오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("클릭수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신거부");
			
			int sumSendCnt = 0;
			int sumSuccCnt = 0;
			int sumFailCnt = 0;
			int sumOpenCnt = 0;
			int sumValidCnt = 0;
			int sumClickCnt = 0;
			int sumBlockCnt = 0;
			
			// 엑셀 내용 추가
			for(PeriodSummVO month:monthList) {
				sumSendCnt += month.getSendCnt();
				sumSuccCnt += month.getSuccCnt();
				sumFailCnt += month.getSendCnt() - month.getSuccCnt();
				sumOpenCnt += month.getOpenCnt();
				sumValidCnt += month.getValidCnt();
				sumClickCnt += month.getClickCnt();
				sumBlockCnt += month.getBlockCnt();
				
				i = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(i++); cell.setCellValue( month.getYmd().substring(0,4)+"-"+month.getYmd().subSequence(4,6) );
				cell = row.createCell(i++); cell.setCellValue( month.getSendCnt() );
				cell = row.createCell(i++); cell.setCellValue( month.getSuccCnt() );
				cell = row.createCell(i++); cell.setCellValue( month.getSendCnt() - month.getSuccCnt() );
				cell = row.createCell(i++); cell.setCellValue( month.getOpenCnt() );
				cell = row.createCell(i++); cell.setCellValue( month.getValidCnt() );
				cell = row.createCell(i++); cell.setCellValue( month.getClickCnt() );
				cell = row.createCell(i++); cell.setCellValue( month.getBlockCnt() );
			}
			
			// 합계 색상 지정
			XSSFCellStyle footerStyle = wb.createCellStyle();
			IndexedColorMap colorMap = wb.getStylesSource().getIndexedColors();
			footerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( "합계" );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumSendCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumSuccCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumFailCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumOpenCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumValidCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumClickCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumBlockCnt );
			
			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			logVO.setContent("M1004004");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(monthList == null?0:monthList.size());
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}
	
	/**
	 * 통계분석 기간별누적분석 요일별 EXCEL
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="/summWeekExcel")
	public void goSummWeekExcel(@ModelAttribute PeriodSummVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSummWeekExcel searchCampNo = " + searchVO.getSearchCampNo());
		logger.debug("goSummWeekExcel searchCampNm = " + searchVO.getSearchCampNm());
		logger.debug("goSummWeekExcel searchDeptNo = " + searchVO.getSearchDeptNo());
		logger.debug("goSummWeekExcel searchDeptNm = " + searchVO.getSearchDeptNm());
		logger.debug("goSummWeekExcel searchUserId = " + searchVO.getSearchUserId());
		logger.debug("goSummWeekExcel searchUserNm = " + searchVO.getSearchUserNm());
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));

		// 요일별 데이터 조회
		List<PeriodSummVO> weekList = null;
		try {
			weekList = analysisService.getPeriodSummWeekList(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getPeriodSummWeekList error = " + e);
		}

		try {
			String fileName = "SUMM_WEEK_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 엑셀 헤더 생성 ( 요일 | 발송수 | 성공수 | 실패수 | 오픈수 | 유효오픈수 | 클릭수 | 수신거부 )
			int i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("요일");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("유효오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("클릭수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신거부");
			
			int sumSendCnt = 0;
			int sumSuccCnt = 0;
			int sumFailCnt = 0;
			int sumOpenCnt = 0;
			int sumValidCnt = 0;
			int sumClickCnt = 0;
			int sumBlockCnt = 0;
			
			// 엑셀 내용 추가
			for(PeriodSummVO week:weekList) {
				sumSendCnt += week.getSendCnt();
				sumSuccCnt += week.getSuccCnt();
				sumFailCnt += week.getSendCnt() - week.getSuccCnt();
				sumOpenCnt += week.getOpenCnt();
				sumValidCnt += week.getValidCnt();
				sumClickCnt += week.getClickCnt();
				sumBlockCnt += week.getBlockCnt();
				
				i = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(i++); cell.setCellValue( week.getWeekNm() );
				cell = row.createCell(i++); cell.setCellValue( week.getSendCnt() );
				cell = row.createCell(i++); cell.setCellValue( week.getSuccCnt() );
				cell = row.createCell(i++); cell.setCellValue( week.getSendCnt() - week.getSuccCnt() );
				cell = row.createCell(i++); cell.setCellValue( week.getOpenCnt() );
				cell = row.createCell(i++); cell.setCellValue( week.getValidCnt() );
				cell = row.createCell(i++); cell.setCellValue( week.getClickCnt() );
				cell = row.createCell(i++); cell.setCellValue( week.getBlockCnt() );
			}
			
			// 합계 색상 지정
			XSSFCellStyle footerStyle = wb.createCellStyle();
			IndexedColorMap colorMap = wb.getStylesSource().getIndexedColors();
			footerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( "합계" );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumSendCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumSuccCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumFailCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumOpenCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumValidCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumClickCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumBlockCnt );
			
			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			logVO.setContent("M1004004");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(weekList == null?0:weekList.size());
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}
	
	/**
	 * 통계분석 기간별누적분석 일자별 EXCEL
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="/summDateExcel")
	public void goSummDateExcel(@ModelAttribute PeriodSummVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSummDateExcel searchCampNo = " + searchVO.getSearchCampNo());
		logger.debug("goSummDateExcel searchCampNm = " + searchVO.getSearchCampNm());
		logger.debug("goSummDateExcel searchDeptNo = " + searchVO.getSearchDeptNo());
		logger.debug("goSummDateExcel searchDeptNm = " + searchVO.getSearchDeptNm());
		logger.debug("goSummDateExcel searchUserId = " + searchVO.getSearchUserId());
		logger.debug("goSummDateExcel searchUserNm = " + searchVO.getSearchUserNm());
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));

		// 일자별 데이터 조회
		List<PeriodSummVO> dateList = null;
		try {
			dateList = analysisService.getPeriodSummDateList(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getPeriodSummDateList error = " + e);
		}

		try {
			String fileName = "SUMM_DATE_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 엑셀 헤더 생성 ( 일자 | 발송수 | 성공수 | 실패수 | 오픈수 | 유효오픈수 | 클릭수 | 수신거부 )
			int i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("일자");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("유효오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("클릭수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신거부");
			
			int sumSendCnt = 0;
			int sumSuccCnt = 0;
			int sumFailCnt = 0;
			int sumOpenCnt = 0;
			int sumValidCnt = 0;
			int sumClickCnt = 0;
			int sumBlockCnt = 0;
			
			// 엑셀 내용 추가
			for(PeriodSummVO date:dateList) {
				sumSendCnt += date.getSendCnt();
				sumSuccCnt += date.getSuccCnt();
				sumFailCnt += date.getSendCnt() - date.getSuccCnt();
				sumOpenCnt += date.getOpenCnt();
				sumValidCnt += date.getValidCnt();
				sumClickCnt += date.getClickCnt();
				sumBlockCnt += date.getBlockCnt();
				
				i = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(i++); cell.setCellValue( date.getYmd().substring(0,4)+"-"+date.getYmd().substring(4,6)+"-"+date.getYmd().substring(6,8) );
				cell = row.createCell(i++); cell.setCellValue( date.getSendCnt() );
				cell = row.createCell(i++); cell.setCellValue( date.getSuccCnt() );
				cell = row.createCell(i++); cell.setCellValue( date.getSendCnt() - date.getSuccCnt() );
				cell = row.createCell(i++); cell.setCellValue( date.getOpenCnt() );
				cell = row.createCell(i++); cell.setCellValue( date.getValidCnt() );
				cell = row.createCell(i++); cell.setCellValue( date.getClickCnt() );
				cell = row.createCell(i++); cell.setCellValue( date.getBlockCnt() );
			}
			
			// 합계 색상 지정
			XSSFCellStyle footerStyle = wb.createCellStyle();
			IndexedColorMap colorMap = wb.getStylesSource().getIndexedColors();
			footerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( "합계" );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumSendCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumSuccCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumFailCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumOpenCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumValidCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumClickCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumBlockCnt );
			
			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			logVO.setContent("M1004004");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(dateList == null?0:dateList.size());
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}
	
	/**
	 * 통계분석 기간별누적분석 도메인별 EXCEL
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="/summDomainExcel")
	public void goSummDomainExcel(@ModelAttribute PeriodSummVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSummDomainExcel searchCampNo = " + searchVO.getSearchCampNo());
		logger.debug("goSummDomainExcel searchCampNm = " + searchVO.getSearchCampNm());
		logger.debug("goSummDomainExcel searchDeptNo = " + searchVO.getSearchDeptNo());
		logger.debug("goSummDomainExcel searchDeptNm = " + searchVO.getSearchDeptNm());
		logger.debug("goSummDomainExcel searchUserId = " + searchVO.getSearchUserId());
		logger.debug("goSummDomainExcel searchUserNm = " + searchVO.getSearchUserNm());
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));

		// 도메인별 데이터 조회
		List<PeriodSummVO> domainList = null;
		try {
			domainList = analysisService.getPeriodSummDomainList(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getPeriodSummDomainList error = " + e);
		}

		try {
			String fileName = "SUMM_DOMAIN_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 엑셀 헤더 생성 ( NO | 도메인명 | 발송수 | 성공수 | 실패수 | 발송단계에러 | 성공률 | 실패율 | 발송단계에러율 )
			int i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("NO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("도메인명");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송단계에러");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공률");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패율");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송단계에러율");
			
			// 백분율(%) 형식
			XSSFCellStyle perStyle = wb.createCellStyle();
			perStyle.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
			
			int sumSendCnt = 0;
			int sumSuccCnt = 0;
			int sumFailCnt = 0;
			int sumErrCnt = 0;
			
			// 엑셀 내용 추가
			for(PeriodSummVO domain:domainList) {
				sumSendCnt += domain.getSendCnt();
				sumSuccCnt += domain.getSuccCnt();
				sumFailCnt += domain.getSendCnt() - domain.getSuccCnt();
				sumErrCnt += domain.getErrCnt();
				
				i = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(i++); cell.setCellValue( rowNum - 1 );
				cell = row.createCell(i++); cell.setCellValue( domain.getDomainNm() );
				cell = row.createCell(i++); cell.setCellValue( domain.getSendCnt() );
				cell = row.createCell(i++); cell.setCellValue( domain.getSuccCnt() );
				cell = row.createCell(i++); cell.setCellValue( domain.getSendCnt() - domain.getSuccCnt() );
				cell = row.createCell(i++); cell.setCellValue( domain.getErrCnt() );
				cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( domain.getSendCnt()==0?0:domain.getSuccCnt()/domain.getSendCnt() );
				cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( domain.getSendCnt()==0?0:(domain.getSendCnt() - domain.getSuccCnt())/domain.getSendCnt() );
				cell = row.createCell(i++); cell.setCellStyle(perStyle); cell.setCellValue( (domain.getSendCnt() - domain.getSuccCnt())==0?0:domain.getErrCnt()/(domain.getSendCnt() - domain.getSuccCnt()) );
			}
			
			// 합계 색상 지정
			XSSFCellStyle footerStyle = wb.createCellStyle();
			IndexedColorMap colorMap = wb.getStylesSource().getIndexedColors();
			footerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 합계 색상 지정(% 형식 추가)
			XSSFCellStyle footerStylePer = wb.createCellStyle();
			footerStylePer.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStylePer.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			footerStylePer.setDataFormat(wb.createDataFormat().getFormat("0.00%"));

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( "합계" );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( "" );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumSendCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumSuccCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumFailCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumErrCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( sumSendCnt==0?0:sumSuccCnt/sumSendCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( sumSendCnt==0?0:sumFailCnt/sumSendCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStylePer); cell.setCellValue( sumFailCnt==0?0:sumErrCnt/sumFailCnt );
			
			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			logVO.setContent("M1004004");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(domainList == null?0:domainList.size());
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}
	
	
	/**
	 * 통계분석 기간별누적분석 사용자그룹별 EXCEL
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="/summDeptExcel")
	public void goSummDeptExcel(@ModelAttribute PeriodSummVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSummDeptExcel searchCampNo = " + searchVO.getSearchCampNo());
		logger.debug("goSummDeptExcel searchCampNm = " + searchVO.getSearchCampNm());
		logger.debug("goSummDeptExcel searchDeptNo = " + searchVO.getSearchDeptNo());
		logger.debug("goSummDeptExcel searchDeptNm = " + searchVO.getSearchDeptNm());
		logger.debug("goSummDeptExcel searchUserId = " + searchVO.getSearchUserId());
		logger.debug("goSummDeptExcel searchUserNm = " + searchVO.getSearchUserNm());
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));

		// 사용자그룹(부서)별 데이터 조회
		List<PeriodSummVO> deptList = null;
		try {
			deptList = analysisService.getPeriodSummDeptList(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getPeriodSummDeptList error = " + e);
		}

		try {
			String fileName = "SUMM_GROUP_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 엑셀 헤더 생성 ( 사용자그룹 | 발송수 | 성공수 | 실패수 | 오픈수 | 유효오픈수 | 클릭수 | 수신거부 )
			int i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("사용자그룹");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("유효오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("클릭수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신거부");
			
			int sumSendCnt = 0;
			int sumSuccCnt = 0;
			int sumFailCnt = 0;
			int sumOpenCnt = 0;
			int sumValidCnt = 0;
			int sumClickCnt = 0;
			int sumBlockCnt = 0;
			
			// 엑셀 내용 추가
			for(PeriodSummVO dept:deptList) {
				sumSendCnt += dept.getSendCnt();
				sumSuccCnt += dept.getSuccCnt();
				sumFailCnt += dept.getSendCnt() - dept.getSuccCnt();
				sumOpenCnt += dept.getOpenCnt();
				sumValidCnt += dept.getValidCnt();
				sumClickCnt += dept.getClickCnt();
				sumBlockCnt += dept.getBlockCnt();
				
				i = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(i++); cell.setCellValue( dept.getDeptNm() );
				cell = row.createCell(i++); cell.setCellValue( dept.getSendCnt() );
				cell = row.createCell(i++); cell.setCellValue( dept.getSuccCnt() );
				cell = row.createCell(i++); cell.setCellValue( dept.getSendCnt() - dept.getSuccCnt() );
				cell = row.createCell(i++); cell.setCellValue( dept.getOpenCnt() );
				cell = row.createCell(i++); cell.setCellValue( dept.getValidCnt() );
				cell = row.createCell(i++); cell.setCellValue( dept.getClickCnt() );
				cell = row.createCell(i++); cell.setCellValue( dept.getBlockCnt() );
			}
			
			// 합계 색상 지정
			XSSFCellStyle footerStyle = wb.createCellStyle();
			IndexedColorMap colorMap = wb.getStylesSource().getIndexedColors();
			footerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( "합계" );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumSendCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumSuccCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumFailCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumOpenCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumValidCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumClickCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumBlockCnt );
			
			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			logVO.setContent("M1004004");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(deptList == null?0:deptList.size());
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}
	
	/**
	 * 통계분석 기간별누적분석 사용자별 EXCEL
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="/summUserExcel")
	public void goSummUserExcel(@ModelAttribute PeriodSummVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSummUserExcel searchCampNo = " + searchVO.getSearchCampNo());
		logger.debug("goSummUserExcel searchCampNm = " + searchVO.getSearchCampNm());
		logger.debug("goSummUserExcel searchDeptNo = " + searchVO.getSearchDeptNo());
		logger.debug("goSummUserExcel searchDeptNm = " + searchVO.getSearchDeptNm());
		logger.debug("goSummUserExcel searchUserId = " + searchVO.getSearchUserId());
		logger.debug("goSummUserExcel searchUserNm = " + searchVO.getSearchUserNm());
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));

		// 사용자별 데이터 조회
		List<PeriodSummVO> userList = null;
		try {
			userList = analysisService.getPeriodSummUserList(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getPeriodSummUserList error = " + e);
		}

		try {
			String fileName = "SUMM_USER_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 엑셀 헤더 생성 ( 사용자 | 발송수 | 성공수 | 실패수 | 오픈수 | 유효오픈수 | 클릭수 | 수신거부 )
			int i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("사용자");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("유효오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("클릭수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신거부");
			
			int sumSendCnt = 0;
			int sumSuccCnt = 0;
			int sumFailCnt = 0;
			int sumOpenCnt = 0;
			int sumValidCnt = 0;
			int sumClickCnt = 0;
			int sumBlockCnt = 0;
			
			// 엑셀 내용 추가
			for(PeriodSummVO user:userList) {
				sumSendCnt += user.getSendCnt();
				sumSuccCnt += user.getSuccCnt();
				sumFailCnt += user.getSendCnt() - user.getSuccCnt();
				sumOpenCnt += user.getOpenCnt();
				sumValidCnt += user.getValidCnt();
				sumClickCnt += user.getClickCnt();
				sumBlockCnt += user.getBlockCnt();
				
				i = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(i++); cell.setCellValue( user.getUserNm() );
				cell = row.createCell(i++); cell.setCellValue( user.getSendCnt() );
				cell = row.createCell(i++); cell.setCellValue( user.getSuccCnt() );
				cell = row.createCell(i++); cell.setCellValue( user.getSendCnt() - user.getSuccCnt() );
				cell = row.createCell(i++); cell.setCellValue( user.getOpenCnt() );
				cell = row.createCell(i++); cell.setCellValue( user.getValidCnt() );
				cell = row.createCell(i++); cell.setCellValue( user.getClickCnt() );
				cell = row.createCell(i++); cell.setCellValue( user.getBlockCnt() );
			}
			
			// 합계 색상 지정
			XSSFCellStyle footerStyle = wb.createCellStyle();
			IndexedColorMap colorMap = wb.getStylesSource().getIndexedColors();
			footerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( "합계" );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumSendCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumSuccCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumFailCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumOpenCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumValidCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumClickCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumBlockCnt );
			
			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			logVO.setContent("M1004004");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(userList == null?0:userList.size());
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}
	
	/**
	 * 통계분석 기간별누적분석 캠페인별 EXCEL
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="/summCampExcel")
	public void goSummCampExcel(@ModelAttribute PeriodSummVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSummCampExcel searchCampNo = " + searchVO.getSearchCampNo());
		logger.debug("goSummCampExcel searchCampNm = " + searchVO.getSearchCampNm());
		logger.debug("goSummCampExcel searchDeptNo = " + searchVO.getSearchDeptNo());
		logger.debug("goSummCampExcel searchDeptNm = " + searchVO.getSearchDeptNm());
		logger.debug("goSummCampExcel searchUserId = " + searchVO.getSearchUserId());
		logger.debug("goSummCampExcel searchUserNm = " + searchVO.getSearchUserNm());
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));

		// 캠페인별 데이터 조회
		List<PeriodSummVO> campList = null;
		try {
			campList = analysisService.getPeriodSummCampList(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getPeriodSummCampList error = " + e);
		}

		try {
			String fileName = "SUMM_CAMP_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("Data");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 엑셀 헤더 생성 ( 캠페인 | 발송수 | 성공수 | 실패수 | 오픈수 | 유효오픈수 | 클릭수 | 수신거부 )
			int i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("성공수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("실패수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("유효오픈수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("클릭수");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신거부");
			
			int sumSendCnt = 0;
			int sumSuccCnt = 0;
			int sumFailCnt = 0;
			int sumOpenCnt = 0;
			int sumValidCnt = 0;
			int sumClickCnt = 0;
			int sumBlockCnt = 0;
			
			// 엑셀 내용 추가
			for(PeriodSummVO camp:campList) {
				sumSendCnt += camp.getSendCnt();
				sumSuccCnt += camp.getSuccCnt();
				sumFailCnt += camp.getSendCnt() - camp.getSuccCnt();
				sumOpenCnt += camp.getOpenCnt();
				sumValidCnt += camp.getValidCnt();
				sumClickCnt += camp.getClickCnt();
				sumBlockCnt += camp.getBlockCnt();
				
				i = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(i++); cell.setCellValue( camp.getCampNm() );
				cell = row.createCell(i++); cell.setCellValue( camp.getSendCnt() );
				cell = row.createCell(i++); cell.setCellValue( camp.getSuccCnt() );
				cell = row.createCell(i++); cell.setCellValue( camp.getSendCnt() - camp.getSuccCnt() );
				cell = row.createCell(i++); cell.setCellValue( camp.getOpenCnt() );
				cell = row.createCell(i++); cell.setCellValue( camp.getValidCnt() );
				cell = row.createCell(i++); cell.setCellValue( camp.getClickCnt() );
				cell = row.createCell(i++); cell.setCellValue( camp.getBlockCnt() );
			}
			
			// 합계 색상 지정
			XSSFCellStyle footerStyle = wb.createCellStyle();
			IndexedColorMap colorMap = wb.getStylesSource().getIndexedColors();
			footerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(205, 225, 250),colorMap));
			footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( "합계" );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumSendCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumSuccCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumFailCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumOpenCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumValidCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumClickCnt );
			cell = row.createCell(i++); cell.setCellStyle(footerStyle); cell.setCellValue( sumBlockCnt );
			
			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			logVO.setContent("M1004004");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(campList == null?0:campList.size());
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}
	
	/**
	 * 대량메일 상세 로그목록를 엑셀로 다운로드 한다.
	 * @param sendLogVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="/detailLogExcel")
	public void goDetailLogExcel(@ModelAttribute SendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goDetailLog searchStartDt     = " + searchVO.getSearchStartDt());
		logger.debug("goDetailLog searchEndDt       = " + searchVO.getSearchEndDt());
		logger.debug("goDetailLog searchCampNo      = " + searchVO.getSearchCampNo());
		logger.debug("goDetailLog searchCustId      = " + searchVO.getSearchCustId());
		logger.debug("goDetailLog searchCustNm      = " + searchVO.getSearchCustNm());
		logger.debug("goDetailLog searchCustEm      = " + searchVO.getSearchCustEm());
		logger.debug("goDetailLog searchSendRepeat  = " + searchVO.getSearchSendRepeat());
		logger.debug("goDetailLog searchWorkStatus  = " + searchVO.getSearchWorkStatus());
		logger.debug("goDetailLog searchUserNm      = " + searchVO.getSearchUserNm());
		logger.debug("goDetailLog searchServiceGb   = " + searchVO.getSearchServiceGb());
		
		
		// 검색 기본값 설정
		if(StringUtil.isNull(searchVO.getSearchStartDt())) {
			searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-2, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		}
		if(StringUtil.isNull(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		}
		
		if(!StringUtil.isNull(searchVO.getSearchCustEm())) {
			searchVO.setSearchCustEm(cryptoService.getEncrypt("CUST_EM", searchVO.getSearchCustEm()));
			logger.debug("goDetailLog Decrypt searchCustEm   = " + searchVO.getSearchCustEm());
		}
		
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		searchVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		
		// 페이지 설정
		searchVO.setPage(1);
		searchVO.setRows(99999999);
		
		// 고객별 로그 목록 조회
		List<SendLogVO> detailList = null;
		try {
			detailList = analysisService.getSendLogListExcel(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getSendLogListExcel error = " + e);
		}

		try {
			String fileName = "Detail_log_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			logger.debug("#### FileName = " + fileName);
			
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("Detail Log");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 엑셀 헤더 생성
			int i = 0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송일시");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송자명");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송자이메일");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("사용자ID");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("이름");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("메일유형");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("TASK_NO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("SUB_TASK_NO");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("메일발송명");	
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("메일명");
			
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("고객ID");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("고객명");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("고객이메일");
			
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("캠페인명");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("발송결과");
			cell = row.createCell(i++); cell.setCellStyle(headerStyle); cell.setCellValue("수신확인일");
			
			// 엑셀 내용 추가
			for(SendLogVO log:detailList) {
				i = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(i++); cell.setCellValue( StringUtil.getFDate(log.getSendDt(), Code.DT_FMT2) );
				cell = row.createCell(i++); cell.setCellValue( log.getMailFromNm() );
				cell = row.createCell(i++); cell.setCellValue(cryptoService.getDecrypt("MAIL_FROM_EM", log.getMailFromEm()));
				cell = row.createCell(i++); cell.setCellValue( log.getUserId() );
				cell = row.createCell(i++); cell.setCellValue( log.getUserNm() );
				cell = row.createCell(i++); cell.setCellValue( log.getSendRepeatNm() );
				cell = row.createCell(i++); cell.setCellValue( log.getTaskNo() );
				cell = row.createCell(i++); cell.setCellValue( log.getSubTaskNo() );
				cell = row.createCell(i++); cell.setCellValue( log.getTaskNm() );
				cell = row.createCell(i++); cell.setCellValue( log.getMailTitle() );
				cell = row.createCell(i++); cell.setCellValue( log.getCustId() );
				cell = row.createCell(i++); cell.setCellValue( log.getCustNm() );
				cell = row.createCell(i++); cell.setCellValue(cryptoService.getDecrypt("CUST_EM", log.getCustEm()) );
				
				cell = row.createCell(i++); cell.setCellValue( log.getCampNm() );
				cell = row.createCell(i++); cell.setCellValue( log.getSendRcodeNm() );
				cell = row.createCell(i++); cell.setCellValue( log.getOpenDt() );
			}
			
			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 002:엑셀추출
			logVO.setContent("M1004005");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setRecCnt(detailList == null?0:detailList.size());
			logVO.setMobilYn("N");
			try {
				logService.insertActionLog(request, session, logVO);
			} catch(Exception e) {
				logger.error("logService.insertActionLog error = " + e);
			}
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}
	
	/**
	 * 캠페인별 메일 발송 목록 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campMailSendListP")
	public String goCampMailSendP(@ModelAttribute SendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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
		if(searchVO.getSearchCampNo() == 0) {
			searchVO.setSearchCampNo(0);
		}
		if(StringUtil.isNull(searchVO.getSearchServiceGb())) {
			searchVO.setSearchServiceGb("10");
		}
		
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		searchVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		searchVO.setPage(page);
		
		
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

		model.addAttribute("searchVO", searchVO);	// 검색항목
		model.addAttribute("deptList", deptList);	// 부서 목록
		model.addAttribute("userList", userList);	// 사용자 목록
		
		return "ems/ana/campMailSendListP";
	}
	
	/**
	 * 캠페인별 메일 발송 목록 조회.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campMailSendList")
	public String goCampMailSendList(@ModelAttribute SendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goCampMailSendList searchStartDt     = " + searchVO.getSearchStartDt());
		logger.debug("goCampMailSendList searchEndDt       = " + searchVO.getSearchEndDt());
		logger.debug("goCampMailSendList searchServiceGb   = " + searchVO.getSearchServiceGb());
		logger.debug("goCampMailSendList searchDeptNo      = " + searchVO.getSearchDeptNo());
		logger.debug("goCampMailSendList searchUserId      = " + searchVO.getSearchUserId());
		logger.debug("goCampMailSendList searchCampNo      = " + searchVO.getSearchCampNo());
		
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
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		List<SendLogVO> campMailSendList = null;
		try {
			campMailSendList = analysisService.getCampMailSendList(searchVO);
			
		} catch(Exception e) {
			logger.error("analysisService.getCampMailSendList error = " + e);
		}
		
		if(campMailSendList != null && campMailSendList.size() > 0) {
			totalCount = campMailSendList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);

		model.addAttribute("searchVO", searchVO);						// 검색항목
		model.addAttribute("campMailSendList", campMailSendList);		// 캠페인별 메일 발송 리스트
		model.addAttribute("pageUtil", pageUtil);						// 페이징
		
		return "ems/ana/campMailSendList";
	}
	
	/**
	 * 캠페인별 발송 -대용량 이메일 메일별 발송목록조회 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campMailEmsListP")
	public String goCampMailEnmsListP(@ModelAttribute SendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		searchVO.setPage(page);		
		
		
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
		
		// 메일유형코드조회
		CodeVO sendRepeat = new CodeVO();
		sendRepeat.setCdGrp("C017");
		sendRepeat.setUilang("000");
		sendRepeat.setUseYn("Y");
		List<CodeVO> repeatList = null;
		try {
			repeatList = codeService.getCodeList(sendRepeat);
		} catch(Exception e) {
			logger.error("codeService.getCodeList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("deptList", deptList);			// 부서 목록
		model.addAttribute("userList", userList);			// 사용자 목록	
		model.addAttribute("repeatList", repeatList);		// 메일 유형
		
		return "ems/ana/campMailEmsListP";
	}
	
	/**
	 * 캠페인별 발송 -대용량 이메일 메일별 발송목록조회 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campMailEmsList")
	public String goCampMailEmsList(@ModelAttribute SendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goDetailLog searchStartDt     = " + searchVO.getSearchStartDt());
		logger.debug("goDetailLog searchEndDt       = " + searchVO.getSearchEndDt());
		logger.debug("goDetailLog searchSendRepeate = " + searchVO.getSearchSendRepeat());
		logger.debug("goDetailLog searchTaskNm      = " + searchVO.getSearchTaskNm());
		logger.debug("goDetailLog searchWorkStatus  = " + searchVO.getSearchWorkStatus());
		logger.debug("goDetailLog searchDeptNo      = " + searchVO.getSearchDeptNo());
		logger.debug("goDetailLog searchUserId      = " + searchVO.getSearchUserId());
		logger.debug("goDetailLog searchCampNo      = " + searchVO.getSearchCampNo());
		
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
		
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		searchVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		List<SendLogVO> campMailEmsList = null;
		try {
			campMailEmsList = analysisService.getCampMailEmsList(searchVO);
			
		} catch(Exception e) {
			logger.error("analysisService.getCampMailEmsList error = " + e);
		}
		
		if(campMailEmsList != null && campMailEmsList.size() > 0) {
			totalCount = campMailEmsList.get(0).getTotalCount();
		}
		
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);

		model.addAttribute("searchVO", searchVO);					// 검색항목
		model.addAttribute("campMailEmsList", campMailEmsList);		// 캠페인별 대용량 이메일 메일 발송 리스트
		model.addAttribute("pageUtil", pageUtil);					// 페이징
		
		return "ems/ana/campMailEmsList";
	}
	
	/**
	 * 통계분석 정기메일 통합 통계분석 화면을 출력한다.
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campMailAnalListP")
	public String goCampMailAnalListP(@ModelAttribute SendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goCampMailAnalListP page             = " + searchVO.getPage());
		logger.debug("goCampMailAnalListP searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goCampMailAnalListP searchCampNm     = " + searchVO.getSearchCampNm());
		logger.debug("goCampMailAnalListP searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goCampMailAnalListP searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("goCampMailAnalListP searchSendRepeat = " + searchVO.getSearchSendRepeat());
		logger.debug("goCampMailAnalListP searchTaskNm     = " + searchVO.getSearchTaskNm());
		logger.debug("goCampMailAnalListP searchWorkStatus = " + searchVO.getSearchWorkStatus());
		logger.debug("goCampMailAnalListP searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goCampMailAnalListP searchUserId     = " + searchVO.getSearchUserId());
		logger.debug("goCampMailAnalListP taskNo           = " + searchVO.getTaskNo());
		logger.debug("goCampMailAnalListP subTaskNo        = " + searchVO.getSubTaskNo());
		
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 메일 정보 조회
		SendLogVO mailSendLog = null;
		try {
			mailSendLog = analysisService.getCampMailEms(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getCampMailEms error = " + e);
		}
		
		//재발송코드리스트 
		CodeVO reSendCode = new CodeVO();
		reSendCode.setUilang((String)session.getAttribute("NEO_UILANG"));
		reSendCode.setUserId((String)session.getAttribute("NEO_USER_ID"));
		reSendCode.setCdGrp("C035");
		reSendCode.setUseYn("Y");
		List<CodeVO> reSendCodeList = null;
		try {
			reSendCodeList =  codeService.getCodeList(reSendCode);
		} catch(Exception e) {
			logger.error("codeService.reSendCodeList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);		// 조건정보
		model.addAttribute("mailSendLog", mailSendLog);	// 메일발송 정보		
		model.addAttribute("reSendCodeList", reSendCodeList);	// 재발송코드리스트
		
		return "ems/ana/campMailAnalListP";
	}
	
	/**
	 * 캠페인별 발송 -대용량 이메일 메일별 발송목록조회 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campMailRnsListP")
	public String goCampMailRnsListP(@ModelAttribute SendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goCampMailRnsListP searchStartDt     = " + searchVO.getSearchStartDt());
		logger.debug("goCampMailRnsListP searchEndDt       = " + searchVO.getSearchEndDt());
		logger.debug("goCampMailRnsListP searchServiceGb   = " + searchVO.getSearchServiceGb());
		logger.debug("goCampMailRnsListP searchDeptNo      = " + searchVO.getSearchDeptNo());
		logger.debug("goCampMailRnsListP searchUserId      = " + searchVO.getSearchUserId());
		logger.debug("goCampMailRnsListP searchCampNo      = " + searchVO.getSearchCampNo());
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
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		searchVO.setPage(page);

		// 발송결과코드
		/*
		CodeVO rcode = new CodeVO();
		rcode.setStatus("C101");
		rcode.setUilang("000");
		List<CodeVO> rcodeList = null;
		try {
			rcodeList =  codeService.getRcodeList(rcode);
		} catch(Exception e) {
			logger.error("codeService.getRcodeList error = " + e);
		}
		*/
		
		//기능권한 추가  - 실시간 건별재발송 
		/*CodeVO funcAuth = new CodeVO();
		funcAuth.setUilang((String)session.getAttribute("NEO_UILANG"));
		funcAuth.setUserId((String)session.getAttribute("NEO_USER_ID"));
		funcAuth.setCd("004");
		String rnsReSendAuth = "N";
		try {
			rnsReSendAuth = codeService.getUserFuncAuth(funcAuth);
		} catch(Exception e) {
			logger.error("codeService.getUserFuncAuth[004] error = " + e);
		}
		*/
		
		
		model.addAttribute("searchVO", searchVO);				// 검색항목
		//model.addAttribute("searchWorkStatusList", rcodeList); 	// 발송결과
		//model.addAttribute("rnsReSendAuth", rnsReSendAuth);		// 기능권한 : 실시간 건별 재발송
		
		return "ems/ana/campMailRnsListP";
	}
	
	/**
	 * 캠페인별 발송 -대용량 이메일 메일별 발송목록조회 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campMailRnsList")
	public String goCampMailRnsList(@ModelAttribute SendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goCampMailRnsList searchStartDt     = " + searchVO.getSearchStartDt());
		logger.debug("goCampMailRnsList searchEndDt       = " + searchVO.getSearchEndDt());
		logger.debug("goCampMailRnsList searchTaskNm      = " + searchVO.getSearchTaskNm());
		logger.debug("goCampMailRnsList searchWorkStatus  = " + searchVO.getSearchWorkStatus());
		logger.debug("goCampMailRnsList searchUserId      = " + searchVO.getSearchUserId());
		logger.debug("goCampMailRnsList searchCampNo      = " + searchVO.getSearchCampNo());
		
		
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
		
		String plainRmail = "";
		if(searchVO.getSearchCustEm() != null && !"".equals(searchVO.getSearchCustEm())) {
			plainRmail = searchVO.getSearchCustEm();
			searchVO.setSearchCustEm(cryptoService.getEncrypt("RMAIL", searchVO.getSearchCustEm()));
		}
		
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		searchVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		List<SendLogVO> campMailRnsList = null;
		try {
			campMailRnsList = analysisService.getCampMailRnsList(searchVO);
			
		} catch(Exception e) {
			logger.error("analysisService.getCampMailRnsList error = " + e);
		}
		
		if(campMailRnsList != null && campMailRnsList.size() > 0) {
			totalCount = campMailRnsList.get(0).getTotalCount();
		}
		
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);

		searchVO.setSearchCustEm(plainRmail);
		model.addAttribute("searchVO", searchVO);						// 검색항목
		model.addAttribute("campMailRnsList", campMailRnsList);			// 캠페인별 실시간 이메일 메일 발송 리스트
		model.addAttribute("pageUtil", pageUtil);						// 페이징
		
		return "ems/ana/campMailRnsList";
	}
	
	/**
	 * 검색유형명 확인
	 * @param searchKind
	 * @return
	 */
	public String getSearchKindNm(String searchKind) {
		if("000".equals(searchKind)) {
			return "";
		} else if("001".equals(searchKind)) {
			return "발송성공";
		} else if("002".equals(searchKind)) {
			return "발송실패";
		} else if("003".equals(searchKind)) {
			return "수신";
		} else if("004".equals(searchKind)) {
			return "미수신";
		} else if("005".equals(searchKind)) {
			return "수신거부";
		} else if("006".equals(searchKind)) {
			return "수신허용";
		} else {
			return "";
		}
	}

}
