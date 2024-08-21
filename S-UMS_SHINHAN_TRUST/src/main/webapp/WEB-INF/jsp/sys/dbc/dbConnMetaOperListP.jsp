<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.19
	*	설명 : DB CONNECTION 메타 추출 VALUE LIST
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<table class="grid">
	<caption>그리드 정보</caption>
	<colgroup>
		<col style="width:20%;">
		<col style="width:20%;">
		<col style="width:10%;">
	</colgroup>
	<thead>
		<tr>
			<th scope="col">VALUE</th>
			<th scope="col">별칭</th>
			<th scope="col">처리</th>
		</tr>
	</thead>
	<tbody id="metaValueList">
		<!-- 데이터가 있을 경우// -->
		<c:if test="${fn:length(metaValueList) > 0}">
			<c:forEach items="${metaValueList}" var="metaValue" varStatus="metaValueStatus">
			<tr>
				<td style="display:none;"><input type="text" placeholder="value값" value="<c:out value='${metaValue.valueNo}'/>"></td>
				<td><input type="text" placeholder="value값" value="<c:out value='${metaValue.valueNm}'/>"></td>
				<td><input type="text" placeholder="별칭값" value="<c:out value='${metaValue.valueAlias}'/>"></td>
				<td>
					<div class="btn-wrap">
						<button type="button" class="btn" onclick="goUpdateValue(this,'<c:out value='${metaValue.valueNo}'/>')">수정</button>
						<button type="button" class="btn" onclick="goDeleteValue(this,'<c:out value='${metaValue.valueNo}'/>')">삭제</button>
					</div>
				</td>
			</tr>
			</c:forEach>
		</c:if>
		 
		<!-- //데이터가 있을 경우 -->
		<!-- 데이터가 없을 경우// -->
		<c:if test="${empty metaValueList}">
			<tr>
				<td colspan="3" class="no_data">등록된 내용이 없습니다.</td>
			</tr>
		</c:if>
		
		<!-- //데이터가 없을 경우 -->
	</tbody>
</table>