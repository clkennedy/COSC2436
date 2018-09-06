/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ListProject;

import Menu.Menu;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.util.converter.LocalDateTimeStringConverter;

/**
 *
 * @author cameron.kennedy
 */
public class ToDoList {
    
    private static final String DATEPATTERN = "dd-MM-yyyy";
    public final static DateTimeFormatter DateFormatter = DateTimeFormatter.ofPattern(DATEPATTERN);
    
    private List<ToDoItem> _toDoItems;
    
    public ToDoList(){
        this._toDoItems = new ArrayList();
    }
    
    public boolean add(ToDoItem item){
        return this._toDoItems.add(item);
    }
    
    public boolean add(){
        Scanner in = new Scanner(System.in);
        
        return this._toDoItems.add(new ToDoItem(GetName(in), GetDesc(in), GetPriority(in)
                , GetDate(in), GetCompleted(in)));
        
    }
    
    private String GetName(Scanner in){
        
        System.out.println();
        System.out.print("Task Name: ");
        return in.nextLine();
    }
    
    private String GetDesc(Scanner in){
        
        System.out.println();
        System.out.print("Task Description: ");
        return in.nextLine();
    }
    
    private Priority GetPriority(Scanner in){
        String input;  
        System.out.println();
        int count = 0;
            do{
                System.out.println("Priority: ");
            for(Priority pri: Priority.values()){
                System.out.println(++count + ") " + pri.toString());
            }
            System.out.print("Select Option: ");
            input = in.nextLine();
        }while(!isValidInt(input, count));
        
        return Priority.values()[Integer.parseInt(input) - 1];
    }
    
    private LocalDate GetDate(Scanner in){
        LocalDate date;
        String input;
        boolean valid = false;
        do{
        System.out.println();
        System.out.print("Due Date(" + ToDoList.DATEPATTERN + "): ");
        input = in.nextLine();
        date = LocalDate.now();
        try{
            date = LocalDate.parse(input, ToDoList.DateFormatter);
            valid = true;
           }catch(Exception e){
            System.out.println("Invalid Date Format");
        }
        }while(!valid);
        
        return date;
    }
    
    private boolean GetCompleted(Scanner in){
        String input; 
        System.out.println();
        System.out.print("Completed? (y/n): ");
        input = in.nextLine();
        
        return (input == "y" || input == "Y") ? true: false;
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
    
    public boolean Display(){
        Menu.clearConsole();
        System.out.print(this);
        return true;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        for(ToDoItem item : this._toDoItems){
            sb.append(item.toStringAll());
            sb.append("\r\n");
        }
        
        return sb.toString();
    }
}
