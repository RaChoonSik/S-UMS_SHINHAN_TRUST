<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.04
	*	설명 : 테스트발송상세정보 팝업창 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header.jsp" %>

<script type="text/javascript">
//상세 내역 이동 EVENT 구현
function goInfo(taskNo, subTaskNo) {
	$("#taskNo").val(taskNo);
	$("#subTaskNo").val(subTaskNo);
	$("#searchForm").attr("target","").attr("action","<c:url value='/ems/cam/mailTestSendLogP.ums'/>").submit();
}
</script>

<form id="searchForm" name="searchForm" method="post">
<input type="hidden" id="sendTestTaskNo" name="sendTestTaskNo" value="<c:out value='${taskVO.sendTestTaskNo}'/>">
<input type="hidden" id="sendTestSubTaskNo" name="sendTestSubTaskNo" value="<c:out value='${taskVO.sendTestSubTaskNo}'/>">
<input type='hidden' id="subTaskNo" name='subTaskNo' value=''>
<input type='hidden' id="taskNo" name='taskNo' value=''>
</form>

<table width="350" border="1" cellspacing="0" cellpadding="0" class="table_line_outline">
	<tr>
		<td class="td_line" colspan="5"></td>
	</tr>
	<tr class="tr_head" align="center">
		<td width='40%'><spring:message code='CAMTBLTL048'/></td><!-- 발송일자 -->
		<td width='30%'><spring:message code='CAMTBLTL012'/></td><!-- 발송상태 -->
		<td width='15%'><spring:message code='COMJSALT014'/></td><!-- 성공 -->
		<td width='15%'><spring:message code='COMJSALT015'/></td><!-- 실패 -->
	</tr>
	<c:if test="${fn:length(testList) > 0}">
		<c:forEach items="${testList}" var="test">
			<tr class="tr_body" height="20">
				<td align="center">
					<fmt:parseDate var="sendDt" value="${test.sendDt}" pattern="yyyyMMddHHmm"/>
					<fmt:formatDate var="sendDt" value="${sendDt}" pattern="yyyy-MM-dd HH:mm"/>
					<a href="javascript:goInfo('<c:out value='${test.taskNo}'/>','<c:out value='${test.subTaskNo}'/>')"><c:out value='${sendDt}'/></a>
				</td>
				<td>
					<c:choose>
						<c:when test="${'000' eq test.workStatus}">
							<spring:message code='CAMTBLLB011'/><!-- 발송대기 -->
						</c:when>
						<c:when test="${'001' eq test.workStatus}">
							<spring:message code='CAMTBLLB012'/><!-- 발송승인 -->
						</c:when>
						<c:when test="${'002' eq test.workStatus}">
							<spring:message code='CAMTBLLB013'/><!-- 발송중... -->
						</c:when>
						<c:when test="${'003' eq test.workStatus}">
							<spring:message code='CAMTBLLB014'/><!-- 발송완료 -->
						</c:when>
						<c:otherwise>
							<a title="<c:out value='${test.workStatusDtl}'/>"><spring:message code='CAMTBLLB015'/></a><!-- 발송실패 -->
						</c:otherwise>
					</c:choose>
				</td>
				<td>
					<fmt:formatNumber var="sucCnt" type="number" value="${test.sucCnt}" />
					<c:out value='${sucCnt}'/>
				</td>
				<td>
					<fmt:formatNumber var="failCnt" type="number" value="${test.failCnt}" />
					<c:out value='${failCnt}'/>
				</td>
			</tr>
		</c:forEach>
	</c:if>
</table>

<table width="350" border="0" cellspacing="0" cellpadding="0">
	<tr><td height=5></td></tr>
	<tr>
		<td align=right>
			<input type="button" class="btn_style" value="<spring:message code='COMBTN001'/>" onClick="window.close()"><!-- 닫기 -->
		</td>
	</tr>
</table>

</body>
</html>
