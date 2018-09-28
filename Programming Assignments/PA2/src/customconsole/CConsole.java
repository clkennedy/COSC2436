/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customconsole;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author cameron.kennedy
 */
public class CConsole extends Application{
    private TextArea ta;
    private Thread consoleStart;
    private Stage priStage;
    public CConsole(){
    }
    
    @Override
    public void start(Stage primaryStage){
        
        this.ta = new TextArea();
        ta.setWrapText(true);
        ta.setEditable(false);
        ta.setText("Test");
        
        StackPane root = new StackPane();
        root.getChildren().add(ta);
        
        
        Scene scene = new Scene(root, 300, 250);
        
        
        primaryStage.setTitle("Console");
        primaryStage.setScene(scene);
        primaryStage.setWidth(640);
        primaryStage.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth() / 2);
        primaryStage.setHeight(400);
        primaryStage.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight());
        primaryStage.show();
        ta.setStyle(" -fx-text-fill:white;-fx-opacity: 1;");
        Region content = (Region) ta.lookup(".content");
        content.setStyle("-fx-background-color:black;");
        priStage = primaryStage;
    }
    public void Show(){
        launch();
    }
    
    public void writeLn(String str){
        this.priStage.getClass();
        this.ta.setText(this.ta.getText() + "\r\n" + str);
    }
}
