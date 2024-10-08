<%--
    /**********************************************************
    *    작성자 : 김상진
    *    작성일시 : 2021.08.05
    *    설명 : 로그인 아이디 비밀번호 입력 화면
    **********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header.jsp" %>

<script type="text/javascript" src="<c:url value='/js/aes.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/sha256.js'/>"></script>

<script type="text/javascript">
$(document).ready(function() {
    
    $("#pUserId").focus();
    $("#pUserId").keypress(function(e) {
        if(e.which == 13) {
            e.preventDefault();
            if($("#pUserId").val() == "") {
                alert("아이디를 입력해주세요.");
            } else {
                $("#pUserPwd").focus();
            }
        }
    });
    
    $("#pUserPwd").keypress(function(e) {
        if(e.which == 13) {
            goLogin();
        }
    });
    
    $("#lgnBtn").click(function(e) {
        goLogin();
    });
    
 
    //initLogin();
});

function initLogin(){
    var needChange = $("#needChange").val();
    if(needChange == "E1"){
        alert("IAM인증에 실패하였습니다.관리자에게 문의해주세요.");
    }
    else if(needChange == "E2"){
        alert("UMS사용자 인증에 실패하였습니다.관리자에게 문의해주세요.");
    }
}

function goLogin() {
    if($("#pUserId").val() == "") {
        alert("아이디를 입력해주세요.");
        $("#pUserId").focus();
        return;
    } 
    if($("#pUserPwd").val() == "") {
        alert("비밀번호를 입력해주세요.");
        $("#pUserPwd").focus();
        return;
    }
    
    const target = document.getElementById('btnLogin');
    target.disabled = true; 
    
    var encUserId = CryptoJS.AES.encrypt($("#pUserId").val(), $("#_keyString_").val());
    var encUserPwd = CryptoJS.AES.encrypt($("#pUserPwd").val(), $("#_keyString_").val());

    $("#eUserId").val(encUserId);
    $("#eUserPwd").val(encUserPwd);
    
    var param = $("#loginForm").serialize();
    
    $.ajax({
        type : "POST",
        url : "/lgn/checkInitPwd.json?" + param,
        dataType : "json",
        success : function(data){
            if(data.result == "Success") {
                if(data.needChange == "Y") {
                    $("#needChange").val("Y");
                    $("#needUserId").val($("#pUserId").val());
                    fn.popupOpen("#popup_user_editpassword");
                    target.disabled = false;
                } else {
                	
                	//인증번호 전송되며 팝업이 뜸
                	
                	if ("ADMIN" == $("#pUserId").val()) {
                		$("#loginForm").submit();
                		return false;
                	}
                	
                	//2차인증 팝업
                	fn.popupOpen("#popup_user_two_factor");
                	
                	if (data.userTel != '') {
                		$("#popUserTelTxt").parent().removeClass("hidden");
	                	$("#popUserTelTxt").text(data.userTel);
	                	
                		$("#popTwoFactorTime").removeClass("hidden");
	                	updateTimer();
                	}else {
                		alert("회원정보에 전화번호가 등록되지 않았습니다. 관리자에게 문의하시기 바랍니다.");
                	}
                	//sms 체크
//                     $("#loginForm").submit();
                }
            } else {
                alert("아이디와 비밀번호를 확인해주세요");
                target.disabled = false;
            }
        },
        error: function (e) {
            alert("로그인 처리에 오류가 발생했습니다");
            target.disabled = false;
        }
    });
}

function popSaveInitPasswordChange(){
    
    if( popCheckInitPasswordChange()) {  
        var encUserId = CryptoJS.AES.encrypt($("#needUserId").val(), $("#_keyString_").val());
        var encUserPwd = CryptoJS.AES.encrypt($("#popUserEditPwd").val(), $("#_keyString_").val());
        var pwInitYn = $("#needChange").val();
        
        $("#popUserEditPwdUserId").val(encUserId);
        $("#popUserEditPwdPassword").val(encUserPwd);
        $("#popUserEditPwdPasswordInit").val(pwInitYn);
        
        var param = $("#popUserInitPwdForm").serialize();
        console.log(param);
        
        $.ajax({
            type : "POST",
            url : "/sys/acc/userUpdateInitPassword.json?" + param,
            dataType : "json",
            success : function(data){
                if(data.result == "Success") {
                    alert(data.message); 
                    $("#needUserId").val(""); 
                    $("#pUserPwd").val("");
                    fn.popupClose('#popup_user_editpassword'); 
                } else {
                    alert(data.message);
                    $("#popUserEditPwd").focus();
                    $("#popUserEditPwd").select();
                }
            },
            error: function (e) {
                alert("비밀번호 변경 처리에 오류가 발생했습니다");
            }
        });
    }
}
function popCloseUserTwoFactor(){
	$("#twoFactorCode").val("");
	$("#twoFactorCodeTxt").val("");
	fn.popupClose('#popup_user_two_factor');
	stopTimer();
}

//2차인증
function popUserTwoFactorConfirm() {
	
	var code = $("#twoFactorCodeTxt").val();
	
	if (code == "") {
		alert("인증번호를 입력해주세요");
		return false;
	}
	
	$("#twoFactorCode").val(code);
	var param = $("#loginForm").serialize();
	
	$.ajax({
        type : "POST",
        url : "/lgn/checkTwoFactor.json?" + param,
        dataType : "json",
        success : function(data){
            if(data.result == "Success") {
               	//sms 체크
                $("#loginForm").submit();
            } else {
//                 alert("인증번호를 다시 확인하시기 바랍니다.");
                alert(data.message);
            }
        },
        error: function (e) {
            alert("로그인 처리에 오류가 발생했습니다");
        }
    });
	
}

//인증번호 재전송
function popUserTwoFactorReSend() {
	stopTimer();
	
	var param = $("#loginForm").serialize();
	  
	  $.ajax({
	      type : "POST",
	      url : "/lgn/reSendTwofactor.json?" + param,
	      dataType : "json",
	      success : function(data){
	          if(data.result == "Success") {
	        	  alert("인증번호가 재발송 되었습니다.");
	        	  updateTimer();
	          }
	      },
	      error: function (e) {
	          alert("로그인 처리에 오류가 발생했습니다");
	          target.disabled = false;
	      }
	  });
	
	
}

const totalTime = 5 * 60; // 5분(300초)
let remainingTime = totalTime;
let timerId = null; // setTimeout의 ID 저장
function updateTimer() {
	//
	const minutes = Math.floor(remainingTime / 60);
	const seconds = remainingTime % 60;

	// 2자리 숫자 형식으로 초 출력
	const timeDisplay = minutes+":"+(seconds < 10 ? '0' + seconds : seconds);
  
	$("#popTwoFactorTimerDisplay").text(timeDisplay);

	remainingTime--;

	// 남은 시간이 0보다 크면 1초 후에 updateTimer를 다시 호출
	if (remainingTime >= 0) {
		timerId = setTimeout(updateTimer, 1000); // 1초 후에 다시 실행
	} else {
		console.log('타이머 종료!');
	}
}

function stopTimer() {
	  if (timerId !== null) {
	    clearTimeout(timerId); // setTimeout 중지
	    remainingTime = totalTime;
	  }
	}

</script>

<body>
    <div id="wrap">

        <!-- login// -->
        <div id="login">
        	<input type="hidden" id="_keyString_" value="!END#ERSUMS">
            <form id="loginForm" name="loginForm" action="<c:url value='/lgn/lgn.ums'/>" method="post">
                <fieldset>
                    <legend>로그인</legend>
                    <section class="login-inner">
                        <h1><img src="<c:url value='/img/common/logo_white.png'/>" class="logo" alt="S-UMS"></h1>
                        <div class="form-box">
                            <input type="text" id="pUserId" placeholder="아이디">
                            <input type="password" id="pUserPwd" placeholder="비밀번호">
                            <input type="hidden" id="eUserId" name="pUserId" placeholder="아이디">
                            <input type="hidden" id="eUserPwd" name="pUserPwd" placeholder="비밀번호">
                            <input type="hidden" id="twoFactorCode" name="twoFactorCode" placeholder="비밀번호">
                            <i class="fa fa-eye fa-lg"></i>
                            <div class="error">                             
    <c:if test="${'N' eq result}">
                                    <p>*  입력된 아이디, 비밀번호가 일치하지 않습니다. 다시 입력해주세요.</p>    
    </c:if>
    <c:if test="${'E' eq result}">
        <c:choose>
            <c:when test="${'E1' eq needChange}">
                                    <p>* IAM인증에 실패하였습니다.관리자에게 문의해주세요.</p>
            </c:when>
            <c:when test="${'E2' eq needChange}">
                                    <p>* UMS사용자 인증에 실패하였습니다.관리자에게 문의해주세요.</p>
            </c:when>
            <c:otherwise>
                                    <p>* 유효하지 않은 사용자입니다. 관리자에게 문의해주세요.</p>
            </c:otherwise>
        </c:choose>
    </c:if>
                            </div>
                            <a href="javascript:goLogin();" class="btn fullblue login" id="btnLogin">로그인</a>
                            <p class="gray_txt">* 비밀번호 변경 등 문의사항은 서비스 관리자에게 문의바랍니다.</p>
                        </div>
                    </section>
                </fieldset>
            </form>
        </div>
        <!-- // login -->

    </div>
    <%@ include file="/WEB-INF/jsp/inc/pop/pop_user_password.jsp" %>
    <%@ include file="/WEB-INF/jsp/inc/pop/pop_user_two_factor.jsp" %>
</body>
</html>
