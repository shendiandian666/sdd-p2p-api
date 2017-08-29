package com.sdd.service;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.sdd.dao.DaoSupport;

@Service("activityPlanService")
public class ActivityPlanService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	public int addService(Map<String, Object> params) throws Exception {
		return dao.save("com.sdd.mapper.ActivityPlanMapper.add", params);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPlanlist(String activityId) throws Exception {
		return  (List<Map<String, Object>>) dao.findForList("com.sdd.mapper.ActivityPlanMapper.getPlanlist", activityId);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPlan(String planId) throws Exception {
		return   (Map<String, Object>) dao.findForObject("com.sdd.mapper.ActivityPlanMapper.getPlan", planId);
	}
	
}
