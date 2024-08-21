<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.05
	*	설명 : 코드 신규등록
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/cod/userCodeUpdate.js'/>"></script>

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
					<h2>코드 정보수정</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->


			<!-- cont-body// -->
			<section class="cont-body">

				<div class="manage-wrap">
					<form id="userCodeInfoForm" name="userCodeInfoForm" method="post">
						<input type="hidden" name="page" value="<c:out value='${searchVO.page}'/>">
						<input type="hidden" name="cd" value="<c:out value='${userCode.cd}'/>">
						<input type="hidden" name="cdGrp" value="<c:out value='${userCode.cdGrp}'/>">
						<input type="hidden" name="searchCdGrp" value="<c:out value='${searchVO.searchCdGrp}'/>">
						<input type="hidden" name="searchCdGrpNm" value="<c:out value='${searchVO.searchCdGrpNm}'/>"> 
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
											<label class="required">분류코드</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value='${userCode.cdGrp}'/></p>
											</div>
										</li>
										<li>
											<label class="required">분류코드명</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value='${userCode.cdGrpNm}'/></p>
											</div>	
										</li>
										<li>
											<label class="required">공통코드</label>
											<div class="list-item">
												<p class="inline-txt" ><c:out value='${userCode.cd}'/></p>
											</div>
										</li>
										<li>
											<label class="required">공통코드명</label>
											<div class="list-item">
												<input type="text" id="cdNm" name="cdNm" value="<c:out value='${userCode.cdNm}'/>" >
											</div>	
										</li>
										<li>
											<label>상위분류</label>
											<div class="list-item">
												<div class="select">
													<select id="upCd" name="upCd" title="상위분류의 코드선택">
														<option value="">선택</option>
														<c:if test="${fn:length(upCdList) > 0}">
															<c:forEach items="${upCdList}" var="upCd">
																<option value="<c:out value='${upCd.cd}'/>"<c:if test="${upCd.cd eq userCode.upCd}"> selected</c:if>><c:out value='${upCd.cdNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
										<li>
											<label class="required">사용여부</label>
											<div class="list-item">
												<div class="select">
													<select id="useYn" name="useYn" title="사용여부 선택">
														<option value="Y" <c:if test="${'Y' eq userCode.useYn}">selected</c:if>> 예</option>
														<option value="N" <c:if test="${'N' eq userCode.useYn}">selected</c:if>> 아니오</option>
													</select>
												</div>
											</div>
										</li>
										<li class="col-full">
											<label>설명</label>
											<div class="list-item">
												<textarea id="cdDtl" name="cdDtl" placeholder="코드 설명을 입력해주세요."><c:out value="${userCode.cdDtl}"/></textarea>
											</div>
										</li>
									</ul>
									<ul>
										<li>
											<label>등록자</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value="${userCode.regNm}"/></p>
											</div> 
										</li>
	
										<li>
											<label>수정자</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value="${userCode.upNm}"/></p>
											</div>
										</li>
	
										<li>
											<label>등록일시</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value="${userCode.regDt}"/></p>
											</div>
										</li>
	
										<li>
											<label>수정일시</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value="${userCode.upDt}"/></p>
											</div>
										</li>
	
									</ul>
								</div>
							</div>
							<!-- //조건 --> 
	
							<!-- btn-wrap// -->
							<div class="btn-wrap">
								<c:if test="${'N' eq userCode.sysYn}">
									<button type="button" class="btn big fullred" onclick="goUpdate();">수정</button>
								</c:if>
								<c:if test="${'Y' eq userCode.sysYn}">
									<button type="button" class="btn big fullred" disabled>수정</button>
								</c:if>								
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
