package com.testing.services.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

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
	
	public static boolean update(User user,  User newData) {
		
		boolean result = false;
		Connection con = null;
		Statement stmt = null;
		Calendar calendar = Calendar.getInstance();
		try {
			con = DBConnection.getConnection();
			
			
			/*String deleteQuery = "UPDATE OAUTH_DATA set (user_id='"+ user.getEmail() +"', access_token='"+ user.get+"'";
			stmt = con.createStatement();
			stmt.execute(deleteQuery);*/
			
			String deleteQuery = "DELETE FROM OAUTH_DATA WHERE user_id='"+ user.getEmail() +"'";
			stmt = con.createStatement();
			stmt.execute(deleteQuery);
			
			String tokenInserQuery = "INSERT INTO OAUTH_DATA (user_id, access_token, expiry_date) VALUES('"+ newData.getEmail() +"', 'first registry', '"+String.valueOf(calendar.getTimeInMillis())+"')";
			stmt = con.createStatement();
			stmt.execute(tokenInserQuery);
			
			
			String query = "UPDATE USERS SET nombre=?, apellido=?, mail=?, password=? WHERE mail='"+ user.getEmail()+"'";
			
			PreparedStatement preparedStmt = con.prepareStatement(query);
			
			preparedStmt.setString(1, newData.getNombre());
			preparedStmt.setString(2, newData.getApellido());
			preparedStmt.setString(3, newData.getEmail());
			preparedStmt.setString(4, newData.getPassword());
			
			preparedStmt.executeUpdate();
			
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
		calendar.add(Calendar.MINUTE, 30);
		
		token.setAccess_token(TokenGenerator.generate());
		token.setUser_id(u.getEmail());
		token.setExpiry_date(String.valueOf(TimeUnit.MILLISECONDS.toMillis(calendar.getTimeInMillis())));
		
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
	
	
	public static Token getToken(String token) {
		Token result = new Token();
		
		Connection con = null;

		try {
			con = DBConnection.getConnection();
			String query = "SELECT user_id, access_token, expiry_date FROM OAUTH_DATA WHERE access_token='"+ token +"'";
			
			Statement preparedStmt = con.createStatement();
			
			ResultSet rs = preparedStmt.executeQuery(query);
			if(rs.next()) {
				
					
					result.setAccess_token(rs.getString("access_token"));
					result.setExpiry_date(rs.getString("expiry_date"));
					result.setUser_id(rs.getString("user_id"));
					
				
			}else{
				result.setAccess_token("NOT_FOUND");
			}
			
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
	
	
	public static String getUserIdFromToken(String token) {
		String result="";
		System.out.println("Obtaining user id from db " + token);
		Connection con = null;

		try {
			con = DBConnection.getConnection();
			String query = "SELECT user_id, access_token, expiry_date FROM OAUTH_DATA WHERE access_token='"+ token +"'";
			
			Statement preparedStmt = con.createStatement();
			
			ResultSet rs = preparedStmt.executeQuery(query);
			int i = 0;
			if(rs.next()) {
				
					
					result = rs.getString("user_id");
					
				i++;
			}
			
			if(i==0) {
				result = "";
			}
			
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
	
	
	public static User getUser(String email) {
		User result = null;
		
		Connection con = null;

		try {
			con = DBConnection.getConnection();
			String query = "SELECT * FROM USERS WHERE mail='"+ email +"'";
			
			Statement preparedStmt = con.createStatement();
			
			ResultSet rs = preparedStmt.executeQuery(query);
			
			if(rs.next()) {
				result = new User();
				result.setNombre(rs.getString("nombre"));
				result.setApellido(rs.getString("apellido"));
				result.setEmail(rs.getString("mail"));
			}
			
			con.close();
			
			
		} catch (SQLException e) {
			result = null;
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
