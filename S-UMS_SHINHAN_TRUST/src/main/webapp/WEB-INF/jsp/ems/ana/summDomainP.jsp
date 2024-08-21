<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.09.08
	*	설명 : 통계분석 기간별누적분석 누적통계 도메인별 탭화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">목록</h3>
		<span class="total">Total: <em><c:out value="${fn:length(domainList)}"/></em></span>
	</div>

	<div class="grid-area pb20">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:5%;">
				<col style="width:auto;">
				<col style="width:9%;">
				<col style="width:9%;">
				<col style="width:9%;">
				<col style="width:10%;">
				<col style="width:9%;">
				<col style="width:9%;">
				<col style="width:10%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">도메인명</th>
					<th scope="col">발송수</th>
					<th scope="col">성공수</th>
					<th scope="col">실패수</th>
					<th scope="col">발송단계에러</th>
					<th scope="col">성공률</th>
					<th scope="col">실패율</th>
					<th scope="col">발송단계에러율</th>
				</tr>
			</thead>
			<tbody>
				<c:set var="sumSendCnt" value="${0}"/>
				<c:set var="sumSuccCnt" value="${0}"/>
				<c:set var="sumFailCnt" value="${0}"/>
				<c:set var="sumErrCnt" value="${0}"/>
				<c:if test="${fn:length(domainList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${domainList}" var="domain" varStatus="domainStatus">
						<c:set var="sumSendCnt" value="${sumSendCnt + domain.sendCnt}"/>
						<c:set var="sumSuccCnt" value="${sumSuccCnt + domain.succCnt}"/>
						<c:set var="sumFailCnt" value="${sumFailCnt + (domain.sendCnt - domain.succCnt)}"/>
						<c:set var="sumErrCnt" value="${sumErrCnt + domain.errCnt}"/>
						<tr>
							<td><c:out value="${domainStatus.count}"/></td>
							<td class="tal"><c:out value="${domain.domainNm}"/></td>
							<td>
								<fmt:formatNumber var="sendCntNum" type="number" value="${domain.sendCnt}" /><c:out value="${sendCntNum}"/>
							</td>
							<td>
								<fmt:formatNumber var="succCntNum" type="number" value="${domain.succCnt}" /><c:out value="${succCntNum}"/>
							</td>
							<td>
								<fmt:formatNumber var="failCntNum" type="number" value="${domain.sendCnt - domain.succCnt}" /><c:out value="${failCntNum}"/>
							</td>
							<td>
								<fmt:formatNumber var="errCntNum" type="number" value="${domain.errCnt}" /><c:out value="${errCntNum}"/>
							</td>
							<td>
								<fmt:formatNumber var="succPer" type="percent" value="${domain.sendCnt == 0 ? 0 : domain.succCnt / domain.sendCnt}" /><c:out value='${succPer}'/>
							</td>
							<td>
								<fmt:formatNumber var="failPer" type="percent" value="${domain.sendCnt == 0 ? 0 : (domain.sendCnt - domain.succCnt) / domain.sendCnt}" /><c:out value='${failPer}'/>
							</td>
							<td>
								<fmt:formatNumber var="errPer" type="percent" value="${domain.sendCnt - domain.succCnt == 0 ? 0 : domain.errCnt / (domain.sendCnt - domain.succCnt)}" /><c:out value='${errPer}'/>
							</td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty domainList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="9" class="no_data">등록된 내용이 없습니다.</td>
					</tr>
					<!-- //데이터가 없을 경우 -->
				</c:if>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="2">합계</td>
					<td>
						<fmt:formatNumber var="sumSendCntNum" type="number" value="${sumSendCnt}" /><c:out value="${sumSendCntNum}"/>
					</td>
					<td>
						<fmt:formatNumber var="sumSuccCntNum" type="number" value="${sumSuccCnt}" /><c:out value="${sumSuccCntNum}"/>
					</td>
					<td>
						<fmt:formatNumber var="sumFailCntNum" type="number" value="${sumFailCnt}" /><c:out value="${sumFailCntNum}"/>
					</td>
					<td>
						<fmt:formatNumber var="sumErrCntNum" type="number" value="${sumErrCnt}" /><c:out value="${sumErrCntNum}"/>
					</td>
					<td>
						<fmt:formatNumber var="sumSuccPer" type="percent" value="${sumSendCnt == 0 ? 0 : sumSuccCnt / sumSendCnt}" /><c:out value='${sumSuccPer}'/>
					</td>
					<td>
						<fmt:formatNumber var="sumFailPer" type="percent" value="${sumSendCnt == 0 ? 0 : sumFailCnt / sumSendCnt}" /><c:out value='${sumFailPer}'/>
					</td>
					<td>
						<fmt:formatNumber var="sumErrPer" type="percent" value="${sumFailCnt == 0 ? 0 : sumErrCnt / sumFailCnt}" /><c:out value='${sumErrPer}'/>
					</td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>
<!-- //목록 -->

<!-- btn-wrap// -->
<div class="btn-wrap">
	<button type="button" class="btn big fullblue" onclick="goExcelDown('Domain');">엑셀 다운로드</button>
</div>
<!-- //btn-wrap -->
