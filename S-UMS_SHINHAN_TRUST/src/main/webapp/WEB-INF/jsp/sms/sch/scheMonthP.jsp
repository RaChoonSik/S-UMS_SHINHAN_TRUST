<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.09.08
	*	설명 : 월간일정 조회 화면	
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sms.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sms/sch/scheMonthP.js'/>"></script>
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
					<h2>월간일정</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
 				<form id="searchForm" name="searchForm" method="post">
 					<input type="hidden" id="msgid" name="msgid" value="">
					<input type="hidden" id="keygen" name="keygen" value="">
					<!-- 월간일정// -->
					<div class="month-wrap">
						<h3 class="hide">월간일정 달력</h3>
						
						<!-- calendar-date// -->
						<div class="calendar-date">
							<button type="button" class="btn-prev" onclick="goPrev()">저번 달</button>
							<input type="text" id="monthPicker" name="monthPicker" class="input-date" dateformat="" readonly /> 
							<button type="button" class="btn-next" onclick="goNext()">다음 주</button>
							<button type="button" class="btn-calendar">달력</button>
						</div>
						  
						<!-- //calendar-date -->
	
						<!-- calendar state// -->
						<ul class="calendar-state">
							<li class="wait">대기</li>
							<li class="ing">진행</li>
							<li class="end">발송요청</li>
						</ul>
						<!-- //calendar state -->
	
						<!-- calendar-month// -->
						<div class="calendar-month" id="month">						
							<table>
								<caption>월간일정 표</caption>
								<colgroup>
									<col style="width:14.2%;">
									<col style="width:14.2%;">
									<col style="width:14.2%;">
									<col style="width:14.2%;">
									<col style="width:14.2%;">
									<col style="width:14.2%;">
									<col style="width:14.2%;">
								</colgroup>
								<thead>
									<tr>
										<th scope="col" class="color-red">일요일</th>
										<th scope="col">월요일</th>
										<th scope="col">화요일</th>
										<th scope="col">수요일</th>
										<th scope="col">목요일</th>
										<th scope="col">금요일</th>
										<th scope="col" class="color-blue">토요일</th>
									</tr>
								</thead>
								<tbody>
									<tr id="week11">
										<td class="color-red" id="day11"></td>
										<td id="day12"></td>
										<td id="day13"></td>
										<td id="day14"></td>
										<td id="day15"></td>
										<td id="day16"></td>
										<td class="color-blue" id="day17"></td>
									</tr>
									<tr id="week2">
										<td class="color-red" id="day21"></td>
										<td id="day22"></td>
										<td id="day23"></td>
										<td id="day24"></td>
										<td id="day25"></td>
										<td id="day26"></td>
										<td class="color-blue" id="day27"></td>
									</tr>
									<tr id="week3">
										<td class="color-red" id="day31"></td>
										<td id="day32"></td>
										<td id="day33"></td>
										<td id="day34"></td>
										<td id="day35"></td>
										<td id="day36"></td>
										<td class="color-blue" id="day37"></td>
									</tr>
									<tr id="week4">
										<td class="color-red" id="day41"></td>
										<td id="day42"></td>
										<td id="day43"></td>
										<td id="day44"></td>
										<td id="day45"></td>
										<td id="day46"></td>
										<td class="color-blue" id="day47"></td>
									</tr>
									<tr id="week5">
										<td class="color-red" id="day51"></td>
										<td id="day52"></td>
										<td id="day53"></td>
										<td id="day54"></td>
										<td id="day55"></td>
										<td id="day56"></td>
										<td class="color-blue" id="day57"></td>
									</tr>
									<tr id="week6">
										<td class="color-red" id="day61"></td>
										<td id="day62"></td>
										<td id="day63"></td>
										<td id="day64"></td>
										<td id="day65"></td>
										<td id="day66"></td>
										<td class="color-blue" id="day67"></td>
									</tr>									
								</tbody>
							</table>
						</div>
						<!-- //calendar-month -->
	
					</div>
					<!-- //월간일정 -->
				</form>
			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>

	<!-- 일간일정// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_sms_day_schedule.jsp" %>
	<!-- //일간일정 -->
</body>
</html>
