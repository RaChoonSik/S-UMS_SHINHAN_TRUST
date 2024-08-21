/**
 * 작성자 : 김재환
 * 작성일시 : 2021.12.27
 * 설명 : PUSH 일정표 서비스 구현
 */
package kr.co.sict.ums.push.sch.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.push.cam.vo.PushVO;
import kr.co.sict.ums.push.sch.dao.PushScheduleDAO;
import kr.co.sict.ums.push.sch.vo.PushScheduleVO;
import kr.co.sict.util.Code;
import kr.co.sict.util.PropertiesUtil;
import kr.co.sict.util.StringUtil;

@Service
public class PushScheduleServiceImpl implements PushScheduleService {
	@Autowired
	private PushScheduleDAO pushScheduleDAO;

	@Override
	public List<PushVO> getScheduleList(PushVO pushVO) throws Exception{
		return pushScheduleDAO.getScheduleList(pushVO);
	}

	@Override
	public List<PushScheduleVO> getScheduleAggrList(PushVO pushVO) throws Exception{
		return pushScheduleDAO.getScheduleAggrList(pushVO);
	}
	
	@Override
	public int getScheduleGrant(PushVO pushVO) throws Exception{
		return pushScheduleDAO.getScheduleGrant(pushVO);
	}

}
