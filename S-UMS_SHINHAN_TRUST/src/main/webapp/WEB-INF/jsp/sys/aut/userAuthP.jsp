<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.30
	*	설명 : 사용자 권한 신규 등록
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/acc/userAdd.js'/>"></script>

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
					<h2>사용자 신규등록</h2>
				</div>

				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->

			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
				<div class="enrolluser-wrap">
					<form id="userAuthInfoForm" name="userInfoForm" method="post">
						<input type="hidden" name="serviceGb" value="">
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
											<label class="required">사용자ID</label>
											<div class="list-item">
												<input type="text" id="userId" name="userId" maxlength="40" placeholder="사용자ID를 검색해주세요." style="width:calc(100% - 52px)">
												<button type="button" class="btn fullred" onclick="checkUserId()">검색</button>
											</div>
										</li>
										<li>
											<label class="required">사용자명</label>
											<div class="list-item">
												<input type="text" id="userNm" name="userNm" placeholder="사용자명을 입력해주세요.">
											</div>
										</li>
										<li>
											<label class="required">비밀번호</label>
											<div class="list-item">
												<input type="text" id="userPwd" name="userPwd" placeholder="비밀번호를 입력해주세요.">
											</div>
										</li>
										<li>
											<label class="required">비밀번호확인</label>
											<div class="list-item">
												<input type="text" id="userPwdChk" name="userPwdChk" placeholder="비밀번호를 다시 입력해주세요.">
											</div>
										</li>
	
										<li>
											<label class="required">이메일</label>
											<div class="list-item">
												<input type="text" id="userEm" name="userEm" placeholder="이메일을 입력해주세요.">
											</div>
										</li>
										<li>
											<label class="required">연락처</label>
											<div class="list-item">
												<input type="text" id="userTel" name="userTel" placeholder="연락처를 입력해주세요.">
											</div>
										</li>
										<li>
											<label class="required">발송자이메일</label>
											<div class="list-item">
												<input type="text" id="mailFromEm" name="mailFromEm" placeholder="발송자이메일을 입력해주세요.">
											</div>
										</li>
										<li>
											<label class="required">회신이메일</label>
											<div class="list-item">
												<input type="text" id="replyToEm" name="replyToEm" placeholder="회신이메일을 입력해주세요.">
											</div>
										</li>
										<li>
											<label class="required">RETURN<br>이메일</label>
											<div class="list-item">
												<input type="text" id="returnEm" name="returnEm" placeholder="RETURN이메일을 입력해주세요.">
											</div>
										</li>
										<li>
											<label class="required">메일문자셋</label>
											<div class="list-item">
												<div class="select">
													<select id="charset" name="charset" title="옵션 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(charsetList) > 0}">
														<c:forEach items="${charsetList}" var="charset">
															<option value="<c:out value='${charset.cd}'/>"><c:out value='${charset.cdNm}'/></option>
														</c:forEach>
													</c:if>
													</select>
												</div>
											</div>
										</li>
										<li>
											<label class="required">타임존</label>
											<div class="list-item">
												<div class="select">
													<select id="tzCd" name="tzCd" title="옵션 선택">
														<option value="">선택</option>
														<c:if test="${fn:length(tzCdList) > 0}">
															<c:forEach items="${tzCdList}" var="tzCd">
																<option value="<c:out value='${tzCd.cd}'/>"><c:out value='${tzCd.cdNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
										<li>
											<label class="required">UI 언어권</label>
											<div class="list-item">
												<div class="select">
													<select  id="uilang" name="uilang" title="옵션 선택">
														<option>선택</option>
														<c:if test="${fn:length(uilangList) > 0}">
															<c:forEach items="${uilangList}" var="uilang">
																<option value="<c:out value='${uilang.cd}'/>"><c:out value='${uilang.cdNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
										<li>
											<label class="required">사용자 상태</label>
											<div class="list-item">
												<div class="select">
													<select  id="status" name="status" title="옵션 선택">
														<option>선택</option>
														<c:if test="${fn:length(statusList) > 0}">
															<c:forEach items="${statusList}" var="status">
																<option value="<c:out value='${status.cd}'/>"><c:out value='${status.cdNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
										<li>
											<label class="required">사용자그룹</label>
											<div class="list-item">
												<div class="select">
													<select  id="deptNo" name="deptNo" title="옵션 선택">
														<option>선택</option>
														<c:if test="${fn:length(deptList) > 0}">
															<c:forEach items="${deptList}" var="dept">
																<option value="<c:out value='${dept.deptNo}'/>"><c:out value='${dept.deptNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
										<li class="col-full">
											<label>사용자설명</label>
											<div class="list-item">
												<textarea id="userDesc" name="userDesc" placeholder="사용자설명을 입력해주세요"></textarea>
											</div>
										</li>
										<li class="col-full">
											<label>발신자명</label>
											<div class="list-item">
												<textarea id="mailFromNm" name="mailFromNm" placeholder="발신자명 입력해주세요"></textarea>
											</div>
										</li>
										<li>
											<label>서비스권한</label>
											<div class="list-item">
												<label><input type="checkbox" name="service" value="10"><span>EMS</span></label>
												<label><input type="checkbox" name="service" value="20"><span>RNS</span></label>
												<label><input type="checkbox" name="service" value="30"><span>SMS</span></label>
												<label><input type="checkbox" name="service" value="40"><span>PUSH</span></label>
												<label><input type="checkbox" name="service" value="99"><span>공통설정</span></label>
											</div>
										</li>
										<li>
											<label class="required">부서명</label>
											<div class="list-item">
												<input type="text" id="orgKorNm" name="orgKorNm" placeholder="선택" style="width:calc(100% - 52px)">
												<input type="hidden" id="orgCd" name="orgCd" value="">
												<button type="button" class="btn fullred" onclick="popOrgSearchView();">검색</button>
											</div>
										</li>
										<li>
											<label class="required">직급</label>
											<div class="list-item">
												<div class="select">
													<select id="positionGb" name="positionGb" title="옵션 선택">
														<option>선택</option>
														<c:if test="${fn:length(positionGbList) > 0}">
															<c:forEach items="${positionGbList}" var="position">
																<option value="<c:out value='${position.cd}'/>"><c:out value='${position.cdNm}'/></option>
															</c:forEach>
															
														</c:if>
													</select>
												</div>
											</div>
										</li>
										<li>
											<label class="required">직책</label>
											<div class="list-item">
												<div class="select">
													<select id="jobGb" name="jobGb" title="옵션 선택">
														<option>선택</option>
														<c:if test="${fn:length(jobGbList) > 0}">
															<c:forEach items="${jobGbList}" var="job">
																<option value="<c:out value='${job.cd}'/>"><c:out value='${job.cdNm}'/></option>
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
							<!-- 부서검색// -->
							<%@ include file="/WEB-INF/jsp/inc/pop/pop_org_search_view.jsp" %>
							
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
