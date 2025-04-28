package ui.admin;

import database.DBconnection;

import java.sql.Connection;

public class OrderRequestPage {
    public static void order(){
        System.out.println("order request page");

        Connection con = DBconnection.getConnection();


    }
}