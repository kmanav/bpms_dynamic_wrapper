package com.redhat.gss.bpms.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "content")
public class ContentModel {

	private int id;
	private byte[] serializedContent;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getSerializedContent() {
		return serializedContent;
	}

	public void setSerializedContent(byte[] serializedContent) {
		this.serializedContent = serializedContent;
	}

	

}
