<%--
	/**********************************************************
	*	작성자 : 이혜민
	*	작성일시 : 2022.06.28
	*	설명 : SMS 템플릿 미리보기 팝업화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="popup_sms_previewtemplate" class="poplayer poppreviewtemplate">
	<div class="inner small">
		<header>
			<h2>템플릿 미리보기</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<div class="graybox">
						<div class="table-area">
							<table>
								<caption>템플릿 미리보기</caption>
								<colgroup>
									<col style="width:110px;">
									<col style="width:auto;">
								</colgroup>
								<tbody>
									<tr>
										<th scope="row">제목</th>
										<td><span id="popupTemplateNm"></span></td>
									</tr>
								</tbody>
							</table>
						</div>
				</div>
	
				<!-- 미리보기 영역// -->
				<div id="previewContent" class="previewbox" style="width:100%;background:none;border:1px solid #dedede;"></div>
				<!-- //미리보기 영역 -->
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_sms_previewtemplate');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background" onclick="fn.popupClose('#popup_sms_previewtemplate');"></span>
</div>
