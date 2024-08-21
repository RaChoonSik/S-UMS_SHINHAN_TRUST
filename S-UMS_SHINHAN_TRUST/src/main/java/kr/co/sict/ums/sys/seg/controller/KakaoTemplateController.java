/**
 * 작성자 : 김준희
 * 작성일시 : 2022.03.19
 * 설명 : 카카오 알림톡 템플릿 관리 Controller
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
import kr.co.sict.ums.sms.cam.vo.SmsVO;
import kr.co.sict.ums.sys.seg.service.KakaoTemplateService;
import kr.co.sict.ums.sys.seg.service.SmsTemplateService;
import kr.co.sict.util.Code;
import kr.co.sict.util.PageUtil;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;
import kr.co.sict.ums.sys.seg.vo.KakaoTemplateVO;
import kr.co.sict.ums.sys.seg.vo.SmsTemplateVO;

@Controller
@RequestMapping("/sys/seg")
public class KakaoTemplateController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private PropertiesUtil properties;
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private KakaoTemplateService kakaoTemplateService;
	
	@Autowired
	private SmsTemplateService smsTemplateService;
	/**
	 * 카카오 알림톡 템플릿 목록 화면 출력.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/kakaoTemplateListP")
	public String goKakaoTemplateListP(@ModelAttribute KakaoTemplateVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goKakaoTemplateListP searchTempCd  = " + searchVO.getSearchTempCd());
		logger.debug("goKakaoTemplateListP searchTempNm  = " + searchVO.getSearchTempNm());
		logger.debug("goKakaoTemplateListP searchDeptNo  = " + searchVO.getSearchDeptNo());
		logger.debug("goKakaoTemplateListP searchStatus  = " + searchVO.getSearchStatus());
		logger.debug("goKakaoTemplateListP searchUserId  = " + searchVO.getSearchUserId());
		logger.debug("goKakaoTemplateListP searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goKakaoTemplateListP searchEndtDt  = " + searchVO.getSearchEndDt());
		
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
		
		// 카카오 알림톡 템플릿 상태 리스트
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
		
		return "sys/seg/kakaoTemplateListP";
	}
	
	/**
	 * 카카오 알림톡 템플릿 목록 조회
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/kakaoTemplateList")
	public String goKakaoTemplateList(@ModelAttribute KakaoTemplateVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goKakaoTemplateList searchTempCd  = " + searchVO.getSearchTempCd());
		logger.debug("goKakaoTemplateList searchTempNm  = " + searchVO.getSearchTempNm());
		logger.debug("goKakaoTemplateList searchDeptNo  = " + searchVO.getSearchDeptNo());
		logger.debug("goKakaoTemplateList searchStatus  = " + searchVO.getSearchStatus());
		logger.debug("goKakaoTemplateList searchUserId  = " + searchVO.getSearchUserId());
		logger.debug("goKakaoTemplateList searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goKakaoTemplateList searchEndtDt  = " + searchVO.getSearchEndDt());
		
		// 검색 기본값 설정
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
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
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		//  카카오 알림톡 템플릿 목록
		List<KakaoTemplateVO> kakaoTemplateList = null;
		try {
			kakaoTemplateList = kakaoTemplateService.getKakaoTemplateList(searchVO);
		} catch(Exception e) {
			logger.error("kakaoTemplateService.getKakaoTemplateList error = " + e);
		}
		
		if(kakaoTemplateList != null && kakaoTemplateList.size() > 0) {
			totalCount = kakaoTemplateList.get(0).getTotalCount();
		}
		
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);

		model.addAttribute("searchVO", searchVO);						// 검색항목
		model.addAttribute("kakaoTemplateList", kakaoTemplateList);		//  알림톡 템플릿  목록
		model.addAttribute("pageUtil", pageUtil);						// 페이징
		
		return "sys/seg/kakaoTemplateList";
	}
	
	/**
	 * 알림톡 템플릿 신규 등록 화면
	 * @param userCodeGroupVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/kakaoTemplateAddP")
	public String goKakaoTemplateAddP(@ModelAttribute KakaoTemplateVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 부서목록(코드성) 조회
		CodeVO deptVO = new CodeVO();
		deptVO.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(deptVO);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 카카오 알림톡 템플릿 상태 리스트
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
		
		//  API 템플릿 목록
		List<SmsTemplateVO> apiTemplateList = null;
		try {
			apiTemplateList = smsTemplateService.getApiTemplateList(null);
		} catch(Exception e) {
			logger.error("smsTemplateService.getApiTemplateList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);   // 검색항목
		model.addAttribute("deptList", deptList);		   // 부서목록
		model.addAttribute("statusList", statusList);	   // 부서번호
		model.addAttribute("apiTemplateList", apiTemplateList);	   // API 템플릿 리스트 
		
		return "sys/seg/kakaoTemplateAddP";
	} 
	
	 /**
	 * 알림톡 템플릿 정보 등록 (신규등록시)
	 * @param KakaoTemplateVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/kakaoTemplateAdd")
	public String goKakaoTemplateAdd(@ModelAttribute KakaoTemplateVO kakaoTemplateVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goKakaoTemplateAdd tempCd       = " + kakaoTemplateVO.getTempCd());
		logger.debug("goKakaoTemplateAdd tempNm       = " + kakaoTemplateVO.getTempNm());
		logger.debug("goKakaoTemplateAdd tempDesc     = " + kakaoTemplateVO.getTempDesc());
		logger.debug("goKakaoTemplateAdd tempContent  = " + kakaoTemplateVO.getTempContent());
		logger.debug("goKakaoTemplateAdd mergyItem    = " + kakaoTemplateVO.getMergyItem());
		logger.debug("goKakaoTemplateAdd status       = " + kakaoTemplateVO.getStatus());
		logger.debug("goKakaoTemplateAdd deptNo       = " + kakaoTemplateVO.getDeptNo());
		logger.debug("goKakaoTemplateAdd userId       = " + kakaoTemplateVO.getUserId());
		logger.debug("goKakaoTemplateAdd apiTempCd    = " + kakaoTemplateVO.getApiTempCd());
		logger.debug("goKakaoTemplateAdd apiMergeCols = " + kakaoTemplateVO.getApiMergeCols());
		
		int result = 0;
		
		if (kakaoTemplateVO.getStatus() == null || "".equals((String)kakaoTemplateVO.getStatus())) {
			kakaoTemplateVO.setStatus("000");
		}
		
		kakaoTemplateVO.setRegId((String) session.getAttribute("NEO_USER_ID"));
		kakaoTemplateVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		kakaoTemplateVO.setUpId((String)session.getAttribute("NEO_USER_ID"));
		kakaoTemplateVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		kakaoTemplateVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		if(kakaoTemplateVO.getMergyItem() != null || !"".equals((String)kakaoTemplateVO.getMergyItem())) {
			kakaoTemplateVO.setMergyItem(StringUtil.removeComma(kakaoTemplateVO.getMergyItem()));
		}
		
		try {
			result = kakaoTemplateService.insertKakaoTemplateInfo(kakaoTemplateVO);
		} catch(Exception e) {
			logger.error("kakaoTemplateService.insertKakaoTemplateInfo error = " + e);
		}
		
		String[] targetInfo = null;
		String[] targetApiMerge = null;  
		// 카카오 API 템플릿 머지 정보 입력 
		if (result > 0) {
			try {
				if (kakaoTemplateService.deleteApiKakaoTemplateMerge(kakaoTemplateVO) >= 0) {
					if ( kakaoTemplateVO.getApiMergeCols() != null &&  !"".equals(kakaoTemplateVO.getApiMergeCols())) {
						targetApiMerge = kakaoTemplateVO.getApiMergeCols().split(",");
						if (targetApiMerge !=null && targetApiMerge.length > 0 ) {
							try {
								for (int i = 0 ; i < targetApiMerge.length; i ++) {
									KakaoTemplateVO targetApiTemplateMerge = new KakaoTemplateVO();
									String strKakaoMerge = targetApiMerge[i];
									targetInfo = strKakaoMerge.split("\\|");
									targetApiTemplateMerge.setTempCd(kakaoTemplateVO.getTempCd());
									targetApiTemplateMerge.setApiKakaoCol(targetInfo[0]);
									targetApiTemplateMerge.setApiMergeCol(targetInfo[1]);
									targetApiTemplateMerge.setRegDt(kakaoTemplateVO.getRegDt());
									targetApiTemplateMerge.setRegId(kakaoTemplateVO.getRegId());
									targetApiTemplateMerge.setUpDt(kakaoTemplateVO.getUpDt());
									targetApiTemplateMerge.setUpId(kakaoTemplateVO.getUpId());
									
									result = kakaoTemplateService.insertApiKakaoTemplateMerge(targetApiTemplateMerge);
								}
							} catch(Exception e) {
								logger.error("smsCampaignService.insertApiKakaoTemplateMerge error = " + e);
							}
						}
					}
				}
			} catch(Exception e) {
				logger.error("smsCampaignService.deleteKakaoTemplateMerge error = " + e);
			}
		}
		
		
		if(result > 0) {
			model.addAttribute("result","Success");
		} else if(result == -2) {
			model.addAttribute("result","Exist");
		} else {
			model.addAttribute("result","Fail");
		}
		
		return "sys/seg/kakaoTemplateAdd";
	}
	
	/**
	 * 알림톡 템플릿 정보 수정 화면
	 * @param kakaoTemplateVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@Transactional
	@RequestMapping(value="/kakaoTemplateUpdateP")
	public String goKakaoTemplateUpdateP(@ModelAttribute KakaoTemplateVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goKakaoTemplateUpdateP tempCd  = " + searchVO.getTempCd());
		
		// 카카오 알림톡 템플릿 정보 조회
		KakaoTemplateVO kakaoTemplateInfo = null;
		try {
			
			kakaoTemplateInfo = kakaoTemplateService.getKakaoTemplateInfo(searchVO.getTempCd()); 
		} catch(Exception e) {
			logger.error("kakaoTemplateService.getKakaoTemplateInfo error = " + e);
		}
		
		// 카카오 알림톡 템플릿 매핑 정보 조회
		List<KakaoTemplateVO> apiTemplateMergeList = null;
		try {
			
			apiTemplateMergeList = kakaoTemplateService.getApiKakaoTemplateMergeList(searchVO); 
		} catch(Exception e) {
			logger.error("kakaoTemplateService.getApiKakaoTemplateMergeList error = " + e);
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
		userVO.setDeptNo(kakaoTemplateInfo.getDeptNo());
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch(Exception e) {
			logger.error("codeService.getUserList erro = " + e);
		}
		
		// 카카오 알림톡 템플릿 상태 리스트
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
		
		//  API 템플릿 목록
		List<SmsTemplateVO> apiTemplateList = null;
		try {
			apiTemplateList = smsTemplateService.getApiTemplateList(null);
		} catch(Exception e) {
			logger.error("smsTemplateService.getApiTemplateList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);							// 검색항목
		model.addAttribute("kakaoTemplateInfo", kakaoTemplateInfo);			// 카카오 알림톡 템플릿 정보
		model.addAttribute("apiTemplateMergeList", apiTemplateMergeList);	// 카카오 알림톡 템플릿 정보
		model.addAttribute("deptList", deptList);							// 부서목록
		model.addAttribute("userList", userList);							// 사용자 목록
		model.addAttribute("statusList", statusList);						// 상태코드
		model.addAttribute("apiTemplateList", apiTemplateList);	    		// API 템플릿 리스트
		
		return "sys/seg/kakaoTemplateUpdateP";
	}
	
	/**
	 * 카카오 알림톡 템플릿 정보를 수정한다.
	 * @param kakaoTemplateVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/kakaoTemplateUpdate")
	public String goKakaoTemplateUpdate(@ModelAttribute KakaoTemplateVO kakaoTemplateVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goKakaoTemplateUpdate userId        = " + kakaoTemplateVO.getUserId());
		logger.debug("goKakaoTemplateUpdate tempCd        = " + kakaoTemplateVO.getTempCd());
		logger.debug("goKakaoTemplateUpdate tempNm        = " + kakaoTemplateVO.getTempNm());
		logger.debug("goKakaoTemplateUpdate tempDesc      = " + kakaoTemplateVO.getTempDesc());
		logger.debug("goKakaoTemplateUpdate tempContent   = " + kakaoTemplateVO.getTempContent());
		logger.debug("goKakaoTemplateUpdate mergyItem     = " + kakaoTemplateVO.getMergyItem());
		logger.debug("goKakaoTemplateUpdate status        = " + kakaoTemplateVO.getStatus());
		logger.debug("goKakaoTemplateUpdate deptNo        = " + kakaoTemplateVO.getDeptNo());
		
		int result = 0;
		
		if(kakaoTemplateVO.getDeptNo() == 0) {
			if(!"Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				kakaoTemplateVO.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		
		if(kakaoTemplateVO.getStatus() == null || "".equals((String)kakaoTemplateVO.getStatus())) {
			kakaoTemplateVO.setStatus("000");
		}
		
		if(kakaoTemplateVO.getMergyItem() != null || !"".equals((String)kakaoTemplateVO.getMergyItem())) {
			kakaoTemplateVO.setMergyItem(StringUtil.removeComma(kakaoTemplateVO.getMergyItem()));
		}
		
		kakaoTemplateVO.setRegId((String) session.getAttribute("NEO_USER_ID"));
		kakaoTemplateVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		kakaoTemplateVO.setUpId((String)session.getAttribute("NEO_USER_ID"));
		kakaoTemplateVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		kakaoTemplateVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		try { 
			result = kakaoTemplateService.updateKakaoTemplateInfo(kakaoTemplateVO);
		} catch(Exception e) {
			logger.error("kakaoTemplateService.updateKakaoTemplateInfo Error = " + e);
		}
		
		String[] targetInfo = null;
		String[] targetApiMerge = null;  
		// 카카오 API 템플릿 머지 정보 입력 
		if (result > 0) {
			try {
				if (kakaoTemplateService.deleteApiKakaoTemplateMerge(kakaoTemplateVO) >= 0) {
					if ( kakaoTemplateVO.getApiMergeCols() != null &&  !"".equals(kakaoTemplateVO.getApiMergeCols())) {
						targetApiMerge = kakaoTemplateVO.getApiMergeCols().split(",");
						if (targetApiMerge !=null && targetApiMerge.length > 0 ) {
							try {
								for (int i = 0 ; i < targetApiMerge.length; i ++) {
									KakaoTemplateVO targetApiTemplateMerge = new KakaoTemplateVO();
									String strKakaoMerge = targetApiMerge[i];
									targetInfo = strKakaoMerge.split("\\|");
									targetApiTemplateMerge.setTempCd(kakaoTemplateVO.getTempCd());
									targetApiTemplateMerge.setApiKakaoCol(targetInfo[0]);
									targetApiTemplateMerge.setApiMergeCol(targetInfo[1]);
									targetApiTemplateMerge.setRegDt(kakaoTemplateVO.getRegDt());
									targetApiTemplateMerge.setRegId(kakaoTemplateVO.getRegId());
									targetApiTemplateMerge.setUpDt(kakaoTemplateVO.getUpDt());
									targetApiTemplateMerge.setUpId(kakaoTemplateVO.getUpId());
									result = kakaoTemplateService.insertApiKakaoTemplateMerge(targetApiTemplateMerge);
								}
							} catch(Exception e) {
								logger.error("smsCampaignService.insertApiKakaoTemplateMerge error = " + e);
							}
						}
					}
				}
			} catch(Exception e) {
				logger.error("smsCampaignService.deleteKakaoTemplateMerge error = " + e);
			}
		}
		
		
		if(result > 0) {
			model.addAttribute("result","Success");
		} else {
			model.addAttribute("result","Fail");
		}
		
		return "sys/seg/kakaoTemplateUpdate";
	}
	
	/**
	 * 알림톡 템플릿 정보 조회 
	 * @param kakaoTemplateVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@Transactional
	@RequestMapping(value="/getKakaoTemplateInfo")
	public ModelAndView getTemplateInfo(@ModelAttribute KakaoTemplateVO kakaoTemplateVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getTemplateInfo kakaoTemplateVO.getTempCd()  = " + kakaoTemplateVO.getTempCd());

		// 카카오 알림톡 템플릿 정보 조회
		KakaoTemplateVO kakaoTemplateInfo = null;
		try {
			
			kakaoTemplateInfo = kakaoTemplateService.getKakaoTemplateInfo(kakaoTemplateVO.getTempCd()); 
		} catch(Exception e) {
			logger.error("kakaoTemplateService.getKakaoTemplateInfo error = " + e);
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		if(kakaoTemplateInfo != null && !"".equals(kakaoTemplateInfo.getTempCd())){
			map.put("result", "Success");
			map.put("tempCd", kakaoTemplateInfo.getTempCd());
			map.put("tempNm", kakaoTemplateInfo.getTempNm());
			map.put("tempContent", kakaoTemplateInfo.getTempContent());
			map.put("mergeItem", kakaoTemplateInfo.getMergyItem());
		} else {
			map.put("result", "NotExist");
		}
		
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
}