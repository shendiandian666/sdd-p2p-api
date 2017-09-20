package com.sdd.service;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.sdd.dao.DaoSupport;

@Service("activityService")
public class ActivityService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	public int addService(Map<String, Object> params) throws Exception {
		return dao.save("com.sdd.mapper.ActivityMapper.add", params);
	}
	
	/**
	 * 获取活动详情
	 * @param activityId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getActivity(String activityId) throws Exception {
		return (Map<String, Object>) dao
				.findForObject("com.sdd.mapper.ActivityMapper.getActivity", activityId);
	}
	
	/**
	 * 获取活动列表
	 * @param pageNum
	 * @param pageSize
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getActivityList(int pageNum, int pageSize, 
			Map<String, Object> params) throws Exception {
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = (List<Map<String, Object>>) dao
				.findForList("com.sdd.mapper.ActivityMapper.getActivityList", params);
		return list;
	}
	
	/**
	 * 热门活动列表
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getHotActivity(int pageNum, int pageSize) throws Exception {
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = (List<Map<String, Object>>) dao
				.findForList("com.sdd.mapper.ActivityMapper.getHotActivity", "");
		return list;
	}
	
	/**
	 * 首页三个大选项活动
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getTopActivity(int pageNum, int pageSize) throws Exception {
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = (List<Map<String, Object>>) dao
				.findForList("com.sdd.mapper.ActivityMapper.getTopActivity", "");
		return list;
	}
	
	/**
	 * 修改参与人数
	 * @param activityId
	 * @return
	 * @throws Exception
	 */
	public int updateJoins(String activityId) throws Exception {
		return (int) dao.update("com.sdd.mapper.ActivityMapper.updateJoins", activityId);
	}
}
