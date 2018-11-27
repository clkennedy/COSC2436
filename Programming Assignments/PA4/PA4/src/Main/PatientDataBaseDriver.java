/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import Database.*;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Douglas Atkinson
 */
public class PatientDataBaseDriver {

    static boolean dirty = false;
    static Scanner keyboard;
    static PatientDataBase pdb;
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    
    public static void main(String[] args) {
            
        try {
            JavaConsole.JavaConsole.Show();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return;
        }
        pdb = null;
        try {
            pdb = new PatientDataBase("patients.txt");
        }
        catch(FileFormatException | FileNotFoundException e)  {
            writeln(e.toString());
            ReadKey();
            return;
        }
        
        keyboard = new Scanner(System.in);
        
        char choice = 'q';
                
        do {
            writeln("1. List all patient names");
            writeln("2. Add a new patient");
            writeln("3. Remove patient");
            writeln("4. Display patient information");
            writeln("5. Add a new visit for a patient");
            writeln("s. Save database");
            writeln("q. Quit Program");
            write("Choice from above: ");
            choice = ReadKey();
            writeln("****************************");
            
            switch(choice) {
                case '1':   // List patient names
                    printPatientNames();
                    writeln("");
                    break;
                    
                case '2':   // Add new patient
                    addPatient();
                    writeln("");
                    break;
                    
                case '3':   // Remove a patient
                    removePatient();
                    writeln("");
                    break;
                    
                case '4':   // Get a patient record
                    displayPatientInformation();
                    writeln("");
                    break;
                    
                case '5':   // Add a new visit
                    addPatientVisit();
                    writeln("");
                    break;
                    
                case 's':   // Save database
                    pdb.writeDatabaseFile("patients.txt");
                    dirty = false;
                    break;
                    
                case 'q':   // Quit
                    if(dirty) {
                        writeln("Changes were made to the database.\nDo you want to save? (y/n): ");
                        choice = ReadKey();
                        if(choice == 'y' || choice == 'Y') {
                            pdb.writeDatabaseFile("patients.txt");
                            dirty = false;
                        }
                        choice = 'q';
                    }
                    break;                                        
            }
            writeln("****************************");
        } while(choice != 'q');
    }
    
    public static void write(String s) {
        JavaConsole.JavaConsole.write(s);
    }
    
    public static void writeln(String s) {
        JavaConsole.JavaConsole.writeLine(s);
    }
    
    public static String ReadLine() {
        return JavaConsole.JavaConsole.ReadLine();
    }
    public static char ReadKey() {
        return JavaConsole.JavaConsole.ReadKey();
    }
    
    public static void printPatientNames() {
        writeln("");
        writeln("*************************************");
        HashMap<Integer, String> patientIdNames = pdb.getPatientIdsNames();
        Set<Integer> keys = patientIdNames.keySet();
        for(Integer key : keys) {
            writeln("" + key + " " + patientIdNames.get(key));
        }
        writeln("*************************************");
        writeln("");
    }
    
    public static void addPatient() {
        writeln("");
        writeln("*************************************");
        write("Enter patient first name: ");
        String firstName = ReadLine();
        write("Enter patient last name: ");
        String lastName = ReadLine();
        write("Enter date of birth (YYYY-MM-DD): ");
        String DOB = ReadLine();
        writeln("*************************************");
        writeln("");
        
        pdb.addPatientRecord(firstName, lastName, LocalDate.parse(DOB, dtf));
        dirty = true;
    }
    
    public static void removePatient() {
        writeln("");
        writeln("*************************************");
        write("Enter patient first name: ");
        String firstName = ReadLine();
        write("Enter patient last name: ");
        String lastName = ReadLine();
        write("Enter date of birth (YYYY-MM-DD): ");
        String DOB = ReadLine();
        writeln("*************************************");
        writeln("");
        pdb.removePatientRecord(firstName, lastName, LocalDate.parse(DOB, dtf));
        dirty = true;
    }
    
    public static void displayPatientInformation() {
        int id;
        writeln("");
        writeln("*************************************");
        write("Enter patient ID: ");
        try {
            id = Integer.parseInt(ReadLine());
            PatientRecord pr = pdb.getPatientRecord(id);
            if(pr == null) {
                writeln("No patient record found for id " + id);                
            }
            else {
                writeln("");
                writeln("Patient Information");
                writeln("ID: " + id);
                writeln("Last name: " + pr.LastName());
                writeln("First name: " + pr.FirstName());
                writeln("DOB: " + pr.DateOfBirth());
                writeln("");
                writeln("Visits");
                List<String> visitDates = pdb.getVisitDates(id);
                for(String date : visitDates) {
                    List<Pair<String, String>> reasonTreatmentPairs = pdb.getReasonTreatmentPairs(id, LocalDate.parse(date, dtf));
                    writeln(date);
                    for(Pair<String, String> pair : reasonTreatmentPairs ) {
                        writeln("\t" + pair.getLeft() + " :: " + pair.getRight());
                    }
                    writeln("");
                }
            }
        }
        catch(NumberFormatException e) {
            writeln("You entered an invalid id");
            writeln("An id should be an integer");
        }
        writeln("*************************************");
        writeln("");
    }
    
    public static void addPatientVisit() {
        int id;
        VisitRecord visit;
        String visitReason;
        String visitTreatment;
        String date;
        
        writeln("");
        writeln("*************************************");
        write("Enter patient ID: ");
        try {
            id = Integer.parseInt(ReadLine());
            PatientRecord pr = pdb.getPatientRecord(id);
            if(pr == null) {
                writeln("No patient record found for id " + id);                
            }
            else {
                write("What is the date of the visit (YYYY-MM-DD): ");
                date = ReadLine();
                writeln("What was the reason for the visit?");
                visitReason = ReadLine();
                writeln("What treatment was provided for the visit?");
                visitTreatment = ReadLine();
                visit = new VisitRecord(visitReason, visitTreatment, LocalDate.parse(date, dtf));
                pdb.addPatientVisit(id, visit);
                dirty = true;
            }
            
        }
        catch(NumberFormatException e) {
            writeln("You entered an invalid id");
            writeln("An id should be an integer");
        }
    }
}


