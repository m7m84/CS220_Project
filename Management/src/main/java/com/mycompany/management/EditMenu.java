
package com.mycompany.management;

import static com.mycompany.management.Management.jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class EditMenu {
    
    Scanner input = new Scanner(System.in);
    
    public void AddItem(){
        System.out.print("Enter Item Name: ");
        String name = input.nextLine();
        
        System.out.print("Enter Category Name: ");
        String category = input.nextLine();
        
        System.out.print("Enter Item Price: ");
        int price = input.nextInt();
        
        String insertSQL = "INSERT INTO Menu (itemName, category, price) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(jdbc);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, category);
            preparedStatement.setInt(3, price);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Item has inserted successfully!");
            } else {
                System.out.println("Failed!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void DeleteItem(){
        displayFullMenu();
        System.out.print("Enter Item Number to delete: ");
        int number = input.nextInt();

        try (Connection connection = DriverManager.getConnection(jdbc);
         PreparedStatement statement = connection.prepareStatement("DELETE FROM Menu WHERE No_item = ?")) {

        statement.setInt(1, number);
        int rowsAffected = statement.executeUpdate();

        System.out.println(rowsAffected > 0 ? "Order " + number + " has been deleted successfully." : "Failed");
        

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void UpdateItem(){
        displayFullMenu();
        
        System.out.print("Enter Item Number to update: ");
        int number = input.nextInt();
        
        System.out.print("Enter new Item Name: ");
        String name = input.nextLine();
        
        System.out.print("Enter new Item Category: ");
        String category = input.next();
        
        System.out.print("Enter new Item Price: ");
        int price = input.nextInt();

        try (Connection connection = DriverManager.getConnection(jdbc);
         PreparedStatement statement = connection.prepareStatement("UPDATE Menu "
                 + "SET itemName = ?, category = ?, price= ? "
                 + "WHERE No_item = ?")) {

        statement.setString(1, name);
        statement.setString(2, category);
        statement.setInt(3, price);
        statement.setInt(4, number);
        
        int rowsAffected = statement.executeUpdate();

        System.out.println(rowsAffected > 0 ? "Order " + number + " has been updated successfully." : "Failed");
        

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void displayFullMenu() {
        System.out.println("\nFull Menu:\n");
        try (Connection connection = DriverManager.getConnection(jdbc);
             Statement statement = connection.createStatement();
             ResultSet res = statement.executeQuery("SELECT * FROM Menu")) {

            while (res.next()) {
                System.out.println(res.getInt("No_item") + " - " +
                        res.getString("itemName") + " Price: " + res.getInt("price"));
            }
            
            System.out.println("\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
