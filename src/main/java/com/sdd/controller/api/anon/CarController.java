package com.sdd.controller.api.anon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdd.controller.api.ApiController;
import com.sdd.service.TeamActivityService;
import com.sdd.util.JSONResult;
import com.sdd.util.Tools;

@RestController("anonCar")
@RequestMapping("/api/anon/car")
public class CarController extends ApiController {

	@Autowired
	private TeamActivityService teamActivityService;
	
	@RequestMapping(value = "/getCarList")
	public String getCarList() throws Exception {
		Map<String, Object> parameters = getParamters();
		List<Map<String, Object>> list = teamActivityService.teamList(getPageNum(), getPageSize(), parameters);
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, list);
	}
	
	@RequestMapping(value = "/getCarDetail")
	public String getCarDetail() throws Exception {
		Map<String, Object> params = getParamters();
		Map<String, Object> result = new HashMap<String, Object>();
		//搭车活动
		Map<String, Object> teamActivity = teamActivityService.teamById(params);
		result.put("teamActivity", teamActivity);
		//活动列表
		String activityIds = Tools.getMapString(teamActivity, "activity_ids");
		List<Map<String, Object>> activityList = teamActivityService.activityList(activityIds);
		result.put("activityList", activityList);
		//交单记录
		String beginDate = Tools.getMapString(teamActivity, "begin_date");
		String endDate = Tools.getMapString(teamActivity, "end_date");
		Map<String, Object> orderParams = new HashMap<String, Object>();
		orderParams.put("activity_ids", activityIds);
		orderParams.put("begin_date", beginDate);
		orderParams.put("end_date", endDate);
		List<Map<String, Object>> orderList = teamActivityService.orderList(getPageNum(), getPageSize(),orderParams);
		result.put("orderList", orderList);
		
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, result);
	}
	
	@RequestMapping(value = "/getCarOrderList")
	public String getCarOrderList() throws Exception {
		Map<String, Object> params = getParamters();
		//搭车活动
		Map<String, Object> teamActivity = teamActivityService.teamById(params);
		//交单记录
		String activityIds = Tools.getMapString(teamActivity, "activity_ids");
		String beginDate = Tools.getMapString(teamActivity, "begin_date");
		String endDate = Tools.getMapString(teamActivity, "end_date");
		Map<String, Object> orderParams = new HashMap<String, Object>();
		orderParams.put("activity_ids", activityIds);
		orderParams.put("begin_date", beginDate);
		orderParams.put("end_date", endDate);
		List<Map<String, Object>> orderList = teamActivityService.orderList(getPageNum(), getPageSize(), orderParams);
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, orderList);
	}
	
}
