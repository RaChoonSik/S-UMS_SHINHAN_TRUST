<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.01.14
	*	설명 :캠페인별 발송 - 실시간이메일 발송목록
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">목록</h3>
		<span class="total">Total: <em><c:out value="${pageUtil.totalRow}"/></em></span>

		<!-- btn-wrap// -->
		<!-- 
 		<div class="btn-wrap">
			<div class="select">
				<select  onchange="goChagePerPageRows(this.value);" title="페이지당 결과">
					<c:if test="${fn:length(perPageList) > 0}">
						<c:forEach items="${perPageList}" var="perPage">
							<option value="<c:out value='${perPage.cdNm}'/>" <c:if test="${perPage.cdNm eq searchVO.rows}"> selected</c:if>><c:out value='${perPage.cdNm}'/>개씩 보기</option>
						</c:forEach>
					</c:if>
				</select>
			</div>
		</div> 
		 -->
		<!-- //btn-wrap -->
	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:5%;">
				<col style="width:9%;">
				<col style="width:auto;">
				<col style="width:8%;">
				<col style="width:8%;">
				<col style="width:10%;">
				<col style="width:8%;">
				<col style="width:10%;">
				<col style="width:8%;">
				<col style="width:8%;">
				<col style="width:0%;">
				<col style="width:0%;">
				<col style="width:0%;">
				<col style="width:0%;">
				<col style="width:0%;">
				<col style="width:0%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">발송일시</th>
					<th scope="col">메일명</th>
					<th scope="col">고객ID</th>
					<th scope="col">고객명</th>
					<th scope="col">고객이메일</th>
					<th scope="col">발송자명</th>
					<th scope="col">발송자이메일</th>
					<th scope="col">발송상태</th>
					<th scope="col">발송구분</th>
					<th scope="col" class="hide">메일유형</th>
					<th scope="col" class="hide">캠페인명</th>
					<th scope="col" class="hide">Bizkey</th>
					<th scope="col" class="hide">보안메일타입</th>
					<th scope="col" class="hide">발송결과코드</th>
					<th scope="col" class="hide">수신확인</th> 
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(campMailRnsList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${campMailRnsList}" var="campMailRns" varStatus="campMailRnsStatus">
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - campMailRnsStatus.index}"/></td>
							<td>
								<fmt:parseDate var="sendDate" value="${campMailRns.sendDt}" pattern="yyyyMMddHHmm"/>
								<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy-MM-dd HH:mm"/>
								<c:out value='${sendDt}'/>
							</td>
							<td><a href="javascript:;" onclick="goMailDetil('<c:out value="${campMailRns.taskNo}"/>', '<c:out value="${campMailRns.subTaskNo}"/>', '<c:out value="${campMailRns.attCnt}"/>', '<c:out value="${campMailRns.serviceGb}"/>', '<c:out value="${campMailRns.webAgent}"/>', '<c:out value="${campMailRns.contFlPath}"/>', this);" class="bold"><c:out value="${campMailRns.mailTitle}"/></a></td>
							<td><c:out value="${campMailRns.custId}"/></td>
							<td><c:out value="${campMailRns.custNm}"/></td>
							<td><crypto:decrypt colNm="CUST_EM" data="${campMailRns.custEm}"/></td>
							<td><c:out value="${campMailRns.mailFromNm}"/></td>
							<td><crypto:decrypt colNm="MAIL_FROM_EM" data="${campMailRns.mailFromEm}"/></td>
							<td><c:out value="${campMailRns.sendRcodeNm}"/></td>
							<td>
								<c:choose>
									<c:when test="${0 eq campMailRns.rtyNo}">
										본발송
									</c:when>
									<c:otherwise>
										재발송
									</c:otherwise>
								</c:choose>
							</td>
							<td class="hide">실시간</td>
							<td class="hide"><c:out value="${campMailRns.campNm}"/></td>
							<td class="hide"><c:out value='${campMailRns.bizkey}'/></td>
							<td class="hide"><c:out value='${campMailRns.webAgentTyp}'/></td>
							<td class="hide"><c:out value='${campMailRns.sendRcode}'/></td>
							<td class="hide"><c:out value='${campMailRns.respAmt}'/></td>
							<td class="hide"><c:out value='${campMailRns.segReal}'/></td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty campMailRnsList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="16" class="no_data">등록된 내용이 없습니다.</td>
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
<!-- btn-wrap// -->
<div class="btn-wrap" style="margin-top:60px;">
	<button type="button" class="btn fullblue big" onClick="goList()">목록</button>
</div> 

