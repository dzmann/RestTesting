package com.testing.services.response;

import javax.xml.bind.annotation.XmlElement;

public class GenericDataResponse {
	
	private String nombre;
	private String apellido;
	private String email;
	private String password;
	
	
	public String getNombre() {
		return nombre;
	}
	
	@XmlElement
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	
	@XmlElement
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getEmail() {
		return email;
	}
	
	@XmlElement
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	
	@XmlElement
	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
