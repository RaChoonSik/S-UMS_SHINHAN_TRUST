/**
 * 작성자 : 이혜민
 * 작성일시 : 2022.10.17
 * 설명 : 문자열 마스킹 처리
 */
package kr.co.sict.util;

public class MaskingUtil {

	/**
	 * 마스킹 패턴 
	 * 
	 * @param colNm
	 * @return
	 */
	public static String getMasking(String colNm, String colData, String[] maskCol) {
		String result = "";

		int len = 0;
		for (int j = 0; j < maskCol.length; j++) {
			// 컬럼 포함된 경우 마스킹
			if (maskCol[j].equals(colNm)) {

				// 고객 아이디
				if ("CUST_ID".equals(colNm) || "ID".equals(colNm)) {

					result = colData.replaceAll("(?<=.{2}).", "*");
					break;
				// 고객 이름
				} else if ("CUST_NM".equals(colNm) || "NAME".equals(colNm)) {
					String frsNm = "";
					//String midNm = "";
					String maskNm = "*"; 
					String lastNm = "";
					if(colData.length() > 0) {
						frsNm = colData.substring(0,1);
						if( colData.length() > 2) {
							lastNm = colData.substring(2, colData.length());
						}
						result = frsNm + maskNm + lastNm;
						break;
					} else {
						result = colData;
						break;
					}
				// 고객 이메일
				} else if ("CUST_EM".equals(colNm) || "EMAIL".equals(colNm)) {
					//이메일 형식 검사
					boolean err = StringUtil.isValidEmail(colData);

					if(err) {
						
					String[] custEm = colData.split("@");
					len = custEm[0].length() - 3;
					result = custEm[0].substring(0, len) + "***@" + custEm[1];
					break;
					} else { 
						result = colData;
					break;
					}
				//고객 전화번호
				} else if ("PHONE".equals(colNm)) {
					String[] phoneArr = colData.split("-");
					result = phoneArr[0] + "-" + phoneArr[1].substring(0, 2) + "**-" + phoneArr[2].substring(0, 2) + "**";
					break;
				} else {
					result = colData;
					break;
				}
			} else {
				result = colData;
			}
		}
		return result;
	}
}