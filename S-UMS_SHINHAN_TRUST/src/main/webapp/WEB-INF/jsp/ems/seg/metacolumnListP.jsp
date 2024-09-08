<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.07.22
	*	설명 : DB CONNECTION 메타 정보 관리 메인화면을 출력
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<table class="grid type-border">
	<caption>그리드 정보</caption>
	<colgroup>
		<col style="width:50%;">
		<col style="width:50%;">
	</colgroup>
	<thead>
		<tr>
			<th scope="col">칼럼명</th>
			<th scope="col">타입</th>
		</tr>
	</thead>
	<tbody>
<%-- 		<c:if test="${fn:length(metaColumnList) > 0}">
			<!-- 데이터가 있을 경우// -->
			<c:forEach items="${metaColumnList}" var="metaColumn">
				<tr>
					<td><c:out value='${metaColumn.colNm}'/></td>
					<td><c:out value='${metaColumn.colDataTy}'/></td>
				</tr>
			</c:forEach>
			<!-- //데이터가 있을 경우 -->
		</c:if>
		
		<c:if test="${empty metaColumnList}">
			<!-- 데이터가 없을 경우// -->
			<tr>
				<td colspan="2" class="no_data">등록된 내용이 없습니다.</td>
			</tr>
			<!-- //데이터가 없을 경우 -->
		</c:if> --%>
		<c:if test="${fn:length(realColumnList) > 0}">
			<!-- 데이터가 있을 경우// -->
			<c:forEach items="${realColumnList}" var="realColumn">
				<tr>
					<td><c:out value='${realColumn.colNm}'/></td>
					<td><c:out value='${realColumn.colDataTy}'/></td>
				</tr>
			</c:forEach>
			<!-- //데이터가 있을 경우 -->
		</c:if>
		
		<c:if test="${empty realColumnList}">
			<!-- 데이터가 없을 경우// -->
			<tr>
				<td colspan="2" class="no_data">조회된 내용이 없습니다.</td>
			</tr>
			<!-- //데이터가 없을 경우 -->
		</c:if>
	</tbody>
</table>
