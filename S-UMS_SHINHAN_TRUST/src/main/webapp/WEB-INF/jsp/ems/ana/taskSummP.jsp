<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.09.06
	*	설명 : 통계분석 정기메일 통합 통계분석 결과요약 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<c:set var="totFailCnt" value="${0}"/>
<c:if test="${not empty sendResult}">
	<c:set var="totFailCnt" value="${sendResult.failCnt}"/>
	<!-- 발송결과// -->
	<div class="graybox">
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
					<col style="width:34%;">
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
						<td rowspan="2">
							<fmt:formatNumber var="sendCntNum" type="number" value="${sendResult.sendCnt}" />
							<c:out value='${sendCntNum}'/>
						</td>
						<td>
							<fmt:formatNumber var="succCntNum" type="number" value="${sendResult.succCnt}" />
							<c:out value='${succCntNum}'/>
						</td>
						<td>
							<fmt:formatNumber var="failCntNum" type="number" value="${sendResult.failCnt}" />
							<c:out value='${failCntNum}'/>
						</td>
					</tr>
					<tr>
						<td>
							<fmt:formatNumber var="succPer" type="percent" value="${sendResult.sendCnt == 0 ? 0 : sendResult.succCnt / sendResult.sendCnt}" />
							<c:out value='${succPer}'/>
						</td>
						<td>
							<fmt:formatNumber var="failPer" type="percent" value="${sendResult.sendCnt == 0 ? 0 : sendResult.failCnt / sendResult.sendCnt}" />
							<c:out value='${failPer}'/>
						</td>
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
					{value: ${sendResult.succCnt}, name: '성공 : ${sendResult.succCnt}'},
					{value: ${sendResult.failCnt}, name: '실패 : ${sendResult.failCnt}'},
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
							<c:out value='${succCntNum}'/>
						</td>
						<td>
							<fmt:formatNumber var="openCntNum" type="number" value="${sendResult.openCnt}" />
							<c:out value='${openCntNum}'/>
						</td>
						<td>
							<fmt:formatNumber var="validCntNum" type="number" value="${sendResult.validCnt}" />
							<c:out value='${validCntNum}'/>
						</td>
						<td>
							<fmt:formatNumber var="validCntNum" type="number" value="${sendResult.validCnt}" />
							<c:out value='${validCntNum}'/>
						</td>
						<td>
							<fmt:formatNumber var="blockCntNum" type="number" value="${sendResult.blockCnt}" />
							<c:out value='${blockCntNum}'/>
						</td>
					</tr>
					<tr>
						<th scope="row">성공대비</th>
						<td>
							<fmt:formatNumber var="succPerCont" type="percent" value="${sendResult.succCnt == 0 ? 0 : sendResult.openCnt / sendResult.succCnt}" />
							<c:out value='${succPerCont}'/>
						</td>
						<td>
							<fmt:formatNumber var="validPerCont" type="percent" value="${sendResult.succCnt == 0 ? 0 : sendResult.validCnt / sendResult.succCnt}" />
							<c:out value='${validPerCont}'/>
						</td>
						<td>
							<fmt:formatNumber var="clickCntCont" type="percent" value="${sendResult.succCnt == 0 ? 0 : sendResult.clickCnt / sendResult.succCnt}" />
							<c:out value='${clickCntCont}'/>
						</td>
						<td>
							<fmt:formatNumber var="blockCntCont" type="percent" value="${sendResult.succCnt == 0 ? 0 : sendResult.blockCnt / sendResult.succCnt}" />
							<c:out value='${blockCntCont}'/>
						</td>
					</tr>
					<tr>
						<th scope="row">전체대비</th>
						<td>
							<fmt:formatNumber var="succCntCont" type="percent" value="${sendResult.sendCnt == 0 ? 0 : sendResult.succCnt / sendResult.sendCnt}" />
							<c:out value='${succCntCont}'/>
						</td>
						<td>
							<fmt:formatNumber var="openCntCont" type="percent" value="${sendResult.sendCnt == 0 ? 0 : sendResult.openCnt / sendResult.sendCnt}" />
							<c:out value='${openCntCont}'/>
						</td>
						<td>
							<fmt:formatNumber var="validCntCont" type="percent" value="${sendResult.sendCnt == 0 ? 0 : sendResult.validCnt / sendResult.sendCnt}" />
							<c:out value='${validCntCont}'/>
						</td>
						<td>
							<fmt:formatNumber var="clickCntCont" type="percent" value="${sendResult.sendCnt == 0 ? 0 : sendResult.clickCnt / sendResult.sendCnt}" />
							<c:out value='${clickCntCont}'/>
						</td>
						<td>
							<fmt:formatNumber var="blockCntCont" type="percent" value="${sendResult.sendCnt == 0 ? 0 : sendResult.blockCnt / sendResult.sendCnt}" />
							<c:out value='${blockCntCont}'/>
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
				data: [${sendResult.blockCnt}, ${sendResult.clickCnt}, ${sendResult.validCnt}, ${sendResult.openCnt}, ${sendResult.succCnt}]
			}
		]
	};
	barChart.setOption(barChartoption);
	</script>
	<!-- //반응결과 -->
</c:if>

<c:if test="${not empty detailList}">
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
	<c:set var="step1" value=""/>
	<c:set var="step2" value=""/>
	<c:set var="failCnt" value="${0}"/>
	
	<c:forEach items="${detailList}" var="detail">
		<c:set var="step1" value="${detail.step1}"/>
		<c:set var="step2" value="${detail.step2}"/>
		<c:set var="failCnt" value="${detail.cntStep2}"/>
		<c:if test="${'001' eq step1}">
			<c:set var="sumStep1Err" value="${sumStep1Err + failCnt}"/>
			<c:choose>
				<c:when test="${'001' eq step2}">
					<c:set var="syntaxErrCnt" value="${failCnt}"/>
				</c:when>
				<c:when test="${'002' eq step2}">
					<c:set var="webAgentErrCnt" value="${failCnt}"/>
				</c:when>
				<c:when test="${'003' eq step2}">
					<c:set var="dbAgentErrCnt" value="${failCnt}"/>
				</c:when>
				<c:when test="${'004' eq step2}">
					<c:set var="mailBodyErrCnt" value="${failCnt}"/>
				</c:when>
				<c:when test="${'005' eq step2}">
					<c:set var="domainErrCnt" value="${failCnt}"/>
				</c:when>
				<c:when test="${'006' eq step2}">
					<c:set var="networkErrCnt" value="${failCnt}"/>
				</c:when>
			</c:choose>
		</c:if>
		<c:if test="${'002' eq step1}">
			<c:set var="sumStep2Err" value="${sumStep2Err + failCnt}"/>
			<c:choose>
				<c:when test="${'001' eq step2}">
					<c:set var="connectErrCnt" value="${failCnt}"/>
				</c:when>
				<c:when test="${'002' eq step2}">
					<c:set var="heloErrCnt" value="${failCnt}"/>
				</c:when>
				<c:when test="${'003' eq step2}">
					<c:set var="mailFromErrCnt" value="${failCnt}"/>
				</c:when>
				<c:when test="${'004' eq step2}">
					<c:set var="rcptToErrCnt" value="${failCnt}"/>
				</c:when>
				<c:when test="${'005' eq step2}">
					<c:set var="resetErrCnt" value="${failCnt}"/>
				</c:when>
				<c:when test="${'006' eq step2}">
					<c:set var="dataErrCnt" value="${failCnt}"/>
				</c:when>
				<c:when test="${'007' eq step2}">
					<c:set var="dotErrCnt" value="${failCnt}"/>
				</c:when>
				<c:when test="${'008' eq step2}">
					<c:set var="quitErrCnt" value="${failCnt}"/>
				</c:when>
			</c:choose>
		</c:if>
	</c:forEach>
	<!-- 세부에러 - 메일전송 전단계// -->
	<div class="graybox">
		<div class="title-area">
			<h3 class="h3-title">세부에러 - 메일전송 전단계</h3>
		</div>
		<div class="grid-area pt0 pb20">
			<table class="grid type-border">
				<caption>그리드 정보</caption>
				<colgroup>
					<col style="width:auto;">
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
							<fmt:formatNumber var="syntaxErrCntNum" type="number" value="${syntaxErrCnt}" />
							<c:out value='${syntaxErrCntNum}'/>
						</td>
						<td>
							<fmt:formatNumber var="webAgentErrCntNum" type="number" value="${webAgentErrCnt}" />
							<c:out value='${webAgentErrCntNum}'/>
						</td>
						<td>
							<fmt:formatNumber var="dbAgentErrCntNum" type="number" value="${dbAgentErrCnt}" />
							<c:out value='${dbAgentErrCntNum}'/>
						</td>
						<td>
							<fmt:formatNumber var="mailBodyErrCntNum" type="number" value="${mailBodyErrCnt}" />
							<c:out value='${mailBodyErrCntNum}'/>
						</td>
						<td>
							<fmt:formatNumber var="domainErrCntNum" type="number" value="${domainErrCnt}" />
							<c:out value='${domainErrCntNum}'/>
						</td>
						<td>
							<fmt:formatNumber var="networkErrCntNum" type="number" value="${networkErrCnt}" />
							<c:out value='${networkErrCntNum}'/>
						</td>
						<td>
							<fmt:formatNumber var="sumStep1ErrNum" type="number" value="${sumStep1Err}" />
							<c:out value='${sumStep1ErrNum}'/>
						</td>
					</tr>
					<tr>
						<td>
							<fmt:formatNumber var="syntaxErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : (syntaxErrCnt / totFailCnt)}" />
							<c:out value='${syntaxErrCntPer}'/>
						</td>
						<td>
							<fmt:formatNumber var="webAgentErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : (webAgentErrCnt / totFailCnt)}" />
							<c:out value='${webAgentErrCntPer}'/>
						</td>
						<td>
							<fmt:formatNumber var="dbAgentErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : (dbAgentErrCnt / totFailCnt)}" />
							<c:out value='${dbAgentErrCntPer}'/>
						</td>
						<td>
							<fmt:formatNumber var="mailBodyErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 :( mailBodyErrCnt / totFailCnt)}" />
							<c:out value='${mailBodyErrCntPer}'/>
						</td>
						<td>
							<fmt:formatNumber var="domainErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : (domainErrCnt / totFailCnt)}" />
							<c:out value='${domainErrCntPer}'/>
						</td>
						<td>
							<fmt:formatNumber var="networkErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : (networkErrCnt / totFailCnt)}" />
							<c:out value='${networkErrCntPer}'/>
						</td>
						<td>
							<fmt:formatNumber var="sumStep1ErrPer" type="percent" value="${totFailCnt == 0 ? 0 : (sumStep1Err / totFailCnt)}" />
							<c:out value='${sumStep1ErrPer}'/>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<!-- //세부에러 - 메일전송 전단계 -->
	
	<!-- 메일발송단계// -->
	<div class="graybox">
		<div class="title-area">
			<h3 class="h3-title">세부에러 - 메일발송 단계</h3>
		</div>
		<div class="grid-area pt0 pb20">
			<table class="grid type-border">
				<caption>그리드 정보</caption>
				<colgroup>
					<col style="width:auto;">
					<col style="width:11%;">
					<col style="width:13%;">
					<col style="width:11%;">
					<col style="width:11%;">
					<col style="width:10%;">
					<col style="width:10%;">
					<col style="width:10%;">
					<col style="width:11%;">
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
							<fmt:formatNumber var="connectErrCntNum" type="number" value="${connectErrCnt}" />
							<c:out value='${connectErrCntNum}'/>
						</td>
						<td>
							<fmt:formatNumber var="heloErrCntNum" type="number" value="${heloErrCnt}" />
							<c:out value='${heloErrCntNum}'/>
						</td>
						<td>
							<fmt:formatNumber var="mailFromErrCntNum" type="number" value="${mailFromErrCnt}" />
							<c:out value='${mailFromErrCntNum}'/>
						</td>
						<td>
							<fmt:formatNumber var="rcptToErrCntNum" type="number" value="${rcptToErrCnt}" />
							<c:out value='${rcptToErrCntNum}'/>
						</td>
						<td>
							<fmt:formatNumber var="resetErrCntNum" type="number" value="${resetErrCnt}" />
							<c:out value='${resetErrCntNum}'/>
						</td>
						<td>
							<fmt:formatNumber var="dataErrCntNum" type="number" value="${dataErrCnt}" />
							<c:out value='${dataErrCntNum}'/>
						</td>
						<td>
							<fmt:formatNumber var="dotErrCntNum" type="number" value="${dotErrCnt}" />
							<c:out value='${dotErrCntNum}'/>
						</td>
						<td>
							<fmt:formatNumber var="quitErrCntNum" type="number" value="${quitErrCnt}" />
							<c:out value='${quitErrCntNum}'/>
						</td>
						<td>
							<fmt:formatNumber var="sumStep2ErrNum" type="number" value="${sumStep2Err}" />
							<c:out value='${sumStep2ErrNum}'/>
						</td>
					</tr>
					<tr>
						<td>
							<fmt:formatNumber var="connectErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : connectErrCnt / totFailCnt}" />
							<c:out value='${connectErrCntPer}'/>
						</td>
						<td>
							<fmt:formatNumber var="heloErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : heloErrCnt / totFailCnt}" />
							<c:out value='${heloErrCntPer}'/>
						</td>
						<td>
							<fmt:formatNumber var="mailFromErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : mailFromErrCnt / totFailCnt}" />
							<c:out value='${mailFromErrCntPer}'/>
						</td>
						<td>
							<fmt:formatNumber var="rcptToErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : rcptToErrCnt / totFailCnt}" />
							<c:out value='${rcptToErrCntPer}'/>
						</td>
						<td>
							<fmt:formatNumber var="resetErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : resetErrCnt / totFailCnt}" />
							<c:out value='${resetErrCntPer}'/>
						</td>
						<td>
							<fmt:formatNumber var="dataErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : dataErrCnt / totFailCnt}" />
							<c:out value='${dataErrCntPer}'/>
						</td>
						<td>
							<fmt:formatNumber var="dotErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : dotErrCnt / totFailCnt}" />
							<c:out value='${dotErrCntPer}'/>
						</td>
						<td>
							<fmt:formatNumber var="quitErrCntPer" type="percent" value="${totFailCnt == 0 ? 0 : quitErrCnt / totFailCnt}" />
							<c:out value='${quitErrCntPer}'/>
						</td>
						<td>
							<fmt:formatNumber var="sumStep2ErrPer" type="percent" value="${totFailCnt == 0 ? 0 : sumStep2Err / totFailCnt}" />
							<c:out value='${sumStep2ErrPer}'/>
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
	<!-- //메일발송단계 -->
</c:if>

<!-- btn-wrap// -->
<div class="btn-wrap">
	<!-- <button type="button" class="btn big fullblue" onclick="goExcelDown('Summ');">엑셀 다운로드</button> -->
	<button type="button" class="btn big" onclick="goList();">목록</button>
</div>
<!-- //btn-wrap -->
