<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.02.13
	*	설명 : 메일 일자 정보 수정 처리 후 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<script type="text/javascript" src="<c:url value='/js/jquery_2.1.1.min.js'/>"></script>
<script type="text/javascript">
var campNo = $("#searchForm input[name='campNo']", parent.document).val();
<c:choose>
	<c:when test="${result eq 'Success'}">
		alert("수정되었습니다.");
		<%--
		<c:if test="${'000' eq sendRepeat}">
			/*
			if(campNo == "0") {
				$("#searchForm", parent.document).attr("target","").attr("action","./mailListP.ums").submit();
			} else {
				$("#searchForm", parent.document).attr("target","").attr("action","./campMailListP.ums").submit();
			}
			*/
			$("#searchForm", parent.document).attr("target","").attr("action","./mailUpdateP.ums").submit();
		</c:if>
		<c:if test="${'001' eq sendRepeat}">
			/*
			if(campNo == "0") {
				$("#searchForm", parent.document).attr("target","").attr("action","./taskListP.ums").submit();
			} else {
				$("#searchForm", parent.document).attr("target","").attr("action","./campMailListP.ums").submit();
			}
			*/
		--%>
			$("#searchForm", parent.document).attr("target","").attr("action","./taskUpdateDateP.ums").submit();
		<%--
		</c:if>
		--%>
	</c:when>
	<c:otherwise>
		alert("수정에 실패하였습니다.");
	</c:otherwise>
</c:choose>
</script>
