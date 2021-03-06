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
package os4.task;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import os4.Common;
import os4.Mls;
import os4.dev.AbstractDevice;
import os4.dev.method.AbstractMethod;
import os4.serv.Dialogs;
import os4.task.comp.FolderViewer;
import os4.task.comp.OSP4TreeListener;

/**
 *
 * @author root
 */
public class TaskMethodSelector extends javax.swing.JPanel implements AbstractTask,OSP4TreeListener {
    private static final Logger Log = Common.getLogger( TaskMethodSelector.class );
    
    FolderViewer FV;
    /**
     * Creates new form TaskMethodSelector
     */
    public TaskMethodSelector() {
        initComponents();
        AbstractDevice dev = Common.getDevice();
        FV = new FolderViewer(getFile(null), "mf", dev.getMethodExtension());
        jpMethodFolder.add(FV,BorderLayout.CENTER);
        FV.setupCallBackNewFile("Создать методику", this);
        FV.setupCallBackSelect("Выбрать методику", this);
        Mls.translate(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jpPreview = new javax.swing.JPanel();
        jpMethodFolder = new javax.swing.JPanel();

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setResizeWeight(0.2);

        jpPreview.setLayout(new java.awt.BorderLayout());
        jSplitPane1.setRightComponent(jpPreview);

        jpMethodFolder.setLayout(new java.awt.BorderLayout());
        jSplitPane1.setLeftComponent(jpMethodFolder);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel jpMethodFolder;
    private javax.swing.JPanel jpPreview;
    // End of variables declaration//GEN-END:variables

    @Override
    public int GetUserLevel() {
        return Common.USER_LEVEL_METHODIST;
    }

    @Override
    public String getGUIName() {
        return Mls.get("Выбор методики");
    }
    
    @Override
    public JPanel open() {
        updatePreview();
        return this;
    }

    @Override
    public void close() {
        
    }

    @Override
    public File getFile(String name) {
        if(name == null){
            File ret = new File(Common.DirData+File.separator+"Methods");
            if(ret.exists() == false)
                ret.mkdir();
            return ret;
        } else {
            File ret = new File(Common.DirData+File.separator+"Methods"+File.separator+name);
            return ret;            
        }
    }

    AbstractMethod SelectedMethod;
    JLabel jlPreviewLb;
    void updatePreview(){
        if(jlPreviewLb != null && SelectedMethod != null)
            jlPreviewLb.setText(SelectedMethod.getDescription());
    }
    @Override
    public boolean treeCallBack(int option, TreePath path) {
        try{
            AbstractDevice dev = Common.getDevice();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
            FolderViewer.TreeNodeUserObject tnuo = (FolderViewer.TreeNodeUserObject)node.getUserObject();
            File selected_file = tnuo.Path;
            switch(option){
                case FolderViewer.OptionSelect:
                    jpPreview.removeAll();
                    JPanel p;
                    if(FV.isFolder(selected_file)){
                        p = new JPanel();
                        p.setLayout(new BorderLayout());
                        p.add(new JLabel(Mls.get("Папка методик")),BorderLayout.CENTER);
                    } else {
                        p = new JPanel();
                        try {
                            SelectedMethod = dev.loadMethod(selected_file);
                            p.setLayout(new BorderLayout());
                            jlPreviewLb = new JLabel();
                            updatePreview();
                            jlPreviewLb.setVerticalAlignment(SwingConstants.TOP);
                            jlPreviewLb.setHorizontalAlignment(SwingConstants.LEFT);
                            p.add(jlPreviewLb,BorderLayout.CENTER);
                            JButton edit_btn = new JButton(Mls.get("Редактировать"));
                            p.add(edit_btn,BorderLayout.SOUTH);
                            edit_btn.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try{
                                        Common.MainWindow.ShowTask(SelectedMethod.getGUIEditor());
                                    } catch(Exception ex){
                                        Log.log(Level.SEVERE,"Open method editor exception",ex);
                                    }
                                }
                            });
                        } catch (IOException ex) {
                            Log.log(Level.SEVERE, null, ex);
                        }
                    }
                    jpPreview.add(p,BorderLayout.CENTER);
                    jpPreview.revalidate();
                    jpPreview.repaint();
                    break;
                case FolderViewer.OptionNewFile:
                    String val = Dialogs.getStringMLS("Введите имя новой калиброванной методкики", 
                            "Создание методики");
                    if(val == null)
                        return false;
                    String method_ext = dev.getMethodExtension();
                    if(val.endsWith(method_ext) == false)
                        val += "."+method_ext;
                    File new_file = new File(selected_file.getAbsolutePath()+File.separator+val);
                    try{
                        new_file.mkdir();
                    } catch(Exception ex){
                        Log.log(Level.SEVERE,"Can't create directory "+new_file,ex);
                        return false;
                    }
                    break;
            }    
        }catch(Exception ex){
            Log.log(Level.SEVERE,"Method selection exception ",ex);
        }
        return true;
    }
}
