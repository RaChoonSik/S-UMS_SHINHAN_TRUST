<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.20
	*	설명 : 수신자그룹 수정(파일연동) 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp" %>

<script type="text/javascript" src="<c:url value='/js/ems/seg/segFileUpdateP.js'/>"></script>

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
					<h2>수신자그룹 정보수정(파일연동)</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">

				<form id="searchForm" name="searchForm" method="post">
					<input type="hidden" name="page" value="<c:out value='${searchVO.page}'/>">
					<input type="hidden" name="searchSegNm" value="<c:out value='${searchVO.searchSegNm}'/>">
					<input type="hidden" name="searchDeptNo" value="<c:out value='${searchVO.searchDeptNo}'/>">
					<input type="hidden" name="searchUserId" value="<c:out value='${searchVO.searchUserId}'/>">
					<input type="hidden" name="searchCreateTy" value="<c:out value='${searchVO.searchCreateTy}'/>">
					<input type="hidden" name="searchStatus" value="<c:out value='${searchVO.searchStatus}'/>">
					<input type="hidden" name="searchStartDt" value="<c:out value='${searchVO.searchStartDt}'/>">
					<input type="hidden" name="searchEndDt" value="<c:out value='${searchVO.searchEndDt}'/>">
					<input type="hidden" name="searchEmsuseYn" value="<c:out value='${searchVO.searchEmsuseYn}'/>">
					<input type="hidden" name="searchSmsuseYn" value="<c:out value='${searchVO.searchSmsuseYn}'/>">
					<input type="hidden" name="searchPushuseYn" value="<c:out value='${searchVO.searchPushuseYn}'/>">
				</form>
				<form id="segInfoForm" name="segInfoForm" method="post">
					<input type="hidden" id="page" name="page" value="1">
					<input type="hidden" id="segNo" name="segNo" value="<c:out value='${segmentInfo.segNo}'/>">
					<input type="hidden" id="status" name="status">
					<input type="hidden" id="dataInfo" name="dataInfo">
					<input type="hidden" id="targetService" name="targetService">
					<input type="hidden" id="downType" name="downType" value="002">
					<input type="hidden" id="segFlPath" name="segFlPath" value="<c:out value='${segmentInfo.segFlPath}'/>">
					<input type="hidden" id="mergeKey" name="mergeKey" value="<c:out value='${segmentInfo.mergeKey}'/>">
					<input type="hidden" id="mergeCol" name="mergeCol" value="<c:out value='${segmentInfo.mergeCol}'/>">
					<input type="hidden" id="createTy" name="createTy" value="<c:out value='${segmentInfo.createTy}'/>">
					<input type="hidden" id="changedFile" value="N">
					<fieldset>
						<legend>조건</legend>

						<!-- 조건// -->
						<div class="graybox">
							<div class="title-area">
								<h3 class="h3-title">조건</h3>

								<!-- 버튼// -->
								<div class="btn-wrap">
									<span class="required">*필수입력 항목</span>
									<c:if test="${'002' ne segmentInfo.status}">
										<button type="button" id="btnDisable" class="btn<c:if test="${'001' eq segmentInfo.status}"> hide</c:if>" onclick="goDisable();">사용중지</button>
										<button type="button" id="btnEnable" class="btn<c:if test="${'000' eq segmentInfo.status}"> hide</c:if>" onclick="goEnable();">복구</button>
										<button type="button" class="btn" onclick="goDelete();">삭제</button>
									</c:if>
								</div>
								<!-- //버튼 -->
							</div>
							
							<div class="list-area">
								<ul>
									<li>
										<label class="required">파일등록</label>
										<div class="list-item">
											<input type="text" id="tempFlPath" name="tempFlPath" style="width:calc(100% - 128px);" value="<c:out value='${fn:substring(segmentInfo.segFlPath,fn:indexOf(segmentInfo.segFlPath,"-")+1,fn:length(segmentInfo.segFlPath))}'/>">
											<button type="button" class="btn fullblue" onclick="fn.popupOpen('#popup_file_seg');">등록</button>
											<button type="button" class="btn" onclick="goDownload();">다운로드</button>
										</div>
									</li>
									<li>
										<label class="required">구분자</label>
										<div class="list-item">
											<input type="text" id="separatorChar" name="separatorChar" placeholder="구분자를 입력해주세요." style="width:calc(100% - 52px);" value="<c:out value='${segmentInfo.separatorChar}'/>">
											<button type="button" class="btn fullblue" onclick="fncSep('<c:out value='${NEO_USER_ID}'/>');">등록</button>
										</div>
									</li>
									<%-- 관리자의 경우 전체 요청부서를 전시하고 그 외의 경우에는 숨김 --%>
									<c:if test="${'Y' eq NEO_ADMIN_YN}">
										<li>
											<label class="required">사용자그룹</label>
											<div class="list-item">
												<div class="select">
													<select id="deptNo" name="deptNo" onchange="getUserList(this.value);" title="사용자그룹 선택">
														<option value="0">선택</option>
														<c:if test="${fn:length(deptList) > 0}">
															<c:forEach items="${deptList}" var="dept">
																<option value="<c:out value='${dept.deptNo}'/>"<c:if test="${dept.deptNo == segmentInfo.deptNo}"> selected</c:if>><c:out value='${dept.deptNm}'/></option>
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
																<option value="<c:out value='${user.userId}'/>"<c:if test="${user.userId == segmentInfo.userId}"> selected</c:if>><c:out value='${user.userNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
									</c:if>
									<li class="col-full">
										<label class="required">서비스유형</label>
										<div class="list-item">
											<label><input type="checkbox" id="emsuseYn" name="emsuseYn" value="Y"<c:if test="${'Y' eq segmentInfo.emsuseYn}"> checked</c:if>><span>EMS</span></label>
											<label><input type="checkbox" id="smsuseYn" name="smsuseYn" value="Y"<c:if test="${'Y' eq segmentInfo.smsuseYn}"> checked</c:if>><span>SMS</span></label>
											<label><input type="checkbox" id="pushuseYn" name="pushuseYn" value="Y"<c:if test="${'Y' eq segmentInfo.pushuseYn}"> checked</c:if>><span>PUSH</span></label>
										</div>
									</li>
									<li class="col-full">
										<label class="required">수신자그룹</label>
										<div class="list-item">
											<input type="text" id="segNm" name="segNm" value="<c:out value='${segmentInfo.segNm}'/>" placeholder="수신자그룹 명칭을 입력해주세요.">
										</div>
									</li>
									<li class="col-full">
										<label>설명</label>
										<div class="list-item">
											<textarea id="segDesc" name="segDesc" placeholder="수신자그룹 설명을 입력해주세요."><c:out value='${segmentInfo.segDesc}'/></textarea>
										</div>
									</li>
									<li class="col-full">
										<label>수신그룹<br>미리보기</label>
										<div class="list-item">
											<p class="inline-txt color-gray" id="txtTotCnt"><c:out value='${segmentInfo.totCnt}'/>명</p>
											<input type="hidden" id="totCnt" name="totCnt" value="<c:out value='${segmentInfo.totCnt}'/>"/>
											<button type="button" class="btn fullblue" onclick="goSegInfo('<c:out value='${NEO_USER_ID}'/>');">미리보기</button>
										</div>
									</li>
								</ul>

								<ul>
									<li>
										<label>등록자</label>
										<div class="list-item">
											<p class="inline-txt"><c:out value="${segmentInfo.regNm}"/></p>
										</div>
									</li>
									<li>
										<label>수정자</label>
										<div class="list-item">
											<p class="inline-txt"><c:out value="${segmentInfo.upNm}"/></p>
										</div>
									</li>
									<li>
										<label>등록일시</label>
										<div class="list-item">
											<fmt:parseDate var="regDt" value="${segmentInfo.regDt}" pattern="yyyyMMddHHmmss"/>
											<fmt:formatDate var="regDt" value="${regDt}" pattern="yyyy.MM.dd HH:mm"/>
											<p class="inline-txt"><c:out value="${regDt}"/></p>
										</div>
									</li>
									<li>
										<label>수정일시</label>
										<div class="list-item">
											<fmt:parseDate var="upDt" value="${segmentInfo.upDt}" pattern="yyyyMMddHHmmss"/>
											<fmt:formatDate var="upDt" value="${upDt}" pattern="yyyy.MM.dd HH:mm"/>
											<p class="inline-txt"><c:out value="${upDt}"/></p>
										</div>
									</li>
								</ul>
							</div>
						</div>
						<!-- //조건 -->

						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullblue" onclick="goSegFileUpdate('<c:out value='${segmentInfo.status}'/>');">수정</button>
							<button type="button" class="btn big" onclick="goSegList();">취소</button>
						</div>
						<!-- //btn-wrap -->
						
					</fieldset>
				</form>
				
			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>

	<!-- 파일등록 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_upload_seg.jsp" %>
	<!-- //파일등록 팝업 -->
	
	<!-- 미리보기 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_seg.jsp" %>
	<!-- //미리보기 팝업 -->
	
	<!-- 조회사유팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_search_reason.jsp" %>
	<!-- //조회사유팝업 -->
	
	<iframe id="iFrmDown" name="iFrmDown" style="width:0px;height:0px;"></iframe>
</body>
</html>
