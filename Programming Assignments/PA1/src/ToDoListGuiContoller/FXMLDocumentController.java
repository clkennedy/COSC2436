/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ToDoListGuiContoller;


import ListProject.Priority;
import ListProject.ToDoItem;
import ListProject.ToDoList;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author ARustedKnight
 */
public class FXMLDocumentController implements Initializable{
    
    private ToDoList _toDoList = new ToDoList();
    
    private static boolean isItemDisplayOn = false;
    private static ToDoItem currentSelectedItem = null;
    private static File currentLoadedFile = null;
    
    @FXML
    private Label label;
    @FXML
    private ToggleGroup priority;
    @FXML
    private CheckBox filterCompleted;
    @FXML
    private CheckBox autoSave;
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
    private ListView<ToDoItem> lView;
    @FXML
    private Button addItemButton;
    @FXML
    private Button saveItemButton;
    @FXML
    private Button cancelItemButton;
    @FXML
    private Button modifyItemButton;
    @FXML
    private Button deleteItemButton;
    @FXML
    private Button completeItemButton;
    @FXML
    private Button toggleSortBy;
    
    @FXML
    private Label nameLabel;
    @FXML
    private Label descLabel;
    @FXML
    private Label priLabel;
    @FXML
    private Label dueDateLabel;
    @FXML
    private Label completeLabel;
    @FXML
    private Label line1;
    @FXML
    private Label line2;
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        nameField.setText("Hello World!");
        //lView.getItems().add();
    }
    
    @FXML 
    public void SelectItemClick(MouseEvent arg0) {
        SelectItem();
    }
    
    @FXML 
    public void SelectItemKey(KeyEvent event) {
        SelectItem();
    }
    
    private void SelectItem(){
        if(lView.getSelectionModel().getSelectedItem() == null) return;
        
        currentSelectedItem = (ToDoItem)lView.getSelectionModel().getSelectedItem();
        
        setItemDisplayFields();
        
        if(!isItemDisplayOn)
            toggleItemDisplay();
    }
    
    private void setItemDisplayFields(){
        nameLabel.setText(currentSelectedItem.Name());
        descLabel.setText(currentSelectedItem.Description());
        priLabel.setText("Priority: " + currentSelectedItem.Priority().toString());
        dueDateLabel.setText("Due Date: " + ToDoList.DATEFORMATTER.format(currentSelectedItem.DueDate()));
        if(LocalDate.now().compareTo(currentSelectedItem.DueDate()) > 0 && !currentSelectedItem.Completed()){
            dueDateLabel.setTextFill(Color.RED);
        }
        else{
            dueDateLabel.setTextFill(Color.BLACK);
        }
        
        completeLabel.setText(((currentSelectedItem.Completed())?"Completed" : "Not Completed"));
        
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
        
        currentLoadedFile = f;
        autoSave.setDisable(false);
        autoSave.setSelected(true);
        
        this._toDoList.Load(f);
        
        filterCompleted.setSelected(ToDoItem.FilterCompleted);
        if(ToDoItem.SortOnPriority){
            toggleSortBy.setText("Sort By: Priority");
        }else{
            toggleSortBy.setText("Sort By: Due Date");
        }
        
    }
    @FXML 
    public void saveList(ActionEvent event) {
        
        FileChooser fChooser = new FileChooser();
        fChooser.setTitle("Save ToDo List");
        File f = new File(System.getProperty("user.dir"));
        if(currentLoadedFile != null){
            fChooser.setInitialDirectory(new File(currentLoadedFile.getParent()));
            fChooser.setInitialFileName(currentLoadedFile.getName());
        }else{
            fChooser.setInitialDirectory(f);
        }
        fChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ToDo List (*.ToDo)", "*.ToDo"));
        
        f = fChooser.showSaveDialog(new Stage());
        
        this._toDoList.Save(f);
    }
    
    public boolean AutoSave(){
        if(autoSave.isSelected())
           return this._toDoList.Save(currentLoadedFile);
        
        return true;
    }
    
    @FXML 
    public void toggleFilter(ActionEvent event) {
        ToDoItem.FilterCompleted = filterCompleted.isSelected();
        this.Display();
    }
    
    @FXML 
    public void toggleAutoSave(ActionEvent event) {
        ToDoItem.FilterCompleted = filterCompleted.isSelected();
        this.Display();
    }
    
    private void toggleItemDisplay(){
        isItemDisplayOn = !isItemDisplayOn;
        
        modifyItemButton.setVisible(isItemDisplayOn);
        completeItemButton.setVisible(isItemDisplayOn);
        deleteItemButton.setVisible(isItemDisplayOn);
        
        nameLabel.setVisible(isItemDisplayOn);
        descLabel.setVisible(isItemDisplayOn);
        priLabel.setVisible(isItemDisplayOn);
        completeLabel.setVisible(isItemDisplayOn);
        dueDateLabel.setVisible(isItemDisplayOn);
        
        line1.setVisible(isItemDisplayOn);
        line2.setVisible(isItemDisplayOn);
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
        addNewItemToList();
    }
    
    private void addNewItemToList(){
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
        
        currentSelectedItem.Name(nameField.getText());
        currentSelectedItem.Description(descField.getText());
        currentSelectedItem.Priority(Priority.valueOf(((RadioButton)priority.getSelectedToggle()).getId().toUpperCase()));
        currentSelectedItem.DueDate(dPicker.getValue());
        
        AutoSave();
        Display();
        clearFeilds();
        
        addItemButton.setVisible(true);
        saveItemButton.setVisible(false);
        cancelItemButton.setVisible(false);
        
        setItemDisplayFields();
    }
    
    @FXML 
    public void cancelItem(ActionEvent event) {
        clearFeilds();
        
        addItemButton.setVisible(true);
        saveItemButton.setVisible(false);
        cancelItemButton.setVisible(false);
    }
    @FXML 
    public void modifyItem(ActionEvent event) {
        
        addItemButton.setVisible(false);
        saveItemButton.setVisible(true);
        cancelItemButton.setVisible(true);
        saveItemButton.setDisable(false);
        cancelItemButton.setDisable(false);
        
        nameField.setText(currentSelectedItem.Name());
        descField.setText(currentSelectedItem.Description());
        for(Toggle t: priority.getToggles()){
            System.out.println(((RadioButton)t).getId().toUpperCase() + " : " + currentSelectedItem.Priority().toString().toUpperCase());
            if(((RadioButton)t).getId().toUpperCase().equals(currentSelectedItem.Priority().toString().toUpperCase())){
                priority.selectToggle(t);
                break;
            }
        }
        
        dPicker.setValue(currentSelectedItem.DueDate());
        
    }
    @FXML 
    public void deleteItem(ActionEvent event) {
        
        this._toDoList.remove(currentSelectedItem);
        toggleItemDisplay();
        
    }
    @FXML 
    public void completeItem(ActionEvent event) {
        currentSelectedItem.Completed(true);
        setItemDisplayFields();
        Display();
    }
    
    @FXML 
    public void ValidateItem(ActionEvent event) {
        Validate();
    }
    @FXML 
    public void ValidateItemKeyBoard(KeyEvent event) {
        Validate();
    }
    
    @FXML 
    public void ValidateOnEnter(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER && !addItemButton.isDisable()){
            addNewItemToList();
        }
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
        _toDoList.ListModified.Subscribe(this::AutoSave);
        
        descLabel.setWrapText(true);
        descLabel.setPrefWidth(180);
        
        lView.setCellFactory(new Callback<ListView<ToDoItem>, ListCell<ToDoItem>>() {
            @Override
            public ListCell<ToDoItem> call(ListView<ToDoItem> param) {
                return new ListCellFormatter();
            }
        });
        
        nameField.lengthProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    // Check if the new character is greater than LIMIT
                    if (nameField.getText().length() >= 30) {

                        // if it's 11th character then just setText to previous
                        // one
                        nameField.setText(nameField.getText().substring(0, 30));
                    }
                }
            }
        });
        
        descField.lengthProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    // Check if the new character is greater than LIMIT
                    if (descField.getText().length() >= 200) {

                        // if it's 11th character then just setText to previous
                        // one
                        descField.setText(descField.getText().substring(0, 200));
                    }
                }
            }
        });
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
        AutoSave();
        return true;
    }

   
    
}
