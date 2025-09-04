
package com.mycompany.menu;

public class Manager extends User{
    
    public Manager(String userId, String name, String email, String role) {
        super(userId, name, email, role);
    }
    
    public Manager(){
        super();
    }

    @Override
    public void login() {
        super.login();
    }

    @Override
    void logout() {
        System.exit(0);
    }
    
    public void updateMenuItem(){
        
    }
    
    public void moderateReview(){
        
    }
    
    public void generateReport(){
        
    }
    
}
