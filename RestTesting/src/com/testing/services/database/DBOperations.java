package com.testing.services.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.testing.services.model.User;

public class DBOperations {
	
	public static boolean insert(User user) {
		
		boolean result = false;
		Connection con = null;
		
		
		try {
			con = DBConnection.getConnection();
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

}
