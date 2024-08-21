package kr.co.sict.ums.com.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.custinfo.safedata.CustInfoSafeData;

import kr.co.sict.util.EncryptUtil;
import kr.co.sict.util.PropertiesUtil;

@Service
public class CryptoServiceImpl implements CryptoService {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private PropertiesUtil properties;
	
	@Override
	public String getEncrypt(String colNm, String data) {
		//CustInfoSafeData  custInfoSafeData = new CustInfoSafeData();
		try {
			String result = "";
			String SAFEDATA_ENC_YN = properties.getProperty("SAFEDATA_ENC_YN");
			int SAFEDATA_ENC_CNT = Integer.parseInt(properties.getProperty("SAFEDATA_ENC_CNT"));
			
			// 사용여부 체크
			if("YES".equals(SAFEDATA_ENC_YN)) {
				Loop1:
				for(int i=1;i<=SAFEDATA_ENC_CNT;i++) {
					String ENC_COLUM = properties.getProperty("ENC_COLUM" + i);
					String[] cols = ENC_COLUM.split("\\;");
					for(int j=0;j<cols.length;j++) {
						// 컬럼 포함된 경우 암호화
						if(cols[j].equals(colNm)) {
							//result = custInfoSafeData.getEncrypt(data, properties.getProperty("ENC_KEY" + i));
							System.out.println("--------CryptoServiceImpl.getEncrypt----------");
							System.out.println(properties.getProperty("JASYPT.ALGORITHM"));
							System.out.println(properties.getProperty("JASYPT.KEYSTRING"));
							
							result = EncryptUtil.getJasyptEncryptedString(properties.getProperty("JASYPT.ALGORITHM"),	properties.getProperty("JASYPT.KEYSTRING"), data);
							break Loop1;
						} else {
							result = data;
						}
					}
				}
			} else {
				result = data;
			}
			
			return result;
		} catch(Exception e) {
			logger.error("getEncrypt error = " + e);
			return data;
		}
	}

	@Override
	public String getDecrypt(String colNm, String data) {
		//CustInfoSafeData  custInfoSafeData = new CustInfoSafeData();
		try {
			String result = "";
			String SAFEDATA_ENC_YN = properties.getProperty("SAFEDATA_ENC_YN");
			int SAFEDATA_ENC_CNT = Integer.parseInt(properties.getProperty("SAFEDATA_ENC_CNT"));
			
			// 사용여부 체크
			if("YES".equals(SAFEDATA_ENC_YN)) {
				Loop1:
				for(int i=1;i<=SAFEDATA_ENC_CNT;i++) {
					String ENC_COLUM = properties.getProperty("ENC_COLUM" + i);
					String[] cols = ENC_COLUM.split("\\;");
					for(int j=0;j<cols.length;j++) {
						// 컬럼 포함된 경우 복호화
						if(cols[j].equals(colNm)) {
							//result = custInfoSafeData.getDecrypt(data, properties.getProperty("ENC_KEY" + i));
							result = EncryptUtil.getJasyptDecryptedString(properties.getProperty("JASYPT.ALGORITHM"),	properties.getProperty("JASYPT.KEYSTRING"), data);
							break Loop1;
						} else {
							result = data;
						}
					}
				}
			} else {
				result = data;
			}
			
			return result;
		} catch(Exception e) {
			logger.error("getDecrypt error = " + e);
			return "err:" + data;
		}
	}

}
