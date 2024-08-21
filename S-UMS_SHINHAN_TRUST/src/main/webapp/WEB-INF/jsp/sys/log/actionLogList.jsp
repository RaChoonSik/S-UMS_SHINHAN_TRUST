<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.26
	*	설명 : 사용자 액션 로그 목록 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">목록</h3>
		
		<span class="total">Total: <em><c:out value="${pageUtil.totalRow}"/></em></span>

		<!-- 버튼// -->
		<div class="btn-wrap">
			<button type="button" class="btn fullred" onclick="goExcel();">엑셀다운로드</button>
		</div>
		<!-- //버튼 -->
	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:12%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:auto;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">로그일시</th>
					<th scope="col">세션ID</th>
					<th scope="col">그룹명</th>
					<th scope="col">사용자ID</th>
					<th scope="col">사용자명</th>
					<th scope="col">상태</th>
					<th scope="col">로그 종별</th>
					<th scope="col">컨텐츠</th>
					<th scope="col">메시지</th>
				</tr>
			</thead>
			<tbody>
				<!-- 데이터가 있을 경우// -->
				<c:if test="${fn:length(actionLogList) > 0}">
					<c:forEach items="${actionLogList}" var="actionLog" varStatus="actionLogStatus">
						<tr>
							<td><c:out value="${actionLog.logDt}"/></td>
							<td><c:out value="${actionLog.sessionId}"/></td>
							<td><c:out value="${actionLog.deptNm}"/></td>
							<td><c:out value="${actionLog.userId}"/></td>
							<td><c:out value="${actionLog.userNm}"/></td>
							<td><c:out value="${actionLog.statusGb}"/></td>
							<td><c:out value="${actionLog.contentTypeNm}"/></td>
							<td><c:out value="${actionLog.content}"/></td> 
							<td><c:out value="${actionLog.message}"/></td>
						</tr>
					</c:forEach>
				</c:if>
				<!-- //데이터가 있을 경우 -->

				<!-- 데이터가 없을 경우// -->
				<c:if test="${empty actionLogList}">
					<tr>
						<td colspan="9" class="no_data">등록된 내용이 없습니다.</td>
					</tr>
				</c:if>
				<!-- //데이터가 없을 경우 -->
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
