/**
 * 작성자 : 김준희
 * 작성일시 : 2021.10.12
 * 설명 : 일정표 서비스 인터페이스
 */
package kr.co.sict.ums.sms.sch.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.sict.ums.sms.cam.vo.SmsVO;
import kr.co.sict.ums.sms.sch.vo.SmsScheduleVO; 
import kr.co.sict.util.PropertiesUtil;

@Service
public interface SmsScheduleService {
	/**
	 * 스케쥴 목록 조회
	 * @param smsVO
	 * @return
	 * @throws Exception
	 */
	public List<SmsVO> getScheduleList(SmsVO smsVO) throws Exception;
	
	/**
	 * 스케쥴 집계 조회
	 * @param smsVO
	 * @return
	 * @throws Exception
	 */
	public List<SmsScheduleVO> getScheduleAggrList(SmsVO smsVO) throws Exception;
	
	/**
	 * 메뉴 접근권한  조회
	 * @param smsVO
	 * @return
	 * @throws Exception
	 */
	public int getScheduleGrant(SmsVO smsVO) throws Exception;
	
	
}
