<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.05
	*	설명 : 코드그룹 신규등록
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/cod/userCodeGroupUpdate.js'/>"></script>

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
					<h2>코드그룹 정보수정</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->


			<!-- cont-body// -->
			<section class="cont-body">
 
				<form id="userCodeGroupInfoForm" name="userCodeGroupInfoForm" method="post">
					<input type="hidden" name="page" value="<c:out value='${searchVO.page}'/>">
					<input type="hidden" name="cdGrp" value="<c:out value='${userCodeGroup.cdGrp}'/>">
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
											<p class="inline-txt"><c:out value='${userCodeGroup.cdGrp}'/></p>
										</div>
									</li>
									<li>
										<label class="required">분류명</label>
										<div class="list-item">
											<input type="text" id="cdGrpNm" name="cdGrpNm" value="<c:out value='${userCodeGroup.cdGrpNm}'/>" >
										</div>	
									</li>
									<li>
										<label>상위분류</label>
										<div class="list-item">
											<div class="select">
												<select id="upCdGrp" name="upCdGrp" title="상위분류 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(cdGrpList) > 0}">
														<c:forEach items="${cdGrpList}" var="cdGrp">
															<option value="<c:out value='${cdGrp.cd}'/>"<c:if test="${cdGrp.cd eq userCodeGroup.upCdGrp}"> selected</c:if>><c:out value='${cdGrp.cdNm}'/></option>
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
													<option value="Y" <c:if test="${'Y' eq userCodeGroup.useYn}">selected</c:if>> 예</option>
													<option value="N" <c:if test="${'N' eq userCodeGroup.useYn}">selected</c:if>> 아니오</option>
												</select>
											</div>
										</div>
									</li>
									<li class="col-full">
										<label>설명</label>
										<div class="list-item">
											<textarea id="cdGrpDtl" name="cdGrpDtl" placeholder="코드그룹 설명을 입력해주세요."><c:out value="${userCodeGroup.cdGrpDtl}"/></textarea>
										</div>
									</li>
								</ul>
								<ul>
									<li>
										<label>등록자</label>
										<div class="list-item">
											<p class="inline-txt"><c:out value="${userCodeGroup.regNm}"/></p>
										</div>
									</li>

									<li>
										<label>수정자</label>
										<div class="list-item">
											<p class="inline-txt"><c:out value="${userCodeGroup.upNm}"/></p>
										</div>
									</li>

									<li>
										<label>등록일시</label>
										<div class="list-item">
											<p class="inline-txt"><c:out value="${userCodeGroup.regDt}"/></p>
										</div> 
									</li> 
									<li>
										<label>수정일시</label>
										<div class="list-item">
											<p class="inline-txt"><c:out value="${userCodeGroup.upDt}"/></p>
										</div> 
									</li> 
								</ul>
							</div>
						</div>
						<!-- //조건 --> 

						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<c:if test="${'N' eq userCodeGroup.sysYn}">
								<button type="button" class="btn big fullred" onclick="goUpdate();">수정</button>
							</c:if>
							<c:if test="${'Y' eq userCodeGroup.sysYn}">
								<button type="button" class="btn big fullred" disabled>수정</button>
							</c:if>								
							<button type="button" class="btn big" onclick="goCancel();">취소</button>
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
