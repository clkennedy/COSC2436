/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ListProject;

import Menu.Menu;
import Menu.MenuItem;

/**
 *
 * @author cameron.kennedy
 */
public class ListProjectMain {
    
    private static ToDoList _toDoList = new ToDoList();
    
    public static void main(String args[]){
        Menu menu = new Menu();
        setupMenu(menu);
        menu.start();
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
