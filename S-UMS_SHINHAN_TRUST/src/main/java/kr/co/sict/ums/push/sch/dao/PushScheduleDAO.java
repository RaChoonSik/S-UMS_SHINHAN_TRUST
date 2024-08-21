/**
 * 작성자 : 김재환
 * 작성일시 : 2021.12.27
 * 설명 : PUSH 일정표 데이터 관리
 */
package kr.co.sict.ums.push.sch.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.push.cam.vo.PushVO;
import kr.co.sict.ums.push.sch.vo.PushScheduleVO;  

@Repository
public class PushScheduleDAO implements PushScheduleMapper {
	@Autowired
	private SqlSession sqlSessionPush;

	@Override
	public List<PushVO> getScheduleList(PushVO pushVO) throws Exception {
		return sqlSessionPush.getMapper(PushScheduleMapper.class).getScheduleList(pushVO);
	} 
	
	@Override
	public List<PushScheduleVO> getScheduleAggrList(PushVO pushVO) throws Exception {
		return sqlSessionPush.getMapper(PushScheduleMapper.class).getScheduleAggrList(pushVO);
	}
	
	@Override
	public int getScheduleGrant(PushVO pushVO)  throws Exception {
		return sqlSessionPush.getMapper(PushScheduleMapper.class).getScheduleGrant(pushVO);
	} 	
}
