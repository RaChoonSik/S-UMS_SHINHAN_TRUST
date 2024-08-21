<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.31
	*	설명 : 조직도 관리
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/acc/orgListP.js'/>"></script>

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
					<h2>조직도 목록</h2>
				</div>

				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
			
				<div class="groupchart-box">
					<form id="searchForm" name="searchForm" method="post">
						<input type="hidden" id="orgCd" name="orgCd" value="">
						<input type="hidden" id="orgUpCd" name="orgUpCd" value="">
						<fieldset>
							<legend>조회 및 목록</legend>
							
							<!-- 조회// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">조회</h3>
								</div>
								
								<div class="list-area">
									<ul>
										<li>
											<label>부서코드</label>
											<div class="list-item">
												<input type="text" id="searchOrgCd" name="searchOrgCd" placeholder="부서코드을 입력해주세요.">
											</div>
										</li>
										<li>
											<label>부서명</label>
											<div class="list-item">
												<input type="text" id="searchOrgNm" name="searchOrgNm" placeholder="부서명을 입력해주세요.">
											</div>
										</li>
										<li>
											<label>상위부서명</label>
											<div class="list-item">
												<div class="select">
													<select id="searchUpOrgCd" name="searchUpOrgCd" title="옵션 선택">
														<option value="">선택</option>
														<c:if test="${fn:length(orgList) > 0}">
															<c:forEach items="${orgList}" var="org">
																<option value="<c:out value='${org.orgCd}'/>"<c:if test="${org.orgCd eq searchVO.searchOrgCd}"> selected</c:if>><c:out value='${org.orgNm}'/></option>
															</c:forEach>
														</c:if>	
													</select>
												</div> 
											</div>
										</li>
									</ul>
								</div>
							</div>
							<!-- //조회 -->
	
							<!-- btn-wrap// -->
							<div class="btn-wrap">
								<button type="button" class="btn big fullred" onclick="goSearch()">검색</button>
								<button type="button" class="btn big" onclick="goReset()">초기화</button>
							</div>
							<!-- //btn-wrap -->
	
							<!-- 목록// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">목록</h3>
									<span class="total" >Total: <em id="orgTotal"></em></span>
							
									<!-- 버튼// -->
									<div class="btn-wrap">
										<button type="button" class="btn fullred plus" onclick="goAdd();">신규등록</button>
										<button type="button" class="btn" onclick="goDelete();">삭제</button>
									</div>
									<!-- //버튼 -->
								</div>
							
								<!-- gridtoggle-area// -->
								<div class="gridtoggle-area">
									<div class="grid">
										<ul class="gridhead">
											<li class="col checkbox">
												<label><input type="checkbox" onclick='selectAll(this)'><span></span></label>
											</li>
											<li class="col departmentcode">부서코드</li>
											<li class="col departmentname">부서명</li>
											<li class="col registrant">등록자</li>
											<li class="col enrolldate">등록일</li>
										</ul> 
										<ul class="gridbody">
											<li>
												<ul id="orgList" class="toggle-unfold">
												</ul>
											</li>
										</ul>
									</div>
								</div>
								<!-- //gridtoggle-area -->
							</div>
							<!-- //목록 -->
							 
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
 