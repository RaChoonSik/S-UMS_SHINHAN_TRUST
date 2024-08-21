<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.01.14
	*	설명 :캠페인별 발송 - 실시간이메일 발송목록화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp" %>


<script type="text/javascript" src="<c:url value='/js/ems/ana/campMailRnsListP.js'/>"></script>

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
					<h2>실시간 메일 발송 목록</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">

				<form id="searchForm" name="searchForm" method="post">
					<input type="hidden" id="page"             name="page" value="<c:out value='${searchVO.page}'/>">
					<input type="hidden" id="rows"             name="rows" value="<c:out value='${searchVO.rows}'/>">
					<input type="hidden" id="orgSearchStartDt" name="orgSearchStartDt" value="<c:out value='${searchVO.searchStartDt}'/>">
					<input type="hidden" id="orgSearchEndDt"   name="orgSearchEndDt"   value="<c:out value='${searchVO.searchEndDt}'/>">
					<input type="hidden" id="orgSearchDeptNo"  name="orgSearchDeptNo"  value="<c:out value='${searchVO.searchDeptNo}'/>">
					<input type="hidden" id="orgSearchUserId"  name="orgSearchUserId"  value="<c:out value='${searchVO.searchUserId}'/>">
					<input type="hidden" id="searchServiceGb"  name="searchServiceGb"  value="<c:out value='${searchVO.searchServiceGb}'/>">
					<input type="hidden" id="searchUserId"     name="searchUserId"     value="<c:out value='${searchVO.searchUserId}'/>">
					<input type="hidden" id="searchDeptNo"     name="searchDeptNo"     value="<c:out value='${searchVO.searchDeptNo}'/>">
					<input type="hidden" id="searchCampNo"     name="searchCampNo"     value="<c:out value='${searchVO.searchCampNo}'/>">
					<input type="hidden" id="searchCampNm"     name="searchCampNm"     value="<c:out value='${searchVO.searchCampNm}'/>">
					<input type="hidden" id="taskNo"           name="taskNo"           value="0">
					<input type="hidden" id="subTaskNo"        name="subTaskNo"        value="1">
					<input type="hidden" id="sendRepeats"      name="sendRepeat"       value="">
					<input type="hidden" id="rnsReSendAuth"                            value="<c:out value='${rnsReSendAuth}'/>">
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
										<label>서비스명</label>
										<div class="list-item">
											<p class="inline-txt" id="campNm"><c:out value='${searchVO.searchCampNm}'/></p>
										</div>
									</li>
									<li>
										<label>메일유형</label>
										<div class="list-item">
											<p class="inline-txt">실시간</p>
										</div>
									</li>
									<li>
										<label>발송일시</label>
										<div class="list-item">
											<!-- datepickerrange// -->
											<div class="datepickerrange fromDate">
												<label>
													<fmt:parseDate var="startDt" value="${searchVO.searchStartDt}" pattern="yyyyMMdd"/>
													<fmt:formatDate var="searchStartDt" value="${startDt}" pattern="yyyy.MM.dd"/> 
													<input type="text" id="searchStartDt" name="searchStartDt" value="<c:out value='${searchStartDt}'/>" readonly>
												</label>
											</div>
											<span class="hyppen date"></span>
											<div class="datepickerrange toDate">
												<label>
													<fmt:parseDate var="endDt" value="${searchVO.searchEndDt}" pattern="yyyyMMdd"/>
													<fmt:formatDate var="searchEndDt" value="${endDt}" pattern="yyyy.MM.dd"/> 
													<input type="text" id="searchEndDt" name="searchEndDt" value="<c:out value='${searchEndDt}'/>" readonly>
												</label>
											</div>
											<!-- //datepickerrange -->
										</div>
									</li>
									<li>
										<label>발송결과</label>
										<div class="list-item">
											<div class="select">
												<select id="searchWorkStatus" name="searchWorkStatus" title="옵션 선택">
													<option value="00">선택</option> 
													<option value="10">성공</option>
													<option value="20">실패</option>
												</select>
											</div>
										</div>
									</li>
									<li>
										<label>고객ID</label>
										<div class="list-item">
											<input type="text" id="searchCustId" name="searchCustId" value="<c:out value='${searchVO.searchCustId}'/>" placeholder="ID를 입력해주세요.">
										</div>
									</li>
									<li>
										<label>고객명</label>
										<div class="list-item">
											<input type="text" id="searchCustNm" name="searchCustNm" value="<c:out value='${searchVO.searchCustNm}'/>" placeholder="고객명을 입력해주세요.">
										</div>
									</li>									
									<li>
										<label>고객 이메일</label>
										<div class="list-item">
											<input type="text" id="searchCustEm" name="searchCustEm" value="<c:out value='${searchVO.searchCustEm}'/>" placeholder="이메일을 입력해주세요.">
										</div>
									</li>
								</ul>
							</div>
						</div>
						<!-- //조회 -->

						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullblue" onclick="goSearch('1');">검색</button>
							<button type="button" class="btn big" onclick="goInit();">초기화</button>
						</div>
						<!-- //btn-wrap -->

						<!-- 목록&페이징// -->
						<div id="divCampMailRnsList" style="margin-top:50px;"></div>
						<!-- //목록&페이징 -->
							
					</fieldset>
				</form>
				<div style="width:0px;height:0px;display:none;">
				<iframe id="iFrmExcel" name="iFrmExcel" style="width:0px;height:0px;"></iframe>
				</div>
				
			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>
	
	<!--  메일 정보 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_mail_detail_ems.jsp" %>
	<!-- //자동발송 메일 정보 -->
	
	<!-- 웹에이전트미리보기 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_webagent.jsp" %>
	<!-- //웹에이전트미리보기 팝업 -->
</body>
</html>
