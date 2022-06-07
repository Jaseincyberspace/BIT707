/*
 * BIT707 - Assignment 3
 * Jason Norton - 5001428
 */
package BIT707_A3_5001428_ToDoList;

import java.sql.*;
import java.util.ArrayList;

/**
 * This class connects the ToDo List application to an SQLite database
 */
public class DbConnection {
    public static Connection DB;
        
    /**
     * Initializes a connection to the SQLite database
     */
    
    public void initDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS Task (" +
            "taskNumber INTEGER PRIMARY KEY, " +
            "taskName TEXT NOT NULL, " +
            "taskDescription TEXT, " +
            "dueDate TEXT CHECK(dueDate is null or length(dueDate) == 10), " +
            "status TEXT NOT NULL CHECK(status == \"true\" or status == \"false\")" +");";
        Statement statement = null;
        
        try {
            DB = DriverManager.getConnection("jdbc:sqlite:toDo.db");
            statement = DB.createStatement();
            statement.executeUpdate(sql);
        }
        catch(SQLException e) {
            // Print error message to console
            System.out.println("Unable to connect to database. Exception: " + e);
        }
    }
    
    /**
     * Finds the highest integer in the taskNumber column of database table 'Task'
     * @return a string denoting the highest taskNumber value found in database table 'Task'
     */
    public String getLargestTaskNumber() {
        String sql = "SELECT MAX(taskNumber) from TASK;";
        Statement statement = null;
        ResultSet resultSet = null;
        String maxID = "";
        
        try {
            // Execute SQLite statement
            statement = DbConnection.DB.createStatement();
            resultSet = statement.executeQuery(sql);
            if(resultSet.next()) {
                maxID = resultSet.getString(1);
            }
        }
        catch(SQLException e) {
            App.controller.displayErrorMessage("Unable to get largest task number from database. Please check your connection");
            System.out.println("Exception: " + e);
        }
        return maxID;
    }
    
    /**
     * Generates a SQLite query string to add a new task record in the 'Task' table
     * The string is passed to insertUpdateOrDelete() for execution
     * @param taskNumber
     * @param taskName
     * @param taskDescription
     * @param taskDate
     * @return true if the task was added to the database successfully, otherwise false
     */
    public boolean createTask(String taskNumber, String taskName, String taskDescription, String taskDate) {
        try{
            String sql = "INSERT INTO Task(taskNumber, taskName, taskDescription, dueDate, status) VALUES (" 
                + "\"" + taskNumber + "\",\"" + taskName + "\",\"" + taskDescription + "\",\"" + taskDate + "\",\"" + "false\");";
            return insertUpdateOrDelete(sql); 
        }
        catch(Exception e) {
            App.controller.displayErrorMessage("Unable to insert task into database. Please check your connection");
            System.out.println("Exception: " + e);
            return false;
        }
    }
    
    /**
     * Generates a SQLite query string to update one or more of the values in an existing task record in the 'Task' table
     * The string is passed to insertUpdateOrDelete() for execution
     * @param taskNumber
     * @param taskName
     * @param taskDescription
     * @param taskDate
     * @param taskStatus
     * @return true if the task was updated successfully in the database, otherwise false
     */
    public boolean updateTask(String taskNumber, String taskName, String taskDescription, String taskDate, String taskStatus) {
        try {
            String sql = "UPDATE Task SET taskName = " + "\"" + taskName + "\", taskDescription = " + "\"" + taskDescription + "\", dueDate = "
            + "\"" + taskDate + "\", status = " + "\"" + taskStatus +  "\"" + " WHERE taskNumber = " + "\"" + taskNumber + "\";";
            return insertUpdateOrDelete(sql); 
        }
        catch(Exception e) {
            App.controller.displayErrorMessage("Unable to update task in database. Please check your connection");
            System.out.println("Exception: " + e);
            return false;
        }
    }
    
    /**
     * Generates a SQLite query string to update the status value of an existing task record in the 'Task' table
     * The string is passed to insertUpdateOrDelete() for execution
     * @param taskNumber
     * @param taskStatus
     * @return true if the task's status value was updated successfully in the database, otherwise false
     */
    public boolean updateTaskStatus(String taskNumber, String taskStatus) {
        try {
            String sql = "UPDATE Task SET status = \"" + taskStatus + "\" WHERE taskNumber = \"" + taskNumber + "\";";
            return insertUpdateOrDelete(sql); 
        }
        catch(Exception e) {
            App.controller.displayErrorMessage("Unable to update task status in database. Please check your connection");
            System.out.println("Exception: " + e);
            return false;
        }
    }
    
    /**
     * Runs a SQLite database query on the 'Task' table to get a task with a given taskNumber (if one exists). 
     * @param taskNumber
     * @return an arrayList of strings containing one table row where taskNumber matches the one supplied, or an empty list if no such table row exists
     */
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
            App.controller.displayErrorMessage("Unable to find task number " + taskNumber + " in database");
            System.out.println("Exception: " + e);
        }
        return tableRow;
    }
        
    /**
     * Runs a database query to select all data from the 'Task' table.
     * @return an ArrayList of ArrayLists. Each list contains one table row
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
            // Print error message to console
            System.out.println("Unable to retrieve tasks from database. Exception: " + e);
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
        try {
            String sql = "DELETE FROM Task WHERE taskNumber = " + "\"" + taskNumber + "\";";
        return insertUpdateOrDelete(sql);  
        }
        catch(Exception e) {
            App.controller.displayErrorMessage("Unable to delete task number " + taskNumber + "from database");
            System.out.println("Exception: " + e);
            return false;
        }
        
    }
    
    /**
     * Executes insert, update, or delete SQLite queries and returns a boolean value to confirm success or failure
     * @param sqlQuery
     * @return true if the query was executed successfully, otherwise false
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