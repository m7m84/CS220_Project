package com.mycompany.menu;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Customer extends User {
    
    private int money;
    private Scanner input = new Scanner(System.in);
    
    public Customer(String userId, String name, String email, String role, int money) {
        super(userId, name, email, role);
        this.money = money;
    }
    
    public Customer() {
        super();
    }

    @Override
public void login() {    
    System.out.print("Enter your number: ");
    number = input.next();

    String selectSQL = "SELECT * FROM Customers WHERE phoneNumber = ?";
    String insertSQL = "INSERT INTO Customers (fName, phoneNumber, TotalBalance) VALUES (?, ?, ?)";
    int customerId = -1; // تخزين معرف المستخدم

    try (Connection connection = DriverManager.getConnection(jdbc);
         PreparedStatement selectStmt = connection.prepareStatement(selectSQL);
         PreparedStatement insertStmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

        // التحقق مما إذا كان رقم الهاتف موجودًا
        selectStmt.setString(1, getNumber());
        ResultSet rs = selectStmt.executeQuery();

        if (rs.next()) {
            // رقم الهاتف موجود، استرجاع معرف العميل
            customerId = rs.getInt("id");
            System.out.println("Welcome back! " + rs.getString("fName"));
        } else {
            // رقم الهاتف غير موجود، إدخال مستخدم جديد
            System.out.print("You're a new customer\n: ");
            System.out.print("Enter your name: ");
            insertStmt.setString(1, input.next());
            
            insertStmt.setString(2, getNumber());
            
            System.out.print("Enter your money: ");
            insertStmt.setInt(3, input.nextInt());
            
            int rowsAffected = insertStmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    customerId = generatedKeys.getInt(1);
                    System.out.println("Signed up successfully! Your new ID is: " + customerId);
                }
            } else {
                System.out.println("Sign-up failed!");
                return;
            }
        }
        startProcess(customerId);

    } catch (Exception e) {
        e.printStackTrace();
    }
}


    @Override
    void logout() {
        System.exit(0);
    }

    private void showMenu() {
        while (true) {
            System.out.println("\nMenu Options");
            System.out.println("1 - View full menu");
            System.out.println("2 - Search for an item");
            System.out.println("3 - Start to add items");
            System.out.print("Your choice: ");

            int choice = input.nextInt();
            input.nextLine(); 

            switch (choice) {
                case 1 -> displayFullMenu();
                case 2 -> searchMenu();
                case 3 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void displayFullMenu() {
    System.out.println("\nFull Menu");

    String query = "SELECT * FROM Menu ORDER BY category";
    try (Connection connection = DriverManager.getConnection(jdbc);
         Statement statement = connection.createStatement();
         ResultSet res = statement.executeQuery(query)) {

        String currentCategory = "";
        while (res.next()) {
            int no = res.getInt("No_item");
            String category = res.getString("category");
            String itemName = res.getString("itemName");
            int price = res.getInt("price");

            // إذا تغير الصنف، اطبع اسمه مرة واحدة
            if (!category.equals(currentCategory)) {
                currentCategory = category;
                System.out.println("\n" + category);
            }

            System.out.println(no + " - " + itemName + " | Price: " + price);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}


    private void searchMenu() {
    System.out.print("\nEnter item name (or part of it): ");
    String searchQuery = input.nextLine();

    System.out.print("Enter category (or leave empty for all): ");
    String category = input.nextLine();

    String query = "SELECT * FROM Menu WHERE itemName LIKE ?" +
                   (category.isEmpty() ? "" : " AND category = ?");
    
    try (Connection connection = DriverManager.getConnection(jdbc);
         PreparedStatement searchStatement = connection.prepareStatement(query)) {

        searchStatement.setString(1, "%" + searchQuery + "%");
        if (!category.isEmpty()) {
            searchStatement.setString(2, category);
        }

        try (ResultSet res = searchStatement.executeQuery()) {
            boolean found = false;
            System.out.println("\nSearch Results:");

            while (res.next()) {
                System.out.println(res.getInt("No_item") + " - " +
                        res.getString("itemName") + " (Category: " + res.getString("category") + ") Price: " + res.getInt("price"));
                found = true;
            }

            if (!found) {
                System.out.println("No items found matching: " + searchQuery);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}


    private ArrayList<Integer> addItems(ArrayList<Integer> moreItems) {
        ArrayList<Integer> items = new ArrayList<>();
        items.addAll(moreItems);
        int itemNumber;

        System.out.println("\nChoose Items\n"
                + "To Add Item, Choose the item's number normally,\n"
                + "To Remove Item, Choose item's number with (-) sign,\n"
                + "To stop, Choose 0\n");
        while (true) {
            itemNumber = input.nextInt();
            
            if (itemNumber == 0){
                return reviewOrder(items);
            } else if (itemNumber < 0){
                if (items.contains(itemNumber*-1)){
                    items.remove(Integer.valueOf(itemNumber*-1));
                    updateMenuSales(itemNumber*-1, false);
                }
            } else {
                items.add(itemNumber);
                updateMenuSales(itemNumber, true);
            }
        }
    }
    
    private void updateMenuSales(int id, boolean increase){
        String selectSQL = "UPDATE Menu" +
              " SET Sales = Sales + ?" +
              " WHERE No_item = ?";
        try (Connection connection = DriverManager.getConnection(jdbc);
             PreparedStatement selectStatement = connection.prepareStatement(selectSQL);) {

            selectStatement.setInt(1, (increase ? 1 : -1));
            selectStatement.setInt(2, id);

            int rowsAffected = selectStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createOrder(ArrayList<Integer> itemList, int idOfCustomer) {
        StringBuilder items = new StringBuilder();
        int total = 0;

        String selectQuery = "SELECT * FROM Menu WHERE No_item = ?";
        String insertSQL = "INSERT INTO Orders (Amount, OrderedBy, Items, State, Rate) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(jdbc);
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {

            for (int itemId : itemList) {
                selectStatement.setInt(1, itemId);
                try (ResultSet res = selectStatement.executeQuery()) {
                    if (res.next()) {
                        total += res.getInt("price");
                        items.append(res.getString("itemName")).append(", ");
                    }
                }
            }

            if (items.length() > 0) {
                items.setLength(items.length() - 2);
            }
            

            insertStatement.setInt(1, total);
            insertStatement.setInt(2, idOfCustomer);
            insertStatement.setString(3, items.toString());
            insertStatement.setString(4, "Waiting");
            insertStatement.setInt(5, rateOrder());
            
            if(checkBalanceOfUser(total, idOfCustomer)){
                int rowsAffected = insertStatement.executeUpdate();
                System.out.println(rowsAffected > 0 ? "Your order has been placed!" : "Order failed!");
            } else System.out.println("You don't have enough money!");
            
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private int rateOrder(){
        System.out.println("Do you like to rate our system? (1 for yes, other for no)");
        String rate = input.next();
        if(!rate.equals("1")){
            return 0;
        }
            System.out.print("5 - ⭐⭐⭐⭐⭐⭐\n"
                    + "4 - ⭐⭐⭐⭐\n"
                    + "3 - ⭐⭐⭐\n"
                    + "2 - ⭐⭐\n"
                    + "1 - ⭐\n"
                    + "Choose Number from 1 to 5: ");
            
            int customerRate;
            customerRate = input.nextInt();
            if(customerRate > 5) customerRate = 5;
            else if(customerRate < 1) customerRate = 0;
            
            return customerRate;
    }

    private boolean checkBalanceOfUser(int totalPrice, int idCustomer) {
        try (Connection connection = DriverManager.getConnection(jdbc);
            Statement statement = connection.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM Customers WHERE ID = " + idCustomer)) {
            
            // it should have only one record
            if(res.next()){
                if(res.getInt(3) < totalPrice){
                    return false;
                }else{
                    updateUserBalance(totalPrice, idCustomer);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void updateUserBalance(int totalPrice, int idCustomer) {
        try (Connection connection = DriverManager.getConnection(jdbc);
         PreparedStatement statement = connection.prepareStatement("UPDATE Customers "
                 + "SET TotalBalance = TotalBalance - ? "
                 + "WHERE ID = ?")) {

        statement.setInt(1, totalPrice);
        statement.setInt(2, idCustomer);
        
        int rowsAffected = statement.executeUpdate();
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private ArrayList<Integer> reviewOrder(ArrayList<Integer> items) {
    while (true) {
        if (items.isEmpty()) {
            System.out.println("\nNo items in your order!");
            return addItems(items); // إعادة المستخدم لاختيار الطلبات
        }

        System.out.println("\nYour Order Review:");

        int total = 0;
        String query = "SELECT * FROM Menu WHERE No_item = ?";
        
        try (Connection connection = DriverManager.getConnection(jdbc);
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            for (int itemId : items) {
                statement.setInt(1, itemId);
                try (ResultSet res = statement.executeQuery()) {
                    if (res.next()) {
                        String itemName = res.getString("itemName");
                        int price = res.getInt("price");
                        total += price;
                        System.out.println("   - " + itemName + " | Price: " + price);
                    }
                }
            }
            
            System.out.println("\nTotal Price: " + total);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\n1 - Complete Order\n2 - Modify Order");
        System.out.print("Your choice: ");

        int choice = input.nextInt();
        input.nextLine(); 

        switch (choice) {
            case 1:
                return items; // تأكيد الطلب وإرساله
            case 2:
                return addItems(items); // إعادة تعديل الطلبات
            default:
                System.out.println("Invalid choice. Try again.");
        }
    }
    
    
}
    
    private void addMoreMoney(int id){
       System.out.print("Enter the money that you wanna add: ");
       int moreMoney = input.nextInt();
       
       String selectSQL = "UPDATE Customers" +
              " SET TotalBalance = TotalBalance + ?" +
              " WHERE ID = ?";
        try (Connection connection = DriverManager.getConnection(jdbc);
             PreparedStatement selectStatement = connection.prepareStatement(selectSQL);) {

            selectStatement.setInt(1, moreMoney);
            selectStatement.setInt(2, id);

            int rowsAffected = selectStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void viewReadyOrders(int id){
        String selectQuery = "SELECT * FROM Orders WHERE OrderedBy = ? AND State = ?";
        
         try (Connection connection = DriverManager.getConnection(jdbc);
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
            selectStatement.setInt(1, id);
            selectStatement.setString(2, "Done");
            
            try (ResultSet res = selectStatement.executeQuery()) {
                int i = 0;
                while(res.next()){
                        i++;
                        System.out.print(i + " - " + res.getString("Items")
                                + "\n/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\n\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void startProcess(int customerId){
        int choice = 0;
        System.out.print("1 - Create order\n"
                + "2 - View ready orders\n"
                + "3 - Add money\n"
                + "Your choice: ");
        choice = input.nextInt();
        
        switch(choice){
            case 1:
                showMenu();
                createOrder(addItems(new ArrayList<Integer>()), customerId);
                break;
            case 2:
                viewReadyOrders(customerId);
                break;
            case 3:
                addMoreMoney(customerId);
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

}
