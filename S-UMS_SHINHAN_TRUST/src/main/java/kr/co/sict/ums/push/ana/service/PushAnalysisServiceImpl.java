 /* 작성자 : 김준희
 * 작성일시 : 2022.04.01
 * 설명 : PUSH통계분석 서비스 인터페이스
 */
package kr.co.sict.ums.push.ana.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.push.ana.dao.PushAnalysisDAO;
import kr.co.sict.ums.push.ana.vo.PushSendLogVO;

@Service
public class PushAnalysisServiceImpl implements PushAnalysisService {
	@Autowired	
	private PushAnalysisDAO pushAnalysisDAO;
	
	@Override
	public List<PushSendLogVO> getPushList(PushSendLogVO pushSendLogVO) throws Exception{
		return pushAnalysisDAO.getPushList(pushSendLogVO);
	}
	
	@Override
	public PushSendLogVO getPushInfo(PushSendLogVO pushSendLogVO) throws Exception{
		return pushAnalysisDAO.getPushInfo(pushSendLogVO);
	}
	
	@Override
	public List<PushSendLogVO> getCampPushList(PushSendLogVO pushSendLogVO) throws Exception{
		return pushAnalysisDAO.getCampPushList(pushSendLogVO);
	}
	
    @Override
    public List<PushSendLogVO> getPushCampList(PushSendLogVO pushSendLogVO) throws Exception{
        return pushAnalysisDAO.getPushCampList(pushSendLogVO);
    }
}