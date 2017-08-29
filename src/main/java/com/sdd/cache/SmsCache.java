package com.sdd.cache;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.sdd.model.SmsEntity;

@Component
public class SmsCache {

	@Cacheable(value="smsCache", key="#key")
	public SmsEntity getSms(String key) {
		return null;
	}
	
	@CachePut(value="smsCache", key="#key")
	public SmsEntity putSms(String key, SmsEntity sms){
		return sms;
	}
	
}
