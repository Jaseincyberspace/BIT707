/*
 * BIT707 - Assignment 3
 * Jason Norton - 5001428
 */
package BIT707_A3_5001428_ToDoList;

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
        controller.run();
    }
}