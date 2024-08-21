<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.04.15
	*	설명 : PUSH  화면 리스트
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_push.jsp" %>

<script type="text/javascript" src="<c:url value='/js/push/ana/pushAnalListP.js'/>">
	//그래프 반응형
	window.addEventListener('resize',function(){
		barChart.resize();
		barChart2.resize();
	});
</script>

<body>
	<div id="wrap" class="push">
		
		<!-- lnb// -->
		<div id="lnb">
			<!-- LEFT MENU -->
			<%@ include file="/WEB-INF/jsp/inc/menu_push.jsp" %>
			<!-- LEFT MENU -->
		</div>
		<!-- //lnb -->

		<!-- content// -->
		<div id="content">

			<!-- cont-head// -->
			<section class="cont-head">
				<div class="title">
					<h2>PUSH알림 통계분석</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
				<form id="searchForm" name="searchForm" method="post">
					<input type="hidden" name="page" value="<c:out value='1'/>">
					<input type="hidden" name="searchStartDt" value="<c:out value='${searchVO.searchStartDt}'/>">
					<input type="hidden" name="searchEndDt"  value="<c:out value='${searchVO.searchEndDt}'/>">
					<input type="hidden" name="searchPushName"  value="<c:out value='${searchVO.searchPushName}'/>">
					<input type="hidden" name="searchPushGubun" value="<c:out value='${searchVO.searchPushGubun}'/>">
					<input type="hidden" name="searchCampNo" value="<c:out value='${searchVO.searchCampNo}'/>">
					<input type="hidden" name="searchCampNm" value="<c:out value='${searchVO.searchCampNm}'/>">
					<input type="hidden" name="searchDeptNo" value="<c:out value='${searchVO.searchDeptNo}'/>">
					<input type="hidden" name="searchUserId" value="<c:out value='${searchVO.searchUserId}'/>">
					<input type="hidden" id="searchCallGubun" name="searchCallGubun" value="<c:out value='${searchVO.searchCallGubun}'/>">
					
				</form>
				<form id="pushInfoForm" name="pushInfoForm" method="post">
					<input type="hidden" id="pushmessageId"      name="pushmessageId"  value="<c:out value="${pushLogInfo.pushmessageId}"/>">
					<input type="hidden" id="rsltCd"     		name="rsltCd" value="">
					<input type="hidden" id="pushName"                    value="<c:out value="${pushLogInfo.pushName}"/>">
					<fieldset>
						<legend>PUSH분석 및 결과</legend>
					
						<!-- 메시지분석// -->
						<div class="graybox">
							<div class="title-area">
								<h3 class="h3-title">PUSH분석</h3>
							</div>
							<div class="table-area">
								<table>
									<caption>PUSH분석</caption>
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
											<fmt:parseDate var="sendDate" value="${pushLogInfo.sendDt}" pattern="yyyyMMddHHmmss"/>
												<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy.MM.dd HH:mmss"/>
												<c:out value='${sendDt}'/>
											</td>
											<th>PUSH명</th>
											<td><c:out value='${pushLogInfo.pushName}'/></td>
										</tr>
										<tr>
											<th>발송유형</th>
											<td><c:out value='${pushLogInfo.pushGubunNm}'/></td>
											<th>캠페인명</th>
											<td><c:out value='${pushLogInfo.campNm}'/></td>
										</tr>
										<tr>
											<th>사용자그룹</th>
											<td><c:out value='${pushLogInfo.deptNm}'/></td>
											<th>사용자명</th>
											<td><c:out value='${pushLogInfo.userId}'/></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<!-- //메시지분석 -->
						
						<!-- 발송 소요시간// -->
						<div class="graybox mgt30">
							<div class="title-area">
								<h3 class="h3-title">발송 소요시간</h3>
							</div>
							<div class="grid-area">
								<table class="grid">
									<caption>그리드 정보</caption>
									<colgroup>
										<col style="width:auto;">
										<col style="width:33%;">
										<col style="width:33%;">
									</colgroup>
									<thead>
										<tr>
											<th scope="col">시작일시</th>
											<th scope="col">종료일시</th>
											<th scope="col">소요시간</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>
											<fmt:parseDate var="sendStartDt" value="${pushLogInfo.sendDt}" pattern="yyyyMMddHHmmss"/>
												<fmt:formatDate var="sendSt" value="${sendStartDt}" pattern="yyyy.MM.dd HH:mm"/>
												<p id="startTime" ><c:out value='${sendSt}'/></p>
											</td>
											<td>
											<fmt:parseDate var="sendEndDt" value="${pushLogInfo.sendEndDt}" pattern="yyyyMMddHHmmss"/>
											<fmt:formatDate var="sendEt" value="${sendEndDt}" pattern="yyyy.MM.dd HH:mm"/>
											<p id="endTime"><c:out value='${sendEt}'/></p>
											</td>
											<td><p id="timeGap"></p></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<!-- //발송 소요시간 -->
						
						<!-- 발송결과// -->
						<div class="graybox mgt30">
							<div class="title-area">
								<h3 class="h3-title">발송결과</h3>
							</div>
							<div class="grid-area piegraph-wrap">
								<div class="piegraph">
									<canvas id="circleChart" width="390" height="220"></canvas>
								</div>
								
								<table class="grid type-border">
									<caption>그리드 정보</caption>
									<colgroup>
										<col style="width:auto;">
										<col style="width:18%;">
										<col style="width:18%;">
										<col style="width:18%;">
										<col style="width:18%;">
									</colgroup>
									<thead>
										<tr>
											<th scope="col" rowspan="2">대상수</th>
											<th scope="col" colspan="2">성공수</th>
											<th scope="col" colspan="2">실패수</th>
										</tr>
										<tr>
											<th scope="col">Android</th>
											<th scope="col">IOS</th>
											<th scope="col">Android</th>
											<th scope="col">IOS</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td rowspan="2"><c:out value="${pushLogInfo.sendTotCnt}"/></td>
											<td><c:out value='${pushLogInfo.succAnd}'/></td>
											<td><c:out value='${pushLogInfo.succIos}'/></td>
											<td><c:out value='${pushLogInfo.failAnd}'/></td>
											<td><c:out value='${pushLogInfo.failIos}'/></td> 
										</tr>
										<tr>
											<td><c:out value="${pushLogInfo.succAndPer}"/></td>
											<td><c:out value="${pushLogInfo.succIosPer}"/></td>
											<c:if test="${pushLogInfo.failAndPer ne '0%'}">
												<td class="color-red"><c:out value='${pushLogInfo.failAndPer}'/></td>
											</c:if>
											<c:if test="${pushLogInfo.failAndPer eq '0%'}">
												<td><c:out value='${pushLogInfo.failAndPer}'/></td>
											</c:if>
											<c:if test="${pushLogInfo.failIosPer ne '0%'}">
												<td class="color-red"><c:out value='${pushLogInfo.failIosPer}'/></td>
											</c:if>
											<c:if test="${pushLogInfo.failIosPer eq '0%'}">
												<td><c:out value='${pushLogInfo.failIosPer}'/></td>
											</c:if>
										</tr>
									</tbody>
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
											{value: ${pushLogInfo.succCnt}, name: '성공 : ${pushLogInfo.succCnt}'},
											{value: ${pushLogInfo.failCnt}, name: '실패 : ${pushLogInfo.failCnt}'},
										]
									}
								],
							};
							circleChartoption && circleChart.setOption(circleChartoption);
						</script>
 
						<div class="btn-wrap">
							<button type="button" class="btn big" onclick="goList();">목록</button>
						</div>
						<!-- //btn-wrap -->
					</fieldset>
				</form>
			</section>
			<!-- //cont-body -->
		</div>
		<!-- // content -->

	</div>
	<!-- 결과 팝업// -->
	<%@ include file="/WEB-INF/jsp/sms/ana/pop/pop_smsSendResultList.jsp" %>
	<!-- //결과 팝업 -->
	<!-- 문자(SMS) 용보기 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_sms.jsp" %>
	<!-- //문자(SMS) 용보기 팝업 -->
	
</body>
</html>