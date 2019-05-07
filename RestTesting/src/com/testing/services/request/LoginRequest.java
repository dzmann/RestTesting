package com.testing.services.request;

import javax.xml.bind.annotation.XmlElement;

public class LoginRequest {
	@XmlElement public String user;
	@XmlElement public String password;
}
