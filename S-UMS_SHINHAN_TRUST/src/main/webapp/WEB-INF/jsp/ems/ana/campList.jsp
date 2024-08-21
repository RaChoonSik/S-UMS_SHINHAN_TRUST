<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.09.07
	*	설명 : 통계분석 캠페인별 분석 목록 조회
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
			<button type="button" class="btn fullblue" onclick="goJoin();">병합분석</button>
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
				<col style="width:13%;">
				<col style="width:13%;">
				<col style="width:13%;">
				<col style="width:13%;">
				<col style="width:13%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">
						<label><input type="checkbox" name="isAll" onclick="goAll();"><span></span></label>
					</th>
					<th scope="col">캠페인명</th>
					<th scope="col">캠페인목적</th>
					<th scope="col">사용자그룹</th>
					<th scope="col">사용자명</th>
					<th scope="col">등록일시</th>
					<th scope="col">상태</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(campaignList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${campaignList}" var="campaign" varStatus="campStatus">
						<tr>
							<td>${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - campStatus.index}</td>
							<td><label><input type="checkbox" name="campNos" value="<c:out value='${campaign.campNo}'/>"><span></span></label></td>
							<td><a href="javascript:goCampStat('<c:out value='${campaign.campNo}'/>');" class="bold"><c:out value='${campaign.campNm}'/></a></td>
							<td><c:out value='${campaign.campTyNm}'/></td>
							<td><c:out value='${campaign.deptNm}'/></td>
							<td><c:out value='${campaign.userNm}'/></td>
							<td>
								<fmt:parseDate var="regDt" value="${campaign.regDt}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="campRegDt" value="${regDt}" pattern="yyyy.MM.dd"/>
								<c:out value='${campRegDt}'/>
							</td>
							<td><c:out value='${campaign.statusNm}'/></td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty campaignList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="8" class="no_data">등록된 내용이 없습니다.</td>
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
