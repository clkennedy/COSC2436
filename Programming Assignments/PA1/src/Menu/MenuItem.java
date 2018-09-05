/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Menu;

import java.util.function.Supplier;
/**
 *
 * @author cameron.kennedy
 */
public class MenuItem {
    private Supplier _callMethod;
    private String _name;
    
    public MenuItem(){
        this._name = "";
        this._callMethod = null;
    }
    
    public MenuItem(String name, Supplier method){
        this._name = name;
        this._callMethod = method;
    }
    
    public void call(){
        this._callMethod.get();
    }
    
    public void CallMethod(Supplier callMethod){
        this._callMethod = callMethod;
    }
    
    public void Name (String name){
        this._name = name;
    }
    
    @Override
    public String toString(){
        return this._name;
    }
    
}
