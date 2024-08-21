<%--
	/**********************************************************
	*	작성자 : 김재환
	*	작성일시 : 2021.12.08
	*	설명 : 카카오 알림톡 발송 등록 처리 후 화면
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
		$("#kakaoInfoForm", parent.document).attr("target","").attr("action","./kakaoUpdateP.ums").submit();
	</c:when>
	<c:otherwise>
		alert("등록에 실패 하였습니다.");
	</c:otherwise>
</c:choose>
</script>
