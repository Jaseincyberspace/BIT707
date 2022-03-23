package BIT707_A1_5001428_ToDoList;
/**
 * ToDo List Application
 * @author Jason Norton
 */
public class App {
    /**
     * The application entry point
     * @param args An array of command-line arguments (Strings)
     */
    public static void main(String[] args) {
        /**
         * Instantiates database connection
         */
        DbConnection dbConnection = new DbConnection();
        dbConnection.initDatabase();
        
        /**
         * Queries the database
         */
        dbConnection.selectAllFromTaskTable();       
    }       
}