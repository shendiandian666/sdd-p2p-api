package com.sdd.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import net.sf.json.JSONObject;

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
	
	@Autowired
	private AlipayToAccount alipayToAccount;
	
	@Autowired
	private AlipayTransferService alipayTransferService;
	
	@Transactional
	public void withDraw(Map<String, Object> params) throws Exception {
		//减去余额
		subBalance(params);
		//增加已提现金额
		addRebate(params);
		String amount = Tools.getMapString(params, "balance");
		//余额变动记录
		params.put("type", "3");
		DecimalFormat df = new DecimalFormat("#.00");
		params.put("remark", "余额提现:" + df.format(amount));
		addRecord(params);
		//查询余额
		String account = Tools.getMapString(params, "account");
		Map<String, Object> userInfo = getUserInfo(account);
		String alipayAccount = Tools.getMapString(userInfo, "alipay_account");
		String bizNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		//支付宝转账
		JSONObject response = alipayToAccount.transfer(bizNo, alipayAccount, amount);
		//记录支付宝转账记录及返回结果
		Map<String, Object> transferMap = new HashMap<String, Object>();
		transferMap.put("biz_no", bizNo);
		transferMap.put("payee_type", "ALIPAY_LOGONID");
		transferMap.put("payee_account", alipayAccount);
		transferMap.put("amount", amount);
		transferMap.put("remark", "余额提现");
		transferMap.put("success", response.getString("success"));
		transferMap.put("orderId", response.getString("orderId"));
		transferMap.put("code", response.getString("code"));
		transferMap.put("msg", response.getString("msg"));
		transferMap.put("subCode", response.getString("subCode"));
		transferMap.put("subMsg", response.getString("subMsg"));
		int count = alipayTransferService.addTransfer(transferMap);
		if(count == 1){
			//支付宝转账(转账成功发送短信通知)
			smsSendService.sendWithDrawNotice(account, amount);
		}
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
