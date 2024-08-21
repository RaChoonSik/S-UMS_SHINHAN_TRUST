/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.06
 * 설명 : 문자열 관련 처리 모음
 */
package kr.co.sict.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class StringUtil {
	
	/**
	 * Null 문자열인 경우 0을 반환한다. Null이 아닌 경우 문자열을 숫자로 변환한다.
	 * @param str
	 * @return
	 */
	public static int setNullToInt(String str) {
		int nReturn = 0 ;
		try {
			if(str != null && !"".equals(str)) {
				nReturn = Integer.parseInt(str);
			}
		} catch(NumberFormatException e) {
			nReturn = 0;
		}
		
		return nReturn ; 
	}
	
	/**
	 * 전달받은 숫자(i)가 0인 경우 res 값을 반환하고. 0이 아닌 경우 해당 숫자로 변환한다.(받은 값이 없을 경우 1로 세팅하기 위함)
	 * @param str
	 * @return
	 */
	public static int setNullToInt(int i, int res) {
		if(i == 0) {
			return res;
		} else {
			return i;
		}
	}
	
	/**
	 * 대문자로 변환하여 반환한다.
	 * @param str
	 * @return
	 */
	public static String setUpperString(String str) {
		if(str == null) {
			return "";
		} else {
			return str.toUpperCase();
		}
	}
	
	/**
	 * 문자(한자리 숫자)를 두자리 문자(숫자)로 변환하여 반환한다.
	 * @param str
	 * @return
	 */
	public static String setTwoDigitsString(String str) {
		try {
			if(str == null || "".equals(str)) {
				return "00";
			} else {
				int i = Integer.parseInt(str);
				if(i < 10)
					return "0" + i;
				else
					return "" + i ;
			}
		} catch(Exception e) {
			return "00";
		}
	}
	
	/**
	 * Null 문자열인 경우 ""을 반환한다. Null이 아닌 경우 문자열을 변환한다.
	 * @param str
	 * @return
	 */
	public static String setNullToString(String str) {
		if(str != null && !"".equals(str)) {
			return str;
		} else {
			return "";
		} 
	}
	
	/**
	 * 값이 NULL인지 체크한다.
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		if(str == null || "".equals(str)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * 문자열이 날짜 형식으로 변경 가능한 것인지 확인 
	 * @param str
	 * @return
	 */
	public static boolean isValidDateString(String str) {
		boolean ret = false;
		if(str != null && !"".equals(str)) {
			try {
				if(str.length() != 14 ) {
					ret = false;
				} else {
					SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyyMMddHHmmss"); //검증할 날짜 포맷 설정
					dateFormatParser.setLenient(false); //false일경우 처리시 입력한 값이 잘못된 형식일 시 오류가 발생
					dateFormatParser.parse(str); //대상 값 포맷에 적용되는지 확인
					ret = true;
				}
			} catch (Exception e) {
				ret = false;
			}
		} 
		return ret;
	}
	
	/**
	 * 현재 날짜에서 기간을 계산한 날짜를 지정한 양식(format)으로 반환한다.
	 * durType => D:Date, M:Month, Y:Year<br/>
	 * @param dur
	 * @param durType
	 * @param format
	 * @return
	 */
	public static String getCalcDateFromCurr(int dur, String durType, String format) {
		Date curDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(curDate);
		if("D".equals(durType)) {
			cal.add(Calendar.DATE, dur);
		} else if("M".equals(durType)) {
			cal.add(Calendar.MONTH, dur);
		} else if("Y".equals(durType)) {
			cal.add(Calendar.YEAR, dur);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(cal.getTime());
	}
	
    /**
    *
    * 현재 시간을 읽어온다.
    * @param   int		시간 포맷
	 *					<pre>
    *   				레벨 :  TM_YMDHMS(1) ==> 년월일시분초, TM_MDHMS(2) ==> 월일시분초,
    *          				TM_DHMS(3) ==> 일시분초, TM_HMS(4) ==> 시분초,
    *          				TM_MS(5) ==> 분초,  TM_S(6) ==> 초,
    *          				TM_YMD(7) ==> 년월일, TM_YMDHM(8) ==> 년월일시분
    *					</pre>
    *
    */
   public static String getDate(int level) {
       Calendar cal = Calendar.getInstance();
       
       String Y = Integer.toString(cal.get(Calendar.YEAR));
       String M = lPad(Integer.toString(cal.get(Calendar.MONTH)+1),2,"0");
       String D = lPad(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)),2,"0");
       String H = lPad(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)),2,"0");
       String MI = lPad(Integer.toString(cal.get(Calendar.MINUTE)),2,"0");
       String S = lPad(Integer.toString(cal.get(Calendar.SECOND)),2,"0");
       String MS = lPad(Integer.toString(cal.get(Calendar.MILLISECOND)),4,"0"); 
       
       String retval = "";
       
		switch(level) {
		
			case Code.TM_Y:		//년
					retval = Y;
					break;
			case Code.TM_M:		//월
					retval = M;
					break;
			case Code.TM_D:		//일
					retval = D;
					break;
			case Code.TM_H:		//시
					retval = H;
					break;
			case Code.TM_MI:	//분
					retval = MI;
					break;
			case Code.TM_S:		//초
					retval = S;
					break;
			case Code.TM_YM:	//년월
					retval = Y+M;
					break;
			case Code.TM_YMD:	//년월일
					retval = Y+M+D;
					break;
			case Code.TM_YMDH:	//년월일시
					retval = Y+M+D+H;
					break;
			case Code.TM_YMDHM:	//년월일시분
					retval = Y+M+D+H+MI;
					break;
			case Code.TM_YMDHMS:	//년월일시분초
					retval = Y+M+D+H+MI+S;
					break;
			case Code.TM_MDHMS:		//월일시분초
					retval = M+D+H+MI+S;
					break;
			case Code.TM_DHMS:		//일시분초
					retval = D+H+MI+S;
					break;
			case Code.TM_HMS:		//시분초
					retval = H+MI+S;
					break;
			case Code.TM_MS:		//분초
					retval = MI+S;
					break;
			case Code.TM_YMDHMSM:	//년월일시분초밀리세컨
					retval = Y+M+D+H+MI+S+MS;
					break;
		
		}					
		return retval;
   }
   
	/**
	 * 0 필러
	 * @param		data		해당 데이터
	 * 				size		단윈
	 *				filler		채울값
	 * @return		String		변형문자열
	 */
	public static String lPad(String data, int size, String filler) {
		int csize = data.trim().length();
		if(size == csize) return data;

		String retstr = data;	
		for(int i = 0; i < (size-csize); i++) {
			retstr = "0" + retstr;
		}
		return retstr;
	}
	
	/**
	 * 
	 * 특정날짜의 특정 시간 이전이나 이후의 날짜를 구한다. <br>
	 * 예를 들어 20030210의 7시간 후 날짜는 언제인가 ?
	 * 
	 * @param stand_date
	 *            기준 일자
	 * @param interval
	 *            간격(시간, +시간, -시간)
	 * @return String 날짜(space면 포맷상의 문제)
	 * 
	 */
	public static String getIntervalTime(String stand_date, String interval) {
		return getIntervalTime(stand_date, parseLong(interval));
	}

	/**
	 * 
	 * 특정날짜의 특정 시간 이전이나 이후의 날짜를 구한다. <br>
	 * 예를 들어 20030210의 7시간 후 날짜는 언제인가 ?
	 * 
	 * @param stand_date
	 *            기준 일자
	 * @param interval
	 *            간격(시간, +시간, -시간)
	 * @return String 날짜(space면 포맷상의 문제)
	 * 
	 */
	public static String getIntervalTime(String stand_date, long interval) {
		if (stand_date == null) {
			return "";
		}

		int length = stand_date.length();

		int year, month, day, hour, minute, second;

		String retval = "";

		Calendar cal = null;

		long stand_date_long, interval_long, temp_long;

		try {

			switch (length) {

			case 8:
				year = Integer.parseInt(stand_date.substring(0, 4));
				month = Integer.parseInt(stand_date.substring(4, 6)) - 1;
				day = Integer.parseInt(stand_date.substring(6, 8));

				// 기준 날짜 설정
				cal = Calendar.getInstance();
				cal.clear();
				cal.set(year, month, day);

				// 기준 날짜의 밀리세컨 총수
				stand_date_long = (cal.getTime()).getTime();

				// 간격에 대한 밀리세컨 총수
				interval_long = interval * (60 * 1000);

				temp_long = stand_date_long + interval_long;

				cal.clear();
				cal.setTime(new Date(temp_long));

				retval = Integer.toString(cal.get(Calendar.YEAR))
						+ lPad(Integer.toString(cal.get(Calendar.MONTH) + 1), 2, "0")
						+ lPad(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)), 2,
								"0");
				break;

			case 10:
				year = Integer.parseInt(stand_date.substring(0, 4));
				month = Integer.parseInt(stand_date.substring(4, 6)) - 1;
				day = Integer.parseInt(stand_date.substring(6, 8));
				hour = Integer.parseInt(stand_date.substring(8, 10));
				minute = 0;

				// 기준 날짜 설정
				cal = Calendar.getInstance();
				cal.clear();
				cal.set(year, month, day, hour, minute);

				// 기준 날짜의 밀리세컨 총수
				stand_date_long = (cal.getTime()).getTime();

				// 간격에 대한 밀리세컨 총수
				interval_long = interval * (60 * 1000);

				temp_long = stand_date_long + interval_long;

				cal.clear();
				cal.setTime(new Date(temp_long));

				retval = Integer.toString(cal.get(Calendar.YEAR))
						+ lPad(Integer.toString(cal.get(Calendar.MONTH) + 1), 2, "0")
						+ lPad(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)), 2,
								"0")
						+ lPad(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)), 2,
								"0");
				break;

			case 12:
				year = Integer.parseInt(stand_date.substring(0, 4));
				month = Integer.parseInt(stand_date.substring(4, 6)) - 1;
				day = Integer.parseInt(stand_date.substring(6, 8));
				hour = Integer.parseInt(stand_date.substring(8, 10));
				minute = Integer.parseInt(stand_date.substring(10, 12));

				// 기준 날짜 설정
				cal = Calendar.getInstance();
				cal.clear();
				cal.set(year, month, day, hour, minute);

				// 기준 날짜의 밀리세컨 총수
				stand_date_long = (cal.getTime()).getTime();

				// 간격에 대한 밀리세컨 총수
				interval_long = interval * (60 * 1000);

				temp_long = stand_date_long + interval_long;

				cal.clear();
				cal.setTime(new Date(temp_long));

				retval = Integer.toString(cal.get(Calendar.YEAR))
						+ lPad(Integer.toString(cal.get(Calendar.MONTH) + 1), 2, "0")
						+ lPad(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)), 2, "0")
						+ lPad(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)), 2, "0")
						+ lPad(Integer.toString(cal.get(Calendar.MINUTE)), 2, "0");
				break;

			case 14:
				year = Integer.parseInt(stand_date.substring(0, 4));
				month = Integer.parseInt(stand_date.substring(4, 6)) - 1;
				day = Integer.parseInt(stand_date.substring(6, 8));
				hour = Integer.parseInt(stand_date.substring(8, 10));
				minute = Integer.parseInt(stand_date.substring(10, 12));
				second = Integer.parseInt(stand_date.substring(12, 14));

				// 기준 날짜 설정
				cal = Calendar.getInstance();
				cal.clear();
				cal.set(year, month, day, hour, minute, second);

				// 기준 날짜의 밀리세컨 총수
				stand_date_long = (cal.getTime()).getTime();

				// 간격에 대한 밀리세컨 총수
				interval_long = interval * (60 * 1000);

				temp_long = stand_date_long + interval_long;

				cal.clear();
				cal.setTime(new Date(temp_long));

				retval = Integer.toString(cal.get(Calendar.YEAR))
						+ lPad(Integer.toString(cal.get(Calendar.MONTH) + 1), 2, "0")
						+ lPad(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)), 2, "0")
						+ lPad(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)), 2, "0")
						+ lPad(Integer.toString(cal.get(Calendar.MINUTE)), 2, "0")
						+ lPad(Integer.toString(cal.get(Calendar.SECOND)), 2, "0");
				break;

			case 18:
				year = Integer.parseInt(stand_date.substring(0, 4));
				month = Integer.parseInt(stand_date.substring(4, 6)) - 1;
				day = Integer.parseInt(stand_date.substring(6, 8));
				hour = Integer.parseInt(stand_date.substring(8, 10));
				minute = Integer.parseInt(stand_date.substring(10, 12));
				second = Integer.parseInt(stand_date.substring(12, 14));

				// 기준 날짜 설정
				cal = Calendar.getInstance();
				cal.clear();
				cal.set(year, month, day, hour, minute, second);

				// 기준 날짜의 밀리세컨 총수
				stand_date_long = (cal.getTime()).getTime();

				// 간격에 대한 밀리세컨 총수
				interval_long = interval * (60 * 1000);

				temp_long = stand_date_long + interval_long;

				cal.clear();
				cal.setTime(new Date(temp_long));

				retval = Integer.toString(cal.get(Calendar.YEAR))
						+ lPad(Integer.toString(cal.get(Calendar.MONTH) + 1), 2, "0")
						+ lPad(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)), 2, "0")
						+ lPad(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)), 2, "0")
						+ lPad(Integer.toString(cal.get(Calendar.MINUTE)), 2, "0")
						+ lPad(Integer.toString(cal.get(Calendar.SECOND)), 2, "0")
						+ lPad(Integer.toString(cal.get(Calendar.MILLISECOND)), 4, "0");
				break;

			default:
				retval = "";

			}
		} catch (Exception e) {
			return "";
		}

		return retval;

	}
	
	/**
	 * 해당 데이터를 long형으로 변경한다(null이거나 space일 경우 0).
	 * 
	 * @param input
	 *            변형할 데이터
	 * @return long 리턴 데이터
	 */
	public static long parseLong(String input) {
		if (input == null || input.equals("")) {
			return 0;
		} else {
			return Long.parseLong(input);
		}
	}
	
	/**
	 * 
	 * 날짜포맷으로 변환한다. <br>
	 * 20010830 --> 2001/08/30
	 * 
	 * @param datestr
	 *            날짜데이터
	 * @return String 날짜 출력 형식 변환 데이터
	 * 
	 */
	public static String getFDate(String datestr) {
		if(datestr == null || "".equals(datestr)) {
			return "";
		} else {
			int length = datestr.length();
			String fdate = null;
			switch (length) {
			case 8:
				fdate = datestr.substring(0, 4);
				fdate += "/";
				fdate += datestr.substring(4, 6);
				fdate += "/";
				fdate += datestr.substring(6, 8);
				break;
			case 12:
				fdate = datestr.substring(0, 4);
				fdate += "/";
				fdate += datestr.substring(4, 6);
				fdate += "/";
				fdate += datestr.substring(6, 8);
				fdate += " ";
				fdate += datestr.substring(8, 10);
				fdate += ":";
				fdate += datestr.substring(10, 12);
				break;
			default:
				fdate = "";
			}
			return fdate;
		}
	}
	
	/**
	 * 
	 * 날짜포맷으로 변환한다. <br>
	 * 20010830 --> 2001/08/30 또는 2001-08-30 또는 2001년 08월 30일 로 변환한다.
	 * 
	 * @param datestr
	 *            날짜데이터
	 * @param type
	 *            출력 포맷<br>
	 *            (DT_FMT1(1) --> xxxx/xx/xx, DT_FMT2(2) --> xxxx-xx-xx,
	 *            DT_KOR(3) --> xxxx년 xx월 xx일)
	 * @return String 날짜 출력 형식 변환 데이터
	 * 
	 */
	public static String getFDate(String datestr, int type) {
		if(datestr == null || "".equals(datestr)) {
			return "";
		} else {
			int length = datestr.length();
			String fdate = null;
			switch (type) {
			case Code.DT_FMT1:
				switch (length) {
				case 8:
					fdate = datestr.substring(0, 4);
					fdate += "/";
					fdate += datestr.substring(4, 6);
					fdate += "/";
					fdate += datestr.substring(6, 8);
					break;
				case 12:
					fdate = datestr.substring(0, 4);
					fdate += "/";
					fdate += datestr.substring(4, 6);
					fdate += "/";
					fdate += datestr.substring(6, 8);
					fdate += " ";
					fdate += datestr.substring(8, 10);
					fdate += ":";
					fdate += datestr.substring(10, 12);
					break;
				case 14:
					fdate = datestr.substring(0, 4);
					fdate += "/";
					fdate += datestr.substring(4, 6);
					fdate += "/";
					fdate += datestr.substring(6, 8);
					fdate += " ";
					fdate += datestr.substring(8, 10);
					fdate += ":";
					fdate += datestr.substring(10, 12);
					fdate += " ";
					fdate += datestr.substring(12, 14);
					break;
				default:
					fdate = "";
				}
				break;
			case Code.DT_FMT2:
				switch (length) {
				case 8:
					fdate = datestr.substring(0, 4);
					fdate += "-";
					fdate += datestr.substring(4, 6);
					fdate += "-";
					fdate += datestr.substring(6, 8);
					break;
				case 12:
					fdate = datestr.substring(0, 4);
					fdate += "-";
					fdate += datestr.substring(4, 6);
					fdate += "-";
					fdate += datestr.substring(6, 8);
					fdate += " ";
					fdate += datestr.substring(8, 10);
					fdate += ":";
					fdate += datestr.substring(10, 12);
					break;
				case 14:
					fdate = datestr.substring(0, 4);
					fdate += "-";
					fdate += datestr.substring(4, 6);
					fdate += "-";
					fdate += datestr.substring(6, 8);
					fdate += " ";
					fdate += datestr.substring(8, 10);
					fdate += ":";
					fdate += datestr.substring(10, 12);
					fdate += ":";
					fdate += datestr.substring(12, 14);
					break;
				default:
					fdate = "";
				}
				break;
			case Code.DT_KOR:
				switch (length) {
				case 8:
					fdate = datestr.substring(0, 4);
					fdate += "년 ";
					fdate += datestr.substring(4, 6);
					fdate += "월 ";
					fdate += datestr.substring(6, 8);
					fdate += "일";
					break;
				case 12:
					fdate = datestr.substring(0, 4);
					fdate += "년 ";
					fdate += datestr.substring(4, 6);
					fdate += "월 ";
					fdate += datestr.substring(6, 8);
					fdate += "일 ";
					fdate += datestr.substring(8, 10);
					fdate += "시 ";
					fdate += datestr.substring(10, 12);
					fdate += "분";
					break;
				case 14:
					fdate = datestr.substring(0, 4);
					fdate += "년 ";
					fdate += datestr.substring(4, 6);
					fdate += "월 ";
					fdate += datestr.substring(6, 8);
					fdate += "일 ";
					fdate += datestr.substring(8, 10);
					fdate += "시 ";
					fdate += datestr.substring(10, 12);
					fdate += "분 ";
					fdate += datestr.substring(12, 14);
					fdate += "초";
					break;
				default:
					fdate = "";
				}
				break;
			default:
				fdate = "";
			}
			return fdate;
		}
	}
	
	/**
	 * 해당 문자열을 변형한다.
	 * 
	 * @param input
	 *            변형할 문자열 전체 dest 변형할 문자열
	 * @return retstr 리턴 데이터
	 */
	public static String repStr(String input, String target, String dest) {
		String s_Data = "";
		String s_Tmp = input;
		int i = s_Tmp.indexOf(target);

		while (i != -1) {
			s_Data = s_Data + s_Tmp.substring(0, i) + dest;
			s_Tmp = s_Tmp.substring(i + target.length());
			i = s_Tmp.indexOf(target);
		}
		s_Data = s_Data + s_Tmp;
		return s_Data;
	}
	
	/**
	 * 요일 정보를 반환한다
	 * 
	 * @param input
	 *            변형할 문자열 전체 dest 변형할 문자열
	 * @return retstr 리턴 데이터
	 */
	public static String getDayOfWeek(String datestr) {
	
		if (datestr == null || datestr.equals("")) {
			return "";
		}
		
		datestr = deleteChar(datestr);
		
		int yyyy = 0, MM = 1, dd = 1, day_of_week; // default
		
		String days[] = { "일", "월", "화", "수", "목", "금", "토" };
		
		try {
			yyyy = Integer.parseInt(datestr.substring(0, 4));
			MM = Integer.parseInt(datestr.substring(4, 6));
			dd = Integer.parseInt(datestr.substring(6, 8));
		} catch (Exception ex) {
			// do nothing
		}
		
		Calendar cal = Calendar.getInstance();
		cal.set(yyyy, MM - 1, dd);
		day_of_week = cal.get(Calendar.DAY_OF_WEEK); // 1(일),2(월),3(화),4(수),5(목),6(금),7(토)
		
		return days[day_of_week - 1];
	}

	/**
	 * 입력된 문자열에서 숫자만을 리턴한다.
	 * 
	 * @param input
	 *            숫자만 반환할 데이터
	 * @return retstr 리턴 데이터
	 */    
	public static String deleteChar(String input) {
		String strNumber = "0123456789";
		String retstr = "";
		
		if (input.length() > 0) {
			for (int i = 0; i < input.length(); i++) {
				if (strNumber.indexOf(input.charAt(i)) >= 0) {
					retstr = retstr + input.charAt(i);
				}
			}
		}
		return retstr;
	}
	
	/**
	 * 입력된 문자열을 핸드폰번호 형식으로 변환한다.
	 * 
	 * @param input
	 *     핸드폰번호 반환할 데이터
	 * @return retstr 리턴 데이터
	 */
	public static String getPhone(String input) {
		String retstr = "";
		
		if (input == null) {
			retstr = "";
		} else {
			if (input.length() == 8) {
				retstr = input.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
			} else if (input.length() == 12) {
				retstr = input.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
			}
			retstr = input.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
		}
		return retstr;
	}
	
	/**
	 * 입력된 문자열중 설정한 특수문자를 지운다
	 * 
	 * @param input
	 *     특수문자 제거한 문자
	 * @return retstr 리턴 데이터
	 */
	public static String removeSpecialChar(String input, String specChar) {
		String retstr = "";
		
		if (input == null) {
			retstr = "";
		} else {
			input = input.trim();
			if(!"".equals(input)) {
				retstr = "";
			}
			char lastChar = input.charAt(input.length() - 1);
			String lastStr = Character.toString(lastChar);
			if (specChar.equals(lastStr)) {
				input = input.substring(0, input.length() - 1);
			}
			retstr = input;
		}
		return retstr;
	}
	
	/**
	 * 입력된 문자열중 설정한 특수문자를 지운다
	 * 
	 * @param input
	 *     특수문자 제거한 문자
	 * @return retstr 리턴 데이터
	 */
	public static String repalcePatternChar(String input) {
		
		String retstr = "";
		
		if (input == null) {
			retstr = "";
		} else {
			input = input.trim();
			if("".equals(input)) {
				retstr = "";
			} else {
				Pattern pattern = Pattern.compile("[$](.*?)[$]"); 
				Matcher matcher = pattern.matcher(input);
				retstr = matcher.replaceAll("null");
			}
		}
		return retstr;
	}
	
	public static boolean isValidEmail(String email) {
		boolean err = false;
		String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(email);
		if (m.matches()) {
			err = true;
		}
		return err;
	}
	
	/**
	 * 타입별 랜덤숫자를 생성한다
	 * 
	 * @param input
	 *    랜덤 문자 생성 타입(숫자/문자+숫자/문자+숫자+특수)
	 * @return retstr 리턴 데이터
	 */
	public static String makeRandomString(int makeType) {
		String retStr = "";
		int randomLength = 0;
		
		char[]charTable =  { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
				'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
				'Y', 'Z','1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

		char[]charPwdTable =  { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
				'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
				'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
				'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 
				'w', 'x', 'y', 'z', '!', '@', '#', '$', '%', '^', '&', '*',
				'(', ')', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
		
		Random random = new Random(System.currentTimeMillis());
		
		if(makeType == Code.RAND_TYPE_A) { //숫자
			randomLength = 6;
			
			int range = (int)Math.pow(10,randomLength);
			int trim = (int)Math.pow(10, randomLength-1);
			int result = random.nextInt(range)+trim;

			if(result>range){
				result = result - trim;
			}
			
			retStr =  String.valueOf(result);
		} else if (makeType == Code.RAND_TYPE_B) { //문자+숫자
			randomLength = 8;
			
			int tableLength  = charTable.length;
			StringBuffer buf = new StringBuffer();
			
			for (int i = 0; i < randomLength ; i ++) {
				buf.append(charTable[random.nextInt(tableLength)]);
			}
			
			retStr = buf.toString(); 
		} else { //문자+숫자+특수문자 
			randomLength = 8;
			
			int tableLength  = charPwdTable.length;
			StringBuffer buf = new StringBuffer();
			
			for (int i =0 ; i < randomLength ; i++) {
				buf.append(charPwdTable[random.nextInt(tableLength)]);
			}
			
			retStr = buf.toString();
		}
		return  retStr;
	}
	
	/**
	 * "_"뒤의 단어 찾아서 삭제 하고 제공된 문자 합친다
	 * 
	 * @param input
	 *    원본문자열, 추가할 문자열
	 * @return reslast 리턴 데이터
	 */
	public static String findAndMakeString(String origin, String appendStr) {
		String reslast = ""; 
		if(origin.contains(appendStr)) {
			int pos = origin.indexOf("_");
			String res = origin.substring(0, pos);
			reslast = res + appendStr;
		}else {
			reslast = origin + appendStr;
		}
		return reslast;
	}
	
	/**
	 * 문자열의 마지막 "," 제고 
	 * 
	 * @param input
	 *    원본문자열
	 * @return retstr 리턴 데이터
	 */
	public static String removeComma(String origin) {
		String reslast = origin; 
		if (origin.endsWith(",")) {
			reslast = origin.substring(0, origin.length() - 1);  
		}
		return reslast;
	}
}
