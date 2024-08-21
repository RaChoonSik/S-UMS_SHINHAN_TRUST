<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.25
	*	설명 : 화면 접근 권한 오류 페이지
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header.jsp" %>
 
<body>
	<div id="wrap">

		<div id="inaccessible">

			<section class="inaccess-inner">
				<h1><img src="../../img/common/thumb_access.png" class="accesskey" alt="접근권한"></h1>
				<h2>접근 권한이 없는 서비스입니다.</h2>
				<p>사용자 권한이 등록되지 않은 서비스입니다. <br>
					서비스 관리자에게 문의바랍니다.
				</p>
				<a href="<c:out value='/service.ums'/>" class="btn fullblue big">전체 서비스로 돌아가기</a>
			</section>

		</div>
		<!-- // inaccessible -->

	</div>

</body> 

