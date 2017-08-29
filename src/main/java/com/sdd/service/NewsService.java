package com.sdd.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sdd.dao.DaoSupport;

@Service
public class NewsService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getNews(String type) throws Exception {
		return (List<Map<String, Object>>) dao.findForList("com.sdd.mapper.NewsMapper.getNews", type);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getContent(String id) throws Exception {
		return (Map<String, Object>) dao.findForObject("com.sdd.mapper.NewsMapper.getContent", id);
	}
}
