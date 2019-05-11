package com.testing.services.util;

public class TokenGenerator {
	
	private static final String RANDOM_STRING = "abcdefghijklmnopqrstuvwxyz0123456789";
	
	public static String generate() {
		int count = 45;
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
		int character = (int)(Math.random()*RANDOM_STRING.length());
		builder.append(RANDOM_STRING.charAt(character));
		}
		return builder.toString();
		
		
	}

}
