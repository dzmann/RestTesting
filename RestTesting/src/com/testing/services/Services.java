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
	public Response registerUser(String request) {
		Response response = null;
		RegisterResponse responseEntity = new RegisterResponse();
		
		try {
			JSONObject jsonRequest = new JSONObject(request);
			
			if(jsonRequest.isNull("name")) {
				responseEntity.setErrorDescription("name field cannot be null!");
				responseEntity.setResult("ERROR");
				responseEntity.setStatusCode("500");
				Response.status(500).type(MediaType.APPLICATION_JSON).entity(responseEntity).build();
			}
			
			
		} catch (JSONException e) {
			responseEntity = new RegisterResponse();
			responseEntity.setErrorDescription("Not a valid JSON");
			responseEntity.setResult("ERROR");
			responseEntity.setStatusCode("500");
			return  Response.status(500).type(MediaType.APPLICATION_JSON).entity(responseEntity).build();
		}
		
		return response;
	}
	
	
	

}
