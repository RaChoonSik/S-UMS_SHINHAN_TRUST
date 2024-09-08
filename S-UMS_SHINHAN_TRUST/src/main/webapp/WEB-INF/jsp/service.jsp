<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.05
	*	설명 : 서비스 선택화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="util" class="com.mp.util.InitApp" scope="application" />
<jsp:useBean id="resp" class="com.mp.util.RespLog" scope="application" />
<%@ include file="/WEB-INF/jsp/inc/header.jsp" %>


<script type="text/javascript">

$(document).ready(function() {

	if ( ${NEO_LINK} > 0 ){
		var checkboxes = document.getElementsByName('chkLink');
    	for(var i = 0; i<checkboxes.length;i++){  
    		 if(  ${NEO_LINK} == $(checkboxes[i]).val()){
    			 $(checkboxes[i]).prop("checked", true);
    		 }
		} 
	} 
	 
	$('input[type=checkbox]').click(function() {
		var serviceGb = $(this).val()
		if($(this).is(":checked") == true){
			var checkboxes = document.getElementsByName('chkLink');
	    	for(var i = 0; i<checkboxes.length;i++){ 
	    		console.log($(checkboxes[i]).val());
	    		 if( serviceGb != $(checkboxes[i]).val()){
	    			 $(checkboxes[i]).prop("checked", false);
	    		 }
			}    
		} else {
			serviceGb = 0; 
		}
		makeLink(serviceGb);
	   
	});
});

function goEMS(payYn) {
	if(payYn == 1) { 
		document.location.href = "<c:url value='/ems/index.ums'/>";
	} else {
		alert("라이선스키가 유효하지 않습니다");
	}
}

function goRNS(payYn) {
	if(payYn == 1) {
		document.location.href = "<c:url value='/rns/index.ums'/>";
	}else {
		alert("라이선스키가 유효하지 않습니다");
	}
}

function goSMS(payYn) {
	if(payYn == 1) {		
		document.location.href = "<c:url value='/sms/index.ums'/>";
	}else {
		alert("라이선스키가 유효하지 않습니다");
	}
}

function goPUSH(payYn) {
	if(payYn == 1) {		
		document.location.href = "<c:url value='/push/index.ums'/>";
	}else {
		alert("라이선스키가 유효하지 않습니다");
	}
}

function goSYS(useSys) { 
	if (useSys == "N"){
		alert("관리자 기능 사용 권한이 없습니다");
		return;		
	} else {
		document.location.href = "<c:url value='/sys/index.ums'/>";
	}
}

function goLogOut() {
	document.location.href = "<c:url value='/lgn/logout.ums'/>";
}

function makeLink(serviceGb){
	$.post("/sys/acc/makeLink.json?progId=" + serviceGb, function(data) {
		if(data.result =="Fail") {
		 	alert("바로 가기 등록에 실패했습니다 잠시 후 다시 이용해주세요");
		}
	});  	
}
</script>

<body>
	<div id="wrap">

		<!-- service// -->
		<div id="service">
			<section class="service-inner">
				<h1 class="logo-box">
					<img src="../img/common/logo.png" alt="ums로고">
					<span>
						Unified Messaging System
						통합 메시징 시스템
					</span>
				</h1>
				 
				<div class="list-area">
					<ul class="service-list"> 
						<%-- <li <c:if test="${userServiceList[0].useYn eq 0}"> class="disabled" </c:if> > --%>
						<li <c:if test="${NEO_USE_EMS eq 'N'}"> class="disabled" </c:if> >
							<a href="javascript:goEMS('<c:out value='${userServiceList[0].payYn}'/>');">
								<strong>S-EMS</strong>
								<p>
									Email Marketing System<br>
									통합 이메일 발송 시스템
								</p>
							</a>
							<c:if test="${userServiceList[0].useYn eq 1}">
								<label for="chk_01"><input type="checkbox" id="chk_01" name="chkLink" value="<c:out value='${userServiceList[0].serviceGb}'/>"><span>S-EMS 바로가기 유지</span></label>
							</c:if>
						</li>
	
						<%-- <li <c:if test="${userServiceList[1].useYn eq 0}"> class="disabled" </c:if> > --%>
						<li <c:if test="${NEO_USE_RNS eq 'N'}"> style="display:none;" </c:if> >
							<a href="javascript:goRNS('<c:out value='${userServiceList[1].payYn}'/>');">
								<strong>S-RNS</strong>
								<p>
									Real-time Notification Service<br>
									실시간 자동응답 서비스
								</p>
							</a>
							<c:if test="${userServiceList[1].useYn eq 1}">
							<label for="chk_02"><input type="checkbox" id="chk_02" name="chkLink" value="<c:out value='${userServiceList[1].serviceGb}'/>"><span>S-RNS 바로가기 유지</span></label>
							</c:if>
						</li>
	
						<%-- <li <c:if test="${userServiceList[2].useYn eq 0}"> class="disabled" </c:if> > --%>
						<li <c:if test="${NEO_USE_SMS eq 'N'}"> class="disabled" </c:if> >
							<a href="javascript:goSMS('<c:out value='${userServiceList[2].payYn}'/>');">
								<strong>S-SMS</strong>
								<p>SMS 문자메시지 발송 시스템</p>
							</a>
							<c:if test="${userServiceList[2].useYn eq 1}">
							<label for="chk_03"><input type="checkbox" id="chk_03" name="chkLink" value="<c:out value='${userServiceList[2].serviceGb}'/>"><span>S-SMS 바로가기 유지</span></label>
							</c:if>
						</li>
	
						<%-- <li <c:if test="${userServiceList[3].useYn eq 0}"> class="disabled" </c:if> > --%>
						<li <c:if test="${NEO_USE_PUSH eq 'N'}"> style="display:none;" </c:if> >
							<a href="javascript:goPUSH('<c:out value='${userServiceList[3].payYn}'/>');">
								<strong>S-PUSH</strong>
								<p>모바일 푸시 알림 발송 시스템</p>
							</a>
							<c:if test="${userServiceList[3].useYn eq 1}">
							<label for="chk_04"><input type="checkbox" id="chk_04" name="chkLink" value="<c:out value='${userServiceList[3].serviceGb}'/>"><span>S-PUSH 바로가기 유지</span></label>
							</c:if>							
						</li>
					</ul>
 
					<!-- 로그아웃 클릭시 로그인 페이지 이동// -->
					<a href="javascript:goLogOut();" class="svcbtn btn-logout">로그아웃</a>
					<a href="javascript:goSYS('${NEO_USE_SYS}');" class="svcbtn btn-setting">공통설정</a>
					
				</div>
			</section>

			 <footer class="ums-footer">
				<p>copyright © Sict All Rights Reserved.</p> 
			</footer>
		</div>
		<!-- //service -->

	</div>

</body>
</html>
