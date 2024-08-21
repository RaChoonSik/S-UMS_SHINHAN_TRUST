<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.31
	*	설명 : 정기메일 차수별 발송목록 화면 출력
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp" %>

<script type="text/javascript" src="<c:url value='/js/ems/cam/subTaskListP.js'/>"></script>

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
					<h2>정기메일 차수별 발송목록</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
			
				<form id="prevSearchForm" name="" method="post">
					<input type="hidden" name="page" value="<c:out value='${searchVO.page}'/>">
					<input type="hidden" name="searchStartDt" value="<c:out value='${searchVO.searchStartDt}'/>">
					<input type="hidden" name="searchEndDt" value="<c:out value='${searchVO.searchEndDt}'/>">
					<input type="hidden" name="searchTaskNm" value="<c:out value='${searchVO.searchTaskNm}'/>">
					<input type="hidden" name="searchCampNo" value="<c:out value='${searchVO.searchCampNo}'/>">
					<input type="hidden" name="searchDeptNo" value="<c:out value='${searchVO.searchDeptNo}'/>">
					<input type="hidden" name="searchUserId" value="<c:out value='${searchVO.searchUserId}'/>">
					<input type="hidden" name="searchStatus" value="<c:out value='${searchVO.searchStatus}'/>">
					<input type="hidden" name="searchWorkStatus" value="<c:out value='${searchVO.searchWorkStatus}'/>">
					<input type="hidden" name="searchSendRepeat" value="<c:out value='${searchVO.searchSendRepeat}'/>">
					<input type="hidden" id="taskNo" name="taskNo" value="<c:out value='${searchVO.taskNo}'/>">
					<input type="hidden" id="subTaskNo" name="subTaskNo" value="<c:out value='${searchVO.subTaskNo}'/>">
					<input type="hidden" id="approvalProcAppYn" name="approvalProcAppYn" value="">					
				</form>

				<form id="searchForm" name="searchForm" method="post">
					<input type="hidden" id="page" name="page" value="<c:out value='${searchVO.page}'/>">
					<input type="hidden" name="taskNo" value="<c:out value='${searchVO.taskNo}'/>">
					<input type="hidden" name="subTaskNo" value="<c:out value='${searchVO.subTaskNo}'/>">
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
										<label>발송일시</label>
										<div class="list-item">
											<!-- datepickerrange// -->
											<div class="datepickerrange fromDate">
												<label>
													<fmt:parseDate var="startDt" value="${subSearchVO.searchStartDt}" pattern="yyyyMMdd"/>
													<fmt:formatDate var="searchStartDt" value="${startDt}" pattern="yyyy.MM.dd"/> 
													<input type="text" id="searchStartDt" name="searchStartDt" value="<c:out value='${searchStartDt}'/>" readonly>
												</label>
											</div>
											<span class="hyppen date"></span>
											<div class="datepickerrange toDate">
												<label>
													<fmt:parseDate var="endDt" value="${subSearchVO.searchEndDt}" pattern="yyyyMMdd"/>
													<fmt:formatDate var="searchEndDt" value="${endDt}" pattern="yyyy.MM.dd"/> 
													<input type="text" id="searchEndDt" name="searchEndDt" value="<c:out value='${searchEndDt}'/>" readonly>
												</label>
											</div>
											<!-- //datepickerrange -->
										</div>
									</li>
								</ul>
							</div>
						</div>
						<!-- //조회 -->

						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullblue" onclick="goSearch('1');">검색</button>
							<button type="button" class="btn big" onclick="goInit();">초기화</button>
						</div>
						<!-- //btn-wrap -->

						<!-- 목록&페이징// -->
						<div id="divMailList" style="margin-top:50px;"></div>
						<!-- //목록&페이징 -->
							
					</fieldset>
				</form>

			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>
</body>
</html>
