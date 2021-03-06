package com.sdd.model;

public class RestResult<T> {

	private String code;
	private String message;
	private T data;
	
	private RestResult(){}
	
	public static <T> RestResult<T> newInstance(){
		return new RestResult<>();
	}
	
	@Override
	public String toString() {
		return "RestResult{" +
				"code=" + code +
				", message='" + message + '\'' +
				", data=" + data +
				'}';
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
