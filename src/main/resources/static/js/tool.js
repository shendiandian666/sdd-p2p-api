// banner
if ((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)) {
} else {
    window.addEventListener("load", function () {
        var innerbn = document.getElementById("bnActive");
        innerbn != null ? innerbn.classList.add("active") : 0;
    });
}

$(function () {
    // 浮动导航
    (function () {
        var headerWrap = $("#headerWrap");
        var headerWrapHeight = headerWrap.height();
        if (headerWrap.length != 0) {
            var headerWrapH = headerWrap.offset().top,
                stopProdp = $("#stop_homeNav");
            if (stopProdp.length != 0) {
                var stopProdpH = stopProdp.offset().top;
            }
            //滚动条事件
            $(window).scroll(function () {
                var scroH = $(this).scrollTop();
                if (scroH > 44) {
                    $('body').css('padding-top', headerWrapHeight);
                    headerWrap.addClass("Topheader");
                    if (scroH >= stopProdpH) {
                        headerWrap.removeClass("Topheader");
                        $('body').css('padding-top', 0);
                    }
                } else {
                    $('body').css('padding-top', 0);
                    headerWrap.removeClass("Topheader");
                }
            });
        }
        var $homeNav = $("#homeNav");
        if (typeof $.fn.onePageNav == "function") {
            $homeNav.onePageNav();
        }
    })();

    // qq客服
    $('.qq_box').hover(function () {
        $(this).children('.sec_qq').stop().fadeIn();
    }, function () {
        $(this).children('.sec_qq').stop().fadeOut();
    });

    // flash
    if ($().slide) {
        $(".banner").slide({
            mainCell: ".bd",
            titCell: ".hd",
            effect: "leftLoop",
            vis: "auto",
            autoPage: true,
            autoPlay: true,
            pnLoop: true,
            delayTime: 500,
            interTime: 5000
        });
        $(".ann_scroll").slide({
            mainCell: ".bd",
            effect: "left",
            autoPage: true,
            autoPlay: true,
            pnLoop: false,
            vis: 1,
            interTime: 5000
        });
        $(".studen_scroll").slide({
            titCell: ".hd",
            mainCell: ".bd",
            effect: "leftLoop",
            autoPage: true,
            autoPlay: true,
            pnLoop: false,
            vis: 1,
            scroll: 1,
            interTime: 5000
        });
    }

    // ftool
    (function () {
        var ftoolTop = $("#ftoolTop");
        ftoolTop.click(function (e) {
            e.preventDefault();
            $("html,body").animate({scrollTop: 0}, 500);
        });
        $(window).on("DOMContentLoaded scroll resize", function () {
            $(this).scrollTop() == 0 ? ftoolTop.hide() : ftoolTop.show();
        });
        $(".ftool_item").mouseenter(function () {
            $(this).addClass("ftool_hover");
            $(this).find(".ftool_code").css({"display": "block"}).stop(true, true).animate({
                "opacity": 1,
                "right": "50px"
            }, 500);
        }).mouseleave(function () {
            $(this).removeClass("ftool_hover");
            $(this).find(".ftool_code").css({"display": "none"}).animate({"opacity": 0, "right": "100px"}, 500);
        });
    })();

    // 下拉选择
    (function () {
        $(".divselect").click(function () {
            $(this).toggleClass("divselect_open");
        });
        $(".divselect li").click(function () {
            $(this).closest(".divselect").find("cite").text($(this).text());
        });
        // 点击其它地方搜索消失
        $(window).on("click", function (e) {
            if ($(e.target).parents(".divselect").length == 0) {
                $(".divselect").removeClass("divselect_open");
            } else {
                e.stopPropagation();
            }
        });
    })();

    // 标签导航
    $(".tab_nav dd").each(function () {
        $(this).children('a:first').addClass('tab_light');
    });
    $(".tab_box > div").hide();
    $(".tab_box").each(function (index, element) {
        $(this).children("div:first").show();
    });
    $('.tab_nav dd a').click(function () {
        $(this).addClass('tab_light').siblings().removeClass('tab_light');
        $(this).parents(".h_tab").children(".tab_box").children("div").hide().eq($(this).index()).fadeIn();
    });

    // 二级
    $(".tab_c_nav li").each(function () {
        $(this).children('a:first').addClass('tab_light');
    });
    $(".tab_c_box > div").hide();
    $(".tab_c_box").each(function (index, element) {
        $(this).children("div:first").show();
    });
    $('.tab_c_nav li a').click(function () {
        $(this).addClass('tab_light').siblings().removeClass('tab_light');
        $(".tab_c_box > div").hide().eq($('.tab_c_nav li a').index(this)).fadeIn();
    });
});
