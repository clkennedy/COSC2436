/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ListProject;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 *
 * @author cameron.kennedy
 */
public class ToDoItem implements Comparable{
    
    public static boolean SortOnPriority = true; 
    public static boolean FilterCompleted = false; 
    
    private String _name;
    private String _description;
    private Priority _priority;
    private LocalDate _dueDate;
    private boolean _completed;
    
    public ToDoItem(){
        this._name = "";
        this._description = "";
        this._priority = Priority.LOW;
        this._dueDate = LocalDate.now();
        this._completed = false;
    }
    
    public static void SortOnPriority(){
        ToDoItem.SortOnPriority = true;
    }
    
    public static void SortOnDate(){
        ToDoItem.SortOnPriority = false;
    }
    
    public ToDoItem(String name, String desc, Priority pri, LocalDate date, boolean completed){
        this._name = name;
        this._description = desc;
        this._priority = pri;
        this._dueDate = date;
        this._completed = completed;
    }
    
    @Override
    public String toString(){
        return this._name;
    }
    
    public String toStringAll(){
        return this._name + " | " + this._priority.toString() + " | " + 
                this._dueDate.format(ToDoList.DATEFORMATTER)+ " | " + ((this._completed)? "Completed" : "Not Completed");
    }
    
    public void Name(String name){
        this._name = name;
    }
    public String Name(){
        return this._name;
    }
    public void Description(String desc){
        this._description = desc;
    }
    public void Completed(boolean completed){
        this._completed = completed;
    }
    public boolean Completed(){
        return this._completed;
    }
    public void Priority(Priority pri){
        this._priority = pri;
    }
    public Priority Priority(){
        return this._priority;
    }
    public void DueDate(LocalDate date){
        this._dueDate = date;
    }
    public LocalDate DueDate(){
        return this._dueDate;
    }
    
    public String toCsv(){
        String csvStr;
               
        csvStr = "";
        
        Field[] f = this.getClass().getDeclaredFields();
        
        for(Field field : f){
            try{
                csvStr += field.getName() + ":" + ((field.get(this).toString().equals(""))? " " : field.get(this).toString());
                csvStr += (field == f[f.length - 1]) ? ";" : ",";
            }catch(Exception e){
                System.out.println(e.getMessage()); 
            }
        }
        return csvStr;
    }
    
    public void loadFromString(String loadStr){
        loadStr = loadStr.replace(";", "");
        String[] keyValuePairFields = loadStr.split(",");
        
        for(String curStr : keyValuePairFields){
            String[] pair = curStr.split(":");
            Field[] f = this.getClass().getDeclaredFields();
            for(Field field : f){
                if(field.getName().equals(pair[0])){
                    try {
                        Object o = field.get(this);
                        if(o instanceof String){
                            this.getClass().getDeclaredField(field.getName()).set(this, pair[1]);
                        }
                        if(o instanceof Priority){
                            this.getClass().getDeclaredField(field.getName()).set(this, Priority.valueOf(pair[1].toUpperCase()) );
                        }
                        if(o instanceof Boolean){
                            this.getClass().getDeclaredField(field.getName()).set(this, Boolean.parseBoolean(pair[1]));
                        }
                        if(o instanceof LocalDate){
                            this.getClass().getDeclaredField(field.getName()).set(this, LocalDate.parse(pair[1], DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        }
                    } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        }
    }

    @Override
    public int compareTo(Object t) {
        ToDoItem compItem;
        if(t instanceof ToDoItem){
            compItem = (ToDoItem)t;
        }
        else{
            return -99;
        }
        if(ToDoItem.SortOnPriority){
            return this._priority.compareTo(compItem.Priority());
        }
        else{
            return this._dueDate.compareTo(compItem.DueDate());
        }
    }
}
