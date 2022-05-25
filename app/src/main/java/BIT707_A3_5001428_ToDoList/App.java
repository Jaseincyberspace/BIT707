/*
 * BIT707 - Assignment 3
 * Jason Norton - 5001428
 */
package BIT707_A3_5001428_ToDoList;
import java.sql.*;
import java.util.ArrayList;

/**
 * ToDo List Application
 * @author Jason Norton
 */
public class App {
    /**
     * The application entry point
     * @param args An array of command-line arguments (Strings)
     */
    public static Controller controller = new Controller();
        
    public static void main(String[] args) {
        /**
         * Instantiates database connection
         
        DbConnection dbConnection = new DbConnection();
        dbConnection.initDatabase();
        
        /**
         * Queries the database
         
        dbConnection.selectAllFromTaskTable(); 
        
        /**
         * Displays MainForm
         
        MainForm mainForm = new MainForm();
        mainForm.setLocationRelativeTo(null);
        mainForm.pack();
        mainForm.setVisible(true);
        
        ListViewForm listViewForm = new ListViewForm();
        listViewForm.pack();
        listViewForm.setVisible(true);
        */
        controller.run();
    }
}