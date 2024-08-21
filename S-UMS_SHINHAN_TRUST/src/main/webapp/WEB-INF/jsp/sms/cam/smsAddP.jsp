<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.10.19
	*	설명 : 문자발송 신규등록 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sms.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld"%>

<script type="text/javascript" src="<c:url value='/js/sms/cam/smsAddP.js'/>"></script>

<body>
	<div id="wrap" class="sms">

		<!-- lnb// -->
		<div id="lnb">
			<!-- LEFT MENU -->
			<%@ include file="/WEB-INF/jsp/inc/menu_sms.jsp" %>
			<!-- LEFT MENU -->
		</div>
		<!-- //lnb -->

		<!-- content// -->
		<div id="content">

			<!-- cont-head// -->
			<section class="cont-head">
				<div class="title">
					<h2>문자발송 신규등록</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
				<form id="searchForm" name="searchForm" method="post">
					<input type="hidden" name="searchStartDt"   value="<c:out value='${searchVO.searchStartDt}'/>"/>
					<input type="hidden" name="searchEndDt"     value="<c:out value='${searchVO.searchEndDt}'/>"/>
					<input type="hidden" name="searchDeptNo"    value="<c:out value='${searchVO.searchDeptNo}'/>"/>
					<input type="hidden" name="searchTaskNm"    value="<c:out value='${searchVO.searchTaskNm}'/>"/>
					<input type="hidden" name="searchCampNo"    value="<c:out value='${searchVO.searchCampNo}'/>"/>
					<input type="hidden" name="searchStatus"    value="<c:out value='${searchVO.searchStatus}'/>"/>
					<input type="hidden" name="searchSmsStatus" value="<c:out value='${searchVO.searchSmsStatus}'/>"/>
					<input type="hidden" name="searchGubun"     value="<c:out value='${searchVO.searchGubun}'/>"/>
				</form>
				<form id="smsInfoForm" name="smsInfoForm" method="post">
					<input type="hidden" id="msgid" 			name="msgid"  value="">
					<input type="hidden" id="keygen" 			name="keygen" value="">
					<input type="hidden" id="campNo"			name="campNo" value="0">
					<input type="hidden" id="imgUploadPath"		name="imgUploadPath" value="<c:out value='${imgUploadPath}'/>">

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
					<input type="hidden" id="gubun"			name="gubun"		 value="000">
					<input type="hidden" id="legalYn"		name="legalYn"		 value="N">
					<input type="hidden" id="legalCf" 		name="legalCf"		 value="<c:out value='${smsLegalCf}'/>">
					<input type="hidden" id="smsPhones"		name="smsPhones"	 value="">
					<input type="hidden" id="segNoc"		name="segNoc"		 value=""> 
					<input type="hidden" id="sendTyp"       name="sendTyp" 		 value="000">
					
					<fieldset>
						<legend>조건 및 SMS, 수신대상자</legend>

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
											<div class="datepickertimebox">
												<div class="radiobox" style="display:inline-block;">
													<label for="instant"><input type="radio" id="instant" name="chkSendCond" value="000"checked><span>즉시발송</span></label>
													<label for="reserve"><input type="radio" id="reserve" name="chkSendCond" value="001"><span>예약발송</span></label>
												</div>
												<div class="datepickertimebox" style="display:inline-block;"><!-- 3월 28일 수정 -->
													<div class="datepickerrange fromDate" style="width:40%;">
														<label>
															<input type="text" id="sendYmd" name="sendYmd" readonly>
														</label>
													</div>
													<div class="select" style="width:25%;">
														<jsp:useBean id="currTime" class="java.util.Date"/>
														<fmt:formatDate var="currHour" value="${currTime}" pattern="HH"/>
														<fmt:formatDate var="currMin" value="${currTime}" pattern="mm"/>
														<select id="sendHour" name="sendHour" title="시간 선택">
															<c:forEach begin="0" end="23" var="hour">
																<option value="<c:out value='${hour}'/>"<c:if test="${hour == currHour}"> selected</c:if>><c:out value='${hour}'/>시</option>
															</c:forEach>
														</select>
													</div>
													<div class="select" style="width:25%;">
														<select id="sendMin" name="sendMin" title="분 선택">
															<c:forEach begin="0" end="59" var="min">
																<option value="<c:out value='${min}'/>"<c:if test="${min == currMin}"> selected</c:if>><c:out value='${min}'/>분</option>
															</c:forEach>
														</select>
													</div>
												</div>
											</div>
										</div>
									</li>
								</ul>
								<ul>
									<li>
										<label class="required">문자명</label>
										<div class="list-item">
											<input type="text" id="taskNm" name="taskNm" placeholder="문자명을 입력해주세요.">
										</div>
									</li>
									<li>
										<label class="required">캠페인명</label>
										<div class="list-item">
											<div class="filebox">
												<p class="label" id="txtCampNm">선택된 캠페인이 없습니다.</p>
												<button type="button" class="btn fullgreen" onclick="popCampSelect();">선택</button>
											</div>
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
 									<li>
										<label class="required">발송자명</label>
										<div class="list-item">
											<input type="text" placeholder="발송자명을 입력해주세요." id="sendNm" name="sendNm" value="<c:out value='${userInfo.userNm}'/>">
										</div>
									</li>
									<li>
										<label class="required">발송자 연락처</label>
										<div class="list-item">
											<input type="text" id="sendTelno" name="sendTelno" placeholder="발송자 연락처를 입력해주세요." value="<crypto:decrypt colNm='USER_TEL' data='${userInfo.userTel}'/>" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');">
										</div>
									</li>
									<li>
										<label class="required">수신자그룹</label>
										<div class="list-item">
											<div class="filebox">
												<p class="label" style="width:calc(100% - 120px);" id="txtSegNm">선택된 수신자그룹이 없습니다.</p>
												<button type="button" class="btn fullgreen" onclick="popSegSelect();">선택</button>
												<button type="button" class="btn fullgreen" onclick="goSegInfoMail('');">미리보기</button>
											</div>
										</div>
									</li>
									<li>
										<label>머지입력</label>
										<div class="list-item">
											<div class="select" style="width:calc(100% - 96px);"><!-- 3월 28일 수정 -->
												<select id="mergeKeySms" name="mergeKeySms" title="머지입력 선택">
												</select>
											</div>
											<button type="button" class="btn fullgreen" onclick="goTitleMerge();">제목</button>
											<button type="button" class="btn fullgreen" onclick="goContMerge();">본문</button>
										</div>
									</li>  
								</ul>
							</div>
						</div>
						<!-- //조건 -->

						<!-- SMS (단문메시지), 수신대상자// -->
						<div class="smsinfobox">
							<div class="box">
								<div class="title-area">
									<h3 class="h3-title" id="smsTitle">SMS (단문메시지)</h3>
								</div>

								<div class="sms-area">
									<div class="phone-preview" id="divPhonePreview">
										<span class="byte" id="messageByte">0/2000byte</span>
									</div>

									<div class="phone-form">
										<div class="phone-checkbox">
											<label for="chklegalYn"><input type="checkbox" id="chklegalYn" name="chklegalYn" onclick="setPhonePreview()"><span>광고</span></label>

											<div class="tooltip">
												<button type="button" class="tooltip-btn"><span class="hide">툴팁박스</span></button>

												<div class="tooltip-cont">
													<strong class="title">광고 문자 발송 의무 표기 사항</strong>
													<ol>
														<li>1.광고문자를 전송하는 경우 (광고) 필수 표기</li>
														<li>2.업체명 또는 브랜드명 등 발송자 정보를 필수 표기</li>
														<li>3.무료수신거부 문구 및 080 전화번호 필수 표기</li>
													</ol>
													<p>▶ 불법 스팸방지를 위한 정보통신망법  <a href="javascript:;">다운로드&gt;&gt;</a></p>
													<p>▶ 정책 관련 문의 사항은 한국인터넷진흥원(☎118) </p>
												</div>
											</div>
										</div>
										<input type="text" placeholder="문자 제목을 입력해주세요" id="smsName" name="smsName">
										<textarea id="smsMessage" name="smsMessage" placeholder="보내실 내용을 입력해주세요."></textarea>

										<button type="button" id="btnAttachFile" class="btn plus fullgreen mgl0" onclick="fn.popupOpen('#popup_file');"> 파일선택</button>
										<ul class="filelist" id="smsAttachFileList"></ul>
									</div>
								</div>
							</div>	
							
							<div class="box">
								<div class="title-area">
									<h3 class="h3-title">추가 수신자 리스트</h3>
									<button type="button" class="btn-refresh" onclick="addPhoneNumberClear();"><span class="hide">새로고침</span></button>
								</div>
								<div class="list-area">
									<ul> 
										<li>
											<label>직접입력</label>
											<div class="list-item">
												<input type="text" id="addPhone" name="addPhone" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" placeholder="연락처를 숫자만 입력해주세요" style="width:calc(100% - 52px);">
												<button type="button" class="btn fullgreen" onclick="addPhoneNumber();">등록</button>
											</div>
										</li>
									</ul>

									<div class="recipient">
										<div class="add-num">
											<p class="inline-txt">등록된 수신자가 없습니다.</p>
											<ul class="numlist" id="addPhoneNumberList">
											</ul>
										</div>
										<p>
											<span>Total</span> 
											<strong>
												<input type="text" id="addPhoneCount" placeholder="0" readonly class="num-read tar">
												명
											</strong>
										</p>
									</div>
									<p class="txt">*중복된 연락처는 1건만 발송됩니다. </p>
								</div>
							</div>
						</div>
						<!-- //SMS (단문메시지), 수신대상자 -->
	
						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullgreen" onclick="goSmsAdd();">등록</button>
							<button type="button" class="btn big" onclick="goSmsCancel();">목록</button>
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
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_seg_sms.jsp" %>
	<!-- //수신자그룹미리보기팝업 -->
	
	<!-- 조회사유팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_search_reason.jsp" %>
	<!-- //조회사유팝업 -->
	
	<!-- 파일등록 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_upload_file_sms.jsp" %>
	<!-- //파일등록 팝업 -->
	
	<!-- 캠페인선택 팝업// -->
	<%@ include file="/WEB-INF/jsp/sms/cam/pop/pop_campaign.jsp" %>
	<!-- //캠페인선택 팝업 -->

	<!-- 수신자그룹선택 팝업// -->
	<%@ include file="/WEB-INF/jsp/sms/cam/pop/pop_segment.jsp" %>
	<!-- //수신자그룹선택 팝업 -->
	
	<!-- 수신자그룹 파일등록 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_upload_seg.jsp" %>
	<!-- //파일등록 팝업 -->
	
</body>
</html>
