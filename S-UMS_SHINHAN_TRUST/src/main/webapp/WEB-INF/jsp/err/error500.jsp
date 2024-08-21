<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.07.12
	*	설명 : 500 에러 페이지
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header.jsp" %>
 
<body>
	<div id="wrap">

		<div id="inaccessible">

			<section class="inaccess-inner">
				<h1><img src="../../img/common/thumb_access.png" class="accesskey" alt="접근권한"></h1>
				<h2>페이지의 상태가 원활하지 않습니다.</h2>
				<p>죄송합니다. 현재 페이지에 오류가 있어 페이지를 표시할 수 없습니다.<br>시스템 관리자에게 문의바랍니다.</p>			
				<a href="<c:out value='/service.ums'/>" class="btn fullblue big">전체 서비스로 돌아가기</a>
			</section>

		</div>
		<!-- // inaccessible -->

	</div>

</body> 
