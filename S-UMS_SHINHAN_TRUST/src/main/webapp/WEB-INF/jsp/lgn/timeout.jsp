<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.05
	*	설명 : 세션타임아웃 처리
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<script type="text/javascript">
//alert("Session Timeout");
if(opener) {
	opener.document.location.href = "<c:url value='/lgn/lgnP.ums'/>";
	self.close();
} else {
	window.top.document.location.href = "<c:url value='/lgn/lgnP.ums'/>";
}
</script>