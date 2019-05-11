package com.testing.services.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	
	public static Connection getConnection() {
		Connection con = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			con = DriverManager.getConnection("jdbc:mysql://bf81a40bd1720b:dcda3f67@us-cdbr-iron-east-02.cleardb.net/heroku_69648511e6314bb?user=bf81a40bd1720b&password=dcda3f67&serverTimezone=UTC");
			//con = DriverManager.getConnection("jdbc:mysql://localhost/testing_rest?user=root&password=&serverTimezone=UTC");
		} catch (SQLException ex) {
			 System.out.println("SQLException: " + ex.getMessage());
			 System.out.println("SQLState: " + ex.getSQLState());
			 System.out.println("VendorError: " + ex.getErrorCode());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return con;
	}

}
