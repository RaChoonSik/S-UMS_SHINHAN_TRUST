<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.05
	*	설명 : 분류코드 관리 화면 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/cod/userCodeGroupListP.js'/>"></script>

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
					<h2>공통코드 분류관리</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
				<!-- <div class="connectdb-wrap"> -->
					<form id="searchForm" name="searchForm" method="post">						
						<input type="hidden" id="page" name="page" value="${searchVO.page}">
						<input type="hidden" id="cdGrp" name="cdGrp" value=""> 
						
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
											<label>분류코드</label>
											<div class="list-item">
												<div class="select">
													<select id="searchCdGrp" name="searchCdGrp" title="분류코드 선택">
														<option value="">선택</option>
														<c:if test="${fn:length(cdGrpList) > 0}">
															<c:forEach items="${cdGrpList}" var="cdGrp">
																<option value="<c:out value='${cdGrp.cd}'/>"<c:if test="${cdGrp.cd eq searchVO.searchCdGrp}"> selected</c:if>><c:out value='${cdGrp.cd}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
										<li>
											<label>분류명</label>
											<div class="list-item">
												<input type="text" id="searchCdGrpNm" name="searchCdGrpNm" placeholder="분류명을 입력해주세요.">
											</div>
										</li>
									</ul>
								</div>
							</div>
							<!-- //조회 -->
	
							<!-- btn-wrap// -->
							<div class="btn-wrap">
								<button type="button" class="btn big fullred" onclick="goSearch('1')">검색</button>
								<button type="button" class="btn big" onclick="goReset()">취소</button>
							</div>
							<!-- //btn-wrap -->
	
							<!-- 목록&페이징// -->
							<div id="divUserCodeGroupList" style="margin-top:36px;"></div>
							<!-- //목록&페이징 -->
								
						</fieldset>
					</form>
				<!-- </div> -->

			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>

</body>
</html>
