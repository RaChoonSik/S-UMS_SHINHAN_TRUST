<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.09.22
	*	설명 : 월별통계 목록 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">목록</h3>
		<span class="total">Total: <em><c:out value="${fn:length(monthDataList)}"/></em></span>
		<div class="btn-wrap">
			<button type="button" class="btn fullpurple" onclick="goExcel();">엑셀다운로드</button>
		</div>
	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:20%;">
				<col style="width:20%;">
				<col style="width:20%;">
				<col style="width:20%;">
				<col style="width:20%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">일자</th>
					<th scope="col">발송건수</th>
					<th scope="col">성공수(%)</th>
					<th scope="col">실패수(%)</th>
					<th scope="col">오픈건수(%)</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(monthDataList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${monthDataList}" var="monthData" varStatus="monthStatus">
						<tr>
							<td><c:out value="${monthData.displayDays}"/></td>
							<td><c:out value="${monthData.send}"/></td>
							<td><c:out value="${monthData.displaySuccess}"/></td>
							<td><c:out value="${monthData.displayFailed}"/></td>
							<td><c:out value="${monthData.displayOpened}"/></td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty monthDataList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="6" class="no_data">등록된 내용이 없습니다.</td>
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