<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.18
	*	설명 : 사용자그룹 관리 목록 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/acc/deptListP.js'/>"></script>

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
					<h2>사용자그룹 목록</h2>
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
										<label>사용자그룹</label>
										<div class="list-item">
											<input type="text" id="searchDeptNm" name="searchDeptNm" placeholder="사용자그룹을 입력해주세요.">
										</div>
									</li>
									<li>
										<label>사용자<br>그룹상태</label>
										<div class="list-item">
											<div class="select">
												<select id="searchStatus" name="searchStatus" title="옵션 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(deptStatusList) > 0}">
														<c:forEach items="${deptStatusList}" var="deptStatus">
															<option value="<c:out value='${deptStatus.cd}'/>"<c:if test="${deptStatus.cd eq searchVO.searchStatus}"> selected</c:if>><c:out value='${deptStatus.cdNm}'/></option>
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
						<div id="divDeptList" style="margin-top:36px;"></div>
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
 