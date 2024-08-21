<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.03
	*	설명 : 발송실패 목록 조회
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header.jsp" %>

<script type="text/javascript">
function goExcel() {
	$("#searchForm").attr("target","iFrmExcel").attr("action","<c:url value='/ems/ana/failExcelList.ums'/>").submit();
}

function goPageNum(page) {
	$("#page").val(page);
	$("#searchForm").attr("target","").attr("action","<c:url value='/ems/ana/failListP.ums'/>").submit();
}
</script>

<form id="searchForm" name="searchForm" method="post">
<input type="hidden" id="page" name="page" value="${sendLogVO.page}"/>
<input type="hidden" name="taskNo" value="<c:out value='${sendLogVO.taskNo}'/>">
<input type="hidden" name="subTaskNo" value="<c:out value='${sendLogVO.subTaskNo}'/>">
<input type="hidden" name="step1" value="<c:out value='${sendLogVO.step1}'/>">
<input type="hidden" name="step2" value="<c:out value='${sendLogVO.step2}'/>">
<input type="hidden" name="step3" value="<c:out value='${sendLogVO.step3}'/>">
<input type="hidden" name="domain" value="<c:out value='${sendLogVO.domain}'/>">
<table border="0" cellspacing="1" cellpadding="1" width="790">
	<tr height="20">
		<td colspan="10" align="right">
			<input type="button" value="Excel download" onClick="goExcel()" class="btn_style">
		</td>
	</tr>
</table>
</form>

<table width="790" border="1" cellspacing="0" cellpadding="1" class="table_line_outline">
	<tr>
		<td class="td_line" colspan="5"></td>
	</tr>
	<tr class="tr_head" align="center">
		<td width="15%">EMAIL</td>
		<td width="10%">ID</td>
		<td width="10%">NAME</td>
		<td width="15%">SENDING DATE</td>
		<td width="50%">MESSAGE</td>
	</tr>
	<c:if test="${fn:length(failList) > 0}">
		<c:forEach items="${failList}" var="fail">
			<tr class="tr_body">
				<td align="left"><c:out value='${fail.custEm}'/></td>
				<td align="center"><c:out value='${fail.custId}'/></td>
				<td align="center"><c:out value='${fail.custNm}'/></td>
				<td align="center">
					<fmt:parseDate var="mailSendDt" value="${fail.sendDt}" pattern="yyyyMMddHHmm"/>
					<fmt:formatDate var="sendDt" value="${mailSendDt}" pattern="yyyy/MM/dd HH:mm"/> 
					<c:out value='${sendDt}'/>
				</td>
				<td align="center"><c:out value='${fail.sendMsg}'/></td>
			</tr>
		</c:forEach>
	</c:if>
	<c:set var="emptyCnt" value="${sendLogVO.rows - fn:length(failList)}"/>
	<c:if test="${emptyCnt > 0}">
		<c:forEach var="i" begin="1" end="${emptyCnt}">
			<tr class="tr_body">
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</c:forEach>
	</c:if>
</table>

<table width="770" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="white" height="30">
		<td align="center" colspan="10">${pageUtil.pageHtml}</td>
	</tr>
</table>

<div style="width:0px;height:0px;display:none;">
<iframe id="iFrmExcel" name="iFrmExcel" style="width:0px;height:0px;"></iframe>
</div>

</body>
</html>
