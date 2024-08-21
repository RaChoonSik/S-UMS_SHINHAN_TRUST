/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 시스템 관리 Controller==>시스템 로그 관리 Controller
 * 수정자 : 김준희
 * 작성일시 : 2021.08.09
 * 수정내역 : 패키지명수정 및 sys관련 분리에 의한 소스 변경 kr.co.sict.ums.sys.controller ==> kr.co.sict.ums.sys.log.controller
 *                사용자코드 관리 기능 외의 항목제거
 */
package kr.co.sict.ums.sys.log.controller;

import java.sql.Connection;
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
import kr.co.sict.ums.com.vo.CodeVO;
import kr.co.sict.ums.sys.acc.service.AccountService;
import kr.co.sict.ums.sys.acc.vo.OrganizationVO;
import kr.co.sict.ums.sys.log.service.SystemLogService;
import kr.co.sict.ums.sys.log.vo.LoginHistVO;
import kr.co.sict.ums.sys.log.vo.ActionLogVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.PageUtil;
import kr.co.sict.util.DBUtil;
import kr.co.sict.util.EncryptUtil;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Controller
@RequestMapping("/sys/log")
public class SystemLogController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private SystemLogService systemLogService;
	
	@Autowired
	private AccountService accountService;;
	
	@Autowired
	private PropertiesUtil properties; 
	
	/******************************************************** 사용자 로그인 관리 ********************************************************/
	/**
	 * 사용자 로그 이력 화면을 출력한다.
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/actionLogListP")
	public String goActionLogListP(@ModelAttribute ActionLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		//searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		searchVO.setPage(page);	
		
		// 코드그룹목록(코드성) 조회 -- 사용자상태 
		CodeVO contentType = new CodeVO();
		contentType.setUilang((String)session.getAttribute("NEO_UILANG"));
		contentType.setCdGrp("C112");  
		contentType.setUseYn("Y");
		
		List<CodeVO> contentTypeList = null;
		try {
			contentTypeList = codeService.getCodeList(contentType);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C112] error = " + e);
		}
		
		// 코드그룹목록(코드성) 조회 --  사용자 그룹 
		CodeVO dept = new CodeVO();
		dept.setUilang((String)session.getAttribute("NEO_UILANG"));
		dept.setStatus("000");
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(dept);
		} catch(Exception e) {
			logger.error("codeService.getDeptList[DEPT]error = " + e);
		}
		
		// 부서 조회
		OrganizationVO orgVO = new OrganizationVO();
		orgVO.setLvlVal("1");
		orgVO.setUseYn("Y");
		List<OrganizationVO> orgList = null;
		try {
			orgList = accountService.getOrgListView(orgVO);

		} catch (Exception e) {
			logger.error("accountService.getOrgListView error = " + e);
		}
		
		
		searchVO.setSearchLogStdDt(StringUtil.getDate(7));
		searchVO.setSearchLogEndDt(StringUtil.getDate(7));
		
		model.addAttribute("searchVO", searchVO);				// 검색 항목		
		model.addAttribute("contentTypeList", contentTypeList);	// 컨텐트 타입 항목 
		model.addAttribute("deptList", deptList);				// 그룹항목 
		model.addAttribute("orgList", orgList);				// 그룹항목
		
		return "sys/log/actionLogListP";
	}
	
	/**
	 * 사용자 로그 이력 목록을 조회한다.
	 * @param loginHistVO
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/actionLogList")
	public String goActionLogList(@ModelAttribute ActionLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goActionLogList searchLogStdDt = " + searchVO.getSearchLogStdDt());
		logger.debug("goActionLogList searchLogEndDt = " + searchVO.getSearchLogEndDt());
		logger.debug("goActionLogList searchLogEndDt = " + searchVO.getSearchStatusGb());
		logger.debug("goActionLogList searchContentType = " + searchVO.getSearchContentType());
		logger.debug("goActionLogList searchOrgCd = " + searchVO.getSearchOrgCd());
		logger.debug("goActionLogList searchDeptNo = " + searchVO.getSearchDeptNo());
		logger.debug("goActionLogList searchUserNm = " + searchVO.getSearchUserNm());
		 
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		//search_map.put("empNoList", ",'1001','1002','1003','1004'"));
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());	
		int totalCount = 0;
		
		String formatStdDt = searchVO.getSearchLogStdDt().replace(".", "");
		String formatEndDt = searchVO.getSearchLogEndDt().replace(".", "");
		
		if(formatStdDt.length() != 8) {
			searchVO.setSearchLogStdDt(StringUtil.getDate(7));
		} else {
			searchVO.setSearchLogStdDt(formatStdDt);
		}
		if(formatEndDt.length() != 8) {
			searchVO.setSearchLogEndDt(StringUtil.getDate(7));
		} else {
			searchVO.setSearchLogEndDt(formatEndDt);
		}
		 
		if (searchVO.getSearchOrgCd() != null && !searchVO.getSearchOrgCd().equals("")) {
			List<OrganizationVO> orgChildList = null;
			try {
				orgChildList = getOrgDescendantListView(searchVO.getOrgCd());
			} catch (Exception e) {
				logger.error("actionLogList getOrgDescendantListView error = " + e);
			}

			String orgCds = "";
			if (orgChildList.size() > 0) {
				for (int i = 0; orgChildList.size() > i; i++) {
					orgCds += orgChildList.get(i).getOrgCd() + ",";
				}
			}

			if (orgCds.length() > 2) {
				orgCds = orgCds.substring(0, orgCds.length() - 1);
			} else {
				orgCds = "";
			}
			logger.info("getActionLogList orgCds= " + orgCds);
			String arrOrgCds[] = null;
			arrOrgCds = orgCds.split(",");
			
			searchVO.setSearchOrgCd(orgCds);
			searchVO.setArrSearchOrgCds(arrOrgCds);
		}
		 
		 
		// 로그인 이력 목록 조회
		List<ActionLogVO> orgActionLog = null;
		List<ActionLogVO> actionLogList = new ArrayList<ActionLogVO>();
		try {
			orgActionLog = systemLogService.getActionLogList(searchVO);
		} catch(Exception e) {
			logger.error("systemService.getActionLogList error = " + e);
		}
		// 등록일시 포멧 수정
		if(orgActionLog != null) {
			for(ActionLogVO nActionLogVO:orgActionLog) {
				nActionLogVO.setLogDt(StringUtil.getFDate(nActionLogVO.getLogDt().substring(0, 14), Code.DT_FMT2));
				logger.debug("nActionLogVO.getLogDt  2= " + nActionLogVO.getLogDt());
				actionLogList.add(nActionLogVO);
			}
		}
		
		if(actionLogList != null && actionLogList.size() > 0) {
			totalCount = actionLogList.get(0).getTotalCount();
		}
		
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("actionLogList", actionLogList);	// 사용자활동이력 목록
		model.addAttribute("pageUtil", pageUtil);	// 페이징
		
		return "sys/log/actionLogList";
	}
	

	/**
	 * 사용자활동 목록 엑셀 다운로드
	 * @param sendLogVO
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/actionLogExcelList")	
	public void goActionLogExcelList(@ModelAttribute ActionLogVO searchVO, HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		logger.debug("goActionLogList searchLogStdDt = " + searchVO.getSearchLogStdDt());
		logger.debug("goActionLogList searchLogEndDt = " + searchVO.getSearchLogEndDt());
		logger.debug("goActionLogList searchLogEndDt = " + searchVO.getSearchStatusGb());
		logger.debug("goActionLogList searchContentType = " + searchVO.getSearchContentType());
		logger.debug("goActionLogList searchOrgCd = " + searchVO.getSearchOrgCd());
		logger.debug("goActionLogList searchDeptNo = " + searchVO.getSearchDeptNo());
		logger.debug("goActionLogList searchUserNm = " + searchVO.getSearchUserNm());
		
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 페이지 설정
		int page = 1;
		int rows = 99999999;
		
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		
		String formatStdDt = searchVO.getSearchLogStdDt().replace(".", "");
		String formatEndDt = searchVO.getSearchLogEndDt().replace(".", "");
		
		if(formatStdDt.length() != 8) {
			searchVO.setSearchLogStdDt(StringUtil.getDate(7));
		} else {
			searchVO.setSearchLogStdDt(formatStdDt);
		}
		if(formatEndDt.length() != 8) {
			searchVO.setSearchLogEndDt(StringUtil.getDate(7));
		} else {
			searchVO.setSearchLogEndDt(formatEndDt);
		}
		
		if (searchVO.getSearchOrgCd() != null && !searchVO.getSearchOrgCd().equals("")) {
			List<OrganizationVO> orgChildList = null;
			try {
				orgChildList = getOrgDescendantListView(searchVO.getOrgCd());
			} catch (Exception e) {
				logger.error("actionLogExcelList getOrgDescendantListView error = " + e);
			}

			String orgCds = "";
			if (orgChildList.size() > 0) {
				for (int i = 0; orgChildList.size() > i; i++) {
					orgCds += orgChildList.get(i).getOrgCd() + ",";
				}
			}

			if (orgCds.length() > 2) {
				orgCds = orgCds.substring(0, orgCds.length() - 1);
			} else {
				orgCds = "";
			}
			logger.info("actionLogExcelList orgCds= " + orgCds);
			String arrOrgCds[] = null;
			arrOrgCds = orgCds.split(",");
			
			searchVO.setSearchOrgCd(orgCds);
			searchVO.setArrSearchOrgCds(arrOrgCds);
		}
		 
		List<ActionLogVO> orgActionLog = null;
		List<ActionLogVO> actionLogList = new ArrayList<ActionLogVO>();
		try {
			orgActionLog = systemLogService.getActionLogList(searchVO);
		} catch(Exception e) {
			logger.error("systemService.getActionLogList error = " + e);
		}
		// 등록일시 포멧 수정
		if(orgActionLog != null) {
			for(ActionLogVO nActionLogVO:orgActionLog) {				
				nActionLogVO.setLogDt(StringUtil.getFDate(nActionLogVO.getLogDt().substring(0, 14), Code.DT_FMT2));
				logger.debug("nActionLogVO.getLogDt  2= " + nActionLogVO.getLogDt());
				actionLogList.add(nActionLogVO);
			}
		}
 
		try {
			// 엑셀 생성
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("Log List");
			Row row = null;
			Cell cell = null;
			int rowNum = 0;
			
			// 헤더 색상 지정
			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			
			// 엑셀 헤더 생성 ( LOGDT | SESSIONID | GROUP | USERID | USERNAME | STATUS | CONTENTTYPE | CONTENT | MESSAGE )
			row = sheet.createRow(rowNum++);
			cell = row.createCell(0); cell.setCellStyle(headerStyle); cell.setCellValue("LOGDT");
			cell = row.createCell(1); cell.setCellStyle(headerStyle); cell.setCellValue("SESSIONID");
			cell = row.createCell(2); cell.setCellStyle(headerStyle); cell.setCellValue("GROUP");
			cell = row.createCell(3); cell.setCellStyle(headerStyle); cell.setCellValue("USERID");
			cell = row.createCell(4); cell.setCellStyle(headerStyle); cell.setCellValue("USERNAME");
			cell = row.createCell(5); cell.setCellStyle(headerStyle); cell.setCellValue("STATUS");
			cell = row.createCell(6); cell.setCellStyle(headerStyle); cell.setCellValue("CONTENTTYPE");
			cell = row.createCell(7); cell.setCellStyle(headerStyle); cell.setCellValue("CONTENT");
			cell = row.createCell(8); cell.setCellStyle(headerStyle); cell.setCellValue("MESSAGE");
			
			int recCnt =0; 
			
			// 엑셀 내용 추가
			for(ActionLogVO log:actionLogList) {
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0); cell.setCellValue( log.getLogDt() );			// LOGDT
				cell = row.createCell(1); cell.setCellValue( log.getSessionId() );		// SESSIONID
				cell = row.createCell(2); cell.setCellValue( log.getDeptNm() );			// GROUP
				cell = row.createCell(3); cell.setCellValue( log.getUserId() );			// USERID
				cell = row.createCell(4); cell.setCellValue( log.getUserNm() );			// USERNAME
				cell = row.createCell(5); cell.setCellValue( log.getStatusGb() );		// STATUS
				cell = row.createCell(6); cell.setCellValue( log.getContentTypeNm() );	// CONTENTTYPE
				cell = row.createCell(7); cell.setCellValue( log.getContent() );		// CONTENT
				cell = row.createCell(8); cell.setCellValue( log.getMessage() );		// MESSAGE
				recCnt += 1;
			}
			
			// 컨텐츠 타입과 파일명 지정
			String fileName = "UserActionLog_" + StringUtil.getDate(Code.TM_YMDHMS) + ".xlsx";
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			// 엑셀 파일 다운로드
			wb.write(response.getOutputStream());
			wb.close();
			
			String requestUri = request.getRequestURI();
			String menuId = (String)request.getParameter("menuId");
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContentType("002"); // 공통코드(C112) = 002:엑셀다운로드
			logVO.setContent(menuId);
			logVO.setContentPath(requestUri);
			logVO.setExtrYn("Y");
			logVO.setMobilYn("N");
			logVO.setRecCnt(recCnt); 
			
			systemLogService.insertActionLog(request, session, logVO);
			
		} catch(Exception e) {
			logger.error("Excel File Download Error = " + e);
		} 
	}
	
	/** 
	 * 사용자 조회시 선택한 소속의  하위 부서도 조회하기 위한 하위 소속 조회 재귀 함수--  VIEW를 사용함  
	 * 
	 * @param orgVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */	
	private List<OrganizationVO> getOrgChildListView(String orgCd) {

		List<OrganizationVO> orgList = new ArrayList<OrganizationVO>();
		List<OrganizationVO> orgChildList = null;
		//List<OrganizationVO> orgDescendantList = null;
		try {

			orgChildList = accountService.getOrgChildListView(orgCd);
			if (orgChildList.size() > 0) {
				for (int i = 0; i < orgChildList.size(); i++) {
					orgList.add(orgChildList.get(i));
					getOrgChildListView(orgChildList.get(i).getOrgCd());
				}
			}
		} catch (Exception e) {
			logger.error("accountService.getOrgChildListView error = " + e);
		}

		return orgChildList;
	}
 
	/** 
	 * 사용자 조회시 선택한 소속의  하위 부서도 조회하기 위한  함수--  VIEW를 사용함 
	 * 
	 * @param orgVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	private List<OrganizationVO> getOrgDescendantListView(String upOrgCd) {

		List<OrganizationVO> orgList = new ArrayList<OrganizationVO>();
		List<OrganizationVO> orgDescendantList = null;
		List<OrganizationVO> orgChildList = null;
		OrganizationVO orgVO = new OrganizationVO();

		try {
			// 첫번째것 넣어줌
			orgVO = accountService.getOrgInfoView(upOrgCd);
			if (orgVO != null ) 
			{
				orgList.add(orgVO);
			}

			orgChildList = accountService.getOrgChildListView(upOrgCd);
			if (orgChildList.size() > 0) {
				for (int i = 0; i < orgChildList.size(); i++) {
					orgList.add(orgChildList.get(i));
					orgDescendantList = getOrgChildListView(orgChildList.get(i).getOrgCd());
					if (orgDescendantList.size() > 0) {
						for (int j = 0; j < orgDescendantList.size(); j++) {
							orgList.add(orgDescendantList.get(j));
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("accountService.getOrgDescendantListView error = " + e);
		}

		return orgList;
	}	
		
}
