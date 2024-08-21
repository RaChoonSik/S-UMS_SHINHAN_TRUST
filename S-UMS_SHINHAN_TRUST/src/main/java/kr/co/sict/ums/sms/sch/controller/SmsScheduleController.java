/**
 * 작성자 : 김준희
 * 작성일시 : 2021.10.12
 * 설명 : 일정 Controller
 */
package kr.co.sict.ums.sms.sch.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.co.sict.ums.sms.sch.vo.SmsScheduleVO;
import kr.co.sict.ums.sys.acc.vo.SysMenuVO;
import oracle.net.aso.c;
import kr.co.sict.ums.sms.cam.vo.SmsVO;
import kr.co.sict.ums.sms.sch.service.SmsScheduleService; 

@Controller
@RequestMapping("/sms/sch")
public class SmsScheduleController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private SmsScheduleService smsScheduleService;
	
	@RequestMapping(value="/scheMonthP")
	public String goScheMonthP(@ModelAttribute SmsVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		return "sms/sch/scheMonthP";
	}
	 
	@RequestMapping(value="/scheMonthList")
	public ModelAndView goScheMonthList(@ModelAttribute SmsVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {	
		logger.debug("goScheMonthList getSearchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goScheMonthList getSearchEndDt = " + searchVO.getSearchEndDt());
		
		Date startDay = new Date(); 
		Date endDay = new Date(); 
		
		String curYmd ="";
		int year = 0;
		int month = 0; 
		int day = 0 ;
		String fromYMD = "";
		String toYMD = "";
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Calendar calendar = Calendar.getInstance(); 
		
		curYmd = transFormat.format(calendar.getTime()).substring(0, 8);
		
		try {
			if (searchVO.getSearchStartDt() == null || "".equals(searchVO.getSearchStartDt())) {
				searchVO.setSearchStartDt(curYmd.substring(0, 6)) ;
			}
 
			searchVO.setSearchStartDt(searchVO.getSearchStartDt().replace(".", "")); 
			
		    if  (searchVO.getSearchStartDt().length() == 8) {
				fromYMD = searchVO.getSearchStartDt();
				toYMD = searchVO.getSearchStartDt();
			} else { 
				if (searchVO.getSearchStartDt().length() == 6) {
					startDay = transFormat.parse(searchVO.getSearchStartDt() +"01" + " 00:00:00");
				} else {
					startDay = transFormat.parse(curYmd + " 00:00:00");
				}
				calendar.setTime(startDay);
				year = calendar.get(Calendar.YEAR);
				month = calendar.get(Calendar.MONTH) + 1;
				day = calendar.get(Calendar.DATE) ;
				int start = 1; 
				int end =calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				fromYMD = Integer.toString(year)+ getIntToZeroString(month, 2) + getIntToZeroString(start, 2) ;
				toYMD  = Integer.toString(year)+ getIntToZeroString(month, 2) + getIntToZeroString(end, 2) ; 
			}   
		} catch (Exception e) {
			logger.error("scheduleService.scheMonthList error = " + e);
		}
		 
		logger.debug("goScheMonthList fromYMD = " + fromYMD);
		logger.debug("goScheMonthList toYMD = " + toYMD);
		
		searchVO.setSearchStartDt(fromYMD);
		searchVO.setSearchEndDt(toYMD);
		 
		if(searchVO.getSearchDeptNo() == 0) {
			if(!"Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		
		//데이터 가져오기 
		List<SmsScheduleVO> orgSmsScheduleList = null;
		try {
			orgSmsScheduleList = smsScheduleService.getScheduleAggrList(searchVO);
		} catch (Exception e) {
			logger.error("scheduleService.scheMonthList error = " + e);		
		} 
		
		//월데이터 테이블 만들기
		try {			
			startDay = transFormat.parse(fromYMD+ " 00:00:00");
			endDay = transFormat.parse(toYMD + " 00:00:00");
		} catch (Exception e) {
			logger.error("scheduleService.scheMonthList error = " + e);
		}
		
		long calDateDays = 0 ; 
		try {
			calDateDays = ( endDay.getTime() - startDay.getTime())  / ( 24*60*60*1000); 
			calDateDays = Math.abs(calDateDays) + 1;
		} catch (Exception e) {
			logger.error("scheduleService.scheMonthList error = " + e);
		}
		logger.debug("goScheMonthList calDateDays = " + Long.toString(calDateDays));
		
		List<SmsScheduleVO> smsScheduleList = new ArrayList<SmsScheduleVO>();
		calendar.setTime(startDay);
		
		int firstDayWeek = calendar.get(Calendar.DAY_OF_WEEK);					
		logger.debug("goScheMonthList firstDayWeek = " + Integer.toString(firstDayWeek));
		 
		String addDay = "";
		String displyYMDFull ="";
		String dayIndex = "";
		int addDayCnt = firstDayWeek; 
		int weeks = 1; 
		
		for (int i = 1 ; i <= calDateDays ; i ++ ) {
			SmsScheduleVO smsScheduleVO = new SmsScheduleVO(); 
			dayIndex = Integer.toString(weeks) +  Integer.toString(addDayCnt);
			
			addDay = transFormat.format(calendar.getTime()).substring(0, 8);
			
			if (curYmd.equals(addDay)) {
				smsScheduleVO.setToday("Y");
			} else {
				smsScheduleVO.setToday("N");
			}
			smsScheduleVO.setSendDate(addDay);
			smsScheduleVO.setDayIndex(dayIndex);
			smsScheduleVO.setDisplayYmd(Integer.toString(Integer.parseInt(addDay.substring(6,8))));
			final String[] week = { "일", "월", "화", "수", "목", "금", "토" };
			displyYMDFull = addDay.substring(0,4) +"." + addDay.substring(4,6) +"." + addDay.substring(6,8) + week[calendar.get(Calendar.DAY_OF_WEEK) - 1] + "요일";			
			smsScheduleVO.setDisplayYmdFull(displyYMDFull);
			calendar.add(Calendar.DATE, 1);
			addDayCnt = addDayCnt + 1;
			if (addDayCnt > 7  ) {
				addDayCnt = 1 ;
				weeks = weeks + 1; 
			} 
			smsScheduleList.add(smsScheduleVO);
		}
		
		String sendDate = "" ; 
		String orgSendDate = ""; 
		int endCnt = 0 ; 
		int approvalCnt = 0 ; 
		for(int i = 0; i < smsScheduleList.size() ; i++) {
			sendDate = smsScheduleList.get(i).getSendDate();  
			for (int j = 0; j < orgSmsScheduleList.size() ; j++) {
				if(orgSmsScheduleList.get(j).getSendDate() !=null ) {
					orgSendDate = orgSmsScheduleList.get(j).getSendDate();
				}
				if(!"".equals(orgSendDate) && sendDate.equals(orgSendDate.substring(0,8))) {
					if (orgSmsScheduleList.get(j).getTotCnt() > 0 ) {
						logger.debug("goScheMonthList sendDate = " + sendDate + " / totCnt : " +  orgSmsScheduleList.get(j).getTotCnt());
						smsScheduleList.get(j).setSchCnt(0);
						smsScheduleList.get(j).setIngCnt(0);
						smsScheduleList.get(j).setSucCnt(0);
						smsScheduleList.get(j).setSchDesc("0건");
						smsScheduleList.get(j).setIngDesc("0건");
						smsScheduleList.get(j).setSucDesc("0건");
						//예약 
						if ( orgSmsScheduleList.get(j).getSchCnt() > 0 ) {
							smsScheduleList.get(i).setSchCnt( orgSmsScheduleList.get(j).getSchCnt());
						}
						smsScheduleList.get(i).setSchDesc( Integer.toString(orgSmsScheduleList.get(j).getSchCnt()) + "건");	
						
						//진행 
						if ( orgSmsScheduleList.get(j).getIngCnt() > 0 ) {
							smsScheduleList.get(i).setIngCnt( orgSmsScheduleList.get(j).getIngCnt());						
						}
						smsScheduleList.get(i).setIngDesc( Integer.toString(orgSmsScheduleList.get(j).getIngCnt()) + "건");
						
						//완료 -- 성공  
						if ( orgSmsScheduleList.get(j).getSucCnt() > 0 ) {
							smsScheduleList.get(i).setSucCnt( orgSmsScheduleList.get(j).getSucCnt());							
						}
						smsScheduleList.get(i).setSucDesc( Integer.toString(orgSmsScheduleList.get(j).getSucCnt()) + "건");
						
  
						//완료 -- 합산 
						endCnt = orgSmsScheduleList.get(j).getSucCnt()  ;
						if ( endCnt  > 0 ) {
							smsScheduleList.get(i).setEndCnt( endCnt );							
						}
						smsScheduleList.get(i).setEndDesc( Integer.toString(endCnt) + "건");						
  						 
						smsScheduleList.get(i).setTotCnt( orgSmsScheduleList.get(j).getTotCnt());
						
					} else {
						smsScheduleList.get(i).setTotCnt( orgSmsScheduleList.get(j).getTotCnt());
					}
				} 
			}  
		}  
		
		if (searchVO.getSearchStartDt().equals(searchVO.getSearchStartDt())){
			if ( orgSmsScheduleList.size() == 0 ) {
				smsScheduleList.get(0).setSchCnt(0);
				smsScheduleList.get(0).setIngCnt(0);
				smsScheduleList.get(0).setEndCnt(0);
				smsScheduleList.get(0).setSucCnt(0); 
				smsScheduleList.get(0).setSchDesc("0건");
				smsScheduleList.get(0).setIngDesc("0건");
				smsScheduleList.get(0).setEndDesc("0건");
				smsScheduleList.get(0).setSucDesc("0건"); 
			}
			
			
		}
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("smsScheduleList", smsScheduleList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);;
		
		return modelAndView;
		
	}
	
	@RequestMapping(value="/scheDayList")
	public ModelAndView goScheDayList(@ModelAttribute SmsVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {	
		logger.debug("goScheDayList getSearchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goScheDayList getSearchEndDt = " + searchVO.getSearchEndDt());
		
		Date startDay = new Date(); 
		Date endDay = new Date(); 
		
		String curYmd ="";
		int year = 0;
		int month = 0; 
		int day = 0 ;
		String fromYMD = "";
		String toYMD = "";
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Calendar calendar = Calendar.getInstance(); 
		
		curYmd = transFormat.format(calendar.getTime()).substring(0, 8);
		searchVO.setUilang("000");
		
		try {
			if (searchVO.getSearchStartDt() == null || "".equals(searchVO.getSearchStartDt())) {
				searchVO.setSearchStartDt(curYmd);
			}
 
			searchVO.setSearchStartDt(searchVO.getSearchStartDt().replace(".", "")); 
		    if  (searchVO.getSearchStartDt().length() == 8) {
		    	searchVO.setSearchEndDt(searchVO.getSearchStartDt());
			} else { 
				searchVO.setSearchStartDt(curYmd);
				searchVO.setSearchEndDt(curYmd); 
			}
		} catch (Exception e) {
			logger.error("scheduleService.goScheDayList error = " + e);
		}
		 
		logger.debug("goScheDayList fromYMD = " + searchVO.getSearchStartDt());
		logger.debug("goScheDayList toYMD = " + searchVO.getSearchEndDt());
		
 
		if(searchVO.getSearchDeptNo() == 0) {
			if(!"Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		
		//데이터 가져오기 
		List<SmsVO> orgSmsScheduleList = null;
		try {
			orgSmsScheduleList = smsScheduleService.getScheduleList(searchVO);
		} catch (Exception e) {
			logger.error("scheduleService.getScheduleList error = " + e);		
		} 
		 
		String sendDate ="";
		int addCnt = 0 ;
		List<SmsScheduleVO> smsScheduleList = new ArrayList<SmsScheduleVO>();
 
		String sendTime ="";
		String desc =""; 
			
		for (int j = 0; j < orgSmsScheduleList.size() ; j++) {
			if(orgSmsScheduleList.get(j).getSendDate().length() >= 12) {
				
				addCnt += 1;
				SmsScheduleVO smsScheduleVO = new SmsScheduleVO();
				  
				
				smsScheduleVO.setSendDate(orgSmsScheduleList.get(j).getSendDate().substring(0,8));
				smsScheduleVO.setMsgid(orgSmsScheduleList.get(j).getMsgid());
				smsScheduleVO.setKeygen(orgSmsScheduleList.get(j).getKeygen());

				
				desc = orgSmsScheduleList.get(j).getSmsName() + " " + "[" + orgSmsScheduleList.get(j).getSegNm() + "]" +" "  + "(" + orgSmsScheduleList.get(j).getUserNm() + ")";
				smsScheduleVO.setTaskNm(orgSmsScheduleList.get(j).getTaskNm());
				smsScheduleVO.setSmsName(orgSmsScheduleList.get(j).getSmsName());
				smsScheduleVO.setScheduleDesc(desc);
				smsScheduleVO.setStatusNm(orgSmsScheduleList.get(j).getStatusNm());
				smsScheduleVO.setStatus(orgSmsScheduleList.get(j).getStatus());
				smsScheduleVO.setGubun(orgSmsScheduleList.get(j).getGubun());
				smsScheduleVO.setStatusCnt(addCnt);	
				smsScheduleList.add(smsScheduleVO);  
			} 
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("smsScheduleList", smsScheduleList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);;
		
		return modelAndView;
		
	}
	
	
	@RequestMapping(value="/scheWeekP")
	public String goScheWeekP(@ModelAttribute SmsVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		return "sms/sch/scheWeekP";
	}
	
	@RequestMapping(value="/scheWeekList")
	public ModelAndView goScheWeekList(@ModelAttribute SmsVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {	
		logger.debug("goScheWeekList getSearchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goScheWeekList getSearchEndDt = " + searchVO.getSearchEndDt());
		
		if (searchVO.getSearchStartDt() == null || "".equals(searchVO.getSearchStartDt())) {
			SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMdd");
	 		Calendar c = Calendar.getInstance();
	 		c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
	 		searchVO.setSearchStartDt(formatter.format(c.getTime()));
	 		c.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
	 		searchVO.setSearchEndDt(formatter.format(c.getTime()));
	 		
			logger.debug("goScheWeekP init getSearchStartDt = " + searchVO.getSearchStartDt());
			logger.debug("goScheWeekP init getSearchEndDt = " + searchVO.getSearchEndDt());
		} else {
			searchVO.setSearchStartDt(searchVO.getSearchStartDt().replace(".", "")); 
			searchVO.setSearchEndDt(searchVO.getSearchEndDt().replace(".", ""));
		}
		
		if(searchVO.getSearchDeptNo() == 0) {
			if(!"Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))) {
				searchVO.setSearchDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
			}
		}
		
		searchVO.setUilang("000");
		
		List<SmsVO> orgSmsScheduleList = null;
		try {
			orgSmsScheduleList = smsScheduleService.getScheduleList(searchVO);
		} catch (Exception e) {
			logger.error("scheduleService.getScheduleList error = " + e);
		} 

		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Date startDay = new Date();
		try {
			startDay = transFormat.parse(searchVO.getSearchStartDt() + " 00:00:00");
		} catch (Exception e) {
			logger.error("goScheWeekP dayArray Make error = " + e);
		} 
		 
		Calendar cal = Calendar.getInstance();
		String today = transFormat.format(cal.getTime()).substring(0, 8)  ;
		
		cal.setTime(startDay); 
		transFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss"); 
		String[] days = new String[7];
		days[0] = searchVO.getSearchStartDt();
		
		for(int i = 1; i < days.length; i++) { 
			cal.add(Calendar.DATE, 1);
			days[i] = transFormat.format(cal.getTime()).substring(0, 8);
		} 
		
		String sendDate ="";
		int addCnt = 0 ;
		List<SmsScheduleVO> smsScheduleList = new ArrayList<SmsScheduleVO>();
  
		String desc ="";
		String dayName ="";
		String todayYn ="N";
		for(int i = 0; i < days.length; i++) {
			sendDate = days[i];
			dayName = getWeekDayName(sendDate, i);
			addCnt = 0;
			if(today.equals(days[i])) {
				todayYn ="Y";
			} else {
				todayYn ="N";
			}
			
			for (int j = 0; j < orgSmsScheduleList.size() ; j++) {
				if(orgSmsScheduleList.get(j).getSendDate().length() >= 12) {
					if(sendDate.equals(orgSmsScheduleList.get(j).getSendDate().substring(0,8))) { 
						addCnt += 1;
						SmsScheduleVO smsScheduleVO = new SmsScheduleVO();
						
						smsScheduleVO.setSendDate(orgSmsScheduleList.get(j).getSendDate().substring(0,8));
						smsScheduleVO.setDisplayYmd(dayName);
						smsScheduleVO.setWeekDay(i);
						smsScheduleVO.setMsgid(orgSmsScheduleList.get(j).getMsgid());
						smsScheduleVO.setKeygen(orgSmsScheduleList.get(j).getKeygen());
 
						desc = orgSmsScheduleList.get(j).getSmsName() + " " + "[" + orgSmsScheduleList.get(j).getSegNm() + "]" +" "  + "(" + orgSmsScheduleList.get(j).getUserNm() + ")";
						smsScheduleVO.setTaskNm(orgSmsScheduleList.get(j).getTaskNm());
						smsScheduleVO.setSmsName(orgSmsScheduleList.get(j).getSmsName());
						smsScheduleVO.setScheduleDesc(desc);
						smsScheduleVO.setStatusNm(orgSmsScheduleList.get(j).getStatusNm());
						smsScheduleVO.setStatus(orgSmsScheduleList.get(j).getStatus());
						smsScheduleVO.setGubun(orgSmsScheduleList.get(j).getGubun());
						smsScheduleVO.setStatusCnt(addCnt);
						smsScheduleVO.setToday(todayYn);
						smsScheduleList.add(smsScheduleVO); 
					}
				}
			} 
			if( addCnt == 0 ) {
				SmsScheduleVO dayVO = new SmsScheduleVO();
				dayVO.setWeekDay(i);
				dayVO.setDisplayYmd(dayName);
				dayVO.setStatusCnt(addCnt);
				dayVO.setToday(todayYn);
				smsScheduleList.add(dayVO); 
			}
		}
		 
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("smsScheduleList", smsScheduleList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);;
		
		return modelAndView;
		
	}

	private String getWeekDayName(String day, int weekDay) {
		String weekDayName ="";
		String sendMonth ="";
		String sendDay ="";
		char removeZero = '0';
		
		switch(weekDay) {
			case 0 : weekDayName = "일";
					 break;
			case 1 : weekDayName = "월";
					 break;
			case 2 : weekDayName = "화";
					 break;
			case 3 : weekDayName = "수";
					 break;
			case 4 : weekDayName = "목";
					 break;
			case 5 : weekDayName = "금";
					 break;		 
			case 6 : weekDayName = "토";
					 break;
			default : weekDayName = "오류";
					 break;
		}
		
		sendMonth = day.substring(4,6);
		sendDay = day.substring(6,8);
		if (sendMonth.charAt(0) == removeZero) {
			sendMonth = sendMonth.substring(1,2);
		}
		if (sendDay.charAt(0) == removeZero) {
			sendDay = sendDay.substring(1,2);
		}
		weekDayName = weekDayName + " " + sendMonth + "/" + sendDay; 
		return weekDayName;
	}
	
	private String getIntToZeroString(int target, int maxLength) {
	 
		String zeroString = Integer.toString(target);
		if (target < 10 ) { 
			zeroString = String.format("%02d", target);		 
		}
		return zeroString;
	}
	
	@RequestMapping(value="/scheduleGrant")
	public ModelAndView scheduleGrant(@ModelAttribute SmsVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {	
		
		searchVO.setUserId((String)session.getAttribute("NEO_USER_ID"));
		boolean hasMenuAuth = false; 
		
		try {
			
			List<SysMenuVO> menuList = (List<SysMenuVO>) session.getAttribute("NEO_MENU_LIST");
			SysMenuVO userMenuVO = new SysMenuVO();
		
			String menuId = searchVO.getSearchGrantMenuId();
		 
			for (int idx = 0; idx < menuList.size(); idx++) {
				userMenuVO = (SysMenuVO) menuList.get(idx);
				if (menuId.equals(userMenuVO.getMenuId())){
					hasMenuAuth = true;
				}
			} 			
		} catch (Exception e) {
			logger.error("scheduleService.getScheduleGrant error = " + e);		
		} 
		  
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (hasMenuAuth ) {
			map.put("result", "Success");
		} else {
			map.put("result", "Fail");
		}	
		
		ModelAndView modelAndView = new ModelAndView("jsonView", map);;
	 
		
		return modelAndView;
		
	}
	
}