/*
 * BIT707 - Assignment 3
 * Jason Norton - 5001428
 */
package BIT707_A3_5001428_ToDoList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author OEM
 */
public class Task {
    private int taskNumber;
    private String taskName;
    private String taskDescription;
    private LocalDate taskDate;
    private boolean taskStatus;
    
    /**
     * Task Constructor 
     * @param _taskNumber
     * @param _taskName
     * @param _taskDescription
     * @param _taskDate
     * @param _taskStatus
     */
    public Task(int _taskNumber, String _taskName, String _taskDescription, LocalDate _taskDate, boolean _taskStatus) {
        this.taskNumber = _taskNumber;
        this.taskName = _taskName;
        this.taskDescription = _taskDescription;      
        this.taskStatus = _taskStatus;       
        this.taskDate = _taskDate;
    }
    
    // ---- GETTERS AND SETTERS: ---- //
    /**
     * @return the taskNumber
     */
    public int getTaskNumber() {
        return taskNumber;
    }

    /**
     * @param taskNumber the taskNumber to set
     */
    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * @return the taskName
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * @param taskName the taskName to set
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * @return the taskDescription
     */
    public String getTaskDescription() {
        return taskDescription;
    }

    /**
     * @param taskDescription the taskDescription to set
     */
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    /**
     * @return the taskDate
     */
    public LocalDate getTaskDate() {
        return taskDate;
    }

    /**
     * @param taskDate the taskDate to set
     */
    public void setTaskDate(LocalDate taskDate) {
        taskDate.format(DateTimeFormatter.ofPattern("dd MM yyyy"));
        this.taskDate = taskDate;
    }

    /**
     * @return the taskStatus
     */
    public boolean isTaskStatus() {
        return taskStatus;
    }

    /**
     * @param taskStatus the taskStatus to set
     */
    public void setTaskStatus(boolean taskStatus) {
        this.taskStatus = taskStatus;
    }
}
