<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.24
	*	설명 : 메일작성 환경설정 팝업
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>

<div id="popup_setting" class="poplayer popsetting">
	<div class="inner">
		<header>
			<h2>환경설정</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<div class="graybox">
					<div class="title-area">
						<h3 class="h3-title">메일헤더 정보</h3>
					</div>
					
					<div class="table-area">
						<table>
							<caption></caption>
							<colgroup>
								<col style="width:105px">
								<col style="width:225px">
								<col style="width:105px">
								<col style="width:225px">
							</colgroup>
							<tbody>
								<tr>
									<th scope="row">발송자명</th>
									<td>
										<c:if test="${not empty userInfo}">
											<input type="text" id="mailFromNm" name="mailFromNm" value="<c:out value='${userInfo.mailFromNm}'/>" placeholder="발송자명을 입력하세요.">
										</c:if>
										<c:if test="${not empty mailInfo}">
											<input type="text" id="mailFromNm" name="mailFromNm" value="<c:out value='${mailInfo.mailFromNm}'/>" placeholder="발송자명을 입력하세요.">
										</c:if>
									</td>
									<th scope="row">발송자 이메일</th>
									<td>
										<c:if test="${not empty userInfo}">
											<input type="text" id="mailFromEm" name="mailFromEm" value="<crypto:decrypt colNm='MAIL_FROM_EM' data='${userInfo.mailFromEm}'/>" placeholder="발송자 이메일을 입력하세요.">
										</c:if>
										<c:if test="${not empty mailInfo}">
											<input type="text" id="mailFromEm" name="mailFromEm" value="<crypto:decrypt colNm='MAIL_FROM_EM' data='${mailInfo.mailFromEm}'/>" placeholder="발송자 이메일을 입력하세요.">
										</c:if>
									</td>
								</tr>
								<tr>
									<th scope="row">반송 이메일</th>
									<td>
										<c:if test="${not empty userInfo}">
											<input type="text" id="replyToEm" name="replyToEm" value="<crypto:decrypt colNm='REPLY_TO_EM' data='${userInfo.replyToEm}'/>" placeholder="반송 이메일을 입력하세요.">
										</c:if>
										<c:if test="${not empty mailInfo}">
											<input type="text" id="replyToEm" name="replyToEm" value="<crypto:decrypt colNm='REPLY_TO_EM' data='${mailInfo.replyToEm}'/>" placeholder="반송 이메일을 입력하세요.">
										</c:if>
									</td>
									<th scope="row">리턴 이메일</th>
									<td>
										<c:if test="${not empty userInfo}">
											<input type="text" id="returnEm" name="returnEm" value="<crypto:decrypt colNm='RETURN_EM' data='${userInfo.returnEm}'/>" placeholder="리턴 이메일을 입력하세요.">
										</c:if>
										<c:if test="${not empty mailInfo}">
											<input type="text" id="returnEm" name="returnEm" value="<crypto:decrypt colNm='RETURN_EM' data='${mailInfo.returnEm}'/>" placeholder="리턴 이메일을 입력하세요.">
										</c:if>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>

				<div class="graybox">
					<div class="title-area">
						<h3 class="h3-title">인코딩 환경</h3>
					</div>

					<div class="table-area">
						<table>
							<caption>그리드 정보</caption>
							<colgroup>
								<col style="width:115px">
								<col style="width:225px">
								<col style="width:115px">
								<col style="width:auto">
							</colgroup>
							<tbody>
								<tr>
									<th scope="row">헤더 인코딩</th>
									<td>
										<c:if test="${empty mailInfo}">
											<p class="inline-txt line-height40"><c:out value='${MAIL_ENCODING_NM}'/></p>
											<input type="hidden" name="headerEnc" value="<c:out value='${MAIL_ENCODING}'/>">
										</c:if>
										<c:if test="${not empty mailInfo}">
											<p class="inline-txt line-height40"><c:out value='${mailInfo_headerEnc_nm}'/></p>
											<input type="hidden" name="headerEnc" value="<c:out value='${mailInfo.headerEnc}'/>">
										</c:if>
									</td>
									<th scope="row">바디 인코딩</th>
									<td>
										<c:if test="${empty mailInfo}">
											<p class="inline-txt line-height40"><c:out value='${MAIL_ENCODING_NM}'/></p>
											<input type="hidden" name="bodyEnc" value="<c:out value='${MAIL_ENCODING}'/>">
										</c:if>
										<c:if test="${not empty mailInfo}">
											<p class="inline-txt line-height40"><c:out value='${mailInfo_bodyEnc_nm}'/></p>
											<input type="hidden" name="bodyEnc" value="<c:out value='${mailInfo.bodyEnc}'/>">
										</c:if>
									</td>
								</tr>
								<tr>
									<th scope="row">문자셋</th>
									<td colspan="3">
										<c:if test="${not empty userInfo}">
											<p class="inline-txt line-height40"><c:out value='${userInfo_charset_nm}'/></p>
											<input type="hidden" name="charset" value="<c:out value='${userInfo.charset}'/>">
										</c:if>
										<c:if test="${not empty mailInfo}">
											<p class="inline-txt line-height40"><c:out value='${mailInfo_charset_nm}'/></p>
											<input type="hidden" name="charset" value="<c:out value='${mailInfo.charset}'/>">
										</c:if>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>

				<div class="graybox">
					<div class="title-area">
						<h3 class="h3-title">발송 환경</h3>
					</div>
					
					<div class="table-area">
						<table>
							<caption></caption>
							<colgroup>
								<col style="width:90px">
								<col style="width:205px">
								<col style="width:90px">
								<col style="width:205px">
							</colgroup>
							<tbody>
								<tr>
									<th scope="row">
										Socket<br>
										timeout
									</th>
									<td>
										<c:if test="${empty mailInfo}">
											<p class="inline-txt line-height40"><c:out value='${SOCKET_TIME_OUT}'/></p>
											<input type="hidden" name="socketTimeout" value="<c:out value='${SOCKET_TIME_OUT}'/>">
										</c:if>
										<c:if test="${not empty mailInfo}">
											<p class="inline-txt line-height40"><c:out value='${mailInfo.socketTimeout}'/></p>
											<input type="hidden" name="socketTimeout" value="<c:out value='${mailInfo.socketTimeout}'/>">
										</c:if>
									</td>
									<th scope="row">
										Connection당<br>
										발송 횟수
									</th>
									<td>
										<c:if test="${empty mailInfo}">
											<p class="inline-txt line-height40"><c:out value='${CONN_PER_CNT}'/></p>
											<input type="hidden" name="connPerCnt" value="<c:out value='${CONN_PER_CNT}'/>">
										</c:if>
										<c:if test="${not empty mailInfo}">
											<p class="inline-txt line-height40"><c:out value='${mailInfo.connPerCnt}'/></p>
											<input type="hidden" name="connPerCnt" value="<c:out value='${mailInfo.connPerCnt}'/>">
										</c:if>
									</td>
								</tr>
								<tr>
									<th scope="row">재발송 횟수</th>
									<td>
										<c:if test="${empty mailInfo}">
											<p class="inline-txt line-height40"><c:out value='${RETRY_CNT}'/></p>
											<input type="hidden" name="retryCnt" value="<c:out value='${RETRY_CNT}'/>">
										</c:if>
										<c:if test="${not empty mailInfo}">
											<p class="inline-txt line-height40"><c:out value='${mailInfo.retryCnt}'/></p>
											<input type="hidden" name="retryCnt" value="<c:out value='${mailInfo.retryCnt}'/>">
										</c:if>
									</td>
									<th scope="row">발송모드</th>
									<td>
										<div class="select" style="width:205px;">
											<select name="sendMode" title="발송모드 선택">
												<option value="000"<c:if test="${'000' eq mailInfo.sendMode}"> selected</c:if>>가상발송</option>
												<option value="001"<c:if test="${empty mailInfo || '001' eq mailInfo.sendMode}"> selected</c:if>>실발송</option>
											</select>
										</div>
									</td> 
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				
				<!-- btn-wrap// -->
				<div class="btn-wrap">
					<button type="button" class="btn big fullblue" onclick="popSettingSave();">등록</button>
					<button type="button" class="btn big" onclick="fn.popupClose('#popup_setting');">닫기</button>
				</div>
				<!-- //btn-wrap -->
				
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_setting');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
