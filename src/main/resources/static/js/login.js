$(function() {
			function getCaptcha() {
				$('#register_captcha_input').val('');
				$('#register_captcha_image').attr('src', /*[[@{/defaultKaptcha}]]*/);
				$('#register_captcha_data').val('xxxx');
				/*
				 * $.getJSON('/api/captcha/new.json', function(r) {
				 * $('#register_captcha_image').attr('src', r.image);
				 * $('#register_captcha_data').val(r.key); });
				 */
			}

			$('#register_captcha_btn').click(function() {
				getCaptcha();
				return false;
			});

			$('#register_captcha_input').focus(function() {
				if ($('#register_captcha_data').val() == '') {
					getCaptcha();
				}
			});

			var resendMobileCode = $('#resend-mobile-code');
			var resendMobileCodeData = null;
			var mobileCodeSendWait = 60;

			function mobileCodeWait() {
				if (mobileCodeSendWait > 0) {
					mobileCodeSendWait--;
					resendMobileCode
							.text('重新发送验证码(' + mobileCodeSendWait + ')');
					setTimeout(mobileCodeWait, 1000);
				} else {
					resendMobileCode.text('重新发送验证码');
				}
			}

			resendMobileCode.click(function() {
				if (mobileCodeSendWait == 0) {
					$.post(/*[[@{/msgSend}]]*/, {
						'mobile' : resendMobileCodeData,
						'type' : 10
					}, function() {
						mobileCodeSendWait = 60;
						mobileCodeWait();
					});
				}
				return false;
			});

			$('#register-form').submit(
					function() {
						var mobile = $('#register-popup-mobile').val();
						$.post([[@{/register_send_code}]], $(this)
								.serializeArray(), function(data) {
							if (data.errorCode == 1201) {
								layer.msg('您的手机号已经在本站注册过，请直接登录。');
								$('#register-popup').hide();
								$('#login-popup').show();
								$('#login-popup-mobile').val(mobile);
								return;
							}
							if (data.errorCode != 0) {
								layer.msg(data.errorMessage);
								return;
							}
							layer.msg('手机验证码已发送');
							$('#register-popup').hide();
							$('#register-info-popup').show();
							$('#register-info-popup-mobile').attr('value',
									mobile);
							$('#register-info-popup-mobile-code').focus();
							resendMobileCodeData = data.mobileEncrypt;
							mobileCodeWait();
						});
						return false;
					});

			$.form('#register-info-form', /*[[@{/register}]]*/, function(
					data) {
				layer.msg("注册成功!");
				setTimeout(function() {
					location.reload();
				}, 2000);
			});

			$('#register-agreement').click(
					function() {
						$('#register-submit').attr('disabled',
								!$(this).is(':checked'));
					});

			$('#login-form').submit(
					function() {
						$.post(/* [[@{/login}]] */, $(this).serializeArray(),
								function(data) {
									if (data.errorCode != 0) {
										$.formErrorsRender('#login-form',
												data.errorMessage);
										return;
									}
									location.reload();
								});
						
						return false;
					});
		});