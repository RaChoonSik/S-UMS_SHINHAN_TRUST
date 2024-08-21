<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.10.25
	*	설명 :캠페인별 발송 현황 목록 조회
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
		<div class="btn-wrap">
		<!-- 	<button type="button" class="btn big fullblue" onclick="excelDownload();">엑셀다운로드</button> -->
		</div> 
		<!-- //btn-wrap -->
	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:5%;">
				<col style="width:15;">
				<col style="width:13%;">
				<col style="width:8%;">
				<col style="width:auto;">
				<col style="width:9%;">
				<col style="width:9%;">
				<col style="width:9%;">
				<col style="width:9%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">캠페인명</th>
					<th scope="col">최종<br>발송일자</th>
					<th scope="col">건수</th>
					<th scope="col">발송PUSH명</th>
					<th scope="col">사용자명</th>
					<th scope="col">PUSH등록일자</th>
					<th scope="col">캠페인등록일자</th>
					<th scope="col">발송상태</th> 
					
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(campPushList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${campPushList}" var="campPush" varStatus="campPushSendStatus">
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - campPushSendStatus.index}"/></td>
							<td><a href="javascript:;" onclick="goCampPushList('<c:out value="${campPush.campNo}"/>','<c:out value="${campPush.campNm}"/>');" class="bold"><c:out value="${campPush.campNm}"/></a></td>
							<td>
								<fmt:parseDate var="sendDate" value="${campPush.sendDt}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy-MM-dd HH:mm"/>
								<c:out value='${sendDt}'/>
							</td>
							<td><c:out value="${campPush.pushCount}"/></td>
							<td><c:out value="${campPush.pushName}"/></td>
							<td><c:out value="${campPush.userNm}"/></td>
							<td>
								<fmt:parseDate var="pushRegDate" value="${campPush.pushRegDt}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="pushRegDt" value="${pushRegDate}" pattern="yyyy-MM-dd"/>
								<c:out value='${pushRegDt}'/>
							</td>
							<td>
								<fmt:parseDate var="campRegDate" value="${campPush.campRegDt}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="campRegDt" value="${campRegDate}" pattern="yyyy-MM-dd"/>
								<c:out value='${campRegDt}'/>
							</td>
							<td><c:out value="${campPush.workStatusNm}"/></td>
							
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty campPushList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="9" class="no_data">발송된 내용이 없습니다.</td>
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

