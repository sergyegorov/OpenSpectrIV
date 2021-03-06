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
package os4.dev.method.formula;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import os4.Common;
import os4.dev.method.AbstractChanel;
import os4.dev.method.AbstractMethod;
import os4.dev.method.IMethodChanelSet;
import os4.task.comp.ProbLibSelector;

/**
 *
 * @author root
 */
public class FormulaGOSTEditor extends javax.swing.JPanel {
    final public static Logger Log = Common.getLogger(ProbLibSelector.class);
    AbstractMethod Method;
    FormulaGOST Formula;
    
    boolean IsIniting;
    /**
     * Creates new form FormulaGOSTEditor
     * @param formula
     * @param method
     */
    public FormulaGOSTEditor(FormulaGOST formula,AbstractMethod method) {
        initComponents();
        Method = method;
        Formula = formula;
        IsIniting = true;
        jspTimeFrom.setValue(Formula.TimeFrom);
        jspTimeTo.setValue(Formula.TimeTo);
        jspConFrom.setValue(Formula.ConFrom);
        jspConTo.setValue(Formula.ConTo);
        jcbFunctionType.setSelectedIndex(Formula.CalibrFk);
        initChanelInformation();
        jcbChanelAnalit.setSelectedIndex(findChanelById(Formula.ChanelAnalitId));
        jcbChanelComp.setSelectedIndex(findChanelById(Formula.ChanelCompId));
        IsIniting = false;
    }
    
    final int findChanelById(int id){
        for(int i = 0;i<Chanels.size();i++){
            AbstractChanel ch = Chanels.get(i);
            if(ch.getId() == id)
                return i;
        }
        return -1;
    }
    
    ArrayList<AbstractChanel> Chanels = new ArrayList<>();
    final void initChanelInformation(){
        jcbChanelAnalit.removeAllItems();
        jcbChanelComp.removeAllItems();
        IMethodChanelSet set = (IMethodChanelSet) Method.getAspect(AbstractMethod.ASPECT_CHANEL);
        for(int i = 0;i<set.getChanelCount();i++){
            AbstractChanel ch = set.getChanel(i);
            Chanels.add(ch);
            jcbChanelAnalit.addItem(ch);
            jcbChanelComp.addItem(ch);
        }
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
        jspTimeFrom = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jspTimeTo = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jcbFunctionType = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jcbChanelAnalit = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jcbChanelComp = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jspConFrom = new javax.swing.JSpinner();
        jspConTo = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();

        jLabel1.setText("Р¤РѕС‚РѕРјРµС‚СЂРёСЂРѕРІР°С‚СЊ");

        jspTimeFrom.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1000.0f), Float.valueOf(1.0f)));
        jspTimeFrom.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jspTimeFromStateChanged(evt);
            }
        });

        jLabel2.setText("РїРѕ");

        jspTimeTo.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1000.0f), Float.valueOf(0.01f), Float.valueOf(1000.0f), Float.valueOf(1.0f)));
        jspTimeTo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jspTimeToStateChanged(evt);
            }
        });

        jLabel3.setText("СЃРµРєСѓРЅРґСѓ");

        jcbFunctionType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "РџСЂСЏРјРѕР№ РіСЂР°С„РёРє", "РљСЂРёРІР°СЏ 2-РіРѕ РїРѕСЂСЏРґРєР°", "РљСЂРёРІР°СЏ 3-РіРѕ РїРѕСЂСЏРґРєР°" }));
        jcbFunctionType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbFunctionTypeItemStateChanged(evt);
            }
        });

        jLabel4.setText("РђРЅР°Р»РёС‚РёС‡РµСЃРєРёР№ РєР°РЅР°Р»");

        jcbChanelAnalit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbChanelAnalit.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbChanelAnalitItemStateChanged(evt);
            }
        });

        jLabel5.setText("РљР°РЅР°Р» РІРЅСѓС‚СЂРµРЅРЅРµРіРѕ СЃС‚Р°РЅРґР°СЂС‚Р°");

        jcbChanelComp.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbChanelComp.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbChanelCompItemStateChanged(evt);
            }
        });

        jLabel6.setText("СЃ");

        jLabel7.setText("Р�Р·РјРµСЂСЏРµРјС‹Р№ РґРёР°РїР°Р·РѕРЅ РєРѕРЅС†РµРЅС‚СЂР°С†РёР№");

        jLabel8.setText("РѕС‚");

        jspConFrom.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), Float.valueOf(1.0f)));
        jspConFrom.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jspConFromStateChanged(evt);
            }
        });

        jspConTo.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(100.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), Float.valueOf(1.0f)));
        jspConTo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jspConToStateChanged(evt);
            }
        });

        jLabel9.setText("РґРѕ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcbFunctionType, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jcbChanelComp, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jcbChanelAnalit, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jspTimeFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jspTimeTo, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel3))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jspConFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jspConTo, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jspTimeFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jspTimeTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jspConFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jspConTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jcbFunctionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbChanelAnalit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbChanelComp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(58, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jspTimeFromStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jspTimeFromStateChanged
        try{
            if(IsIniting)
                return;
            Formula.TimeFrom = (float)jspTimeFrom.getValue();
            Formula.update(Method.getDBConnection());
            Method.getDBConnection().commit();
        }catch(Exception ex){
            Log.log(Level.SEVERE, "TimeFrom change error...",ex);
        }
    }//GEN-LAST:event_jspTimeFromStateChanged

    private void jspTimeToStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jspTimeToStateChanged
        try{
            if(IsIniting)
                return;
            Formula.TimeTo = (float)jspTimeTo.getValue();
            Formula.update(Method.getDBConnection());
            Method.getDBConnection().commit();
        }catch(Exception ex){
            Log.log(Level.SEVERE, "TimeFrom change error...",ex);
        }
    }//GEN-LAST:event_jspTimeToStateChanged

    private void jspConFromStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jspConFromStateChanged
        try{
            if(IsIniting)
                return;
            Formula.ConFrom = (float)jspConFrom.getValue();
            Formula.update(Method.getDBConnection());
            Method.getDBConnection().commit();
        }catch(Exception ex){
            Log.log(Level.SEVERE, "TimeFrom change error...",ex);
        }
    }//GEN-LAST:event_jspConFromStateChanged

    private void jspConToStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jspConToStateChanged
        try{
            if(IsIniting)
                return;
            Formula.ConTo = (float)jspConTo.getValue();
            Formula.update(Method.getDBConnection());
            Method.getDBConnection().commit();
        }catch(Exception ex){
            Log.log(Level.SEVERE, "TimeFrom change error...",ex);
        }
    }//GEN-LAST:event_jspConToStateChanged

    private void jcbFunctionTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbFunctionTypeItemStateChanged
        try{
            if(IsIniting)
                return;
            Formula.CalibrFk = jcbFunctionType.getSelectedIndex();
            Formula.update(Method.getDBConnection());
            Method.getDBConnection().commit();
        }catch(Exception ex){
            Log.log(Level.SEVERE, "TimeFrom change error...",ex);
        }
    }//GEN-LAST:event_jcbFunctionTypeItemStateChanged

    private void jcbChanelAnalitItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbChanelAnalitItemStateChanged
        try{
            if(IsIniting)
                return;
            IMethodChanelSet set = (IMethodChanelSet)Method.getAspect(AbstractMethod.ASPECT_CHANEL);
            Formula.ChanelAnalitId = set.getChanel(jcbChanelAnalit.getSelectedIndex()).getId();
            Formula.update(Method.getDBConnection());
            Method.getDBConnection().commit();
        }catch(Exception ex){
            Log.log(Level.SEVERE, "TimeFrom change error...",ex);
        }

    }//GEN-LAST:event_jcbChanelAnalitItemStateChanged

    private void jcbChanelCompItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbChanelCompItemStateChanged
        try{
            if(IsIniting)
                return;
            IMethodChanelSet set = (IMethodChanelSet)Method.getAspect(AbstractMethod.ASPECT_CHANEL);
            Formula.ChanelCompId = set.getChanel(jcbChanelComp.getSelectedIndex()).getId();
            Formula.update(Method.getDBConnection());
            Method.getDBConnection().commit();
        }catch(Exception ex){
            Log.log(Level.SEVERE, "TimeFrom change error...",ex);
        }    }//GEN-LAST:event_jcbChanelCompItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JComboBox jcbChanelAnalit;
    private javax.swing.JComboBox jcbChanelComp;
    private javax.swing.JComboBox jcbFunctionType;
    private javax.swing.JSpinner jspConFrom;
    private javax.swing.JSpinner jspConTo;
    private javax.swing.JSpinner jspTimeFrom;
    private javax.swing.JSpinner jspTimeTo;
    // End of variables declaration//GEN-END:variables
}
