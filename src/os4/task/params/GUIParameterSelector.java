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

package os4.task.params;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import os4.OS4UnExpectedError;
import os4.serv.StreamTools;
import os4.Common;

/**
 *
 * @author root
 */
public class GUIParameterSelector extends javax.swing.JPanel 
            implements AbstractGUIParameter
{
    private static final Logger Log = Common.getLogger( GUIParameterSelector.class );
    
    String Id;
    String Description;
    String DefaultValue;
    String[] Values;
    String Value;
    
    public String getValue(){
        return Value;
    }
    
    /**
     * Creates new form GUIParameterSelector
     * @param id - name of parameter
     * @param description - text string for GUI
     * @param def_value - default value
     * @param values - values
     */
    public GUIParameterSelector(String id,String description,String def_value,String[] values) {
        Id = id;
        Description = description;
        DefaultValue = def_value;
        Values = (String[])values.clone();
        Value = DefaultValue;
    }
    
    public GUIParameterSelector(){
        
    }
    
    boolean IsInited = false;
    public JPanel initGui(){
        if(IsInited)
            initComponents();
        return this;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbName = new javax.swing.JLabel();
        cbValueSelector = new javax.swing.JComboBox();

        lbName.setText("jLabel1");

        cbValueSelector.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbValueSelector.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbValueSelectorItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbName)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(cbValueSelector, 0, 412, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbValueSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbValueSelectorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbValueSelectorItemStateChanged
        try{
            if(Initing)
                return;
            Value = (String)cbValueSelector.getSelectedItem();
            Master.commit();
        }catch(Exception ex){
            Log.log(Level.SEVERE,"Value save error",ex);
        }
    }//GEN-LAST:event_cbValueSelectorItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbValueSelector;
    private javax.swing.JLabel lbName;
    // End of variables declaration//GEN-END:variables

    @Override
    public String getId() {
        return Id;
    }

    @Override
    public void load(DataInputStream str) throws IOException{
        StreamTools.versionBlockBegin(str, 1, 1);
        Id = StreamTools.readString(str);//StreamTools.writeString(Id, str);//String Id;
        Description = StreamTools.readString(str);//StreamTools.writeString(Description, str);//String Description;
        DefaultValue = StreamTools.readString(str);//StreamTools.writeString(DefaultValue, str);//String DefaultValue;
        int count = str.readInt();//str.writeInt(Values.length);
        Values = new String[count];
        for(int i = 0;i<Values.length;i++)
            Values[i] = StreamTools.readString(str);//StreamTools.writeString(Values[i], str);//String[] Values;
        Value = StreamTools.readString(str);//StreamTools.writeString(Value, str);//String Value;
        StreamTools.versionBlockEnd(str);
    }

    @Override
    public void save(DataOutputStream str) throws IOException {
        StreamTools.versionBlockBegin(str, 1);
        StreamTools.writeString(Id, str);//String Id;
        StreamTools.writeString(Description, str);//String Description;
        StreamTools.writeString(DefaultValue, str);//String DefaultValue;
        str.writeInt(Values.length);
        for(int i = 0;i<Values.length;i++)
            StreamTools.writeString(Values[i], str);//String[] Values;
        StreamTools.writeString(Value, str);//String Value;
        StreamTools.versionBlockEnd(str);
    }

    @Override
    public void reset() {
        cbValueSelector.setSelectedItem(DefaultValue);
    }

    boolean GuiInited = false;
    boolean Initing = false;
    @Override
    public JPanel getGui() {
        if(GuiInited != true){
            Initing = true;
            initComponents();
            GuiInited = true;
            lbName.setText(Description);
            cbValueSelector.setModel(new DefaultComboBoxModel(Values));
            cbValueSelector.setSelectedItem(Value);
            Initing = false;
        }
        return this;
    }

    @Override
    public int getType() {
        return AbstractGUIParameter.TypeSelector;
    }
    
    @Override
    public String toString(){
        return AbstractGUIParameter.getShortDescription(Description);
    }

    GUIParameterCollection Master;
    @Override
    public void initMaster(GUIParameterCollection master) {
        Master = master;
    }

    @Override
    public GUIParameterCollection getMaster() {
        return Master;
    }

    @Override
    public double getAsDouble() {
        for(int i = 0;i<Values.length;i++)
            if(Values[i].equals(Value))
                return i;
        throw new OS4UnExpectedError("Can't find option for selected value");
    }

    @Override
    public String getAsString() {
        return Value;
    }
}
