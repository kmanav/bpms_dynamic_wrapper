package com.redhat.gss.bpms.service;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.codec.binary.Base64;
import org.jboss.resteasy.client.ClientRequest;
import org.jbpm.services.task.utils.ContentMarshallerHelper;
import org.kie.services.client.api.RemoteRestRuntimeEngineFactory;
import org.kie.services.client.api.command.RemoteRuntimeEngine;

import com.redhat.gss.bpms.model.ContentModel;
import com.redhat.gss.bpms.model.ParamInfo;
import com.redhat.gss.bpms.model.ProcessInfo;

@Startup
@Singleton
public class BPMSService {

	final String USERNAME =       System.getProperty("BPMS_USERNAME", "admin");
	final String PASSWORD =       System.getProperty("BPMS_PASSWORD", "password");
	final String DEPLOYMENT_ID =  System.getProperty("BPMS_GAV", "test:test:1.0");
	final String BPMS_URL =       System.getProperty("BPMS_URL", "http://localhost:8080/business-central");
	
	private final static Logger LOG = Logger.getLogger(BPMSService.class.getName());

	@Inject
	DataModelsLoader loader;

	RemoteRuntimeEngine engine;

	@PostConstruct
	public void initialize() {
		try {
		   LOG.log(Level.INFO, "Initializing BPMSService[url=" + BPMS_URL + ", deployment=" + DEPLOYMENT_ID + ", credentials=" + USERNAME + "/*****]");
			engine = RemoteRestRuntimeEngineFactory.newBuilder()
					.addDeploymentId(DEPLOYMENT_ID).addUserName(USERNAME)
					.addPassword(PASSWORD).addUrl(new URL(BPMS_URL)).build()
					.newRuntimeEngine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RemoteRuntimeEngine getRemoteEngine() {
		return engine;
	}

	public ProcessInfo startProcess(ProcessInfo processInfo) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		for (ParamInfo param : processInfo.getParams()) {
			Object value = loader.loadObject(param);
			params.put(param.getParamName(), value);
		}
		long processId = engine.getKieSession().startProcess(processInfo.getProcessId(), params).getId();
		LOG.log(Level.FINE, "process instance " + processId + " created");
		processInfo.setProcessInstanceId(processId);
		return processInfo;
	}
	
	public boolean abortProcess(ProcessInfo processInfo) throws Exception {
	   try {
	      // only works with singleton strategy
   		//engine.getKieSession().abortProcessInstance(processInfo.getProcessInstanceId());
   		
   		// use with process instance strategy:
   		RemoteRuntimeEngine processEngine = RemoteRestRuntimeEngineFactory.newBuilder()
					.addDeploymentId(DEPLOYMENT_ID)
					.addProcessInstanceId(processInfo.getProcessInstanceId())
					.addUserName(USERNAME)
					.addPassword(PASSWORD).addUrl(new URL(BPMS_URL)).build()
					.newRuntimeEngine();
			processEngine.getKieSession().abortProcessInstance(processInfo.getProcessInstanceId());
      } catch(Exception ex) {
         ex.printStackTrace();
         return false;
      }
		return true;
	}	

	public Object getTaskContent(int id) throws Exception {
		String url = UriBuilder.fromPath(BPMS_URL).path("rest/task")
				.path(String.valueOf(id)).path("content").build().toString();
		ClientRequest cr = new ClientRequest(url).header("Authorization",
				getAuthHeader(USERNAME, PASSWORD));
		ContentModel content = cr.get(ContentModel.class).getEntity();
		return ContentMarshallerHelper.unmarshall(content.getSerializedContent(), null);
	}

	private Object getAuthHeader(String username, String password) {
		String auth = username + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset
				.forName("US-ASCII")));
		return "Basic " + new String(encodedAuth);
	}
}
