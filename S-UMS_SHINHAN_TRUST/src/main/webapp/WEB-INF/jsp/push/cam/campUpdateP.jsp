<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.04.15
	*	설명 : 캠페인 정보 수정 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_push.jsp" %>

<script type="text/javascript" src="<c:url value='/js/push/cam/campUpdateP.js'/>"></script>

<body>
	<div id="wrap" class="push">

		<!-- lnb// -->
		<div id="lnb">
			<!-- LEFT MENU -->
			<%@ include file="/WEB-INF/jsp/inc/menu_push.jsp" %>
			<!-- LEFT MENU -->
		</div>
		<!-- //lnb -->

		<!-- content// -->
		<div id="content">

			<!-- cont-head// -->
			<section class="cont-head">
				<div class="title">
					<h2>캠페인 정보수정</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">

				<div class="campaign-wrap">
					<form id="searchForm" name="searchForm" method="post">
						<input type="hidden" name="page" value="<c:out value='${searchVO.page}'/>">
						<input type="hidden" name="campNo" value="<c:out value='${searchVO.campNo}'/>">
						<input type="hidden" name="searchStartDt" value="<c:out value='${searchVO.searchStartDt}'/>">
						<input type="hidden" name="searchEndDt" value="<c:out value='${searchVO.searchEndDt}'/>">
						<input type="hidden" name="searchCampNm" value="<c:out value='${searchVO.searchCampNm}'/>">
						<input type="hidden" name="searchCampTy" value="<c:out value='${searchVO.searchCampTy}'/>">
						<input type="hidden" name="searchDeptNo" value="<c:out value='${searchVO.searchDeptNo}'/>">
						<input type="hidden" name="searchUserId" value="<c:out value='${searchVO.searchUserId}'/>">
						<input type="hidden" name="searchStatus" value="<c:out value='${searchVO.searchStatus}'/>">
					</form>
					<form id="campaignInfoForm" name="campaignInfoForm" method="post">
						<input type="hidden" name="campNo" value="<c:out value='${searchVO.campNo}'/>">
						<fieldset>
							<legend>조건 및 캠페인 내용</legend>
	
							<!-- 조건// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">조건</h3>
									<span class="required">*필수입력 항목</span>
								</div>
								
								<div class="list-area">
									<ul>
										<li>
											<label class="required">캠페인목적</label>
											<div class="list-item">
												<div class="select">
													<select id="campTy" name="campTy" title="캠페인목적 선택">
														<option value="">선택</option>
														<c:if test="${fn:length(campTyList) > 0}">
															<c:forEach items="${campTyList}" var="campTy">
																<option value="<c:out value='${campTy.cd}'/>"<c:if test="${campTy.cd eq campInfo.campTy}"> selected</c:if>><c:out value='${campTy.cdNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
										<li>
											<label class="required">사용자그룹</label>
											<div class="list-item">
												<div class="select">
													<!-- 관리자의 경우 전체 요청그룹을 전시하고 그 외의 경우에는 해당 그룹만 전시함 -->
													<c:if test="${'Y' eq NEO_ADMIN_YN}">
														<select id="deptNo" name="deptNo" onchange="getUserList(this.value);" title="사용자그룹 선택">
															<option value="0">선택</option>
															<c:if test="${fn:length(deptList) > 0}">
																<c:forEach items="${deptList}" var="dept">
																	<option value="<c:out value='${dept.deptNo}'/>"<c:if test="${dept.deptNo == campInfo.deptNo}"> selected</c:if>><c:out value='${dept.deptNm}'/></option>
																</c:forEach>
															</c:if>
														</select>
													</c:if>
													<c:if test="${'N' eq NEO_ADMIN_YN}">
														<select id="deptNo" name="deptNo" title="사용자그룹 선택">
															<c:if test="${fn:length(deptList) > 0}">
																<c:forEach items="${deptList}" var="dept">
																	<c:if test="${dept.deptNo == searchVO.searchDeptNo}">
																		<option value="<c:out value='${dept.deptNo}'/>"<c:if test="${dept.deptNo == campInfo.deptNo}"> selected</c:if>><c:out value='${dept.deptNm}'/></option>
																	</c:if>
																</c:forEach>
															</c:if>
														</select>
													</c:if>
												</div>
											</div>
										</li>
										<li>
											<label class="required">사용자명</label>
											<div class="list-item">
												<div class="select">
													<select id="userId" name="userId" title="사용자명 선택">
														<option value="">선택</option>
														<c:if test="${fn:length(userList) > 0}">
															<c:forEach items="${userList}" var="user">
																<option value="<c:out value='${user.userId}'/>"<c:if test="${user.userId eq campInfo.userId}"> selected</c:if>><c:out value='${user.userNm}'/></option>
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
														<c:if test="${fn:length(statusList) > 0}">
															<c:forEach items="${statusList}" var="status">
																<option value="<c:out value='${status.cd}'/>"<c:if test="${status.cd eq campInfo.status}"> selected</c:if>><c:out value='${status.cdNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
										<%--
										<li>
											<label>EAI 번호</label>
											<div class="list-item">
												<input type="text" id="eaiCampNo" name="eaiCampNo" value="<c:out value='${campInfo.eaiCampNo}'/>" maxlength="40" style="text-transform:uppercase;" placeholder="EAI 번호를 입력해주세요.">
											</div>
										</li>
										--%>
									</ul>
								</div>
							</div>
							<!-- //조건 -->
	
							<!-- 캠페인 내용// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">캠페인 내용</h3>
								</div>
								
								<div class="list-area">
									<ul>
										<li class="col-full">
											<label class="required">캠페인명</label>
											<div class="list-item">
												<input type="text" id="campNm" name="campNm" placeholder="캠페인명을 입력해주세요." value="<c:out value='${campInfo.campNm}'/>">
											</div>
										</li>
										<li class="col-full">
											<label>설명</label>
											<div class="list-item">
												<textarea id="campDesc" name="campDesc" placeholder="캠페인 설명을 입력해주세요."><c:out value="${campInfo.campDesc}"/></textarea>
											</div>
										</li>
									</ul>
	
									<ul>
										<li>
											<label>등록자</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value="${campInfo.regNm}"/></p>
											</div>
										</li>
	
										<li>
											<label>수정자</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value="${campInfo.upNm}"/></p>
											</div>
										</li>
	
										<li>
											<label>등록일시</label>
											<fmt:parseDate var="regDt" value="${campInfo.regDt}" pattern="yyyyMMddHHmmss"/>
											<fmt:formatDate var="regDt" value="${regDt}" pattern="yyyy.MM.dd HH:mm"/>
											<div class="list-item">
												<p class="inline-txt"><c:out value="${regDt}"/></p>
											</div>
										</li>
	
										<li>
											<label>수정일시</label>
											<fmt:parseDate var="upDt" value="${campInfo.upDt}" pattern="yyyyMMddHHmmss"/>
											<fmt:formatDate var="upDt" value="${upDt}" pattern="yyyy.MM.dd HH:mm"/>
											<div class="list-item">
												<p class="inline-txt"><c:out value="${upDt}"/></p>
											</div>
										</li>
	
									</ul>
								</div>
							</div>
							<!-- //캠페인 내용 -->
	
							<!-- btn-wrap// -->
							<div class="btn-wrap">
								<button type="button" class="btn big fullorange" onclick="goUpdate();">수정</button>
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
