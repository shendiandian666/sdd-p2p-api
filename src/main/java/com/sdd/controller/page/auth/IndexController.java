package com.sdd.controller.page.auth;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.github.pagehelper.PageInfo;
import com.sdd.controller.WebController;
import com.sdd.service.AccountInfoService;
import com.sdd.service.AccountService;
import com.sdd.service.InvestorService;
import com.sdd.service.PostOrderService;
import com.sdd.util.Tools;

@Controller("authIndexController")
@RequestMapping("/page/auth")
public class IndexController extends WebController {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AccountInfoService accountInfoService;
	
	@Autowired
	private InvestorService investorService;
	
	@RequestMapping(value = "/index")
	public String index(Map<String, Object> map) throws Exception {
		Map<String, Object> params = getParameters();
		map.put("account",accountService.getAccount(params));
		map.put("accountInfo",accountInfoService.getAccountInfo(params));
		map.put("url", getRequest().getRequestURI());
		return "/account/account";
	}
	
	@Autowired
	private PostOrderService postOrderService;
	
	@RequestMapping(value = "/account_order")
	public String accountOrder(Map<String, Object> map) throws Exception {
		Map<String, Object> params = getParameters();
		Map<String, Object> sumOrder = postOrderService.sumInvestAndRebate(params);
		map.put("sumOrder", sumOrder);
		List<Map<String, Object>> list = postOrderService.listService(getPageNum(), getPageSize(), params);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
		map.put("pageInfo", pageInfo);
		map.put("postOrderList", list);
		map.put("invest_date", params.get("invest_date"));
		map.put("received_date", params.get("received_date"));
		return "/account/account_order";
	}
	
	@RequestMapping(value = "/account_order/detail/{id}")
	public String orderDetail(@PathVariable("id")String id, Map<String, Object> map) throws Exception {
		Map<String, Object> params = getParameters();
		params.put("id", id);
		Map<String, Object> orderDetail = postOrderService.getDetail(params);
		map.put("orderDetail", orderDetail);
		return "/account/account_order_detail";
	}
	
	@RequestMapping(value = "/account_investor")
	public String accountInvestor(Map<String, Object> map) throws Exception {
		Map<String, Object> params = getParameters();
		List<Map<String, Object>> list = investorService.investorList(params);
		map.put("investorList", list);
		return "/account/account_investor";
	}
	
	@RequestMapping(value = "/account_del_investor")
	public String accountDelInvestor(Map<String, Object> map) throws Exception {
		//Map<String, Object> params = getParameters();
		//investorService.delInvestor(investor);
		return "redirect:/page/member_center/account_investor";
	}
	
	
	@RequestMapping(value = "/account_record")
	public String accountRecord(Map<String, Object> map) throws Exception {
		Map<String, Object> params = getParameters();
		List<Map<String, Object>> list = accountService.balanceRecord(getPageNum(), getPageSize(),params);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
		map.put("pageInfo", pageInfo);
		map.put("recordList", list);
		return "/account/account_record";
	}
	
	@RequestMapping(value = "/account_setting")
	public String accountSetting(Map<String, Object> map) throws Exception {
		Map<String, Object> params = getParameters();
		Map<String, Object> accountInfo = accountInfoService.getAccountInfo(params);
		map.put("accountInfo", accountInfo);
		map.put("url", getRequest().getRequestURI());
		return "/account/account_setting";
	}
	
	@RequestMapping(value = "/account_withdraw")
	public String accountWithdraw(Map<String, Object> map) throws Exception {
		Map<String, Object> params = getParameters();
		String account = Tools.getMapString(params, "account");
		Map<String, Object> userInfo = accountService.getUserInfo(account);
		map.put("userInfo", userInfo);
		return "/account/account_withdraw";
	}
	
}
