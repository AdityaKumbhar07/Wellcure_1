package database;

// importing the connection

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
    public static Connection getConnection(){
        Connection con = null;
        System.out.println("DB connection page");

        try{
             con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/wellcure", "root", "aditya");
            System.out.println("Connected Successfully! in DBconnection");

        }catch (SQLException e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
        }
        return con;
    }
}