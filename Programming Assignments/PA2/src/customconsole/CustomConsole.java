/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customconsole;


import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl;

/**
 *
 * @author cameron.kennedy
 */
public class CustomConsole extends Application implements Runnable{
    private volatile TextArea ta;
    private volatile Stage priStage;
    private static Thread consoleThread;
    private static Thread mainThread;
    private static CustomConsole console;
    private static volatile boolean editing = false;
    private static volatile String consoleText;
    private static volatile boolean readSingleKey = false;
    
    private static volatile Stack<String> typeHistory = new Stack<>();
    private static volatile int typeHistoryIndex = 0;
    //private CustomConsole thread;
    
    public CustomConsole(){
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
                if(readSingleKey){
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
        root.getChildren().add(ta);
        
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Console");
        primaryStage.setScene(scene);
        primaryStage.setWidth(640);
        primaryStage.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth() / 2);
        primaryStage.setHeight(400);
        primaryStage.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight());
        primaryStage.setOnCloseRequest(event -> {
            editing = false;
            CustomConsole.Close();
            CustomConsole.consoleThread.stop();
            System.exit(0);
        });
        primaryStage.show();
        
        ta.setStyle(" -fx-text-fill:white;-fx-opacity: 1;");
        Region content = (Region) ta.lookup(".content");
        content.setStyle("-fx-background-color:black;");
        priStage = primaryStage;
        console = this;
    }
    public static void Show() throws Exception{
        if(console != null || consoleThread != null){
            throw new Exception("Console Already Running");
        }
        consoleThread = new Thread(new CustomConsole());
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
            Logger.getLogger(CustomConsole.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void writeLine(String str){
        consoleText = console.ta.getText();
        if(console.ta.isEditable()){
            Platform.runLater( new Runnable() {
            @Override
            public void run() {
                console.ta.positionCaret( console.ta.getText().length() );
            }
        });
        }
        console.ta.setText(console.ta.getText() + str + "\r\n");
    }
    public static void writeLine(boolean bool){
        CustomConsole.writeLine("" + bool);
    }
    
    public static void write(String str){
        consoleText = console.ta.getText();
        if(console.ta.isEditable()){
            Platform.runLater( new Runnable() {
            @Override
            public void run() {
                console.ta.positionCaret( console.ta.getText().length() );
            }
        });
        }
        console.ta.setText(console.ta.getText() + str);
    }
    
    public static String ReadLine(){
        consoleText = console.ta.getText();
        editing = true;
        console.ta.setEditable(true);
        typeHistoryIndex = typeHistory.size();
        Platform.runLater( new Runnable() {
            @Override
            public void run() {
                console.ta.positionCaret( console.ta.getText().length() );
            }
        });
        //CustomConsole.writeLn(Thread.currentThread().getName());
        
        do{
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(CustomConsole.class.getName()).log(Level.SEVERE, null, ex);
            }
        }while(editing);
        console.ta.setEditable(false);
        String str = console.ta.getText().substring(consoleText.length());
        typeHistory.push(str);
        return str;
    }
    
    public static String ReadKey(){
        consoleText = console.ta.getText();
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
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(CustomConsole.class.getName()).log(Level.SEVERE, null, ex);
            }
        }while(editing);
        console.ta.setEditable(false);
        return console.ta.getText().substring(consoleText.length());
    }
    
    public static void Close(){
        try {
            consoleThread.stop();
            Platform.runLater( new Runnable() {
            @Override
            public void run() {
                console.priStage.close();
            }
        });
            Thread.sleep(1000);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
    public static void setTitle(String title){
        console.priStage.setTitle(title);
    }
    
    public static void Clear(){
        consoleText = "";
        console.ta.setText(consoleText);
    }

    @Override
    public void run() {
        launch();
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
                    System.out.println(ex.getMessage());
                }
            }
            CustomConsole.Close();
        }
        
    }
