<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.10.21
	*	설명 : 웹에이전트 미리보기 팝업화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="popup_preview_url" class="poplayer poppreviewlog">
		<div class="inner">
			<header>
				<h2>이동경로 미리보기</h2>
			</header>
			<div class="popcont">
				<div class="cont">

					<!-- tab// -->
					<div class="tab">
						<div class="tab-menu col2">
							<a href="javascript:;" class="active">Android</a>
							<a href="javascript:;">iOS</a>
						</div>
						<div class="tab-cont">
							<div class="active">
								<div class="graybox">
									<div class="title-area">
										<h3>조회</h3>
									</div>

									<div class="table-area">
										<table>
											<caption>이동경로 미리보기</caption>
											<colgroup>
												<col style="width:110px">
												<col style="width:auto">
											</colgroup>
											<tbody>
												<tr>
													<th scope="row">경로유형</th>
													<td id="previewUrlTypeAnd"></td>
												</tr>
												<tr>
													<th scope="row">URL</th>
													<td id="previewUrlAnd"></td>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
								
								<!-- 미리보기 영역// -->
								<div id="previewContentAnd" class="previewbox" style="overflow:auto;">
									<iframe id="iFrmUrlAnd" name="iFrmUrlAnd" frameborder="0" src="about:blank" style="border:0;margin:0;padding:0;width:100%;height:500px;"></iframe>
								</div>
								<!-- //미리보기 영역 -->
							</div>

							<div>
								<div class="graybox">
									<div class="title-area">
										<h3>조회</h3>
									</div>
			
									<div class="table-area">
										<table>
											<caption>이동경로 미리보기</caption>
											<colgroup>
												<col style="width:110px">
												<col style="width:auto">
											</colgroup>
											<tbody>
												<tr>
													<th scope="row">경로유형</th>
													<td id="previewUrlTypeIos"></td>
												</tr>
												<tr>
													<th scope="row">URL</th>
													<td id="previewUrlIos"></td>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
				
								<!-- 미리보기 영역// -->
								<div id="previewContentIos" class="previewbox" style="overflow:auto;">
									<iframe id="iFrmUrlIos" name="iFrmUrlIos" frameborder="0" src="about:blank" style="border:0;margin:0;padding:0;width:100%;height:500px;"></iframe>
								</div>
								<!-- //미리보기 영역 -->
							</div>
						</div>
					</div>
					<!-- //tab -->
				</div>
			</div>
			<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_preview_url');"><span class="hidden">팝업닫기</span></button>
		</div>
		<span class="poplayer-background"></span>
	</div>
