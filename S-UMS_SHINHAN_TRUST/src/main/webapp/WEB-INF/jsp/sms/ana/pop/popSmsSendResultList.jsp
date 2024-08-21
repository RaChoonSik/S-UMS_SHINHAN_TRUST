<%--
	/**********************************************************
	*	작성자 : 이혜민
	*	작성일시 : 2022.03.25
	*	설명 : SMS 알림톡 팝업 발송건수 조회 리스트
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>

<!-- 조회 결과// -->
<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">조회 결과</h3>
	</div>
	
	<div class="grid-area">
		<div class="table-scroll" style="max-height:295px;">
			<table class="grid">
				<caption>그리드 정보</caption>
				<colgroup>
					<col style="width:50%;">
					<col style="width:auto;">
				</colgroup>
			<thead>
				<tr>
					<th scope="col">발송상태</th>
					<th scope="col">연락처</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(smsSendResultList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${smsSendResultList}" var="smsSendResult" varStatus="smsSendResultStatus">
						<tr>
							<c:if test="${smsSendResult.rsltCd eq '0'}">
								<td><span class="color-red">실패</span></td> 
							</c:if>
							<c:if test="${smsSendResult.rsltCd eq '1'}">
								<td>성공</td>
								<%-- <td><c:out value="${smsSendResult.rsltCd eq '0'?'실패':'성공'}"/></td> --%> 
							</c:if>
							<td><crypto:decrypt colNm="PHONE" data="${smsSendResult.phone}"/></td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>
				<c:if test="${empty smsSendResultList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="2" class="no_data">등록된 내용이 없습니다.</td>
					</tr>
					<!-- //데이터가 없을 경우 -->
				</c:if>
			</tbody>
			</table>
		</div>
	</div>
</div>
