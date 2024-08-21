<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.09.22
	*	설명 : 월별통계 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_rns.jsp" %>

<script type="text/javascript" src="<c:url value='/js/rns/ana/monthListP.js'/>"></script>

<body>
	<div id="wrap" class="rns">

		<!-- lnb// -->
		<div id="lnb">
			<!-- LEFT MENU -->
			<%@ include file="/WEB-INF/jsp/inc/menu_rns.jsp" %>
			<!-- LEFT MENU -->
		</div>
		<!-- //lnb -->

		<!-- content// -->
		<div id="content">

			<!-- cont-head// -->
			<section class="cont-head">
				<div class="title">
					<h2>자동메일 월별 발송통계</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">

				<form id="searchForm" name="searchForm" method="post">
					<input type="hidden" id="page" name="page" value="1">
					<fieldset>
						<legend>조회 및 목록</legend>

						<!-- 조회// -->
						<div class="graybox">
							<div class="title-area">
								<h3 class="h3-title">조회</h3>
							</div>
							
							<div class="list-area">
								<ul>
									<li>
										<label>발송일자</label>
										<div class="list-item">
											<!-- datepickerrange// -->
												<div class="datepicker">
												<label>
													<input type="text" id="searchYm" name="searchYm" class="input-date" readonly />
												</label>
											</div>
										</div>
									</li>
<!-- 								<li>
										<label>상태</label>
										<div class="list-item">
											<div class="select">
												<select title="옵션 선택">
													<option>발송현황</option>
													<option>실패현황</option>
												</select>
											</div>
										</div>
									</li> -->
									<li>
										<label>서비스</label>
										<div class="list-item">
											<div class="select">
												<select id="searchService" name="searchService" title="옵션 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(serviceCodeList) > 0}">
														<c:forEach items="${serviceCodeList}" var="serviceCode">
															<option value="<c:out value='${serviceCode.cd}'/>"<c:if test="${serviceCode.cd eq searchVO.searchService}"> selected</c:if>><c:out value='${serviceCode.cdNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
									<li>
										<label>발송자명</label>
										<div class="list-item">
											<div class="select">
												<select id="searchSname" name="searchSname" title="옵션 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(senderList) > 0}">
														<c:forEach items="${senderList}" var="sender">
															<option value="<c:out value='${sender}'/>"<c:if test="${sender eq searchVO.searchSname}"> selected</c:if>><c:out value='${sender}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
								</ul>
							</div>
						</div>
						<!-- //조회 -->

						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullpurple" onclick="goSearch();">검색</button>
							<button type="button" class="btn big" onclick="goInit();">초기화</button>
						</div>
						<!-- //btn-wrap -->

						<!-- 목록&페이징// -->
						<div id="divMonthDataList" style="margin-top:50px;"></div>
						<!-- //목록&페이징 -->
					
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
