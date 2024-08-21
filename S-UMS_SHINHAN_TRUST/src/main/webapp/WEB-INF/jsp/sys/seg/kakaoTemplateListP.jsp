<%--
	/**********************************************************
	*	작성자 : 김재환
	*	작성일시 : 2021.12.09
	*	설명 : 카카오 알림톡 템플릿 목록 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/seg/kakaoTemplateListP.js'/>"></script>

<body>
	<div id="wrap" class="sys">

		<!-- lnb// -->
		<div id="lnb">
			<!-- LEFT MENU -->
			<%@ include file="/WEB-INF/jsp/inc/menu_sys.jsp" %>
			<!-- LEFT MENU -->
		</div>
		<!-- //lnb -->

		<!-- content// -->
		<div id="content">

			<!-- cont-head// -->
			<section class="cont-head">
				<div class="title">
					<h2>알림톡 템플릿 목록</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">

				<form id="searchForm" name="searchForm" method="post">
					<input type="hidden" name="page" value="${searchVO.page}"/>
					<input type="hidden" id="tempCd" name="tempCd" />
					<input type="hidden" id="status" name="status"/>
					
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
										<label>상태</label>
										<div class="list-item">
											<div class="select">
												<select id="searchStatus" name="searchStatus" title="상태 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(statusList) > 0}">
														<c:forEach items="${statusList}" var="status">
															<option value="<c:out value='${status.cd}'/>"<c:if test="${status.cd eq searchVO.searchStatus}"> selected</c:if>><c:out value='${status.cdNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
									<li>
										<label>템플릿명</label>
										<div class="list-item">
											<input type="text" id="searchTempNm" name="searchTempNm"  value="<c:out value='${searchVO.searchTempNm}'/>" placeholder="템플릿명을 입력해주세요." />
										</div>
									</li>
									<li>
									
										<label>템플릿 코드</label>
										<div class="list-item">
											<input type="text" id="searchTempCd" name="searchTempCd" value="<c:out value='${searchVO.searchTempCd}'/>" placeholder="템플릿 코드를 입력해주세요." />
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
															<option value="<c:out value='${dept.deptNo}'/>"><c:out value='${dept.deptNm}'/></option>
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
								</ul>
							</div>
						</div>
						<!-- //조회 -->

						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullred" onclick="goSearch('1');">검색</button>
							<button type="button" class="btn big" onclick="goInit();">초기화</button>
						</div>
						<!-- //btn-wrap -->

						<!-- 목록&페이징// -->
						<div id="divSegList" style="margin-top:50px;"></div>
						<!-- //목록&페이징 -->
							
					</fieldset>
				</form>

			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>
	
	<!-- 수신자그룹미리보기팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_seg_mail_sys.jsp" %>
	<!-- //수신자그룹미리보기팝업 -->
	
	<!-- 조회사유팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_search_reason_sys.jsp" %>
	<!-- //조회사유팝업 -->
</body>
</html>
