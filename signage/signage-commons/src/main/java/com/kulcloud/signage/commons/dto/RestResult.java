package com.kulcloud.signage.commons.dto;

public class RestResult {
	private boolean result;
	private String exception;
	private Object data;

	public RestResult() {
		this("Successful logic");
	}
	
	public RestResult(Object data) {
		this(true, data, null);
	}
	
	public RestResult(Throwable ex) {
		this(false, null, ex);
	}
	
	public RestResult(boolean result, Object data, Throwable ex) {
		this.result = result;
		this.data = data;
		this.exception = ex == null ? null : ex.getMessage();
	}
	
	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

}
