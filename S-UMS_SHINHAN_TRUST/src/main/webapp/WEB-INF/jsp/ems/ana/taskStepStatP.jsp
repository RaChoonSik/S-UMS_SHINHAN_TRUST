<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.09.08
	*	설명 : 통계분석 정기메일 차수별 통계분석 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp" %>

<script type="text/javascript" src="<c:url value='/js/ems/ana/taskStepStatP.js'/>"></script>

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
					<h2>정기메일 차수별 통계분석</h2>
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
					<input type="hidden" name="taskNo" value="<c:out value='${searchVO.taskNo}'/>">
					<input type="hidden" name="subTaskNo" value="<c:out value='${searchVO.subTaskNo}'/>">
					<input type="hidden" name="searchStartDt" value="<c:out value='${searchVO.searchStartDt}'/>">
					<input type="hidden" name="searchEndDt" value="<c:out value='${searchVO.searchEndDt}'/>">
					<input type="hidden" name="searchCampNo" value="<c:out value='${searchVO.searchCampNo}'/>">
					<input type="hidden" name="searchTaskNm" value="<c:out value='${searchVO.searchTaskNm}'/>">
					<input type="hidden" name="searchSegNo" value="<c:out value='${searchVO.searchSegNo}'/>">
					<input type="hidden" name="searchDeptNo" value="<c:out value='${searchVO.searchDeptNo}'/>">
					<input type="hidden" name="searchUserId" value="<c:out value='${searchVO.searchUserId}'/>">
				</form>
				<form id="mailInfoForm" name="mailInfoForm" method="post">
					<input type="hidden" name="page" value="1">
					<input type="hidden" name="taskNo" value="<c:out value='${mailInfo.taskNo}'/>">
					<input type="hidden" name="subTaskNo" value="<c:out value='${mailInfo.subTaskNo}'/>">
					<input type="hidden" name="sendRepeat" value="001">
					
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
												<fmt:parseDate var="mailSendDt" value="${mailInfo.sendDt}" pattern="yyyyMMddHHmm"/>
												<fmt:formatDate var="sendDt" value="${mailSendDt}" pattern="yyyy-MM-dd HH:mm"/>
												<fmt:parseDate var="mailEndDt" value="${mailInfo.endDt}" pattern="yyyyMMddHHmm"/>
												<fmt:formatDate var="endDt" value="${mailEndDt}" pattern="yyyy-MM-dd HH:mm"/>
												<c:out value='${sendDt}'/> ~ <c:out value='${endDt}'/>
											</td>
											<th>수신자그룹</th>
											<td><c:out value="${mailInfo.segNm}"/></td>
										</tr>
										<tr>
											<th>캠페인명</th>
											<td><c:out value="${mailInfo.campNm}"/></td>
											<th>캠페인목적</th>
											<td><c:out value="${mailInfo.campTy}"/></td>
										</tr>
										<tr>
											<th>메일명</th>
											<td colspan="3">[<c:out value="${mailInfo.subTaskNo}"/>차] <c:out value="${mailInfo.taskNm}"/></td>
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
								<div class="active" id="divMailSummP"></div>
								<!-- //tab-cont : 결과요약 -->

								<!-- tab-cont : 세부에러// -->
								<div id="divMailErrorP"></div>
								<!-- //tab-cont : 세부에러 -->

								<!-- tab-cont : 도메인별// -->
								<div id="divMailDomainP"></div>
								<!-- //tab-cont : 도메인별 -->

								<!-- tab-cont : 발송시간별// -->
								<div id="divMailSendP"></div>
								<!-- //tab-cont : 발송시간별 -->

								<!-- tab-cont : 응답시간별// -->
								<div id="divMailRespP"></div>
								<!-- //tab-cont : 응답시간별 -->

								<!-- tab-cont : 고객별// -->
								<div id="divMailCustP"></div>
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
</body>
</html>
