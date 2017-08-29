package com.sdd.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.sdd.util.JSONResult;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	public JWTLoginFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		// JSON反序列化成 AccountCredentials(这个只能接收JSON参数)
		//AccountCredentials creds = new ObjectMapper().readValue(req.getInputStream(), AccountCredentials.class);
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
			response.setContentType("application/json;charset=UTF-8");
	        response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(JSONResult.fillResultString("-1", "登录失败!", "身份验证失败!"));
			return null;
		}
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, password);
		// 返回一个验证令牌
		return getAuthenticationManager().authenticate(auth);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		SecurityContextHolder.getContext().setAuthentication(auth);
		HttpSession session = req.getSession();  
	    //在session中存放security context,方便同一个session中控制用户的其他操作  
	    session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
		TokenAuthenticationService.addAuthentication(req, res, auth.getName());
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		//failed.getMessage();
		
		List<Map<String, Object>> fields = new ArrayList<>();
		Map<String, Object> mobileMap = new HashMap<>();
		mobileMap.put("field", "username");
		mobileMap.put("message", "登录失败!");
		fields.add(mobileMap);
		
		response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println(JSONResult.fillResultString("-1", "登录失败!", fields));
    }

}
