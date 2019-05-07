package com.testing.services.response;

import javax.xml.bind.annotation.XmlElement;

public class LoginResponse {
	
	private String access_token;
	private String expiry_date;
	
	public String getAccess_token() {
		return access_token;
	}
	
	@XmlElement
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getExpiry_date() {
		return expiry_date;
	}
	
	@XmlElement
	public void setExpiry_date(String expiry_date) {
		this.expiry_date = expiry_date;
	}
	
	
}
