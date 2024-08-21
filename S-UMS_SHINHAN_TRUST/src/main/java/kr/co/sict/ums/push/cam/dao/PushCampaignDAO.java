/**
 * 작성자 : 김재환
 * 작성일시 : 2021.12.27
 * 설명 : 캠페인관리 데이터 처리
 */
package kr.co.sict.ums.push.cam.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.push.cam.vo.PushCampaignVO;
import kr.co.sict.ums.push.cam.vo.PushVO;

@Repository
public class PushCampaignDAO implements PushCampaignMapper {
	@Autowired
	private SqlSession sqlSessionPush;
	
    @Override
    public List<PushCampaignVO> getCampaignList(PushCampaignVO pushCampaignVO) throws Exception{        
        return sqlSessionPush.getMapper(PushCampaignMapper.class).getCampaignList(pushCampaignVO);
    }

    @Override
    public PushCampaignVO getCampaignInfo(PushCampaignVO pushCampaignVO) throws Exception{
        return sqlSessionPush.getMapper(PushCampaignMapper.class).getCampaignInfo(pushCampaignVO);
    }

    @Override
    public int insertCampaignInfo(PushCampaignVO pushCampaignVO) throws Exception{        
        return sqlSessionPush.getMapper(PushCampaignMapper.class).insertCampaignInfo(pushCampaignVO);
    }

    @Override
    public int updateCampaignInfo(PushCampaignVO pushCampaignVO) throws Exception{
        return sqlSessionPush.getMapper(PushCampaignMapper.class).updateCampaignInfo(pushCampaignVO);
    }
    
	@Override
	public List<PushVO> getPushList(PushVO pushVO) throws Exception {
		return sqlSessionPush.getMapper(PushCampaignMapper.class).getPushList(pushVO);
	}

	@Override
	public int insertPushInfo(PushVO pushVO) throws Exception {
		return sqlSessionPush.getMapper(PushCampaignMapper.class).insertPushInfo(pushVO);
	}
	
	@Override
	public int updatePushStatus(PushVO pushVO) throws Exception {
		return sqlSessionPush.getMapper(PushCampaignMapper.class).updatePushStatus(pushVO);
	}

	@Override
	public int updatePushStatusAdmit(PushVO pushVO) throws Exception {
		return sqlSessionPush.getMapper(PushCampaignMapper.class).updatePushStatusAdmit(pushVO);
	}

	@Override
	public PushVO getPushInfo(PushVO pushVO) throws Exception {
		return sqlSessionPush.getMapper(PushCampaignMapper.class).getPushInfo(pushVO);
	}
	
	@Override
	public int updatePushInfo(PushVO pushVO) throws Exception {
		return sqlSessionPush.getMapper(PushCampaignMapper.class).updatePushInfo(pushVO);
	}
	
	@Override
	public int getCountRequestKey(String requestKey) throws Exception {
		return sqlSessionPush.getMapper(PushCampaignMapper.class).getCountRequestKey(requestKey);
	}
}
