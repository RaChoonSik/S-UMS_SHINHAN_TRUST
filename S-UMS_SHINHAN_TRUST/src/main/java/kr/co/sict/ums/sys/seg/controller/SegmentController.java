/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.17
 * 설명 : 수신자그룹 관리 Controller
 */
package kr.co.sict.ums.sys.seg.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.co.sict.ums.com.service.CodeService;
import kr.co.sict.ums.com.service.CryptoService;
import kr.co.sict.ums.com.vo.CodeVO;
import kr.co.sict.ums.ems.cam.service.CampaignService;
import kr.co.sict.ums.ems.cam.vo.CampaignVO;
import kr.co.sict.ums.ems.cam.vo.TaskVO;
import kr.co.sict.ums.push.cam.vo.PushCampaignVO;
import kr.co.sict.ums.sys.dbc.service.DBConnService;
import kr.co.sict.ums.sys.dbc.vo.DbConnVO;
import kr.co.sict.ums.sys.dbc.vo.MetaColumnVO;
import kr.co.sict.ums.sys.dbc.vo.MetaJoinVO;
import kr.co.sict.ums.sys.dbc.vo.MetaOperatorVO;
import kr.co.sict.ums.sys.dbc.vo.MetaTableVO;
import kr.co.sict.ums.sys.log.service.SystemLogService;
import kr.co.sict.ums.sys.log.vo.ActionLogVO;
import kr.co.sict.ums.sys.seg.service.SegmentService;
import kr.co.sict.ums.sys.seg.vo.SegmentMemberVO;
import kr.co.sict.ums.sys.seg.vo.SegmentVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.DBUtil;
import kr.co.sict.util.EncryptUtil;
import kr.co.sict.util.PageUtil;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;
import kr.co.sict.ums.sys.seg.vo.KakaoTemplateVO;

@Controller
@RequestMapping("/sys/seg")
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
		
		searchVO.setSearchEmsuseYn("Y");
		searchVO.setSearchSmsuseYn("Y");
		searchVO.setSearchPushuseYn("Y");
		
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
		
		return "sys/seg/segMainP";
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
		if(StringUtil.isNull(searchVO.getSearchEmsuseYn())) searchVO.setSearchEmsuseYn("N");
		if(StringUtil.isNull(searchVO.getSearchSmsuseYn())) searchVO.setSearchSmsuseYn("N");
		if(StringUtil.isNull(searchVO.getSearchPushuseYn())) searchVO.setSearchPushuseYn("N");
				
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		// 수신자그룹 목록 조회
		SegmentVO search = searchVO;
		search.setUilang((String)session.getAttribute("NEO_UILANG"));
		search.setSearchStartDt(search.getSearchStartDt().replaceAll("\\.",""));
		search.setSearchEndDt(search.getSearchEndDt().replaceAll("\\.", ""));
		List<SegmentVO> segmentList = null;
		try {
			segmentList = segmentService.getSegmentList(search);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentList error = " + e);
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
		
		return "sys/seg/segList";
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
		
		return "sys/seg/segFileAddP";
	}
	
	/**
	 * 샘플파일 다운로드 팝업 화면을 출력한다.
	 * @return
	 */
	@RequestMapping(value="/fileSampleDownPop")
	public String goFileSampleDown() {
		return "sys/seg/fileSampleDownPop";
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
		
		return "sys/seg/segFileUpdateP";
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
	public String goSegFileMemberList(@ModelAttribute SegmentVO segmentVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSegFileMemberList segFlPath = " + segmentVO.getSegFlPath());
		logger.debug("goSegFileMemberList separatorChar = " + segmentVO.getSeparatorChar());
		logger.debug("goSegFileMemberList page = " + segmentVO.getPage());
		logger.debug("goSegFileMemberList page = " + segmentVO.getPage());
		logger.debug("goSegFileMemberList checkSearchReason = " + segmentVO.getCheckSearchReason());
		logger.debug("goSegFileMemberList contentPath = " + segmentVO.getContentPath());

		int pageRow = Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_POP"));
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
		try {
			String tmpFlPath = properties.getProperty("FILE.UPLOAD_PATH") + "/" + segmentVO.getSegFlPath();
			line = new BufferedReader(new InputStreamReader(new FileInputStream(tmpFlPath), "UTF-8"));
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
							//광주버전에서 가져옴 추후확인 필요  (파일암호화시)
							//alias = (String)memAlias.get(cnt);
							//String decStr = cryptoService.getDecrypt(alias, st.nextToken()); 
							//unitInfo.put(alias,decStr);
							//기존리만버전
							unitInfo.put((String)memAlias.get(cnt), st.nextToken());
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
			
			memAlias.add("LINE");
			memAlias.add("Error");
			
			HashMap<String,String> unitInfo = new HashMap<String,String>();
			unitInfo.put("Line", "");
			unitInfo.put("Error", "File Loadding Error!!");
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
		
		return "sys/seg/segFileMemberListP";
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
		logger.debug("insertSegmentInfo emsuseYn      = " + segmentVO.getEmsuseYn());
		logger.debug("insertSegmentInfo pushuseYn     = " + segmentVO.getPushuseYn());
		logger.debug("insertSegmentInfo smsuseYn      = " + segmentVO.getSmsuseYn());
		logger.debug("insertSegmentInfo serviceGb      = " + segmentVO.getServiceGb());
		
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
		if(StringUtil.isNull(segmentVO.getEmsuseYn())) segmentVO.setEmsuseYn("N");
		if(StringUtil.isNull(segmentVO.getSmsuseYn())) segmentVO.setSmsuseYn("N");
		if(StringUtil.isNull(segmentVO.getPushuseYn())) segmentVO.setPushuseYn("N");
		
		if(!"003".equals(segmentVO.getCreateTy()) && !"002".equals(segmentVO.getCreateTy())) {
			String query = "";
			
			query = " SELECT " + segmentVO.getSelectSql();
			query += " FROM " + segmentVO.getFromSql();
			if(!"".equals(segmentVO.getWhereSql())) {
				query += " WHERE " + segmentVO.getWhereSql();
			}
			segmentVO.setQuery(query);
		}
		
		if("003".equals(segmentVO.getCreateTy())) {
			if(segmentVO.getMergeKey() == null || "".equals(segmentVO.getMergeKey())) {
				logger.debug("insertSegmentInfo segFileMerge      = " + segmentVO.getMergeKey());
				String mergeKey = "";
				
				List<HashMap<String,String>> memList = new ArrayList<HashMap<String,String>>();
				List<String> memAlias = new ArrayList<String>();
				
				BufferedReader line = null;
				String tempStr = "";
				
				int totCount = 0;
				try {
					String tmpFlPath = properties.getProperty("FILE.UPLOAD_PATH") + "/" + segmentVO.getSegFlPath();
					line = new BufferedReader(new InputStreamReader(new FileInputStream(tmpFlPath), "UTF-8"));
					while((tempStr = line.readLine()) != null) {
						if("".equals(tempStr.trim())) continue;
						StringTokenizer st = new StringTokenizer(tempStr, segmentVO.getSeparatorChar());
						
						if(totCount == 0) {	// 첫줄의 알리아스를 읽어 셋팅한다.
							while(st.hasMoreTokens()) {
								memAlias.add(st.nextToken());
							}
						} else {
							if(tempStr == null || "".equals(tempStr)) break;
								HashMap<String, String> unitInfo = new HashMap<String,String>();
						}
						totCount++;
					} 
				} catch(FileNotFoundException fe) {
					memAlias.clear();
				} catch(Exception e) {
					memAlias.clear(); 
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
					segmentVO.setMergeKey(mergeKey);
					segmentVO.setMergeCol(mergeKey);
				}
				totCount = totCount -1;
				segmentVO.setTotCnt(totCount);
			}
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
		logger.debug("insertSegmentInfo retryQuery    = " + segmentVO.getRetryQuery());
		logger.debug("insertSegmentInfo realQuery     = " + segmentVO.getRealQuery());		
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
		if(StringUtil.isNull(segmentVO.getEmsuseYn())) segmentVO.setEmsuseYn("N");
		if(StringUtil.isNull(segmentVO.getSmsuseYn())) segmentVO.setSmsuseYn("N");
		if(StringUtil.isNull(segmentVO.getPushuseYn())) segmentVO.setPushuseYn("N");
		
		if(!"003".equals(segmentVO.getCreateTy()) && !"002".equals(segmentVO.getCreateTy())) {
			String query = "";
	    	
	    	query = " SELECT " + segmentVO.getSelectSql();
	    	query += " FROM " + segmentVO.getFromSql();
	    	if(!"".equals(segmentVO.getWhereSql())) {
	    		query += " WHERE " + segmentVO.getWhereSql();
	    	}
	    	segmentVO.setQuery(query);
		}
		
		if("003".equals(segmentVO.getCreateTy())) {
			if(segmentVO.getMergeKey() == null || "".equals(segmentVO.getMergeKey())) {
				logger.debug("insertSegmentInfo segFileMerge      = " + segmentVO.getMergeKey());
				String mergeKey = "";
				
				List<HashMap<String,String>> memList = new ArrayList<HashMap<String,String>>();
				List<String> memAlias = new ArrayList<String>();
				
				BufferedReader line = null;
				String tempStr = "";
				
				int totCount = 0;
				try {
					String tmpFlPath = properties.getProperty("FILE.UPLOAD_PATH") + "/" + segmentVO.getSegFlPath();
					line = new BufferedReader(new InputStreamReader(new FileInputStream(tmpFlPath), "UTF-8"));
					while((tempStr = line.readLine()) != null) {
						if("".equals(tempStr.trim())) continue;
						StringTokenizer st = new StringTokenizer(tempStr, segmentVO.getSeparatorChar());
						
						if(totCount == 0) {	// 첫줄의 알리아스를 읽어 셋팅한다.
							while(st.hasMoreTokens()) {
								memAlias.add(st.nextToken());
							}
						} else {
							if(tempStr == null || "".equals(tempStr)) break;
								HashMap<String, String> unitInfo = new HashMap<String,String>();
						}
						totCount++;
					} 
				} catch(FileNotFoundException fe) {
					memAlias.clear();
				} catch(Exception e) {
					memAlias.clear(); 
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
					segmentVO.setMergeKey(mergeKey);
					segmentVO.setMergeCol(mergeKey);
				}
				totCount = totCount -1;
				segmentVO.setTotCnt(totCount);
			}
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
		
		return "sys/seg/segToolAddP";
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
		
		return "sys/seg/segToolUpdateP";
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
		
		return "sys/seg/segMetaFrameP";
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
			String loginPwd = EncryptUtil.getJasyptDecryptedString(properties.getProperty("JASYPT.ALGORITHM"), properties.getProperty("JASYPT.KEYSTRING"), dbConnInfo.getLoginPwd());
			if(segmentVO.getDbConnNo() == 0) {
				String dataSourceName = "";
				try {
					String realPath = request.getServletContext().getRealPath("/");
					Properties prop = new Properties();
					prop.load(new FileInputStream(realPath + "/WEB-INF/config/properties/db.properties"));
					dataSourceName = prop.getProperty("Ums.DataSourceName");
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
		
		return "sys/seg/segInfoP";
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
		
		int pageRow = Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_POP"));
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
			String loginPwd = EncryptUtil.getJasyptDecryptedString(properties.getProperty("JASYPT.ALGORITHM"), properties.getProperty("JASYPT.KEYSTRING"), dbConnInfo.getLoginPwd());
			if(segmentVO.getDbConnNo() == 0) {
				String dataSourceName = "";
				try {
					String realPath = request.getServletContext().getRealPath("/");
					Properties prop = new Properties();
					prop.load(new FileInputStream(realPath + "/WEB-INF/config/properties/db.properties"));
					dataSourceName = prop.getProperty("Ums.DataSourceName");
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
		
		return "sys/seg/segDbMemberListP";
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
		
		return "sys/seg/segDirectSQLAddP";
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
		
		return "sys/seg/segDirectSQLUpdateP";
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
		List<String> realTableList = null;
		DBUtil dbUtil = new DBUtil();
		String dbTy = dbConnInfo.getDbTy();
		String dbDriver = dbConnInfo.getDbDriver();
		String dbUrl = dbConnInfo.getDbUrl();
		String loginId = dbConnInfo.getLoginId();
		String loginPwd = EncryptUtil.getJasyptDecryptedString(properties.getProperty("JASYPT.ALGORITHM"), properties.getProperty("JASYPT.KEYSTRING"), dbConnInfo.getLoginPwd());
		realTableList = dbUtil.getRealTableList(dbTy, dbDriver, dbUrl, loginId, loginPwd);
		
		model.addAttribute("realTableList", realTableList);	// 실제테이블 목록
		
		return "sys/seg/metatableListP";
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
		logger.debug("getMetaColumnListP getTblNm = " + metaColumnVO.getTblNm());
		
		DbConnVO dbConnInfo = new DbConnVO();
		try {
			dbConnInfo.setDbConnNo(metaColumnVO.getDbConnNo());
			dbConnInfo.setUilang((String)session.getAttribute("NEO_UILANG"));
			dbConnInfo = dbConnService.getDbConnInfo(dbConnInfo);
		} catch(Exception e) {
			logger.error("dbConnService.getDbConnInfo error = " + e);
		}
		
		// 실제 DB 테이블 컬럼 목록 조회
		List<MetaColumnVO> realColumnList = null;
		DBUtil dbUtil = new DBUtil();
		String dbTy = dbConnInfo.getDbTy();
		String dbDriver = dbConnInfo.getDbDriver();
		String dbUrl = dbConnInfo.getDbUrl();
		String loginId = dbConnInfo.getLoginId();
		String loginPwd = EncryptUtil.getJasyptDecryptedString(properties.getProperty("JASYPT.ALGORITHM"), properties.getProperty("JASYPT.KEYSTRING"), dbConnInfo.getLoginPwd());
		realColumnList = dbUtil.getRealColumnList(dbTy, dbDriver, dbUrl, loginId, loginPwd, metaColumnVO.getTblNm());
		
		// 메타 컬럼 목록 조회
		/*
		List<MetaColumnVO> metaColumnList = null;
		try {
			metaColumnList = dbConnService.getMetaColumnList(metaColumnVO);
		} catch(Exception e) {
			logger.error("dbConnService.getMetaColumnList error = " + e);
		}
		*/
		
		model.addAttribute("tableName", metaColumnVO.getTblNm());	// 테이블명
		model.addAttribute("realColumnList", realColumnList);		// 실제컬럼 목록
		//model.addAttribute("metaColumnList", metaColumnList);		// 메타컬럼 목록
		
		return "sys/seg/metacolumnListP";
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
	
//  리만 기존 (2022.03.19)
//	@RequestMapping(value="/getMetaColumnList")
//	public ModelAndView getMetaColumnList(@ModelAttribute MetaTableVO metaTableVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//		logger.debug("getMetaColumnList dbConnNo = " + metaTableVO.getDbConnNo());
//		logger.debug("getMetaColumnList tblNo = " + metaTableVO.getTblNo());
//		logger.debug("getMetaColumnList tblNm = " + metaTableVO.getTblNm());
//		
//		DbConnVO dbConnInfo = null;
//		try {
//			DbConnVO dbConnVO = new DbConnVO();
//			dbConnVO.setDbConnNo(metaTableVO.getDbConnNo());
//			dbConnVO.setUilang((String) session.getAttribute("NEO_UILANG"));
//			dbConnInfo = dbConnService.getDbConnInfo(dbConnVO);
//		} catch (Exception e) {
//			logger.error("dbConnService.getDbConnInfo error = " + e);
//		}
//		
//		if(metaTableVO.getTblNm() == null || "".equals(metaTableVO.getTblNm())){
//			try {
//				MetaTableVO tmpMetaTableVO = new MetaTableVO(); 
//				tmpMetaTableVO = dbConnService.getMetaTableInfo(metaTableVO);
//				metaTableVO.setTblNm(tmpMetaTableVO.getTblNm());
//				logger.debug("getMetaColumnList tblNm = " + metaTableVO.getTblNm());
//			} catch (Exception e) {
//				logger.error("dbConnService.getDbConnInfo error = " + e);
//			}
//		}		
//
//		// 실제 DB 테이블 컬럼 목록 조회
//		List<MetaColumnVO> realColumnList = null;
//		DBUtil dbUtil = new DBUtil();
//		String dbTy = dbConnInfo.getDbTy();
//		String dbDriver = dbConnInfo.getDbDriver();
//		String dbUrl = dbConnInfo.getDbUrl();
//		String loginId = dbConnInfo.getLoginId();
//		String loginPwd = EncryptUtil.getJasyptDecryptedString(properties.getProperty("JASYPT.ALGORITHM"),
//		properties.getProperty("JASYPT.KEYSTRING"), dbConnInfo.getLoginPwd());
//		realColumnList = dbUtil.getRealColumnList(dbTy, dbDriver, dbUrl, loginId, loginPwd, metaTableVO.getTblNm());
//
//		List<MetaColumnVO> useColumnList = null;
//		MetaColumnVO columnVO = new MetaColumnVO();
//		columnVO.setTblNo(metaTableVO.getTblNo());
//		try {
//			useColumnList = dbConnService.getMetaColumnList(columnVO);
//		} catch (Exception e) {
//			logger.error("dbConnService.getMetaColumnList error = " + e);
//		}
//
//		// real table 과 metaTable join
//		List<MetaColumnVO> metaColumnList = new ArrayList<MetaColumnVO>();
//
//		String realColumnName = "";
//
//		if (realColumnList.size() > 0) {
//			for (int i = 0; i < realColumnList.size(); i++) {
//				MetaColumnVO metaColumn = new MetaColumnVO();
//				try {
//					metaColumn.setColNo(0);
//					metaColumn.setColNm(realColumnList.get(i).getColNm());
//					metaColumn.setColDataTy(realColumnList.get(i).getColDataTy());
//					metaColumnList.add(metaColumn);
//				} catch (Exception e) {
//					logger.error("dbConnService.getMetaColumnList (make real-use column list info step1) error = " + e);
//				}
//			}
//
//			if (useColumnList.size() > 0) {
//				for (int i = 0; i < metaColumnList.size(); i++) {
//					realColumnName = metaColumnList.get(i).getColNm();
//					for (int j = 0; j < useColumnList.size(); j++) { 
//						if (realColumnName.equals(useColumnList.get(j).getColNm())) {
//							try {
//								metaColumnList.get(i).setColNo(useColumnList.get(j).getColNo());
//								metaColumnList.get(i).setColAlias(useColumnList.get(j).getColAlias());
//								metaColumnList.get(i).setColDesc(useColumnList.get(j).getColDesc());
//								metaColumnList.get(i).setColHiddenYn(useColumnList.get(j).getColHiddenYn());
//								metaColumnList.get(i).setColEncrDecrYn(useColumnList.get(j).getColEncrDecrYn());
//							} catch (Exception e) {
//								logger.error("dbConnService.getMetaColumnList (make real-use column list info step2) error = " + e);
//							}
//						}
//					}
//				}
//			}
//		}
//		// jsonView 생성
//		HashMap<String, Object> map = new HashMap<String, Object>();
//		
//		map.put("result", "Success");
//		map.put("realColumnList", realColumnList);		// 실제컬럼 목록
//		map.put("metaColumnList", metaColumnList);
//	 
//		ModelAndView modelAndView = new ModelAndView("jsonView", map);
//		
//		return modelAndView;
//	}
	
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
		String loginPwd = EncryptUtil.getJasyptDecryptedString(properties.getProperty("JASYPT.ALGORITHM"), properties.getProperty("JASYPT.KEYSTRING"), dbConnInfo.getLoginPwd());
		String exchangeQuery = ""; 
		if(segmentVO.getDbConnNo() == 0) {
			String dataSourceName = "";
			try {
				String realPath = request.getServletContext().getRealPath("/");
				Properties prop = new Properties();
				prop.load(new FileInputStream(realPath + "/WEB-INF/config/properties/db.properties"));
				dataSourceName = prop.getProperty("Ums.DataSourceName");
			} catch(Exception e) {
				logger.error("getDbMemberList DataSourceName Read Error!!");
			}
			memberVO = dbUtil.getDirectSqlTest(dataSourceName, segmentVO);
			if(segmentVO.getRetryQuery() != null && !"".equals(segmentVO.getRetryQuery())){
				exchangeQuery = segmentVO.getRetryQuery();
				logger.debug("segmentVO.getRetryQuer orgQuery = " + exchangeQuery);
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
				exchangeQuery= exchangeQuery.replace("$:BIZKEY:$", "'000'");
				logger.debug("segmentVO.getRealQuery orgQuery = " + exchangeQuery);
				segmentVO.setQuery(exchangeQuery);
				memberRealVO = dbUtil.getDirectSqlTest(dataSourceName, segmentVO);
			}
			
		} else {
			memberVO = dbUtil.getDirectSqlTest(dbDriver, dbUrl, loginId, loginPwd, segmentVO);
			if(segmentVO.getRetryQuery() != null && !"".equals(segmentVO.getRetryQuery())){
				exchangeQuery = segmentVO.getRetryQuery();
				logger.debug("segmentVO.getRetryQuer orgQuery = " + exchangeQuery);
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
		
		return "sys/seg/segRemarketAddP";
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
		
		return "sys/seg/segRemarketUpdateP";
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
		
		return "sys/seg/segRemarketMailMainP";
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
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_POP")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
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
		
		return "sys/seg/segRemarketMailListP";
	}
	
	
	/**
	 * API : segmentList
	 * @param params
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return 
	 */
	@RequestMapping(value="/api/segList")
	public void goApiSegList(@RequestBody Map<String, Object> params, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
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
		
		SegmentVO segVO = new SegmentVO();
		
		int deptNo = 0;
		if (json.has("deptNo")) {
			deptNo = StringUtil.setNullToInt(json.get("deptNo").toString());
			segVO.setSearchDeptNo(deptNo);
		} else {
			segVO.setSearchDeptNo(deptNo);
		}
		
		if (json.has("startDt")) {
			if ("".equals(StringUtil.setNullToString(json.get("startDt").toString()))) {
				sReturnValue = "Fail";
				sErrorCode = "E001";
				sErrorMessage += "[검색시작일(startDt)] ";
			} else {
				segVO.setSearchStartDt(StringUtil.setNullToString(json.get("startDt").toString()));
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
				segVO.setSearchEndDt(StringUtil.setNullToString(json.get("endDt").toString()));
			}
		} else {
			sReturnValue = "Fail";
			sErrorCode = "E001";
			sErrorMessage += "[검색종료일(endDt)] ";
		}
		
		if (json.has("status")) {
			if(!"".equals(StringUtil.setNullToString(json.get("status").toString()))) {
				segVO.setSearchStatus(StringUtil.setNullToString(json.get("status").toString()));
			} else {
				segVO.setSearchStatus("000");
			}
		} else {
			segVO.setSearchStatus("000");
		}
		
		if (json.has("createTy")) {
			segVO.setSearchCreateTy(StringUtil.setNullToString(json.get("createTy").toString()));
		}
		
		if (json.has("segNm")) {
			segVO.setSearchSegNm(StringUtil.setNullToString(json.get("segNm").toString()));
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
		
		segVO.setUilang("000");
		segVO.setPage(page);
		segVO.setRows(rows);
		segVO.setStartRow(( page-1) * rows);
		segVO.setUilang("000");
		
		int resultCount = 0;
		int totalCount = 0;
		String resultData = "";
		
		if ("0000".equals(sErrorCode)) {
			
			List<SegmentVO> segmentList = null;
			
			try {
				segmentList = segmentService.getSegmentList(segVO);
				if(segmentList == null || segmentList.size() == 0) {
					sReturnValue ="Success";
					sErrorMessage= "조회된 내역이 없습니다";
					resultData = "";
					resultCount = 0; 
				} else {
					resultCount =  segmentList.size();
					String resultString = "";
					
					for (int m = 0; m < segmentList.size(); m++)
					{
						SegmentVO sObjectVO = (SegmentVO) segmentList.get(m);
						resultString += String.format("{ ", "");
						resultString += String.format("\"userId\":\"%s\",", StringUtil.setNullToString(sObjectVO.getUserId()));
						resultString += String.format("\"userNm\":\"%s\",", StringUtil.setNullToString(sObjectVO.getUserNm()));
						resultString += String.format("\"deptNm\":\"%s\",", StringUtil.setNullToString(sObjectVO.getDeptNm()));
						resultString += String.format("\"campNo\":\"%s\",", sObjectVO.getSegNo());
						resultString += String.format("\"campNm\":\"%s\",", StringUtil.setNullToString(sObjectVO.getSegNm()));
						resultString += String.format("\"campNm\":\"%s\",", StringUtil.setNullToString(sObjectVO.getSegDesc()));
						resultString += String.format("\"status\":\"%s\",", StringUtil.setNullToString(sObjectVO.getStatus()));
						resultString += String.format("\"statusNm\":\"%s\",", StringUtil.setNullToString(sObjectVO.getStatusNm()));
						resultString += String.format("\"dbConnNo\":\"%s\",", sObjectVO.getDbConnNo());
						resultString += String.format("\"createTy\":\"%s\",", StringUtil.setNullToString(sObjectVO.getCreateTy()));
						resultString += String.format("\"createTyNm\":\"%s\",", StringUtil.setNullToString(sObjectVO.getCreateTyNm()));
						resultString += String.format("\"selectSql\":\"%s\",", StringUtil.setNullToString(sObjectVO.getSelectSql()));
						resultString += String.format("\"fromSql\":\"%s\",", StringUtil.setNullToString(sObjectVO.getFromSql()));
						resultString += String.format("\"whereSql\":\"%s\",", StringUtil.setNullToString(sObjectVO.getWhereSql()));
						resultString += String.format("\"orderbySql\":\"%s\",", StringUtil.setNullToString(sObjectVO.getOrderbySql()));
						resultString += String.format("\"query\":\"%s\",", StringUtil.setNullToString(sObjectVO.getQuery()));
						resultString += String.format("\"mergeyKey\":\"%s\",", StringUtil.setNullToString(sObjectVO.getMergeKey()));
						resultString += String.format("\"mergeCol\":\"%s\",", StringUtil.setNullToString(sObjectVO.getMergeCol()));
						resultString += String.format("\"segFlPath\":\"%s\",", StringUtil.setNullToString(sObjectVO.getSegFlPath()));
						resultString += String.format("\"srcWhere\":\"%s\"", StringUtil.setNullToString(sObjectVO.getSrcWhere()));
						
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
}