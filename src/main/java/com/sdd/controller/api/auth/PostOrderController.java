package com.sdd.controller.api.auth;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sdd.controller.api.ApiController;
import com.sdd.service.ConfigService;
import com.sdd.service.PostOrderService;
import com.sdd.util.JSONResult;

@RestController("authPostOrder")
@RequestMapping("/api/auth/order")
public class PostOrderController extends ApiController {

	@Autowired
	private PostOrderService postOrderService;

	@Autowired
	private ConfigService configService;
	
	@RequestMapping(value = "/add_order")
	public String addOrder() throws Exception {
		//是否系暂停
		String reson = configService.getStatus("post_order");
		if(!"".equals(reson)){
			return JSONResult.fillResultString("1000", reson, Collections.emptyMap());
		}
		String message = postOrderService.addOrder(getParamters());
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, message);
	}
	
	@RequestMapping(value = "/order_list")
	public String orderList() throws Exception {
		Map<String, Object> parameters = getParamters();
		List<Map<String, Object>> result = postOrderService.listService(getPageNum(), getPageSize(), parameters);
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, result);
	}
	
	@RequestMapping(value = "/order_detail")
	public String orderDetail() throws Exception {
		Map<String, Object> params = getParamters();
		Map<String, Object> result = postOrderService.getDetail(params);
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, result);
	}

}
