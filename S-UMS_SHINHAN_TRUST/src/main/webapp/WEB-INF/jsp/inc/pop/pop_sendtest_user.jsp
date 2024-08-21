<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.23
	*	설명 : 테스트발송 사용자목록 팝업화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_testsend_user" class="poplayer poptestsend">
	<div class="inner">
		<header>
			<h2>테스트메일 발송</h2>
		</header>
		<div class="popcont">
			<div class="cont">
					<div class="graybox">
						<form id="testUserForm" name="testUserForm" method="post">
							<input type="hidden" id="testTaskNos1" name="taskNos" value="0">
							<input type="hidden" id="testSubTaskNo1" name="subTaskNos" value="0">
							<div class="title-area">
								<h3 class="h3-title">추가</h3>
							</div>
							
							<div class="list-area">
								<ul>
									<li>
										<label>수신자</label>
										<div class="list-item">
											<input type="text" id="testUserNm" name="testUserNm" placeholder="수신자를 입력해주세요.">
										</div>
									</li>
									<li>
										<label>이메일</label>
										<div class="list-item">
											<input type="text" id="testUserEm" name="testUserEm" placeholder="이메일을 입력해주세요.">
										</div>
									</li>
								</ul>
							</div>
						</form>
					</div>

				<!-- 버튼// -->
				<div class="btn-wrap tar" style="margin-top:10px;">
					<button type="button" class="btn fullblue" onClick="goTestUserAdd();">추가</button>
				</div>
				<!-- //버튼 -->

				<!-- 목록// -->
				<form id="listForm" name="listForm" method="post">
				<input type="hidden" id="testTaskNos2" name="taskNos" value="0">
				<input type="hidden" id="testSubTaskNo2" name="subTaskNos" value="0">
				<div id="divTestUserList" style="margin-top:30px;"></div>
				</form>
				<!-- //목록 -->

				<!-- btn-wrap// -->
				<div class="btn-wrap">
					<button type="button" class="btn big fullblue" onclick="goTestMailSend();">테스트발송</button>
					<button type="button" class="btn big" onclick="goTestSendCancel();">취소</button>
				</div>
				<!-- //btn-wrap -->
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_testsend_user');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
