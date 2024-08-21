<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.04.01
	*	설명 : SMS 문자발송 미리보기 팝업화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 팝업// -->
<div id="popup_sms_preview" class="poplayer popsmssend"><!-- id값 수정 가능 -->
	<div class="inner small">
		<header>
			<h2>메시지발송 상세보기</h2>
		</header>
 		<div class="popcont">
			<div class="cont">
				<div class="phone">
					<div class="phone-preview" id="divPopPhonePreview">
					</div>
				</div>
			</div>
		</div> 
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_sms_preview');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background" onclick="fn.popupClose('#popup_sms_preview');"></span>
</div>
<!-- //팝업 -->