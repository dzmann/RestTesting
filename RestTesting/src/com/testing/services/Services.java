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

import com.testing.services.authBeans.TokenService;
import com.testing.services.beans.RegisterResponse;
import com.testing.services.beans.RequestRegisterUser;
import com.testing.services.database.DBOperations;
import com.testing.services.model.User;
import com.testing.services.request.LoginRequest;
import com.testing.services.request.RegisterRequest;
import com.testing.services.response.GenericDataResponse;
import com.testing.services.response.GenericResponse;
import com.testing.services.response.LoginResponse;
import com.testing.services.response.Token;

@Path("/")
public class Services {
	
	private static final String BEARER = "Bearer";
	private String errorDescription;
	private int code =-1;
	private static final Pattern VALIDATE_EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	
	@GET
	@Path("/users/{user}")
	public Response testToken(@HeaderParam("authorization") String bearer, @PathParam("user") String user){
		GenericResponse genericResponse = new GenericResponse();
		Response response = null;
		User u = new User();
		u.setEmail(user);
		String token = bearer.substring(BEARER.length()).trim();
		
		
		
		if(DBOperations.checkUser(u)) {
			
			boolean validToken = TokenService.isValidToken(user, token);
			
			if(!validToken) {
				genericResponse.setDescription("Device credentials expired");
				response = Response.status(401).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
			}else {
				
				u=DBOperations.getUser(user);
				GenericDataResponse genericDataResponse = new GenericDataResponse();
				genericDataResponse.setNombre(u.getNombre());
				genericDataResponse.setApellido(u.getApellido());
				genericDataResponse.setEmail(u.getEmail());
				genericResponse.setDescription("Account information obtained");
				genericResponse.setErrorCode("OK");
				genericResponse.setData(genericDataResponse);
				
				response = Response.status(200).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
				
			}
			
		}else {
			
			genericResponse.setDescription("User does not exist");
			response = Response.status(401).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
		}
		
		
	  
	  return response;
	  
	 }

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
			
			
			if(!DBOperations.checkUser(u)) {
				try {

					DBOperations.insert(u);
					genericResponse.setDescription("user registered");
					response =  Response.status(200).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
					
				} catch (Exception e) {
					genericResponse.setDescription("An error ocurred inserting new user");
					response = Response.status(500).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
				}
				
			}else {
				
				genericResponse.setDescription("the user already exists");
				response = Response.status(500).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
			}
			
			
			
			
		}else if(this.code==1) {
			genericResponse.setErrorCode("MALFORMED REQUEST");
			genericResponse.setDescription(this.errorDescription);
			response =  Response.status(400).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
		}
		
		this.code=-1;
		this.errorDescription="";

		return response;
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(LoginRequest request) {
		Response response = null;
		Token tokenResponse = new Token();
		GenericResponse genericResponse = new GenericResponse();
		LoginResponse loginResponse = new LoginResponse();
		
		User u = new User();
		u.setEmail(request.user);
		u.setPassword(request.password);
		
		if(DBOperations.login(u)) {
			tokenResponse = DBOperations.saveToken(u);
			genericResponse.setDescription("User logged");
			response = Response.status(200).type(MediaType.APPLICATION_JSON).entity(tokenResponse).build();
		}else {
			genericResponse.setDescription("Wrong user or password");
			response = Response.status(401).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
		}
		
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
