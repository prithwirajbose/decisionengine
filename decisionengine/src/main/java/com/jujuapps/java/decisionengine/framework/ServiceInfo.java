package com.jujuapps.java.decisionengine.framework;

import java.lang.reflect.Method;

public class ServiceInfo {
	private Class controller;
	private Method serviceMethod;
	public Class getController() {
		return controller;
	}
	public void setController(Class controller) {
		this.controller = controller;
	}
	public Method getServiceMethod() {
		return serviceMethod;
	}
	public void setServiceMethod(Method serviceMethod) {
		this.serviceMethod = serviceMethod;
	}
	
}
