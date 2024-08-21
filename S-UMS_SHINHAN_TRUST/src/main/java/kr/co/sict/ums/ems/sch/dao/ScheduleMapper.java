/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.22
 * 설명 : 캠페인관리 매퍼
 */
package kr.co.sict.ums.ems.sch.dao;

import java.util.List;

import kr.co.sict.ums.ems.cam.vo.TaskVO;
import kr.co.sict.ums.ems.sch.vo.ScheduleVO;  

public interface ScheduleMapper {
 
	/**
	 * 일정 목록 조회
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public List<TaskVO> getScheduleList(TaskVO taskVO) throws Exception;
	
	/**
	 * 일접 집계 조회
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public List<ScheduleVO> getScheduleAggrList(TaskVO taskVO) throws Exception;
	
	/**
	 * 접근 권한 
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int getScheduleGrant(TaskVO taskVO) throws Exception;	
	
}
