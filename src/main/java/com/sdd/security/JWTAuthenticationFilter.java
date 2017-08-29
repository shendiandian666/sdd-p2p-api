package com.sdd.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import com.sdd.util.JSONResult;

public class JWTAuthenticationFilter extends GenericFilterBean {

	private RequestMatcher requiresAuthenticationRequestMatcher;

	protected JWTAuthenticationFilter(String defaultFilterProcessesUrl) {
		setFilterProcessesUrl(defaultFilterProcessesUrl);
	}

	protected JWTAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
		this.requiresAuthenticationRequestMatcher = requiresAuthenticationRequestMatcher;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		if (requiresAuthentication(request, response)) {
			Authentication authentication = null;
			try {
				authentication = TokenAuthenticationService.getAuthentication(request, response);
			} catch (Exception e1) {
				response.setContentType("application/json;charset=UTF-8");
				response.setStatus(HttpServletResponse.SC_OK);
				try {
					response.getWriter().println(JSONResult.fillResultString("-1", "身份验证失败!", "身份验证失败!"));
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (authentication == null) {
				response.setContentType("application/json;charset=UTF-8");
				response.setStatus(HttpServletResponse.SC_OK);
				try {
					response.getWriter().println(JSONResult.fillResultString("-1", "身份验证失败!", "身份验证失败!"));
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		chain.doFilter(request, response);
	}

	public void setFilterProcessesUrl(String filterProcessesUrl) {
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(filterProcessesUrl));
	}

	public final void setRequiresAuthenticationRequestMatcher(RequestMatcher requestMatcher) {
		Assert.notNull(requestMatcher, "requestMatcher cannot be null");
		this.requiresAuthenticationRequestMatcher = requestMatcher;
	}

	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		return requiresAuthenticationRequestMatcher.matches(request);
	}

}
