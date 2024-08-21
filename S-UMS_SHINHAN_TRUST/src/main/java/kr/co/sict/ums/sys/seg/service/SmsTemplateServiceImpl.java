/**
 * 작성자 : 이혜민
 * 작성일시 : 2022.06.22
 * 설명 : SMS 템플릿관리 서비스 구현
 */
package kr.co.sict.ums.sys.seg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.sys.seg.dao.KakaoTemplateDAO;
import kr.co.sict.ums.sys.seg.dao.SmsTemplateDAO;
import kr.co.sict.ums.sys.seg.vo.KakaoTemplateVO;
import kr.co.sict.ums.sys.seg.vo.SmsTemplateVO;

@Service
public class SmsTemplateServiceImpl implements SmsTemplateService {
	@Autowired
	private SmsTemplateDAO smsTemplateDAO;
	@Autowired
	private KakaoTemplateDAO kakaoTemplateDAO;
	@Override
	public List<SmsTemplateVO> getSmsTemplateList(SmsTemplateVO smsTemplateVO) throws Exception {
		return smsTemplateDAO.getSmsTemplateList(smsTemplateVO);
	}
	@Override
	public List<SmsTemplateVO> getSmsTemplateSimpleList() throws Exception {
		return smsTemplateDAO.getSmsTemplateSimpleList();
	}
	@Override
	public SmsTemplateVO getSmsTemplateInfo(String tempCd) throws Exception {
		return smsTemplateDAO.getSmsTemplateInfo(tempCd);
	}
	@Override
	public int insertSmsTemplateInfo(SmsTemplateVO smsTemplateVO) throws Exception {
		return smsTemplateDAO.insertSmsTemplateInfo(smsTemplateVO);
	}
	@Override
	public List<SmsTemplateVO> checkSmsTemplateCode(String tempCd) throws Exception {
		return smsTemplateDAO.checkSmsTemplateCode(tempCd);
	}
	@Override
	public int updateSmsTemplateInfo(SmsTemplateVO smsTemplateVO) throws Exception {
		int result = smsTemplateDAO.updateSmsTemplateInfo(smsTemplateVO);
		if (result > 0 ) {
			if (smsTemplateVO.getMappInfoInit() == 1) {
				//해당 TEMP_CD를 MAPP_TEMP_CD로 가지고 있는 NEO_KAKAO_TEMPLATE의 TEMP_CD 정보리스트
				String mappTempCd = smsTemplateVO.getTempCd();
				List<KakaoTemplateVO> kakaoTemplateList = kakaoTemplateDAO.getApiKakaoTemplateMappList(mappTempCd);
				if(kakaoTemplateList != null & kakaoTemplateList.size() > 0) {
					for(KakaoTemplateVO kakaoTemplate:kakaoTemplateList) {
						result = kakaoTemplateDAO.deleteApiKakaoTemplateMerge(kakaoTemplate);
					}
				}
			}
		}
		return result;
	}
	@Override
	public int updateSmsTemplateStatus(SmsTemplateVO smsTemplateVO) throws Exception {
		return smsTemplateDAO.updateSmsTemplateStatus(smsTemplateVO);
	}
	@Override
	public SmsTemplateVO getSmsTemplate(String tempCd) throws Exception {
		return smsTemplateDAO.getSmsTemplate(tempCd);
	}
	@Override
	public List<SmsTemplateVO> getApiTemplateList(SmsTemplateVO smsTemplateVO) throws Exception {
		return smsTemplateDAO.getApiTemplateList(smsTemplateVO);
	}
}
