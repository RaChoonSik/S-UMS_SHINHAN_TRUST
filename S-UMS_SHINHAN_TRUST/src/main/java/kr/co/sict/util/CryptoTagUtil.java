/**
 * 작성자 : 김상진
 * 작성일시 : 2021.09.11
 * 설명 : 암호화된 문자열을 복호화 후 출력하는 Tag
 */
package kr.co.sict.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.custinfo.safedata.CustInfoSafeData;

public class CryptoTagUtil extends SimpleTagSupport {
	private Logger logger = Logger.getLogger(this.getClass());
	
	private String colNm;	// 컬럼명
	private String data;	// 데이터
	
	public String getColNm() {
		return colNm;
	}
	public void setColNm(String colNm) {
		this.colNm = colNm;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

	@Override
	public void doTag() throws JspException, IOException {
		//CustInfoSafeData  custInfoSafeData = new CustInfoSafeData();
		PageContext context = (PageContext)getJspContext();
		HttpServletRequest request = (HttpServletRequest)context.getRequest();

		// 암호화 문자열 복호화 처리
		String result = "";
		try {
			// 설정파일 읽기
			String realPath = request.getServletContext().getRealPath("/");
			Properties prop = new Properties();
			prop.load(new FileInputStream(realPath + "/WEB-INF/config/properties/ums.crypto.properties"));
			
			Properties propEnc = new Properties();
			propEnc.load(new FileInputStream(realPath + "/WEB-INF/config/properties/ums.properties"));
			
			String SAFEDATA_ENC_YN = prop.getProperty("SAFEDATA_ENC_YN").trim();
			int SAFEDATA_ENC_CNT = Integer.parseInt(prop.getProperty("SAFEDATA_ENC_CNT").trim());
			
			// 암호화 사용여부 체크
			if("YES".equals(SAFEDATA_ENC_YN)) {
				Loop1:
				for(int i=1;i<=SAFEDATA_ENC_CNT;i++) {
					String ENC_COLUM = prop.getProperty("ENC_COLUM" + i).trim();
					String[] cols = ENC_COLUM.split("\\;");
					for(int j=0;j<cols.length;j++) {
						// 컬럼 포함된 경우 복호화
						if(cols[j].equals(colNm)) {
							//result = custInfoSafeData.getDecrypt(data, prop.getProperty("ENC_KEY" + i).trim());
							result = EncryptUtil.getJasyptDecryptedString(propEnc.getProperty("JASYPT.ALGORITHM"),	propEnc.getProperty("JASYPT.KEYSTRING"), data);
							break Loop1;
						} else {
							result = data;
						}
					}
				}
			} else {
				result = data;
			}
		} catch(Exception e) {
			logger.error("CryptoTagUtil Error = " + e);
			result = "err:" + data;
		}
		
		// 복호화 문자열 출력
		JspWriter out = context.getOut();
		out.print(result);
	}

}
