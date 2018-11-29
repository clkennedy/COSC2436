/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaConsole;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author cameron.kennedy
 */
public class JavaConsole extends Application implements Runnable{
    private volatile TextArea ta;
    private volatile Stage priStage;
    private static Thread consoleThread;
    private static Thread mainThread;
    private static JavaConsole console;
    private static volatile boolean editing = false;
    private static volatile String consoleText;
    private static volatile boolean readSingleKey = false;
    
    private static volatile Stack<String> typeHistory = new Stack<>();
    private static volatile int typeHistoryIndex = 0;
    
    private static volatile boolean isRunning = false;
    //private CustomConsole thread;
    
    public JavaConsole(){
        //this.thread = thread;
    }
    
    @Override
    public void start(Stage primaryStage){
        
        this.ta = new TextArea();
        ta.setWrapText(true);
        ta.setEditable(false);
        ta.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke){
                if(!console.ta.isEditable())return;
                if(console.ta.getCaretPosition() < consoleText.length() + 1){
                    ta.positionCaret(console.ta.getText().length());
                }
                if(readSingleKey){
                    ke.consume();
                    editing = false;
                    readSingleKey = false;
                    return;
                }
                
                if((ke.getCode() == KeyCode.LEFT)
                        && console.ta.getCaretPosition() < consoleText.length() + 1){
                    ke.consume();
                    ta.positionCaret(console.ta.getText().length());
                }
                if((ke.getCode() == KeyCode.BACK_SPACE)
                        && console.ta.getCaretPosition() < consoleText.length() + 1){
                    ke.consume();
                    ta.positionCaret(console.ta.getText().length());
                }
                if(ke.getCode() == KeyCode.UP){
                    ke.consume();
                    
                    if(typeHistory.size() > 0){
                        if(typeHistoryIndex > 0)
                            typeHistoryIndex--;
                        console.ta.setText(consoleText + typeHistory.get(typeHistoryIndex));
                    }
                    console.ta.positionCaret( console.ta.getText().length() );
                }
                if(ke.getCode() == KeyCode.DOWN){
                    ke.consume();
                    if(typeHistory.size() > 0){
                        if(typeHistoryIndex < typeHistory.size())
                            typeHistoryIndex++;
                        if(typeHistoryIndex == typeHistory.size()){
                            console.ta.setText(consoleText);
                        }else{
                            console.ta.setText(consoleText + typeHistory.get(typeHistoryIndex));
                        }
                        
                    }
                    console.ta.positionCaret( console.ta.getText().length() );
                }
                if(ke.getCode() == KeyCode.ENTER){
                    ke.consume();
                    editing = false;
                }
            }
        });
        
        StackPane root = new StackPane();
        ScrollPane sPane = new ScrollPane();
        //sPane.setPrefSize(300, 250);
        sPane.setFitToWidth(true);
        sPane.setFitToHeight(true);
        //sPane.setPrefHeight(300);
        sPane.setContent(ta);
        
        VBox v = VBoxBuilder.create().children(sPane).build();
        v.setPrefHeight(300);
        //root.getChildren().add(v);
        
        root.getChildren().add(sPane);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Console");
        primaryStage.setScene(scene);
        primaryStage.setWidth(640);
        primaryStage.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth() / 2);
        primaryStage.setHeight(400);
        primaryStage.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight());
        primaryStage.setOnCloseRequest(event -> {
            editing = false;
            JavaConsole.Close();
            JavaConsole.consoleThread.stop();
            System.exit(0);
        });
        primaryStage.show();
        
        ta.setStyle(" -fx-text-fill:white;-fx-opacity: 1;");
        Region content = (Region) ta.lookup(".content");
        content.setStyle("-fx-background-color:black;");
        priStage = primaryStage;
        console = this;
    }
    public static boolean isRunning(){
        return isRunning;
    }
    public static void Show() throws ConsoleRunningException{
        if(console != null || consoleThread != null){
            throw new ConsoleRunningException("Console Already Running");
        }
        consoleThread = new Thread(new JavaConsole());
        mainThread = Thread.currentThread();
        
        MainTracker m = new MainTracker(mainThread);
        Thread mT = new Thread(m);
        mT.setName("MainTracker");
        mT.start();
        
        consoleThread.setName("Console");
        consoleThread.start();
        try {
            do{
                Thread.sleep(1000);
            }while(console == null);
        } catch (InterruptedException ex) {
            Log(ex.getMessage());
        }
        
        isRunning = true;
    }
    
    public static void writeLine(String str){
        if(!isRunning){
            try {
                JavaConsole.Show();
            } catch (ConsoleRunningException ex) {
                Log(ex.getMessage());
            }
        }
        
        if(console.ta.isEditable()){
            Platform.runLater( new Runnable() {
            @Override
            public void run() {
                console.ta.positionCaret( console.ta.getText().length() );
            }
        });
        }
        Platform.runLater( new Runnable() {
            @Override
            public void run() {
                console.ta.setText(console.ta.getText() + str + "\r\n");
                consoleText = console.ta.getText();
            }
        });
    }
    public static void writeLine(boolean bool){
        JavaConsole.writeLine("" + bool);
    }
    public static void writeLine(Double d){
        JavaConsole.writeLine("" + d);
    }
    public static void writeLine(){
        JavaConsole.writeLine("");
    }
    
    public static void write(String str){
        
        if(console.ta.isEditable()){
            Platform.runLater( new Runnable() {
            @Override
            public void run() {
                
                console.ta.positionCaret( console.ta.getText().length() );
            }
        });
        }
        Platform.runLater( new Runnable() {
            @Override
            public void run() {
                console.ta.setText(console.ta.getText() + str);
                consoleText = console.ta.getText();
            }
        });
    }
    
    public static String ReadLine(){
        
        editing = true;
        console.ta.setEditable(true);
        typeHistoryIndex = typeHistory.size();
        Platform.runLater( new Runnable() {
            @Override
            public void run() {
                consoleText = console.ta.getText();
                console.ta.positionCaret( console.ta.getText().length() );
            }
        });
        //CustomConsole.writeLn(Thread.currentThread().getName());
        
        do{
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                Log(ex.getMessage());
            }
        }while(editing);
        console.ta.setEditable(false);
        String str = console.ta.getText().substring(consoleText.length());
        typeHistory.push(str);
        JavaConsole.writeLine("");
        return str;
    }
    
    public static char ReadKey(){
        try{
            consoleText = console.ta.getText();
        }catch(Exception e){
            //return ' ';
        }
        editing = true;
        readSingleKey = true;
        console.ta.setEditable(true);
        Platform.runLater( new Runnable() {
            @Override
            public void run() {
                console.ta.positionCaret( console.ta.getText().length() );
            }
        });
        
        do{
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
               Log(ex.getMessage());
            }
        }while(editing);
        String str = console.ta.getText().substring(consoleText.length());
        typeHistory.push(str);
        JavaConsole.writeLine("");
        if(str.length() == 0)
            return ' ';
        return str.charAt(0);
    }
    
    public static void Close(){
        try {
            consoleThread.stop();
            Platform.runLater( new Runnable() {
            @Override
            public void run() {
                console.priStage.close();
                isRunning = false;
            }
        });
            Thread.sleep(1000);
        } catch (Exception ex) {
            Log(ex.getMessage());
        }
        
    }
    public static void setTitle(String title){
        console.priStage.setTitle(title);
    }
    
    public static void Clear(){
        Platform.runLater( new Runnable() {
            @Override
            public void run() {
                consoleText = "";
                console.ta.setText("");;
            }
        });
    }
              
    @Override
    public void run() {
        launch();
    }

    public static void Log(String str){
        File f = new File(System.getProperty("user.dir") + "\\JavaConsoleLog.log");
        //final PrintWriter pw = null;
        Scanner fileReader = null;
        try {
            fileReader = new Scanner(new FileReader(f));
            List<String> lines = new ArrayList();
            while(fileReader.hasNextLine()){
                lines.add(fileReader.nextLine());
            }
            fileReader.close();
            
            final PrintWriter pw = new PrintWriter(f);
            lines.forEach(line -> {pw.println(line);});
            pw.println(LocalDateTime.now() + ": " + str);
            
            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JavaConsole.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            
        }
    }
    
}

class ConsoleRunningException extends Exception{
    public ConsoleRunningException(String str){
        super(str);
    }
}

class MainTracker implements Runnable{

        private Thread mainThread;
        
        public MainTracker(Thread mThread){
            mainThread = mThread;
        }
        
        @Override
        public void run() {
            while(mainThread.getState() != Thread.State.TERMINATED){
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    JavaConsole.Log(ex.getMessage());
                }
            }
            JavaConsole.Close();
        }
        
    }
