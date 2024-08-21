<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.18
	*	설명 : 사용자그룹 정보수정
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/acc/deptUpdate.js'/>"></script>

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
					<h2>사용자그룹 정보수정</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body"> 
				<form id="deptInfoForm" name="deptInfoForm" method="post">
					<input type="hidden" name="page" value="<c:out value='${searchVO.page}'/>">
					<input type="hidden" name="searchDeptNm" value="<c:out value='${searchVO.searchDeptNm}'/>">
					<input type="hidden" name="searchStatus" value="<c:out value='${searchVO.searchStatus}'/>"> 
					<fieldset>
						<legend>내용</legend>

						<!-- 조건// -->
						<div class="graybox">
							<div class="title-area">
								<h3 class="h3-title">내용</h3> 
								<span class="required">
									*필수입력 항목
								</span>
							</div>

							<div class="list-area">
								<ul>
									<li class="col-full">
										<label class="required">번호</label>
										<div class="list-item">
											<p class="inline-txt"><c:out value="${deptInfo.deptNo}"/></p>
											<input type="hidden" id="deptNo" name="deptNo" value="<c:out value='${deptInfo.deptNo}'/>" >
										</div>
									</li>
									<li>
										<label class="required">사용자그룹</label>
										<div class="list-item">
											<input type="text" id="deptNm" name="deptNm"  placeholder="등록data" value="<c:out value='${deptInfo.deptNm}'/>">
										</div>	
									</li>
									<li>
										<label class="required">사용자<br>그룹상태</label>
										<div class="list-item">
											<div class="select">
												<select id="status" name="status" title="사용자그룹 상태 선택">
													<c:if test="${fn:length(deptStatusList) > 0}">
														<c:forEach items="${deptStatusList}" var="deptStatus">
															<option value="<c:out value='${deptStatus.cd}'/>"<c:if test="${deptStatus.cd eq deptInfo.status}"> selected</c:if>><c:out value='${deptStatus.cdNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
									<li class="col-full">
										<label>사용자<br>그룹설명</label>
										<div class="list-item">
											<textarea id="deptDesc" name="deptDesc" placeholder="등록data"><c:out value="${deptInfo.deptDesc}"/></textarea>
										</div>
									</li>
								</ul>
								<ul>
									<li>
										<label>등록자</label>
										<div class="list-item">
											<p class="inline-txt"><c:out value="${deptInfo.regNm}"/></p>
										</div>
									</li>

									<li>
										<label>수정자</label>
										<div class="list-item">
											<p class="inline-txt"><c:out value="${deptInfo.upNm}"/></p>
										</div>
									</li>

									<li>
										<label>등록일시</label>
										<div class="list-item">
											<fmt:parseDate var="regDt" value="${deptInfo.regDt}" pattern="yyyyMMddHHmmss"/>
											<fmt:formatDate var="regDt" value="${regDt}" pattern="yyyy.MM.dd HH:mm"/>
											<p class="inline-txt"><c:out value="${regDt}"/></p>
										</div>
									</li>

									<li>
										<label>수정일시</label>
										<div class="list-item">
											<fmt:parseDate var="upDt" value="${deptInfo.upDt}" pattern="yyyyMMddHHmmss"/>
											<fmt:formatDate var="upDt" value="${upDt}" pattern="yyyy.MM.dd HH:mm"/>
											<p class="inline-txt"><c:out value="${upDt}"/></p>
										</div>
									</li>
								</ul>
							</div>
						</div>
						<!-- //조건 --> 

						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullred" onclick="goUpdate();">수정</button>
							<button type="button" class="btn" onclick="goCancel();">취소</button>
						</div>
						<!-- //btn-wrap -->
							
					</fieldset>
				</form> 
				
			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>

</body>
</html>
