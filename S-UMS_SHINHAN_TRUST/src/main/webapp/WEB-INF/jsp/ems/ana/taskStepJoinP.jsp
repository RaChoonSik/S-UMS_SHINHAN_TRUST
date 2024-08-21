<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.09.16
	*	설명 : 통계분석 정기메일 차수별 병합분석 병합분석 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp" %>

<script type="text/javascript">
// goExcelDown
function goExcelDown() {
	$("#searchForm").attr("target","iFrmExcel").attr("action","./taskStepJoinExcel.ums").submit();
}

// 목록 클릭시
function goList() {
	$("#searchForm").attr("target","").attr("action","./taskStepListP.ums").submit();
}
</script>

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
					<h2>정기메일 차수별 병합분석</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">

				<div>
					<form id="searchForm" name="searchForm" method="post">
						<input type="hidden" name="page" value="<c:out value='${sendLogVO.page}'/>">
						<input type="hidden" name="searchStartDt" value="<c:out value='${sendLogVO.searchStartDt}'/>">
						<input type="hidden" name="searchEndDt" value="<c:out value='${sendLogVO.searchEndDt}'/>">
						<input type="hidden" name="searchCampNo" value="<c:out value='${sendLogVO.searchCampNo}'/>">
						<input type="hidden" name="searchTaskNm" value="<c:out value='${sendLogVO.searchTaskNm}'/>">
						<input type="hidden" name="searchSegNo" value="<c:out value='${sendLogVO.searchSegNo}'/>">
						<input type="hidden" name="searchDeptNo" value="<c:out value='${sendLogVO.searchDeptNo}'/>">
						<input type="hidden" name="searchUserId" value="<c:out value='${sendLogVO.searchUserId}'/>">
						<input type="hidden" name="taskNo" value="<c:out value='${sendLogVO.taskNo}'/>">
						<input type="hidden" name="taskNos" value="<c:out value='${sendLogVO.taskNos}'/>">
						<input type="hidden" name="subTaskNos" value="<c:out value='${sendLogVO.subTaskNos}'/>">
						
						<fieldset>
							<legend>정기메일 차수별목록, 발송결과 및 세부에러</legend>
						
							<!-- 목록// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">정기메일 차수별목록</h3>
								</div>

								<div class="grid-area pb20">
									<table class="grid">
										<caption>그리드 정보</caption>
										<colgroup>
											<col style="width:9%;">
											<col style="width:auto;">
											<col style="width:15%;">
											<col style="width:15%;">
											<col style="width:15%;">
										</colgroup>
										<thead>
											<tr>
												<th scope="col">NO</th>
												<th scope="col">메일명</th>
												<th scope="col">대상수</th>
												<th scope="col">성공수</th>
												<th scope="col">실패수</th>
											</tr>
										</thead>
										<tbody>
											<c:set var="totSendCnt" value="${0}"/>
											<c:set var="totSuccCnt" value="${0}"/>
											<c:set var="totFailCnt" value="${0}"/>
											<c:if test="${fn:length(mailList) > 0}">
												<c:forEach items="${mailList}" var="mail">
													<c:set var="totSendCnt" value="${totSendCnt + mail.totCnt}"/>
													<c:set var="totSuccCnt" value="${totSuccCnt + mail.sucCnt}"/>
													<c:set var="totFailCnt" value="${totFailCnt + (mail.totCnt - mail.sucCnt)}"/>
													<tr>
														<td><c:out value='${mail.taskNo}'/></td>
														<td class="tal">[<c:out value="${mail.subTaskNo}"/>차] <c:out value='${mail.taskNm}'/></td>
														<td><c:out value='${mail.totCnt}'/></td>
														<td><c:out value='${mail.sucCnt}'/></td>
														<td><c:out value='${mail.totCnt - mail.sucCnt}'/></td>
													</tr>
												</c:forEach>
											</c:if>
										</tbody>
										<tfoot>
											<tr>
												<td colspan="2">합계</td>
												<td><c:out value='${totSendCnt}'/></td>
												<td><c:out value='${totSuccCnt}'/></td>
												<td><c:out value='${totFailCnt}'/></td>
											</tr>
										</tfoot>
									</table>
								</div>
							</div>
							<!-- //목록 -->
	
							<!-- 발송결과// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">발송결과</h3>
								</div>

								<div class="grid-area piegraph-wrap">
									<div class="piegraph">
										<canvas id="circleChart" width="390" height="220"></canvas>
									</div>
									
									<table class="grid">
										<caption>그리드 정보</caption>
										<colgroup>
											<col style="width:34%;">
											<col style="width:33%;">
											<col style="width:33%;">
										</colgroup>
										<thead>
											<tr>
												<th scope="col">총 대상수</th>
												<th scope="col">총 성공수</th>
												<th scope="col">총 실패수</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>
													<fmt:formatNumber var="totSendCntNum" type="number" value="${totSendCnt}" /><c:out value="${totSendCntNum}"/>
												</td>
												<td>
													<fmt:formatNumber var="totSuccCntNum" type="number" value="${totSuccCnt}" /><c:out value="${totSuccCntNum}"/>
												</td>
												<td>
													<fmt:formatNumber var="totFailCntNum" type="number" value="${totFailCnt}" /><c:out value="${totFailCntNum}"/>
												</td>
											</tr>
										</tbody>
										<tfoot>
											<tr>
												<td>비율(%)</td>
												<td>
													<fmt:formatNumber var="succPer" type="percent" value="${totSendCnt == 0 ? 0 : totSuccCnt / totSendCnt}" /><c:out value='${succPer}'/>
												</td>
												<td>
													<fmt:formatNumber var="failPer" type="percent" value="${totSendCnt == 0 ? 0 : totFailCnt / totSendCnt}" /><c:out value='${failPer}'/>
												</td>
											</tr>
										</tfoot>
									</table>
									
								</div>
							</div>
							<script type="text/javascript">
							/* 발송결과 파이차트 */
							var circleChartDom = document.getElementById('circleChart');
							var circleChart = echarts.init(circleChartDom);
							var circleChartoption;
							var piecolor = ['#5660F0', '#F9984E'];
							circleChartoption = {
								tooltip: {
									trigger: 'item'
								},
								legend: {
									bottom: '2%',
									right: 'right',
									icon:'circle',
									itemGap: 15,
									textStyle: {
										color: "#444",
										fontFamily: 'NotoSansKR',
										fontSize: 14,
										fontWeight: '500'
									},
								},
								series: [
									{
										type: 'pie',
										top: '30px',
										color: piecolor,
										radius: ['60%', '93%'],
										avoidLabelOverlap: false,
										label: {
											show: false,
											position: 'center'
										},
										emphasis: {
											label: {
												show: true,
												fontFamily: 'NotoSansKR',
												fontSize: '13',
												fontWeight: '600'
											}
										},
										labelLine: {
											show: false
										},
										data: [
											{value: ${totSuccCnt}, name: '성공 : ${totSuccCnt}'},
											{value: ${totFailCnt}, name: '실패 : ${totFailCnt}'},
										]
									}
								],
							};
							circleChartoption && circleChart.setOption(circleChartoption);
							</script>
							<!-- //발송결과 -->
							
							<!-- 반응결과// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">반응결과</h3>
								</div>
								<div class="grid-area pt0 pb20">
									<table class="grid type-border">
										<caption>그리드 정보</caption>
										<colgroup>
											<col style="width:auto;">
											<col style="width:18%;">
											<col style="width:18%;">
											<col style="width:18%;">
											<col style="width:18%;">
											<col style="width:18%;">
										</colgroup>
										<thead>
											<tr>
												<th scope="col">&nbsp;</th>
												<th scope="col">성공수</th>
												<th scope="col">오픈수</th>
												<th scope="col">유효오픈수</th>
												<th scope="col">링크클릭수</th>
												<th scope="col">수신거부</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<th scope="row">반응수</th>
												<td rowspan="2">
													<fmt:formatNumber var="totSuccCntNum" type="number" value="${totSuccCnt}" /><c:out value="${totSuccCntNum}"/>
												</td>
												<td>
													<fmt:formatNumber var="openCntNum" type="number" value="${respLog.openCnt}" /><c:out value="${openCntNum}"/>
												</td>
												<td>
													<fmt:formatNumber var="validCntNum" type="number" value="${respLog.validCnt}" /><c:out value="${validCntNum}"/>
												</td>
												<td>
													<fmt:formatNumber var="clickCntNum" type="number" value="${respLog.clickCnt}" /><c:out value="${clickCntNum}"/>
												</td>
												<td>
													<fmt:formatNumber var="blockCntNum" type="number" value="${respLog.blockCnt}" /><c:out value="${blockCntNum}"/>
												</td>
											</tr>
											<tr>
												<th scope="row">성공대비</th>
												<td>
													<fmt:formatNumber var="openPer" type="percent" value="${totSuccCnt == 0 ? 0 : respLog.openCnt / totSuccCnt}" /><c:out value='${openPer}'/>
												</td>
												<td>
													<fmt:formatNumber var="validPer" type="percent" value="${totSuccCnt == 0 ? 0 : respLog.validCnt / totSuccCnt}" /><c:out value='${validPer}'/>
												</td>
												<td>
													<fmt:formatNumber var="clickPer" type="percent" value="${totSuccCnt == 0 ? 0 : respLog.clickCnt / totSuccCnt}" /><c:out value='${clickPer}'/>
												</td>
												<td>
													<fmt:formatNumber var="blockPer" type="percent" value="${totSuccCnt == 0 ? 0 : respLog.blockCnt / totSuccCnt}" /><c:out value='${blockPer}'/>
												</td>
											</tr>
											<tr>
												<th scope="row">전체대비</th>
												<td>
													<fmt:formatNumber var="totSuccPer" type="percent" value="${totSendCnt == 0 ? 0 : totSuccCnt / totSendCnt}" /><c:out value='${totSuccPer}'/>
												</td>
												<td>
													<fmt:formatNumber var="totOpenPer" type="percent" value="${totSendCnt == 0 ? 0 : respLog.openCnt / totSendCnt}" /><c:out value='${totOpenPer}'/>
												</td>
												<td>
													<fmt:formatNumber var="totValidPer" type="percent" value="${totSendCnt == 0 ? 0 : respLog.validCnt / totSendCnt}" /><c:out value='${totValidPer}'/>
												</td>
												<td>
													<fmt:formatNumber var="totClickPer" type="percent" value="${totSendCnt == 0 ? 0 : respLog.clickCnt / totSendCnt}" /><c:out value='${totClickPer}'/>
												</td>
												<td>
													<fmt:formatNumber var="totBlockPer" type="percent" value="${totSendCnt == 0 ? 0 : respLog.blockCnt / totSendCnt}" /><c:out value='${totBlockPer}'/>
												</td>
											</tr>
										</tbody>
									</table>
									
									<div class="bar-chartwrap" style="height:230px;">
										<div id="barChart"></div>
									</div>
								</div>
							</div>
							<script type="text/javascript">
							/* 반응결과 막대그래프 */
							var barChart  = echarts.init(document.querySelector('#barChart'), null);
							var barChartoption = {
								tooltip: {
									trigger: 'axis',
									axisPointer: {
										type: 'shadow'
									}
								},
								grid: {
									left: '0',
									right: '4%',
									bottom: '0',
									y: '30px',
									containLabel: true,
								},
								xAxis: {
									type: 'value',
									boundaryGap: [0, 0.01],
									axisLabel:{
										color: '#666',
										fontFamily: 'NotoSansKR',
										fontSize: '13'
									}
								},
								yAxis: {
									type: 'category',
									axisLabel:{
										color: '#444',
										fontFamily: 'NotoSansKR',
										fontSize: '14',
										fontWeight: '600',
									},
									axisTick: {
										show: false
									},
									data: ['수신거부', '링크클릭수', '유효오픈', '오픈수', '성공수']
								},
								series: [
									{
										name: '반응결과',
										type: 'bar',
										itemStyle:{
											color: '#5668F0',
										},
										data: [${totSuccCnt}, ${respLog.openCnt}, ${respLog.validCnt}, ${respLog.clickCnt}, ${respLog.blockCnt}]
									}
								]
							};
							barChart.setOption(barChartoption);
							</script>
							<!-- //반응결과 -->

							<%-- 세부에러 변수설정 --%>
							<c:set var="syntaxErrCnt" value="${0}"/>
							<c:set var="webAgentErrCnt" value="${0}"/>
							<c:set var="dbAgentErrCnt" value="${0}"/>
							<c:set var="mailBodyErrCnt" value="${0}"/>
							<c:set var="domainErrCnt" value="${0}"/>
							<c:set var="networkErrCnt" value="${0}"/>
							<c:set var="connectErrCnt" value="${0}"/>
							<c:set var="heloErrCnt" value="${0}"/>
							<c:set var="mailFromErrCnt" value="${0}"/>
							<c:set var="rcptToErrCnt" value="${0}"/>
							<c:set var="resetErrCnt" value="${0}"/>
							<c:set var="dataErrCnt" value="${0}"/>
							<c:set var="dotErrCnt" value="${0}"/>
							<c:set var="quitErrCnt" value="${0}"/>
							<c:set var="sumStep1Err" value="${0}"/>
							<c:set var="sumStep2Err" value="${0}"/>
							<c:set var="failCnt" value="${0}"/>
							<c:if test="${fn:length(errorList) > 0}">
								<c:forEach items="${errorList}" var="error">
									<c:set var="failCnt" value="${error.cntStep2}"/>
									<c:if test="${'001' eq error.step1}">
										<c:set var="sumStep1Err" value="${sumStep1Err + failCnt}"/>
										<c:choose>
											<c:when test="${'001' eq error.step2}">
												<c:set var="syntaxErrCnt" value="${failCnt}"/>
											</c:when>
											<c:when test="${'002' eq error.step2}">
												<c:set var="webAgentErrCnt" value="${failCnt}"/>
											</c:when>
											<c:when test="${'003' eq error.step2}">
												<c:set var="dbAgentErrCnt" value="${failCnt}"/>
											</c:when>
											<c:when test="${'004' eq error.step2}">
												<c:set var="mailBodyErrCnt" value="${failCnt}"/>
											</c:when>
											<c:when test="${'005' eq error.step2}">
												<c:set var="domainErrCnt" value="${failCnt}"/>
											</c:when>
											<c:when test="${'006' eq error.step2}">
												<c:set var="networkErrCnt" value="${failCnt}"/>
											</c:when>
										</c:choose>
									</c:if>
									<c:if test="${'002' eq error.step1}">
										<c:set var="sumStep2Err" value="${sumStep2Err + failCnt}"/>
										<c:choose>
											<c:when test="${'001' eq error.step2}">
												<c:set var="connectErrCnt" value="${failCnt}"/>
											</c:when>
											<c:when test="${'002' eq error.step2}">
												<c:set var="heloErrCnt" value="${failCnt}"/>
											</c:when>
											<c:when test="${'003' eq error.step2}">
												<c:set var="mailFromErrCnt" value="${failCnt}"/>
											</c:when>
											<c:when test="${'004' eq error.step2}">
												<c:set var="rcptToErrCnt" value="${failCnt}"/>
											</c:when>
											<c:when test="${'005' eq error.step2}">
												<c:set var="resetErrCnt" value="${failCnt}"/>
											</c:when>
											<c:when test="${'006' eq error.step2}">
												<c:set var="dataErrCnt" value="${failCnt}"/>
											</c:when>
											<c:when test="${'007' eq error.step2}">
												<c:set var="dotErrCnt" value="${failCnt}"/>
											</c:when>
											<c:when test="${'008' eq error.step2}">
												<c:set var="quitErrCnt" value="${failCnt}"/>
											</c:when>
										</c:choose>
									</c:if>
								</c:forEach>
							</c:if>
							
							<!-- 세부에러 - 메일전송 전단계// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">세부에러 - 메일전송 전단계</h3>
								</div>
								<div class="grid-area pt0 pb20">
									<table class="grid type-border">
										<caption>그리드 정보</caption>
										<colgroup>
											<col style="width:15%;">
											<col style="width:15%;">
											<col style="width:15%;">
											<col style="width:15%;">
											<col style="width:15%;">
											<col style="width:15%;">
											<col style="width:15%;">
										</colgroup>
										<thead>
											<tr>
												<th scope="col">Syntax</th>
												<th scope="col">Web Agent</th>
												<th scope="col">DB Agent</th>
												<th scope="col">Mail Body</th>
												<th scope="col">Domain</th>
												<th scope="col">Network</th>
												<th scope="col">합계</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>
													<fmt:formatNumber var="syntaxErrCntNum" type="number" value="${syntaxErrCnt}" /><c:out value="${syntaxErrCntNum}"/>
												</td>
												<td>
													<fmt:formatNumber var="webAgentErrCntNum" type="number" value="${webAgentErrCnt}" /><c:out value="${webAgentErrCntNum}"/>
												</td>
												<td>
													<fmt:formatNumber var="dbAgentErrCntNum" type="number" value="${dbAgentErrCnt}" /><c:out value="${dbAgentErrCntNum}"/>
												</td>
												<td>
													<fmt:formatNumber var="mailBodyErrCntNum" type="number" value="${mailBodyErrCnt}" /><c:out value="${mailBodyErrCntNum}"/>
												</td>
												<td>
													<fmt:formatNumber var="domainErrCntNum" type="number" value="${domainErrCnt}" /><c:out value="${domainErrCntNum}"/>
												</td>
												<td>
													<fmt:formatNumber var="networkErrCntNum" type="number" value="${networkErrCnt}" /><c:out value="${networkErrCntNum}"/>
												</td>
												<td class="color-red">
													<fmt:formatNumber var="sumStep1ErrNum" type="number" value="${sumStep1Err}" /><c:out value="${sumStep1ErrNum}"/>
												</td>
											</tr>
											<tr>
												<td>
													<fmt:formatNumber var="syntaxErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : syntaxErrCnt / totFailCnt}" /><c:out value='${syntaxErrCntPer}'/>
												</td>
												<td>
													<fmt:formatNumber var="webAgentErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : webAgentErrCnt / totFailCnt}" /><c:out value='${webAgentErrCntPer}'/>
												</td>
												<td>
													<fmt:formatNumber var="dbAgentErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : dbAgentErrCnt / totFailCnt}" /><c:out value='${dbAgentErrCntPer}'/>
												</td>
												<td>
													<fmt:formatNumber var="mailBodyErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : mailBodyErrCnt / totFailCnt}" /><c:out value='${mailBodyErrCntPer}'/>
												</td>
												<td>
													<fmt:formatNumber var="domainErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : domainErrCnt / totFailCnt}" /><c:out value='${domainErrCntPer}'/>
												</td>
												<td>
													<fmt:formatNumber var="networkErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : networkErrCnt / totFailCnt}" /><c:out value='${networkErrCntPer}'/>
												</td>
												<td class="color-red">
													<fmt:formatNumber var="sumStep1ErrPer" type="percent" value="${totFailCnt == 0 ? 0 : sumStep1Err / totFailCnt}" /><c:out value='${sumStep1ErrPer}'/>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							<!-- //세부에러 - 메일전송 전단계 -->

							<!-- 세부에러 - 메일발송 단계// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">세부에러 - 메일발송 단계</h3>
								</div>
								<div class="grid-area pt0 pb20">
									<table class="grid type-border">
										<caption>그리드 정보</caption>
										<colgroup>
											<col style="width:12%;">
											<col style="width:12%;">
											<col style="width:12%;">
											<col style="width:12%;">
											<col style="width:12%;">
											<col style="width:12%;">
											<col style="width:12%;">
											<col style="width:12%;">
											<col style="width:12%;">
										</colgroup>
										<thead>
											<tr>
												<th scope="col">CONNECT</th>
												<th scope="col">HELO</th>
												<th scope="col">MAIL FROM</th>
												<th scope="col">RCPT TO</th>
												<th scope="col">RESET</th>
												<th scope="col">DATA</th>
												<th scope="col">DOT</th>
												<th scope="col">QUIT</th>
												<th scope="col">합계</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>
													<fmt:formatNumber var="connectErrCntNum" type="number" value="${connectErrCnt}" /><c:out value="${connectErrCntNum}"/>
												</td>
												<td>
													<fmt:formatNumber var="heloErrCntNum" type="number" value="${heloErrCnt}" /><c:out value="${heloErrCntNum}"/>
												</td>
												<td>
													<fmt:formatNumber var="mailFromErrCntNum" type="number" value="${mailFromErrCnt}" /><c:out value="${mailFromErrCntNum}"/>
												</td>
												<td>
													<fmt:formatNumber var="rcptToErrCntNum" type="number" value="${rcptToErrCnt}" /><c:out value="${rcptToErrCntNum}"/>
												</td>
												<td>
													<fmt:formatNumber var="resetErrCntNum" type="number" value="${resetErrCnt}" /><c:out value="${resetErrCntNum}"/>
												</td>
												<td>
													<fmt:formatNumber var="dataErrCntNum" type="number" value="${dataErrCnt}" /><c:out value="${dataErrCntNum}"/>
												</td>
												<td>
													<fmt:formatNumber var="dotErrCntNum" type="number" value="${dotErrCnt}" /><c:out value="${dotErrCntNum}"/>
												</td>
												<td>
													<fmt:formatNumber var="quitErrCntNum" type="number" value="${quitErrCnt}" /><c:out value="${quitErrCntNum}"/>
												</td>
												<td class="color-red">
													<fmt:formatNumber var="sumStep2ErrNum" type="number" value="${sumStep2Err}" /><c:out value="${sumStep2ErrNum}"/>
												</td>
											</tr>
											<tr>
												<td>
													<fmt:formatNumber var="connectErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : connectErrCnt / totFailCnt}" /><c:out value='${connectErrCntPer}'/>
												</td>
												<td>
													<fmt:formatNumber var="heloErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : heloErrCnt / totFailCnt}" /><c:out value='${heloErrCntPer}'/>
												</td>
												<td>
													<fmt:formatNumber var="mailFromErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : mailFromErrCnt / totFailCnt}" /><c:out value='${mailFromErrCntPer}'/>
												</td>
												<td>
													<fmt:formatNumber var="rcptToErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : rcptToErrCnt / totFailCnt}" /><c:out value='${rcptToErrCntPer}'/>
												</td>
												<td>
													<fmt:formatNumber var="resetErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : resetErrCnt / totFailCnt}" /><c:out value='${resetErrCntPer}'/>
												</td>
												<td>
													<fmt:formatNumber var="dataErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : dataErrCnt / totFailCnt}" /><c:out value='${dataErrCntPer}'/>
												</td>
												<td>
													<fmt:formatNumber var="dotErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : dotErrCnt / totFailCnt}" /><c:out value='${dotErrCntPer}'/>
												</td>
												<td>
													<fmt:formatNumber var="quitErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : quitErrCnt / totFailCnt}" /><c:out value='${quitErrCntPer}'/>
												</td>
												<td class="color-red">
													<fmt:formatNumber var="sumStep2ErrPer" type="percent" value="${sumStep2Err == 0 ? 0 : quitErrCnt / totFailCnt}" /><c:out value='${sumStep2ErrPer}'/>
												</td>
											</tr>
										</tbody>
									</table>
									
									<div class="barstep-chartwrap" style="height:460px;">
										<div id="barStepChart"></div>
									</div>
								</div>
							</div>
							<script type="text/javascript">
							/* 메일발송단계 막대그래프 */
							var barStepChart  = echarts.init(document.querySelector('#barStepChart'), null);
							var barStepChartoption = {
								tooltip: {
									trigger: 'axis',
									axisPointer: {
										type: 'shadow'
									}
								},
								grid: {
									left: '0',
									right: '4%',
									bottom: '0',
									y: '30px',
									containLabel: true,
								},
								xAxis: {
									type: 'value',
									boundaryGap: [0, 0.01],
									axisLabel:{
										color: '#666',
										fontFamily: 'NotoSansKR',
										fontSize: '13'
									}
								},
								yAxis: {
									type: 'category',
									axisLabel:{
										color: '#444',
										fontFamily: 'NotoSansKR',
										fontSize: '14',
										fontWeight: '600',
									},
									axisTick: {
										show: false
									},
									data: ['QUIT', 'DOT', 'DATA', 'RESET', 'RCPTTO', 'MAILFROM', 'HELO', 'CONNECT',
											'Network', 'Domain', 'Mail Body', 'DB Agent', 'Web Agent', 'Syntax']
								},
								series: [
									{
										name: '메일발송단계',
										type: 'bar',
										itemStyle:{
											color: '#5668F0',
										},
										data: [${quitErrCnt}, ${dotErrCnt}, ${dataErrCnt}, ${resetErrCnt}, ${rcptToErrCnt}, ${mailFromErrCnt}, ${heloErrCnt}, ${connectErrCnt},
												${networkErrCnt}, ${domainErrCnt}, ${mailBodyErrCnt}, ${dbAgentErrCnt}, ${webAgentErrCnt}, ${syntaxErrCnt}]
									}
								]
							};
							barStepChart.setOption(barStepChartoption);
							</script>
							<!-- //세부에러 - 메일발송 단계 -->
	
							<!-- btn-wrap// -->
							<div class="btn-wrap">
								<button type="button" class="btn fullblue big" onclick="goExcelDown();">엑셀 다운로드</button>
								<button type="button" class="btn big" onclick="goList();">목록</button>
							</div>
							<!-- //btn-wrap -->

						</fieldset>
					</form>
				</div>
				<div style="width:0px;height:0px;display:none;">
				<iframe id="iFrmExcel" name="iFrmExcel" style="width:0px;height:0px;"></iframe>
				</div>

			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>
	<script type="text/javascript">
	//그래프 반응형
	window.addEventListener('resize',function(){
		barChart.resize();
		barStepChart.resize();
	});
	</script>
</body>
</html>
