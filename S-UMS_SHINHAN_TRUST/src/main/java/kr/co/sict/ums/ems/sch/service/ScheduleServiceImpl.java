/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.08
 * 설명 : 일정표 서비스 구현
 */
package kr.co.sict.ums.ems.sch.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.ems.sch.dao.ScheduleDAO;
import kr.co.sict.ums.ems.cam.vo.TaskVO;
import kr.co.sict.ums.ems.sch.vo.ScheduleVO; 
import kr.co.sict.util.Code;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Service
public class ScheduleServiceImpl implements ScheduleService {
	@Autowired
	private ScheduleDAO scheduleDAO;

	@Override
	public List<TaskVO> getScheduleList(TaskVO taskVO) throws Exception{
		return scheduleDAO.getScheduleList(taskVO);
	}

	@Override
	public List<ScheduleVO> getScheduleAggrList(TaskVO taskVO) throws Exception{
		return scheduleDAO.getScheduleAggrList(taskVO);
	}
	
	@Override
	public int getScheduleGrant(TaskVO taskVO) throws Exception{
		return scheduleDAO.getScheduleGrant(taskVO);
	}

}
