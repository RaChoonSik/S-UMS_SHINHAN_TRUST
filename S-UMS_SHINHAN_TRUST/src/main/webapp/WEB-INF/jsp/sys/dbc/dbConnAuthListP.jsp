<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.09.06
	*	설명 : 데이터베이스 권한 정보 관리
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/dbc/dbConnAuthListP.js'/>"></script>
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
					<h2>데이터베이스 권한관리</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
				<div class="connectdb-wrap"> 
					<form id="dbConnInfoForm" name="dbConnInfoForm" method="post">
						<input hidden="hidden" id="dbConnNo" name="dbConnNo" value="<c:out value='${dbConnInfo.dbConnNo}'/>" >
						<input type="hidden" id="deptNo" name="deptNo" value="0">
						<fieldset>
							<legend>데이터베이스 정보 및 권한관리</legend>
							<!-- 데이터베이스 정보// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">데이터베이스 정보</h3>
								</div>
								
								<div class="list-area">
									<ul>
										<li>
											<label>Connection</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value='${dbConnInfo.dbConnNm}'/></p>
											</div>
										</li>
										<li>
											<label>DB종류</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value='${dbConnInfo.statusNm}'/></p>
											</div>
										</li>
										<li class="col-full">
											<label>설명</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value='${dbConnInfo.dbConnDesc}'/></p>
											</div>
										</li>
									</ul> 
								</div>
							</div>
							<!-- //데이터베이스 정보 -->
							
 							<!-- 사용자 권한관리// --> 
							<div class="graybox editpw active">
								<div class="title-area">
									<h3 class="h3-title">사용자 권한관리</h3>
									<button type="button"><span class="hidden">여닫기버튼</span></button>
								</div>
								
								<div class="adminauthority clear">
									<!-- 조직도// -->
									<div class="graybox fl">
										<div class="title-area">
											<h3 class="h3-title">조직 목록</h3>
										</div>
										
										<div class="content-area" style="height:340px;">
											<ul class="toggle">
												<c:if test="${fn:length(orgList) > 0}">
													<c:forEach items="${orgList}" var="org">
														<li>
															<button type="button" class="btn-toggle" onclick="getDbConnOrgListView('<c:out value='${org.orgCd}'/>','<c:out value='${org.orgNm}'/>');"><c:out value="${org.orgNm}"/></button>
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
									<div class="graybox fl">
										<div class="title-area">
											<h3 class="h3-title">사용자 검색</h3>
										</div>
										
										<div class="content-area" style="height:340px;">
											<div class="item"><!-- 2021-08-17 클래스 수정 -->
												<input type="text" id="userNm" placeholder="사용자명 검색">
												<button type="button" class="btn fullred" onclick="getUserList('')">검색</button>
											</div>
	
											<ul id="userList" class="user">
											</ul>
										</div>
									</div>
									<!-- //사용자 검색 -->

									<!-- btn-area// -->
									<div class="btn-area">
										<button type="button" class="btn-remove" onclick="goRemove()"><span class="hide">삭제</span></button>
										<button type="button" class="btn-add" onclick="goAdd()"><span class="hide" >추가</span></button>
									</div>
									<!-- //btn-area -->
	
									<!-- 권한 매핑 사용자// -->
									<div class="graybox fr">
										<div class="title-area">
											<h3 class="h3-title">권한 매핑 사용자</h3>
										</div>
										
										<div class="content-area outcome">
											<ul id="dbAuthUserList" class="user"> 
												<c:if test="${fn:length(dbAuthUserList) > 0}">
													<c:forEach items="${dbAuthUserList}" var="dbAuthUser">
														<li id="la_<c:out value='${dbAuthUser.userId}'/>">
															<label class="clear">
																<span class="fl">
																	<strong><c:out value="${dbAuthUser.userNm}"/></strong>
																	<span><c:out value="${dbAuthUser.orgNm}"/> | </span>
																	<span><c:out value="${dbAuthUser.jobNm}"/></span>
																</span>
																<span class="fr">
																	<input type="checkbox" name="chkAuth" id="ca_<c:out value='${dbAuthUser.userId}'/>" value="<c:out value='${dbAuthUser.userId}'/>"><span></span>
																</span> 
															</label>
														</li>
													</c:forEach>
												</c:if>	
											</ul>
										</div>
									</div>
									<!-- //권한 매핑 사용자 -->
										
								</div>
							</div> 
							<!--// 사용자 권한관리 -->
							<!-- 그룹 권한관리// -->
							<div class="graybox editpw active">
								<div class="title-area">
									<h3 class="h3-title">그룹 권한관리</h3>
									<button type="button"><span class="hidden">여닫기버튼</span></button>
								</div>
								<div class="adminauthority clear">

									<!-- 사용자// -->
									<div class="graybox fl" style="width:62%;">
										<div class="title-area">
											<h3 class="h3-title">그룹 검색</h3>
										</div>
										
										<div class="content-area" style="height:340px;">
											<div class="item"><!-- 2021-08-17 클래스 수정 -->
												<input type="text" id="deptNm" placeholder="그룹명 검색">
												<button type="button" class="btn fullred" onclick="getDbConnDeptList('')">검색</button>
											</div>

											<ul class="user" id="deptList">
												<c:if test="${fn:length(deptList) > 0}">
													<c:forEach items="${deptList}" var="dept">
														<li id="ld_<c:out value='${dept.deptNo}'/>" class="clear">
															<label class="clear">
																<span class="fl"><c:out value="${dept.deptNm}"/></span>
																<span class="fr">
																	<input type="checkbox" name="chkDept" id="cd_<c:out value='${dept.deptNo}'/>" value="<c:out value='${dept.deptNo}'/>"><span></span>
																</span> 
															</label>
														</li>
													</c:forEach>
												</c:if>
											</ul>
										</div>
									</div>
									<!-- //사용자 -->

									<!-- btn-area// -->
									<div class="btn-area">
										<button type="button" class="btn-remove" onclick="goRemoveDept()"><span class="hide">삭제</span></button>
										<button type="button" class="btn-add" onclick="goAddDept()"><span class="hide">추가</span></button>
									</div>
									<!-- //btn-area -->

									<!-- 권한 사용자// -->
									<div class="graybox fr">
										<div class="title-area">
											<h3 class="h3-title">권한 그룹</h3>
										</div>
										
										<div class="content-area outcome" style="height:340px;">
											<ul class="user" id="dbAuthDeptList">
												<c:if test="${fn:length(dbAuthDeptList) > 0}">
													<c:forEach items="${dbAuthDeptList}" var="dbAuthDept">
														<li id="lad_<c:out value='${dbAuthDept.deptNo}'/>">	 
															<label class="clear">
																<span class="fl"><c:out value="${dbAuthDept.deptNm}"/></span>
																<span class="fr">
																	<input type="checkbox" name="chkAuthDept" id="cad_<c:out value='${dbAuthDept.deptNo}'/>" value="<c:out value='${dbAuthDept.deptNo}'/>"><span></span>
																</span> 
															</label>
														</li>
													</c:forEach>
												</c:if>
											</ul>
										</div>
									</div>
									<!-- //권한 사용자 -->
										
								</div>
							</div>

							<!-- //그룹 권한관리 -->

							<!-- 버튼// -->
							<div class="btn-wrap">
								<button type="button" class="btn big fullred" onclick="goUpdate();">저장</button>
								<button type="button" class="btn big" onclick="goCancel();">취소</button>
							</div>
							<!-- //버튼 -->
						</fieldset>
					</form>
				<!-- </div> -->
				</div>
			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>
</body>
</html>
