/**
* --------------------------------
* PUSH JS
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

    //pushinfobox preview btn-toggle
   $(document).on("click", ".pushinfobox .preview .btn-toggle", function() {
       var $obj = $(this).closest(".preview");
       if($obj.hasClass("active")){
           $obj.removeClass("active");
       }else{
           $obj.addClass("active");
       }
   });
    
});
