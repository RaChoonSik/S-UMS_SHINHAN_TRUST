<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.07.29
	*	설명 : 메일 등록 처리 후 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<script type="text/javascript" src="<c:url value='/js/jquery_2.1.1.min.js'/>"></script>
<script type="text/javascript">
<c:choose>
	<c:when test="${result eq 'Success'}">
		alert("등록되었습니다.");
		<%--
		<c:if test="${'000' eq sendRepeat}">
			$("#taskNo", parent.document).val("<c:out value='${taskNo}'/>");
			$("#subTaskNo", parent.document).val("<c:out value='${subTaskNo}'/>");
			$("#campNo", parent.document).val("0");
			$("#mailInfoForm", parent.document).attr("target","").attr("action","./mailUpdateP.ums").submit();;
			//window.parent.location.href = "<c:url value='/ems/cam/mailListP.ums'/>";
		</c:if>
		<c:if test="${'001' eq sendRepeat}">
		--%>
		<%--
			$("#taskNo", parent.document).val("<c:out value='${taskNo}'/>");
			$("#subTaskNo", parent.document).val("<c:out value='${subTaskNo}'/>");
			$("#campNo", parent.document).val("0");
			$("#mailInfoForm", parent.document).attr("target","").attr("action","./taskUpdateP.ums").submit();;
		--%>
			window.parent.location.href = "<c:url value='/ems/cam/taskListP.ums'/>";
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
			alert("등록 처리중 오류가 발생하였습니다.");
		</c:if>
	</c:otherwise>
</c:choose>
</script>
