<%--
	/**********************************************************
	*	작성자 : 김재환
	*	작성일시 : 2021.12.10
	*	설명 : 알림톡 템플릿 미리보기 팝업화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="popup_kakao_previewtemplate" class="poplayer poppreviewtemplate">
	<div class="inner small">
		<header>
			<h2>알림톡 미리보기</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<div class="graybox">
						<div class="table-area">
							<table>
								<caption>알림톡 미리보기</caption>
								<colgroup>
									<col style="width:110px;">
									<col style="width:auto;">
								</colgroup>
								<tbody>
									<tr>
										<th scope="row">템플릿명</th>
										<td><span id="popupTemplateNm"></span></td>
									</tr>
								</tbody>
							</table>
						</div>
				</div>
	
				<!-- 미리보기 영역// -->
				<div id="previewContent" class="previewbox"></div>
				<!-- //미리보기 영역 -->
	
				<p class="infotxt">※ 줄바꿈은 해상도에 따라 상이 할 수 있습니다.</p>
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_kakao_previewtemplate');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background" onclick="fn.popupClose('#popup_kakao_previewtemplate');"></span>
</div>
