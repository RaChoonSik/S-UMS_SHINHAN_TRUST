<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.04.05
	*	설명 : PUSH작성 내부 수신자그룹 관리
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 팝업// -->
<div id="popup_push_campaign" class="poplayer poptestsendinfo">
	<div class="inner">
		<header>
			<h2>캠페인 선택</h2>
		</header>
		<div class="popcont" id="divPopContCamp">
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_push_campaign');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
<!-- //팝업 -->
