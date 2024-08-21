<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.05
	*	설명 : 코드그룹 신규등록
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/cod/userCodeGroupAdd.js'/>"></script>

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
					<h2>분류 코드 신규등록</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">

				<div class="manage-wrap">
					<form id="userCodeGroupInfoForm" name="userCodeGroupInfoForm" method="post">
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
											<label>분류코드</label>
											<div class="list-item">
												<input type="text" id="cdGrp" name="cdGrp" placeholder="분류 코드는 자동 발법 됩니다" disabled>
											</div>
										</li>
										<li> 
											<label class="required">분류명</label>
											<div class="list-item">
												<input type="text" id="cdGrpNm" name="cdGrpNm" placeholder="분류 코드명을 입력하여주세요" maxlength="60">
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
																<option value="<c:out value='${cdGrp.cd}'/>"><c:out value='${cdGrp.cdNm}'/></option>
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
												<textarea id="cdGrpDtl" name="cdGrpDtl" placeholder="분류코드 설명을 입력해주세요." maxlength="100"></textarea>
											</div>
										</li>
									</ul>
								</div>
							</div>
							<!-- //조건 --> 
	
							<!-- btn-wrap// -->
							<div class="btn-wrap">
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
