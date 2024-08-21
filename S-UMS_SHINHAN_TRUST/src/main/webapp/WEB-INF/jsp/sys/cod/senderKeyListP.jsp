<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2023.04.17
	*	설명 : 발송키  관리 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/cod/senderKeyListP.js'/>"></script>

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
					<h2>발송키 관리</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body commoncode">

				<form id="senderKeyInfoForm" name="senderKeyInfoForm" method="post">
					<fieldset>
						<legend> 목록</legend>
						<!-- 목록 -->
						<div class="graybox-wrap">
							<div id="divSenderKeyList"></div>
							<!-- //목록 -->
							<!-- 신규등록, 정보수정 데이터 입력 목록// -->
								<div class="graybox col-single">
									<div class="title-area">
										<h3 class="h3-title">데이터 입력</h3>
	
										<!-- 버튼 영역// -->
										<div class="btn-wrap">
											<button type="button" id="btnAddCode" class="btn fullred plus" onclick="goAdd();">신규등록</button>
											<!-- <button type="button" class="btn fullred">수정</button> -->
											<button type="button" id="btnUpdateCode" class="btn" onclick="goUpdate();">저장</button>
										</div>
										<!-- //버튼 영역 -->
									</div>
									
									<div class="list-area">
										<ul>
											<li>
												<label>발송키</label>
												<div class="list-item">
													<input type="text" id="senderKeyId" name="senderKeyId" placeholder="발송키아이디는 자동 발번 됩니다" disabled >
												</div>
											</li>
											<li> 
												<label class="required">발송키</label>
												<div class="list-item">
													<input type="text" id="senderKey" name="senderKey" placeholder="발송키를 입력하여주세요" maxlength="100">
												</div>	
											</li>
											<li>
												<label class="required">사용여부</label>
												<div class="list-item">
													<div class="select">
														<select id="useYn" name="useYn" title="사용여부 선택">
															<option value="Y"> 예 </option>
															<option value="N"> 아니오 </option>
														</select>
													</div>
												</div>
											</li>
										</ul>
	
										<ul style="margin-top:20px;">
											<li>
												<label>수정일시</label>
												<div class="list-item">
													<p class="inline-txt" id="upDt" ></p>
												</div>
											</li>
											<li>
												<label>수정자</label>
												<div class="list-item">
													<p class="inline-txt" id="upNm"></p>
												</div>
											</li>
											<li>
												<label>등록일시</label>
												<div class="list-item">
													<p class="inline-txt" id="regDt"></p>
												</div>
											</li>
											<li>
												<label>등록자</label>
												<div class="list-item">
													<p class="inline-txt" id="regNm"></p>
												</div> 
											</li>
										</ul>
									</div>
								</div>
							<!-- //신규등록, 정보수정 데이터 입력 목록 -->
						</div>
					</fieldset>
				</form>
			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>

</body>
</html>
				
 