package com.sdd.cache;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sdd.model.SmsEntity;
import com.sdd.util.JSONResult;

@RestController("cacheTest")
public class CacheTest {

	@Autowired
	private SmsCache smsCache;
	
	@RequestMapping(value = "/getCache")
	public String getCache() throws Exception {
		SmsEntity smsEntity = smsCache.getSms("13651619487register");
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, smsEntity);
	}
	
	@RequestMapping(value = "/putCache")
	public String putCache() throws Exception {
		SmsEntity entity = new SmsEntity();
		entity.setMobile("13651619487");
		entity.setTime(new Date());
		entity.setCaptcha("888888");
		SmsEntity smsEntity = smsCache.putSms("13651619487register", entity);
		return JSONResult.fillResultString(JSONResult.SC_OK, JSONResult.SC_OK_MSG, smsEntity);
	}
	
}
