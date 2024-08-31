/* 작성자 : 김준희
 * 작성일시 : 2022.03.31
 * 설명 : SMS통계분석 Controller
 */
package kr.co.sict.ums.sms.ana.controller;

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
import kr.co.sict.ums.com.service.CryptoService;
import kr.co.sict.ums.com.vo.CodeVO;
import kr.co.sict.ums.sms.ana.service.SmsAnalysisService;
import kr.co.sict.ums.sms.ana.vo.SmsSendLogVO;
import kr.co.sict.ums.sms.cam.service.SmsCampaignService;
import kr.co.sict.ums.sms.cam.vo.SmsAttachVO;
import kr.co.sict.ums.sms.cam.vo.SmsCampaignVO;
import kr.co.sict.ums.sms.cam.vo.SmsVO;
import kr.co.sict.ums.sys.seg.service.SmsTemplateService;
import kr.co.sict.ums.sys.seg.vo.SmsTemplateVO;
import kr.co.sict.util.PageUtil;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Controller
@RequestMapping("/sms/ana")
public class SmsAnalysisController {
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private CodeService codeService;

	@Autowired
	private SmsAnalysisService smsAnalysisService;
	
	@Autowired
	private SmsCampaignService smsCampaignService;
	
	@Autowired
	private SmsTemplateService smsTemplateService;
	
	@Autowired
	private CryptoService cryptoService;
	
	@Autowired
	private PropertiesUtil properties;

	/**
	 * SMS발송 현황 화면을 출력한다.
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/smsListP")
	public String goSmsListP(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSmsListP searchTaskNm  = " + searchVO.getSearchTaskNm());
		logger.debug("goSmsListP searchTempNm  = " + searchVO.getSearchTempNm());
		logger.debug("goSmsListP searchCampNm  = " + searchVO.getSearchCampNm());
		logger.debug("goSmsListP searchDeptNo  = " + searchVO.getSearchDeptNo());
		logger.debug("goSmsListP searchUserId  = " + searchVO.getSearchUserId());
		logger.debug("goSmsListP searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goSmsListP searchEndtDt  = " + searchVO.getSearchEndDt());
		logger.debug("goSmsListP searchGubun   = " + searchVO.getSearchGubun());
		
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
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 전송유형 목록
		CodeVO gubun = new CodeVO();
		gubun.setUilang((String)session.getAttribute("NEO_UILANG"));
		gubun.setCdGrp("C119");
		gubun.setCd("000");
		gubun.setUseYn("Y"); 
		List<CodeVO> gubunList = null;
		try {
			gubunList = codeService.getCodeListByUpCd(gubun);
		} catch(Exception e) {
			logger.error("codeService.getCodeListByUpCd[C119] error = " + e);
		}
		
		// 캠페인 목록 조회
		SmsCampaignVO camp = new SmsCampaignVO();
		camp.setUilang((String)session.getAttribute("NEO_UILANG"));
		camp.setSearchStatus("000");
		camp.setPage(1);
		camp.setRows(10000);
		camp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<SmsCampaignVO> smsCampList = null;
		try {
			smsCampList = smsCampaignService.getCampaignList(camp);
		} catch(Exception e) {
			logger.error("smsCampaignService.getCampaignList error = " + e);
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
		model.addAttribute("gubunList", gubunList);		// 문자전송유형
		model.addAttribute("smsCampList",smsCampList);	// 캠페인목록
		model.addAttribute("deptList", deptList);		// 부서
		model.addAttribute("userList", userList);		// 사용자
		
		return "sms/ana/smsListP";
	}
	
	/**
	 * SMS발송 현황 목록을 조회 력한다.
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/smsList")
	public String goSmsList(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		logger.debug("goSmsList searchTaskNm  = " + searchVO.getSearchTaskNm());
		logger.debug("goSmsList searchTempNm  = " + searchVO.getSearchTempNm());
		logger.debug("goSmsList searchCampNm  = " + searchVO.getSearchCampNm());
		logger.debug("goSmsList searchDeptNo  = " + searchVO.getSearchDeptNo());
		logger.debug("goSmsList searchUserId  = " + searchVO.getSearchUserId());
		logger.debug("goSmsList searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goSmsList searchEndtDt  = " + searchVO.getSearchEndDt());
		logger.debug("goSmsList searchGubun   = " + searchVO.getSearchGubun());
		
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
		
		if(searchVO.getSearchDeptNo() == 0) {
			if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo(0);
			} else {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		// 문자발송 내역 조회
		List<SmsSendLogVO> smsSendList = null;
		try {
			smsSendList = smsAnalysisService.getSmsList(searchVO);
		} catch(Exception e) {
			logger.error("smsAnalysisService.getSmsList error = " + e);
		}
		
		if(smsSendList != null && smsSendList.size() > 0) {
			totalCount = smsSendList.get(0).getTotalCount();
		}
		
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("searchVO", searchVO);		// 검색항목
		model.addAttribute("smsSendList", smsSendList);	// 항목
		model.addAttribute("pageUtil", pageUtil);		// 페이징
		
		return "sms/ana/smsList";
	}
	
	/**
	 * 문자 통계발송분석 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/smsAnalListP")
	public String goSmsAnalListP(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSmsAnalListP msgid  = " + searchVO.getMsgid());
		logger.debug("goSmsAnalListP keygen = " + searchVO.getKeygen());
		logger.debug("goSmsAnalListP tempCd = " + searchVO.getTempCd());
		logger.debug("goSmsAnalListP tempNm = " + searchVO.getTempNm());
		
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG")); 
		searchVO.setAdminYn((String) session.getAttribute("NEO_ADMIN_YN"));
		
		SmsSendLogVO smsLogInfo = null; 
		try {
			smsLogInfo = smsAnalysisService.getSmsInfo(searchVO);
		} catch(Exception e) {
			logger.error("smsAnalysisService.getSmsInfo error = " + e);
		}
		
		// 첨부파일 목록
		SmsVO smsInfo = new SmsVO();
		smsInfo.setKeygen(smsLogInfo.getKeygen());
		smsInfo.setMsgid(smsLogInfo.getMsgid());
		
		List<SmsAttachVO> smsAttachList = new ArrayList<SmsAttachVO>();
		try {
			if (smsLogInfo != null) {
				smsAttachList = smsCampaignService.getSmsAttachList(smsInfo);
			}
			//
		} catch(Exception e) {
			logger.error("smsCampaignService.getSmsAttachList error = " + e);
		}
		
		
		// 페이지 설정
		searchVO.setPage(1);
		searchVO.setRows(9999999);
		
		model.addAttribute("searchVO", searchVO);		// 검색조건
		model.addAttribute("smsLogInfo", smsLogInfo);	// SMS 발송 정보
		model.addAttribute("smsAttachList", smsAttachList);	// SMS 첨부파일 정보
		model.addAttribute("imgUploadPath", properties.getProperty("IMG.SMS_UPLOAD_PATH"));	//이미지 업로드 경로 
		
		return "sms/ana/smsAnalListP";
	}
	
	/**
	 * 통계분석 카카오알림분석 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/kakaoListP")
	public String goKakaoListP(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goKakaoListP searchTaskNm  = " + searchVO.getSearchTaskNm());
		logger.debug("goKakaoListP searchTempNm  = " + searchVO.getSearchTempNm());
		logger.debug("goKakaoListP searchCampNm  = " + searchVO.getSearchCampNm());
		logger.debug("goKakaoListP searchDeptNo  = " + searchVO.getSearchDeptNo());
		logger.debug("goKakaoListP searchUserId  = " + searchVO.getSearchUserId());
		logger.debug("goKakaoListP searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goKakaoListP searchEndtDt  = " + searchVO.getSearchEndDt());
		
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
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 캠페인 목록 조회
		SmsCampaignVO camp = new SmsCampaignVO();
		camp.setUilang((String)session.getAttribute("NEO_UILANG"));
		camp.setSearchStatus("000");
		camp.setPage(1);
		camp.setRows(10000);
		camp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<SmsCampaignVO> smsCampList = null;
		try {
			smsCampList = smsCampaignService.getCampaignList(camp);
		} catch(Exception e) {
			logger.error("smsCampaignService.getCampaignList error = " + e);
		}
		
		// 카카오 알림톡 템플릿 목록 조회
		CodeVO kakaoTemplate = new CodeVO();
		List<CodeVO> kakaoTemplateList = null;
		kakaoTemplate.setStatus("000");
		
		try {
			kakaoTemplateList = codeService.getKakaoTemplateList(kakaoTemplate);
		} catch (Exception e) {
			logger.error("codeService.getKakaoTemplateList error = " + e);
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
		
		model.addAttribute("searchVO", searchVO);						// 검색항목
		model.addAttribute("smsCampList",smsCampList);					// 캠페인목록
		model.addAttribute("deptList", deptList);						// 부서
		model.addAttribute("userList", userList);						// 사용자
		model.addAttribute("kakaoTemplateList", kakaoTemplateList);		// 카카오 알림톡 템플릿목록
		
		return "sms/ana/kakaoListP";
	}
	
	/**
	  통계분석 카카오알림분석 목록을 조회한다.
	  @param searchVO
	  @param model
	  @param request
	  @param response
	  @param session
	  @return
	*/ 
	@RequestMapping(value="/kakaoList")
	public String goKakaoList(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goKakaoList searchTaskNm  = " + searchVO.getSearchTaskNm());
		logger.debug("goKakaoList searchTempNm  = " + searchVO.getSearchTempNm());
		logger.debug("goKakaoList searchCampNm  = " + searchVO.getSearchCampNm());
		logger.debug("goKakaoList searchDeptNo  = " + searchVO.getSearchDeptNo());
		logger.debug("goKakaoList searchUserId  = " + searchVO.getSearchUserId());
		logger.debug("goKakaoList searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goKakaoList searchEndtDt  = " + searchVO.getSearchEndDt());
		
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
		
		if(searchVO.getSearchDeptNo() == 0) {
			if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo(0);
			} else {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		searchVO.setSearchGubun("004");
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;

		// 카카오발송 조회
		List<SmsSendLogVO> kakaoList = null;
		try {
			kakaoList = smsAnalysisService.getSmsList(searchVO);
		} catch(Exception e) {
			logger.error("smsAnalysisService.getSmsList error = " + e);
		}
		
		if(kakaoList != null && kakaoList.size() > 0) {
			totalCount = kakaoList.get(0).getTotalCount();
		}
		
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("searchVO", searchVO);		// 검색항목
		model.addAttribute("kakaoList", kakaoList);		// 카카오알림목록
		model.addAttribute("pageUtil", pageUtil);		// 페이징
		
		return "sms/ana/kakaoList";
	}
	
	/**
	 * 통계분석 카카오알림분석 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/kakaoAnalListP")
	public String goKakaoAnalListP(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goKakaoAnalListP msgid       = " + searchVO.getMsgid());
		logger.debug("goKakaoAnalListP keygen      = " + searchVO.getKeygen());
		
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		searchVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		searchVO.setSearchGubun("004");
		
		SmsSendLogVO kakaoLogInfo = null;
		
		try {
			kakaoLogInfo = smsAnalysisService.getSmsInfo(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.getSmsInfo error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO); 			// 검색항목
		model.addAttribute("kakaoLogInfo", kakaoLogInfo);	// 카카오알림분석
		
		return "sms/ana/kakaoAnalListP";
	} 
	
	/**
	 * Sms, 알림톡 성공 실패 팝업 리스트.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popSmsSendResultList")
	public String goPopSmsSendResultList(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopSmsSendResultList msgid     = " + searchVO.getMsgid());
		logger.debug("goPopSmsSendResultList keygen    = " + searchVO.getKeygen());
		logger.debug("goPopSmsSendResultList rsltCd    = " + searchVO.getRsltCd());
		logger.debug("goPopSmsSendResultList smsSendYn = " + searchVO.getSmsSendYn());
		logger.debug("goPopSmsSendResultList phone     = " + searchVO.getPhone());
		logger.debug("goPopSmsSendResultList gubun     = " + searchVO.getGubun());
		
		//연락처 암호화
		if(searchVO.getPhone() != null && !"".equals(searchVO.getPhone().trim())) {
			searchVO.setPhone(cryptoService.getEncrypt("PHONE", searchVO.getPhone()));
		}
		
		List<SmsSendLogVO> smsSendResultList = null;
		try {
			smsSendResultList = smsAnalysisService.getPopSmsSendList(searchVO);
		} catch(Exception e) {
			logger.error("analysisService.goPopSmsSendList error = " + e);
		}

		model.addAttribute("searchVO", searchVO); 					// 검색항목
		model.addAttribute("smsSendResultList", smsSendResultList);	// 발송성공실패 리스트
		
	return "sms/ana/pop/popSmsSendResultList";
	}
	
	/**
	 * 상세로그 화면을 출력 한다. 
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/detailLogListP")
	public String goDetailLogListP(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goDetailLogListP searchStartDt     = " + searchVO.getSearchStartDt());
		logger.debug("goDetailLogListP searchEndtDt      = " + searchVO.getSearchEndDt());
		logger.debug("goDetailLogListP searchGubunNm     = " + searchVO.getSearchGubunNm());
		logger.debug("goDetailLogListP searchCampNm      = " + searchVO.getSearchCampNm());
		logger.debug("goDetailLogListP searchCustId      = " + searchVO.getSearchCustId());
		logger.debug("goDetailLogListP searchCustNm      = " + searchVO.getSearchCustNm());
		logger.debug("goDetailLogListP searchCustPhone   = " + searchVO.getSearchCustPhone());
		logger.debug("goDetailLogListP searchExeUserNm   = " + searchVO.getSearchExeUserNm());
		
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
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		
		// 전송유형 목록
		CodeVO gubun = new CodeVO();
		gubun.setUilang((String)session.getAttribute("NEO_UILANG"));
		gubun.setCdGrp("C119");
		gubun.setCd("000");
		gubun.setUseYn("Y"); 
		List<CodeVO> gubunList = null;
		try {
			gubunList = codeService.getCodeListByUpCd(gubun);
		} catch(Exception e) {
			logger.error("codeService.getCodeListByUpCd[C119] error = " + e);
		}
		
		// 캠페인 목록 조회
		SmsCampaignVO camp = new SmsCampaignVO();
		camp.setUilang((String)session.getAttribute("NEO_UILANG"));
		camp.setSearchStatus("000");
		camp.setPage(1);
		camp.setRows(10000);
		camp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<SmsCampaignVO> smsCampList = null;
		try {
			smsCampList = smsCampaignService.getCampaignList(camp);
		} catch(Exception e) {
			logger.error("smsCampaignService.getCampaignList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);		// 검색항목
		model.addAttribute("gubunList", gubunList);		// 문자전송유형
		model.addAttribute("smsCampList",smsCampList);	// 캠페인목록
		model.addAttribute("imgUploadPath", properties.getProperty("IMG.SMS_UPLOAD_PATH"));
		
		return "sms/ana/detailLogListP";
	}
	/**
	 * 상세로그 화면 목록을 출력 한다. 
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/detailLogList")
	public String goDetailLogList(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goDetailLogListP searchStartDt     = " + searchVO.getSearchStartDt());
		logger.debug("goDetailLogListP searchEndtDt      = " + searchVO.getSearchEndDt());
		logger.debug("goDetailLogListP searchGubunNm     = " + searchVO.getSearchGubunNm());
		logger.debug("goDetailLogListP searchCampNm      = " + searchVO.getSearchCampNm());
		logger.debug("goDetailLogListP searchCustId      = " + searchVO.getSearchCustId());
		logger.debug("goDetailLogListP searchCustNm      = " + searchVO.getSearchCustNm());
		logger.debug("goDetailLogListP searchCustPhone   = " + searchVO.getSearchCustPhone());
		logger.debug("goDetailLogListP searchExeUserNm   = " + searchVO.getSearchExeUserNm());
		
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
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		//메세지 구분 형식
		String gubunNms;
		String[] arrGubunNms = null;
		
		if(searchVO.getSearchGubunNm().length() > 1) {
			gubunNms = searchVO.getSearchGubunNm();
			arrGubunNms = gubunNms.split(",");
			searchVO.setArrGubunNms(arrGubunNms);
		}
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		// 전송유형 목록
		CodeVO gubun = new CodeVO();
		gubun.setUilang((String)session.getAttribute("NEO_UILANG"));
		gubun.setCdGrp("C119");
		gubun.setCd("000");
		gubun.setUseYn("Y"); 
		List<CodeVO> gubunList = null;
		try {
			gubunList = codeService.getCodeListByUpCd(gubun);
		} catch(Exception e) {
			logger.error("codeService.getCodeListByUpCd[C119] error = " + e);
		}
		
		// 캠페인 목록 조회
		SmsCampaignVO camp = new SmsCampaignVO();
		camp.setUilang((String)session.getAttribute("NEO_UILANG"));
		camp.setSearchStatus("000");
		camp.setPage(1);
		camp.setRows(10000);
		camp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<SmsCampaignVO> smsCampList = null;
		try {
			smsCampList = smsCampaignService.getCampaignList(camp);
		} catch(Exception e) {
			logger.error("smsCampaignService.getCampaignList error = " + e);
		
		}
		
		if(searchVO.getSearchCustPhone().length() > 0) {
			searchVO.setSearchCustPhone(cryptoService.getEncrypt("PHONE",searchVO.getSearchCustPhone()));
		}
		
		//상세로그 리스트 
		List<SmsSendLogVO> sendLogList = null;
		try {
			sendLogList = smsAnalysisService.getSmsSendLogList(searchVO);
		} catch (Exception e) {
			logger.error("smsCampaignService.getSmsSendLogList error = " + e);
		}
		
		//sms 첨부파일 리스트
		if (sendLogList.size()> 0) {
			
			for(int i=0; i < sendLogList.size(); i++) {
				SmsVO smsInfo = new SmsVO();
				smsInfo.setKeygen(sendLogList.get(i).getKeygen());
				smsInfo.setMsgid(sendLogList.get(i).getMsgid());
				
				List<SmsAttachVO> smsAttachList = new ArrayList<SmsAttachVO>();
				try {
					smsAttachList = smsCampaignService.getSmsAttachList(smsInfo);
					String strAttachList = "";
					if (smsAttachList.size()> 0) {
						
						for(int j=0; j<smsAttachList.size(); j++) {
							strAttachList += smsAttachList.get(j).getAttFlPath() + ",";
						}
						if(!"".equals(strAttachList)){
							
							strAttachList = strAttachList.substring(0, strAttachList.length()-1);
							sendLogList.get(i).setAttachFileList(strAttachList);
						}
					}
				} catch(Exception e) {
					logger.error("smsCampaignService.getSmsAttachList error = " + e);
				}
			} 
		}
		
		if(sendLogList != null && sendLogList.size() > 0) {
			totalCount = sendLogList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("gubunList", gubunList);			// 문자전송유형
		model.addAttribute("smsCampList",smsCampList);		// 캠페인목록
		model.addAttribute("sendLogList", sendLogList);		// 상세로그 리스트 
		model.addAttribute("sendLogList", sendLogList);		// 상세로그 리스트 
		model.addAttribute("pageUtil", pageUtil);			// 페이징 
		
		return "sms/ana/detailLogList";
	}
	
	/**
	 * SMS message 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/getSmsMessage")
	public ModelAndView goSmsPreview(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSmsPreview msgId       = " + searchVO.getMsgid());
		logger.debug("goSmsPreview keygen      = " + searchVO.getKeygen());
		logger.debug("goSmsPreview cmid        = " + searchVO.getCmid());
		
		SmsSendLogVO smslogVO = new SmsSendLogVO();
		
		boolean result = false;
		String smsMessage = ""; 

		String msgId  = searchVO.getMsgid();
		String keygen = searchVO.getKeygen();
		String cmid   = searchVO.getCmid();
		
		if(!"".equals(msgId) && msgId != null && !"".equals(keygen) && keygen != null && !"".equals(cmid) && cmid != null) {
			smslogVO.setMsgid(msgId);
			smslogVO.setKeygen(keygen);
			smslogVO.setCmid(cmid);
		
			try {
				smsMessage = smsAnalysisService.getSmsMessage(smslogVO); 
			} catch (Exception e) {
				logger.error("smsAnalysisService.getSmsMessage error = " + e);
			}
		
			if(!"".equals(smsMessage) && smsMessage != null) {
				result = true;
			}else {
				result = false;
			}
		
		}else {
			result = false;
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result) {
			map.put("result", "Success");
			map.put("smsMessage", smsMessage);
		} else {
			map.put("result", "Fail");
		}
		
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	/****************************************** SMS 발송 내부 팝업 처리 ******************************************/
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
		searchVO.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		
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
		
		return "sms/ana/pop/popCampList";
	}
	
	/**
	 * SMS 등록/수정 캠페인 등록(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popCampAdd")
	public String goPopCampAdd(@ModelAttribute SmsCampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopCampAdd searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goPopCampAdd searchEndDt   = " + searchVO.getSearchEndDt());
		logger.debug("goPopCampAdd searchCampNm  = " + searchVO.getSearchCampNm());
		
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
		
		model.addAttribute("searchVO", searchVO);		// 검색항목
		model.addAttribute("campTyList", campTyList);	// 캠페인목적
		
		return "sms/ana/pop/popCampAdd";
	}
	
	/**
	 * SMS 등록/수정 캠페인 수정(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popCampUpdate")
	public String goPopCampUpdate(@ModelAttribute SmsCampaignVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopCampUpdate campNo        = " + searchVO.getCampNo());
		
		// 캠페인 정보 조회
		SmsCampaignVO campInfo = null;
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		try {
			campInfo = smsCampaignService.getCampaignInfo(searchVO);
		} catch(Exception e) {
			logger.error("campaignService.getCampaignInfo error = " + e);
		}
		
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
		
		model.addAttribute("searchVO", searchVO);		// 검색항목
		model.addAttribute("campInfo", campInfo);		// 캠페인정보
		model.addAttribute("campTyList", campTyList);	// 캠페인목적
		model.addAttribute("statusList", statusList);	// 캠페인상태
		
		return "sms/ana/pop/popCampUpdate";
	}
	
	/**
	 * 캠페인별 문자 분석 화면
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	
	@RequestMapping(value="/campSmsListP")
	public String goCampSmsListP(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goCampSmsListP searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goCampSmsListP searchEndtDt  = " + searchVO.getSearchEndDt());
		logger.debug("goCampSmsListP searchDeptNo  = " + searchVO.getSearchDeptNo());
		logger.debug("goCampSmsListP searchUserId  = " + searchVO.getSearchUserId());
		logger.debug("goCampSmsListP searchTempCd  = " + searchVO.getSearchTempCd());
		
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
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
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
		
		List<SmsTemplateVO> tempList = null;
		try {
			tempList = smsTemplateService.getSmsTemplateSimpleList();
		} catch(Exception e) {
			logger.error("smsTemplateService.getSmsTemplateSimpleList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);		// 검색항목
		model.addAttribute("deptList", deptList);		// 부서
		model.addAttribute("userList", userList);		// 사용자
		model.addAttribute("tempList", tempList);		// 템플릿 리스트 
		
		return "sms/ana/campSmsListP";
	}
	
	/**
	 * 캠페인별 문자 분석 화면 목록을 조회 
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/campSmsList")
	public String goCampSmsList(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		logger.debug("goCampSmsList searchTempCd  = " + searchVO.getSearchTempCd());
		logger.debug("goCampSmsList searchDeptNo  = " + searchVO.getSearchDeptNo());
		logger.debug("goCampSmsList searchUserId  = " + searchVO.getSearchUserId());
		logger.debug("goCampSmsList searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goCampSmsList searchEndtDt  = " + searchVO.getSearchEndDt());
		
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
		
		if(searchVO.getSearchDeptNo() == 0) {
			if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo(0);
			} else {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		// 문자발송 내역 조회
		List<SmsSendLogVO> campSmsList = null;
		try {
			campSmsList = smsAnalysisService.getCampSmsList(searchVO);
		} catch(Exception e) {
			logger.error("smsAnalysisService.getCampSmsList error = " + e);
		}
		
		if(campSmsList != null && campSmsList.size() > 0) {
			totalCount = campSmsList.get(0).getTotalCount();
		}
		
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("searchVO", searchVO);		// 검색항목
		model.addAttribute("campSmsList", campSmsList);	// 항목
		model.addAttribute("pageUtil", pageUtil);		// 페이징
		
		return "sms/ana/campSmsList";
	}
	
	/**
	 * 캠페인별 발송이력 -> 문자 발송 현황 화면을 출력한다.
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/smsSendListP")
	public String goSmsSendListP(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goSmsSendListP searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goSmsSendListP searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("goSmsSendListP searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goSmsSendListP searchCampNm     = " + searchVO.getSearchCampNm());
		logger.debug("goSmsSendListP searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goSmsSendListP searchUserId     = " + searchVO.getSearchUserId());
		logger.debug("goSmsSendListP getSearchGubun   = " + searchVO.getSearchGubun());
		logger.debug("goSmsSendListP getSearchSmsName = " + searchVO.getSearchSmsName());
		
		
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
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));

		// 전송유형 목록
		CodeVO gubun = new CodeVO();
		gubun.setUilang((String) session.getAttribute("NEO_UILANG"));
		gubun.setCdGrp("C119");
		gubun.setCd("000");
		gubun.setUseYn("Y");
		List<CodeVO> gubunList = null;
		try {
			gubunList = codeService.getCodeListByUpCd(gubun);
		} catch (Exception e) {
			logger.error("codeService.getCodeListByUpCd[C119] error = " + e);
		}

		// 캠페인 목록
		SmsCampaignVO smsCampVO = new SmsCampaignVO();
		smsCampVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		smsCampVO.setSearchStatus("000");
		smsCampVO.setPage(1);
		smsCampVO.setRows(10000);
		smsCampVO.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<SmsCampaignVO> smsCampList = null;
		try {
			smsCampList = codeService.getCampaignSmsList(smsCampVO);
		} catch (Exception e) {
			logger.error("codeService.getCampaignSmsList error = " + e);
		}
		
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
		userVO.setDeptNo("Y".equals((String) session.getAttribute("NEO_ADMIN_YN")) ? 0
				: (int) session.getAttribute("NEO_DEPT_NO"));
		userVO.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch (Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}

		// 발송상태코드 목록
		CodeVO statusVO = new CodeVO();
		statusVO.setUilang("000");
		statusVO.setCdGrp("C118");
		statusVO.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(statusVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error = " + e);
		}

		// 문자상태코드 목록
		CodeVO smsStatus = new CodeVO();
		smsStatus.setUilang((String) session.getAttribute("NEO_UILANG"));
		smsStatus.setCdGrp("C116");
		smsStatus.setUseYn("Y");
		List<CodeVO> smsStatusList = null;
		try {
			smsStatusList = codeService.getCodeList(smsStatus);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error = " + e);
		}

		model.addAttribute("searchVO", searchVO); // 검색항목
		model.addAttribute("gubunList", gubunList); // 문자전송유형
		model.addAttribute("smsCampList", smsCampList); // 캠페인목록
		model.addAttribute("deptList", deptList); // 사용자그룹목록
		model.addAttribute("userList", userList); // 사용자목록
		model.addAttribute("statusList", statusList); // 문자발송상태목록
		model.addAttribute("smsStatusList", smsStatusList); // 문자상태목록
	
		return "sms/ana/smsSendListP";
	}
	
	/**
	 * 캡페인별 발송이력 -> 문자 발송 현황 목록을 조회 력한다.
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/smsSendList")
	public String goSmsSendList(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		logger.debug("goSmsSendList searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goSmsSendList searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("goSmsSendList searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goSmsSendList searchTaskNm     = " + searchVO.getSearchTaskNm());
		logger.debug("goSmsSendList searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goSmsSendList searchStatus     = " + searchVO.getSearchStatus());
		logger.debug("goSmsSendList searchSmsStatus  = " + searchVO.getSearchSmsStatus());
		logger.debug("goSmsSendList searchGubun      = " + searchVO.getSearchGubun());
		
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
		
		if(searchVO.getSearchDeptNo() == 0) {
			if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo(0);
			} else {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;

		// 문자 목록 조회
		List<SmsSendLogVO> smsSendList = null;
		try {
			smsSendList = smsAnalysisService.getSmsList(searchVO);
		} catch(Exception e) {
			logger.error("smsCampaignService.getSmsList error = " + e);
		}
		
		if(smsSendList != null && smsSendList.size() > 0) {
			totalCount = smsSendList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		
		model.addAttribute("searchVO", searchVO);				// 검색 항목
		model.addAttribute("smsSendList", smsSendList);			// 문자발송 목록
		model.addAttribute("pageUtil", pageUtil);				// 페이징
		
		return "sms/ana/smsSendList";
	}
	
	
	/**
	 * 캠페인별 카카오통계 화면 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	
	@RequestMapping(value="/campKakaoListP")
	public String goCampKakaoListP(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goCampKakaoListP searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goCampKakaoListP searchEndtDt  = " + searchVO.getSearchEndDt());
		logger.debug("goCampSmsListP searchTempCd  = " + searchVO.getSearchTempCd());
		logger.debug("goCampKakaoListP searchDeptNo  = " + searchVO.getSearchDeptNo());
		logger.debug("goCampKakaoListP searchUserId  = " + searchVO.getSearchUserId());
		
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
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
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
		
		CodeVO template = new CodeVO();
		template.setStatus("000");
		List<CodeVO> tempList = null;
		try {
			tempList = codeService.getKakaoTemplateList(template);
		} catch(Exception e) {
			logger.error("smsTemplateService.getSmsTemplateSimpleList error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);		// 검색항목
		model.addAttribute("deptList", deptList);		// 부서
		model.addAttribute("userList", userList);		// 사용자
		model.addAttribute("tempList", tempList);		// 템플릿 리스트 
		
		return "sms/ana/campKakaoListP";
	}
	
	
	/**
	 * 알림톡 발송 현황 목록을 조회 력한다.
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/campKakaoList")
	public String goCampKakaoList(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		logger.debug("goCampKakaoList searchTaskNm  = " + searchVO.getSearchTaskNm());
		logger.debug("goCampKakaoList searchTempNm  = " + searchVO.getSearchTempNm());
		logger.debug("goCampKakaoList searchCampNm  = " + searchVO.getSearchCampNm());
		logger.debug("goCampKakaoList searchDeptNo  = " + searchVO.getSearchDeptNo());
		logger.debug("goCampKakaoList searchUserId  = " + searchVO.getSearchUserId());
		logger.debug("goCampKakaoList searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goCampKakaoList searchEndtDt  = " + searchVO.getSearchEndDt());
		logger.debug("goCampKakaoList searchCampNo  = " + searchVO.getSearchCampNo());
		
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
		
		if(searchVO.getSearchDeptNo() == 0) {
			if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo(0);
			} else {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		//카카오 구분값 설정
		searchVO.setGubun("004");

		// 문자발송 내역 조회
		List<SmsSendLogVO> campKakaoList = null;
		try {
			campKakaoList = smsAnalysisService.getCampSmsList(searchVO);
		} catch(Exception e) {
			logger.error("smsAnalysisService.getSmsList error = " + e);
		}
		
		if(campKakaoList != null && campKakaoList.size() > 0) {
			totalCount = campKakaoList.get(0).getTotalCount();
		}
		
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("searchVO", searchVO);				// 검색항목
		model.addAttribute("campKakaoList", campKakaoList);		// 항목
		model.addAttribute("pageUtil", pageUtil);				// 페이징
		
		return "sms/ana/campKakaoList";
	}
	
	/**
	 * 캠페인별 알림톡 발송이력 -> 알림톡 발송 현황 화면을 출력한다.
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/kakaoSendListP")
	public String goKakaoSendListP(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goKakaoSendListP searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goKakaoSendListP searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("goKakaoSendListP searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goKakaoSendListP searchCampNm     = " + searchVO.getSearchCampNm());
		logger.debug("goKakaoSendListP searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goKakaoSendListP searchUserId     = " + searchVO.getSearchUserId());
		logger.debug("goKakaoSendListP getSearchGubun   = " + searchVO.getSearchGubun());
		logger.debug("goKakaoSendListP getSearchSmsName = " + searchVO.getSearchSmsName());
		
		
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
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));

		// 전송유형 목록
		CodeVO gubun = new CodeVO();
		gubun.setUilang((String) session.getAttribute("NEO_UILANG"));
		gubun.setCdGrp("C119");
		gubun.setCd("000");
		gubun.setUseYn("Y");
		List<CodeVO> gubunList = null;
		try {
			gubunList = codeService.getCodeListByUpCd(gubun);
		} catch (Exception e) {
			logger.error("codeService.getCodeListByUpCd[C119] error = " + e);
		}

		// 캠페인 목록
		SmsCampaignVO smsCampVO = new SmsCampaignVO();
		smsCampVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		smsCampVO.setSearchStatus("000");
		smsCampVO.setPage(1);
		smsCampVO.setRows(10000);
		smsCampVO.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<SmsCampaignVO> smsCampList = null;
		try {
			smsCampList = codeService.getCampaignSmsList(smsCampVO);
		} catch (Exception e) {
			logger.error("codeService.getCampaignSmsList error = " + e);
		}
		
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
		userVO.setDeptNo("Y".equals((String) session.getAttribute("NEO_ADMIN_YN")) ? 0
				: (int) session.getAttribute("NEO_DEPT_NO"));
		userVO.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch (Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}

		// 발송상태코드 목록
		CodeVO statusVO = new CodeVO();
		statusVO.setUilang("000");
		statusVO.setCdGrp("C118");
		statusVO.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(statusVO);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error = " + e);
		}

		// 문자상태코드 목록
		CodeVO smsStatus = new CodeVO();
		smsStatus.setUilang((String) session.getAttribute("NEO_UILANG"));
		smsStatus.setCdGrp("C116");
		smsStatus.setUseYn("Y");
		List<CodeVO> smsStatusList = null;
		try {
			smsStatusList = codeService.getCodeList(smsStatus);
		} catch (Exception e) {
			logger.error("codeService.getCodeList error = " + e);
		}

		model.addAttribute("searchVO", searchVO); // 검색항목
		model.addAttribute("gubunList", gubunList); // 문자전송유형
		model.addAttribute("smsCampList", smsCampList); // 캠페인목록
		model.addAttribute("deptList", deptList); // 사용자그룹목록
		model.addAttribute("userList", userList); // 사용자목록
		model.addAttribute("statusList", statusList); // 문자발송상태목록
		model.addAttribute("smsStatusList", smsStatusList); // 문자상태목록
	
		return "sms/ana/kakaoSendListP";
	}
	
	/**
	 * 캡페인별 알림톡 발송이력 -> 알림톡 발송 현황 목록을 조회 력한다.
	 * 
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/kakaoSendList")
	public String goKakaoSendList(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		logger.debug("goKakaoSendList searchStartDt    = " + searchVO.getSearchStartDt());
		logger.debug("goKakaoSendList searchEndDt      = " + searchVO.getSearchEndDt());
		logger.debug("goKakaoSendList searchDeptNo     = " + searchVO.getSearchDeptNo());
		logger.debug("goKakaoSendList searchTaskNm     = " + searchVO.getSearchTaskNm());
		logger.debug("goKakaoSendList searchCampNo     = " + searchVO.getSearchCampNo());
		logger.debug("goKakaoSendList searchStatus     = " + searchVO.getSearchStatus());
		logger.debug("goKakaoSendList searchSmsStatus  = " + searchVO.getSearchSmsStatus());
		logger.debug("goKakaoSendList searchGubun      = " + searchVO.getSearchGubun());
		
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
		
		if(searchVO.getSearchDeptNo() == 0) {
			if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo(0);
			} else {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		searchVO.setSearchGubun("004");
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;

		// 카카오발송 조회
		List<SmsSendLogVO> kakaoSendList = null;
		try {
			kakaoSendList = smsAnalysisService.getSmsList(searchVO);
		} catch(Exception e) {
			logger.error("smsAnalysisService.getSmsList error = " + e);
		}
		
		if(kakaoSendList != null && kakaoSendList.size() > 0) {
			totalCount = kakaoSendList.get(0).getTotalCount();
		}
		
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("searchVO", searchVO);					// 검색항목
		model.addAttribute("kakaoSendList", kakaoSendList);		// 카카오알림목록
		model.addAttribute("pageUtil", pageUtil);				// 페이징
		
		return "sms/ana/kakaoSendList";
	}
	
	
    /**
     * SMS발송 현황 화면을 출력한다.
     * 
     * @param searchVO
     * @param model
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping(value="/smsCampListP")
    public String goSmsCampListP(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        logger.debug("goSmsCampListP searchTaskNm  = " + searchVO.getSearchTaskNm());
        logger.debug("goSmsCampListP searchTempNm  = " + searchVO.getSearchTempNm());
        logger.debug("goSmsCampListP searchCampNm  = " + searchVO.getSearchCampNm());
        logger.debug("goSmsCampListP searchDeptNo  = " + searchVO.getSearchDeptNo());
        logger.debug("goSmsCampListP searchUserId  = " + searchVO.getSearchUserId());
        logger.debug("goSmsCampListP searchStartDt = " + searchVO.getSearchStartDt());
        logger.debug("goSmsCampListP searchEndtDt  = " + searchVO.getSearchEndDt());
        logger.debug("goSmsCampListP searchGubun   = " + searchVO.getSearchGubun());
        
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
        searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
        
        // 전송유형 목록
        CodeVO gubun = new CodeVO();
        gubun.setUilang((String)session.getAttribute("NEO_UILANG"));
        gubun.setCdGrp("C119");
        gubun.setCd("000");
        gubun.setUseYn("Y"); 
        List<CodeVO> gubunList = null;
        try {
            gubunList = codeService.getCodeListByUpCd(gubun);
        } catch(Exception e) {
            logger.error("codeService.getCodeListByUpCd[C119] error = " + e);
        }
        
        // 캠페인 목록 조회
        SmsCampaignVO camp = new SmsCampaignVO();
        camp.setUilang((String)session.getAttribute("NEO_UILANG"));
        camp.setSearchStatus("000");
        camp.setPage(1);
        camp.setRows(10000);
        camp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
        List<SmsCampaignVO> smsCampList = null;
        try {
            smsCampList = smsCampaignService.getCampaignList(camp);
        } catch(Exception e) {
            logger.error("smsCampaignService.getCampaignList error = " + e);
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
        
        model.addAttribute("searchVO", searchVO);       // 검색항목
        model.addAttribute("gubunList", gubunList);     // 문자전송유형
        model.addAttribute("smsCampList",smsCampList);  // 캠페인목록
        model.addAttribute("deptList", deptList);       // 부서
        model.addAttribute("userList", userList);       // 사용자
        
        return "sms/ana/smsCampListP";
    }
    
    /**
     * SMS발송 현황 목록을 조회 력한다.
     * 
     * @param searchVO
     * @param model
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping(value = "/smsCampList")
    public String goSmsCampList(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
        logger.debug("goSmsCampList searchTaskNm  = " + searchVO.getSearchTaskNm());
        logger.debug("goSmsCampList searchTempNm  = " + searchVO.getSearchTempNm());
        logger.debug("goSmsCampList searchCampNm  = " + searchVO.getSearchCampNm());
        logger.debug("goSmsCampList searchDeptNo  = " + searchVO.getSearchDeptNo());
        logger.debug("goSmsCampList searchUserId  = " + searchVO.getSearchUserId());
        logger.debug("goSmsCampList searchStartDt = " + searchVO.getSearchStartDt());
        logger.debug("goSmsCampList searchEndtDt  = " + searchVO.getSearchEndDt());
        logger.debug("goSmsCampList searchGubun   = " + searchVO.getSearchGubun());
        
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
        
        if(searchVO.getSearchDeptNo() == 0) {
            if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
                searchVO.setSearchDeptNo(0);
            } else {
                searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
            }
        }
        searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
        
        // 페이지 설정
        int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
        int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
        searchVO.setPage(page);
        searchVO.setRows(rows);
        searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
        int totalCount = 0;
        
        // 문자발송 내역 조회
        List<SmsSendLogVO> smsSendList = null;
        try {
            smsSendList = smsAnalysisService.getSmsCampList(searchVO);
        } catch(Exception e) {
            logger.error("smsAnalysisService.getSmsCampList error = " + e);
        }
        
        if(smsSendList != null && smsSendList.size() > 0) {
            totalCount = smsSendList.get(0).getTotalCount();
        }
        
        PageUtil pageUtil = new PageUtil();
        pageUtil.init(request, searchVO.getPage(), totalCount, rows);
        
        model.addAttribute("searchVO", searchVO);       // 검색항목
        model.addAttribute("smsSendList", smsSendList); // 항목
        model.addAttribute("pageUtil", pageUtil);       // 페이징
        
        return "sms/ana/smsCampList";
    }
    
    /**
     * 통계분석 카카오알림분석 화면을 출력한다.
     * @param searchVO
     * @param model
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping(value="/kakaoCampListP")
    public String goKakaoCampListP(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        logger.debug("goKakaoCampListP searchTaskNm  = " + searchVO.getSearchTaskNm());
        logger.debug("goKakaoCampListP searchTempNm  = " + searchVO.getSearchTempNm());
        logger.debug("goKakaoCampListP searchCampNm  = " + searchVO.getSearchCampNm());
        logger.debug("goKakaoCampListP searchDeptNo  = " + searchVO.getSearchDeptNo());
        logger.debug("goKakaoCampListP searchUserId  = " + searchVO.getSearchUserId());
        logger.debug("goKakaoCampListP searchStartDt = " + searchVO.getSearchStartDt());
        logger.debug("goKakaoCampListP searchEndtDt  = " + searchVO.getSearchEndDt());
        
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
        searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
        
        // 캠페인 목록 조회
        SmsCampaignVO camp = new SmsCampaignVO();
        camp.setUilang((String)session.getAttribute("NEO_UILANG"));
        camp.setSearchStatus("000");
        camp.setPage(1);
        camp.setRows(10000);
        camp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
        List<SmsCampaignVO> smsCampList = null;
        try {
            smsCampList = smsCampaignService.getCampaignList(camp);
        } catch(Exception e) {
            logger.error("smsCampaignService.getCampaignList error = " + e);
        }
        
        // 카카오 알림톡 템플릿 목록 조회
        CodeVO kakaoTemplate = new CodeVO();
        List<CodeVO> kakaoTemplateList = null;
        kakaoTemplate.setStatus("000");
        
        try {
            kakaoTemplateList = codeService.getKakaoTemplateList(kakaoTemplate);
        } catch (Exception e) {
            logger.error("codeService.getKakaoTemplateList error = " + e);
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
        
        model.addAttribute("searchVO", searchVO);                       // 검색항목
        model.addAttribute("smsCampList",smsCampList);                  // 캠페인목록
        model.addAttribute("deptList", deptList);                       // 부서
        model.addAttribute("userList", userList);                       // 사용자
        model.addAttribute("kakaoTemplateList", kakaoTemplateList);     // 카카오 알림톡 템플릿목록
        
        return "sms/ana/kakaoCampListP";
    }
    
    /**
      통계분석 카카오알림분석 목록을 조회한다.
      @param searchVO
      @param model
      @param request
      @param response
      @param session
      @return
    */ 
    @RequestMapping(value="/kakaoCampList")
    public String goKakaoCampList(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        logger.debug("goKakaoCampList searchTaskNm  = " + searchVO.getSearchTaskNm());
        logger.debug("goKakaoCampList searchTempNm  = " + searchVO.getSearchTempNm());
        logger.debug("goKakaoCampList searchCampNm  = " + searchVO.getSearchCampNm());
        logger.debug("goKakaoCampList searchDeptNo  = " + searchVO.getSearchDeptNo());
        logger.debug("goKakaoCampList searchUserId  = " + searchVO.getSearchUserId());
        logger.debug("goKakaoCampList searchStartDt = " + searchVO.getSearchStartDt());
        logger.debug("goKakaoCampList searchEndtDt  = " + searchVO.getSearchEndDt());
        
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
        
        if(searchVO.getSearchDeptNo() == 0) {
            if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
                searchVO.setSearchDeptNo(0);
            } else {
                searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
            }
        }
        searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
        searchVO.setSearchGubun("004");
        
        // 페이지 설정
        int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
        int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
        searchVO.setPage(page);
        searchVO.setRows(rows);
        searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
        int totalCount = 0;

        // 카카오발송 조회
        List<SmsSendLogVO> kakaoList = null;
        try {
            kakaoList = smsAnalysisService.getSmsCampList(searchVO);
        } catch(Exception e) {
            logger.error("smsAnalysisService.getSmsCampList error = " + e);
        }
        
        if(kakaoList != null && kakaoList.size() > 0) {
            totalCount = kakaoList.get(0).getTotalCount();
        }
        
        PageUtil pageUtil = new PageUtil();
        pageUtil.init(request, searchVO.getPage(), totalCount, rows);
        
        model.addAttribute("searchVO", searchVO);       // 검색항목
        model.addAttribute("kakaoList", kakaoList);     // 카카오알림목록
        model.addAttribute("pageUtil", pageUtil);       // 페이징
        
        return "sms/ana/kakaoCampList";
    }
    
    /**
     * 통계분석 카카오알림분석 화면을 출력한다.
     * @param searchVO
     * @param model
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping(value="/kakaoCampSendListP")
    public String goKakaoCampSendListP(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        logger.debug("goKakaoCampSendListP searchTaskNm  = " + searchVO.getSearchTaskNm());
        logger.debug("goKakaoCampSendListP searchTempNm  = " + searchVO.getSearchTempNm());
        logger.debug("goKakaoCampSendListP searchCampNm  = " + searchVO.getSearchCampNm());
        logger.debug("goKakaoCampSendListP searchDeptNo  = " + searchVO.getSearchDeptNo());
        logger.debug("goKakaoCampSendListP searchUserId  = " + searchVO.getSearchUserId());
        logger.debug("goKakaoCampSendListP searchStartDt = " + searchVO.getSearchStartDt());
        logger.debug("goKakaoCampSendListP searchEndtDt  = " + searchVO.getSearchEndDt());
        logger.debug("goKakaoCampSendListP campNo        = " + searchVO.getCampNo());
        
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
        searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
        
        // 캠페인 목록 조회
        SmsCampaignVO camp = new SmsCampaignVO();
        camp.setUilang((String)session.getAttribute("NEO_UILANG"));
        camp.setSearchStatus("000");
        camp.setPage(1);
        camp.setRows(10000);
        camp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
        List<SmsCampaignVO> smsCampList = null;
        try {
            smsCampList = smsCampaignService.getCampaignList(camp);
        } catch(Exception e) {
            logger.error("smsCampaignService.getCampaignList error = " + e);
        }
        
        // 카카오 알림톡 템플릿 목록 조회
        CodeVO kakaoTemplate = new CodeVO();
        List<CodeVO> kakaoTemplateList = null;
        kakaoTemplate.setStatus("000");
        
        try {
            kakaoTemplateList = codeService.getKakaoTemplateList(kakaoTemplate);
        } catch (Exception e) {
            logger.error("codeService.getKakaoTemplateList error = " + e);
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
        
        model.addAttribute("searchVO", searchVO);                       // 검색항목
        model.addAttribute("smsCampList",smsCampList);                  // 캠페인목록
        model.addAttribute("deptList", deptList);                       // 부서
        model.addAttribute("userList", userList);                       // 사용자
        model.addAttribute("kakaoTemplateList", kakaoTemplateList);     // 카카오 알림톡 템플릿목록
        
        return "sms/ana/kakaoCampSendListP";
    }
    
    /**
      통계분석 카카오알림분석 목록을 조회한다.
      @param searchVO
      @param model
      @param request
      @param response
      @param session
      @return
    */ 
    @RequestMapping(value="/kakaoCampSendList")
    public String goKakaoCampSendList(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        logger.debug("goKakaoCampSendList searchTaskNm  = " + searchVO.getSearchTaskNm());
        logger.debug("goKakaoCampSendList searchTempNm  = " + searchVO.getSearchTempNm());
        logger.debug("goKakaoCampSendList searchCampNm  = " + searchVO.getSearchCampNm());
        logger.debug("goKakaoCampSendList searchDeptNo  = " + searchVO.getSearchDeptNo());
        logger.debug("goKakaoCampSendList searchUserId  = " + searchVO.getSearchUserId());
        logger.debug("goKakaoCampSendList searchStartDt = " + searchVO.getSearchStartDt());
        logger.debug("goKakaoCampSendList searchEndtDt  = " + searchVO.getSearchEndDt());
        
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
        
        if(searchVO.getSearchDeptNo() == 0) {
            if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
                searchVO.setSearchDeptNo(0);
            } else {
                searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
            }
        }
        searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
        searchVO.setSearchGubun("004");
        
        // 페이지 설정
        int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
        int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
        searchVO.setPage(page);
        searchVO.setRows(rows);
        searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
        int totalCount = 0;

        // 카카오발송 조회
        List<SmsSendLogVO> kakaoList = null;
        try {
            kakaoList = smsAnalysisService.getSmsList(searchVO);
        } catch(Exception e) {
            logger.error("smsAnalysisService.getSmsList error = " + e);
        }
        
        if(kakaoList != null && kakaoList.size() > 0) {
            totalCount = kakaoList.get(0).getTotalCount();
        }
        
        PageUtil pageUtil = new PageUtil();
        pageUtil.init(request, searchVO.getPage(), totalCount, rows);
        
        model.addAttribute("searchVO", searchVO);       // 검색항목
        model.addAttribute("kakaoList", kakaoList);     // 카카오알림목록
        model.addAttribute("pageUtil", pageUtil);       // 페이징
        
        return "sms/ana/kakaoCampSendList";
    }
    
    /**
     * SMS발송 현황 화면을 출력한다.
     * 
     * @param searchVO
     * @param model
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping(value="/smsCampSendListP")
    public String goSmsCampSendListP(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        logger.debug("goSmsCampSendListP searchTaskNm  = " + searchVO.getSearchTaskNm());
        logger.debug("goSmsCampSendListP searchTempNm  = " + searchVO.getSearchTempNm());
        logger.debug("goSmsCampSendListP searchCampNm  = " + searchVO.getSearchCampNm());
        logger.debug("goSmsCampSendListP searchDeptNo  = " + searchVO.getSearchDeptNo());
        logger.debug("goSmsCampSendListP searchUserId  = " + searchVO.getSearchUserId());
        logger.debug("goSmsCampSendListP searchStartDt = " + searchVO.getSearchStartDt());
        logger.debug("goSmsCampSendListP searchEndtDt  = " + searchVO.getSearchEndDt());
        logger.debug("goSmsCampSendListP searchGubun   = " + searchVO.getSearchGubun());
        logger.debug("goSmsCampSendListP campNo        = " + searchVO.getCampNo());
        
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
        searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
        
        // 전송유형 목록
        CodeVO gubun = new CodeVO();
        gubun.setUilang((String)session.getAttribute("NEO_UILANG"));
        gubun.setCdGrp("C119");
        gubun.setCd("000");
        gubun.setUseYn("Y"); 
        List<CodeVO> gubunList = null;
        try {
            gubunList = codeService.getCodeListByUpCd(gubun);
        } catch(Exception e) {
            logger.error("codeService.getCodeListByUpCd[C119] error = " + e);
        }
        
        // 캠페인 목록 조회
        SmsCampaignVO camp = new SmsCampaignVO();
        camp.setUilang((String)session.getAttribute("NEO_UILANG"));
        camp.setSearchStatus("000");
        camp.setPage(1);
        camp.setRows(10000);
        camp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
        List<SmsCampaignVO> smsCampList = null;
        try {
            smsCampList = smsCampaignService.getCampaignList(camp);
        } catch(Exception e) {
            logger.error("smsCampaignService.getCampaignList error = " + e);
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
        
        model.addAttribute("searchVO", searchVO);       // 검색항목
        model.addAttribute("gubunList", gubunList);     // 문자전송유형
        model.addAttribute("smsCampList",smsCampList);  // 캠페인목록
        model.addAttribute("deptList", deptList);       // 부서
        model.addAttribute("userList", userList);       // 사용자
        
        return "sms/ana/smsCampSendListP";
    }
    
    /**
     * SMS발송 현황 목록을 조회 력한다.
     * 
     * @param searchVO
     * @param model
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping(value = "/smsCampSendList")
    public String goSmsCampSendList(@ModelAttribute SmsSendLogVO searchVO, Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
        logger.debug("goSmsCampSendList searchTaskNm  = " + searchVO.getSearchTaskNm());
        logger.debug("goSmsCampSendList searchTempNm  = " + searchVO.getSearchTempNm());
        logger.debug("goSmsCampSendList searchCampNm  = " + searchVO.getSearchCampNm());
        logger.debug("goSmsCampSendList searchDeptNo  = " + searchVO.getSearchDeptNo());
        logger.debug("goSmsCampSendList searchUserId  = " + searchVO.getSearchUserId());
        logger.debug("goSmsCampSendList searchStartDt = " + searchVO.getSearchStartDt());
        logger.debug("goSmsCampSendList searchEndtDt  = " + searchVO.getSearchEndDt());
        logger.debug("goSmsCampSendList searchGubun   = " + searchVO.getSearchGubun());
        
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
        
        if(searchVO.getSearchDeptNo() == 0) {
            if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
                searchVO.setSearchDeptNo(0);
            } else {
                searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
            }
        }
        searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
        
        // 페이지 설정
        int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
        int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE_ANA")));
        searchVO.setPage(page);
        searchVO.setRows(rows);
        searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
        int totalCount = 0;
        
        // 문자발송 내역 조회
        List<SmsSendLogVO> smsSendList = null;
        try {
            smsSendList = smsAnalysisService.getSmsList(searchVO);
        } catch(Exception e) {
            logger.error("smsAnalysisService.getSmsList error = " + e);
        }
        
        if(smsSendList != null && smsSendList.size() > 0) {
            totalCount = smsSendList.get(0).getTotalCnt();
        }
        
        PageUtil pageUtil = new PageUtil();
        pageUtil.init(request, searchVO.getPage(), totalCount, rows);
        
        model.addAttribute("searchVO", searchVO);       // 검색항목
        model.addAttribute("smsSendList", smsSendList); // 항목
        model.addAttribute("pageUtil", pageUtil);       // 페이징
        
        return "sms/ana/smsCampSendList";
    }
    
}
