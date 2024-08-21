/**
 * 작성자 : 김상진
 * 작성일시 : 2021.08.02
 * 설명 : 일정 Controller
 */
package kr.co.sict.ums.ems.sch.controller;

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

import kr.co.sict.ums.ems.cam.vo.TaskVO;
import kr.co.sict.ums.ems.sch.vo.ScheduleVO;
import kr.co.sict.ums.sys.acc.vo.SysMenuVO;
import oracle.net.aso.c;
import kr.co.sict.ums.ems.sch.service.ScheduleService; 

@Controller
@RequestMapping("/ems/sch")
public class ScheduleController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private ScheduleService scheduleService;
	
	@RequestMapping(value="/scheMonthP")
	public String goScheMonthP(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		return "ems/sch/scheMonthP";
	}
	 
	@RequestMapping(value="/scheMonthList")
	public ModelAndView goScheMonthList(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {	
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
		List<ScheduleVO> orgScheduleList = null;
		try {
			orgScheduleList = scheduleService.getScheduleAggrList(searchVO);
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
		
		List<ScheduleVO> scheduleList = new ArrayList<ScheduleVO>();
		calendar.setTime(startDay);
		
		int firstDayWeek = calendar.get(Calendar.DAY_OF_WEEK);
		logger.debug("goScheMonthList firstDayWeek = " + Integer.toString(firstDayWeek));
		 
		String addDay = "";
		String displyYMDFull ="";
		String dayIndex = "";
		int addDayCnt = firstDayWeek;
		int weeks = 1; 
		
		for (int i = 1 ; i <= calDateDays ; i ++ ) {
			ScheduleVO scheduleVO = new ScheduleVO();
			dayIndex = Integer.toString(weeks) +  Integer.toString(addDayCnt);
			
			addDay = transFormat.format(calendar.getTime()).substring(0, 8);
			
			if (curYmd.equals(addDay)) {
				scheduleVO.setToday("Y");
			} else {
				scheduleVO.setToday("N");
			}
			scheduleVO.setSendDt(addDay);
			scheduleVO.setDayIndex(dayIndex);
			scheduleVO.setDisplayYmd(Integer.toString(Integer.parseInt(addDay.substring(6,8))));
			final String[] week = { "일", "월", "화", "수", "목", "금", "토" };
			displyYMDFull = addDay.substring(0,4) +"." + addDay.substring(4,6) +"." + addDay.substring(6,8) + week[calendar.get(Calendar.DAY_OF_WEEK) - 1] + "요일";			
			scheduleVO.setDisplayYmdFull(displyYMDFull);
			calendar.add(Calendar.DATE, 1);
			addDayCnt = addDayCnt + 1;
			if (addDayCnt > 7  ) {
				addDayCnt = 1 ;
				weeks = weeks + 1; 
			} 
			scheduleList.add(scheduleVO);
		}
		
		String sendDate = "" ; 
		int endCnt = 0 ; 
		int approvalCnt = 0 ; 
		for(int i = 0; i < scheduleList.size() ; i++) {
			sendDate = scheduleList.get(i).getSendDt();
			for (int j = 0; j < orgScheduleList.size() ; j++) {
				if(sendDate.equals(orgScheduleList.get(j).getSendDt())) {
					if (orgScheduleList.get(j).getTotCnt() > 0 ) {
						logger.debug("goScheMonthList sendDate = " + sendDate + " / totCnt : " +  orgScheduleList.get(j).getTotCnt());
						scheduleList.get(j).setSchCnt(0);
						scheduleList.get(j).setIngCnt(0);
						scheduleList.get(j).setEndCnt(0);
						scheduleList.get(j).setSucCnt(0);
						scheduleList.get(j).setFailCnt(0);
						scheduleList.get(j).setAppCnt(0);
						scheduleList.get(0).setAigCnt(0);
						scheduleList.get(j).setRejCnt(0); 
						scheduleList.get(j).setAfnCnt(0);
						scheduleList.get(j).setApprovalCnt(0);
						scheduleList.get(j).setSchDesc("0건");
						scheduleList.get(j).setIngDesc("0건");
						scheduleList.get(j).setEndDesc("0건");
						scheduleList.get(j).setSucDesc("0건");
						scheduleList.get(j).setFailDesc("0건");
						scheduleList.get(j).setAppDesc("0건");
						scheduleList.get(0).setAigDesc("0건");
						scheduleList.get(j).setRejDesc("0건"); 
						scheduleList.get(j).setAfnDesc("0건");
						scheduleList.get(j).setApprovalDesc("0건");
						//예약 
						if ( orgScheduleList.get(j).getSchCnt() > 0 ) {
							scheduleList.get(i).setSchCnt( orgScheduleList.get(j).getSchCnt());
						}
						scheduleList.get(i).setSchDesc( Integer.toString(orgScheduleList.get(j).getSchCnt()) + "건");	
						
						//진행 
						if ( orgScheduleList.get(j).getIngCnt() > 0 ) {
							scheduleList.get(i).setIngCnt( orgScheduleList.get(j).getIngCnt());
						}
						scheduleList.get(i).setIngDesc( Integer.toString(orgScheduleList.get(j).getIngCnt()) + "건");
						
						//완료 -- 성공  
						if ( orgScheduleList.get(j).getSucCnt() > 0 ) {
							scheduleList.get(i).setSucCnt( orgScheduleList.get(j).getSucCnt());
						}
						scheduleList.get(i).setSucDesc( Integer.toString(orgScheduleList.get(j).getSucCnt()) + "건");
						
						//완료 -- 실패   
						if ( orgScheduleList.get(j).getFailCnt() > 0 ) {
							scheduleList.get(i).setFailCnt( orgScheduleList.get(j).getFailCnt());
						}
						scheduleList.get(i).setFailDesc( Integer.toString(orgScheduleList.get(j).getFailCnt()) + "건");
						
						//완료 -- 합산 
						endCnt = orgScheduleList.get(j).getSucCnt()  +  orgScheduleList.get(j).getFailCnt() ;
						if ( endCnt  > 0 ) {
							scheduleList.get(i).setEndCnt( endCnt );
						}
						scheduleList.get(i).setEndDesc( Integer.toString(endCnt) + "건");
						
						//결재 --대기 
						if ( orgScheduleList.get(j).getAppCnt() > 0 ) {
							scheduleList.get(i).setAppCnt( orgScheduleList.get(j).getAppCnt());
						}
						scheduleList.get(i).setAppDesc( Integer.toString(orgScheduleList.get(j).getAppCnt()) + "건");
						
						//결재 --진행  
						if ( orgScheduleList.get(j).getAigCnt() > 0 ) {
							scheduleList.get(i).setAigCnt( orgScheduleList.get(j).getAigCnt());
						}
						scheduleList.get(i).setAigDesc( Integer.toString(orgScheduleList.get(j).getAigCnt()) + "건");
						
						//결재 --반려	
						if ( orgScheduleList.get(j).getRejCnt() > 0 ) {
							scheduleList.get(i).setRejCnt( orgScheduleList.get(j).getRejCnt());
						}
						scheduleList.get(i).setRejDesc( Integer.toString(orgScheduleList.get(j).getRejCnt()) + "건");
						
						//결재 --완료
						if ( orgScheduleList.get(j).getAfnCnt() > 0 ) {
							scheduleList.get(i).setAfnCnt( orgScheduleList.get(j).getAfnCnt());
						}
						scheduleList.get(i).setAfnDesc( Integer.toString(orgScheduleList.get(j).getAfnCnt()) + "건");
						
						//결재 -- 합산 
						approvalCnt = orgScheduleList.get(j).getAppCnt() + orgScheduleList.get(j).getAigCnt() + orgScheduleList.get(j).getRejCnt() + orgScheduleList.get(j).getAfnCnt();
						if ( approvalCnt > 0 ) {
							scheduleList.get(i).setApprovalCnt( approvalCnt );
						}
						scheduleList.get(i).setApprovalDesc( Integer.toString(approvalCnt) + "건");
						 
						scheduleList.get(i).setTotCnt( orgScheduleList.get(j).getTotCnt());
						
					} else {
						scheduleList.get(i).setTotCnt( orgScheduleList.get(j).getTotCnt());
					}
				} 
			}  
		} 
 
		
		if (searchVO.getSearchStartDt().equals(searchVO.getSearchStartDt())){
			if ( orgScheduleList.size() == 0 ) {
				scheduleList.get(0).setSchCnt(0);
				scheduleList.get(0).setIngCnt(0);
				scheduleList.get(0).setEndCnt(0);
				scheduleList.get(0).setSucCnt(0);
				scheduleList.get(0).setFailCnt(0);
				scheduleList.get(0).setAppCnt(0);
				scheduleList.get(0).setRejCnt(0); 
				scheduleList.get(0).setAigCnt(0);
				scheduleList.get(0).setAfnCnt(0);
				scheduleList.get(0).setApprovalCnt(0);
				scheduleList.get(0).setSchDesc("0건");
				scheduleList.get(0).setIngDesc("0건");
				scheduleList.get(0).setEndDesc("0건");
				scheduleList.get(0).setSucDesc("0건");
				scheduleList.get(0).setFailDesc("0건");
				scheduleList.get(0).setAppDesc("0건");
				scheduleList.get(0).setRejDesc("0건"); 
				scheduleList.get(0).setAigDesc("0건");
				scheduleList.get(0).setAfnDesc("0건");
				scheduleList.get(0).setApprovalDesc("0건");
			}
			
			
		}
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("scheduleList", scheduleList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);;
		
		return modelAndView;
		
	}
	
	@RequestMapping(value="/scheDayList")
	public ModelAndView goScheDayList(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {	
		logger.debug("goScheDayList getSearchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goScheDayList getSearchEndDt = " + searchVO.getSearchEndDt());
 
		
		String curYmd =""; 
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Calendar calendar = Calendar.getInstance(); 
		
		curYmd = transFormat.format(calendar.getTime()).substring(0, 8);
		
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
		List<TaskVO> orgScheduleList = null;
		try {
			orgScheduleList = scheduleService.getScheduleList(searchVO);
		} catch (Exception e) {
			logger.error("scheduleService.getScheduleList error = " + e);		
		} 
		  
		int addCnt = 0 ;
		List<ScheduleVO> scheduleList = new ArrayList<ScheduleVO>();
 
		String sendTime ="";
		String desc =""; 
			
		for (int j = 0; j < orgScheduleList.size() ; j++) {
			if(orgScheduleList.get(j).getSendDt().length() == 12) {
				
				addCnt += 1;
				ScheduleVO scheduleVO = new ScheduleVO();
				
				scheduleVO.setSendDt(orgScheduleList.get(j).getSendDt().substring(0,8));
				sendTime = orgScheduleList.get(j).getSendDt().substring(8,10) +":" + orgScheduleList.get(j).getSendDt().substring(10,12);
				scheduleVO.setTaskNo(orgScheduleList.get(j).getTaskNo());
				scheduleVO.setSubTaskNo(orgScheduleList.get(j).getSubTaskNo());
				scheduleVO.setSendRepeat(orgScheduleList.get(j).getSendRepeat());
				
				if ( orgScheduleList.get(j).getSendRepeat().equals("000")) {
					scheduleVO.setSendTimeRepeat(sendTime + " " + "[단기]");
				} else if (orgScheduleList.get(j).getSendRepeat().equals("001")){ 
					scheduleVO.setSendTimeRepeat(sendTime + " " + "[정기]");
				} else {
					scheduleVO.setSendTimeRepeat(sendTime + " " + "[오류]");
				}
				
				desc = orgScheduleList.get(j).getTaskNm() + " " + "[" + orgScheduleList.get(j).getSegNm() + "]" +" "  + "(" + orgScheduleList.get(j).getUserNm() + ")";
				scheduleVO.setMailTitle(orgScheduleList.get(j).getMailTitle());
				scheduleVO.setScheduleDesc(desc);
				scheduleVO.setWorkStatusNm(orgScheduleList.get(j).getWorkStatusNm());
				scheduleVO.setWorkStatus(orgScheduleList.get(j).getWorkStatus());
				scheduleVO.setWorkStatusCnt(addCnt);
				scheduleVO.setApprovalProcAppYn(orgScheduleList.get(j).getApprovalProcAppYn());
				scheduleList.add(scheduleVO);  
			} 
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("scheduleList", scheduleList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);;
		
		return modelAndView;
		
	}
	
	
	@RequestMapping(value="/scheWeekP")
	public String goScheWeekP(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		return "ems/sch/scheWeekP";
	}
	
	@RequestMapping(value="/scheWeekList")
	public ModelAndView goScheWeekList(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {	
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
				
		List<TaskVO> orgScheduleList = null;
		try {
			orgScheduleList = scheduleService.getScheduleList(searchVO);
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
		List<ScheduleVO> scheduleList = new ArrayList<ScheduleVO>();
 
		String sendTime ="";
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
			
			for (int j = 0; j < orgScheduleList.size() ; j++) {
				if(orgScheduleList.get(j).getSendDt().length() == 12) {
					if(sendDate.equals(orgScheduleList.get(j).getSendDt().substring(0,8))) { 
						addCnt += 1;
						ScheduleVO scheduleVO = new ScheduleVO();
						
						scheduleVO.setSendDt(orgScheduleList.get(j).getSendDt().substring(0,8));
						sendTime = orgScheduleList.get(j).getSendDt().substring(8,10) +":" + orgScheduleList.get(j).getSendDt().substring(10,12);
						scheduleVO.setDisplayYmd(dayName);
						scheduleVO.setWeekDay(i);
						scheduleVO.setTaskNo(orgScheduleList.get(j).getTaskNo());
						scheduleVO.setSubTaskNo(orgScheduleList.get(j).getSubTaskNo());
						scheduleVO.setSendRepeat(orgScheduleList.get(j).getSendRepeat());
						scheduleVO.setApprovalProcAppYn(orgScheduleList.get(j).getApprovalProcAppYn());
						
						if ( orgScheduleList.get(j).getSendRepeat().equals("000")) {
							scheduleVO.setSendTimeRepeat(sendTime + " " + "[단기]");
						} else if (orgScheduleList.get(j).getSendRepeat().equals("001")){ 
							scheduleVO.setSendTimeRepeat(sendTime + " " + "[정기]");
						} else {
							scheduleVO.setSendTimeRepeat(sendTime + " " + "[오류]");
						}
						
						desc = orgScheduleList.get(j).getTaskNm() + " " + "[" + orgScheduleList.get(j).getSegNm() + "]" +" "  + "(" + orgScheduleList.get(j).getUserNm() + ")";
						scheduleVO.setMailTitle(orgScheduleList.get(j).getMailTitle());
						scheduleVO.setScheduleDesc(desc);
						scheduleVO.setWorkStatusNm(orgScheduleList.get(j).getWorkStatusNm());
						scheduleVO.setWorkStatus(orgScheduleList.get(j).getWorkStatus());
						scheduleVO.setWorkStatusCnt(addCnt);
						scheduleVO.setToday(todayYn);
						scheduleList.add(scheduleVO); 
					}
				}
			} 
			if( addCnt == 0 ) {
				ScheduleVO dayVO = new ScheduleVO();
				dayVO.setWeekDay(i);
				dayVO.setDisplayYmd(dayName);
				dayVO.setWorkStatusCnt(addCnt);
				dayVO.setToday(todayYn);
				scheduleList.add(dayVO); 
			}
		}
		 
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("scheduleList", scheduleList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
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
	public ModelAndView scheduleGrant(@ModelAttribute TaskVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {	
		logger.debug("goScheDayList getSearchSendRepeat = " + searchVO.getSearchSendRepeat()); 
		 
		searchVO.setUserId((String)session.getAttribute("NEO_USER_ID"));
		boolean hasMenuAuth = false; 
		
		try {
			
			List<SysMenuVO> menuList = (List<SysMenuVO>) session.getAttribute("NEO_MENU_LIST");
			SysMenuVO userMenuVO = new SysMenuVO();
		
			String menuId = searchVO.getSearchSendRepeat();
		 
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
		
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		return modelAndView;
		
	}
	
}