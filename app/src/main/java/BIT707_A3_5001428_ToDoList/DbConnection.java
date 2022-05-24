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
        catch(Exception e) {
            System.out.println("Exception: " + e);
        }
    }
    
    /**
     * This method runs a database query to select all data from the 'Task' table.
     */
    public void selectAllFromTaskTable() {
        
        String sql = "SELECT * FROM Task";
        Statement statement = null;
        ResultSet resultSet = null;
        
        try {
            statement = DbConnection.DB.createStatement();
            resultSet = statement.executeQuery(sql);
            // Get number of columns from metadata
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numOfColumns = metaData.getColumnCount();

            // Print results to console:
            System.out.println(
                    "______________________________"
                    + "\n SQL query: " + sql 
                    + "\n"
                    + "\nResults: "
            );
            int i = 1;
            String columnHeaders = "";
            while(i <= numOfColumns) {
                    columnHeaders += ("| " + metaData.getColumnName(i) + " ");
                    i++;
            }
            System.out.println(columnHeaders);
            System.out.println("------------------------------");
            
            while (resultSet.next()) {
                i = 1;
                String tableRow = "";
                while(i <= numOfColumns) {
                    columnHeaders += (metaData.getColumnName(i) + " | ");
                    tableRow += ("| " + resultSet.getString(i) + "          ");
                    i++ ;
                }
                System.out.println(tableRow);
            }
            System.out.println("______________________________");
        }
        // Handle invalid SQL queries
        catch (SQLException e) {
            System.out.println("Exception: " + e);
        } 
    }
}