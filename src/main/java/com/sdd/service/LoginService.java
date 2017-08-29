package com.sdd.service;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.sdd.dao.DaoSupport;
import com.sdd.model.User;

@Service("loginService")
public class LoginService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	public User loginService(String account) throws Exception {
		return (User) dao.findForObject("com.sdd.mapper.AccountMapper.getAccount", account);
	}
	
	public int loginCount(String account) throws Exception {
		return (int) dao.update("com.sdd.mapper.AccountMapper.loginCount", account);
	}
}
