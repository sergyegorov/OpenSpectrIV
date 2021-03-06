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
package os4.dev.method.aspect;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import os4.Common;
import os4.dev.method.IMethodAspectEditor;
import os4.dev.method.IMethodDescription;

/**
 *
 * @author root
 */
public class AspectDescriptionEditor extends javax.swing.JPanel 
    implements IMethodAspectEditor{
    private static final Logger Log = Common.getLogger(AspectDescriptionEditor.class);

    @Override
    public void update() {
        
    }

    @Override
    public JPanel getPanel() {
        return this;
    }
    
    class UpdateListener implements DocumentListener{        
        JTextComponent ControledComponent;
        IMethodDescription.DescriptionTypes Type;
        @SuppressWarnings("LeakingThisInConstructor")
        public UpdateListener(JTextComponent controled_field,IMethodDescription.DescriptionTypes type){
            ControledComponent = controled_field;
            ControledComponent.getDocument().addDocumentListener(this);
            Type = type;
        }
        @Override
        public void insertUpdate(DocumentEvent e) { update(e); }
        @Override
        public void removeUpdate(DocumentEvent e) { update(e); }
        @Override
        public void changedUpdate(DocumentEvent e) { update(e); }
        void update(DocumentEvent e){
            try{
                if(Initing) return;
                String text = ControledComponent.getText();
                IDesc.setDescription(Type, text);
            } catch(Exception ex){
                Log.log(Level.SEVERE,"Update error...",ex);
            }
        }
    }
    IMethodDescription IDesc;
    boolean Initing = false;
    /**
     * Creates new form AspectDescriptionEditor
     * @param descr
     */
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public AspectDescriptionEditor(IMethodDescription descr) {
        Initing = true;
        IDesc = descr;
        initComponents();
        jtaDescription.setText(descr.getDescription(IMethodDescription.DescriptionTypes.Description));
        new UpdateListener(jtaDescription, IMethodDescription.DescriptionTypes.Description);
        jtaWarning.setText(descr.getDescription(IMethodDescription.DescriptionTypes.Warrning));
        new UpdateListener(jtaWarning, IMethodDescription.DescriptionTypes.Warrning);
        jtfMeasuredElements.setText(descr.getDescription(IMethodDescription.DescriptionTypes.Elements));
        new UpdateListener(jtfMeasuredElements, IMethodDescription.DescriptionTypes.Elements);
        jtfUsedStandards.setText(descr.getDescription(IMethodDescription.DescriptionTypes.Standarts));
        new UpdateListener(jtfUsedStandards, IMethodDescription.DescriptionTypes.Standarts);
        Initing = false;
        jtbEditableActionPerformed(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaDescription = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtaWarning = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jtfMeasuredElements = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtfUsedStandards = new javax.swing.JTextField();
        jtbEditable = new javax.swing.JToggleButton();

        jLabel1.setText("Описание");

        jtaDescription.setColumns(20);
        jtaDescription.setRows(5);
        jScrollPane1.setViewportView(jtaDescription);

        jLabel2.setText("Предупреждение перед рядовым измерением");

        jtaWarning.setColumns(20);
        jtaWarning.setRows(5);
        jScrollPane2.setViewportView(jtaWarning);

        jLabel3.setText("Измеряемые элементы");

        jLabel4.setText("Используемые стандарты");

        jtbEditable.setText("Редактировать");
        jtbEditable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbEditableActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(0, 462, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfUsedStandards)
                    .addComponent(jtfMeasuredElements)))
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jScrollPane2))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jtbEditable, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(4, 4, 4)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfMeasuredElements, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfUsedStandards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addComponent(jtbEditable))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jtbEditableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbEditableActionPerformed
        try{
            boolean editable_flag = false;
            if(jtfMeasuredElements.getText().trim().length() == 0)
                editable_flag = true;
            if(editable_flag == false && jtfUsedStandards.getText().trim().length() == 0)
                editable_flag = true;
            jtbEditable.setEnabled(!editable_flag);
            if(editable_flag == false && jtbEditable.isSelected())
                editable_flag = true;
            jtaDescription.setEditable(editable_flag);
            jtaWarning.setEditable(editable_flag);
            jtfMeasuredElements.setEditable(editable_flag);
            jtfUsedStandards.setEditable(editable_flag);
        }catch(Exception ex){
            Log.log(Level.SEVERE,"Togle editable exception",ex);
        }
    }//GEN-LAST:event_jtbEditableActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jtaDescription;
    private javax.swing.JTextArea jtaWarning;
    private javax.swing.JToggleButton jtbEditable;
    private javax.swing.JTextField jtfMeasuredElements;
    private javax.swing.JTextField jtfUsedStandards;
    // End of variables declaration//GEN-END:variables
}
