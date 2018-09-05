/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ListProject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.util.converter.LocalDateTimeStringConverter;

/**
 *
 * @author cameron.kennedy
 */
public class ToDoList {
    private List<ToDoItem> _toDoItems;
    
    public ToDoList(){
        this._toDoItems = new ArrayList();
    }
    
    public void add(ToDoItem item){
        this._toDoItems.add(item);
    }
    
    public void add(){
        String name;
        String desc;
        Priority priority;
        LocalDateTime date;
        boolean completed;
        
        String input;
        Scanner in = new Scanner(System.in);
        
        System.out.print("Task Name: ");
        name = in.nextLine();
        
        System.out.println();
        System.out.print("Task Description: ");
        desc = in.nextLine();
        
        System.out.println();
        
        int count = 0;
            do{
                System.out.println("Priority: ");
            for(Priority pri: Priority.values()){
                System.out.println(++count + ") " + pri.toString());
            }
            input = in.nextLine();
        }while(isValidInt(input, count));
        
        priority = Priority.values()[Integer.parseInt(input)];
            
        
        System.out.println();
        System.out.print("Due Date(yyyy/mm/dd): ");
        input = in.nextLine();
        try{
            date = new LocalDateTimeStringConverter().fromString(input);
        }catch(Exception e){
            
        }
        
        
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
}
