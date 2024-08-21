<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.07.28
	*	설명 : 수신거부 팝업 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header.jsp" %>

<c:set var="EMAIL" value=""/>
<c:set var="NAME" value=""/>
<c:set var="ID" value=""/>
<c:if test="${fn:length(mergeList) > 0}">
	<c:forEach items="${mergeList}" var="merge" varStatus="mergeStatus">
		<c:if test="${mergeStatus.index == 0}">
			<c:set var="EMAIL" value="${merge.cdNm}"/>
		</c:if>
		<c:if test="${mergeStatus.index == 1}">
			<c:set var="NAME" value="${merge.cdNm}"/>
		</c:if>
		<c:if test="${mergeStatus.index == 2}">
			<c:set var="ID" value="${merge.cdNm}"/>
		</c:if>
	</c:forEach>
</c:if>

<script type='text/javascript'>
function goAdd() {
	if( $("#rejectUrl").val() == "") {
		alert("<spring:message code='CAMJSALT024'/>");	// 수신거부 URL을 입력하세요!!
		return;
	}
	
	var tempStr = "";
	if($("input[name='rejectTy']").eq(0).is(":checked")) {
		tempStr = "<a href='<c:out value='${RES_REJECT_URL}'/>?$:RESP_END_DT:$|001|$:<c:out value='${ID}'/>:$|$:TASK_NO:$|$:SUB_TASK_NO:$|$:DEPT_NO:$|$:USER_ID:$|$:CAMP_TY:$|$:CAMP_NO:$|"+$("#redirectUrl").val()+"'>"+$("#rejectUrl").val()+"</a>";
	} else {
		tempStr = "<a href='<c:out value='${RES_REJECT_URL}'/>?$:RESP_END_DT:$|001|$:<c:out value='${ID}'/>:$|$:TASK_NO:$|$:SUB_TASK_NO:$|$:DEPT_NO:$|$:USER_ID:$|$:CAMP_TY:$|$:CAMP_NO:$|"+$("#redirectUrl").val()+"'><img src='"+$("#rejectUrl").val()+"' border=0></a>";
	}
	
	opener.setReject(tempStr);
	window.close();
}

//수신 거부 타이틀 설정
function setTitle(str) {
	$("#nm").html(str);
}
-->
</script>

<form id="rejectForm" name="rejectForm" method="post">
<table width="394" border="0" cellspacing="1" cellpadding="0" class="table_line_outline">
	<tr>
		<td class="td_line" colspan="2"></td>
	</tr>
	<tr height="20" class="tr_head">
		<td colspan='2' align="left" class="td_d"><spring:message code='CAMTBLTL041'/></td><!-- 수신거부 설정 -->
	</tr>
	<tr height="20" >
		<td width='30%' class="td_title"><spring:message code='CAMTBLTL042'/></td><!-- 수신거부 유형 -->
		<td width='70%' class="td_body">
			<input type="radio" name="rejectTy" value="1" checked onClick="setTitle('<spring:message code='CAMTBLTL047'/>')"> <spring:message code='CAMTBLLB018'/><!-- 수신거부 문구 --><!-- 문자 -->
			<input type="radio" name="rejectTy" value="2" onClick="setTitle('IMAGE URL')"> <spring:message code='CAMTBLLB019'/><!-- 이미지 -->
		</td>
	</tr>
	<tr height="20">
		<td class="td_title" id="nm"><spring:message code='CAMTBLTL047'/></td><!-- 수신거부 문구 -->
		<td class="td_body"><input type='text' id="rejectUrl" name='rejectUrl' size="40"></td>
	</tr>
	<tr height="20">
		<td class="td_title"><spring:message code='CAMTBLTL043'/></td><!-- 수신거부 URL -->
		<td class="td_body"><input type='text' id="redirectUrl" name='redirectUrl' size="40"></td>
	</tr>
</table>

<table width="394" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td height='30' align="right">
			<input type="button" class="btn_style" value="<spring:message code='COMBTN005'/>" onClick="goAdd()"><!-- 등록 -->
			<input type="button" class="btn_style" value="<spring:message code='COMBTN001'/>" onClick="window.close()"><!-- 닫기 -->
		</td>
	</tr>
</table>
</form>

</body>
</html>
