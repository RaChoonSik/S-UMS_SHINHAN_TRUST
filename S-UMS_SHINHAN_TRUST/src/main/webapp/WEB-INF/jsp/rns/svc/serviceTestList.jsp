<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.09.03
	*	설명 : 자동발송메일 테스트메일 발송목록 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">목록</h3>
		<span class="total">Total: <em><c:out value="${pageUtil.totalRow}"/></em></span>
	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:5%;">
				<col style="width:20%;">
				<col style="width:auto;">
				<col style="width:15%;">
				<col style="width:10%;">
				<col style="width:10%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">발송일시</th>
					<th scope="col">서비스명</th>
					<th scope="col">테스트진행자</th>
					<th scope="col">성공</th>
					<th scope="col">실패</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(serviceTestList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${serviceTestList}" var="serviceTest" varStatus="testStatus">
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - testStatus.index}"/></td>
							<td>
								<fmt:formatDate var="sdate" value="${serviceTest.sdate}" pattern="yyyy.MM.dd HH:mm"/>
								<c:out value="${sdate}"/>
							</td>
							<td><a href="javascript:goTestList('<c:out value="${serviceTest.mid}"/>','<c:out value="${sdate}"/>','<c:out value="${serviceTest.tnm}"/>');" class="bold"><c:out value="${serviceTest.tnm}"/></a></td>
							<td><c:out value="${serviceTest.regNm}"/></td>
							<td><c:out value="${serviceTest.succCnt}"/></td>
							<td><c:out value="${serviceTest.failCnt}"/></td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty serviceTestList}">
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