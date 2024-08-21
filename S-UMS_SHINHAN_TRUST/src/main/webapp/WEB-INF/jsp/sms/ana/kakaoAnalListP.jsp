<%--
	/**********************************************************
	*	작성자 : 이혜민
	*	작성일시 : 2022.03.22
	*	설명 : 통계분석 카카오알림 분석 목록 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sms.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sms/ana/kakaoAnalListP.js'/>"></script>
<body>
	<div id="wrap" class="sms">
	
		<!-- lnb// -->
		<div id="lnb">
			<!-- LEFT MENU -->
			<%@ include file="/WEB-INF/jsp/inc/menu_sms.jsp" %>
			<!-- LEFT MENU -->
		</div>
		<!-- //lnb -->
		<!-- content// -->
		<div id="content">
			<!-- cont-head// -->
			<section class="cont-head">
				<div class="title">
					<h2>알림톡 통계분석</h2>
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
					<input type="hidden" name="searchEndDt"   value="<c:out value='${searchVO.searchEndDt}'/>">
					<input type="hidden" name="searchTaskNm"  value="<c:out value='${searchVO.searchTaskNm}'/>">
					<input type="hidden" name="searchTempCd" value="<c:out value='${searchVO.searchTempCd}'/>">
					<input type="hidden" name="searchTempNm" value="<c:out value='${searchVO.searchTempNm}'/>">
					<input type="hidden" name="searchDeptNo"  value="<c:out value='${searchVO.searchDeptNo}'/>">
					<input type="hidden" name="searchUserId"  value="<c:out value='${searchVO.searchUserId}'/>">
					<input type="hidden" id="searchCallGubun" name="searchCallGubun" value="<c:out value='${searchVO.searchCallGubun}'/>">
				</form>
				<form id="smsInfoForm" name="smsInfoForm" method="post">
					<input type="hidden" id="msgid"       name="msgid"       value="<c:out value="${kakaoLogInfo.msgid}"/>">
					<input type="hidden" id="keygen"      name="keygen"      value="<c:out value="${kakaoLogInfo.keygen}"/>">
					<input type="hidden" id="tempNm"      name="tempNm"      value="<c:out value="${kakaoLogInfo.tempNm}"/>">
					<input type="hidden" id="tempContent" name="tempContent" value="<c:out value="${kakaoLogInfo.tempContent}"/>">
					<input type="hidden" id="mergyItem"   name="mergyItem"   value=""/>
					<input type="hidden" id="rsltCd"      name="rsltCd"      value="">
					<input type="hidden" id="smsSendYn"   name="smsSendYn"   value="">
					<input type="hidden" id="taskNm"                         value="<c:out value="${kakaoLogInfo.taskNm}"/>">
					<input type="hidden" id="smsMessage"                     value="<c:out value="${kakaoLogInfo.smsMessage}"/>">
					
					<fieldset>
						<legend>알림톡 분석 및 발송 소요시간 및 발송결과 및 알림톡 확인결과</legend>
						<!-- 알림톡 분석// -->
						<div class="graybox">
							<div class="title-area">
								<h3 class="h3-title">알림톡 분석</h3>
								<!-- 버튼// -->
								<div class="btn-wrap">
									<button type="button" class="btn" onclick="goKakaoPreview()">알림톡 상세보기</button>
								</div>
								<!-- //버튼 -->
							</div>
							<div class="table-area">
								<table>
									<caption>알림톡 분석</caption>
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
												<fmt:parseDate var="kakaoSendDt" value="${kakaoLogInfo.sendDate}" pattern="yyyyMMddHHmmss"/>
												<fmt:formatDate var="sendDate" value="${kakaoSendDt}" pattern="yyyy-MM-dd HH:mm"/>
												<c:out value="${sendDate}"/>
											</td>
											<th>알림톡명</th>
											<td><c:out value="${kakaoLogInfo.taskNm}"/></td>
										</tr>
										<tr>
											<th>템플릿명</th>
											<td><c:out value="${kakaoLogInfo.tempNm}"/></td>
											<th>캠페인명</th>
											<td><c:out value='${searchVO.searchTempNm}'/></td>
										</tr>
										<tr>
											<th>사용자그룹</th>
											<td><c:out value="${kakaoLogInfo.deptNm}"/></td>
											<th>사용자명</th>
											<td><c:out value="${kakaoLogInfo.userId}"/></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<!-- //알림톡 분석 -->
						
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
												<fmt:parseDate var="kakaoStartDt" value="${kakaoLogInfo.sendStartDt}" pattern="yyyyMMddHHmmss"/>
												<fmt:formatDate var="startDt" value="${kakaoStartDt}" pattern="yyyy.MM.dd HH:mm:ss"/>
												<p id="startTime" ><c:out value="${startDt}"/></p>
											</td>
											<td>
												<fmt:parseDate var="kakaoEndDt" value="${kakaoLogInfo.sendEndDt}" pattern="yyyyMMddHHmmss"/>
												<fmt:formatDate var="endDt" value="${kakaoEndDt}" pattern="yyyy.MM.dd HH:mm:ss"/>
												<p id="endTime"><c:out value="${endDt}"/></p>
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
										<col style="width:17%;">
										<col style="width:17%;">
										<col style="width:17%;">
										<col style="width:17%;">
									</colgroup>
									<thead>
										<tr>
											<th scope="col" rowspan="2">대상수</th>
											<th scope="col" colspan="2">알림톡</th>
											<th scope="col" colspan="2">SMS</th>
										</tr>
										<tr>
											<th scope="col">성공</th>
											<th scope="col">실패</th>
											<th scope="col">성공</th>
											<th scope="col">실패</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td rowspan="2"><c:out value="${kakaoLogInfo.sendTotCnt}"/></td>
											<td><span onclick="goKakaoSendList('<c:out value='${kakaoLogInfo.msgid}'/>','<c:out value='${kakaoLogInfo.keygen}'/>','1','N')" style="cursor: pointer;"><c:out value='${kakaoLogInfo.succCnt}'/></span></td>
											<td><span class="color-red" onclick="goKakaoSendList('<c:out value='${kakaoLogInfo.msgid}'/>','<c:out value='${kakaoLogInfo.keygen}'/>','0','N')" style="cursor: pointer;"><c:out value='${kakaoLogInfo.failCnt}'/></span></td>
											<td><span onclick="goKakaoSendList('<c:out value='${kakaoLogInfo.msgid}'/>','<c:out value='${kakaoLogInfo.keygen}'/>','1','Y')" style="cursor: pointer;"><c:out value='${kakaoLogInfo.succSms}'/></span></td>
											<td><span class="color-red" onclick="goKakaoSendList('<c:out value='${kakaoLogInfo.msgid}'/>','<c:out value='${kakaoLogInfo.keygen}'/>','0','Y')" style="cursor: pointer;"><c:out value='${kakaoLogInfo.failSms}'/></span></td>
										</tr>
										<tr>
											<td><c:out value="${kakaoLogInfo.succPer}"/></td>
											<td class="color-red"><c:out value="${kakaoLogInfo.failPer}"/></td>
											<td><c:out value="${kakaoLogInfo.succSmsPer}"/></td>
											<td class="color-red"><c:out value="${kakaoLogInfo.failSmsPer}"/></td>
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
											{value: ${kakaoLogInfo.succCnt} + ${kakaoLogInfo.succSms}, name: '성공 : ${kakaoLogInfo.succCnt + kakaoLogInfo.succSms}'},
											{value: ${kakaoLogInfo.failCnt} + ${kakaoLogInfo.failSms}, name: '실패 : ${kakaoLogInfo.failCnt + kakaoLogInfo.failSms}'},
										]
									}
								],
							};
							circleChartoption && circleChart.setOption(circleChartoption);
							</script>
						<!-- //발송결과 -->
						
						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big" onclick="goList()">목록</button>
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
	<!-- 알림톡 템플릿 미리보기 또는 상세내용보기 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_kakao_template.jsp" %>
	<!-- //알림톡 템플릿 미리보기 또는 상세내용보기 팝업 -->
</body>
</html>
