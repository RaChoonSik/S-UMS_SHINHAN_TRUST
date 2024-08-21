<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.09.24
	*	설명 : 자동발송메일 상세정보 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %> 

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">발송 정보</h3>
	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:25%;">
				<col style="width:25%;">
				<col style="width:25%;">
				<col style="width:25%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">발송차수</th>
					<th scope="col">총발송량</th>
					<th scope="col">성공</th>
					<th scope="col">IP-checking</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(mailSendResultList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${mailSendResultList}" var="mailSendResult">
						<tr>
							<c:if test="${mailSendResult.subid eq 0}">
							<td>본발송</td>
							</c:if>
							<c:if test="${mailSendResult.subid ne 0}">
							<td><c:out value="${mailSendResult.subid}"/>차</td>
							</c:if>							
							<td><c:out value="${mailSendResult.send}"/></td> 
							<td><c:out value="${mailSendResult.success}"/></td>
							<td><c:out value="${mailSendResult.ipChecking}"/></td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty mailSendResultList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="4" class="no_data">발송 정보가 없습니다</td>
					</tr>
					<!-- //데이터가 없을 경우 -->
				</c:if>
			</tbody>
		</table>
	</div>
</div>
<!-- //목록 --> 
