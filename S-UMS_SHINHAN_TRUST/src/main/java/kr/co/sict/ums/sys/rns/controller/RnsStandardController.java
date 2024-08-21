/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.08
 * 설명 : 도메인 정보 관리
 */
package kr.co.sict.ums.sys.rns.controller;

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

import kr.co.sict.ums.sys.rns.service.RnsStandardService;
import kr.co.sict.ums.sys.rns.vo.DomainInfoVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.PageUtil;
import kr.co.sict.util.DBUtil;
import kr.co.sict.util.EncryptUtil;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Controller
@RequestMapping("/sys/rns")
public class RnsStandardController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private RnsStandardService rnsStandardService;
	
	@Autowired
	private PropertiesUtil properties; 
	
	/******************************************************** 도메인 관리 ********************************************************/
	/**
	 * 도메인 목록 조회 화면을 출력한다.
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/domainListP")
	public String goDomainListP(@ModelAttribute DomainInfoVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		//searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		searchVO.setPage(page);	 
		 
		
		model.addAttribute("searchVO", searchVO);				// 검색 항목	 
		
		return "sys/rns/domainListP";
	}
	
	/**
	 * 도메인 목록을 출력한다.
	 * @param domainInfoVO
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/domainList")
	public String goDomainList(@ModelAttribute DomainInfoVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goDomainList searchDomainName = " + searchVO.getSearchDomainName()); 
		  
		//search_map.put("empNoList", ",'1001','1002','1003','1004'"));
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0; 
		  
		// 로그인 이력 목록 조회 
		List<DomainInfoVO> orgDomainList = new ArrayList<DomainInfoVO>();
		List<DomainInfoVO> domainList = new ArrayList<DomainInfoVO>();
		try {
			orgDomainList = rnsStandardService.getDomainList(searchVO);
		} catch(Exception e) {
			logger.error("rnsStandardService.getDomainList error = " + e);
		}
		// 등록일시 포멧 수정
		if(orgDomainList != null) {
			for(DomainInfoVO nDomainVO:orgDomainList) {				
				nDomainVO.setRegDt(StringUtil.getFDate(nDomainVO.getRegDt().substring(0, 14), Code.DT_FMT2)); 
				domainList.add(nDomainVO);
			}
		}
		
		if(domainList != null && domainList.size() > 0) {
			totalCount = domainList.get(0).getTotalCount();
		}
		
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("domainList", domainList);	// 사용자활동이력 목록
		model.addAttribute("pageUtil", pageUtil);	// 페이징
		model.addAttribute("searchVO", searchVO);
		
		return "sys/rns/domainList";
	}
	 
	@RequestMapping(value="/domainAddP")
	public String goDomainAddP(@ModelAttribute DomainInfoVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		model.addAttribute("searchVO", searchVO);// 검색 항목	 
		
		return "sys/rns/domainAddP";
	}
	
	@RequestMapping(value="/domainUpdateP")
	public String goDomainUpdateP(@ModelAttribute DomainInfoVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goDomainUpdateP getDomainId = " + searchVO.getDomainId());
		 
		// 로그인 이력 목록 조회 
		DomainInfoVO domainInfo = new DomainInfoVO();
		try {
			domainInfo = rnsStandardService.getDomainInfo(searchVO);
		} catch(Exception e) {
			logger.error("rnsStandardService.getDomainInfo error = " + e);
		}
		// 등록일시 포멧 수정
		if(domainInfo != null) {
			domainInfo.setRegDt(StringUtil.getFDate(domainInfo.getRegDt().substring(0, 14), Code.DT_FMT2)); 	
		}
						
		model.addAttribute("searchVO", searchVO);// 검색 항목
		model.addAttribute("domainInfo", domainInfo);	
		
		return "sys/rns/domainUpdateP";
	}
	
	/**
	 * 도메인 신규 정보를 등록한다.
	 * 
	 * @param domainInfoVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/domainAdd")
	public ModelAndView insertDomainInfo(@ModelAttribute DomainInfoVO domainInfoVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateDomainInfo domainName= " + domainInfoVO.getDomainName()); 
 
		domainInfoVO.setRegId((String) session.getAttribute("NEO_USER_ID"));
		domainInfoVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
				
		int result = 0;
		try {
			result = rnsStandardService.insertDomainInfo(domainInfoVO);
		} catch (Exception e) {
			logger.error("rnsStandardService.insertDinsertDomainInfoeptInfo error = " + e);
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}

	/**
	 * 도메인 정보를 수정한다.
	 * 
	 * @param domainInfoVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session @return/
	 */
	@RequestMapping(value = "/domainUpdate")
	public ModelAndView updateDomainInfo(@ModelAttribute DomainInfoVO domainInfoVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateDomainInfo domainId= " + domainInfoVO.getDomainId()); 
		logger.debug("updateDomainInfo domainName= " + domainInfoVO.getDomainName()); 
  
		domainInfoVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		domainInfoVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		int result = 0;
		try {
			result = rnsStandardService.updateDomainInfo(domainInfoVO);
		} catch (Exception e) {
			logger.error("rnsStandardService.updateDomainInfo error = " + e);
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}

	/**
	 * 도메인 정보를 삭제한다
	 * 
	 * @param domainInfoVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/domainDelete")
	public ModelAndView deleteDeptInfo(@ModelAttribute DomainInfoVO domainInfoVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("deleteDeptInfo getDomainIds      = " + domainInfoVO.getDomainIds());
 
		String[] domainIds= domainInfoVO.getDomainIds().split(",");
		int result = 0;
		
		if (domainIds.length > 0) {
			int[] arrDomainId = new int[domainIds.length];
		 

			for (int i = 0; i < domainIds.length; i++) {
				if( !"".equals(domainIds[i])){
					arrDomainId[i] = Integer.parseInt(domainIds[i]);
				}
			}

			domainInfoVO.setArrDomainId(arrDomainId);

			try {
				result = rnsStandardService.deleteDomainInfo(domainInfoVO); 
			} catch (Exception e) {
				result = -1;
				logger.error("rnsStandardService.deleteDomainInfo error = " + e);
			}
		} else {
			result = -1;
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
}
