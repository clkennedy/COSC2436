/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ListProject;

import Menu.Menu;
import Menu.MenuItem;

/**
 *
 * @author cameron.kennedy
 */
public class ListProjectMain {
    
    public static void main(String args[]){
        Menu menu = new Menu();
        setupMenu(menu);
        menu.start();
    }
    
    public static void setupMenu(Menu menu){
        ListProjectMain m = new ListProjectMain();
        menu.add(new MenuItem("New Item", m::newItem));
        menu.add(new MenuItem("Quit", menu::Quit));
    }
    
    public boolean newItem(){
        System.out.println("New Item");
        return true;
    }
    
    
}
