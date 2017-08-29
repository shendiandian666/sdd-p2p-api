package com.sdd.util;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONResult {
	public static final String SC_OK = "200";
	public static final String SC_OK_MSG = "SUCCESS";
	public static final String SC_500 = "500";
	public static final String SC_500_MSG = "系统异常";

	public static String fillResultString(String status, 
			String message, Object data) {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject() {
				{
					put("status", status);
					put("message", message);
					put("data", data);
				}
			};
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}
}
