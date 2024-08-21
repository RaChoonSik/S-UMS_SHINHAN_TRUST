<%--
	/**********************************************************
	*	작성자 : 박찬용
	*	작성일시 : 2022.02.11
	*	수정설명 : 승인 팝업
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>


<!-- 팝업// -->
<div id="popup_confirm_approval" class="poplayer popreturnreason"><!-- id값 수정 가능 -->
	<div class="inner">
		<header>
			<h2>승인 및 기타의견 등록</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<div class="graybox">

					<div class="table-area">
						<table class="grid">
							<caption>승인 및 기타의견 등록</caption>
							<colgroup>
								<col style="width:110px">
								<col style="width:auto">
							</colgroup>
							<tbody>
								<tr>
									<th scope="row">결재유형</th>
									<td id="rejectView">일반 결재 or 준법심의 결재</td>
								</tr>
								<tr>
									<th scope="row">기타의견</th>
									<td>
										<textarea placeholder="의견을 등록해주세요." id="rejectConfirmDesc" name="rejectConfirmDesc"></textarea>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>

				<!-- 버튼// -->
				<div class="btn-wrap">
					<button type="button" class="btn big fullblue" onclick="goApprStepConfirm()">승인</button>
				</div>
				<!-- //버튼 -->
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_confirm_approval');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background" onclick="fn.popupClose('#popup_confirm_approval');"></span>
</div>
<!-- //팝업 -->