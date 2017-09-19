package com.sdd.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageHelper;
import com.sdd.dao.DaoSupport;
import com.sdd.util.Tools;

@Service("postOrderService")
public class PostOrderService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	@Autowired
	private AccountService accountService;
	@Autowired
	private InvestorService investorService;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private ActivityPlanService activityPlanService;
	@Autowired
	private PlatformService platformService;
	
	/**
	 * 交单记录添加
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public String addOrder(Map<String, Object> params) throws Exception{
		//获取投资人信息
		String investorId = Tools.getMapString(params, "investorId");
		Map<String, Object> investor = investorService.getInvestor(investorId);
		params.put("investor_name", investor.get("name"));
		params.put("investor_phone", investor.get("mobile"));
		params.put("invest_date", Tools.getMapString(params, "date"));
		//获取返利信息
		String planId = Tools.getMapString(params, "planId");
		params.put("plan_id", planId);
		Map<String, Object> plan =activityPlanService.getPlan(planId);
		params.put("plan_name", plan.get("name"));
		params.put("plan_item", plan.get("item"));
		params.put("deposit_range", plan.get("depositDisplay_txt"));//方案投资金额范围描述
		//计算回款日期
		Calendar calendar = Calendar.getInstance();
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(Tools.getMapString(params, "date"));//投资日期
		calendar.setTime(date);
		params.put("days", plan.get("days"));
		int days = Tools.getIntValue(plan, "days");
		calendar.add(Calendar.DATE, days);
		//reveived_date
		Date reveivedDate = calendar.getTime();
		params.put("received_date", new SimpleDateFormat("yyyy-MM-dd").format(reveivedDate));//回款时间
		//投资金额
		double deposit = Double.valueOf(Tools.getMapString(params, "deposit"));
		//返利比例
		double rebate = Double.valueOf(plan.get("rebate").toString());
		params.put("rebate", plan.get("rebate"));
		//返现金额(只计算到千位，百位不计算)
		double cash_back = Math.floor(deposit/1000)*1000*rebate/100;
		params.put("cash_back", cash_back);
		//奖励
		double rebatePlusRate = Double.valueOf(plan.get("rebatePlus").toString());
		double rebatePlus = Math.floor(deposit/1000)*1000*rebatePlusRate/100;
		params.put("rebatePlus", rebatePlus);
		//利息(本金*年利率/365*天数)
		double rate = Double.valueOf(plan.get("rate").toString());
		params.put("rate", rate); //方案年化利率
		double interest = Math.floor(deposit*rate/100/365*days*100)/100;//先乘以100向下取整数，除100即精确到小数两位
		params.put("interest", interest);
		//红包
		double redback = Double.valueOf(plan.get("redback").toString());
		params.put("redback", redback);
		//体验金
		double bbin = Double.valueOf(plan.get("bbin").toString());
		params.put("bbin", bbin);
		//体验金收益
		double bbinInterest = Math.floor(bbin*rate/100/365*days*100)/100;
		params.put("bbin_interest", bbinInterest);
		//总收益计算(总收益=返现+奖励+利息+红包+体验金收益)
		double totalIncome = cash_back + rebatePlus + interest + redback + bbinInterest;
		params.put("totalIncome", totalIncome);
		//年化利率计算(年利率=收益/天数*365/本金*100%)
		double yearRate = totalIncome/days*365/deposit*100;
		params.put("year_rate", yearRate);
		//获取活动信息
		String activityId = Tools.getMapString(plan, "activity_id");
		Map<String, Object> activity = activityService.getActivity(activityId);
		String platformId = Tools.getMapString(activity, "platform_id");
		params.put("platform_id", platformId);
		//平台logo及url
		Map<String, Object> platform = platformService.getPlatform(platformId);
		params.put("logo", platform.get("platform_img"));
		params.put("url", platform.get("url"));
		String isFirst = activity.get("isFirst") == null ? "" : activity.get("isFirst").toString();
		params.put("is_first", "1".equals(isFirst) ? "首投" : "2".equals(isFirst) ? "复投" : "异常");
		params.put("activity_id", activity.get("id"));
		String platformName = Tools.getMapString(activity, "name");
		params.put("platform_name", platformName);
		dao.save("com.sdd.mapper.PostOrderMapper.add", params);
		//修改可提现余额
		params.put("rebate_late", cash_back);
		accountService.addRebateLate(params);
		//参与人数+1
		activityService.updateJoins(activityId);
		//添加账户变动记录
		Map<String, Object> recordMap = new HashMap<String, Object>();
		recordMap.put("type", "2");
		recordMap.put("remark", platformName + ",待返现:" + String.format("%.2f", cash_back) + "元");
		recordMap.put("account", params.get("account"));
		accountService.addRecord(recordMap);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> listService(int pageNum, int pageSize, Map<String, Object> params) throws Exception {
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = (List<Map<String, Object>>) dao
				.findForList("com.sdd.mapper.PostOrderMapper.list", params);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> sumInvestAndRebate(Map<String, Object> params) throws Exception {
		return (Map<String, Object>) dao.findForObject("com.sdd.mapper.PostOrderMapper.sumInvestAndRebate", params);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getDetail(Map<String, Object> params) throws Exception {
		return (Map<String, Object>) dao.findForObject("com.sdd.mapper.PostOrderMapper.getDetail", params);
	}
	
	public void examine() throws Exception {
		dao.update("com.sdd.mapper.PostOrderMapper.examine", "");
	}
	
}
