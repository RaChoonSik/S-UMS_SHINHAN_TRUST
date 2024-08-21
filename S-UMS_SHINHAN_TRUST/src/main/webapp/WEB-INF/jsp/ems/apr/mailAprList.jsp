<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.23
	*	설명 : 메일발송결재 목록
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
				<col style="width:7%;">
				<col style="width:7%;">
				<col style="width:auto;">
				<col style="width:7%;">
				<col style="width:7%;">
				<col style="width:7%;">
				<col style="width:7%;">
				<col style="width:7%;">
				<col style="width:7%;">
				<col style="width:7%;">
				<col style="width:7%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">메일유형</th>
					<th scope="col">예약일시</th>
					<th scope="col">메일명</th>
					<th scope="col">캠페인/서비스명</th>
					<th scope="col">마케팅동의여부</th>
					<th scope="col">보안메일</th><!-- <th scope="col">수신자그룹</th> -->
					<th scope="col">사용자명</th>
					<th scope="col">부서</th>
					<th scope="col">상태</th>
					<th scope="col">등록일</th>
					<th scope="col">발송상태</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${not empty aprMailList}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${aprMailList}" var="mail" varStatus="mailStatus">
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - mailStatus.index}"/></td>
							<td style="display:none"><c:out value="${mail.svcType}"/></td>
							<td><c:out value="${mail.svcTypeNm}"/></td>
							<td>
								<fmt:parseDate var="sendDate" value="${mail.sendDt}" pattern="yyyyMMddHHmm"/>
								<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy.MM.dd HH:mm"/>
								<c:out value="${sendDt}"/>
							</td>
							<td><a href="javascript:goAprUpdate('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.subTaskNo}'/>', '<c:out value='${mail.svcType}'/>');" class="bold"><c:out value="${mail.taskNm}"/></a></td>
							<td><c:out value="${mail.campNm}"/></td>
							<td><c:out value="${mail.mktYn }"/></td>
							<td>
								<c:if test="${'Y' eq mail.secuAttYn}">해당</c:if>
								<!-- <a href="javascript:goSegInfoMail('<c:out value='${mail.segNo}'/>')" class="regular"><c:out value="${mail.segNm}"/></a> -->
							</td>
							<td><c:out value="${mail.userNm}"/></td>
							<td><c:out value="${mail.orgKorNm}"/></td>
							<td><c:out value="${mail.statusNm }"/></td>
							<td>
								<fmt:parseDate var="regDate" value="${mail.regDt}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="regDt" value="${regDate}" pattern="yyyy.MM.dd"/>
								<c:out value="${regDt}"/>
							</td>
							<td class="color-red medium"><c:out value="${mail.workStatusNm}"/></td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>
				<c:if test="${empty aprMailList}">
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
