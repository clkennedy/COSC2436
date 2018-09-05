/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ListProject;

/**
 *
 * @author cameron.kennedy
 */
public enum Priority {
    
    LOW("low"), MEDIUM("Medium"), HIGH("High"), URGENT("Urgent");
    
    private final String name;
    
    private Priority(String name){
        this.name = name;
    }
    
    @Override
    public String toString(){
        return this.name;
    }
}
