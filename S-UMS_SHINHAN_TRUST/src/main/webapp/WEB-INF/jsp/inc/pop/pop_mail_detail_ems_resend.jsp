<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.01.19
	*	설명 : 캠페인별메일발송 > 메일분석 > 재발송이메일상세 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="popup_mail_detail_ems_resend" class="poplayer poptestsendinfo">
	<div class="inner medium">
		<header>
			<h2>재발송 메일 상세 정보</h2>
		</header>
		<div class="popcont">
			<div class="cont" style="padding-bottom:26px;">
				<form id="mailDetailForm" name="mailDetailForm" method="post">
					<input type="hidden" id="popServiceGb" value="10">
					<input type="hidden" id="popResendEnable" value="">
					<input type="hidden" id="popWebAgent" value="">
					<input type="hidden" id="popTaskNo" name="taskNo" value="0">
					<input type="hidden" id="popSubTaskNo" name="subTaskNo" value="0">
					<input type="hidden" id="popSegRetry" name="segRetry" value="0">
					<input type="hidden" id="popRtyCode" name="rtyCode" value="">
					<input type="hidden" name="rtyTyp" value="001">
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
										<col style="width:105px;">
										<col style="width:280px;">
										<col style="width:105px;">
										<col style="width:auto;">
									</colgroup>
									<tbody>
										<tr>
											<th scope="row">메일명</th>
											<td colspan="3" id="popMailTitle"></td>
										</tr>
										<tr>
											<th scope="row">발송자명</th>
											<td id="popSendMailFromNm"></td>
											<th scope="row">발송자이메일</th>
											<td id="popSendMailFromEm"></td>
										</tr>
										<tr>
											<th scope="row">첨부파일</th>
											<td id="popAttCnt" >(파일 개수 : 0개)</td>
											<th scope="row">캠페인명</th>
											<td id="popCampNm"></td>
										</tr>
										<tr>
											<th scope="row">보안메일</th>
											<td id="popWebAgentDesc">
												첨부파일 형식이 지정되었습니다.(HTML)
											</td>
											<td colspan="2">
												<button type="button" class="btn" style="margin-left:10px;" id="btnPopWebAgentPreview" onclick="popWebAgentPreview();">미리보기</button>
											</td>
										</tr>
									</tbody>
								</table>

								<table><!-- 2022.01.20 이제하 추가 -->
									<caption>재발송 체크박스</caption>
									<colgroup>
										<col style="width:auto;">
										<col style="width:21.7%;">
										<col style="width:21.7%;">
										<col style="width:21.7%;">
										<col style="width:21.7%;">
									</colgroup>
									<tbody>
											<tr>
												<th scope="row" rowspan="4">재발송조건</th><!-- rowspan 값으로 행 개수를 조절합니다. -->
												<td><label for="chk_01"><input type="checkbox" id="chk_01" name="popRcode" value="000" ><span>성공</span></label></td>
												<td><label for="chk_02"><input type="checkbox" id="chk_02" name="popRcode" value="001" checked><span>생성에러</span></label></td>
												<td><label for="chk_03"><input type="checkbox" id="chk_03" name="popRcode" value="002" checked><span>문법오류</span></label></td>
												<td><label for="chk_04"><input type="checkbox" id="chk_04" name="popRcode" value="003" checked><span>도메인없음</span></label></td>
											</tr>
											<tr>
												<td><label for="chk_05"><input type="checkbox" id="chk_05" name="popRcode" value="004" checked><span>S/W 네트워크에러</span></label></td>
												<td><label for="chk_06"><input type="checkbox" id="chk_06" name="popRcode" value="005" checked><span>H/W 네트워크에러</span></label></td>
												<td><label for="chk_07"><input type="checkbox" id="chk_07" name="popRcode" value="006" checked><span>트랜잭션에러</span></label></td>
												<td><label for="chk_08"><input type="checkbox" id="chk_08" name="popRcode" value="007" checked><span>스팸차단</span></label></td>
											</tr>
											<tr>
												<td><label for="chk_09"><input type="checkbox" id="chk_09" name="popRcode" value="008" checked><span>메일박스용량부족</span></label></td>
												<td><label for="chk_10"><input type="checkbox" id="chk_10" name="popRcode" value="009" checked><span>계정없음</span></label></td>
												<td><label for="chk_11"><input type="checkbox" id="chk_11" name="popRcode" value="010" checked><span>보안메일 생성오류</span></label></td>
												<td><label for="chk_12"><input type="checkbox" id="chk_12" name="popRcode" value="011" checked><span>고객정보포함</span></label></td>
											</tr>
											<tr>
												<td><label for="chk_13"><input type="checkbox" id="chk_13" name="popRcode" value="012" checked><span>고객정보체크오류</span></label></td>
												<td></td>
												<td></td>
												<td></td>
											</tr>
									</tbody>
								</table>
							</div>
						</div>
						<!-- 상세정보// -->
						
						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullblue" id="btnPopReSend" onclick="mailReSend();">발송</button>
						</div>
						<!-- 미리보기 영역// -->
						<div id="popContents" class="previewbox mail-reset" style="margin-top:30px;"></div>
						<!-- //미리보기 영역 -->
						
						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullblue" onclick="fn.popupClose('#popup_mail_detail_ems_resend');">닫기</button>
						</div>
						<!-- //btn-wrap -->
						
					</fieldset>
				</form>
				</div>
			</div>
			<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_mail_detail_ems_resend');"><span class="hidden">팝업닫기</span></button>
		</div>
		<span class="poplayer-background" onclick="fn.popupClose('#popup_mail_detail_ems_resend');"></span>
	</div>
	<!-- //팝업 -->
</body>
</html>