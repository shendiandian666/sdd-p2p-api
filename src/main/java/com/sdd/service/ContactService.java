package com.sdd.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

import com.sdd.dao.DaoSupport;

@Service("contactService")
public class ContactService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> contactList() throws Exception {
		return (List<Map<String, Object>>) dao.findForList("com.sdd.mapper.ContactMapper.contactList", "");
	}
}
