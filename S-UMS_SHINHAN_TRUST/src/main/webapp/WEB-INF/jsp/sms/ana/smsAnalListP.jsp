<%--
	/**********************************************************
	*	작성자 : 이문용
	*	작성일시 : 2022.03.24
	*	설명 : SMS디테일 화면 리스트
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sms.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sms/ana/smsAnalListP.js'/>"></script>

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
					<h2>메시지 통계분석</h2>
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
					<input type="hidden" name="searchTaskNm" value="<c:out value='${searchVO.searchTaskNm}'/>">
					<input type="hidden" name="searchTempCd" value="<c:out value='${searchVO.searchTempCd}'/>">
					<input type="hidden" name="searchTempNm" value="<c:out value='${searchVO.searchTempNm}'/>">
					<input type="hidden" name="searchDeptNo" value="<c:out value='${searchVO.searchDeptNo}'/>">
					<input type="hidden" name="searchUserId" value="<c:out value='${searchVO.searchUserId}'/>">
					<input type="hidden" id="searchCallGubun" name="searchCallGubun" value="<c:out value='${searchVO.searchCallGubun}'/>">
				</form>
				<form id="smsInfoForm" name="smsInfoForm" method="post">
					<input type="hidden" id="msgid"      name="msgid"  value="<c:out value="${smsLogInfo.msgid}"/>">
					<input type="hidden" id="keygen"     name="keygen" value="<c:out value="${smsLogInfo.keygen}"/>">
					<input type="hidden" id="rsltCd"     name="rsltCd" value="">
					<input type="hidden" id="taskNm"                   value="<c:out value="${smsLogInfo.taskNm}"/>">
					<input type="hidden" id="smsMessage"               value="<c:out value="${smsLogInfo.smsMessage}"/>">
					<input type="hidden" id="imgUploadPath"            value="<c:out value='${imgUploadPath}'/>">
					<fieldset>
						<legend>메시지분석 및 발송 소요시간 및 발송결과 및 메시지 확인결과</legend>
					
						<!-- 메시지분석// -->
						<div class="graybox">
							<div class="title-area">
								<h3 class="h3-title">메시지분석</h3>
								<!-- 버튼// -->
								<div class="btn-wrap">
									<button type="button" class="btn" onclick="goSmsPreview()">메시지 내용보기</button>
								</div>
								<!-- //버튼 -->
							</div>
							<div class="table-area">
								<table>
									<caption>메시지분석</caption>
									<colgroup>
										<col style="width:20%">
										<col style="width:20%">
										<col style="width:20%">
										<col style="width:40%">
									</colgroup> 
									<tbody>
										<tr>
											<th>발송일시</th>
											<td>
											<fmt:parseDate var="sendDate" value="${smsLogInfo.sendDate}" pattern="yyyyMMddHHmmss"/>
												<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy.MM.dd HH:mmss"/>
												<c:out value='${sendDt}'/>
											</td>
											<th>메시지명</th>
											<td><c:out value='${smsLogInfo.taskNm}'/></td>
										</tr>
										<tr>
											<th>전송유형</th>
											<td><c:out value='${smsLogInfo.gubunNm}'/></td>
											<th>캠페인명</th>
											<td><c:out value='${searchVO.searchTempNm}'/></td>
										</tr>
										<tr>
											<th>사용자그룹</th>
											<td><c:out value='${smsLogInfo.deptNm}'/></td>
											<th>사용자명</th>
											<td><c:out value='${smsLogInfo.userId}'/></td>
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
											<fmt:parseDate var="sendStartDt" value="${smsLogInfo.sendStartDt}" pattern="yyyyMMddHHmmss"/>
												<fmt:formatDate var="sendSt" value="${sendStartDt}" pattern="yyyy.MM.dd HH:mm"/>
												<p id="startTime" ><c:out value='${sendSt}'/></p>
											</td>
											<td>
											<fmt:parseDate var="sendEndDt" value="${smsLogInfo.sendEndDt}" pattern="yyyyMMddHHmmss"/>
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
										<col style="width:33%;">
										<col style="width:33%;">
									</colgroup>
									<thead>
										<tr>
											<th scope="col">대상수</th>
											<th scope="col">성공수</th>
											<th scope="col">실패수</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td rowspan="2"><c:out value="${smsLogInfo.sendTotCnt}"/></td>
											<td><span onclick="goSmsSendList('<c:out value='${smsLogInfo.msgid}'/>','<c:out value='${smsLogInfo.keygen}'/>','1')" style="cursor: pointer;"><c:out value='${smsLogInfo.succCnt}'/></span></td>
											<td><span class="color-red" onclick="goSmsSendList('<c:out value='${smsLogInfo.msgid}'/>','<c:out value='${smsLogInfo.keygen}'/>','0')" style="cursor: pointer;"><c:out value='${smsLogInfo.failCnt}'/></span></td>
										</tr>
										<tr>
											<td><c:out value="${smsLogInfo.succPer}"/></td>
											<td class="color-red"><c:out value="${smsLogInfo.failPer}"/></td>
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
											{value: ${smsLogInfo.succCnt}, name: '성공 : ${smsLogInfo.succCnt}'},
											{value: ${smsLogInfo.failCnt}, name: '실패 : ${smsLogInfo.failCnt}'},
										]
									}
								],
							};
							circleChartoption && circleChart.setOption(circleChartoption);
						</script>
						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big" onclick="goList();">목록</button>
						</div>
						<!-- SMS등록한 이미지 파일 리스트  -->
						<div class="list-area" style="display:none;">
							<ul class="filelist" id="smsAttachFileList" >
								<c:if test="${fn:length(smsAttachList) > 0}">
									<c:forEach items="${smsAttachList}" var="attach">
										<li>
											<c:set var="attFlPath" value="${fn:substring(attach.attFlPath, fn:indexOf(attach.attFlPath,'/')+1, fn:length(attach.attFlPath))}"/>
											<input type="hidden" name="attachPath" value="<c:out value='${attach.attFlPath}'/>">
										</li>
									</c:forEach>
								</c:if>
							</ul>
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