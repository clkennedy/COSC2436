/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Menu;

import com.sun.xml.internal.ws.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author cameron.kennedy
 */
public class Menu {
    
    private List<MenuItem> _menuItems;
    
    public Menu(){
        this._menuItems = new ArrayList<>();
    }
    
    public void add(MenuItem item){
        this._menuItems.add(item);
    }
    
    public void start(){
        while(true){
            this.Display();
        }
    }
    
    public void Display(){
        int count = 0;
        for(MenuItem item : this._menuItems){
            System.out.println(++count + ") " + item);
        }
        
        Scanner in = new Scanner(System.in);
        String input;
        
        do{
            System.out.print("Select Option: ");
            input = in.nextLine();
        }while(!isValidInt(input, count));
        
        this._menuItems.get(Integer.parseInt(input) - 1).call();
        
    }
    
    private boolean isValidInt(String numToTest, int number){
        try{
            Integer.parseInt(numToTest);
            if((Integer.parseInt(numToTest) < 1 || Integer.parseInt(numToTest) > number)){
                throw new Exception();
            }
            return true;
        }catch(Exception e){
            System.out.println("Invalid Input.");
            return false;
        }
    }
    
    public boolean Quit(){
        System.exit(0);
        return true;
    }
    
    public static void clearConsole(){
        
    }
    
}
