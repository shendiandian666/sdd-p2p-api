package com.sdd.service;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.sdd.dao.DaoSupport;

@Service("withdrawService")
public class WithdrawService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> listService(int pageNum, int pageSize, Map<String, Object> params) throws Exception {
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = (List<Map<String, Object>>) dao.findForList("com.sdd.mapper.WithdrawMapper.list", "");
		return list;
	}
	
}
