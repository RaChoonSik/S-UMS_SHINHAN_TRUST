<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.18
	*	설명 : 사용자그룹 신규등록
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/acc/deptAdd.js'/>"></script>

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
					<h2>사용자그룹 신규등록</h2>
				</div>

				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->

			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
				<form id="deptInfoForm" name="deptInfoForm" method="post">
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
									<li>
										<label class="required">사용자그룹</label>
										<div class="list-item">
											<input type="text" id="deptNm" name="deptNm" maxlength="40" placeholder="사용자그룹명을 입력해주세요.">
										</div>
									</li>
									<li>
										<label class="required">사용자<br>그룹상태</label>
										<div class="list-item">
											<div class="select">
												<select id="status" name="status" title="옵션 선택" disabled="disabled">
													<c:if test="${fn:length(deptStatusList) > 0}">
														<c:forEach items="${deptStatusList}" var="deptStatus">
															<option value="<c:out value='${deptStatus.cd}'/>"<c:if test="${deptStatus.cd eq '000'}"> selected</c:if>><c:out value='${deptStatus.cdNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
									<li class="col-full">
										<label>사용자<br>그룹설명</label>
										<div class="list-item">
											<textarea id="deptDesc" name="deptDesc" placeholder="사용자그룹설명을 입력해주세요" maxlength="400"></textarea>
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
				
			</section>
			<!-- //cont-body -->

		</div>
		<!-- // content -->
	</div>

</body>
</html>
