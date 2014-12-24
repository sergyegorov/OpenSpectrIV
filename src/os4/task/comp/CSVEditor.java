/*
 * The MIT License
 *
 * Copyright 2014 root.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package os4.task.comp;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import os4.Mls;
import os4.serv.Dialogs;
import os4.Common;

/**
 *
 * @author root
 */
public class CSVEditor extends javax.swing.JPanel {
    private static final Logger Log = Common.getLogger( CSVEditor.class );
    CSVEditorTableModel TModel;
    String SelectedRowName;
    String SelectedColName;
    /**
     * Creates new form CSVEditor
     * @param csv_file
     * @param column_object_name
     * @param row_object_name
     */
    public CSVEditor(File csv_file, 
            String column_object_name,
            String row_object_name) {
        initComponents();
        TModel = new CSVEditorTableModel(csv_file,this);
        jtTableView.setModel(TModel);
        jtTableView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtTableView.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            try{
                SelectedRowName = null;
                int col = jtTableView.getSelectedColumn();
                int row = jtTableView.getSelectedRow();
                SelectedRowName = TModel.getValueAt(row, 0).toString();
                SelectedColName = TModel.getColumnName(col);
            }catch(Exception ex){
                Log.log(Level.SEVERE,"Selection value exception",ex);
            }
        });
        jbAddColumn.setText("Добавить "+column_object_name);
        jbRemoveColumn.setText("Удалить");
        jbAddRow.setText(getVerticalString("Добавить"+row_object_name));
        jbRemoveRow.setText(getVerticalString(jbRemoveRow.getText()));
        jbRenameRow.setText(getVerticalString(jbRenameColumn.getText()));
        Mls.translate(this);
    }

    public void cancelEditing(){
        jtTableView.getCellEditor().stopCellEditing();
    }
    
    final String getVerticalString(String in){
        return in;
        /*String ret = "<html>";
        for(int i = 0;i<in.length();i++)
            ret += in.charAt(i)+"<br>";
        ret += "<html>";
        return ret;*/
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jbAddColumn = new javax.swing.JButton();
        jbRemoveColumn = new javax.swing.JButton();
        jbRenameColumn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtTableView = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jbAddRow = new javax.swing.JButton();
        jbRemoveRow = new javax.swing.JButton();
        jbRenameRow = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setPreferredSize(new java.awt.Dimension(669, 28));
        jPanel1.setLayout(new java.awt.GridLayout(1, 3));

        jbAddColumn.setText("Добавить колонку");
        jbAddColumn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAddColumnActionPerformed(evt);
            }
        });
        jPanel1.add(jbAddColumn);

        jbRemoveColumn.setText("Удалить");
        jPanel1.add(jbRemoveColumn);

        jbRenameColumn.setText("Переименовать");
        jPanel1.add(jbRenameColumn);

        jtTableView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jtTableView);

        jPanel2.setLayout(new java.awt.GridLayout(1, 3));

        jbAddRow.setText("<html>1<br>2<br></html>");
        jbAddRow.setToolTipText("");
        jbAddRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAddRowActionPerformed(evt);
            }
        });
        jPanel2.add(jbAddRow);

        jbRemoveRow.setText("Удалить");
        jPanel2.add(jbRemoveRow);

        jbRenameRow.setText("Переименовать");
        jPanel2.add(jbRenameRow);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbAddColumnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddColumnActionPerformed
        try{
            String name = Dialogs.getStringMLS("Введите имя нового элемента", "Добавление элемента");
            if(name == null)
                return;
            TModel.addColumn(name);
            TModel.fireTableStructureChanged();
        }catch(Exception ex){
            Log.log(Level.SEVERE,"New Element error",ex);
        }
    }//GEN-LAST:event_jbAddColumnActionPerformed

    private void jbAddRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddRowActionPerformed
        try{
            String name = Dialogs.getStringMLS("Введите имя нового стандатра", "Добавление стандарта");
            if(name == null)
                return;
            TModel.addRow(name);
            TModel.fireTableStructureChanged();
        }catch(Exception ex){
            Log.log(Level.SEVERE,"New Element error",ex);
        }
    }//GEN-LAST:event_jbAddRowActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbAddColumn;
    private javax.swing.JButton jbAddRow;
    private javax.swing.JButton jbRemoveColumn;
    private javax.swing.JButton jbRemoveRow;
    private javax.swing.JButton jbRenameColumn;
    private javax.swing.JButton jbRenameRow;
    private javax.swing.JTable jtTableView;
    // End of variables declaration//GEN-END:variables
}