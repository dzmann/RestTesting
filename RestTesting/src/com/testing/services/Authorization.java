package com.testing.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.testing.services.authBeans.TokenService;
import com.testing.services.beans.RegisterResponse;
import com.testing.services.beans.Token;

@Path("/oauth/")
public class Authorization {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getToken(String request) {

		Response response = null;
		RegisterResponse errorResponse = new RegisterResponse();
		
		try {
			JSONObject jsonRequest = new JSONObject(request);
			String user = jsonRequest.getString("user");
			String password = jsonRequest.getString("password");
			
			if(TokenService.validateUser(user, password)) {
				Token token = TokenService.generateToken();
				response = Response.status(201).type(MediaType.APPLICATION_JSON).entity(token).build();
			}else {
				errorResponse.setErrorDescription("Invalid credentials");
				errorResponse.setResult("ERROR");
				errorResponse.setStatusCode("401");
				response = Response.status(401).type(MediaType.APPLICATION_JSON).entity(errorResponse).build();
			}
			
		} catch (JSONException e) {
			errorResponse.setErrorDescription("Not a valid JSON");
			errorResponse.setResult("ERROR");
			errorResponse.setStatusCode("418");
			response = Response.status(418).type(MediaType.APPLICATION_JSON).entity(errorResponse).build();
			
		} catch(Exception e) {
			errorResponse.setErrorDescription("Not a valid JSON");
			errorResponse.setResult("ERROR");
			errorResponse.setStatusCode("400");
			response =  Response.status(400).type(MediaType.APPLICATION_JSON).entity(errorResponse).build();
			
		}
		
		return response;
	}
	
	

}
