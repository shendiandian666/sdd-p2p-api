package com.sdd.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import net.sf.json.JSONObject;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;

@Service("alipayToAccount")
public class AlipayToAccount {

	@Value("${alipay.appId}")
	private String appId;
	@Value("${alipay.private.key}")
	private String privateKey;
	@Value("${alipay.public.key}")
	private String alipayPublicKey;
	private String alipayPayerShowName="马上多返利网";
	private String alipayRemark="余额提现";
	
	
	public JSONObject transfer(String bizNo, String account, String amount) {
		AlipayClient alipayClient = new DefaultAlipayClient(
				"https://openapi.alipay.com/gateway.do",
				appId,
				privateKey,"JSON","utf-8",
				alipayPublicKey,"RSA2");
		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		request.setBizContent("{" +
		"    \"out_biz_no\":\""+bizNo+"\"," +
		"    \"payee_type\":\"ALIPAY_LOGONID\"," +
		"    \"payee_account\":\""+account+"\"," +
		"    \"amount\":\""+amount+"\"," +
		"    \"payer_show_name\":\""+alipayPayerShowName+"\"," +
		"    \"payee_real_name\":\"\"," +
		"    \"remark\":\""+alipayRemark+"\"," +
		"  }");
		AlipayFundTransToaccountTransferResponse response;
		try {
			response = alipayClient.execute(request);
			if(response.isSuccess()){
				System.out.println(JSONObject.fromObject(response).toString());
				System.out.println("调用成功");
			} else {
				System.out.println(JSONObject.fromObject(response).toString());
				System.out.println("调用失败");
			}
			return JSONObject.fromObject(response);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
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
				System.out.println(response.toString());
				System.out.println("调用成功");
			} else {
				System.out.println(response.toString());
				System.out.println("调用失败");
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		AlipayToAccount alipay = new AlipayToAccount();
		String bizNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		alipay.transfer(bizNo, "13651619487@1633.com", "0.1");
	}
}
