package com.testing.services.response;

import javax.xml.bind.annotation.XmlElement;

public class GenericResponse {
	
	private String errorCode;
	private String description;
	private GenericDataResponse data;
	
	public String getErrorCode() {
		return errorCode;
	}
	
	@XmlElement
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getDescription() {
		return description;
	}
	
	@XmlElement
	public void setDescription(String description) {
		this.description = description;
	}

	public GenericDataResponse getData() {
		return data;
	}

	@XmlElement
	public void setData(GenericDataResponse data) {
		this.data = data;
	}
	
	
	
	
}
