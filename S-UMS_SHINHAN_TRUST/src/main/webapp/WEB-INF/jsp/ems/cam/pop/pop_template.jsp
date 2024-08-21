<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.10.14
	*	설명 : 메일작성 내부 템플릿 관리
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 팝업// -->
<div id="popup_template" class="poplayer poptestsendinfo">
	<div class="inner">
		<header>
			<h2>템플릿 선택</h2>
		</header>
		<div class="popcont" id="divPopContTemp">
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_template');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
<!-- //팝업 -->
