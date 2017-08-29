package com.sdd.security;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.sdd.util.JSONResult;
import static java.util.Collections.emptyList;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenAuthenticationService {

	static final long EXPIRATIONTIME = 7_200_000; //
	//static final long EXPIRATIONTIME = 300_000;
	static final String SECRET = "ThisIsASecret";
	static final String TOKEN_PREFIX = "Bearer";
	static final String HEADER_STRING = "Authorization";

	static void addAuthentication(HttpServletRequest req, HttpServletResponse res, String username) {
		String JWT = Jwts.builder()
				// 保存权限（角色）
				.claim("authorities", "ROLE_ADMIN,AUTH_WRITE")
				// 用户名写入标题
				.setSubject(username)
				// 有效期设置
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				// 签名设置
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		//token放入session方便页面读取，如果是单独的API系统则不需要
		req.getSession().setAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		req.getSession().setAttribute("token", JWT);
		req.getSession().setAttribute("username", username);
		StringBuilder sb = new StringBuilder(username);
		req.getSession().setAttribute("safename", sb.replace(3, 7, "****").toString());
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
		// 将 JWT 写入 body
		try {
			res.setContentType("application/json");
			res.setStatus(HttpServletResponse.SC_OK);
			res.getOutputStream().println(JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, JWT));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");
		if (!StringUtils.isEmpty(token)) {
			String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
					.getSubject();
			System.out.println("tokenuser:"+user);
			Authentication auth = user != null ? new UsernamePasswordAuthenticationToken(user, null, emptyList()) : null;
			//只在API调用时使用,(如果API调用改变session，会导致页面session取不到值)
			request.setAttribute("account", auth.getName());
			//SecurityContextHolder.getContext().setAuthentication(auth);
			//HttpSession session = request.getSession();  
		    //session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
			return auth;
		}
		System.out.println("auth failed:" + token);
		return null;
	}

}
