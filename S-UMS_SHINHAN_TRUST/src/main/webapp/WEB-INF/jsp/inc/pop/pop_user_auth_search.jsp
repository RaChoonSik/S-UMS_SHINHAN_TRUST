<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.28
	*	설명 : 사용자 메뉴 권한 팝업화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_user_auth_search" class="poplayer popaccessiblemenu">
	<div class="inner" style="max-width:454px;">
		<header>
			<h2>접근메뉴</h2>
		</header>
		<div class="popcont layertoggle">
			<div class="cont">
				<!--메뉴목록 토글// -->
				<div class="adminauthority" >
					<div class="graybox">
						<div class="content-area">
								<ul id="ulUserAuthList" class="user">
									<li class="clear"><!-- 활성화 : li class="open" -->
										 
									</li> 
								</ul>
						</div>
					</div>
				</div>
				<!-- //그룹목록 --> 
				<!-- 버튼// -->
				<div class="btn-wrap">
					<button type="button" class="btn big fullred" onclick="fn.popupClose('#popup_user_auth_search');">확인</button> 
				</div>
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_user_auth_search');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>	
</div>
