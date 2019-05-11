package com.testing.services;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
	@Path("/users")
	public Response testToken(@HeaderParam("authorization") String bearer){
		GenericResponse genericResponse = new GenericResponse();
		Response response = null;
		User u = new User();
		String token = bearer.substring(BEARER.length()).trim();
		
			
		boolean validToken = TokenService.isValidToken("", token);
			
			if(!validToken) {
				genericResponse.setDescription("Invalid token");
				response = Response.status(401).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
			}else {
				String userId = DBOperations.getUserIdFromToken(token);
				System.out.println("USerId: " + userId);
				u=DBOperations.getUser(userId);
				GenericDataResponse genericDataResponse = new GenericDataResponse();
				genericDataResponse.setNombre(u.getNombre());
				genericDataResponse.setApellido(u.getApellido());
				genericDataResponse.setEmail(u.getEmail());
				genericResponse.setDescription("User found");
				genericResponse.setErrorCode("OK");
				genericResponse.setData(genericDataResponse);
				
				response = Response.status(200).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
				
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
					genericResponse.setErrorCode("OK");
					genericResponse.setDescription("User registered successfully");
					dataResponse.setNombre(u.getNombre());
					dataResponse.setApellido(u.getApellido());
					dataResponse.setEmail(u.getEmail());
					genericResponse.setData(dataResponse);
					response =  Response.status(200).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
					
				} catch (Exception e) {
					genericResponse.setErrorCode("UNKNOWN_ERROR");
					genericResponse.setDescription("An error ocurred inserting new user");
					response = Response.status(500).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
				}
				
			}else {
				genericResponse.setErrorCode("DUPLICATED_USER");
				genericResponse.setDescription("The user " + u.getEmail() + " already exists");
				response = Response.status(500).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
			}
			
			
			
			
		}else if(this.code==1) {
			genericResponse.setErrorCode("MALFORMED_REQUEST");
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
	
	
	@PUT
	@Path("/users/{user}")
	public Response updateAccount(@HeaderParam("authorization") String bearer, @PathParam("user") String user, RegisterRequest request){
		
		GenericResponse genericResponse = new GenericResponse();
		GenericDataResponse dataResponse = new GenericDataResponse();
		Response response = null;
		User u = new User();
		u.setEmail(user);
		String token = bearer.substring(BEARER.length()).trim();
		validateRequest(request);
		User newData = null;
		
		if(DBOperations.checkUser(u)) {
			
			boolean validToken = TokenService.isValidToken(user, token);
			
			
			if(!validToken) {
				genericResponse.setDescription("Device credentials expired");
				response = Response.status(401).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
			}else {
				
				u=DBOperations.getUser(user);
				if(this.code==0) {
					
					newData = new User(); 
					newData.setApellido(request.apellido);
					newData.setEmail(request.email);
					newData.setNombre(request.nombre);
					newData.setPassword(request.password);
					
					boolean update = DBOperations.update(u, newData);
					
					if(update) {
						dataResponse.setNombre(newData.getNombre());
						dataResponse.setApellido(newData.getApellido());
						dataResponse.setEmail(newData.getEmail());
						
						genericResponse.setDescription("Account information updated");
						genericResponse.setErrorCode("OK");
						genericResponse.setData(dataResponse);
						response = Response.status(200).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
					}else {
						genericResponse.setDescription("An error ocurred updating the account");
						genericResponse.setErrorCode("Unknown error");
						response = Response.status(500).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
					}
					
				}else if(this.code==1) {
					genericResponse.setErrorCode("MALFORMED_REQUEST");
					genericResponse.setDescription(this.errorDescription);
					response =  Response.status(400).type(MediaType.APPLICATION_JSON).entity(genericResponse).build();
				}
				
				
			}
			
		}else {
			
			genericResponse.setDescription("User does not exist");
			genericResponse.setErrorCode("INVALID_USER");
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
