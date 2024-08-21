<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.20
	*	설명 : 데이터베이스 권한 정보 관리
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div class="box">
	<div class="title-area">
		<h4 class="h4-title">사용자</h4>
	</div>	
	<div class="list-area">
		<table>
		<c:if test="${fn:length(userList) > 0}">
			<c:forEach items="${userList}" var="user">
				<tr>
					<td><c:out value="${user.deptNm}"/></td>
					<td><c:out value="${user.userNm}"/></td>
					<td><c:out value="${user.userId}"/></td>
				</tr>
			</c:forEach>
		</c:if>	
		</table>
	</div>
</div>