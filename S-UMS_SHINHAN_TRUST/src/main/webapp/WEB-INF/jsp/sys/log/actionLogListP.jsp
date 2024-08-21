<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.25
	*	설명 : 시스템로그 목록
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/log/actionLogListP.js'/>"></script>

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
					<h2>시스템로그 목록</h2>
				</div>

				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
				<form id="searchForm" name="searchForm" method="post">
					<input type="hidden" id="page" name="page" value="${searchVO.page}">
					<input type="hidden" id="deptNo" name="deptNo" value="0">
					<input type="hidden" id="searchOrgCd" name="searchOrgCd" value="0">
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
										<label>기준일</label>
										<div class="list-item">
											<fmt:parseDate var="startDt" value="${searchVO.searchLogStdDt}" pattern="yyyyMMdd"/>
											<fmt:formatDate var="searchLogStdDt" value="${startDt}" pattern="yyyy.MM.dd"/> 
											<fmt:parseDate var="endDt" value="${searchVO.searchLogEndDt}" pattern="yyyyMMdd"/>
											<fmt:formatDate var="searchLogEndDt" value="${endDt}" pattern="yyyy.MM.dd"/>
											<div class="datepickerrange fromDate">
												<label>
													<input type="text" id="searchLogStdDt" name=searchLogStdDt value="<c:out value='${searchLogStdDt}'/>" readonly>
												</label>
											</div>
											<span class="hyppen date"></span>
											<div class="datepickerrange toDate">
												<label>
													<input type="text" id="searchLogEndDt" name="searchLogEndDt" value="<c:out value='${searchLogEndDt}'/>" readonly>
												</label>
											</div>
											<!-- //datepickerrange -->
										</div>
									</li>
									<li>
										<label>상태</label>
										<div class="list-item">
											<div class="select">
												<select id="searchStatusGb" name="searchStatusGb" title="옵션 선택">
													<option value="">선택</option>
													<option value="Success">Success</option>
													<option value="Failure">Failure</option>
													<option value="Warning">Warning</option>
													
												</select>
											</div>
										</div>
									</li>
									<li>
										<label>로그 종별</label>
										<div class="list-item">
											<div class="select">
												<select id="searchContentType" name="searchContentType" title="옵션 선택">
													<c:if test="${fn:length(contentTypeList) > 0}">
														<c:forEach items="${contentTypeList}" var="contentType">
															<option value="<c:out value='${contentType.cd}'/>"<c:if test="${contentType.cd eq searchVO.contentType}"> selected</c:if>><c:out value='${contentType.cdNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
									<li>
										<label>부서명</label>
										<div class="list-item">
											<input type="text" id="orgKorNm" name="orgKorNm" placeholder="선택" style="width:calc(100% - 52px)">
											<input type="hidden" id="orgCd" name="orgCd" value="">
											<button type="button" class="btn fullred" onclick="popOrgSearchView();">검색</button>
										</div>
									</li>
									<li>
										<label>사용자그롭</label>
										<div class="list-item">
											<div class="select">
												<select id="searchDeptNo" name="searchDeptNo" title="옵션 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(deptList) > 0}">
														<c:forEach items="${deptList}" var="dept">
															<option value="<c:out value='${dept.deptNo}'/>"<c:if test="${dept.deptNo eq searchVO.deptNo}"> selected</c:if>><c:out value='${dept.deptNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
									<li>
										<label>사용자명</label>
										<div class="list-item"> 
											<input type="text" id="searchUserNm" name="searchUserNm" placeholder="사용자명을 입력해주세요.">
										</div>
									</li>
								</ul>
							</div>
						</div>
						<!-- //조회 -->
							<!-- 부서검색// -->
							<%@ include file="/WEB-INF/jsp/inc/pop/pop_org_search_view.jsp" %>
						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullred" onclick="goSearch('1')">검색</button>
							<button type="button" class="btn big" onclick="goReset()">초기화</button>
						</div>
						<!-- //btn-wrap -->

						<!-- 목록&페이징// -->
						<div id="divActionLogList" style="margin-top:36px;"></div>
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
 