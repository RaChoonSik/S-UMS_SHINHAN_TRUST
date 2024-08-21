<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.30
	*	설명 : 사용자 권한 정보 수정
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/aut/userAuthUpdate.js'/>"></script>

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
					<h2>사용자권한 정보수정</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body"> 
				<div class="userauthority-wrap">
					<form id="userAuthInfoForm" name="userAuthInfoForm" method="post">
						<input type="hidden" name="page" value="<c:out value='${searchVO.page}'/>">
						<input type="hidden" id="userId" name ="userId" value="<c:out value='${userInfo.userId}'/>">
						<input type="hidden" id="menuIds" name ="menuIds" value="">
						<fieldset>
							<legend>내용</legend>
	
							<!-- 내용// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">내용</h3>  
								</div>
	
								<div class="list-area">
									<ul>
										<li>
											<label>사용자ID</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value='${userInfo.userId}'/></p>
											</div>
										</li>
										<li>
											<label>사용자명</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value='${userInfo.userNm}'/></p>
											</div>
										</li>
										<li>
											<label>사용자상태</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value='${userInfo.statusNm}'/></p>
											</div>
										</li>
										<li>
											<label>사용자그롭</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value='${userInfo.deptNm}'/></p>
											</div>
										</li>
									</ul>
								</div>
								<!-- 접근대상 메뉴 및 접근 메뉴// -->
								<div class="adminauthority clear">
									<!-- 접근대상메뉴// -->
									<div class="graybox fl">
										<div class="title-area">
											<h3 class="h3-title">접근 대상 메뉴</h3>
										</div> 
										<!-- content-area// -->
										<div class="content-area" style="height: 370px;">
											<ul id="userMenuList" class="toggle">
												<li><!-- 활성화 : li class="open" -->
													<button type="button" class="btn-toggle">EMS 메뉴</button>
													<label><input type="checkbox"><span></span></label>
			
													<ul class="depth2">
														<li><!-- 활성화 : li class="active" -->
															<button type="button" class="btn-toggle">발송현황</button>
															<label><input type="checkbox"><span></span></label>
															<ul class="depth3">
																<li>
																	<button type="button">주간일정</button>
																	<label><input type="checkbox"><span></span></label>
																</li>
																<li>
																	<button type="button">월간일정</button>
																	<label><input type="checkbox"><span></span></label>
																</li>
															</ul>
														</li>
														   
													</ul>
												</li> 
											</ul>
										</div>
										<!-- //content-area -->
									</div>
									<!-- //접근대상메뉴 -->

									<!-- btn-area// -->
									<div class="btn-area">
										<button type="button" class="btn-remove" onclick="goRemove();"><span class="hide">삭제</span></button>
										<button type="button" class="btn-add" onclick="goAdd();"><span class="hide">추가</span></button>
									</div>
									<!-- //btn-area -->
	
									<!-- 접근 메뉴// -->
									<div class="graybox fr">
										<div class="title-area">
											<h3 class="h3-title">접근 메뉴</h3>
										</div>
										<div class="content-area">
											<ul id="userAuthList"  class="user" style="height:365px;overflow-y:auto;">
											</ul>
										</div>
									</div>
									<!-- //접근 메뉴 -->
	
								</div>
								<!-- //접근대상 메뉴 및 접근 메뉴 -->
							</div>
							<!-- //내용 --> 
						 
							<!-- btn-wrap// -->
							<div class="btn-wrap">
								<button type="button" class="btn big fullred" onclick="goUpdate();">수정</button>
								<button type="button" class="btn" onclick="goCancel();">취소</button>
							</div>
							<!-- //btn-wrap -->
								
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
