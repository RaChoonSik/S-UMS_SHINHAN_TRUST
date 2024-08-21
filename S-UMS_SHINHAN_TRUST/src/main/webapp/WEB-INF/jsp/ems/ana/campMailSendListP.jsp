<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.01.13
	*	설명 :  캠페인별 발송 현황 조회 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp" %>


<script type="text/javascript" src="<c:url value='/js/ems/ana/campMailSendListP.js'/>"></script>

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
					<h2>캠페인별 발송현황</h2>
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
					<input type="hidden" id="searchServiceGb" name="searchServiceGb" value="<c:out value='${searchVO.searchServiceGb}'/>">
					<input type="hidden" id="searchCampNo" name="searchCampNo" value="<c:out value='${searchVO.searchCampNo}'/>">
					<input type="hidden" id="searchCampNm" name="searchCampNm" value="<c:out value='${searchVO.searchCampNm}'/>">
					
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
									<li>
										<label>메일유형</label>
										<div class="list-item">
											<label for="rdo_01" name="rdo_01"><input type="radio" id="rdo_01" value="Y" name="isSendTerm" <c:if test="${'10' eq searchVO.searchServiceGb}"> checked</c:if>><span>이메일(단기/정기)</span></label>
											<label for="rdo_02" name="rdo_02"><input type="radio" id="rdo_02" value="N" name="isSendTerm" <c:if test="${'20' eq searchVO.searchServiceGb}"> checked</c:if>><span>실시간이메일</span></label>
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
																<option value="<c:out value='${dept.deptNo}'/>"<c:if test="${dept.deptNo eq searchVO.searchDeptNo}"> selected</c:if>><c:out value='${dept.deptNm}'/></option>
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
									<li>
										<label>캠페인명</label>
										<div class="list-item">
											<div class="filebox">
												<c:if test= "${0 eq searchVO.searchCampNo}">
													<p class="label bg-gray" id="txtCampNm" style="width:calc(100% - 38px);">선택된 캠페인이 없습니다.</p>
												</c:if>
												<c:if test= "${0 ne searchVO.searchCampNo}">
													<p class="label bg-gray" id="txtCampNm" style="width:calc(100% - 38px);"><c:out value='${searchVO.searchCampNm}'/></p>
												</c:if>
												<button type="button" class="btn btn-search" onclick="popCampRnsSelect();"><span class="hidden">선택</span></button>
											</div>
										</div>
									</li>
								</ul>
							</div>
						</div>
						<!-- //조회 -->

						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullblue" onclick="goSearch('1');">검색</button>
							<button type="button" class="btn big" onclick="goInit('<c:out value='${NEO_ADMIN_YN}'/>','<c:out value='${NEO_DEPT_NO}'/>');">초기화</button>
						</div>
						<!-- //btn-wrap -->

						<!-- 목록&페이징// -->
						<div id="divCampMailSendList" style="margin-top:50px;"></div>
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
	
	<!-- 캠페인+RNS서비스선택 팝업// -->
	<%@ include file="/WEB-INF/jsp/ems/cam/pop/pop_campaign_rns.jsp" %>
	<!-- //캠페인+RNS서비스선택 팝업 -->
</body>
</html>
