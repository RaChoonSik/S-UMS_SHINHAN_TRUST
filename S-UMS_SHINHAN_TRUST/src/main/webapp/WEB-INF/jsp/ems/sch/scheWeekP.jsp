<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.05
	*	설명 : 주간일정 조회 화면
	*	수정자 : 김준희
	*	작성일시 : 2021.09.08
	*	설명 : 주간일정 조회 화면	
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp" %>

<script type="text/javascript" src="<c:url value='/js/ems/sch/scheWeekP.js'/>"></script>

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
					<h2>주간일정</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
 				<form id="searchForm" name="searchForm" method="post">
 					<input type="hidden" id="taskNo" name="taskNo" value="0">
					<input type="hidden" id="subTaskNo" name="subTaskNo" value="0">
				<!-- 주간일정// -->
				<div class="week-wrap">
					<h3 class="hide">주간일정 스케줄</h3>

					<!-- calendar-date// -->
					<div class="calendar-date">
						<button type="button" class="btn-prev" onclick="goPrev()">저번 주</button>
						<input type="text" id ="weekpicker" class="input-date"  style="display:none;">
						<input type="text" id="weekInfo" class="input-date"  readonly>
						<button type="button" class="btn-next" onclick="goNext()">다음 주</button>
						<button type="button" class="btn-calendar">달력</button>
					</div>
					<!-- //calendar-date -->

					<!-- calendar state// -->
					<ul class="calendar-state">
<!-- 						<li class="payment">결재</li> -->
						<li class="wait">예약</li>
						<li class="ing">진행</li>
						<li class="end">완료</li>
					</ul>
					<!-- //calendar state -->

					<!-- calendar-week// -->
					<div class="calendar-week">
						<ul id="weekScheList">
							<li>
								<div class="color-red" id="day0">일요일</div>
								<div class="date-list" id="dayList0"></div>
							</li>
							<li>
								<div id="day1">월요일</div>
								<div class="date-list" id="dayList1"></div>
							</li>
							<li>
								<div id="day2">화요일</div>
								<div class="date-list" id="dayList2"></div>
							</li>
							<li>
								<div id="day3"> <!-- ><span class="today">Today</span>-->수요일</div>
								<div class="date-list" id="dayList3"></div>
							</li>
							<li>
								<div id="day4">목요일</div>
								<div class="date-list" id="dayList4"></div>
							</li>
							<li>
								<div id="day5">금요일</div>
								<div class="date-list" id="dayList5"></div>
							</li>
							<li>
								<div class="color-blue" id="day6">토요일</div>
								<div class="date-list" id="dayList6"></div>
							</li>
						</ul>
					</div>
					<!-- //calendar-week -->

				</div>
				<!-- //주간일정 -->  
				</form>	
			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>
	
</body>
</html>
