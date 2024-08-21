/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.16
 * 설명 : 메타 조인 VO
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : sys.vo -->sys.dbc.vo 
 */
package kr.co.sict.ums.sys.dbc.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class MetaJoinVO extends CommonVO {
	private int joinNo;			// 메타조인 일련번호
	private int dbConnNo;		// DB등록번호
	private String mstTblNm;	// 마스터 테이블명
	private String mstColNm;	// 마스터 컬럼명
	private String forTblNm;	// 포린 테이블명
	private String forColNm;	// 포린 컬럼명
	private String joinTy;		// 조인유형
	private String relTy;		// 관계유형
	
	// 추가정보
	private String uilang;		// 언어권
	private String tblNm;		// 테이블명
	private int tblNo;		// 테이블번호
	
	public int getJoinNo() {
		return joinNo;
	}
	public void setJoinNo(int joinNo) {
		this.joinNo = joinNo;
	}
	public int getDbConnNo() {
		return dbConnNo;
	}
	public void setDbConnNo(int dbConnNo) {
		this.dbConnNo = dbConnNo;
	}
	public String getMstTblNm() {
		return mstTblNm;
	}
	public void setMstTblNm(String mstTblNm) {
		this.mstTblNm = mstTblNm;
	}
	public String getMstColNm() {
		return mstColNm;
	}
	public void setMstColNm(String mstColNm) {
		this.mstColNm = mstColNm;
	}
	public String getForTblNm() {
		return forTblNm;
	}
	public void setForTblNm(String forTblNm) {
		this.forTblNm = forTblNm;
	}
	public String getForColNm() {
		return forColNm;
	}
	public void setForColNm(String forColNm) {
		this.forColNm = forColNm;
	}
	public String getJoinTy() {
		return joinTy;
	}
	public void setJoinTy(String joinTy) {
		this.joinTy = joinTy;
	}
	public String getRelTy() {
		return relTy;
	}
	public void setRelTy(String relTy) {
		this.relTy = relTy;
	}
	public String getUilang() {
		return uilang;
	}
	public void setUilang(String uilang) {
		this.uilang = uilang;
	}
	public String getTblNm() {
		return tblNm;
	}
	public void setTblNm(String tblNm) {
		this.tblNm = tblNm;
	}
	public int getTblNo() {
		return tblNo;
	}
	public void setTblNo(int tblNo) {
		this.tblNo = tblNo;
	} 
	
}
