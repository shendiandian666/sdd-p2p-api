package com.sdd.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.sdd.util.MD5;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 关闭CSRF验证
		http.csrf().disable()
				// 对请求进行认证
				.authorizeRequests()
				// 所有 / 的所有请求 都放行
				.antMatchers("/").permitAll()
				.antMatchers("/404").permitAll()
				// 所有 /login 的POST请求 都放行
				.antMatchers("/login").permitAll()
				.antMatchers("/web").permitAll()
				.antMatchers("/app").permitAll()
				.antMatchers("/index").permitAll()
				.antMatchers("/first").permitAll()
				.antMatchers("/first/*").permitAll()
				.antMatchers("/repetition").permitAll()
				.antMatchers("/repetition/*").permitAll()
				.antMatchers("/activity").permitAll()
				.antMatchers("/car").permitAll()
				.antMatchers("/car/detail").permitAll()
				.antMatchers("/down").permitAll()
				.antMatchers("/announcement").permitAll()
				.antMatchers("/about").permitAll()
				.antMatchers("/service").permitAll()
				.antMatchers("/help").permitAll()
				.antMatchers("/agreement").permitAll()
				.antMatchers("/appDownload").permitAll()
				.antMatchers("/*/news/**").permitAll()
				.antMatchers("/*/getActivity").permitAll()
				.antMatchers("/defaultKaptcha").permitAll()
				.antMatchers("/msgSend").permitAll()
				.antMatchers("/register").permitAll()
				.antMatchers("/register_send_code").permitAll()
				.antMatchers("/css/**").permitAll()
				.antMatchers("/images/**").permitAll()
				.antMatchers("/js/**").permitAll()
				.antMatchers("/layui2/**").permitAll()
				.antMatchers("/upload/**").permitAll()
				.antMatchers("/page/others/**").permitAll()
				.antMatchers("/api/**").permitAll()
				// 所有请求需要身份认证
				.anyRequest().authenticated()
				//.and().exceptionHandling().accessDeniedHandler(AccessDeniedHandlerImpl)
				.and()
				.logout().logoutSuccessUrl("/index").and()
				// We filter the api/login requests
				// 添加一个过滤器 所有访问 /login 的请求交给 JWTLoginFilter 来处理 这个类处理所有的JWT相关内容
				.addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
						UsernamePasswordAuthenticationFilter.class)
				// And filter other requests to check the presence of JWT in
				// header
				// 添加一个过滤器验证其他请求的Token是否合法
				.addFilterBefore(new JWTAuthenticationFilter("/api/auth/**"),
						UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// 使用自定义身份验证组件
		auth.userDetailsService(customUserService()).passwordEncoder(new org.springframework.security.crypto.password.PasswordEncoder() {
			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				return encodedPassword.equals(MD5.md5(rawPassword.toString()));
			}
			@Override
			public String encode(CharSequence rawPassword) {
				return MD5.md5(rawPassword.toString());
			}
		});
		//auth.authenticationProvider(new CustomAuthenticationProvider());
	}
	
	@Bean
    UserDetailsService customUserService(){ //注册UserDetailsService 的bean
        return new CustomUserService();
    }
	
}
