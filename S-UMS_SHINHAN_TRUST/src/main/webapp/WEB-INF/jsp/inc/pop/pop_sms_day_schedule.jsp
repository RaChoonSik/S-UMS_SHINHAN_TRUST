<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.10.19
	*	설명 : SMS 일별 일정 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 팝업 : 발송현황 일간일정// -->
<div id="popup_sms_day_calendar" class="poplayer popcalendar"><!-- id값 수정 가능 -->
	<div class="inner">
		<header>
			<h2 id="displyYmdFull"><!-- 2021.07.15 목요일 --></h2>
		</header>
		<div class="popcont">
			<div class="cont">

				<div class="datebannerwrap">
					<div class="swiper-container datebannerswiper">
						<div class="swiper-wrapper">
							<div class="swiper-slide">
								<!-- calendar state// -->
								<ul class="calendar-state">
									<li class="wait"id="schDesc"></li>
									<li class="ing" id="ingDesc"></li>
									<li class="end" id="endDesc"></li>
								</ul>
								<!-- //calendar state -->
								<input type="hidden" id="sendDate" name="sendDate" value="">
								<!-- date-list// -->
								<div class="date-list" id="popSmsDayList">
								<!-- 
									<div class="wait">
										<span class="time">10:00</span>
										<p>
											<a href="javascript:;">[광고]테스트 광고입니다.</a>
											<span>이벤트혜택  [A이벤트대상자] (관리자A) <em>발송완료</em></span>
										</p>
									</div>
									<div class="wait">
										<span class="time">10:30</span>
										<p>
											<a href="javascript:;">[광고]테스트 광고입니다.</a>
											<span>이벤트혜택  [A이벤트대상자] (관리자A) <em class="color-red">발송실패</em></span>
										</p>
									</div>
									<div class="ing">
										<span class="time">12:00</span>
										<p>
											<a href="javascript:;">[1차][광고]테스트 광고입니다.</a>
											<span>이벤트혜택  [수신자그룹] (발송자명) <em>발송승인</em></span>
										</p>
									</div>
									<div class="end">
										<span class="time">15:00</span>
										<p>
											<a href="javascript:;">[1차][광고]테스트 광고입니다.</a>
											<span>이벤트혜택  [수신자그룹] (발송자명) <em>발송대기</em></span>
										</p>
									</div>
								 -->
								</div>
								<!-- //date-list -->
							</div> 
						</div>
					</div>
					<div class="swiper-button-prev-date" onclick="goDay('M')" ></div>
					<div class="swiper-button-next-date" onclick="goDay('p')"></div>
				</div>

			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_sms_day_calendar');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
<!-- //팝업 : 발송현황 일간일정 -->