/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.08
 * 설명 : 일정표 서비스 인터페이스
 */
package kr.co.sict.ums.ems.sch.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.sict.ums.ems.cam.vo.TaskVO;
import kr.co.sict.ums.ems.sch.vo.ScheduleVO; 
import kr.co.sict.util.PropertiesUtil;

@Service
public interface ScheduleService {
	/**
	 * 스케쥴 목록 조회
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public List<TaskVO> getScheduleList(TaskVO taskVO) throws Exception;
	
	/**
	 * 스케쥴 집계 조회
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public List<ScheduleVO> getScheduleAggrList(TaskVO taskVO) throws Exception;
	
	/**
	 * 메뉴 접근권한  조회
	 * @param taskVO
	 * @return
	 * @throws Exception
	 */
	public int getScheduleGrant(TaskVO taskVO) throws Exception;
	
	
}
