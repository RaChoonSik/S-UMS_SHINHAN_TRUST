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
    
    var encUserId = CryptoJS.AES.encrypt($("#pUserId").val(), "!END#ERSUMS");
    var encUserPwd = CryptoJS.AES.encrypt($("#pUserPwd").val(), "!END#ERSUMS");

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
                    $("#loginForm").submit();
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
        var encUserId = CryptoJS.AES.encrypt($("#needUserId").val(), "!END#ERSUMS");
        var encUserPwd = CryptoJS.AES.encrypt($("#popUserEditPwd").val(), "!END#ERSUMS");
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

</script>

<body>
    <div id="wrap">

        <!-- login// -->
        <div id="login">
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
</body>
</html>
