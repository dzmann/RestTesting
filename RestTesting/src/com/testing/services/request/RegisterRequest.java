package com.testing.services.request;

import javax.xml.bind.annotation.XmlElement;

public class RegisterRequest {
	
	@XmlElement public String nombre;
	@XmlElement public String apellido;
	@XmlElement public String email;
	@XmlElement public String password;
	
	

}
