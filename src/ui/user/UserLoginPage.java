package ui.user;

import java.sql.*;
import java.util.Scanner;

import controller.UserController;
import database.DBconnection;

public class UserLoginPage {

    public static void login() {
        System.out.println("User Login Page");
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Username: ");
        String username = sc.nextLine();

        System.out.print("Enter Password: ");
        String pass = sc.nextLine();

        boolean valid = UserController.loginvalid(username,pass);

        if (valid){
            System.out.println("login succesfull");
            UserHome.UserHome(username);
        }else System.out.println("no login");

    }
}
