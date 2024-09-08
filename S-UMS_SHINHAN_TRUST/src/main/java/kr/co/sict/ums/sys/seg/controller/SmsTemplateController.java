/**
 * 작성자 : 이혜민
 * 작성일시 : 2022.06.22
 * 설명 : SMS 템플릿 관리 Controller
 */
package kr.co.sict.ums.sys.seg.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.co.sict.ums.com.service.CodeService;
import kr.co.sict.ums.com.vo.CodeVO;
import kr.co.sict.ums.sms.cam.service.SmsCampaignService;
import kr.co.sict.ums.sms.cam.vo.SmsCampaignVO;
import kr.co.sict.ums.sys.dbc.vo.DbConnVO;
import kr.co.sict.ums.ems.seg.service.SegmentService;
import kr.co.sict.ums.sys.seg.service.SmsTemplateService;
import kr.co.sict.ums.ems.seg.vo.SegmentVO;
import kr.co.sict.ums.sys.seg.vo.SmsTemplateVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.PageUtil;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Controller
@RequestMapping("/sys/seg")
public class SmsTemplateController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private PropertiesUtil properties;
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private SegmentService segmentService;
	
	@Autowired
	private SmsCampaignService smsCampaignService;
	
	@Autowired
	private SmsTemplateService smsTemplateService;
	
	/**
	 * SMS 템플릿 목록 화면 출력.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/smsTemplateListP")
	public String gosmsTemplateListP(@ModelAttribute SmsTemplateVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("gosmsTemplateListP searchTempCd  = " + searchVO.getSearchTempCd());
		logger.debug("gosmsTemplateListP searchTempNm  = " + searchVO.getSearchTempNm());
		logger.debug("gosmsTemplateListP searchDeptNo  = " + searchVO.getSearchDeptNo());
		logger.debug("gosmsTemplateListP searchStatus  = " + searchVO.getSearchStatus());
		logger.debug("gosmsTemplateListP searchUserId  = " + searchVO.getSearchUserId());
		logger.debug("gosmsTemplateListP searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("gosmsTemplateListP searchEndtDt  = " + searchVO.getSearchEndDt());
		
		// 검색 기본값 설정
		if(searchVO.getSearchStartDt() == null || "".equals(searchVO.getSearchStartDt())) {
			String year = StringUtil.getDate(Code.TM_Y);
			searchVO.setSearchStartDt(year + "0101");
		} else {
			searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		}
		
		
		if(searchVO.getSearchEndDt() == null || "".equals(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		}
		
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 부서목록(코드성) 조회
		CodeVO deptVO = new CodeVO();
		deptVO.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(deptVO);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// SMS 템플릿 상태 리스트
		CodeVO statusVO = new CodeVO();
		statusVO.setUilang("000");
		statusVO.setCdGrp("C023");
		statusVO.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(statusVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("deptList", deptList);			// 부서번호
		model.addAttribute("statusList", statusList);		// 선택상태
		
		return "sys/seg/smsTemplateListP";
	}
	
	/**
	 * SMS 템플릿 목록 조회
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/smsTemplateList")
	public String goSmsTemplateList(@ModelAttribute SmsTemplateVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSmsTemplateList searchTempCd  = " + searchVO.getSearchTempCd());
		logger.debug("goSmsTemplateList searchTempNm  = " + searchVO.getSearchTempNm());
		logger.debug("goSmsTemplateList searchDeptNo  = " + searchVO.getSearchDeptNo());
		logger.debug("goSmsTemplateList searchStatus  = " + searchVO.getSearchStatus());
		logger.debug("goSmsTemplateList searchUserId  = " + searchVO.getSearchUserId());
		logger.debug("goSmsTemplateList searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goSmsTemplateList searchEndtDt  = " + searchVO.getSearchEndDt());
		
		// 검색 기본값 설정
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		if(searchVO.getSearchStartDt() == null || "".equals(searchVO.getSearchStartDt())) {
			String year = StringUtil.getDate(Code.TM_Y);
			searchVO.setSearchStartDt(year + "0101");
		} else {
			searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		}
		
		if(searchVO.getSearchEndDt() == null || "".equals(searchVO.getSearchEndDt())) {
			searchVO.setSearchEndDt(StringUtil.getCalcDateFromCurr(0, "D", "yyyyMMdd"));
		} else {
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		}
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		//  SMS 템플릿 목록
		List<SmsTemplateVO> smsTemplateList = null;
		try {
			smsTemplateList = smsTemplateService.getSmsTemplateList(searchVO);
		} catch(Exception e) {
			logger.error("smsTemplateService.getSmsTemplateList error = " + e);
		}
		
		if(smsTemplateList != null && smsTemplateList.size() > 0) {
			totalCount = smsTemplateList.get(0).getTotalCount();
		}
		
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);

		model.addAttribute("searchVO", searchVO);						// 검색항목
		model.addAttribute("smsTemplateList", smsTemplateList);			//  알림톡 템플릿  목록
		model.addAttribute("pageUtil", pageUtil);						// 페이징
		
		return "sys/seg/smsTemplateList";
	}
	
	/**
	 * SMS 템플릿 신규 등록 화면
	 * @param SmsTemplateVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/smsTemplateAddP")
	public String goSmsTemplateAddP(@ModelAttribute SmsTemplateVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
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
		userVO.setDeptNo("Y".equals((String) session.getAttribute("NEO_ADMIN_YN")) ? 0 : (int) session.getAttribute("NEO_DEPT_NO"));
		userVO.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch (Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}

		// SMS 템플릿 상태 리스트
		CodeVO statusVO = new CodeVO();
		statusVO.setUilang("000");
		statusVO.setCdGrp("C023");
		statusVO.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(statusVO);
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
		
		model.addAttribute("searchVO", searchVO);		   // 검색항목
		model.addAttribute("deptList", deptList);		   // 부서목록
		model.addAttribute("userList", userList);		   // 사용자 목록
		model.addAttribute("statusList", statusList);	   // 부서번호
		model.addAttribute("reasonList", reasonList);		// 고객정보 조회사유코드
		
		
		return "sys/seg/smsTemplateAddP";
	} 
	/**
	 * SMS 템플릿 신규 등록 
	 * @param SmsTemplateVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/smsTemplateAdd")
	public String goSmsTemplateAdd(@ModelAttribute SmsTemplateVO smsTemplateVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		logger.debug("goSmsTemplateAdd tempCd      = " + smsTemplateVO.getTempCd());
		logger.debug("goSmsTemplateAdd tempNm      = " + smsTemplateVO.getTempNm());
		logger.debug("goSmsTemplateAdd tempDesc    = " + smsTemplateVO.getTempDesc());
		logger.debug("goSmsTemplateAdd tempContent = " + smsTemplateVO.getTempContent());
		logger.debug("goSmsTemplateAdd mergeItem   = " + smsTemplateVO.getMergeItem());
		logger.debug("goSmsTemplateAdd status      = " + smsTemplateVO.getStatus());
		logger.debug("goSmsTemplateAdd gubun       = " + smsTemplateVO.getGubun());
		logger.debug("goSmsTemplateAdd deptNo      = " + smsTemplateVO.getDeptNo());
		logger.debug("goSmsTemplateAdd userId      = " + smsTemplateVO.getUserId());
		
		int result = 0;
		
		if (smsTemplateVO.getStatus() == null || "".equals((String)smsTemplateVO.getStatus())) {
			smsTemplateVO.setStatus("000");
		}
		
		smsTemplateVO.setRegId((String) session.getAttribute("NEO_USER_ID"));
		smsTemplateVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		smsTemplateVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		smsTemplateVO.setStatus("000");//문자발송상태 기본값 발송대기 //문자발송상태 기본값 발송대기 
		if(!StringUtil.isNull(smsTemplateVO.getSegNoc())) smsTemplateVO.setSegNo(Integer.parseInt( smsTemplateVO.getSegNoc().substring(0, smsTemplateVO.getSegNoc().indexOf("|")) ));// 세그먼트번호(발송대상그룹)
		
		
		if(smsTemplateVO.getMergeItem() != null || !"".equals((String)smsTemplateVO.getMergeItem())) {
			smsTemplateVO.setMergeItem(StringUtil.removeComma(smsTemplateVO.getMergeItem()));
		}
		
		try {
			result = smsTemplateService.insertSmsTemplateInfo(smsTemplateVO);
		} catch(Exception e) {
			logger.error("smsTemplateService.insertSmsTemplateInfo error = " + e);
		}
		
		if(result > 0) {
			model.addAttribute("result","Success");
		} else {
			model.addAttribute("result","Fail");
		}
		
		return "sys/seg/smsTemplateAdd";
	} 
	
	/**
	 * SMS 템플릿 코드 중복확인 
	 * 
	 * @param smsTemplateVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/checkSmsTempCd")
	public ModelAndView checkSmsTempCd(@ModelAttribute SmsTemplateVO smsTemplateVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("checkSmsTempCd tempCd = " + smsTemplateVO.getTempCd());

		boolean result = false;
		List<SmsTemplateVO> tempList = null;
		try {
			tempList = smsTemplateService.checkSmsTemplateCode(smsTemplateVO.getTempCd());
		} catch (Exception e) {
			logger.error("smsTemplateService.checkSmsTemplateCode error = " + e);
		}

		if (tempList != null && tempList.size() > 0) {
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
	 * SMS 템플릿 정보 수정 화면
	 * @param SmsTemplateVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@Transactional
	@RequestMapping(value="/smsTemplateUpdateP")
	public String goSmsTemplateUpdateP(@ModelAttribute SmsTemplateVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSmsTemplateUpdateP tempCd  = " + searchVO.getTempCd());
		
		// SMS 템플릿 정보 조회
		SmsTemplateVO smsTemplateInfo = null;
		
		try {
			smsTemplateInfo = smsTemplateService.getSmsTemplateInfo(searchVO.getTempCd());
		} catch (Exception e) {
			logger.error("SmsTemplateService.getSmsTemplateInfo error = " + e);
		}
		
		// 수신자그룹(발송대상) 목록
		SegmentVO seg = new SegmentVO();
		seg.setUilang((String)session.getAttribute("NEO_UILANG"));
		seg.setSearchStatus("000");
		seg.setPage(1);
		seg.setRows(10000);
		//seg.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		//seg.setSearchDeptNo(0);
		//20240908 주석처리
//		seg.setSearchSmsuseYn("Y");
		List<SegmentVO> segList = null;
		try {
			segList = segmentService.getSegmentList(seg);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentList error = " + e);
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
		userVO.setDeptNo(smsTemplateInfo.getDeptNo());
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch(Exception e) {
			logger.error("codeService.getUserList erro = " + e);
		}
		
		// SMS 템플릿 상태 리스트
		CodeVO statusVO = new CodeVO();
		statusVO.setUilang("000");
		statusVO.setCdGrp("C023");
		statusVO.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(statusVO);
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
		
		model.addAttribute("searchVO", searchVO);					// 검색항목
		model.addAttribute("smsTemplateInfo", smsTemplateInfo);		// SMS 템플릿 정보
		model.addAttribute("deptList", deptList);					// 부서목록
		model.addAttribute("segList", segList);						// 발송대상(세그먼트) 목록
		model.addAttribute("userList", userList);					// 사용자 목록
		model.addAttribute("statusList", statusList);				// 상태코드
		model.addAttribute("reasonList", reasonList);				// 조회사유코드
		
		return "sys/seg/smsTemplateUpdateP";
	}
	
	/**
	 * SMS 템플릿 정보를 수정한다.
	 * @param smsTemplateVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/smsTemplateUpdate")
	public String goSmsTemplateUpdate(@ModelAttribute SmsTemplateVO smsTemplateVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSmsTemplateUpdate userId           = " + smsTemplateVO.getUserId());
		logger.debug("goSmsTemplateUpdate tempCd           = " + smsTemplateVO.getTempCd());
		logger.debug("goSmsTemplateUpdate tempNm           = " + smsTemplateVO.getTempNm());
		logger.debug("goSmsTemplateUpdate tempDesc         = " + smsTemplateVO.getTempDesc());
		logger.debug("goSmsTemplateUpdate tempContent      = " + smsTemplateVO.getTempContent());
		logger.debug("goSmsTemplateUpdate mergeItem        = " + smsTemplateVO.getMergeItem());
		logger.debug("goSmsTemplateUpdate status           = " + smsTemplateVO.getStatus());
		logger.debug("goSmsTemplateUpdate gubun            = " + smsTemplateVO.getGubun());
		logger.debug("goSmsTemplateUpdate deptNo           = " + smsTemplateVO.getDeptNo());
		logger.debug("goSmsTemplateUpdate mappKakaoTempCnt = " + smsTemplateVO.getMappKakaoTempCnt());
		
		int result = 0;
		
		if(smsTemplateVO.getStatus() == null || "".equals((String)smsTemplateVO.getStatus())) {
			smsTemplateVO.setStatus("000");
		}
		
		if(smsTemplateVO.getMergeItem() != null || !"".equals((String)smsTemplateVO.getMergeItem())) {
			smsTemplateVO.setMergeItem(StringUtil.removeComma(smsTemplateVO.getMergeItem()));
		}
		
		smsTemplateVO.setUpId((String)session.getAttribute("NEO_USER_ID"));
		smsTemplateVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		smsTemplateVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		if(StringUtil.isNull(smsTemplateVO.getUserId())) smsTemplateVO.setUserId((String)session.getAttribute("NEO_USER_ID"));		// 사용자아이디		
		if(!StringUtil.isNull(smsTemplateVO.getSegNoc())) smsTemplateVO.setSegNo(Integer.parseInt( smsTemplateVO.getSegNoc().substring(0, smsTemplateVO.getSegNoc().indexOf("|")) ));// 세그먼트번호(발송대상그룹)
		
		try { 
			result = smsTemplateService.updateSmsTemplateInfo(smsTemplateVO);
		} catch(Exception e) {
			logger.error("smsTemplateService.smsTemplateService Error = " + e);
		}
		
		if(result > 0) {
			model.addAttribute("result","Success");
		} else {
			model.addAttribute("result","Fail");
		}
		
		return "sys/seg/smsTemplateUpdate";
	}
	
	/**
	 * SMS 템플릿 상태를 업데이트 한다.
	 * @param smsTemplateVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/smsTemplateDelete")
	public ModelAndView updateSmsTemplateStatus(@ModelAttribute SmsTemplateVO smsTemplateVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateSmsTemplateStatus status     = " + smsTemplateVO.getStatus());
		logger.debug("updateSmsTemplateStatus tempCd     = " + smsTemplateVO.getTempCd());
		
		smsTemplateVO.setUpId((String)session.getAttribute("NEO_USER_ID"));
		smsTemplateVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));

		int result = 0;
		try {
			result = smsTemplateService.updateSmsTemplateStatus(smsTemplateVO);
		} catch(Exception e) {
			logger.error("smsTemplateService.updateSmsTemplateStatus error = " + e);
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
	
	@Transactional
	@RequestMapping(value="/getSmsTemplateInfo")
	public ModelAndView getTemplateInfo(@ModelAttribute SmsTemplateVO smsTemplateVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getTemplateInfo smsTemplateVO.getTempCd()   = " + smsTemplateVO.getTempCd());
		
		// SMS 알림톡 템플릿 정보 조회
		
		SmsTemplateVO smsTemplateInfo = null;
		
		try {
			smsTemplateInfo = smsTemplateService.getSmsTemplateInfo(smsTemplateVO.getTempCd());
			
		} catch (Exception e) {
			logger.error("smsTemplateService.getSmsTemplateInfo error = " + e);
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(smsTemplateInfo != null && !"".equals(smsTemplateInfo.getTempCd())) {
			map.put("result", "Success");
			map.put("tempCd", smsTemplateInfo.getTempCd());
			map.put("tempNm", smsTemplateInfo.getTempNm());
			map.put("tempContent", smsTemplateInfo.getTempContent());
			map.put("mergeItem", smsTemplateInfo.getMergeItem());
			map.put("segNo", smsTemplateInfo.getSegNo());
		}else {
			map.put("result", "NotExist");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		return modelAndView;
	}
	
	
	/****************************************** SMS 템플릿 내부 팝업 처리 ******************************************/
	
	/**
	 * SMS 템플릿 등록/수정 수신자그룹 목록(팝업)
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
		//searchVO.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		//searchVO.setSearchDeptNo(0);
		
		searchVO.setSearchStatus("000");
		//20240908 주석처리
//		searchVO.setSearchSmsuseYn("Y");
		
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
		
		return "sys/seg/pop/popSegList";
	}
	
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
		//searchVO.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		
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
		
		return "sys/seg/pop/popCampList";
	}

}