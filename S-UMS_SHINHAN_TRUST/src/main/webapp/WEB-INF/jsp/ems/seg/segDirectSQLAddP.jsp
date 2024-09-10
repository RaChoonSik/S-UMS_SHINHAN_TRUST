<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.18
	*	설명 : 수신자그룹 등록(직접SQL) 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp" %>

<script type="text/javascript" src="<c:url value='/js/ems/seg/segDirectSQLAddP.js'/>"></script>

<body>
	<div id="wrap" class="ems">

		<!-- lnb// -->
		<div id="lnb">
			<!-- LEFT MENU -->
			<%@ include file="/WEB-INF/jsp/inc/menu_ems.jsp" %>
			<!-- LEFT MENU -->
		</div>
		<!-- //lnb -->

		<!-- content// -->
		<div id="content">

			<!-- cont-head// -->
			<section class="cont-head">
				<div class="title">
					<h2>수신자그룹 신규등록</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">

				<form id="segInfoForm" name="segInfoForm" method="post">
					<input type="hidden" id="page" name="page" value="1">
					<input type="hidden" id="mergeKey" name="mergeKey">
					<input type="hidden" id="mergeCol" name="mergeCol">
					<input type="hidden" id="testType" name="testType">
					<input type="hidden" id="dataInfo" name="dataInfo">
					<input type="hidden" id="targetService" name="targetService">
					<input type="hidden" id="createTy" name="createTy" value="002">
					<fieldset>
						<legend>수신자그룹 신규등록</legend>

						<!-- tab// -->
						<div class="tab">
							<div class="tab-menu col2">
								<a href="javascript:goCreateTy('003');">파일연동</a>
<!-- 								<a href="javascript:goCreateTy('000');">추출조건</a> -->
								<a href="javascript:goCreateTy('002');" class="active">직접SQL</a>
<!-- 								<a href="javascript:goCreateTy('004');">리타게팅</a> -->
							</div>
							<div class="tab-cont">

								<!-- tab-cont : 직접SQL// -->
								<div class="active">
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
														<div class="select">
															<select id="dbConnNo" name='dbConnNo' onchange='goReload()' title="Connection 선택">
																<c:if test="${fn:length(dbConnList) > 0}">
																	<c:forEach items="${dbConnList}" var="dbConn">
																		<option value="<c:out value='${dbConn.dbConnNo}'/>"<c:if test="${dbConn.dbConnNo == searchVO.dbConnNo}"> selected</c:if>><c:out value='${dbConn.dbConnNm}'/></option>
																	</c:forEach>
																</c:if>
															</select>
														</div>
													</div>
												</li>
												<%-- 관리자의 경우 전체 요청부서를 전시하고 그 외의 경우에는 숨김 --%>
												<c:if test="${'Y' eq NEO_ADMIN_YN}">
													<li>
														<label class="required">사용자그룹</label>
														<div class="list-item">
															<div class="select">
																<select id="deptNo" name="deptNo" onchange="getUserList(this.value);" title="사용자그룹 선택">
																	<option value="0">선택</option>
																	<c:if test="${fn:length(deptList) > 0}">
																		<c:forEach items="${deptList}" var="dept">
																			<option value="<c:out value='${dept.deptNo}'/>"><c:out value='${dept.deptNm}'/></option>
																		</c:forEach>
																	</c:if>
																</select>
															</div>
														</div>
													</li>
													<li>
														<label class="required">사용자명</label>
														<div class="list-item">
															<div class="select">
																<select id="userId" name="userId" title="사용자 선택">
																	<option value="">선택</option>
																</select>
															</div>
														</div>
													</li>
												</c:if>
												<%-- 디자인 깨짐 방지 --%>
												<c:if test="${'N' eq NEO_ADMIN_YN}">
													<li>
														<label></label>
														<div class="list-item">
														</div>
													</li>
												</c:if>
												<li>
													<label class="required">서비스유형</label>
													<div class="list-item">
														<label><input type="checkbox" id="emsuseYn" name="emsuseYn" value="Y"><span>EMS</span></label>
														<label><input type="checkbox" id="smsuseYn" name="smsuseYn" value="Y"><span>SMS</span></label>
<!-- 														<label><input type="checkbox" id="pushuseYn" name="pushuseYn" value="Y"><span>PUSH</span></label> -->
													</div>
												</li>
												<li class="col-full">
													<label class="required">수신자그룹</label>
													<div class="list-item">
														<input type="text" id="segNm" name="segNm" placeholder="수신자그룹 명칭을 입력해주세요.">
													</div>
												</li>
												<li class="col-full">
													<label>설명</label>
													<div class="list-item">
														<textarea id="segDesc" name="segDesc" placeholder="수신자그룹 설명을 입력해주세요."></textarea>
													</div>
												</li>
											</ul>
										</div>
									</div>
									<!-- //조건 -->

									<!-- 테이블 정보, 상세 정보// -->
									<div class="tableinfobox">
										<div class="box">
											<div class="title-area">
												<h3 class="h3-title">테이블 정보</h3>
											</div>

											<!-- 테이블목록// -->
											<div class="grid-area" id="divMetaTableInfo"></div>
											<!-- //테이블목록 -->
										</div>

										<!-- btn-area// -->
										<!-- // 불필요 주석처리
										<div class="btn-area">
											<button type="button" class="btn-add"><span class="hide">상세정보 보기</span></button>
										</div>
										-->
										<!-- //btn-area -->
										
										<!-- 컬럼목록// -->
										<div class="box">
											<div class="title-area">
												<h3 class="h3-title">컬럼 정보</h3>
											</div>
											<div class="grid-area" id="divMetaColumnInfo"></div>
										</div>
										<!-- //컬럼목록 -->
									</div>
									<!-- //테이블 정보, 상세 정보 -->

									<!-- 쿼리 등록// -->
									<div class="graybox">
										<div class="title-area">
											<h3 class="h3-title">쿼리 등록</h3>
											<!-- 버튼// -->
											<div class="btn-wrap">
												<button type="button" class="btn" onclick="goQueryTest('000');">QUERY TEST</button>
											</div>
											<!-- //버튼 -->
										</div>
										
										<div class="list-area">
											<ul>
												<li class="col-full">
													<label class="required">기본<br>QUERY</label>
													<div class="list-item">
														<textarea id="query" name="query" placeholder="QUERY를 입력해주세요."></textarea>
													</div>
												</li>
												<li>
													<label onclick="goSegCnt();">수신그룹<br>미리보기</label>
													<div class="list-item">
														<p class="inline-txt color-gray" id="txtTotCnt">0명</p>
														<input type="hidden" id="totCnt" name="totCnt" value="0">
														<button type="button" class="btn fullblue" onclick="goQueryTest('002');">미리보기</button>
													</div>
												</li>
												<li class="col-full">
													<label>재발송<br>QUERY</label>
													<div class="list-item">
														<textarea id="retryQuery" name="retryQuery" placeholder="재발송 QUERY를 입력해주세요."></textarea>
													</div>
												</li>
												<li class="col-full">
													<label>실시간<br>QUERY</label>
													<div class="list-item">
														<textarea id="realQuery" name="realQuery" placeholder="QUERY를 입력해주세요."></textarea>
													</div>
												</li>
											</ul>
										</div>
									</div>
									<!-- //쿼리 등록 -->
									
									<!-- btn-wrap// -->
									<div class="btn-wrap">
										<button type="button" class="btn big fullblue" onclick="goQueryTest('001');">등록</button>
										<button type="button" class="btn big" onclick="goCancel();">취소</button>
									</div>
									<!-- //btn-wrap -->
								</div>
								<!-- //tab-cont : 직접SQL -->

							</div>
						</div>
						<!-- //tab -->
							
					</fieldset>
				</form>

			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>

	<!-- 미리보기 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_seg.jsp" %>
	<!-- //미리보기 팝업 -->
	
	<!-- 조회사유팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_search_reason.jsp" %>
	<!-- //조회사유팝업 -->	
	
</body>
</html>
