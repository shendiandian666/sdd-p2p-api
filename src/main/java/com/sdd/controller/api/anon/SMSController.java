package com.sdd.controller.api.anon;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.sdd.cache.SmsCache;
import com.sdd.controller.api.ApiController;
import com.sdd.model.SmsEntity;
import com.sdd.service.SmsSendService;
import com.sdd.util.JSONResult;

@RestController("anonSMS")
@RequestMapping("/api/anon/sms")
public class SMSController extends ApiController {

	@Autowired
	private SmsCache smsCache;
	@Autowired
	private SmsSendService smsSendService;
	
	@RequestMapping("/resendMsg")
	@ResponseBody
	public String resendMsg(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, HttpSession httpSession) throws Exception {
		String mobile = httpServletRequest.getParameter("mobile");
		int number = getRandNum(1, 999999);
		SmsEntity smsEntity = smsCache.getSms(mobile + "register");
		if(smsEntity != null){
			long begin = smsEntity.getTime().getTime();
			long now = new Date().getTime();
			int r = (int) ((now - begin)/1000 >= 60 ? 0 : (now -begin)/1000);
			if(r != 0){
				return JSONResult.fillResultString("1211", (60-r) + "秒可再次发送!", null);
			}
		}
		smsSendService.sendSms(mobile, number);
		//放入缓存
		SmsEntity entity = new SmsEntity();
		entity.setMobile(mobile);
		entity.setTime(new Date());
		entity.setCaptcha(number+"");
		smsCache.putSms(mobile + "register", entity);
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, null);
	}
	
	@RequestMapping("/send_forget_passwd")
	@ResponseBody
	public String sendForgetPasswd(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, HttpSession httpSession) throws Exception {
		String mobile = httpServletRequest.getParameter("mobile");
		int number = getRandNum(1, 999999);
		SmsEntity smsEntity = smsCache.getSms(mobile + "forgetpasswd");
		if(smsEntity != null){
			long begin = smsEntity.getTime().getTime();
			long now = new Date().getTime();
			int r = (int) ((now - begin)/1000 >= 60 ? 0 : (now -begin)/1000);
			if(r != 0){
				return JSONResult.fillResultString("1211", (60-r) + "秒可再次发送!", null);
			}
		}
		smsSendService.sendSms(mobile, number);
		//放入缓存
		SmsEntity entity = new SmsEntity();
		entity.setMobile(mobile);
		entity.setTime(new Date());
		entity.setCaptcha(number+"");
		smsCache.putSms(mobile + "forgetpasswd", entity);
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, null);
	}
	
	public int getRandNum(int min, int max) {
		int randNum = min + (int) (Math.random() * ((max - min) + 1));
		return randNum;
	}
	
}
