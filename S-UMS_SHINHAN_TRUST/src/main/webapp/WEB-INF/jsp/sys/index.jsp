<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.05
	*	설명 : SYS 메인 화면(기본 실행 프로그램으로 이동)
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<form name="menuForm" action="<c:out value='${menuInfo.sourcePath}'/>" method="post">
<input type="hidden" name="pMenuId" value="<c:out value='${menuInfo.parentmenuId}'/>"/>
<input type="hidden" name="menuId" value="<c:out value='${menuInfo.menuId}'/>"/>
</form>
<script type="text/javascript">
document.menuForm.submit();
</script>