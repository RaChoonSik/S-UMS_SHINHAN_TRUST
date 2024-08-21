<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.19
	*	설명 : 메일검색 팝업화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_mailsearch" class="poplayer popmailsearch">
	<div class="inner">
		<header>
			<h2>메일명 검색</h2>
		</header>
		<form id="searchForm" name="searchForm" method="post">
		<input type="hidden" name="page" value="1">
		<div class="popcont">
			<div class="cont">
				<!-- 조회// -->
				<div class="graybox">
					<div class="title-area">
						<h3 class="h3-title">조회</h3>
					</div>
					
					<div class="list-area">
						<ul>
							<li>
								<label>등록일시</label>
								<div class="list-item">
									<!-- datepickerrange// -->
									<div class="datepickerrange fromDate">
										<label>
											<input type="text" name="searchStartDt" readonly>
										</label>
									</div>
									<span class="hyppen date"></span>
									<div class="datepickerrange toDate">
										<label>
											<input type="text" name="searchEndDt" readonly>
										</label>
									</div>
									<!-- //datepickerrange -->
								</div>
							</li>
							<li>
								<label>메일명</label>
								<div class="list-item">
									<input type="text" name="taskNm" placeholder="메일명을 입력해주세요.">
								</div>
							</li>
							<li>
								<label>캠페인명</label>
								<div class="list-item">
									<div class="select">
										<select name="campNo" title="캠페인 선택">
											<option value="0">선택</option>
											<c:if test="${fn:length(campaignList) > 0}">
												<c:forEach items="${campaignList}" var="campaign">
													<option value="<c:out value='${campaign.campNo}'/>"><c:out value='${campaign.campNm}'/></option>
												</c:forEach>
											</c:if>
										</select>
									</div>
								</div>
							</li>
							<li>
								<label>사용자그룹</label>
								<div class="list-item">
									<div class="select">
										<c:if test="${'Y' eq NEO_ADMIN_YN}">
											<select name="searchDeptNo" onchange="getUserListMail(this.value);" title="사용자그룹 선택">
												<option value="0">선택</option>
												<c:if test="${fn:length(deptList) > 0}">
													<c:forEach items="${deptList}" var="dept">
														<option value="<c:out value='${dept.deptNo}'/>"><c:out value='${dept.deptNm}'/></option>
													</c:forEach>
												</c:if>
											</select>
										</c:if>
										<c:if test="${'N' eq NEO_ADMIN_YN}">
											<select id="searchDeptNo" name="searchDeptNo" title="사용자그룹 선택">
												<c:if test="${fn:length(deptList) > 0}">
													<c:forEach items="${deptList}" var="dept">
														<c:if test="${dept.deptNo == NEO_DEPT_NO}">
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
								<label>사용자명</label>
								<div class="list-item">
									<div class="select">
										<select id="searchUserId" name="searchUserId" title="사용자 선택">
											<option value="">선택</option>
											<c:if test="${fn:length(userList) > 0}">
												<c:forEach items="${userList}" var="user">
													<option value="<c:out value='${user.userId}'/>"><c:out value='${user.userNm}'/></option>
												</c:forEach>
											</c:if>
										</select>
									</div>
								</div>
							</li>
							<li>
								<label>단기/정기</label>
								<div class="list-item">
									<div class="select">
										<select id="searchSendRepeat" name="searchSendRepeat" title="단기/정기 선택">
											<option value="">선택</option>
											<option value="000">단기</option>
											<option value="001">정기</option>
										</select>
									</div>
								</div>
							</li>
						</ul>
					</div>
				</div>
				<!-- //조회 -->

				<!-- 버튼// -->
				<div class="btn-wrap tar" style="margin-top:10px;">
					<button type="button" class="btn fullblue" onclick="goPageNumMail('1');">검색</button>
					<button type="button" class="btn" onclick="goResetMail();">초기화</button>
				</div>
				<!-- //버튼 -->

				<!-- 목록&페이징// -->
				<div id="divMailList" style="margin-top:30px;"></div>
				<!-- //목록&페이징 -->

			</div>
		</div>
		</form>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_mailsearch');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
