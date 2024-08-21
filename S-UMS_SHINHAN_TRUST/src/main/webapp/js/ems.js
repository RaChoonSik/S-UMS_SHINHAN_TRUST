/**
* --------------------------------
* EMS JS
* creator : chowoobin
* from : sict
* --------------------------------
*/

$(window).on('load', function(){
    
    //datebannerswiper
    var $datebannerswiperObj = $(".datebannerswiper");
    if ( $datebannerswiperObj.find(".swiper-slide").length > 1 ){
        var datebannerswiper = new Swiper(".datebannerswiper", {
            slidesPerView: "1",
            navigation: {
                nextEl: ".swiper-button-next-date",
                prevEl: ".swiper-button-prev-date",
            },
            observer: true,
            observeParents: true,
        });
    }

	//발송결재라인 등록 팝업 - 사용자명 폰트색상
	$(document).on("click", ".popsendenroll .user button", function(){
		if($(this).closest("li").hasClass("active")){
			$(this).closest("li").removeClass("active");
		}else{
			$(this).closest("li").addClass("active").siblings().removeClass("active");
		}
	});

    //popsendenroll
	$(document).on("click", ".popsendenroll .grid .sortable tr", function(){
		if(!$(this).hasClass("selected")){
			$(this).addClass("selected").siblings().removeClass("selected");
		}
	});
	$(document).on("click", function(event){
		if ($(event.target).closest(".popsendenroll .grid .sortable tr.selected").length === 0){
			$(".popsendenroll .grid .sortable tr").removeClass("selected");
		}
		event.stopPropagation();
	});
    
});
