 /* 작성자 : 김준희
 * 작성일시 : 2022.04.01
 * 설명 : 푸시통계분석 데이터 처리
 */


package kr.co.sict.ums.push.ana.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.push.ana.vo.PushSendLogVO;

@Repository
public class PushAnalysisDAO implements PushAnalysisMapper{
	@Autowired
	private SqlSession sqlSessionAna;
	
	@Override
	public List<PushSendLogVO> getPushList(PushSendLogVO pushSendLogVO) throws Exception{
		return sqlSessionAna.getMapper(PushAnalysisMapper.class).getPushList(pushSendLogVO);
	}
	
	@Override
	public PushSendLogVO getPushInfo(PushSendLogVO pushSendLogVO) throws Exception{
		return sqlSessionAna.getMapper(PushAnalysisMapper.class).getPushInfo(pushSendLogVO);
	}
	
	@Override
	public List<PushSendLogVO> getCampPushList(PushSendLogVO pushSendLogVO) throws Exception{
		return sqlSessionAna.getMapper(PushAnalysisMapper.class).getCampPushList(pushSendLogVO);
	}
	
    @Override
    public List<PushSendLogVO> getPushCampList(PushSendLogVO pushSendLogVO) throws Exception{
        return sqlSessionAna.getMapper(PushAnalysisMapper.class).getPushCampList(pushSendLogVO);
    }
    
}
