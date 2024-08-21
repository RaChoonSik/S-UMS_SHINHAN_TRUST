<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.11.16
	*	설명 : 실시간 이메일 발송결재 결재라인 출력
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
				<c:forEach items="${apprLineList}" var="apprLine" varStatus="apprLineStatus">
					<input type="hidden" name="tempUserId" value="<c:out value='${apprLine.apprUserId}'/>">
					<input type="hidden" name="tempApprStep" value="<c:out value='${apprLine.apprStep}'/>">
					<input type="hidden" name="tempTotalCount" value="<c:out value='${apprLine.totalCount}'/>">	
					<input type="hidden" name="tempRejectNm" value="<c:out value='${apprLine.rejectNm}'/>">	
					<input type="hidden" name="tempRegId" value="<c:out value='${apprLine.regId}'/>">	
					<fmt:parseDate var="upDate" value="${apprLine.upDt}" pattern="yyyyMMddHHmmss"/>
					<fmt:formatDate var="upDt" value="${upDate}" pattern="yyyy.MM.dd"/>
					<input type="hidden" name="tempUpdt" value="<c:out value='${upDt}'/>">
					<c:choose>
						<c:when test="${'001' eq apprLine.rsltCd && apprLine.apprUserId eq NEO_USER_ID}">
							<td>
								<button type="button" class="btn fullblue" onclick="goApprStepConfirm('<c:out value='${apprLine.apprStep}'/>','<c:out value='${apprLine.totalCount}'/>');">결재</button>
								<button type="button" class="btn" onclick="popApprStepReject('<c:out value='${apprLine.apprStep}'/>');">반려</button>
							</td>
						</c:when>
						<c:otherwise>
								<td>
									<strong>
									<c:choose>		
										<c:when test="${'002' eq apprLine.rsltCd}">
											<c:choose>
											<c:when test="${apprLine.rsltNm eq '승인'}">
											<a href="javascript:popConfirmDisplay(<c:out value='${apprLineStatus.count}'/>)" class="color-blue"><c:out value="${apprLine.rsltNm}"/></a>
											</c:when>
											<c:when test="${apprLine.rsltNm eq '전결'}">
											<c:out value="${apprLine.rsltNm}"/>
											</c:when>	
											</c:choose>
										</c:when>	
										<c:when test="${'003' eq apprLine.rsltCd}">
											<c:choose>
											<c:when test="${apprLine.rsltNm eq '반려'}">
											<a href="javascript:popRejectDisplay(<c:out value='${apprLineStatus.count}'/>)" class="color-red"><c:out value="${apprLine.rsltNm}"/></a>
											</c:when>
											<c:when test="${apprLine.rsltNm eq '전결'}">
											<c:out value="${apprLine.rsltNm}"/>
											</c:when>
											</c:choose>
										</c:when>
										
										<c:otherwise>
											<c:out value="${apprLine.rsltNm}"/>
										</c:otherwise>								
									</c:choose>
									</strong>	
									<c:if test="${'000' ne apprLine.rsltCd}">
									${upDt}	
									</c:if>
								</td>
							</c:otherwise>
					</c:choose>
				</c:forEach>
			</tr>
		</tbody>
	</table>
</div>

