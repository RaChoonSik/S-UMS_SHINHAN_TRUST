<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.01.19
	*	설명 : 통계분석 캠페인별메일현황 - 대용량 단/정기메일 통합 통계분석 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp" %>

<script type="text/javascript" src="<c:url value='/js/ems/ana/campMailAnalListP.js'/>"></script>

<body>
	<div id="wrap" class="ems">

		<!-- lnb// -->
		<div id="lnb">
			<!-- LEFT MENU -->
			<%@ include file="/WEB-INF/jsp/inc/menu_ems.jsp" %>
			<!-- LEFT MENU -->
		</div>
		<!-- //lnb -->

		<!-- content// -->
		<div id="content">

			<!-- cont-head// -->
			<section class="cont-head">
				<div class="title">
					<h2>메일 통계분석</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">

				<form id="searchForm" name="searchForm" method="post">
					<input type="hidden" name="page" value="<c:out value='${searchVO.page}'/>">
					<input type="hidden" name="searchCampNo" value="<c:out value='${searchVO.searchCampNo}'/>">
					<input type="hidden" name="searchCampNm" value="<c:out value='${searchVO.searchCampNm}'/>">
					<input type="hidden" name="searchStartDt" value="<c:out value='${searchVO.searchStartDt}'/>">
					<input type="hidden" name="searchEndDt" value="<c:out value='${searchVO.searchEndDt}'/>">
					<input type="hidden" name="searchSendRepeat" value="<c:out value='${searchVO.searchSendRepeat}'/>">
					<input type="hidden" name="searchTaskNm" value="<c:out value='${searchVO.searchTaskNm}'/>">
					<input type="hidden" name="searchWorkStatus" value="<c:out value='${searchVO.searchWorkStatus}'/>">
					<input type="hidden" name="searchDeptNo" value="<c:out value='${searchVO.searchDeptNo}'/>">
					<input type="hidden" name="searchUserId" value="<c:out value='${searchVO.searchUserId}'/>">
				</form>
				<form id="mailInfoForm" name="mailInfoForm" method="post">
					<input type="hidden" name="page" value="1">
					<input type="hidden" id="taskNo" name="taskNo" value="<c:out value='${mailSendLog.taskNo}'/>">
					<input type="hidden" id="subTaskNo" name="subTaskNo" value="<c:out value='${mailSendLog.subTaskNo}'/>">
					<input type="hidden" id="segRetry" value="<c:out value='${mailSendLog.segRetry}'/>">
					<input type="hidden" id="reSendAuth" value="<c:out value='${reSendAuth}'/>">
					<input type="hidden" id="mailTitle" value="<c:out value='${mailSendLog.mailTitle}'/>">
					<%-- <input type="hidden" id="mailFromNm" value="<c:out value='${mailSendLog.mailFromNm}'/>"> --%>
					<%-- <input type="hidden" id="mailFromEm" value="<c:out value='${mailSendLog.mailFromEm}'/>"> --%>
					<input type="hidden" id="attCnt" value="<c:out value='${mailSendLog.attCnt}'/>">
					<%-- <input type="hidden" id="campNm" value="<c:out value='${mailSendLog.campNm}'/>"> --%>
					<input type="hidden" id="webAgent" value="<c:out value='${mailSendLog.webAgent}'/>">
					<input type="hidden" id="webAgentTyp" value="<c:out value='${mailSendLog.webAgentTyp}'/>">
					<input type="hidden" id="contFlPath" value="<c:out value='${mailSendLog.contFlPath}'/>">
					<input type="hidden" id="segCreateTy" value="<c:out value='${mailSendLog.segCreateTy}'/>">
					<fieldset>
						<legend>메일분석 및 결과</legend>
					
						<!-- 메일분석// -->
						<div class="graybox">
							<div class="title-area">
								<h3 class="h3-title">메일분석</h3>
							</div>

							<div class="table-area">
								<table>
									<caption>메일분석</caption>
									<colgroup>
										<col style="width:15%">
										<col style="width:35%">
										<col style="width:15%">
										<col style="width:35%">
									</colgroup> 
									<tbody>
										<tr>
											<th>발송일시</th>
											<td>
												<fmt:parseDate var="sendDate" value="${mailSendLog.sendDt}" pattern="yyyyMMddHHmm"/>
												<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy.MM.dd HH:mm"/>
												<fmt:parseDate var="endDate" value="${mailSendLog.endDt}" pattern="yyyyMMddHHmm"/>
												<fmt:formatDate var="endDt" value="${endDate}" pattern="yyyy.MM.dd HH:mm"/>
												<c:out value='${sendDt}'/> ~ <c:out value='${endDt}'/>
											</td>
											<th>메일유형</th>
											<td><c:out value="${mailSendLog.sendRepeatNm}"/></td>
										</tr>
										<tr>
											<th>발송주기</th>
											<td>
												<c:if test="${'000' eq mailSendLog.sendRepeat}">
													 단기 1회
												</c:if>
												<c:if test="${'000' ne mailSendLog.sendRepeat}">
													<fmt:parseDate var="termStartDt" value="${mailSendLog.sendTermStartDt}" pattern="yyyyMMddHHmm"/>
													<fmt:formatDate var="startDt" value="${termStartDt}" pattern="yyyy.MM.dd"/>
													<fmt:parseDate var="termStartDtHour" value="${mailSendLog.sendTermStartDt}" pattern="yyyyMMddHHmm"/>
													<fmt:formatDate var="tmerHour" value="${termStartDtHour}" pattern="HH:mm"/>
													
													<c:if test="${'999912312359' eq mailSendLog.sendTermEndDt}">
														<c:out value='${startDt}'/> ~ 무기한/ <c:out value='${mailSendLog.sendTermLoop}'/> (<c:out value='${mailSendLog.sendTermLoopTyNm}'/>) / <c:out value='${tmerHour}'/>
													</c:if>
													<c:if test="${'999912312359' ne mailSendLog.sendTermEndDt}">
														<fmt:parseDate var="termEndDt" value="${mailSendLog.sendTermEndDt}" pattern="yyyyMMddHHmm"/>
														<fmt:formatDate var="endDt" value="${termEndDt}" pattern="yyyy.MM.dd"/>
														<c:out value='${startDt}'/> ~ <c:out value='${endDt}'/> / <c:out value='${mailSendLog.sendTermLoop}'/> (<c:out value='${mailSendLog.sendTermLoopTyNm}'/>) / <c:out value='${tmerHour}'/>
													</c:if>
												</c:if>
												

											</td>
											<th>캠페인명</th>
											<td id="campNm"><c:out value="${mailSendLog.campNm}"/></td>
										</tr>
										<tr>
											<th>메일명</th>
											<td ><c:out value="${mailSendLog.taskNm}"/></td>
											<th>수신자그룹</th>
											<td>
												<c:out value="${mailSendLog.segNm}"/>
												<c:if test="${'003' eq mailSendLog.segCreateTy}">
													(파일연동)
												</c:if>
											</td>
										</tr>
										<tr>
											<th>발송자명</th>
											<td id="mailFromNm"><c:out value="${mailSendLog.mailFromNm}"/></td>
											<th>발송자이메일</th>
											<td id="mailFromEm"><crypto:decrypt colNm="MAIL_FROM_EM" data="${mailSendLog.mailFromEm}"/></td>
										</tr>
										<tr>
											<th>등록일자</th>
											<fmt:parseDate var="mailTaskRegDt" value="${mailSendLog.taskRegDt}" pattern="yyyyMMddHHmmss"/>
											<fmt:formatDate var="taskRegDt" value="${mailTaskRegDt}" pattern="yyyy.MM.dd"/>
											<td><c:out value="${taskRegDt}"/></td>
											<th>발송결과</th>
											<td><c:out value="${mailSendLog.workStatusNm}"/>
												<c:if test="${0 ne mailSendLog.rtyNo}">
													(재발송)
												</c:if>
												<c:if test="${'Y' eq reSendAuth}">
													<c:if test="${'002' eq mailSendLog.segCreateTy}">
														<button type="button" class="btn fullblue" style="margin-left:20px;" onclick="goMailDetil();">재발송</button>
													</c:if>
												</c:if>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<!-- //메일분석 -->
						
						<!-- tab// -->
						<div class="tab">
							<div class="tab-menu col6">
								<a href="javascript:goStatTab(1);" class="active">결과요약</a>
								<a href="javascript:goStatTab(2);">세부에러</a>
								<a href="javascript:goStatTab(3);">도메인별</a>
								<a href="javascript:goStatTab(4);">발송시간별</a>
								<a href="javascript:goStatTab(5);">응답시간별</a>
								<a href="javascript:goStatTab(6);">고객별</a>
							</div>
							<div class="tab-cont">

								<!-- tab-cont : 결과요약// -->
								<div class="active" id="divTaskSummP"></div>
								<!-- //tab-cont : 결과요약 -->

								<!-- tab-cont : 세부에러// -->
								<div id="divTaskErrorP"></div>
								<!-- //tab-cont : 세부에러 -->

								<!-- tab-cont : 도메인별// -->
								<div id="divTaskDomainP"></div>
								<!-- //tab-cont : 도메인별 -->

								<!-- tab-cont : 발송시간별// -->
								<div id="divTaskSendP"></div>
								<!-- //tab-cont : 발송시간별 -->

								<!-- tab-cont : 응답시간별// -->
								<div id="divTaskRespP"></div>
								<!-- //tab-cont : 응답시간별 -->
								
								<!-- tab-cont : 고객별// -->
								<div id="divTaskCustP"></div>
								<!-- //tab-cont : 고객별 -->
							</div>
						</div>
						<!-- //tab -->

					</fieldset>
				</form>
				<div style="width:0px;height:0px;display:none;">
				<iframe id="iFrmExcel" name="iFrmExcel" style="width:0px;height:0px;"></iframe>
				</div>

			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>
	
	<!--  메일 정보 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_mail_detail_ems_resend.jsp" %>
	<!-- //자동발송 메일 정보 -->
	
	<!-- 웹에이전트미리보기 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_webagent.jsp" %>
	<!-- //웹에이전트미리보기 팝업 -->		
</body>
</html>
