/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ListProject;

import Menu.Menu;
import Menu.MenuItem;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author cameron.kennedy
 */
public class ListProjectMain extends Application{
    
    private final static ToDoList _toDoList = new ToDoList();
    private static boolean launchGUI = true;
    public static PrintWriter printWriter;
    
    public static void main(String args[]){
        
        try {
            Scanner scan = new Scanner( new File(System.getProperty("user.dir") + "\\useGui.txt"));
            launchGUI = scan.nextBoolean();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
        
        if(!launchGUI){
            Menu menu = new Menu();
            setupMenu(menu);
            menu.start();
        }else{
            launch(args);
        }
        
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ToDoListGuiLayout.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setResizable(false);
        
        stage.setHeight(425);
        stage.setWidth(root.prefWidth(425));
        stage.setTitle("ToDo List");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void setupMenu(Menu menu){
        ListProjectMain m = new ListProjectMain();
        menu.add(new MenuItem("New Item", _toDoList::add));
        menu.add(new MenuItem("Remove Item", _toDoList::remove));
        menu.add(new MenuItem("Modify Item", _toDoList::modify));
        menu.add(new MenuItem("Display ToDo List", _toDoList::Display));
        menu.add(new MenuItem("Toggle Display (Priority/Due Date)", _toDoList::ToggleDisplayBy));
        menu.add(new MenuItem("Filter/Unfilter Completed Items", _toDoList::ToggleFilterCompleted));
        menu.add(new MenuItem("Save", _toDoList::Save));
        menu.add(new MenuItem("Load", _toDoList::Load));
        menu.add(new MenuItem("Quit", menu::Quit));
    }
}
