<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.09.07
	*	설명 : 통계분석 기간별누적분석 목록 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp" %>

<script type="text/javascript" src="<c:url value='/js/ems/ana/summMainP.js'/>"></script>

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
					<h2>기간별누적분석 목록</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">

				<form id="searchForm" name="searchForm" method="post">
					<fieldset>
						<legend>조회</legend>

						<!-- 조회// -->
						<div class="graybox">
							<div class="title-area">
								<h3 class="h3-title">조회</h3>
							</div>
							
							<div class="list-area">
								<ul>
									<li>
										<label>캠페인명</label>
										<div class="list-item">
											<div class="select">
												<select id="searchCampNo" name="searchCampNo" title="캠페인명 선택">
													<option value="0">선택</option>
													<c:if test="${fn:length(campList) > 0}">
														<c:forEach items="${campList}" var="camp">
															<option value="<c:out value='${camp.campNo}'/>"><c:out value='${camp.campNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
									<li>
										<label>사용자그룹</label>
										<div class="list-item">
											<div class="select">
												<%-- 관리자의 경우 전체 요청그룹을 전시하고 그 외의 경우에는 해당 그룹만 전시함 --%>
												<c:if test="${'Y' eq NEO_ADMIN_YN}">
													<select id="searchDeptNo" name="searchDeptNo" onchange="getUserListSearch(this.value);" title="사용자그룹 선택">
														<option value="0">선택</option>
														<c:if test="${fn:length(deptList) > 0}">
															<c:forEach items="${deptList}" var="dept">
																<option value="<c:out value='${dept.deptNo}'/>"<c:if test="${dept.deptNo == NEO_DEPT_NO}"> selected</c:if>><c:out value='${dept.deptNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</c:if>
												<c:if test="${'N' eq NEO_ADMIN_YN}">
													<select id="searchDeptNo" name="searchDeptNo" onchange="getUserListSearch(this.value);" title="사용자그룹 선택">
														<c:if test="${fn:length(deptList) > 0}">
															<c:forEach items="${deptList}" var="dept">
																<c:if test="${dept.deptNo == searchVO.searchDeptNo}">
																	<option value="<c:out value='${dept.deptNo}'/>" selected><c:out value='${dept.deptNm}'/></option>
																</c:if>
															</c:forEach>
														</c:if>
													</select>
												</c:if>
											</div>
										</div>
									</li>
									<li>
										<label>사용자명</label>
										<div class="list-item">
											<div class="select">
												<select id="searchUserId" name="searchUserId" title="사용자명 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(userList) > 0}">
														<c:forEach items="${userList}" var="user">
															<option value="<c:out value='${user.userId}'/>"<c:if test="${user.userId eq NEO_USER_ID}"> selected</c:if>><c:out value='${user.userNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
									<li>
										<label>발송일시</label>
										<div class="list-item">
											<!-- datepickerrange// -->
											<div class="datepickerrange fromDate">
												<label>
													<fmt:parseDate var="startDt" value="${searchVO.searchStartDt}" pattern="yyyyMMdd"/>
													<fmt:formatDate var="searchStartDt" value="${startDt}" pattern="yyyy.MM.dd"/> 
													<input type="text" id="searchStartDt" name="searchStartDt" value="<c:out value='${searchStartDt}'/>" readonly>
												</label>
											</div>
											<span class="hyppen date"></span>
											<div class="datepickerrange toDate">
												<label>
													<fmt:parseDate var="endDt" value="${searchVO.searchEndDt}" pattern="yyyyMMdd"/>
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
							<button type="button" class="btn big fullblue" onclick="goSearch();">검색</button>
							<button type="button" class="btn big" onclick="goInit();">초기화</button>
						</div>
						<!-- //btn-wrap -->
						
						<!-- 기간별분석// -->
						<div class="graybox">
							<div class="title-area">
								<h3 class="h3-title">기간별분석</h3>
							</div>

							<div class="table-area">
								<table>
									<caption>기간별분석</caption>
									<colgroup>
										<col style="width:15%">
										<col style="width:35%">
										<col style="width:15%">   
										<col style="width:35%">
									</colgroup> 
									<tbody>
										<tr>
											<th>발송일시</th>
											<td id="txtStartEndDt"></td>
											<th>캠페인명</th>
											<td id="txtCampNm"></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<!-- //기간별분석 -->
						
						<!-- tab// -->
						<div class="tab">
							<div class="tab-menu col7" id="tabMenuChange">
								<a href="javascript:goStatTab(1);" class="active">월별</a>
								<a href="javascript:goStatTab(2);">요일별</a>
								<a href="javascript:goStatTab(3);">일자별</a>
								<a href="javascript:goStatTab(4);">도메인별</a>
								<a href="javascript:goStatTab(5);">사용자그룹별</a>
								<a href="javascript:goStatTab(6);">사용자별</a>
								<a href="javascript:goStatTab(7);">캠페인별</a>
							</div>
							<div class="tab-cont">
								<!-- tab-cont : 월별// -->
								<div class="active" id="divSummMonthP"></div>
								<!-- //tab-cont : 월별 -->

								<!-- tab-cont : 요일별// -->
								<div id="divSummWeekP"></div>
								<!-- //tab-cont : 요일별 -->

								<!-- tab-cont : 일자별// -->
								<div id="divSummDateP"></div>
								<!-- //tab-cont : 일자별 -->

								<!-- tab-cont : 도메인별// -->
								<div id="divSummDomainP"></div>
								<!-- //tab-cont : 도메인별 -->

								<!-- tab-cont : 사용자그룹별// -->
								<div id="divSummDeptP"></div>
								<!-- //tab-cont : 사용자그룹별 -->

								<!-- tab-cont : 사용자별// -->
								<div id="divSummUserP"></div>
								<!-- //tab-cont : 사용자별 -->

								<!-- tab-cont : 캠페인별// -->
								<div id="divSummCampP"></div>
								<!-- //tab-cont : 캠페인별 -->


							</div>
						</div>
						<!-- //tab -->
							
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
