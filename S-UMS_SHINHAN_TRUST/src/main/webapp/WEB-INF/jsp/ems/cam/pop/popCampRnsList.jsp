<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.01.03
	*	설명 : 상세로그 내부 이메일서비스의 캠페인과 실시간 서비스목록
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div class="cont">
	<form id="popCampRnsSearchForm" name="popCampRnsSearchForm" method="post">
		<input type="hidden" name="page" value="${searchVO.page}">
		<input type="hidden" name="searchServiceGb" value="${searchVO.searchServiceGb}">
		<fieldset>
			<legend>캠페인/실시간서비스 선택</legend>

			<!-- 검색// -->
			<div class="graybox" style="margin-top:0;">
				<div class="title-area">
					<h3 class="h3-title">검색</h3>
				</div>
				
				<div class="list-area type-short">
					<ul>
						<li>
							<label>등록일시</label>
							<div class="list-item">
								<!-- datepickerrange// -->
								<div class="datepickerrange fromDate">
									<label>
										<fmt:parseDate var="startDt" value="${searchVO.searchStartDt}" pattern="yyyyMMdd"/>
										<fmt:formatDate var="searchStartDt" value="${startDt}" pattern="yyyy.MM.dd"/> 
										<input type="text" name="searchStartDt" value="<c:out value='${searchStartDt}'/>" readonly>
									</label>
								</div>
								<span class="hyppen date"></span>
								<div class="datepickerrange toDate">
									<label>
										<fmt:parseDate var="endDt" value="${searchVO.searchEndDt}" pattern="yyyyMMdd"/>
										<fmt:formatDate var="searchEndDt" value="${endDt}" pattern="yyyy.MM.dd"/> 
										<input type="text" name="searchEndDt" value="<c:out value='${searchEndDt}'/>" readonly>
									</label>
								</div>
								<!-- //datepickerrange -->
							</div>
						</li>
						<li>
							<label>캠페인명</label>
							<div class="list-item">
								<input type="text" name="searchCampNm" value="<c:out value='${searchVO.searchCampNm}'/>" placeholder="캠페인명을 입력해주세요.">
							</div>
						</li>
					</ul>

				</div>
			</div>
			<!-- //검색 -->

			<!-- btn-wrap// -->
			<div class="btn-wrap mgt10 tar">
				<button type="button" class="btn fullblue" onclick="goPopCampRnsSearch('1');">검색</button>
			</div>
			<!-- //btn-wrap -->

			<!-- 목록// -->
			<div class="graybox">
				<div class="title-area">
					<h3 class="h3-title">목록</h3>
					<span class="total">Total: <em><c:out value="${pageUtil.totalRow}"/></em></span>
				</div>

				<div class="grid-area">
					<table class="grid">
						<caption>그리드 정보</caption>
						<colgroup>
							<col style="width:5%;">
							<col style="width:15%;">
							<col style="width:auto">
							<col style="width:20%;">
							<col style="width:20%;">
						</colgroup>
						<thead>
							<tr>
								<th scope="col">NO</th>
								<th scope="col">메일유형</th>
								<th scope="col">캠페인명</th>
								<th scope="col">캠페인유형</th>
								<th scope="col">등록일자</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${fn:length(campList) > 0}">
								<!-- 데이터가 있을 경우// -->
								<c:forEach items="${campList}" var="camp" varStatus="campStatus">
									<tr>
										<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - campStatus.index}"/></td>
										<td>
											<c:if test="${'10' eq camp.serviceGb}">
												대용량메일
											</c:if>
											<c:if test="${'20' eq camp.serviceGb}">
												실시간메일
											</c:if>
										</td>
										<td>
											<c:if test="${'000' eq camp.status}">
												<a href="javascript:setPopCampRnsInfo('<c:out value="${camp.campNo}"/>','<c:out value="${camp.campNm}"/>','<c:out value="${camp.serviceGb}"/>');" class="bold"><c:out value="${camp.campNm}"/></a>
											</c:if>
											<c:if test="${'000' ne camp.status}">
												<c:out value="${camp.campNm}"/>
											</c:if>
										</td>
										<td><c:out value="${camp.campTyNm}"/></td>
										<td>
											<fmt:parseDate var="regDate" value="${camp.regDt}" pattern="yyyyMMddHHmmss"/>
											<fmt:formatDate var="regDt" value="${regDate}" pattern="yyyy-MM-dd"/>
											<c:out value='${regDt}'/>
										</td>
									</tr>
								</c:forEach>
								<!-- //데이터가 있을 경우 -->
							</c:if>
							<c:if test="${empty campList}">
								<!-- 데이터가 없을 경우// -->
								<tr>
									<td colspan="5" class="no_data">등록된 내용이 없습니다.</td>
								</tr>
								<!-- //데이터가 없을 경우 -->
							</c:if>
						</tbody>
					</table>
				</div>
			</div>
			<!-- //목록 -->
		
			<!-- 페이징// -->
			<div class="paging">
				${pageUtil.pageHtml}
			</div>
			<!-- //페이징 -->

		</fieldset>
	</form>
</div>
<script type="text/javascript">
//시작일.
$('.datepickerrange.fromDate input').datepicker({
	dateFormat: 'yy.mm.dd', //날짜 포맷이다.
	closeText: '닫기', // 닫기 버튼 텍스트 변경
	currentText: '오늘', // 오늘 텍스트 변경
	changeMonth: true,
	changeYear: true,
	monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],	//한글 캘린더중 월 표시를 위한 부분
	monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],	//한글 캘린더 중 월 표시를 위한 부분
	dayNames: ['일', '월', '화', '수', '목', '금', '토'],	//한글 캘린더 요일 표시 부분
	dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],	//한글 요일 표시 부분
	dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],	// 한글 요일 표시 부분
	showMonthAfterYear: true,	// true : 년 월	false : 월 년 순으로 보여줌
	yearSuffix: '년',
	showButtonPanel: true,	// 오늘로 가는 버튼과 달력 닫기 버튼 보기 옵션

	onClose: function( selectedDate ) {    
		// 시작일(fromDate) datepicker가 닫힐때
		// 종료일(toDate)의 선택할수있는 최소 날짜(minDate)를 선택한 시작일로 지정
		$(".datepickerrange.toDate input").datepicker( "option", "minDate", selectedDate );
	}  
});


	//종료일
	$('.datepickerrange.toDate input').datepicker({
		dateFormat: 'yy.mm.dd', //날짜 포맷이다.
		closeText: '닫기', // 닫기 버튼 텍스트 변경
		currentText: '오늘', // 오늘 텍스트 변경
		changeMonth: true,
		changeYear: true,
		monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],	//한글 캘린더중 월 표시를 위한 부분
		monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],	//한글 캘린더 중 월 표시를 위한 부분
		dayNames: ['일', '월', '화', '수', '목', '금', '토'],	//한글 캘린더 요일 표시 부분
		dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],	//한글 요일 표시 부분
		dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],	// 한글 요일 표시 부분
		showMonthAfterYear: true,	// true : 년 월	false : 월 년 순으로 보여줌
		yearSuffix: '년',
		showButtonPanel: true,	// 오늘로 가는 버튼과 달력 닫기 버튼 보기 옵션

		onClose: function( selectedDate ) {
			// 종료일(toDate) datepicker가 닫힐때
			// 시작일(fromDate)의 선택할수있는 최대 날짜(maxDate)를 선택한 종료일로 지정 
			$(".datepickerrange.fromDate input").datepicker( "option", "maxDate", selectedDate );
		}                
	});
</script>
