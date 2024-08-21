/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 공통처리 VO
 */
package kr.co.sict.ums.com.vo;

public class CommonVO {
	private String procMode;		// 처리모드
	private int totalCount;			// 전체건수
	private int	pageNum;			// 페이지번호
	private int rowPerPage;			// 페이지당행수
	private int	page;				// 페이지번호
	private int rows;				// 페이지당행수
	private int seq;				// 순번
	private int startRow;			// MySQL limit 시작행
	
	public String getProcMode() {
		return procMode;
	}
	public void setProcMode(String procMode) {
		this.procMode = procMode;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getRowPerPage() {
		return rowPerPage;
	}
	public void setRowPerPage(int rowPerPage) {
		this.rowPerPage = rowPerPage;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
}
