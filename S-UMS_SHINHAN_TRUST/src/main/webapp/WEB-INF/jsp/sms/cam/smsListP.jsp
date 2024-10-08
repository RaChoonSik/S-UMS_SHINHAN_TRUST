<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.10.19
	*	설명 : 뮨자 발송목록 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sms.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sms/cam/smsListP.js'/>"></script>

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
					<h2>문자 발송목록</h2>
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
					<input type="hidden" id="msgid" name="msgid" value="">
					<input type="hidden" id="keygen" name="keygen" value="">
					<input type="hidden" id="campNo" name="campNo" value="<c:out value='${searchVO.campNo}'/>"/>
					<input type="hidden" id="status" name="status">
					<input type="hidden" id="smsStatus" name="smsStatus">
					
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
										<label>예약일시</label>
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
										<label>문자명</label>
										<div class="list-item">
											<input type="text" id="searchTaskNm" name="searchTaskNm" placeholder="문자명을 입력해주세요.">
										</div>
									</li>
									<li>
										<label>전송유형</label>
										<div class="list-item">
											<div class="select">
												<select id="searchGubun" name="searchGubun" title="전송유형 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(gubunList) > 0}">
														<c:forEach items="${gubunList}" var="gubun" varStatus="varStatus">
															<c:if test="${varStatus.index < 2}">
																<option value="<c:out value='${gubun.cd}'/>"<c:if test="${gubun.cd eq searchVO.searchGubun}"> selected</c:if>><c:out value='${gubun.cdNm}'/></option>
															</c:if>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
									<li>
										<label>캠페인명</label>
										<div class="list-item">
											<div class="select">
												<select id="searchCampNo" name="searchCampNo" title="캠페인명 선택">
													<option value="0">선택</option>
													<c:if test="${fn:length(smsCampList) > 0}">
														<c:forEach items="${smsCampList}" var="camp">
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
											<c:if test="${'Y' eq NEO_ADMIN_YN}">
												<select id="searchUserId" name="searchUserId" title="사용자명 선택">
													<option value="">선택</option>
												</select>
											</c:if>
											<c:if test="${'N' eq NEO_ADMIN_YN}">
												<select id="searchUserId" name="searchUserId" title="사용자명 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(userList) > 0}">
														<c:forEach items="${userList}" var="user">
															<option value="<c:out value='${user.userId}'/>"<c:if test="${user.userId eq searchVO.searchUserId}"> selected</c:if>><c:out value='${user.userNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</c:if>
											</div>
										</div>
									</li>
									<li>
										<label>발송상태</label>
										<div class="list-item">
											<div class="select">
												<select id="searchStatus" name="searchStatus" title="발송상태 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(statusList) > 0}">
														<c:forEach items="${statusList}" var="status" varStatus="varStatus">
															<c:if test="${varStatus.index < 3}">
																<option value="<c:out value='${status.cd}'/>"<c:if test="${status.cd eq searchVO.searchStatus}"> selected</c:if>><c:out value='${status.cdNm}'/></option>
															</c:if>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
									<li>
										<label>상태</label>
										<div class="list-item">
											<div class="select">
												<select id="searchSmsStatus" name="searchSmsStatus" title="문자상태 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(smsStatusList) > 0}">
														<c:forEach items="${smsStatusList}" var="smsStatus">
															<option value="<c:out value='${smsStatus.cd}'/>"<c:if test="${smsStatus.cd eq searchVO.searchSmsStatus}"> selected</c:if>><c:out value='${smsStatus.cdNm}'/></option>
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
						<div id="divSmsList" style="margin-top:50px;"></div>
						<!-- //목록&페이징 --> 
							
					</fieldset>
				</form>
  
				
			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>
 
	<!-- 수신자그룹미리보기팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_seg_sms.jsp" %>
	<!-- //수신자그룹미리보기팝업 -->
	
	<!-- 조회사유팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_search_reason.jsp" %>
	<!-- //조회사유팝업 -->
	
</body>
</html>
