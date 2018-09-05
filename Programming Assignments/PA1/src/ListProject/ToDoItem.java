/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ListProject;

import java.time.LocalDateTime;

/**
 *
 * @author cameron.kennedy
 */
public class ToDoItem {
    
    private String _name;
    private String _description;
    private Priority _priority;
    private LocalDateTime _dueDate;
    private boolean _completed;
    
    public ToDoItem(){
        this._name = "";
        this._description = "";
        this._priority = Priority.LOW;
        this._dueDate = LocalDateTime.now();
        this._completed = false;
    }
    
    @Override
    public String toString(){
        return this._name;
    }
    
    public String toStringAll(){
        return this._name + " | " + this._priority.toString() + " | " + 
                this._dueDate.toString() + " | " + ((this._completed)? "Completed" : "Not Completed");
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
    public void DueDate(LocalDateTime date){
        this._dueDate = date;
    }
}
