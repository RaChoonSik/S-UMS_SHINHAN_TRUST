/**
 * 작성자 : 김준희
 * 작성일시 : 2021.10.13
 * 설명 : 캠페인관리 서비스 구현
 */
package kr.co.sict.ums.sms.cam.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.sms.ana.vo.SmsSendLogVO;
import kr.co.sict.ums.sms.cam.dao.SmsCampaignDAO;
import kr.co.sict.ums.sms.cam.vo.SmsAttachVO;
import kr.co.sict.ums.sms.cam.vo.SmsCampaignVO;
import kr.co.sict.ums.sms.cam.vo.SmsPhoneVO;
import kr.co.sict.ums.sms.cam.vo.SmsStgVO;
import kr.co.sict.ums.sms.cam.vo.SmsVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Service
public class SmsCampaignServiceImpl implements SmsCampaignService {
	@Autowired
	private SmsCampaignDAO smsServiceDAO;
	
	@Override
	public List<SmsCampaignVO> getCampaignList(SmsCampaignVO smsCampaignVO) throws Exception{
	    return smsServiceDAO.getCampaignList(smsCampaignVO);
	}


	@Override
	public SmsCampaignVO getCampaignInfo(SmsCampaignVO smsCampaignVO) throws Exception{
	    return smsServiceDAO.getCampaignInfo(smsCampaignVO);
	    
	}

	@Override
	public int insertCampaignInfo(SmsCampaignVO smsCampaignVO) throws Exception{
	    return smsServiceDAO.insertCampaignInfo(smsCampaignVO);
	}

	@Override
	public int updateCampaignInfo(SmsCampaignVO smsCampaignVO) throws Exception{
	    return smsServiceDAO.updateCampaignInfo(smsCampaignVO);
	}
	
	@Override
	public List<SmsVO> getSmsList(SmsVO smsVO) throws Exception {
		return smsServiceDAO.getSmsList(smsVO);
	}
 
	@Override
	public int insertSmsInfo(SmsVO smsVO, List<SmsAttachVO> smsAttachList) throws Exception {
		return  smsServiceDAO.insertSmsInfo(smsVO);
	}

	@Override
	public int updateSmsStatus(SmsVO smsVO) throws Exception {
		return smsServiceDAO.updateSmsStatus(smsVO);
	}
	

	@Override
	public int updateSmsStatusAdmit(SmsVO smsVO) throws Exception {
		return smsServiceDAO.updateSmsStatusAdmit(smsVO);
	}	

	@Override
	public int copySmsInfo(SmsVO smsVO, PropertiesUtil properties, String serverUrl) throws Exception {
		int result = 0;
		// 기존 문자발송 업무 읽기
		SmsVO copySms = smsServiceDAO.getSmsInfo(smsVO);
		// 복사한 문자발송 등록		
		copySms.setKeygen(StringUtil.getDate(Code.TM_YMDHMSM));
		copySms.setRegDt(StringUtil.getDate(Code.TM_YMDHMSM).substring(0,14));
		copySms.setStatus("000");
		copySms.setSmsStatus("000");
		// 기존 대비 추가건
		copySms.setSendDate(StringUtil.getDate(Code.TM_YMDHMSM).substring(0,14));
		copySms.setTaskNm(StringUtil.findAndMakeString(copySms.getTaskNm(), "_COPY"));
		
		result = smsServiceDAO.insertSmsInfo(copySms);
		
		List<SmsPhoneVO> copySmsPhoneList = smsServiceDAO.getSmsPhoneList(smsVO);
		if(copySmsPhoneList != null && copySmsPhoneList.size() > 0) {
			for(SmsPhoneVO smsPhone:copySmsPhoneList) {
				smsPhone.setMsgid(copySms.getMsgid());
				smsPhone.setKeygen(copySms.getKeygen());
				result += smsServiceDAO.insertSmsPhone(smsPhone);
			}
		}
		
		// 첨부파일 목록 읽기
		byte[] buff = new byte[1024];
		int read;
		List<SmsAttachVO> copySmsAttachList = smsServiceDAO.getSmsAttachList(smsVO);
		if(copySmsAttachList != null && copySmsAttachList.size() > 0) {
			for(SmsAttachVO smsAttach:copySmsAttachList) {
				
				// 첨부파일 복사
				String newAttachPath = System.currentTimeMillis() + "_" + smsAttach.getAttNm().replaceAll(" ", "_");
				
				String oldFullAttachPath = serverUrl + properties.getProperty("IMG.SMS_UPLOAD_PATH") + "/" + smsAttach.getAttFlPath();
				String newFullAttachPath = serverUrl + properties.getProperty("IMG.SMS_UPLOAD_PATH") + "/" + newAttachPath;
				
				File oldAttachFile = new File(oldFullAttachPath);
				File newAttachFile = new File(newFullAttachPath);
				
				FileInputStream inputAttach = new FileInputStream(oldAttachFile);
				FileOutputStream outputAttach = new FileOutputStream(newAttachFile);
				
				buff = new byte[1024];
				while((read = inputAttach.read(buff)) > 0) {
					outputAttach.write(buff, 0, read);
					outputAttach.flush();
				}
				inputAttach.close();
				outputAttach.close();
				
				// 복사한 첨부파일 값 설정(설정하지 않은 경우 기존값 사용) 및 등록
				smsAttach.setAttFlPath(newAttachPath);
				smsAttach.setMsgid(copySms.getMsgid());
				smsAttach.setKeygen(copySms.getKeygen());
				
				result += smsServiceDAO.insertSmsAttachInfo(smsAttach);
			}
		}
		
		return result;
	}

	@Override
	public int copyKakaoInfo(SmsVO smsVO) throws Exception {
		int result = 0;
		// 기존 카카오발송 업무 읽기
		SmsVO copyKakao = smsServiceDAO.getSmsInfo(smsVO);
		// 복사한 카카오발송 등록		
		copyKakao.setKeygen(StringUtil.getDate(Code.TM_YMDHMSM));
		copyKakao.setRegDt(StringUtil.getDate(Code.TM_YMDHMSM).substring(0,14));
		copyKakao.setStatus("000");
		copyKakao.setSmsStatus("000");
		// 기존 대비 추가건
		copyKakao.setSendDate(StringUtil.getDate(Code.TM_YMDHMSM).substring(0,14));
		copyKakao.setTaskNm(StringUtil.findAndMakeString(copyKakao.getTaskNm(), "_COPY"));
		
		result = smsServiceDAO.insertSmsInfo(copyKakao);
		
		List<SmsVO> copyKakaoTemplateMergeList = smsServiceDAO.getKakaoTemplateMergeList(smsVO);
		if(copyKakaoTemplateMergeList != null && copyKakaoTemplateMergeList.size() > 0) {
			for(SmsVO kakaoTemplateMerge:copyKakaoTemplateMergeList) {
				kakaoTemplateMerge.setMsgid(copyKakao.getMsgid());
				kakaoTemplateMerge.setKeygen(copyKakao.getKeygen());
				result += smsServiceDAO.insertKakaoTemplateMerge(kakaoTemplateMerge);
			}
		}
		
		List<SmsPhoneVO> copySmsPhoneList = smsServiceDAO.getSmsPhoneList(smsVO);
		if(copySmsPhoneList != null && copySmsPhoneList.size() > 0) {
			for(SmsPhoneVO smsPhone:copySmsPhoneList) {
				smsPhone.setMsgid(copyKakao.getMsgid());
				smsPhone.setKeygen(copyKakao.getKeygen());
				result += smsServiceDAO.insertSmsPhone(smsPhone);
			}
		}
		
		return result;
	}

	
	@Override
	public SmsVO getSmsInfo(SmsVO smsVO) throws Exception {
		return smsServiceDAO.getSmsInfo(smsVO);
	}
	
	@Override
	public int updateSmsInfo(SmsVO smsVO, List<SmsAttachVO> smsAttachList) throws Exception {		
		return smsServiceDAO.updateSmsInfo(smsVO);	
	}
	
	@Override
	public List<SmsPhoneVO> getSmsPhoneList(SmsVO smsVO) throws Exception {
		return smsServiceDAO.getSmsPhoneList(smsVO);
	}
	
	@Override
	public int insertSmsPhone(SmsPhoneVO smsPhoneVO) throws Exception {
		return smsServiceDAO.insertSmsPhone(smsPhoneVO);
	}
	
	@Override
	public int copySmsPhone(SmsVO smsVO) throws Exception {
		return smsServiceDAO.copySmsPhone(smsVO);
	}
	
	@Override
	public int deleteSmsPhone(SmsVO smsVO) throws Exception {
		return smsServiceDAO.deleteSmsPhone(smsVO);
	}
	
	@Override
	public List<SmsAttachVO> getSmsAttachList(SmsVO smsVO) throws Exception {
		return smsServiceDAO.getSmsAttachList(smsVO);
	}
	
	@Override
	public int insertSmsAttachInfo(SmsAttachVO smsAttachVO) throws Exception {
		return smsServiceDAO.insertSmsAttachInfo(smsAttachVO);
	}
	
	@Override
	public int deleteSmsAttachInfo(SmsVO smsVO) throws Exception {
		return smsServiceDAO.deleteSmsAttachInfo(smsVO);
	}
	
	@Override
	public List<SmsVO> getKakaoTemplateMergeList(SmsVO smsVO) throws Exception {
		return smsServiceDAO.getKakaoTemplateMergeList(smsVO);
	}
	
	@Override
	public int insertKakaoTemplateMerge(SmsVO smsVO) throws Exception {
		return smsServiceDAO.insertKakaoTemplateMerge(smsVO);
	}
	
	@Override
	public int copyKakaoTemplateMerge(SmsVO smsVO) throws Exception {
		return smsServiceDAO.copyKakaoTemplateMerge(smsVO);
	}
	
	@Override
	public int deleteKakaoTemplateMerge(SmsVO smsVO) throws Exception {
		return smsServiceDAO.deleteKakaoTemplateMerge(smsVO);
	}
	
	@Override
	public List<SmsStgVO> getSmsStgList(SmsStgVO smsStgVO) throws Exception{
	    return smsServiceDAO.getSmsStgList(smsStgVO);
	}

	@Override
	public int insertSmsStg(SmsStgVO smsStgVO) throws Exception{
	    return smsServiceDAO.insertSmsStg(smsStgVO);
	}

	@Override
	public int getCountRequestKey(String requestKey) throws Exception{
		return smsServiceDAO.getCountRequestKey(requestKey);
	}
}
