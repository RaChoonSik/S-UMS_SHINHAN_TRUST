<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.18
	*	설명 : 사용자 정보수정
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp"%>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld"%>

<script type="text/javascript"
	src="<c:url value='/js/sys/acc/userUpdate.js'/>"></script>

<body>
	<div id="wrap" class="sys">

		<!-- lnb// -->
		<div id="lnb">
			<!-- LEFT MENU -->
			<%@ include file="/WEB-INF/jsp/inc/menu_sys.jsp"%>
			<!-- LEFT MENU -->
		</div>
		<!-- //lnb -->

		<!-- content// -->
		<div id="content">

			<!-- cont-head// -->
			<section class="cont-head">
				<div class="title">
					<h2>사용자 정보수정</h2>
				</div>

				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp"%>
				<!-- //공통 표시부 -->

			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
				<div class="enrolluser-wrap">
					<form id="userInfoForm" name="userInfoForm" method="post">
						<input type="hidden" name="page" value="<c:out value='${searchVO.page}'/>">
						<input type="hidden" name="userId" value="<c:out value='${userInfo.userId}'/>"> 
						<input type="hidden" name="serviceGb" value="">
						<fieldset>
							<legend>내용</legend>

							<!-- 조건// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">내용</h3>
									<span class="required"> *필수입력 항목 </span>
								</div>

								<div class="list-area">
									<ul>
										<li><label class="required">사용자ID</label>
											<div class="list-item">
												<p class="inline-txt">
													<c:out value='${userInfo.userId}' />
												</p>
												<button type="button" class="btn fullred" onclick="resetPassword()" style="margin-right: 20px;">비밀번호 초기화</button>
											</div>
										</li>
										<li><label class="required">사용자명</label>
											<div class="list-item">
												<input type="text" id="userNm" name="userNm" value="<c:out value='${userInfo.userNm}'/>">
											</div>
										</li>
										<li><label class="required">이메일</label>
											<div class="list-item">
												<input type="text" id="userEm" name="userEm" value="<crypto:decrypt colNm='USER_EM' data='${userInfo.userEm}'/>">
											</div>
										</li>
										<li><label class="required">연락처</label>
											<div class="list-item">
												<input type="text" id="userTel" name="userTel" value="<crypto:decrypt colNm='USER_TEL' data='${userInfo.userTel}'/>">
											</div>
										</li>
										<li><label class="required">발송자명</label>
											<div class="list-item">
												<input type="text" id="mailFromNm" name="mailFromNm" value="<c:out value='${userInfo.mailFromNm}'/>">
											</div>
										</li>
										<li><label class="required">발송자이메일</label>
											<div class="list-item">
												<input type="text" id="mailFromEm" name="mailFromEm" value="<crypto:decrypt colNm='MAIL_FROM_EM' data='${userInfo.mailFromEm}'/>">
											</div>
										</li>
										<li><label class="required">회신이메일</label>
											<div class="list-item">
												<input type="text" id="replyToEm" name="replyToEm" value="<crypto:decrypt colNm='REPLY_TO_EM' data='${userInfo.replyToEm}'/>">
											</div>
										</li>
										<li><label class="required">RETURN<br>이메일</label>
											<div class="list-item">
												<input type="text" id="returnEm" name="returnEm" value="<crypto:decrypt colNm='RETURN_EM' data='${userInfo.returnEm}'/>">
											</div>
										</li>
										<li><label class="required">메일문자셋</label>
											<div class="list-item">
												<div class="select">
													<select id="charset" name="charset" title="옵션 선택">
														<option value="0">선택</option>
														<c:if test="${fn:length(charsetList) > 0}">
															<c:forEach items="${charsetList}" var="charset">
																<option value="<c:out value='${charset.cd}'/>"
																	<c:if test="${charset.cd eq userInfo.charset}"> selected</c:if>><c:out
																		value='${charset.cdNm}' /></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
										<li><label class="required">타임존</label>
											<div class="list-item">
												<div class="select">
													<select id="tzCd" name="tzCd" title="옵션 선택">
														<option value="0">선택</option>
														<c:if test="${fn:length(tzCdList) > 0}">
															<c:forEach items="${tzCdList}" var="tzCd">
																<option value="<c:out value='${tzCd.cd}'/>"
																	<c:if test="${tzCd.cd eq userInfo.tzCd}"> selected</c:if>><c:out
																		value='${tzCd.cdNm}' /></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
										<li><label class="required">UI 언어권</label>
											<div class="list-item">
												<div class="select">
													<select id="uilang" name="uilang" title="옵션 선택">
														<option value="0">선택</option>
														<c:if test="${fn:length(uilangList) > 0}">
															<c:forEach items="${uilangList}" var="uilang">
																<option value="<c:out value='${uilang.cd}'/>"
																	<c:if test="${uilang.cd eq userInfo.uilang}"> selected</c:if>><c:out
																		value='${uilang.cdNm}' /></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
										<li><label class="required">사용자 상태</label>
											<div class="list-item">
												<div class="select">
													<select id="status" name="status" title="옵션 선택">
														<c:if test="${fn:length(statusList) > 0}">
															<c:forEach items="${statusList}" var="status">
																<option value="<c:out value='${status.cd}'/>"
																	<c:if test="${status.cd eq userInfo.status}"> selected</c:if>><c:out
																		value='${status.cdNm}' /></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
										<li><label class="required">사용자그룹</label>
											<div class="list-item">
												<div class="select">
													<select id="deptNo" name="deptNo" title="옵션 선택">
														<option value="0">선택</option>
														<c:if test="${fn:length(deptList) > 0}">
															<c:forEach items="${deptList}" var="dept">
																<option value="<c:out value='${dept.deptNo}'/>"
																	<c:if test="${dept.deptNo eq userInfo.deptNo}"> selected</c:if>><c:out
																		value='${dept.deptNm}' /></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
										<li class="col-full"><label>사용자설명</label>
											<div class="list-item">
												<textarea id="userDesc" name="userDesc" placeholder="등록data"><c:out value="${userInfo.userDesc}" /></textarea>
											</div>
										</li>
										<li><label>서비스권한</label>
											<div class="list-item">
												<label>
													<input type="checkbox" name="service" value="10" <c:if test="${userInfo.useEMS eq 'Y'}"> checked</c:if>>
													<span>EMS</span>
												</label>
												<label>
													<input type="checkbox" name="service" value="20" <c:if test="${userInfo.useRNS eq 'Y'}"> checked</c:if>>
													<span>RNS</span>
												</label>
												<label>
													<input type="checkbox" name="service" value="30" <c:if test="${userInfo.useSMS eq 'Y'}"> checked</c:if>>
													<span>SMS</span>
												</label>
												<label>
													<input type="checkbox" name="service" value="40" <c:if test="${userInfo.usePUSH eq 'Y'}"> checked</c:if>>
													<span>PUSH</span>
												</label>
												<label>
													<input type="checkbox" name="service" id="chkUseSys" value="99" <c:if test="${userInfo.useSYS eq 'Y'}"> checked</c:if> onclick="setSysUse();">
													<span>공통설정</span>
												</label>
											</div>
										</li>
										<li><label class="required">부서명</label>
											<div class="list-item">
												<input type="text" id="orgKorNm" name="orgKorNm" style="width: calc(100% - 52px)" value="<c:out value='${userInfo.orgKorNm}'/>"> 
												<input type="hidden" id="orgCd" name="orgCd" value="<c:out value='${userInfo.orgCd}'/>">
												<button type="button" class="btn fullred" onclick="popOrgSearchView();">검색</button>
											</div>
										</li>
										<li><label class="required">직급</label>
											<div class="list-item">
												<div class="select">
													<select id="positionGb" name="positionGb" title="옵션 선택">
														<option value="0">선택</option>
														<c:if test="${fn:length(positionGbList) > 0}">
															<c:forEach items="${positionGbList}" var="position">
																<option value="<c:out value='${position.cd}'/>"
																	<c:if test="${position.cd eq userInfo.positionGb}"> selected</c:if>><c:out value='${position.cdNm}'/>
																</option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
										<li><label class="required">직책</label>
											<div class="list-item">
												<div class="select">
													<select id="jobGb" name="jobGb" title="옵션 선택">
														<option value="0">선택</option>
														<c:if test="${fn:length(jobGbList) > 0}">
															<c:forEach items="${jobGbList}" var="job">
																<option value="<c:out value='${job.cd}'/>"
																	<c:if test="${job.cd eq userInfo.jobGb}"> selected</c:if>><c:out value='${job.cdNm}'/>
																</option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
									</ul>
									<ul>
										<li><label>등록자</label>
											<div class="list-item">
												<p class="inline-txt">
													<c:out value="${userInfo.regNm}"/>
												</p>
											</div>
										</li>
										<li><label>수정자</label>
											<div class="list-item">
												<p class="inline-txt">
													<c:out value="${userInfo.upNm}"/>
												</p>
											</div>
										</li>
										<li><label>등록일시</label>
											<div class="list-item">
												<fmt:parseDate var="regDt" value="${userInfo.regDt}" pattern="yyyyMMddHHmmss"/>
												<fmt:formatDate var="regDt" value="${regDt}" pattern="yyyy.MM.dd HH:mm"/>
												<p class="inline-txt">
													<c:out value="${regDt}"/>
												</p>
											</div>
										</li>
										<li><label>수정일시</label>
											<div class="list-item">
												<fmt:parseDate var="upDt" value="${userInfo.upDt}" pattern="yyyyMMddHHmmss"/>
												<fmt:formatDate var="upDt" value="${upDt}" pattern="yyyy.MM.dd HH:mm"/>
												<p class="inline-txt">
													<c:out value="${upDt}"/>
												</p>
											</div>
										</li>
									</ul>
								</div>
							</div>

							<!-- 비밀번호 변경// -->
							<div class="graybox editpw">
								<div class="title-area">
									<h3 class="h3-title">비밀번호 변경</h3>
									<button type="button">
										<span class="hidden">여닫기버튼</span>
									</button>
								</div>

								<div class="list-area" id="divEditPw">
									<ul>
										<li class="col-full">
											<label>신규 비밀번호</label>
											<div class="list-item">
												<div class="pw-area fl">
													<input type="password" id="userPwd" name="userPwd" placeholder="신규 비밀번호를 입력해주세요." style="width: 280px;">
													<button type="button" class="btn-pwshow"><span class="hidden">비밀번호 보이기버튼</span></button>
												</div>
												<p class="inline-txt color-red">*영문자, 숫자, 특수문자를 포함한 8자리 이상 설정</p>
											</div>
										</li>
										<li class="col-full">
											<label>비밀번호 확인</label>
											<div class="list-item clear">
												<div class="pw-area fl">
													<input type="password" id="userPwdChk" placeholder="신규 비밀번호를 한 번 더 입력해주세요." style="width:280px;">
													<button type="button" class="btn-pwshow"><span class="hidden">비밀번호 보이기버튼</span></button>
												</div>
												<button type="button" class="btn fullred" onclick="savePassword()" style="margin-right:10px;">변경</button>
												<!-- 비밀번호 일치여부 안내문 노출 -->
												<p class="inline-txt color-red" id="passwordMessage"></p>
											</div>
										</li>
									</ul>
								</div>
							</div>
							<!-- //비밀번호 변경 -->

							<!-- 부서 조회 팝업 // -->
							<%@ include file="/WEB-INF/jsp/inc/pop/pop_org_search_view.jsp"%>

							<!-- btn-wrap// -->
							<div class="btn-wrap">
								<button type="button" class="btn big fullred" onclick="goUpdate();">수정</button>
								<button type="button" class="btn" onclick="goCancel();">취소</button>
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
