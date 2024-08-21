/**
 * 작성자 : 김준희
 * 작성일시 : 2021.10.13
 * 설명 : 캠페인관리 데이터 처리
 */
package kr.co.sict.ums.sms.cam.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.sms.ana.vo.SmsSendLogVO;
import kr.co.sict.ums.sms.cam.vo.SmsAttachVO;
import kr.co.sict.ums.sms.cam.vo.SmsCampaignVO;
import kr.co.sict.ums.sms.cam.vo.SmsPhoneVO;
import kr.co.sict.ums.sms.cam.vo.SmsStgVO;
import kr.co.sict.ums.sms.cam.vo.SmsVO;

@Repository
public class SmsCampaignDAO implements SmsCampaignMapper {
	@Autowired
	private SqlSession sqlSessionSms;
	
	@Override
	public List<SmsCampaignVO> getCampaignList(SmsCampaignVO smsCampaignVO) throws Exception{        
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).getCampaignList(smsCampaignVO);
	}

	@Override
	public SmsCampaignVO getCampaignInfo(SmsCampaignVO smsCampaignVO) throws Exception{
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).getCampaignInfo(smsCampaignVO);
	}

	@Override
	public int insertCampaignInfo(SmsCampaignVO smsCampaignVO) throws Exception{        
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).insertCampaignInfo(smsCampaignVO);
	}

	@Override
	public int updateCampaignInfo(SmsCampaignVO smsCampaignVO) throws Exception{
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).updateCampaignInfo(smsCampaignVO);
	}

	@Override
	public List<SmsVO> getSmsList(SmsVO smsVO) throws Exception {
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).getSmsList(smsVO);
	}

	@Override
	public int insertSmsInfo(SmsVO smsVO) throws Exception {
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).insertSmsInfo(smsVO);
	}
	
	@Override
	public int updateSmsStatus(SmsVO smsVO) throws Exception {
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).updateSmsStatus(smsVO);
	}

	@Override
	public int updateSmsStatusAdmit(SmsVO smsVO) throws Exception {
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).updateSmsStatusAdmit(smsVO);
	}

	@Override
	public SmsVO getSmsInfo(SmsVO smsVO) throws Exception {
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).getSmsInfo(smsVO);
	}

	@Override
	public int updateSmsInfo(SmsVO smsVO) throws Exception {
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).updateSmsInfo(smsVO);
	}

	@Override
	public List<SmsPhoneVO> getSmsPhoneList(SmsVO smsVO) throws Exception {
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).getSmsPhoneList(smsVO);
	}

	@Override
	public int insertSmsPhone(SmsPhoneVO smsPhoneVO) throws Exception {
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).insertSmsPhone(smsPhoneVO);
	}
	
	@Override
	public int copySmsPhone(SmsVO smsVO) throws Exception {
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).copySmsPhone(smsVO);
	}

	@Override
	public int deleteSmsPhone(SmsVO smsVO) throws Exception {
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).deleteSmsPhone(smsVO);
	}

	@Override
	public int insertSmsAttachInfo(SmsAttachVO smsAttachVO) throws Exception {
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).insertSmsAttachInfo(smsAttachVO);
	}

	@Override
	public int deleteSmsAttachInfo(SmsVO smsVO) throws Exception {
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).deleteSmsAttachInfo(smsVO);
	}

	@Override
	public List<SmsAttachVO> getSmsAttachList(SmsVO smsVO) throws Exception {
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).getSmsAttachList(smsVO);
	}

	@Override
	public List<SmsVO> getKakaoTemplateMergeList(SmsVO smsVO) throws Exception {
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).getKakaoTemplateMergeList(smsVO);
	}

	@Override
	public int insertKakaoTemplateMerge(SmsVO smsVO) throws Exception {
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).insertKakaoTemplateMerge(smsVO);
	}

	@Override
	public int copyKakaoTemplateMerge(SmsVO smsVO) throws Exception {
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).copyKakaoTemplateMerge(smsVO);
	}

	@Override
	public int deleteKakaoTemplateMerge(SmsVO smsVO) throws Exception {
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).deleteKakaoTemplateMerge(smsVO);
	}
	
	@Override
	public int insertSmsStg(SmsStgVO smsStgVO) throws Exception {
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).insertSmsStg(smsStgVO);
	}

	@Override
	public List<SmsStgVO> getSmsStgList(SmsStgVO smsStgVO) throws Exception {
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).getSmsStgList(smsStgVO);
	}
	

	@Override
	public int getCountRequestKey(String requestKey) throws Exception {
		return sqlSessionSms.getMapper(SmsCampaignMapper.class).getCountRequestKey(requestKey);
	}
}
