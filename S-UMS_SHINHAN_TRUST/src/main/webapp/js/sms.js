/**
* --------------------------------
* SMS JS
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

    //tooltip
	$(document).on("click", ".tooltip .tooltip-btn", function() {
        var tooltip = $(this).closest(".tooltip");
        if(tooltip.hasClass("active")){
            tooltip.removeClass("active");
        }else{
            tooltip.addClass("active");
        }
    });
    // tooltip mouseover
    $(document).on("mouseover",".validity-area .tooltip .tooltip-btn", function(){
        $(this).closest(".tooltip").addClass("active");
    }).on("mouseout", ".validity-area .tooltip .tooltip-btn", function(){
        $(this).closest(".tooltip").removeClass("active");
    });

});
