<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.01.14
	*	설명 :캠페인별 발송 - 메일별 발송목록화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp" %>


<script type="text/javascript" src="<c:url value='/js/ems/ana/campMailEmsListP.js'/>"></script>

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
					<h2>메일별 발송목록</h2>
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
					<input type="hidden" id="orgSearchStartDt" name="orgSearchStartDt" value="<c:out value='${searchVO.searchStartDt}'/>">
					<input type="hidden" id="orgSearchEndDt" name="orgSearchEndDt" value="<c:out value='${searchVO.searchEndDt}'/>">
					<input type="hidden" id="orgSearchDeptNo" name="orgSearchDeptNo" value="<c:out value='${searchVO.searchDeptNo}'/>">
					<input type="hidden" id="orgSearchUserId" name="orgSearchUserId" value="<c:out value='${searchVO.searchUserId}'/>">
					<input type="hidden" id="searchServiceGb" name="searchServiceGb" value="<c:out value='${searchVO.searchServiceGb}'/>">
					<input type="hidden" id="searchCampNo" name="searchCampNo" value="<c:out value='${searchVO.searchCampNo}'/>">
					<input type="hidden" id="searchCampNm" name="searchCampNm" value="<c:out value='${searchVO.searchCampNm}'/>">
					<input type="hidden" id="searchTaskNo" name="searchTaskNo" value="0">
					<input type="hidden" id="taskNo" name="taskNo" value="0">
					<input type="hidden" id="subTaskNo" name="subTaskNo" value="0">
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
										<label>캠페인명</label>
										<div class="list-item">
											<p class="inline-txt" id="campNm"><c:out value='${searchVO.searchCampNm}'/></p>
										</div>
									</li>
								</ul>
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
											<div class="select">
												<select id="searchSendRepeat" name="searchSendRepeat" title="메일유형 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(repeatList) > 0}">
														<c:forEach items="${repeatList}" var="repeat">
															<option value="<c:out value='${repeat.cd}'/>"<c:if test="${repeat.cd == searchVO.searchSendRepeat}"> selected</c:if>><c:out value='${repeat.cdNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
									<li>
										<label>이메일명</label>
										<div class="list-item">
											<input type="text" name="searchTaskNm" placeholder="이메일명을 입력해주세요.">
										</div>
									</li>
									<li>
										<label>발송결과</label>
										<div class="list-item">
											<div class="select">
												<select id="searchWorkStatus" name="searchWorkStatus" title="발송상태 선택">
													<option value="">선택</option>
													<option value="003">발송성공</option>
													<option value="004">발송실패</option>
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
							<button type="button" class="btn big fullblue" onclick="goSearch('1');">검색</button>
							<button type="button" class="btn big" onclick="goInit();">초기화</button>
						</div>
						<!-- //btn-wrap -->

						<!-- 목록&페이징// -->
						<div id="divCampMailEmsList" style="margin-top:50px;"></div>
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
	
	<!-- 메일발송이력// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_mail_send_hist.jsp" %>
	<!-- //신규발송팝업 -->	
	
</body>
</html>
