/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 시스템 관리 Controller==>데이터베이스 연결 관리 Controller
 * 수정자 : 김준희
 * 작성일시 : 2021.08.09
 * 수정내역 : 패키지명수정 및 sys관련 분리에 의한 소스 변경 kr.co.sict.ums.sys.controller ==> kr.co.sict.ums.sys.dbc.controller
 *                데이터베이스 연결 관리 기능 외의 항목제거
 */
package kr.co.sict.ums.sys.dbc.controller;

import java.sql.Connection;
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
import kr.co.sict.ums.sys.acc.service.AccountService;
import kr.co.sict.ums.sys.acc.vo.DeptVO;
import kr.co.sict.ums.sys.acc.vo.OrganizationVO;
import kr.co.sict.ums.sys.acc.vo.UserVO;
import kr.co.sict.ums.sys.dbc.service.DBConnService;
import kr.co.sict.ums.sys.dbc.vo.DbConnGrpPermVO;
import kr.co.sict.ums.sys.dbc.vo.DbConnPermVO;
import kr.co.sict.ums.sys.dbc.vo.DbConnUserVO;
import kr.co.sict.ums.sys.dbc.vo.DbConnVO;
import kr.co.sict.ums.sys.dbc.vo.MetaColumnVO;
import kr.co.sict.ums.sys.dbc.vo.MetaJoinVO;
import kr.co.sict.ums.sys.dbc.vo.MetaOperatorVO;
import kr.co.sict.ums.sys.dbc.vo.MetaTableVO;
import kr.co.sict.ums.sys.dbc.vo.MetaValueVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.DBUtil;
import kr.co.sict.util.EncryptUtil;
import kr.co.sict.util.PageUtil;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Controller
@RequestMapping("/sys/dbc")
public class DBConnController {
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private CodeService codeService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private DBConnService dbConnService;

	@Autowired
	private PropertiesUtil properties;

	// #############################################################  데이터베이스 연결 정보 처리  ############################################################# //
	/**
	 * DB Connection 목록 화면을 출력한다.
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/dbConnListP")
	public String goDbConnListP(@ModelAttribute DbConnVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goDbConnListP getSearchDbConnNm = " + searchVO.getSearchDbConnNm());
		logger.debug("goDbConnListP getSearchDbTy = " + searchVO.getSearchDbTy());
		logger.debug("goDbConnListP getSearchStatus = " + searchVO.getSearchStatus());

		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		searchVO.setPage(page);

		// DBMS 유형 목록을 조회한다.
		CodeVO dbmsTypeVO = new CodeVO();
		dbmsTypeVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		dbmsTypeVO.setCdGrp("C033"); // DBMS 유형
		dbmsTypeVO.setUseYn("Y");
		List<CodeVO> dbmsTypeList = null;
		try {
			dbmsTypeList = codeService.getCodeList(dbmsTypeVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error[C033] = " + e);
		}

		// DB Connection 상태코드 목록을 조회한다.
		CodeVO dbConnStatusVO = new CodeVO();
		dbConnStatusVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		dbConnStatusVO.setCdGrp("C011"); // DB Connection 상태
		dbConnStatusVO.setUseYn("Y");
		List<CodeVO> dbConnStatusList = null;
		try {
			dbConnStatusList = codeService.getCodeList(dbConnStatusVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error[C011] = " + e);
		}

		// model에 코드 목록 추가
		model.addAttribute("searchVO", searchVO); // 검색 항목
		model.addAttribute("dbmsTypeList", dbmsTypeList); // DBMS유형코드
		model.addAttribute("dbConnStatusList", dbConnStatusList); // DBConnection상태코드

		return "sys/dbc/dbConnListP";
	}

	/**
	 * DB Connection 목록을 출력한다
	 * 
	 * @param dbconnVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/dbConnList")
	public String goDbConnList(@ModelAttribute DbConnVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getDbConnList searchDbConnNm = " + searchVO.getSearchDbConnNm());
		logger.debug("getDbConnList searchDbType = " + searchVO.getSearchDbTy());
		logger.debug("getDbConnList searchDbStatus = " + searchVO.getSearchStatus());

		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;

		// DB 목록 조회
		List<DbConnVO> orgDbConnList = null;
		List<DbConnVO> dbConnList = new ArrayList<DbConnVO>();
		searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		try {
			orgDbConnList = dbConnService.getDbConnList(searchVO);
		} catch (Exception e) {
			logger.error("dbConnService.getDbConnList error = " + e);
		}
		// 등록일시 포멧 수정
		if (orgDbConnList != null) {
			String formatRegDt = "";
			for (DbConnVO lDbconnVO : orgDbConnList) {
				if (lDbconnVO.getRegDt() != null && !"".equals(lDbconnVO.getRegDt())) {
					formatRegDt = StringUtil.getFDate(lDbconnVO.getRegDt(), Code.DT_FMT2);
					formatRegDt = formatRegDt.substring(0, 10);
					lDbconnVO.setRegDt(formatRegDt);
				}
				dbConnList.add(lDbconnVO);
			}
		}

		if (dbConnList != null && dbConnList.size() > 0) {
			totalCount = dbConnList.get(0).getTotalCount();
		}

		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);

		model.addAttribute("dbConnList", dbConnList); // DB 목록
		model.addAttribute("pageUtil", pageUtil); // 페이징
		model.addAttribute("searchVO", searchVO); 
		
		return "sys/dbc/dbConnList";
	}

	/**
	 * DB 연결 신규 등록 화면
	 * 
	 * @param dbConnVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/dbConnAddP")
	public String goDbConnAddP(@ModelAttribute DbConnVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		// DBMS 유형 목록을 조회한다.
		CodeVO dbmsTypeVO = new CodeVO();
		dbmsTypeVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		dbmsTypeVO.setCdGrp("C033"); // DBMS 유형
		dbmsTypeVO.setUseYn("Y");
		List<CodeVO> dbmsTypeList = null;
		try {
			dbmsTypeList = codeService.getCodeList(dbmsTypeVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error[C033] = " + e);
		}

		// DB CharSet 코드 목록을 조회한다.
		CodeVO dbCharSetVO = new CodeVO();
		dbCharSetVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		dbCharSetVO.setCdGrp("C032"); // DB CharSet
		dbCharSetVO.setUseYn("Y");
		List<CodeVO> dbCharSetList = null;
		try {
			dbCharSetList = codeService.getCodeList(dbCharSetVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error[C032] = " + e);
		}

		// DB Connection 상태코드 목록을 조회한다.
		CodeVO dbConnStatusVO = new CodeVO();
		dbConnStatusVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		dbConnStatusVO.setCdGrp("C011"); // DB Connection 상태
		dbConnStatusVO.setUseYn("Y");
		List<CodeVO> dbConnStatusList = null;
		try {
			dbConnStatusList = codeService.getCodeList(dbConnStatusVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error[C011] = " + e);
		}

		// model에 코드 목록 추가
		model.addAttribute("dbmsTypeList", dbmsTypeList); // DBMS유형코드
		model.addAttribute("dbCharSetList", dbCharSetList); // DB CharSet
		model.addAttribute("dbConnStatusList", dbConnStatusList); // DB Connection상태코드

		return "sys/dbc/dbConnAddP";
	}

	/**
	 * DB 연결 정보 조회 화면
	 * 
	 * @param dbConnVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/dbConnUpdateP")
	public String goDbConnUpdateP(@ModelAttribute DbConnVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goDbConnInfo getDbConnNo = " + searchVO.getDbConnNo());

		// DB Connection 정보를 조회한다.
		DbConnVO dbConnInfo = null;
		try {
			searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
			dbConnInfo = dbConnService.getDbConnInfo(searchVO);
			
			dbConnInfo.setLoginPwd(EncryptUtil.getJasyptDecryptedString(properties.getProperty("JASYPT.ALGORITHM"),
			properties.getProperty("JASYPT.KEYSTRING"), dbConnInfo.getLoginPwd()));
			dbConnInfo.setRegDt(StringUtil.getFDate(dbConnInfo.getRegDt(), Code.DT_FMT2));
			dbConnInfo.setUpDt(StringUtil.getFDate(dbConnInfo.getUpDt(), Code.DT_FMT2));

		} catch (Exception e) {
			logger.error("dbConnService.getDbConnInfo error = " + e);
		}

		// DBMS 유형 목록을 조회한다.
		CodeVO dbmsTypeVO = new CodeVO();
		dbmsTypeVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		dbmsTypeVO.setCdGrp("C033"); // DBMS 유형
		dbmsTypeVO.setUseYn("Y");
		List<CodeVO> dbmsTypeList = null;
		try {
			dbmsTypeList = codeService.getCodeList(dbmsTypeVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error[C033] = " + e);
		}

		// DB CharSet 코드 목록을 조회한다.
		CodeVO dbCharSetVO = new CodeVO();
		dbCharSetVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		dbCharSetVO.setCdGrp("C032"); // DB CharSet
		dbCharSetVO.setUseYn("Y");
		List<CodeVO> dbCharSetList = null;
		try {
			dbCharSetList = codeService.getCodeList(dbCharSetVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error[C032] = " + e);
		}

		// DB Connection 상태코드 목록을 조회한다.
		CodeVO dbConnStatusVO = new CodeVO();
		dbConnStatusVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		dbConnStatusVO.setCdGrp("C011"); // DB Connection 상태
		dbConnStatusVO.setUseYn("Y");
		List<CodeVO> dbConnStatusList = null;
		try {
			dbConnStatusList = codeService.getCodeList(dbConnStatusVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error[C011] = " + e);
		}

		// model에 코드 목록 추가
		model.addAttribute("searchInfo", searchVO); // 리스트 페이지 검색정보
		model.addAttribute("dbConnInfo", dbConnInfo); // DB Connetion 정보
		model.addAttribute("dbmsTypeList", dbmsTypeList); // DBMS유형코드
		model.addAttribute("dbCharSetList", dbCharSetList); // DB CharSet
		model.addAttribute("dbConnStatusList", dbConnStatusList); // DB Connection상태코드

		return "sys/dbc/dbConnUpdateP";
	}


	/**
	 * DB 연결 정보 추가
	 * 
	 * @param dbConnVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/dbConnAdd")
	public ModelAndView insertDbConnInfo(@ModelAttribute DbConnVO dbConnVO, Model model, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {

		logger.debug("insertDbConnInfo dbConnNm = " + dbConnVO.getDbConnNm());
		logger.debug("insertDbConnInfo dbTy = " + dbConnVO.getDbTy());
		logger.debug("insertDbConnInfo status = " + dbConnVO.getStatus());
		logger.debug("insertDbConnInfo dbDriver = " + dbConnVO.getDbDriver());
		logger.debug("insertDbConnInfo dbUrl = " + dbConnVO.getDbUrl());
		logger.debug("insertDbConnInfo dbCharSet = " + dbConnVO.getDbCharSet());
		logger.debug("insertDbConnInfo loginId = " + dbConnVO.getLoginId());
		logger.debug("insertDbConnInfo loginPwd = " + dbConnVO.getLoginPwd());
		logger.debug("insertDbConnInfo dbConnDesc = " + dbConnVO.getDbConnDesc());

		dbConnVO.setUilang((String) session.getAttribute("NEO_UILANG"));

		dbConnVO.setLoginPwd(EncryptUtil.getJasyptEncryptedString(properties.getProperty("JASYPT.ALGORITHM"),
		properties.getProperty("JASYPT.KEYSTRING"), dbConnVO.getLoginPwd()));
		dbConnVO.setRegId((String) session.getAttribute("NEO_USER_ID"));
		dbConnVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));

		int result = 0;

		// DB Connection 정보를 등록한다.
		try {
			result = dbConnService.insertDbConnInfo(dbConnVO);
		} catch (Exception e) {
			logger.error("dbConnService.insertDbConnInfo error = " + e);
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
	 * DB Connection 정보를 수정한다.
	 * 
	 * @param dbConnVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/dbConnUpdate")
	public ModelAndView updateDbConnInfo(@ModelAttribute DbConnVO dbConnVO, Model model, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {

		logger.debug("updateDbConnInfo dbConnNo = " + dbConnVO.getDbConnNo());
		logger.debug("updateDbConnInfo dbConnNm = " + dbConnVO.getDbConnNm());
		logger.debug("updateDbConnInfo dbTy = " + dbConnVO.getDbTy());
		logger.debug("updateDbConnInfo status = " + dbConnVO.getStatus());
		logger.debug("updateDbConnInfo dbDriver = " + dbConnVO.getDbDriver());
		logger.debug("updateDbConnInfo dbUrl = " + dbConnVO.getDbUrl());
		logger.debug("updateDbConnInfo dbCharSet = " + dbConnVO.getDbCharSet());
		logger.debug("updateDbConnInfo loginId = " + dbConnVO.getLoginId());
		logger.debug("updateDbConnInfo loginPwd = " + dbConnVO.getLoginPwd());
		logger.debug("updateDbConnInfo dbConnDesc = " + dbConnVO.getDbConnDesc());

		dbConnVO.setLoginPwd(EncryptUtil.getJasyptEncryptedString(properties.getProperty("JASYPT.ALGORITHM"),
		properties.getProperty("JASYPT.KEYSTRING"), dbConnVO.getLoginPwd()));
		dbConnVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		dbConnVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));

		int result = 0;

		// DB Connection 정보를 수정한다.
		try {
			result = dbConnService.updateDbConnInfo(dbConnVO);
		} catch (Exception e) {
			logger.error("dbConnService.updateDbConnInfo error = " + e);
		}

		// DB Connection 정보를 조회한다.
		DbConnVO dbConnInfo = null;
		try {
			dbConnVO.setUilang((String) session.getAttribute("NEO_UILANG"));
			dbConnInfo = dbConnService.getDbConnInfo(dbConnVO);
			dbConnInfo.setLoginPwd(EncryptUtil.getJasyptDecryptedString(properties.getProperty("JASYPT.ALGORITHM"),
			properties.getProperty("JASYPT.KEYSTRING"), dbConnInfo.getLoginPwd()));
			dbConnInfo.setRegDt(StringUtil.getFDate(dbConnInfo.getRegDt(), Code.DT_FMT2));
			dbConnInfo.setUpDt(StringUtil.getFDate(dbConnInfo.getUpDt(), Code.DT_FMT2));
		} catch (Exception e) {
			logger.error("dbConnService.getDbConnInfo error = " + e);
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");
			map.put("dbConnInfo", dbConnInfo);
		} else {
			map.put("result", "Fail");
		}

		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		return modelAndView;
	}

	/**
	 * DB 연결 정보를 삭제한다
	 * 
	 * @param deptVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/dbConnDelete")
	public ModelAndView deleteDbConnInfo(@ModelAttribute DbConnVO dbConnVO, Model model, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		logger.debug("deleteDbConnInfo DbConnNos      = " + dbConnVO.getDbConnNos());

		String[] dbConnNo = dbConnVO.getDbConnNos().split(",");

		dbConnVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		dbConnVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		dbConnVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		dbConnVO.setStatus("002");

		int result = 0;

		for (int i = 0; i < dbConnNo.length; i++) {
			dbConnVO.setDbConnNo(Integer.parseInt(dbConnNo[i]));
			try {
				result = dbConnService.deleteDbConnInfo(dbConnVO);
			} catch (Exception e) {
				logger.error("dbConnService.deleteDbConnInfo error = " + e);
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
	 * DB Connection 연결을 테스트 한다.
	 * 
	 * @param dbConnVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/dbConnTest")
	public ModelAndView testDbConn(@ModelAttribute DbConnVO dbConnVO, Model model, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		int result = 0;

		logger.debug("testDbConn dbDriver = " + dbConnVO.getDbDriver());
		logger.debug("testDbConn dbUrl = " + dbConnVO.getDbUrl());
		logger.debug("testDbConn loginId = " + dbConnVO.getLoginId());
		logger.debug("testDbConn loginPwd = " + dbConnVO.getLoginPwd());

		// DB 접속 테스트
		DBUtil dbUtil = new DBUtil();
		Connection conn = null;
		String errMsg = "";
		try {
			conn = dbUtil.getConnection(dbConnVO.getDbDriver(), dbConnVO.getDbUrl(), dbConnVO.getLoginId(),
					dbConnVO.getLoginPwd());
			result++;
		} catch (Exception e) {
			logger.error("dbUtil.getConnection error = " + e);
			errMsg = e.toString();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
				}
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");
		} else {
			map.put("result", "Fail");
			map.put("errMsg", errMsg);
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		return modelAndView;
	}
	
	// #############################################################  데이터베이스 사용 권한   ############################################################# //
	/**
	 * DB 연결 권한 조회 화면
	 * 
	 * @param dbConnVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/dbConnAuthListP")
	public String goDbConnAuthListP(@ModelAttribute DbConnVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goDbConnAuthListP getDbConnNo = " + searchVO.getDbConnNo());

		// DB Connection 정보를 조회한다.
		DbConnVO dbConnInfo = null;
		try {
			searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
			dbConnInfo = dbConnService.getDbConnInfo(searchVO);
		} catch (Exception e) {
			logger.error("dbConnService.getDbConnInfo error = " + e);
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
		model.addAttribute("orgList", orgList); // 부서(최상위)항목

		// 코드그룹목록(코드성) 조회 --  사용자 그룹 
		CodeVO dept = new CodeVO();
		dept.setUilang(searchVO.getUilang());
		dept.setStatus("000");
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(dept);
		} catch(Exception e) {
			logger.error("codeService.getDeptList[DEPT]error = " + e);
		}
		
		// 현재 등록된 권한자 조회
		DbConnPermVO connPerm = new DbConnPermVO();
		connPerm.setDbConnNo(searchVO.getDbConnNo());
		connPerm.setUilang((String) session.getAttribute("NEO_UILANG"));
		List<DbConnPermVO> dbAuthUserList = null;
		try {
			dbAuthUserList = dbConnService.getDbConnPermList(connPerm);
		} catch (Exception e) {
			logger.error("dbConnService.getDbConnPermList error = " + e);
		}
		
		// 현재 등록된 그룹 권한자 조회
		DbConnGrpPermVO connGrpPerm = new DbConnGrpPermVO();
		connGrpPerm.setDbConnNo(searchVO.getDbConnNo());
		connGrpPerm.setUilang((String) session.getAttribute("NEO_UILANG"));
		List<DbConnGrpPermVO> dbAuthDeptList = null;
		try {
			dbAuthDeptList = dbConnService.getDbConnGrpPermList(connGrpPerm);
		} catch (Exception e) {
			logger.error("dbConnService.getDbConnGrpPermList error = " + e);
		}
		
		// model에 코드 목록 추가
		model.addAttribute("searchInfo", searchVO); // 리스트 페이지 검색정보
		model.addAttribute("dbConnInfo", dbConnInfo); // DB Connetion 정보
		model.addAttribute("orgList", orgList); // 조직도
		model.addAttribute("deptList", deptList); // 조직도
		model.addAttribute("dbAuthDeptList", dbAuthDeptList); // 조직도
		model.addAttribute("dbAuthUserList", dbAuthUserList); // 현재 권한 있는 사람들
		return "sys/dbc/dbConnAuthListP";
	}

	/**
	 * DB 연결 사용자 조회
	 * 
	 * @param dbConnVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/dbConnUserList")
	public ModelAndView goDbConnUserList(@ModelAttribute DbConnUserVO dbConnUserVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goDbConnUserList getDbConnNo = " + dbConnUserVO.getDbConnNo());
		logger.debug("goDbConnUserList getUserNm = " + dbConnUserVO.getUserNm());

		dbConnUserVO.setUilang("000");
		// 사용자 목록 조회
		List<DbConnUserVO> dbConnUserList = null;
		try {
			dbConnUserList = dbConnService.getDbConnUserList(dbConnUserVO);
		} catch (Exception e) {
			logger.error("dbConnService.getDbConnUserList error = " + e);
		}
 
		model.addAttribute("dbConnUserList", dbConnUserList);// 실제테이블 목록
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbConnUserList", dbConnUserList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}
	
	/**
	 * DB 연결 그룹  조회
	 * 
	 * @param dbConnVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/dbConnDeptList")
	public ModelAndView goDbConnDeptList(@ModelAttribute DbConnUserVO dbConnUserVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goDbConnUserList getDbConnNo = " + dbConnUserVO.getDbConnNo());
		logger.debug("goDbConnUserList getDeptNm   = " + dbConnUserVO.getDeptNm());

		dbConnUserVO.setUilang("000");
		
		// 사용자 목록 조회
		List<DbConnUserVO> dbConnDeptList = null;
		try {
			dbConnDeptList = dbConnService.getDbConnDeptList(dbConnUserVO);
		} catch (Exception e) {
			logger.error("dbConnService.getDbConnDeptList error = " + e);
		}
 
		model.addAttribute("dbConnDeptList", dbConnDeptList);// 실제테이블 목록
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbConnUserList", dbConnDeptList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}
	
	/**
	 * DB 사용 권한 정보를 저장한다.
	 * 
	 * @param dbConnPermVO
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/dbConnAuthUpdate")
	public ModelAndView updateDbConnAuth(@ModelAttribute DbConnPermVO dbConnPermVO, Model model, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("updateDbConnAuth dbConnNo = " + dbConnPermVO.getDbConnNo());
		logger.debug("updateDbConnAuth getUserIds = " + dbConnPermVO.getUserIds());
		logger.debug("updateDbConnAuth getDeptNos = " + dbConnPermVO.getDeptNos());

		String[] dbConnAuthUserIds = dbConnPermVO.getUserIds().split(",");
		String[] dbConnAuthDeptNos = dbConnPermVO.getDeptNos().split(",");
		
		int result = 0;
		int dbConnNo = dbConnPermVO.getDbConnNo();
 
		try {
			result = dbConnService.deleteDbConnPermInfo(dbConnNo);
			
			if (result > -1) {
				for (int i = 0; i < dbConnAuthUserIds.length; i++) {
					if(! "".equals(dbConnAuthUserIds[i])){
						dbConnPermVO.setUserId(dbConnAuthUserIds[i]);
						try {
							if (result > -1) {
								result = dbConnService.insertDbConnPermInfo(dbConnPermVO);
							}
						} catch (Exception e) {
							logger.error("dbConnService.insertDbConnPermInfo error = " + e);
						}
					}
				}
			}
			
			result = dbConnService.deleteDbConnGrpPermInfo(dbConnNo);
			
			if (result > -1) {
				for (int i = 0; i < dbConnAuthDeptNos.length; i++) {
					if(! "".equals(dbConnAuthDeptNos[i])){
						DbConnGrpPermVO dbConnGrpPermVO = new DbConnGrpPermVO();
						dbConnGrpPermVO.setDbConnNo(dbConnNo);
						// Integer.parseInt(from);
						dbConnGrpPermVO.setDeptNo(Integer.parseInt(dbConnAuthDeptNos[i]));
						
						try {
							result = dbConnService.insertDbConnGrpPermInfo(dbConnGrpPermVO);
						} catch (Exception e) {
							logger.error("dbConnService.insertDbConnGrpPermInfo error = " + e);
						}	
					}
				}
			}
		}catch (Exception e) {
			logger.error("dbConnService.updateDbConnAuth error = " + e);
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

	// #############################################################  메타 테이블  정보 처리  ############################################################# //
	/**
	 * 메타 테이블 정보를 조회한다.
	 * 
	 * @param dbConnVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/dbConnMetaP")
	public String getDbConnMeta(@ModelAttribute DbConnVO searchVO, Model model, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		logger.debug("getDbConnMetaMain dbConnNo = " + searchVO.getDbConnNo());

		try {
			searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
			searchVO = dbConnService.getDbConnInfo(searchVO);
		} catch (Exception e) {
			logger.error("dbConnService.getDbConnMeta error = " + e);
		}

		// DB Connection 정보를 조회한다.
		DbConnVO dbConnInfo = null;
		try {
			searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
			dbConnInfo = dbConnService.getDbConnInfo(searchVO);
		} catch (Exception e) {
			logger.error("dbConnService.getDbConnInfo error = " + e);
		}

		// 실제 DB 테이블 목록 조회
		List<String> realTableList = null;
		DBUtil dbUtil = new DBUtil();
		String dbTy = dbConnInfo.getDbTy();
		String dbDriver = dbConnInfo.getDbDriver();
		String dbUrl = dbConnInfo.getDbUrl();
		String loginId = dbConnInfo.getLoginId();
		String loginPwd = EncryptUtil.getJasyptDecryptedString(properties.getProperty("JASYPT.ALGORITHM"),
		properties.getProperty("JASYPT.KEYSTRING"), dbConnInfo.getLoginPwd());
		realTableList = dbUtil.getRealTableList(dbTy, dbDriver, dbUrl, loginId, loginPwd);

		// 메타 테이블 목록 조회
		List<MetaTableVO> useMetaTableList = null;
		try {
			useMetaTableList = dbConnService.getMetaTableList(searchVO);
		} catch (Exception e) {
			logger.error("dbConnService.getMetaTableList error = " + e);
		}

		// real table 과 metaTable join
		List<MetaTableVO> metaTableList = new ArrayList<MetaTableVO>();

		String realTableName = "";

		if (realTableList.size() > 0) {
			for (int i = 0; i < realTableList.size(); i++) {
				MetaTableVO metaTable = new MetaTableVO();
				try {
					metaTable.setTblNm(realTableList.get(i));
					metaTable.setDbConnNo(searchVO.getDbConnNo());
					metaTableList.add(metaTable);
				} catch (Exception e) {
					logger.error("dbConnService.getMetaTableList (make real-use table list info step1) error = " + e);
				}
			}

			if (useMetaTableList.size() > 0) {
				for (int i = 0; i < metaTableList.size(); i++) {
					realTableName = metaTableList.get(i).getTblNm();
					for (int j = 0; j < useMetaTableList.size(); j++) {
						logger.info("useMetaTableList : " + useMetaTableList.get(j).getTblNm());

						if (realTableName.equals(useMetaTableList.get(j).getTblNm())) {
							try {
								metaTableList.get(i).setTblAlias(useMetaTableList.get(j).getTblAlias());
								metaTableList.get(i).setTblDesc(useMetaTableList.get(j).getTblDesc());
								metaTableList.get(i).setTblNo(useMetaTableList.get(j).getTblNo());
							} catch (Exception e) {
								logger.error("dbConnService.getMetaTableList (make real-use table list info step2) error = " + e);
							}
						}

					}
				}
			}
		}

		model.addAttribute("searchInfo", searchVO); // 리스트 페이지 검색정보
		model.addAttribute("dbConnInfo", dbConnInfo); // DB Connetion 정보
		model.addAttribute("metaTableList", metaTableList); // 기 등록 메타테이블 정보

		return "sys/dbc/dbConnMetaP";
	}
 
	/**
	 * 메타 테이블 정보를 등록한다.(등록후 테이블 번호를 조회함)
	 * 
	 * @param metaTableVO
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/metatableAdd")
	public ModelAndView insertMetaTableInfo(@ModelAttribute MetaTableVO metaTableVO, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		logger.debug("insertMetaTableInfo dbConnNo = " + metaTableVO.getDbConnNo());
		logger.debug("insertMetaTableInfo tblNm = " + metaTableVO.getTblNm());
		logger.debug("insertMetaTableInfo tblAlias = " + metaTableVO.getTblAlias());
		logger.debug("insertMetaTableInfo tblDesc = " + metaTableVO.getTblDesc());
		
		if (metaTableVO.getTblAlias() == null || "".equals(metaTableVO.getTblAlias().trim())) {
			metaTableVO.setTblAlias(metaTableVO.getTblNm());
		}
		
		// 메타 테이블 정보 저장
		boolean result = false;
		int tblNo = 0;
		try {
			tblNo = dbConnService.insertMetaTableInfo(metaTableVO);
			result = true;
		} catch (Exception e) {
			logger.error("dbConnService.insertMetaTableInfo error = " + e);
		}

		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result) {
			map.put("result", "Success");
			map.put("tblNo", tblNo);
			map.put("tblNm", metaTableVO.getTblNm());
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}

	/**
	 * 메타 테이블 정보 수정
	 * 
	 * @param metaTableVO
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/metatableUpdate")
	public ModelAndView updateMetaTableInfo(@ModelAttribute MetaTableVO metaTableVO, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		logger.debug("updateMetaTableInfo dbConnNo = " + metaTableVO.getDbConnNo());
		logger.debug("updateMetaTableInfo tblNo = " + metaTableVO.getTblNo());
		logger.debug("updateMetaTableInfo tblNm = " + metaTableVO.getTblNm());
		logger.debug("updateMetaTableInfo tblAlias = " + metaTableVO.getTblAlias());
		logger.debug("updateMetaTableInfo tblDesc = " + metaTableVO.getTblDesc());

		if (metaTableVO.getTblAlias() == null || "".equals(metaTableVO.getTblAlias().trim())) {
			metaTableVO.setTblAlias(metaTableVO.getTblNm());
		}
		// 메타 테이블 정보 저장
		int result = 0;
		try {
			result = dbConnService.updateMetaTableInfo(metaTableVO);
		} catch (Exception e) {
			logger.error("dbConnService.updateMetaTableInfo error = " + e);
		}

		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");
			map.put("tblNo", metaTableVO.getTblNo());
			map.put("tblNm", metaTableVO.getTblNm());
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}

	/**
	 * 메타 테이블 정보 삭제
	 * 
	 * @param metaTableVO
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/metatableDelete")
	public ModelAndView deleteMetaTableInfo(@ModelAttribute MetaTableVO metaTableVO, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		logger.debug("deleteMetaTableInfo tblNo = " + metaTableVO.getTblNo());
		logger.debug("deleteMetaTableInfo tblNo = " + metaTableVO.getTblNm());

		// 메타 테이블 삭제(관계식 삭제 -> 관계값 삭제 -> 메타컬럼 삭제 -> 메타테이블 삭제)
		int result = 0;
		try {
			result = dbConnService.deleteMetaTableInfo(metaTableVO);
		} catch (Exception e) {
			logger.error("dbConnService.deleteMetaTableInfo error = " + e);
		}

		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");
			map.put("tblNo", "0");
			map.put("tblNm", metaTableVO.getTblNm());
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}

	/**
	 * 메타 컬럼 정보 조회
	 * 
	 * @param metaTableVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/metaTableInfo")
	public ModelAndView getMetaTableInfo(@ModelAttribute MetaTableVO metaTableVO, Model model,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getMetaTableInfo dbConnNo = " + metaTableVO.getDbConnNo());
		logger.debug("getMetaTableInfo tblNm = " + metaTableVO.getTblNm());
		
		int result = 0;
		MetaTableVO metaTable = new MetaTableVO();
		try {
			metaTableVO.setTblNo(0);
			metaTable = dbConnService.getMetaTableInfo(metaTableVO);
			if (metaTable != null && !"".equals(metaTable.getTblNm())) {
				result = 1;
			}
		} catch (Exception e) {
			logger.error("dbConnService.getMetaTableInfo error = " + e);
			result = -1;
		}

		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		map.put("metaTable", metaTable);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		return modelAndView;
	}

	/**
	 * 메타 컬럼 목록 조회
	 * 
	 * @param metaTableVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/metacolumnList")
	public String getMetaColumnList(@ModelAttribute MetaTableVO metaTableVO, Model model, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		logger.debug("getMetaColumnList dbConnNo = " + metaTableVO.getDbConnNo());
		logger.debug("getMetaColumnList tblNo = " + metaTableVO.getTblNo());
		logger.debug("getMetaColumnList tblNm = " + metaTableVO.getTblNm());

		DbConnVO dbConnInfo = null;
		try {
			DbConnVO dbConnVO = new DbConnVO();
			dbConnVO.setDbConnNo(metaTableVO.getDbConnNo());
			dbConnVO.setUilang((String) session.getAttribute("NEO_UILANG"));
			dbConnInfo = dbConnService.getDbConnInfo(dbConnVO);
		} catch (Exception e) {
			logger.error("dbConnService.getDbConnInfo error = " + e);
		}
		
		if(metaTableVO.getTblNm() == null || "".equals(metaTableVO.getTblNm())){
			try {
				MetaTableVO tmpMetaTableVO = new MetaTableVO(); 
				tmpMetaTableVO = dbConnService.getMetaTableInfo(metaTableVO);
				metaTableVO.setTblNm(tmpMetaTableVO.getTblNm());
				logger.debug("getMetaColumnList tblNm = " + metaTableVO.getTblNm());
			} catch (Exception e) {
				logger.error("dbConnService.getDbConnInfo error = " + e);
			}
		}		

		// 실제 DB 테이블 컬럼 목록 조회
		List<MetaColumnVO> realColumnList = null;
		DBUtil dbUtil = new DBUtil();
		String dbTy = dbConnInfo.getDbTy();
		String dbDriver = dbConnInfo.getDbDriver();
		String dbUrl = dbConnInfo.getDbUrl();
		String loginId = dbConnInfo.getLoginId();
		String loginPwd = EncryptUtil.getJasyptDecryptedString(properties.getProperty("JASYPT.ALGORITHM"),
		properties.getProperty("JASYPT.KEYSTRING"), dbConnInfo.getLoginPwd());
		realColumnList = dbUtil.getRealColumnList(dbTy, dbDriver, dbUrl, loginId, loginPwd, metaTableVO.getTblNm());

		List<MetaColumnVO> useColumnList = null;
		MetaColumnVO columnVO = new MetaColumnVO();
		columnVO.setTblNo(metaTableVO.getTblNo());
		try {
			useColumnList = dbConnService.getMetaColumnList(columnVO);
		} catch (Exception e) {
			logger.error("dbConnService.getMetaColumnList error = " + e);
		}

		// real table 과 metaTable join
		List<MetaColumnVO> metaColumnList = new ArrayList<MetaColumnVO>();

		String realColumnName = "";

		if (realColumnList.size() > 0) {
			for (int i = 0; i < realColumnList.size(); i++) {
				MetaColumnVO metaColumn = new MetaColumnVO();
				try {
					metaColumn.setColNo(0);
					metaColumn.setColNm(realColumnList.get(i).getColNm());
					metaColumn.setColDataTy(realColumnList.get(i).getColDataTy());
					metaColumnList.add(metaColumn);
				} catch (Exception e) {
					logger.error("dbConnService.getMetaColumnList (make real-use column list info step1) error = " + e);
				}
			}

			if (useColumnList.size() > 0) {
				for (int i = 0; i < metaColumnList.size(); i++) {
					realColumnName = metaColumnList.get(i).getColNm();
					for (int j = 0; j < useColumnList.size(); j++) { 
						if (realColumnName.equals(useColumnList.get(j).getColNm())) {
							try {
								metaColumnList.get(i).setColNo(useColumnList.get(j).getColNo());
								metaColumnList.get(i).setColAlias(useColumnList.get(j).getColAlias());
								metaColumnList.get(i).setColDesc(useColumnList.get(j).getColDesc());
								metaColumnList.get(i).setColHiddenYn(useColumnList.get(j).getColHiddenYn());
								metaColumnList.get(i).setColEncrDecrYn(useColumnList.get(j).getColEncrDecrYn());
							} catch (Exception e) {
								logger.error("dbConnService.getMetaColumnList (make real-use column list info step2) error = " + e);
							}
						}
					}
				}
			}
		}

		model.addAttribute("metaColumnList", metaColumnList); // 메타컬럼 리스트
		return "sys/dbc/dbConnMeta";
	}

	/**
	 * 메타 컬럼 정보 등록 및 수정
	 * 
	 * @param metaColumnVO
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/metacolumnUpdate")
	public ModelAndView updateMetaColumnInfo(@ModelAttribute MetaColumnVO metaColumnVO, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		logger.debug("updateMetaColumnInfo tblNo = " + metaColumnVO.getTblNo());
		logger.debug("updateMetaColumnInfo colNo = " + metaColumnVO.getColNo());
		logger.debug("updateMetaColumnInfo colNm = " + metaColumnVO.getColNm());
		
		if (metaColumnVO.getColAlias() == null || "".equals(metaColumnVO.getColAlias())) {
			metaColumnVO.setColAlias(metaColumnVO.getColNm());
		}

		int result = 0;
		try {
			if (metaColumnVO.getColNo() == 0) {
				result = dbConnService.insertMetaColumnInfo(metaColumnVO);
				result = dbConnService.getMetaColumnNo(metaColumnVO);
			} else {
				result = dbConnService.updateMetaColumnInfo(metaColumnVO);
				result = metaColumnVO.getColNo(); 

			}
		} catch (Exception e) {
			if (metaColumnVO.getColNo() == 0) {
				logger.error("dbConnService.insertMetaColumnInfo error = " + e);
			} else {
				logger.error("dbConnService.updateMetaColumnInfo error = " + e);
			}
		}

		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");
			map.put("colNo", result);
		} else {
			map.put("result", "Fail");
			map.put("colNo", 0);
		}
		
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}

	/**
	 * 메타 컬럼 정보 삭제(관계식, 관계값, 컬럼정보)
	 * 
	 * @param metaColumnVO
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/metacolumnDelete")
	public ModelAndView deleteMetaColumnInfo(@ModelAttribute MetaColumnVO metaColumnVO, Model model,HttpServletRequest request, HttpServletResponse response) {
		logger.debug("deleteMetaColumnInfo colNo = " + metaColumnVO.getColNo());
		logger.debug("deleteMetaColumnInfo tblNo = " + metaColumnVO.getTblNo());

		int result = 0;
		try {
			result = dbConnService.deleteMetaColumnInfo(metaColumnVO.getColNo());
		} catch (Exception e) {
			logger.error("dbConnService.deleteMetaColumnInfo error = " + e);
		}

		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");
			map.put("colNo", 0);
		} else {
			map.put("result", "Fail");
			map.put("colNo", 0);
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		return modelAndView;
	}

	/**
	 * 메타 관계식, 관계값 화면 출력
	 * 
	 * @param metaOperVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/metaOperList")
	public ModelAndView getMetaOperationListP(@ModelAttribute MetaOperatorVO metaOperVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getMetaOperationListP colNo = " + metaOperVO.getColNo());
		logger.debug("getMetaOperationListP tblNo = " + metaOperVO.getTblNo());

		// 관계식코드 목록을 조회한다.
		CodeVO codeVO = new CodeVO();
		codeVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		codeVO.setCdGrp("C037"); // 관계식
		codeVO.setUseYn("Y");
		List<CodeVO> operCodeList = null;
		try {
			operCodeList = codeService.getCodeList(codeVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error[C037] = " + e);
		}

		List<MetaOperatorVO> operatorList = null;
		metaOperVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		try {
			operatorList = dbConnService.getMetaOperatorList(metaOperVO);
		} catch (Exception e) {
			logger.error("dbConnService.getMetaOperatorList error = " + e);
		}
		
		//codeList와 oper 정보 join 
		List<MetaOperatorVO> metaOperatorList = new ArrayList<MetaOperatorVO>();
		
		if (operCodeList.size() > 0) {
			for (int i = 0; i < operCodeList.size(); i++) {
				MetaOperatorVO operVO = new MetaOperatorVO();
				try {
					operVO.setCdDtl(operCodeList.get(i).getCdDtl());
					operVO.setColNm(operCodeList.get(i).getCdNm());
					operVO.setOperCd(operCodeList.get(i).getCd());
					metaOperatorList.add(operVO);
				} catch (Exception e) {
					logger.error("dbConnService.getMetaColumnList (make code-oper  list info step1) error = " + e);
				}
			}
			
			String operCd ="";
			if (operatorList.size() > 0) {
				for (int i = 0; i < metaOperatorList.size(); i++) {
					operCd = metaOperatorList.get(i).getOperCd();
					for (int j = 0; j < operatorList.size(); j++) {
						if (operCd.equals(operatorList.get(j).getOperCd())) {
							try {
								if (operatorList.get(j).getOperNo() == 0) {
									operatorList.get(j).setOperNo(0);
								}
								metaOperatorList.get(i).setOperNo(operatorList.get(j).getOperNo());
								metaOperatorList.get(i).setColNo(operatorList.get(j).getColNo());
								metaOperatorList.get(i).setOperAlias(operatorList.get(j).getOperAlias());
								metaOperatorList.get(i).setOperNm(operatorList.get(j).getOperNm()); 
							} catch (Exception e) {
								logger.error("dbConnService.getMetaColumnList (make code-oper column list info step2) error = " + e);
							}
						}
						
					}
				}
			}
		}
		
		List<MetaValueVO> metaValueList = null;
		MetaValueVO valueVO = new MetaValueVO();
		valueVO.setColNo(metaOperVO.getColNo());
		try {
			metaValueList = dbConnService.getMetaValueList(valueVO);
		} catch (Exception e) {
			logger.error("dbConnService.getMetaValueList error = " + e);
		}

		MetaColumnVO columVO = new MetaColumnVO();
		MetaColumnVO metaColumn = new MetaColumnVO();
		columVO.setColNo(metaOperVO.getColNo());
		columVO.setTblNo(metaOperVO.getTblNo());
		try {
			metaColumn = dbConnService.getMetaColumnInfo(columVO);
		} catch (Exception e) {
			logger.error("dbConnService.getMetaColumnInfo error = " + e);
		}

		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("metaColumn", metaColumn);		
		map.put("metaOperatorList", metaOperatorList);
		map.put("metaValueList", metaValueList);
 
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;

	}

	/**
	 * 메타 관계식, 관계값 화면 출력
	 * 
	 * @param metaOperVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/metaValueList")
	public String getMetaValueList(@ModelAttribute MetaOperatorVO metaOperVO, Model model, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		logger.debug("getMetaOperationListP colNo = " + metaOperVO.getColNo());
		logger.debug("getMetaOperationListP colNm = " + metaOperVO.getColNm());

		List<MetaValueVO> metaValueList = null;
		MetaValueVO valueVO = new MetaValueVO();
		valueVO.setColNo(metaOperVO.getColNo());
		try {
			metaValueList = dbConnService.getMetaValueList(valueVO);
			//if (metaValueList.size() == 0) {
			//	metaValueList.add(valueVO);
			//}
		} catch (Exception e) {
			logger.error("dbConnService.getMetaValueList error = " + e);
		}

		model.addAttribute("metaValueList", metaValueList); // 메타컬럼 리스트
		
		return "sys/dbc/dbConnMetaOperListP";
	}

	/**
	 * 메타 관계식 정보 수정
	 * 
	 * @param metaOperatorVO
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/metaoperUpdate")
	public ModelAndView updateMetaOperatorInfo(@ModelAttribute MetaOperatorVO metaOperatorVO, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		logger.debug("updateMetaOperatorInfo colNo = " + metaOperatorVO.getColNo());
		logger.debug("updateMetaOperatorInfo operCd = " + metaOperatorVO.getOperCd());

		int result = 0;
		try {
			result = dbConnService.updateMetaOperatorInfo(metaOperatorVO);
		} catch (Exception e) {
			logger.error("dbConnService.updateMetaOperatorInfo error = " + e);
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
	 * 메타 관계값 정보 등록
	 * 
	 * @param metaValueVO
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/metavalAdd")
	public ModelAndView insertMetaValueInfo(@ModelAttribute MetaValueVO metaValueVO, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		logger.debug("insertMetaValueInfo colNo = " + metaValueVO.getColNo());
		logger.debug("insertMetaValueInfo valueNm = " + metaValueVO.getValueNm());
		logger.debug("insertMetaValueInfo valueAlias = " + metaValueVO.getValueAlias());

		int result = 0;
		try {
			result = dbConnService.insertMetaValueInfo(metaValueVO);
		} catch (Exception e) {
			logger.error("dbConnService.insertMetaValueInfo error = " + e);
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
	 * 메타 관계값 정보 수정
	 * 
	 * @param metaValueVO
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/metavalUpdate")
	public ModelAndView updateMetaValueInfo(@ModelAttribute MetaValueVO metaValueVO, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		logger.debug("updateMetaValueInfo colNo = " + metaValueVO.getColNo());
		logger.debug("updateMetaValueInfo valueNm = " + metaValueVO.getValueNm());
		logger.debug("updateMetaValueInfo valueAlias = " + metaValueVO.getValueAlias());

		int result = 0;
		try {
			result = dbConnService.updateMetaValueInfo(metaValueVO);
		} catch (Exception e) {
			logger.error("dbConnService.updateMetaValueInfo error = " + e);
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
	 * 메타 관계값 정보 삭제
	 * 
	 * @param metaValueVO
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/metavalDelete")
	public ModelAndView deleteMetaValueInfo(@ModelAttribute MetaValueVO metaValueVO, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		logger.debug("metavalDelete valueNo = " + metaValueVO.getValueNo());

		int result = 0;
		try {
			result = dbConnService.deleteMetaValueInfo(metaValueVO);
		} catch (Exception e) {
			logger.error("dbConnService.deleteMetaValueInfo error = " + e);
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
	 * 메타 관계값 목록 조회(JSON)
	 * 
	 * @param metaValueVO
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/metavalList")
	public ModelAndView getMetaValueList(@ModelAttribute MetaValueVO metaValueVO, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getMetaValueList colNo = " + metaValueVO.getColNo());

		List<MetaValueVO> metaValueList = null;
		try {
			metaValueList = dbConnService.getMetaValueList(metaValueVO);
		} catch (Exception e) {
			logger.error("dbConnService.getMetaValueList error = " + e);
		}

		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("metaValueList", metaValueList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}
  
	// #############################################################  메타 조인  정보 처리  ############################################################# //	
	/**
	 * 메타 조인 화면 출력
	 * 
	 * @param metaJoinVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/dbConnMetaJoinP")
	public String getMetaJoinListP(@ModelAttribute MetaJoinVO metaJoinVO, Model model, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		logger.debug("getMetaJoinListP dbConnNo = " + metaJoinVO.getDbConnNo());

		// DB Connection 정보를 조회한다.
		DbConnVO dbConnInfo = null;
		try {
			DbConnVO searchVO = new DbConnVO();
			searchVO.setDbConnNo(metaJoinVO.getDbConnNo());
			searchVO.setUilang((String) session.getAttribute("NEO_UILANG"));
			dbConnInfo = dbConnService.getDbConnInfo(searchVO);
		} catch (Exception e) {
			logger.error("dbConnService.getDbConnInfo error = " + e);
		}
		
		// 조인유형코드 목록을 조회한다.
		CodeVO joinTyVO = new CodeVO();
		joinTyVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		joinTyVO.setCdGrp("C040"); // 조인유형코드
		joinTyVO.setUseYn("Y");
		List<CodeVO> joinTyList = null;
		try {
			joinTyList = codeService.getCodeList(joinTyVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error[C040] = " + e);
		}

		// 관계유형코드 목록을 조회한다.
		CodeVO relTyVO = new CodeVO();
		relTyVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		relTyVO.setCdGrp("C041"); // 관계유형코드
		relTyVO.setUseYn("Y");
		List<CodeVO> relTyList = null;
		try {
			relTyList = codeService.getCodeList(relTyVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error[C041] = " + e);
		}

		// 페이지 설정
		int page = StringUtil.setNullToInt(metaJoinVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(metaJoinVO.getRows(),
				Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		metaJoinVO.setPage(1);
		metaJoinVO.setRows(100);


		// 메타 테이블 목록 조회
		List<MetaTableVO> metaTableList = null;
		DbConnVO connVO = new DbConnVO();
		connVO.setDbConnNo(metaJoinVO.getDbConnNo());
		try {
			metaTableList = dbConnService.getMetaTableList(connVO);
		} catch (Exception e) {
			logger.error("dbConnService.getMetaTableList error = " + e);
		}

		model.addAttribute("dbConnInfo", dbConnInfo);
		model.addAttribute("joinTyList", joinTyList);
		model.addAttribute("relTyList", relTyList);
		model.addAttribute("metaTableList", metaTableList);

		return "sys/dbc/dbConnMetaJoinP";
	}

	/**
	 * 메타 조인 화면 출력
	 * 
	 * @param metaJoinVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/dbConnMetaJoinList")
	public String getMetaJoinList(@ModelAttribute MetaJoinVO metaJoinVO, Model model, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		logger.debug("getMetaJoinListP dbConnNo = " + metaJoinVO.getDbConnNo());

		// 조인유형코드 목록을 조회한다.
		CodeVO joinTyVO = new CodeVO();
		joinTyVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		joinTyVO.setCdGrp("C040"); // 조인유형코드
		joinTyVO.setUseYn("Y");
		List<CodeVO> joinTyList = null;
		try {
			joinTyList = codeService.getCodeList(joinTyVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error[C040] = " + e);
		}

		// 관계유형코드 목록을 조회한다.
		CodeVO relTyVO = new CodeVO();
		relTyVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		relTyVO.setCdGrp("C041"); // 관계유형코드
		relTyVO.setUseYn("Y");
		List<CodeVO> relTyList = null;
		try {
			relTyList = codeService.getCodeList(relTyVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error[C041] = " + e);
		}
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(metaJoinVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(metaJoinVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		metaJoinVO.setPage(1);
		metaJoinVO.setRows(100);
		metaJoinVO.setStartRow((metaJoinVO.getPage()-1)*metaJoinVO.getRows());

		// 메타 조인 목록 조회
		List<MetaJoinVO> metaJoinList = null;
		metaJoinVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		try {
			metaJoinList = dbConnService.getMetaJoinList(metaJoinVO);
		} catch (Exception e) {
			logger.error("dbConnService.getMetaJoinList error = " + e);
		}
 
		model.addAttribute("metaJoinList", metaJoinList);
		model.addAttribute("joinTyList", joinTyList);
		model.addAttribute("relTyList", relTyList);

		return "sys/dbc/dbConnMetaJoinList";
	}

	
	/**
	 * 메타 컬럼 목록 조회
	 * 
	 * @param metaJoinVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getMetaJoinColumnList")
	public ModelAndView getMetaJoinColumnList(@ModelAttribute MetaJoinVO metaJoinVO, Model model, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		logger.debug("getColumnList dbConnNo = " + metaJoinVO.getDbConnNo());
		logger.debug("getColumnList tblNo = " + metaJoinVO.getTblNo());
 
		List<MetaColumnVO> metaColumnList = null;
		MetaColumnVO columnVO = new MetaColumnVO(); 
		columnVO.setTblNo(metaJoinVO.getTblNo());
		try {
			metaColumnList = dbConnService.getMetaColumnList(columnVO);
		} catch (Exception e) {
			logger.error("dbConnService.getMetaColumnList error = " + e);
		}
 
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("metaColumnList", metaColumnList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}

	/**
	 * 메타 조인 정보 등록
	 * 
	 * @param metaJoinVO
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/metajoinAdd")
	public ModelAndView insertMetaJoinInfo(@ModelAttribute MetaJoinVO metaJoinVO, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		logger.debug("insertMetaJoinInfo dbConnNo = " + metaJoinVO.getDbConnNo());
		logger.debug("insertMetaJoinInfo mstTblNm = " + metaJoinVO.getMstTblNm());
		logger.debug("insertMetaJoinInfo forTblNm = " + metaJoinVO.getForTblNm());
		logger.debug("insertMetaJoinInfo joinTy = " + metaJoinVO.getJoinTy());
		logger.debug("insertMetaJoinInfo relTy = " + metaJoinVO.getRelTy());

		int result = 0;
		try {
			result = dbConnService.insertMetaJoinInfo(metaJoinVO);
		} catch (Exception e) {
			logger.error("dbConnService.insertMetaJoinInfo error = " + e);
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
	 * 메타 조인 정보 수정
	 * 
	 * @param metaJoinVO
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/metajoinUpdate")
	public ModelAndView updateMetaJoinInfo(@ModelAttribute MetaJoinVO metaJoinVO, Model model,
			HttpServletRequest request, HttpServletResponse response) { 
		logger.debug("updateMetaJoinInfo joinNo = " + metaJoinVO.getJoinNo()); 
		logger.debug("updateMetaJoinInfo joinTy = " + metaJoinVO.getJoinTy());
		logger.debug("updateMetaJoinInfo relTy = " + metaJoinVO.getRelTy());

		int result = 0;
		try {
			result = dbConnService.updateMetaJoinInfo(metaJoinVO);
		} catch (Exception e) {
			logger.error("dbConnService.updateMetaJoinInfo error = " + e);
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
	 * 메타 조인 정보 삭제
	 * 
	 * @param metaJoinVO
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/metajoinDelete")
	public ModelAndView deleteMetaJoinInfo(@ModelAttribute MetaJoinVO metaJoinVO, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		logger.debug("deleteMetaJoinInfo joinNo = " + metaJoinVO.getJoinNo());

		int result = 0;
		try {
			result = dbConnService.deleteMetaJoinInfo(metaJoinVO);
		} catch (Exception e) {
			logger.error("dbConnService.deleteMetaJoinInfo error = " + e);
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
