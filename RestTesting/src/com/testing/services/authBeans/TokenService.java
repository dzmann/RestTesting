package com.testing.services.authBeans;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;

import com.testing.services.beans.Token;
import com.testing.services.database.DBOperations;

public class TokenService {
	
	private static HashMap<String, String> usersAuth = new HashMap<String, String>();

	
	public static boolean validateUser(String user, String password) {
		addUsers();
		@SuppressWarnings("rawtypes")
		Iterator it = usersAuth.entrySet().iterator();
		
		boolean valid = false;
		while (it.hasNext()) {
	        @SuppressWarnings("unchecked")
			Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
	        
	        if(pair.getKey().equals(user) && pair.getValue().equals(password)) {
	        	valid=true;
	        }
	    }
		
		return valid;
	}
	
	
	public static Token generateToken() {
		Token token = new Token();
		
		String tokenString = RandomStringUtils.random(49, true, true);
		
		System.out.println(tokenString + "   generated");
		
		token.setDescription("token generated successfully");
		token.setExpires("");
		token.setToken(tokenString);
		
		return token;
	}
	
	public static void addUsers() {
		usersAuth.put("098045376", "1234");
		usersAuth.put("098045377", "4321");
	}
	
	public static boolean isValidToken(String userId, String token) {
		boolean result = true;
		com.testing.services.response.Token dbToken = DBOperations.getToken(token);
		System.out.println(dbToken.getAccess_token());
		if(!dbToken.getAccess_token().equals("NOT_FOUND")) {
			
			Calendar dbTokenDate = Calendar.getInstance();
			Calendar nowDate = Calendar.getInstance();
			dbTokenDate.setTimeInMillis(Long.parseLong(dbToken.getExpiry_date()));
			
			if(token.equals(dbToken.getAccess_token())) {
				
				int resultDate = nowDate.compareTo(dbTokenDate);

				if(resultDate > 0) {
					result=false;
				}
				
			}else {
				result=false;
			}
			
			
		}else {
			result=false;
		}
		
		
		return result;
		
		
	}
	
	

}
