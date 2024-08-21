<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.31
	*	설명 : 테스트메일 발송결과 조회
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">목록</h3>
	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:15%;">
				<col style="width:auto;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:25%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">수신자</th>
					<th scope="col">이메일</th>
					<th scope="col">발송성공</th>
					<th scope="col">발송실패</th>
					<th scope="col">실패사유</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(sendLogList) > 0}">
					<c:forEach items="${sendLogList}" var="sendLog">
						<!-- 데이터가 있을 경우// -->
						<tr>
							<td><c:out value="${sendLog.custNm}"/></td>
							<td><crypto:decrypt colNm="CUST_EM" data="${sendLog.custEm}"/></td>
							<td><c:out value="${sendLog.succCnt}"/></td>
							<td><c:out value="${sendLog.failCnt}"/></td>
							<td><c:out value="${sendLog.failDesc}"/></td>
						</tr>
						<!-- //데이터가 있을 경우 -->
					</c:forEach>
				</c:if>

				<c:if test="${empty sendLogList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="5" class="no_data">등록된 내용이 없습니다.</td>
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