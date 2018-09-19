/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ListProject;

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
import CustomEvent.CustomEventHandler;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;

/**
 *
 * @author cameron.kennedy
 */
public class ToDoList{
    
    private static final String DATEPATTERN = "MM-dd-yyyy";
    public final static DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern(DATEPATTERN);
    
    
    public final CustomEventHandler ListModified;
    
    private List<ToDoItem> _toDoItems;
    
    public ToDoList(){
        this._toDoItems = new ArrayList();
        ListModified = new CustomEventHandler();
    }
    
    public boolean add(ToDoItem item){
        boolean success = this._toDoItems.add(item);
        this.ListModified.Activate();
        return success;
    }
    
    public boolean add(){
        Scanner in = new Scanner(System.in);
        
        boolean success = this._toDoItems.add(new ToDoItem(GetUserInputName(in), GetUserInputDesc(in), 
                GetUserInputPriority(in), GetUserInputDate(in), GetUserInputCompleted(in)));
        
        this.ListModified.Activate();
        return success;
    }
    
    public boolean remove(ToDoItem item){
        boolean success = this._toDoItems.remove(item);
        this.ListModified.Activate();
        return success;
    }
    
    public boolean remove(int index){
        boolean success = this._toDoItems.remove(this._toDoItems.get(index));
        this.ListModified.Activate();
        return success;
    }
    
    public boolean remove(){
        
        System.out.println("");
        int count = 0;
        for(ToDoItem item : this._toDoItems){
            System.out.println(++count + ") " + item);
        }
        
        Scanner in = new Scanner(System.in);
        String input;
        
        do{
            System.out.print("Select Option: ");
            input = in.nextLine();
        }while(!isValidInt(input, count));
        
        return remove(Integer.parseInt(input) - 1);
    }
    
    public boolean modify(){
        
        System.out.println("");
        int count = 0;
        for(ToDoItem item : this._toDoItems){
            System.out.println(++count + ") " + item);
        }
        
        Scanner in = new Scanner(System.in);
        String input;
        
        do{
            System.out.print("Select Option: ");
            input = in.nextLine();
        }while(!isValidInt(input, count));
        
        ToDoItem item = this._toDoItems.get(Integer.parseInt(input) - 1);
        
        count = 0;
        for(Field f : item.getClass().getDeclaredFields()){
            if(!Modifier.isStatic(f.getModifiers())){
                System.out.println(++count + ") " + f.getName().replace("_", ""));
            }
        }
        
        do{
            System.out.print("Select Option: ");
            input = in.nextLine();
        }while(!isValidInt(input, count));
        
        try {
            Field field = item.getClass().getDeclaredFields()[Integer.parseInt(input) + 1];
            
            
            Method rightMethodToCall = null;
            for(Method m : this.getClass().getMethods()){
                if(m.getName().toUpperCase().contains(field.getName().replace("_", "").toUpperCase())){
                    if(m.getName().contains("GetUserInput")){
                        rightMethodToCall = m;
                    }
                }
            }
            
            if(rightMethodToCall == null){
                System.out.println("Could Not find right Method");
                return false;
            }
            
            Object collectedData = rightMethodToCall.invoke(this, new Scanner(System.in));
            
            field.setAccessible(true);
            Object o = field.get(item);
            
            if(o instanceof String){
                field.set(item, ((String)collectedData));
            }
            if(o instanceof Priority){
                field.set(item, ((Priority)collectedData));
            }
            if(o instanceof Boolean){
                field.set(item, ((boolean)collectedData));
            }
            if(o instanceof LocalDate){
                field.set(item, ((LocalDate)collectedData));
            }
        } catch (IllegalArgumentException | IllegalAccessException |
                SecurityException | InvocationTargetException ex) {
            System.out.println(ex.getMessage());
        }
        
        return true;
    }
    
    public List<ToDoItem> getItems(){
        return this._toDoItems;
    }
    
    public String GetUserInputName(Scanner in){
        
        System.out.println();
        System.out.print("Task Name: ");
        return in.nextLine();
    }
    
    public String GetUserInputDesc(Scanner in){
        
        System.out.println();
        System.out.print("Task Description: ");
        return in.nextLine();
    }
    
    public Priority GetUserInputPriority(Scanner in){
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
    
    public LocalDate GetUserInputDate(Scanner in){
        LocalDate date;
        String input;
        boolean valid = false;
        do{
        System.out.println();
        System.out.print("Due Date(" + ToDoList.DATEPATTERN + "): ");
        input = in.nextLine();
        date = LocalDate.now();
        try{
            date = LocalDate.parse(input, ToDoList.DATEFORMATTER);
            valid = true;
           }catch(Exception e){
            System.out.println("Invalid Date Format");
        }
        }while(!valid);
        
        return date;
    }
    
    public boolean GetUserInputCompleted(Scanner in){
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
        //Menu.clearConsole();
        this.Sort();
        System.out.println("");
        System.out.print(this);
        System.out.println("");
        return true;
    }
    
    public void Sort(){
        Collections.sort(this._toDoItems);
    }
    
    public boolean ToggleDisplayBy(){
        ToDoItem.SortOnPriority = !ToDoItem.SortOnPriority;
        
        System.out.println((ToDoItem.SortOnPriority)? "Displaying Based on Priority" : "Displaying Based on Date");
        
        return true;
    }
    
    public boolean ToggleFilterCompleted(){
        ToDoItem.FilterCompleted = !ToDoItem.FilterCompleted;
        
        System.out.println((ToDoItem.FilterCompleted)? "Filtering Out Completed Task" : 
                "Displaying Completed Task");
        
        return true;
    }
    
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        for(ToDoItem item : this._toDoItems){
            if(!ToDoItem.FilterCompleted || !item.Completed()){
                sb.append(item.toStringAll());
            sb.append("\r\n");
            }
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
    
    public boolean Save(File file){
       
        PrintWriter pstream = null;
        
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
    
    public boolean Load(File file){
        
        this._toDoItems = new ArrayList();
        
        Scanner filein = null;
        
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
                    this.add(item);
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
