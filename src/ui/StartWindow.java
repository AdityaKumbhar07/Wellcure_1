package ui;

import java.util.Scanner;

import ui.admin.AdminPage;
import ui.user.UserLoginPage;
import ui.user.UserRegistrationPage;


public class StartWindow {
    public StartWindow(){
        System.out.println("Welcome to Wellcure");

        System.out.println("Enter choice: login or register 3 for admin login");
        Scanner sc = new Scanner(System.in);

        switch (sc.nextInt()){
            case 1:
                UserLoginPage.login();
                break;
            case 2:
                UserRegistrationPage.register();
                break;
            case 3:
                AdminPage.admin();
                break;
            default:
                System.out.println("close the application");
        }
    }
}