package com.sdd.controller.page.anon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.github.pagehelper.PageInfo;
import com.sdd.controller.WebController;
import com.sdd.service.ActivityPlanService;
import com.sdd.service.ActivityService;
import com.sdd.service.CoPartnerService;
import com.sdd.service.NewsService;
import com.sdd.service.TeamActivityService;
import com.sdd.util.Tools;

@Controller("anonIndexController")
@RequestMapping("")
public class IndexController extends WebController {

	@Autowired
	private ActivityService activityService;
	@Autowired
	private CoPartnerService coPartnerService;
	
	@RequestMapping(value = "/web")
	public String web(HttpServletRequest request, Map<String, Object> map) throws Exception {
		List<Map<String, Object>> list = activityService.getHotActivity(1, 15);
		map.put("activityList", list);
		return "/index";
	}
	
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request, Map<String, Object> map) throws Exception {
		List<Map<String, Object>> list = activityService.getHotActivity(1, 15);
		map.put("activityList", list);
		List<Map<String, Object>> coPartnerList = coPartnerService.coPartnerList();
		map.put("coPartnerList", coPartnerList);
		return "/index";
	}
	
	@RequestMapping(value = "/first")
	public String first(Map<String, Object> map) throws Exception {
		Map<String, Object> params = getParameters();
		params.put("first", "1");
		List<Map<String, Object>> list = activityService.getActivityList(getPageNum(), getPageSize(), params);
		map.put("activityList", list);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
		map.put("pageInfo", pageInfo);
		map.put("grade", Tools.getMapString(params, "grade"));
		map.put("minDays", Tools.getMapString(params, "minDays"));
		return "/first";
	}
	
	@RequestMapping(value = "/first/{grade}")
	public String first(@PathVariable("grade")String grade, Map<String, Object> map) throws Exception {
		Map<String, Object> params = getParameters();
		params.put("first", "1");
		List<Map<String, Object>> list = activityService.getActivityList(getPageNum(), getPageSize(), params);
		map.put("activityList", list);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
		map.put("pageInfo", pageInfo);
		map.put("grade", grade);
		map.put("minDays", Tools.getMapString(params, "minDays"));
		return "/first";
	}
	
	@RequestMapping(value = "/repetition")
	public String repetition(HttpServletRequest request, Map<String, Object> map) throws Exception {
		Map<String, Object> params = getParameters();
		params.put("first", "2");
		List<Map<String, Object>> list = activityService.getActivityList(getPageNum(), getPageSize(), params);
		map.put("activityList", list);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
		map.put("pageInfo", pageInfo);
		map.put("grade", Tools.getMapString(params, "grade"));
		map.put("minDays", Tools.getMapString(params, "minDays"));
		return "/repetition";
	}
	
	@RequestMapping(value = "/repetition/{grade}")
	public String repetition(@PathVariable("grade")String grade, HttpServletRequest request, Map<String, Object> map) throws Exception {
		Map<String, Object> params = getParameters();
		params.put("first", "2");
		List<Map<String, Object>> list = activityService.getActivityList(getPageNum(), getPageSize(), params);
		map.put("activityList", list);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
		map.put("pageInfo", pageInfo);
		map.put("grade", grade);
		map.put("minDays", Tools.getMapString(params, "minDays"));
		return "/repetition";
	}
	
	@Autowired
	private TeamActivityService teamActivityService;
	
	@RequestMapping(value = "/car")
	public String car(HttpServletRequest request, Map<String, Object> map) throws Exception {
		Map<String, Object> params = getParameters();
		List<Map<String, Object>> list = teamActivityService.teamList(getPageNum(), getPageSize(),params);
		map.put("teamList", list);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
		map.put("pageInfo", pageInfo);
		return "/car";
	}
	
	@RequestMapping(value = "/car/detail")
	public String carDetail(HttpServletRequest request, Map<String, Object> map) throws Exception {
		Map<String, Object> params = getParameters();
		//搭车活动
		Map<String, Object> teamActivity = teamActivityService.teamById(params);
		map.put("teamActivity", teamActivity);
		//活动列表
		String activityIds = Tools.getMapString(teamActivity, "activity_ids");
		List<Map<String, Object>> activityList = teamActivityService.activityList(activityIds);
		map.put("activityList", activityList);
		//交单记录
		String beginDate = Tools.getMapString(teamActivity, "begin_date");
		String endDate = Tools.getMapString(teamActivity, "end_date");
		Map<String, Object> orderParams = new HashMap<String, Object>();
		orderParams.put("activity_ids", activityIds);
		orderParams.put("begin_date", beginDate);
		orderParams.put("end_date", endDate);
		List<Map<String, Object>> orderList = teamActivityService.orderList(getPageNum(), getPageSize(),orderParams);
		map.put("orderList", orderList);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(orderList);
		map.put("pageInfo", pageInfo);
		return "/carDetail";
	}
	
	@RequestMapping(value = "/down")
	public String down(Map<String, Object> map) throws Exception {
		return "/others/down";
	}
	
	@RequestMapping(value = "/announcement")
	public String announcement(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> newsList = newsService.getNews("1");
		map.put("newsList", newsList);
		return "/others/announcement";
	}
	
	@RequestMapping(value = "/about")
	public String about(Map<String, Object> map) throws Exception {
		return "/others/about";
	}
	
	@RequestMapping(value = "/service")
	public String service(Map<String, Object> map) throws Exception {
		return "/others/service";
	}
	
	@RequestMapping(value = "/help")
	public String help(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> newsList = newsService.getNews("2");
		map.put("newsList", newsList);
		return "/others/help";
	}
	
	@RequestMapping(value = "/agreement")
	public String agreement(Map<String, Object> map) throws Exception {
		return "/others/agreement";
	}
	
	@RequestMapping(value = "/appDownload")
	public String appDownload(HttpServletRequest request, Map<String, Object> map) throws Exception {
		String userAgent = request.getHeader("user-agent");
		String system = Tools.getMapString(getParameters(), "system");
		if("".equals(system)){
			if(userAgent.contains("iPhone") || userAgent.contains("iPod") || userAgent.contains("iPad")){
				
			}else{
				return "redirect:http://www.msdfanli.com/upload/mobile_msd_android.apk";
			}
		}else{
			if("android".equals(system)){
				return "redirect:http://www.msdfanli.com/upload/mobile_msd_android.apk";
			}else if("apple".equals(system)){
				
			}
		}
		return "/others/down";
	}
	
	@Autowired
	private ActivityPlanService activityPlanService;
	
	@RequestMapping(value = "/*/getActivity")
	public String getActivity(Map<String, Object> map) throws Exception {
		Map<String, Object> params = getParameters();
		String activityId = Tools.getMapString(params, "activity_id");
		
		Map<String, Object> activity = activityService.getActivity(activityId);
		map.put("activity", activity);
		
		List<Map<String, Object>> planList = activityPlanService.getPlanlist(activityId);
		map.put("planList", planList);
		return "/activity";
	}
	
	@Autowired
	private NewsService newsService;
	
	@RequestMapping(value = "/*/news/{id}")
	public String news(@PathVariable("id")String id, Map<String, Object> map) throws Exception {
		map.put("news", newsService.getContent(id));
		return "/others/news";
	}
	
	
}
