<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.05
	*	설명 : 데이터베이스 연결 정보 수정
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/dbc/dbConnUpdate.js'/>"></script>

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
					<h2>데이터베이스 연결 정보수정</h2>
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
						<fieldset>
							<legend>조회 및 목록 </legend>
							<!-- 조건// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">조건</h3>
									<span class="required">*필수입력 항목</span>
								</div>
								
								<div class="list-area">
								<ul>
									<li> 
										<label class="required">Connection</label>
										<div class="list-item">
											<input type="text" id="dbConnNm" name="dbConnNm" value="<c:out value='${dbConnInfo.dbConnNm}'/>" maxlength="50">
											<input type="hidden" id="deptNo" name="dbConnNo" value="<c:out value='${dbConnInfo.dbConnNo}'/>" >
										</div>	
									</li>
									<li>
										<label class="required">DB종류</label>
										<div class="list-item">
											<div class="select">
												<select id="dbTy" name="dbTy" title="DB종류 선택" onchange="setDB(this.options[this.selectedIndex].value)">
													<option value="">선택</option>
													<c:if test="${fn:length(dbmsTypeList) > 0}">
														<c:forEach items="${dbmsTypeList}" var="dbmsType">
															<option value="<c:out value='${dbmsType.cd}'/>"<c:if test="${dbmsType.cd eq dbConnInfo.dbTy}"> selected</c:if>><c:out value='${dbmsType.cdNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
									<li> 
										<label class="required">DB DRIVER</label>
										<div class="list-item">
											<input type="text" id="dbDriver" name="dbDriver" value="<c:out value='${dbConnInfo.dbDriver}'/>" maxlength="200">
										</div>	
									</li>
									<li> 
										<label class="required">URL</label>
										<div class="list-item">
											<input type="text" id="dbUrl" name="dbUrl" value="<c:out value='${dbConnInfo.dbUrl}'/>"  maxlength="1024">
										</div>	
									</li>
									<li>
										<label class="required">문자타입</label>
										<div class="list-item">
											<div class="select">
												<select id="dbCharSet" name="dbCharSet" title="문자타입 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(dbCharSetList) > 0}">
														<c:forEach items="${dbCharSetList}" var="dbCharSet">
															<option value="<c:out value='${dbCharSet.cd}'/>"<c:if test="${dbCharSet.cd eq dbConnInfo.dbCharSet}"> selected</c:if>><c:out value='${dbCharSet.cdNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
									<li>
										<label class="required">상태</label>
										<div class="list-item">
											<div class="select">
												<select id="status" name="status" title="상태 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(dbConnStatusList) > 0}">
														<c:forEach items="${dbConnStatusList}" var="status">
															<option value="<c:out value='${status.cd}'/>"<c:if test="${status.cd eq dbConnInfo.status}"> selected</c:if>><c:out value='${status.cdNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
									<li> 
										<label class="required">DB ID</label>
										<div class="list-item">
											<input type="text" id="loginId" name="loginId" value="<c:out value='${dbConnInfo.loginId}'/>" maxlength="20">
										</div>	
									</li>
									<li> 
										<label class="required">DB PASSWORD</label>
										<div class="list-item">
											<input type="password" id="loginPwd" name="loginPwd" value="<c:out value='${dbConnInfo.loginPwd}'/>" maxlength="100">
										</div>	
									</li>
									<li class="col-full">
										<label>설명</label>
										<div class="list-item">
											<textarea id="dbConnDesc" name="dbConnDesc" maxlength="1024"><c:out value="${dbConnInfo.dbConnDesc}"/></textarea>
										</div>
									</li>
								</ul>
								<ul>
									<li>
										<label>등록자</label>
										<div class="list-item">
											<p class="inline-txt"><c:out value="${dbConnInfo.regNm}"/></p>
										</div>
									</li>

									<li>
										<label>수정자</label>
										<div class="list-item">
											<p class="inline-txt"><c:out value="${dbConnInfo.upNm}"/></p>
										</div>
									</li>

									<li>
										<label>등록일시</label>
										<div class="list-item">
											<p class="inline-txt"><c:out value="${dbConnInfo.regDt}"/></p>
										</div>
									</li>

									<li>
										<label>수정일시</label>
										<div class="list-item">
											<p class="inline-txt"><c:out value="${dbConnInfo.upDt}"/></p>
										</div>
									</li>
								</ul>
							</div>
						</div>
							<!-- //조건 --> 
	
						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big" onclick="testDbConn();">연결테스트</button>
							<button type="button" class="btn big" onclick="goReset();">재입력</button>
							<button type="button" class="btn big fullred" onclick="goUpdate();">저장</button>
							<button type="button" class="btn big" onclick="goCancel();">취소</button>
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
