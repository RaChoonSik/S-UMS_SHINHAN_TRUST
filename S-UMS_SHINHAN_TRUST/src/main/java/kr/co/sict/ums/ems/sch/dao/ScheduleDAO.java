/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.08
 * 설명 : 일정표 데이터 관리
 */
package kr.co.sict.ums.ems.sch.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.ems.cam.vo.TaskVO;
import kr.co.sict.ums.ems.sch.vo.ScheduleVO;  

@Repository
public class ScheduleDAO implements ScheduleMapper {
	@Autowired
	private SqlSession sqlSessionEms;

	@Override
	public List<TaskVO> getScheduleList(TaskVO taskVO) throws Exception {
		return sqlSessionEms.getMapper(ScheduleMapper.class).getScheduleList(taskVO);
	} 
	
	@Override
	public List<ScheduleVO> getScheduleAggrList(TaskVO taskVO) throws Exception {
		return sqlSessionEms.getMapper(ScheduleMapper.class).getScheduleAggrList(taskVO);
	}
	
	@Override
	public int getScheduleGrant(TaskVO taskVO)  throws Exception {
		return sqlSessionEms.getMapper(ScheduleMapper.class).getScheduleGrant(taskVO);
	} 	
}
