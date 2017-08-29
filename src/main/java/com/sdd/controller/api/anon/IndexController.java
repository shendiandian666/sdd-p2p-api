package com.sdd.controller.api.anon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.sdd.cache.SmsCache;
import com.sdd.controller.api.ApiController;
import com.sdd.model.SmsEntity;
import com.sdd.service.AccountService;
import com.sdd.util.JSONResult;
import com.sdd.util.MD5;
import com.sdd.util.Tools;

@RestController("anonIndex")
@RequestMapping("/api/anon/index")
public class IndexController extends ApiController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private SmsCache smsCache;
	
	@RequestMapping("/forgetPasswd")
	@ResponseBody
	public String forgetPasswd() throws Exception {
		Map<String, Object> params = getParamters();
		String mobile = Tools.getMapString(params, "mobile");
		String passwd = Tools.getMapString(params, "password");
		params.put("new_password", MD5.md5(passwd));
		//缓存中获取验证码
		SmsEntity smsEntity = smsCache.getSms(mobile + "forgetpasswd");
		if(smsEntity != null){
			String mobileCode = Tools.getMapString(params, "mobile_code");
			if (smsEntity.getCaptcha().equals(mobileCode)) {
				params.put("account", mobile);
				accountService.resetPasswd(params);
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
