package com.redhat.gss.bpms.rs;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.gss.bpms.service.BPMSService;

@Path("task")
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource {
	
	@Inject
	BPMSService service;
	
	@GET
	@Path("{id}/content")
	public Object getTaskContent(@PathParam("id")int id) throws Exception {
		return service.getTaskContent(id);
	}
}
