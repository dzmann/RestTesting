package com.testing.services;

import java.util.Iterator;

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
import com.testing.services.request.RegisterRequest;
import com.testing.services.response.GenericDataResponse;
import com.testing.services.response.GenericResponse;

@Path("/")
public class Services {
	
	/*@GET
	@Path("/test/{user}")
	public Response probarGet(@PathParam("user") String user,@HeaderParam("authorization") String token) {
		
		
		
		
		
		return Response.ok("Invocación correcta usando Tomcat " + user + " - " + token,  MediaType.APPLICATION_JSON).build();
		
	}
	
	@GET
	public Response authenticate(@HeaderParam("authorization") String token) {
		
		return Response.ok("token=" + token).build();
	}*/
	
	
	@POST
	@Path("/users")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerUser(RegisterRequest request) {
		Response response = null;
		GenericResponse genericResponse = new GenericResponse();
		GenericDataResponse dataResponse = new GenericDataResponse();
		
		if(request.nombre==null || request.apellido==null || request.email==null || request.password==null) {
			
			genericResponse.setDescription("Missing fields, expected are: nombre, apellido, email, password");
			genericResponse.setErrorCode("ERROR");
			
			return Response.status(500).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
		}else{
			
			if(request.nombre.length()>30) {
				genericResponse.setDescription("'nombre' field length exceeded, max length is 30");
				genericResponse.setErrorCode("ERROR");
				return Response.status(500).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
			}else if(request.apellido.length()>50) {
				genericResponse.setDescription("'apellido' field length exceeded, max length is 50");
				genericResponse.setErrorCode("ERROR");
				return Response.status(500).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
			}else if(!checkEmail(request.email)) {
				genericResponse.setDescription("not a valid email");
				genericResponse.setErrorCode("ERROR");
				return Response.status(500).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
			}
			
		}
		
		
		
		
		return Response.status(200).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
	}
	
	
	private boolean checkEmail(String email) {
		boolean isValidMail=true;
		
		
		
		return isValidMail;
		
	}
	
	
	

}
