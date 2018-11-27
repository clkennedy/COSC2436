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
import com.sun.javafx.scene.layout.region.Margins;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

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
        Pattern p = Pattern.compile("[\\?]");
        while(file.hasNext()){
            while(file.hasNext(p))  
             str = file.nextLine();
            int record = Integer.parseInt(file.nextLine());
            while(file.hasNext(p))  file.nextLine();
            String fName = file.nextLine();
            while(file.hasNext(p))  file.nextLine();
            String lName = file.nextLine();
            while(file.hasNext(p))  file.nextLine();
            String dob = file.nextLine();
            String vDate = "";
            String reason = "";
            String treatment = "";
            PatientRecord pr = new PatientRecord(fName, lName, LocalDate.parse(dob, dtf));
            while(!file.hasNextInt() && file.hasNext()){
                while(file.hasNext(p))  file.nextLine();
                vDate = file.nextLine();
                while(file.hasNext(p))  file.nextLine();
                reason = file.nextLine();
                while(file.hasNext(p))  file.nextLine();
                treatment = file.nextLine();
                pr.addVisit(new VisitRecord(reason, treatment, LocalDate.parse(vDate, dtf)));
                while(file.hasNext(p))  file.nextLine();
            }
            _patients.put(pr.hashCode(), pr);
        }
        file.close();
        return true;
    }
    
    public void writeDatabaseFile(String filename){
        Scanner scan = null;
        FileWriter fw;
        File fr;
        File fs;
        Pattern p = Pattern.compile("[\\?]");
        try {
            fr = new File(System.getProperty("user.dir") + "\\src\\DataStorage\\" + filename);
            fs = new File(System.getProperty("user.dir") + "\\src\\DataStorage\\TempDb.txt");
            scan = new Scanner(fr);
            fw = new FileWriter(fs);
            
            int recordNum = 0;
        
            Set<Integer> keys = _patients.keySet();
            Iterator keysIt = keys.iterator();
            String frs = "?";
            while(scan.hasNext() || keysIt.hasNext()){
               
                while(scan.hasNext() && !isDigit(frs)){
                    frs = scan.nextLine();
                    if(frs.charAt(0) == '?'){
                        fw.write(frs + "\r\n");
                    }
                }
                
                if(keysIt.hasNext()){
                    PatientRecord pr = _patients.get(keysIt.next());
                
                    fw.write(++recordNum + "\r\n");
                    fw.write(pr.FirstName() + "\r\n");
                    fw.write(pr.LastName()+ "\r\n");
                    fw.write(pr.DateOfBirth().toString() + "\r\n");
                    
                    List<VisitRecord> vrs = pr.getAllRecords();
                    
                    for(VisitRecord vr : vrs){
                        fw.write(vr.DateVisisted().toString() + "\r\n");
                        fw.write(vr.ReasonForVisit()+ "\r\n");
                        fw.write(vr.Treatment() + "\r\n");
                    }
                    
                }
                
                if(scan.hasNext())
                    frs = scan.nextLine();
            }
            fw.close();
            scan.close();
            fr.delete();
            fs.renameTo(fr);
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        
    }
    
    private boolean isDigit(String str){
        try{
            Integer.parseInt(str);
            return true;
        }catch(Exception ex){
            return false;
        }
    }
    
    public HashMap<Integer, String> getPatientIdsNames(){
        HashMap<Integer, String> rtn = new HashMap<Integer, String>(_patients.size());
        Set<Integer> keys = _patients.keySet();
        for(Integer key : keys){
            PatientRecord pr = _patients.get(key);
            rtn.put(pr.hashCode(), pr.FirstName() + " " + pr.LastName());
        }
        
        return rtn;
    }
    
    public boolean addPatientRecord(PatientRecord pr){
        _patients.put(pr.hashCode(), pr);
        
        return true;
    }
    
    public boolean addPatientRecord(String firstName,String lastName,LocalDate DOB){
        return addPatientRecord(new PatientRecord(firstName, lastName, DOB));
    }
    
    public boolean removePatientRecord(PatientRecord pr){
        _patients.remove(pr.hashCode());
        
        return true;
    }
    
    public boolean removePatientRecord(String firstName,String lastName,LocalDate DOB){
        return removePatientRecord( new PatientRecord(firstName, lastName, DOB));
        
    }
    public PatientRecord getPatientRecord(int id){
        return ((PatientRecord)_patients.get(id));
    }
    
    public PatientRecord getPatientRecord(String firstName,String lastName,LocalDate DOB){
        return ((PatientRecord)_patients.get( new PatientRecord(firstName, lastName, DOB).hashCode()));
    }
    
    public List<String> getVisitDates(int id){
        List<String> rtn = new ArrayList();
        
        List<VisitRecord> vrs = _patients.get(id).getAllRecords();
        
        for(VisitRecord vr : vrs){
            rtn.add(vr.DateVisisted().toString());
        }
        
        return rtn;
    }
    
    public List<Pair<String, String>> getReasonTreatmentPairs(int id,LocalDate date){
        List<Pair<String, String>> rtn = new ArrayList();
        
        List<VisitRecord> vrs = _patients.get(id).getAllRecords();
        
        for(VisitRecord vr : vrs){
            if(date.equals(vr.DateVisisted()))
            rtn.add(new Pair(vr.ReasonForVisit(), vr.Treatment()));
        }
        
        return rtn;
    }
    
    public boolean addPatientVisit(int id,VisitRecord visit){
        
        _patients.get(id).addVisit(visit);
        
        return true;
    }
}
