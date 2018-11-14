/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.util.HashMap;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import patient.Pair;

/**
 *
 * @author cameron.kennedy
 */
public class PatientDataBase {
    
    
    public PatientDataBase(String filename){
        
    }
    
    public void writeDatabaseFile(String filename){
        
    }
    
    public HashMap<Integer, String> getPatientIdsNames(){
        return new HashMap<Integer, String>();
    }
    
    public boolean addPatientRecord(String firstName,String lastName,LocalDate DOB){
        return true;
    }
    public boolean removePatientRecord(String firstName,String lastName,LocalDate DOB){
        return true;
    }
    public PatientRecord getPatientRecord(int id){
        return new PatientRecord("", "", LocalDate.MIN);
    }
    
    public List<String> getVisitDates(int id){
        return new ArrayList();
    }
    
    public List<Pair<String, String>> getReasonTreatmentPairs(int id,LocalDate date){
        return new ArrayList();
    }
    
    public boolean addPatientVisit(int id,VisitRecord visit){
        return true;
    }
}
