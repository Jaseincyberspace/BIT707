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
     * Adds a new task record to the database
     * @param taskNumber
     * @param taskName
     * @param taskDescription
     * @param taskDate
     * @return 
     */
    public boolean createTask(String taskNumber, String taskName, String taskDescription, String taskDate) {
        String sql = "INSERT INTO Task(taskNumber, taskName, taskDescription, dueDate, status) VALUES (" 
                + "\"" + taskNumber + "\",\"" + taskName + "\",\"" + taskDescription + "\",\"" + taskDate + "\",\"" + "false\");";
        Statement statement = null;
        Boolean resultOfQuery = false;
        
        try {
            // Execute SQLite statement
            statement = DbConnection.DB.createStatement();
            statement.executeUpdate(sql);
            resultOfQuery = true;
        }
       
        catch(SQLException e) {
            // Handle invalid SQL queries
            System.out.println("Exception: " + e);
        } 
        return resultOfQuery;        
    }
    
    /**
     * This method runs a database query to select all data from the 'Task' table.
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
            while (resultSet.next()) {
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
        catch (SQLException e) {
            System.out.println("Exception: " + e);
        } 
        return listOfTasks;
    }
    
    public boolean deleteTask(String taskNumber) {
        String sql = "DELETE FROM Task WHERE taskNumber = " + "\"" + taskNumber + "\";";
        Statement statement = null;
        Boolean resultOfQuery = false;
        
        try {
            // Execute SQLite statement
            statement = DbConnection.DB.createStatement();
            statement.executeUpdate(sql);
            resultOfQuery = true;
        }
       
        catch(SQLException e) {
            // Handle invalid SQL queries
            System.out.println("Exception: " + e);
        } 
        return resultOfQuery;   
    }
}