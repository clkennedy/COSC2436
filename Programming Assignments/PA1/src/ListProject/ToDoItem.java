/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ListProject;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author cameron.kennedy
 */
public class ToDoItem {
    
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
                this._dueDate.format(ToDoList.DateFormatter)+ " | " + ((this._completed)? "Completed" : "Not Completed");
    }
    
    public void Name(String name){
        this._name = name;
    }
    public void Description(String desc){
        this._description = desc;
    }
    public void Completed(boolean completed){
        this._completed = completed;
    }
    public void Priority(Priority pri){
        this._priority = pri;
    }
    public void DueDate(LocalDate date){
        this._dueDate = date;
    }
    
    public void toCsv(){
        String csvStr;
               
        csvStr = "";
        
        Field[] f = this.getClass().getDeclaredFields();
        
        for(Field field : f){
            csvStr += field.getName() + ":";
            
        }
        
    }
}
