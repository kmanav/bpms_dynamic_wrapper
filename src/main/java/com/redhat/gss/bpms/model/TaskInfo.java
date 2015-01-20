package com.redhat.gss.bpms.model;

import java.util.List;

public class TaskInfo {
	
	private int taskId;
	private List<ParamInfo> params;
	
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public List<ParamInfo> getParams() {
		return params;
	}
	public void setParams(List<ParamInfo> params) {
		this.params = params;
	}
	
}
