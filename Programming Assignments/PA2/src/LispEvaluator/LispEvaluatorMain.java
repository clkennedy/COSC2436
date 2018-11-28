/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LispEvaluator;

import customconsole.JavaConsole;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author cameron.kennedy
 */
public class LispEvaluatorMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
           
        try {
            JavaConsole.Show();
        } catch (Exception ex) {
            JavaConsole.Log(ex.getMessage());
        }
        String input="";
        do{
            JavaConsole.write("Type Lisp Expression to Evaluate (leave blank to quit): ");
            input = JavaConsole.ReadLine();
            if(!input.equals("")){
                JavaConsole.Clear();
                JavaConsole.writeLine("Lisp Expression: " + input);
                
                JavaConsole.writeLine("Result: " + LispEvaluator.GetResult(input));
                
            }
        }while(!input.equals(""));
        JavaConsole.writeLine("Press Any Key to Continue");
        JavaConsole.ReadKey();
    }
    
}
