package com.testing.services.response;

import javax.xml.bind.annotation.XmlElement;

public class Token {
	
	 public String user_id;
	 public String access_token;
	 public String expiry_date;
	
	public String getUser_id() {
		return user_id;
	}
	@XmlElement
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
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
