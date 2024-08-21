<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.10.19
	*	설명 : 문자발송 등록 처리 후 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<script type="text/javascript" src="<c:url value='/js/jquery_2.1.1.min.js'/>"></script>
<script type="text/javascript">
<c:choose>
	<c:when test="${result eq 'Success'}">
		alert("등록되었습니다.");
		$("#msgid", parent.document).val("<c:out value='${msgid}'/>");
		$("#keygen", parent.document).val("<c:out value='${keygen}'/>");
		$("#campNo", parent.document).val("<c:out value='${campNo}'/>");
		$("#smsInfoForm", parent.document).attr("target","").attr("action","./smsUpdateP.ums").submit();;
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
