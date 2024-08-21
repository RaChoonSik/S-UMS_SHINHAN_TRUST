<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.19
	*	설명 : 수신자그룹 미리보기 팝업화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_preview_seg" class="poplayer poppreviewreceiver">
	<div class="inner">
		<header>
			<h2>수신자그룹 미리보기</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<div class="graybox">
					<div class="title-area">
						<h3 class="h3-title">조회</h3>
					</div>
					
					<div class="table-area">
						<table>
							<caption></caption>
							<colgroup>
								<col style="width:130px">
								<col style="width:220px">
								<col style="width:220px">
								<col style="width:auto">
							</colgroup>
							<tbody>
								<tr>
									<th scope="row">수신자그룹</th>
									<td colspan="3" id="previewSegNm"></td>
								</tr>
								<tr>
									<th scope="row">질의문</th>
									<td colspan="3" id="previewSql"></td>
								</tr>
	
								<c:if test="${'002' eq createTy || '003' eq createTy}">
									<!-- 파일연동/직접SQL인 경우 노출// -->
									<tr class="pdtb-small">
										<th scope="row">회원정보</th>
										<td colspan="2">
											파일연동/직접SQL로 등록된 수신자그룹은 일괄조회만 가능합니다.
										</td>
										<td><button type="button" class="btn fullblue" onclick="goSearchReason();">조회</button></td>
									</tr>
									<!-- //파일연동,직접SQL인 경우 노출 -->
								</c:if>
								
								<c:if test="${'000' eq createTy || '004' eq createTy}">
									<!-- 추출조건,리타게팅인 경우 노출// -->
									<tr class="pdtb-small">
										<th scope="row">회원정보</th>
										<td>
											<div class="select">
												<select id="previewSearch" name="search" title="회원정보 선택">
												</select>
											</div>
										</td>
										<td><input type="text" name="value" placeholder="정보입력"></td>
										<td>
											<button type="button" class="btn fullblue" onclick="goSearchReason();">조회</button>
										</td>
									</tr>
									<!-- //추출조건,리타게팅인 경우 노출 -->
								</c:if>
	
							</tbody>
						</table>
					</div>
				</div>
	
				<!-- 목록&페이징// -->
				<div id="previewMemberList" style="margin-top:30px;">
					<div class="graybox">
						<div class="title-area">
							<h3 class="h3-title">목록</h3>
							<span class="total">Total: <em>0</em></span>
						</div>
						
						<div class="grid-area">
							<table class="grid">
								<caption>그리드 정보</caption>
								<tr>
									<td colspan="6" class="no_data">조회 사유를 등록해 주세요.</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
				<!-- //목록&페이징 -->
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_preview_seg');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
