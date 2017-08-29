package com.sdd.controller.api.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sdd.controller.api.ApiController;
import com.sdd.model.Investor;
import com.sdd.service.InvestorService;
import com.sdd.util.JSONResult;
import com.sdd.util.Tools;

@RestController("authInvestor")
@RequestMapping("/api/auth/investor")
public class InvestorController extends ApiController {

	@Autowired
	private InvestorService investorService;
	
	/**
	 * 获取投资人列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/get_investor_list")
	public String getInvestorList() throws Exception {
		List<Map<String, Object>> list = investorService.investorList(getParamters());
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, list);
	}
	
	/**
	 * 添加投资人
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/add_investor")
	public String addInvestor() throws Exception {
		Map<String, Object> params = getParamters();
		Investor investor = new Investor();
		investor.setAccount(Tools.getMapString(params, "account"));
		investor.setMobile(Tools.getMapString(params, "mobile"));
		investor.setName(Tools.getMapString(params, "name"));
		investor = investorService.addInvestor(investor);
		Map<String, Object> result = new HashMap<>();
		result.put("id", investor.getId());
		result.put("name", investor.getName());
		result.put("mobile", investor.getMobile());
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, result);
	}
	
	/**
	 * 删除投资人
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete_investor")
	public String delInvestor() throws Exception {
		Map<String, Object> params = getParamters();
		Investor investor = new Investor();
		investor.setId(Tools.getMapInt(params, "id"));
		investor.setAccount(Tools.getMapString(params, "account"));
		investorService.delInvestor(investor);
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, null);
	}
	
}
