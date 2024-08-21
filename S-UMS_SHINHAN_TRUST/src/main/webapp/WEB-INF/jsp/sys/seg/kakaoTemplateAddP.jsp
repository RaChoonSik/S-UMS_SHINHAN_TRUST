<%--
	/**********************************************************
	*	작성자 : 김재환
	*	작성일시 : 2021.12.10
	*	설명 : 알림톡 템플릿 신규등록
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/seg/kakaoTemplateAddP.js'/>"></script>

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
					<h2>알림톡 템플릿 신규등록</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
				<div class="manage-wrap">
					<form id="searchForm" name="kakaoTemplateInfoForm" method="post">
						<input type="hidden" name="page"          value="<c:out value='${searchVO.page}'/>">
						<input type="hidden" name="searchTempCd"  value="<c:out value='${searchVO.searchTempCd}'/>">
						<input type="hidden" name="searchTempNm"  value="<c:out value='${searchVO.searchTempNm}'/>">
						<input type="hidden" name="searchDeptNo"  value="<c:out value='${searchVO.searchDeptNo}'/>">
						<input type="hidden" name="searchUserId"  value="<c:out value='${searchVO.searchUserId}'/>">
						<input type="hidden" name="searchStatus"  value="<c:out value='${searchVO.searchStatus}'/>">
						<input type="hidden" name="searchStartDt" value="<c:out value='${searchVO.searchStartDt}'/>">
						<input type="hidden" name="searchEndDt"   value="<c:out value='${searchVO.searchEndDt}'/>">
					</form>
					<form id="kakaoTemplateInfoForm" name="kakaoTemplateInfoForm" method="post">
						<input type="hidden" id="mergyItem"    name="mergyItem"    value=""/>
						<input type="hidden" id="legalYn"      name="legalYn"      value="N">
						<input type="hidden" id="weblinkYn"    name="weblinkYn"    value="N">
						<input type="hidden" id="apiMergeCols" name="apiMergeCols" value="">
						<input type="hidden" id="segNo"        name="segNo"        value="0">
						
						<input type="hidden" id="apiMergeItem"                     value=""/>
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
											<label class="required">사용자그룹</label>
											<div class="list-item">
												<div class="select">
													<c:if test="${not empty deptList}"> 
														<select id="deptNo" name="deptNo" onchange="getUserList(this.value);" title="사용자그룹 선택">
															<option value="0">선택</option>
															<c:forEach items="${deptList}" var="dept">
																<option value="<c:out value='${dept.deptNo}'/>"><c:out value='${dept.deptNm}'/></option>
															</c:forEach>
														</select>
													</c:if>
												</div>
											</div>
										</li>
										<li>
											<label class="required">사용자명</label>
											<div class="list-item">
												<div class="select">
													<select id="userId" name="userId" title="사용자 선택">
														<option value="">선택</option>
													</select>
												</div>
											</div>
										</li>
										<li>
											<label class="required">템플릿 코드</label>
											<div class="list-item">
												<input type="text" id="tempCd" name="tempCd" placeholder="템플릿 코드를 입력해주세요.">
												
											</div>
										</li>
										<li>
											<label>광고여부</label>
											<div class="list-item">
												<label for="chkWeblinkYn"><input type="checkbox" id="chkWeblinkYn" name="chkWeblinkYn"><span>웹링크</span></label>
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
													<input type="text" id="tempNm" name="tempNm" placeholder="템플릿명을 입력해주세요." maxlength="128">
												</div>
											</li>
											<li class="col-full">
												<label>설명</label>
												<div class="list-item">
													<textarea id="tempDesc" name="tempDesc" placeholder="템플릿 설명을 입력해주세요"></textarea>
												</div>
											</li>
											<li class="col-full">
												<label class="required">템플릿 내용</label>
												<div class="list-item">
													<textarea id="tempContent"  name="tempContent" placeholder="카카오 승인된 템플릿 내용을 입력해주세요. (머지함수 및 공백을 제외한 1000byte 이내로 입력)" onkeyup="checkByte(this, 1000)" style="height:250px;"></textarea>
													<textarea id="tempMappContent" name="tempMappContent" style="display:none;"></textarea>
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
															<option value="<c:out value='${apiTemplate.tempCd}'/>"><c:out value='${apiTemplate.tempNm}' />
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
								<button type="button" class="btn big fullred" onclick="goKakaoTemplateAdd();">등록</button>
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
	
	<iframe id="iFrmMail" name="iFrmMail" style="width:0px;height:0px;"></iframe>
	<!-- 카카오 알림톡 미리보기 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_kakao_template.jsp" %>
	<!-- 카카오 알림톡 미리보기 팝업// -->

</body>
</html>
