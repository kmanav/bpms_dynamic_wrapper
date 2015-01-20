package com.redhat.gss.bpms.service;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
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

	final String USERNAME = "jesuino";
	final String PASSWORD = "redhat2014!";
	final String DEPLOYMENT_ID = "test:test:1.0";
	final String BPMS_URL = "http://localhost:8180/business-central";

	@Inject
	DataModelsLoader loader;

	RemoteRuntimeEngine engine;

	@PostConstruct
	public void initialize() {
		try {
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

	public void startProcess(ProcessInfo processInfo) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		for (ParamInfo param : processInfo.getParams()) {
			Object value = loader.loadObject(param);
			params.put(param.getParamName(), value);
		}
		engine.getKieSession().startProcess(processInfo.getProcessId(), params)
				.getId();
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
