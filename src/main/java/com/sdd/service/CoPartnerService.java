package com.sdd.service;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sdd.dao.DaoSupport;

@Service
public class CoPartnerService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> coPartnerList() throws Exception{
		return (List<Map<String, Object>>) dao.findForList("com.sdd.mapper.CoPartnerMapper.coPartnerList", "");
	}
}
