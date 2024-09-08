<%--
	/**********************************************************
	*	작성자 : 이혜민
	*	작성일시 : 2022.06.20
	*	설명 : sms 상세 로그 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="pop_detail_sms" class="poplayer poptestsendinfo">
	<div class="inner medium">
		<header>
			<h2>SMS 상세 내용</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<form id="smsDetailForm" name="smsDetailForm" method="post">
					<input type="hidden" id="popMsgid"          name="msgid" value=""/>
					<input type="hidden" id="popCmid"           name="cmid" value=""/>
					<input type="hidden" id="popAttachFileList" name="attachPath" value=""/>
					<fieldset>
						<legend>상세 정보</legend>
						<div class="graybox" style="margin-top:0;">
							<div class="title-area">
								<h3 class="h3-title">SMS상세 정보</h3>
								<!-- btn-wrap// -->
								<div class="btn-wrap">
<!-- 									<button type="button" class="btn" style="margin-left:10px;" id="btnSmsPreview" onclick="getSmsMsg(msgid,keygen);">미리보기</button> -->
								</div>
								<!-- //btn-wrap -->
							</div>
							
							<div class="table-area">
								<table>
									<caption>SMS상세 정보</caption>
									<colgroup>
										<col style="width:105px">
										<col style="width:225px">
										<!-- <col style="width:105px">
										<col style="width:225px"> --> 
									</colgroup>
									<tbody>
										<tr>
											<th scope="row">keygen</th>
											<td id="popKeygen"></td>
										</tr>
										<tr>
											<th scope="row">발송내용</th>
											<td id="" style="height:130px">
											<textarea id="popMsgBody" name="popMsgBody"></textarea>
											</td>
										</tr>
										<tr>
											<th scope="row">발송결과</th>
											<td id="popCallStatusNm" style="height:80px"></td> 
										</tr>
<!-- 										<tr> -->
<!-- 											<th scope="row">발송유형</th> -->
<!-- 											<td id="popSendGubunNm"></td>  -->
<!-- 										</tr> -->
<!-- 										<tr> -->
<!-- 											<th scope="row">메세지 제목</th> -->
<!-- 											<td id="popSmsTitle"></td> -->
<!-- 										</tr> -->
<!-- 										<tr> -->
<!-- 											<th scope="row">고객ID</th> -->
<!-- 											<td id="popCustId"></td> -->
<!-- 										</tr> -->
<!-- 										<tr> -->
<!-- 											<th scope="row">고객명</th> -->
<!-- 											<td id="popCustNm"></td> -->
<!-- 										</tr> -->
<!-- 										<tr> -->
<!-- 											<th scope="row" >고객 전화번호</th> -->
<!-- 											<td id="popCustPhone"></td> -->
<!-- 										</tr> -->
<!-- 										<tr> -->
<!-- 											<th scope="row" >발송결과</th> -->
<!-- 											<td id="popRsltCd"></td> -->
<!-- 										</tr> -->
									</tbody>
								</table>
							</div>
						</div>
						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullgreen" onclick="fn.popupClose('#pop_detail_sms');">닫기</button>
						</div>
						<!-- //btn-wrap -->
						
					</fieldset>
				</form>
				</div>
			</div>
			<button type="button" class="btn_popclose" onclick="fn.popupClose('#pop_detail_sms');"><span class="hidden">팝업닫기</span></button>
		</div>
		<span class="poplayer-background" onclick="fn.popupClose('#pop_detail_sms');"></span>
	</div>
	<!-- //팝업 -->
</body>
</html>