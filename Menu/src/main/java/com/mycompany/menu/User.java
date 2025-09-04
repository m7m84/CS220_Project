
package com.mycompany.menu;
import java.util.*;

public abstract class User {
    String userId, name, number, role;
    Scanner input = new Scanner(System.in);
    public static final String jdbc="jdbc:ucanaccess://C:\\Users\\qasim alolaiwi\\OneDrive\\المستندات\\Database.accdb"; 

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User(String userId, String name, String number, String role) {
        this.userId = userId;
        this.name = name;
        this.number = number;
        this.role = role;
    }

    public User() {
    }
    
    
    
    
    public void login(){
        System.out.println("Enter your name: ");
        name = input.next();
        System.out.println("Enter your number: ");
        number = input.next();
        
    };
    abstract void logout();
}
