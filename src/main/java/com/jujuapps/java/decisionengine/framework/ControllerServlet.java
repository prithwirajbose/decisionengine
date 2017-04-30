package com.jujuapps.java.decisionengine.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.reflections.Reflections;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ControllerServlet extends HttpServlet {
	private static HashMap<String, ServiceInfo> SERVICE_MAPPINGS = null;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doHead(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doTrace(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.addHeader("Content-type", "application/json");
		PrintWriter out = response.getWriter();
		ObjectMapper mapper = new ObjectMapper();
		InputStream requestStream = request.getInputStream();
		String requestBodyForLogging = IOUtils.toString(requestStream);
		requestStream.close();
		requestStream = IOUtils.toInputStream(requestBodyForLogging);

		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
		Date now = new Date();
		LinkedHashMap<String, Object> requestLog = new LinkedHashMap<String, Object>();
		requestLog.put("remoteAddr", request.getRemoteHost() + ":" + request.getRemotePort());
		requestLog.put("timestamp", now.toLocaleString());
		requestLog.put("url", request.getMethod().toUpperCase() + " " + request.getRequestURL());
		requestLog.put("parameters", request.getParameterMap());
		requestLog.put("body", mapper.readValue(requestBodyForLogging.replace("\\n", "\n"), LinkedHashMap.class));

		System.out.println("************************************************************************\n\n"
				+ mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestLog)
				+ "\n\n************************************************************************");
		try {
			if (!request.getContentType().equals("application/json")) {
				throw new ServletException("Request Content-Type not supported");
			}

			String servicePath = request.getRequestURI().toString()
					.replace(request.getContextPath() + request.getServletPath(), "");
			if (SERVICE_MAPPINGS.containsKey(servicePath)) {
				ServiceInfo serviceInfo = SERVICE_MAPPINGS.get(servicePath);
				Object controller = serviceInfo.getController().newInstance();
				Method m = serviceInfo.getServiceMethod();
				Class type = m.getParameterTypes()[0];
				Object responseObject = m.invoke(controller, mapper.readValue(requestStream, type));
				ServiceResponse serviceResponse = new ServiceResponse(responseObject, true);
				out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(serviceResponse));
			} else {
				throw new ServiceNotFoundException(request.getRequestURI().toString());
			}
			/*
			 * DecisionRequest requestObject = mapper.readValue(requestStream,
			 * DecisionRequest.class); Object responseObject =
			 * controllerMethod.invoke(controller, requestObject);
			 */

		} catch (InvocationTargetException ite) {
			final Throwable actualException = ite.getCause();
			actualException.printStackTrace();
			HashMap<String, String> error = new HashMap<String, String>() {
				{
					put("message", actualException.getMessage());
					put("type", actualException.getClass().getSimpleName());
				}
			};
			ServiceResponse serviceResponse = new ServiceResponse(error, false);
			response.setStatus(500);
			out.print(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(serviceResponse));
		} catch (final Exception e) {
			e.printStackTrace();
			HashMap<String, String> error = new HashMap<String, String>() {
				{
					put("message", e.getMessage());
					put("type", e.getClass().getSimpleName());
				}
			};
			ServiceResponse serviceResponse = new ServiceResponse(error, false);
			response.setStatus(500);
			out.print(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(serviceResponse));
		}
	}

	public void init() {
		init(getServletConfig());
	}

	public void init(ServletConfig config) {
		if (SERVICE_MAPPINGS == null || SERVICE_MAPPINGS.size() <= 0) {
			SERVICE_MAPPINGS = new HashMap<String, ServiceInfo>();
			Reflections reflections = new Reflections(config.getInitParameter("scanPackages"));
			// Reflections reflections = new
			// Reflections("com.jujuapps.java.decisionengine.rest");
			Set<Class<? extends ApplicationController>> classes = reflections
					.getSubTypesOf(ApplicationController.class);
			for (Class controller : classes) {
				Method[] methods = controller.getDeclaredMethods();

				if (methods != null && methods.length > 0) {
					for (Method m : methods) {
						Service service = m.getDeclaredAnnotation(Service.class);
						if (service != null && service.path() != null) {
							ServiceInfo serviceInfo = new ServiceInfo();
							serviceInfo.setController(controller);
							serviceInfo.setServiceMethod(m);

							SERVICE_MAPPINGS.put(service.path(), serviceInfo);
						}
					}
				}
			}
		}
	}
}
