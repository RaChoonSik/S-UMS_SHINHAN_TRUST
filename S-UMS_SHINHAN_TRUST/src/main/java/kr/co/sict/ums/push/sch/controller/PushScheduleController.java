/**

 * 작성자 : 김준희
 * 작성일시 : 2022.04.15
 * 설명 : PUSH 일정 Controller
 */
package kr.co.sict.ums.push.sch.controller;

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

import kr.co.sict.ums.push.cam.vo.PushVO;
import kr.co.sict.ums.push.sch.vo.PushScheduleVO; 
import kr.co.sict.ums.sys.acc.vo.SysMenuVO;
import oracle.net.aso.c;
import kr.co.sict.ums.push.sch.service.PushScheduleService;


@Controller
@RequestMapping("/push/sch")
public class PushScheduleController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private PushScheduleService pushScheduleService;
	
	@RequestMapping(value="/scheMonthP")
	public String goScheMonthP(@ModelAttribute PushVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		return "push/sch/scheMonthP";
	}
	 
	@RequestMapping(value="/scheMonthList")
	public ModelAndView goScheMonthList(@ModelAttribute PushVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {	
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
			
			if (searchVO.getSearchStartDt().length() == 8) {
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
		List<PushScheduleVO> orgPushScheduleList = null;
		try {
			orgPushScheduleList = pushScheduleService.getScheduleAggrList(searchVO);
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
			calDateDays = ( endDay.getTime() - startDay.getTime()) / ( 24*60*60*1000); 
			calDateDays = Math.abs(calDateDays) + 1;
		} catch (Exception e) {
			logger.error("scheduleService.scheMonthList error = " + e);
		}
		logger.debug("goScheMonthList calDateDays = " + Long.toString(calDateDays));
		
		List<PushScheduleVO> pushScheduleList = new ArrayList<PushScheduleVO>();
		calendar.setTime(startDay);
		
		int firstDayWeek = calendar.get(Calendar.DAY_OF_WEEK);
		logger.debug("goScheMonthList firstDayWeek = " + Integer.toString(firstDayWeek));
		 
		String addDay = "";
		String displyYMDFull ="";
		String dayIndex = "";
		int addDayCnt = firstDayWeek; 
		int weeks = 1; 
		
		for (int i = 1 ; i <= calDateDays ; i ++ ) {
			PushScheduleVO pushScheduleVO = new PushScheduleVO(); 
			dayIndex = Integer.toString(weeks) +  Integer.toString(addDayCnt);
			
			addDay = transFormat.format(calendar.getTime()).substring(0, 8);
			
			if (curYmd.equals(addDay)) {
				pushScheduleVO.setToday("Y");
			} else {
				pushScheduleVO.setToday("N");
			}
			pushScheduleVO.setSendDt(addDay);
			pushScheduleVO.setDayIndex(dayIndex);
			pushScheduleVO.setDisplayYmd(Integer.toString(Integer.parseInt(addDay.substring(6,8))));
			final String[] week = { "일", "월", "화", "수", "목", "금", "토" };
			displyYMDFull = addDay.substring(0,4) +"." + addDay.substring(4,6) +"." + addDay.substring(6,8) + week[calendar.get(Calendar.DAY_OF_WEEK) - 1] + "요일";			
			pushScheduleVO.setDisplayYmdFull(displyYMDFull);
			calendar.add(Calendar.DATE, 1);
			addDayCnt = addDayCnt + 1;
			if (addDayCnt > 7  ) {
				addDayCnt = 1 ;
				weeks = weeks + 1; 
			} 
			pushScheduleList.add(pushScheduleVO);
		}
		
		String sendDate = "" ; 
		String orgSendDate = ""; 
		int endCnt = 0 ; 
		int approvalCnt = 0 ; 
		for(int i = 0; i < pushScheduleList.size() ; i++) {
			sendDate = pushScheduleList.get(i).getSendDt();  
			for (int j = 0; j < orgPushScheduleList.size() ; j++) {
				if(orgPushScheduleList.get(j).getSendDt() !=null ) {
					orgSendDate = orgPushScheduleList.get(j).getSendDt();
				}
				if(!"".equals(orgSendDate) && sendDate.equals(orgSendDate.substring(0,8))) {
					if (orgPushScheduleList.get(j).getTotCnt() > 0 ) {
						logger.debug("goScheMonthList sendDate = " + sendDate + " / totCnt : " +  orgPushScheduleList.get(j).getTotCnt());
						pushScheduleList.get(j).setSchCnt(0);
						pushScheduleList.get(j).setIngCnt(0);
						pushScheduleList.get(j).setEndCnt(0);
						pushScheduleList.get(j).setSucCnt(0);
						pushScheduleList.get(j).setFailCnt(0);
						pushScheduleList.get(j).setSchDesc("0건");
						pushScheduleList.get(j).setIngDesc("0건");
						pushScheduleList.get(j).setSucDesc("0건");
						pushScheduleList.get(j).setEndDesc("0건");
						pushScheduleList.get(j).setSucDesc("0건");
						pushScheduleList.get(j).setFailDesc("0건");			 
						//예약 
						if ( orgPushScheduleList.get(j).getSchCnt() > 0 ) {
							pushScheduleList.get(i).setSchCnt( orgPushScheduleList.get(j).getSchCnt());
						}
						pushScheduleList.get(i).setSchDesc( Integer.toString(orgPushScheduleList.get(j).getSchCnt()) + "건");	
						
						//진행 
						if ( orgPushScheduleList.get(j).getIngCnt() > 0 ) {
							pushScheduleList.get(i).setIngCnt( orgPushScheduleList.get(j).getIngCnt());
						}
						pushScheduleList.get(i).setIngDesc( Integer.toString(orgPushScheduleList.get(j).getIngCnt()) + "건");
						
						//완료 -- 성공  
						if ( orgPushScheduleList.get(j).getSucCnt() > 0 ) {
							pushScheduleList.get(i).setSucCnt( orgPushScheduleList.get(j).getSucCnt());
						}
						pushScheduleList.get(i).setSucDesc( Integer.toString(orgPushScheduleList.get(j).getSucCnt()) + "건");
						
						//완료 -- 실패   
						if ( orgPushScheduleList.get(j).getFailCnt() > 0 ) {
							pushScheduleList.get(i).setFailCnt( orgPushScheduleList.get(j).getFailCnt());
						}
						pushScheduleList.get(i).setFailDesc( Integer.toString(orgPushScheduleList.get(j).getFailCnt()) + "건");
						
						//완료 -- 합산 
						endCnt = orgPushScheduleList.get(j).getSucCnt()  +  orgPushScheduleList.get(j).getFailCnt() ;
						if ( endCnt  > 0 ) {
							pushScheduleList.get(i).setEndCnt( endCnt );
						}
						pushScheduleList.get(i).setEndDesc( Integer.toString(endCnt) + "건");						
  						 
						pushScheduleList.get(i).setTotCnt( orgPushScheduleList.get(j).getTotCnt());
						
					} else {
						pushScheduleList.get(i).setTotCnt( orgPushScheduleList.get(j).getTotCnt());
					}
				} 
			}  
		}  
		
		if (searchVO.getSearchStartDt().equals(searchVO.getSearchStartDt())){
			if ( orgPushScheduleList.size() == 0 ) {
				pushScheduleList.get(0).setSchCnt(0);
				pushScheduleList.get(0).setIngCnt(0);
				pushScheduleList.get(0).setEndCnt(0);
				pushScheduleList.get(0).setSucCnt(0); 
				pushScheduleList.get(0).setFailCnt(0);								  
				pushScheduleList.get(0).setSchDesc("0건");
				pushScheduleList.get(0).setIngDesc("0건");
				pushScheduleList.get(0).setEndDesc("0건");
				pushScheduleList.get(0).setSucDesc("0건"); 
				pushScheduleList.get(0).setFailDesc("0건");											
			}
			
			
		}
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("pushScheduleList", pushScheduleList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);;
		
		return modelAndView;
		
	}
	
	@RequestMapping(value="/scheDayList")
	public ModelAndView goScheDayList(@ModelAttribute PushVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {	
		logger.debug("goScheDayList getSearchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goScheDayList getSearchEndDt = " + searchVO.getSearchEndDt());
		logger.debug("goScheDayList getPushmessageId = " + searchVO.getPushmessageId());
		
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
			if (searchVO.getSearchStartDt().length() == 8) {
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
		List<PushVO> orgPushScheduleList = null;
		try {
			orgPushScheduleList = pushScheduleService.getScheduleList(searchVO);
		} catch (Exception e) {
			logger.error("scheduleService.getScheduleList error = " + e);		
		} 
		
		String sendDate ="";
		int addCnt = 0 ;
		List<PushScheduleVO> pushScheduleList = new ArrayList<PushScheduleVO>();
		
		String sendTime ="";
		String desc =""; 
			
		for (int j = 0; j < orgPushScheduleList.size() ; j++) {
			if(orgPushScheduleList.get(j).getSendDt().length() >= 12) {
				
				addCnt += 1;
				PushScheduleVO pushScheduleVO = new PushScheduleVO();
				
				pushScheduleVO.setSendDt(orgPushScheduleList.get(j).getSendDt().substring(0,8));
				pushScheduleVO.setPushmessageId(orgPushScheduleList.get(j).getPushmessageId());
				
				desc = orgPushScheduleList.get(j).getPushTitle() + " " + "[" + orgPushScheduleList.get(j).getSegNm() + "]" +" "  + "(" + orgPushScheduleList.get(j).getUserNm() + ")";
				pushScheduleVO.setPushTitle(orgPushScheduleList.get(j).getPushTitle());
				pushScheduleVO.setPushName(orgPushScheduleList.get(j).getPushName());
				pushScheduleVO.setScheduleDesc(desc);
				pushScheduleVO.setWorkStatusNm(orgPushScheduleList.get(j).getWorkStatusNm());
				pushScheduleVO.setWorkStatus(orgPushScheduleList.get(j).getWorkStatus());
				pushScheduleVO.setWorkStatusCnt(addCnt);
				pushScheduleList.add(pushScheduleVO);  
			} 
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("pushScheduleList", pushScheduleList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);;
		
		return modelAndView;
		
	}
	
	
	@RequestMapping(value="/scheWeekP")
	public String goScheWeekP(@ModelAttribute PushVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		return "push/sch/scheWeekP";
	}
	
	@RequestMapping(value="/scheWeekList")
	public ModelAndView goScheWeekList(@ModelAttribute PushVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {	
		logger.debug("goScheWeekList searchStartDt = " + searchVO.getSearchStartDt());
		logger.debug("goScheWeekList searchEndDt   = " + searchVO.getSearchEndDt());
		logger.debug("goScheWeekList pushmessageId = " + searchVO.getPushmessageId());
		
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
		
		List<PushVO> orgPushScheduleList = null;
		try {
			orgPushScheduleList = pushScheduleService.getScheduleList(searchVO);
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
		List<PushScheduleVO> pushScheduleList = new ArrayList<PushScheduleVO>();
  
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
			
			for (int j = 0; j < orgPushScheduleList.size() ; j++) {
				if(orgPushScheduleList.get(j).getSendDt().length() >= 12) {
					if(sendDate.equals(orgPushScheduleList.get(j).getSendDt().substring(0,8))) { 
						addCnt += 1;
						PushScheduleVO pushScheduleVO = new PushScheduleVO();
						String time_str = orgPushScheduleList.get(j).getSendDt().substring(8,12);
						pushScheduleVO.setSendTime(String.format("%s:%s" , time_str.substring(0, 2),time_str.substring(2, 4)));
						pushScheduleVO.setSendDt(orgPushScheduleList.get(j).getSendDt().substring(0,8));
						pushScheduleVO.setDisplayYmd(dayName);
						pushScheduleVO.setWeekDay(i);
						pushScheduleVO.setPushmessageId(orgPushScheduleList.get(j).getPushmessageId());
						
						desc = orgPushScheduleList.get(j).getPushTitle() + " " + "[" + orgPushScheduleList.get(j).getSegNm() + "]" +" "  + "(" + orgPushScheduleList.get(j).getUserNm() + ")";
						pushScheduleVO.setPushTitle(orgPushScheduleList.get(j).getPushTitle());
						pushScheduleVO.setPushName(orgPushScheduleList.get(j).getPushName());
						pushScheduleVO.setScheduleDesc(desc);
						pushScheduleVO.setWorkStatusNm(orgPushScheduleList.get(j).getWorkStatusNm());
						pushScheduleVO.setWorkStatus(orgPushScheduleList.get(j).getWorkStatus());
						pushScheduleVO.setWorkStatusCnt(addCnt);
						pushScheduleVO.setToday(todayYn);
						pushScheduleList.add(pushScheduleVO); 
					}
				}
			} 
			if( addCnt == 0 ) {
				PushScheduleVO dayVO = new PushScheduleVO();
				dayVO.setWeekDay(i);
				dayVO.setDisplayYmd(dayName);
				dayVO.setWorkStatusCnt(addCnt);
				dayVO.setToday(todayYn);
				pushScheduleList.add(dayVO); 
			}
		}
		 
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("pushScheduleList", pushScheduleList);
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
	public ModelAndView scheduleGrant(@ModelAttribute PushVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {		
		
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