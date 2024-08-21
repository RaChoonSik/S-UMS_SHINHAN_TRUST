<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.05
	*	설명 : 코드목록 관리 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/cod/userCodeListP.js'/>"></script>

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
					<h2>공통코드 관리</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body commoncode">

				<form id="userCodeInfoForm" name="userCodeInfoForm" method="post">
				<input type="hidden" name="cd" value="">
				<input type="hidden" name="cdGrp" id="cdGrp" value="">
				<input type="hidden" id="cdGrpNm" value="<c:out value='${cdGrpList}'/>">
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
										<label>분류코드</label>
										<div class="list-item">
											<div class="select" style="width:calc(100% - 52px);">
												<select id="searchCdGrp" name="searchCdGrp" title="분류코드 선택" onchange="searchUpCdSelect(this.value)">
													<option value="0">필수</option>
													<c:if test="${fn:length(cdGrpList) > 0}">
														<c:forEach items="${cdGrpList}" var="cdGrp">
															<option value="<c:out value='${cdGrp.cdGrp}'/>"
																<c:if test="${cdGrp.cdGrp eq searchVO.searchCdGrp}"> selected</c:if>>
																<c:choose>
																	<c:when test="${cdGrp.sysYn eq 'Y'}">
																		[<c:out value='${cdGrp.cdGrp}'/>]<c:out value='${cdGrp.cdGrpNm}'/> - 수정불가
																	</c:when>
																	<c:otherwise>
																		[<c:out value='${cdGrp.cdGrp}'/>]<c:out value='${cdGrp.cdGrpNm}'/> 
																	</c:otherwise>
																</c:choose>
															</option>
														</c:forEach>
													</c:if>
												</select>
											</div>
											<!-- btn-wrap// -->
											<!-- <div class="btn-wrap"> -->
												<button type="button" class="btn fullred" onclick="goSearch('1')">검색</button>
												<!-- <button type="button" class="btn big" onclick="goReset()">초기화</button> -->
											<!-- </div> -->
											<!-- //btn-wrap -->
										</div>
									</li>
									<li><!-- 2022년 3월 14일 추가 -->
										<label>현재 분류코드</label>
										<div class="list-item">
											<p id="viewGrp" class="inline-txt">[CNP120]guswo qnsfb</p>
										</div>
									</li> 
								</ul>
							</div>
						</div>
						<!-- //조회 -->

						<!-- 목록 -->
						<div class="graybox-wrap">
							<div id="divUserCodeList"></div>
							<!-- //목록 -->
							<!-- 신규등록, 정보수정 데이터 입력 목록// -->
								<div class="graybox col-single">
									<div class="title-area">
										<h3 class="h3-title">데이터 입력</h3>
	
										<!-- 버튼 영역// -->
										<div class="btn-wrap">
											<button type="button" id="btnAddCode" class="btn fullred plus" onclick="goAdd();">신규등록</button>
											<!-- <button type="button" class="btn fullred">수정</button> -->
											<button type="button" id="btnUpdateCode" class="btn" onclick="goUpdate();">저장</button>
										</div>
										<!-- //버튼 영역 -->
									</div>
									
									<div class="list-area">
										<ul>
											<li>
												<label>공통코드</label>
												<div class="list-item">
													<input type="text" id="cd" placeholder="공통 코드는 자동 발번 됩니다" disabled >
												</div>
											</li>
											<li> 
												<label class="required">코드명</label>
												<div class="list-item">
													<input type="text" id="cdNm" name="cdNm" placeholder="코드명을 입력하여주세요" maxlength="60">
												</div>	
											</li>
											<li>
												<label>상위분류</label>
												<div class="list-item">
													<div class="select">
														<select id="upCd" name="upCd" title="상위분류의 코드선택">
															<option value="">선택</option>
														</select>
													</div>
												</div>
											</li>
											<li>
												<label class="required">사용여부</label>
												<div class="list-item">
													<div class="select">
														<select id="useYn" name="useYn" title="사용여부 선택">
															<option value="Y"> 예 </option>
															<option value="N"> 아니오 </option>
														</select>
													</div>
												</div>
											</li>
											<li class="col-full">
												<label>설명</label>
												<div class="list-item">
													<textarea id="cdDtl" name="cdDtl" placeholder="코드 설명을 입력해주세요."></textarea>
												</div>
											</li>
										</ul>
	
										<ul style="margin-top:20px;">
											<li>
												<label>수정일시</label>
												<div class="list-item">
													<p class="inline-txt" id="upDt" ></p>
												</div>
											</li>
											<li>
												<label>수정자</label>
												<div class="list-item">
													<p class="inline-txt" id="upNm"></p>
												</div>
											</li>
											<li>
												<label>등록일시</label>
												<div class="list-item">
													<p class="inline-txt" id="regDt"></p>
												</div>
											</li>
											<li>
												<label>등록자</label>
												<div class="list-item">
													<p class="inline-txt" id="regNm"></p>
												</div> 
											</li>
										</ul>
									</div>
								</div>
							<!-- //신규등록, 정보수정 데이터 입력 목록 -->
						</div>
					</fieldset>
				</form>
			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>

</body>
</html>
				
 