
package com.mycompany.management;

import java.util.Scanner;

public class Management {

    public static final String jdbc="jdbc:ucanaccess://C:\\Users\\qasim alolaiwi\\OneDrive\\المستندات\\Database.accdb";
    static Scanner input = new Scanner(System.in);
    static int  choice;
    static EditMenu e = new EditMenu();
    static Report r = new Report();

    public static void main(String[] args) {
        
        
        
        do{
            System.out.print("How are you\n Choose:\n"
                + "1 - Add Item to the Menu\n"
                + "2 - Delete Item from the Menu\n"
                + "3 - Update Item Information\n"
                + "4 - Get Report\n"
                + "5 - Exit\n"
                    + "Your option: ");
        choice = input.nextInt();
        
        switch(choice){
            case 1:
                e.AddItem();
                break;
            case 2:
                e.DeleteItem();
                break;
            case 3:
                e.UpdateItem();
                break;
            case 4:
                r.extractReport();
                break;
        }
        
        }while (choice != 5);
        
    }
}
