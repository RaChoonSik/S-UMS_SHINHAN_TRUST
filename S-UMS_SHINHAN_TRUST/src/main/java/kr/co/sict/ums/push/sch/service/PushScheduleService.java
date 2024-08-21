/**
 * 작성자 : 김재환
 * 작성일시 : 2021.12.27
 * 설명 : PUSH 일정표 서비스 인터페이스
 */
package kr.co.sict.ums.push.sch.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.sict.ums.push.cam.vo.PushVO;
import kr.co.sict.ums.push.sch.vo.PushScheduleVO;
import kr.co.sict.util.PropertiesUtil;

@Service
public interface PushScheduleService {
	/**
	 * 스케쥴 목록 조회
	 * @param pushVO
	 * @return
	 * @throws Exception
	 */
	public List<PushVO> getScheduleList(PushVO pushVO) throws Exception;
	
	/**
	 * 스케쥴 집계 조회
	 * @param pushVO
	 * @return
	 * @throws Exception
	 */
	public List<PushScheduleVO> getScheduleAggrList(PushVO pushVO) throws Exception;
	
	/**
	 * 메뉴 접근권한  조회
	 * @param pushVO
	 * @return
	 * @throws Exception
	 */
	public int getScheduleGrant(PushVO pushVO) throws Exception;
	
	
}
