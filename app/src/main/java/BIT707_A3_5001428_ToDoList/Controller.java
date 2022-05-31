/*
 * BIT707 - Assignment 3
 * Jason Norton - 5001428
 */
package BIT707_A3_5001428_ToDoList;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class Controller {
    final private DbConnection dbConnection;
    private static ArrayList<Task> taskList;
    private int selectedRow;
    private int selectedTaskNumber;
    // Views
    public static MainForm mainForm = null;
    public static ListViewForm listViewForm = null;
    public static CalendarViewForm calendarViewForm = null;
    
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
        mainForm = new MainForm();
        mainForm.setLocationRelativeTo(null);
        mainForm.pack();
        mainForm.setVisible(true);
        // Displays ListView form inside MainForm
        listViewForm = new ListViewForm();
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
            taskDescription = allTaskData.get(i).get(2); 
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
     public void populateTableData(JTable jTable) {  
        String tableName = jTable.getName();
        DefaultTableModel tableModel = (DefaultTableModel)jTable.getModel();
        // Removes any existing row data first
        tableModel.setRowCount(0);
        // Sets table model
        jTable.setModel(tableModel);
        // Sets table listener to keep track of checkbox events
        addTableModelListener(tableModel);     
        
        // Adds row data from list of Task objects
        for (int i = 0; i < taskList.size(); i++) {
            // Breaks down Task object into its attributes (one attribute goes into each column)
            int taskNumber = taskList.get(i).getTaskNumber();
            Boolean taskStatus = taskList.get(i).isTaskStatus();
            String taskName = String.valueOf(taskList.get(i).getTaskName());
            LocalDate taskDate = taskList.get(i).getTaskDate();
            // Formats date for NZ
            // String taskDate = formatDate((tasks.get(i).getTaskDate()));
            // Sets row data
            Object[] rowData = {
                taskNumber,
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
          
    public void updateTableData(JTable jTable) {
        DefaultTableModel tableModel = (DefaultTableModel)jTable.getModel();
        // Removes any existing row data first
        tableModel.setRowCount(0);
        
        // Adds row data from list of Task objects
        for (int i = 0; i < taskList.size(); i++) {
            // Breaks down Task object into its attributes (one attribute goes into each column)
            int taskNumber = taskList.get(i).getTaskNumber();
            Boolean taskStatus = taskList.get(i).isTaskStatus();
            String taskName = String.valueOf(taskList.get(i).getTaskName());
            LocalDate taskDate = taskList.get(i).getTaskDate();
            // Formats date for NZ
            // String taskDate = formatDate((tasks.get(i).getTaskDate()));
            // Sets row data
            Object[] rowData = {
                taskNumber,
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
     
     public void addTableModelListener(DefaultTableModel tableModel) {
         tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                TableModel tableModel = (TableModel)e.getSource();
                if(column >= 0) {
                    Object data = tableModel.getValueAt(row, column);
                    if(data instanceof Boolean) {
                        String taskNumber = String.valueOf(tableModel.getValueAt(row, 0));
                        String taskName = String.valueOf(tableModel.getValueAt(row, 2));
                        String taskDate = String.valueOf(tableModel.getValueAt(row, 3));
                        String taskStatus = String.valueOf(tableModel.getValueAt(row, 1));
                                                 
                        // Updates database 
                        dbConnection.updateTask(taskNumber, taskStatus);
                        // Updates taskList
                        for(int i = 0; i < taskList.size(); i++) {
                            if(taskList.get(i).getTaskNumber() == (int)tableModel.getValueAt(row, 0)) {
                                taskList.get(i).setTaskStatus((Boolean)tableModel.getValueAt(row, 1));
                            }
                        }
                        
                        // Print statement is for testing and debugging only
                        System.out.println("bool changed at row: " + row + " column: " + column);
                    }
                    else if(data instanceof String) {
                        String taskNumber = String.valueOf(tableModel.getValueAt(row, 0));
                        String taskName = String.valueOf(tableModel.getValueAt(row, 2));
                        String taskDate = String.valueOf(tableModel.getValueAt(row, 3));
                        String taskStatus = String.valueOf(tableModel.getValueAt(row, 1));
                        
                        // Updates database 
                        dbConnection.updateTask(taskNumber, taskStatus);
                        // Updates taskList
                        for(int i = 0; i < taskList.size(); i++) {
                            if(taskList.get(i).getTaskNumber() == (int)tableModel.getValueAt(row, 0)) {
                                taskList.get(i).setTaskName((String)tableModel.getValueAt(row, 2));
                                taskList.get(i).setTaskDescription((String)tableModel.getValueAt(row, 3));
                            }
                        }
                        
                        // Print statement is for testing and debugging only
                        System.out.println("strings changed at row: " + row);
                    }   
                }   
            }
        });
     }
             
    /**
     * Renders the date column of jTable to display dates formatted for NZ (dd-MM-yyyy)
     * @param jTable 
     */
    public void renderTableDateColumn(JTable jTable) {
        TableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable jTable, Object value, boolean isSelected, boolean hasFocus, 
            int row, int column) {
                String date = value.toString();
                LocalDate localDate = LocalDate.parse(date);
                value = formatDate(localDate);
                return super.getTableCellRendererComponent(jTable, value, isSelected, hasFocus, row, column);
            }  
        };
        jTable.getColumnModel().getColumn(3).setCellRenderer(tableCellRenderer);
    }
       
    /**
     * Sets default sort order of jTable data based on date column
     * @param jTable 
     */
    public void sortTable(JTable jTable) {
        // Sorts Jtable data by the date column
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(jTable.getModel());
        jTable.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
    }
    
    /**
     * Formats date for New Zealand. Takes a LocalDate value yyyy-MM-dd and returns a string dd-MM-yyyy
     * @param localDate
     * @return 
     */
    public String formatDate(LocalDate localDate) {
        return (localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }
    
    /**
     * Formats date for New Zealand. Takes a String value yyyy-MM-dd and returns a string dd-MM-yyyy
     * @param localDate
     * @return 
     */
     public String formatDate(String date) {
        
         try {
            LocalDate localDate = LocalDate.parse(date);
            return (localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        }
        catch(Exception e) {
            System.out.println("Unable to parse date from string");
            return(date);
        }                 
    }
    
    /**
     * Displays a dialog showing the detailed view of a task when user double clicks on a table row 
     * @param jTable 
     */
    public void addTableMouseClickListener(JTable jTable, JDialog dialog) {
        jTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                // Gets task number from selected table row
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    // Saves row and task number variables for use later (otherwise they are lost when the dialog box is displayed)
                    selectedRow = jTable.getSelectedRow();
                    selectedTaskNumber = (int)jTable.getValueAt(selectedRow, 0);        
                    // Gets selected task from database
                    ArrayList<String> taskComponents = dbConnection.readTask(String.valueOf(selectedTaskNumber));
                    // Reformats date to dd-MM-yyyy
                    taskComponents.set(3, formatDate(taskComponents.get(3)));
                    if(jTable.getName().equals("ListView")) {
                        // Displays task details in dialog box
                        listViewForm.displayTask(taskComponents);
                    }
                    else if(jTable.getName().equals("CalendarView")) {
                        // Displays task details in dialog box
                        calendarViewForm.displayTask(taskComponents);
                    }
                }
            }
        });
    }

    /**
     * Creates a new listView form and displays it on screen
     * @param jDesktopPane_formContainer 
     */
    public void displayListView(JDesktopPane jDesktopPane_formContainer) {
        // Creates a new listView form and sets it's size
        listViewForm = new ListViewForm();
        listViewForm.setBounds(0, 0, jDesktopPane_formContainer.getWidth(), jDesktopPane_formContainer.getHeight());
        listViewForm.pack();
        jDesktopPane_formContainer.add(listViewForm); 
        // Closes any existing calendarView form
        if(calendarViewForm != null) {
            calendarViewForm.dispose();
        }
        // Displays listView form on screen
        listViewForm.setVisible(true);
    }
        
    /**
     * Creates a new calendarView form and displays it on screen.
     * @param jDesktopPane_formContainer 
     */
    public void displayCalendarView(JDesktopPane jDesktopPane_formContainer) {
        // Creates a new calendarView form and sets it's size
        calendarViewForm = new CalendarViewForm();
        calendarViewForm.setBounds(0, 0, jDesktopPane_formContainer.getWidth(), jDesktopPane_formContainer.getHeight());
        calendarViewForm.pack();
        jDesktopPane_formContainer.add(calendarViewForm);
        // Closes any existing listView form
        if(listViewForm != null) {
            listViewForm.dispose();
        }
        // Displays calendarView form on screen
        calendarViewForm.setVisible(true);
    }
    
    public boolean addTask(JTextField taskNameField, JTextArea taskDetailsField, JTextField taskDateField) {
        String taskNumber = "";
        String taskName = "";
        String taskDescription = ""; 
        String taskDate = ""; 
        boolean validInput = false;
        boolean methodSuccessful = false;
        
        // Generates new taskID
        taskNumber = dbConnection.getLargestTaskNumber();
        int nextTaskNumber;
        try {
            nextTaskNumber = Integer.parseInt(taskNumber) + 1;
            taskNumber = String.valueOf(nextTaskNumber);
        }
        catch(NumberFormatException e) {
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
        if(!taskDetailsField.getText().equals("Add task details")) {
            taskDescription = taskDetailsField.getText();
        }
        if(!taskDateField.getText().equals("") && !taskDateField.getText().equals("dd-mm-yyyy")) {
            // Reverses the string to check it can be parsed into a valid ISO date format
            try {
                // Parses string into a LocalDate object with format dd-MM-yyyy 
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
                    methodSuccessful = true;
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
         return methodSuccessful;
    }
    
    public boolean editTask(JTable jTable, JTextField taskNameField, JTextArea taskDetailsField, JTextField taskDateField) {
        String taskNumber = "";
        String taskName = "";
        String taskDescription = taskDetailsField.getText(); // This field can be blank or contain any string (validation not reqiired)
        String taskDate = ""; 
        String taskStatus = "";
        int taskListIndex = -1;
        boolean validInput = false;
        boolean methodSuccessful = false;
        
        // Sets taskID
        taskNumber = String.valueOf(selectedTaskNumber);   
        
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
        
        // Gets task status
        for(int i = 0; i < taskList.size(); i++) {
            if(taskList.get(i).getTaskNumber() == selectedTaskNumber) {
                taskStatus = String.valueOf(taskList.get(i).isTaskStatus());
            }
        }
                        
        // If input is valid a record is added to the database 
        if(validInput) {
            Boolean taskUpdatedInDB = dbConnection.updateTask(taskNumber, taskName, taskDescription, taskDate, taskStatus);
            // If the database update is successful the task is added to the taskList
            if(taskUpdatedInDB) {
                Task task = parseStringsToTask(taskNumber, taskName, taskDescription, taskDate, taskStatus);
                // Updates task in taskList if String to Task parse was successful
                if(task.getTaskNumber() != -1) {
                    for(int i = 0; i < taskList.size(); i++) {
                        if(taskList.get(i).getTaskNumber() == task.getTaskNumber()) {
                            taskList.set(i, task);
                            methodSuccessful = true;
                        }
                    }
                updateTableData(jTable);
                }
                else {
                    System.out.println("Error: Unable to update task in controller's task list");
                    dbConnection.deleteTask(String.valueOf(taskNumber));
                }
            }
            else {
                System.out.println("Error: Unable to update task in database");
            }
        }
        else {
            System.out.println("Error: Unable to update task due to invalid user input");
        }
        return methodSuccessful;
    }
            
    public boolean deleteTask(JTable jTable, int selectedRow, int selectedTaskNumber) {
        boolean methodSuccessful = false;
        if(dbConnection.deleteTask(String.valueOf(selectedTaskNumber))) {
            for(int i = 0; i < taskList.size(); i++) {
                if(taskList.get(i).getTaskNumber() == selectedTaskNumber) {
                    taskList.remove(i);
                    methodSuccessful = true;
                }
            }
            populateTableData(jTable);
        }
        return methodSuccessful;
    }
    
    public String[] getTask() {
        String taskNumber = String.valueOf(selectedTaskNumber);
        String taskName = "";
        String taskDescription = "";
        String taskDate = "";
        String taskStatus = "";
        
        for(int i = 0; i < taskList.size(); i++) {
            if(taskList.get(i).getTaskNumber() == selectedTaskNumber) {
                taskName = taskList.get(i).getTaskName();
                taskDescription = taskList.get(i).getTaskDescription();
                taskDate = formatDate(taskList.get(i).getTaskDate());
                taskStatus = String.valueOf(taskList.get(i).isTaskStatus());
            }
        }
        String[] taskComponents = new String[] {taskNumber, taskName, taskDescription, taskDate, taskStatus};
        return taskComponents;
    }
    
    public boolean editTaskStatus(JTable jTable) {
        boolean methodSuccessful = false;
        // Finds the selected task in the taskList
        for(int i = 0; i < taskList.size(); i++) {
            if(taskList.get(i).getTaskNumber() == selectedTaskNumber) {
                                               
                // Finds the current status of the specified task
                boolean status = taskList.get(i).isTaskStatus();
                String updatedStatus = String.valueOf(!status);
                // Updates the database to change the status to the opposite of what it currently is
                 if(dbConnection.updateTaskStatus(String.valueOf(selectedTaskNumber), updatedStatus)) {
                    // Updates the taskList
                    taskList.get(i).setTaskStatus(!status);
                    // Repopulates the table data
                    populateTableData(jTable);
                    methodSuccessful = true;
     
                    // Print to console for debugging purposes
                    System.out.println(selectedTaskNumber + " was found at index " + selectedRow);  
                    System.out.println(taskList.get(i).getTaskNumber() + " " + taskList.get(i).getTaskName() + " " + taskList.get(i).getTaskDescription() + " " + taskList.get(i).isTaskStatus());
                };
            }
            else {
                System.out.println("Unable to update task status. Task number " + selectedTaskNumber + " not found in task list");
            }
        }
        // Repopulates the table data
        populateTableData(jTable);
        listViewForm.dispose();
    return methodSuccessful;
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