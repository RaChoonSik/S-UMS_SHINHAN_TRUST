<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.11.16
	*	설명 :실시간 이메일 발송결재라인 정보 출력(팝업내용)
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div class="cont">
	<!-- 결재라인 정보// -->
	<h3>요청자</h3>

	<div class="requester">
		<span><c:out value="${userInfo.userNm}"/></span>
		<span><c:out value="${userInfo.orgKorNm}"/></span>
		<span><c:out value="${userInfo.jobNm}"/></span>
	</div>

	<h3>결재라인</h3>
	<table class="grid type-border">
		<caption>그리드 정보</caption>
		<colgroup>
			<col style="width:20%;">
			<col style="width:30%;">
			<col style="width:20%;">
			<col style="width:30%;">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">결재순서</th>
				<th scope="col">사용자명</th>
				<th scope="col">조직명</th>
				<th scope="col">직책</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${fn:length(rnsApprLineList) > 0}">
				<c:forEach items="${rnsApprLineList}" var="apprLine" varStatus="apprStatus">
					<tr>
						<td><c:out value="${apprStatus.count}"/></td>
						<td><c:out value="${apprLine.apprUserNm}"/></td>
						<td><c:out value="${apprLine.orgNm}"/></td>
						<td><c:out value="${apprLine.jobNm}"/></td>
					</tr>
				</c:forEach>
			</c:if>
		</tbody>
	</table>
	<!-- //결재라인 정보 -->

	<!-- btn-wrap// -->
	<div class="btn-wrap">
		<button type="button" class="btn big fullpurple" onclick="goRnsSubmitApproval('<c:out value='${serviceVO.tid}'/>');">결재상신</button>
	</div>
	<!-- //btn-wrap -->
	
</div>
