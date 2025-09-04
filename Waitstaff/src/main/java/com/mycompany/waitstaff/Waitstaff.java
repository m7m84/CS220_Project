
package com.mycompany.waitstaff;



public class Waitstaff {

    public static void main(String[] args) {
        States s = new States();
        
        System.out.print("Which order you want to update? (Enter -1 to stop)");
        
        while(true){
        int id = s.showOrders();
        if(id == -1) break;
        s.pushState(id);
        }
    }
    
}
