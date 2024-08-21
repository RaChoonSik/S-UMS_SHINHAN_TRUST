<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.04
	*	설명 : 발송테스트로그 조회
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header.jsp" %>

<script type="text/javascript">
//리스트로 이동
function goList() {
	$("#searchForm").attr("target","").attr("action","<c:url value='/ems/cam/mailTestTaskP.ums'/>").submit();
}
</script>

<form id="searchForm" name="searchForm" method="post">
<input type="hidden" name="sendTestTaskNo" value="<c:out value='${taskVO.sendTestTaskNo}'/>">
<input type="hidden" name="sendTestSubTaskNo" value="<c:out value='${taskVO.sendTestSubTaskNo}'/>">
<input type='hidden' name='taskNo' value='<c:out value='${taskVO.taskNo}'/>'>
<input type='hidden' name='subTaskNo' value='<c:out value='${taskVO.subTaskNo}'/>'>
</form>
<table width="350" border="0" cellspacing="1" cellpadding="0" class="table_line_outline">
    <tr>
        <td class="td_line" colspan="5"></td>
    </tr>
    <tr class="tr_head" align="center">
        <td width='60%'>이메일</td>
        <td width='40%'>발송결과</td>
    </tr>
    <c:if test="${fn:length(logList) > 0}">
    	<c:forEach items="${logList}" var="log">
		    <tr class="tr_body" height="20">
		        <td align="center"><c:out value="${log.custEm}"/></td>
		        <td><c:out value='${log.sendRcodeDtl}'/></td>
		    </tr>
    	</c:forEach>
	</c:if>
</table>

<table width="350" border="0" cellspacing="0" cellpadding="0">
    <tr><td height=5></td></tr>
    <tr>
        <td align=right>
        	<input type="button" value="<spring:message code='COMBTN010'/>" onClick="goList()" class="btn_style"><!-- 리스트 -->
            <input type="button" class="btn_style" value="<spring:message code='COMBTN001'/>" onClick="window.close()"><!-- 닫기 -->
        </td>
    </tr>
</table>

</body>
</html>
