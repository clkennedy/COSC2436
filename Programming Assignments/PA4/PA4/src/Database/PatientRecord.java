/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author cameron.kennedy
 */
public class PatientRecord {
    private String _firstName;
    private String _lastName;
    private LocalDate _dob;
    private List<VisitRecord> _visitRecords;
    
    public PatientRecord(String firstName, String lastName, LocalDate DOB){
        _firstName = firstName;
        _lastName = lastName;
        _dob = DOB;
        _visitRecords = new ArrayList();
    }
    
    public String FirstName(){
        return _firstName;
    }
    public String LastName(){
        return _lastName;
    }
    public LocalDate DateOfBirth(){
        return _dob;
    }
    public VisitRecord getRecord(int index){
        return _visitRecords.get(index).Copy();
    }
    public List<VisitRecord> getAllRecords(){
        List<VisitRecord> copyOf = new ArrayList<>(_visitRecords.size());
        for(VisitRecord vr : _visitRecords){
            copyOf.add(new VisitRecord(vr.ReasonForVisit(), vr.Treatment(), vr.DateVisisted()));
        }
        return copyOf;
    }
    
    public boolean addVisit(VisitRecord vr){
        return this._visitRecords.add(vr);
    }
    
    
    @Override
    public String toString(){
        return "";
    }
    @Override
    public boolean equals(Object o){
        if(!(o instanceof PatientRecord))
            return false;
        
        return this.hashCode() == ((PatientRecord)o).hashCode();
    }
    
    @Override
    public int hashCode(){
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this._firstName);
        hash = 79 * hash + Objects.hashCode(this._lastName);
        hash = 79 * hash + Objects.hashCode(this._dob);
        return hash & 0x7fffffff;
    }
}
