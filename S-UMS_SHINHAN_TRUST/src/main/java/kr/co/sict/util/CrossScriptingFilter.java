/**
 * 작성자 : 김준희
 * 작성일시 : 2021.12.05
 * 설명 : 크로스스크립팅 필터 
 */
package kr.co.sict.util;

import java.io.FileInputStream;
import java.util.Properties;
 
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class CrossScriptingFilter  {
	private static Logger logger = Logger.getLogger(CrossScriptingFilter.class);
 
	
	public static boolean existScript(HttpServletRequest request , String editText) {
		
		boolean retVal = false;
		
		editText= editText.toLowerCase();
		logger.debug("CrossScriptingFilter existScript text = [" + editText + "]");
		
		try {
			// 설정파일 읽기
			String realPath = request.getServletContext().getRealPath("/");
			Properties prop = new Properties();
			prop.load(new FileInputStream(realPath + "/WEB-INF/config/properties/ums.xss.properties"));
			
			String REGEXP_COLUM = prop.getProperty("XSS_REGEXP").trim();
			String[] regexpCols = REGEXP_COLUM.split("\\;");
			
			Loop1:for(int j=0;j<regexpCols.length;j++) {
					String regex =  regexpCols[j];
					if(editText.matches(regex)) {
						retVal = true;
						logger.debug("CrossScriptingFilter existScript matches = [" + regexpCols[j] + "]");
						break Loop1;
					}
				}
			
			String FILTER_COLUM = prop.getProperty("XSS_FILTER").trim();
			String[] filterCols = FILTER_COLUM.split("\\;");
			
			Loop2:for(int j=0;j<filterCols.length;j++) {
				if(editText.contains(filterCols[j])) {
					retVal = true;
					//editText = editText.replaceAll(filterCols[j], "x-"+ filterCols[j]);
					logger.debug("CrossScriptingFilter existScript matches = [" + filterCols[j] + "]");
					break Loop2;
				}
			}
			return retVal;
		} catch(Exception e) {
			logger.error("CrossScriptingFilter existScript error = " + e);
			return true;
		}
	}
}
