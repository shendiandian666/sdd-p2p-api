package com.sdd.controller.api.anon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sdd.controller.api.ApiController;
import com.sdd.service.ActivityPlanService;
import com.sdd.service.ActivityService;
import com.sdd.util.JSONResult;

@RestController("anonActivity")
@RequestMapping("/api/anon/activity")
public class ActivityController extends ApiController {

	@Autowired
	private ActivityService activityService;
	@Autowired
	private ActivityPlanService activityPlanService;
	
	/**
	 * 热门活动
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getHotActivity")
	public String getHotActivity() throws Exception {
		List<Map<String, Object>> list = activityService.getHotActivity(1, 15);
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, list);
	}
	
	/**
	 * 活动列表
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getActivityList")
	public String getActivityList(HttpServletRequest request) throws Exception {
		Map<String, Object> parameters = getParamters();
		List<Map<String, Object>> list = 
				activityService.getActivityList(getPageNum(), getPageSize(), parameters);
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, list);
	}
	
	/**
	 * 活动详情
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getActivity")
	public String getActivity(HttpServletRequest request) throws Exception {
		String activityId = getString("activity_id");
		Map<String, Object> activity = activityService.getActivity(activityId);
		List<Map<String, Object>> planList = activityPlanService.getPlanlist(activityId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("activity", activity);
		result.put("planList", planList);
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, result);
	}
	
}
