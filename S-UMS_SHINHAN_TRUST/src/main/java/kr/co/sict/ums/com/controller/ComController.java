/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.17
 * 설명 : 공통 Controller
 */
package kr.co.sict.ums.com.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import kr.co.sict.ums.com.service.CodeService;
import kr.co.sict.ums.com.service.CryptoService;
import kr.co.sict.ums.com.vo.CodeVO;
import kr.co.sict.ums.com.vo.DownloadVO;
import kr.co.sict.ums.com.vo.UploadVO;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Controller
@RequestMapping("/com")
public class ComController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private CryptoService cryptoService;
	
	@Autowired
	private PropertiesUtil properties;
	
	/**
	 * 사용자 목록 조회
	 * @param metaJoinVO
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getUserList")
	public ModelAndView getUserList(@ModelAttribute CodeVO codeVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getUserList deptNo = " + codeVO.getDeptNo());
		
		// 사용자 목록 조회
		CodeVO userVO = new CodeVO();
		userVO.setDeptNo(codeVO.getDeptNo());
		userVO.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch(Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userList", userList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 파일 업로드 화면을 출력한다.
	 * @param uploadVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/uploadP")
	public String goUploadMain(@ModelAttribute UploadVO uploadVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goUpload ext = " + uploadVO.getExt());
		
		model.addAttribute("upload", uploadVO);
		
		return "com/uploadP";
	}
	
	/**
	 * 파일을 지정된 경로로 업로드 한다.
	 * @param multipartRequest
	 * @param uploadVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/upload")
	public String goUploadProc(MultipartHttpServletRequest multipartRequest, @ModelAttribute UploadVO uploadVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goUploadProc folder    = " + uploadVO.getFolder());
		logger.debug("goUploadProc title     = " + uploadVO.getTitle());
		logger.debug("goUploadProc charset   = " + uploadVO.getCharset());
		logger.debug("goUploadProc formName  = " + uploadVO.getFormName());
		logger.debug("goUploadProc rFileName = " + uploadVO.getrFileName());
		logger.debug("goUploadProc vFileName = " + uploadVO.getvFileName());
		logger.debug("goUploadProc inputType = " + uploadVO.getInputType());
		
		// 업로드 폴더
		String folder = uploadVO.getFolder();
		
		// 파일 디렉토리 체크, 없으면 생성
		String addressFileDirStr  = properties.getProperty("FILE.UPLOAD_PATH") + "/" + folder;
		
		logger.debug("goUploadProc addressFileDirStr = " + addressFileDirStr);
		
		File addressFileDir = new File(addressFileDirStr);
		if(!addressFileDir.exists()) {
			addressFileDir.mkdirs();
		}
		
		// 업로드 파일 처리, 파일이름을 yyyyMMddHHmmss_idx 형식으로 변경하여 저장.
		Iterator<String> files = multipartRequest.getFileNames();
		MultipartFile multipartFile = null;
		String oldFileName = null;
		String newFileName = null;
		while(files.hasNext()) {
			multipartFile = multipartRequest.getFile(files.next());
			if(!multipartFile.isEmpty()) {
				oldFileName		= multipartFile.getOriginalFilename();
				String newFileLong	= Long.toString(System.currentTimeMillis());
				newFileName = newFileLong + "-" + oldFileName.replaceAll(" ", "_");
				String fileLocNm	= addressFileDirStr + "/" + newFileName;
				
				logger.debug("goUploadProc oldFileName = " + oldFileName);
				logger.debug("goUploadProc newFileName = " + newFileName);
				
				try {
					File file = new File(fileLocNm);
					multipartFile.transferTo(file);
				} catch(Exception e) {
					
				}
			}
		}

		model.addAttribute("result", "Success");
		model.addAttribute("uploadVO", uploadVO);
		model.addAttribute("oldFileName", oldFileName);
		model.addAttribute("newFileName", newFileName);
		
		return "com/upload";
	}
	
	/**
	 * 파일을 다운로드 한다.
	 * @param downloadVO
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/down")
	public void fileDownload(@ModelAttribute DownloadVO downloadVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("fileDownload downType = " + downloadVO.getDownType());
		
		String fileName = "";
		String filePath = "";
		String serverUrl = request.getServletContext().getRealPath("/");
		
		// 파일연동 샘플 다운로드
		if("003".equals(downloadVO.getDownType())) {
			fileName = "sample01.csv";
			filePath = properties.getProperty("FILE.UPLOAD_PATH") + "/sample/" + fileName; 
		
		// 파일연동 업로드한 파일 다운로드(수신자그룹 파일연동)
		} else if("002".equals(downloadVO.getDownType())) {
			logger.debug("fileDownload tempFlPath = " + downloadVO.getTempFlPath());
			logger.debug("fileDownload segFlPath = " + downloadVO.getSegFlPath());
			
			fileName = downloadVO.getTempFlPath();
			filePath = properties.getProperty("FILE.UPLOAD_PATH") + "/" + downloadVO.getSegFlPath();
		
		// 메일 첨부파일 다운로드
		} else if("009".equals(downloadVO.getDownType())) {
			logger.debug("fileDownload attachNm   = " + downloadVO.getAttachNm());
			logger.debug("fileDownload attachPath = " + downloadVO.getAttachPath());
			
			fileName = downloadVO.getAttachNm();
			filePath = properties.getProperty("FILE.UPLOAD_PATH") + "/" + downloadVO.getAttachPath();
		
		// PUSH 이미지 첨부파일 다운로드
		} else if("008".equals(downloadVO.getDownType())) {
			
			logger.debug("fileDownload attachNm   = " + downloadVO.getAttachNm());
			logger.debug("fileDownload attachPath = " + downloadVO.getAttachPath());
			
			fileName = downloadVO.getAttachNm();
			filePath = serverUrl + properties.getProperty("IMG.PUSH_UPLOAD_PATH") + "/" + downloadVO.getAttachPath();
		
		// SMS 및 카카오 이미지 첨부파일 다운로드
		} else if("007".equals(downloadVO.getDownType())) {
			logger.debug("fileDownload attachNm   = " + downloadVO.getAttachNm());
			logger.debug("fileDownload attachPath = " + downloadVO.getAttachPath());
			
			fileName = downloadVO.getAttachNm();
			filePath = serverUrl + properties.getProperty("IMG.SMS_UPLOAD_PATH") + "/" + downloadVO.getAttachPath();
		} else if("005".equals(downloadVO.getDownType())) {
			fileName = "sample05.csv";
			filePath = properties.getProperty("FILE.UPLOAD_PATH") + "/sample/" + fileName;
		} else if("006".equals(downloadVO.getDownType())) {
			fileName = "sample06.csv";
			filePath = properties.getProperty("FILE.UPLOAD_PATH") + "/sample/" + fileName;		
		}  else if("011".equals(downloadVO.getDownType())) {
			fileName = "sample11.csv";
			filePath = properties.getProperty("FILE.UPLOAD_PATH") + "/sample/" + fileName;		
		}
		
		
		logger.debug("fileDownload fileName = " + fileName);
		logger.debug("fileDownload filePath = " + filePath);
		
		byte fileBytes[] = FileUtils.readFileToByteArray(new File(filePath));
		
		response.setContentType("application/octet-stream");
		response.setContentLength(fileBytes.length);
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(fileName,"UTF-8") + "\";");
		response.setHeader("Content-Trasfer-Encoding", "binary");
		response.getOutputStream().write(fileBytes);
		
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
	
	/**
	 * 파일을 지정된 경로로 업로드 한다.(디자인 적용으로 추가)
	 * @param multipartRequest
	 * @param uploadVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/uploadSegFile")
	public ModelAndView goUploadSegFile(MultipartHttpServletRequest multipartRequest, @ModelAttribute UploadVO uploadVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goUploadSegFile folder = " + uploadVO.getFolder());
		logger.debug("goUploadSegFile rFileName = " + uploadVO.getrFileName());
		logger.debug("goUploadSegFile vFileName = " + uploadVO.getvFileName());
		
		// 업로드 폴더
		String folder = uploadVO.getFolder();
		
		// 파일 디렉토리 체크, 없으면 생성
		String segFileDirStr  = properties.getProperty("FILE.UPLOAD_PATH") + "/" + folder;
		
		logger.debug("goUploadSegFile segFileDirStr = " + segFileDirStr);
		
		File htmlFileDir = new File(segFileDirStr);
		if(!htmlFileDir.exists()) {
			htmlFileDir.mkdirs();
		}
		
		String fileConvPathStr = properties.getProperty("FILE.CONV_PATH");
		File fileConvDir = new File(fileConvPathStr);
		if(!fileConvDir.exists()) {
			fileConvDir.mkdirs();
		}
		
		// 업로드 파일 처리, 파일이름을 yyyyMMddHHmmss_idx 형식으로 변경하여 저장.
		Iterator<String> files = multipartRequest.getFileNames();
		MultipartFile multipartFile = null;
		String orgFileName = null;
		String newFileName = null;
		String headerInfo  = null;
		
		String uploadResult = "Fail";
		while(files.hasNext()) {
			multipartFile = multipartRequest.getFile(files.next());
			if(!multipartFile.isEmpty()) {
				try {
					orgFileName		= multipartFile.getOriginalFilename();
					String newFileLong	= Long.toString(System.currentTimeMillis());
					newFileName = newFileLong + "-" + orgFileName.replaceAll(" ", "_");
					String fileLocNm	= segFileDirStr + "/" + newFileName;
					String convFileLocNm =  fileConvPathStr + "/" + newFileName;
					
					logger.debug("goUploadSegFile orgFileName = " + orgFileName);
					logger.debug("goUploadSegFile fileLocNm = " + fileLocNm);
					logger.debug("goUploadSegFile convFileLocNm = " + convFileLocNm);
					
					/*
					String line;
					
					BufferedReader br = new BufferedReader(new InputStreamReader(multipartFile.getInputStream(), "UTF-8"));
					
					//+개발서버용 (For the Sict  Dev Server ) 
					File file = new File(fileLocNm);
					FileOutputStream fos = new FileOutputStream(file);
					OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
					BufferedWriter bw = new BufferedWriter(osw);
					
					//
					//BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(fileLocNm, true));
					
					boolean first = true;
					int aliasCnt = 0;	// 알리아스수
					String alias = "";
					while((line=br.readLine()) != null) {
						StringTokenizer st = new StringTokenizer(line, memDelim);
						String encStr = ""; 
						if (first) {
							while(st.hasMoreTokens()) {
								memAlias.add(st.nextToken());
								aliasCnt++;
							}
							if(!"".equals(line.trim()) && line.trim()!= null) {
								headerInfo = line;
							}else {
								headerInfo = "N";
							}
							first = false;
							bw.write(line+"\n");
						} else {
							if(line == null || "".equals(line)) break;
							for(int cnt = 0; cnt < aliasCnt; cnt++) {
								alias = (String)memAlias.get(cnt);
								encStr += cryptoService.getEncrypt(alias,st.nextToken()) + memDelim;
							}
							if(encStr.length() > 1 ) {
								encStr = encStr.substring(0, encStr.length() - 1);
								bw.write(encStr+"\n");
							}
						}
					}
					bw.flush();
					bw.close();
					*/
					try {
						Path convFileLocNmPath = Paths.get(convFileLocNm);
						Files.copy(multipartFile.getInputStream(), convFileLocNmPath, StandardCopyOption.REPLACE_EXISTING); 
						
						if (!uploadSegFileWrite(multipartFile, convFileLocNm, fileLocNm, "UTF-8")) {
							uploadResult = "Fail";
							logger.error("goUploadSegFile uploadSegFileWrite Fail fileLocNm : " + fileLocNm );
						} else {
							uploadResult = "Success";
							logger.debug("goUploadSegFile uploadSegFileWrite Success fileLocNm " + fileLocNm );	
						}
						
					} catch (Exception e ) {
						logger.error("goUploadSegFile copy file error [orgFileName = " + orgFileName + "]");
					}
					
					
					if ("Success".equals(uploadResult)) {
						Path convFilePath = Paths.get(convFileLocNm); 
						try {
							Files.deleteIfExists(convFilePath); 
						} catch (Exception e) {
							logger.error("goUploadSegFile Delete Conv File : + " + convFileLocNm  + " / Fail error :" + e.getMessage() );
						}
					}
				} catch (Exception e) {
					logger.error("goUploadSegFile error = " + e);
				}
			}
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("oldFileName", orgFileName);
		map.put("newFileName", newFileName);
		map.put("headerInfo", headerInfo);
		map.put("result", uploadResult);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	private boolean uploadSegFileWrite(MultipartFile multipartFile, String convFileLocNm, String fileLocNm, String encodingType) {
		boolean retResult = false;

		String orgFileEncoding = getDetectedCharset(convFileLocNm, encodingType);
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(multipartFile.getInputStream(), orgFileEncoding));
			File file = new File(fileLocNm);
			FileOutputStream fos = new FileOutputStream(file); 
			OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
			BufferedWriter bw = new BufferedWriter(osw);
			List<String> memAlias = new ArrayList<String>();
			String memDelim=",";
			boolean first = true;
			int aliasCnt = 0;
			String alias = "";
			String line; 
			 
			String segEncrypt  = properties.getProperty("FILE.SEG_ENCRYPT");
			
			while((line=br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, memDelim);
				String encStr = ""; 
				if (first) {
					while(st.hasMoreTokens()) {
						memAlias.add(st.nextToken());
						aliasCnt++;
					}
					first = false;
					bw.write(line+"\n");
				} else {
					if(line == null || "".equals(line)) break;
					for(int cnt = 0; cnt < aliasCnt; cnt++) {
						alias = (String)memAlias.get(cnt);
						if (!"Y".equals(segEncrypt)) {
							encStr += st.nextToken() + memDelim;
						} else {
							encStr += cryptoService.getEncrypt(alias,st.nextToken()) + memDelim;
						}
					}
					if(encStr.length() > 1 ) {
						encStr = encStr.substring(0, encStr.length() - 1);
						bw.write(encStr+"\n");
					}
				}
			}
			bw.flush();
			bw.close();	
			retResult = true;
			
		} catch (Exception e) {
			retResult = false;
			logger.error("comController.uploadSegFileWrite fileLocNm = ["+ fileLocNm + "] error = " + e.getMessage());
		}
		
		return retResult;
	}
	
	/**
	 * 파일을 지정된 경로로 업로드 한다.(디자인 적용으로 추가)
	 * @param multipartRequest
	 * @param uploadVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/uploadFile")
	public ModelAndView goUploadFile(MultipartHttpServletRequest multipartRequest, @ModelAttribute UploadVO uploadVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goUploadFile folder = " + uploadVO.getFolder());
		logger.debug("goUploadFile rFileName = " + uploadVO.getrFileName());
		logger.debug("goUploadFile vFileName = " + uploadVO.getvFileName());
		
		// 업로드 폴더 (이미 경로 앞에 찍혀 있음 그래서 인위로 앞에 / 붙일 필요 없음)
		String folder = uploadVO.getFolder();
		
		// 파일 디렉토리 체크, 없으면 생성
		String htmlFileDirStr  = properties.getProperty("FILE.UPLOAD_PATH") + "/"  + folder;
		
		logger.debug("goUploadFile htmlFileDirStr = " + htmlFileDirStr);
		
		File htmlFileDir = new File(htmlFileDirStr);
		if(!htmlFileDir.exists()) {
			htmlFileDir.mkdirs();
		}
		
		// 업로드 파일 처리, 파일이름을 yyyyMMddHHmmss_idx 형식으로 변경하여 저장.
		Iterator<String> files = multipartRequest.getFileNames();
		MultipartFile multipartFile = null;
		String oldFileName = null;
		String newFileName = null;
		while(files.hasNext()) {
			multipartFile = multipartRequest.getFile(files.next());
			if(!multipartFile.isEmpty()) {
				oldFileName		= multipartFile.getOriginalFilename();
				String newFileLong	= Long.toString(System.currentTimeMillis());
				newFileName = newFileLong + "-" + oldFileName.replaceAll(" ", "_");
				String fileLocNm	= htmlFileDirStr + "/" + newFileName;
				
				logger.debug("goUploadFile oldFileName = " + oldFileName);
				logger.debug("goUploadFile newFileName = " + newFileName);
				
				try {
					File file = new File(fileLocNm);
					multipartFile.transferTo(file);
				} catch(Exception e) {
					logger.error("goUploadFile error = " + e);
				}
			}
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("oldFileName", oldFileName);
		map.put("newFileName", newFileName);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	
	
	/**
	 * 파일을 지정된 경로로 업로드 한다.(디자인 적용으로 추가) -디렉토리별 
	 * @param multipartRequest
	 * @param uploadVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/uploadFileDir")
	public ModelAndView goUploadFileDir(MultipartHttpServletRequest multipartRequest, @ModelAttribute UploadVO uploadVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goUploadFileDir folder = " + uploadVO.getFolder());
		logger.debug("goUploadFileDir rFileName = " + uploadVO.getrFileName());
		logger.debug("goUploadFileDir vFileName = " + uploadVO.getvFileName());
		
		// 업로드 폴더
		String folder = uploadVO.getFolder();
		
		// 파일 디렉토리 체크, 없으면 생성
		String htmlFileDirStr  = properties.getProperty("FILE.UPLOAD_PATH") + "/" + folder;
		
		logger.debug("uploadFileDir htmlFileDirStr = " + htmlFileDirStr);
		
		File htmlFileDir = new File(htmlFileDirStr);
		if(!htmlFileDir.exists()) {
			htmlFileDir.mkdirs();
		}
		
		String newFileDirStr=  Long.toString(System.currentTimeMillis());
		
		String fileDirStr  = htmlFileDirStr + "/" +  newFileDirStr;
		File fileDir = new File(fileDirStr);
		if(!fileDir.exists()) {
			fileDir.mkdirs();
		}

		// 업로드 파일 처리, 폴더를 yyyyMMddHHmmss 형식으로 만든 후 하단에 저장함 
		Iterator<String> files = multipartRequest.getFileNames();
		MultipartFile multipartFile = null;
		String oldFileName = null;
		String newFileName = null;
		while(files.hasNext()) {
			multipartFile = multipartRequest.getFile(files.next());
			if(!multipartFile.isEmpty()) {
				oldFileName = multipartFile.getOriginalFilename().replaceAll("," , "");
				newFileName = oldFileName.replaceAll(" ", "_").replaceAll("," , ""); 
				
				logger.debug("uploadFileDir fileDir = " + fileDir);
				logger.debug("uploadFileDir newFileName = " + newFileName);
				
				String fileLocNm = fileDir + "/" + newFileName;
				newFileName = newFileDirStr +"/" + newFileName;
				
				logger.debug("uploadFileDir oldFileName = " + oldFileName);
				logger.debug("uploadFileDir newFileName = " + newFileName );
				logger.debug("uploadFileDir fileLocNm = " + fileLocNm);
				  
				try { File file = new File(fileLocNm); multipartFile.transferTo(file); }
				  catch(Exception e) { logger.error("goUploadFile error = " + e); }
				 
			}
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("oldFileName", oldFileName);
		map.put("newFileName", newFileName);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}	
	/**
	 * 파일을 지정된 경로로 업로드 한다.(디자인 적용으로 추가)
	 * @param multipartRequest
	 * @param uploadVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/uploadImgFile")
	public ModelAndView goUploadImgFile(MultipartHttpServletRequest multipartRequest, @ModelAttribute UploadVO uploadVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goUploadFile folder = " + uploadVO.getFolder());
		logger.debug("goUploadFile rFileName = " + uploadVO.getrFileName());
		logger.debug("goUploadFile vFileName = " + uploadVO.getvFileName());
		
		// 업로드 폴더
		String rootPath =request.getSession().getServletContext().getRealPath("/");
		rootPath = rootPath.replace("\\","/");
		
		// 파일 디렉토리 체크, 없으면 생성
		String htmlFileDirStr  = rootPath+ properties.getProperty("IMG.SMS_UPLOAD_PATH") ;
		
		logger.debug("goUploadFile htmlFileDirStr = " + htmlFileDirStr);
		
		File htmlFileDir = new File(htmlFileDirStr);
		if(!htmlFileDir.exists()) {
			htmlFileDir.mkdirs();
		}
		
		// 업로드 파일 처리, 파일이름을 yyyyMMddHHmmss_idx 형식으로 변경하여 저장.
		Iterator<String> files = multipartRequest.getFileNames();
		MultipartFile multipartFile = null;
		String oldFileName = null;
		String newFileName = null;
		while(files.hasNext()) {
			multipartFile = multipartRequest.getFile(files.next());
			if(!multipartFile.isEmpty()) {
				oldFileName		= multipartFile.getOriginalFilename();
				String newFileLong	= Long.toString(System.currentTimeMillis());
				newFileName = newFileLong + "-" + oldFileName.replaceAll(" ", "_");
				String fileLocNm	= htmlFileDirStr + "/" + newFileName;
				
				logger.debug("goUploadFile oldFileName = " + oldFileName);
				logger.debug("goUploadFile newFileName = " + newFileName);
				
				try {
					File file = new File(fileLocNm);
					multipartFile.transferTo(file);
				} catch(Exception e) {
					logger.error("goUploadFile error = " + e);
				}
			}
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("oldFileName", oldFileName);
		map.put("newFileName", newFileName);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * Push Upload Path => Push 이미지 첨부파일 (최대 2개) 
	 * 
	 * Target 경로 => IMG.PUSH_UPLOAD_PATH 
	 * 
	 * @param multipartRequest
	 * @param uploadVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/imgUploadPushPath")
	public ModelAndView goUploadPushImgPath(MultipartHttpServletRequest multipartRequest, @ModelAttribute UploadVO uploadVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goUploadFile For Push folder = " + uploadVO.getFolder());
		logger.debug("goUploadFile For Push rFileName = " + uploadVO.getrFileName());
		logger.debug("goUploadFile For Push vFileName = " + uploadVO.getvFileName());
		
		// 업로드 폴더
		String rootPath =request.getSession().getServletContext().getRealPath("/");
		rootPath = rootPath.replace("\\","/");
		
		// 파일 디렉토리 체크, 없으면 생성
		String htmlFileDirStr  = rootPath+ properties.getProperty("IMG.PUSH_UPLOAD_PATH") ;
		
		logger.debug("goUploadFile htmlFileDirStr = " + htmlFileDirStr);
		
		File htmlFileDir = new File(htmlFileDirStr);
		if(!htmlFileDir.exists()) {
			htmlFileDir.mkdirs();
		}
		
		// 업로드 파일 처리, 파일이름을 yyyyMMddHHmmss_idx 형식으로 변경하여 저장.
		Iterator<String> files = multipartRequest.getFileNames();
		MultipartFile multipartFile = null;
		String oldFileName = null;
		String newFileName = null;
		while(files.hasNext()) {
			multipartFile = multipartRequest.getFile(files.next());
			if(!multipartFile.isEmpty()) {
				oldFileName		= multipartFile.getOriginalFilename();
				String newFileLong	= Long.toString(System.currentTimeMillis());
				newFileName = newFileLong + "-" + oldFileName.replaceAll(" ", "_");
				String fileLocNm	= htmlFileDirStr + "/" + newFileName;
				
				logger.debug("goUploadFile oldFileName = " + oldFileName);
				logger.debug("goUploadFile newFileName = " + newFileName);
				
				try {
					File file = new File(fileLocNm);
					multipartFile.transferTo(file);
				} catch(Exception e) {
					logger.error("goUploadFile error = " + e);
				}
			}
		}
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("oldFileName", oldFileName);
		map.put("newFileName", newFileName);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		return modelAndView;
	}
	
	private String  getDetectedCharset(String fileName, String defaultCharSet) {
		byte[] buf = new byte[2048];
		String encoding = "";
		FileInputStream fis  = null;
		
		try {
			UniversalDetector detector = new UniversalDetector(null);
			
			fis = new FileInputStream(fileName);
			
			int nread;
			while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
				detector.handleData(buf, 0, nread);
			}
			detector.dataEnd();

			encoding = detector.getDetectedCharset();
			
			if(encoding != null && detector.isDone() && Charset.isSupported(encoding)){
				detector.reset();
			}
			if(StringUtil.isNull(encoding)) {
				logger.debug("comController.getDetectedCharset fileName = ["+ fileName + "] No encoding detected"); 
				encoding =defaultCharSet;
			} else {
				logger.debug("comController.getDetectedCharset fileName = ["+ fileName + "] Detected encoding = " + encoding );
			}
			
			detector.reset();
			fis.close();
		} catch(Exception e) {
			logger.error("comController.getDetectedCharset fileName = ["+ fileName + "] error = " + e.getMessage());
		} finally {
			try { 
				if(fis != null) fis.close();
			} catch (Exception e){}
		}
		
		if(StringUtil.isNull(encoding) || "".equals(encoding)) {
			encoding = defaultCharSet;
		}
		return encoding;
	}
}