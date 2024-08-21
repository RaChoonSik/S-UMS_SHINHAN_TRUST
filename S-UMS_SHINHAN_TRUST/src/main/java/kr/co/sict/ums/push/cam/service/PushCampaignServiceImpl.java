/**
 * 작성자 : 김재환
 * 작성일시 : 2021.12.27
 * 설명 : 캠페인관리 서비스 구현
 */
package kr.co.sict.ums.push.cam.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.ems.cam.vo.TaskVO;
import kr.co.sict.ums.push.cam.dao.PushCampaignDAO;
import kr.co.sict.ums.push.cam.vo.PushCampaignVO;
import kr.co.sict.ums.push.cam.vo.PushVO;
//import kr.co.sict.ums.push.ana.vo.PushSendLogVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Service
public class PushCampaignServiceImpl implements PushCampaignService {
	@Autowired
	private PushCampaignDAO pushServiceDAO;
	
	@Override
	public List<PushCampaignVO> getCampaignList(PushCampaignVO pushCampaignVO) throws Exception{
		return pushServiceDAO.getCampaignList(pushCampaignVO);
	}
	
	@Override
	public PushCampaignVO getCampaignInfo(PushCampaignVO pushCampaignVO) throws Exception{
		return pushServiceDAO.getCampaignInfo(pushCampaignVO);
	}
	
	@Override
	public int insertCampaignInfo(PushCampaignVO pushCampaignVO) throws Exception{
		return pushServiceDAO.insertCampaignInfo(pushCampaignVO);
	}
	
	@Override
	public int updateCampaignInfo(PushCampaignVO pushCampaignVO) throws Exception{
		return pushServiceDAO.updateCampaignInfo(pushCampaignVO);
	}
	
	@Override
	public List<PushVO> getPushList(PushVO pushVO) throws Exception {
		return pushServiceDAO.getPushList(pushVO);
	}
	
	@Override
	public int insertPushInfo(PushVO pushVO) throws Exception {
		return  pushServiceDAO.insertPushInfo(pushVO);
	}

	@Override
	public int updatePushStatus(PushVO pushVO) throws Exception {
		return pushServiceDAO.updatePushStatus(pushVO);
	}
	
	@Override
	public int updatePushStatusAdmit(PushVO pushVO) throws Exception {
		return pushServiceDAO.updatePushStatusAdmit(pushVO);
	}
	
	@Override
	public int copyPushInfo(PushVO pushVO, PropertiesUtil properties, String serverUrl) throws Exception {
		int result = 0;
		
		// 기존 문자발송 업무 읽기
		PushVO copyPush = pushServiceDAO.getPushInfo(pushVO);
		
		copyPush.setStatus("000");
		copyPush.setWorkStatus("000");
		copyPush.setPushName(StringUtil.findAndMakeString(copyPush.getPushName(), "_COPY"));
		
		if(copyPush.getFileNm() != null && !"".equals(copyPush.getFileNm()) ) {
			
			// 첨부파일 복사
			String newAttachPath = System.currentTimeMillis() + "_" + copyPush.getFileNm().replaceAll(" ", "_");
			
			String oldFullAttachPath = serverUrl + properties.getProperty("IMG.PUSH_UPLOAD_PATH") + "/" + copyPush.getFilePath();
			String newFullAttachPath = serverUrl + properties.getProperty("IMG.PUSH_UPLOAD_PATH") + "/" + newAttachPath;
			
			File oldAttachFile = new File(oldFullAttachPath);
			File newAttachFile = new File(newFullAttachPath);
			
			FileInputStream inputAttach = new FileInputStream(oldAttachFile);
			FileOutputStream outputAttach = new FileOutputStream(newAttachFile);
			
			byte[] buff = new byte[1024];
			int read;
			while((read = inputAttach.read(buff)) > 0) {
				outputAttach.write(buff, 0, read);
				outputAttach.flush();
			}
			inputAttach.close();
			outputAttach.close();
			copyPush.setFilePath(newAttachPath); 
		}
		result = pushServiceDAO.insertPushInfo(copyPush);
		return result;
	} 

	@Override
	public PushVO getPushInfo(PushVO pushVO) throws Exception {
		return pushServiceDAO.getPushInfo(pushVO);
	}
	
	@Override
	public int updatePushInfo(PushVO pushVO)throws Exception {
		return pushServiceDAO.updatePushInfo(pushVO);
	}
	
	@Override
	public int getCountRequestKey(String requestKey) throws Exception{
		return pushServiceDAO.getCountRequestKey(requestKey);
	}

}
