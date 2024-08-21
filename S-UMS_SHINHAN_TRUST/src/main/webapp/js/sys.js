/**
* --------------------------------
* SYS JS
* creator : chowoobin
* from : sict
* --------------------------------
*/
$(window).on('load', function(){

    //발송결재라인 등록 팝업 - 사용자명 폰트색상
	$(document).on("click", ".popsendenroll .user button", function(){
		if($(this).closest("li").hasClass("active")){
			$(this).closest("li").removeClass("active");
		}else{
			$(this).closest("li").addClass("active").siblings().removeClass("active");
		}
	});

    
    // popup :: 추출조건
    $('.valueenroll').hide();
    $('.popextractcondition .graybox .plus').on('click',function(){
        $(this).parents('.graybox').hide();
        $('.valueenroll').show();
    });

    $('.valueenroll button:nth-child(2)').on('click',function(){
        $(this).parents('.valueenroll').hide();
        $(this).parents('.valueenroll').siblings('.graybox').show();
    });

    // 메타정보관리 - 메타테이블 폰트색상
    $(document).on("click", ".grid-btn .grid td", function(){
		if($(this).hasClass("active")){
			$(this).removeClass("active");
		}else{
			$(this).addClass("active").closest("tr").siblings("tr").find("td").removeClass("active");
		}
	});

});
