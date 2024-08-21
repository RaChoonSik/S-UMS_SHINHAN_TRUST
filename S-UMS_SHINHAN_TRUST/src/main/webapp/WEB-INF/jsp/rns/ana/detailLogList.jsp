<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.09.22
	*	설명 : 상세로그 목록 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">목록</h3>
		<span class="total">Total: <em><c:out value="${pageUtil.totalRow}"/></em></span>
		<div class="btn-wrap">
			<button type="button" class="btn fullpurple" onclick="goExcel();">엑셀다운로드</button>
		</div>
	</div>

	<div class="grid-area pb20">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:5%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:auto;">
				<col style="width:9%;">
				<col style="width:9%;">
				<col style="width:9%;">
				<col style="width:9%;">
				<col style="width:6%;">
				<col style="width:6%;">
				<col style="display:none;">
				<col style="display:none;">						
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">발송일시</th>
					<th scope="col">서비스명</th>
					<th scope="col">메일명</th>
					<th scope="col">수신자명</th>
					<th scope="col">수신자이메일</th>
					<th scope="col">발신자명</th>
					<th scope="col">발신자이메일</th>
					<th scope="col">재발송여부</th>
					<th scope="col">발송결과</th>
					<th scope="col" style="display:none;">등록일시</th>
					<th scope="col" style="display:none;">발송템플릿</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(detailLogDataList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${detailLogDataList}" var="detailLogData" varStatus="detailLogDataStatus">
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - detailLogDataStatus.index}"/></td>
							<td><c:out value="${detailLogData.sdate}"/></td>
							<td><c:out value="${detailLogData.tnm}"/></td>
							<td><a href="javascript:;" onclick="goMailDetil('<c:out value="${detailLogData.mid}"/>', '<c:out value="${detailLogData.deptNm}"/>', '<c:out value="${detailLogData.attchCnt}"/>', this);" class="bold"><c:out value="${detailLogData.subject}"/></a></td>
							<td><c:out value="${detailLogData.rname}"/></td>
							<%-- <td><c:out value="${detailLogData.rmail}"/></td> --%>
							<td><crypto:decrypt colNm='RMAIL' data='${detailLogData.rmail}'/></td>
							<td><c:out value="${detailLogData.sname}"/></td>
							<%-- <td><c:out value="${detailLogData.smail}"/></td> --%>
							<td><crypto:decrypt colNm='SMAIL' data='${detailLogData.smail}'/></td>
							<c:if test="${detailLogData.refmid eq 0}">
							<td>N</td>
							</c:if>
							<c:if test="${detailLogData.refmid ne 0}">
							<td>Y</td>
							</c:if>
							<td><c:out value="${detailLogData.rcode}"/></td>
							
							<td style="display:none;"><c:out value="${detailLogData.cdate}"/></td>
							<td style="display:none;"><c:out value="${detailLogData.contents}"/></td>
							
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty detailLogDataList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="8" class="no_data">등록된 내용이 없습니다.</td>
					</tr>
					<!-- //데이터가 없을 경우 -->
				</c:if>
			</tbody>
		</table>
	</div>
</div>
<!-- //목록 -->

<!-- 페이징// -->
<div class="paging">
	${pageUtil.pageHtml}
</div>
<!-- //페이징 -->