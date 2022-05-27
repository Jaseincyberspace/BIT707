/*
 * BIT707 - Assignment 3
 * Jason Norton - 5001428
 */
package BIT707_A3_5001428_ToDoList;

import javax.swing.BorderFactory;

public class ListViewForm extends javax.swing.JInternalFrame {

    /**
     * ListViewForm constructor
     */
    public ListViewForm() {
        initComponents();
        // Removes title bar from jInternalForm
        javax.swing.plaf.InternalFrameUI internalFrameUI = this.getUI();
        ((javax.swing.plaf.basic.BasicInternalFrameUI)internalFrameUI).setNorthPane(null);
        
        // Gets data for the jTable
        App.controller.populateTableData(App.controller.getTaskList(), jTable_listView);  
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog_addTask = new javax.swing.JDialog();
        jPanel_addTaskPanel = new javax.swing.JPanel();
        jButton_addTaskSave = new javax.swing.JButton();
        jTextField_addTaskName = new javax.swing.JTextField();
        jTextField_addTaskDate = new javax.swing.JTextField();
        jScrollPane_addTask = new javax.swing.JScrollPane();
        jTextArea_addTaskDetails = new javax.swing.JTextArea();
        jButton_addTaskCancel = new javax.swing.JButton();
        jLabel_addTaskDetailsIcon = new javax.swing.JLabel();
        jLabel_addTaskDateIcon = new javax.swing.JLabel();
        jPanel_errorMessage = new javax.swing.JPanel();
        jLabel_errorMessage = new javax.swing.JLabel();
        jScrollPane_listView = new javax.swing.JScrollPane();
        jTable_listView = new javax.swing.JTable();
        jPanel_addTask = new javax.swing.JPanel();
        jButton_addTask = new javax.swing.JButton();

        jDialog_addTask.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialog_addTask.setTitle("Add Task");
        jDialog_addTask.setAlwaysOnTop(true);
        jDialog_addTask.setBackground(new java.awt.Color(79, 79, 79));
        jDialog_addTask.setFocusTraversalPolicyProvider(true);
        jDialog_addTask.setIconImage(null);
        jDialog_addTask.setIconImages(null);
        jDialog_addTask.setLocation(new java.awt.Point(0, 0));
        jDialog_addTask.setMinimumSize(new java.awt.Dimension(480, 435));
        jDialog_addTask.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        jDialog_addTask.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        jDialog_addTask.setName(""); // NOI18N
        jDialog_addTask.setPreferredSize(new java.awt.Dimension(437, 390));
        jDialog_addTask.setResizable(false);

        jPanel_addTaskPanel.setBackground(new java.awt.Color(255, 255, 255));

        jButton_addTaskSave.setBackground(new java.awt.Color(75, 114, 153));
        jButton_addTaskSave.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton_addTaskSave.setForeground(new java.awt.Color(255, 255, 255));
        jButton_addTaskSave.setText("Save");
        jButton_addTaskSave.setNextFocusableComponent(jButton_addTaskCancel);
        jButton_addTaskSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_addTaskSaveActionPerformed(evt);
            }
        });

        jTextField_addTaskName.setBackground(new java.awt.Color(224, 224, 224));
        jTextField_addTaskName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_addTaskName.setForeground(new java.awt.Color(0, 0, 0));
        jTextField_addTaskName.setText("Enter task name");
        jTextField_addTaskName.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jTextField_addTaskName.setNextFocusableComponent(jTextArea_addTaskDetails);
        jTextField_addTaskName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField_addTaskNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField_addTaskNameFocusLost(evt);
            }
        });

        jTextField_addTaskDate.setBackground(new java.awt.Color(224, 224, 224));
        jTextField_addTaskDate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_addTaskDate.setForeground(new java.awt.Color(0, 0, 0));
        jTextField_addTaskDate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_addTaskDate.setText("dd-mm-yyyy");
        jTextField_addTaskDate.setNextFocusableComponent(jButton_addTaskSave);
        jTextField_addTaskDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField_addTaskDateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField_addTaskDateFocusLost(evt);
            }
        });

        jTextArea_addTaskDetails.setBackground(new java.awt.Color(224, 224, 244));
        jTextArea_addTaskDetails.setColumns(20);
        jTextArea_addTaskDetails.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextArea_addTaskDetails.setForeground(new java.awt.Color(0, 0, 0));
        jTextArea_addTaskDetails.setRows(5);
        jTextArea_addTaskDetails.setText("Add task details");
        jTextArea_addTaskDetails.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jTextArea_addTaskDetails.setNextFocusableComponent(jTextField_addTaskDate);
        jTextArea_addTaskDetails.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextArea_addTaskDetailsFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextArea_addTaskDetailsFocusLost(evt);
            }
        });
        jScrollPane_addTask.setViewportView(jTextArea_addTaskDetails);

        jButton_addTaskCancel.setBackground(new java.awt.Color(75, 114, 153));
        jButton_addTaskCancel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton_addTaskCancel.setForeground(new java.awt.Color(255, 255, 255));
        jButton_addTaskCancel.setText("Cancel");
        jButton_addTaskCancel.setNextFocusableComponent(jTextField_addTaskName);
        jButton_addTaskCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_addTaskCancelActionPerformed(evt);
            }
        });

        jLabel_addTaskDetailsIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/comment.png"))); // NOI18N

        jLabel_addTaskDateIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/calendar.png"))); // NOI18N

        jPanel_errorMessage.setBackground(new java.awt.Color(224, 224, 244));

        jLabel_errorMessage.setBackground(new java.awt.Color(69, 69, 69));
        jLabel_errorMessage.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_errorMessage.setForeground(new java.awt.Color(204, 0, 102));
        jLabel_errorMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel_errorMessageLayout = new javax.swing.GroupLayout(jPanel_errorMessage);
        jPanel_errorMessage.setLayout(jPanel_errorMessageLayout);
        jPanel_errorMessageLayout.setHorizontalGroup(
            jPanel_errorMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel_errorMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel_errorMessageLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel_errorMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel_errorMessageLayout.setVerticalGroup(
            jPanel_errorMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 57, Short.MAX_VALUE)
            .addGroup(jPanel_errorMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel_errorMessageLayout.createSequentialGroup()
                    .addComponent(jLabel_errorMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 21, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel_addTaskPanelLayout = new javax.swing.GroupLayout(jPanel_addTaskPanel);
        jPanel_addTaskPanel.setLayout(jPanel_addTaskPanelLayout);
        jPanel_addTaskPanelLayout.setHorizontalGroup(
            jPanel_addTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_addTaskPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel_addTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTextField_addTaskName, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel_addTaskPanelLayout.createSequentialGroup()
                        .addComponent(jLabel_addTaskDetailsIcon)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel_addTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane_addTask, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_addTaskDate, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel_addTaskPanelLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel_addTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_addTaskDateIcon)
                            .addGroup(jPanel_addTaskPanelLayout.createSequentialGroup()
                                .addComponent(jButton_addTaskSave, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton_addTaskCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel_errorMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel_addTaskPanelLayout.setVerticalGroup(
            jPanel_addTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_addTaskPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField_addTaskName, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel_addTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane_addTask, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_addTaskDetailsIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_addTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField_addTaskDate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_addTaskDateIcon))
                .addGap(29, 29, 29)
                .addGroup(jPanel_addTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_addTaskSave, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_addTaskCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel_errorMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog_addTaskLayout = new javax.swing.GroupLayout(jDialog_addTask.getContentPane());
        jDialog_addTask.getContentPane().setLayout(jDialog_addTaskLayout);
        jDialog_addTaskLayout.setHorizontalGroup(
            jDialog_addTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_addTaskPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog_addTaskLayout.setVerticalGroup(
            jDialog_addTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_addTaskLayout.createSequentialGroup()
                .addComponent(jPanel_addTaskPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 404, Short.MAX_VALUE)
                .addContainerGap())
        );

        setBorder(null);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(44, 100));
        setOpaque(true);
        setPreferredSize(new java.awt.Dimension(406, 1463));

        jScrollPane_listView.setBorder(null);

        jTable_listView.setAutoCreateRowSorter(true);
        jTable_listView.setBackground(new java.awt.Color(255, 255, 255));
        jTable_listView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Status", "Task", "Due Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable_listView.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable_listView.setMaximumSize(new java.awt.Dimension(2147483647, 128000000));
        jTable_listView.setMinimumSize(new java.awt.Dimension(165, 180));
        jTable_listView.setName(""); // NOI18N
        jTable_listView.setPreferredSize(null);
        jTable_listView.setRowHeight(40);
        jTable_listView.setSelectionBackground(new java.awt.Color(79, 79, 79));
        jTable_listView.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jTable_listView.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable_listView.setShowGrid(true);
        jScrollPane_listView.setViewportView(jTable_listView);
        if (jTable_listView.getColumnModel().getColumnCount() > 0) {
            jTable_listView.getColumnModel().getColumn(0).setMinWidth(20);
            jTable_listView.getColumnModel().getColumn(0).setPreferredWidth(80);
            jTable_listView.getColumnModel().getColumn(0).setMaxWidth(80);
            jTable_listView.getColumnModel().getColumn(2).setMinWidth(80);
            jTable_listView.getColumnModel().getColumn(2).setPreferredWidth(100);
            jTable_listView.getColumnModel().getColumn(2).setMaxWidth(100);
        }

        getContentPane().add(jScrollPane_listView, java.awt.BorderLayout.CENTER);

        jPanel_addTask.setMinimumSize(new java.awt.Dimension(100, 80));
        jPanel_addTask.setPreferredSize(new java.awt.Dimension(406, 80));

        jButton_addTask.setBackground(new java.awt.Color(75, 114, 153));
        jButton_addTask.setForeground(new java.awt.Color(255, 255, 255));
        jButton_addTask.setText("Add Task");
        jButton_addTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_addTaskActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_addTaskLayout = new javax.swing.GroupLayout(jPanel_addTask);
        jPanel_addTask.setLayout(jPanel_addTaskLayout);
        jPanel_addTaskLayout.setHorizontalGroup(
            jPanel_addTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_addTaskLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jButton_addTask, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(90, Short.MAX_VALUE))
        );
        jPanel_addTaskLayout.setVerticalGroup(
            jPanel_addTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_addTaskLayout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(jButton_addTask, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        getContentPane().add(jPanel_addTask, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Displays a modal dialog box on screen for user to add a new task
     * @param evt 
     */
    private void jButton_addTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_addTaskActionPerformed
        // Reset dialog fields
        jTextField_addTaskName.setText("Enter task name");
        jTextArea_addTaskDetails.setText("Add task details");
        jTextField_addTaskDate.setText("dd-mm-yyyy");
        jLabel_errorMessage.setText("");
        jTextField_addTaskName.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jTextArea_addTaskDetails.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jTextField_addTaskDate.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        // Display the addTask dialog box in the centre of the parent form
        jDialog_addTask.setLocationRelativeTo(this);
        jDialog_addTask.setVisible(true);
    }//GEN-LAST:event_jButton_addTaskActionPerformed

    private void jButton_addTaskSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_addTaskSaveActionPerformed
        if(App.controller.addTask(jTextField_addTaskName, jTextArea_addTaskDetails, jTextField_addTaskDate)) {
             // Re-populates data in the jTable to reflect changes
            App.controller.populateTableData(App.controller.getTaskList(), jTable_listView);
            jDialog_addTask.setVisible(false);
        }
        else {
            jLabel_errorMessage.setText("Unable to add task. Contact your system administrator for assistance.");
        }
        
    }//GEN-LAST:event_jButton_addTaskSaveActionPerformed

    private void jTextField_addTaskNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_addTaskNameFocusGained
        if(jTextField_addTaskName.getText().equals("Enter task name") || jTextField_addTaskName.getText().equals("*Task name is required")) {
            jTextField_addTaskName.setText("");
            jTextField_addTaskName.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            jLabel_errorMessage.setText("");
        }
    }//GEN-LAST:event_jTextField_addTaskNameFocusGained

    private void jTextField_addTaskNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_addTaskNameFocusLost
        if(jTextField_addTaskName.getText().equals("")) {
            jTextField_addTaskName.setText("Enter task name");
        }
    }//GEN-LAST:event_jTextField_addTaskNameFocusLost

    private void jTextArea_addTaskDetailsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextArea_addTaskDetailsFocusGained
        if(jTextArea_addTaskDetails.getText().equals("Add task details")) {
        jTextArea_addTaskDetails.setText("");
        jLabel_errorMessage.setText("");
        }
    }//GEN-LAST:event_jTextArea_addTaskDetailsFocusGained

    private void jTextArea_addTaskDetailsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextArea_addTaskDetailsFocusLost
        if(jTextArea_addTaskDetails.getText().equals("")) {
        jTextArea_addTaskDetails.setText("Add task details");
        }
    }//GEN-LAST:event_jTextArea_addTaskDetailsFocusLost

    private void jTextField_addTaskDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_addTaskDateFocusGained
        if(jTextField_addTaskDate.getText().equals("dd-mm-yyyy") || 
        jTextField_addTaskDate.getText().equals("*Date required") || 
        jTextField_addTaskDate.getText().equals("*Must be: dd-mm-yyyy")) {
            jTextField_addTaskDate.setText("");
            jTextField_addTaskDate.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            jLabel_errorMessage.setText("");
        }
    }//GEN-LAST:event_jTextField_addTaskDateFocusGained

    private void jTextField_addTaskDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_addTaskDateFocusLost
        if(jTextField_addTaskDate.getText().equals("")) {
            jTextField_addTaskDate.setText("dd-mm-yyyy");
            
        }
    }//GEN-LAST:event_jTextField_addTaskDateFocusLost

    private void jButton_addTaskCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_addTaskCancelActionPerformed
        jDialog_addTask.setVisible(false);
    }//GEN-LAST:event_jButton_addTaskCancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_addTask;
    private javax.swing.JButton jButton_addTaskCancel;
    private javax.swing.JButton jButton_addTaskSave;
    private javax.swing.JDialog jDialog_addTask;
    private javax.swing.JLabel jLabel_addTaskDateIcon;
    private javax.swing.JLabel jLabel_addTaskDetailsIcon;
    private javax.swing.JLabel jLabel_errorMessage;
    private javax.swing.JPanel jPanel_addTask;
    private javax.swing.JPanel jPanel_addTaskPanel;
    private javax.swing.JPanel jPanel_errorMessage;
    private javax.swing.JScrollPane jScrollPane_addTask;
    private javax.swing.JScrollPane jScrollPane_listView;
    private javax.swing.JTable jTable_listView;
    private javax.swing.JTextArea jTextArea_addTaskDetails;
    private javax.swing.JTextField jTextField_addTaskDate;
    private javax.swing.JTextField jTextField_addTaskName;
    // End of variables declaration//GEN-END:variables
}
