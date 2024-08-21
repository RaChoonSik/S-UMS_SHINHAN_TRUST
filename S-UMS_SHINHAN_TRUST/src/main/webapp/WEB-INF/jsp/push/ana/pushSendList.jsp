<%--
   /**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.04.02
	*	설명 : 캠페인별 -->PUSH알림별 발송현황 목록
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
				<col style="width:10%;">
				<col style="width:auto;">
				<col style="width:5%;">
				<col style="width:10%;">
				<col style="width:12%;">
				<col style="width:5%;">
				<col style="width:5%;">
				<col style="width:5%;">
				<col style="width:5%;">
				<col style="width:8%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col" rowspan="2">NO</th>
					<th scope="col" rowspan="2">발송일시</th>
					<th scope="col" rowspan="2">PUSH명</th>
					<th scope="col" rowspan="2">발송유형</th>
					<th scope="col" rowspan="2">캠페인</th>
					<th scope="col" rowspan="2">사용자그룹</th>
					<th scope="col" colspan="2" style="border-bottom:none;">발송성공</th>
					<th scope="col" colspan="2" style="border-bottom:none;">발송실패</th>
					<th scope="col" rowspan="2">총 건수</th>
				</tr>
				<tr>
					<th scope="col">Android</th>
					<th scope="col">IOS</th>
					<th scope="col">Android</th>
					<th scope="col">IOS</th>
				</tr>
			</thead>
			<tbody>
				<!-- 데이터가 있을 경우// -->
			<c:if test="${fn:length(pushSendList) > 0}">
				<c:forEach items="${pushSendList}" var="push" varStatus="status">
					<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - status.index}"/></td>
						<td>
							<fmt:parseDate var="sendDate" value="${push.sendDt}" pattern="yyyyMMddHHmmss"/>
							<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy.MM.dd HH:mm"/>
							<c:out value="${sendDt}"/>
						</td>
							<td><a href="javascript:goPushAnalList('<c:out value='${push.pushmessageId}'/>');" class="bold"><c:out value="${push.pushName}"/></a></td>
						<td><c:out value='${push.pushGubunNm}'/></td>
						<td><c:out value='${push.campNm}'/></td>
						<td><c:out value='${push.deptNm}'/></td>
						<td><c:out value='${push.succAnd}'/></td>
						<td><c:out value='${push.succIos}'/></td>
						<c:if test="${push.failAnd > 0}">
							<td class="color-red"><c:out value='${push.failAnd}'/></td>
						</c:if>
						<c:if test="${push.failAnd == 0}">
							<td><c:out value='${push.failAnd}'/></td>
						</c:if>
						<c:if test="${push.failIos > 0}">
							<td class="color-red"><c:out value='${push.failIos}'/></td>
						</c:if>
						<c:if test="${push.failIos == 0}">
							<td><c:out value='${push.failIos}'/></td>
						</c:if>
						<td><c:out value='${push.sendTotCnt}'/></td>
					</tr>
				</c:forEach>
			</c:if>

			<c:if test="${empty pushSendList}">
				<!-- 데이터가 없을 경우// -->
				<tr>
					<td colspan="11" class="no_data">등록된 내용이 없습니다.</td>
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
<div class="btn-wrap" style="margin-top:60px;">
	<button type="button" class="btn big fullorange" onclick="goList();">목록</button>
</div>