package controller;

import database.DBconnection;
import Model.User;
import java.sql.*;

public class UserController {
    // Add user-related methods here

    public static boolean loginvalid(String username, String password) {

    try {
        Connection con = DBconnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM users WHERE username = ? AND password = ?");

        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        return rs.next();

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

    }

    public static boolean registervalid(User user) {
        try {
            Connection con = DBconnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users (name, username, password, address, email) VALUES (?, ?, ?, ? , ?)");
            ps.setString(1, user.getName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getAddress());
            ps.setString(5, user.getEmail());
            ps.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}