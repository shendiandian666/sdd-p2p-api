package com.sdd.controller.api;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.sdd.model.PageData;

public class ApiController {

	@SuppressWarnings("unchecked")
	public Map<String, Object> getParamters(){
		PageData pd = new PageData(this.getRequest());
		//将验证用户名放入参数(放入request中,因为本程序页面和API在一起，session改变会影响页面取值)
		/*Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		pd.put("account", auth.getName());*/
		String account = getRequest().getAttribute("account") == null ? "" : getRequest().getAttribute("account").toString();
		System.out.println("API user:" + account);
		pd.put("account", account);
		return pd;
	}
	
	public int getPageNum(){
		Object obj = getParamters().get("pageNum");
		if(obj == null)
			return 1;
		if(obj.toString().equals(""))
			return 1;
		return Integer.valueOf(obj.toString());
	}
	
	public int getPageSize(){
		Object obj = getParamters().get("pageSize");
		if(obj == null)
			return 15;
		if(obj.toString().equals(""))
			return 15;
		return Integer.valueOf(obj.toString());
	}
	
	public String getString(String str){
		Object obj = getParamters().get(str);
		if(obj == null)
			return "";
		return obj.toString();
	}
	
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}
	
}
