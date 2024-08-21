<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.19
	*	설명 : 메일 목록 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">목록</h3>
		<span class="total">Total: <em><c:out value="${pageUtil.totalRow}"/></em></span>
	</div>
	
	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:19%;">
				<col style="width:auto;">
				<col style="width:17%;">
				<col style="width:11%;">
				<col style="width:10%;">
				<col style="width:11%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">등록일시</th><!-- 등록일이냐 예약일이냐?? 예약일 sendDt -->
					<th scope="col">메일명</th>
					<th scope="col">캠페인명</th>
					<th scope="col">수신자그룹</th>
					<th scope="col">사용자명</th>
					<th scope="col">단기/정기</th>
				</tr>
			</thead>
			<tbody>
				<!-- 데이터가 있을 경우// -->
				<c:if test="${fn:length(mailList) > 0}">
					<c:forEach items="${mailList}" var="mail">
						<tr>           
							<td>
								<fmt:parseDate var="regDt" value="${mail.regDt}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="regDt" value="${regDt}" pattern="yyyy-MM-dd HH:mm"/>
								<c:out value="${regDt}"/>
							</td>     
							<td>
								<a href="javascript:goMailSelect('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.subTaskNo}'/>','<c:out value='${mail.taskNm}'/>');" class="bold"><c:out value='${mail.taskNm}'/></a>
							</td>
							<td><c:out value="${mail.campNm}"/></td>
							<td><c:out value="${mail.segNm}"/></td>
							<td><c:out value="${mail.userId}"/></td>
							<td><c:out value="${mail.sendRepeatNm}"/></td>
						</tr>
					</c:forEach>
				</c:if>
				<!-- //데이터가 있을 경우 -->

				<!-- 데이터가 없을 경우// -->
				<c:if test="${empty mailList}">
					<tr>
						<td colspan="6" class="no_data">등록된 내용이 없습니다.</td>
					</tr>
				</c:if>
				<!-- //데이터가 없을 경우 -->
			</tbody>
		</table>
	</div>
</div>
<!-- //목록 -->

<!-- 페이징// -->
<div class="paging">
	${pageUtil.pageHtml}
</div>
