/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.22
 * 설명 : RNS 통계분석 Controller
 */
package kr.co.sict.ums.rns.ana.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.co.sict.ums.com.service.CodeService;
import kr.co.sict.ums.com.service.CryptoService;
import kr.co.sict.ums.com.vo.CodeVO;
import kr.co.sict.ums.ems.ana.vo.SendLogVO;
import kr.co.sict.ums.rns.ana.service.RnsAnalysisService;
import kr.co.sict.ums.rns.ana.vo.RnsAnaDetailLogVO;
import kr.co.sict.ums.rns.ana.vo.RnsAnaDomainVO;
import kr.co.sict.ums.rns.ana.vo.RnsAnaMailSendResultVO; 
import kr.co.sict.ums.rns.ana.vo.RnsAnaMonthVO; 
import kr.co.sict.ums.rns.ana.vo.RnsAnaServiceVO; 
import kr.co.sict.ums.sys.acc.service.AccountService;
import kr.co.sict.ums.sys.acc.vo.UserVO;
import kr.co.sict.ums.sys.log.service.SystemLogService;
import kr.co.sict.ums.sys.log.vo.ActionLogVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.PageUtil;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Controller
@RequestMapping("/rns/ana")
public class RnsAnalysisController {
	private Logger logger = Logger.getLogger(this.getClass());
 
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private RnsAnalysisService rnsAnalysisService;
	
	@Autowired
	private CryptoService cryptoService;
	
	@Autowired
	private SystemLogService systemLogService;
	
	@Autowired
	private PropertiesUtil properties;
	
	/**
	 * 월별통계 서비스 목록 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/monthListP")
	public String goMonthListP(@ModelAttribute RnsAnaMonthVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
 
		// 서비스 목록 조회
		List<CodeVO> serviceCodeList = null;
		try {
			serviceCodeList = rnsAnalysisService.getServiceCodeList();
		} catch(Exception e) {
			logger.error("rnsAnalysisService.getServiceCodeList error = " + e);
		}
		
		// 발송자 목록 조회 
		List<String> senderList = null;
		try {
			senderList = rnsAnalysisService.getSenderList();
		} catch(Exception e) {
			logger.error("rnsAnalysisService.getSenderList error = " + e);
		} 
		 
		model.addAttribute("searchVO", searchVO); 				// 검색항목
		model.addAttribute("serviceCodeList", serviceCodeList); // 서비스목록
		model.addAttribute("senderList", senderList); 			// 발신자목록  
		
		return "rns/ana/monthListP";
	}
	
	/**
	 * 월별통계 서비스  목록을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/monthList")
	public String goMonthList(@ModelAttribute RnsAnaMonthVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMonthList searchYm     = " + searchVO.getSearchYm());
		logger.debug("goMonthList searchStatus       = " + searchVO.getSearchStatus());
		logger.debug("goMonthList searchService      = " + searchVO.getSearchService());
		logger.debug("goMonthList searchSname      = " + searchVO.getSearchSname());
 
		searchVO.setSearchYm(searchVO.getSearchYm().replaceAll("\\.", ""));  
		searchVO.setSearchYm(searchVO.getSearchYm().substring(0,6));
		
		logger.debug("goMonthList searchYm      = " + searchVO.getSearchYm());
 
		//월별 일자 리스트 만들기 
		int year = 0;
		int month = 0; 
		int day = 0 ;
		String fromYMD = "";
		String toYMD = "";
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Calendar calendar = Calendar.getInstance(); 		

		Date startDay = new Date(); 
		Date endDay = new Date(); 
		
		List<RnsAnaMonthVO> monthDataList = new ArrayList<RnsAnaMonthVO>();
		
		try { 
			startDay = transFormat.parse(searchVO.getSearchYm() +"01" + " 00:00:00");
			calendar.setTime(startDay);
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH) + 1;
			day = calendar.get(Calendar.DATE) ;
			int start = 1; 
			int end =calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			fromYMD = Integer.toString(year)+ getIntToZeroString(month, 2) + getIntToZeroString(start, 2) ;
			toYMD  = Integer.toString(year)+ getIntToZeroString(month, 2) + getIntToZeroString(end, 2) ; 

			startDay = transFormat.parse(fromYMD+ " 00:00:00");
			endDay = transFormat.parse(toYMD + " 00:00:00");
			
			long calDateDays = 0 ; 
			
			calDateDays = (endDay.getTime() - startDay.getTime()) / ( 24*60*60*1000); 
			calDateDays = Math.abs(calDateDays) + 1;	
			
			calendar.setTime(startDay);	
			String addDay = "";
			String addDisplayDay="";
			
			for (int i = 1 ; i <= calDateDays ; i ++ ) {
				RnsAnaMonthVO monthVO = new RnsAnaMonthVO(); 
				addDay = transFormat.format(calendar.getTime()).substring(6, 8);
				addDisplayDay = Integer.toString( Integer.parseInt(addDay) ) + '일';
				monthVO.setDays( Integer.parseInt(addDay));
				monthVO.setDisplayDays(addDisplayDay);
				monthVO.setSend(0); 
				monthVO.setSuccess(0);
				monthVO.setFailed(0);
				monthVO.setOpened(0);				
				monthVO.setDisplaySuccess("0(0%)");
				monthVO.setDisplayFailed("0(0%)");
				monthVO.setDisplayOpened("0(0%)");
				
				monthDataList.add(monthVO);
				calendar.add(Calendar.DATE, 1);
			}
		} catch (Exception e) {
			logger.error("scheduleService.goMonthList Make Month List error = " + e);
		}
		
		List<RnsAnaMonthVO> orgMonthList = null;
		try {			
			// 월별 내역  조회			 
			orgMonthList = rnsAnalysisService.getMonthList(searchVO);			 
		} catch (Exception e) {
			logger.error("rnsAnalysisService.getMonthList error = " + e);
		}
		
		int days = 0;
		int send = 0 ;
		int success = 0 ;
		int failed = 0 ; 
		int opened = 0 ;
		 
		int perSuccess = 0 ;
		int perFailed = 0 ; 
		int perOpened = 0 ;
		
		String displaySuccess = "";
		String displayFailed= "";
		String displayOpened = "";
		
		for(int i = 0; i < monthDataList.size() ; i++) {
			days = monthDataList.get(i).getDays();
			for (int j = 0; j < orgMonthList.size() ; j++) {		
				if(days == orgMonthList.get(j).getDays()) {
					send = orgMonthList.get(j).getSend(); 
					success = orgMonthList.get(j).getSuccess();
					failed = orgMonthList.get(j).getFailed();
					opened = orgMonthList.get(j).getOpened();
					 
					perFailed = (int) Math.round((double) failed / send * 100.0)  ;
					perOpened = (int) Math.round((double) opened / send * 100.0) ;
					perSuccess =(int) Math.round((double) success / send * 100.0) ;
					
					displaySuccess  = Integer.toString(success) + "(" + perSuccess + "%)";
					displayFailed  = Integer.toString(failed) + "(" + perFailed + "%)";
					displayOpened  = Integer.toString(opened) + "(" + perOpened + "%)";
										
					monthDataList.get(i).setSend(orgMonthList.get(j).getSend());
					monthDataList.get(i).setSuccess(orgMonthList.get(j).getSuccess());
					monthDataList.get(i).setFailed(orgMonthList.get(j).getFailed());
					monthDataList.get(i).setOpened(orgMonthList.get(j).getOpened());
					
					monthDataList.get(i).setDisplaySuccess(displaySuccess);
					monthDataList.get(i).setDisplayFailed(displayFailed);
					monthDataList.get(i).setDisplayOpened(displayOpened);				
				}
			}
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("monthDataList", monthDataList);		// 서비스목록 
		
		return "rns/ana/monthList";
	}

	/**
	 * 월별통계 목록 엑셀 다운로드
	 * @param sendLogVO
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/monthExcelList")	
	public void goMonthExcelList(@ModelAttribute RnsAnaMonthVO searchVO, HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		logger.debug("goMonthList searchYm     = " + searchVO.getSearchYm());
		logger.debug("goMonthList searchStatus       = " + searchVO.getSearchStatus());
		logger.debug("goMonthList searchService      = " + searchVO.getSearchService());
		logger.debug("goMonthList searchSname      = " + searchVO.getSearchSname());
 
		searchVO.setSearchYm(searchVO.getSearchYm().replaceAll("\\.", ""));  
		searchVO.setSearchYm(searchVO.getSearchYm().substring(0,6));
		
		logger.debug("goMonthList searchYm      = " + searchVO.getSearchYm());
		  
		//월별 일자 리스트 만들기 
		int year = 0;
		int month = 0; 
		int day = 0 ;
		String fromYMD = "";
		String toYMD = "";
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Calendar calendar = Calendar.getInstance(); 		

		Date startDay = new Date(); 
		Date endDay = new Date(); 
		
		List<RnsAnaMonthVO> monthDataList = new ArrayList<RnsAnaMonthVO>();
		
		try { 
			startDay = transFormat.parse(searchVO.getSearchYm() +"01" + " 00:00:00");
			calendar.setTime(startDay);
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH) + 1;
			day = calendar.get(Calendar.DATE) ;
			int start = 1; 
			int end =calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			fromYMD = Integer.toString(year)+ getIntToZeroString(month, 2) + getIntToZeroString(start, 2) ;
			toYMD  = Integer.toString(year)+ getIntToZeroString(month, 2) + getIntToZeroString(end, 2) ; 

			startDay = transFormat.parse(fromYMD+ " 00:00:00");
			endDay = transFormat.parse(toYMD + " 00:00:00");
			
			long calDateDays = 0 ; 
			
			calDateDays = (endDay.getTime() - startDay.getTime()) / ( 24*60*60*1000); 
			calDateDays = Math.abs(calDateDays) + 1;	
			
			calendar.setTime(startDay);	
			String addDay = "";
			String addDisplayDay="";
			
			for (int i = 1 ; i <= calDateDays ; i ++ ) {
				RnsAnaMonthVO monthVO = new RnsAnaMonthVO(); 
				addDay = transFormat.format(calendar.getTime()).substring(6, 8);
				addDisplayDay = Integer.toString( Integer.parseInt(addDay) ) + '일';
				monthVO.setDays( Integer.parseInt(addDay));
				monthVO.setDisplayDays(addDisplayDay);
				monthVO.setSend(0); 
				monthVO.setSuccess(0);
				monthVO.setFailed(0);
				monthVO.setOpened(0);				
				monthVO.setDisplaySuccess("0(0%)");
				monthVO.setDisplayFailed("0(0%)");
				monthVO.setDisplayOpened("0(0%)");
				
				monthDataList.add(monthVO);
				calendar.add(Calendar.DATE, 1);
			}
		} catch (Exception e) {
			logger.error("scheduleService.goMonthList Make Month List error = " + e);
		}
		
		List<RnsAnaMonthVO> orgMonthList = null;
		try {			
			// 월별 내역  조회			 
			orgMonthList = rnsAnalysisService.getMonthList(searchVO);			 
		} catch (Exception e) {
			logger.error("rnsAnalysisService.getMonthList error = " + e);
		}
		
		int days = 0;
		int send = 0 ;
		int success = 0 ;
		int failed = 0 ; 
		int opened = 0 ;
		 
		int perSuccess = 0 ;
		int perFailed = 0 ; 
		int perOpened = 0 ;
		
		String displaySuccess = "";
		String displayFailed= "";
		String displayOpened = "";
		
		for(int i = 0; i < monthDataList.size() ; i++) {
			days = monthDataList.get(i).getDays();
			for (int j = 0; j < orgMonthList.size() ; j++) {		
				if(days == orgMonthList.get(j).getDays()) {
					send = orgMonthList.get(j).getSend(); 
					success = orgMonthList.get(j).getSuccess();
					failed = orgMonthList.get(j).getFailed();
					opened = orgMonthList.get(j).getOpened();
					 
					perFailed = (int) Math.round((double) failed / send * 100.0)  ;
					perOpened = (int) Math.round((double) opened / send * 100.0) ;
					perSuccess =(int) Math.round((double) success / send * 100.0) ;
					
					displaySuccess  = Integer.toString(success) + "(" + perSuccess + "%)";
					displayFailed  = Integer.toString(failed) + "(" + perFailed + "%)";
					displayOpened  = Integer.toString(opened) + "(" + perOpened + "%)";
										
					monthDataList.get(i).setSend(orgMonthList.get(j).getSend());
					monthDataList.get(i).setSuccess(orgMonthList.get(j).getSuccess());
					monthDataList.get(i).setFailed(orgMonthList.get(j).getFailed());
					monthDataList.get(i).setOpened(orgMonthList.get(j).getOpened());
					
					monthDataList.get(i).setDisplaySuccess(displaySuccess);
					monthDataList.get(i).setDisplayFailed(displayFailed);
					monthDataList.get(i).setDisplayOpened(displayOpened);				
				}
			}
		}
  
		try {
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("AnalysisMonth");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			
			// 엑셀 헤더 생성 ( DAYS | SEND | SUCCESS | FAILED | OPENED  )
			row = sheet.createRow(rowNum++);
			cell = row.createCell(0); cell.setCellStyle(headerStyle); cell.setCellValue("일자");
			cell = row.createCell(1); cell.setCellStyle(headerStyle); cell.setCellValue("발송건수");
			cell = row.createCell(2); cell.setCellStyle(headerStyle); cell.setCellValue("성공건수(%)");
			cell = row.createCell(3); cell.setCellStyle(headerStyle); cell.setCellValue("실패건수(%)");
			cell = row.createCell(4); cell.setCellStyle(headerStyle); cell.setCellValue("오픈건수(%)"); 
			
			int recCnt =0; 
			
			// 엑셀 내용 추가
			for(RnsAnaMonthVO log:monthDataList) {
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0); cell.setCellValue( log.getDisplayDays() );// DAYS
				cell = row.createCell(1); cell.setCellValue( log.getSend());			// SEND
				cell = row.createCell(2); cell.setCellValue( log.getDisplaySuccess() );	// SUCCESS
				cell = row.createCell(3); cell.setCellValue( log.getDisplayFailed() );	// FAILED
				cell = row.createCell(4); cell.setCellValue( log.getDisplayOpened() );	// OPENED 
				recCnt += 1;
			}
			
			// 컨텐츠 타입과 파일명 지정
			String fileName = "AnalysisMonth_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			 
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 공통코드(C112) = 002:엑셀다운로드
			logVO.setContent("M2002001");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setMobilYn("N");
			logVO.setRecCnt(recCnt); 
			
			systemLogService.insertActionLog(request, session, logVO);
			
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		} 
	}
	
	/**
	 *  서비스별 통계 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/serviceListP")
	public String goServiceListP(@ModelAttribute RnsAnaServiceVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 검색 기본값 설정
		if(StringUtil.isNull(searchVO.getSearchStdDt())) {
			searchVO.setSearchStdDt(StringUtil.getCalcDateFromCurr(-2, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchStdDt(searchVO.getSearchStdDt().replaceAll("\\.", ""));
		}
		
		if(StringUtil.isNull(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		}
		 
		// 발송자 목록 조회 
		List<String> senderList = null;
		try {
			senderList = rnsAnalysisService.getSenderList();
		} catch(Exception e) {
			logger.error("rnsAnalysisService.getSenderList error = " + e);
		}
		
		 
		model.addAttribute("searchVO", searchVO); 			// 검색항목 
		model.addAttribute("senderList", senderList); 		// 사용자목록  
		
		return "rns/ana/serviceListP";
	}
	
	/**
	 * 서비스별 통계 목록을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/serviceList")
	public String goServiceList(@ModelAttribute RnsAnaServiceVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goServiceList SearchStdDt     = " + searchVO.getSearchStdDt());
		logger.debug("goServiceList SearchEndDt       = " + searchVO.getSearchEndDt());
		logger.debug("goServiceList searchSname      = " + searchVO.getSearchSname());
		 
		searchVO.setSearchStdDt(searchVO.getSearchStdDt().replaceAll("\\.", ""));  
		searchVO.setSearchStdDt(searchVO.getSearchStdDt().substring(0,8));
		
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));  
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().substring(0,8));
		
		
		logger.debug("goServiceList SearchStdDt      = " + searchVO.getSearchStdDt());
		logger.debug("goServiceList SearchEndDt      = " + searchVO.getSearchEndDt());
 
		List<RnsAnaServiceVO> serviceDataList = null;
		try {			
			// 서비스별 내역  조회			 
			serviceDataList = rnsAnalysisService.getServiceList(searchVO);			 
		} catch (Exception e) {
			logger.error("rnsAnalysisService.getServiceList error = " + e);
		}
		
		int send= 0;
		int success = 0 ; 
		int failed = 0 ;
		int reply = 0;
		 
		int perSuccess = 0 ;
		int perFailed = 0 ; 
		int perReply = 0 ;
		
		String displaySuccess = "";
		String displayFailed= "";
		String displayReply = "";		
		
		if (serviceDataList.size() > 0 ) {
			for (int i = 0 ; i < serviceDataList.size(); i++) {
				
				send = serviceDataList.get(i).getSend(); 
				success = serviceDataList.get(i).getSuccess();
				failed = serviceDataList.get(i).getFailed();
				reply = serviceDataList.get(i).getReply();
				 
				perFailed = (int) Math.round((double) failed / send * 100.0)  ;
				perReply = (int) Math.round((double) reply / send * 100.0) ;
				perSuccess =(int) Math.round((double) success / send * 100.0) ;
				
				displaySuccess  = Integer.toString(success) + "(" + perSuccess + "%)";
				displayFailed  = Integer.toString(failed) + "(" + perFailed + "%)";
				displayReply  = Integer.toString(reply) + "(" + perReply + "%)";
  
				serviceDataList.get(i).setDisplaySuccess(displaySuccess);;
				serviceDataList.get(i).setDisplayFailed(displayFailed);
				serviceDataList.get(i).setDisplayReply(displayReply);
			}
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("serviceDataList", serviceDataList);		// 서비스목록 
		
		return "rns/ana/serviceList";
	}
	  
	/**
	 * 서비스별 통계 목록을 엘셀 파일로 다운로드 한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/serviceExcelList")
	public void goServiceExcelList(@ModelAttribute RnsAnaServiceVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goServiceExcelList SearchStdDt     = " + searchVO.getSearchStdDt());
		logger.debug("goServiceExcelList SearchEndDt       = " + searchVO.getSearchEndDt());
		logger.debug("goServiceExcelList searchSname      = " + searchVO.getSearchSname());
		 
		searchVO.setSearchStdDt(searchVO.getSearchStdDt().replaceAll("\\.", ""));  
		searchVO.setSearchStdDt(searchVO.getSearchStdDt().substring(0,8));
		
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));  
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().substring(0,8));
		  
		logger.debug("goServiceList SearchStdDt      = " + searchVO.getSearchStdDt());
		logger.debug("goServiceList SearchEndDt      = " + searchVO.getSearchEndDt());
 
		List<RnsAnaServiceVO> serviceDataList = null;
		try {			
			// 서비스별 내역  조회			 
			serviceDataList = rnsAnalysisService.getServiceList(searchVO);			 
		} catch (Exception e) {
			logger.error("rnsAnalysisService.getServiceList error = " + e);
		}
		
		int send= 0;
		int success = 0 ; 
		int failed = 0 ;
		int reply = 0;
		 
		int perSuccess = 0 ;
		int perFailed = 0 ; 
		int perReply = 0 ;
		
		String displaySuccess = "";
		String displayFailed= "";
		String displayReply = "";		
		
		if (serviceDataList.size() > 0 ) {
			for (int i = 0 ; i < serviceDataList.size(); i++) {
				
				send = serviceDataList.get(i).getSend(); 
				success = serviceDataList.get(i).getSuccess();
				failed = serviceDataList.get(i).getFailed();
				reply = serviceDataList.get(i).getReply();
				 
				perFailed = (int) Math.round((double) failed / send * 100.0)  ;
				perReply = (int) Math.round((double) reply / send * 100.0) ;
				perSuccess =(int) Math.round((double) success / send * 100.0) ;
				
				displaySuccess  = Integer.toString(success) + "(" + perSuccess + "%)";
				displayFailed  = Integer.toString(failed) + "(" + perFailed + "%)";
				displayReply  = Integer.toString(reply) + "(" + perReply + "%)";
  
				serviceDataList.get(i).setDisplaySuccess(displaySuccess);;
				serviceDataList.get(i).setDisplayFailed(displayFailed);
				serviceDataList.get(i).setDisplayReply(displayReply);
			}
		}
		
		try {
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("AnalysisService");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			
			// 엑셀 헤더 생성 ( TID | TNM | SEND | SUCCESS | FAILED | REPLY |   )
			row = sheet.createRow(rowNum++);
			cell = row.createCell(0); cell.setCellStyle(headerStyle); cell.setCellValue("서비스ID");
			cell = row.createCell(1); cell.setCellStyle(headerStyle); cell.setCellValue("서비스항목");
			cell = row.createCell(2); cell.setCellStyle(headerStyle); cell.setCellValue("발송건수");
			cell = row.createCell(3); cell.setCellStyle(headerStyle); cell.setCellValue("성공수(%)");
			cell = row.createCell(4); cell.setCellStyle(headerStyle); cell.setCellValue("실패수(%)");
			cell = row.createCell(5); cell.setCellStyle(headerStyle); cell.setCellValue("응답수(%)"); 
			
			int recCnt =0; 
			
			// 엑셀 내용 추가
			for(RnsAnaServiceVO log:serviceDataList) {
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0); cell.setCellValue( log.getTid() );			// TID
				cell = row.createCell(1); cell.setCellValue( log.getTnm() );			// TNM
				cell = row.createCell(2); cell.setCellValue( log.getSend() );			// SEND
				cell = row.createCell(3); cell.setCellValue( log.getDisplaySuccess() );	// SUCCESS
				cell = row.createCell(4); cell.setCellValue( log.getDisplayFailed() );	// FAILED
				cell = row.createCell(5); cell.setCellValue( log.getDisplayReply() );	// REPLY 
				recCnt += 1;
			}
			
			// 컨텐츠 타입과 파일명 지정
			String fileName = "AnalysisService_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			 
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 공통코드(C112) = 002:엑셀다운로드
			logVO.setContent("M2002002");			
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setMobilYn("N");
			logVO.setRecCnt(recCnt); 
			
			systemLogService.insertActionLog(request, session, logVO);
			
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}
	}
	

	/**
	 *  도메인별  통계 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/domainListP")
	public String goDomainListP(@ModelAttribute RnsAnaDomainVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
 
		// 검색 기본값 설정
		if(StringUtil.isNull(searchVO.getSearchStdDt())) {
			searchVO.setSearchStdDt(StringUtil.getCalcDateFromCurr(-2, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchStdDt(searchVO.getSearchStdDt().replaceAll("\\.", ""));
		}
		
		if(StringUtil.isNull(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		} 
		 
		model.addAttribute("searchVO", searchVO); 			// 검색항목  
		
		return "rns/ana/domainListP";
	}
	
	/**
	 * 도메인별 통계 목록을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/domainList")
	public String goDomainList(@ModelAttribute RnsAnaDomainVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
 
		logger.debug("goDomainList SearchStdDt     = " + searchVO.getSearchStdDt());
		logger.debug("goDomainList SearchEndDt       = " + searchVO.getSearchEndDt()); 
		 
		searchVO.setSearchStdDt(searchVO.getSearchStdDt().replaceAll("\\.", ""));  
		searchVO.setSearchStdDt(searchVO.getSearchStdDt().substring(2,6));
		
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));  
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().substring(2,6));		
		
		logger.debug("goDomainList SearchStdDt      = " + searchVO.getSearchStdDt());
		logger.debug("goDomainList SearchEndDt      = " + searchVO.getSearchEndDt());
 
		List<RnsAnaDomainVO> domainDataList = null;
		try {			
			// 서비스별 내역  조회
			domainDataList = rnsAnalysisService.getDomainList(searchVO);			 
		} catch (Exception e) {
			logger.error("rnsAnalysisService.goDomainList error = " + e);
		}
		
		int total;		//총발송건수
		int success;	//성공수
		int fail;		//실패수
		int syntax;		//문법오류
		int dns;		//DNS에러
		int transact;	//트랜잭션에러
		int receiver;	//수신자오류
		int network;	//네트워크
		int service;	//서비스장애

		int perSuccess = 0; 
		int perFail = 0;  
		int perSyntax = 0; 
		int perDns = 0; 
		int perTransact = 0; 
		int perReceiver = 0;
		int perNetwork = 0;
		int perService = 0;
		
		String displaySuccess= "";		//표기되는 성공(%)
		String displayFail= "";			//표기되는 실패(%)
		String displaySyntax= "";		//표기되는 문법오류(%)
		String displayDns= "";			//표기되는 DNS에러(%)
		String displayTransact= "";		//표기되는 트랜잭션에러(%)
		String displayReceiver= "";		//표기되는 수신자오류(%)
		String displayNetwork= "";		//표기되는 네트워크(%)
		String displayService= "";		//표기되는 서비스장애(%)
		
		if (domainDataList.size() > 0 ) {
			for (int i = 0 ; i < domainDataList.size(); i++) {
				
				total = domainDataList.get(i).getTotal(); 
				success = domainDataList.get(i).getSuccess();
				fail = domainDataList.get(i).getFail();
				syntax = domainDataList.get(i).getSyntax();
				dns = domainDataList.get(i).getDns(); 
				transact = domainDataList.get(i).getTransact();
				receiver = domainDataList.get(i).getReceiver();
				network = domainDataList.get(i).getNetwork();
				service = domainDataList.get(i).getService();
			  
				perSuccess = (int) Math.round((double) success / total * 100.0);
				perFail = (int) Math.round((double) fail / total * 100.0);
				perSyntax =(int) Math.round((double) syntax / total * 100.0);
				perDns =(int) Math.round((double) dns / total * 100.0);
				perTransact =(int) Math.round((double) transact / total * 100.0);
				perReceiver =(int) Math.round((double) receiver / total * 100.0);
				perNetwork =(int) Math.round((double) network / total * 100.0);
				perService =(int) Math.round((double) service / total * 100.0);
				
				displaySuccess = "(" + perSuccess + "%)"; 
				displayFail = "(" + perFail + "%)";
				displaySyntax = "(" + perSyntax + "%)";
				displayDns = "(" + perDns + "%)";
				displayTransact = "(" + perTransact + "%)";
				displayReceiver = "(" + perReceiver + "%)";
				displayNetwork = "(" + perNetwork + "%)";
				displayService = "(" + perService + "%)";
				 
				domainDataList.get(i).setDisplaySuccess(displaySuccess);;
				domainDataList.get(i).setDisplayFail(displayFail);
				domainDataList.get(i).setDisplaySyntax(displaySyntax);
				domainDataList.get(i).setDisplayDns(displayDns);
				domainDataList.get(i).setDisplayTransact(displayTransact);
				domainDataList.get(i).setDisplayReceiver(displayReceiver);
				domainDataList.get(i).setDisplayNetwork(displayNetwork);
				domainDataList.get(i).setDisplayService(displayService);
			}
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("domainDataList", domainDataList);		// 서비스목록 
		 
		
		return "rns/ana/domainList";
	}
 
	/**
	 * 도메인별 통계 목록을 엑셀로 다운로드한다
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/domainExcelList")
	public void doDomainExcelList(@ModelAttribute RnsAnaDomainVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
 
		logger.debug("doDomainExcelList SearchStdDt     = " + searchVO.getSearchStdDt());
		logger.debug("doDomainExcelList SearchEndDt       = " + searchVO.getSearchEndDt()); 
		 
		searchVO.setSearchStdDt(searchVO.getSearchStdDt().replaceAll("\\.", ""));  
		searchVO.setSearchStdDt(searchVO.getSearchStdDt().substring(2,6));
		
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));  
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().substring(2,6));		
		
		logger.debug("goDomainList SearchStdDt      = " + searchVO.getSearchStdDt());
		logger.debug("goDomainList SearchEndDt      = " + searchVO.getSearchEndDt());
 
		List<RnsAnaDomainVO> domainDataList = null;
		try {			
			// 서비스별 내역  조회
			domainDataList = rnsAnalysisService.getDomainList(searchVO);			 
		} catch (Exception e) {
			logger.error("rnsAnalysisService.goDomainList error = " + e);
		}
		
		int total;		//총발송건수
		int success;	//성공수
		int fail;		//실패수
		int syntax;		//문법오류
		int dns;		//DNS에러
		int transact;	//트랜잭션에러
		int receiver;	//수신자오류
		int network;	//네트워크
		int service;	//서비스장애

		int perSuccess = 0; 
		int perFail = 0;  
		int perSyntax = 0; 
		int perDns = 0; 
		int perTransact = 0; 
		int perReceiver = 0;
		int perNetwork = 0;
		int perService = 0;
		
		String displaySuccess= "";		//표기되는 성공(%)
		String displayFail= "";			//표기되는 실패(%)
		String displaySyntax= "";		//표기되는 문법오류(%)
		String displayDns= "";			//표기되는 DNS에러(%)
		String displayTransact= "";		//표기되는 트랜잭션에러(%)
		String displayReceiver= "";		//표기되는 수신자오류(%)
		String displayNetwork= "";		//표기되는 네트워크(%)
		String displayService= "";		//표기되는 서비스장애(%)
		
		if (domainDataList.size() > 0 ) {
			for (int i = 0 ; i < domainDataList.size(); i++) {
				
				total = domainDataList.get(i).getTotal(); 
				success = domainDataList.get(i).getSuccess();
				fail = domainDataList.get(i).getFail();
				syntax = domainDataList.get(i).getSyntax();
				dns = domainDataList.get(i).getDns(); 
				transact = domainDataList.get(i).getTransact();
				receiver = domainDataList.get(i).getReceiver();
				network = domainDataList.get(i).getNetwork();
				service = domainDataList.get(i).getService();
			  
				perSuccess = (int) Math.round((double) success / total * 100.0);
				perFail = (int) Math.round((double) fail / total * 100.0);
				perSyntax =(int) Math.round((double) syntax / total * 100.0);
				perDns =(int) Math.round((double) dns / total * 100.0);
				perTransact =(int) Math.round((double) transact / total * 100.0);
				perReceiver =(int) Math.round((double) receiver / total * 100.0);
				perNetwork =(int) Math.round((double) network / total * 100.0);
				perService =(int) Math.round((double) service / total * 100.0);
				
				displaySuccess = "(" + perSuccess + "%)"; 
				displayFail = "(" + perFail + "%)";
				displaySyntax = "(" + perSyntax + "%)";
				displayDns = "(" + perDns + "%)";
				displayTransact = "(" + perTransact + "%)";
				displayReceiver = "(" + perReceiver + "%)";
				displayNetwork = "(" + perNetwork + "%)";
				displayService = "(" + perService + "%)";
				 
				domainDataList.get(i).setDisplaySuccess(displaySuccess);;
				domainDataList.get(i).setDisplayFail(displayFail);
				domainDataList.get(i).setDisplaySyntax(displaySyntax);
				domainDataList.get(i).setDisplayDns(displayDns);
				domainDataList.get(i).setDisplayTransact(displayTransact);
				domainDataList.get(i).setDisplayReceiver(displayReceiver);
				domainDataList.get(i).setDisplayNetwork(displayNetwork);
				domainDataList.get(i).setDisplayService(displayService);
			}
		}
 
		try {
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("AnalysisDomain");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			
			// 엑셀 헤더 생성 ( DOMAINNAME | SEND | SUCCESS | FAILED | SYNTAX | DNS | TRANSACT | RECEIVER | NETWORK | SERVICE  )
			row = sheet.createRow(rowNum++);
			cell = row.createCell(0); cell.setCellStyle(headerStyle); cell.setCellValue("도메인명");			
			cell = row.createCell(1); cell.setCellStyle(headerStyle); cell.setCellValue("총발송건수");
			cell = row.createCell(2); cell.setCellStyle(headerStyle); cell.setCellValue("성공수(%)");
			cell = row.createCell(3); cell.setCellStyle(headerStyle); cell.setCellValue("실패수(%)");
			cell = row.createCell(4); cell.setCellStyle(headerStyle); cell.setCellValue("문법오류(%)");
			cell = row.createCell(5); cell.setCellStyle(headerStyle); cell.setCellValue("DNS에러(%)");
			cell = row.createCell(6); cell.setCellStyle(headerStyle); cell.setCellValue("트랜잭션에러(%)");
			cell = row.createCell(7); cell.setCellStyle(headerStyle); cell.setCellValue("수신자오류(%)"); 
			cell = row.createCell(8); cell.setCellStyle(headerStyle); cell.setCellValue("네트워크(%)");
			cell = row.createCell(9); cell.setCellStyle(headerStyle); cell.setCellValue("서비스장애(%)");
			
			int recCnt =0; 
			
			// 엑셀 내용 추가
			for(RnsAnaDomainVO log:domainDataList) {
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0); cell.setCellValue( log.getDomainname() );		// DOMAINNAME
				cell = row.createCell(1); cell.setCellValue( log.getTotal() );			// SEND
				cell = row.createCell(2); cell.setCellValue( log.getDisplaySuccess() );	// SUCCESS
				cell = row.createCell(3); cell.setCellValue( log.getDisplayFail() );	// FAILED
				cell = row.createCell(4); cell.setCellValue( log.getDisplaySyntax() );	// SYNTAX
				cell = row.createCell(5); cell.setCellValue( log.getDisplayDns() );		// DNS
				cell = row.createCell(6); cell.setCellValue( log.getDisplayTransact() );// TRANSACT
				cell = row.createCell(7); cell.setCellValue( log.getDisplayReceiver() );// RECEIVER 
				cell = row.createCell(6); cell.setCellValue( log.getDisplayNetwork() );	// NETWORK
				cell = row.createCell(7); cell.setCellValue( log.getDisplayService() );	// SERVICE 				
				recCnt += 1;
			}
			
			// 컨텐츠 타입과 파일명 지정
			String fileName = "AnalysisDomain_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			 
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 공통코드(C112) = 002:엑셀다운로드
			logVO.setContent("M2002003");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setMobilYn("N");
			logVO.setRecCnt(recCnt); 
			
			systemLogService.insertActionLog(request, session, logVO);
			
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}		
		
	}
	
	/**
	 *  수신자별  통계 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/receiverListP")
	public String goReceiverListP(@ModelAttribute RnsAnaDetailLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
 
		searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		searchVO.setPage(page);
		
		// 검색 기본값 설정
		if(StringUtil.isNull(searchVO.getSearchStdDt())) {
			searchVO.setSearchStdDt(StringUtil.getCalcDateFromCurr(-2, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchStdDt(searchVO.getSearchStdDt().replaceAll("\\.", ""));
		}
		
		if(StringUtil.isNull(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		}
		 
		// 발송결과 코드 목록을 조회한다.(ROCDE를 조회하는건데 코드가 안맞음 추후 수정필요함)
		CodeVO rcodeVO = new CodeVO();
		rcodeVO.setUilang((String) session.getAttribute("NEO_UILANG")); 
		rcodeVO.setUseYn("Y");
		
		List<CodeVO> rcodeList = null;
		try {
			rcodeList = rnsAnalysisService.getRcodeList(rcodeVO);
		} catch (Exception e) {
			logger.error("rnsAnalysisService.getRcodeList error = " + e);
		}
		
		// 서비스 목록 조회
		List<CodeVO> serviceCodeList = null;
		try {
			serviceCodeList = rnsAnalysisService.getServiceCodeList();
		} catch(Exception e) {
			logger.error("rnsAnalysisService.getServiceCodeList error = " + e);
		} 
		
		model.addAttribute("searchVO", searchVO); 			 // 검색항목
		model.addAttribute("rcodeList", rcodeList);			 //
		model.addAttribute("serviceCodeList", serviceCodeList); // 
		
		return "rns/ana/receiverListP";
	}
	
	/**
	 * 수신자별 통계 목록을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/receiverList")
	public String goReceiverList(@ModelAttribute RnsAnaDetailLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		logger.debug("goReceiverList searchStdDt     = " + searchVO.getSearchStdDt());
		logger.debug("goReceiverList searchEndDt       = " + searchVO.getSearchEndDt()); 
		logger.debug("goReceiverList searchRcode       = " + searchVO.getSearchRcode());
		logger.debug("goReceiverList searchRid      = " + searchVO.getSearchRid());
		logger.debug("goReceiverList searchRname      = " + searchVO.getSearchRname());
		logger.debug("goReceiverList searchRmail      = " + searchVO.getSearchRmail());		
		logger.debug("goReceiverList searchTid      = " + searchVO.getSearchTid()); 
		 
		searchVO.setSearchStdDt(searchVO.getSearchStdDt().replaceAll("\\.", ""));  
		searchVO.setSearchStdDt(searchVO.getSearchStdDt().substring(0,8));
		
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));  
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().substring(0,8));
		 
		logger.debug("goServiceList SearchStdDt      = " + searchVO.getSearchStdDt());
		logger.debug("goServiceList SearchEndDt      = " + searchVO.getSearchEndDt());
 
		if(searchVO.getSearchRcode() != null) {
			searchVO.setSearchRcode(searchVO.getSearchRcode().replaceAll(" ", ""));
		}
  
		searchVO.setUilang((String) session.getAttribute("NEO_UILANG")); 

		String plainRmail = "";
		if(searchVO.getSearchRmail() != null && !"".equals(searchVO.getSearchRmail())) {
			plainRmail = searchVO.getSearchRmail();
			searchVO.setSearchRmail(cryptoService.getEncrypt("RMAIL", searchVO.getSearchRmail()));
		}
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		List<RnsAnaDetailLogVO> receiverDataList = null;
		try {			
			//수신자별 내역  조회
			receiverDataList = rnsAnalysisService.getReceiverList(searchVO);
		} catch (Exception e) {
			logger.error("rnsAnalysisService.getReceiverList error = " + e);
		}
		 
		// 페이징 설정
		if(receiverDataList != null && receiverDataList.size() > 0) {
			totalCount = receiverDataList.get(0).getTotalCount();
		}
	
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);		
		
		searchVO.setSearchRmail(plainRmail);		
		model.addAttribute("searchVO", searchVO);				// 검색항목
		model.addAttribute("receiverDataList", receiverDataList);		// 수신자별 결과
		model.addAttribute("pageUtil", pageUtil); 				// 페이징
		
		return "rns/ana/receiverList";
	}
	
	/**
	 * 수신자별 통계 목록을 엑셀로 다운르도한다
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/receiverExcelList")
	public void goReceiverExcelList(@ModelAttribute RnsAnaDetailLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		logger.debug("goReceiverExcelList searchStdDt   = " + searchVO.getSearchStdDt());
		logger.debug("goReceiverExcelList searchEndDt   = " + searchVO.getSearchEndDt()); 
		logger.debug("goReceiverExcelList searchRcode   = " + searchVO.getSearchRcode());
		logger.debug("goReceiverExcelList searchRid     = " + searchVO.getSearchRid());
		logger.debug("goReceiverExcelList searchRname   = " + searchVO.getSearchRname());
		logger.debug("goReceiverExcelList searchRmail   = " + searchVO.getSearchRmail());
		logger.debug("goReceiverExcelList searchTid     = " + searchVO.getSearchTid()); 
		 
		searchVO.setSearchStdDt(searchVO.getSearchStdDt().replaceAll("\\.", ""));  
		searchVO.setSearchStdDt(searchVO.getSearchStdDt().substring(0,8));
		
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));  
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().substring(0,8));
		 
		if(searchVO.getSearchRcode() != null) {
			searchVO.setSearchRcode(searchVO.getSearchRcode().replaceAll(" ", ""));
		}
		
		if(searchVO.getSearchRmail() != null && !"".equals(searchVO.getSearchRmail())) {			
			searchVO.setSearchRmail(cryptoService.getEncrypt("RMAIL", searchVO.getSearchRmail()));
		}
		
		// 페이지 설정
		int page = 1;
		int rows = 99999999;
		
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		
		logger.debug("goReceiverExcelList SearchStdDt      = " + searchVO.getSearchStdDt());
		logger.debug("goReceiverExcelList SearchEndDt      = " + searchVO.getSearchEndDt());
 
		searchVO.setUilang((String) session.getAttribute("NEO_UILANG")); 
 
		List<RnsAnaDetailLogVO> receiverDataList = null;
		try {			
			// 메일별  내역  조회
			receiverDataList = rnsAnalysisService.getReceiverList(searchVO);
		} catch (Exception e) {
			logger.error("rnsAnalysisService.getReceiverList error = " + e);
		}

		try {
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("AnalysisReceiver");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			
			// 엑셀 헤더 생성 ( ID | RMAIL | TNM | SDATE | RCODE | RSDATE | MID | SUBID | SUBJECT | SNAME | SMAIL | CDATE | CONTENTS )
			row = sheet.createRow(rowNum++);
			cell = row.createCell(0); cell.setCellStyle(headerStyle); cell.setCellValue("ID");			
			cell = row.createCell(1); cell.setCellStyle(headerStyle); cell.setCellValue("이메일");
			cell = row.createCell(2); cell.setCellStyle(headerStyle); cell.setCellValue("서비스구분");
			cell = row.createCell(3); cell.setCellStyle(headerStyle); cell.setCellValue("발송일시");
			cell = row.createCell(4); cell.setCellStyle(headerStyle); cell.setCellValue("발송결과");
			cell = row.createCell(5); cell.setCellStyle(headerStyle); cell.setCellValue("오픈일");
			cell = row.createCell(6); cell.setCellStyle(headerStyle); cell.setCellValue("MID");
			cell = row.createCell(7); cell.setCellStyle(headerStyle); cell.setCellValue("SUBID"); 
			
			int recCnt =0; 
			
			// 엑셀 내용 추가
			for(RnsAnaDetailLogVO log:receiverDataList) {
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0); cell.setCellValue( log.getRid() );	// ID
				cell = row.createCell(1); cell.setCellValue( cryptoService.getDecrypt("RMAIL", log.getRmail()) ); 	// RMAIL
				//cell = row.createCell(1); cell.setCellValue( log.getRmail() );	// RMAIL
				cell = row.createCell(2); cell.setCellValue( log.getTnm() );	// TNM
				cell = row.createCell(3); cell.setCellValue( log.getSdate() );	// SDATE
				cell = row.createCell(4); cell.setCellValue( log.getRcode() );	// RCODE
				cell = row.createCell(5); cell.setCellValue( log.getRsdate() );	// RSDATE
				cell = row.createCell(6); cell.setCellValue( log.getMid() );	// MID
				cell = row.createCell(7); cell.setCellValue( log.getSubid() );	// SUBID 
				recCnt += 1;
			}
			
			// 컨텐츠 타입과 파일명 지정
			String fileName = "AnalysisReceiver_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close(); 
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 공통코드(C112) = 002:엑셀다운로드
			logVO.setContent("M2002004");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setMobilYn("N");
			logVO.setRecCnt(recCnt); 
			
			systemLogService.insertActionLog(request, session, logVO);
			
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}		
	}
	

	/**
	 *  메일별  통계 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailListP")
	public String goMailListP(@ModelAttribute RnsAnaDetailLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
 
		searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		searchVO.setPage(page);
		
		// 검색 기본값 설정
		if(StringUtil.isNull(searchVO.getSearchStdDt())) {
			searchVO.setSearchStdDt(StringUtil.getCalcDateFromCurr(-2, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchStdDt(searchVO.getSearchStdDt().replaceAll("\\.", ""));
		}
		
		if(StringUtil.isNull(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		} 
		   
		// 서비스 목록 조회
		List<CodeVO> serviceCodeList = null;
		try {
			serviceCodeList = rnsAnalysisService.getServiceCodeList();
		} catch(Exception e) {
			logger.error("rnsAnalysisService.getServiceCodeList error = " + e);
		} 
		
		model.addAttribute("searchVO", searchVO);				// 검색항목 
		model.addAttribute("serviceCodeList", serviceCodeList); // 서비스목록
		
		return "rns/ana/mailListP";
	}
	
	/**
	 * 메일별 통계 목록을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailList")
	public String goMailList(@ModelAttribute RnsAnaDetailLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		logger.debug("goDetailLogList SearchStdDt     = " + searchVO.getSearchStdDt());
		logger.debug("goDetailLogList SearchEndDt       = " + searchVO.getSearchEndDt()); 
		logger.debug("goDetailLogList searchTid      = " + searchVO.getTid()); 
		 
		searchVO.setSearchStdDt(searchVO.getSearchStdDt().replaceAll("\\.", ""));  
		searchVO.setSearchStdDt(searchVO.getSearchStdDt().substring(0,8));
		
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));  
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().substring(0,8));
		 
		logger.debug("goServiceList SearchStdDt      = " + searchVO.getSearchStdDt());
		logger.debug("goServiceList SearchEndDt      = " + searchVO.getSearchEndDt());
 
		searchVO.setUilang((String) session.getAttribute("NEO_UILANG")); 
 
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		List<RnsAnaDetailLogVO> mailDataList = null;
		try {			
			// 메일별  내역  조회			 
			mailDataList = rnsAnalysisService.getMailList(searchVO);			 
		} catch (Exception e) {
			logger.error("rnsAnalysisService.getMailList error = " + e);
		}
		
		String mailStatus = "";
		if (mailDataList != null && mailDataList.size() > 0) {
			totalCount = mailDataList.get(0).getTotalCount();
 
			for (int i = 0 ; i < mailDataList.size(); i++) {
				mailStatus  = mailDataList.get(i).getStatus();
				if (mailStatus == null ){
					mailDataList.get(i).setStatus("전송성공");
				} else if ("0".equals(mailStatus)){
					mailDataList.get(i).setStatus("예약상태");
				} else if ("1".equals(mailStatus)){
					mailDataList.get(i).setStatus("발송자 이메일 주소 에러");
				} else if ("2".equals(mailStatus)){
					mailDataList.get(i).setStatus("수신자 이메일 주소 에러");
				} else if ("3".equals(mailStatus)){
					mailDataList.get(i).setStatus("수신자 리스트 추출 실패");
				} else if ("5".equals(mailStatus)){
					mailDataList.get(i).setStatus("컨텐트 생성 에러");
				} 
			}
 
		}

		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);		
		
		model.addAttribute("searchVO", searchVO);				// 검색항목
		model.addAttribute("mailDataList", mailDataList);		// 메일별 결과
		model.addAttribute("pageUtil", pageUtil); 				// 페이징
 
		return "rns/ana/mailList";
	}

	/**
	 * 메일별 통계 목록을 엑셀로 다운로드 한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailExcelList")
	public void goMailExcelList(@ModelAttribute RnsAnaDetailLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		logger.debug("goDetailLogList SearchStdDt     = " + searchVO.getSearchStdDt());
		logger.debug("goDetailLogList SearchEndDt       = " + searchVO.getSearchEndDt()); 
		logger.debug("goDetailLogList searchTid      = " + searchVO.getTid()); 
		 
		searchVO.setSearchStdDt(searchVO.getSearchStdDt().replaceAll("\\.", ""));  
		searchVO.setSearchStdDt(searchVO.getSearchStdDt().substring(0,8));
		
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));  
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().substring(0,8));
		 
		logger.debug("goServiceList SearchStdDt      = " + searchVO.getSearchStdDt());
		logger.debug("goServiceList SearchEndDt      = " + searchVO.getSearchEndDt());
 
		searchVO.setUilang((String) session.getAttribute("NEO_UILANG")); 
 
		// 페이지 설정
		int page = 1;
		int rows = 99999999;
		
		searchVO.setPage(page);
		searchVO.setRows(rows);		
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		
		List<RnsAnaDetailLogVO> mailDataList = null;
		try {			
			// 메일별  내역  조회			 
			mailDataList = rnsAnalysisService.getMailList(searchVO);
		} catch (Exception e) {
			logger.error("rnsAnalysisService.getMailList error = " + e);
		}
		
		try {
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("AnalysisService");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// 엑셀 헤더 생성 ( SUBJECT | TNM | CDATE | SDATE | STATUS | SUBID |   )
			row = sheet.createRow(rowNum++);
			cell = row.createCell(0); cell.setCellStyle(headerStyle); cell.setCellValue("제목");
			cell = row.createCell(1); cell.setCellStyle(headerStyle); cell.setCellValue("서비스구분");
			cell = row.createCell(2); cell.setCellStyle(headerStyle); cell.setCellValue("등록일시");
			cell = row.createCell(3); cell.setCellStyle(headerStyle); cell.setCellValue("발송일시");
			cell = row.createCell(4); cell.setCellStyle(headerStyle); cell.setCellValue("발송상태");
			cell = row.createCell(5); cell.setCellStyle(headerStyle); cell.setCellValue("재발송"); 
			
			int recCnt =0; 
			
			// 엑셀 내용 추가
			for(RnsAnaDetailLogVO log:mailDataList) {
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0); cell.setCellValue( log.getSubject() );// SUBJECT
				cell = row.createCell(1); cell.setCellValue( log.getTnm() );	// TNM
				cell = row.createCell(2); cell.setCellValue( log.getCdate() );	// CDATE
				cell = row.createCell(3); cell.setCellValue( log.getSdate() );	// SDATE
				cell = row.createCell(4); cell.setCellValue( log.getStatus() );	// STATUS
				if ( log.getSubid() == 0 ) {
					cell = row.createCell(5); cell.setCellValue( "본발송");		// SUBID
				} else {
					cell = row.createCell(5); cell.setCellValue( Integer.toString(log.getSubid()) + "차");// SUBID
				}
				 
				recCnt += 1;
			}
			
			// 컨텐츠 타입과 파일명 지정
			String fileName = "AnalysisMail_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			 
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 공통코드(C112) = 002:엑셀다운로드
			logVO.setContent("M2002005");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setMobilYn("N");
			logVO.setRecCnt(recCnt); 
			
			systemLogService.insertActionLog(request, session, logVO);
			
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}		
	}
	

	/**
	 *  상세로그내역 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/detailLogListP")
	public String goDetailLogListP(@ModelAttribute RnsAnaDetailLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
 
		searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		searchVO.setPage(page);
		
		// 검색 기본값 설정
		if(StringUtil.isNull(searchVO.getSearchStdDt())) {
			searchVO.setSearchStdDt(StringUtil.getCalcDateFromCurr(-2, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchStdDt(searchVO.getSearchStdDt().replaceAll("\\.", ""));
		}
		
		if(StringUtil.isNull(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		} 
		 
		 
		// 발송결과 코드 목록을 조회한다 
		CodeVO rcodeVO = new CodeVO();
		rcodeVO.setUilang((String) session.getAttribute("NEO_UILANG")); 
		rcodeVO.setUseYn("Y");

		List<CodeVO> rcodeList = null;
		try {
			rcodeList = rnsAnalysisService.getRcodeList(rcodeVO);
		} catch (Exception e) {
			logger.error("rnsAnalysisService.getRcodeList error = " + e);
		}
		
		
		// 서비스 목록 조회
		List<CodeVO> serviceCodeList = null;
		try {
			serviceCodeList = rnsAnalysisService.getServiceCodeList();
		} catch(Exception e) {
			logger.error("rnsAnalysisService.getServiceCodeList error = " + e);
		}
		
		// 발송자 목록 조회 
		List<String> senderList = null;
		try {
			senderList = rnsAnalysisService.getSenderList();
		} catch(Exception e) {
			logger.error("rnsAnalysisService.getSenderList error = " + e);
		} 
		 
		model.addAttribute("searchVO", searchVO); 			 	// 검색항목
		model.addAttribute("searchRcodeList", rcodeList); 			// 발송결과 코드 
		model.addAttribute("serviceCodeList", serviceCodeList); // 서비스목록
		model.addAttribute("senderList", senderList); 			// 발신자목록   
		  
		return "rns/ana/detailLogListP";
	}
	
	/**
	 * 상세로그내역 목록을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/detailLogList")
	public String goDetailLogList(@ModelAttribute RnsAnaDetailLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
 
		logger.debug("goDetailLogList SearchStdDt     = " + searchVO.getSearchStdDt());
		logger.debug("goDetailLogList SearchEndDt       = " + searchVO.getSearchEndDt());
		logger.debug("goDetailLogList searchSname      = " + searchVO.getSearchSname());
		logger.debug("goDetailLogList searchTid      = " + searchVO.getTid());
		logger.debug("goDetailLogList searchRcode      = " + searchVO.getSearchRcode());
		 
		searchVO.setSearchStdDt(searchVO.getSearchStdDt().replaceAll("\\.", ""));  
		searchVO.setSearchStdDt(searchVO.getSearchStdDt().substring(0,8));
		
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));  
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().substring(0,8));
		 
		logger.debug("goServiceList SearchStdDt      = " + searchVO.getSearchStdDt());
		logger.debug("goServiceList SearchEndDt      = " + searchVO.getSearchEndDt());
 
		if(searchVO.getSearchRcode() != null) {
			searchVO.setSearchRcode(searchVO.getSearchRcode().replaceAll(" ", ""));
		}
		 
		searchVO.setUilang((String) session.getAttribute("NEO_UILANG")); 

		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		List<RnsAnaDetailLogVO> detailLogDataList = null;
		try {			
			// 상세로그 내역 조회
			detailLogDataList = rnsAnalysisService.getDetailLogList(searchVO);
		} catch (Exception e) {
			logger.error("rnsAnalysisService.getDetailLogList error = " + e);
		}
		
		if (detailLogDataList != null && detailLogDataList.size() > 0) {
			totalCount = detailLogDataList.get(0).getTotalCount();
		}

		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);		
		
		model.addAttribute("searchVO", searchVO);					// 검색항목
		model.addAttribute("detailLogDataList", detailLogDataList);	// 서비스목록
		model.addAttribute("pageUtil", pageUtil); 					// 페이징
		
		return "rns/ana/detailLogList";
	}

	/**
	 * 상세로그내역 목록을 엑셀로 다운로드한다
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/detailLogExcelList")
	public void goDetailLogExcelList(@ModelAttribute RnsAnaDetailLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
 
		logger.debug("goDetailLogList SearchStdDt     = " + searchVO.getSearchStdDt());
		logger.debug("goDetailLogList SearchEndDt       = " + searchVO.getSearchEndDt());
		logger.debug("goDetailLogList searchSname      = " + searchVO.getSearchSname());
		logger.debug("goDetailLogList searchTid      = " + searchVO.getSearchTid());
		logger.debug("goDetailLogList searchRcode      = " + searchVO.getSearchRcode());
		 
		searchVO.setSearchStdDt(searchVO.getSearchStdDt().replaceAll("\\.", ""));  
		searchVO.setSearchStdDt(searchVO.getSearchStdDt().substring(0,8));
		
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));  
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().substring(0,8));
		 
		if(searchVO.getSearchRcode() != null) {
			searchVO.setSearchRcode(searchVO.getSearchRcode().replaceAll(" ", ""));
		}
		
		logger.debug("goServiceList SearchStdDt      = " + searchVO.getSearchStdDt());
		logger.debug("goServiceList SearchEndDt      = " + searchVO.getSearchEndDt());
 
		searchVO.setUilang((String) session.getAttribute("NEO_UILANG")); 

		// 페이지 설정
		int page = 1;
		int rows = 99999999;
		
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		
		List<RnsAnaDetailLogVO> detailLogDataList = null;
		try {			
			// 상세로그 내역 조회
			detailLogDataList = rnsAnalysisService.getDetailLogList(searchVO);
		} catch (Exception e) {
			logger.error("rnsAnalysisService.getDetailLogList error = " + e);
		}
		
		try {
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("AnalysisDetailLog");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			
			// 엑셀 헤더 생성 (SDATE| CDATE| MID | SUBID | TID | TNM | SUBJECT | RID | RNAME | RMAIL | SID | SNAME | SMAIL | RE | STATUS    )
			row = sheet.createRow(rowNum++);
			cell = row.createCell(0); cell.setCellStyle(headerStyle); cell.setCellValue("발송일시");
			cell = row.createCell(1); cell.setCellStyle(headerStyle); cell.setCellValue("예약일자");
			cell = row.createCell(2); cell.setCellStyle(headerStyle); cell.setCellValue("그룹ID");
			cell = row.createCell(3); cell.setCellStyle(headerStyle); cell.setCellValue("재발송ID");
			cell = row.createCell(4); cell.setCellStyle(headerStyle); cell.setCellValue("서비스ID");
			cell = row.createCell(5); cell.setCellStyle(headerStyle); cell.setCellValue("서비스명"); 			
			cell = row.createCell(6); cell.setCellStyle(headerStyle); cell.setCellValue("메일제목");
			cell = row.createCell(7); cell.setCellStyle(headerStyle); cell.setCellValue("수신자ID");
			cell = row.createCell(8); cell.setCellStyle(headerStyle); cell.setCellValue("수신자명");
			cell = row.createCell(9); cell.setCellStyle(headerStyle); cell.setCellValue("수신자이메일");
			cell = row.createCell(10); cell.setCellStyle(headerStyle); cell.setCellValue("발신자ID");
			cell = row.createCell(11); cell.setCellStyle(headerStyle); cell.setCellValue("발신자명");
			cell = row.createCell(12); cell.setCellStyle(headerStyle); cell.setCellValue("발신자이메일");
			cell = row.createCell(13); cell.setCellStyle(headerStyle); cell.setCellValue("재발송여부");
			cell = row.createCell(14); cell.setCellStyle(headerStyle); cell.setCellValue("발송결과");
			
			int recCnt =0; 
			
			// 엑셀 내용 추가
			for(RnsAnaDetailLogVO log:detailLogDataList) {
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0); cell.setCellValue( log.getSdate() );	// SDATE
				cell = row.createCell(1); cell.setCellValue( log.getCdate() );	// CDATE
				cell = row.createCell(2); cell.setCellValue( log.getMid() );	// MID
				cell = row.createCell(3); cell.setCellValue( log.getSubid() );	// SUBID
				cell = row.createCell(4); cell.setCellValue( log.getTid() );	// TID
				cell = row.createCell(5); cell.setCellValue( log.getTnm() );	// TNM
				
				cell = row.createCell(6); cell.setCellValue( log.getSubject() );// SUBJECT
				cell = row.createCell(7); cell.setCellValue( log.getRid() );	// RID
				cell = row.createCell(8); cell.setCellValue( log.getRname() );	// RNAME
				cell = row.createCell(9); cell.setCellValue( cryptoService.getDecrypt("RMAIL", log.getRmail()) ); // RMAIL
				//cell = row.createCell(9); cell.setCellValue( log.getRmail() );	// RMAIL
				cell = row.createCell(10); cell.setCellValue( log.getSid() );	// SID
				cell = row.createCell(11); cell.setCellValue( log.getSname() );	// SNAME
				cell = row.createCell(12); cell.setCellValue( cryptoService.getDecrypt("SMAIL", log.getSmail()) ); // SMAIL
				//cell = row.createCell(12); cell.setCellValue( log.getSmail() );	// SMAIL
				
				if ( log.getSubid() == 0 ) {
					cell = row.createCell(13); cell.setCellValue( "본발송");		// SUBID
				} else {
					cell = row.createCell(13); cell.setCellValue( Integer.toString(log.getSubid()) + "차");// SUBID
				}
				 
				cell = row.createCell(14); cell.setCellValue( log.getRcode() );	// STATUS
				recCnt += 1;
			}
			
			// 컨텐츠 타입과 파일명 지정
			String fileName = "AnalysisDetailLog_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			 
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 공통코드(C112) = 002:엑셀다운로드
			logVO.setContent("M2002006");
			logVO.setContentPath(request.getRequestURI());
			logVO.setExtrYn("Y");
			logVO.setMobilYn("N");
			logVO.setRecCnt(recCnt); 
			
			systemLogService.insertActionLog(request, session, logVO);
			
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		}		
	 
	}
	
	/**
	 * 메일 상세 내역을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailSendResultList")
	public String goMailSendResultList(@ModelAttribute RnsAnaMailSendResultVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
  
		logger.debug("goDetailLogList searchMid      = " + searchVO.getSearchMid()); 
		// 페이지 설정
		searchVO.setPage(1);
		searchVO.setRows(100); 
		
		List<RnsAnaMailSendResultVO> mailSendResultList = null;
		try {			
			// 메일별 발송  내역  조회
			mailSendResultList = rnsAnalysisService.getMailSendResultList(searchVO); 
		} catch (Exception e) {
			logger.error("rnsAnalysisService.getDetailLogList error = " + e);
		}
		model.addAttribute("mailSendResultList", mailSendResultList);	// 서비스목록 
		
		return "rns/ana/mailSendResultList";
	}
	
	private String getIntToZeroString(int target, int maxLength) {
		 
		String zeroString = Integer.toString(target);
		if (target < 10 ) { 
			zeroString = String.format("%02d", target);		 
		}
		return zeroString;
	}

}
