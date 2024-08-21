<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.27
	*	설명 : 메일 수정 처리 후 화면
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
			$("#searchForm", parent.document).attr("target","").attr("action","./taskUpdateP.ums").submit();
		<%--
		</c:if>
		--%>
	</c:when>
	<c:when test="${result eq 'filter'}">
		alert("사용 할수 없는 스크립트가 포함되어 있습니다");
	</c:when>
	<c:otherwise>
		<c:if test="${FILE_SIZE eq 'EXCESS' }">
			alert("파일 크기가 제한용량을 초과하였습니다.");
		</c:if>
		<c:if test="${FILE_SIZE ne 'EXCESS' }">
			alert("수정 처리중 오류가 발생하였습니다.");
		</c:if>
	</c:otherwise>
</c:choose>
</script>
