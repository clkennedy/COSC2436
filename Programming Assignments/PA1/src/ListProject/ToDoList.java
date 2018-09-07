/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ListProject;

import Menu.Menu;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cameron.kennedy
 */
public class ToDoList{
    
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
        
        return (input.toUpperCase().equals("Y"));
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
    
    public boolean Save(){
       
        System.out.println();
        File file;
        Scanner scanner = new Scanner(System.in);
        PrintWriter pstream = null;
        
        do{
            System.out.print("Enter Name of File (No Extension): ");
            file = new File(scanner.nextLine() + ".ToDo");
        }while(!ExistsAndOverwrite(file, scanner));
         
        try {
            pstream = new PrintWriter( new FileWriter(file, false));
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        
        if(pstream != null){
            for(ToDoItem item : this._toDoItems){
                pstream.println(item.toCsv());
            }
            pstream.close();
        }
        return true;
    }
    
    public boolean Load(){
        
        this._toDoItems = new ArrayList();
        
        System.out.println();
        File file;
        Scanner scanner = new Scanner(System.in);
        Scanner filein = null;
        
        do{
            System.out.print("Enter Name of File (No Extension): ");
            file = new File(scanner.nextLine() + ".ToDo");
        }while(!file.exists());
        
        try {
            filein = new Scanner(new FileReader(file));
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        if(filein != null){
            while(filein.hasNext()){
                ToDoItem item = new ToDoItem();
                
                item.loadFromString(filein.nextLine());
                if(!item.Name().isEmpty()){
                    this._toDoItems.add(item);
                }
            }
        }
        
        return true;
    }
    
    private boolean ExistsAndOverwrite(File file, Scanner in){
        if(file.exists()){
            System.out.print("File Already Exist, Overwrite? (y/n): ");
            String input = in.nextLine();
            return input.toUpperCase().equals("y".toUpperCase());
        }
        return true;
    }
}
