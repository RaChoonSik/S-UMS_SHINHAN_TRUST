<%--
	/**********************************************************
	*	작성자 : 이혜민
	*	작성일시 : 2022.07.04
	*	설명 : SMS 템플릿 수정 처리 후 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<script type="text/javascript" src="<c:url value='/js/jquery_2.1.1.min.js'/>"></script>
<script type="text/javascript">
<c:choose>
	<c:when test="${result eq 'Success'}">
		alert("수정되었습니다.");
			window.parent.location.href = "<c:url value='/ems/seg/smsTemplateListP.ums'/>";
	</c:when>
	<c:when test="${result eq 'Fail'}">
		alert("수정 처리중 오류가 발생하였습니다");
	</c:when>
</c:choose>
</script>
