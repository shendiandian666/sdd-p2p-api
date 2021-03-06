package com.sdd.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.sdd.service.PostOrderService;

@Component
public class PostOrderJob {

	@Autowired
	private PostOrderService postOrderService;
	
	@Scheduled(fixedDelay = 1800*1000)
    //1800秒执行一次
    public void examine(){
		try {
			postOrderService.examine();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
}
