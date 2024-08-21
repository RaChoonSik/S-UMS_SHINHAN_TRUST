<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.21
	*	설명 : RNS 템플릿 수정 화면을 출력한다.
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_rns.jsp" %>

<script type="text/javascript" src="<c:url value='/smarteditor/js/HuskyEZCreator.js'/>" charset="utf-8"></script>
<script type="text/javascript" src="<c:url value='/js/rns/tmp/tempUpdateP.js'/>"></script>
<script type="text/javascript">
$(document).ready(function() {
	setTimeout(function(){
		setTempContent("<c:out value='${tempInfo.tempFlPath}'/>");
	},1000);
});
</script>

<body>
	<div id="wrap" class="rns">

		<!-- lnb// -->
		<div id="lnb">
			<!-- LEFT MENU -->
			<%@ include file="/WEB-INF/jsp/inc/menu_rns.jsp" %>
			<!-- LEFT MENU -->
		</div>
		<!-- //lnb -->

		<!-- content// -->
		<div id="content">

			<!-- cont-head// -->
			<section class="cont-head">
				<div class="title">
					<h2>이메일 템플릿 정보수정</h2>
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
						<input type="hidden" name="page" value="<c:out value='${searchVO.page}'/>"/>
						<input type="hidden" name="searchStartDt" value="<c:out value='${searchVO.searchStartDt}'/>"/>
						<input type="hidden" name="searchEndDt" value="<c:out value='${searchVO.searchEndDt}'/>"/>
						<input type="hidden" name="searchTempNm" value="<c:out value='${searchVO.searchTempNm}'/>"/>
						<input type="hidden" name="searchDeptNo" value="<c:out value='${searchVO.searchDeptNo}'/>"/>
						<input type="hidden" name="searchUserId" value="<c:out value='${searchVO.searchUserId}'/>"/>
						<input type="hidden" name="searchStatus" value="<c:out value='${searchVO.searchStatus}'/>"/>
					</form>
					<form id="templateInfoForm" name="templateInfoForm" method="post">
						<input type="hidden" id="tempNo" name="tempNo" value="<c:out value='${tempInfo.tempNo}'/>"/>
						<input type="hidden" id="channel" name="channel" value="<c:out value='${tempInfo.channel}'/>"/>
						<input type="hidden" id="tempVal" name="tempVal" value="">
						<input type="hidden" id="tempFlPath" name="tempFlPath" value="<c:out value='${tempInfo.tempFlPath}'/>"/>
						<fieldset>
							<legend>조건 및 메일 내용</legend>
	
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
													<!-- 관리자의 경우 전체 요청그룹을 전시하고 그 외의 경우에는 해당 그룹만 전시함 -->
													<c:if test="${'Y' eq NEO_ADMIN_YN}">
														<select id="deptNo" name="deptNo" onchange="getUserList(this.value);" title="사용자그룹 선택">
															<option value="0">선택</option>
															<c:if test="${fn:length(deptList) > 0}">
																<c:forEach items="${deptList}" var="dept">
																	<option value="<c:out value='${dept.deptNo}'/>"<c:if test="${dept.deptNo == tempInfo.deptNo}"> selected</c:if>><c:out value='${dept.deptNm}'/></option>
																</c:forEach>
															</c:if>
														</select>
													</c:if>
													<c:if test="${'N' eq NEO_ADMIN_YN}">
														<select id="deptNo" name="deptNo" title="사용자그룹 선택">
															<c:if test="${fn:length(deptList) > 0}">
																<c:forEach items="${deptList}" var="dept">
																	<c:if test="${dept.deptNo == tempInfo.deptNo}">
																		<option value="<c:out value='${dept.deptNo}'/>"><c:out value='${dept.deptNm}'/></option>
																	</c:if>
																</c:forEach>
															</c:if>
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
														<c:if test="${fn:length(userList) > 0}">
															<c:forEach items="${userList}" var="user">
																<option value="<c:out value='${user.userId}'/>"<c:if test="${user.userId eq tempInfo.userId}"> selected</c:if>><c:out value='${user.userNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
										<li>
											<label class="required">상태</label>
											<div class="list-item">
												<div class="select">
													<select id="status" name="status" title="상태 선택">
														<option value="">선택</option>
														<c:if test="${fn:length(statusList) > 0}">
															<c:forEach items="${statusList}" var="status">
																<option value="<c:out value='${status.cd}'/>"<c:if test="${status.cd eq tempInfo.status}"> selected</c:if>><c:out value='${status.cdNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
									</ul>
								</div>
							</div>
							<!-- //조건 -->
	
							<!-- 메일 내용// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">템플릿 내용</h3>
	
									<!-- 버튼// -->
									<div class="btn-wrap">
										<button type="button" class="btn" onclick="fn.popupOpen('#popup_html');">HTML등록</button>
										<!--button type="button" class="btn" onclick="previewTemplate();">미리보기</button-->
									</div>
									<!-- //총 건 -->
								</div>
								
								<div class="list-area">
									<ul>
										<li class="col-full">
											<label class="required">템플릿명</label>
											<div class="list-item">
												<input type="text" id="tempNm" name="tempNm" value="<c:out value='${tempInfo.tempNm}'/>" placeholder="템플릿명을 입력해주세요.">
											</div>
										</li>
										<li class="col-full">
											<label>설명</label>
											<div class="list-item">
												<textarea id="tempDesc" name="tempDesc" placeholder="템플릿 설명을 입력해주세요."><c:out value='${tempInfo.tempDesc}'/></textarea>
											</div>
										</li>
										<li class="col-full">
											<!-- 에디터 영역// -->
											<div class="editbox">
												<textarea name="ir1" id="ir1" rows="10" cols="100" style="text-align: center; width: 100%; height: 400px; display: none; border: none;" ></textarea>
												<script type="text/javascript">
												var oEditors = [];
									
												// 추가 글꼴 목록
												//var aAdditionalFontSet = [["MS UI Gothic", "MS UI Gothic"], ["Comic Sans MS", "Comic Sans MS"],["TEST","TEST"]];
									
												nhn.husky.EZCreator.createInIFrame({
													oAppRef: oEditors,
													elPlaceHolder: "ir1",
													sSkinURI: "<c:url value='/smarteditor/SmartEditor2Skin.html'/>",	
													htParams : {
														bUseToolbar : true,				// 툴바 사용 여부 (true:사용/ false:사용하지 않음)
														bUseVerticalResizer : true,		// 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
														bUseModeChanger : true,			// 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
														//aAdditionalFontList : aAdditionalFontSet,		// 추가 글꼴 목록
														fOnBeforeUnload : function(){
														//alert("완료!");
													}
												}, //boolean
												fOnAppLoad : function(){
													//예제 코드
													//oEditors.getById["ir1"].exec("PASTE_HTML", ["로딩이 완료된 후에 본문에 삽입되는 text입니다."]);
													//body_loaded();
												},
												fCreator: "createSEditor2"
												});
									
												function pasteHTML(obj) {
													var sHTML = "<img src='<c:url value='/img/upload'/>/"+obj+"'>";
													oEditors.getById["ir1"].exec("PASTE_HTML", [sHTML]);
												}
									
												function showHTML() {
													var sHTML = oEditors.getById["ir1"].getIR();
													alert(sHTML);
												}
									
												function submitContents(elClickedObj) {
													oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);	// 에디터의 내용이 textarea에 적용됩니다.
									
													// 에디터의 내용에 대한 값 검증은 이곳에서 document.getElementById("ir1").value를 이용해서 처리하면 됩니다.
									
													try {
														elClickedObj.form.submit();
													} catch(e) {}
													}
									
												function setDefaultFont() {
													var sDefaultFont = '궁서';
													var nFontSize = 24;
													oEditors.getById["ir1"].setDefaultFont(sDefaultFont, nFontSize);
												}
												</script>
											</div>
											<!-- //에디터 영역 -->
										</li>
									</ul>
	
									<ul>
										<li>
											<label>등록자</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value="${tempInfo.regNm}"/></p>
											</div>
										</li>
	
										<li>
											<label>수정자</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value="${tempInfo.upNm}"/></p>
											</div>
										</li>
	
										<li>
											<label>등록일시</label>
											<div class="list-item">
												<fmt:parseDate var="regDt" value="${tempInfo.regDt}" pattern="yyyyMMddHHmmss"/>
												<fmt:formatDate var="regDt" value="${regDt}" pattern="yyyy.MM.dd HH:mm"/>
												<p class="inline-txt"><c:out value="${regDt}"/></p>
											</div>
										</li>
	
										<li>
											<label>수정일시</label>
											<div class="list-item">
												<fmt:parseDate var="upDt" value="${tempInfo.upDt}" pattern="yyyyMMddHHmmss"/>
												<fmt:formatDate var="upDt" value="${upDt}" pattern="yyyy.MM.dd HH:mm"/>
												<p class="inline-txt"><c:out value="${upDt}"/></p>
											</div>
										</li>
	
									</ul>
								</div>
							</div>
							<!-- //메일 내용 -->
	
							<!-- btn-wrap// -->
							<div class="btn-wrap">
								<button type="button" class="btn big fullpurple" onclick="goUpdate();">수정</button>
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
	
	<!-- HTML등록 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_upload_html_rns.jsp" %>
	<!-- //HTML등록 팝업 -->
	
	<!-- 템플릿미리보기팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_tmp_rns.jsp" %>
	<!-- //템플릿미리보기팝업 -->
	
</body>
</html>
