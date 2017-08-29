
$.ajaxSetup({cache:false, dataType:'json'});

$.ajaxPrefilter(function(options, originalOptions, xhr){
	if (!options.crossDomain) {
		var token = $.cookie('loginToken');
		if (token) xhr.setRequestHeader('X-Login-Token', token);
	}
});

$(document).ajaxError(function(event, xhr, ajaxSettings, thrownError){
	if (xhr.status >= 500 && xhr.status < 600) {
		layer.msg('500: 服务器程序错误');
	}
	else if (xhr.status == 403) {
		layer.msg('403: 禁止访问');
	}
	else if (xhr.status == 404) {
		layer.msg('404: 请求的资源不存在');
	}
});


$.formErrorsRender = function(el, errors) {
	if (errors) {
		for (var i in errors) {
			if (errors[i].field == '__all__') {
				layer.msg('错误：' + errors[i].message);
			} else {
				layer.tips(errors[i].message, el+' [name=' + errors[i].field + ']',{tipsMore: true});
			}
		}
	}
};

$.formPost = function (formEl, api, postData, callback) {
	$.post(api, postData, function (data) {
		if (data.status == -1) {
			layer.msg('验证：身份验证失败,请重新登录!');
			return;
		}
		if (data.status == -2) {
			$.formErrorsRender(formEl, data.result);
			return;
		}
		if (data.status != 200) {
			layer.msg(data.message);
			return;
		}
		if (typeof callback == 'function') callback(data);
	});
};

$.form = function(el, api, callback) {
	$(el).submit(function () {
		$.formPost(el, api, $(this).serializeArray(), callback);
        return false;
    });
};


$.sendMobilePopup = null;
$.sendMobileLoadCaptcha = function () {
	$.get('/api/captcha/new.json', function(r){
		$('#send-mobile-code-popup-img img').attr('src', r.image);
		$('input[name=captcha_data]', $.sendMobilePopup).val(r.key);
		$('input[name=captcha]', $.sendMobilePopup).val('');
	});
};
$.sendMobile = function (mobileInputEl, type) {
	var mobileInput = $(mobileInputEl);
	var mobile = mobileInput.val();
	if (!mobile || mobile.length != 11) {
		layer.msg('请填写正确的手机号');
		mobileInput.focus();
	} else {
		if ($.sendMobilePopup == null) {
			$.sendMobilePopup = $('#send-mobile-code-popup');
			$('#send-mobile-code-popup-img').click(function () {
				$.sendMobileLoadCaptcha();
				return false;
			});
			$.form('#send-mobile-code-popup form', '/api/mobile/send_code', function (data) {
				layer.msg('已经发送验证码到:' + data.mobile);
				$.sendMobilePopup.hide();
			});
		}
		$('h1', $.sendMobilePopup).text('发送验证码到:' + mobile);
		$('input[name=mobile]', $.sendMobilePopup).val(mobile);
		$('input[name=type]', $.sendMobilePopup).val(type);
		$.sendMobileLoadCaptcha();
		$.sendMobilePopup.show();
	}
	return false;
};
