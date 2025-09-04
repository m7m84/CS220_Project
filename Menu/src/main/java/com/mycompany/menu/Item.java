
package com.mycompany.menu;

public class Item {
    private int itemID;
    private String name;
    private String description;
    private double price;

    public Item(int itemID, String name, String description, double price) {
        this.itemID = itemID;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    
}
