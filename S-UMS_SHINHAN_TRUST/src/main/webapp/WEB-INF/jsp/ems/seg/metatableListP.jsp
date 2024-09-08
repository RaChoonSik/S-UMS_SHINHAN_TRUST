<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.19
	*	설명 : DB CONNECTION 메타 정보 관리 메인화면을 출력
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<table class="grid type-border">
	<caption>그리드 정보</caption>
	<colgroup>
		<col>
	</colgroup>
	<thead>
		<tr>
			<th scope="col">메타테이블</th>
		</tr>
	</thead>
	<tbody>
		<c:if test="${fn:length(realTableList) > 0}">
			<!-- 데이터가 있을 경우// -->
			<c:forEach items="${realTableList}" var="realTable">
				<tr>
					<td>
						<button type="button" class="btn-meta" onclick="goColumnInfo('<c:out value="${realTable}"/>');"><c:out value="${realTable}"/></button>
					</td>
				</tr>
			</c:forEach>
			<!-- //데이터가 있을 경우 -->
		</c:if>

		<c:if test="${empty realTableList}">
			<!-- 데이터가 없을 경우// -->
			<tr>
				<td class="no_data">조회된 내용이 없습니다.</td>
			</tr>
			<!-- //데이터가 없을 경우 -->
		</c:if>
	</tbody>
</table>
