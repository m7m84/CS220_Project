
package com.mycompany.menu;

public class Waitstaff extends User{

    public Waitstaff(String userId, String name, String email, String role) {
        super(userId, name, email, role);
    }
    
    public Waitstaff(){
        super();
    }

    @Override
    public void login() {
        super.login();
    }

    @Override
    void logout() {
        
    }
    
    public void viewOrders(){
        
    }
    
    public void updateOrderStatus(){
        
    }
    
}
