package com.sdd.controller.api.auth;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sdd.controller.api.ApiController;
import com.sdd.service.ActivityPlanService;
import com.sdd.service.ActivityService;
import com.sdd.service.ConfigService;
import com.sdd.service.InvestorService;
import com.sdd.util.JSONResult;
import com.sdd.util.Tools;

@RestController("authActivity")
@RequestMapping("/api/auth/activity")
public class ActivityController extends ApiController {
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private InvestorService investinfoService;
	
	@Autowired
	private ActivityPlanService activityPlanService;
	
	@Autowired
	private ConfigService configService;
	
	/**
	 * PC页面join
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/activity_join")
	public String activityJoin(Map<String, Object> map) throws Exception {
		Map<String, Object> params = getParamters();
		Map<String, Object> result = new HashMap<String, Object>();
		
		String activityId = getString("activity_id");
		Map<String, Object> activity = activityService.getActivity(activityId);
		result.put("activity", activity);
		//是否系暂停
		String reson = configService.getStatus("activity_join");
		if(!"".equals(reson)){
			return JSONResult.fillResultString("1000", reson, Collections.emptyMap());
		}
		//判断活动状态
		String status = activity.get("status").toString();
		if("1".equals(status)){
			// 投资人列表
			List<Map<String, Object>> investorList = investinfoService.investorList(params);
			result.put("investorList", investorList);
			// 投资方案列表
			List<Map<String, Object>> planList = activityPlanService.getPlanlist(activityId);
			result.put("planList", planList);
		}else{
			return JSONResult.fillResultString("1000", "活动不可参与", Collections.emptyMap());
		}
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, result);
	}
	
	/**
	 * 手机页面join
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/join")
	public String join(Map<String, Object> map) throws Exception {
		//是否系暂停
		String reson = configService.getStatus("activity_join");
		if(!"".equals(reson)){
			return JSONResult.fillResultString("1000", reson, Collections.emptyMap());
		}
		Map<String, Object> params = getParamters();
		Map<String, Object> result = new HashMap<String, Object>();
		String planId = getString("planId");
		Map<String, Object> plan = activityPlanService.getPlan(planId);
		result.put("plan", plan);
		String activityId = Tools.getMapString(plan, "activity_id");
		Map<String, Object> activity = activityService.getActivity(activityId);
		result.put("activity", activity);
		// 投资人列表
		List<Map<String, Object>> investorList = investinfoService.investorList(params);
		result.put("investorList", investorList);
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, result);
	}
	
}
