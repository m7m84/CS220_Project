
package com.mycompany.menu;

import java.util.ArrayList;

public class Category {
    private int categoryId;
    private String name;
    ArrayList<Item> items;

    public Category(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
        this.items = new ArrayList<Item>();
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
    
    public void addItem(Item item){
        items.add(item);
    }
    
    
}
