<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.01.26
	*	설명 : 테스트발송 EAI 사용자목록
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>

<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">발송 대상 고객</h3>
	</div>
	
	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:15%;">
				<col style="width:12%;">
				<col style="width:15%;">
				<col style="width:15%;">
				<col style="width:auto;">
				<col style="width:12%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">연계 생성일</th>
					<th scope="col">생성 순번</th>
					<th scope="col">ID</th>
					<th scope="col">고객명</th>
					<th scope="col">이메일</th>
					<th scope="col">발송상태</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(testCustList) > 0}">
					<c:forEach items="${testCustList}" var="testCust" varStatus="testCustStatus">
						<tr>
							<td>
								<fmt:parseDate var="ifMakeDate" value="${testCust.ifMakeDate}" pattern="yyyyMMdd"/>
								<fmt:formatDate var="ifMakeDt" value="${ifMakeDate}" pattern="yyyy.MM.dd"/> 
								<c:out value="${ifMakeDt}"/>
							</td>
							<td><c:out value="${testCust.recevSeqno}"/></td>
							<td><a href="javascript:popTestCustSelect('<c:out value='${testCust.sendResCd}'/>','<c:out value='${testCust.ifId}'/>','<crypto:decrypt colNm="IF_EMAIL" data="${testCust.ifEmail}"/>','<c:out value='${testCust.ifName}'/>','<c:out value='${ifMakeDt}'/>','<c:out value='${testCust.bizkey}'/>','<c:out value='${testCust.recevSeqno}'/>');" class="bold">
								<c:out value="${testCust.ifId}"/>
								</a>
							</td>
							<td><c:out value="${testCust.ifName}"/></td>
							<td><crypto:decrypt colNm="IF_EMAIL" data="${testCust.ifEmail}"/></td>
							<td class="txt-black bold"> 
								<c:choose>
									<c:when test="${'000' eq testCust.sendResCd}">
										발송성공
									</c:when>
									<c:when test="${'NOT' eq testCust.sendResCd}">
									</c:when>
									<c:otherwise>
										발송실패
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
				</c:if>
				<c:if test="${empty testCustList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="6" class="no_data">등록된 내용이 없습니다.</td>
					</tr>
					<!-- //데이터가 없을 경우 -->
				</c:if>
			</tbody>
		</table>
	</div>
</div>
