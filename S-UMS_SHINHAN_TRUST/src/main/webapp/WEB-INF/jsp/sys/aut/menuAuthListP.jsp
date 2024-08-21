<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.09.02
	*	설명 : 메뉴 권한 목록 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/aut/menuAuthListP.js'/>"></script>

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
					<h2>메뉴권한 목록</h2>
				</div>

				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
			
 				<div class="menuauthority-wrap clear" style="position: relative;">
					<form>
						<input type="hidden" id="enaleYn" value="Y">
						<fieldset>
							<legend>목록 및 선택</legend>
	
							<!-- 목록// -->
							<div class="graybox" style="width:21%;">
								<div class="title-area">
									<h3 class="h3-title">목록</h3>
								</div>

								<!-- gridtoggle-area// -->
								<div class="content-area"> 
									<ul class="toggle-check">
										<li>
											<ul id="menuList" class="toggle-unfold">
												<!-- 1depth// -->
												<li class="depth1">
													<ul class="col-box">
														<li>
															<span class="col service">
																<button type="button" class="btn-toggle">EMS</button>
															</span>
															<span class="col checkbox">
																<label><input type="checkbox"><span></span></label>
															</span>
														</li>
													</ul>

													<!-- 2depth// -->
													<ul class="depth2"> 
														<li>
															<ul class="col-box">
																<li>
																	<span class="col menu">
																		<button type="button" class="btn-toggle">발송현황</button>
																	</span>
																	<span class="col checkbox">
																		<label><input type="checkbox"><span></span></label>
																	</span>
																</li>
															</ul>

															<!-- 3depth// -->
															<ul class="depth3">
																<li class="col-box">
																	<span class="col menu">
																		<button type="button">주간현황</button>
																	</span>
																	<span class="col checkbox">
																		<label><input type="checkbox"><span></span></label>
																	</span>
																</li>
																<li class="col-box">
																	<span class="col menu">
																		<button type="button">월간현황</button>
																	</span>
																	<span class="col checkbox">
																		<label><input type="checkbox"><span></span></label>
																	</span>
																</li>
																<!-- //3depth li --> 
															</ul>
															<!-- //3depth ul -->
														</li>
														<!-- 2depth li End -->
														</ul>
														<!-- 2depth ul End -->
													</li>
													<!--1 depthi li End-->
											</ul>
											<!-- menulist End--> 
										</li>
									</ul>  
								</div>
								<!-- //gridtoggle-area -->
							</div>
							<!-- //목록 -->
	
							<!-- 권한관리// -->
							<div class="graybox" style="width:78%;">
								<div class="title-area clear">
									<h3 class="h3-title">권한관리</h3>
									<!-- 버튼// -->
									<button type="button" class="btn fullred fr" onclick="goUpdate();">저장</button>
									<!-- //버튼 -->
								</div>
								<div class="adminauthority clear">
									<!-- 조직도// -->
									<div class="graybox">
										<div class="title-area">
											<h3 class="h3-title">조직도</h3>
										</div>
										
										<div class="content-area">
											<h4>부서목록</h4>
											<ul class="toggle">
												<c:if test="${fn:length(orgList) > 0}">
													<c:forEach items="${orgList}" var="org">
														<li>  
															<button type="button" class="btn-toggle" onclick="getViewOrgList('<c:out value='${org.orgCd}'/>','<c:out value='${org.orgNm}'/>');"><c:out value="${org.orgNm}"/></button>
															<c:if test="${org.childCnt > 0}">
																<ul class="depth<c:out value='${org.lvlVal+1}'/>" id="<c:out value='${org.orgCd}'/>"></ul>
															</c:if>
														</li>
													</c:forEach>
												</c:if>
											</ul>
										</div>
									</div>
									<!-- //조직도 -->
	
									<!-- 사용자 검색// -->
									<div class="graybox">
										<div class="title-area">
											<h3 class="h3-title">사용자 검색</h3>
										</div>
										
										<div class="content-area">
											<div class="item"><!-- 2021-08-17 클래스 수정 -->
												<input type="text" id="userNm" placeholder="사용자명">
												<input type="text" style="display:none">
												<button type="button" class="btn fullred" onclick="getUserList('')">검색</button>
											</div>
	
											<ul id="userList" class="user">
											</ul>
										</div>
									</div>
									<!-- //사용자 검색 -->

									<!-- btn-area// -->
									<div class="btn-area">
										<button type="button" id="btnRemove" class="btn-remove" onclick="goRemove()"><span class="hide">삭제</span></button> 
										<button type="button" id="btnAdd" class="btn-add" onclick="goAdd()"><span class="hide" >추가</span></button> 
									</div>
									<!-- //btn-area -->
	
									<!-- 권한 매핑 사용자// -->
									<div class="graybox fr">
										<div class="title-area">
											<h3 id="hTitle" class="h3-title">권한 매핑 사용자</h3>
										</div>
										
										<div class="content-area outcome">
											<ul id="authUserList" class="user"> 
											</ul>
										</div>
									</div>
									<!-- //권한 매핑 사용자 -->
										
								</div>
							</div>
							<!-- //권한관리 -->
							<div class="databox" >
								<p id="checkedMenuList"> 
									선택 :
								</p>
							</div>
						</fieldset>
					</form>
				</div>
 
			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>

</body>
</html>
 