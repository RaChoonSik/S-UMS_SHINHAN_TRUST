<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.31
	*	설명 : 조직도 정보수정
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp"%>

<script type="text/javascript"
	src="<c:url value='/js/sys/acc/orgUpdate.js'/>"></script>

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
					<h2>조직도 정보수정</h2>
				</div>

				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp"%>
				<!-- //공통 표시부 -->

			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
				<form id="orgInfoForm" name="orgInfoForm" method="post">
					<input type="hidden" name="page"
						value="<c:out value='${searchVO.page}'/>"> <input
						type="hidden" name="updOrgCd" id="updOrgCd"
						value="<c:out value='${orgInfo.orgCd}'/>"> <input
						type="hidden" name="orgNm" id="orgNm" value=""> <input
						type="hidden" name="orgEngNm" id="orgEngNm" value=""> <input
						type="hidden" name="upOrgCd" id="upOrgCd" value="">
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
									<li><label>부서코드</label>
										<div class="list-item">
											<%-- <input type="text"  disabled="disabled" value="<c:out value='${orgInfo.orgCd}'/>"> --%>
											<p class="inline-txt line-height40">
												<c:out value='${orgInfo.orgCd}' />
											</p>
										</div></li>
									<li><label class="required">부서명</label>
										<div class="list-item">
											<input type="text" id="updOrgNm" name="updOrgNm" placeholder="부서명을 입력해주세요." value="<c:out value='${orgInfo.orgNm}'/>">
										</div></li>
									<li><label>상위부서</label>
										<div class="list-item">
											<input type="text" id="orgKorNm" name="orgKorNm"style="width: calc(100% - 52px)" value="<c:out value='${orgInfo.upOrgNm}'/>" readonly > 
											<input	type="hidden" id="orgCd" name="orgCd"	value="<c:out value='${orgInfo.upOrgCd}'/>">
											<button type="button" class="btn fullred" onclick="popOrgSearch();">검색</button>
										</div></li>
								</ul>
							</div>
						</div>
						<!-- //조건 -->

						<!-- 부서검색// -->
						<%@ include file="/WEB-INF/jsp/inc/pop/pop_org_search.jsp"%>

						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullred"
								onclick="goUpdate();">수정</button>
							<button type="button" class="btn" onclick="goCancel();">취소</button>
						</div>
						<!-- //btn-wrap -->

					</fieldset>
				</form>

			</section>
			<!-- //cont-body -->

		</div>
		<!-- // content -->
	</div>

</body>
</html>
