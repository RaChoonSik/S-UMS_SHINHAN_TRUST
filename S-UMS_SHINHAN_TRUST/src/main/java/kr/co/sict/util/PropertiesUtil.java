/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 :설 정 정보 조회를 위한 Utility이다.
 * /WEB-INF/config/properties/ums.properties
 */ 
package kr.co.sict.util;

import java.util.Locale;
import org.springframework.context.support.MessageSourceAccessor;

public class PropertiesUtil {
	
	private MessageSourceAccessor msa;
	
	public void setPropertiesSourceAccessor(MessageSourceAccessor msa){
		this.msa = msa;
	}
	
	public String getProperty(String key){
		return msa.getMessage(key).trim();
	}
	
	public String getProperty(String key, Object[] objs){
		return msa.getMessage(key, objs, Locale.getDefault()).trim();
	}
}
