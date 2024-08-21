<%--
	/**********************************************************
	*	작성자 : 이혜민
	*	작성일시 : 2022.10.26
	*	설명 : 캠페인별 문자발송 분석
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sms.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sms/ana/campSmsListP.js'/>"></script>
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
					<h2>템플릿별 문자 발송현황</h2>
				</div>
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->
			<!-- cont-body// -->
			<section class="cont-body">
			<form id="searchForm" name="searchForm" method="post">
					<input type="hidden" id="page" name="page" value="<c:out value='${searchVO.page}'/>">
					<input type="hidden" id="rows" name="rows" value="<c:out value='${searchVO.rows}'/>">
					<input type="hidden" id="msgid" name="msgid" value="">
					<input type="hidden" id="keygen" name="keygen" value="">
					<input type="hidden" id="rsltCd" name="rsltCd" value="">
					<input type="hidden" id="searchTempNm" name="searchTempNm" value="<c:out value='${searchVO.searchTempNm}'/>">
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
										<label>템플릿명</label>
										<div class="list-item">
											<div class="select">
												<select id="searchTempCd" name="searchTempCd" title="템플릿 선택">
													<option value="ALL">선택</option>
													<c:if test="${fn:length(tempList) > 0}">
														<c:forEach items="${tempList}" var="temp">
															<option value="<c:out value='${temp.tempCd}'/>"<c:if test="${temp.tempCd eq searchVO.searchTempCd}"> selected</c:if>><c:out value='${temp.tempNm}'/></option>
														</c:forEach>
													</c:if>
													<option value="999999">웹발송</option>
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
													<input type="text" id="searchEndDt"  name="searchEndDt" value="<c:out value='${searchEndDt}'/>" readonly>
												</label>
											</div>
											<!-- //datepickerrange -->
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
																<option value="<c:out value='${dept.deptNo}'/>"<c:if test="${dept.deptNo == searchVO.searchDeptNo}"> selected</c:if>><c:out value='${dept.deptNm}'/></option>
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
															<option value="<c:out value='${user.userId}'/>"<c:if test="${user.userId eq searchVO.searchUserId}"> selected</c:if>><c:out value='${user.userNm}'/></option>
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
							<button type="button" class="btn big fullgreen" onclick="goSearch('1');">검색</button>
							<button type="button" class="btn big" onclick="goInit('<c:out value='${NEO_ADMIN_YN}'/>','<c:out value='${NEO_DEPT_NO}'/>');">초기화</button>
						</div>
						<!-- //btn-wrap -->
						<!-- 목록&페이징// -->
						<div id="divCampSmsList" style="margin-top:50px;"></div>
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
