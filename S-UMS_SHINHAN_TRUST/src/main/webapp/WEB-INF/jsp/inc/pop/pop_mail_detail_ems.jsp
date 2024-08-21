<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.01.04
	*	설명 : 실시간 및 자동 메일 상세 정보  
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="popup_mail_detail_ems" class="poplayer poptestsendinfo">
	<div class="inner medium">
		<header>
			<h2>메일 상세 정보</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<form id="mailDetailForm" name="mailDetailForm" method="post">
					<input type="hidden" id="popServiceGb" value="">
					<input type="hidden" id="popResendEnable" value="">
					<input type="hidden" id="popWebAgent" value="">
					<input type="hidden" id="popTaskNo" name="taskNo" value="0">
					<input type="hidden" id="popSubTaskNo" name="subTaskNo" value="0">
					<input type="hidden" id="popCustIdd" name="custId" value="">
					<input type="hidden" id="popCustEm" name="custEm" value="">
					<input type="hidden" id="popBizkey" name="bizkey" value="">
					<input type="hidden" id="popTid" name="tid" value="0">
					<input type="hidden" id="popMid" name="mid" value="0">
					<input type="hidden" id="popRid" name="rid" value="">
					<input type="hidden" id="popSegReal" name="popSegReal" value="0">
					<input type="hidden" name="rtyTyp" value="002">
					<fieldset>
						<legend>상세 정보</legend>
						<div class="graybox" style="margin-top:0;">
							<div class="title-area">
								<h3 class="h3-title">메일상세 정보</h3>
							</div>
							
							<div class="table-area">
								<table>
									<caption>메일상세 정보</caption>
									<colgroup>
										<col style="width:105px">
										<col style="width:225px">
										<col style="width:105px">
										<col style="width:225px">
									</colgroup>
									<tbody>
										<tr>
											<th scope="row">발송일시</th>
											<td id="popSendDate"></td>
											<th scope="row">수신확인</th>
											<td id="popResponseLogNm"></td>
										</tr>
										<tr>
											<th scope="row">캠페인명</th>
											<td id="popCampNm"></td>
											<th scope="row">메일유형</th>
											<td id="popSendRepeatNm"></td> 
										</tr>
										<tr>
											<th scope="row">메일명</th>
											<td colspan="3" id="popMailTitle"></td>
										</tr>
										<tr>
											<th scope="row">고객ID</th>
											<td id="popCustId"></td>
											<th scope="row">고객명</th>
											<td id="popCustNm"></td>
										</tr>
										<tr>
											<th scope="row">고객 이메일</th>
											<td id="popCustEmail"></td>
											<th scope="row">첨부파일</th>
											<td colspan="3" id="popAttCnt" >(파일 개수 : 0개)</td>
										</tr>
										<tr>
											<th scope="row">보안메일</th>
											<td colspan="2" id="popWebAgentDesc"></td>
											<td>
												<button type="button" class="btn" style="margin-left:10px;" id="btnWebAgentPreview" onclick="popWebAgentPreview();">미리보기</button>
											</td>
										</tr>
										<tr>
											<th scope="row">발송결과</th>
											<td colspan="2" id="popResendEnabeDesc"></td>
											<td>
												<button type="button" class="btn fullblue" style="margin-left:10px;" id="btnReSend" onclick="mailReSend();">재발송</button>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						
						<!-- 미리보기 영역// -->
						<div id="popContents" class="previewbox mail-reset" style="height:200px;"></div>
						<!-- //미리보기 영역 -->
						
						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullblue" onclick="fn.popupClose('#popup_mail_detail_ems');">닫기</button>
						</div>
						<!-- //btn-wrap -->
						
					</fieldset>
				</form>
				</div>
			</div>
			<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_mail_detail_ems');"><span class="hidden">팝업닫기</span></button>
		</div>
		<span class="poplayer-background" onclick="fn.popupClose('#popup_mail_detail_ems');"></span>
	</div>
	<!-- //팝업 -->
</body>
</html>