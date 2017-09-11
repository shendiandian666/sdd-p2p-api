package com.sdd.service;

import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sdd.dao.DaoSupport;
import com.sdd.util.Tools;

@Service("configService")
public class ConfigService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 查询业务状态(状态为0表示正常，不为0则返回原因描述)
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getStatus(String type) throws Exception {
		Map<String, Object> result = (Map<String, Object>) dao.findForObject("com.sdd.mapper.ConfigMapper.getStatus", type);
		String status = Tools.getMapString(result, "status");
		if("0".equals(status)){
			return "";
		}else{
			String reson = Tools.getMapString(result, "reson");
			return reson;
		}
	}
}
