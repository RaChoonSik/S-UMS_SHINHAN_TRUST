<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.10.30
	*	설명 : 문자발송 수정 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sms.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld"%>

<script type="text/javascript" src="<c:url value='/js/sms/cam/smsUpdateP.js'/>"></script>

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
					<h2>문자발송 정보수정</h2>
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
					<input type="hidden" id="msgid" 		name="msgid"		 value="<c:out value='${smsInfo.msgid}'/>">
					<input type="hidden" id="keygen" 		name="keygen"		 value="<c:out value='${smsInfo.keygen}'/>">
					<input type="hidden" id="campNo"		name="campNo"		 value="<c:out value='${smsInfo.campNo}'/>">
					<input type="hidden" id="imgUploadPath" name="imgUploadPath" value="<c:out value='${imgUploadPath}'/>">
					<%-- 					
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
					--%>
					<input type="hidden" id="gubun"			name="gubun"		 value="<c:out value='${smsInfo.gubun}'/>">
					<input type="hidden" id="legalYn"		name="legalYn"		 value="<c:out value='${smsInfo.legalYn}'/>">
					<input type="hidden" id="legalCf"		name="legalCf"		 value="<c:out value='${smsLegalCf}'/>">
					<input type="hidden" id="sendTyp"		name="sendTyp" 		 value="<c:out value='${smsInfo.sendTyp}'/>">
					<input type="hidden" id="status"		name="status"		 value="<c:out value='${smsInfo.status}'/>">
					<input type="hidden" id="smsStatus" 	name="smsStatus"	 value="<c:out value='${smsInfo.smsStatus}'/>">
					<input type="hidden" id="attachCnt"		name="attachCnt"	 value="<c:out value='${fn:length(smsAttachList)}'/>">
					<input type="hidden" id="smsPhones"		name="smsPhones"	 value="">
					
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
													<label for="instant"><input type="radio" id="instant" name="chkSendCond" value="000" <c:if test="${'000' eq smsInfo.sendTyp}"> checked </c:if>><span>즉시발송</span></label>
													<label for="reserve"><input type="radio" id="reserve" name="chkSendCond" value="001"  <c:if test="${'001' eq smsInfo.sendTyp}"> checked </c:if>><span>예약발송</span></label>
												</div>
												<div class="datepickertimebox" style="display:inline-block;"><!-- 3월 28일 수정 -->
													<div class="datepickerrange fromDate" style="width:40%;">
														<fmt:parseDate var="sendDate" value="${smsInfo.sendDate}" pattern="yyyyMMddHHmmss"/>
														<fmt:formatDate var="sendYmd" value="${sendDate}" pattern="yyyy.MM.dd"/>
														<fmt:formatDate var="sendHour" value="${sendDate}" pattern="HH"/>
														<fmt:formatDate var="sendMin" value="${sendDate}" pattern="mm"/>
														<script type="text/javascript">var sendYmd = "<c:out value='${sendYmd}'/>";</script>
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
										</div>
									</li>
								</ul>
								<ul>
									<li>
										<label class="required">문자명</label>
										<div class="list-item">
											<input type="text" id="taskNm" name="taskNm" placeholder="문자명을 입력해주세요." value="<c:out value='${smsInfo.taskNm}'/>">
										</div>
									</li>
									<li>
										<label class="required">캠페인명</label>
										<div class="list-item">
											<div class="filebox">
												<c:set var="campNm" value=""/>
												<c:forEach items="${smsCampList}" var="camp">
													<c:if test="${camp.campNo == smsInfo.campNo}">
														<c:set var="campNm" value="${camp.campNm}"/>
													</c:if>
												</c:forEach>
												<p class="label" id="txtCampNm"><c:out value="${campNm}"/></p>
												<button type="button" class="btn fullgreen" onclick="popCampSelect();">선택</button>
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
																<option value="<c:out value='${dept.deptNo}'/>"<c:if test="${dept.deptNo == smsInfo.deptNo}"> selected</c:if>><c:out value='${dept.deptNm}'/></option>
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
																<option value="<c:out value='${user.userId}'/>"<c:if test="${user.userId eq smsInfo.userId}"> selected</c:if>><c:out value='${user.userNm}'/></option>
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
											<input type="text" placeholder="발송자명을 입력해주세요." id="sendNm" name="sendNm" value="<c:out value='${smsInfo.sendNm}'/>">
										</div>
									</li>
									<li>
										<label class="required">발송자 연락처</label>
										<div class="list-item">
											<input type="text" id="sendTelno" name="sendTelno" placeholder="발송자 연락처를 입력해주세요." value="<crypto:decrypt colNm='SEND_TELNO' data='${smsInfo.sendTelno}'/>" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');">
										</div>
									</li>
									<li>
										<label class="required">수신자그룹</label>
										<div class="list-item">
											<div class="filebox">
												<c:set var="segNm" value=""/>
												<c:set var="segNoc" value=""/>
												<c:forEach items="${segList}" var="seg">
													<c:if test="${seg.segNo == smsInfo.segNo}">
														<c:set var="segNm" value="${seg.segNm}"/>
														<c:set var="segNoc" value="${seg.segNo}|${seg.mergeKey}"/>
													</c:if>
												</c:forEach>
												<p class="label" style="width:calc(100% - 120px);" id="txtSegNm"><c:out value="${segNm}"/></p>
												<input type="hidden" id="segNoc" name="segNoc" value="<c:out value="${segNoc}"/>">
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

						<!-- btn-wrap// -->
						<div class="btn-wrap tar">
							<button type="button" class="btn big fullgreen" id="btnSmsAdmit" style="display:none;" onclick="goSmsAdmit()">발송승인</button>
							<button type="button" class="btn big" id="btnSmsDelete"  style="display:none;" onclick="goSmsDelete()">삭제</button>
							<button type="button" class="btn big fullgreen" id="btnSmsEnable" style="display:none;" onclick="goSmsEnable()">복구</button>
							<button type="button" class="btn big" onclick="goSmsCopy()">복사</button>
						</div>
						
						<!-- SMS (단문메시지), 수신대상자// -->
						<div class="smsinfobox">
							<div class="box">
								<div class="title-area">
									<h3 class="h3-title" id="smsTitle">
									<c:if test="${'000' eq smsInfo.gubun}">SMS SMS (단문 메시지)</c:if>
									<c:if test="${'001' eq smsInfo.gubun}">LMS (장문 메시지)</c:if>
									<c:if test="${'002' eq smsInfo.gubun}">MMS (멀티미디어 메시지)</c:if>
									
									</h3>
								</div>

								<div class="sms-area">
									<div class="phone-preview" id="divPhonePreview">
										<span class="byte" id="messageByte">0/2000byte</span>
									</div>

									<div class="phone-form">
										<div class="phone-checkbox">
											<label for="chklegalYn"><input type="checkbox" id="chklegalYn" name="chklegalYn" onclick="setPhonePreview()" <c:if test="${'Y' eq smsInfo.legalYn}"> checked </c:if>><span>광고</span></label>

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
										<input type="text" placeholder="문자 제목을 입력해주세요" id="smsName" name="smsName" value="<c:out value='${smsInfo.smsName}'/>">
										<textarea id="smsMessage" name="smsMessage" placeholder="보내실 내용을 입력해주세요." ><c:out value='${smsInfo.smsMessage}'/></textarea>

										<button type="button" id="btnAttachFile" class="btn plus fullgreen mgl0" onclick="fn.popupOpen('#popup_file');" <c:if test="${fn:length(smsAttachList) > 2}">disabled</c:if>> 파일선택</button>
										<ul class="filelist" id="smsAttachFileList">
											<c:if test="${fn:length(smsAttachList) > 0}">
												<c:set var="totalCount" value="${0}"/>
												<c:forEach items="${smsAttachList}" var="attach">
													<c:set var="totalCount" value="${totalCount + 1}"/>
													<li>
														<input type="hidden" name="attachNm" value="<c:out value='${attach.attNm}'/>">
														<c:set var="attFlPath" value="${fn:substring(attach.attFlPath, fn:indexOf(attach.attFlPath,'/')+1, fn:length(attach.attFlPath))}"/>
														<input type="hidden" name="attachPath" value="<c:out value='${attach.attFlPath}'/>">
														<span><a href="javascript:goImgFileDown('007', '<c:out value="${attach.attNm}"/>','<c:out value='${attach.attFlPath}'/>');"><c:out value='${attach.attNm}'/></a></span>
														<button type="button" class="btn-del" onclick="deleteAttachFile(this)"><span class="hide">삭제</span></button>
													</li>
												</c:forEach>
												<script type="text/javascript">
												totalFileCnt = <c:out value='${totalCount}'/>;
												</script>
											</c:if>
										</ul>
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
												<button type="button" class="btn fullgreen" onclick="addPhoneNumber();" id="btnAddPhoneNumber">등록</button>
											</div>
										</li>
									</ul>

									<div class="recipient">
										<div class="add-num">
											<p class="inline-txt" id="addPhoneNone" style="display:none;">등록된 수신자가 없습니다.</p>
											<ul class="numlist" id="addPhoneNumberList">
												<c:if test="${fn:length(smsPhoneList) > 0}">
													<c:set var="phoneTotalCount" value="${0}"/>
													<c:forEach items="${smsPhoneList}" var="smsPhone">
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
						<!-- //SMS (단문메시지), 수신대상자 -->
	
						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullgreen" id="btnSmsUpdate" style="display:none;" onclick="goSmsUpdate();">수정</button>
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
	
	<!-- 수신자 그룹파일등록 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_upload_seg.jsp" %>
	<!-- //파일등록 팝업 -->
	
</body> 
</html>
