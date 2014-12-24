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
package os4.dev.debug;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import os4.Mls;
import os4.dev.AbstractMeasuringCondition;
import os4.serv.StreamTools;

/**
 *
 * @author root
 */
public class DevDebugMeasuringCondition extends javax.swing.JPanel 
    implements AbstractMeasuringCondition{
    int ExpCount = 1;
    /**
     * Creates new form DeviceDebugMeasuringConditionEditor
     */
    public DevDebugMeasuringCondition() {
    }

    public void setExpositionCount(int exp) {
        ExpCount = exp;
    }
    
    public int getExpositionCount(){
        return ExpCount;
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
        jsNumberOfExpositions = new javax.swing.JSpinner();

        setMinimumSize(new java.awt.Dimension(248, 40));

        jLabel1.setText("Количество экспозиций");

        jsNumberOfExpositions.setModel(new javax.swing.SpinnerNumberModel(1, 1, 5, 1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsNumberOfExpositions, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(67, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jsNumberOfExpositions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSpinner jsNumberOfExpositions;
    // End of variables declaration//GEN-END:variables

    boolean IsInited = false;
    @Override
    public JPanel getGUIEditor() {
        if(IsInited == false){
            initComponents();
            jsNumberOfExpositions.setValue(ExpCount);
            jsNumberOfExpositions.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    ExpCount = (Integer)jsNumberOfExpositions.getValue();
                }
            });
            IsInited = true;
            Mls.translate(this);
        }
        return this;
    }

    @Override
    public void save(DataOutputStream os)  throws IOException{
        os.writeInt(AbstractMeasuringCondition.TypeDebug);
        StreamTools.versionBlockBegin(os, 1);
        os.writeInt(ExpCount);
        StreamTools.versionBlockEnd(os);
    }

    @Override
    public void load(DataInputStream is)  throws IOException{
        int type = is.readInt();
        if(type != AbstractMeasuringCondition.TypeDebug)
            throw new IOException("This is not a DebugCondition");
        StreamTools.versionBlockBegin(is, 1, 1);
        ExpCount = is.readInt();
        StreamTools.versionBlockEnd(is);
    }

    @Override
    public boolean isCompatibleWith(String id) {
        return id.equals(DeviceDebug.ID);
    }
}