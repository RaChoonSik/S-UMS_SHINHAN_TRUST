<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.18
	*	설명 : DB연결 신규등록
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/dbc/dbConnAdd.js'/>"></script>

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
					<h2>데이터베이스연계 신규등록</h2>
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
							<legend>내용</legend>
							
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
												<input type="text" id="dbConnNm" name="dbConnNm" placeholder="Connection을 입력해주세요" maxlength="50">
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
																<option value="<c:out value='${dbmsType.cd}'/>"><c:out value='${dbmsType.cdNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
										<li> 
											<label class="required">DB DRIVER</label>
											<div class="list-item">
												<input type="text" id="dbDriver" name="dbDriver" placeholder="oracle.jdbc.driver.OracleDriver" maxlength="200">
											</div>	
										</li>
										<li> 
											<label class="required">URL</label>
											<div class="list-item">
												<input type="text" id="dbUrl" name="dbUrl" placeholder="jdbc:oracle:thin:@[IP]:[PORT]:[SID]" maxlength="1024">
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
																<option value="<c:out value='${dbCharSet.cd}'/>"><c:out value='${dbCharSet.cdNm}'/></option>
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
																<option value="<c:out value='${status.cd}'/>"><c:out value='${status.cdNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
										<li> 
											<label class="required">DB ID</label>
											<div class="list-item">
												<input type="text" id="loginId" name="loginId" placeholder="로그인 아이디를 입력하세요" maxlength="20">
											</div>	
										</li>
										<li> 
											<label class="required">DB PASSWORD</label>

											 <div class="list-item">
												<input type="password" id="loginPwd" name="loginPwd" placeholder="로그인 암호를 입력하세요" maxlength="100">
											</div>  
											
										</li>
										<li class="col-full">
											<label>설명</label>
											<div class="list-item">
												<textarea id="dbConnDesc" name="dbConnDesc" placeholder="DB 연결 설명을 입력해주세요." maxlength="1024"></textarea>
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
								<button type="button" class="btn big fullred" onclick="goAdd();">등록</button>
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
