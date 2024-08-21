<%--
   /**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.10.26
	*	설명 :  캠페인별 조회 -> PUSH 발송현황 조회
	**********************************************************/
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_push.jsp" %>

<script type="text/javascript" src="<c:url value='/js/push/ana/pushSendListP.js'/>"></script>
<body>
	<div id="wrap" class="push">
	    
		<div id="lnb">
			<!-- LEFT MENU -->
			<%@ include file="/WEB-INF/jsp/inc/menu_push.jsp" %>
			<!-- LEFT MENU -->
		</div>
		
		<!-- content// -->
		<div id="content">
		
			<!-- cont-head// -->
			<section class="cont-head">
				<div class="title">
					<h2>PUSH알림별발송목록</h2>
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
					<input type="hidden" id="searchCampNo" name="searchCampNo" value="<c:out value='${searchVO.searchCampNo}'/>">
					<input type="hidden" id="searchCampNm" name="searchCampNm" value="<c:out value='${searchVO.searchCampNm}'/>">
					<input type="hidden" id="rsltCd" name="rsltCd" value="">
					<input type="hidden" id="pushmessageId" name="pushmessageId" value="">
					<input type="hidden" id="searchCallGubun" name="searchCallGubun" value="C">
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
										<label>PUSH명 </label>
										<div class="list-item">
											<input type="text" id="searchPushName" name="searchPushName" placeholder="PUSH명을 입력해주세요.">
										</div>
									</li>
									<li>
										<label>발송유형</label>
										<div class="list-item">
											<div class="select">
												<select id="searchPushGubun" name="searchPushGubun" title="전송유형 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(pushGubunList) > 0}">
														<c:forEach items="${pushGubunList}" var="pushGubun">
															<option value="<c:out value='${pushGubun.cd}'/>"<c:if test="${pushGubun.cd eq searchVO.searchPushGubun}"> selected</c:if>><c:out value='${pushGubun.cdNm}'/></option>
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
							<button type="button" class="btn big fullorange" onclick="goSearch('1');">검색</button>
							<button type="button" class="btn big" onclick="goInit();">초기화</button>
						</div>
						<!-- //btn-wrap -->
						<!-- 목록&페이징// -->
						<div id="divPushList" style="margin-top:50px;"></div>
						<!-- //목록&페이징 -->
					</fieldset>
				</form>
			</section>
			<!-- //cont-body -->
		</div>
	<!-- 결과 팝업// -->


	<!-- //결과 팝업 -->
	</div>
</body>