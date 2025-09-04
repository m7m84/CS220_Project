
package com.mycompany.management;

import static com.mycompany.management.Management.jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Report {
    
    public void extractReport(){
        
        String selectFromMenu = "SELECT * FROM Menu ORDER BY Sales DESC, price*Sales DESC";
        String selectFromOrders = "SELECT * FROM Orders";
        
        
        try (Connection connection = DriverManager.getConnection(jdbc);
         PreparedStatement selectMenu = connection.prepareStatement(selectFromMenu);
         PreparedStatement selectOrders = connection.prepareStatement(selectFromOrders)) {
            ResultSet rs = selectMenu.executeQuery(), rs2 = selectOrders.executeQuery();
            
            int totalEarnings = 0, totalSales = 0, noItems = 0, noRates = 0, noOrders = 0;
            double avrageOfRates = 0, i = 0;
                while (rs.next()) {

                    int earnings = rs.getInt("Sales") * rs.getInt("price");
                    totalEarnings += earnings;
                    totalSales += rs.getInt("Sales");
                    noItems++;

                    System.out.println(rs.getInt("No_item") + " - " +
                            rs.getString("itemName") + " | Sales: " + rs.getInt("Sales")
                            + " | Earnings: " + earnings);
                }
            while (rs2.next()){
                int item = rs2.getInt("Rate");
                if(item > 0){
                    i++;
                    noRates += item;
                    
                }
                noOrders++;
            }
            if(i > 0) avrageOfRates = noRates / i;
            
            System.out.println("\n# of Items: " + noItems + "\nTotal Sales: " + totalSales + 
                    "\nTotal Earnings: " + totalEarnings + "\n# of Rates: " + i + " out of " + noOrders + " Orders" + 
                    "\nAvrage of Rates: " + (noRates / i));
            
            System.out.println("\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
