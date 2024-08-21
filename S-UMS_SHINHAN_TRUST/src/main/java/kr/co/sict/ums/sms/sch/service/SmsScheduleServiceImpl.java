/**
 * 작성자 : 김준희
 * 작성일시 : 2021.10.12
 * 설명 : 일정표 서비스 구현
 */
package kr.co.sict.ums.sms.sch.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.sms.cam.vo.SmsVO;
import kr.co.sict.ums.sms.sch.dao.SmsScheduleDAO;
import kr.co.sict.ums.sms.sch.vo.SmsScheduleVO; 
import kr.co.sict.util.Code;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Service
public class SmsScheduleServiceImpl implements SmsScheduleService {
	@Autowired
	private SmsScheduleDAO smsScheduleDAO;

	@Override
	public List<SmsVO> getScheduleList(SmsVO smsVO) throws Exception{
		return smsScheduleDAO.getScheduleList(smsVO);
	}

	@Override
	public List<SmsScheduleVO> getScheduleAggrList(SmsVO smsVO) throws Exception{
		return smsScheduleDAO.getScheduleAggrList(smsVO);
	}
	
	@Override
	public int getScheduleGrant(SmsVO smsVO) throws Exception{
		return smsScheduleDAO.getScheduleGrant(smsVO);
	}

}
