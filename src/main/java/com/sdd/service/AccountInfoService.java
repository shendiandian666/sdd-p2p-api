package com.sdd.service;

import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.sdd.dao.DaoSupport;

@Service("accountInfoService")
public class AccountInfoService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	public void addAccountInfo(Map<String, Object> params) throws Exception{
		dao.save("com.sdd.mapper.AccountInfoMapper.add", params);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getAccountInfo(Map<String, Object> params) throws Exception {
		return (Map<String, Object>) dao
				.findForObject("com.sdd.mapper.AccountInfoMapper.getAccountInfo", params);
	}
	
	public int updateNickName(Map<String, Object> params) throws Exception {
		return (int) dao.update("com.sdd.mapper.AccountInfoMapper.updateNickName", params);
	}
	
	public int updateQQ(Map<String, Object> params) throws Exception {
		return (int) dao.update("com.sdd.mapper.AccountInfoMapper.updateQQ", params);
	}
	
	public int updateMail(Map<String, Object> params) throws Exception {
		return (int) dao.update("com.sdd.mapper.AccountInfoMapper.updateMail", params);
	}
	
	public int updateAlipay(Map<String, Object> params) throws Exception {
		return (int) dao.update("com.sdd.mapper.AccountInfoMapper.updateAlipay", params);
	}
	
	
}
