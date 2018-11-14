/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author cameron.kennedy
 */
public class VisitRecord {
    
    private LocalDate _visitDate;
    private String _reasonForVisit;
    private String _treatment;
    
    public VisitRecord(String reason, String treatment, LocalDate visited){
        _visitDate = visited;
        _reasonForVisit = reason;
        _treatment = treatment;
    }
    
    public LocalDate DateVisisted(){
        return _visitDate;
    }
    
    public String ReasonForVisit(){
        return _reasonForVisit;
    }
    
    public String Treatment(){
        return _treatment;
    }
    
    @Override
    public String toString(){
        return "";
    }
    @Override
    public boolean equals(Object o){
        if(!(o instanceof VisitRecord))
            return false;
        
        return this.hashCode() == ((PatientRecord)o).hashCode();
    }
    
    @Override
    public int hashCode(){
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this._reasonForVisit);
        hash = 79 * hash + Objects.hashCode(this._treatment);
        return hash & 0x7fffffff;
    }
    
    public VisitRecord Copy(){
        return new VisitRecord(this.ReasonForVisit(), this.Treatment(), this.DateVisisted());
    }
}
