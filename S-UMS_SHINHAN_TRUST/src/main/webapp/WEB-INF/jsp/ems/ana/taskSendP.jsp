<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.09.07
	*	설명 : 통계분석 정기메일 통합 통계분석 발송시간별 화면
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
				<col style="width:15%;">
				<col style="width:15%;">
				<col style="width:15%;">
				<col style="width:15%;">
				<col style="width:15%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">시간</th>
					<th scope="col">발송수</th>
					<th scope="col">성공수</th>
					<th scope="col">실패수</th>
					<th scope="col">성공률</th>
					<th scope="col">실패율</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(sendList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${sendList}" var="send" varStatus="sendStatus">
						<tr>
							<td><c:out value='${(pageUtil.currPage-1)*pageUtil.pageRow + sendStatus.count}'/></td>
							<td>
								<fmt:parseDate var="sendTime" value="${send.sendTime}" pattern="yyyyMMddHH"/>
								<fmt:formatDate var="sendTime" value="${sendTime}" pattern="yyyy-MM-dd HH"/>
								<c:out value='${sendTime}'/>시
							</td>
							<td>
								<fmt:formatNumber var="sendCnt" type="number" value="${send.sendCnt}" /><c:out value="${sendCnt}"/>
							</td>
							<td>
								<fmt:formatNumber var="succCnt" type="number" value="${send.succCnt}" /><c:out value="${succCnt}"/>
							</td>
							<td>
								<fmt:formatNumber var="failCnt" type="number" value="${send.sendCnt - send.succCnt}" /><c:out value="${failCnt}"/>
							</td>
							<td>
								<fmt:formatNumber var="succPer" type="percent" value="${send.sendCnt == 0 ? 0 : send.succCnt / send.sendCnt}" /><c:out value='${succPer}'/>
							</td>
							<td>
								<fmt:formatNumber var="failPer" type="percent" value="${send.sendCnt == 0 ? 0 : (send.sendCnt - domain.succCnt) / send.sendCnt}" /><c:out value='${failPer}'/>
							</td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty sendList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="7" class="no_data">등록된 내용이 없습니다.</td>
					</tr>
					<!-- //데이터가 없을 경우 -->
				</c:if>
			</tbody>
			<c:if test="${not empty sendSum && pageUtil.totalPage == pageUtil.currPage}">
			<tfoot>
				<tr>
					<td colspan="2">합계</td>
					<td>
						<fmt:formatNumber var="sumSendCnt" type="number" value="${sendSum.sendCnt}" /><c:out value="${sumSendCnt}"/>
					</td>
					<td>
						<fmt:formatNumber var="sumSuccCnt" type="number" value="${sendSum.succCnt}" /><c:out value="${sumSuccCnt}"/>
					</td>
					<td>
						<fmt:formatNumber var="sumFailCnt" type="number" value="${sendSum.sendCnt - sendSum.succCnt}" /><c:out value="${sumFailCnt}"/>
					</td>
					<td>
						<fmt:formatNumber var="sumSuccPer" type="percent" value="${sendSum.sendCnt == 0 ? 0 : sendSum.succCnt / sendSum.sendCnt}" /><c:out value='${sumSuccPer}'/>
					</td>
					<td>
						<fmt:formatNumber var="sumFailPer" type="percent" value="${sendSum.sendCnt == 0 ? 0 : (sendSum.sendCnt - sendSum.succCnt) / sendSum.sendCnt}" /><c:out value='${sumFailPer}'/>
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
	<!-- <button type="button" class="btn big fullblue" onclick="goExcelDown('Send');">엑셀 다운로드</button> -->
	<button type="button" class="btn big" onclick="goList();">목록</button>
</div>
<!-- //btn-wrap -->
