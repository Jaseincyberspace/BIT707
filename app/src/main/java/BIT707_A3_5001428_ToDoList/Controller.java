/*
 * BIT707 - Assignment 3
 * Jason Norton - 5001428
 */
package BIT707_A3_5001428_ToDoList;
import java.awt.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class Controller {
    /**
     * Controller Attributes
     */
    final private DbConnection dbConnection;
    private ArrayList<Task> taskList;
    // Views
    public static ListViewForm listViewForm = null;
    public static CalendarViewForm calendarViewForm = null;
    
    /**
     * Controller Constructor
     */
    public Controller() {
        this.dbConnection = new DbConnection();
        if(taskList == null) {
             this.taskList = new ArrayList<>();
        }
    }
        
    public void run() {
        /**
        * Instantiates database connection
        */
        dbConnection.initDatabase();
        getAllTasks();
       /**
        * Displays MainForm as a MDI container
        */
       MainForm mainForm = new MainForm();
       mainForm.setLocationRelativeTo(null);
       mainForm.pack();
       mainForm.setVisible(true);
       /**
        * Displays ListView form inside MainForm
        */
       listViewForm = new ListViewForm();
       listViewForm.pack();
       listViewForm.setVisible(true);
    }
    
    public void getAllTasks() {
        /**
        * Queries the database
        */
        ArrayList<ArrayList<String>> allTaskData = (dbConnection.readAllTasks()); 
        // Creates Task objects from data returned by query
        int listLength = allTaskData.size();
        for (int i = 0; i < listLength; i++) {
            // Try to parse data
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
            /**
             * Creates a new task with validated input data 
             */
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
    
    public void populateTableData(ArrayList<Task> tasks, javax.swing.JTable jTable) {  
        /**
         * Uses the default JTable data model to add row data to the table
         */
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
    
    public void sortTable(javax.swing.JTable jTable) {
        // Sorts Jtable data by the date column
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(jTable.getModel());
        jTable.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
    }
    
    public String formatDate(LocalDate localDate) {
        /**
         * Formats date for New Zealand
         */ 
        return (localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }
        
    public void displayListView(javax.swing.JDesktopPane jDesktopPane_formContainer) {
        /**
        * Creates a new listView form and displays it on screen
        */
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
    
    public void displayCalendarView(javax.swing.JDesktopPane jDesktopPane_formContainer) {
        /**
        * Creates a new calendarView form and displays it on screen
        */
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
    /**
    * @return the taskList
    */
    public ArrayList<Task> getTaskList() {
        return taskList;
    }
}
