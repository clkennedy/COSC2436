/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;


import ListProject.Priority;
import ListProject.ToDoItem;
import ListProject.ToDoList;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 *
 * @author ARustedKnight
 */
public class FXMLDocumentController implements Initializable {
    
    private ToDoList _toDoList = new ToDoList();
    
    @FXML
    private Label label;
    @FXML
    private ToggleGroup priority;
    @FXML
    private CheckBox filterCompleted;
    @FXML
    private TextField nameField;
    @FXML
    private TextArea descField;
    @FXML
    private RadioButton low;
    @FXML
    private RadioButton medium;
    @FXML
    private RadioButton high;
    @FXML
    private RadioButton urgent;
    @FXML
    private DatePicker dPicker;
    @FXML
    private ListView lView;
    @FXML
    private Button addItemButton;
    @FXML
    private Button saveItemButton;
    @FXML
    private Button cancelItemButton;
    @FXML
    private Button toggleSortBy;
    
    @FXML
    private Label nameLabel;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        nameField.setText("Hello World!");
        //lView.getItems().add();
    }
    
    @FXML 
    public void SelectItemClick(MouseEvent arg0) {
        ToDoItem item = (ToDoItem)lView.getSelectionModel().getSelectedItem();
        nameLabel.setText(item.Name());
        nameLabel.setVisible(true);
    }
    @FXML 
    public void newList(ActionEvent event) {
        lView.getItems().clear();
        _toDoList = new ToDoList();
    }
    @FXML 
    public void loadList(ActionEvent event) {
        FileChooser fChooser = new FileChooser();
        fChooser.setTitle("Select ToDo List");
        File f = new File(System.getProperty("user.dir"));
        fChooser.setInitialDirectory(f);
        fChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ToDo List (*.ToDo)", "*.ToDo"));
        f = fChooser.showOpenDialog(new Stage());
        System.out.println(f.getAbsoluteFile());
        this._toDoList.Load(f);
        
    }
    @FXML 
    public void saveList(ActionEvent event) {
        
        FileChooser fChooser = new FileChooser();
        fChooser.setTitle("Save ToDo List");
        File f = new File(System.getProperty("user.dir"));
        fChooser.setInitialDirectory(f);
        fChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ToDo List (*.ToDo)", "*.ToDo"));
        
        f = fChooser.showSaveDialog(new Stage());
        
        this._toDoList.Save(f);
        
    }
    @FXML 
    public void toggleFilter(ActionEvent event) {
        ToDoItem.FilterCompleted = filterCompleted.isSelected();
        this.Display();
    }
    
    @FXML 
    public void toggleSortBy(ActionEvent event) {
        ToDoItem.SortOnPriority = !ToDoItem.SortOnPriority;
        
        if(ToDoItem.SortOnPriority){
            toggleSortBy.setText("Sort By: Priority");
        }else{
            toggleSortBy.setText("Sort By: Due Date");
        }
        
        this.Display();
    }
    
    @FXML 
    public void addItem(ActionEvent event) {
        
        this._toDoList.add(new ToDoItem(nameField.getText(), descField.getText()
                , Priority.valueOf(((RadioButton)priority.getSelectedToggle()).getId().toUpperCase()), dPicker.getValue(), false ));
        
        clearFeilds();
    }
    
    private void clearFeilds(){
        nameField.clear();
        descField.clear();
        priority.selectToggle(low);
        dPicker.setValue(null);
        addItemButton.setDisable(true);
        saveItemButton.setVisible(false);
        cancelItemButton.setVisible(false);
    }
    
    @FXML 
    public void saveItem(ActionEvent event) {
    }
    
    @FXML 
    public void cancelItem(ActionEvent event) {
    }
    
    @FXML 
    public void ValidateItem(ActionEvent event) {
        Validate();
    }
    @FXML 
    public void ValidateItemKeyBoard(KeyEvent event) {
        Validate();
    }
    
    
    public void Validate(){
        if(!"".equals(nameField.getText()) && !nameField.getText().isEmpty() && dPicker.getValue() != null){
            addItemButton.setDisable(false);
        }
        else{
            addItemButton.setDisable(true);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        _toDoList.ListModified.Subscribe(this::Display);
    }    
    
    public boolean Display(){
        lView.getItems().clear();
        
        this._toDoList.Sort();
        
        for(ToDoItem item : this._toDoList.getItems()){
            if(!ToDoItem.FilterCompleted || !item.Completed()){
                lView.getItems().add(item);
            }
        }
        
        lView.refresh();
        return true;
    }
    
}
