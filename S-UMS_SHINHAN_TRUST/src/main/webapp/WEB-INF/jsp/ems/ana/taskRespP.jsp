<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.09.07
	*	설명 : 통계분석 정기메일 통합 통계분석 응답시간별 화면
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

	<div class="grid-area pb20">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:9%;">
				<col style="width:auto;">
				<col style="width:17%;">
				<col style="width:17%;">
				<col style="width:17%;">
				<col style="width:17%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">시간</th>
					<th scope="col">오픈수</th>
					<th scope="col">유효오픈수</th>
					<th scope="col">클릭수</th>
					<th scope="col">수신거부</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(respList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${respList}" var="resp" varStatus="respStatus">
						<tr>
							<td><c:out value='${(pageUtil.currPage-1)*pageUtil.pageRow + respStatus.count}'/></td>
							<td>
								<fmt:parseDate var="respTime" value="${resp.respTime}" pattern="yyyyMMddHH"/>
								<fmt:formatDate var="respTime" value="${respTime}" pattern="yyyy-MM-dd HH"/>
								<c:out value='${respTime}'/>시
							</td>
							<td>
								<fmt:formatNumber var="openCnt" type="number" value="${resp.openCnt}" /><c:out value="${openCnt}"/>
							</td>
							<td>
								<fmt:formatNumber var="validCnt" type="number" value="${resp.validCnt}" /><c:out value="${validCnt}"/>
							</td>
							<td>
								<fmt:formatNumber var="clickCnt" type="number" value="${resp.clickCnt}" /><c:out value="${clickCnt}"/>
							</td>
							<td>
								<fmt:formatNumber var="blockCnt" type="number" value="${resp.blockCnt}" /><c:out value="${blockCnt}"/>
							</td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty respList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="6" class="no_data">등록된 내용이 없습니다.</td>
					</tr>
					<!-- //데이터가 없을 경우 -->
				</c:if>
			</tbody>
			<c:if test="${not empty respSum && pageUtil.totalPage == pageUtil.currPage}">
				<tfoot>
					<tr>
						<td colspan="2">합계</td>
						<td>
							<fmt:formatNumber var="sumOpenCnt" type="number" value="${respSum.openCnt}" /><c:out value="${sumOpenCnt}"/>
						</td>
						<td>
							<fmt:formatNumber var="sumValidCnt" type="number" value="${respSum.validCnt}" /><c:out value="${sumValidCnt}"/>
						</td>
						<td>
							<fmt:formatNumber var="sumClickCnt" type="number" value="${respSum.clickCnt}" /><c:out value="${sumClickCnt}"/>
						</td>
						<td>
							<fmt:formatNumber var="sumBlockCnt" type="number" value="${respSum.blockCnt}" /><c:out value="${sumBlockCnt}"/>
						</td>
					</tr>
				</tfoot>
			</c:if>
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
<div class="btn-wrap">
	<!-- <button type="button" class="btn big fullblue" onclick="goExcelDown('Resp');">엑셀 다운로드</button> -->
	<button type="button" class="btn big" onclick="goList();">목록</button>
</div>
<!-- //btn-wrap -->
