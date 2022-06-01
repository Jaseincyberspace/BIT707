/*
 * BIT707 - Assignment 3
 * Jason Norton - 5001428
 */
package BIT707_A3_5001428_ToDoList;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        DefaultTableModel tableModel = (DefaultTableModel)jTable.getModel();
        // Removes any existing row data first
        tableModel.setRowCount(0);
        // Sets table model
        jTable.setModel(tableModel);
        // Sets table listener to keep track of checkbox events
        addTableModelListener(tableModel);     
        
        String tableName = jTable.getName();
        if(tableName.equals("ListView")) {
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
            // Sorts table by date column but only when calendarView is not being displayed
            sortTable(jTable);
        }
        
        // Separates data into days of the week for the selected week 
        else {
            mondayList = new ArrayList<>();
            tuesdayList = new ArrayList<>();
            wednesdayList = new ArrayList<>();
            thursdayList = new ArrayList<>();
            fridayList = new ArrayList<>();
            saturdayList = new ArrayList<>();
            sundayList = new ArrayList<>();
            
            // These "tasks" are used as day of the week headers in the CalendarView table. HTML sytling is added to make them bold.
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
                                System.out.println("Unable to determine which day of the week task number " + task.getTaskNumber()
                                    + "falls on with date: " + String.valueOf(task.getTaskDate()));
                                break;
                        }   
                    }
                }
            }
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
            // Displays dates as dd-mm-yyyy        
            renderTableDateColumn(jTable);
        }              
        // Notifies of changes to the data model
        tableModel.fireTableDataChanged();
    }
     
    public void generateRowData(Task task, DefaultTableModel tableModel) {
        // Breaks down Task object into its attributes (one attribute goes into each column)
          
        int taskNumber = task.getTaskNumber();
        Boolean taskStatus = task.isTaskStatus();
        String taskName = task.getTaskName();
        LocalDate taskDate = task.getTaskDate();
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
                        dbConnection.updateTask(taskNumber, taskStatus);
                        // Updates taskList
                        for(int i = 0; i < taskList.size(); i++) {
                            if(taskList.get(i).getTaskNumber() == (int)tableModel.getValueAt(row, 0)) {
                                taskList.get(i).setTaskName((String)tableModel.getValueAt(row, 2));
                                taskList.get(i).setTaskDate((LocalDate)tableModel.getValueAt(row, 3));
                            }
                        }
                        // Print statement is for testing and debugging only
                        System.out.println("strings changed at row: " + row);
                    }   
                }   
            }
        });
    }
    
    public void addOrRemoveStrikethrough(TableModel tableModel, int row, int taskNumber, String taskName, LocalDate taskDate, boolean taskStatus) {
        // Updates database 
        dbConnection.updateTask(String.valueOf(taskNumber), String.valueOf(taskStatus));
        // Updates taskList
        for(int i = 0; i < taskList.size(); i++) {
             if(taskList.get(i).getTaskNumber() == (int)tableModel.getValueAt(row, 0)) {
                taskList.get(i).setTaskStatus((Boolean)tableModel.getValueAt(row, 1));
             }
        }
        // Marks table row as completed (adds a strikethrough)
        if(taskStatus) {
            tableModel.setValueAt("<html><strike>" + taskName + "</strike></html>", row, 2);
        }
        else {
            // Removes strikethrough if checkbox changes from checked to unchecked
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
                        
        // If input is valid the selected record is updated in the database 
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
                populateTableData(jTable);
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
            DefaultTableModel tableModel = (DefaultTableModel)jTable.getModel();
            tableModel.fireTableRowsDeleted(selectedRow, selectedRow);
            populateTableData(jTable);
        }
        return methodSuccessful;
    }
    
    public String[] getSelectedTask() {
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
    
    /**
     * Initially calculates the start and end dates of the current week (Monday - Sunday). 
     * If called again after being initialized it applies an offset of plus or minus one week from the last selected week based on user input
     * @return 
     */    
    public void setSelectedWeek(int plusOrMinus1Week) {        
        if(selectedWeek == null || plusOrMinus1Week == 0) {
            selectedWeek = new LocalDate[2];
            selectedWeek[0] = LocalDate.now().plusWeeks(plusOrMinus1Week).with(DayOfWeek.MONDAY);
            selectedWeek[1] = LocalDate.now().plusWeeks(plusOrMinus1Week).with(DayOfWeek.SUNDAY);
        }
        else {
            selectedWeek[0] = selectedWeek[0].plusWeeks(plusOrMinus1Week).with(DayOfWeek.MONDAY);
            selectedWeek[1] = selectedWeek[1].plusWeeks(plusOrMinus1Week).with(DayOfWeek.SUNDAY);
        }
        
    }
    
    /**
     * Returns a string containing the start and end dates of the selected week formatted for NZ (dd-MM-yyyy)
     * @return 
     */
    public String getSelectedWeek() {
        String _selectedWeek = formatDate(selectedWeek[0]) + "  to  " + formatDate(selectedWeek[1]);
        return _selectedWeek;
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