/*
 * BIT707 - Assignment 3
 * Jason Norton - 5001428
 */
package BIT707_A3_5001428_ToDoList;

import java.sql.*;
import java.util.ArrayList;

public class Controller {
    /**
     * Controller Attributes
     */
    private DbConnection dbConnection;
    private ArrayList<Task> taskList;
    // Views
    public static ListViewForm listViewForm = null;
    public static CalendarViewForm calendarViewForm = null;
    
    /**
     * Controller Constructor
     */
    public Controller() {
        this.dbConnection = new DbConnection();
        if(taskList == null) {
             this.taskList = new ArrayList<Task>();
        }
    }
    
    public void run() {
        /**
        * Instantiates database connection
        */
        dbConnection.initDatabase();
        getAllTasks();
       /**
        * Displays MainForm as a MDI container
        */
       MainForm mainForm = new MainForm();
       mainForm.setLocationRelativeTo(null);
       mainForm.pack();
       mainForm.setVisible(true);
       /**
        * Displays ListView form inside MainForm
        */
       ListViewForm listViewForm = new ListViewForm();
       listViewForm.pack();
       listViewForm.setVisible(true);
    }
    
    public void getAllTasks() {
        /**
        * Queries the database
        */
        dbConnection.selectAllFromTaskTable(); 
    }
    
    public void displayListView(javax.swing.JDesktopPane jDesktopPane_formContainer) {
        /**
        * Creates a new listView form and displays it on screen
        */
        if(listViewForm != null && calendarViewForm != null) {
            calendarViewForm.setVisible(false);
            listViewForm.setVisible(true);
        }
        else if(listViewForm != null) {
             listViewForm.setVisible(true);
        }
        else {
            listViewForm = new ListViewForm();
            listViewForm.setBounds(0, 0, jDesktopPane_formContainer.getWidth(), jDesktopPane_formContainer.getHeight());
            jDesktopPane_formContainer.add(listViewForm); 
            if(calendarViewForm != null) {
                calendarViewForm.setVisible(false);
            }
            listViewForm.setVisible(true);
        }
    }
    
    public void displayCalendarView(javax.swing.JDesktopPane jDesktopPane_formContainer) {
        /**
        * Creates a new calendarView form and displays it on screen
        */
        if(calendarViewForm != null && listViewForm != null) {
            listViewForm.setVisible(false);
            calendarViewForm.setVisible(true);
        }
        else if(calendarViewForm != null) {
            calendarViewForm.setVisible(true);
        }
        else {
            calendarViewForm = new CalendarViewForm();
            calendarViewForm.setBounds(0, 0, jDesktopPane_formContainer.getWidth(), jDesktopPane_formContainer.getHeight());
            jDesktopPane_formContainer.add(calendarViewForm);
            if(listViewForm != null) {
                listViewForm.setVisible(false);
            }
            calendarViewForm.setVisible(true);
        }
    }
}
