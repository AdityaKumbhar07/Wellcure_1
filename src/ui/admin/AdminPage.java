package ui.admin;

import ui.StartWindow;

import java.util.Scanner;

public class AdminPage {
    public static void admin(){
        System.out.println("Admin page");
        Scanner sc = new Scanner(System.in);

        int choice = sc.nextInt();

        switch (choice){
            case 1:
                OrderRequestPage.order();
                break;
            case 2:
                StockManagementPage.showStockMenu();
                break;
            case 3:
                //GenerateReport();
                break;
            case 4:
                new StartWindow();
                break;
            default:
                System.out.println("no choice");
        }

    }
}