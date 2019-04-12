package com.testing.services.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RequestRegisterUser {
	
	@XmlElement public String name;
    @XmlElement public String surname;
	@XmlElement public String age;
    @XmlElement public String sex;
	@XmlElement public String country;
	

}
