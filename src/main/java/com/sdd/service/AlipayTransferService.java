package com.sdd.service;

import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sdd.dao.DaoSupport;

@Service("alipayTransferService")
public class AlipayTransferService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	public int addTransfer(Map<String, Object> map) throws Exception{
		return dao.save("com.sdd.mapper.AlipayTransferMapper.add", map);
	}
	
}
