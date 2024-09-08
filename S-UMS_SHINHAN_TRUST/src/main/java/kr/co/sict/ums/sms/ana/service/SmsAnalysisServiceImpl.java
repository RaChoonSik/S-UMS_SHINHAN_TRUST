 /* 작성자 : 김준희
 * 작성일시 : 2022.03.31
 * 설명 : SMS통계분석 서비스 인터페이스
 */
package kr.co.sict.ums.sms.ana.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.sms.ana.dao.SmsAnalysisDAO;
import kr.co.sict.ums.sms.ana.vo.SmsSendLogVO;

@Service
public class SmsAnalysisServiceImpl implements SmsAnalysisService {
	@Autowired
	private SmsAnalysisDAO smsAnalysisDAO;

	@Override
	public List<SmsSendLogVO> getSmsList(SmsSendLogVO smsSendLogVO) throws Exception {
		return smsAnalysisDAO.getSmsList(smsSendLogVO);
	}

	@Override
	public SmsSendLogVO getSmsInfo(SmsSendLogVO smsSendLogVO) throws Exception {
		return smsAnalysisDAO.getSmsInfo(smsSendLogVO);
	}

	@Override
	public List<SmsSendLogVO> getPopSmsSendList(SmsSendLogVO smsSendLogVO) throws Exception {
		return smsAnalysisDAO.getPopSmsSendList(smsSendLogVO);
	}

	@Override
	public List<SmsSendLogVO> getSmsSendLogList(SmsSendLogVO smsSendLogVO) throws Exception {
		return smsAnalysisDAO.getSmsSendLogList(smsSendLogVO);
	}

	@Override
	public String getSmsMessage(SmsSendLogVO smsSendLogVO) throws Exception {
		return smsAnalysisDAO.getSmsMessage(smsSendLogVO);
	}

	@Override
	public List<SmsSendLogVO> getCampSmsList(SmsSendLogVO smsSendLogVO) throws Exception {
		return smsAnalysisDAO.getCampSmsList(smsSendLogVO);
	}
	
    @Override
    public List<SmsSendLogVO> getSmsCampList(SmsSendLogVO smsSendLogVO) throws Exception {
        return smsAnalysisDAO.getSmsCampList(smsSendLogVO);
    }

	@Override
	public List<SmsSendLogVO> getInterfaceList(SmsSendLogVO smsSendLogVO) {
		return smsAnalysisDAO.getInterfaceList(smsSendLogVO);
	}
}
