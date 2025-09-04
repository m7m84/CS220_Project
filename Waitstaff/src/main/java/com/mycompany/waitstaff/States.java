package com.mycompany.waitstaff;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class States {

    public final String jdbc = "jdbc:ucanaccess://C:\\Users\\qasim alolaiwi\\OneDrive\\المستندات\\Database.accdb"; 
    private Scanner input = new Scanner(System.in);

    public int showOrders() {
        try (Connection connection = DriverManager.getConnection(jdbc);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Orders WHERE NOT State = 'Completed'");
             ResultSet res = statement.executeQuery()) {

            while (res.next()) {
                System.out.println(res.getInt("No_order") + " |||| " + res.getString("State") +
                        "\n" + res.getString("Items"));
            }
            
            System.out.print("\nYour Number: ");
            return input.nextInt();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void pushState(int id) {
        String state = "";

        try (Connection connection = DriverManager.getConnection(jdbc);
             PreparedStatement statement = connection.prepareStatement("SELECT State FROM Orders WHERE No_order = ?")) {

            statement.setInt(1, id);
            try (ResultSet res = statement.executeQuery()) {
                if (res.next()) {
                    state = res.getString("State");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (!state.equals("Completed")) {
            switch (state) {
                case "Waiting":
                    state = "Prepared";
                    break;
                case "Prepared":
                    state = "Done";
                    break;
                case "Done":
                    state = "Completed";
                    break;
            }

            try (Connection connection = DriverManager.getConnection(jdbc);
                 PreparedStatement statement = connection.prepareStatement("UPDATE Orders SET State = ? WHERE No_order = ?")) {

                statement.setString(1, state);
                statement.setInt(2, id);
                statement.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}