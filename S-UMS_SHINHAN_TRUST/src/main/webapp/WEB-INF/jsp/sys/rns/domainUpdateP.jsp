<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.09.08
	*	설명 : 도메인 수정
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/rns/domainUpdate.js'/>"></script>

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
					<h2>RNS 도메인 정보수정</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body"> 
				<form id="domainInfoForm" name="domainInfoForm" method="post">
					<input type="hidden" name="page" value="<c:out value='${searchVO.page}'/>">
					<input type="hidden" name="domainId" value="<c:out value='${searchVO.domainId}'/>">					 
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
									<li class="col-full">
										<label class="required">도메인명</label>
										<div class="list-item">
											<input type="text" id="domainName" name="domainName"  placeholder="도메인명을 입력해주세요." value="<c:out value='${domainInfo.domainName}'/>">
										</div>	
									</li> 
								</ul>
								<ul>
									<li>
										<label>등록자</label>
										<div class="list-item">
											<p class="inline-txt"><c:out value="${domainInfo.regNm}"/></p>
										</div>
									</li>

									<li>
										<label>수정자</label>
										<div class="list-item">
											<p class="inline-txt"><c:out value="${domainInfo.upNm}"/></p>
										</div>
									</li>

									<li>
										<label>등록일시</label>
										<div class="list-item"> 
											<p class="inline-txt"><c:out value="${domainInfo.regDt}"/></p>
										</div>
									</li>

									<li>
										<label>수정일시</label>
										<div class="list-item">
											<fmt:parseDate var="upDt" value="${domainInfo.upDt}" pattern="yyyyMMddHHmmss"/>
											<fmt:formatDate var="upDt" value="${upDt}" pattern="yyyy.MM.dd HH:mm"/>
											<p class="inline-txt"><c:out value="${upDt}"/></p>
										</div>
									</li>
								</ul>
							</div>
						</div>
						<!-- //조건 --> 

						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullred" onclick="goUpdate();">수정</button>
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
