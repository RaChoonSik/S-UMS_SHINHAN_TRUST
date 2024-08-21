<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.31
	*	설명 : 조직신규등롱
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/acc/orgAdd.js'/>"></script>

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
					<h2>조직도 신규등록</h2>
				</div>

				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->

			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
				<form id="orgInfoForm" name="orgInfoForm" method="post">
					<input type="hidden" name="upOrgCd" id="upOrgCd" value="">  
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
										<label class="required">부서코드</label>
										<div class="list-item">
											<input type="text"  id="addOrgCd" name="addOrgCd" placeholder="부서코드는 자동 발번됩니다." disabled="disabled">
										</div>
									</li>
									<li>
										<label class="required">부서명</label>
										<div class="list-item">
											<input type="text"  id="orgNm" name="orgNm" placeholder="부서명을 입력해주세요.">
										</div>
									</li>
									<li>
										<label>상위부서명</label>
										<div class="list-item">
											<input type="text" id="orgKorNm" name="orgKorNm" placeholder="부서를 검색해주세요." style="width:calc(100% - 52px)" value="">
											<input type="hidden" id="orgCd" name="orgCd" value="">
											<button type="button" class="btn fullred" onclick="popOrgSearch();">검색</button>
										</div>
									</li>
								</ul>
							</div> 
						</div>
						<!-- //내용 --> 
						<!-- 부서검색// -->
						<%@ include file="/WEB-INF/jsp/inc/pop/pop_org_search.jsp" %>
						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullred" onclick="goAdd();">등록</button>
							<button type="button" class="btn big" onclick="goCancel();">취소</button>
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
