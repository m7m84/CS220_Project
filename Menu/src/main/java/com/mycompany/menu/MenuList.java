
package com.mycompany.menu;

import java.util.ArrayList;

public class MenuList {
    ArrayList<Category> categories;

    public MenuList() {
        this.categories = new ArrayList<Category>();
    }
    
    public ArrayList<Category> getCategories() {
        return categories;
    }
    
    public void setCategory(Category category){
        categories.add(category);
    }
}
