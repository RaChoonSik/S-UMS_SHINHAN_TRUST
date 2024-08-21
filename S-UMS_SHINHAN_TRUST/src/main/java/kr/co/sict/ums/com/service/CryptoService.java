package kr.co.sict.ums.com.service;

import org.springframework.stereotype.Service;

@Service
public interface CryptoService {
	/**
	 * 컬럼 데이터에 대한 암호화 처리
	 * @param colNm
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String getEncrypt(String colNm, String data);

	/**
	 * 컬럼 데이터에 대한 복호화 처리
	 * @param colNm
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String getDecrypt(String colNm, String data);
}
