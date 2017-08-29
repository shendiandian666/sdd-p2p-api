package com.sdd.service;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.sdd.dao.DaoSupport;
import com.sdd.model.Investor;

@Service("investorService")
public class InvestorService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> investorList(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = (List<Map<String, Object>>) dao
				.findForList("com.sdd.mapper.InvestorMapper.investorList", params);
		return list;
	}
	
	/**
	 * 获取投次人数据
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> investorList(int pageNum, int pageSize, Map<String, Object> params) throws Exception {
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = (List<Map<String, Object>>) dao
				.findForList("com.sdd.mapper.InvestorMapper.investorList", params);
		return list;
	}
	
	/**
	 * 添加投资人
	 * @param investor
	 * @return
	 * @throws Exception
	 */
	public Investor addInvestor(Investor investor) throws Exception {
		dao.save("com.sdd.mapper.InvestorMapper.add", investor);
		return investor;
	}
	
	/**
	 * 删除投资人
	 * @param investor
	 * @return
	 * @throws Exception
	 */
	public int delInvestor(Investor investor) throws Exception {
		int count = (int) dao.delete("com.sdd.mapper.InvestorMapper.delete", investor);
		return count;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getInvestor(String id) throws Exception {
		Map<String, Object> obj = (Map<String, Object>) dao
				.findForObject("com.sdd.mapper.InvestorMapper.getInvestor", id);
		return obj;
	}
	
}
