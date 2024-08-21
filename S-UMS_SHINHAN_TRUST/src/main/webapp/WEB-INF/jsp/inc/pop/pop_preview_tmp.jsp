<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.21
	*	설명 : 템플릿 미리보기 팝업화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="popup_preview" class="poplayer poppreviewtemplate">
	<div class="inner medium">
		<header>
			<h2>템플릿 미리보기</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<div class="graybox">
					<div class="table-area">
						<table>
							<caption></caption>
							<colgroup>
								<col style="width:110px">
								<col style="width:auto">
							</colgroup>
							<tbody>
								<tr>
									<th scope="row">템플릿명</th>
									<td id="previewTitle"></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
	
				<!-- 미리보기 영역// -->
				<div id="previewContent" class="previewbox">
					<iframe id="iFrmWebAgent" name="iFrmWebAgent" frameborder="0" src="about:blank" style="border:0;margin:0;padding:0;width:800px;height:500px;"></iframe>
				</div>
				<!-- //미리보기 영역 -->
	
				<!-- 버튼// -->
				<div class="btn-wrap">
					<button type="button" class="btn big" onclick="fn.popupClose('#popup_preview');">닫기</button>
				</div>
				<!-- //버튼 -->
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_preview');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
