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

/**
 * Determines what action to take when a user completes an action. Retrieves any required data from database and sends it to the view
 */
public class Controller {
    final private DbConnection dbConnection;
    private static ArrayList<Task> taskList;
    private static ArrayList<Task> mondayList;
    private static ArrayList<Task> tuesdayList;
    private static ArrayList<Task> wednesdayList;
    private static ArrayList<Task> thursdayList;
    private static ArrayList<Task> fridayList;
    private static ArrayList<Task> saturdayList;
    private static ArrayList<Task> sundayList;
    private int selectedRow;
    private int selectedTaskNumber;
    private LocalDate[] selectedWeek;
    // Views
    public static MainForm mainForm = null;
    public static ListViewForm listViewForm = null;
    public static CalendarViewForm calendarViewForm = null;
    
    public Controller() {
        // Establishes database connection
        this.dbConnection = new DbConnection();
        // Instantitates taskList if it doesn't already exist
        if(taskList == null) {
             this.taskList = new ArrayList<>();
        }
    }
    
    /**
    * Gets the taskList 
    * @return a list of all tasks retrieved from the database
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
    }
    
    public void displayErrorMessage(String message) {
        listViewForm.displayErrorMessage(message);
    }
    
    /**
    * Queries the database to create a list of all tasks
    */
    public void getAllTasks() {
        ArrayList<ArrayList<String>> allTaskData = (dbConnection.readAllTasks()); 
        // Creates Task objects from data returned by query
        for(ArrayList tableRow : allTaskData) {
            // Validates data retrieved from database
            int taskNumber = 0;
            String taskName = String.valueOf(tableRow.get(1));
            String taskDescription = String.valueOf(tableRow.get(2)); 
            LocalDate taskDate = LocalDate.now();
            boolean taskStatus = false;
            boolean validData = true;            
            // Checks to ensure data can be parsed 
            try {
                taskNumber = Integer.parseInt((String)tableRow.get(0)); 
            }  
            catch(NumberFormatException e) {
                validData = false;
                displayErrorMessage("Invalid task number retrieved from database. Please contact your system administrator for assistance");
                System.out.println("Unable to parse task number " + "EXCEPTION: " + e);
            }
            try {
                taskDate = LocalDate.parse((String)tableRow.get(3));                               
            }
            catch(java.time.format.DateTimeParseException e) {
                validData = false;
                displayErrorMessage("Invalid task date retrieved from database. Please contact your system administrator for assistance");
                System.out.println("Unable to parse task date " + "EXCEPTION: " + e);
            } 
            try {
                taskStatus = Boolean.parseBoolean((String)tableRow.get(4)); 
            }
            catch(Exception e) {
                validData = false;
                displayErrorMessage("Invalid task status retrieved from database. Please contact your system administrator for assistance");
                System.out.println("Unable to parse task status " + "EXCEPTION: " + e);
            }
            // Creates a new task with validated input data 
            if (validData) {
                Task task = new Task(
                    taskNumber, 
                    taskName, 
                    taskDescription, 
                    taskDate,
                    taskStatus);
                // Adds task to the Controller's taskList
                taskList.add(task);
            }
        }
    }
     
    /**
     * Uses the default JTable data model to add row data to the table
     * @param jTable 
     */
     public void populateTableData(JTable jTable) {
        DefaultTableModel tableModel = (DefaultTableModel)jTable.getModel();
        // Removes any existing row data first 
        tableModel.setRowCount(0);
        // Sets table model
        jTable.setModel(tableModel);
        // Sets table listener to keep track of checkbox events
        addTableModelListener(tableModel);     
        
        // Generates data differently depending on whether it is being sent to listView or CalendarView
        String tableName = jTable.getName();
        
        // ------ ListView Data: ------
        if(tableName.equals("ListView")) {
            // Adds row data from list of Task objects
            for(Task task : taskList) {
                generateRowData(task, tableModel);
            }
            // Sorts table by date column (Only for listView)
            sortTable(jTable);
        }
        
        // ------ CalendarView Data: ------
        else {
            // Separates data into days of the week for the selected week 
            mondayList = new ArrayList<>();
            tuesdayList = new ArrayList<>();
            wednesdayList = new ArrayList<>();
            thursdayList = new ArrayList<>();
            fridayList = new ArrayList<>();
            saturdayList = new ArrayList<>();
            sundayList = new ArrayList<>();
            
            // These are pseudo "tasks" - Used as day of the week headers in the CalendarView table. HTML sytling is added to make them bold.
            LocalDate date = LocalDate.of(1582,10,15); 
            Task monday = new Task(-1, "<html><b>Monday</b></html>", "", date, false);
            Task tuesday = new Task(-1, "<html><b>Tuesday</b></html>", "", date, false);
            Task wednesday = new Task(-1, "<html><b>Wednesday</b></html>", "", date, false);
            Task thursday = new Task(-1, "<html><b>Thursday</b></html>", "", date, false);
            Task friday = new Task(-1, "<html><b>Friday</b></html>", "", date, false);
            Task saturday = new Task(-1, "<html><b>Saturday</b></html>", "", date, false);
            Task sunday = new Task(-1, "<html><b>Sunday</b></html>", "", date, false);
            mondayList.add(monday);
            tuesdayList.add(tuesday);
            wednesdayList.add(wednesday);
            thursdayList.add(thursday);
            fridayList.add(friday);
            saturdayList.add(saturday);
            sundayList.add(sunday);
            
            // Adds tasks to the relevant list of Task objects depending on the day of the week they are scheduled for
            for(Task task : taskList) {
                LocalDate taskDate = task.getTaskDate();
                int dayOfWeek = taskDate.getDayOfWeek().getValue();
                if(taskDate.isEqual(selectedWeek[0]) || taskDate.isAfter(selectedWeek[0])) {
                    if(taskDate.isBefore(selectedWeek[1]) || taskDate.isEqual(selectedWeek[1])) {
                        switch (dayOfWeek) {
                            case 1:
                                mondayList.add(task);
                                break;
                            case 2:
                                tuesdayList.add(task);
                                break;
                            case 3:
                                wednesdayList.add(task);
                                break;
                            case 4:
                                thursdayList.add(task);
                                break;
                            case 5:
                                fridayList.add(task);
                                break;
                            case 6:
                                saturdayList.add(task);
                                break;
                            case 7:
                                sundayList.add(task);
                                break;
                            default:
                                displayErrorMessage("Unable to determine which day of the week task number " + task.getTaskNumber()
                                    + "falls on with date: " + String.valueOf(task.getTaskDate()));
                                System.out.println("Unable to determine which day of the week task number " + task.getTaskNumber()
                                    + "falls on with date: " + String.valueOf(task.getTaskDate()));
                                break;
                        }   
                    }
                }
            }
            // Adds row data from lists of Task objects
            for(Task task : mondayList) {
                generateRowData(task, tableModel);
            } 
            for(Task task : tuesdayList) {
                generateRowData(task, tableModel);
            } 
            for(Task task : wednesdayList) {
                generateRowData(task, tableModel);
            } 
            for(Task task : thursdayList) {
                generateRowData(task, tableModel);
            } 
            for(Task task : fridayList) {
                generateRowData(task, tableModel);
            } 
            for(Task task : saturdayList) {
                generateRowData(task, tableModel);
            } 
            for(Task task : sundayList) {
                generateRowData(task, tableModel);
            } 
        }              
        // Displays dates as dd-mm-yyyy        
        renderTableDateColumn(jTable);
        // Notifies table model listeners of changes to the data model
        tableModel.fireTableDataChanged();
    }

    /**
     * Takes a task object and parses it into an Object array before sending it to the table model
     * @param task
     * @param tableModel 
     */
    public void generateRowData(Task task, DefaultTableModel tableModel) {
        // Breaks down Task object into its attributes (one attribute goes into each column)         
        int taskNumber = task.getTaskNumber();
        Boolean taskStatus = task.isTaskStatus();
        String taskName = task.getTaskName();
        LocalDate taskDate = task.getTaskDate();
        // Adds strikethrough to tasks (marks them as completed) when their status is set as 'true'
        if(taskStatus) {
            taskName = addInitialStrikethrough(taskName);
        }
        // Sets row data and sends it to table model
        Object[] rowData = {
            taskNumber,
            taskStatus, 
            taskName,
            taskDate
        };
        tableModel.addRow(rowData);
    }

    /**
     * Keeps track of changes made to the table model. Adds/removes strikethrough when table check boxes are clicked in the 'status' column
     * @param tableModel 
     */
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
                        int taskNumber = (int)tableModel.getValueAt(row, 0);
                        boolean taskStatus = (boolean)tableModel.getValueAt(row, 1);
                        String taskName = String.valueOf(tableModel.getValueAt(row, 2));
                        LocalDate taskDate = (LocalDate)tableModel.getValueAt(row, 3);
                        
                        // Excludes Day of Week headers which have taskNumber set as -1
                        if(taskNumber != -1) {
                            addOrRemoveStrikethrough(tableModel, row, taskNumber, taskName, taskDate, taskStatus);
                            // Print statement is for testing and debugging only
                            System.out.println("bool changed at row: " + row + " column: " + column);
                        }    
                    }
                    else if(data instanceof String) {
                        String taskNumber = String.valueOf(tableModel.getValueAt(row, 0));
                        String taskName = String.valueOf(tableModel.getValueAt(row, 2));
                        String taskDate = String.valueOf(tableModel.getValueAt(row, 3));
                        String taskStatus = String.valueOf(tableModel.getValueAt(row, 1));

                        // Updates database 
                        dbConnection.updateTaskStatus(taskNumber, taskStatus);
                        // Updates taskList
                        for(Task task : taskList) {
                            if(task.getTaskNumber() == (int)tableModel.getValueAt(row, 0)) {
                                task.setTaskName((String)tableModel.getValueAt(row, 2));
                                task.setTaskDate((LocalDate)tableModel.getValueAt(row, 3));
                            }
                        }
                        // This print statement is for testing and debugging only
                        System.out.println("strings changed at row: " + row);
                    }   
                }   
            }
        });
    }
    
    /**
     * Finds the index position in Controller's taskList for the task with the given taskNumber
     * @param taskNumber
     * @return index position in Controller task list for selected task 
     */
    public int getTaskIndex(int taskNumber) {
        int index = -1;
        for(int i = 0; i < taskList.size(); i++) {
            if(taskList.get(i).getTaskNumber() == taskNumber) {
                index = i;
            }
        }
        return index;
    }

    /**
     * Adds HTML tags to a string. The tags cause the string to render with a strikethrough 
     * @param taskName
     * @return the given string with strikethrough HTML tags added
     */
    public String addInitialStrikethrough(String taskName) {
        taskName = "<html><strike>" + taskName + "</strike></html>";
        return taskName;
    }
    
    /**
     * Adds or removes HTML tags to/from a string based on the given task's updated status. Saves the changes to the taskList, database and table model
     * @param tableModel
     * @param row
     * @param taskNumber
     * @param taskName
     * @param taskDate
     * @param taskStatus 
     */
    public void addOrRemoveStrikethrough(TableModel tableModel, int row, int taskNumber, String taskName, LocalDate taskDate, boolean taskStatus) {        
        // Updates database 
        dbConnection.updateTaskStatus(String.valueOf(taskNumber), String.valueOf(taskStatus));
        // Updates taskList
        for(Task task : taskList) {
            if(task.getTaskNumber() == (int)tableModel.getValueAt(row, 0)) {
                task.setTaskStatus((Boolean)tableModel.getValueAt(row, 1));
            }
        }
        // Where status is set to 'true' - Marks table row as completed (adds a strikethrough)
        if(taskStatus) {
            tableModel.setValueAt("<html><strike>" + taskName + "</strike></html>", row, 2);
        }
        else {
            // Removes strikethrough if status checkbox changes from checked to unchecked (true -> false)
            String RegExMatchHTMLTags = "<[^>]*>";
            taskName = taskName.replaceAll(RegExMatchHTMLTags, "");
            tableModel.setValueAt(taskName, row, 2);  
        } 
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
     * Sets default sort order of jTable data based on date column (ascending)
     * @param jTable 
     */
    public void sortTable(JTable jTable) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(jTable.getModel());
        jTable.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
    }
    
    /**
     * Takes a LocalDate value in ISO format yyyy-MM-dd and formats it for New Zealand
     * @param localDate
     * @return a string in format dd-MM-yyyy
     */
    public String formatDate(LocalDate localDate) {
        return (localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }
    
    /**
     * Takes a String value in ISO format yyyy-MM-dd and formats it for New Zealand 
     * @param localDate
     * @return a string in format dd-MM-yyyy
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
     * Fires when user double clicks on a table row - Displays a dialog showing the detailed view of a task  
     * @param jTable 
     * @param jDialog
     */
    public void addTableMouseClickListener(JTable jTable, JDialog dialog) {
        jTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                // Gets task number from selected table row
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                // Only executes if the user double clicked with their mouse on a valid table row (not a day of week header row)
                boolean mouseDoubleClicked = mouseEvent.getClickCount() == 2;
                boolean validTableRowSelected = table.getSelectedRow() != -1;
                if (mouseDoubleClicked && validTableRowSelected) {
                    // Saves row and task number variables for use later (otherwise they are lost when the dialog box is displayed)
                    selectedRow = jTable.getSelectedRow();
                    selectedTaskNumber = (int)jTable.getValueAt(selectedRow, 0);        
                     // Database query is only sent if taskNumber is not -1. Calendar view's DayOfWeek headings all have taskNumber -1 but they do not exist in the database
                    if(selectedTaskNumber != -1) {
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
            }
        });
    }

    /**
     * Creates a new listView form and displays it on screen within the main form's desktop pane
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
     * Creates a new calendarView form and displays it on screen within the main form's desktop pane
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
    
    /**
     * Adds a task to the taskList and database then calls for the table model to be refreshed
     * @param jTable
     * @param taskNameField
     * @param taskDetailsField
     * @param taskDateField
     * @return true if task was successfully added to the database and taskList, otherwise false
     */
    public boolean addTask(JTable jTable, JTextField taskNameField, JTextArea taskDetailsField, JTextField taskDateField) {
        String taskNumber = "";
        String taskName = "";
        String taskDescription = ""; 
        String taskDate = ""; 
        boolean validInput = false;
        boolean methodSuccessful = false;
        
        // Generates new taskID
        taskNumber = dbConnection.getLargestTaskNumber();
        int nextTaskNumber;
        
        if(taskNumber == null) {
            nextTaskNumber = 1;
            taskNumber = "1";
        }
        else {
            try {
                nextTaskNumber = Integer.parseInt(taskNumber) + 1;
                taskNumber = String.valueOf(nextTaskNumber);
            }
            catch(NumberFormatException e) {
                displayErrorMessage("Unable to add task due to an invalid task number being generated by the system. "
                        + "Please contact your system administrator for further assistance");
                System.out.println("Exception: " + e);
            }
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
        // Prevents default hint text from being saved to the task object's description field if it is not changed by the user
        if(!taskDetailsField.getText().equals("Add task details")) {
            taskDescription = taskDetailsField.getText();
        }
        // Reverses the string to check it can be parsed into a valid ISO date format
        if(!taskDateField.getText().equals("") && !taskDateField.getText().equals("dd-mm-yyyy")) {
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
                // Displays error message to user if date input formatting is invalid 
                taskDateField.setText("*Must be: dd-mm-yyyy");
                taskDateField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.RED));
                validInput = false;
            }
        }
        else {
            // Displays error message to user if they have not entered a date 
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
                    // Refresh the jTable model
                    populateTableData(jTable); 
                    methodSuccessful = true;
                }
                else {
                    displayErrorMessage("Unable to add task to the task list. Please contact your system administrator for further assistance");
                    System.out.println("Error: Unable to add task to controller's task list");
                    dbConnection.deleteTask(String.valueOf(taskNumber));
                }
            }
            else {
                System.out.println("Error: Unable to add task to database. Check database connection");
            }
        }
        else {
            System.out.println("Error: Unable to add task due to invalid user input");
        }
         return methodSuccessful;
    }
    
    /**
     * Updates an existing task based on user input
     * @param jTable
     * @param taskNameField
     * @param taskDetailsField
     * @param taskDateField
     * @return true if task was updated successfully, otherwise false
     */
    public boolean editTask(JTable jTable, JTextField taskNameField, JTextArea taskDetailsField, JTextField taskDateField) {
        String taskNumber = "";
        String taskName = "";
        String taskDescription = "";
        String taskDate = ""; 
        String taskStatus = "";
        int taskListIndex = -1;
        boolean validInput = false;
        boolean methodSuccessful = false;
                      
        // Sets taskID
        taskNumber = String.valueOf(selectedTaskNumber);   
        
        // Validates user input for taskName and taskDate 
        validInput = validateNameAndDate(taskNameField, taskDateField);
        if (validInput) {
            taskName = taskNameField.getText();
            taskDate = convertNZDateToISO(taskDateField.getText());
        }
        
        // Prevents default hint text from being saved to the task object's description field if user has not entered task details (this is not a required field) 
        // There are no further constraints on the sring value for taskDetails 
        if(!taskDetailsField.getText().equals("Add task details")) {
            taskDescription = taskDetailsField.getText();
        }
        
        // Gets task status
        for(Task task : taskList) {
            if(task.getTaskNumber() == selectedTaskNumber) {
                taskStatus = String.valueOf(task.isTaskStatus());
            }
        }           
        // If input is valid the selected record is updated in the database
        if(validInput) {
            Boolean taskUpdatedInDB = dbConnection.updateTask(taskNumber, taskName, taskDescription, taskDate, taskStatus);
            // If the database update is successful the task is added to the taskList
            if(taskUpdatedInDB) {
                Task task = parseStringsToTask(taskNumber, taskName, taskDescription, taskDate, taskStatus);
                for(int i = 0; i < taskList.size(); i++) {
                    if(taskList.get(i).getTaskNumber() == task.getTaskNumber()) {
                        taskList.set(i, task);
                        methodSuccessful = true;
                    }
                }
                // Updates the table model
                populateTableData(jTable);
            }
            else {
                System.out.println("Error: Unable to update task in database. Check your connection");
            }
        }
        else {
            System.out.println("Error: Unable to update task due to invalid user input");
        }
        return methodSuccessful;
    }
    
    /**
     * Checks taskName and taskDate to ensure the user has entered a value for each field and date formatting is correct (dd-MM-yyyy)
     * @param taskNameField
     * @param taskDetailsField
     * @param taskDateField
     * @return true if user input for taskName and taskDate fields is valid, otherwise false
     */
    public boolean validateNameAndDate(JTextField taskNameField, JTextField taskDateField) {
        boolean validInput = false;        
        // Validates input in taskName field - Checks taskName field is not blank and does not contain the default hint text
        if(!taskNameField.getText().equals("") && !taskNameField.getText().equals("Enter task name")) {
            validInput = true;
        }
        else {
            // Displays error message to user if input is invalid
            taskNameField.setText("*Task name is required");
            taskNameField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.RED));
            validInput = false;
        }

        // Validates input in taskDate field - Checks the date field is not blank and does not contain the default hint text
        if(!taskDateField.getText().equals("") && !taskDateField.getText().equals("dd-mm-yyyy")) {
            String date = convertNZDateToISO(taskDateField.getText());
            if(date.equals("")) {
                // Displays error message to user if date formatting is invalid
                taskDateField.setText("*Must be: dd-mm-yyyy");
                taskDateField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.RED));
                validInput = false;
            }
        }
        else {
            // Displays error message to user if they have not entered a date
            taskDateField.setText("*Date required");
            taskDateField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.RED));
            validInput = false;
        }
        return validInput;
    }
    
    /**
     * Reverses a given date (expressed as a String) from dd-MM-yyyy into a valid ISO date format
     * @param unverifiedDate
     * @return a string with format yyyy-MM-dd, or a null String if parse was unsuccessful
     */
    public String convertNZDateToISO(String unverifiedDate) {
        
        String convertedDate = "";
        try {
            // Parses string into date with format dd-MM-yyyy 
            LocalDate dateNZFormat = LocalDate.parse(unverifiedDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            // Parses date into ISO format for storage in the database
            LocalDate localDate = LocalDate.of(dateNZFormat.getYear(), dateNZFormat.getMonth(), dateNZFormat.getDayOfMonth());
            localDate.format(DateTimeFormatter.ISO_DATE);
            convertedDate = String.valueOf(localDate);
        }
        catch(Exception e) {
            System.out.println("Unable to parse date String due to invalid input (must be in format dd-MM-yyyy)");
        }
        return convertedDate;
    }
    
    /**
     * Removes the selected task from the database, the taskList and the table model
     * @param jTable
     * @param selectedRow
     * @param selectedTaskNumber
     * @return true if task was removed successfully, otherwise false
     */
    public boolean deleteTask(JTable jTable, int selectedRow, int selectedTaskNumber) {
        boolean methodSuccessful = false;
        // Removes task from database
        if(dbConnection.deleteTask(String.valueOf(selectedTaskNumber))) {
            // Removes task from taskList
            for(int i = 0; i < taskList.size(); i++) {
                if(taskList.get(i).getTaskNumber() == selectedTaskNumber) {
                    taskList.remove(i);
                    methodSuccessful = true;
                }
            }
            // Updates table model
            DefaultTableModel tableModel = (DefaultTableModel)jTable.getModel();
            tableModel.fireTableRowsDeleted(selectedRow, selectedRow);
            populateTableData(jTable);
        }
        return methodSuccessful;
    }
    
    /**
     * Takes a taskNumber and finds the other elements that make up the corresponding task object
     * @return a String array containing the values of the selected task object's attributes
     */
    public String[] getSelectedTask() {
        String taskNumber = String.valueOf(selectedTaskNumber);
        String taskName = "";
        String taskDescription = "";
        String taskDate = "";
        String taskStatus = "";
        
        for(Task task : taskList) {
            if(task.getTaskNumber() == selectedTaskNumber) {
                taskName = task.getTaskName();
                taskDescription = task.getTaskDescription();
                taskDate = formatDate(task.getTaskDate());
                taskStatus = String.valueOf(task.isTaskStatus());
            }
        }
        String[] taskComponents = new String[] {taskNumber, taskName, taskDescription, taskDate, taskStatus};
        return taskComponents;
    }
    
    /**
     * Initially calculates the start and end dates of the current week (Monday - Sunday). 
     * If called again after being initialized it applies an offset of plus or minus one week from the last selected week based on user input
     */    
    public void setSelectedWeek(int plusOrMinus1Week) {        
        if(selectedWeek == null || plusOrMinus1Week == 0) {
            // Sets the selected week to the current week
            selectedWeek = new LocalDate[2];
            selectedWeek[0] = LocalDate.now().plusWeeks(plusOrMinus1Week).with(DayOfWeek.MONDAY);
            selectedWeek[1] = LocalDate.now().plusWeeks(plusOrMinus1Week).with(DayOfWeek.SUNDAY);
        }
        else {
            // Adds or subtracts one week from the selected week depending on user input
            selectedWeek[0] = selectedWeek[0].plusWeeks(plusOrMinus1Week).with(DayOfWeek.MONDAY);
            selectedWeek[1] = selectedWeek[1].plusWeeks(plusOrMinus1Week).with(DayOfWeek.SUNDAY);
        }
    }
    
    /**
     * Gets the selected week 
     * @return a string containing the start and end dates of the selected week (Monday - Sunday) formatted for NZ (dd-MM-yyyy)
     */
    public String getSelectedWeek() {
        String _selectedWeek = formatDate(selectedWeek[0]) + "  to  " + formatDate(selectedWeek[1]);
        return _selectedWeek;
    }
        
    /**
     * Parses String data into types required for task object. 
     * @param _taskNumber
     * @param _taskName
     * @param _taskDescription
     * @param _taskDate
     * @param _taskStatus
     * @return a task object. If the parse was not successful the task object taskNumber will be set as -1
     */
    public Task parseStringsToTask(String _taskNumber, String _taskName, String _taskDescription, String _taskDate, String _taskStatus) {   
        int taskNumber = -1;
        String taskName = _taskName;
        String taskDescription = _taskDescription;
        LocalDate taskDate = LocalDate.now();
        boolean taskStatus = false;
        boolean parametersValid = true;            

        // Tries to parse parameters
        try {
            taskNumber = Integer.parseInt(_taskNumber); 
        }  
        catch(NumberFormatException e) {
            parametersValid = false;
            System.out.println("EXCEPTION: " + e);
        }                  
        if (_taskName.equals("")) {
            _taskName = "TaskName";
            parametersValid = false;
        }
        try {
            taskDate = LocalDate.parse(_taskDate);                               
        }
        catch(java.time.format.DateTimeParseException e) {
            parametersValid = false;
            System.out.println("EXCEPTION: " + e);
        } 
        try {
            taskStatus = Boolean.parseBoolean(_taskStatus); 
        }
        catch(Exception e) {
            parametersValid = false;
            System.out.println("EXCEPTION: " + e);
        }
        
        // Creates a new Task object with default values
        LocalDate localDate = LocalDate.now();
        Task task = new Task(-1, "taskName", "taskDescription", localDate, true);
        
        if(parametersValid) {
            // All parameters parsed ok so the Task is created with the given values
            task = new Task(taskNumber, taskName, taskDescription, taskDate, taskStatus);
        }
        else {
            // One or more parameters could not be parsed 
            // Task retains default values and taskNumber == -1 to identify it as a failed attempt
            task = new Task(taskNumber, taskName, taskDescription, taskDate, taskStatus);
        }
        return task;       
    }
}