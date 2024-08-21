/**
 * 작성자 : 김준희
 * 작성일시 : 2021.10.12
 * 설명 : 캠페인관리 매퍼
 */
package kr.co.sict.ums.sms.sch.dao;

import java.util.List;

import kr.co.sict.ums.sms.cam.vo.SmsVO;
import kr.co.sict.ums.sms.sch.vo.SmsScheduleVO;  

public interface SmsScheduleMapper {
 
	/**
	 * 일정 목록 조회
	 * @param smsVO
	 * @return
	 * @throws Exception
	 */
	public List<SmsVO> getScheduleList(SmsVO smsVO) throws Exception;
	
	/**
	 * 일접 집계 조회
	 * @param smsVO
	 * @return
	 * @throws Exception
	 */
	public List<SmsScheduleVO> getScheduleAggrList(SmsVO smsVO) throws Exception;
	
	/**
	 * 접근 권한 
	 * @param smsVO
	 * @return
	 * @throws Exception
	 */
	public int getScheduleGrant(SmsVO smsVO) throws Exception;	
	
}
