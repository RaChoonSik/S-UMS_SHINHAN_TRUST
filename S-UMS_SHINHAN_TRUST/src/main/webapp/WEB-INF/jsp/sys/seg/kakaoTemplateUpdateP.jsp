<%--
	/**********************************************************
	*	작성자 : 김재환
	*	작성일시 : 2021.12.10
	*	설명 : 알림톡 템플릿 신규등록
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/seg/kakaoTemplateUpdateP.js'/>"></script>

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
					<h2>알림톡 템플릿 정보수정</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
				<div class="manage-wrap">
					<form id="searchForm" name="searchForm" method="post">
						<input type="hidden" name="page" value="<c:out value='${searchVO.page}'/>">
						<input type="hidden" name="searchTempCd" value="<c:out value='${searchVO.searchTempCd}'/>">
						<input type="hidden" name="searchTempNm" value="<c:out value='${searchVO.searchTempNm}'/>">
						<input type="hidden" name="searchDeptNo" value="<c:out value='${searchVO.searchDeptNo}'/>">
						<input type="hidden" name="searchUserId" value="<c:out value='${searchVO.searchUserId}'/>">
						<input type="hidden" name="searchStatus" value="<c:out value='${searchVO.searchStatus}'/>">
						<input type="hidden" name="searchStartDt" value="<c:out value='${searchVO.searchStartDt}'/>">
						<input type="hidden" name="searchEndDt" value="<c:out value='${searchVO.searchEndDt}'/>">
					</form>
					
					<form id="kakaoTemplateInfoForm" name="kakaoTemplateInfoForm" method="post">
						<input type="hidden" id="mergyItem"    name="mergyItem"    value=""/>
						<input type="hidden" id="tempCd"       name="tempCd"       value="<c:out value='${kakaoTemplateInfo.tempCd}'/>"/>
						<input type="hidden" id="legalYn"      name="legalYn"      value="<c:out value='${kakaoTemplateInfo.legalYn}'/>"/>
						<input type="hidden" id="weblinkYn"    name="weblinkYn"    value="<c:out value='${kakaoTemplateInfo.weblinkYn}'/>"/>
						<input type="hidden" id="apiMergeCols" name="apiMergeCols" value="">
						<input type="hidden" id="segNo"        name="segNo"        value="<c:out value='${kakaoTemplateInfo.segNo}'/>"/>
						<input type="hidden" id="apiMergeItem"                     value=""/>
						<input type="hidden" id="orgApiTempCd"                     value="<c:out value='${kakaoTemplateInfo.apiTempCd}'/>">
						
						<fieldset>
							<legend>조건 및 템플릿 내용</legend>
	
							<!-- 조건// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">조건</h3>
									<span class="required">*필수입력 항목</span>
								</div>
								
								<div class="list-area">
									<ul>
										<li>
											<label class="required">사용자그룹</label>
											<div class="list-item">
												<div class="select">
													<select id="deptNo" name="deptNo" onchange="getUserList(this.value);" title="사용자그룹 선택">
														<option value="0">선택</option>
														<c:if test="${fn:length(deptList) > 0}">
															<c:forEach items="${deptList}" var="dept">
																<option value="<c:out value='${dept.deptNo}'/>"<c:if test="${dept.deptNo == kakaoTemplateInfo.deptNo}"> selected</c:if>><c:out value='${dept.deptNm}'/></option>
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
														<c:if test="${fn:length(userList) > 0}">
															<c:forEach items="${userList}" var="user">
																<option value="<c:out value='${user.userId}'/>"<c:if test="${user.userId == kakaoTemplateInfo.userId}"> selected</c:if>><c:out value='${user.userNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
										<li>
											<label class="required">템플릿 코드</label>
											<div class="list-item">
												<p class="inline-txt line-height40"><c:out value='${kakaoTemplateInfo.tempCd}'/></p>
												<label for="chkWeblinkYn"><input type="checkbox" id="chkWeblinkYn" name="chkWeblinkYn" <c:if test="${'Y' eq kakaoTemplateInfo.weblinkYn}"> checked </c:if>><span>웹링크</span></label>
											</div>
										</li>
										<li>
											<label>상태</label>
											<div class="list-item">
												<div class="select">
													<select id="status" name="status" title="상태 선택">
														<c:if test="${fn:length(statusList) > 0}">
															<c:forEach items="${statusList}" var="status">
																<option value="<c:out value='${status.cd}'/>"<c:if test="${status.cd eq kakaoTemplateInfo.status}"> selected</c:if>><c:out value='${status.cdNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li> 
										<li>
											<label>등록자</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value="${kakaoTemplateInfo.regNm}"/></p>
											</div>
										</li>
										<li>
											<label>수정자</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value="${kakaoTemplateInfo.upNm}"/></p>
											</div>
										</li>
										<li>
											<label>등록일시</label>
											<div class="list-item">
												<fmt:parseDate var="regDt" value="${kakaoTemplateInfo.regDt}" pattern="yyyyMMddHHmmss"/>
												<fmt:formatDate var="regDt" value="${regDt}" pattern="yyyy.MM.dd HH:mm"/>
												<p class="inline-txt"><c:out value="${regDt}"/></p>
											</div>
										</li>
										<li>
											<label>수정일시</label>
											<div class="list-item">
												<fmt:parseDate var="upDt" value="${kakaoTemplateInfo.upDt}" pattern="yyyyMMddHHmmss"/>
												<fmt:formatDate var="upDt" value="${upDt}" pattern="yyyy.MM.dd HH:mm"/>
												<p class="inline-txt"><c:out value="${upDt}"/></p>
											</div>
										</li>
									</ul>
								</div>
							</div>
							<!-- //조건 --> 
							
							<!-- 템플릿 내용// -->
							<div class="templateInfobox">
								<div class="box">
									<div class="title-area">
										<h3 class="h3-title">템플릿 내용</h3>
										<!-- 버튼// -->
										<button type="button" class="btn fr" onclick="goKakaoTemplatePreview();">미리보기</button>
										<!-- //버튼 -->
									</div>
									
									<div class="list-area">
										<ul>
											<li class="col-full">
												<label class="required">템플릿명</label>
												<div class="list-item">
													<input type="text" id="tempNm" name="tempNm" value="<c:out value='${kakaoTemplateInfo.tempNm}'/>"  placeholder="템플릿명을 입력해주세요." maxlength="128">
												</div>
											</li>
											<li class="col-full">
												<label>설명</label>
												<div class="list-item">
													<textarea id="tempDesc" name="tempDesc" placeholder="템플릿 설명을 입력해주세요"><c:out value='${kakaoTemplateInfo.tempDesc}'/></textarea>
												</div>
											</li>
											<li class="col-full">
												<label class="required">템플릿 내용</label>
												<div class="list-item">
													<textarea id="tempContent"  name="tempContent" placeholder="카카오 승인된 템플릿 내용을 입력해주세요. (머지함수 및 공백을 제외한  1000byte 이내로 입력)" onkeyup="checkByte(this, 1000)" style="height:250px;"><c:out value='${kakaoTemplateInfo.tempContent}'/></textarea>
													<textarea id="tempMappContent" name="tempMappContent" style="display:none;"><c:out value='${kakaoTemplateInfo.tempMappContent}'/></textarea>
												</div>
											</li>
										</ul>
										
									</div>
								</div>
								<div class="box">
									<div class="title-area">
										<h3 class="h3-title">API 템플릿 매핑</h3>
										<button type="button" class="btn fr" onclick="goKakaoApiTemplatePreview();">미리보기</button>
									</div>
	 								<div class="mapp-area">
	 									<div class="function-form">
											<label class="bold">템플릿 선택</label>
											<div class="select" style="margin-top:5px;margin-bottom:20px;">
												<select id="apiTemplateList" name="apiTempCd" onchange="getApiTemplateInfo();" title="Api템플릿 선택" >
													<option value="" selected>선택</option>
													<c:if test="${fn:length(apiTemplateList) > 0}">
														<c:forEach items="${apiTemplateList}" var="apiTemplate">
															<option value="<c:out value='${apiTemplate.tempCd}'/>"
																<c:if test="${apiTemplate.tempCd == kakaoTemplateInfo.apiTempCd}"> selected</c:if>>
															<c:out value='${apiTemplate.tempNm}' />
														</option>
														</c:forEach>
													</c:if>
												</select>
												<select id="apiTemplateMergeList" style="display:none;">
													<c:if test="${fn:length(apiTemplateMergeList) > 0}">
														<c:forEach items="${apiTemplateMergeList}" var="apiTemplateMerge">
															<%-- <option value="<c:out value='${apiTemplateMerge.apiMergeCol}'/>|<c:out value='${apiTemplateMerge.apiKakaoCol}'/>"> --%>
															<option value="<c:out value='${apiTemplateMerge.apiKakaoCol}'/>|<c:out value='${apiTemplateMerge.apiMergeCol}'/>">
														</option>
														</c:forEach>
													</c:if>
												</select>
											</div>
											<div class="grid-area" style="height:450px;">
												<table class="grid">
													<caption>그리드 정보</caption>
													<colgroup>
														<col style="width: 10%;">
														<col style="width: auto;">
														<col style="width: 36%;">
													</colgroup>
													<thead>
														<tr>
															<th scope="col">NO</th>
															<th scope="col">함수명칭</th>
															<th scope="col" style="opacity: 1;">함수입력</th>
														</tr>
													</thead>
													<tbody id="mergeItems">
													</tbody>
												</table>
											</div>
										</div>
									</div>
								</div>
							</div>
							<!-- //템플릿 내용 -->
	
							<!-- btn-wrap// -->
							<div class="btn-wrap">
								<button type="button" class="btn big fullred" onclick="goKakaoTemplateUpdate();">수정</button>
								<button type="button" class="btn big" onclick="goKakaoTemplateListP();">목록</button>
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
	<iframe id="iFrmKakaoTemplate" name="iFrmKakaoTemplate" style="width:0px;height:0px;"></iframe>
	<!-- 카카오 알림톡 미리보기 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_kakao_template.jsp" %>
	
</body>
</html>
