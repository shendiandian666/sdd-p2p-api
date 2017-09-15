package com.sdd.service;

import org.springframework.beans.factory.annotation.Value;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;

public class AlipayToAccount {

	@Value("${alipay.appId}")
	private String appId;
	@Value("${alipay.private.key}")
	private String privateKey;
	@Value("${alipay.public.key}")
	private String alipayPublicKey;
	
	public void transfer(String bizNo, String account, String amount) {
		AlipayClient alipayClient = new DefaultAlipayClient(
				"https://openapi.alipay.com/gateway.do",
				appId,
				privateKey,"JSON","utf-8",
				alipayPublicKey,"RSA2");
		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		request.setBizContent("{" +
		"    \"out_biz_no\":"+bizNo+"," +
		"    \"payee_type\":\"ALIPAY_LOGONID\"," +
		"    \"payee_account\":"+account+"," +
		"    \"amount\":"+amount+"," +
		"    \"payer_show_name\":\"马上多返利提现\"," +
		"    \"payee_real_name\":\"\"," +
		"    \"remark\":\"转账备注\"," +
		"  }");
		AlipayFundTransToaccountTransferResponse response;
		try {
			response = alipayClient.execute(request);
			if(response.isSuccess()){
				System.out.println("调用成功");
			} else {
				System.out.println("调用失败");
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}
	
	public void query(){
		AlipayClient alipayClient = new DefaultAlipayClient(
				"https://openapi.alipay.com/gateway.do",
				"app_id",
				"your private_key","JSON","utf-8",
				"alipay_public_key","RSA2");
		AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
		request.setBizContent("{" +
		"    \"out_biz_no\":\"3142321423432\"," +
		"    \"order_id\":\"20160627110070001502260006780837\"" +
		"  }");
		AlipayFundTransOrderQueryResponse response;
		try {
			response = alipayClient.execute(request);
			if(response.isSuccess()){
				System.out.println("调用成功");
			} else {
				System.out.println("调用失败");
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		
	}
}
