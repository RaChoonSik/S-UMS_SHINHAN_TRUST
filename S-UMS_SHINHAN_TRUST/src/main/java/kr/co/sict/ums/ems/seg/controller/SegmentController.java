/**
 * 작성자 : 김준희
 * 작성일시 : 2021.07.17
 * 설명 : 수신자그룹 관리 Controller
 */
package kr.co.sict.ums.ems.seg.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.co.sict.ums.com.service.CodeService;
import kr.co.sict.ums.com.service.CryptoService;
import kr.co.sict.ums.com.vo.CodeVO;
import kr.co.sict.ums.ems.cam.service.CampaignService;
import kr.co.sict.ums.ems.cam.vo.CampaignVO;
import kr.co.sict.ums.ems.cam.vo.TaskVO;
import kr.co.sict.ums.ems.seg.service.SegmentService;
import kr.co.sict.ums.ems.seg.vo.SegmentMemberVO;
import kr.co.sict.ums.ems.seg.vo.SegmentVO;
import kr.co.sict.ums.sys.dbc.service.DBConnService;
import kr.co.sict.ums.sys.dbc.vo.DbConnVO;
import kr.co.sict.ums.sys.dbc.vo.MetaColumnVO;
import kr.co.sict.ums.sys.dbc.vo.MetaJoinVO;
import kr.co.sict.ums.sys.dbc.vo.MetaOperatorVO;
import kr.co.sict.ums.sys.dbc.vo.MetaTableVO;
import kr.co.sict.ums.sys.log.service.SystemLogService;
import kr.co.sict.ums.sys.log.vo.ActionLogVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.DBUtil;
import kr.co.sict.util.EncryptUtil;
import kr.co.sict.util.MaskingUtil;
import kr.co.sict.util.PageUtil;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Controller
@RequestMapping("/ems/seg")
public class SegmentController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private PropertiesUtil properties;
	
	@Autowired
	private CryptoService cryptoService;
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private DBConnService dbConnService;
	
	@Autowired
	private SegmentService segmentService;
	
	@Autowired
	private CampaignService campaignService;
	
	@Autowired
	private SystemLogService systemService;
	
	/**
	 * 수신자그룹 목록 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/segMainP")
	public String goSegMain(@ModelAttribute SegmentVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSegMain searchSegNm = " + searchVO.getSearchSegNm());
		logger.debug("goSegMain searchCreateTy = " + searchVO.getSearchCreateTy());
		logger.debug("goSegMain searchStatus = " + searchVO.getSearchStatus());
		logger.debug("goSegMain searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goSegMain searchEndDt = " + searchVO.getSearchEndDt());
		logger.debug("goSegMain searchDeptNo = " + searchVO.getSearchDeptNo());
		logger.debug("goSegMain searchUserId = " + searchVO.getSearchUserId());
		
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
		
		// 세그먼트 생성 유형 코드 조회
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
		
		// 부서목록(코드성) 조회
		CodeVO deptVO = new CodeVO();
		deptVO.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(deptVO);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 사용자 목록 조회
		CodeVO userVO = new CodeVO();
		userVO.setDeptNo(searchVO.getSearchDeptNo());
		userVO.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch(Exception e) {
			logger.error("codeService.getUserList error = " + e);
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
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("createTyList", createTyList);	// 세그먼트 생성 유형
		model.addAttribute("statusList", statusList);		// 수신자그룹상태
		model.addAttribute("deptList", deptList);			// 부서번호
		model.addAttribute("userList", userList);			// 사용자
		model.addAttribute("reasonList", reasonList);		// 조회사유코드
		
		return "ems/seg/segMainP";
	}
	
	/**
	 * 수신자그룹 목록 조회
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/segList")
	public String goSegList(@ModelAttribute SegmentVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("segList searchSegNm = " + searchVO.getSearchSegNm());
		logger.debug("segList searchCreateTy = " + searchVO.getSearchCreateTy());
		logger.debug("segList searchStatus = " + searchVO.getSearchStatus());
		logger.debug("segList searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("segList searchEndDt = " + searchVO.getSearchEndDt());
		logger.debug("segList searchDeptNo = " + searchVO.getSearchDeptNo());
		logger.debug("segList searchUserId = " + searchVO.getSearchUserId());
		
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
				
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), (int)session.getAttribute("NEO_PER_PAGE"));
		//int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		//int rows = StringUtil.setNullToInt((int)session.getAttribute("NEO_PER_PAGE"), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		
		searchVO.setPage(page);
		searchVO.setRows(rows);
		int totalCount = 0;
		
		// 수신자그룹 목록 조회
		SegmentVO search = searchVO;
		search.setServiceGb(10);
		search.setUilang((String)session.getAttribute("NEO_UILANG"));
		search.setSearchStartDt(search.getSearchStartDt().replaceAll("\\.",""));
		search.setSearchEndDt(search.getSearchEndDt().replaceAll("\\.", ""));
		List<SegmentVO> segmentList = null;
		try {
			segmentList = segmentService.getSegmentList(search);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentList error = " + e);
		}
		
		// 코드그룹목록(코드성) 조회 -- 개인별페이지
		CodeVO perPage = new CodeVO();
		perPage.setUilang(searchVO.getUilang());
		perPage.setCdGrp("C132"); //여기에선 C126 => 
		perPage.setUseYn("Y");
		List<CodeVO> perPageList = null;
		try {
			perPageList = codeService.getCodeList(perPage);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[126] error = " + e);
		}
		
		if(segmentList != null && segmentList.size() > 0) {
			totalCount = segmentList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);

		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("segmentList", segmentList);		// 수신자그룹 목록
		model.addAttribute("pageUtil", pageUtil);			// 페이징
		model.addAttribute("uploadPath", properties.getProperty("FILE.UPLOAD_PATH"));	// 업로드경로
		model.addAttribute("perPageList", perPageList);			//개인별페이지
		
		return "ems/seg/segList";
	}
	
	/**
	 * 수신자그룹 파일연동 등록화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/segFileAddP")
	public String goSegFileAdd(@ModelAttribute SegmentVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSegFileAdd searchSegNm    = " + searchVO.getSearchSegNm());
		logger.debug("goSegFileAdd searchCreateTy = " + searchVO.getSearchCreateTy());
		logger.debug("goSegFileAdd searchStatus   = " + searchVO.getSearchStatus());
		logger.debug("goSegFileAdd searchStartDt  = " + searchVO.getSearchStartDt());
		logger.debug("goSegFileAdd searchEndDt    = " + searchVO.getSearchEndDt());
		logger.debug("goSegFileAdd searchDeptNo   = " + searchVO.getSearchDeptNo());
		logger.debug("goSegFileAdd searchUserId   = " + searchVO.getSearchUserId());
		
		// 부서목록(코드성) 조회
		CodeVO deptVO = new CodeVO();
		deptVO.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(deptVO);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
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
		
		model.addAttribute("searchVO", searchVO);		// 검색 항목
		model.addAttribute("createTy", "003");			// 생성 유형
		model.addAttribute("deptList", deptList);		// 부서 목록
		model.addAttribute("reasonList", reasonList);	// 고객정보 조회사유코드
		model.addAttribute("uploadPath", properties.getProperty("FILE.UPLOAD_PATH"));	// 업로드경로
		
		return "ems/seg/segFileAddP";
	}
	
	/**
	 * 샘플파일 다운로드 팝업 화면을 출력한다.
	 * @return
	 */
	@RequestMapping(value="/fileSampleDownPop")
	public String goFileSampleDown() {
		return "ems/seg/fileSampleDownPop";
	}
	
	/**
	 * 수신자그룹 파일연동 수정화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/segFileUpdateP")
	public String goSegFileUpdate(@ModelAttribute SegmentVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSegFileUpdate segNo          = " + searchVO.getSegNo());
		logger.debug("goSegFileUpdate searchSegNm    = " + searchVO.getSearchSegNm());
		logger.debug("goSegFileUpdate searchCreateTy = " + searchVO.getSearchCreateTy());
		logger.debug("goSegFileUpdate searchStatus   = " + searchVO.getSearchStatus());
		logger.debug("goSegFileUpdate searchStartDt  = " + searchVO.getSearchStartDt());
		logger.debug("goSegFileUpdate searchEndDt    = " + searchVO.getSearchEndDt());
		logger.debug("goSegFileUpdate searchDeptNo   = " + searchVO.getSearchDeptNo());
		logger.debug("goSegFileUpdate searchUserId   = " + searchVO.getSearchUserId());
		
		// 수신자그룹 정보 조회
		SegmentVO segmentInfo = null;
		try {
			searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
			segmentInfo = segmentService.getSegmentInfo(searchVO);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentInfo error = " + e);
		}

		// 부서목록(코드성) 조회
		CodeVO deptVO = new CodeVO();
		deptVO.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(deptVO);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 사용자목록(코드성) 조회
		CodeVO userVO = new CodeVO();
		userVO.setStatus("000");
		userVO.setDeptNo(segmentInfo.getDeptNo());
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch(Exception e) {
			logger.error("codeService.getUserList erro = " + e);
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
		
		model.addAttribute("searchVO", searchVO);			// 검색 항목
		model.addAttribute("createTy", "003");				// 생성 유형
		model.addAttribute("segmentInfo", segmentInfo);		// 수신자그룹 정보
		model.addAttribute("deptList", deptList);			// 부서 목록
		model.addAttribute("userList", userList);			// 사용자 목록
		model.addAttribute("reasonList", reasonList);		// 고객정보 조회사유코드
		
		return "ems/seg/segFileUpdateP";
	}
	
	/**
	 * 파일의 내용을 화면에 출력한다.
	 * @param segmentVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/segFileMemberListP")
	public String goSegFileMemberList(@ModelAttribute SegmentVO segmentVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws JspException, IOException {
		logger.debug("goSegFileMemberList segFlPath = " + segmentVO.getSegFlPath());
		logger.debug("goSegFileMemberList separatorChar = " + segmentVO.getSeparatorChar());
		logger.debug("goSegFileMemberList page = " + segmentVO.getPage());
		logger.debug("goSegFileMemberList page = " + segmentVO.getPage());
		logger.debug("goSegFileMemberList checkSearchReason = " + segmentVO.getCheckSearchReason());
		logger.debug("goSegFileMemberList contentPath = " + segmentVO.getContentPath());
		
		// 설정파일 읽기
		String realPath = request.getServletContext().getRealPath("/");
		Properties prop = new Properties();
		try (FileInputStream fileInputStream = new FileInputStream(realPath + "/WEB-INF/config/properties/ums.crypto.properties");) {
			prop.load(fileInputStream);
		} catch(Exception e) {
			logger.error("goSegFileMemberList properties load error!!");
		}
		

		String SAFEDATA_MASK_YN = properties.getProperty("SAFEDATA_MASK_YN").trim(); 
		String maskResult = "";
		
		//int pageRow = Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_POP"));
		int pageRow = StringUtil.setNullToInt((int)session.getAttribute("NEO_PER_PAGE"), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_POP")));
		if(segmentVO.getPage() == 0) segmentVO.setPage(1);
		
		String result = "";
		int totCount = 0;	// 회원총수
		int aliasCnt = 0;	// 알리아스수
		String mergeKey = "";
		
		List<HashMap<String,String>> memList = new ArrayList<HashMap<String,String>>();
		List<String> memAlias = new ArrayList<String>();
		
		BufferedReader line = null;
		String tempStr = "";
		String alias = ""; 
		String filePath = "" ;
		try {
			String tmpFlPath = properties.getProperty("FILE.UPLOAD_PATH") + "/" + segmentVO.getSegFlPath();
			filePath = tmpFlPath; 
			line = new BufferedReader(new InputStreamReader(new FileInputStream(tmpFlPath), "UTF-8"));
			//line = new BufferedReader(new InputStreamReader(new FileInputStream(tmpFlPath), "EUC-KR"));
			while((tempStr = line.readLine()) != null) {
				if("".equals(tempStr.trim())) continue;
				StringTokenizer st = new StringTokenizer(tempStr, segmentVO.getSeparatorChar());
				
				if(totCount == 0) {	// 첫줄의 알리아스를 읽어 셋팅한다.
					while(st.hasMoreTokens()) {
						memAlias.add(st.nextToken());
						aliasCnt++;
					}
				} else {
					if(tempStr == null || "".equals(tempStr)) break;
					if(totCount > (segmentVO.getPage()-1)*pageRow && totCount <= segmentVO.getPage() * pageRow) { // 페이지별로 size만큼만 저장.
						HashMap<String, String> unitInfo = new HashMap<String,String>();
						for(int cnt = 0; cnt < aliasCnt; cnt++) {
							alias = (String)memAlias.get(cnt);
							String decStr = cryptoService.getDecrypt(alias, st.nextToken());
							//마스킹
							if("YES".equals(SAFEDATA_MASK_YN)) { 
								String MASK_COLUM = properties.getProperty("MASK_COLUM").trim();
								String[] maskCol = MASK_COLUM.split("\\;");
								maskResult = MaskingUtil.getMasking(alias, decStr, maskCol);
							} else {
								maskResult = decStr;
							}
							unitInfo.put(alias,maskResult);
						}
						memList.add(unitInfo);
					}
				}
				totCount++;
			}
			totCount = totCount - 1;	// 첫라인은 알리아스이기때문에 한줄을 빼준다.
			result = "Success";
			
			if("false".equals(segmentVO.getCheckSearchReason())) {
				ActionLogVO actionLog = new ActionLogVO();
				actionLog.setStatusGb("Success");
				actionLog.setContentType("005");
				actionLog.setContent((String)session.getAttribute("NEO_MENU_ID"));
				actionLog.setContentPath(segmentVO.getContentPath());
				actionLog.setMessage(segmentVO.getSearchReasonCd());
				actionLog.setExtrYn("Y");
				actionLog.setRecCnt(totCount);
				actionLog.setMobilYn("N");
				try {
					systemService.insertActionLog(request, session, actionLog);
				} catch(Exception e) {
					logger.error("systemService.insertActionLog error = " + e);
				}
			}
		} catch(FileNotFoundException fe) {
			memAlias.clear();
			memList.clear();
			
			HashMap<String,String> unitInfo = new HashMap<String,String>();
			memAlias.add("Error");
			
			if("000".equals(segmentVO.getUilang())){
				unitInfo.put("Error",  "해당 경로(" + filePath + ")에 파일이 존재 하지 않습니다 관리자에게 문의 해주세요");
			} else { 
				unitInfo.put("Error",  "The file does not exist in that path(" + filePath + ").Please contact the administrator");
			}
			
			memList.add(unitInfo); 
            result = "Fail";
		} catch(Exception e) {
			memAlias.clear();
			memList.clear();
			
			memAlias.add("LINE");
			memAlias.add("Error");
			
			HashMap<String,String> unitInfo = new HashMap<String,String>();
			unitInfo.put("Line", Integer.toString(totCount));
			unitInfo.put("Error", tempStr);
			memList.add(unitInfo);
			
            result = "Fail";
		} finally {
			if(line != null) try {line.close(); } catch(Exception e) {}
		}
		
		if(memAlias != null && memAlias.size() > 0) {
			for(int i=0;i<memAlias.size();i++) {
				if(i == 0) {
					mergeKey += (String)memAlias.get(i);
				} else {
					mergeKey += "," + (String)memAlias.get(i);
				}
			}
		}
		
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, segmentVO.getPage(), totCount, pageRow);
		pageUtil.setSubmitFunc("goPageNumSeg");
		
		model.addAttribute("result", result);
		model.addAttribute("totCount", totCount);
		model.addAttribute("memList", memList);
		model.addAttribute("memAlias", memAlias);
		model.addAttribute("mergeKey", mergeKey);
		model.addAttribute("pageUtil", pageUtil);
		
		return "ems/seg/segFileMemberListP";
	}
	
	/**
	 * 수신자그룹 정보를 등록한다.
	 * @param segmentVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/segAdd")
	public ModelAndView insertSegmentInfo(@ModelAttribute SegmentVO segmentVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("insertSegmentInfo userId        = " + segmentVO.getUserId());
		logger.debug("insertSegmentInfo dbConnNo      = " + segmentVO.getDbConnNo());
		logger.debug("insertSegmentInfo segNm         = " + segmentVO.getSegNm());
		logger.debug("insertSegmentInfo createTy      = " + segmentVO.getCreateTy());
		logger.debug("insertSegmentInfo mergeKey      = " + segmentVO.getMergeKey());
		logger.debug("insertSegmentInfo mergeCol      = " + segmentVO.getMergeCol());
		logger.debug("insertSegmentInfo segFlPath     = " + segmentVO.getSegFlPath());
		logger.debug("insertSegmentInfo srcWhere      = " + segmentVO.getSrcWhere());
		logger.debug("insertSegmentInfo totCnt        = " + segmentVO.getTotCnt());
		logger.debug("insertSegmentInfo selectSql     = " + segmentVO.getSelectSql());
		logger.debug("insertSegmentInfo fromSql       = " + segmentVO.getFromSql());
		logger.debug("insertSegmentInfo whereSql      = " + segmentVO.getWhereSql());
		logger.debug("insertSegmentInfo orderbySql    = " + segmentVO.getOrderbySql());
		logger.debug("insertSegmentInfo query         = " + segmentVO.getQuery());
		logger.debug("insertSegmentInfo retryQuery    = " + segmentVO.getRetryQuery());
		logger.debug("insertSegmentInfo realQuery     = " + segmentVO.getRealQuery());
		logger.debug("insertSegmentInfo separatorChar = " + segmentVO.getSeparatorChar());
		
		int result = 0;
		
		if(StringUtil.isNull(segmentVO.getUserId())) segmentVO.setUserId((String)session.getAttribute("NEO_USER_ID"));
		
		if(segmentVO.getDeptNo() == 0) {
			if(!"Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				 segmentVO.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		
		if(segmentVO.getQuery() != null) {
			segmentVO.setQuery(segmentVO.getQuery().trim());
			if(!"".equals(segmentVO.getQuery())){
				segmentVO.setQuery(StringUtil.removeSpecialChar(segmentVO.getQuery(), ";"));
			}
		}
		
		if(segmentVO.getRetryQuery() != null) {
			segmentVO.setRetryQuery(segmentVO.getRetryQuery().trim());
			if(!"".equals(segmentVO.getRetryQuery())){
				segmentVO.setRetryQuery(StringUtil.removeSpecialChar(segmentVO.getRetryQuery(), ";"));
			}
		}
		
		if(segmentVO.getRealQuery() != null) {
			segmentVO.setRealQuery(segmentVO.getRealQuery().trim());
			if(!"".equals(segmentVO.getRealQuery())){
				segmentVO.setRealQuery(StringUtil.removeSpecialChar(segmentVO.getRealQuery(), ";"));
			}
		}
		
		segmentVO.setRegId((String)session.getAttribute("NEO_USER_ID"));
		segmentVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		segmentVO.setStatus("000");
		if(segmentVO.getServiceGb() == 0) segmentVO.setServiceGb(10);
		
		if(!"003".equals(segmentVO.getCreateTy()) && !"002".equals(segmentVO.getCreateTy())) {
			String query = "";
	    	
	    	query = " SELECT " + segmentVO.getSelectSql();
	    	query += " FROM " + segmentVO.getFromSql();
	    	if(!"".equals(segmentVO.getWhereSql())) {
	    		query += " WHERE " + segmentVO.getWhereSql();
	    	}
	    	segmentVO.setQuery(query);
		}
		
		try {
			result = segmentService.insertSegmentInfo(segmentVO);
		} catch(Exception e) {
			logger.error("segmentService.insertSegmentInfo Error = " + e);
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
	 * 수신자그룹 정보를 수정한다.
	 * @param segmentVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/segUpdate")
	public ModelAndView updateSegmentInfo(@ModelAttribute SegmentVO segmentVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateSegmentInfo userId        = " + segmentVO.getUserId());
		logger.debug("updateSegmentInfo dbConnNo      = " + segmentVO.getDbConnNo());
		logger.debug("updateSegmentInfo segNm         = " + segmentVO.getSegNm());
		logger.debug("updateSegmentInfo createTy      = " + segmentVO.getCreateTy());
		logger.debug("updateSegmentInfo mergeKey      = " + segmentVO.getMergeKey());
		logger.debug("updateSegmentInfo mergeCol      = " + segmentVO.getMergeCol());
		logger.debug("updateSegmentInfo segFlPath     = " + segmentVO.getSegFlPath());
		logger.debug("updateSegmentInfo srcWhere      = " + segmentVO.getSrcWhere());
		logger.debug("updateSegmentInfo totCnt        = " + segmentVO.getTotCnt());
		logger.debug("updateSegmentInfo selectSql     = " + segmentVO.getSelectSql());
		logger.debug("updateSegmentInfo fromSql       = " + segmentVO.getFromSql());
		logger.debug("updateSegmentInfo whereSql      = " + segmentVO.getWhereSql());
		logger.debug("updateSegmentInfo orderbySql    = " + segmentVO.getOrderbySql());
		logger.debug("updateSegmentInfo query         = " + segmentVO.getQuery());
		logger.debug("updateSegmentInfo retryQuery    = " + segmentVO.getRetryQuery());
		logger.debug("updateSegmentInfo realQuery     = " + segmentVO.getRealQuery());
		logger.debug("updateSegmentInfo separatorChar = " + segmentVO.getSeparatorChar());
		
		int result = 0;
		
		if(StringUtil.isNull(segmentVO.getUserId())) segmentVO.setUserId((String)session.getAttribute("NEO_USER_ID"));
		
		if(segmentVO.getDeptNo() == 0) {
			if(!"Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				 segmentVO.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}

		if(segmentVO.getQuery() != null) {
			segmentVO.setQuery(segmentVO.getQuery().trim());
			if(!"".equals(segmentVO.getQuery())){
				segmentVO.setQuery(StringUtil.removeSpecialChar(segmentVO.getQuery(), ";"));
			}
		}
		
		if(segmentVO.getRetryQuery() != null) {
			segmentVO.setRetryQuery(segmentVO.getRetryQuery().trim());
			if(!"".equals(segmentVO.getRetryQuery())){
				segmentVO.setRetryQuery(StringUtil.removeSpecialChar(segmentVO.getRetryQuery(), ";"));
			}
		}
		
		if(segmentVO.getRealQuery() != null) {
			segmentVO.setRealQuery(segmentVO.getRealQuery().trim());
			if(!"".equals(segmentVO.getRealQuery())){
				segmentVO.setRealQuery(StringUtil.removeSpecialChar(segmentVO.getRealQuery(), ";"));
			}
		}
		
		if(StringUtil.isNull(segmentVO.getStatus())) segmentVO.setStatus("000");
		segmentVO.setUpId((String)session.getAttribute("NEO_USER_ID"));
		segmentVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		if(segmentVO.getServiceGb() == 0) segmentVO.setServiceGb(10);
		
		if(!"003".equals(segmentVO.getCreateTy()) && !"002".equals(segmentVO.getCreateTy())) {
			String query = "";
	    	
	    	query = " SELECT " + segmentVO.getSelectSql();
	    	query += " FROM " + segmentVO.getFromSql();
	    	if(!"".equals(segmentVO.getWhereSql())) {
	    		query += " WHERE " + segmentVO.getWhereSql();
	    	}
	    	segmentVO.setQuery(query);
		}
		
		try {
			result = segmentService.updateSegmentInfo(segmentVO);
		} catch(Exception e) {
			logger.error("segmentService.updateSegmentInfo Error = " + e);
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
	 * 수신자그룹 추출조건 등록화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/segToolAddP")
	public String goSegToolAdd(@ModelAttribute SegmentVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSegToolAdd searchSegNm = " + searchVO.getSearchSegNm());
		logger.debug("goSegToolAdd searchCreateTy = " + searchVO.getSearchCreateTy());
		logger.debug("goSegToolAdd searchStatus = " + searchVO.getSearchStatus());
		logger.debug("goSegToolAdd searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goSegToolAdd searchEndDt = " + searchVO.getSearchEndDt());
		logger.debug("goSegToolAdd searchDeptNo = " + searchVO.getSearchDeptNo());
		logger.debug("goSegToolAdd searchUserId = " + searchVO.getSearchUserId());
		logger.debug("goSegToolAdd createTy = " + searchVO.getCreateTy());
		String createTy =  searchVO.getCreateTy()==null||"".equals(searchVO.getCreateTy())?"000":searchVO.getCreateTy();
		
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
		
		
		// 부서목록(코드성) 조회
		CodeVO deptVO = new CodeVO();
		deptVO.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(deptVO);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
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
		
		int dbConnNo = 0;
		if(searchVO.getDbConnNo() == 0) {
			if("GET".equals(request.getMethod())) {
				if(dbConnList != null && dbConnList.size() > 0) {
					dbConnNo = ((DbConnVO)dbConnList.get(0)).getDbConnNo();
					searchVO.setDbConnNo(dbConnNo);
				}
			}
		} else {
			dbConnNo = searchVO.getDbConnNo();
		}

		// 메타 테이블 목록 조회
		List<MetaTableVO> metaTableList = null;
		DbConnVO metaDbConn = new DbConnVO();
		metaDbConn.setDbConnNo(dbConnNo);
		try {
			metaTableList = dbConnService.getMetaTableList(metaDbConn);
		} catch(Exception e) {
			logger.error("dbConnService.getMetaTableList error = " + e);
		}
		
		
		// 페이지 설정
		MetaJoinVO metaJoinVO = new MetaJoinVO();
		metaJoinVO.setPage(1);
		metaJoinVO.setRows(100);
		metaJoinVO.setDbConnNo(dbConnNo);
		metaJoinVO.setUilang((String)session.getAttribute("NEO_UILANG"));

		// 메타 조인 목록 조회
		List<MetaJoinVO> metaJoinList = null;
		try {
			metaJoinList = dbConnService.getMetaJoinList(metaJoinVO);
		} catch(Exception e) {
			logger.error("dbConnService.getMetaJoinList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색 항목
		model.addAttribute("createTy", createTy);			// 생성 유형
		model.addAttribute("dbConnList", dbConnList);		// DB연결 목록
		model.addAttribute("deptList", deptList);			// 부서 목록
		model.addAttribute("reasonList", reasonList);		// 고객정보 조회사유코드
		model.addAttribute("metaTableList", metaTableList);	// 메타테이블 목록
		model.addAttribute("metaJoinList", metaJoinList);	// 메타조인 목록
		
		return "ems/seg/segToolAddP";
	}
	
	/**
	 * 수신자그룹 추출조건 수정 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/segToolUpdateP")
	public String goSegToolUpdate(@ModelAttribute SegmentVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSegToolUpdate segNo = " + searchVO.getSegNo());
		logger.debug("goSegToolUpdate searchSegNm = " + searchVO.getSearchSegNm());
		logger.debug("goSegToolUpdate searchCreateTy = " + searchVO.getSearchCreateTy());
		logger.debug("goSegToolUpdate searchStatus = " + searchVO.getSearchStatus());
		logger.debug("goSegToolUpdate searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goSegToolUpdate searchEndDt = " + searchVO.getSearchEndDt());
		logger.debug("goSegToolUpdate searchDeptNo = " + searchVO.getSearchDeptNo());
		logger.debug("goSegToolUpdate searchUserId = " + searchVO.getSearchUserId());
		logger.debug("goSegToolUpdate createTy = " + searchVO.getCreateTy());
		String createTy =  searchVO.getCreateTy()==null||"".equals(searchVO.getCreateTy())?"000":searchVO.getCreateTy();
		
		// 수신자그룹 정보 조회
		SegmentVO segmentInfo = null;
		try {
			searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
			segmentInfo = segmentService.getSegmentInfo(searchVO);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentInfo error = " + e);
		}
		
		String srcWhere = segmentInfo.getSrcWhere();
		if(srcWhere != null && !"".equals(srcWhere)) {
			if(!srcWhere.substring(srcWhere.length()-2).equals("##")) {
				srcWhere += srcWhere + "##";
			}
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
		
		// 부서목록(코드성) 조회
		CodeVO deptVO = new CodeVO();
		deptVO.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(deptVO);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 사용자목록(코드성) 조회
		CodeVO userVO = new CodeVO();
		userVO.setStatus("000");
		userVO.setDeptNo(segmentInfo.getDeptNo());
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch(Exception e) {
			logger.error("codeService.getUserList erro = " + e);
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
		
		// 메타 테이블 목록 조회
		int dbConnNo = segmentInfo.getDbConnNo();
		List<MetaTableVO> metaTableList = null;
		DbConnVO metaDbConn = new DbConnVO();
		metaDbConn.setDbConnNo(dbConnNo);
		try {
			metaTableList = dbConnService.getMetaTableList(metaDbConn);
		} catch(Exception e) {
			logger.error("dbConnService.getMetaTableList error = " + e);
		}
		
		// 메타 조인 페이지 설정
		MetaJoinVO metaJoinVO = new MetaJoinVO();
		metaJoinVO.setPage(1);
		metaJoinVO.setRows(100);
		metaJoinVO.setDbConnNo(dbConnNo);
		metaJoinVO.setUilang((String)session.getAttribute("NEO_UILANG"));

		// 메타 조인 목록 조회
		List<MetaJoinVO> metaJoinList = null;
		try {
			metaJoinList = dbConnService.getMetaJoinList(metaJoinVO);
		} catch(Exception e) {
			logger.error("dbConnService.getMetaJoinList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색 항목
		model.addAttribute("createTy", createTy);			// 생성 유형
		model.addAttribute("dbConnList", dbConnList);		// DB연결 목록
		model.addAttribute("deptList", deptList);			// 부서 목록
		model.addAttribute("userList", userList);			// 사용자 목록
		model.addAttribute("reasonList", reasonList);		// 고객정보 조회사유코드
		model.addAttribute("metaTableList", metaTableList);	// 메타테이블 목록
		model.addAttribute("metaJoinList", metaJoinList);	// 메타조인 목록
		model.addAttribute("segmentInfo", segmentInfo);		// 수신자그룹 정보
		model.addAttribute("srcWhere", srcWhere);
		
		return "ems/seg/segToolUpdateP";
	}
	
	/**
	 * 추출도구 이용화면에서 메타 테이블 컨텐츠를 출력한다.
	 * @param columnVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/segMetaFrameP")
	public String goSegMetaFrame(@ModelAttribute MetaColumnVO columnVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSegMetaFrame dbConnNo = " + columnVO.getDbConnNo());
		logger.debug("goSegMetaFrame mergeCol = " + columnVO.getMergeCol());
		logger.debug("goSegMetaFrame tblNo    = " + columnVO.getTblNo());
		
		// 메타 테이블 목록 조회
		List<MetaTableVO> metaTableList = null;
		DbConnVO connVO = new DbConnVO();
		connVO.setDbConnNo( columnVO.getDbConnNo());
		try {
			metaTableList = dbConnService.getMetaTableList(connVO);
		} catch(Exception e) {
			logger.error("dbConnService.getMetaTableList error = " + e);
		}
		
		// 메타 컬럼 목록 조회
		List<MetaColumnVO> metaColumnList = null;
		try {
			metaColumnList = dbConnService.getMetaColumnList(columnVO);
		} catch(Exception e) {
			logger.error("dbConnService.getMetaColumnList error = " + e);
		}
		
		List<String> mergeCol = new ArrayList<String>();
		if(columnVO.getMergeCol() != null && !"".equals(columnVO.getMergeCol())) {
			StringTokenizer st = new StringTokenizer(columnVO.getMergeCol(),",");
			while(st.hasMoreElements()) mergeCol.add(st.nextToken());
		}

		model.addAttribute("metaTableList", metaTableList);
		model.addAttribute("metaColumnList", metaColumnList);
		model.addAttribute("mergeCol", mergeCol);
		
		return "ems/seg/segMetaFrameP";
	}
	
	/**
	 * 대상자 수 추출(DB로 조회)
	 * @param segmentVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/segCount")
	public ModelAndView getSegCount(@ModelAttribute SegmentVO segmentVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getSegCount dbConnNo      = " + segmentVO.getDbConnNo());
		logger.debug("getSegCount selectSql     = " + segmentVO.getSelectSql());
		logger.debug("getSegCount fromSql       = " + segmentVO.getFromSql());
		logger.debug("getSegCount whereSql      = " + segmentVO.getWhereSql());
		logger.debug("getSegCount query         = " + segmentVO.getQuery());
		logger.debug("getSegCount createTy      = " + segmentVO.getCreateTy());
		
		int totCnt = 0;
		
		// DB Connection 정보를 조회한다.
		DbConnVO dbConnInfo = null;
		try {
			DbConnVO searchVO = new DbConnVO();
			searchVO.setDbConnNo(segmentVO.getDbConnNo());
			searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
			dbConnInfo = dbConnService.getDbConnInfo(searchVO);
		} catch(Exception e) {
			logger.error("dbConnService.getDbConnInfo error = " + e);
		}
		
		// 대상자 수 조회
		if(dbConnInfo != null) {
			DBUtil dbUtil = new DBUtil();
			String dbDriver = dbConnInfo.getDbDriver();
			String dbUrl = dbConnInfo.getDbUrl();
			String loginId = dbConnInfo.getLoginId();
			//String loginPwd = EncryptUtil.getJasyptDecryptedString(properties.getProperty("JASYPT.ALGORITHM"), properties.getProperty("JASYPT.KEYSTRING"), dbConnInfo.getLoginPwd());
			String loginPwd = EncryptUtil.getJasyptDecryptedFixString(properties.getProperty("JASYPT.ALGORITHM"), properties.getProperty("JASYPT.KEYSTRING"), dbConnInfo.getLoginPwd());
			if(segmentVO.getDbConnNo() == 0) {
				String dataSourceName = "";
				
				String realPath = request.getServletContext().getRealPath("/");
				Properties prop = null;
				try (FileInputStream fileInputStream = new FileInputStream(realPath + "/WEB-INF/config/properties/db.properties");) {
					prop = new Properties();
					prop.load(fileInputStream);
					dataSourceName = prop.getProperty("Ems.DataSourceName");
				} catch(Exception e) {
					logger.error("DataSourceName Read Error!!");
				}
				totCnt = dbUtil.getSegmentCount(dataSourceName, segmentVO);
			} else {
				totCnt = dbUtil.getSegmentCount(dbDriver, dbUrl, loginId, loginPwd, segmentVO);
			}
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totCnt", totCnt);
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
	@RequestMapping(value="/segInfoP")
	public String goSegInfoPreview(@ModelAttribute SegmentVO segmentVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSegInfoPreview segNo         = " + segmentVO.getSegNo());
		logger.debug("goSegInfoPreview dbConnNo      = " + segmentVO.getDbConnNo());
		logger.debug("goSegInfoPreview selectSql     = " + segmentVO.getSelectSql());
		logger.debug("goSegInfoPreview fromSql       = " + segmentVO.getFromSql());
		logger.debug("goSegInfoPreview whereSql      = " + segmentVO.getWhereSql());
		logger.debug("goSegInfoPreview orderbySql    = " + segmentVO.getOrderbySql());
		logger.debug("goSegInfoPreview query         = " + segmentVO.getQuery());
		logger.debug("goSegInfoPreview createTy      = " + segmentVO.getCreateTy());
		logger.debug("goSegInfoPreview mergeKey      = " + segmentVO.getMergeKey());
		logger.debug("goSegInfoPreview mergeCol      = " + segmentVO.getMergeCol());

		if(segmentVO.getSegNo() != 0) {
			try {
				segmentVO.setUilang((String)session.getAttribute("NEO_UILANG"));
				segmentVO = segmentService.getSegmentInfo(segmentVO);
			} catch(Exception e) {
				logger.error("segmentService.getSegmentInfo error = " + e);
			}
		}
		
		model.addAttribute("segmentVO", segmentVO);
		model.addAttribute("uploadPath", properties.getProperty("FILE.UPLOAD_PATH"));	// 업로드경로
		
		return "ems/seg/segInfoP";
	}
	
	/**
	 * 수신자그룹 멤버 데이터 목록 화면을 출력한다.
	 * @param segmentVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/segDbMemberListP")
	public String getDbMemberList(@ModelAttribute SegmentVO segmentVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getDbMemberList segNo      = " + segmentVO.getSegNo());
		logger.debug("getDbMemberList dbConnNo   = " + segmentVO.getDbConnNo());
		logger.debug("getDbMemberList selectSql  = " + segmentVO.getSelectSql());
		logger.debug("getDbMemberList fromSql    = " + segmentVO.getFromSql());
		logger.debug("getDbMemberList whereSql   = " + segmentVO.getWhereSql());
		logger.debug("getDbMemberList orderbySql = " + segmentVO.getOrderbySql());
		logger.debug("getDbMemberList query      = " + segmentVO.getQuery());
		logger.debug("getDbMemberList createTy   = " + segmentVO.getCreateTy());
		logger.debug("getDbMemberList mergeKey   = " + segmentVO.getMergeKey());
		logger.debug("getDbMemberList mergeCol   = " + segmentVO.getMergeCol());
		logger.debug("getDbMemberList page       = " + segmentVO.getPage());
		logger.debug("getDbMemberList checkSearchReason = " + segmentVO.getCheckSearchReason());
		logger.debug("getDbMemberList contentPath = " + segmentVO.getContentPath());
		
		//int pageRow = Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_POP"));
		int pageRow = StringUtil.setNullToInt((int)session.getAttribute("NEO_PER_PAGE"), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_POP")));
		if(segmentVO.getPage() == 0) segmentVO.setPage(1);
		
		// DB Connection 정보를 조회한다.
		DbConnVO dbConnInfo = null;
		try {
			DbConnVO searchVO = new DbConnVO();
			searchVO.setDbConnNo(segmentVO.getDbConnNo());
			searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
			dbConnInfo = dbConnService.getDbConnInfo(searchVO);
		} catch(Exception e) {
			logger.error("dbConnService.getDbConnInfo error = " + e);
		}
		
		SegmentMemberVO memberVO = null;
		List<HashMap<String,String>> memberList = null;
		List<HashMap<String,String>> pageMemberList = new ArrayList<HashMap<String,String>>();
		// 대상자 수 조회
		if(dbConnInfo != null) {
			DBUtil dbUtil = new DBUtil();
			String dbDriver = dbConnInfo.getDbDriver();
			String dbUrl = dbConnInfo.getDbUrl();
			String loginId = dbConnInfo.getLoginId();
			//String loginPwd = EncryptUtil.getJasyptDecryptedString(properties.getProperty("JASYPT.ALGORITHM"), properties.getProperty("JASYPT.KEYSTRING"), dbConnInfo.getLoginPwd());
			String loginPwd = EncryptUtil.getJasyptDecryptedFixString(properties.getProperty("JASYPT.ALGORITHM"), properties.getProperty("JASYPT.KEYSTRING"), dbConnInfo.getLoginPwd());
			if(segmentVO.getDbConnNo() == 0) {
				String dataSourceName = "";
				String realPath = request.getServletContext().getRealPath("/");
				Properties prop = null;
				try (FileInputStream fileInputStream = new FileInputStream(realPath + "/WEB-INF/config/properties/db.properties");) {
					prop = new Properties();
					prop.load(fileInputStream);
					dataSourceName = prop.getProperty("Ems.DataSourceName");
				} catch(Exception e) {
					logger.error("DataSourceName Read Error!!");
				}
				
				memberVO = dbUtil.getMemberList(dataSourceName, segmentVO);
			} else {
				memberVO = dbUtil.getMemberList(dbDriver, dbUrl, loginId, loginPwd, segmentVO);
			}
		}
		
		
		int totalCount = 0;
		if(memberVO != null) {
			totalCount = memberVO.getTotalCount();
			memberList = memberVO.getMemberList();
			if(memberList != null && memberList.size() > 0) {
				for(int i=0;i<memberList.size();i++) {
					if(i >= (segmentVO.getPage()-1)*pageRow && i< segmentVO.getPage()*pageRow) { // 페이지별로 size만큼만 저장.
						HashMap<String,String> member = (HashMap<String,String>)memberList.get(i);
						pageMemberList.add(member);
					}
				}
			}
		}
		
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, segmentVO.getPage(), totalCount, pageRow);
		pageUtil.setSubmitFunc("goPageNumSeg");
		
		model.addAttribute("segmentVO", segmentVO);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("memberList", pageMemberList);
		model.addAttribute("pageUtil", pageUtil);
		
		if("false".equals(segmentVO.getCheckSearchReason())) {
			ActionLogVO actionLog = new ActionLogVO();
			actionLog.setStatusGb("Success");
			actionLog.setContentType("005");
			actionLog.setContent((String)session.getAttribute("NEO_MENU_ID"));
			actionLog.setContentPath(segmentVO.getContentPath());
			actionLog.setMessage(segmentVO.getSearchReasonCd());
			actionLog.setExtrYn("Y");
			actionLog.setRecCnt(totalCount);
			actionLog.setMobilYn("N");
			try {
				systemService.insertActionLog(request, session, actionLog);
			} catch(Exception e) {
				logger.error("systemService.insertActionLog error = " + e);
			}
		}
		
		return "ems/seg/segDbMemberListP";
	}
	
	/**
	 * 수신자그룹 상태 수정(복구, 사용중지, 삭제...)
	 * @param segmentVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/segDelete")
	public ModelAndView updateSegmentStatus(@ModelAttribute SegmentVO segmentVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateSegmentStatus segNo  = " + segmentVO.getSegNo());
		logger.debug("updateSegmentStatus segNos = " + segmentVO.getSegNos());
		logger.debug("updateSegmentStatus status = " + segmentVO.getStatus());
		
		int result = 0;
		
		segmentVO.setUpId((String)session.getAttribute("NEO_USER_ID"));
		segmentVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		try {
			result = segmentService.updateSegmentStatus(segmentVO);
		} catch(Exception e) {
			logger.error("segmentService.updateSegmentStatus Error = " + e);
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
	 * 수신자그룹 직접SQL이용 등록 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/segDirectSQLAddP")
	public String goSegDirectSQLAddP(@ModelAttribute SegmentVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSegDirectSQLAddP searchSegNm    = " + searchVO.getSearchSegNm());
		logger.debug("goSegDirectSQLAddP searchCreateTy = " + searchVO.getSearchCreateTy());
		logger.debug("goSegDirectSQLAddP searchStatus   = " + searchVO.getSearchStatus());
		logger.debug("goSegDirectSQLAddP searchStartDt  = " + searchVO.getSearchStartDt());
		logger.debug("goSegDirectSQLAddP searchEndDt    = " + searchVO.getSearchEndDt());
		logger.debug("goSegDirectSQLAddP searchDeptNo   = " + searchVO.getSearchDeptNo());
		logger.debug("goSegDirectSQLAddP searchUserId   = " + searchVO.getSearchUserId());
		logger.debug("goSegDirectSQLAddP createTy = " + searchVO.getCreateTy());
		String createTy =  searchVO.getCreateTy()==null||"".equals(searchVO.getCreateTy())?"002":searchVO.getCreateTy();
		
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
		
		// 부서목록(코드성) 조회
		CodeVO deptVO = new CodeVO();
		deptVO.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(deptVO);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
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
		
		int dbConnNo = 0;
		if(searchVO.getDbConnNo() == 0) {
			if("GET".equals(request.getMethod())) {
				if(dbConnList != null && dbConnList.size() > 0) {
					dbConnNo = ((DbConnVO)dbConnList.get(0)).getDbConnNo();
					searchVO.setDbConnNo(dbConnNo);
				}
			}
		} else {
			dbConnNo = searchVO.getDbConnNo();
		}

		// 메타 테이블 목록 조회
		List<MetaTableVO> metaTableList = null;
		DbConnVO metaDbConn = new DbConnVO();
		metaDbConn.setDbConnNo(dbConnNo);
		try {
			metaTableList = dbConnService.getMetaTableList(metaDbConn);
		} catch(Exception e) {
			logger.error("dbConnService.getMetaTableList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색 항목
		model.addAttribute("createTy", createTy);			// 생성 유형
		model.addAttribute("dbConnList", dbConnList);		// DB연결 목록
		model.addAttribute("deptList", deptList);			// 부서 목록
		model.addAttribute("reasonList", reasonList);		// 고객정보 조회사유코드
		model.addAttribute("metaTableList", metaTableList);	// 메타테이블 목록
		
		return "ems/seg/segDirectSQLAddP";
	}
	
	/**
	 * 수신자그룹 직접SQL이용 수정 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/segDirectSQLUpdateP")
	public String goSegDirectSQLUpdateP(@ModelAttribute SegmentVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSegDirectSQLUpdateP segNo = " + searchVO.getSegNo());
		logger.debug("goSegDirectSQLUpdateP searchSegNm    = " + searchVO.getSearchSegNm());
		logger.debug("goSegDirectSQLUpdateP searchCreateTy = " + searchVO.getSearchCreateTy());
		logger.debug("goSegDirectSQLUpdateP searchStatus   = " + searchVO.getSearchStatus());
		logger.debug("goSegDirectSQLUpdateP searchStartDt  = " + searchVO.getSearchStartDt());
		logger.debug("goSegDirectSQLUpdateP searchEndDt    = " + searchVO.getSearchEndDt());
		logger.debug("goSegDirectSQLUpdateP searchDeptNo   = " + searchVO.getSearchDeptNo());
		logger.debug("goSegDirectSQLUpdateP searchUserId   = " + searchVO.getSearchUserId());
		logger.debug("goSegDirectSQLUpdateP createTy       = " + searchVO.getCreateTy());
		String createTy =  searchVO.getCreateTy()==null||"".equals(searchVO.getCreateTy())?"002":searchVO.getCreateTy();
		
		// 발송대상(세그먼트) 정보 조회
		SegmentVO segmentInfo = null;
		try {
			searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
			segmentInfo = segmentService.getSegmentInfo(searchVO);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentInfo error = " + e);
		}
		
		String srcWhere = segmentInfo.getSrcWhere();
		if(srcWhere != null && !"".equals(srcWhere)) {
			if(!srcWhere.substring(srcWhere.length()-2).equals("##")) {
				srcWhere += srcWhere + "##";
			}
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
		
		// 부서목록(코드성) 조회
		CodeVO deptVO = new CodeVO();
		deptVO.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(deptVO);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 사용자목록(코드성) 조회
		CodeVO userVO = new CodeVO();
		userVO.setStatus("000");
		userVO.setDeptNo(segmentInfo.getDeptNo());
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch(Exception e) {
			logger.error("codeService.getUserList erro = " + e);
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

		
		int dbConnNo = segmentInfo.getDbConnNo();
		// 메타 테이블 목록 조회
		List<MetaTableVO> metaTableList = null;
		DbConnVO metaDbConn = new DbConnVO();
		metaDbConn.setDbConnNo(dbConnNo);
		try {
			metaTableList = dbConnService.getMetaTableList(metaDbConn);
		} catch(Exception e) {
			logger.error("dbConnService.getMetaTableList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색 항목
		model.addAttribute("createTy", createTy);			// 생성 유형
		model.addAttribute("dbConnList", dbConnList);		// DB연결 목록
		model.addAttribute("deptList", deptList);			// 부서 목록
		model.addAttribute("userList", userList);			// 사용자 목록
		model.addAttribute("reasonList", reasonList);		// 고객정보 조회사유코드
		model.addAttribute("metaTableList", metaTableList);	// 메타테이블 목록
		model.addAttribute("segmentInfo", segmentInfo);		// 수신자그룹 정보		
		
		return "ems/seg/segDirectSQLUpdateP";
	}
	
	
	/**
	 * 직접SQL이용 테이블 목록 조회
	 * @param dbConnVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/metatableListP")
	public String getMetaTableListP(@ModelAttribute DbConnVO dbConnVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getMetaTableListP dbConnNo = " + dbConnVO.getDbConnNo());
		
		DbConnVO dbConnInfo = null;
		try {
			dbConnVO.setUilang((String)session.getAttribute("NEO_UILANG"));
			dbConnInfo = dbConnService.getDbConnInfo(dbConnVO);
		} catch(Exception e) {
			logger.error("dbConnService.getDbConnInfo error = " + e);
		}
		
		// 실제 DB 테이블 목록 조회
		/*List<String> realTableList = null;
		DBUtil dbUtil = new DBUtil();
		String dbTy = dbConnInfo.getDbTy();
		String dbDriver = dbConnInfo.getDbDriver();
		String dbUrl = dbConnInfo.getDbUrl();
		String loginId = dbConnInfo.getLoginId();
		String loginPwd = EncryptUtil.getJasyptDecryptedString(properties.getProperty("JASYPT.ALGORITHM"), properties.getProperty("JASYPT.KEYSTRING"), dbConnInfo.getLoginPwd());
		realTableList = dbUtil.getRealTableList(dbTy, dbDriver, dbUrl, loginId, loginPwd);
		*/
		
		// 메타 테이블 목록 조회
		List<MetaTableVO> metaTableList = null;
		try {
			metaTableList = dbConnService.getMetaTableList(dbConnVO);
		} catch(Exception e) {
			logger.error("dbConnService.getMetaTableList error = " + e);
		}
		
		//model.addAttribute("realTableList", realTableList);	// 실제테이블 목록
		model.addAttribute("metaTableList", metaTableList);	// 메타테이블 목록
		
		return "ems/seg/metatableListP";
	}
	
	/**
	 * 직접SQL이용 컬럼 목록 조회
	 * @param metaColumnVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/metacolumnListP")
	public String getMetaColumnListP(@ModelAttribute MetaColumnVO metaColumnVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getMetaColumnListP dbConnNo = " + metaColumnVO.getDbConnNo());
		logger.debug("getMetaColumnListP tblNo    = " + metaColumnVO.getTblNo());
		
		DbConnVO dbConnInfo = new DbConnVO();
		try {
			dbConnInfo.setDbConnNo(metaColumnVO.getDbConnNo());
			dbConnInfo.setUilang((String)session.getAttribute("NEO_UILANG"));
			dbConnInfo = dbConnService.getDbConnInfo(dbConnInfo);
		} catch(Exception e) {
			logger.error("dbConnService.getDbConnInfo error = " + e);
		}
		
		// 실제 DB 테이블 컬럼 목록 조회
		/*List<MetaColumnVO> realColumnList = null;
		DBUtil dbUtil = new DBUtil();
		String dbTy = dbConnInfo.getDbTy();
		String dbDriver = dbConnInfo.getDbDriver();
		String dbUrl = dbConnInfo.getDbUrl();
		String loginId = dbConnInfo.getLoginId();
		String loginPwd = EncryptUtil.getJasyptDecryptedString(properties.getProperty("JASYPT.ALGORITHM"), properties.getProperty("JASYPT.KEYSTRING"), dbConnInfo.getLoginPwd());
		realColumnList = dbUtil.getRealColumnList(dbTy, dbDriver, dbUrl, loginId, loginPwd, metaColumnVO.getTblNm());
		*/
		
		// 메타 컬럼 목록 조회
		List<MetaColumnVO> metaColumnList = null;
		try {
			metaColumnList = dbConnService.getMetaColumnList(metaColumnVO);
		} catch(Exception e) {
			logger.error("dbConnService.getMetaColumnList error = " + e);
		}
		
		model.addAttribute("tableName", metaColumnVO.getTblNm());	// 테이블명
		//model.addAttribute("realColumnList", realColumnList);		// 실제컬럼 목록
		model.addAttribute("metaColumnList", metaColumnList);		// 메타컬럼 목록
		
		return "ems/seg/metacolumnListP";
	}
	
	/**
	 * 메타 컬럼 목록 조회
	 * @param segmentVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/getMetaColumnList")
	public ModelAndView getMetaColumnList(@ModelAttribute MetaTableVO metaTableVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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
 
		List<MetaColumnVO> metaColumnList = null;
		MetaColumnVO columnVO = new MetaColumnVO();
		columnVO.setTblNo(metaTableVO.getTblNo());
		try {
			metaColumnList = dbConnService.getMetaColumnList(columnVO);
		} catch (Exception e) {
			logger.error("dbConnService.getMetaColumnList error = " + e);
		}
 
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put("result", "Success");
		map.put("metaColumnList", metaColumnList);
	 
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
 
		List<MetaOperatorVO> metaOperatorList = null;
		metaOperVO.setUilang((String) session.getAttribute("NEO_UILANG"));
		try {
			metaOperatorList = dbConnService.getMetaOperatorList(metaOperVO);
		} catch (Exception e) {
			logger.error("dbConnService.getMetaOperatorList error = " + e);
		}
	 
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("metaOperatorList", metaOperatorList);
 
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;

	}
	
	/**
	 * SQL TES 실행
	 * @param segmentVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/segDirectSQLTest")
	public ModelAndView goSegDirectSQLTest(@ModelAttribute SegmentVO segmentVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSegDirectSQLTest dbConnNo    = " + segmentVO.getDbConnNo());
		logger.debug("goSegDirectSQLTest query       = " + segmentVO.getQuery());
		logger.debug("goSegDirectSQLTest retryQuery  = " + segmentVO.getRetryQuery());
		logger.debug("goSegDirectSQLTest realQuery   = " + segmentVO.getRealQuery());
		logger.debug("goSegDirectSQLTest testType    = " + segmentVO.getTestType());
	
		DbConnVO dbConnInfo = new DbConnVO();
		try {
			dbConnInfo.setDbConnNo(segmentVO.getDbConnNo());
			dbConnInfo.setUilang((String)session.getAttribute("NEO_UILANG"));
			dbConnInfo = dbConnService.getDbConnInfo(dbConnInfo);
		} catch(Exception e) {
			logger.error("dbConnService.getDbConnInfo error = " + e);
		}
		
		if(segmentVO.getQuery() != null) {
			segmentVO.setQuery(segmentVO.getQuery().trim());
			if(!"".equals(segmentVO.getQuery())){
				segmentVO.setQuery(StringUtil.removeSpecialChar(segmentVO.getQuery(), ";"));
			}
		} 
		
		if(segmentVO.getRetryQuery() != null) {
			segmentVO.setRetryQuery(segmentVO.getRetryQuery().trim());
			if(!"".equals(segmentVO.getRetryQuery())){
				segmentVO.setRetryQuery(StringUtil.removeSpecialChar(segmentVO.getRetryQuery(), ";"));
			}
			
			if(!"".equals(segmentVO.getRetryQuery())){
				segmentVO.setRetryQuery(StringUtil.repalcePatternChar(segmentVO.getRetryQuery()));
			}
			logger.error("goSegDirectSQLTest getRetryQuery  = " + segmentVO.getRetryQuery() );
		}
		
		if(segmentVO.getRealQuery() != null) {
			segmentVO.setRealQuery(segmentVO.getRealQuery().trim());
			if(!"".equals(segmentVO.getRealQuery())){
				segmentVO.setRealQuery(StringUtil.removeSpecialChar(segmentVO.getRealQuery(), ";"));
			}
			
			if(!"".equals(segmentVO.getRealQuery())){
				segmentVO.setRealQuery(StringUtil.repalcePatternChar(segmentVO.getRealQuery()));
			}
			logger.error("goSegDirectSQLTest getRealQuery  = " + segmentVO.getRealQuery() );
		}
		
		// 실제 DB 테이블 컬럼 목록 조회
		SegmentMemberVO memberVO = null;
		SegmentMemberVO memberRetryVO = null;
		SegmentMemberVO memberRealVO = null;
		
		DBUtil dbUtil = new DBUtil();
		String dbDriver = dbConnInfo.getDbDriver();
		String dbUrl = dbConnInfo.getDbUrl();
		String loginId = dbConnInfo.getLoginId();
		//String loginPwd = EncryptUtil.getJasyptDecryptedString(properties.getProperty("JASYPT.ALGORITHM"), properties.getProperty("JASYPT.KEYSTRING"), dbConnInfo.getLoginPwd());
		String loginPwd = EncryptUtil.getJasyptDecryptedFixString(properties.getProperty("JASYPT.ALGORITHM"), properties.getProperty("JASYPT.KEYSTRING"), dbConnInfo.getLoginPwd());
		String exchangeQuery = "";
		if(segmentVO.getDbConnNo() == 0) {
			String dataSourceName = "";
			try {
				String realPath = request.getServletContext().getRealPath("/");
				Properties prop = new Properties();
				prop.load(new FileInputStream(realPath + "/WEB-INF/config/properties/db.properties"));
				dataSourceName = prop.getProperty("Ems.DataSourceName");
			} catch(Exception e) {
				logger.error("getDbMemberList DataSourceName Read Error!!");
			}
			
			if(segmentVO.getQuery() != null && !"".equals(segmentVO.getQuery())){
				exchangeQuery = segmentVO.getQuery();
				logger.debug("segmentVO.getQuery orgQuery = " + exchangeQuery);
				exchangeQuery= exchangeQuery.replace("@REQUEST_KEY", "0");
				exchangeQuery= exchangeQuery.replace("$:REQUEST_KEY:$", "0");
				logger.debug("segmentVO.getQuery orgQuery = " + exchangeQuery);
				segmentVO.setQuery(exchangeQuery);
				memberVO = dbUtil.getDirectSqlTest(dataSourceName, segmentVO);
			}
			
			if(segmentVO.getRetryQuery() != null && !"".equals(segmentVO.getRetryQuery())){
				exchangeQuery = segmentVO.getRetryQuery();
				logger.debug("segmentVO.getRetryQuer orgQuery = " + exchangeQuery);
				exchangeQuery= exchangeQuery.replace("@REQUEST_KEY", "0");
				exchangeQuery= exchangeQuery.replace("$:REQUEST_KEY:$", "0");
				exchangeQuery= exchangeQuery.replace("@TASK_NO", "0");
				exchangeQuery= exchangeQuery.replace("@SUBTASK_NO", "0");
				exchangeQuery= exchangeQuery.replace("@SEND_RCODE", "'000'");
				exchangeQuery= exchangeQuery.replace("$:TASK_NO:$", "0");
				exchangeQuery= exchangeQuery.replace("$:SUBTASK_NO:$", "0");
				exchangeQuery= exchangeQuery.replace("$:SEND_RCODE:$", "'000'");
				logger.debug("segmentVO.getRetryQuer orgQuery = " + exchangeQuery);
				segmentVO.setQuery(exchangeQuery);
				memberRetryVO = dbUtil.getDirectSqlTest(dataSourceName, segmentVO);
			}
			
			if(segmentVO.getRealQuery() != null && !"".equals(segmentVO.getRealQuery())){
				exchangeQuery = segmentVO.getRealQuery();
				logger.debug("segmentVO.getRealQuery orgQuery = " + exchangeQuery);
				exchangeQuery= exchangeQuery.replace("@REQUEST_KEY", "0");
				exchangeQuery= exchangeQuery.replace("$:REQUEST_KEY:$", "0");
				exchangeQuery= exchangeQuery.replace("@BIZKEY", "'000'");
				exchangeQuery= exchangeQuery.replace("$:BIZKEY:$", "'000'");
				logger.debug("segmentVO.getRealQuery orgQuery = " + exchangeQuery);
				segmentVO.setQuery(exchangeQuery);
				memberRealVO = dbUtil.getDirectSqlTest(dataSourceName, segmentVO);
			}
		} else {
			if(segmentVO.getQuery() != null && !"".equals(segmentVO.getQuery())){
				exchangeQuery = segmentVO.getQuery();
				logger.debug("segmentVO.getQuery orgQuery = " + exchangeQuery);
				exchangeQuery= exchangeQuery.replace("@REQUEST_KEY", "0");
				exchangeQuery= exchangeQuery.replace("$:REQUEST_KEY:$", "0");
				logger.debug("segmentVO.getQuery orgQuery = " + exchangeQuery);
				segmentVO.setQuery(exchangeQuery);
				memberVO = dbUtil.getDirectSqlTest(dbDriver, dbUrl, loginId, loginPwd, segmentVO);
			}
			
			if(segmentVO.getRetryQuery() != null && !"".equals(segmentVO.getRetryQuery())){
				exchangeQuery = segmentVO.getRetryQuery();
				logger.debug("segmentVO.getRetryQuer orgQuery = " + exchangeQuery);
				exchangeQuery= exchangeQuery.replace("@REQUEST_KEY", "0");
				exchangeQuery= exchangeQuery.replace("$:REQUEST_KEY:$", "0");
				exchangeQuery= exchangeQuery.replace("@TASK_NO", "0");
				exchangeQuery=exchangeQuery.replace("@SUBTASK_NO", "0");
				exchangeQuery= exchangeQuery.replace("@SEND_RCODE", "'000'");
				exchangeQuery= exchangeQuery.replace("$:TASK_NO:$", "0");
				exchangeQuery=exchangeQuery.replace("$:SUBTASK_NO:$", "0");
				exchangeQuery= exchangeQuery.replace("$:SEND_RCODE:$", "'000'");
				logger.debug("segmentVO.getRetryQuer orgQuery = " + exchangeQuery);
				segmentVO.setQuery(exchangeQuery);
				memberRetryVO = dbUtil.getDirectSqlTest(dbDriver, dbUrl, loginId, loginPwd, segmentVO);
			}
			
			if(segmentVO.getRealQuery() != null && !"".equals(segmentVO.getRealQuery())){
				exchangeQuery = segmentVO.getRealQuery();
				logger.debug("segmentVO.getRealQuery orgQuery = " + exchangeQuery);
				exchangeQuery= exchangeQuery.replace("@REQUEST_KEY", "0");
				exchangeQuery= exchangeQuery.replace("$:REQUEST_KEY:$", "0");
				exchangeQuery= exchangeQuery.replace("$:BIZKEY:$", "'000'");
				logger.debug("segmentVO.getRealQuery orgQuery = " + exchangeQuery);
				segmentVO.setQuery(exchangeQuery);
				memberRealVO = dbUtil.getDirectSqlTest(dbDriver, dbUrl, loginId, loginPwd, segmentVO);
			}
		}

		boolean retryResult = true;
		String retryMessgae ="";
		
		boolean realResult = true;
		String realMessgae ="";
		
		if( memberVO != null && memberVO.isResult() && !"".equals(memberVO.getMergeKey())){
			if(segmentVO.getRetryQuery() != null && !"".equals(segmentVO.getRetryQuery())){
				if( memberRetryVO != null && memberRetryVO.isResult() ){
					if(memberVO.getMergeKey().equals(memberRetryVO.getMergeKey())){
						retryResult = true;
					} else {
						retryResult = false;
						retryMessgae = "[재발송]기본 쿼리와 재발송 쿼리간의 MergeKey가 동일하지 않습니다";
					}
				} else {
					retryResult = false;
					retryMessgae = "[재발송]" + memberRetryVO.getMessage();
				}
			} else {
				retryResult = true;
				retryMessgae = "";
			}
			
			if(segmentVO.getRealQuery() != null && !"".equals(segmentVO.getRealQuery())){
				if( memberRealVO != null && memberRealVO.isResult() ){
					if(memberVO.getMergeKey().equals(memberRealVO.getMergeKey())){
						realResult = true;
					} else {
						realResult = false;
						realMessgae = "[실시간]기본 쿼리와 실시간 쿼리간의 MergeKey가 동일하지 않습니다";
					}
				} else {
					realResult = false;
					realMessgae = "[실시간]" + memberRealVO.getMessage();
				}
			} else {
				realResult = true;
				realMessgae = "";
			}
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(memberVO.isResult() && retryResult && realResult ) {
				map.put("result", "Success");
				map.put("equal", "");
				map.put("mergeKey", memberVO.getMergeKey());
		} else {
			if (!memberVO.isResult()) {
				map.put("result", "Fail");
				map.put("message", memberVO.getMessage());
			} else {
				if (!retryResult  && !realResult) {
					map.put("result", "Fail");
					map.put("equal", "Fail");
					map.put("message", retryMessgae + "\n" + realMessgae);
				} else {
					if (!retryResult ) {
						map.put("result", "Fail");
						map.put("equal", "Fail");
						map.put("message", retryMessgae); 
					} 
					if (!realResult) {
						map.put("result", "Fail");
						map.put("equal", "Fail");
						map.put("message", realMessgae);
					}
				}
				
			}
			//map.put("result", "Fail");
			//map.put("message", memberVO.getMessage());
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 수신자그룹 연계서비스지정(리타게팅) 등록 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/segRemarketAddP")
	public String goSegRemarketAddP(@ModelAttribute SegmentVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSegRemarketAddP searchSegNm    = " + searchVO.getSearchSegNm());
		logger.debug("goSegRemarketAddP searchCreateTy = " + searchVO.getSearchCreateTy());
		logger.debug("goSegRemarketAddP searchStatus   = " + searchVO.getSearchStatus());
		logger.debug("goSegRemarketAddP searchStartDt  = " + searchVO.getSearchStartDt());
		logger.debug("goSegRemarketAddP searchEndDt    = " + searchVO.getSearchEndDt());
		logger.debug("goSegRemarketAddP searchDeptNo   = " + searchVO.getSearchDeptNo());
		logger.debug("goSegRemarketAddP searchUserId   = " + searchVO.getSearchUserId());
		
		// 수신자정보머지키 조회
		CodeVO merge = new CodeVO();
		merge.setUilang((String)session.getAttribute("NEO_UILANG"));
		merge.setCdGrp("C001");	// 수신자그룹상태
		merge.setUseYn("Y");
		List<CodeVO> mergeKeyList = null;
		try {
			mergeKeyList = codeService.getCodeList(merge);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C001] error = " + e);
		}
		
		// 부서목록(코드성) 조회
		CodeVO deptVO = new CodeVO();
		deptVO.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(deptVO);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 사용자목록(코드성) 조회
		CodeVO userVO = new CodeVO();
		userVO.setStatus("000"); // 정상
		if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
			userVO.setDeptNo(0);
		} else {
			userVO.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
		}
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch(Exception e) {
			logger.error("codeService.getUserList error = " + e);
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
		CampaignVO campaignVO = new CampaignVO();
		campaignVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		campaignVO.setPage(1);
		campaignVO.setRows(9999999);
		List<CampaignVO> campaignList = null;
		try {
			campaignList = campaignService.getCampaignList(campaignVO);
		} catch(Exception e) {
			logger.error("campaignService.getCampaignList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색 항목
		model.addAttribute("createTy", "004");				// 생성 유형
		model.addAttribute("mergeKeyList", mergeKeyList);	// 수신자정보머지키 목록
		model.addAttribute("deptList", deptList);			// 부서목록
		model.addAttribute("userList", userList);			// 사용자목록
		model.addAttribute("reasonList", reasonList);		// 고객정보 조회사유코드
		model.addAttribute("campaignList", campaignList);	// 캠페인목록
		
		return "ems/seg/segRemarketAddP";
	}
	
	/**
	 * 수신자그룹 연계서비스지정(리타게팅) 수정 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/segRemarketUpdateP")
	public String goSegRemarketUpdateP(@ModelAttribute SegmentVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSegRemarketUpdateP segNo          = " + searchVO.getSegNo());
		logger.debug("goSegRemarketUpdateP searchSegNm    = " + searchVO.getSearchSegNm());
		logger.debug("goSegRemarketUpdateP searchCreateTy = " + searchVO.getSearchCreateTy());
		logger.debug("goSegRemarketUpdateP searchStatus   = " + searchVO.getSearchStatus());
		logger.debug("goSegRemarketUpdateP searchStartDt  = " + searchVO.getSearchStartDt());
		logger.debug("goSegRemarketUpdateP searchEndDt    = " + searchVO.getSearchEndDt());
		logger.debug("goSegRemarketUpdateP searchDeptNo   = " + searchVO.getSearchDeptNo());
		logger.debug("goSegRemarketUpdateP searchUserId   = " + searchVO.getSearchUserId());
		
		// 수신자그룹 정보 조회
		SegmentVO segmentInfo = null;
		try {
			searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
			segmentInfo = segmentService.getSegmentInfo(searchVO);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentInfo error = " + e);
		}
		
		// 수신자정보머지키 조회
		CodeVO merge = new CodeVO();
		merge.setUilang((String)session.getAttribute("NEO_UILANG"));
		merge.setCdGrp("C001");	// 수신자그룹상태
		merge.setUseYn("Y");
		List<CodeVO> mergeKeyList = null;
		try {
			mergeKeyList = codeService.getCodeList(merge);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C001] error = " + e);
		}
		
		// 부서목록(코드성) 조회
		CodeVO deptVO = new CodeVO();
		deptVO.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(deptVO);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 사용자목록(코드성) 조회
		CodeVO userVO = new CodeVO();
		userVO.setStatus("000");
		userVO.setDeptNo(segmentInfo.getDeptNo());
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch(Exception e) {
			logger.error("codeService.getUserList erro = " + e);
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
		CampaignVO campaignVO = new CampaignVO();
		campaignVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		campaignVO.setPage(1);
		campaignVO.setRows(9999999);
		List<CampaignVO> campaignList = null;
		try {
			campaignList = campaignService.getCampaignList(campaignVO);
		} catch(Exception e) {
			logger.error("campaignService.getCampaignList error = " + e);
		}
		
		String taskNo = "";
		String subTaskNo = "";
		String taskNm = "";
		String[] tasks = segmentInfo.getSrcWhere().split("\\|");
		if(tasks != null && tasks.length == 3) {
			taskNo = tasks[0];
			subTaskNo = tasks[1];
			taskNm = tasks[2];
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색 항목
		model.addAttribute("createTy", "004");				// 생성 유형
		model.addAttribute("segmentInfo", segmentInfo);		// 수신자그룹 정보
		model.addAttribute("mergeKeyList", mergeKeyList);	// 수신자정보머지키 목록
		model.addAttribute("deptList", deptList);			// 부서목록
		model.addAttribute("userList", userList);			// 사용자목록
		model.addAttribute("reasonList", reasonList);		// 고객정보 조회사유코드
		model.addAttribute("campaignList", campaignList);	// 캠페인목록
		model.addAttribute("taskNo", taskNo);				// Task번호
		model.addAttribute("subTaskNo", subTaskNo);			// SubTask 번호
		model.addAttribute("taskNm", taskNm);				// Task명
		
		return "ems/seg/segRemarketUpdateP";
	}
	
	
	/**
	 * 연계서비스지정(리타게팅) 메일 찾기 팝업 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/segRemarketMailMainP")
	public String goSegRemarketMailMainP(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		// 검색 기본값 설정
		if(searchVO.getSearchStartDt() == null || "".equals(searchVO.getSearchStartDt())) {
			searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-1, "M", "yyyy-MM-dd"));
		}
		if(searchVO.getSearchEndDt() == null || "".equals(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyy-MM-dd"));
		}
		
		// 부서목록(코드성) 조회
		CodeVO deptVO = new CodeVO();
		deptVO.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(deptVO);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 사용자목록(코드성) 조회
		CodeVO userVO = new CodeVO();
		userVO.setStatus("000"); // 정상
		if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
			userVO.setDeptNo(0);
		} else {
			userVO.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
		}
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch(Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		// 캠페인 목록 조회
		CampaignVO campaignVO = new CampaignVO();
		campaignVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		List<CampaignVO> campaignList = null;
		try {
			campaignList = campaignService.getCampaignList(campaignVO);
		} catch(Exception e) {
			logger.error("campaignService.getCampaignList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("deptList", deptList);			// 부서목록
		model.addAttribute("userList", userList);			// 사용자목록
		model.addAttribute("campaignList", campaignList);	// 캠페인목록
		
		return "ems/seg/segRemarketMailMainP";
	}
	
	/**
	 * 연계서비스지정 메일 찾기 팝업화면의 메일 목록을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/segRemarketMailListP")
	public String goSegRemarketMailListP(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSegRemarketMailListP searchTaskNm     = " + searchVO.getSearchTaskNm());
		logger.debug("goSegRemarketMailListP searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goSegRemarketMailListP searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goSegRemarketMailListP searchUserId     = " + searchVO.getSearchUserId());
		logger.debug("goSegRemarketMailListP searchStatus     = " + searchVO.getSearchStatus());
		logger.debug("goSegRemarketMailListP searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goSegRemarketMailListP searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("goSegRemarketMailListP searchWorkStatus = " + searchVO.getSearchWorkStatus());
		logger.debug("goSegRemarketMailListP searchSendRepeat = " + searchVO.getSearchSendRepeat());
		logger.debug("goSegRemarketMailListP page             = " + searchVO.getPage());
		
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		searchVO.setSearchStatus("000");
		searchVO.setSearchWorkStatus("000,001,002,003");
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		List<String> workStatusList = new ArrayList<String>();
		String[] workStatus = searchVO.getSearchWorkStatus().split(",");
		for(int i=0;i<workStatus.length;i++) {
			workStatusList.add(workStatus[i]);
		}
		searchVO.setSearchWorkStatusList(workStatusList);
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		//int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_POP")));
		int rows = StringUtil.setNullToInt((int)session.getAttribute("NEO_PER_PAGE"), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_POP")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		int totalCount = 0;
		
		List<TaskVO> mailList = null;
		try {
			mailList = campaignService.getMailListUnion(searchVO);
		} catch(Exception e) {
			logger.error("campaignService.getMailList error = " + e);
		}
		if(mailList != null && mailList.size() > 0) {
			totalCount = mailList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.setSubmitFunc("goPageNumMail");
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("mailList", mailList);		// 메일목록
		model.addAttribute("pageUtil", pageUtil);		// 페이징
		
		return "ems/seg/segRemarketMailListP";
	}

}
