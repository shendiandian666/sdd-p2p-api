package com.sdd.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import com.sdd.model.PageData;

public class WebController {

	/** new PageData对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getParameters(){
		PageData pd = new PageData(this.getRequest());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
		pd.put("account", auth.getName());
		if(auth.getPrincipal() instanceof UserDetails){
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			pd.put("user", userDetails);
		}
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}else{
			pd.put("keywords", "");
		}
		return pd;
	}
	
	public int getPageNum(){
		String pageNum = this.getRequest().getParameter("pageNum");
		if(pageNum == null || "".equals(pageNum))
			return 1;
		return Integer.valueOf(pageNum);
	}
	
	public int getPageSize(){
		String pageSize = this.getRequest().getParameter("pageSize");
		if(pageSize == null || "".equals(pageSize))
			return 15;
		return Integer.valueOf(pageSize);
	}
	
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}
	
	public ModelAndView getModelAndView(){
		return new ModelAndView();
	}
	
}
