package com.sdd.job;

import org.springframework.stereotype.Component;
import com.sdd.model.AccessToken;

@Component
public class DataSourceJob {

	//private Logger logger = LogManager.getLogger(getClass());
    // 第三方用户唯一凭证
    public static String appid = "13651619487";
    // 第三方用户唯一凭证密钥
    public static String appsecret = "tgb12345";
    // 第三方用户唯一凭证
    public static AccessToken accessToken = null;

    //@Scheduled(fixedDelay = 2*3600*1000)
    //7200秒执行一次
    public void getDatas(){
    }
    
   
    
}
