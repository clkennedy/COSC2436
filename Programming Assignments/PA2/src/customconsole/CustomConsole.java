/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customconsole;

import java.util.logging.Level;
import java.util.logging.Logger;





/**
 *
 * @author cameron.kennedy
 */
public class CustomConsole extends Thread {
    
    private CConsole console;
    private Thread[] threadPool;
    
    public CustomConsole(){
        console = new CConsole();
        threadPool = new Thread[1];
    }
    
    public void Show(){
        this.start();
    }
    
    @Override
    public void start(){
        super.start();
    }
    
    public void writeLn(String str){
        System.out.println(this.getState());
        console.writeLn(str);
    }
    
    @Override
    public void run(){
        this.threadPool[0] = this;
        console.Show();
    }
}
