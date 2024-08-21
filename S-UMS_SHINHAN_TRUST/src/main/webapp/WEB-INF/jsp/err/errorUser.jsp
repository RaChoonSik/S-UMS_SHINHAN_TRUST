<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.07.12
	*	설명 : 404 에러 페이지
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header.jsp" %>
 
<body>
	<div id="wrap">

		<div id="inaccessible">

			<section class="inaccess-inner">
				<h1><img src="../../img/common/thumb_access.png" class="accesskey" alt="접근권한"></h1>
				<h2>시스템에 사용자가 등록되어 있지 않습니다.</h2>
				<p> 관리자에게 사용자 등록을 요청하세요.</p>				
				<a href="<c:out value='/index.jsp'/>" class="btn fullblue big">로그인 돌아가기</a>
			</section>

		</div>
		<!-- // inaccessible -->

	</div>

</body> 
