/**
 * 작성자 : 김준희
 * 작성일시 : 2021.10.12
 * 설명 : 일정표 데이터 관리
 */
package kr.co.sict.ums.sms.sch.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.sms.cam.vo.SmsVO;
import kr.co.sict.ums.sms.sch.vo.SmsScheduleVO;  

@Repository
public class SmsScheduleDAO implements SmsScheduleMapper {
	@Autowired
	private SqlSession sqlSessionSms;

	@Override
	public List<SmsVO> getScheduleList(SmsVO smsVO) throws Exception {
		return sqlSessionSms.getMapper(SmsScheduleMapper.class).getScheduleList(smsVO);
	} 
	
	@Override
	public List<SmsScheduleVO> getScheduleAggrList(SmsVO smsVO) throws Exception {
		return sqlSessionSms.getMapper(SmsScheduleMapper.class).getScheduleAggrList(smsVO);
	}
	
	@Override
	public int getScheduleGrant(SmsVO smsVO)  throws Exception {
		return sqlSessionSms.getMapper(SmsScheduleMapper.class).getScheduleGrant(smsVO);
	} 	
}
