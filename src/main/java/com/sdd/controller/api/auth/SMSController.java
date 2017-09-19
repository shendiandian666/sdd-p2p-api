package com.sdd.controller.api.auth;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sdd.cache.SmsCache;
import com.sdd.controller.api.ApiController;
import com.sdd.model.SmsEntity;
import com.sdd.service.SmsSendService;
import com.sdd.util.JSONResult;

@RestController("authSMS")
@RequestMapping("/api/auth/sms")
public class SMSController extends ApiController {

	@Autowired
	private SmsCache smsCache;
	@Autowired
	private SmsSendService smsSendService;

	@RequestMapping("/send_alipay_code")
	public String msgSend(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			HttpSession httpSession) throws Exception {
		String mobile = httpServletRequest.getParameter("mobile");
		int number = getRandNum(1, 999999);
		SmsEntity smsEntity = smsCache.getSms(mobile + "alipay");
		if (smsEntity != null) {
			long begin = smsEntity.getTime().getTime();
			long now = new Date().getTime();
			int r = (int) ((now - begin) / 1000 >= 60 ? 0 : (now - begin) / 1000);
			if (r != 0) {
				return JSONResult.fillResultString("1211", (60 - r) + "秒可再次发送!", null);
			}
		}
		smsSendService.sendSms(mobile, number);
		// 放入缓存
		SmsEntity entity = new SmsEntity();
		entity.setMobile(mobile);
		entity.setTime(new Date());
		entity.setCaptcha(number + "");
		smsCache.putSms(mobile + "alipay", entity);

		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, null);
	}
	
	@RequestMapping("/send_withdraw_code")
	public String sendWithDraw(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			HttpSession httpSession) throws Exception {
		String mobile = httpServletRequest.getParameter("mobile");
		int number = getRandNum(1, 999999);
		SmsEntity smsEntity = smsCache.getSms(mobile + "withdraw");
		if (smsEntity != null) {
			long begin = smsEntity.getTime().getTime();
			long now = new Date().getTime();
			int r = (int) ((now - begin) / 1000 >= 60 ? 0 : (now - begin) / 1000);
			if (r != 0) {
				return JSONResult.fillResultString("1211", (60 - r) + "秒可再次发送!", null);
			}
		}
		smsSendService.sendSms(mobile, number);
		// 放入缓存
		SmsEntity entity = new SmsEntity();
		entity.setMobile(mobile);
		entity.setTime(new Date());
		entity.setCaptcha(number + "");
		smsCache.putSms(mobile + "withdraw", entity);

		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, null);
	}

	public int getRandNum(int min, int max) {
		int randNum = min + (int) (Math.random() * ((max - min) + 1));
		return randNum;
	}

	@RequestMapping("/encrypt_code_valid")
	public String register(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Exception {
		String mobile = request.getParameter("mobile");
		String mobileCode = request.getParameter("mobile_code");
		/**
		 * 提供ios审核用start
		if("13298516963".equals(mobile)){
			if(!"8888".equals(mobileCode)){
				return JSONResult.fillResultString("1000", "验证码错误", null);
			}
			return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, null);
		}
		 * 提供ios审核用end
		 */
		// 缓存中获取验证码
		SmsEntity smsEntity = smsCache.getSms(mobile + "alipay");
		if (smsEntity != null) {
			if (!smsEntity.getCaptcha().equals(mobileCode)) {
				return JSONResult.fillResultString("1000", "验证码错误", null);
			}
		} else {
			return JSONResult.fillResultString("1000", "验证码错误", null);
		}
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, null);
	}

}
