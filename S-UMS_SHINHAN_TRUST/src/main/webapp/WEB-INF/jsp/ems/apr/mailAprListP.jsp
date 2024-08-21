<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.23
	*	설명 : 메일발송결재 목록 조회
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp" %>

<script type="text/javascript" src="<c:url value='/js/ems/apr/mailAprListP.js'/>"></script>

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
					<h2>
						<c:if test="${'Y' eq searchVO.topNotiYn}">
							<c:out value="${NEO_USER_NM}"/> 발송결재 목록
						</c:if>
						<c:if test="${'Y' ne searchVO.topNotiYn}">
							메일발송결재 목록
						</c:if>
					</h2>
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
					<input type="hidden" id="taskNo" name="taskNo" value="<c:out value='${searchVO.taskNo}'/>">
					<input type="hidden" id="subTaskNo" name="subTaskNo" value="<c:out value='${searchVO.subTaskNo}'/>">
					<input type="hidden" id="topNotiYn" name="topNotiYn" value="<c:out value='${searchVO.topNotiYn}'/>">
					<input type="hidden" id="tid" name="tid" value="<c:out value='${searchVO.taskNo}'/>">
					
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
										<label>등록일시</label>
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
										<label>메일명</label>
										<div class="list-item">
											<input type="text" id="searchTaskNm" name="searchTaskNm" value="<c:out value='${searchVO.searchTaskNm}'/>" placeholder="메일명을 입력해주세요.">
										</div>
									</li>
									<li>
										<label>캠페인명</label>
										<div class="list-item">
											<div class="select">
												<select id="searchCampNo" name="searchCampNo" title="캠페인명 선택">
													<option value="0">선택</option>
													<c:if test="${fn:length(campList) > 0}">
														<c:forEach items="${campList}" var="camp">
															<option value="<c:out value='${camp.campNo}'/>"<c:if test="${camp.campNo == searchVO.searchCampNo}"> selected</c:if>><c:out value='${camp.campNm}'/></option>
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
												<select id="searchDeptNo" name="searchDeptNo" onchange="getUserList(this.value);" title="사용자그룹 선택">
													<option value="0">선택</option>
													<c:if test="${fn:length(deptList) > 0}">
														<c:forEach items="${deptList}" var="dept">
															<option value="<c:out value='${dept.deptNo}'/>"<c:if test="${dept.deptNo eq searchVO.searchDeptNo}"> selected</c:if>><c:out value='${dept.deptNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
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
										<label>발송상태</label>
										<div class="list-item">
											<div class="select">
												<select id="searchWorkStatus" name="searchWorkStatus" title="발송상태 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(workStatusList) > 0}">
														<c:forEach items="${workStatusList}" var="status">
															<option value="<c:out value='${status.cd}'/>"<c:if test="${status.cd eq searchVO.searchWorkStatus}"> selected</c:if>><c:out value='${status.cdNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
									<li>
										<label>결재자 그룹</label>
										<div class="list-item">
											<div class="select">
												<%-- 관리자의 경우 전체 요청그룹을 전시하고 그 외의 경우에는 해당 그룹만 전시함 --%>
												<c:if test="${'Y' eq NEO_ADMIN_YN}">
													<select id="searchAprDeptNo" name="searchAprDeptNo" onchange="getUserListApr(this.value);" title="결재자그룹 선택">
														<option value="0">선택</option>
														<c:if test="${fn:length(deptList) > 0}">
															<c:forEach items="${deptList}" var="dept">
																<option value="<c:out value='${dept.deptNo}'/>"<c:if test="${dept.deptNo eq searchVO.searchAprDeptNo}"> selected</c:if>><c:out value='${dept.deptNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</c:if>
												<c:if test="${'N' eq NEO_ADMIN_YN}">
													<select id="searchAprDeptNo" name="searchAprDeptNo" onchange="getUserListApr(this.value);" title="승인예정그룹 선택">
														<c:if test="${fn:length(deptList) > 0}">
															<c:forEach items="${deptList}" var="dept">
																<c:if test="${dept.deptNo == searchVO.searchAprDeptNo}">
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
										<label>결재자명</label>
										<div class="list-item">
											<div class="select">
												<select id="searchAprUserId" name="searchAprUserId" title="결재자명 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(userList) > 0}">
														<c:forEach items="${userList}" var="user">
															<option value="<c:out value='${user.userId}'/>"<c:if test="${user.userId eq searchVO.searchAprUserId}"> selected</c:if>><c:out value='${user.userNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
									<li>
										<label>메일유형</label>
										<div class="list-item">
											<div class="select">
												<select id="searchSvcType" name="searchSvcType" title="메일유형 선택">
													<option value="">선택</option>
													<option value="00">단기메일</option> 
													<option value="10">정기메일</option> 
													<option value="20">실시간</option> 
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
							<button type="button" class="btn big fullblue" onclick="goSearch('1');">검색</button>
							<button type="button" class="btn big" onclick="goInit('<c:out value='${NEO_ADMIN_YN}'/>','<c:out value='${NEO_DEPT_NO}'/>');">초기화</button>
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
	
	<!-- 수신자그룹미리보기팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_seg_mail.jsp" %>
	<!-- //수신자그룹미리보기팝업 -->
	
	<!-- 조회사유팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_search_reason.jsp" %>
	<!-- //조회사유팝업 -->
	
</body>
</html>
