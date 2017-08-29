package com.sdd.service;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.sdd.dao.DaoSupport;

@Service("teamActivityService")
public class TeamActivityService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	public List<Map<String, Object>> teamList(int pageNum, int pageSize, Map<String, Object> params) throws Exception {
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = (List<Map<String, Object>>) dao.findForList("com.sdd.mapper.TeamActivityMapper.teamList", params);
		return list;
	}
	
	public Map<String, Object> teamById(Map<String, Object> params) throws Exception {
		return (Map<String, Object>) dao.findForObject("com.sdd.mapper.TeamActivityMapper.teamById", params);
	}
	
	public List<Map<String, Object>> activityList(String activityIds) throws Exception {
		return (List<Map<String, Object>>) dao.findForList("com.sdd.mapper.TeamActivityMapper.activityList", activityIds);
	}
	
	public List<Map<String, Object>> orderList(int pageNum, int pageSize, Map<String, Object> params) throws Exception {
		PageHelper.startPage(pageNum, pageSize);
		return (List<Map<String, Object>>) dao.findForList("com.sdd.mapper.TeamActivityMapper.orderList", params);
	}
	
}
