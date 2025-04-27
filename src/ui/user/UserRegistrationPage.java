package ui.user;

import java.util.Scanner;
import controller.UserController;
import Model.User;

public class UserRegistrationPage {

    //registration logic
    public static void register() {
        System.out.println("User Registration Page");

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter Name:");
        String name = sc.nextLine();

        System.out.print("Enter New Username: ");
        String username = sc.nextLine();

        System.out.print("Enter New Password: ");
        String password = sc.nextLine();

        System.out.println("Enter address:");
        String address = sc.nextLine();

        User user = new User(name,username,password,address);
        boolean success = UserController.registervalid(user);

        if (success) {
            System.out.println("Register success");
        }
        else System.out.println("no register");

    }
}