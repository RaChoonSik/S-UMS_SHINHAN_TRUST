<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.18
	*	설명 : 데이터베이스연결 관리 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/dbc/dbConnListP.js'/>"></script>

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
					<h2>데이터베이스연결</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
				<div class="connectdb-wrap">
					<form id="searchForm" name="searchForm" method="post">
					<input type="hidden" id="page" name="page" value="${searchVO.page}">
					<input type="hidden" id="dbConnNo" name="dbConnNo" value="0">
					<fieldset>
						<legend>조회 및 목록</legend>

						<!-- 조회// -->
						<div class="graybox">
							<div class="title-area">
								<h3 class="h3-title">조회</h3>
							</div>
							
							<div class="list-area">
								<ul>
									<li class="col-full">
										<label>Connection</label>
										<div class="list-item">											
											<input type="text" id="searchDbConnNm" name="searchDbConnNm" placeholder="Connection을 입력해주세요.">
										</div>
									</li>
									<li style="margin-top:15px;">
										<label>DB종류</label>
										<div class="list-item">
											<div class="select">
												<select id="searchDbTy" name="searchDbTy" title="옵션 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(dbmsTypeList) > 0}">
														<c:forEach items="${dbmsTypeList}" var="dbType">
															<option value="<c:out value='${dbType.cd}'/>"<c:if test="${dbType.cd eq searchVO.searchDbTy}"> selected</c:if>><c:out value='${dbType.cdNm}'/></option>
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
												<select id="searchStatus" name="searchStatus" title="DB상태 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(dbConnStatusList) > 0}">
														<c:forEach items="${dbConnStatusList}" var="dbConnStatus">
															<option value="<c:out value='${dbConnStatus.cd}'/>"<c:if test="${dbConnStatus.cd eq searchVO.searchStatus}"> selected</c:if>><c:out value='${dbConnStatus.cdNm}'/></option>
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
							<button type="button" class="btn big fullred" onclick="goSearch('1')">검색</button>
							<button type="button" class="btn big" onclick="goReset()">초기화</button>
						</div>
						<!-- //btn-wrap -->

						<!-- 목록&페이징// -->
						<div id="divDbConnList" style="margin-top:36px;"></div>
						<!-- //목록&페이징 -->
							
					</fieldset>
				</form>
				</div>


			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>

	<!-- //페이징 -->

 
	

</body>
</html>
				
 