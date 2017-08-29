package com.sdd.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.sdd.model.User;
import com.sdd.model.UserInfo;
import com.sdd.service.LoginService;

@Service
public class CustomUserService implements UserDetailsService {

	@Autowired
	private LoginService loginService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userInfo = null;
		try {
			userInfo = loginService.loginService(username);
			//StringBuilder sb = new StringBuilder(userInfo.getAccount());
			//userInfo.setAccount(sb.replace(3, 7, "*").toString());
			/*
			 * 这里判断用户的一些状态信息，抛出对应的异常，
			 * 然后在loginfilter的unsuccessfulAuthentication方法中根据异常返回不同的信息
			 * 
			 * 用户名不存在:UsernameNotFoundException; 
			密码错误:BadCredentialException; 
			帐户被锁:LockedException; 
			帐户未启动:DisabledException; 
			密码过期:CredentialExpiredException;等等!*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		return new UserInfo(userInfo.getAccount(), userInfo.getAccountName(), userInfo.getBalance(), userInfo.getPasswd(),authorities);
	}

}
