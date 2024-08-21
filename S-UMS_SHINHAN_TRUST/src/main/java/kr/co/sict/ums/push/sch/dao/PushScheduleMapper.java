/**
 * 작성자 : 김재환
 * 작성일시 : 2021.12.27
 * 설명 : PUSH 캠페인관리 매퍼
 */
package kr.co.sict.ums.push.sch.dao;

import java.util.List;

import kr.co.sict.ums.push.cam.vo.PushVO;
import kr.co.sict.ums.push.sch.vo.PushScheduleVO;  

public interface PushScheduleMapper {
 
	/**
	 * 일정 목록 조회
	 * @param pushVO
	 * @return
	 * @throws Exception
	 */
	public List<PushVO> getScheduleList(PushVO pushVO) throws Exception;
	
	/**
	 * 일접 집계 조회
	 * @param pushVO
	 * @return
	 * @throws Exception
	 */
	public List<PushScheduleVO> getScheduleAggrList(PushVO pushVO) throws Exception;
	
	/**
	 * 접근 권한 
	 * @param pushVO
	 * @return
	 * @throws Exception
	 */
	public int getScheduleGrant(PushVO pushVO) throws Exception;	
	
}
