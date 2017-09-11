package com.sdd.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageHelper;
import com.sdd.dao.DaoSupport;
import com.sdd.model.User;
import com.sdd.util.JSONResult;
import com.sdd.util.MD5;
import com.sdd.util.Tools;

@Service("accountService")
public class AccountService {

	@Autowired
	private AccountInfoService accountInfoService;
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	public int isExists(String mobile) throws Exception {
		int count = (int) dao.findForObject("com.sdd.mapper.AccountMapper.isExists", mobile);
		return count;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getUserInfo(String account) throws Exception {
		return (Map<String, Object>) dao.findForObject("com.sdd.mapper.AccountMapper.getUserInfo", account);
	}
	
	public User getAccount(Map<String, Object> params) throws Exception {
		String account = Tools.getMapString(params, "account");
		return (User) dao.findForObject("com.sdd.mapper.AccountMapper.getAccount", account);
	}
	
	public String updatePassword(Map<String, Object> params) throws Exception {
		String password = Tools.getMapString(params, "password");
		String newPassword = Tools.getMapString(params, "new_password");
		String newPasswordConfirm = Tools.getMapString(params, "new_password_confirm");
		//获取登录用户信息作比较
		User account = getAccount(params);
		String passwd = Tools.getStringValue(account.getPasswd());
		if(MD5.md5(password).equals(passwd)){
			if(newPassword.equals(newPasswordConfirm)){
				//设置加密后的密码
				params.put("new_password", MD5.md5(newPassword));
				dao.update("com.sdd.mapper.AccountMapper.updatePassword", params);
			}else{
				return JSONResult.fillResultString("0000", "密码输入不一致!", null);
			}
		}else{
			return JSONResult.fillResultString("0000", "原始密码不正确!", null);
		}
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, null);
	}
	
	public void resetPasswd(Map<String, Object> params) throws Exception {
		dao.update("com.sdd.mapper.AccountMapper.updatePassword", params);
	}
	
	@Autowired
	private AccountService accountService;
	
	@Transactional
	public void addService(Map<String, Object> params) throws Exception{
		String passwd = Tools.getMapString(params, "password");
		params.put("passwd", MD5.md5(passwd));
		int count = dao.save("com.sdd.mapper.AccountMapper.add", params);
		if(count > 0){
			//添加账户基本信息
			accountInfoService.addAccountInfo(params);
			//添加账户变动记录
			Map<String, Object> recordMap = new HashMap<String, Object>();
			recordMap.put("type", "0");
			recordMap.put("remark", "用户注册");
			recordMap.put("account", params.get("mobile"));
			accountService.addRecord(recordMap);
		}
	}
	
	@Autowired
	private SmsSendService smsSendService;
	
	@Transactional
	public void withDraw(Map<String, Object> params) throws Exception {
		//减去余额
		subBalance(params);
		//增加已提现金额
		addRebate(params);
		//余额变动记录
		params.put("type", "3");
		params.put("remark", "余额提现:" + Tools.getMapString(params, "balance"));
		addRecord(params);
		//查询余额
		Map<String, Object> userInfo = getUserInfo(Tools.getMapString(params, "account"));
		//支付宝转账(转账成功发送短信通知)
		smsSendService.sendWithDrawNotice(
				Tools.getMapString(params, "account"), 
				Tools.getMapString(params, "account"), 
				Tools.getMapString(params, "balance"), 
				Tools.getMapString(userInfo, "alipay_account"), 
				Tools.getMapString(userInfo, "balance"));
	}
	
	public Double getBalance(String account) throws Exception {
		return (Double) dao.findForObject("com.sdd.mapper.AccountMapper.getBalance", account);
	}
	
	public void addBalance(Map<String, Object> params) throws Exception {
		dao.update("com.sdd.mapper.AccountMapper.addBalance", params);
	}
	
	public void subBalance(Map<String, Object> params) throws Exception {
		dao.update("com.sdd.mapper.AccountMapper.subBalance", params);
	}
	
	public void addRebate(Map<String, Object> params) throws Exception {
		dao.update("com.sdd.mapper.AccountMapper.addRebate", params);
	}
	
	public void subRebate(Map<String, Object> params) throws Exception {
		dao.update("com.sdd.mapper.AccountMapper.subRebate", params);
	}
	
	public void addRebateLate(Map<String, Object> params) throws Exception {
		dao.update("com.sdd.mapper.AccountMapper.addRebateLate", params);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> balanceRecord(int pageNum, int pageSize, Map<String, Object> params) throws Exception{
		PageHelper.startPage(pageNum, pageSize);
		return (List<Map<String, Object>>) dao.findForList("com.sdd.mapper.AccountMapper.balanceRecord", params);
	}
	
	public int addRecord(Map<String, Object> params) throws Exception{
		return (int) dao.update("com.sdd.mapper.AccountMapper.addRecord", params);
	}
	
}
