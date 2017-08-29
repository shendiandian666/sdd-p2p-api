package com.sdd.controller.api.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdd.cache.SmsCache;
import com.sdd.controller.api.ApiController;
import com.sdd.model.SmsEntity;
import com.sdd.service.AccountInfoService;
import com.sdd.service.AccountService;
import com.sdd.util.JSONResult;
import com.sdd.util.Tools;

@RestController("authAccountController")
@RequestMapping("/api/auth/account")
public class AccountController extends ApiController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private AccountInfoService accountInfoService;

	@RequestMapping(value = "/getUserInfo")
	public String getUserInfo() throws Exception {
		String account = Tools.getMapString(getParamters(), "account");
		Map<String, Object> userInfo = accountService.getUserInfo(account);
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, userInfo);
	}
	
	@RequestMapping(value = "/update_nickName")
	public String updateNickName() throws Exception {
		accountInfoService.updateNickName(getParamters());
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, null);
	}

	@RequestMapping(value = "/update_qq")
	public String updateQQ() throws Exception {
		accountInfoService.updateQQ(getParamters());
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, null);
	}

	@RequestMapping(value = "/update_mail")
	public String updateMail() throws Exception {
		accountInfoService.updateMail(getParamters());
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, null);
	}

	@Autowired
	private SmsCache smsCache;
	
	@RequestMapping(value = "/update_alipay")
	public String updateAlipay(HttpSession session) throws Exception {
		Map<String, Object> params = getParamters();
		String mobile = Tools.getMapString(params, "mobile");
		String account = Tools.getMapString(params, "account");
		if(!mobile.equals(account)){
			List<Map<String, Object>> fields = new ArrayList<>();
			Map<String, Object> mobileMap = new HashMap<>();
			mobileMap.put("field", "mobile");
			mobileMap.put("message", "手机号不匹配!");
			fields.add(mobileMap);
			return JSONResult.fillResultString("1000", "手机号不匹配", fields);
		}else{
			//缓存中获取验证码
			SmsEntity smsEntity = smsCache.getSms(mobile + "alipay");
			String mobileCode = Tools.getMapString(params, "mobile_code");
			if(smsEntity != null){
				if (smsEntity.getCaptcha().equals(mobileCode)) {
					accountInfoService.updateAlipay(params);
				} else {
					List<Map<String, Object>> fields = new ArrayList<>();
					Map<String, Object> mobileMap = new HashMap<>();
					mobileMap.put("field", "mobile_code");
					mobileMap.put("message", "验证码错误!");
					fields.add(mobileMap);
					return JSONResult.fillResultString("1000", "验证码错误", fields);
				}
			} else {
				return JSONResult.fillResultString("1000", "验证码错误!", null);
			}
		}
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, null);
	}

	@RequestMapping(value = "/update_password")
	public String updatePassword() throws Exception {
		return accountService.updatePassword(getParamters());
	}
	
	@RequestMapping(value = "/withdraw")
	public String withdraw() throws Exception {
		Map<String, Object> params = getParamters();
		String mobile = Tools.getMapString(params, "mobile");
		String account = Tools.getMapString(params, "account");
		if(!mobile.equals(account)){
			return JSONResult.fillResultString("1000", "手机号不匹配!", null);
		}else{
			//缓存中获取验证码
			SmsEntity smsEntity = smsCache.getSms(mobile + "withdraw");
			String mobileCode = Tools.getMapString(params, "mobile_code");
			if(smsEntity != null){
				if (smsEntity.getCaptcha().equals(mobileCode)) {
					//支付宝提现alipay_account,校验余额
					//查询余额
					double balance = accountService.getBalance(Tools.getMapString(params, "account"));
					double money = Double.valueOf(Tools.getMapString(params, "money"));
					if(money > balance){
						return JSONResult.fillResultString("1000", "余额不足!", null);
					}
					params.put("balance", money);
					params.put("rebate", money);
					accountService.withDraw(params);
					//支付宝转账
				} else {
					return JSONResult.fillResultString("1000", "验证码错误!", null);
				}
			} else {
				return JSONResult.fillResultString("1000", "验证码错误!", null);
			}
		}
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, null);
	}
	
}
