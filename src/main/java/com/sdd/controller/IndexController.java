package com.sdd.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.sdd.cache.SmsCache;
import com.sdd.model.SmsEntity;
import com.sdd.service.AccountService;
import com.sdd.service.SmsSendService;
import com.sdd.util.JSONResult;
import com.sdd.util.Tools;

@Controller
public class IndexController extends WebController {

	@Autowired
	DefaultKaptcha defaultKaptcha;
	
	@RequestMapping("/app")
	public String app(){
		return "/app";
	}

	@RequestMapping("/defaultKaptcha")
	public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws Exception {
		Properties properties = new Properties();
		//是否有边框
		properties.setProperty("kaptcha.border", "no");  
		//边框颜色
        //properties.setProperty("kaptcha.border.color", "105,179,90");  
		//验证码文本字符颜色
        //properties.setProperty("kaptcha.textproducer.font.color", "black");  
        properties.setProperty("kaptcha.image.width", "100");  
        properties.setProperty("kaptcha.image.height", "30");  
        //验证码文本字符长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");  
        //properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");  
        //properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.WaterRipple"); 
		//Config config = new Config(properties);
		//defaultKaptcha.setConfig(config);
		
		byte[] captchaChallengeAsJpeg = null;
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		try {
			// 生产验证码字符串并保存到session中
			String createText = defaultKaptcha.createText();
			httpServletRequest.getSession().setAttribute("vrifyCode", createText);
			// 使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
			BufferedImage challenge = defaultKaptcha.createImage(createText);
			ImageIO.write(challenge, "jpg", jpegOutputStream);
		} catch (IllegalArgumentException e) {
			httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		// 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
		captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
		httpServletResponse.setHeader("Cache-Control", "no-store");
		httpServletResponse.setHeader("Pragma", "no-cache");
		httpServletResponse.setDateHeader("Expires", 0);
		httpServletResponse.setContentType("image/jpeg");
		ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
		responseOutputStream.write(captchaChallengeAsJpeg);
		responseOutputStream.flush();
		responseOutputStream.close();
	}
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private SmsSendService smsSendService;
	@Autowired
	private SmsCache smsCache;
	
	@RequestMapping("/register_send_code")
	@ResponseBody
	public String registerSendCode(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, HttpSession httpSession) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		String captchaId = (String) httpServletRequest.getSession().getAttribute("vrifyCode");
		String parameter = httpServletRequest.getParameter("vrifyCode");
		if (!captchaId.equals(parameter)) {
			return JSONResult.fillResultString("1000", "验证码错误!", null);
		} else {
			int count = accountService.isExists(httpServletRequest.getParameter("mobile"));
			if(count > 0){
				return JSONResult.fillResultString("1210", "该手机号已注册,请直接登录!", null);
			}else{
				String mobile = httpServletRequest.getParameter("mobile");
				int number = getRandNum(1, 999999);
				SmsEntity smsEntity = smsCache.getSms(mobile + "register");
				if(smsEntity != null){
					long begin = smsEntity.getTime().getTime();
					long now = new Date().getTime();
					int r = (int) ((now - begin)/1000 >= 60 ? 0 : (now -begin)/1000);
					if(r != 0){
						return JSONResult.fillResultString("1211", (60-r) + "秒可再次注册!", null);
					}
				}
				smsSendService.sendSms(mobile, number);
				//放入缓存
				SmsEntity entity = new SmsEntity();
				entity.setMobile(mobile);
				entity.setTime(new Date());
				entity.setCaptcha(number+"");
				smsCache.putSms(mobile + "register", entity);
				result.put("mobileEncrypt", mobile);
				result.put("vrifyCode", parameter);
			}
		}
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, result);
	}

	public int getRandNum(int min, int max) {
		int randNum = min + (int) (Math.random() * ((max - min) + 1));
		return randNum;
	}
	
	@RequestMapping("/register")
	@ResponseBody
	public String register(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws Exception {
		Map<String, Object> params = getParameters();
		String mobile = Tools.getMapString(params, "mobile");
		//缓存中获取验证码
		SmsEntity smsEntity = smsCache.getSms(mobile + "register");
		if(smsEntity != null){
			String mobileCode = Tools.getMapString(params, "mobile_code");
			if (smsEntity.getCaptcha().equals(mobileCode)) {
				accountService.addService(params);
			} else {
				List<Map<String, Object>> fields = new ArrayList<>();
				Map<String, Object> mobileMap = new HashMap<>();
				mobileMap.put("field", "mobile_code");
				mobileMap.put("message", "验证码错误!");
				fields.add(mobileMap);
				return JSONResult.fillResultString("1000", "验证码错误", fields);
			}
			return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, null);
		}
		return JSONResult.fillResultString("1000", "验证码错误!", null);
	}
	
}
