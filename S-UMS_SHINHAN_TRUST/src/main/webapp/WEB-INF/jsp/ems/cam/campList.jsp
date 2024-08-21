<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.07.12
	*	설명 : 캠페인 목록 조회
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">목록</h3>
		<span class="total">Total: <em><c:out value="${pageUtil.totalRow}"/></em></span>

		<!-- 버튼// -->
		<div class="btn-wrap">
			<button type="button" class="btn fullblue plus" onclick="goAdd();">신규등록</button>
		</div>
		<!-- //버튼 -->
	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:5%;">
				<col style="width:10%;">
				<col style="width:auto;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:15%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">캠페인번호</th>
					<th scope="col">캠페인명</th>
					<th scope="col">발송메일</th>
					<th scope="col">캠페인목적</th>
					<th scope="col">사용자그룹</th>
					<th scope="col">사용자명</th>
					<th scope="col">상태</th>
					<th scope="col">등록일시</th>
				</tr>
			</thead>
			<tbody>
				<!-- 데이터가 있을 경우// -->
				<c:if test="${fn:length(campaignList) > 0}">
					<c:forEach items="${campaignList}" var="campaign" varStatus="campStatus">
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - campStatus.index}"/></td>
							<td><c:out value="${campaign.campNo}"/></td>
							<td>
								<a href="javascript:goUpdate('<c:out value='${campaign.campNo}'/>');" class="bold"><c:out value='${campaign.campNm}'/></a>
							</td>
							<td><a href="javascript:goMail('<c:out value='${campaign.campNo}'/>');" class="regular"><c:out value="${campaign.campMailCnt}"/>건</a></td>
							<td><c:out value="${campaign.campTyNm}"/></td>
							<td><c:out value="${campaign.deptNm}"/></td>
							<td><c:out value="${campaign.userNm}"/></td>
							<td><c:out value="${campaign.statusNm}"/></td>
							<td>
								<fmt:parseDate var="regDate" value="${campaign.regDt}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="regDt" value="${regDate}" pattern="yyyy.MM.dd HH:mm"/>
								<c:out value="${regDt}"/>
							</td>
						</tr>
					</c:forEach>
				</c:if>
				<!-- //데이터가 있을 경우 -->

				<!-- 데이터가 없을 경우// -->
				<c:if test="${empty campaignList}">
					<tr>
						<td colspan="9" class="no_data">등록된 내용이 없습니다.</td>
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
<!-- //페이징 -->
