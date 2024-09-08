 /* 작성자 : 김준희
 * 작성일시 : 2022.03.31
 * 설명 : 통계분석 데이터 처리
 */
package kr.co.sict.ums.sms.ana.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.sms.ana.vo.SmsSendLogVO;

@Repository
public class SmsAnalysisDAO implements SmsAnalysisMapper {
	@Autowired
	private SqlSession sqlSessionAna;
	
	@Override
	public List<SmsSendLogVO> getSmsList(SmsSendLogVO smsSendLogVO) throws Exception {
		return sqlSessionAna.getMapper(SmsAnalysisMapper.class).getSmsList(smsSendLogVO);
	} 
	
	@Override
	public SmsSendLogVO getSmsInfo(SmsSendLogVO smsSendLogVO) throws Exception {
		return sqlSessionAna.getMapper(SmsAnalysisMapper.class).getSmsInfo(smsSendLogVO);
	} 
	@Override
	public List<SmsSendLogVO> getPopSmsSendList(SmsSendLogVO smsSendLogVO) throws Exception {
		return sqlSessionAna.getMapper(SmsAnalysisMapper.class).getPopSmsSendList(smsSendLogVO);
	}

	@Override
	public List<SmsSendLogVO> getSmsSendLogList(SmsSendLogVO smsSendLogVO) throws Exception {
		return sqlSessionAna.getMapper(SmsAnalysisMapper.class).getSmsSendLogList(smsSendLogVO);
	}

	@Override
	public String getSmsMessage(SmsSendLogVO smsSendLogVO) throws Exception {
		return sqlSessionAna.getMapper(SmsAnalysisMapper.class).getSmsMessage(smsSendLogVO);
	}

	@Override
	public List<SmsSendLogVO> getCampSmsList(SmsSendLogVO smsSendLogVO) throws Exception {
		return sqlSessionAna.getMapper(SmsAnalysisMapper.class).getCampSmsList(smsSendLogVO);
	}
	
    @Override
    public List<SmsSendLogVO> getSmsCampList(SmsSendLogVO smsSendLogVO) throws Exception {
        return sqlSessionAna.getMapper(SmsAnalysisMapper.class).getSmsCampList(smsSendLogVO);
    }

	public List<SmsSendLogVO> getInterfaceList(SmsSendLogVO smsSendLogVO) {
		return sqlSessionAna.getMapper(SmsAnalysisMapper.class).getInterfaceList(smsSendLogVO);
	} 
    
}
