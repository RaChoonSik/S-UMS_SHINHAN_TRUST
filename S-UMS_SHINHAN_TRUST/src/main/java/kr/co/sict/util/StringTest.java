package kr.co.sict.util;

import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONObject; 

public class StringTest {
	public static void main(String[] args) {
		//JSON 테스트 
		//jaonTest();
		//암복호화테스트시 
		encTest();
		//DB암복호화테스트시 
		//encDbTest
		//날짜  테스트 
		/*
		isValidDateString("202212271401001");
		isValidDateString("202212271401");
		isValidDateString("2022122714");
		isValidDateString("20221227");
		isValidDateString("202212");
		isValidDateString("2022");
		
		isValidDateString("20221327140100");
		isValidDateString("20221237140100");
		isValidDateString("20221227340100");
		isValidDateString("20221227149900");
		isValidDateString("20221227140188");
		*/
		
	}
	
	private static void jaonTest() {
		String target ="{ \"phoneList\": [{\"phone\": \"YF4mTvzHfXK7LTiS4RGpWg==\", \"id\":\"A\", \"name\": \"\"}, {\"phone\": \"fdQxlqMyFjXvVM4+fgmPRQ==\",\"id\":\"A\", \"name\": \"\"}] }";
		
		JSONObject obj = new JSONObject(target);
		JSONObject obj2 = obj.getJSONObject("phoneList");
		
		 

	}
	
	private static void encTest() {
		/*
		String ems =EncryptUtil.getJasyptDecryptedString("PBEWithMD5AndDES", "andi@enders.co.kr", "AOE7zpjB9kx/rhakVNUYqlhrT5Z1Hbuy1M0sSPsFtGmuGNPAcMkpzsqRH372EOYG");	
		String rns =EncryptUtil.getJasyptDecryptedString("PBEWithMD5AndDES", "andi@enders.co.kr", "WIfNEUs6eMIcq4coe1WwjayGY+rNjtkJbJbuUdHiIsGwi/EHVc7D/Pp3Hdk9o/hq");
		String sms =EncryptUtil.getJasyptDecryptedString("PBEWithMD5AndDES", "andi@enders.co.kr", "CqygLkqZ0XZ92cRWN62ZxSZHV2+x01OshqNHXLsJIjaU40w4TwvPksgK8LSJX6TG");
		String push =EncryptUtil.getJasyptDecryptedString("PBEWithMD5AndDES", "andi@enders.co.kr", "E0ep/chY5VYE59tPp90U/JjnbQQGOMzXLzWBEr6E8CoCJjUtll6j+bvUiyHPXFKs");
		
		System.out.println(ems);
		System.out.println(rns);
		System.out.println(sms);
		System.out.println(push);
		
		System.out.println(EncryptUtil.getJasyptEncryptedString("PBEWithMD5AndDES", "andi@enders.co.kr", ems));
		System.out.println(EncryptUtil.getJasyptEncryptedString("PBEWithMD5AndDES", "andi@enders.co.kr", rns));
		System.out.println(EncryptUtil.getJasyptEncryptedString("PBEWithMD5AndDES", "andi@enders.co.kr", sms));
		System.out.println(EncryptUtil.getJasyptEncryptedString("PBEWithMD5AndDES", "andi@enders.co.kr", push));
		*/
		
		String encStr =EncryptUtil.getJasyptEncryptedSHA512String("PBEWithHmacSHA512AndAES_256", "!SICT#UMS", "kim.jh@sictglobal.com");
		//System.out.println(encStr);
		
		String decStr =EncryptUtil.getJasyptDecryptedSHA512String("PBEWithHmacSHA512AndAES_256","!SICT#UMS", encStr);
		//System.out.println(decStr);
		
		/*
		happyjune =EncryptUtil.getJasyptEncryptedString("PBEWithMD5AndDES", "ENDERSUMS", "happyjune@enders.co.kr");
		System.out.println(happyjune);
		
		happyjuneD =EncryptUtil.getJasyptDecryptedString("PBEWithMD5AndDES", "ENDERSUMS", happyjune);
		System.out.println(happyjuneD);
		*/
	}
	
	private static void encDbTest() {
		/*
		String dbUrl = "jdbc:mysql://127.0.0.1:3306/ums?characterEncoding=utf8";
		String schema = ""; if(dbUrl.indexOf("?") > 0) { dbUrl =
		dbUrl.substring(0,dbUrl.indexOf("?")); } schema =
		dbUrl.split("/")[dbUrl.split("/").length-1];
		
		System.out.println(schema);
		*/
	}
	
	public static boolean isValidDateString(String str) {
		boolean ret = false;
		if(str != null && !"".equals(str)) {
			try {
				SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyyMMddHHmmss"); //검증할 날짜 포맷 설정
				dateFormatParser.setLenient(false); //false일경우 처리시 입력한 값이 잘못된 형식일 시 오류가 발생
				dateFormatParser.parse(str); //대상 값 포맷에 적용되는지 확인
				System.out.println("OK : " +  str);
				ret = true;
			} catch (Exception e) {
				ret = false;
				System.out.println("FAIL : " +  str);
			}
		} 
		return ret;
	}
	
	
}
