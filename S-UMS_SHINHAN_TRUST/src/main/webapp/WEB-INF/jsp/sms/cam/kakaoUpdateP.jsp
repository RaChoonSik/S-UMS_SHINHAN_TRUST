<%--
	/**********************************************************
	*	작성자 : 김재환
	*	작성일시 : 2021.12.21
	*	설명 : 카카오 알림톡 발송 수정 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sms.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld"%>

<script type="text/javascript" src="<c:url value='/js/sms/cam/kakaoUpdateP.js'/>"></script>

<body>
	<div id="wrap" class="sms">

		<!-- lnb// -->
		<div id="lnb">
			<!-- LEFT MENU -->
			<%@ include file="/WEB-INF/jsp/inc/menu_sms.jsp"%>
			<!-- LEFT MENU -->
		</div>
		<!-- //lnb -->

		<!-- content// -->
		<div id="content">

			<!-- cont-head// -->
			<section class="cont-head">
				<div class="title">
					<h2>알림톡 정보수정</h2>
				</div>

				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp"%>
				<!-- //공통 표시부 -->

			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
				<form id="searchForm" name="searchForm" method="post">
					<input type="hidden" name="page" value="<c:out value='1'/>">
					<input type="hidden" name="searchStartDt" value="<c:out value='${searchVO.searchStartDt}'/>">
					<input type="hidden" name="searchEndDt" value="<c:out value='${searchVO.searchEndDt}'/>">
					<input type="hidden" name="searchTaskNm" value="<c:out value='${searchVO.searchTaskNm}'/>">
					<input type="hidden" name="searchTempCd" value="<c:out value='${searchVO.searchTempCd}'/>">
					<input type="hidden" name="searchCampNo" value="<c:out value='${searchVO.searchCampNo}'/>">
					<input type="hidden" name="searchDeptNo" value="<c:out value='${searchVO.searchDeptNo}'/>">
					<input type="hidden" name="searchUserId" value="<c:out value='${searchVO.searchUserId}'/>">
					<input type="hidden" name="searchStatus" value="<c:out value='${searchVO.searchStatus}'/>">
					<input type="hidden" name="searchSmsStatus" value="<c:out value='${searchVO.searchSmsStatus}'/>">
				</form>

				<form id="kakaoInfoForm" name="kakaoInfoForm" method="post">
					<input type="hidden" id="msgid"          name="msgid"          value="<c:out value='${kakaoInfo.msgid}'/>">
					<input type="hidden" id="keygen"         name="keygen"         value="<c:out value='${kakaoInfo.keygen}'/>">
					<input type="hidden" id="campNo"         name="campNo"         value="<c:out value='${kakaoInfo.campNo}'/>">
					<input type="hidden" id="sendTyp"        name="sendTyp"        value="<c:out value='${kakaoInfo.sendTyp}'/>">
					<input type="hidden" id="status"         name="status"         value="<c:out value='${kakaoInfo.status}'/>">
					<input type="hidden" id="smsStatus"      name="smsStatus"      value="<c:out value='${kakaoInfo.smsStatus}'/>">
					<input type="hidden" id="smsPhones"      name="smsPhones"      value="">
					<input type="hidden" id="smsName"        name="smsName"        value="<c:out value='${kakaoInfo.smsName}'/>">
					<input type="hidden" id="tempCd"         name="tempCd"         value="<c:out value='${kakaoInfo.tempCd}'/>">
					<input type="hidden" id="segNo"          name="segNo"          value="<c:out value='${kakaoInfo.segNo}'/>">
					<input type="hidden" id="validYn"        name="validYn"        value="<c:out value='${kakaoInfo.validYn}'/>">
					<input type="hidden" id="kakaoCol"		 name="kakaoCol"       value="<c:out value='${kakaoInfo.kakaoCol}'/>">
					<input type="hidden" id="kakaoMergeCol"  name="kakaoMergeCol"  value="<c:out value='${kakaoInfo.kakaoMergeCol}'/>">
					<input type="hidden" id="kakaoMergeCols" name="kakaoMergeCols" value="">
					<input type="hidden" id="orgTempCd"      name="orgTempCd"      value="<c:out value='${kakaoInfo.tempCd}'/>">
					<input type="hidden" id="orgSegNo"       name="orgSegNo"       value="<c:out value='${kakaoInfo.segNo}'/>">
					<input type="hidden" id="orgSmsMessage"                        value="<c:out value='${kakaoInfo.smsMessage}'/>">
					<input type="hidden" id="orgTempMessage"                       value="">
					<input type="hidden" id="templateLength"                       value="0">
					<input type="hidden" id="legalYn"		name="legalYn"         value="<c:out value='${kakaoInfo.legalYn}'/>">
					<input type="hidden" id="legalCf"		name="legalCf"         value="<c:out value='${kakaoInfo.legalCf}'/>">
					<fieldset>
						<legend>조건 및 알림톡, 수신대상자</legend>

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
											<fmt:parseDate var="sendDate" value="${kakaoInfo.sendDate}" pattern="yyyyMMddHHmmss"/>
											<fmt:formatDate var="sendYmd" value="${sendDate}" pattern="yyyy.MM.dd"/>
											<fmt:formatDate var="sendHour" value="${sendDate}" pattern="HH"/>
											<fmt:formatDate var="sendMin" value="${sendDate}" pattern="mm"/>
											<script type="text/javascript">var sendYmd = "<c:out value='${sendYmd}'/>";</script>
											
											<div class="radiobox" style="display:inline-block;">
												<label for="instant"><input type="radio" id="instant" name="chkSendCond" value="000" <c:if test="${'000' eq kakaoInfo.sendTyp}"> checked </c:if>><span>즉시발송</span></label>
												<label for="reserve"><input type="radio" id="reserve" name="chkSendCond" value="001"  <c:if test="${'001' eq kakaoInfo.sendTyp}"> checked </c:if>><span>예약발송</span></label>
											</div>
											<div class="datepickertimebox" style="display:inline-block;"><!-- 3월 28일 수정 -->
												<div class="datepickerrange fromDate" style="width:40%;">
													<label>
														<input type="text" id="sendYmd" name="sendYmd" value="<c:out value='${sendYmd}'/>">
													</label>
												</div>
												<div class="select" style="width:25%;">
													<select id="sendHour" name="sendHour" title="시간 선택">
														<c:forEach begin="0" end="23" var="hour">
															<option value="<c:out value='${hour}'/>"<c:if test="${hour == sendHour}"> selected</c:if>><c:out value='${hour}'/>시</option>
														</c:forEach>
													</select>
												</div>
												<div class="select" style="width:25%;">
													<select id="sendMin" name="sendMin" title="분 선택">
														<c:forEach begin="0" end="59" var="min">
															<option value="<c:out value='${min}'/>"<c:if test="${min == sendMin}"> selected</c:if>><c:out value='${min}'/>분</option>
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
											<div class="filebox">
												<c:set var="campNm" value="" />
												<c:forEach items="${campList}" var="camp">
													<c:if test="${camp.campNo == kakaoInfo.campNo}">
														<c:set var="campNm" value="${camp.campNm}" />
													</c:if>
												</c:forEach>
												<p class="label" id="txtCampNm"> <c:out value="${campNm}" /></p>
												<button type="button" class="btn fullgreen" onclick="popCampSelect();">선택</button>
											</div>
										</div>
									</li>
									<li>
										<label class="required">수신자그룹</label>
										<div class="list-item">
											<div class="filebox">
												<c:set var="segNm" value="" />
												<c:set var="segNoc" value="" />
												<c:forEach items="${segList}" var="seg">
													<c:if test="${seg.segNo == kakaoInfo.segNo}">
														<c:set var="segNm" value="${seg.segNm}" />
														<c:set var="segNoc" value="${seg.segNo}|${seg.mergeKey}" />
													</c:if>
												</c:forEach>
												<p class="label" style="width:calc(100% - 116px);" id="txtSegNm"><c:out value="${kakaoInfo.segNm}"/></p>
												<input type="hidden" id="segNoc" name="segNoc" value="<c:out value="${kakaoInfo.segNoc}"/>">
												<button type="button" class="btn fullgreen" onclick="popSegSelect();">선택</button>
												<button type="button" class="btn fullgreen" onclick="goSegInfoMail('');">미리보기</button>
											</div>
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
																<option value="<c:out value='${dept.deptNo}'/>"<c:if test="${dept.deptNo == kakaoInfo.deptNo}"> selected</c:if>><c:out value='${dept.deptNm}'/></option>
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
																<option value="<c:out value='${user.userId}'/>"<c:if test="${user.userId eq kakaoInfo.userId}"> selected</c:if>><c:out value='${user.userNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
									</c:if>
 									<li>
										<label class="required">발송자명</label>
										<div class="list-item">
											<input type="text" placeholder="발송자명을 입력해주세요." id="sendNm" name="sendNm" value="<c:out value='${userInfo.userNm}'/>">
										</div>
									</li>
									<li>
										<label class="required">발신번호</label>
										<div class="list-item">
											<input type="text" id="sendTelno" name="sendTelno" placeholder="발송자 연락처를 입력해주세요." value="<crypto:decrypt colNm='SEND_TELNO' data='${kakaoInfo.sendTelno}'/>" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');">
										</div>
									</li> 
									<li class="col-full">
										<label class="required">알림톡명</label>
										<div class="list-item">
											<input type="text" id="taskNm" name="taskNm" placeholder="알림톡명을 입력해주세요." value="<c:out value='${kakaoInfo.taskNm}'/>">
										</div>
									</li>
								</ul>
							</div>
						</div>
						<!-- //조건 -->

						<!-- btn-wrap// -->
						<div class="btn-wrap tar">
							<button type="button" class="btn big fullgreen" id="btnKakaoAdmit" style="display:none;" onclick="goKakaoAdmit()">발송승인</button>
							<button type="button" class="btn big" id="btnKakaoDelete"  style="display:none;" onclick="goKakaoDelete()">삭제</button>
							<button type="button" class="btn big fullgreen" id="btnKakaoEnable" style="display:none;" onclick="goKakaoEnable()">복구</button>
							<button type="button" class="btn big" onclick="goKakaoCopy()">복사</button>
						</div>
						
						<!-- 카카오 알림톡,  수신대상자// -->
						<div class="smsinfobox">
							<div class="box">
								<div class="title-area">
									<h3 class="h3-title" id="smsTitle">카카오 알림톡 템플릿 항목 등록</h3>
								</div>

								<div class="kakao-area">
									<!-- 미리보기 영역// -->
									<div>
										<div class="previewbox">
											<span class="name">카카오 채널명</span>
											<div class="messagebox">
												<span class="tit">알림톡 도착</span>
												<textarea id="smsMessage" name="smsMessage" class="message" readonly><c:out value='${kakaoInfo.smsMessage}' /></textarea>
											</div>
										</div>
										<!-- //미리보기 영역 -->
										<p class="infotxt">※ 줄바꿈은 해상도에 따라 상이합니다.</p>
									</div>

									<div class="function-form">
										<label class="bold">알림톡 템플릿 선택</label>
										<div class="select" style="margin-top:5px;margin-bottom:20px;">
											<select id="templateList" onchange="getKakaoTemplateInfo();" title="알림톡 템플릿 선택">
												<option value="<c:out value='${kakaoInfo.tempCd}' />">선택</option>
												<c:if test="${fn:length(kakaoTemplateList) > 0}">
													<c:forEach items="${kakaoTemplateList}" var="template">
														<option value="<c:out value='${template.tempCd}'/>"
															<c:if test="${template.tempCd == kakaoInfo.tempCd}"> selected</c:if>><c:out value='${template.tempNm}' />
														</option>
													</c:forEach>
												</c:if>
											</select>
										</div>
										<div class="grid-area" id="divTemplateMergeList">
											<%@ include file="/WEB-INF/jsp/sms/cam/kakaoTemplateMergeList.jsp"%>
										</div>

										<div class="validity-area clear">
											<p class="infotxt color-red" id="validMessage" style="display:none;">*알림톡 템플릿과 유효하지 않아 SMS로 발송됩니다.</p>
											<button type="button" id="btnValid" class="btn btn-validity" onclick="checkValidate();">유효성 확인</button><!-- 유효성 일치 시 .fullgreen 클래스 추가 or 유효성 불일치 시 .error 클래스 추가 -->

											<div class="tooltip" style="display:inline-block;">
												<button type="button" class="tooltip-btn"><span class="hide">툴팁박스</span></button>

												<div class="tooltip-cont tal" style="z-index:999;">
													<strong class="title">알림톡 유효성 확인이란?</strong>
													<ul>
														<li style="margin-bottom:10px;">카카오 승인을 받은 알림톡 템플릿 형식과 내용이 유효한지 검토하는 기능입니다.</li>
														<li>유효성 확인이 실패된 카카오 알림톡은 SMS 또는 LMS로 전송됩니다.</li>
													</ul>
												</div>
											</div>
										</div>
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
												<input type="text" id="addPhone" name="addPhone" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" placeholder="연락처를 숫자만 입력해주세요" style="width: calc(100% - 52px);">
												<button type="button" class="btn fullgreen" onclick="addPhoneNumber();" id="btnAddPhoneNumber">등록</button>
											</div>
										</li>
									</ul>

									<div class="recipient">
										<div class="add-num">
											<p class="inline-txt" id="addPhoneNone" style="display:none;">등록된 수신자가 없습니다.</p>
											<ul class="numlist" id="addPhoneNumberList">
												<c:if test="${fn:length(kakaoPhoneList) > 0}">
													<c:set var="phoneTotalCount" value="${0}"/>
													<c:forEach items="${kakaoPhoneList}" var="smsPhone">
														<c:set var="phoneTotalCount" value="${phoneTotalCount + 1}"/>
														<li>
															<span>${smsPhone.phone}</span>
															<input type="hidden" name="phoneNumber" value="<c:out value='${smsPhone.phone}'/>">
															<button type="button" class="btn-del" onclick="deletePhoneNumber(this)" name="phoneNumber" ><span class="hide">삭제</span></button>
														</li>
													</c:forEach>
													<script type="text/javascript">
														addPhoneNumberCount = <c:out value='${phoneTotalCount}'/>;
													</script>
												</c:if>
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
						<!-- //카카오 알림톡, 수신대상자 -->

						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullgreen" id="btnKakaoUpdate" onclick="goKakaoUpdate();">수정</button>
							<button type="button" class="btn big" onclick="goKakaoCancel();">목록</button>
						</div>
						<!-- //btn-wrap -->

					</fieldset>
				</form>

			</section>
			<!-- //cont-body -->

		</div>
		<!-- // content -->
	</div>

	<!-- 수신자그룹미리보기팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_seg_sms.jsp"%>
	<!-- //수신자그룹미리보기팝업 -->

	<!-- 조회사유팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_search_reason.jsp"%>
	<!-- //조회사유팝업 -->

	<!-- 파일등록 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_upload_file_sms.jsp"%>
	<!-- //파일등록 팝업 -->

	<!-- 캠페인선택 팝업// -->
	<%@ include file="/WEB-INF/jsp/sms/cam/pop/pop_campaign.jsp"%>
	<!-- //캠페인선택 팝업 -->

	<!-- 수신자그룹선택 팝업// -->
	<%@ include file="/WEB-INF/jsp/sms/cam/pop/pop_segment.jsp"%>
	<!-- //수신자그룹선택 팝업 -->

	<!--수신자그룹 파일등록 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_upload_seg.jsp"%>
	<!-- //수신자그룹 파일등록 팝업 -->
</body>
</html>
