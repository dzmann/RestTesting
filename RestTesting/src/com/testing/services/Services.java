package com.testing.services;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.testing.services.beans.RegisterResponse;
import com.testing.services.beans.RequestRegisterUser;
import com.testing.services.database.DBOperations;
import com.testing.services.model.User;
import com.testing.services.request.RegisterRequest;
import com.testing.services.response.GenericDataResponse;
import com.testing.services.response.GenericResponse;

@Path("/")
public class Services {
	
	private String errorDescription;
	private int code =-1;
	private static final Pattern VALIDATE_EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	/*
	 * @GET
	 * 
	 * @Path("/test/{user}") public Response probarGet(@PathParam("user") String
	 * user,@HeaderParam("authorization") String token) {
	 * 
	 * 
	 * 
	 * 
	 * 
	 * return Response.ok("Invocación correcta usando Tomcat " + user + " - " +
	 * token, MediaType.APPLICATION_JSON).build();
	 * 
	 * }
	 * 
	 * @GET public Response authenticate(@HeaderParam("authorization") String token)
	 * {
	 * 
	 * return Response.ok("token=" + token).build(); }
	 */

	@POST
	@Path("/users")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerUser(RegisterRequest request) {
		Response response = null;
		GenericResponse genericResponse = new GenericResponse();
		GenericDataResponse dataResponse = new GenericDataResponse();
		
		validateRequest(request);
		
		if(this.code==0) {
			
			User u = new User();
			
			u.setNombre(request.nombre);
			u.setApellido(request.apellido);
			u.setEmail(request.email);
			u.setPassword(request.password);
			
			DBOperations.insert(u);
			
			response =  Response.status(200).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
			
		}else if(this.code==1) {
			genericResponse.setErrorCode("MALFORMED REQUEST");
			genericResponse.setDescription(this.errorDescription);
			response =  Response.status(400).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
		}
		
		this.code=-1;
		this.errorDescription="";

		return response;
	}
	
	

	private void validateRequest(RegisterRequest request) {
		
		if (request.nombre == null || request.apellido == null || request.email == null || request.password == null) {

			this.errorDescription="Missing fields, expected are: nombre, apellido, email, password";
			this.code = 1;
			
		} else {
			
			if (request.nombre.length() > 30) {
				this.errorDescription="'nombre' field length exceeded, max length is 30";
				this.code = 1;
				
			} else if (request.apellido.length() > 50) {
				this.errorDescription="'apellido' field length exceeded, max length is 50";
				this.code=1;
				
				
			} else if (!checkEmail(request.email)) {
				this.errorDescription="not a valid email";
				this.code = 1;
				
			}else {
				this.code = 0;
			}

		}
		
	}

	private boolean checkEmail(String email) {
		Matcher matcher = VALIDATE_EMAIL_REGEX .matcher(email);

		return matcher.find();
	}

}
