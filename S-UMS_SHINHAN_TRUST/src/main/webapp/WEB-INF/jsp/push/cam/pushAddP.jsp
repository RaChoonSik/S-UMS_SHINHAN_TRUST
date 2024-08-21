<%--
/**********************************************************
*	작성자 : 김준희
*	작성일시 : 2022.04.01
*	설명 : PUSH 발송 신규등록 화면
**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_push.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld"%>

<script type="text/javascript" src="<c:url value='/js/push/cam/pushAddP.js'/>"></script>

<body>
	<div id="wrap" class="push">

		<!-- lnb// -->
		<div id="lnb">
			<!-- LEFT MENU -->
			<%@ include file="/WEB-INF/jsp/inc/menu_push.jsp" %>
			<!-- LEFT MENU -->
		</div>
		<!-- //lnb -->

		<!-- content// -->
		<div id="content">

			<!-- cont-head// -->
			<section class="cont-head">
				<div class="title">
					<h2>PUSH알림발송 신규등록</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
				<form id="searchForm" name="searchForm" method="post">
					<input type="hidden" name="page"             value="<c:out value='${searchVO.page}'/>">
					<input type="hidden" name="searchStartDt"    value="<c:out value='${searchVO.searchStartDt}'/>"/>
					<input type="hidden" name="searchEndDt"      value="<c:out value='${searchVO.searchEndDt}'/>"/>
					<input type="hidden" name="searchPushName"   value="<c:out value='${searchVO.searchPushName}'/>"/>
					<input type="hidden" name="searchCampNo"     value="<c:out value='${searchVO.searchCampNo}'/>"/>
					<input type="hidden" name="searchPushGubun"  value="<c:out value='${searchVO.searchPushGubun}'/>"/>
					<input type="hidden" name="searchDeptNo"     value="<c:out value='${searchVO.searchDeptNo}'/>"/>
					<input type="hidden" name="searchUserId"     value="<c:out value='${searchVO.searchUserId}'/>"/>
					<input type="hidden" name="searchWorkStatus" value="<c:out value='${searchVO.searchWorkStatus}'/>"/>
					<input type="hidden" name="searchStatus"     value="<c:out value='${searchVO.searchStatus}'/>"/>
				</form>
				<form id="pushInfoForm" name="pushInfoForm" method="post">
					<input type="hidden" id="pushmessageId" 	name="pushmessageId"     value="0">
					<input type="hidden" id="campNo"			name="campNo"            value="0">
					<input type="hidden" id="imgAppLogo"		                         value="<c:out value='${appLogo}'/>">
					<input type="hidden" id="imgDefaultDomain"	                         value="<c:out value='${DEFAULT_DOMAIN}'/>">
					<input type="hidden" id="imgUploadPushPath"	name="imgUploadPushPath" value="<c:out value='${imgUploadPushPath}'/>">
					<input type="hidden" id="smsYn"				name="smsYn"             value="">
					<input type="hidden" id="callUrlTyp"		name="callUrlTyp"        value=""/>
					<c:set var="nmMerge" value=""/>
					<c:set var="idMerge" value=""/> 
					<c:if test="${fn:length(mergeList) > 0}">
						<c:forEach items="${mergeList}" var="merge" varStatus="mergeStatus">
							<c:if test="${mergeStatus.index == 1}">
								<c:set var="nmMerge" value="${merge.cdNm}"/>
							</c:if>
							<c:if test="${mergeStatus.index == 2}">
								<c:set var="idMerge" value="${merge.cdNm}"/>
							</c:if>
						</c:forEach>
					</c:if>
					<input type="hidden" id="nmMerge"		name="nmMerge"		 value="$:<c:out value='${nmMerge}'/>:$">
					<input type="hidden" id="idMerge"		name="idMerge"		 value="$:<c:out value='${idMerge}'/>:$">
					<input type="hidden" id="pushGubun"		name="pushGubun"	 value="000">
					<input type="hidden" id="legalYn"		name="legalYn"		 value="">
					<input type="hidden" id="legalCf"		name="legalCf"		 value="<c:out value='${pushLegalCf}'/>">
					<input type="hidden" id="segNoc"		name="segNoc"		 value=""> 
					<input type="hidden" id="sendTyp"       name="sendTyp" 		 value="">
					<input type="hidden" id="callUri"       name="callUri" 		 value="">
					<input type="hidden" id="callUriIos"    name="callUriIos" 	 value="">
					<input type="hidden" id="fileSize"      name="fileSize" 	 value="0">
					
					<fieldset>
						<legend>조건 및 PUSH</legend>

						<!-- 조건// -->
						<div class="graybox">
							<div class="title-area">
								<h3 class="h3-title">조건</h3>
								<span class="required">*필수입력 항목</span>
							</div>
							
							<div class="list-area">
								<ul>
									<li class="col-full">
										<label class="required">예약일시</label>
										<div class="list-item">
											<div class="radiobox" style="display:inline-block;">
												<label for="instant"><input type="radio" id="instant" name="chkSendCond" value="000"checked><span>즉시발송</span></label>
												<label for="reserve"><input type="radio" id="reserve" name="chkSendCond" value="001"><span>예약발송</span></label>
											</div>
											<div class="datepickertimebox" style="display:inline-block;width:280px;">
												<div class="datepicker">
													<label>
														<input type="text" id="sendYmd" name="sendYmd" readonly>
													</label>
												</div>
												<div class="select">
													<jsp:useBean id="currTime" class="java.util.Date"/>
													<fmt:formatDate var="currHour" value="${currTime}" pattern="HH"/>
													<fmt:formatDate var="currMin" value="${currTime}" pattern="mm"/>
													<select id="sendHour" name="sendHour" title="시간 선택">
														<c:forEach begin="8" end="20" var="hour">
															<option value="<c:out value='${hour}'/>"<c:if test="${hour == currHour}"> selected</c:if>><c:out value='${hour}'/>시</option>
														</c:forEach>
													</select>
												</div>
												<div class="select">
													<select id="sendMin" name="sendMin" title="분 선택">
														<c:forEach begin="0" end="59" var="min">
															<option value="<c:out value='${min}'/>"<c:if test="${min == currMin}"> selected</c:if>><c:out value='${min}'/>분</option>
														</c:forEach>
													</select>
												</div>
											</div>
										</div>
									</li>
								</ul>
								<ul>
									<li>
										<label class="required">캠페인명</label>
										<div class="list-item">
											<p class="inline-txt" style="width:calc(100% - 63px);" id="txtCampNm">선택된 캠페인이 없습니다.</p>
											<button type="button" class="btn fullorange" onclick="popCampSelect();">선택</button>
										</div>
									</li>
									<li>
										<label class="required">수신자그룹</label>
										<div class="list-item">
											<p class="inline-txt" style="width:calc(100% - 131px);" id="txtSegNm">선택된 수신자그룹이 없습니다.</p>
											<button type="button" class="btn fullorange" onclick="popSegSelect();">선택</button>
											<button type="button" class="btn fullorange" onclick="goSegInfoMail('');">미리보기</button>
										</div>
									</li>
									<%-- 관리자의 경우 전체 요청부서를 전시하고 그 외의 경우에는 숨김 --%>
									<li>
										<label class="required">사용자그룹</label>
										<div class="list-item">
											<div class="select">
											<c:if test="${'Y' eq NEO_ADMIN_YN}">>
												<select id="deptNo" name="deptNo" onchange="getUserList(this.value);" title="사용자그룹 선택">
													<option value="0">선택</option>
													<c:if test="${fn:length(deptList) > 0}">
														<c:forEach items="${deptList}" var="dept">
															<option value="<c:out value='${dept.deptNo}'/>"><c:out value='${dept.deptNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</c:if>
											<c:if test="${'N' eq NEO_ADMIN_YN}">
												<select id="deptNo" name="deptNo" onchange="getUserList(this.value);" title="사용자그룹 선택">
													<c:if test="${fn:length(deptList) > 0}">
														<c:forEach items="${deptList}" var="dept">
															<c:if test="${dept.deptNo == NEO_DEPT_NO}">
																<option value="<c:out value='${dept.deptNo}'/>" selected><c:out value='${dept.deptNm}'/></option>
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
									<li class="col-full">
										<label>옵션</label>
										<div class="list-item">
											<label for="chkSmsYn"><input type="checkbox" id="chkSmsYn" checked><span>메시지 전환 발송</span></label>
											<p class="inline-txt">*PUSH알림 발송 실패 시 메시지로 전환되어 발송 </p>
										</div>
									</li>
									<li class="col-full">
										<label class="required">PUSH명</label>
										<div class="list-item">
											<input type="text" placeholder="PUSH명을 입력해주세요." id="pushName" name="pushName">
										</div>
									</li>
								</ul>
							</div>
						</div>
						<!-- //조건 -->
						
						<div>
							<div class="pushinfobox" id="divPushinfobox">
								<!-- 단문 : pushinfobox / 중문 : type-long / 이미지 : pushinfobox type-img  -->
								<!-- 미리보기// -->
								<div class="box">
									<div class="title-area">
										<h3 class="h3-title">PUSH 미리보기</h3>
									</div>

									<div class="push-area">
										<div class="preview" id="divPushPreview">
											<!-- btn-toggle 클릭시 내림 preview active (on/off) -->
											<button type="button" class="btn-toggle" id="btnToggle" style="display:none;"><span class="hide">버튼 열기/닫기</span></button>
										
											<div class="info">
												<span class="ico">
													<img src='../..<c:out value='${appLogo}'/>' alt="SHINHAN_FUND로고">
												</span>
												<p>
													<strong><c:out value='${appName}'/></strong>
													<jsp:useBean id="appNow" class="java.util.Date" />
													<span><fmt:formatDate value="${appNow}"  pattern="a hh:mm"/></span>
												</p>
											</div>
											
											<div class="cont" id="contText">
												<!-- 내용이 없을경우 display:none처리 해주세요. -->
											</div>
										</div>
										<span class="byte" id="byteText">
										    <!-- 하단 글자 체크하는 부분 -->
										</span>
									</div>
									<p class="guidetxt">※ 안드로이드 환경의 미리보기이며 IOS 및 실제 발송화면과 상이합니다. </p>
								</div>
								<!-- //미리보기 -->
								
								<!-- 발송정보// -->
								<div class="box" style="height:422px;min-height:422px;">
									<div class="title-area">
										<h3 class="h3-title" id="pushInfoTitle">단문 PUSH 발송정보</h3>
									</div>

									<div class="push-area">
										<div class="push-checkbox">
											<label><input type="checkbox" id="chkLegalYn" name="chkLegalYn" onclick="setPushPreview()"><span>광고</span></label>

											<div class="tooltip">
												<button type="button" class="tooltip-btn"><span class="hide">툴팁박스</span></button>

												<div class="tooltip-cont">
													<strong class="title">광고성 알림 발송 의무 표기 사항</strong>
													<ol>
														<li>1.광고성 알림을 전송하는 경우 (광고) 필수 표기</li>
														<li>2.업체명 또는 브랜드명 등 발송자 정보를 필수 표기</li>
														<li>3.수신거부 방법 또는 경로 필수 표기</li>
													</ol>
													<p>▶ 불법 스팸방지를 위한 정보통신망법 &nbsp;<a href="javascript:;">다운로드&gt;&gt;</a></p>
													<p>▶ 정책 관련 문의 사항은 한국인터넷진흥원(☎118) </p>
												</div>
											</div>
										</div>
										
										<div class="list-area">
											<ul>
												<li>
													<div class="list-item">
														<input type="text" placeholder="PUSH 제목을 입력해주세요.(공백포함 40자 이내)" id="pushTitle" name="pushTitle" maxlength="40">
													</div>
												</li>
												<li>
													<div class="list-item">
														<textarea placeholder="보내실 내용을 입력해주세요.(공백포함 30자 이내)" id="pushMessage" name="pushMessage" maxlength="140"></textarea>
													</div>
												</li>
												<li style="margin-top:15px;">
													<label style="display:inline-block;margin-bottom:0;">함수입력</label>
													<div class="list-item" style="display:inline-block;width:calc(100% - 79px);">
														<div class="select" style="width:calc(100% - 100px);">
															<select id="mergeKeyPush" name="mergeKeyPush" title="함수입력">
															</select>
														</div>
														<button type="button" class="btn fullorange" onclick="goTitleMerge();">제목</button>
														<button type="button" class="btn fullorange" onclick="goContMerge();">본문</button>
													</div>
												</li>
												<li class="col-full" style="margin-top:15px;">
													<div class="list-item">
														<button type="button" id=btnAttachFile" class="btn fullorange plus mgl0" onclick="fn.popupOpen('#popup_file');">이미지 첨부</button><!-- 이미지첨부 1개만 가능 -->
														<ul class="filelist" id="pushAttachFileList"></ul>
													</div>
												</li>
											</ul>
										</div>
									</div>
								</div>
								<!-- //발송정보 -->

								<!-- 이동경로// -->
								<!-- 
								<div class="box" style="min-height:235px;">
									<div class="title-area">
										<h3 class="h3-title">이동경로 등록</h3>
										<div class="btn-wrap tar">
											<button type="button" class="btn" onclick="popUrlPreview();">미리보기</button>
										</div>
									</div>
										
									<div class="push-area">
										<div class="list-area">
											<ul>
												<li>
													<label>타입</label>
													<div class="radiobox" style="display:inline-block;">
														<label for="chkWebLink"><input type="radio" id="chkWebLink" name="urlType" value="000" checked><span>Weblink</span></label>
														<label for="chkAppLink"><input type="radio" id="chkAppLink" name="urlType" value="001"><span>Applink</span></label>
														<label for="chkVideoLink"><input type="radio" id="chkVideoLink" name="urlType" value="002"><span>Web Video</span></label>
													</div>
												</li>
												<li>
													<label style="display:inline-block;margin-bottom:0;">Android</label>
													<div class="list-item" style="display:inline-block;width:calc(100% - 78px);">
														<input id="txtCallUri" type="text" placeholder="Android 이동경로 URL을 등록해주세요." style="width:calc(100% - 70px);">
														<button type="button" class="btn fullorange" onclick="goValidUrl('and')">유효성확인</button>
													</div>
												</li>
												<li style="margin-top:15px;">
													<label style="display:inline-block;margin-bottom:0;">iOS</label>
													<div class="list-item" style="display:inline-block;width:calc(100% - 78px);">
														<input id="txtCallUriIos" type="text" placeholder="IOS 이동경로 URL을 등록해주세요." style="width:calc(100% - 70px);">
														<button type="button" class="btn fullorange" onclick="goValidUrl('ios')">유효성확인</button>
													</div>
												</li>
											</ul>
										</div>
									</div>
								</div>
								 -->
								<!-- //이동경로 -->
							</div>
						</div>
						
						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullorange" onclick="goPushAdd();">등록</button>
							<button type="button" class="btn big" onclick="goPushCancel();">목록</button>
						</div>
						<!-- //btn-wrap -->
							
					</fieldset>
				</form>

			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>

	<iframe id="iFrmMail" name="iFrmMail" style="width:0px;height:0px;"></iframe>
	<!-- 수신자그룹미리보기팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_seg_push.jsp" %>
	<!-- //수신자그룹미리보기팝업 -->
	
	<!-- 조회사유팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_search_reason.jsp" %>
	<!-- //조회사유팝업 -->
	
	<!-- 파일등록 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_upload_file_push.jsp" %>
	<!-- //파일등록 팝업 -->
	
	<!-- 캠페인선택 팝업// -->
	<%@ include file="/WEB-INF/jsp/push/cam/pop/pop_campaign.jsp" %>
	<!-- //캠페인선택 팝업 -->

	<!-- 수신자그룹선택 팝업// -->
	<%@ include file="/WEB-INF/jsp/push/cam/pop/pop_segment.jsp" %>
	<!-- //수신자그룹선택 팝업 -->
	
	<!-- 파일등록 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_upload_seg.jsp" %>
	<!-- //파일등록 팝업 -->
	
	<!-- 이동 경로 미리보기  -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_url.jsp" %>
	<!-- //이동 경로 미리보기 -->
</body>
</html>
