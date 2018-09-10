/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 *
 * @author cameron.kennedy
 */
public class CustomEventHandler {
    private final List<Supplier> _subcribedEvents;
    
    public CustomEventHandler(){
        _subcribedEvents = new ArrayList();
    }
    
    public boolean Subscribe(Supplier method){
        return this._subcribedEvents.add(method);
    }
    
    public boolean UnSubcribe(Supplier method){
        return this._subcribedEvents.remove(method);
    }
    
    public boolean Activate(){
        boolean anyFailed = false;
        if(!this._subcribedEvents.isEmpty()){
            for(Supplier s: this._subcribedEvents){
                try{
                    anyFailed = anyFailed  || !((boolean)s.get());
                }catch(Exception e){
                    anyFailed = true;
                }
            }
        }
        return anyFailed;
    }
    
}
