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
package os4.dev.method;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import os4.Common;
import os4.Mls;
import os4.task.AbstractTask;

/**
 *
 * @author root
 */
public class MultiAspectMethodEditor extends javax.swing.JPanel 
    implements AbstractTask{
    public static final Logger Log = Common.getLogger(MultiAspectMethodEditor.class);
    
    AbstractMethod Method;
    ArrayList<IMethodAspectEditor> Editors = new ArrayList<>();
    /**
     * Creates new form MultiAspectMethodEditor
     * @param method
     */
    public MultiAspectMethodEditor(AbstractMethod method) {
        Method = method;
        initComponents();
        IMethodAspect[] aspects = method.getAspectSet();
        if(aspects != null)
            for(IMethodAspect aspect : aspects){
                IMethodAspectEditor editor = aspect.getGUIEditor();
                Editors.add(editor);
                JPanel pan = editor.getPanel();
                String name = aspect.toString();
                jtpMainPanel.addTab(name, pan);
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

        jtpMainPanel = new javax.swing.JTabbedPane();

        jtpMainPanel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jtpMainPanelStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtpMainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 669, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtpMainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jtpMainPanelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jtpMainPanelStateChanged
        try{
            int sel = jtpMainPanel.getSelectedIndex();
            Editors.get(sel).update();
        }catch(Exception ex){
            Log.log(Level.SEVERE,"Update gui error...",ex);
        }
    }//GEN-LAST:event_jtpMainPanelStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane jtpMainPanel;
    // End of variables declaration//GEN-END:variables

    @Override
    public int GetUserLevel() {
        return Common.USER_LEVEL_METHODIST;
    }

    @Override
    public String getGUIName() {
        return Mls.get("Редактор методики");
    }

    @Override
    public JPanel open() {
        return this;
    }

    @Override
    public void close() {
        
    }

    @Override
    public File getFile(String name) {
        return Method.getFile(name);
    }
}