package com.jujuapps.java.decisionengine.framework;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
public class ServiceResponse {
	private boolean success = false;
	private Object response;
	private Object error;
	public ServiceResponse(Object obj, boolean success) {
		if(success) {
			this.response = obj;
		}
		else {
			this.error = obj;
		}
		this.success = success;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Object getResponse() {
		return response;
	}
	public void setResponse(Object response) {
		this.response = response;
	}
	public Object getError() {
		return error;
	}
	public void setError(Object error) {
		this.error = error;
	}
}
