<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.09.06
	*	설명 : 데이터베이스 메타 정보 관리
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/dbc/dbConnMetaJoin.js'/>"></script>

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
					<h2>데이터베이스 조인정보관리</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->


			<!-- cont-body// -->
			<section class="cont-body">

				<div class="connectdb-wrap">
					<form id="dbJoinInfoForm" name="dbJoinInfoForm" method="post">
						<input type="hidden" id="dbConnNo" name="dbConnNo" value="<c:out value='${dbConnInfo.dbConnNo}'/>">
						<fieldset>
							<legend>데이터베이스 정보, 조인정보관리 및 조인정보 등록</legend>
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
												<p class="inline-txt"><c:out value='${dbConnInfo.dbTyNm}'/></p>
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
 
							<!-- 조인정보등록// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">조인정보 등록</h3>
									<!-- 버튼// -->
									<div class="btn-wrap">
										<button type="button" class="btn fullred" onclick="goAdd()">저장</button>
										<button type="button" class="btn" onclick="goList()">목록</button>
									</div>
									<!-- //버튼 -->
								</div> 
							 
								<!-- 등록내용// -->
								<div class="enroll-info clear">
									<!-- 마스터키 정보// -->
									<div class="graybox">
										<div class="title-area">
											<h3 class="h3-title">마스터키 정보</h3>
										</div>

										<div class="list-area">
											<ul>
												<li class="col-full">
													<label>테이블</label>
													<div class="list-item">
														<select id="masterTable" name="masterTable" title="옵션 선택" onchange="changeMasterTable()">
															<option value="">선택</option>
															<c:if test="${fn:length(metaTableList) > 0}">
																<c:forEach items="${metaTableList}" var="masterTable">
																	<option value="<c:out value='${masterTable.tblNo}'/>"><c:out value='${masterTable.tblNm}'/></option>
																</c:forEach>
															</c:if>
														</select> 
													</div>
												</li>
												<li class="col-full">
													<label>컬럼</label>
													<div class="list-item">
														<div class="select">
															<select id="masterColumn" name="masterColumn" title="옵션 선택">
																<option value="">선택</option>
															</select>
														</div>
													</div>
												</li>
											</ul>
										</div>
									</div>
									<!-- //마스터키 정보 -->

									<!-- 조인유형,관계유형// -->
									<div class="graybox">
										<ul>
											<li class="col-full">
												<label>조인유형</label>
												<div class="list-item">
													<div class="select">
														<select id="joinTypeCodeList" name="joinTypeCodeList" title="옵션 선택">
															<option value="">선택</option>
															<c:if test="${fn:length(joinTyList) > 0}">
																<c:forEach items="${joinTyList}" var="joinTypeCode">
																	<option value="<c:out value='${joinTypeCode.cd}'/>"><c:out value='${joinTypeCode.cdNm}'/></option>
																</c:forEach>
															</c:if>
														</select>
													</div>
												</div>
											</li>
											<li class="col-full">
												<label>관계유형</label>
												<div class="list-item">
													<div class="select">
														<select id="relTypeCodeList" name="relTypeCodeList" title="옵션 선택">
															<option value="">선택</option>
															<c:if test="${fn:length(relTyList) > 0}">
																<c:forEach items="${relTyList}" var="relTypeCode">
																	<option value="<c:out value='${relTypeCode.cd}'/>"><c:out value='${relTypeCode.cdNm}'/></option>
																</c:forEach>
															</c:if>
														</select>
													</div>
												</div>
											</li>
										</ul>
									</div>
									<!-- //조인유형,관계유형 -->

									<!-- 포린 키 정보// -->
									<div class="graybox">
										<div class="title-area">
											<h3 class="h3-title">포린 키 정보</h3>
										</div>

										<div class="list-area">
											<ul>
												<li class="col-full">
													<label>테이블</label>
													<div class="list-item">
														<div class="select">
															<select id="foreignTable" name="foreignTable" title="옵션 선택" onchange="changeForeignTable()">
																<option value="">선택</option>
																<c:if test="${fn:length(metaTableList) > 0}">
																	<c:forEach items="${metaTableList}" var="foreignTable">
																		<option value="<c:out value='${foreignTable.tblNo}'/>"><c:out value='${foreignTable.tblNm}'/></option>
																	</c:forEach>
																</c:if>
															</select>
														</div>
													</div>
												</li>
												<li class="col-full">
													<label>컬럼</label>
													<div class="list-item">
														<div class="select">
															<select id="foreignColumn" name="foreignColumn" title="옵션 선택">
																<option value="">선택</option>
															</select>
														</div>
													</div>
												</li>
											</ul>
										</div>
									</div>
									<!-- //포린 키 정보 -->

								</div>
								<!-- //등록내용 -->
							</div>
							<!-- //조인정보등록 -->  
	
							<!-- 조인정보관리// -->  
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">조인정보관리</h3>
								</div> 
								<div class="grid-area" id="divJoinList">
								</div>
							</div>
							<!-- //조인정보관리 -->

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
