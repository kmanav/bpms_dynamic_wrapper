/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the 
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.gss.bpms.rs;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.redhat.gss.bpms.model.ParamInfo;
import com.redhat.gss.bpms.model.ProcessInfo;
import com.redhat.gss.bpms.service.BPMSService;

@Path("/process")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProcessResource {

	@Inject
	BPMSService service;

	@Path("/start")
	@POST
	public Response startProcess(ProcessInfo processInfo) throws Exception {
		service.startProcess(processInfo);
		return Response.ok().build();
	}

	// used to generate a sample json for me
	@GET
	public ProcessInfo get() {
		ProcessInfo pi = new ProcessInfo();
		ArrayList<ParamInfo> params = new ArrayList<ParamInfo>();
		ParamInfo personParam = new ParamInfo();
		HashMap<String, Object> personAttrs = new HashMap<String, Object>();
		personParam.setParamName("person");
		personParam.setParamType("test.test.Person");
		personParam.setArtifactId("test");
		personParam.setGroupId("test");
		personParam.setVersion("1.0");
		personAttrs.put("name", "William");
		personAttrs.put("age", 26);
		personParam.setAttributes(personAttrs);
		params.add(personParam);
		pi.setProcessId("test.hello-process");
		pi.setParams(params);
		return pi;
	}

}
