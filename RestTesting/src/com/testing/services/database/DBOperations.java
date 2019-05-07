package com.testing.services.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import com.testing.services.model.User;
import com.testing.services.response.Token;
import com.testing.services.util.TokenGenerator;


public class DBOperations {
	
	public static boolean insert(User user) {
		
		boolean result = false;
		Connection con = null;
		Statement stmt = null;
		Calendar calendar = Calendar.getInstance();
		try {
			con = DBConnection.getConnection();
			
			
			String deleteQuery = "INSERT INTO OAUTH_DATA (user_id, access_token, expiry_date) VALUES('"+ user.getEmail() +"', 'first registry', '"+String.valueOf(calendar.getTimeInMillis())+"')";
			stmt = con.createStatement();
			stmt.execute(deleteQuery);
			
			
			
			String query = "INSERT INTO USERS (nombre, apellido, mail, password) VALUES (?,?,?,?)";
			
			PreparedStatement preparedStmt = con.prepareStatement(query);
			
			preparedStmt.setString(1, user.getNombre());
			preparedStmt.setString(2, user.getApellido());
			preparedStmt.setString(3, user.getEmail());
			preparedStmt.setString(4, user.getPassword());
			
			preparedStmt.execute();
			
			con.close();
			
			result = true;
		} catch (SQLException e) {
			result = false;
			e.printStackTrace();
		}finally {
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}
	
	public static boolean login (User user) {
		boolean result = false;
		
		Connection con = null;

		try {
			con = DBConnection.getConnection();
			String query = "SELECT * FROM USERS WHERE mail='"+ user.getEmail() +"' AND password='"+ user.getPassword() +"'";
			
			Statement preparedStmt = con.createStatement();
			
			ResultSet rs = preparedStmt.executeQuery(query);
			
			result = rs.next();
			
			con.close();
			
			
		} catch (SQLException e) {
			result = false;
			e.printStackTrace();
		}finally {
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		return result;
	}
	
	
	
	public static Token saveToken(User u) {
		
		
		Connection con = null;
		PreparedStatement preparedStmt = null;
		Statement stmt = null;
		Token token = new Token();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 2);
		
		token.setAccess_token(TokenGenerator.generate());
		token.setUser_id(u.getEmail());
		token.setExpiry_date(String.valueOf(calendar.getTimeInMillis()));
		
		System.out.println(calendar.getTime().toString());
		
		
		try {
			con = DBConnection.getConnection();
			
			String deleteQuery = "DELETE FROM OAUTH_DATA WHERE user_id='"+ u.getEmail() +"'";
			stmt = con.createStatement();
			stmt.execute(deleteQuery);
			
			
			String query = "INSERT INTO OAUTH_DATA (user_id, access_token, expiry_date) VALUES (?,?,?)";
			preparedStmt = con.prepareStatement(query);
			
			preparedStmt.setString(1, token.getUser_id());
			preparedStmt.setString(2, token.getAccess_token());
			preparedStmt.setString(3, token.getExpiry_date());
			
			preparedStmt.execute();
			
			con.close();
			
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally {
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return token;
		
	}
	
	
	public static boolean checkUser(User user) {
		boolean result = false;
		
		Connection con = null;

		try {
			con = DBConnection.getConnection();
			String query = "SELECT * FROM USERS WHERE mail='"+ user.getEmail() +"'";
			
			Statement preparedStmt = con.createStatement();
			
			ResultSet rs = preparedStmt.executeQuery(query);
			
			result = rs.next();
			
			con.close();
			
			
		} catch (SQLException e) {
			result = false;
			e.printStackTrace();
		}finally {
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		return result;
	}
	
	
	public static Token getToken(String email) {
		Token result = new Token();
		
		Connection con = null;

		try {
			con = DBConnection.getConnection();
			String query = "SELECT access_token, expiry_date FROM OAUTH_DATA WHERE user_id='"+ email +"'";
			
			Statement preparedStmt = con.createStatement();
			
			ResultSet rs = preparedStmt.executeQuery(query);
			
			result.setAccess_token(rs.getString("access_token"));
			result.setExpiry_date(rs.getString("expiry_date"));
			result.setUser_id(email);
			
			
			
			con.close();
			
			
		} catch (SQLException e) {
			result=null;
			e.printStackTrace();
		}finally {
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		return result;
	}
	


}
