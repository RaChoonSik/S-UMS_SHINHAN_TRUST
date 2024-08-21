<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.01.03
	*	설명 : 상세로그에서의 메일 재전송위 한 캠페인 선택
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 팝업// -->
<div id="popup_campaign_rns" class="poplayer poptestsendinfo">
	<div class="inner">
		<header>
			<h2>캠페인/서비스선택</h2>
		</header>
		<div class="popcont" id="divPopContCampRns">
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_campaign_rns');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
<!-- //팝업 -->
