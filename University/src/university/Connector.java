/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package university;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connector {

	private static Connection con ;
	
        public static Connection connect()
	{
		try {
		
		Class.forName("com.mysql.jdbc.Driver");
			
		con= DriverManager.getConnection("jdbc:mysql://localhost:3306", "root" , "root");

                return con;
		} catch (ClassNotFoundException | SQLException e) {
                    System.out.println(e);
		}
		return null;
	}
        
        public void DisConnect()
        {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public static Connection getConnection()
        {
            return con;
        }
}
