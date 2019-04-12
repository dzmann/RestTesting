package com.testing.services.request;

import javax.xml.bind.annotation.XmlElement;

public class TokenRequest {
	@XmlElement public String user;
	@XmlElement public String password;
}
