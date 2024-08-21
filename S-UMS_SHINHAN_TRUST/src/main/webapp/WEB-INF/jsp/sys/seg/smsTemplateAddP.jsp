<%--
	/**********************************************************
	*	작성자 : 이혜민
	*	작성일시 : 2022.06.22
	*	설명 : SMS 템플릿 신규 등록 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/seg/smsTemplateAddP.js'/>"></script>

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
					<h2>API 템플릿 신규등록</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
				<div class="manage-wrap">
					<form id="searchForm" name="smsTemplateInfoForm" method="post">
						<input type="hidden" name="page"          value="<c:out value='${searchVO.page}'/>">
						<input type="hidden" name="searchTempCd"  value="<c:out value='${searchVO.searchTempCd}'/>">
						<input type="hidden" name="searchTempNm"  value="<c:out value='${searchVO.searchTempNm}'/>">
						<input type="hidden" name="searchDeptNo"  value="<c:out value='${searchVO.searchDeptNo}'/>">
						<input type="hidden" name="searchUserId"  value="<c:out value='${searchVO.searchUserId}'/>">
						<input type="hidden" name="searchStatus"  value="<c:out value='${searchVO.searchStatus}'/>">
						<input type="hidden" name="searchStartDt" value="<c:out value='${searchVO.searchStartDt}'/>">
						<input type="hidden" name="searchEndDt"   value="<c:out value='${searchVO.searchEndDt}'/>">
					</form>
					<form id="smsTemplateInfoForm" name="smsTemplateInfoForm" method="post">
						<input type="hidden" name="mergeItem" value=""/>
						<input type="hidden" id="segNoc"	name="segNoc"    value=""> 
						<input type="hidden" id="campNo"	name="campNo"    value="999999"> 
						<input type="hidden" id="gubun"		name="gubun"     value=""> 

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
										<%-- 관리자의 경우 전체 요청부서를 전시하고 그 외의 경우에는 숨김 --%>
										<li>
											<label class="required">사용자그룹</label>
											<div class="list-item">
												<div class="select">
													<select id="deptNo" name="deptNo" onchange="getUserList(this.value);" title="사용자그룹 선택">
														<option value="0">선택</option>
														<c:if test="${fn:length(deptList) > 0}">
															<c:forEach items="${deptList}" var="dept">
																<option value="<c:out value='${dept.deptNo}'/>"<c:if test="${dept.deptNo eq NEO_DEPT_NO}"> selected</c:if>><c:out value='${dept.deptNm}'/></option>
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
													<select id="userId" name="userId" title="사용자명 선택">
														<option value="">선택</option>
														<c:if test="${fn:length(userList) > 0}">
															<c:forEach items="${userList}" var="user">
																<option value="<c:out value='${user.userId}'/>"<c:if test="${user.userId eq NEO_USER_ID}"> selected</c:if>><c:out value='${user.userNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
										<li>
											<label class="required">수신자그룹</label>
											<div class="list-item">
												<div class="filebox">
													<p class="inline-txt" style="width:calc(100% - 130px);" id="txtSegNm">선택된 수신자그룹이 없습니다.</p>
													<button type="button" class="btn fullred" onclick="popSegSelect()">선택</button>
													<button type="button" class="btn fullred" onclick="goSegInfoMail('');">미리보기</button>
												</div>
											</div>
										</li>
										<!--  API전용이므로 미사용처리 -->
										<!-- 
										<li>
											<label class="required">캠페인명</label>
											<div class="list-item">
												<p class="inline-txt" style="width:calc(100% - 63px);" id="txtCampNm">선택된 캠페인이 없습니다.</p>
												<button type="button" class="btn fullred" onclick="popCampSelect();">선택</button>
											</div>
										</li>
										 -->
									</ul>
								</div>
							</div>
							<!-- 템플릿 내용// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">템플릿 내용</h3>
									<!-- 버튼// -->
									<div class="btn-wrap">
										<button type="button" class="btn" onclick="goSmsTemplatePreview();">미리보기</button>
									</div>
									<!-- //버튼 -->
								</div>
								<div class="list-area">
									<ul>
										<li>
											<label class="required">템플릿 코드</label>
											<div class="list-item">
												<input type="text" id="tempCd" name="tempCd" placeholder="템플릿 코드를 입력해주세요." style="width:calc(100% - 69px)"> 
												<button type="button" class="btn fullred" id="checkTempCd" onclick="checkSmsTempCd()" value="N">중복확인</button>
											</div>
										</li>
										<li>
											<label class="required">템플릿 명</label>
											<div class="list-item">
												<input type="text" id="tempNm" name="tempNm" placeholder="템플릿 입력해주세요." maxlength="128">
											</div>
										</li>
										<li class="col-full">
											<label>설명</label>
											<div class="list-item">
												<textarea id="tempDesc" name="tempDesc" placeholder="템플릿 설명을 입력해주세요"></textarea>
											</div>
										</li>
									</ul>
									<ul>
										<li>
											<label class="required">제목</label>
											<div class="list-item">
												<input type="text" id="tempSubject" name="tempSubject"  placeholder="제목을 입력하세요.">
											</div>
										</li>
										<li>
											<label>머지 항목</label>
											<div class="list-item">
											<div class="select" style="width:calc(100% - 96px);"><!-- 3월 28일 수정 -->s
												<select id="mergeKeySms" name="mergeKeySms" title="머지 항목 선택">
												</select>
											</div>
											<button type="button" class="btn fullred" onclick="goTitleMerge();">제목</button>
											<button type="button" class="btn fullred" onclick="goContMerge();">본문</button>
										</div>
										</li> 
										<li class="col-full">
											<label class="required">템플릿 내용</label>
											<div class="list-item">
												<textarea id="tempContent"  name="tempContent" placeholder="API 템플릿 내용을 입력해주세요. SMS는 80byte (한글 40자), LMS는 2000byte (한글 1,000자)" onkeyup="byteCheck(this, 1000)" style="height:250px;"></textarea>
												<b class="inline-txt" style="margin-top: 15px;"> &#8251; SMS의 경우는 머지항목의 내용에 따라 문자 길이가 변동되어 LMS 처리 될 수 있습니다 </b>
											</div>
										</li>
										<li class="col-full">
											<label>머지 항목</label>
											<div class="list-item">
												<p class ="inline-txt" id="mergeItem"></p>
											</div>
										</li>
									</ul>
								</div>
							</div>
							<!-- //템플릿 내용 -->
	
							<!-- btn-wrap// -->
							<div class="btn-wrap">
								<button type="button" class="btn big fullred" onclick="goAdd();">등록</button>
								<button type="button" class="btn big" onclick="goCancel();">목록</button>
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
	
	<!-- 수신자그룹선택 팝업// -->
	<%@ include file="/WEB-INF/jsp/sys/seg/pop/pop_segment.jsp"%>
	<!-- //수신자그룹선택 팝업 -->
	
	<!-- 수신자그룹미리보기팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_seg_mail_sys.jsp" %>
	<!-- //수신자그룹미리보기팝업 -->
	
	<!-- 조회사유팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_search_reason_sys.jsp" %>
	<!-- //조회사유팝업 -->
	
	<!-- 캠페인선택 팝업// -->
	<%@ include file="/WEB-INF/jsp/sys/seg/pop/pop_campaign.jsp" %>
	<!-- //캠페인선택 팝업 -->
	
	<!-- SMS 템플릿 미리보기 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_sms_template.jsp" %>
	<!-- SMS 템플릿 미리보기 팝업// -->

</body>
</html>
