/*
 * BIT707 - Assignment 3
 * Jason Norton - 5001428
 */
package BIT707_A3_5001428_ToDoList;

import java.sql.*;
import java.util.ArrayList;

/**
 * This class connects the ToDo List application to an SQLite database
 * @author Jason Norton
 */
public class DbConnection {
    /**
     * Initializes a connection to the SQLite database
     */
    public static Connection DB;
    public void initDatabase() {
        try {
            DbConnection.DB = DriverManager.getConnection("jdbc:sqlite:toDo.db");
        }
        catch(SQLException e) {
            System.out.println("Exception: " + e);
        }
    }
    
    /**
     * Generates a SQLite query string to add a new task record in the 'Task' table
     * The string is passed to insertUpdateOrDelete() for execution
     * @param taskNumber
     * @param taskName
     * @param taskDescription
     * @param taskDate
     * @return 
     */
    public boolean createTask(String taskNumber, String taskName, String taskDescription, String taskDate) {
        String sql = "INSERT INTO Task(taskNumber, taskName, taskDescription, dueDate, status) VALUES (" 
                + "\"" + taskNumber + "\",\"" + taskName + "\",\"" + taskDescription + "\",\"" + taskDate + "\",\"" + "false\");";
        return insertUpdateOrDelete(sql); 
    }
        
    public ArrayList readTask(String taskNumber) {
        String sql = "SELECT * FROM Task WHERE taskNumber = " + "\"" + taskNumber + "\";";
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<String> tableRow = new ArrayList<String>();
        
        try {
            // Execute SQLite statement
            statement = DbConnection.DB.createStatement();
            resultSet = statement.executeQuery(sql);
            
            // Get number of columns from metadata
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numOfColumns = metaData.getColumnCount();
            
            // Store resultSet data in a list
            if(resultSet.next()) {
                int i = 1;
                while(i <= numOfColumns) {
                    tableRow.add(resultSet.getString(i));
                    i++;
                }
            }
        }
        // Handle invalid SQL queries
        catch(SQLException e) {
            System.out.println("Exception: " + e);
        }
        return tableRow;
    }
    
    /**
     * Generates a SQLite query string to update the status of a selected task in the 'Task' table
     * The string is passed to insertUpdateOrDelete() for execution
     * @param taskNumber
     * @param status
     * @return 
     */
    public boolean updateTaskStatus (String taskNumber, String status) {
        String sql = "UPDATE Task SET status = " + "\"" + status + "\"" + "WHERE taskNumber = " + "\"" + taskNumber + "\";";
        return insertUpdateOrDelete(sql);
    }
    
    /**
     * Runs a database query to select all data from the 'Task' table.
     * @return ArrayList
     */
    public ArrayList readAllTasks() {
        
        String sql = "SELECT * FROM Task";
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<ArrayList<String>> listOfTasks = new ArrayList<>();
        
        try {
            // Execute SQLite statement
            statement = DbConnection.DB.createStatement();
            resultSet = statement.executeQuery(sql);
            
            // Get number of columns from metadata
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numOfColumns = metaData.getColumnCount();
            
            // Store resultSet data in a list           
            while(resultSet.next()) {
                int i = 1;
                ArrayList<String> tableRow = new ArrayList<>();
                
                while (i <= numOfColumns) {
                    tableRow.add(resultSet.getString(i));
                    i++;
                }
                listOfTasks.add(tableRow);
            } 
        }
        // Handle invalid SQL queries
        catch(SQLException e) {
            System.out.println("Exception: " + e);
        } 
        return listOfTasks;
    }
    
    /**
     * Generates a SQLite query string to remove the selected task from the 'Task' table
     * The string is passed to insertUpdateOrDelete() for execution
     * @param taskNumber
     * @return 
     */
    public boolean deleteTask(String taskNumber) {
        String sql = "DELETE FROM Task WHERE taskNumber = " + "\"" + taskNumber + "\";";
        return insertUpdateOrDelete(sql);  
    }
    
    /**
     * Executes insert, update, or delete SQLite queries and returns a boolean value to confirm success or failure
     * @param sqlQuery
     * @return 
     */
    public boolean insertUpdateOrDelete(String sqlQuery) {
        Statement statement = null;
        boolean resultOfQuery = false;
        
        try {
            // Execute SQLite statement
            statement = DbConnection.DB.createStatement();
            statement.executeUpdate(sqlQuery);
            resultOfQuery = true;
        }
       
        catch(SQLException e) {
            // Handle invalid SQL queries
            System.out.println("Exception: " + e);
        } 
        return resultOfQuery;   
    }
}