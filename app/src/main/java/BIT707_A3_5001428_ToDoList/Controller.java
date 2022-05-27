/*
 * BIT707 - Assignment 3
 * Jason Norton - 5001428
 */
package BIT707_A3_5001428_ToDoList;
import java.awt.Color;
import java.awt.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class Controller {
    // Attributes
    final private DbConnection dbConnection;
    private ArrayList<Task> taskList;
    // Views
    public static ListViewForm listViewForm = null;
    public static CalendarViewForm calendarViewForm = null;
    
    /**
     * Controller constructor
     */
    public Controller() {
        this.dbConnection = new DbConnection();
        if(taskList == null) {
             this.taskList = new ArrayList<>();
        }
    }
    
    /**
    * @return the taskList
    */
    public ArrayList<Task> getTaskList() {
        return taskList;
    }
        
    /**
    * Instantiates database connection, gets data and displays the listView form
    */
    public void run() {
        // Get data from database
        dbConnection.initDatabase();
        getAllTasks();
        // Displays MainForm as a MDI container
        MainForm mainForm = new MainForm();
        mainForm.setLocationRelativeTo(null);
        mainForm.pack();
        mainForm.setVisible(true);
        // Displays ListView form inside MainForm
        listViewForm = new ListViewForm();
        listViewForm.pack();
        listViewForm.setVisible(true);
    }
    
    /**
    * Queries the database to create a list of all tasks
    */
    public void getAllTasks() {
        ArrayList<ArrayList<String>> allTaskData = (dbConnection.readAllTasks()); 
        // Creates Task objects from data returned by query
        int listLength = allTaskData.size();
        for (int i = 0; i < listLength; i++) {
            // Validates data retrieved from database
            int taskNumber = 0;
            String taskName = "";
            String taskDescription = "";
            LocalDate taskDate = LocalDate.now();
            boolean taskStatus = false;
            String errorType = "";            
            
            try {
                taskNumber = Integer.parseInt(allTaskData.get(i).get(0)); 
            }  
            catch(NumberFormatException e) {
                errorType = "taskNumber";
                System.out.println("EXCEPTION:" + e);
            }                   
            taskName = allTaskData.get(i).get(1); 
            taskDescription = allTaskData.get(i).get(1); 
            try {
                taskDate = LocalDate.parse(allTaskData.get(i).get(3));                               
            }
            catch(java.time.format.DateTimeParseException e) {
                errorType = "taskDate";
                System.out.println("EXCEPTION:" + e);
            } 
            try {
                taskStatus = Boolean.parseBoolean(allTaskData.get(i).get(4)); 
            }
            catch(Exception e) {
                errorType = "taskStatus";
                System.out.println("EXCEPTION:" + e);
            }
            // Creates a new task with validated input data 
            if (errorType.equals("")) {
                Task task = new Task(
                    taskNumber, 
                    taskName, 
                    taskDescription, 
                    taskDate,
                    taskStatus);
                // Adds task to the Controller's taskList
                taskList.add(task);
            }
            else {
                switch (errorType) {
                    case "taskNumber":
                        System.out.println("Unable to parse task number");
                        break;
                    case "taskDate":
                        System.out.println("Unable to parse task date");
                        break;
                    case "taskStatus":
                        System.out.println("Unable to parse task status");
                        break; 
                    default: 
                        break;
                }      
            }   
        }
    }
    
    /**
     * Uses the default JTable data model to add row data to the table
     * @param tasks
     * @param jTable 
     */
    public void populateTableData(ArrayList<Task> tasks, javax.swing.JTable jTable) {  
        DefaultTableModel tableModel = (DefaultTableModel)jTable.getModel();
        // Removes any existing row data first
        tableModel.setRowCount(0);
        jTable.setModel(tableModel);
        // Adds row data from list of Task objects
        for (int i = 0; i < tasks.size(); i++) {
            // Breaks down Task object into its attributes (one attribute goes into each column)
            Boolean taskStatus = tasks.get(i).isTaskStatus();
            String taskName = String.valueOf(tasks.get(i).getTaskName());
            LocalDate taskDate = tasks.get(i).getTaskDate();
            // Formats date for NZ
            // String taskDate = formatDate((tasks.get(i).getTaskDate()));
            // Sets row data
            Object[] rowData = {
                taskStatus, 
                taskName,
                taskDate
            };
            tableModel.addRow(rowData);
        }
        // Displays dates as dd-mm-yyyy        
        renderTableDateColumn(jTable);
        // Sorts table by date column
        sortTable(jTable);
        // Notifies of changes to the data model
        tableModel.fireTableDataChanged();
    }
    
    /**
     * Renders the date column of jTable to display dates formatted for NZ (dd-MM-yyyy)
     * @param jTable 
     */
    public void renderTableDateColumn(javax.swing.JTable jTable) {
        TableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable jTable, Object value, boolean isSelected, boolean hasFocus, 
            int row, int column) {
                String date = value.toString();
                LocalDate localDate = LocalDate.parse(date);
                value = formatDate(localDate);
                return super.getTableCellRendererComponent(jTable, value, isSelected, hasFocus, row, column);
            }  
        };
        jTable.getColumnModel().getColumn(2).setCellRenderer(tableCellRenderer);
    }
    
    /**
     * Sets default sort order of jTable data based on date column
     * @param jTable 
     */
    public void sortTable(javax.swing.JTable jTable) {
        // Sorts Jtable data by the date column
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(jTable.getModel());
        jTable.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
    }
    
    /**
     * Formats date for New Zealand
     * @param localDate
     * @return 
     */
    public String formatDate(LocalDate localDate) {
        return (localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }
    
    /**
     * Creates a new listView form and displays it on screen
     * @param jDesktopPane_formContainer 
     */
    public void displayListView(javax.swing.JDesktopPane jDesktopPane_formContainer) {
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
            listViewForm.pack();
            jDesktopPane_formContainer.add(listViewForm); 
            if(calendarViewForm != null) {
                calendarViewForm.setVisible(false);
            }
            listViewForm.setVisible(true);
        }
    }
    
    /**
     * Creates a new calendarView form and displays it on screen
     * @param jDesktopPane_formContainer 
     */
    public void displayCalendarView(javax.swing.JDesktopPane jDesktopPane_formContainer) {
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
    
    public boolean addTask(
    javax.swing.JTextField taskNameField, 
    javax.swing.JTextArea taskDetailsField, 
    javax.swing.JTextField taskDateField) {
        String taskNumber = "";
        String taskName = "";
        String taskDescription = taskDetailsField.getText(); // This field can be blank or contain any string (validation not reqiired)
        String taskDate = ""; 
        boolean validInput = false;
        
        // Generates new taskID
        try {
            taskNumber = String.valueOf(taskList.size() + 1);
        }
        catch(Exception e) {
            System.out.println("Exception: " + e);
        }   
        
        // Validates input
        if(!taskNameField.getText().equals("") && !taskNameField.getText().equals("Enter task name")) {
            taskName = taskNameField.getText();
            validInput = true;
        }
        else {
            // Displays error message to user if input is invalid
            taskNameField.setText("*Task name is required");
            taskNameField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.RED));
            validInput = false;
        }
        if(!taskDateField.getText().equals("") && !taskDateField.getText().equals("dd-mm-yyyy")) {
            // Reverses the string to check it can be parsed into a valid ISO date format
            
            try {
                // Parses string into date with format dd-MM-yyyy 
                String unverifiedDate = taskDateField.getText();
                LocalDate dateNZFormat = LocalDate.parse(unverifiedDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                // Parses date into ISO format for storage in the database
                LocalDate localDate = LocalDate.of(dateNZFormat.getYear(), dateNZFormat.getMonth(), dateNZFormat.getDayOfMonth());
                localDate.format(DateTimeFormatter.ISO_DATE);
                taskDate = String.valueOf(localDate);
            }
            catch(Exception e) {
                // Displays error message to user if input is invalid
                taskDateField.setText("*Must be: dd-mm-yyyy");
                taskDateField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.RED));
                validInput = false;
            }
        }
        else {
            // Displays error message to user if input is invalid
            taskDateField.setText("*Date required");
            taskDateField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.RED));
            validInput = false;
        }
                        
        // If input is valid a record is added to the database 
        if(validInput) {
            Boolean taskAddedToDB = dbConnection.createTask(taskNumber, taskName, taskDescription, taskDate);
            // If the database update is successful the task is added to the taskList
            if(taskAddedToDB) {
                Task task = parseStringsToTask(taskNumber, taskName, taskDescription, taskDate, "false");
                if(task.getTaskNumber() != -1) {
                    taskList.add(task);
                    return true;
                }
                else {
                    System.out.println("Error: Unable to add task to controller's task list");
                    dbConnection.deleteTask(String.valueOf(taskNumber));
                }
            }
            else {
                System.out.println("Error: Unable to add task to database");
            }
        }
        else {
            System.out.println("Error: Unable to add task due to invalid user input");
        }
    return false;
    }
    
    /**
     * Parses String data into types required for task object. Returns -1 if successful, 
     * otherwise returns an int relative to the argument index of the parameter that could not be parsed
     * @param _taskNumber
     * @param _taskName
     * @param _taskDescription
     * @param _taskDate
     * @param _taskStatus
     * @return 
     */
    public Task parseStringsToTask(String _taskNumber, String _taskName, String _taskDescription, String _taskDate, String _taskStatus) {   
        int taskNumber = -1;
        String taskName = _taskName;
        String taskDescription = _taskDescription;
        LocalDate taskDate = LocalDate.now();
        boolean taskStatus = false;
        int invalidParameter = -1;            

        // Tries to parse parameters
        try {
            taskNumber = Integer.parseInt(_taskNumber); 
        }  
        catch(NumberFormatException e) {
            invalidParameter = 1;
            System.out.println("EXCEPTION:" + e);
        }                   
        try {
            taskDate = LocalDate.parse(_taskDate);                               
        }
        catch(java.time.format.DateTimeParseException e) {
            invalidParameter = 4;
            System.out.println("EXCEPTION:" + e);
        } 
        try {
            taskStatus = Boolean.parseBoolean(_taskStatus); 
        }
        catch(Exception e) {
            invalidParameter = 5;
            System.out.println("EXCEPTION:" + e);
        }
        
        // Creates a new Task object with default values
        LocalDate localDate = LocalDate.now();
        Task task = new Task(-1, "taskName", "taskDescription", localDate, false);
        
        switch(invalidParameter) {
            // All parameters parsed ok so the Task is created with the given values
            case -1:
                task = new Task(taskNumber, taskName, taskDescription, taskDate, taskStatus);
                break;
            // One or more parameters could not be parsed 
            // Task retains default values and taskNumber == -1 to identify it as a failed attempt
            default:
                task = new Task(taskNumber, taskName, taskDescription, taskDate, taskStatus);
                break;           
        }   
        return task;       
    }
}
