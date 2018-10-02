/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pa2;

import customconsole.CustomConsole;
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
            CustomConsole.Show();
        } catch (Exception ex) {
            Logger.getLogger(LispEvaluatorMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        String input="";
        do{
            CustomConsole.write("Type Something: ");
            input = CustomConsole.ReadLine();
            if(!input.equals("")){
                CustomConsole.Clear();
                CustomConsole.writeLine("You Typed: " + input);
            }
        }while(!input.equals(""));
        CustomConsole.writeLine("Press Enter to Continue");
        CustomConsole.ReadLine();
        CustomConsole.Close();
    }
    
}
