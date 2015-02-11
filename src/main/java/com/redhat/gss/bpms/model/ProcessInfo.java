package com.redhat.gss.bpms.model;

import java.util.List;

public class ProcessInfo {
	
	private String processId;	
	private long processInstanceId;
	private List<ParamInfo> params;

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}
	
	public long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public List<ParamInfo> getParams() {
		return params;
	}

	public void setParams(List<ParamInfo> params) {
		this.params = params;
	}

}
