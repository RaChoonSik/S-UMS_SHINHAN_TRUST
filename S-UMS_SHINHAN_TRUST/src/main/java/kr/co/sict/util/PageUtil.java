/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.20
 * 설명 : 페이징 처리 클래스
 */
package kr.co.sict.util;

import javax.servlet.http.HttpServletRequest;

public class PageUtil {
	private String pageUrl = "";

	private int pageRow = 10;           // 페이지당 행수
	private int pageNumsPerScreen = 10; // 한번에 표현될 페이지수
	private int totalRow = 0;           // 전체 행수
	private int totalPage = 0;          // 전체 페이지수
	private int currPage = 1;           // 현재 페이지 번호
	private int startPage = 1;          // 시작 페이지 번호
	private int endPage = 1;            // 종료 페이지 번호
	private int startRow = 1;           // 시작 행번호
	private int endRow = 1;             // 종료 행번호
	private int prevPage = 0;           // 이전페이지 번호
	private int nextPage = 0;           // 다음페이지 번호
	private String pageHtml = "";       // Page별 기능을 처리할 Html
	
	private String header = "";
	private String footer = "";
	private String frmName = "";
	private String submitFunc = "goPageNum";
	private String firstMark = "처음"; //"<img src='../../images/common/btn_prev_02.gif' border='0' align='absmiddle' class='m01'>";
	private String preMark   = "<button type='button' class='prev' disabled onclick>이전</button>"; //"<img src='../../images/common/btn_prev_01.gif' border='0' align='absmiddle' class='m02'>";
	private String nextMark  = "<button type='button' class='next' disabled onclick>다음</button>"; //"<img src='../../images/common/btn_next_01.gif' border='0' align='absmiddle' class='m03'>";
	private String lastMark  = "마지막"; //"<img src='../../images/common/btn_next_02.gif' border='0' align='absmiddle' class='m04'>";
	private String pageSeperate = " "; //"<span class='pageBar'>|</span>"; // 페이지 번호 사이의 구분자

	/**
	 * 페이징에 필요한 변수들의 값을 설정한다.
	 * @param 
	 * @return 
	 */
	private void procPageValues(){
		//	전체 페이지수 계산
		totalPage = (totalRow + (pageRow - 1)) / pageRow;

		// 시작 페이지와 종료 페이지 계산
		startPage = (((currPage + (pageNumsPerScreen - 1)) / pageNumsPerScreen) * pageNumsPerScreen) - (pageNumsPerScreen - 1);
		endPage = ((currPage + (pageNumsPerScreen - 1)) / pageNumsPerScreen) * pageNumsPerScreen;

		if (endPage > totalPage) {
			endPage = totalPage;
		}

		//Start Row 설정
		if (totalRow == 0) {
			startRow = 0;
		} else {
			startRow = (currPage * pageRow) - (pageRow - 1);
		}

		// End Row 설정
		if (totalRow == 0) {
			startRow = 0;
		} else {
			endRow = currPage * pageRow;
		}
	}

	/**
	 * 한 페이지 글만 존재하는 경우의 페이지 네비게이션을 반환한다.
	 * @param 
	 * @return String
	 */
	private String getOnePageHtml(){
		return preMark.replaceAll("onclick", "") + " <button type='button' class='active'>1</button> " + nextMark.replaceAll("onclick", "");
	}

	/**
	 * Form 의 이름을 반환한다.
	 * @param 
	 * @return String
	 */
	public String getFrmName() {
		return frmName;
	}

	/**
	 * Form 의 이름을 설정한다.
	 * @param 
	 * @return String
	 */
	public void setFrmName(String frmName) {
		this.frmName = frmName;
	}

	/**
	 * 페이지 클릭시 동작을 설정한다.
	 * @param 
	 * @return String
	 */
	private String getLinkedStr(String str, int linkNum){
		if(currPage == linkNum){
			return "<button type='button' class='active'>" + str + "</button>";
		}
		if(firstMark.equals(str) || preMark.equals(str) || nextMark.equals(str) || lastMark.equals(str)) {
			str = str.replaceAll("onclick", "onclick='" + submitFunc + "(" + linkNum + ");'").replaceAll("disabled", "");
		} else {
			str = "<button type='button' onclick='"+ submitFunc +"("+ linkNum +");'>" + str +"</button>";
		}

		return str;
	}

	/**
	 * 페이징 Form 내용을 전송한다.
	 * @param 
	 * @return String
	 */
	private String getSubmitScript(){
		StringBuffer script = new StringBuffer("\n<script>\n");
		script.append("function goSubmit(cPage){ \n\t");
		script.append(frmName);
		script.append(".action='");
		script.append(pageUrl);
		script.append("?cPage='+cPage; \n\t");
		script.append(frmName);
		script.append(".submit(); \n");
		script.append("}\n");
		script.append("</script>");
		return script.toString();
	}

	/**
	 * 페이지 네비게이션을 반환한다.
	 * @param 
	 * @return String
	 */
	public String getPageHtml() {

		if (totalRow == 0) {
			return this.getOnePageHtml();
		}

		// 이전 페이지 추가
		if (startPage < pageNumsPerScreen) {
			pageHtml += preMark.replaceAll(" onclick", "") + " ";
		} else {
			prevPage = startPage - 1;
			pageHtml += getLinkedStr(preMark, prevPage) + " ";
		}

		// 페이지 Number 추가
		for (int i = startPage; i <= endPage; i++) {
			// 현재 페이지 인지 확인
			if (i == currPage) {
				pageHtml += "<button type='button' class='active'>"+ i +"</button>";
			} else {
				pageHtml += getLinkedStr(i + "" , i) + " ";
			}
			if(i<endPage) {
				pageHtml += pageSeperate + " ";
			} else {
				pageHtml += " ";
			}
		}

		// 다음 페이지 추가
		if (endPage >= totalPage) {
			pageHtml += nextMark.replaceAll(" onclick", "") + " ";
		} else {
			nextPage = endPage + 1;
			pageHtml += getLinkedStr(nextMark, nextPage) + " ";
		}

		if(submitFunc.equals("goSubmit")){
			pageHtml += this.getSubmitScript();
		}

		return pageHtml;

	}

	/**
	 * 링크 걸린 페이지 번호들을 return한
	 *
	 * @param int
	 *            iCurrPage 현재 페이지 index정보
	 * @param int
	 *            iTotRow 총 데이터 건수
	 * @return void
	 */
	public void init(HttpServletRequest request, int iCurrPage, int iTotalRow, int pageRow) {

		this.pageUrl = request.getRequestURI();

		this.currPage = (iCurrPage==0)? 1 : iCurrPage ;
		this.totalRow = iTotalRow;
		this.pageRow = pageRow;

		this.procPageValues();
	}

	public int getStartNum(){
		return this.totalRow - (this.pageRow * (this.currPage-1));
	}

	public int getCurrPage() {
		return currPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public int getEndRow() {
		return endRow;
	}

	public String getFirstMark() {
		return firstMark;
	}

	public String getFooter() {
		return footer;
	}

	public String getHeader() {
		return header;
	}

	public String getLastMark() {
		return lastMark;
	}

	public String getNextMark() {
		return nextMark;
	}

	public int getNextPage() {
		return nextPage;
	}

	public int getPagePerScreen() {
		return pageNumsPerScreen;
	}

	public int getPageRow() {
		return pageRow;
	}
	
	public void setPageRow(int pageRow) {
		this.pageRow = pageRow;
	}

	public String getPageSeperate() {
		return pageSeperate;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public String getPreMark() {
		return preMark;
	}

	public int getPrevPage() {
		return prevPage;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getStartRow() {
		return startRow;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public int getTotalRow() {
		return totalRow;
	}

	public String getSubmitFunc() {
		return submitFunc;
	}

	public void setSubmitFunc(String submitFunc) {
		this.submitFunc = submitFunc;
	}
}

