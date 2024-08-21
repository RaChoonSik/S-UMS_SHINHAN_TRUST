<%--
	/**********************************************************
	*	작성자 : 이혜민
	*	작성일시 : 2022.06.16
	*	설명 : SMS 상세로그 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sms.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sms/ana/detailLogListP.js'/>"></script>
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
					<h2>상세로그</h2>
				</div>
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->
			<!-- cont-body// -->
			<section class="cont-body">
			<form id="searchForm" name="searchForm" method="post">
					<input type="hidden" id="page" name="page"       value="<c:out value='${searchVO.page}'/>">
					<input type="hidden" id="rows" name="rows"       value="<c:out value='${searchVO.rows}'/>">
					<input type="hidden" id="msgid" name="msgid"     value="">
					<input type="hidden" id="keygen" name="keygen"   value="">
					<input type="hidden" id="searchCampNm"  name="searchCampNm"         value="">
					<input type="hidden" id="imgUploadPath"          value="<c:out value='${imgUploadPath}'/>">
					<fieldset>
						<legend>조회 및 목록</legend>
						<!-- 조회// -->
						<div class="graybox">
							<div class="title-area">
								<h3 class="h3-title">조회</h3>
							</div>
							<div class="list-area">
								<ul>
									<li>
										<label>발송일시</label>
										<div class="list-item">
											<!-- datepickerrange// -->
											<div class="datepickerrange fromDate">
												<label>
													<fmt:parseDate var="startDt" value="${searchVO.searchStartDt}" pattern="yyyyMMdd"/>
													<fmt:formatDate var="searchStartDt" value="${startDt}" pattern="yyyy.MM.dd"/> 
													<input type="text" id="searchStartDt" name="searchStartDt" value="<c:out value='${searchStartDt}'/>" readonly>
												</label>
											</div>
											<span class="hyppen date"></span>
											<div class="datepickerrange toDate">
												<label>
													<fmt:parseDate var="endDt" value="${searchVO.searchEndDt}" pattern="yyyyMMdd"/>
													<fmt:formatDate var="searchEndDt" value="${endDt}" pattern="yyyy.MM.dd"/> 
													<input type="text" id="searchEndDt"  name="searchEndDt" value="<c:out value='${searchEndDt}'/>" readonly>
												</label>
											</div>
											<!-- //datepickerrange -->
										</div>
									</li>
									<li>
										<label>메세지 구분</label>
										<div class="list-item">
											<label><input type="checkbox" name= "searchGubunNm" value="000" checked ><span>SMS</span></label>
											<label><input type="checkbox" name= "searchGubunNm" value="001" checked ><span>LMS</span></label>
											<label><input type="checkbox" name= "searchGubunNm" value="002" checked ><span>MMS</span></label>
											<label><input type="checkbox" name= "searchGubunNm" value="004" checked ><span>알림톡</span></label>
										</div>
									</li>
									<li>
									<label>고객ID</label>
										<input type="text" id="searchCustId" name="searchCustId" placeholder="ID를 입력하세요">
									</li>
									<li>
									<label>고객명</label>
										<input type="text" id="searchCustNm" name="searchCustNm" placeholder="고객명을 입력하세요">
									</li>
									<li>
									<label>고객 전화번호</label>
										<input type="text" id="searchCustPhone" name="searchCustPhone" placeholder="전화번호를 입력하세요" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');">
									</li>
									<li>
										<label>캠페인명</label>
										<div class="list-item">
											<div class="filebox">
												<p class="label" id="txtCampNm">선택된 캠페인이 없습니다.</p>
												<button type="button" class="btn fullgreen" onclick="popCampSelect();">선택</button>
											</div>
										</div>
									</li>
									<li>
										<label>발송상태</label>
										<div class="list-item">
											<div class="select">
												<select id="searchStatus" name="searchStatus" title="발송상태 선택">
													<option value="">선택</option>
													<option value="1">성공</option>
													<option value="0">실패</option>
												</select>
											</div>
										</div>
									</li>
									<li>
									<label>발송자명</label>
										<input type="text" id="searchExeUserNm" name="searchExeUserNm" placeholder="발송자명을 입력하세요">
									</li>

								</ul>
							</div>
						</div>
						<!-- //조회 -->

						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullgreen" onclick="goSearch('1');">검색</button>
							<button type="button" class="btn big" onclick="goInit();">초기화</button>
						</div>
						<!-- //btn-wrap -->
						<!-- 목록&페이징// -->
						<div id="divDetailLogList" style="margin-top:50px;"></div>
						<!-- //목록&페이징 -->
					</fieldset>
				</form>
			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>
	<!-- 캠페인선택 팝업// -->
	<%@ include file="/WEB-INF/jsp/sms/ana/pop/pop_campaign.jsp" %>
	<!-- //캠페인선택 팝업 -->
	
	<!--  SMS 정보 팝업// -->
	<%@ include file="/WEB-INF/jsp/sms/ana/pop/pop_detail_sms.jsp" %>
	<!-- //자동발송 메일 정보 -->
	
	<!-- 문자(SMS) 용보기 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_sms.jsp" %> 
	<!-- //문자(SMS) 용보기 팝업 -->
	
</body>
</html>
