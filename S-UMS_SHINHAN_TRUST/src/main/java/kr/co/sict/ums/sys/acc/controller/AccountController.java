/**
8 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 시스템 관리 Controller==>사용자 코드 관리 Controller
 * 수정자 : 김준희
 * 작성일시 : 2021.08.09
 * 수정내역 : 패키지명수정 및 sys관련 분리에 의한 소스 변경 kr.co.sict.ums.sys.controller ==> kr.co.sict.ums.sys.acc.controller
 *                사용자코드 관리 기능 외의 항목제거
 */
package kr.co.sict.ums.sys.acc.controller;

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

import kr.co.sict.ums.com.service.CryptoService;
import kr.co.sict.ums.com.service.CodeService;
import kr.co.sict.ums.com.vo.CodeVO;

import kr.co.sict.ums.sys.acc.service.AccountService;

import kr.co.sict.ums.sys.acc.vo.DeptVO;
import kr.co.sict.ums.sys.acc.vo.ServiceVO;
import kr.co.sict.ums.sys.acc.vo.UserProgVO;
import kr.co.sict.ums.sys.acc.vo.UserVO;
import kr.co.sict.ums.sys.acc.vo.UserOrgVO;
import kr.co.sict.ums.sys.acc.vo.OrganizationVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.PageUtil;
import kr.co.sict.util.EncryptUtil;
import kr.co.sict.util.EncryptAccUtil;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Controller
@RequestMapping("/sys/acc")
public class AccountController {
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private CodeService codeService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private CryptoService cryptoService;
	
	
	@Autowired
	private PropertiesUtil properties;

	/****************************** 사용자그룹관리 ******************************/
	/**
	 * 사용자그룹 목록 화면을 출력한다.
	 * 
	 * @param deptVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deptListP")
	public String goDeptListP(@ModelAttribute DeptVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goDeptListP searchUserCodeGroupNm = " + searchVO.getSearchDeptNm());
		logger.debug("goDeptListP searchUserCodeGroup = " + searchVO.getSearchStatus());

		searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		searchVO.setPage(page);

		// 사용자그룹 상태 코드 목록을 조회한다.
		CodeVO statusCodeVO = new CodeVO();
		statusCodeVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		statusCodeVO.setCdGrp("C009");
		statusCodeVO.setUseYn("Y");

		List<CodeVO> deptStatusList = null;
		try {
			deptStatusList = codeService.getCodeList(statusCodeVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error[C009] = " + e);
		}
		
		model.addAttribute("searchVO", searchVO); 				// 검색 항목
		model.addAttribute("deptStatusList", deptStatusList);	// 사용자그룹 상태 코드 목록

		return "sys/acc/deptListP";
	}

	/**
	 * 사용자그룹 목록을 조회한다
	 * 
	 * @param deptVO
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deptList")
	public String goDeptList(@ModelAttribute DeptVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goDeptList searchDeptNm = " + searchVO.getSearchDeptNm());
		logger.debug("goDeptList searchStatus = " + searchVO.getSearchStatus());

		searchVO.setUilang((String) session.getAttribute("NEO_UILANG")); 

		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;

		// 사용자그룹 목록 조회
		List<DeptVO> orgDeptList = null;
		List<DeptVO> deptList = new ArrayList<DeptVO>();

		try {
			orgDeptList = accountService.getDeptList(searchVO);
		} catch (Exception e) {
			logger.error("accountService.getDeptList error = " + e);
		}
		// 등록일시 포맷 수정

		if (orgDeptList != null) {
			String formatRegDt = "";
			for (DeptVO lDeptVO : orgDeptList) {
				if (lDeptVO.getRegDt() != null && !"".equals(lDeptVO.getRegDt())) {
					formatRegDt = StringUtil.getFDate(lDeptVO.getRegDt(), Code.DT_FMT2);
					formatRegDt = formatRegDt.substring(0, 10);
					lDeptVO.setRegDt(formatRegDt);
				}
				deptList.add(lDeptVO);
			}
		}

		if (deptList != null && deptList.size() > 0) {
			totalCount = deptList.get(0).getTotalCount();
		}

		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);

		model.addAttribute("deptList", deptList); // 사용자그룹 목록
		model.addAttribute("pageUtil", pageUtil); // 페이징

		return "sys/acc/deptList";
	}

	/**
	 * 사용자그룹 등록 화면을 출력한다
	 * 
	 * @param userCodeGroupVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deptAddP")
	public String goDeptAddP(@ModelAttribute DeptVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		// 사용자그룹 상태코드 목록을 조회한다.
		CodeVO statusCodeVO = new CodeVO();
		statusCodeVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		statusCodeVO.setCdGrp("C009"); // 그룹상태
		statusCodeVO.setUseYn("Y");

		List<CodeVO> deptStatusList = null;
		
		try {
			deptStatusList = codeService.getCodeList(statusCodeVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error[C009] = " + e);
		}

		model.addAttribute("deptStatusList", deptStatusList); // 사용자그룹 상태 항목

		return "sys/acc/deptAddP";
	}

	/**
	 * 사용자그룹 수정 화면을 출력한다
	 * 
	 * @param deptVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deptUpdateP")
	public String goDeptUpdateP(@ModelAttribute DeptVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goDeptUpdateP getDeptNo = " + searchVO.getDeptNo());
 
		DeptVO deptInfo = null;
		try {
			deptInfo = accountService.getDeptInfo(searchVO);
		} catch (Exception e) {
			logger.error("accountService.getDeptInfo error = " + e);
		}

		// 그룹상태코드 목록을 조회한다.
		CodeVO statusCodeVO = new CodeVO();
		statusCodeVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		statusCodeVO.setCdGrp("C009");
		statusCodeVO.setUseYn("Y");

		List<CodeVO> deptStatusList = null;
		try {
			deptStatusList = codeService.getCodeList(statusCodeVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error[C009] = " + e);
		}

		model.addAttribute("searchVO", searchVO); 				// 검색항목
		model.addAttribute("deptInfo", deptInfo); 				// 사용자그룹 조회 결과
		model.addAttribute("deptStatusList", deptStatusList); 	// 사용자그룹 조회 결과

		return "sys/acc/deptUpdateP";
	}

	/**
	 * 사용자그룹을 등록한다
	 * 
	 * @param deptVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deptAdd")
	public ModelAndView insertDeptInfo(@ModelAttribute DeptVO deptVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("insertDeptInfo deptNm = " + deptVO.getDeptNm());
		logger.debug("insertDeptInfo status = " + deptVO.getStatus());
		logger.debug("insertDeptInfo deptDesc = " + deptVO.getDeptDesc());

		deptVO.setRegId((String) session.getAttribute("NEO_USER_ID"));
		deptVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		deptVO.setUseYn("Y");

		if (deptVO.getStatus() == null || "".equals(deptVO.getStatus())){
			deptVO.setStatus("000");
		}
		 
		int result = 0;
		try {
			result = accountService.insertDeptInfo(deptVO);
		} catch (Exception e) {
			logger.error("accountService.insertDeptInfo error = " + e);
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
	 * 사용자그룹을 수정한다
	 * 
	 * @param deptVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session @return/
	 */
	@RequestMapping(value = "/deptUpdate")
	public ModelAndView updateDeptInfo(@ModelAttribute DeptVO deptVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateDeptInfo deptNm = " + deptVO.getDeptNm());
		logger.debug("updateDeptInfo status = " + deptVO.getStatus());
		logger.debug("updateDeptInfo deptDesc = " + deptVO.getDeptDesc());

		deptVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		deptVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));

		int result = 0;
		try {
			result = accountService.updateDeptInfo(deptVO);
		} catch (Exception e) {
			logger.error("accountService.updateDeptInfo error = " + e);
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
	 * 사용자그룹을 삭제한다
	 * 
	 * @param deptVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deptDelete")
	public ModelAndView deleteDeptInfo(@ModelAttribute DeptVO deptVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("deleteDeptInfo getDeptNos      = " + deptVO.getDeptNos());

		deptVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		deptVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		deptVO.setUseYn("N");

		String[] deptNo = deptVO.getDeptNos().split(",");

		int result = 0;

		for (int i = 0; i < deptNo.length; i++) {
			deptVO.setDeptNo(Integer.parseInt(deptNo[i]));
			try {
				result = accountService.deleteDeptInfo(deptVO);
			} catch (Exception e) {
				logger.error("accountService.deleteDeptInfo error = " + e);
			}
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

	/****************************** 조직관리******************************/
	/**
	 * 조직관리 화면을 출력한다
	 * 
	 * @param deptVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/orgListP")
	public String goOrgListP(@ModelAttribute OrganizationVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
 
		searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		logger.debug("goOrgListP getUilang = " + searchVO.getUilang());

		// 상위부서  목록을 조회한다.
		OrganizationVO orgVO = new OrganizationVO();
		orgVO.setLvlVal("1");
		orgVO.setUseYn("Y");

		List<OrganizationVO> orgList = null;
		try {
			orgList = accountService.getOrgList(orgVO);
		} catch (Exception e) {
			logger.error("accountService.getOrgList error[C009] = " + e);
		}

		// model에 코드 목록 추가
		model.addAttribute("searchVO", searchVO); 	// 검색 항목
		model.addAttribute("orgList", orgList); 	// 조직 목록

		return "sys/acc/orgListP";
	}
	
	/**
	 * 조직 목록을 조회한다
	 * 
	 * @param orgVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/orgList")
	public ModelAndView goOrgList(@ModelAttribute OrganizationVO searchVO, Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		logger.debug("goOrgListP getSearchOrgCd = " + searchVO.getSearchOrgCd());
		logger.debug("goOrgListP getSearchOrgNm = " + searchVO.getSearchOrgNm());
		logger.debug("goOrgListP getSearchUpOrgCd = " + searchVO.getSearchUpOrgCd());
 
		String orgCds = "";
		String searchUpOrgCd ="";
		
		if (searchVO.getSearchUpOrgCd() != null) {
			searchUpOrgCd = searchVO.getSearchUpOrgCd();
			searchUpOrgCd = searchUpOrgCd.trim();
		}
		
		List<OrganizationVO> treeOrgList =  new ArrayList<OrganizationVO>();
		try {
			treeOrgList = accountService.getOrgList(searchVO);
		} catch (Exception e) {
			logger.error("accountService.getOrgListTree error = " + e);
		}
		
		if (treeOrgList != null) {
			int treeLvl = 1;  
			
			for (OrganizationVO tOrgVO : treeOrgList) {
				if (tOrgVO.getLvlVal() != null && !"".equals(tOrgVO.getLvlVal())) {
					treeLvl = Integer.parseInt(tOrgVO.getLvlVal());
					 
					if(treeLvl == 1) {
						if(!"".equals(searchUpOrgCd)) {
							if(tOrgVO.getOrgCd().equals(searchUpOrgCd)) {
								orgCds += tOrgVO.getOrgCd() + ",";
							}
						} else {
							orgCds += tOrgVO.getOrgCd() + ",";
						}
					} else if(treeLvl == 2 ) {
						if(!"".equals(searchUpOrgCd)) {
							if(tOrgVO.getUpOrgCd().equals(searchUpOrgCd)) {
								orgCds += tOrgVO.getOrgCd() + ",";
								orgCds += tOrgVO.getUpOrgCd() + ",";
							}
						} else {
							orgCds += tOrgVO.getOrgCd() + ",";
							orgCds += tOrgVO.getUpOrgCd() + ",";
						}
					} else {
						orgCds += getUpOrgList(tOrgVO.getOrgCd(), tOrgVO.getUpOrgCd(),treeLvl, searchUpOrgCd); 
					}
				}
			} 
			
			if (orgCds.length() > 2 ) {
				orgCds = orgCds.substring(0, orgCds.length() - 1);
				String[] arrOrgCds = null;
				arrOrgCds = orgCds.split(",");
				
				searchVO.setSearchOrgCd(orgCds);
				searchVO.setArrSearchOrgCds(arrOrgCds); 
			} else {
				searchVO.setSearchOrgCd("");
			}
			
		} else {
			searchVO.setSearchOrgCd("");
		} 

		List<OrganizationVO> orgList = new ArrayList<OrganizationVO>();
		List<OrganizationVO> orgOrgList = null;
		
		if(orgCds.length() > 2) {
			try {
				orgOrgList = accountService.getOrgInfoList(searchVO);
			} catch (Exception e) {
				logger.error("accountService.getOrgInfoList error = " + e);
			}

			// 등록일시 포맷 수정
			if (orgOrgList != null) {
				String formatRegDt = "";
				for (OrganizationVO lOrgVO : orgOrgList) {
					if (lOrgVO.getRegDt() != null && !"".equals(lOrgVO.getRegDt())) {
						formatRegDt = StringUtil.getFDate(lOrgVO.getRegDt(), Code.DT_FMT2);
						formatRegDt = formatRegDt.substring(0, 10);
						lOrgVO.setRegDt(formatRegDt);
					}
					orgList.add(lOrgVO);
				}
			}
		}

		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("orgList", orgList); 
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}
	
	/**
	 * 조직 등록 화면을 출력한다
	 * 
	 * @param organizationVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/orgAddP")
	public String goOrgAddP(@ModelAttribute OrganizationVO searchVO, Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
	  
		// 부서 조회
		OrganizationVO orgVO = new OrganizationVO();
		orgVO.setLvlVal("1");
		orgVO.setUseYn("Y");
		List<OrganizationVO> orgList = null;
		try {
			orgList = accountService.getOrgList(orgVO); 
		} catch (Exception e) {
			logger.error("accountService.getOrgList error = " + e);
		} 
  
		model.addAttribute("searchVO", searchVO); 	// 검색항목 
		model.addAttribute("orgList", orgList); // 부서(최상위)항목
		 
		return "sys/acc/orgAddP";
	}
 
	/**
	 * 조직 수정 화면을 출력한다
	 * 
	 * @param organizationVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/orgUpdateP")
	public String goOrgUpdateP(@ModelAttribute OrganizationVO searchVO, Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
 
		logger.debug("goOrgUpdateP getOrgCd =   " + searchVO.getOrgCd());
		logger.debug("goOrgUpdateP getUpOrgCd = " + searchVO.getUpOrgCd());
		logger.debug("goOrgUpdateP getOrgNm =   " + searchVO.getOrgNm());
 
		OrganizationVO orgInfo = null;
		try {
			orgInfo = accountService.getOrgInfo(searchVO.getOrgCd());
		} catch (Exception e) {
			logger.error("accountService.goOrgUpdateP error = " + e);
		}
		
		// 부서 조회
		OrganizationVO orgVO = new OrganizationVO();
		orgVO.setLvlVal("1");
		orgVO.setUseYn("Y");
		
		List<OrganizationVO> orgList = null;
		try {
			orgList = accountService.getOrgList(orgVO);

		} catch (Exception e) {
			logger.error("accountService.getOrgList error = " + e);
		}

		model.addAttribute("searchVO", searchVO); 	// 검색항목
		model.addAttribute("orgInfo", orgInfo); 	// 부서 조회 결과
		model.addAttribute("orgList", orgList);     // 부서(최상위)항목
		
		return "sys/acc/orgUpdateP";
	}
	

	/**
	 * 조직을 등록한다
	 * 
	 * @param organizationVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/orgAdd")
	public ModelAndView insertOrgInfo(@ModelAttribute OrganizationVO organizationVO, Model model, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {

		logger.debug("insertOrgInfo orgCd = " + organizationVO.getOrgCd()); 
		logger.debug("insertOrgInfo orgNm = " + organizationVO.getOrgNm());
		logger.debug("insertOrgInfo orgUpCd = " + organizationVO.getUpOrgCd()); 
		logger.debug("insertOrgInfo orgUpCd = " + organizationVO.getOrgKorNm());

		organizationVO.setRegId((String) session.getAttribute("NEO_USER_ID"));
		organizationVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		organizationVO.setUseYn("Y");
		
		OrganizationVO upOrgInfo = null;

		if (organizationVO.getUpOrgCd() == null || "".equals(organizationVO.getUpOrgCd())){
			organizationVO.setUpOrgCd("0");
		}
		try {
			upOrgInfo = accountService.getOrgInfo(organizationVO.getUpOrgCd());
		} catch (Exception e) {
			logger.error("accountService.insertOrgInfo error = " + e);
		}
		
		String lvl = "1";
		if (upOrgInfo != null) {
			lvl = upOrgInfo.getLvlVal() ;
			
			if(lvl != null && !"".equals(lvl)) {
				lvl = Integer.toString( Integer.parseInt(lvl) + 1); 
			} 
		}
		
		//레벨에 따른 Prefix 
		// 1단계 H 2단계 D 3단계 T 4단계 P 모르겠는건 E 
		String newOrgCd ="";
		String curMaxCd ="";
		String prefix = "E";
		int newOrg = 1; 
		int curMax = 1;
		
		switch(lvl) 
		{
			case "1" : prefix = "H"; break;
			case "2" : prefix = "D"; break;
			case "3" : prefix = "T"; break;
			case "4" : prefix = "P"; break;
			default :  prefix = "E"; break; 
		}
		try {
			curMaxCd = accountService.getOrgCodeMax(lvl);
		} catch (Exception e) {
			logger.error("accountService.insertOrgInfo error = " + e);
		}
		
		if (curMaxCd != null && !"".equals(curMaxCd) ) {
			curMax = Integer.parseInt(curMaxCd);
			newOrg = curMax +1;
			newOrgCd =  prefix +  String.format("%04d", newOrg);
		} else {
			newOrg = 1;
			newOrgCd =  prefix +  String.format("%04d", newOrg);
		}
		 
		organizationVO.setLvlVal(lvl);
		organizationVO.setOrgCd(newOrgCd);
		organizationVO.setOrgKorNm(organizationVO.getOrgNm());
		
		int result = 0;
		try {
			result = accountService.insertOragnizationInfo(organizationVO);
		} catch (Exception e) {
			logger.error("accountService.insertOrgInfo error = " + e);
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
	 * 조직을 수정한다
	 * 
	 * @param organizationVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session @return/
	 */
	@RequestMapping(value = "/orgUpdate")
	public ModelAndView updateOrgInfo(@ModelAttribute OrganizationVO organizationVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
 
		logger.debug("updateOrgInfo orgCd = " + organizationVO.getOrgCd()); 
		logger.debug("updateOrgInfo orgNm = " + organizationVO.getOrgEngNm());
		logger.debug("updateOrgInfo orgUpCd = " + organizationVO.getUpOrgCd());  

		organizationVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		organizationVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));

		organizationVO.setOrgKorNm(organizationVO.getOrgEngNm());
		
		OrganizationVO upOrgInfo = null;
		try {
			upOrgInfo = accountService.getOrgInfo(organizationVO.getUpOrgCd());
		} catch (Exception e) {
			logger.error("accountService.updateOrgInfo error = " + e);
		}
		
		String lvl = "1";
		if (upOrgInfo != null) {
			lvl = upOrgInfo.getLvlVal() ;
			
			if(lvl != null && !"".equals(lvl)) {
				lvl = Integer.toString( Integer.parseInt(lvl) + 1); 
			} 
		}
		
		organizationVO.setLvlVal(lvl);
		organizationVO.setUseYn("Y");
		 
		int result = 0;
		try {
			result = accountService.updateOrganizationInfo(organizationVO);
		} catch (Exception e) {
			logger.error("accountService.updateOrgInfo error = " + e);
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
	 * 조직을 삭제한다
	 * 
	 * @param organizationVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/orgDelete")
	public ModelAndView deleteOrgInfo(@ModelAttribute OrganizationVO organizationVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("deleteOrgInfo getOrgNos      = " + organizationVO.getOrgCds());

		organizationVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		organizationVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		organizationVO.setUseYn("N");

		String[] orgCd = organizationVO.getOrgCds().split(",");
 
		organizationVO.setSearchOrgCd(organizationVO.getOrgCds());
		organizationVO.setArrSearchOrgCds(orgCd);

		int userCount = 0;
		try {
			userCount = accountService.getOrgUserCount(organizationVO);
			logger.debug("사용자수 : " + userCount);
		} catch (Exception e) {
			logger.error("accountService.deleteOrganizationInfo error = " + e);
		}
		
		int result = 1;
		
		if ( userCount == 0 ) { 
			for (int i = 0; i < orgCd.length; i++) {
				organizationVO.setOrgCd( orgCd[i]); 
				try { 
					result = accountService.deleteOrganizationInfo(organizationVO); 
				} catch (Exception e) {
					logger.error("accountService.deleteOrganizationInfo error = " + e); 
				} 
			} 
		} else { 
			result = userCount * -1; 
		}
		 
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) { 
			map.put("result", "Success");
		} else { 
			if (userCount > 0 ) {
				map.put("result", "Fail");
				map.put("errMsg", "해당 부서에 소속된 사용자가 있습니다 [" + Integer.toString(userCount) + "(명)]" );
			} else {
				map.put("result", "Fail");
				map.put("errMsg", "삭제에 실패했습니다");
			}			
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}
	
	/**
	 * 조직의 상위 부서를 조회한다
	 * 
	 * @param organizationVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	private String getUpOrgList(String orgCd, String upOrgCd, int orgCdLvl, String searchUpOrgCd) {
		
		String grandCd ="";
		String parentCd =""; 
		String returnOrgCds ="";
		
		parentCd = upOrgCd;
		returnOrgCds = orgCd+ "," +  parentCd + ",";
		
		for (int i = orgCdLvl; i <= orgCdLvl ; i ++) {
			try {
				grandCd = accountService.getOrgListTreeParent(parentCd);
				returnOrgCds += grandCd + ",";  
				parentCd = grandCd;
			} catch (Exception e){
				logger.error("accountService.getOrgListTreeParent error = " + e);
			}
		}
		if (!"".equals(searchUpOrgCd) && !grandCd.equals(searchUpOrgCd)){
			returnOrgCds ="";
		}
		
		return returnOrgCds;
	}
 
	 
	/****************************** 팝업용 조직 조회  ******************************/
	/** 
	 * 팝업용 : 조직 조회   
	 * 
	 * @param orgVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getOrgList")
	public ModelAndView getOrgList(@ModelAttribute OrganizationVO orgVO, Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		logger.debug("getOrgList getUpOrgCd = " + orgVO.getUpOrgCd());

		// 조직 목록 
		if (orgVO.getUpOrgCd() == null || orgVO.getUpOrgCd().equals("")) {
			orgVO.setUpOrgCd("");
			orgVO.setLvlVal("1");
		}

		List<OrganizationVO> orgList = null;
		try {
			orgList = accountService.getOrgList(orgVO);
		} catch (Exception e) {
			logger.error("accountService.getOrgList error = " + e);
		}

		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("orgList", orgList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}

	/****************************** 팝업용 조직 조회 : VIEW 사용 ******************************/
	/** 
	 * 팝업용 : 조직 조회  --  VIEW를 사용함  
	 * 
	 * @param orgVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getOrgListView")
	public ModelAndView getOrgListView(@ModelAttribute OrganizationVO orgVO, Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		logger.debug("getOrgListView getUpOrgCd = " + orgVO.getUpOrgCd());

		// 조직 목록 
		if (orgVO.getUpOrgCd() == null || orgVO.getUpOrgCd().equals("")) {
			orgVO.setUpOrgCd("");
			orgVO.setLvlVal("1");
		}

		List<OrganizationVO> orgList = null;
		try {
			orgList = accountService.getOrgListView(orgVO);
		} catch (Exception e) {
			logger.error("accountService.getOrgList error = " + e);
		}

		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("orgList", orgList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	} 
 
	/****************************** 사용자관리 ******************************/
	/**
	 * 사용자 관리화면을 조회한다
	 * 
	 * @param userVO
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * 
	 */
	@RequestMapping(value = "/userListP")
	public String gotUserList(@ModelAttribute UserVO searchVO, Model model, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		logger.debug("goUserListP getUilang = " + searchVO.getUilang());

		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		searchVO.setPage(page);

		// 코드그룹목록(코드성) 조회 -- 사용자상태
		CodeVO status = new CodeVO();
		status.setUilang(searchVO.getUilang());
		status.setCdGrp("C010");
		status.setUseYn("Y");

		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(status);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[C010] error = " + e);
		}

		// 코드그룹목록(코드성) 조회 -- 사용자 그룹
		CodeVO dept = new CodeVO();
		dept.setUilang(searchVO.getUilang());
		dept.setStatus("000");
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(dept);
		} catch (Exception e) {
			logger.error("codeService.getDeptList[DEPT]error = " + e);
		}

		// 코드그룹목록(코드성) 조회 -- 서비스리스트
		ServiceVO orgService = new ServiceVO();
		List<ServiceVO> orgServiceList = null;
		try {
			orgServiceList = accountService.getServiceList(orgService);
		} catch (Exception e) {
			logger.error("accountService.getServiceList error = " + e);
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

		List<CodeVO> serviceList = new ArrayList<CodeVO>();

		for (ServiceVO lServiceVO : orgServiceList) {
			CodeVO service = new CodeVO();
			service.setCd(Integer.toString(lServiceVO.getServiceGb()));
			service.setCdNm(lServiceVO.getServiceNm());
			serviceList.add(service);
		}

		model.addAttribute("searchVO", searchVO); // 검색 항목
		model.addAttribute("statusList", statusList); // 상태항목
		model.addAttribute("deptList", deptList); // 그룹항목
		model.addAttribute("orgList", orgList); // 부서항목
		model.addAttribute("serviceList", serviceList); // 서비스항목

		return "sys/acc/userListP";
	}

	/**
	 * 사용자 목록을 조회한다.
	 * 
	 * @param deptVO
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/userList")
	public String goUserList(@ModelAttribute UserVO searchVO, Model model, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		logger.debug("goUserList getSearchUserId = " + searchVO.getSearchUserId());
		logger.debug("goUserList getSearchUserNm = " + searchVO.getSearchUserNm());
		logger.debug("goUserList getSearchStatus = " + searchVO.getSearchStatus());
		logger.debug("goUserList getSearchDeptNo = " + searchVO.getSearchDeptNo());
		logger.debug("goUserList getSearchOrgCd = " + searchVO.getSearchOrgCd());
		 
		searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		logger.debug("goUserList getUilang= " + searchVO.getUilang());

		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;

		if (searchVO.getOrgCd() != null && !searchVO.getOrgCd().equals("")) {
			List<OrganizationVO> orgChildList = null;
			try {
				orgChildList = getOrgDescendantListView(searchVO.getOrgCd());
			} catch (Exception e) {
				logger.error("goUserList getOrgDescendantListView error = " + e);
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
			logger.info("goUserList orgCds= " + orgCds);
			String arrOrgCds[] = null;
			arrOrgCds = orgCds.split(",");
			
			searchVO.setSearchOrgCd(orgCds);
			searchVO.setArrSearchOrgCds(arrOrgCds);
		}
		
		logger.debug("goUserList SearchOrgCd = " + searchVO.getSearchOrgCd());

		// 사용자 조회
		List<UserVO> orgUserList = null;
		List<UserVO> userList = new ArrayList<UserVO>();

		try {
			orgUserList = accountService.getUserList(searchVO);
		} catch (Exception e) {
			logger.error("accountService.goUserList error = " + e);
		}

		// 코드그룹목록(코드성) 조회 -- 서비스리스트
		UserVO userVO = new UserVO();
		List<UserVO> userServiceList = null;
		try {
			userServiceList = accountService.getUserServiceList(userVO);
		} catch (Exception e) {
			logger.error("accountService.getUserServiceList error = " + e);
		}

		// 등록일시 포맷 수정
		if (orgUserList != null) {
			String formatRegDt = "";
			for (UserVO lUserVO : orgUserList) {
				if (lUserVO.getRegDt() != null && !"".equals(lUserVO.getRegDt())) {
					formatRegDt = StringUtil.getFDate(lUserVO.getRegDt(), Code.DT_FMT2);
					formatRegDt = formatRegDt.substring(0, 10);
					lUserVO.setRegDt(formatRegDt);
				}

				String userService = "";
				for (UserVO lUserServiceVO : userServiceList) {
					if (lUserVO.getUserId().equals(lUserServiceVO.getUserId())) {
						if ( !"99".equals(lUserServiceVO.getServiceGb())) {
							userService += lUserServiceVO.getServiceNm() + " ";
						}
					}
				}
				lUserVO.setServiceNm(userService);
				userList.add(lUserVO);
			}
		}

		if (userList != null && userList.size() > 0) {
			totalCount = userList.get(0).getTotalCount();
		}

		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);

		model.addAttribute("searchVO", searchVO); //검색항목
		model.addAttribute("userList", userList); // 사용자그룹 목록
		model.addAttribute("pageUtil", pageUtil); // 페이징

		return "sys/acc/userList";
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
	 * 하위 부서 목록 전체 조회 -- NEO_ORGANIZATION (현재 미사용 )
	 * 
	 * @param orgVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	private List<OrganizationVO> getOrgDescendantList(String upOrgCd) {

		List<OrganizationVO> orgList = new ArrayList<OrganizationVO>();
		List<OrganizationVO> orgDescendantList = null;
		List<OrganizationVO> orgChildList = null;
		OrganizationVO orgVO = new OrganizationVO();

		try {
			// 첫번째것 넣어줌
			orgVO = accountService.getOrgInfo(upOrgCd);
			if (orgVO != null ) 
			{
				orgList.add(orgVO);
			}

			orgChildList = accountService.getOrgChildList(upOrgCd);
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
			logger.error("accountService.getOrgDescendantList error = " + e);
		}

		return orgList;
	}

	/** 
	 * 하위 부서 목록 전체 재귀 함수 -- NEO_ORGANIZATION (현재 미사용 ) 
	 * 
	 * @param orgVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */	
	private List<OrganizationVO> getOrgChildList(String orgCd) {

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
			logger.error("accountService.getOrgChildList error = " + e);
		}

		return orgChildList;
	}	
	
	/**
	 * 사용자 신규 등록 화면을 출력한다
	 * 
	 * @param userCodeGroupVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/userAddP")
	public String goUserAddP(@ModelAttribute UserVO searchVO, Model model, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {

		searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		logger.debug("goUserList getUilang= " + searchVO.getUilang());

		// 코드그룹목록(코드성) 조회 -- 타임존
		CodeVO tzCd = new CodeVO();
		tzCd.setUilang(searchVO.getUilang());
		tzCd.setUseYn("Y");
		List<CodeVO> tzCdList = null;
		try {
			tzCdList = codeService.getTimezoneList(tzCd);
		} catch (Exception e) {
			logger.error("codeService.timeZoneList error = " + e);
		}

		// 코드그룹목록(코드성) 조회 -- 매일문자셋
		CodeVO charset = new CodeVO();
		charset.setUilang(searchVO.getUilang());
		charset.setCdGrp("C022");
		charset.setUseYn("Y");
		List<CodeVO> charsetList = null;
		try {
			charsetList = codeService.getCodeList(charset);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[022] error = " + e);
		}

		// 코드그룹목록(코드성) 조회 -- UI언어권
		CodeVO uilang = new CodeVO();
		uilang.setUilang(searchVO.getUilang());
		uilang.setCdGrp("C025");
		uilang.setUseYn("Y");
		List<CodeVO> uilangList = null;
		try {
			uilangList = codeService.getCodeList(uilang);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[022] error = " + e);
		}

		// 코드그룹목록(코드성) 조회 -- 사용자상태
		CodeVO status = new CodeVO();
		status.setUilang(searchVO.getUilang());
		status.setCdGrp("C010");
		status.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(status);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[C010] error = " + e);
		}

		// 코드그룹목록(코드성) 조회 -- 사용자그룹
		CodeVO dept = new CodeVO();
		dept.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(dept);
		} catch (Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		// 코드그룹목록(코드성) 조회 -- 직책
		CodeVO jobGb = new CodeVO();
		jobGb.setUilang(searchVO.getUilang());
		jobGb.setCdGrp("C105");
		jobGb.setUseYn("Y");
		List<CodeVO> jobGbList = null;
		try {
			jobGbList = codeService.getCodeList(jobGb);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[C105] error = " + e);
		}

		// 코드그룹목록(코드성) 조회 -- 직급
		CodeVO positionGb = new CodeVO();
		positionGb.setUilang(searchVO.getUilang());
		positionGb.setCdGrp("C106");
		positionGb.setUseYn("Y");
		List<CodeVO> positionGbList = null;
		try {
			positionGbList = codeService.getCodeList(positionGb);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[C010] error = " + e);
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

		model.addAttribute("tzCdList", tzCdList); // 타임존 항목
		model.addAttribute("charsetList", charsetList); // 메일문자셋 항목
		model.addAttribute("uilangList", uilangList); // UI언어권 항목
		model.addAttribute("statusList", statusList); // 사용자상태 항목
		model.addAttribute("deptList", deptList); // 그룹 항목
		model.addAttribute("jobGbList", jobGbList); // 직책 항목
		model.addAttribute("orgList", orgList); // 부서(최상위)항목
		model.addAttribute("positionGbList", positionGbList); // 직급 항목

		return "sys/acc/userAddP";
	}

	/**
	 * 사용자 정보를 조회하고 화면을 출력한다
	 * 
	 * @param userVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/userUpdateP")
	
	public String goUserUpdateP(@ModelAttribute UserVO searchVO, Model model, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		logger.debug("goUserUpdateP getUserId = " + searchVO.getUserId());

		searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));

		UserVO userInfo = null;
		if (searchVO.getUserId() == null || "".equals(searchVO.getUserId())){
			searchVO.setUserId((String) session.getAttribute("NEO_USER_ID")); 
		}
		try {
			userInfo = accountService.getUserInfoDetail(searchVO);
			userInfo.setUseEMS("N");
			userInfo.setUseRNS("N");
			userInfo.setUsePUSH("N");
			userInfo.setUseSMS("N");			
			userInfo.setUseSYS("N");

		} catch (Exception e) {
			logger.error("accountService.getUserInfo error = " + e);
		}

		// 코드그룹목록(코드성) 조회 -- 서비스리스트
		List<UserVO> userServiceList = null;
		try {
			userServiceList = accountService.getUserServiceList(searchVO);
		} catch (Exception e) {
			logger.error("accountService.getUserServiceList error = " + e);
		}

		for (UserVO lUserServiceVO : userServiceList) {
			if (userInfo.getUserId().equals(lUserServiceVO.getUserId())) {
				if (lUserServiceVO.getServiceGb().equals("10")) {
					userInfo.setUseEMS("Y");
				} else if (lUserServiceVO.getServiceGb().equals("20")) {
					userInfo.setUseRNS("Y");
				} else if (lUserServiceVO.getServiceGb().equals("30")) {
					userInfo.setUseSMS("Y");					
				} else if (lUserServiceVO.getServiceGb().equals("40")) {
					userInfo.setUsePUSH("Y");
				} else if (lUserServiceVO.getServiceGb().equals("99")) {
					userInfo.setUseSYS("Y");
				}
			}
		}

		// 코드그룹목록(코드성) 조회 -- 타임존
		CodeVO tzCd = new CodeVO();
		tzCd.setUilang(searchVO.getUilang());
		tzCd.setUseYn("Y");
		List<CodeVO> tzCdList = null;
		try {
			tzCdList = codeService.getTimezoneList(tzCd);
		} catch (Exception e) {
			logger.error("codeService.timeZoneList error = " + e);
		}

		// 코드그룹목록(코드성) 조회 -- 매일문자셋
		CodeVO charset = new CodeVO();
		charset.setUilang(searchVO.getUilang());
		charset.setCdGrp("C022");
		charset.setUseYn("Y");
		List<CodeVO> charsetList = null;
		try {
			charsetList = codeService.getCodeList(charset);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[022] error = " + e);
		}

		// 코드그룹목록(코드성) 조회 -- UI언어권
		CodeVO uilang = new CodeVO();
		uilang.setUilang(searchVO.getUilang());
		uilang.setCdGrp("C025");
		uilang.setUseYn("Y");
		List<CodeVO> uilangList = null;
		try {
			uilangList = codeService.getCodeList(uilang);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[022] error = " + e);
		}

		// 코드그룹목록(코드성) 조회 -- 사용자상태
		CodeVO status = new CodeVO();
		status.setUilang(searchVO.getUilang());
		status.setCdGrp("C010");
		status.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(status);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[C010] error = " + e);
		}

		// 코드그룹목록(코드성) 조회 -- 사용자그룹
		CodeVO dept = new CodeVO();
		dept.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(dept);
		} catch (Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		// 코드그룹목록(코드성) 조회 -- 직책
		CodeVO jobGb = new CodeVO();
		jobGb.setUilang(searchVO.getUilang());
		jobGb.setCdGrp("C105");
		jobGb.setUseYn("Y");
		List<CodeVO> jobGbList = null;
		try {
			jobGbList = codeService.getCodeList(jobGb);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[C105] error = " + e);
		}

		// 코드그룹목록(코드성) 조회 -- 직급
		CodeVO positionGb = new CodeVO();
		positionGb.setUilang(searchVO.getUilang());
		positionGb.setCdGrp("C106");
		positionGb.setUseYn("Y");
		List<CodeVO> positionGbList = null;
		try {
			positionGbList = codeService.getCodeList(positionGb);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[C010] error = " + e);
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

		model.addAttribute("searchVO", searchVO); // 검색항목
		model.addAttribute("userInfo", userInfo); // 사용자 조회 결과
		model.addAttribute("tzCdList", tzCdList); // 타임존 항목
		model.addAttribute("charsetList", charsetList); // 메일문자셋 항목
		model.addAttribute("uilangList", uilangList); // UI언어권 항목
		model.addAttribute("statusList", statusList); // 사용자상태 항목
		model.addAttribute("orgList", orgList); // 부서(최상위)항목
		model.addAttribute("deptList", deptList); // 그룹 항목
		model.addAttribute("jobGbList", jobGbList); // 직책 항목
		model.addAttribute("positionGbList", positionGbList); // 직급 항목

		return "sys/acc/userUpdateP";
	}

	/**
	 * 사용자 정보를 조회한다.
	 * 
	 * @param deptVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/userInfo")
	public ModelAndView getUserInfo(@ModelAttribute UserVO userVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		logger.debug("getUserInfo getUserId = " + userVO.getUserId());

		userVO.setUilang((String) session.getAttribute("NEO_UILANG"));

		UserVO userInfoVO = null;
		try {
			userInfoVO = accountService.getUserInfo(userVO);
			userInfoVO.setRegDt(StringUtil.getFDate(userInfoVO.getRegDt(), Code.DT_FMT2));
			userInfoVO.setUpDt(StringUtil.getFDate(userInfoVO.getUpDt(), Code.DT_FMT2));

		} catch (Exception e) {
			logger.error("accountService.getUserInfo error = " + e);
		}

		//암복호화 처리 
		if (userInfoVO != null) {
			String userEM = userInfoVO.getUserEm();
			String userTel = userInfoVO.getUserTel();
			
			logger.debug("userTel = " + userTel +  " / userTel = " + userTel  );
			
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userInfo", userInfoVO);

		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}

	/**
	 * 팝업 사용자 정보를 조회하고 화면을 출력한다
	 * 
	 * @param userVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/popUserInfo")
	public ModelAndView goPopUserInfo(@ModelAttribute UserVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopUserInfo getUserId = " + searchVO.getUserId());
		
		searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		searchVO.setUserId((String) session.getAttribute("NEO_USER_ID"));
		
		UserVO userInfo = null; 

		try {
			userInfo = accountService.getUserInfo(searchVO); 
		} catch (Exception e) {
			logger.error("accountService.goPopUserInfo error = " + e);
		}
		
		// 코드그룹목록(코드성) 조회 -- 타임존
		CodeVO tzCd = new CodeVO();
		tzCd.setUilang(searchVO.getUilang());
		tzCd.setUseYn("Y");
		List<CodeVO> tzCdList = null;
		try {
			tzCdList = codeService.getTimezoneList(tzCd);
		} catch (Exception e) {
			logger.error("codeService.timeZoneList error = " + e);
		}

		// 코드그룹목록(코드성) 조회 -- 매일문자셋
		CodeVO charset = new CodeVO();
		charset.setUilang(searchVO.getUilang());
		charset.setCdGrp("C022");
		charset.setUseYn("Y");
		List<CodeVO> charsetList = null;
		try {
			charsetList = codeService.getCodeList(charset);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[022] error = " + e);
		}

		// 코드그룹목록(코드성) 조회 -- UI언어권
		CodeVO uilang = new CodeVO();
		uilang.setUilang(searchVO.getUilang());
		uilang.setCdGrp("C025");
		uilang.setUseYn("Y");
		List<CodeVO> uilangList = null;
		try {
			uilangList = codeService.getCodeList(uilang);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[022] error = " + e);
		} 

		HashMap<String, Object> map = new HashMap<String, Object>();
		if (userInfo != null) {
			map.put("result", "Success"); 
			map.put("userInfo", userInfo);
			map.put("tzCdList", tzCdList);
			map.put("charsetList", charsetList);
			map.put("uilangList", uilangList);
		} else {
			map.put("result", "Fail");
			map.put("resultMessage", "Wrong Session Info");
		}
		
		ModelAndView modelAndView = new ModelAndView("jsonView", map); 

		return modelAndView;
	}

	/**
	 * 사용자 아이디를 체크한다. 아이디 중복을 방지.
	 * 
	 * @param userVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/userIdCheck")
	public ModelAndView userIdCheck(@ModelAttribute UserVO userVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("userIdCheck userId = " + userVO.getUserId());

		boolean result = false;
		List<UserVO> userList = null;
		try {
			userList = accountService.userIdCheck(userVO.getUserId());
		} catch (Exception e) {
			logger.error("accountService.userIdCheck error = " + e);
		}

		if (userList != null && userList.size() > 0) {
			result = false;
		} else {
			result = true;
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result) {
			map.put("result", "Success");
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}

	/**
	 * 사용자 정보를 등록한다.
	 * 
	 * @param userVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/userAdd")
	public ModelAndView insertUserInfo(@ModelAttribute UserVO userVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		logger.debug("insertUserInfo userId = " + userVO.getUserId());
		logger.debug("insertUserInfo userNm = " + userVO.getUserNm());
		logger.debug("insertUserInfo userPwd = " + userVO.getUserPwd());
		logger.debug("insertUserInfo userEm = " + userVO.getUserEm()); 
		logger.debug("insertUserInfo deptNo = " + userVO.getDeptNo());
		logger.debug("insertUserInfo progId = " + userVO.getProgId());
		logger.debug("insertUserInfo serviceGb = " + userVO.getServiceGb());
		logger.debug("insertUserInfo orgCd = " + userVO.getOrgCd());

		userVO.setUserPwd(EncryptUtil.getEncryptedSHA256(userVO.getUserPwd()));
		userVO.setRegId((String) session.getAttribute("NEO_USER_ID"));
		userVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		userVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		userVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));

		int result = 0;
 
		if(userVO.getUserTel() != null && !"".equals(userVO.getUserTel().trim())) {
			userVO.setUserTel(cryptoService.getEncrypt("USER_TEL", userVO.getUserTel()));
		}
		if(userVO.getUserEm() != null && !"".equals(userVO.getUserEm().trim())) {
			userVO.setUserEm(cryptoService.getEncrypt("USER_EM", userVO.getUserEm()));
		}
		if(userVO.getReturnEm() != null && !"".equals(userVO.getReturnEm().trim())) {
			userVO.setReturnEm(cryptoService.getEncrypt("RETURN_EM", userVO.getReturnEm()));
		}
		if(userVO.getReplyToEm() != null && !"".equals(userVO.getReplyToEm().trim())) {
			userVO.setReplyToEm(cryptoService.getEncrypt("REPLY_TO_EM", userVO.getReplyToEm() ));
		}
		if(userVO.getMailFromEm() != null && !"".equals(userVO.getMailFromEm().trim())) {
			userVO.setMailFromEm(cryptoService.getEncrypt("MAIL_FROM_EM", userVO.getMailFromEm()));
		} 

		// 사용자 정보를 등록한다.
		try {
			result = accountService.insertUserInfo(userVO);
		} catch (Exception e) {
			logger.error("accountService.insertUserInfo error = " + e);
		}

		// 서비스
		UserProgVO userPrgVO = new UserProgVO();
		userPrgVO.setUserId(userVO.getUserId());
		userPrgVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		userPrgVO.setRegId((String) session.getAttribute("NEO_USER_ID"));		

		try {
			result = accountService.deleteUserProgInfo(userPrgVO);
			if (result >= 0) {
				if (userVO.getServiceGb().length() > 0) {

					String[] serviceGb = userVO.getServiceGb().split(",");

					for (int i = 0; i < serviceGb.length; i++) {
						userPrgVO.setProgId((Integer.parseInt(serviceGb[i])));
						try {
							result = accountService.insertUserProgInfo(userPrgVO);
							result = accountService.insertUserDefaultMenu(userPrgVO);
						} catch (Exception e) {
							logger.error("accountService.insertUserProgInfo error = " + e);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("accountService.deleteUserProgInfo error = " + e);
		}

		// 조직
		UserOrgVO userOrgVO = new UserOrgVO();
		userOrgVO.setUserId(userVO.getUserId());
		userOrgVO.setOrgCd(userVO.getOrgCd());
		userOrgVO.setPositionGb(userVO.getPositionGb());
		userOrgVO.setJobGb(userVO.getJobGb());
		userOrgVO.setRegDt(userVO.getRegDt());
		userOrgVO.setRegId(userVO.getRegId());
		userOrgVO.setUpDt(userVO.getUpDt());
		userOrgVO.setUpId(userVO.getUpId());

		try {
			result = accountService.deleteUserOrgInfo(userOrgVO);
			if (result >= 0) {
				try {
					result = accountService.insertUserOrgInfo(userOrgVO);
				} catch (Exception e) {
					logger.error("accountService.insertUserOrgInfo error = " + e);
				}
			}
		} catch (Exception e) {
			logger.error("accountService.deleteUserOrgInfo error = " + e);
		}

		// 결과값
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
	 * 사용자 정보를 업데이트 한다.
	 * 
	 * @param userVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/userUpdate")
	public ModelAndView updateUserInfo(@ModelAttribute UserVO userVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateUserInfo userId = " + userVO.getUserId());
		logger.debug("updateUserInfo userNm = " + userVO.getUserNm());
		logger.debug("updateUserInfo userPwd = " + userVO.getUserPwd());
		logger.debug("updateUserInfo userEm = " + userVO.getUserEm());
		logger.debug("updateUserInfo userTel = " + userVO.getUserTel());
		logger.debug("updateUserInfo status = " + userVO.getStatus());
		logger.debug("updateUserInfo deptNo = " + userVO.getDeptNo());
		logger.debug("updateUserInfo progId = " + userVO.getProgId());
		logger.debug("updateUserInfo serviceGb = " + userVO.getServiceGb());
		logger.debug("updateUserInfo orgCd = " + userVO.getOrgCd());
 
		userVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		userVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		int result = 0;

		if(userVO.getUserTel() != null && !"".equals(userVO.getUserTel().trim())) {
			userVO.setUserTel(cryptoService.getEncrypt("USER_TEL", userVO.getUserTel()));
		}
		if(userVO.getUserEm() != null && !"".equals(userVO.getUserEm().trim())) {
			userVO.setUserEm(cryptoService.getEncrypt("USER_EM", userVO.getUserEm()));
		}
		if(userVO.getReturnEm() != null && !"".equals(userVO.getReturnEm().trim())) {
			userVO.setReturnEm(cryptoService.getEncrypt("RETURN_EM", userVO.getReturnEm()));
		}
		if(userVO.getReplyToEm() != null && !"".equals(userVO.getReplyToEm().trim())) {
			userVO.setReplyToEm(cryptoService.getEncrypt("REPLY_TO_EM", userVO.getReplyToEm() ));
		}
		if(userVO.getMailFromEm() != null && !"".equals(userVO.getMailFromEm().trim())) {
			userVO.setMailFromEm(cryptoService.getEncrypt("MAIL_FROM_EM", userVO.getMailFromEm()));
		} 
		
		// 사용자 정보를 수정한다.
		try {
			result = accountService.updateUserInfo(userVO);
		} catch (Exception e) {
			logger.error("accountService.updateUserInfo error = " + e);
		}

		// 서비스
		UserProgVO userPrgVO = new UserProgVO();
		userPrgVO.setUserId(userVO.getUserId());
		userPrgVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		userPrgVO.setRegId((String) session.getAttribute("NEO_USER_ID"));
		boolean sysYn = false;

		try {
			result = accountService.deleteUserProgInfo(userPrgVO);
			if (result >= 0) {
				if (userVO.getServiceGb().length() > 0) {

					String[] serviceGb = userVO.getServiceGb().split(",");

					for (int i = 0; i < serviceGb.length; i++) {
						userPrgVO.setProgId((Integer.parseInt(serviceGb[i])));
						try {
							result = accountService.insertUserProgInfo(userPrgVO);
							//시스템 공통설정에 한하여 처리함  
							if ( userPrgVO.getProgId() == 99 )
							{
								sysYn = true; 
								result = accountService.deleteUserSysMenu(userVO);
								result = accountService.insertUserDefaultMenu(userPrgVO);
							}
						} catch (Exception e) {
							logger.error("accountService.insertUserProgInfo error = " + e);
						}
					}
					
					if (sysYn == false ) {
						result = accountService.deleteUserSysMenu(userVO);
					}
				}
			}
		} catch (Exception e) {
			logger.error("accountService.deleteUserProgInfo error = " + e);
		}

		// 조직
		UserOrgVO userOrgVO = new UserOrgVO();
		userOrgVO.setUserId(userVO.getUserId());
		userOrgVO.setOrgCd(userVO.getOrgCd());
		userOrgVO.setPositionGb(userVO.getPositionGb());
		userOrgVO.setJobGb(userVO.getJobGb());
		userOrgVO.setRegDt(userVO.getRegDt());
		userOrgVO.setRegId(userVO.getRegId());
		userOrgVO.setUpDt(userVO.getUpDt());
		userOrgVO.setUpId(userVO.getUpId());

		try {
			result = accountService.deleteUserOrgInfo(userOrgVO);
			if (result >= 0) {
				try {
					result = accountService.insertUserOrgInfo(userOrgVO);
				} catch (Exception e) {
					logger.error("accountService.insertUserOrgInfo error = " + e);
				}
			}
		} catch (Exception e) {
			logger.error("accountService.deleteUserOrgInfo error = " + e);
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
	 * 사용자 정보를 업데이트 한다.
	 * 
	 * @param userVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/popUserUpdate")
	public ModelAndView updatePopUserInfo(@ModelAttribute UserVO userVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updatePopUserInfo userNm     = " + userVO.getPopUserNm());
		logger.debug("updatePopUserInfo userEm     = " + userVO.getPopUserEm());
		logger.debug("updatePopUserInfo userTel    = " + userVO.getPopUserTel());	
		logger.debug("updatePopUserInfo deptNo     = " + userVO.getPopDeptNo());
		logger.debug("updatePopUserInfo userEm     = " + userVO.getPopUserEm());
		logger.debug("updatePopUserInfo mailFromNm = " + userVO.getPopMailFromNm());
		logger.debug("updatePopUserInfo mailFromEm = " + userVO.getPopMailFromEm());
		logger.debug("updatePopUserInfo returnEm   = " + userVO.getPopReturnEm());
		logger.debug("updatePopUserInfo replyToEm  = " + userVO.getPopReplyToEm());
		logger.debug("updatePopUserInfo charset    = " + userVO.getPopCharset());
		logger.debug("updatePopUserInfo tzCd       = " + userVO.getPopTzCd());
		logger.debug("updatePopUserInfo uilang     = " + userVO.getPopUilang());
		
		int result = 0;
		
		userVO.setDeptNo(userVO.getPopDeptNo());
		userVO.setUserNm(userVO.getPopUserNm());
		userVO.setUserTel(userVO.getPopUserTel());
		userVO.setUserEm(userVO.getPopUserEm());
		userVO.setMailFromNm(userVO.getPopMailFromNm());
		userVO.setMailFromEm(userVO.getPopMailFromEm());
		userVO.setReplyToEm(userVO.getPopReplyToEm());
		userVO.setReturnEm(userVO.getPopReturnEm());
		userVO.setCharset(userVO.getPopCharset());
		userVO.setTzCd(userVO.getPopTzCd());
		userVO.setUilang(userVO.getPopUilang());
		userVO.setTzTerm(userVO.getPopTzTerm());
		
		userVO.setUserId((String) session.getAttribute("NEO_USER_ID"));
		userVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		userVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		if(userVO.getUserTel() != null && !"".equals(userVO.getUserTel().trim())) {
			userVO.setUserTel(cryptoService.getEncrypt("USER_TEL", userVO.getUserTel()));
		}
		if(userVO.getUserEm() != null && !"".equals(userVO.getUserEm().trim())) {
			userVO.setUserEm(cryptoService.getEncrypt("USER_EM", userVO.getUserEm()));
		}
		if(userVO.getReturnEm() != null && !"".equals(userVO.getReturnEm().trim())) {
			userVO.setReturnEm(cryptoService.getEncrypt("RETURN_EM", userVO.getReturnEm()));
		}
		if(userVO.getReplyToEm() != null && !"".equals(userVO.getReplyToEm().trim())) {
			userVO.setReplyToEm(cryptoService.getEncrypt("REPLY_TO_EM", userVO.getReplyToEm() ));
		}
		if(userVO.getMailFromEm() != null && !"".equals(userVO.getMailFromEm().trim())) {
			userVO.setMailFromEm(cryptoService.getEncrypt("MAIL_FROM_EM", userVO.getMailFromEm()));
		} 
		
		// 사용자 정보를 수정한다.
		try {
			result = accountService.updateUserInfo(userVO);
		} catch (Exception e) {
			logger.error("accountService.updateUserInfo error = " + e);
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");
			//세션정보를 수정한다 
			session.setAttribute("NEO_USER_NM", userVO.getUserNm());		// 사용자명
			session.setAttribute("NEO_TZ_CD", userVO.getTzCd());			// 타임존코드
			session.setAttribute("NEO_TZ_TERM", userVO.getTzTerm());		// 타임존시간차
			session.setAttribute("NEO_UILANG", userVO.getUilang());			// 언어권
			session.setAttribute("NEO_CHARSET", userVO.getCharset());		// 문자셋
			session.setAttribute("NEO_MAIL_FROM_NM", userVO.getMailFromNm()); 	//발송자명 
			session.setAttribute("NEO_MAIL_FROM_EM", userVO.getMailFromEm()); 	//발송자이메일주소
			session.setAttribute("NEO_USER_TEL", userVO.getUserTel());			//연락처
			session.setAttribute("NEO_USER_EM", userVO.getUserEm());			//이메일
			session.setAttribute("NEO_REPLY_TO_EM", userVO.getReplyToEm());		//회신이메일
			session.setAttribute("NEO_RETURN_EM", userVO.getReturnEm()); 		//return 이메일 
			
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		return modelAndView;
	}

	/**
	 * 사용자 비밀번호를 업데이트 한다.
	 * 
	 * @param userVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/userUpdatePassword")
	public ModelAndView userUpdatePassword(@ModelAttribute UserVO userVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("userUpdatePassword userPwd = " + userVO.getUserPwd()); //변경할 암호

		int result = 0;
		
		if (userVO.getUserPwd() != null && !"".equals(userVO.getUserPwd())) {
			userVO.setUserPwd(EncryptUtil.getEncryptedSHA256(userVO.getUserPwd()));
		}
		
		userVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		userVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		try {		 
			result = accountService.updateUserPwd(userVO); 
		} catch (Exception e) {
			logger.error("accountService.userUpdatePassword error = " + e);
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");
		} else if (result == -7) { 
			map.put("result", "No Session Info");
		} else if (result == -9) {
			map.put("result", "Not Equal");
		} else {
			map.put("result", "Fail");
		}

		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		return modelAndView;
	}
	
	/**
	 * 팝업에서 사용자 비밀번호를 업데이트 한다.
	 * 
	 * @param userVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/userUpdatePasswordPop")
	public ModelAndView userUpdatePasswordPop(@ModelAttribute UserVO userVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("userUpdatePasswordPop userPwd = " + userVO.getUserPwd()); //변경할 암호
		logger.debug("userUpdatePasswordPop curPwd = " + userVO.getCurPwd()); // 이전 사용 암호 

		int result = 0;
		
		String cipherUserPwd = userVO.getUserPwd();
		String cipherCurPwd = userVO.getCurPwd();

		try {
			userVO.setUserPwd(EncryptAccUtil.getEncryptedSHA256(cipherUserPwd,properties.getProperty("ACCOUNT.KEYSTRING")));
			userVO.setCurPwd(EncryptAccUtil.getEncryptedSHA256(cipherCurPwd, properties.getProperty("ACCOUNT.KEYSTRING")));
		} catch (Exception e) {
			result = -8;
			logger.error("userUpdatePasswordPop EncryptAccUtil Error = " + e);
		} 
		
		logger.debug("userUpdatePasswordPop userPwd = " + userVO.getUserPwd()); //변경할 암호
		logger.debug("userUpdatePasswordPop curPwd = " + userVO.getCurPwd()); // 이전 사용 암호 
		
		if (session.getAttribute("NEO_USER_ID") != null && !"".equals(session.getAttribute("NEO_USER_ID"))){
			userVO.setUserId((String) session.getAttribute("NEO_USER_ID")); 
			
			if (userVO.getUserPwd() != null && !"".equals(userVO.getUserPwd())) {
				userVO.setUserPwd(EncryptUtil.getEncryptedSHA256(userVO.getUserPwd()));
			}

			if (userVO.getCurPwd() != null && !"".equals(userVO.getCurPwd())) {
				userVO.setCurPwd(EncryptUtil.getEncryptedSHA256(userVO.getCurPwd()));
			}
			
			userVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
			userVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
			
			try {
				// 팝업에서 사용자 비밀번호 변경시 현재 비밀번호 일치 여부 확인 
				if (userVO.getCurPwd() != null && !"".equals(userVO.getCurPwd())) {
					result = accountService.checkUserPwd(userVO);
				} else {
					result = 1 ;
				}

				if(result > 0 ) {
					result = accountService.updateUserPwd(userVO);
				} else {
					result = -9;
				}
			} catch (Exception e) {
				logger.error("accountService.userUpdatePassword error = " + e);
			}
		} else {
			result = -7;
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");
		} else if (result == -7) {
			map.put("result", "Decrypt Fail");
		} else if (result == -8) {
			map.put("result", "No Session Info");
		} else if (result == -9) {
			map.put("result", "Not Equal");
		} else {
			map.put("result", "Fail");
		}

		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		return modelAndView;
	}
	
	
	/**
	 * 초기화된 비밀번호 사용자의 비밀번호를 업데이트 한다.
	 * 
	 * @param userVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/userUpdateInitPassword")
	public ModelAndView userUpdateInitPassword(@ModelAttribute UserVO userVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("userUpdateInitPassword userId = " + userVO.getUserId()); // 초기화된 비밀번호 변경할 사용자 ID 암호화 된것 
		logger.debug("userUpdateInitPassword userPwd = " + userVO.getUserPwd()); // 초기화된 비밀번호 변경할 암호 암호화 된것 
 
		int result = 0;
		
		String cipherUserId = userVO.getUserId();
		String cipherUserPwd = userVO.getUserPwd();
 
		try {
			userVO.setUserId(EncryptAccUtil.getEncryptedSHA256(cipherUserId, properties.getProperty("ACCOUNT.KEYSTRING")));
			userVO.setUserPwd(EncryptAccUtil.getEncryptedSHA256(cipherUserPwd,properties.getProperty("ACCOUNT.KEYSTRING")));
		} catch (Exception e) {
			result = -8;
			logger.error("userUpdateInitPassword EncryptAccUtil Error = " + e);
		} 
		
		logger.debug("userUpdateInitPassword userId = " + userVO.getUserId()); // 사용자ID
		logger.debug("userUpdateInitPassword userPwd = " + userVO.getUserPwd()); //변경할 암호

		if (!"Y".equals(userVO.getPwInitYn())){
			result = -9;
		} else {
			if (userVO.getUserPwd() != null && !"".equals(userVO.getUserPwd())) {
				userVO.setUserPwd(EncryptUtil.getEncryptedSHA256(userVO.getUserPwd()));
			}
			
			userVO.setUpId(userVO.getUserId());
			userVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
			
			// 사용자 정보를 수정한다.
			try {
				result = accountService.updateUserPwd(userVO);
			} catch (Exception e) {
				logger.error("accountService.userUpdateInitPassword error = " + e);
			}
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");	
			map.put("message", "비밀번호가 변경되었습니다 변경된 비밀번호로 로그인해주세요");
		} else {
			map.put("result", "Fail");
			if(result == -9) {
				map.put("message", "비밀번호가 초기화되지 않은 사용자입니다 사용하시던 비밀번호로 로그인해주세요");
			} else if(result == -8) {
				map.put("message", "요청 정보 복호화에 실패했습니다");
			} else {
				map.put("message", "비밀번호가 변경에 실패하였습니다 다시 시도하여주세요 ");
			} 
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		return modelAndView;
	}
	
	/**
	 * 사용자 비밀번호를 초기화 한다.
	 * 
	 * @param userVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/userResetPassword")
	public ModelAndView userResetPassword(@ModelAttribute UserVO userVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
 		userVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		userVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		int result = 0;
		String strInitPwd ="";
		// 사용자 정보를 수정한다.
		
		try { 	
			//strInitPwd =  StringUtil.makeRandomString(Code.RAND_TYPE_C);
			strInitPwd =  "shinhanfund!" + userVO.getUserId();
			userVO.setUserPwd(EncryptUtil.getEncryptedSHA256(strInitPwd));
			result = accountService.initUserPwd(userVO);
		} catch (Exception e) {
			logger.error("accountService.initUserPwd error = " + e);
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");
			map.put("initWord", strInitPwd);
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		return modelAndView;
	}
	
	/**
	 * 사용자 정보를 삭제한다
	 * 
	 * @param deptVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/userDelete")
	public ModelAndView deleteUserInfo(@ModelAttribute UserVO userVo, Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		logger.debug("userDelete getUserIds      = " + userVo.getUserIds());

		userVo.setUpId((String) session.getAttribute("NEO_USER_ID"));
		userVo.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		userVo.setStatus("002");

		String[] userId = userVo.getUserIds().split(",");

		int result = 0;

		for (int i = 0; i < userId.length; i++) {
			userVo.setUserId(userId[i]);
			try {
				result = accountService.deleteUserInfo(userVo);
			} catch (Exception e) {
				logger.error("accountService.deleteUserInfo error = " + e);
			}
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


	/**
	 * 사용자 바로 가기 정보 업데이트 
	 * 
	 * @param deptVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/makeLink")
	public ModelAndView makeLink(@ModelAttribute UserVO userVo, Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		
		logger.debug("makeLink getProgId      = " + userVo.getProgId());

		userVo.setUserId((String) session.getAttribute("NEO_USER_ID"));
	  
		int result = 0;
		
		try { 
			result = accountService.deleteServiceLink(userVo); 
			
			if (!"0".equals(userVo.getProgId())){
				try {
					result = accountService.updateServiceLink(userVo); 
				} catch (Exception e) {
					logger.error("accountService.updateServiceLink error = " + e);
				} 
			} 
		} catch (Exception e) {
			logger.error("accountService.deleteServiceLink error = " + e);
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
