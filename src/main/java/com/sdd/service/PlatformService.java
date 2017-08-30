package com.sdd.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sdd.dao.DaoSupport;

@Service("platformService")
public class PlatformService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPlatform(String id) throws Exception{
		return (Map<String, Object>) dao.findForObject("com.sdd.mapper.PlatformMapper.getPlatform", id);
	}
}
