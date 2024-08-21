<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.28
	*	설명 : 테스트메일 발송목록 조회
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
				<col style="width:15%;">
				<col style="width:auto;">
				<col style="width:10%;">
				<col style="width:12%;">
				<col style="width:5%;">
				<col style="width:5%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">발송일시</th>
					<th scope="col">메일명</th>
					<th scope="col">테스트진행자</th>
					<th scope="col">메일유형</th>
					<th scope="col">성공</th>
					<th scope="col">실패</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(mailTestList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${mailTestList}" var="mail" varStatus="mailStatus">
						<tr>
							<td>${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - mailStatus.index}</td>
							<td>
								<fmt:parseDate var="sendDate" value="${mail.sendDt}" pattern="yyyyMMddHHmm"/>
								<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy.MM.dd HH:mm"/> 
								<c:out value="${sendDt}"/>
							</td>
							<td>
								<a href="javascript:goTestList('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.subTaskNo}'/>','<c:out value="${sendDt}"/>','<c:out value="${mail.sendRepeatNm}"/>','<c:out value="${mail.taskNm}"/>');" class="bold"><c:out value="${mail.taskNm}"/></a>
							</td>
							<td><c:out value="${mail.userNm}"/></td>
							<td><c:out value="${mail.sendRepeatNm}"/></td>
							<td><c:out value="${mail.sucCnt}"/></td>
							<td>
								<c:if test="${mail.failCnt > 0 }">
									<a href="javascript:goTestList('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.subTaskNo}'/>','<c:out value="${sendDt}"/>','<c:out value="${mail.sendRepeatNm}"/>','<c:out value="${mail.taskNm}"/>');" class="bold"><c:out value="${mail.failCnt}"/></a>
								</c:if>
								<c:if test="${mail.failCnt == 0 }">
									<c:out value="${mail.failCnt}"/>
								</c:if>
							</td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty mailTestList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="7" class="no_data">등록된 내용이 없습니다.</td>
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