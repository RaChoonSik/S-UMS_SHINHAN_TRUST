<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.09.02
	*	설명 : RNS 테스트발송 사용자목록 팝업화면
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
					<div class="title-area">
						<h3 class="h3-title">추가</h3>
					</div>
					
					<div class="list-area">
						<ul>
							<li>
								<label>ID</label>
								<div class="list-item">
									<input type="text" id="rid" name="rid" placeholder="ID를 입력해주세요.">
								</div>
							</li>
							<li>
								<label>수신자</label>
								<div class="list-item">
									<input type="text" id="rname" name="rname" placeholder="수신자를 입력해주세요.">
								</div>
							</li>
							<li>
								<label>이메일</label>
								<div class="list-item">
									<input type="text" id="rmail" name="rmail" placeholder="이메일을 입력해주세요.">
								</div>
							</li>
						</ul>
					</div>
				</div> 

				<!-- 버튼// -->
				<div class="btn-wrap tar" style="margin-top:10px;">
					<button type="button" class="btn fullpurple" onClick="goRnsTestUserAdd();">추가</button>
				</div>
				<!-- //버튼 -->

				<!-- 목록// -->
				<form id="listForm" name="listForm" method="post">
					<input type="hidden" id="tidTest" name="tid" value="0">
					<div class="graybox">
						<div class="title-area">
							<h3 class="h3-title">수신대상자 조회</h3>
	
							<!-- 버튼// -->
							<div class="btn-wrap">
								<button type="button" class="btn" onclick="goRnsTestUserDelete();">삭제</button>
							</div>
							<!-- //버튼 -->
						</div>
						
						<div class="grid-area">
							<table class="grid">
								<caption>그리드 정보</caption>
								<colgroup>
									<col style="width:10%;">
									<col style="width:25%;">
									<col style="width:25%;">
									<col style="width:auto;">
								</colgroup>
								<thead>
									<tr>
										<th scope="col">
											<label><input type="checkbox" name="isUserAll" onclick="goRnsUserAll();"><span></span></label>
										</th>
										<th scope="col">ID</th>
										<th scope="col">수신자</th>
										<th scope="col">이메일</th>
									</tr>
								</thead>
								<tbody id="rnsTestUserList">
									<tr>
										<td colspan="4" class="no_data">등록된 내용이 없습니다.</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</form>
				<!-- //목록 -->

				<!-- btn-wrap// -->
				<div class="btn-wrap">
					<button type="button" class="btn big fullpurple" onclick="goRnsTestMailSend();">테스트발송</button>
					<button type="button" class="btn big" onclick="fn.popupClose('#popup_testsend_user');">취소</button>
				</div>
				<!-- //btn-wrap -->

			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_testsend_user');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
