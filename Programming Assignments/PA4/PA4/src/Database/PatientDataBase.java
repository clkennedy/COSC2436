/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Main.FileFormatException;
import java.util.HashMap;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import Main.Pair;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 *
 * @author cameron.kennedy
 */
public class PatientDataBase {
    
    private HashMap<Integer, PatientRecord> _patients;
    
    public PatientDataBase(String filename)throws FileFormatException, FileNotFoundException{
        _patients = new HashMap();
        Scanner scan = new Scanner(new FileReader(
                new File(System.getProperty("user.dir") + "\\src\\DataStorage\\" + filename)));
        
        loadData(scan);
    }
    
    private boolean loadData(Scanner file){
        String str = "0";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while(file.hasNext()){
            str = file.nextLine();
            if(str.charAt(0) == '?')
                continue;
            
            int record = Integer.parseInt(str);
            String fName = file.nextLine();
            String lName = file.nextLine();
            String dob = file.nextLine();
            String vDate = "";
            String reason = "";
            String treatment = "";
            PatientRecord pr = new PatientRecord(fName, lName, LocalDate.parse(dob, dtf));
            while(!file.hasNextInt() && file.hasNext()){
                vDate = file.nextLine();
                reason = file.nextLine();
                treatment = file.nextLine();
                pr.addVisit(new VisitRecord(reason, treatment, LocalDate.parse(vDate, dtf)));
            }
            _patients.put(pr.hashCode(), pr);
        }
        return true;
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
