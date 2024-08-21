<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.09.04
	*	설명 : 통계분석 메일별분석 세부에러 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<c:set var="totCnt" value="${0}"/>
<c:if test="${fn:length(errorList) > 0}">
	<c:forEach items="${errorList}" var="error">
		<c:set var="totCnt" value="${totCnt + error.cntStep3}"/>
	</c:forEach>
</c:if>
<!-- 발송결과// -->
<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">메일 에러 종류별 건수 <span class="mgl40">총에러건수 : <c:out value='${totCnt}'/></span></h3>
	</div>
	
	<div class="grid-area pb20">
		<table class="grid type-border type-pdlarge">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:34%;">
				<col style="width:33%;">
				<col style="width:33%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">대분류건수</th>
					<th scope="col">중분류건수</th>
					<th scope="col">소분류건수</th>
				</tr>
			</thead>
			<tbody>
				<c:set var="tmpNmStep1" value=""/>
				<c:set var="tmpNmStep2" value=""/>
				<c:if test="${fn:length(errorList) > 0}">
					<c:forEach items="${errorList}" var="error">
						<tr>
							<c:if test="${empty tmpNmStep1 || tmpNmStep1 ne error.nmStep1}">
								<td rowspan="<c:out value='${error.step1Rowspan}'/>">
									<c:out value='${error.nmStep1}'/> : <fmt:formatNumber var="cntStep1Num" type="number" value="${error.cntStep1}" /><c:out value="${cntStep1Num}"/>
								</td>
							</c:if>
							<c:if test="${empty tmpNmStep2 || tmpNmStep2 ne error.nmStep2}">
								<td class="tar"<c:if test="${error.step2Rowspan != 1}"> rowspan="<c:out value='${error.step2Rowspan}'/>"</c:if>>
									<c:out value='${error.nmStep2}'/> : <fmt:formatNumber var="cntStep2Num" type="number" value="${error.cntStep2}" /><c:out value="${cntStep2Num}"/>
								</td>
							</c:if>
							<c:choose>
								<c:when test="${empty error.nmStep3}">
									<td class="tar">
										<c:out value='${error.nmStep2}'/> : <fmt:formatNumber var="cntStep2Num" type="number" value="${error.cntStep2}" /><c:out value="${cntStep2Num}"/>
									</td>
								</c:when>
								<c:otherwise>
									<td class="tar">
										<c:out value='${error.nmStep3}'/> : <fmt:formatNumber var="cntStep3Num" type="number" value="${error.cntStep3}" /><c:out value="${cntStep3Num}"/>
									</td>
								</c:otherwise>
							</c:choose>
						</tr>
						<c:set var="tmpNmStep1" value="${error.nmStep1}"/>
						<c:set var="tmpNmStep2" value="${error.nmStep2}"/>
					</c:forEach>
				</c:if>
			</tbody>
		</table>
	</div>
</div>
<!-- //발송결과 -->

<!-- btn-wrap// -->
<div class="btn-wrap">
	<button type="button" class="btn big fullblue" onclick="goExcelDown('Error')">엑셀 다운로드</button>
	<button type="button" class="btn big" onclick="goList();">목록</button>
</div>
<!-- //btn-wrap -->
