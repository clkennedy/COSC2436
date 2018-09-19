/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ToDoListGuiContoller;

import ListProject.Priority;
import ListProject.ToDoItem;
import ListProject.ToDoList;
import java.time.LocalDate;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;

/**
 *
 * @author cameron.kennedy
 */
public class ListCellFormatter extends ListCell<ToDoItem>{
    
    public void ListCellFormatter(){}
    
    @Override
    protected void updateItem(ToDoItem item, boolean empty){
        super.updateItem(item, empty);
        setText(item == null ? "" : ((!item.Completed() && item.DueDate().compareTo(LocalDate.now()) < 0) ? "! - " :"") + item.Name() + " | " 
                + ((ToDoItem.SortOnPriority)? item.Priority().toString() : ToDoList.DATEFORMATTER.format(item.DueDate()) ) + " | " 
                + ((item.Completed())? "Completed" : "Not Completed" )
        );
        setTextFill(((item != null) ? (item.Completed())? Color.FORESTGREEN : 
                (item.DueDate().compareTo(LocalDate.now()) < 0) ? Color.RED :
                (item.Priority() == Priority.LOW) ? Color.DARKCYAN :
                (item.Priority() == Priority.MEDIUM) ? Color.BLACK :
                (item.Priority() == Priority.HIGH) ? Color.DARKGOLDENROD :
                (item.Priority() == Priority.URGENT) ? Color.FIREBRICK :
                          Color.BLACK : Color.BLACK
        ));
    }
    
}
