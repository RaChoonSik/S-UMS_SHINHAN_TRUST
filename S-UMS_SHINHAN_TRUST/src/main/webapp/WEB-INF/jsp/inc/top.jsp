<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.05
	*	설명 : 상단 메뉴 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<script type="text/javascript" src="<c:url value='/js/sys.js'/>"></script>
<script type="text/javascript">

function goService() {
	document.location.href = "<c:url value='/service.ums'/>";
}

function goEms() {
	document.location.href = "<c:url value='/ems/index.ums'/>";
}

function goRns() {
	document.location.href = "<c:url value='/rns/index.ums'/>";
}

function goSms() {
	document.location.href = "<c:url value='/sms/index.ums'/>";
}

function goPush() {
	document.location.href = "<c:url value='/push/index.ums'/>";
}

function goSys(useSys) { 
	if (useSys == "N"){
		alert("관리자 기능 사용 권한이 없습니다");
		return;
	} else {
		document.location.href = "<c:url value='/sys/index.ums'/>";
	}
}

function goLogout() {
	document.location.href = "<c:url value='/lgn/logout.ums'/>";
}

function goUserInfo(userId){	
	popUserInfo(userId);
}

</script>

<div class="util">
	<button type="button" class="btn-util item01">전체 서비스</button>
	<!-- 전체서비스 툴팁// -->
	<div class="service">
		<c:set var="uri" value="${pageContext.request.requestURI}" />
		<c:if test="${NEO_USE_EMS eq 'Y'}">
			<c:if test = "${!fn:contains(uri, '/ems/')}">
				<a href="javascript:goEms();">S-EMS</a>
			</c:if>
		</c:if>
		<c:if test="${NEO_USE_RNS eq 'Y'}">
			<c:if test = "${!fn:contains(uri, '/rns/')}">
				<a href="javascript:goRns();">S-RNS</a>
			</c:if>
		</c:if>
		<c:if test="${NEO_USE_SMS eq 'Y'}">
			<c:if test = "${!fn:contains(uri, '/sms/')}">
				<a href="javascript:goSms();">S-SMS</a>
			</c:if>
		</c:if>
		<c:if test="${NEO_USE_PUSH eq 'Y'}">
			<c:if test = "${!fn:contains(uri, '/push/')}">
				<a href="javascript:goPush();">S-PUSH</a>
			</c:if>
		</c:if>
		<a href="javascript:goService();">서비스(홈)</a>
	</div>
	<!-- //전체서비스 툴팁 -->
	<%-- <c:if test="${'M9901000' ne NEO_P_MENU_ID}"> --%>
	<c:if test="${fn:contains(NEO_P_MENU_ID, 'M99')}">
<%-- 	<a href="javascript:goMailApprLine('<c:out value='${NEO_USER_ID}'/>');" class="btn-util item02<c:if test='${apprMailCnt > 0}'> active</c:if>">발송결재 --%>
<%-- 		<c:choose> --%>
<%-- 			<c:when test="${apprMailCnt > 99}"> --%>
<!-- 				<span class="alertnum">99+</span> -->
<%-- 			</c:when> --%>
<%-- 			<c:when test="${apprMailCnt > 0}"> --%>
<%-- 				<span class="alertnum"><c:out value="${apprMailCnt}"/></span> --%>
<%-- 			</c:when> --%>
<%-- 		</c:choose> --%>
<!-- 	</a> -->
	</c:if>
	<c:if test="${fn:contains(NEO_P_MENU_ID, 'M10')}">
	<a href="javascript:goSys('${NEO_USE_SYS}');" class="btn-util item02">공통 설정</a>
<%-- 	<a href="javascript:goMailApprLine('<c:out value='${NEO_USER_ID}'/>');" class="btn-util item03<c:if test='${apprMailCnt > 0}'> active</c:if>">발송결재 --%>
<%-- 		<c:choose> --%>
<%-- 			<c:when test="${apprMailCnt > 99}"> --%>
<!-- 				<span class="alertnum">99+</span> -->
<%-- 			</c:when> --%>
<%-- 			<c:when test="${apprMailCnt > 0}"> --%>
<%-- 				<span class="alertnum"><c:out value="${apprMailCnt}"/></span> --%>
<%-- 			</c:when> --%>
<%-- 		</c:choose> --%>
<!-- 	</a> -->
	</c:if>
	<c:if test="${fn:contains(NEO_P_MENU_ID, 'M20')}">
	<a href="javascript:goSys('${NEO_USE_SYS}');" class="btn-util item02">공통 설정</a>
<%-- 	<a href="javascript:goMailApprLine('<c:out value='${NEO_USER_ID}'/>');" class="btn-util item03<c:if test='${apprMailCnt > 0}'> active</c:if>">발송결재 --%>
<%-- 		<c:choose> --%>
<%-- 			<c:when test="${apprMailCnt > 99}"> --%>
<!-- 				<span class="alertnum">99+</span> -->
<%-- 			</c:when> --%>
<%-- 			<c:when test="${apprMailCnt > 0}"> --%>
<%-- 				<span class="alertnum"><c:out value="${apprMailCnt}"/></span> --%>
<%-- 			</c:when> --%>
<%-- 		</c:choose> --%>
<!-- 	</a> -->
	</c:if>
	<c:if test="${fn:contains(NEO_P_MENU_ID, 'M30')}">
	<a href="javascript:goSys('${NEO_USE_SYS}');" class="btn-util item02">공통 설정</a>
<%-- 	<a href="javascript:goMailApprLine('<c:out value='${NEO_USER_ID}'/>');" class="btn-util item03<c:if test='${apprMailCnt > 0}'> active</c:if>">발송결재 --%>
<%-- 		<c:choose> --%>
<%-- 			<c:when test="${apprMailCnt > 99}"> --%>
<!-- 				<span class="alertnum">99+</span> -->
<%-- 			</c:when> --%>
<%-- 			<c:when test="${apprMailCnt > 0}"> --%>
<%-- 				<span class="alertnum"><c:out value="${apprMailCnt}"/></span> --%>
<%-- 			</c:when> --%>
<%-- 		</c:choose> --%>
<!-- 	</a>	 -->
	</c:if>
	<c:if test="${fn:contains(NEO_P_MENU_ID, 'M40')}">
	<a href="javascript:goSys('${NEO_USE_SYS}');" class="btn-util item02">공통 설정</a>
<%-- 	<a href="javascript:goMailApprLine('<c:out value='${NEO_USER_ID}'/>');" class="btn-util item03<c:if test='${apprMailCnt > 0}'> active</c:if>">발송결재 --%>
<%-- 		<c:choose> --%>
<%-- 			<c:when test="${apprMailCnt > 99}"> --%>
<!-- 				<span class="alertnum">99+</span> -->
<%-- 			</c:when> --%>
<%-- 			<c:when test="${apprMailCnt > 0}"> --%>
<%-- 				<span class="alertnum"><c:out value="${apprMailCnt}"/></span> --%>
<%-- 			</c:when> --%>
<%-- 		</c:choose> --%>
<!-- 	</a> -->
	</c:if>
	<div class="user" style="margin-top:0px">
		<div class="info">
			<em><c:out value='${NEO_USER_NM}'/></em>
			<span><c:out value='${NEO_DEPT_NM}'/></span>
			<button type="button">열기</button>
		</div>
		<div class="link">
			<a href="javascript:goUserUpdate();">개인 정보수정</a>
			<a href="javascript:goLogout();">로그아웃</a>
		</div>
	</div>
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_user_info.jsp" %> 
</div>
