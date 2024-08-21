<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.30
	*	설명 : 메일발송결재 결재라인 출력
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

	<div>
		<table>
			<caption></caption>
			<colgroup>
				<c:forEach items="${apprLineList}" var="apprLine">
					<col style="width:120px">
				</c:forEach>
			</colgroup>
			<tbody>
				<tr>
					<c:forEach items="${apprLineList}" var="apprLine">
						<th>
							<c:out value="${apprLine.orgNm}"/>
							<strong><c:out value="${apprLine.apprUserNm}"/></strong>
						</th>
					</c:forEach>
				</tr>
			<tr>
				<c:forEach items="${apprLineList}" var="apprLine">
					<input type="hidden" name="tempUserId" value="<c:out value='${apprLine.apprUserId}'/>">
					<fmt:parseDate var="upDate" value="${apprLine.upDt}" pattern="yyyyMMddHHmmss"/>
					<fmt:formatDate var="upDt" value="${upDate}" pattern="yyyy.MM.dd"/>
					<c:choose>
						<c:when test="${'001' eq apprLine.rsltCd && apprLine.apprUserId eq NEO_USER_ID}">
							<td>
								<button type="button" class="btn fullblue" onclick="goApprStepConfirm('<c:out value='${apprLine.apprStep}'/>','<c:out value='${apprLine.totalCount}'/>');">결재</button>
								<button type="button" class="btn" onclick="popApprStepReject('<c:out value='${apprLine.apprStep}'/>');">반려</button>
							</td>
						</c:when>
						<c:when test="${'001' eq apprLine.rsltCd && apprLine.apprUserId ne NEO_USER_ID}">
							<td>
								<strong class="txt-gray"><c:out value="${apprLine.rsltNm}"/></strong>
							</td>
						</c:when>
						<c:when test="${'002' eq apprLine.rsltCd}">
							<td>
								<strong><c:out value="${apprLine.rsltNm}"/></strong>
								<c:out value="${upDt}"/>
							</td>
						</c:when>
						<c:when test="${'003' eq apprLine.rsltCd}">
							<td>
								<strong>
									<a href="javascript:popRejectDisplay('<c:out value="${upDt}"/>','<c:out value='${apprLine.rejectNm}'/>');" class="color-red">반려</a>
								</strong>
								<c:out value="${upDt}"/>
							</td>
						</c:when>
						<c:otherwise>
							<td>
								<strong>&nbsp;</strong>
								&nbsp;
							</td>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</tr>
			</tbody>
		</table>
	</div>

