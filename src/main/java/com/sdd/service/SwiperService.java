package com.sdd.service;

import org.springframework.stereotype.Service;
import com.sdd.dao.DaoSupport;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

@Service("swiperService")
public class SwiperService {
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSwiper(String type) throws Exception {
		return (List<Map<String, Object>>) dao.findForList("com.sdd.mapper.SwiperMapper.getSwiper", type);
	}

}
