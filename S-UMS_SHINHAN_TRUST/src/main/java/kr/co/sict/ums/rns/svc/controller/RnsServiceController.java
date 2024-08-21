/**
 * 작성자 : 김상진
 * 작성일시 : 2021.09.01
 * 설명 : RNS 서비스 관리 Controller
 */
package kr.co.sict.ums.rns.svc.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.co.sict.ums.com.service.CodeService;
import kr.co.sict.ums.com.service.CryptoService;
import kr.co.sict.ums.com.vo.CodeVO;
import kr.co.sict.ums.ems.cam.service.CampaignService;
import kr.co.sict.ums.ems.cam.vo.ApprovalOrgVO;
import kr.co.sict.ums.ems.cam.vo.TaskVO;
import kr.co.sict.ums.rns.svc.service.RnsServiceService;
import kr.co.sict.ums.rns.svc.vo.RnsAttachVO;
import kr.co.sict.ums.rns.svc.vo.RnsMailQueueTestVO;
import kr.co.sict.ums.rns.svc.vo.RnsMailQueueVO;
import kr.co.sict.ums.rns.svc.vo.RnsRecipientInfoVO;
import kr.co.sict.ums.rns.svc.vo.RnsSecuApprovalLineVO;
import kr.co.sict.ums.rns.svc.vo.RnsServiceVO;
import kr.co.sict.ums.rns.svc.vo.RnsWebAgentVO;
import kr.co.sict.ums.rns.tmp.service.RnsTemplateService;
import kr.co.sict.ums.rns.tmp.vo.RnsTemplateVO;
import kr.co.sict.ums.sys.acc.service.AccountService;
import kr.co.sict.ums.sys.acc.vo.UserVO;
import kr.co.sict.ums.sys.dbc.vo.DbConnVO;
import kr.co.sict.ums.sys.seg.service.SmsTemplateService;
import kr.co.sict.ums.sys.seg.vo.SmsTemplateVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.DBUtil;
import kr.co.sict.util.EncryptUtil;
import kr.co.sict.util.PageUtil;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;
import kr.co.sict.util.CrossScriptingFilter;

@Controller
@RequestMapping("/rns/svc")
public class RnsServiceController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private RnsTemplateService rnsTemplateService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private RnsServiceService rnsServiceService;
	
	@Autowired
	private CryptoService cryptoService;
	
	@Autowired
	private CampaignService campaignService;
	
	@Autowired
	private PropertiesUtil properties;
	
	/**
	 * 자동메일 서비스 목록 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/serviceListP")
	public String goServiceListP(@ModelAttribute RnsServiceVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goServiceListP searchStartDt     = " + searchVO.getSearchStartDt());
		logger.debug("goServiceListP searchEndDt       = " + searchVO.getSearchEndDt());
		logger.debug("goServiceListP searchTnm         = " + searchVO.getSearchTnm());
		logger.debug("goServiceListP searchDeptNo      = " + searchVO.getSearchDeptNo());
		logger.debug("goServiceListP searchUserId      = " + searchVO.getSearchStartDt());
		logger.debug("goServiceListP searchContentsTyp = " + searchVO.getSearchContentsTyp());
		logger.debug("goServiceListP searchStatus      = " + searchVO.getSearchStatus());
		
		// 검색 기본값 설정
		if(searchVO.getSearchStartDt() == null || "".equals(searchVO.getSearchStartDt())) {
			searchVO.setSearchStartDt(StringUtil.getCalcDateFromCurr(-1, "Y", "yyyyMM")+"01");
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
		searchVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		
		// 사용자그룹 목록 조회
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
		
		// 컨텐츠타입 목록
		CodeVO typeVO = new CodeVO();
		typeVO.setUilang("000");
		typeVO.setCdGrp("C109");
		typeVO.setUseYn("Y");
		List<CodeVO> typeList = null;
		try {
			typeList = codeService.getCodeList(typeVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C109] error = " + e);
		}
		
		// 메일상태 목록
		CodeVO statusVO = new CodeVO();
		statusVO.setUilang("000");
		statusVO.setCdGrp("C023");
		statusVO.setUseYn("Y");
		List<CodeVO> statusList = null;
		try {
			statusList = codeService.getCodeList(statusVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C023] error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO); 		// 검색항목
		model.addAttribute("deptList", deptList); 		// 부서목록
		model.addAttribute("userList", userList); 		// 사용자목록
		model.addAttribute("typeList", typeList); 		// 컨텐츠타입목록
		model.addAttribute("statusList", statusList);	// 메일상태목록
		
		return "rns/svc/serviceListP";
	}
	
	/**
	 * 자동메일 서비스 목록을 조회한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/serviceList")
	public String goServiceList(@ModelAttribute RnsServiceVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goServiceList searchStartDt     = " + searchVO.getSearchStartDt());
		logger.debug("goServiceList searchEndDt       = " + searchVO.getSearchEndDt());
		logger.debug("goServiceList searchTnm         = " + searchVO.getSearchTnm());
		logger.debug("goServiceList searchDeptNo      = " + searchVO.getSearchDeptNo());
		logger.debug("goServiceList searchUserId      = " + searchVO.getSearchStartDt());
		logger.debug("goServiceList searchContentsTyp = " + searchVO.getSearchContentsTyp());
		logger.debug("goServiceList searchStatus      = " + searchVO.getSearchStatus());
		
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		if(searchVO.getSearchDeptNo() == 0) {
			if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo(0);
			} else {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		searchVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;
		
		// 자동메일 서비스목록 조회
		List<RnsServiceVO> serviceList = null;
		try {
			serviceList = rnsServiceService.getServiceList(searchVO);
		} catch(Exception e) {
			logger.error("rnsServiceService.getServiceList error = " + e);
		}
		
		if(serviceList != null && serviceList.size() > 0) {
			totalCount = serviceList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("serviceList", serviceList);		// 서비스목록
		model.addAttribute("pageUtil", pageUtil);			// 페이징
		
		return "rns/svc/serviceList";
	}
	
	/**
	 * 자동메일 서비스 신규등록 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/serviceAddP")
	public String goServiceAddP(@ModelAttribute RnsServiceVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		// 템플릿 목록
		RnsTemplateVO temp = new RnsTemplateVO();
		temp.setUilang((String)session.getAttribute("NEO_UILANG"));
		temp.setSearchStatus("000");
		temp.setPage(1);
		temp.setRows(10000);
		temp.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		temp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<RnsTemplateVO> tempList = null;
		try {
			tempList = rnsTemplateService.getTemplateList(temp);
		} catch(Exception e) {
			logger.error("rnsTemplateService.getTemplateList error = " + e);
		}
		
		// 사용자그룹 목록 조회
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
		
		// 컨텐츠타입 목록
		CodeVO typeVO = new CodeVO();
		typeVO.setUilang("000");
		typeVO.setCdGrp("C109");
		typeVO.setUseYn("Y");
		List<CodeVO> typeList = null;
		try {
			typeList = codeService.getCodeList(typeVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C109] error = " + e);
		}
		
		// RNS머지항목
		CodeVO mergeVO = new CodeVO();
		mergeVO.setUilang("000");
		mergeVO.setCdGrp("C110");
		mergeVO.setUseYn("Y");
		List<CodeVO> mergeList = null;
		try {
			mergeList = codeService.getCodeList(mergeVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C110] error = " + e);
		}
		
		// 사용자정보
		UserVO userInfo = new UserVO();
		userInfo.setUilang((String)session.getAttribute("NEO_UILANG"));
		userInfo.setUserId((String)session.getAttribute("NEO_USER_ID"));
		try {
			userInfo = accountService.getUserInfo(userInfo);
		} catch(Exception e) {
			logger.error("systemService.getUserInfo error = " + e);
		}
		
		// 최상위 조직목록 조회
		List<ApprovalOrgVO> orgList = null;
		try {
			orgList = campaignService.getOrgListLvl1();
		} catch(Exception e) {
			logger.error("campaignService.getOrgListLvl1 error = " + e);
		}
		
		model.addAttribute("tempList", tempList); 		// 템플릿목록
		model.addAttribute("deptList", deptList); 		// 부서목록
		model.addAttribute("userList", userList); 		// 사용자목록
		model.addAttribute("typeList", typeList); 		// 컨텐츠타입목록
		model.addAttribute("mergeList", mergeList);		// RNS머지항목
		model.addAttribute("userInfo", userInfo);		// 사용자정보
		model.addAttribute("orgList", orgList);			// 최상위조직목록
		
		return "rns/svc/serviceAddP";
	}
	
	/**
	 * 자동메일 서비스 신규등록 처리
	 * @param serviceVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/serviceAdd")
	public String goServiceAdd(@ModelAttribute RnsServiceVO serviceVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goServiceAdd contentsTyp  = " + serviceVO.getContentsTyp());
		logger.debug("goServiceAdd deptNo       = " + serviceVO.getDeptNo());
		logger.debug("goServiceAdd userId       = " + serviceVO.getUserId());
		logger.debug("goServiceAdd sname        = " + serviceVO.getSname());
		logger.debug("goServiceAdd smail        = " + serviceVO.getSmail());
		logger.debug("goServiceAdd webAgentUrl  = " + serviceVO.getWebAgentUrl());
		logger.debug("goServiceAdd tnm          = " + serviceVO.getTnm());
		logger.debug("goServiceAdd emailSubject = " + serviceVO.getEmailSubject());
		logger.debug("goServiceAdd eaiCampNo    = " + serviceVO.getEaiCampNo());
		
		//XSS 필터 
		/*
		if (CrossScriptingFilter.existScript(request, serviceVO.getServiceContent())) {
			// jsonView 생성
			model.addAttribute("result","filter");
			return "rns/svc/serviceAdd";
		}
		*/
		
		List<RnsAttachVO> attachList = new ArrayList<RnsAttachVO>();		// 첨부파일 목록
		// 파일 사이즈 체크
		if(serviceVO.getAttachPath() != null && !"".equals(serviceVO.getAttachPath())) {
			File attachFile = null;
			String basePath = properties.getProperty("FILE.UPLOAD_PATH") + "/attach";
			
			String[] fileNm = serviceVO.getAttachNm().split(",");
			String[] filePath = serviceVO.getAttachPath().split(",");
			serviceVO.setAttchCnt(filePath.length);		// 첨부파일 수 설정
			for(int i=0;i<filePath.length;i++) {
				attachFile = new File(basePath + "/" + filePath[i]);
				RnsAttachVO attach = new RnsAttachVO();
				attach.setAttNo(i);
				attach.setAttNm(fileNm[i]);
				attach.setAttFlPath("attach/" + filePath[i]);
				attach.setAttFlSize(attachFile.length());
				attachList.add(attach);
			}
		}
		
		// 기본값 설정
		if(serviceVO.getDeptNo() == 0) serviceVO.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));					// 사용자그룹번호
		if(StringUtil.isNull(serviceVO.getUserId())) serviceVO.setUserId((String)session.getAttribute("NEO_USER_ID"));	// 사용자아이디
		if(StringUtil.isNull(serviceVO.getRecvChkYn())) serviceVO.setRecvChkYn("N");									// 수신확인
		if(StringUtil.isNull(serviceVO.getUseYn())) serviceVO.setUseYn("Y");											// 사용여부
		if(StringUtil.isNull(serviceVO.getSid())) serviceVO.setSid((String)session.getAttribute("NEO_USER_ID"));		// 발송자아이디
		serviceVO.setStatus("000");																						// 상태
		serviceVO.setRegId((String)session.getAttribute("NEO_USER_ID"));												// 등록(생성)자ID
		serviceVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));															// 등록(생성)일시
		
		if(StringUtil.isNull(serviceVO.getWebAgentAttachYn())) serviceVO.setWebAgentAttachYn("N");						// WebAgent 첨부파일로 지정
		
		if(StringUtil.isNull(serviceVO.getApprUserId())) {
			serviceVO.setWorkStatus("000");
			//logger.error("RnsServiceController.serviceAdd error = ApprUserID Not Exist");
			//model.addAttribute("result","Fail");
			//return "rns/svc/serviceAdd";
		} else {
			serviceVO.setWorkStatus("201");
		}

		
		// 컬럼 암호화 설정
		serviceVO.setSmail( cryptoService.getEncrypt("SMAIL", serviceVO.getSmail()) );
		
		// 서비스 작성내용 파일로 생성(파일 생성 전 디렉토리 생성)
		String contFlPath = "content/" + (String)session.getAttribute("NEO_USER_ID") + "/" + StringUtil.getDate(Code.TM_YMDHMSM) + ".tmp";
		String basePath = properties.getProperty("FILE.UPLOAD_PATH");
		String filePath = basePath + "/" + contFlPath;
		if("1".equals(serviceVO.getContentsTyp().trim())) {
			serviceVO.setContentsPath(null);
		} else {
			serviceVO.setContentsPath(filePath);
		}
		String dirPath = basePath + "/content/" + (String)session.getAttribute("NEO_USER_ID");
		File fileDir = new File(dirPath);
		if(!fileDir.exists()) {
			fileDir.mkdir();
		}
		
		FileOutputStream fos = null;
		OutputStreamWriter writer = null;
		try {
			File contentFile = new File(filePath);
			fos = new FileOutputStream(contentFile);
			writer = new OutputStreamWriter(fos, "UTF-8");
			writer.write(serviceVO.getServiceContent());
			writer.flush();
		} catch(Exception e) {
			logger.error("serviceAdd File Write Error = " + e);
		} finally {
			if(writer != null) try { writer.close(); } catch(Exception e) {}
			if(fos != null) try { fos.close(); } catch(Exception e) {}
		}
		
		// 서비스 정보 등록
		int result = 0;
		try {
			result = rnsServiceService.insertServiceInfo(serviceVO, attachList);
		} catch(Exception e) {
			logger.error("rnsServiceService.insertServiceInfo error = " + e);
		}
		
		if(result > 0) {
			model.addAttribute("result","Success");
		} else {
			model.addAttribute("result","Fail");
		}

		return "rns/svc/serviceAdd";
	}
	
	/**
	 * 자동메일 서비스 상태변경(사용중지,삭제,복구)
	 * @param serviceVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/updateServiceStatus")
	public ModelAndView goUpdateServiceStatus(@ModelAttribute RnsServiceVO serviceVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goUpdateServiceStatus tids   = " + serviceVO.getTids());
		logger.debug("goUpdateServiceStatus status = " + serviceVO.getStatus());
		
		serviceVO.setUpId((String)session.getAttribute("NEO_USER_ID"));
		serviceVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		int result = 0;
		
		try {
			result = rnsServiceService.updateServiceStatus(serviceVO);
		} catch(Exception e) {
			logger.error("rnsServiceService.updateServiceStatus error = " + e);
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
	 * 자동메일 서비스 테스트발송 처리
	 * @param serviceVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/serviceTestSend")
	public ModelAndView goServiceTestSend(@ModelAttribute RnsRecipientInfoVO recipientVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goServiceTestSend tid  = " + recipientVO.getTid());
		logger.debug("goServiceTestSend rids = " + recipientVO.getRids());
		
		int result = 0;
		
		String userId = (String)session.getAttribute("NEO_USER_ID");
		String currDts = StringUtil.getDate(Code.TM_YMDHMSM);
		String currDt  = currDts.substring(0,14);
		int tid = recipientVO.getTid();
		
		// 서비스 정보 조회
		RnsServiceVO serviceVO = null;
		try {
			serviceVO = rnsServiceService.getServiceInfo(tid);
		} catch(Exception e) {
			logger.error("rnsServiceService.getServiceInfo error = " + e);
		}
		if(serviceVO == null) {
			// jsonView 생성
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("result", "Fail");
			ModelAndView modelAndView = new ModelAndView("jsonView", map);
			return modelAndView;
		}
		
		// 웹에이전트 정보 조회
		RnsWebAgentVO webAgent = null;
		try {
			webAgent = rnsServiceService.getWebAgentInfo(tid);
		} catch(Exception e) {
			logger.error("rnsServiceService.getWebAgentInfo error = " + e);
		}
		
		// 첨부파일 목록 조회
		List<RnsAttachVO> attachList = null;
		try {
			attachList = rnsServiceService.getAttachList(tid);
		} catch(Exception e) {
			logger.error("rnsServiceService.getAttachList error = " + e);
		}
		
		List<RnsRecipientInfoVO> recipientList = new ArrayList<RnsRecipientInfoVO>();
		RnsMailQueueVO queue = new RnsMailQueueVO();
		RnsMailQueueTestVO queueTest = new RnsMailQueueTestVO();
		
		if(!StringUtil.isNull(recipientVO.getRids())) {
			
			String[] mailInfo = recipientVO.getRids().split(",");
			for(int i=0;i<mailInfo.length;i++) {
				String id = "";
				String name = "";
				String mail = "";
				String[] user = mailInfo[i].split("\\|");
				for(int j=0;j<user.length;j++) {
					if(j == 0) id = user[j];
					if(j == 1) name = user[j];
					if(j == 2) mail = user[j];
				}
				
				// RecipientInfo
				RnsRecipientInfoVO recipient = new RnsRecipientInfoVO();
				recipient.setSubid(i);
				recipient.setTid(tid);
				recipient.setRid(id);
				recipient.setRname(name);
				recipient.setRmail( cryptoService.getEncrypt("RMAIL", mail) );
				recipient.setEnckey(StringUtil.getDate(Code.TM_YMD));
				recipientList.add(recipient);
			}
			
			// MailQueue
			queue.setSubid(0);
			queue.setTid(tid);
			queue.setSpos("0");
			queue.setSname(serviceVO.getSname());
			queue.setSmail(serviceVO.getSmail());
			queue.setSid(serviceVO.getSid());
			queue.setRpos("0");
			queue.setQuery("");
			queue.setCtnpos(serviceVO.getContentsTyp().trim());
			queue.setSubject(serviceVO.getEmailSubject());
			if("0".equals(queue.getCtnpos())) {
				queue.setContents(getContFileText(serviceVO.getContentsPath()));
			} else if("1".equals(queue.getCtnpos())) {
				//queue.setContents(webAgent.getSourceUrl());
				queue.setContents(webAgent!=null?webAgent.getSourceUrl():"");
			} else if("2".equals(queue.getCtnpos())) {
				queue.setContents(serviceVO.getContentsPath());
			}
			queue.setStatus("0");
			queue.setDbcode("");
			queue.setRefmid(0);
			queue.setCharset(0);
			if(attachList != null && attachList.size() > 0) {
				for(int x=0;x<attachList.size();x++) {
					if(x == 0) queue.setAttachfile01(attachList.get(x).getAttFlPath());
					if(x == 1) queue.setAttachfile02(attachList.get(x).getAttFlPath());
					if(x == 2) queue.setAttachfile03(attachList.get(x).getAttFlPath());
					if(x == 3) queue.setAttachfile04(attachList.get(x).getAttFlPath());
					if(x == 4) queue.setAttachfile05(attachList.get(x).getAttFlPath());
				}
			}
			
			// MailQueueTest
			queueTest.setUseYn("Y");
			queueTest.setRegId(userId);
			queueTest.setRegDt(currDt);
		}
		
		// 테스트 발송 등록
		try {
			result = rnsServiceService.serviceTestSend(recipientList, queue, queueTest);
		} catch(Exception e) {
			logger.error("rnsServiceService.serviceTestSend error = " + e);
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
	 * 자동메일 서비스 재발송처리
	 * @param serviceVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailReSend")
	public ModelAndView reSendMail(@ModelAttribute RnsRecipientInfoVO recipientVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("reSendMail tid  = " + recipientVO.getTid());
		logger.debug("reSendMail mid = " + recipientVO.getMid());
		logger.debug("reSendMail rid = " + recipientVO.getRid());
		
		int result = 0;
	
		// 재발송 등록
		try {
			result = rnsServiceService.serviceReSend(recipientVO);
		} catch(Exception e) {
			logger.error("rnsServiceService.serviceReSend error = " + e);
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
	 * 자동메일 서비스 복사
	 * @param serviceVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/serviceCopy")
	public ModelAndView goServiceCopy(@ModelAttribute RnsServiceVO serviceVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("serviceCopy tids   = " + serviceVO.getTids());
		
		serviceVO.setTid(Integer.parseInt(serviceVO.getTids()));
		serviceVO.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
		serviceVO.setUserId((String)session.getAttribute("NEO_USER_ID"));
		serviceVO.setRegId((String)session.getAttribute("NEO_USER_ID"));
		serviceVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		int result = 0;
		
		try {
			result = rnsServiceService.serviceCopy(serviceVO, properties);
		} catch(Exception e) {
			logger.error("rnsServiceService.serviceCopy error = " + e);
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
	 * 자동메일 서비스 정보수정 화면 출력
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/serviceUpdateP")
	public String goServiceUpdateP(@ModelAttribute RnsServiceVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goServiceUpdateP tid = " + searchVO.getTid());
		
		// 서비스 정보 조회
		RnsServiceVO serviceInfo = null;
		try {
			serviceInfo = rnsServiceService.getServiceInfo(searchVO.getTid());
		} catch(Exception e) {
			logger.error("rnsServiceService.getServiceInfo error = " + e);
		}
		
		// 발송결재라인 목록
		List<RnsSecuApprovalLineVO> apprLineList = null;
		if ( serviceInfo !=null ) {
			if("Y".equals(serviceInfo.getApprovalLineYn())){
				try {
					apprLineList = rnsServiceService.getRnsApprovalLineList(searchVO.getTid());
				} catch(Exception e) {
					logger.error("rnsServiceService.getRnsApprovalLineList error = " + e);
				}
			}
		}

		// WebAgent 정보 조회
		RnsWebAgentVO webAgent = null;
		try {
			webAgent = rnsServiceService.getWebAgentInfo(searchVO.getTid());
		} catch(Exception e) {
			logger.error("rnsServiceService.getWebAgentInfo error = " + e);
		}
		
		// 첨부파일 목록 조회
		List<RnsAttachVO> attachList = null;
		try {
			attachList = rnsServiceService.getAttachList(searchVO.getTid());
		} catch(Exception e) {
			logger.error("rnsServiceService.getAttachList error = " + e);
		}
		
		// 템플릿 목록
		RnsTemplateVO temp = new RnsTemplateVO();
		temp.setUilang((String)session.getAttribute("NEO_UILANG"));
		temp.setSearchStatus("000");
		temp.setPage(1);
		temp.setRows(1000);
		temp.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
		List<RnsTemplateVO> tempList = null;
		try {
			tempList = rnsTemplateService.getTemplateList(temp);
		} catch(Exception e) {
			logger.error("rnsTemplateService.getTemplateList error = " + e);
		}
		
		// 사용자그룹 목록 조회
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
		user.setDeptNo(serviceInfo.getDeptNo());
		user.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(user);
		} catch(Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		// 컨텐츠타입 목록
		CodeVO typeVO = new CodeVO();
		typeVO.setUilang("000");
		typeVO.setCdGrp("C109");
		typeVO.setUseYn("Y");
		List<CodeVO> typeList = null;
		try {
			typeList = codeService.getCodeList(typeVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C109] error = " + e);
		}
		
		// RNS머지항목
		CodeVO mergeVO = new CodeVO();
		mergeVO.setUilang("000");
		mergeVO.setCdGrp("C110");
		mergeVO.setUseYn("Y");
		List<CodeVO> mergeList = null;
		try {
			mergeList = codeService.getCodeList(mergeVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C110] error = " + e);
		}
		
		// 최상위 조직목록 조회
		List<ApprovalOrgVO> orgList = null;
		try {
			orgList = campaignService.getOrgListLvl1();
		} catch(Exception e) {
			logger.error("campaignService.getOrgListLvl1 error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("serviceInfo", serviceInfo);		// 서비스정보
		model.addAttribute("webAgent", webAgent);			// 웹에이전트
		model.addAttribute("attachList", attachList);		// 첨부파일목록
		model.addAttribute("tempList", tempList); 			// 템플릿목록
		model.addAttribute("deptList", deptList); 			// 부서목록
		model.addAttribute("userList", userList); 			// 사용자목록
		model.addAttribute("typeList", typeList); 			// 컨텐츠타입목록
		model.addAttribute("mergeList", mergeList);			// RNS머지항목
		model.addAttribute("apprLineList", apprLineList);	// 발송결재라인 목록
		model.addAttribute("orgList", orgList);				// 최상위조직목록
		
		return "rns/svc/serviceUpdateP";
	}
	
	/**
	 * 자동메일 서비스 정보 수정
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/serviceUpdate")
	public String goServiceUpdate(@ModelAttribute RnsServiceVO serviceVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goServiceUpdate tid = " + serviceVO.getTid());
		logger.debug("goServiceUpdate contentsTyp = " + serviceVO.getContentsTyp());
		logger.debug("goServiceUpdate deptNo      = " + serviceVO.getDeptNo());
		logger.debug("goServiceUpdate userId      = " + serviceVO.getUserId());
		logger.debug("goServiceUpdate sname       = " + serviceVO.getSname());
		logger.debug("goServiceUpdate smail       = " + serviceVO.getSmail());
		logger.debug("goServiceUpdate webAgentUrl = " + serviceVO.getWebAgentUrl());
		logger.debug("goServiceUpdate tnm         = " + serviceVO.getTnm());
		logger.debug("goServiceUpdate emailSubject = " + serviceVO.getEmailSubject());
		
		//XSS 필터 
		/*
		if (CrossScriptingFilter.existScript(request, serviceVO.getServiceContent())) {
			// jsonView 생성
			model.addAttribute("result","filter");
			return "rns/svc/serviceUpdate";
		}
		*/
		List<RnsAttachVO> attachList = new ArrayList<RnsAttachVO>();		// 첨부파일 목록
		// 파일 사이즈 체크
		if(serviceVO.getAttachPath() != null && !"".equals(serviceVO.getAttachPath())) {
			File attachFile = null;
			String basePath = properties.getProperty("FILE.UPLOAD_PATH") + "/attach";
			
			String[] fileNm = serviceVO.getAttachNm().split(",");
			String[] filePath = serviceVO.getAttachPath().split(",");
			serviceVO.setAttchCnt(filePath.length);		// 첨부파일 수 설정
			for(int i=0;i<filePath.length;i++) {
				attachFile = new File(basePath + "/" + filePath[i]);
				RnsAttachVO attach = new RnsAttachVO();
				attach.setTid(serviceVO.getTid());
				attach.setAttNo(i);
				attach.setAttNm(fileNm[i]);
				attach.setAttFlPath("attach/" + filePath[i]);
				attach.setAttFlSize(attachFile.length());
				attachList.add(attach);
			}
		}
		
		// 기본값 설정
		if(serviceVO.getDeptNo() == 0) serviceVO.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));					// 사용자그룹번호
		if(StringUtil.isNull(serviceVO.getUserId())) serviceVO.setUserId((String)session.getAttribute("NEO_USER_ID"));	// 사용자아이디
		if(StringUtil.isNull(serviceVO.getRecvChkYn())) serviceVO.setRecvChkYn("N");									// 수신확인
		if(StringUtil.isNull(serviceVO.getUseYn())) serviceVO.setUseYn("Y");											// 사용여부
		if(StringUtil.isNull(serviceVO.getSid())) serviceVO.setSid((String)session.getAttribute("NEO_USER_ID"));		// 발송자아이디
		serviceVO.setUpId((String)session.getAttribute("NEO_USER_ID"));													// 수정자ID
		serviceVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));															// 수정일시
		
		if(StringUtil.isNull(serviceVO.getApprUserId())) {
			serviceVO.setWorkStatus("000");
		} else {
			serviceVO.setWorkStatus("201");
		}
		
		// 컬럼 암호화 설정
		serviceVO.setSmail( cryptoService.getEncrypt("SMAIL", serviceVO.getSmail()) );

		// 서비스 작성내용 파일 수정
		String contentsPath = serviceVO.getContentsPath();
		if("1".equals(serviceVO.getContentsTyp().trim())) {
			serviceVO.setContentsPath(null);
		} else {
  			if(contentsPath == null || "".equals(contentsPath)) {
				String contFlPath = "content/" + (String)session.getAttribute("NEO_USER_ID") + "/" + StringUtil.getDate(Code.TM_YMDHMSM) + ".tmp";
				String basePath = properties.getProperty("FILE.UPLOAD_PATH");
				String filePath = basePath + "/" + contFlPath;
				contentsPath = filePath;
			}
			
			// 파일을 수정한다.
			FileOutputStream fos = null;
			OutputStreamWriter writer = null;
			try {
				File contFile = new File(contentsPath);
				fos = new FileOutputStream(contFile);
				writer = new OutputStreamWriter(fos, "UTF-8");
				writer.write(serviceVO.getServiceContent());
				writer.flush();
			} catch(Exception e) {
				logger.error("serviceUpdate File Write Error = " + e);
			}
			
			serviceVO.setContentsPath(contentsPath);
		}
		
		// 서비스 정보 등록
		int result = 0;
		try {
			result = rnsServiceService.updateServiceInfo(serviceVO, attachList);
		} catch(Exception e) {
			logger.error("rnsServiceService.insertServiceInfo error = " + e);
		}
		
		if(result > 0) {
			model.addAttribute("result","Success");
		} else {
			model.addAttribute("result","Fail");
		}
		
		return "rns/svc/serviceUpdate";
	}
	
	/**
	 * 자동메일 서비스 부분수정 화면 출력
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/serviceUpdateDateP")
	public String goServiceUpdateDateP(@ModelAttribute RnsServiceVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goServiceUpdateDateP tid = " + searchVO.getTid());
		
		// 서비스 정보 조회
		RnsServiceVO serviceInfo = null;
		try {
			serviceInfo = rnsServiceService.getServiceInfo(searchVO.getTid());
		} catch(Exception e) {
			logger.error("rnsServiceService.getServiceInfo error = " + e);
		}
		 
		// 발송결재라인 목록
		List<RnsSecuApprovalLineVO> apprLineList = null;
		if ( serviceInfo !=null ) {
			if("Y".equals(serviceInfo.getApprovalLineYn())){
				try {
					apprLineList = rnsServiceService.getRnsApprovalLineList(searchVO.getTid());
				} catch(Exception e) {
					logger.error("rnsServiceService.getRnsApprovalLineList error = " + e);
				}
			}
		}
		
		// WebAgent 정보 조회
		RnsWebAgentVO webAgent = null;
		try {
			webAgent = rnsServiceService.getWebAgentInfo(searchVO.getTid());
		} catch(Exception e) {
			logger.error("rnsServiceService.getWebAgentInfo error = " + e);
		}
		
		// 첨부파일 목록 조회
		List<RnsAttachVO> attachList = null;
		try {
			attachList = rnsServiceService.getAttachList(searchVO.getTid());
		} catch(Exception e) {
			logger.error("rnsServiceService.getAttachList error = " + e);
		}
		
		// 템플릿 목록
		RnsTemplateVO temp = new RnsTemplateVO();
		temp.setUilang((String)session.getAttribute("NEO_UILANG"));
		temp.setSearchStatus("000");
		temp.setPage(1);
		temp.setRows(1000);
		//temp.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
		List<RnsTemplateVO> tempList = null;
		try {
			tempList = rnsTemplateService.getTemplateList(temp);
		} catch(Exception e) {
			logger.error("rnsTemplateService.getTemplateList error = " + e);
		}
		
		// 사용자그룹 목록 조회
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
		user.setDeptNo(serviceInfo.getDeptNo());
		user.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(user);
		} catch(Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		// 컨텐츠타입 목록
		CodeVO typeVO = new CodeVO();
		typeVO.setUilang("000");
		typeVO.setCdGrp("C109");
		typeVO.setUseYn("Y");
		List<CodeVO> typeList = null;
		try {
			typeList = codeService.getCodeList(typeVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C109] error = " + e);
		}
		
		// RNS머지항목
		CodeVO mergeVO = new CodeVO();
		mergeVO.setUilang("000");
		mergeVO.setCdGrp("C110");
		mergeVO.setUseYn("Y");
		List<CodeVO> mergeList = null;
		try {
			mergeList = codeService.getCodeList(mergeVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C110] error = " + e);
		}
		
		// 최상위 조직목록 조회
		List<ApprovalOrgVO> orgList = null;
		try {
			orgList = campaignService.getOrgListLvl1();
		} catch(Exception e) {
			logger.error("campaignService.getOrgListLvl1 error = " + e);
		}
		
	 
		model.addAttribute("searchVO", searchVO);					// 검색항목
		model.addAttribute("serviceInfo", serviceInfo);				// 서비스정보
		model.addAttribute("webAgent", webAgent);					// 웹에이전트
		model.addAttribute("attachList", attachList);				// 첨부파일목록
		model.addAttribute("tempList", tempList); 					// 템플릿목록
		model.addAttribute("deptList", deptList); 					// 부서목록
		model.addAttribute("userList", userList); 					// 사용자목록
		model.addAttribute("typeList", typeList); 					// 컨텐츠타입목록
		model.addAttribute("mergeList", mergeList);					// RNS머지항목
		model.addAttribute("apprLineList", apprLineList);			// 발송결재라인 목록
		model.addAttribute("orgList", orgList);						// 최상위조직목록
		
		return "rns/svc/serviceUpdateDateP";
	}
	
	/**
	 * 자동메일 서비스 정보 수정
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/serviceUpdateDate")
	public String goServiceUpdateDate(@ModelAttribute RnsServiceVO serviceVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goServiceUpdateDate tid              = " + serviceVO.getTid());
		logger.debug("goServiceUpdateDate sname            = " + serviceVO.getSname());
		logger.debug("goServiceUpdateDate smail            = " + serviceVO.getSmail()); 

		// 기본값 설정
		if(serviceVO.getDeptNo() == 0) serviceVO.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));					// 사용자그룹번호
		if(StringUtil.isNull(serviceVO.getUserId())) serviceVO.setUserId((String)session.getAttribute("NEO_USER_ID"));	// 사용자아이디
		serviceVO.setUpId((String)session.getAttribute("NEO_USER_ID"));													// 수정자ID
		serviceVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));															// 수정일시

		// 컬럼 암호화 설정
		serviceVO.setSmail( cryptoService.getEncrypt("SMAIL", serviceVO.getSmail()) );

		// 서비스 정보 등록
		int result = 0;
		try {
			result = rnsServiceService.updateServicePartInfo(serviceVO);
		} catch(Exception e) {
			logger.error("rnsServiceService.updateServicePartInfo error = " + e);
		}
		
		if(result > 0) {
			model.addAttribute("result","Success");
		} else {
			model.addAttribute("result","Fail");
		}
		
		return "rns/svc/serviceUpdateDate";
	}
	

	/**
	 * 템플릿 코드를 체크한다. 템플릿 중복을 방지.
	 * 
	 * @param userVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/eaiCampNoCheck")
	public ModelAndView getServiceInfoByEai(@ModelAttribute RnsServiceVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getServiceInfoByEai tid       = " + searchVO.getTid());
		logger.debug("getServiceInfoByEai eaiCampNo = " + searchVO.getEaiCampNo());

		boolean result = false;
		RnsServiceVO rnsServiceVO = new RnsServiceVO();
		try {
			rnsServiceVO = rnsServiceService.getServiceInfoByEai(searchVO.getEaiCampNo());
			if (rnsServiceVO != null ) {
				if(searchVO.getTid() == 0) {
					result = false;
				} else {
					if(rnsServiceVO.getTid() == searchVO.getTid()) {
						result = true;
					} else {
						result = false;
					}
				}
			} else {
				result = true;
			}
		} catch (Exception e) {
			logger.error("rnsServiceService.getServiceInfoByEai error = " + e);
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
	 * 자동메일 서비스 테스트메일 발송목록 화면 출력
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/serviceTestListP")
	public String goServiceTestListP(@ModelAttribute RnsServiceVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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
		
		model.addAttribute("searchVO", searchVO);		// 검색항목
		
		return "rns/svc/serviceTestListP";
	}
	
	/**
	 * 자동메일 서비스 테스트메일 목록 조회
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/serviceTestList")
	public String goServiceTestList(@ModelAttribute RnsServiceVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goServiceTestList searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goServiceTestList searchEndDt   = " + searchVO.getSearchEndDt());
		logger.debug("goServiceTestList searchTnm   = " + searchVO.getSearchTnm());
		
		searchVO.setSearchStartDt(searchVO.getSearchStartDt().replaceAll("\\.", ""));
		searchVO.setSearchEndDt(searchVO.getSearchEndDt().replaceAll("\\.", ""));
		if(searchVO.getSearchDeptNo() == 0) {
			if("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo(0);
			} else {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		searchVO.setAdminYn((String)session.getAttribute("NEO_ADMIN_YN"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;

		// 테스트목록 조회
		List<RnsServiceVO> serviceTestList = null;
		try {
			serviceTestList = rnsServiceService.getServiceTestList(searchVO);
		} catch(Exception e) {
			logger.error("rnsServiceService.getServiceTestList error = " + e);
		}
		
		if(serviceTestList != null && serviceTestList.size() > 0) {
			totalCount = serviceTestList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("searchVO", searchVO);				// 검색항목
		model.addAttribute("serviceTestList", serviceTestList);	// 테스트목록
		model.addAttribute("pageUtil", pageUtil);				// 페이징
		
		return "rns/svc/serviceTestList";
	}
	
	/**
	 * 자동메일 서비스 테스트메일 발송결과목록 조회
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/serviceTestResultList")
	public String goServiceTestResultList(@ModelAttribute RnsRecipientInfoVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goServiceTestResultList mid = " + searchVO.getMid());
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		searchVO.setPage(page);
		searchVO.setRows(rows);
		searchVO.setStartRow((searchVO.getPage()-1)*searchVO.getRows());
		int totalCount = 0;

		// 테스트결과목록 조회
		List<RnsRecipientInfoVO> testResultList = null;
		try {
			testResultList = rnsServiceService.getServiceTestResultList(searchVO);
		} catch(Exception e) {
			logger.error("rnsServiceService.getServiceTestResultList error = " + e);
		}
		
		if(testResultList != null && testResultList.size() > 0) {
			totalCount = testResultList.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		pageUtil.setSubmitFunc("goPageNumTest");
		
		model.addAttribute("searchVO", searchVO);				// 검색항목
		model.addAttribute("testResultList", testResultList);	// 테스트결과목록
		model.addAttribute("pageUtil", pageUtil);				// 페이징
		
		return "rns/svc/serviceTestResultList";
	}
	
	/**
	 * 파일의 내용을 읽는다.
	 * @param contFlPath
	 * @return
	 */
	public String getContFileText(String contFlPath) {
		logger.debug("getContFileText contFlPath  = " + contFlPath);
		
		FileInputStream input = null;
		InputStreamReader reader = null;
		BufferedReader bufferedReader = null;
		StringBuffer sb = new StringBuffer();
		try {
			input = new FileInputStream(contFlPath);
			reader = new InputStreamReader(input,"UTF-8");
			bufferedReader = new BufferedReader(reader);
			String line = "";
			while((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
		} catch(Exception e) {
			logger.error("getContFileText error = " + e);
		} finally {
			if(bufferedReader != null) try { bufferedReader.close(); } catch(Exception e) {};
			if(reader != null) try { reader.close(); } catch(Exception e) {};
		}
		
		String fileContent = sb.toString().trim();
		fileContent = fileContent.replaceAll("\"", "'");
		fileContent = fileContent.replaceAll("\n", " ");
		fileContent = fileContent.replaceAll("\r", " ");
		
		return fileContent;
	}
	
	/**
	 * 템플릿 파일 읽기
	 * @param templateVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/serviceFileView")
	public ModelAndView goServiceFileView(@ModelAttribute RnsServiceVO serviceVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goServiceFileView contentsPath  = " + serviceVO.getContentsPath());
		
		/*
		File file = null;
		FileReader fileReader = null;
		*/
		FileInputStream input = null;
		InputStreamReader reader = null;	
		BufferedReader bufferedReader = null;
		StringBuffer sb = new StringBuffer();
		try {
			String contentsPath = serviceVO.getContentsPath();
			//file = new File(contentsPath);
			//fileReader = new FileReader(file);
			input = new FileInputStream(contentsPath);
			reader = new InputStreamReader(input,"UTF-8");
			bufferedReader = new BufferedReader(reader);
			String line = "";
			while((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
		} catch(Exception e) {
			logger.error("goServiceFileView error = " + e);
		} finally {
			if(bufferedReader != null) try { bufferedReader.close(); } catch(Exception e) {};
			if(reader != null) try { reader.close(); } catch(Exception e) {};
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("result", "Success");
		map.put("contVal", sb.toString().trim());
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 웹에이전트 미리보기 수신자그룹 정보
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/webAgentPreview")
	public ModelAndView webAgentPreview(@ModelAttribute RnsServiceVO serviceVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {		
		logger.debug("webAgentPreview webAgentUrl = " + serviceVO.getWebAgentUrl());
		
		String webAgentUrl = "";
		try {
			webAgentUrl = serviceVO.getWebAgentUrl();
			
			logger.debug("webAgentPreview webAgentUrl[1] = " + webAgentUrl);
			
			String paramUrl = "";
			if(webAgentUrl.indexOf("?")>=0) {
				String tempParamUrl = webAgentUrl.substring(webAgentUrl.indexOf("?")+1);
				String[] paramStr = tempParamUrl.split("&");
				for(int i=0;i<paramStr.length;i++) {
					String param = paramStr[i];
					if(param.indexOf("=")>=0) {
						String key = param.split("=")[0];
						String val = param.split("=")[1];
						paramUrl += (i==0?"":"&") + key + "=" + java.net.URLEncoder.encode(val,"UTF-8");
					}
				}
				webAgentUrl = webAgentUrl.substring(0, webAgentUrl.indexOf("?")) + "?" + paramUrl;
			}

			logger.debug("webAgentPreview webAgentUrl[2] = " + webAgentUrl);
		} catch(Exception e) {
			logger.error("webAgentPreview error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(webAgentUrl != null && !"".equals(webAgentUrl)) {
			map.put("result", "Success");
			map.put("webAgentUrl", webAgentUrl);
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 메일 파일 읽기
	 * @param templateVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mailFileView")
	public ModelAndView goMailFileView(@ModelAttribute RnsServiceVO serviceVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goMailFileView ContentsPath  = " + serviceVO.getContentsPath());
		
		FileInputStream input = null;
		InputStreamReader reader = null;	
		BufferedReader bufferedReader = null;
		StringBuffer sb = new StringBuffer();
		try {
			String contPath = serviceVO.getContentsPath();
			
			//file = new File(contentsPath);
			//fileReader = new FileReader(file);
			input = new FileInputStream(contPath);
			reader = new InputStreamReader(input,"UTF-8");
			bufferedReader = new BufferedReader(reader);
			String line = "";
			while((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
		} catch(Exception e) {
			logger.error("goMailFileView error = " + e);
		} finally {
			if(bufferedReader != null) try { bufferedReader.close(); } catch(Exception e) {};
			if(reader != null) try { reader.close(); } catch(Exception e) {};
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("result", "Success");
		map.put("contVal", sb.toString().trim());
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	
	
	/**
	 * 실시간 메일 등록/수정 수신자그룹 등록:SQL(팝업)
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/pop/popRnsApprList")
	public String goPopRnsApprList(@ModelAttribute RnsServiceVO serviceVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goPopRnsApprList tid    = " + serviceVO.getTid()); 
		logger.debug("goPopRnsApprList userId    = " + serviceVO.getUserId());
		
		// 요청자 정보 조회
		UserVO userInfo = new UserVO();
		userInfo.setUserId(serviceVO.getUserId());
		userInfo.setUilang((String)session.getAttribute("NEO_UILANG"));
		try {
			userInfo = accountService.getUserInfoDetail(userInfo);
		} catch(Exception e) {
			logger.error("accountService.getUserInfoDetail error = " + e);
		}
		
		// 발송결재라인 목록
		List<RnsSecuApprovalLineVO> rnsApprLineList = null;
		try {
			rnsApprLineList = rnsServiceService.getRnsApprovalLineList(serviceVO.getTid());
		} catch(Exception e) {
			logger.error("rnsServiceService.getRnsApprovalLineList error = " + e);
		}
		
		model.addAttribute("serviceVO", serviceVO);				// 메일정보
		model.addAttribute("userInfo", userInfo);				// 요청자정보
		model.addAttribute("rnsApprLineList", rnsApprLineList);	// 발송결재라인목록
		
		return "rns/svc/pop/popRnsApprList";
	}
	
	/**
	 * 발송대기를 발송승인 상태로 변경한다. 
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/rnsAdmit")
	public ModelAndView updateRnsAdmit(@ModelAttribute RnsServiceVO serviceVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateRnsAdmit taskNo    = " + serviceVO.getTid());
		
		serviceVO.setWorkStatus("001");	// 발송상태:발송승인	
		
		int result = 0;
		try {
			result = rnsServiceService.updateRnsServiceWorkStatus(serviceVO);
		} catch(Exception e) {
			logger.error("rnsServiceService.updateRnsServiceWorkStatus error = " + e);
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
	 * 발송결재라인 결재정보 상신 처리(수정화면에서 처리)
	 * [2021.10.25 상신 처리시 메일정보 수정처리 추가]
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/rnsSubmitApprovalP")
	public ModelAndView updateRnsSubmitApprovalP(@ModelAttribute RnsServiceVO serviceVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		logger.debug("updateRnsSubmitApprovalP tid          = " + serviceVO.getTid());
		logger.debug("updateRnsSubmitApprovalP contentsTyp  = " + serviceVO.getContentsTyp());
		logger.debug("updateRnsSubmitApprovalP deptNo       = " + serviceVO.getDeptNo());
		logger.debug("updateRnsSubmitApprovalP userId       = " + serviceVO.getUserId());
		logger.debug("updateRnsSubmitApprovalP sname        = " + serviceVO.getSname());
		logger.debug("updateRnsSubmitApprovalP smail        = " + serviceVO.getSmail());
		logger.debug("updateRnsSubmitApprovalP webAgentUrl  = " + serviceVO.getWebAgentUrl());
		logger.debug("updateRnsSubmitApprovalP tnm          = " + serviceVO.getTnm());
		logger.debug("updateRnsSubmitApprovalP emailSubject = " + serviceVO.getEmailSubject());
		
		List<RnsAttachVO> attachList = new ArrayList<RnsAttachVO>();		// 첨부파일 목록
		// 파일 사이즈 체크
		if(serviceVO.getAttachPath() != null && !"".equals(serviceVO.getAttachPath())) {
			File attachFile = null;
			String basePath = properties.getProperty("FILE.UPLOAD_PATH") + "/attach";
			
			String[] fileNm = serviceVO.getAttachNm().split(",");
			String[] filePath = serviceVO.getAttachPath().split(",");
			serviceVO.setAttchCnt(filePath.length);		// 첨부파일 수 설정
			for(int i=0;i<filePath.length;i++) {
				attachFile = new File(basePath + "/" + filePath[i]);
				RnsAttachVO attach = new RnsAttachVO();
				attach.setTid(serviceVO.getTid());
				attach.setAttNo(i);
				attach.setAttNm(fileNm[i]);
				attach.setAttFlPath("attach/" + filePath[i]);
				attach.setAttFlSize(attachFile.length());
				attachList.add(attach);
			}
		}
		
		// 기본값 설정
		if(serviceVO.getDeptNo() == 0) serviceVO.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));					// 사용자그룹번호
		if(StringUtil.isNull(serviceVO.getUserId())) serviceVO.setUserId((String)session.getAttribute("NEO_USER_ID"));	// 사용자아이디
		if(StringUtil.isNull(serviceVO.getRecvChkYn())) serviceVO.setRecvChkYn("N");									// 수신확인
		if(StringUtil.isNull(serviceVO.getUseYn())) serviceVO.setUseYn("Y");											// 사용여부
		if(StringUtil.isNull(serviceVO.getSid())) serviceVO.setSid((String)session.getAttribute("NEO_USER_ID"));		// 발송자아이디
		serviceVO.setUpId((String)session.getAttribute("NEO_USER_ID"));													// 수정자ID
		serviceVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));															// 수정일시
		
		// 컬럼 암호화 설정
		serviceVO.setSmail( cryptoService.getEncrypt("SMAIL", serviceVO.getSmail()) );

		// 서비스 작성내용 파일 수정
		String contentsPath = serviceVO.getContentsPath();
		if("1".equals(serviceVO.getContentsTyp().trim())) {
			serviceVO.setContentsPath(null);
		} else {
			if(contentsPath == null || "".equals(contentsPath)) {
				String contFlPath = "content/" + (String)session.getAttribute("NEO_USER_ID") + "/" + StringUtil.getDate(Code.TM_YMDHMSM) + ".tmp";
				String basePath = properties.getProperty("FILE.UPLOAD_PATH");
				String filePath = basePath + "/" + contFlPath;
				contentsPath = filePath;
			}
			
			// 파일을 수정한다.
			FileOutputStream fos = null;
			OutputStreamWriter writer = null;
			try {
				File contFile = new File(contentsPath);
				fos = new FileOutputStream(contFile);
				writer = new OutputStreamWriter(fos, "UTF-8");
				writer.write(serviceVO.getServiceContent());
				writer.flush();
			} catch(Exception e) {
				logger.error("serviceUpdate File Write Error = " + e);
			}
			
			serviceVO.setContentsPath(contentsPath);
		}
		
		// 서비스 정보 등록
		int result = 0;
		try {
			result += rnsServiceService.updateServiceInfo(serviceVO, attachList);
		} catch(Exception e) {
			logger.error("rnsServiceService.updateServiceInfo error = " + e);
		}
 
		serviceVO.setWorkStatus("202");	// 결재진행
		serviceVO.setUpId((String)session.getAttribute("NEO_USER_ID"));
		serviceVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));

		// 메일 상태 업데이트(결재진행)
		try {
			result = rnsServiceService.updateSubmitApproval(serviceVO);
		} catch(Exception e) {
			logger.error("rnsServiceService.updateSubmitApproval error = " + e);
		}
		
		// 첫번째 결재자 정보 조회
		String apprUserId = "";
		try {
			apprUserId = rnsServiceService.getFirstApprUserId(serviceVO.getTid());
		} catch(Exception e) {
			logger.error("rnsServiceService.getFirstApprUserId error = " + e);
		}
		
		// 메일 정보 조회
		RnsServiceVO serviceInfo = new RnsServiceVO();
		try {
			serviceInfo = rnsServiceService.getServiceInfo(serviceVO.getTid());
		} catch(Exception e) {
			logger.error("rnsServiceService.getServiceInfo error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(result > 0) {
			map.put("result", "Success");
			map.put("apprUserId", apprUserId);
			map.put("mailTitle", serviceInfo.getEmailSubject());
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 발송결재라인 결재정보 상신 처리(목록에서 처리)
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/rnsSubmitApproval")
	public ModelAndView updateRnsSubmitApproval(@ModelAttribute RnsServiceVO serviceVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateRnsSubmitApproval tids        = " + serviceVO.getTids());
		logger.debug("updateRnsSubmitApproval status      = " + serviceVO.getStatus());
		
		RnsServiceVO submit = new RnsServiceVO();
		submit.setTid(Integer.parseInt(serviceVO.getTids()));
		submit.setWorkStatus(serviceVO.getStatus());
		submit.setUpId((String)session.getAttribute("NEO_USER_ID"));
		submit.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));

		// 메일 상태 업데이트(결재진행)
		int result = 0;
		try {
			result = rnsServiceService.updateSubmitApproval(submit);
		} catch(Exception e) {
			logger.error("rnsServiceService.updateSubmitApproval error = " + e);
		}
		
		// 첫번째 결재자 정보 조회
		String apprUserId = "";
		try {
			apprUserId = rnsServiceService.getFirstApprUserId(submit.getTid());
		} catch(Exception e) {
			logger.error("rnsServiceService.getFirstApprUserId error = " + e);
		}
		
		// 메일 정보 조회
		RnsServiceVO serviceInfo = new RnsServiceVO();
		try {
			serviceInfo = rnsServiceService.getServiceInfo(submit.getTid());
		} catch(Exception e) {
			logger.error("rnsServiceService.getServiceInfo error = " + e);
		}
		
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(result > 0) {
			map.put("result", "Success");
			map.put("apprUserId", apprUserId);
			map.put("mailTitle", serviceInfo.getEmailSubject());
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 결재취소처리
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/apprCancel")
	public ModelAndView cancelRnsApproval(@ModelAttribute RnsServiceVO serviceVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("cancelRnsApproval tids    = " + serviceVO.getTids());
		logger.debug("cancelRnsApproval status  = " + serviceVO.getStatus());
		
		serviceVO.setUpId((String)session.getAttribute("NEO_USER_ID"));
		serviceVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));

		int result = 0;
		try {
			result = rnsServiceService.cancelRnsApproval(serviceVO);
		} catch(Exception e) {
			logger.error("rnsServiceService.cancelRnsApproval error = " + e);
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
	 * API : goApiSendTsEmailList 
	 * @param params
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @apiNote This Api, for  Huge(over 10,000) items 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/api/sendTsEmailList")
	public void goApiSendTsEmailList(@RequestBody Map<String, Object> params, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("===========endTsEmailList=====================");
		logger.debug("params : " + params);
		
		String sResultmessage ="";
		String sResultcode ="000"; 
		
		JSONObject json = new JSONObject(); 
		String key = "";
		Object value = null;
		for(Map.Entry<String, Object> entry : params.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();
			json.put(key, value); 
		}
		 
		String requestkey = ""; 
		String templatecode = ""; 
		String senderphonenumber = "";
		int campaignno = 999999;
		int pagenumber = 0;
		int totalpagenumber = 0;
		int pagesize = 0;
		
		String requestoption = "";
		String messagename = "";
		String sendduedatatime = "";
		
		if(json.has("requestkey")) {
			requestkey = StringUtil.setNullToString(json.get("requestkey").toString());
			if("".equals(requestkey)) {
				sResultmessage += "[ 메시지요청키 누락 (requestkey)]";
				sResultcode = "E001";
			} else {
				if(requestkey.contains("_")){
					sResultmessage += "[ 메시지요청키 사용불가문자(_) 포함 (requestkey)]";
					sResultcode = "E011";
				} else {
					if(requestkey.length() >28){
						sResultmessage += "[ 메시지요청키 길이제한(28) 초과 (requestkey)]";
						sResultcode = "E011";
					} else {
						try {
							int requestKeyCount =  rnsServiceService.getCountRequestKey(requestkey);
							if (requestKeyCount > 0 ) {
								sResultmessage = "[ 이미 사용된 키값임 (" + requestkey +")]";
								sResultcode = "E007";
							}
						} catch (Exception e) {
							logger.error("rnsServiceService.getCountRequestKey error = " + e);
							sResultmessage = "[ 사용된 키 조회 오류 (" + requestkey +")]";
							sResultcode = "E006";
						}
					}
				}
			}
		} else {
			sResultcode = "E001";
			sResultmessage += "[ 메시지요청키 누락 (requestkey)]";
		}
		
		if(json.has("requestoption")) {
			requestoption = StringUtil.setNullToString(json.get("requestoption").toString());
			if("".equals(requestoption)) {
				sResultmessage += "[ 요청옵션 누락 (requestoption)]";
				sResultcode = "E001";  
			} 
		} else {
			sResultmessage += "[ 요청옵션 누락 (requestoption)]";
			sResultcode = "E001";  
		}
		
		if(json.has("messagename")) {
			messagename = StringUtil.setNullToString(json.get("messagename").toString());
			if("".equals(messagename)) {
				sResultmessage += "[ 메시지 대표 명칭 (messagename)]";
				sResultcode = "E001";  
			} 
		} else {
			sResultmessage += "[ 메시지 대표 명칭 (messagename)]";
			sResultcode = "E001";  
		}
		
		if(json.has("templatecode")) {
			templatecode = StringUtil.setNullToString(json.get("templatecode").toString());
			if("".equals(templatecode)) {
				sResultmessage += "[ 템플릿코드 누락 (templatecode)]";
				sResultcode = "E001";  
			} 
		} else {
			sResultmessage += "[ 템플릿코드 누락 (templatecode)]";
			sResultcode = "E001";  
		}
		
		if(json.has("sendduedatatime")) {
			sendduedatatime = StringUtil.setNullToString(json.get("sendduedatatime").toString());
			if("".equals(sendduedatatime)) {
				sResultmessage += "[ 발송 예정 일시 (sendduedatatime)]";
				sResultcode = "E001";  
			}
			if (!StringUtil.isValidDateString(json.get("sendduedatatime").toString())) {
				sResultmessage += "[ 발송 예정 일시 데이터 오류 (sendduedatatime)]";
				sResultcode = "E008";  
			}
		} else {
			sResultmessage += "[ 발송 예정 일시 (sendduedatatime)]";
			sResultcode = "E001";  
		}
		
		if(json.has("senderemail")) {
			senderphonenumber = StringUtil.setNullToString(json.get("senderemail").toString());
			if("".equals(senderphonenumber)) {
				sResultmessage += "[ 발신자이메일 누락 (senderemail)]";
				sResultcode = "E001";  
			} 
		} else {
			sResultmessage += "[ 발신자이메일 누락 (senderemail)]";
			sResultcode = "E001";  
		}
		
		if(json.has("campaignno")) {
			campaignno = StringUtil.setNullToInt(json.get("campaignno").toString());
			if(campaignno == 0) {
				sResultmessage += "[ 캠페인번호 (campaignno)]";
				sResultcode = "E001";
			}
		} else {
			sResultmessage += "[ 캠페인번호 (campaignno)]";
			sResultcode = "E001";
		}
		
		if(json.has("pagenumber")) {
			pagenumber = StringUtil.setNullToInt(json.get("pagenumber").toString());
			if(pagenumber == 0) {
				sResultmessage += "[ 페이지번호 누락 (pagenumber)]";
				sResultcode = "E001";  
			} 
		} else {
			sResultmessage += "[ 페이지번호 누락 (pagenumber)]";
			sResultcode = "E001";  
		}
		
		if(json.has("totalpagenumber")) {
			totalpagenumber = StringUtil.setNullToInt(json.get("totalpagenumber").toString());
			if(totalpagenumber == 0) {
				sResultmessage += "[ 전체페이지번호 (totalpagenumber)]";
				sResultcode = "E001";
			} 
		} else {
			sResultmessage += "[ 전체페이지번호 (totalpagenumber)]";
			sResultcode = "E001";
		}
		
		if(json.has("pagesize")) {
			pagesize = StringUtil.setNullToInt(json.get("pagesize").toString());
			if(pagesize == 0) {
				sResultmessage += "[ 데이터건수 (pagesize)]";
				sResultcode = "E001";
			} 
		} else {
			sResultmessage += "[ 데이터건수 (pagesize)]";
			sResultcode = "E001";
		}
		
		if ("000".equals(sResultcode)) {
			try {
				// 서비스 정보 조회 By EaiCampNo(templatecode)
				RnsServiceVO templateVO  = rnsServiceService.getServiceInfoByEai(templatecode);
				if (templateVO == null) {
					sResultmessage = "[ 캠페인정보 -실시간이메일템플릿정보 없음 (" + templatecode +")]";
					sResultcode = "E003";
				} else {
					if( templateVO.getTid() != campaignno) {
						sResultmessage = "[ 캠페인정보 -실시간 이메일 캠페인(서비스정보) 없음 (" + campaignno +")]";
						sResultcode = "E010";
					}
				} 
				
			} catch (Exception e) {
				logger.error("rnsServiceService.getSmsTemplate error = " + e);
				sResultmessage = "[ 서버 데이터 캠페인정보 -실시간이메일템플릿정보 조회 오류 (" + templatecode +")]";
				sResultcode = "E006";
			}
		}
		
		if ("000".equals(sResultcode)) {
			ArrayList<Map<String, Object>> dataArrList = (ArrayList) json.get("data");
			if(dataArrList.size() != pagesize) {
				sResultmessage += "[ 데이터건수 불일치 (pagesize)]";
				sResultcode = "E001";
			}
		}
		
		if (!"000".equals(sResultcode)) {
			try {
				sendApiResultJson(response, requestkey, sResultcode, sResultmessage, pagenumber, totalpagenumber, pagesize);
			} catch (Exception e) { 
				logger.error("RnsServiceController.goApiSendTsEmailList Send Return error = " + e);
			} 
		} else {
			//파일로 만든다 ..
			//디렉토리있는지 확인
			String basePath = properties.getProperty("FILE.UPLOAD_PATH") + "/api/tsemail";
			String filePath = basePath + "/" + requestkey + "_" + pagenumber + "_"+ totalpagenumber + ".tmp";
			logger.debug("goApiSendTsEmailList filePath = " + filePath);
			
			// 파일 쓰기
			FileOutputStream fos = null;
			OutputStreamWriter writer = null;
			try {
				File baseDir = new File(basePath);
				if(!baseDir.isDirectory()) {
					baseDir.mkdir();
				}
				 
				File oldfile = new File(filePath);
				if(oldfile.exists()) {
					oldfile.delete();
				}
				
				File file = new File(filePath);
				fos = new FileOutputStream(file);
				writer = new OutputStreamWriter(fos, "UTF-8");
				writer.write(json.toString());
				writer.flush();
			} catch(Exception e) {
				logger.error("goApiSendTsEmailList file write error = " + e);
			} finally {
				if(writer != null) try { writer.close(); } catch(Exception e) {};
				if(fos != null) try { fos.close(); } catch(Exception e) {};
			}
			
			File path = new File(basePath);
			final String pattern = requestkey +"_";
			
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.startsWith(pattern);
				}
			};
			
			String[] fileList = path.list(filter);
			if (fileList.length == totalpagenumber) {
				for (int k = 0 ; k<fileList.length ; k++) {
					String oldName = fileList[k];
					String newName = fileList[k].replace(".tmp", ".txt") ;
					
					Path file = Paths.get(basePath + "/" + oldName);
					Path newFile = Paths.get(basePath + "/" + newName);
					
					try 
					{
						Path newFilePath = Files.move(file, newFile);
						System.out.println(newFilePath);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				sendApiResultJson(response, requestkey, sResultcode, sResultmessage, pagenumber, totalpagenumber, pagesize);
			} catch (Exception e) { 
				logger.error("RnsServiceController.goApiSendTsEmailList Send Return error = " + e);
			}
		}
	}
	
	public static void sendApiResultJson(HttpServletResponse response, String sRequestkey, String sResultcode, String sResultmessage, int nPagenumber, int nTotalpageNumber, int nPagesize) throws Exception {
		
		PrintWriter writer;
		String returnValue = "";
		String sPagenumber = Integer.toString(nPagenumber);
		String sTotalpageNumber = Integer.toString(nTotalpageNumber);
		String sPagesize = Integer.toString(nPagesize);
		
		returnValue = "{ \"requestkey\":\"%s\", \"resultcode\":\"%s\", \"resultmessage\":\"%s\", \"pagenumber\":\"%s\", \"totalpagenumber\":\"%s\", \"pagesize\":[%s]}"; 
		
		if (sResultcode == null){
			sResultcode = "9999";
		}
		if ("000".equals(sResultcode)) {
			sResultmessage ="Success";
		}
		
		response.setContentType("text/plain; charset=UTF-8");
		writer = response.getWriter();
		
		writer.write(String.format(returnValue, sRequestkey, sResultcode, sResultmessage, sPagenumber, sTotalpageNumber, sPagesize));
		
		writer.flush();
		writer.close();
	}
}
