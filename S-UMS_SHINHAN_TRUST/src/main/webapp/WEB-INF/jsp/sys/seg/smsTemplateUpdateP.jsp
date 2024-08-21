<%--
	/**********************************************************
	*	작성자 : 이혜민
	*	작성일시 : 2022.06.22
	*	설명 : API 템플릿 수정 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/seg/smsTemplateUpdateP.js'/>"></script>
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
					<h2>API 템플릿 정보수정</h2>
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
						<input type="hidden" name="tempCd" value="<c:out value='${searchVO.tempCd}'/>">
						<input type="hidden" id="status"	name="status"     value="<c:out value='${smsTemplateInfo.status}'/>"/>
					</form>
					
					<form id="smsTemplateInfoForm" name="smsTemplateInfoForm" method="post">
						<input type="hidden" 						name="mergeItem"    value=""/>
						<input type="hidden" id="campNo"    		name="campNo"       value="<c:out value='${smsTemplateInfo.campNo}'/>"/> 
						<input type="hidden" id="gubun"				name="gubun"        value="<c:out value='${smsTemplateInfo.gubun}'/>"/>
						<input type="hidden" id="mappKakaoTempCnt"	                    value="<c:out value='${smsTemplateInfo.mappKakaoTempCnt}'/>"/>
						<input type="hidden" id="oldSegNo"				                value="<c:out value='${smsTemplateInfo.segNo}'/>"/>
						<input type="hidden" id="curSegNo"				                value="<c:out value='${smsTemplateInfo.segNo}'/>"/>
						<input type="hidden" id="mappInfoInit"		name="mappInfoInit" value="0"/>

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
																<option value="<c:out value='${dept.deptNo}'/>"<c:if test="${dept.deptNo == smsTemplateInfo.deptNo}"> selected</c:if>><c:out value='${dept.deptNm}'/></option>
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
																<option value="<c:out value='${user.userId}'/>"<c:if test="${user.userId == smsTemplateInfo.userId}"> selected</c:if>><c:out value='${user.userNm}'/></option>
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
													<c:set var="segNm" value=""/>
													<c:set var="segNoc" value=""/>
													<c:forEach items="${segList}" var="seg">
														<c:if test="${seg.segNo == smsTemplateInfo.segNo}">
															<c:set var="segNm" value="${seg.segNm}"/>
															<c:set var="segNoc" value="${seg.segNo}|${seg.mergeKey}"/>
														</c:if>
													</c:forEach>
													<p class="label" style="width:calc(100% - 120px);" id="txtSegNm"><c:out value="${segNm}"/></p>
													<input type="hidden" id="segNoc" name="segNoc" value="<c:out value="${segNoc}"/>">
													<button type="button" class="btn fullred" onclick="popSegSelect();">선택</button>
													<button type="button" class="btn fullred" onclick="goSegInfoMail('');">미리보기</button>
												</div>
											</div>
										</li>
										<!--  API전용이므로 미사용처리 -->
										<!--
										<li>
											<label class="required">캠페인명</label>
											<div class="list-item">
												<p class="inline-txt" style="width:calc(100% - 63px);" id="txtCampNm"><c:out value='${smsTemplateInfo.campNm}'/></p>
												<button type="button" class="btn fullred" onclick="popCampSelect();">선택</button>
											</div>
										</li>
										-->
									</ul>
								</div>
							</div>
							<!-- //조건 --> 
							
							<!-- 템플릿 내용// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">템플릿 내용</h3>
									
									<!-- 버튼// -->
									<div class="btn-wrap">
										<c:choose>
											<c:when test="${'002' eq smsTemplateInfo.status}">
												<button type="button" id="btnDisable" class="btn hide" onclick="goDisable();">사용중지</button>
												<button type="button" id="btnEnable" class="btn hide" onclick="goEnable();">복구</button>
												<button type="button" id="btnDelete" class="btn hide" onclick="goDelete();">삭제</button>
											</c:when>
											<c:otherwise>
												<button type="button" id="btnDisable" class="btn<c:if test="${'001' eq smsTemplateInfo.status}"> hide</c:if>" onclick="goDisable();">사용중지</button>
												<button type="button" id="btnEnable" class="btn<c:if test="${'000' eq smsTemplateInfo.status}"> hide</c:if>" onclick="goEnable();">복구</button>
												<button type="button" id="btnDelete" class="btn" onclick="goDelete();">삭제</button>
											</c:otherwise>
										</c:choose>
										<button type="button" class="btn" onclick="goSmsTemplatePreview();">미리보기</button>
									</div>
									<!-- //버튼 -->
								</div>
								
								<div class="list-area">
									<ul>
										<li>
											<label class="required">템플릿 코드</label>
											<div class="list-item">
												<input type="text" id="tempCd" name="tempCd" value="<c:out value='${smsTemplateInfo.tempCd}'/>" placeholder="템플릿 코드를 입력해주세요." disabled="disabled">
											</div>
										</li>
										<li>
											<label class="required">템플릿명</label>
											<div class="list-item">
												<input type="text" id="tempNm" name="tempNm" value="<c:out value='${smsTemplateInfo.tempNm}'/>" placeholder="템플릿명을 입력해주세요." maxlength="128">
											</div>
										</li>
										<li class="col-full">
											<label>설명</label>
											<div class="list-item">
												<textarea id="tempDesc" name="tempDesc" placeholder="템플릿 설명을 입력해주세요"><c:out value='${smsTemplateInfo.tempDesc}'/></textarea>
											</div>
										</li>
									</ul>
									<ul>
										<li>
											<label class="required">제목</label>
											<div class="list-item">
												<input type="text" id="tempSubject" name="tempSubject"  placeholder="제목을 입력하세요." value="<c:out value='${smsTemplateInfo.tempSubject}'/>">
											</div>
										</li>
										<li>
											<label>머지 항목</label>
											<div class="list-item">
											<div class="select" style="width:calc(100% - 96px);"><!-- 3월 28일 수정 -->
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
												<textarea id="tempContent"  name="tempContent" placeholder="API 템플릿 내용을 입력해주세요. SMS는 80byte (한글 40자), LMS는 2000byte (한글 1,000자)" onkeyup="byteCheck(this, 1000)" style="height:250px;"><c:out value='${smsTemplateInfo.tempContent}'/></textarea>
												<b class="inline-txt" style="margin-top: 15px;"> &#8251; SMS의 경우는 머지항목의 내용에 따라 문자 길이가 변동되어 LMS 처리 될 수 있습니다 </b>
											</div>
										</li>
										<li class="col-full">
											<label>머지 항목</label>
											<div class="list-item">
												<p class ="inline-txt" id="mergeItem"><c:out value="${smsTemplateInfo.mergeItem }"/></p>
											</div>
										</li>
										<li class="col-full">
											<label>알림톡 연동</label>
											<div class="list-item">
												<c:if test="${smsTemplateInfo.mappKakaoTempCnt eq 1 }">
													<p class ="inline-txt" style="color:red;font-weight:bold;" id="mappKakaoTemp"><c:out value="${smsTemplateInfo.mappKakaoTempNm }"/> ( 1건 )</p>
												</c:if>
												<c:if test="${smsTemplateInfo.mappKakaoTempCnt > 1}">
													<p class ="inline-txt" style="color:red;font-weight:bold;" id="mappKakaoTemp"><c:out value="${smsTemplateInfo.mappKakaoTempNm }"/> 외 <c:out value="${smsTemplateInfo.mappKakaoTempCnt - 1}"/>건</p>
												</c:if>
												<c:if test="${smsTemplateInfo.mappKakaoTempCnt eq 0 }">
													<p class ="inline-txt">알림톡 템플릿 연동 내역 없음</p>
												</c:if>
											</div>
										</li>
									</ul>
									<ul>

										<li>
											<label>등록자</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value="${smsTemplateInfo.regNm}"/></p>
											</div>
										</li>
										<li>
											<label>수정자</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value="${smsTemplateInfo.upNm}"/></p>
											</div>
										</li>
										<li>
											<label>등록일시</label>
											<div class="list-item">
												<fmt:parseDate var="regDt" value="${smsTemplateInfo.regDt}" pattern="yyyyMMddHHmmss"/>
												<fmt:formatDate var="regDt" value="${regDt}" pattern="yyyy.MM.dd HH:mm"/>
												<p class="inline-txt"><c:out value="${regDt}"/></p>
											</div>
										</li>
										<li>
											<label>수정일시</label>
											<div class="list-item">
												<fmt:parseDate var="upDt" value="${smsTemplateInfo.upDt}" pattern="yyyyMMddHHmmss"/>
												<fmt:formatDate var="upDt" value="${upDt}" pattern="yyyy.MM.dd HH:mm"/>
												<p class="inline-txt"><c:out value="${upDt}"/></p>
											</div>
										</li>
									</ul>
								</div>
							</div>
							<!-- //템플릿 내용 -->
	
							<!-- btn-wrap// -->
							<div class="btn-wrap">
								<button type="button" class="btn big fullred <c:if test="${'002' eq smsTemplateInfo.status}">hide</c:if>" onclick="goUpdate();">수정</button>
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
