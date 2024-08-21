package com.custinfo.safedata;

import kr.co.sict.util.EncryptUtil;

public class CustInfoSafeData {
	
	/**
	 * 암호화
	 * @param data
	 * @param type (RNNO/NOT_RNNO)
	 * @return
	 * @throws Exception
	 */
	public synchronized String getEncrypt(String data, String type) throws Exception {
		return EncryptUtil.getJasyptEncryptedString("PBEWithMD5AndDES", type, data);
	}
	
	/**
	 * 복호화
	 * @param data
	 * @param type (RNNO/NOT_RNNO)
	 * @return
	 * @throws Exception
	 */
	public synchronized String getDecrypt(String data, String type) throws Exception {
		return EncryptUtil.getJasyptDecryptedString("PBEWithMD5AndDES", type, data);
	}
}
