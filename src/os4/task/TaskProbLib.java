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
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import os4.Common;
import os4.Mls;
import os4.dev.AbstractDevice;
import os4.serv.Dialogs;
import os4.serv.llib.StandardProbLib;
import os4.task.comp.CSVEditor;
import os4.task.comp.FolderViewer;
import os4.task.comp.OSP4TreeListener;

/**
 *
 * @author root
 */
public class TaskProbLib extends javax.swing.JPanel implements AbstractTask,OSP4TreeListener{
    private static final Logger Log = Common.getLogger( TaskProbLib.class );
    
    FolderViewer FV;
    CSVEditor ConEditor;
    /**
     * Creates new form TaskProbLib
     */
    public TaskProbLib() {
        
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
        jpFolderPanel = new javax.swing.JPanel();
        jpEditorPanel = new javax.swing.JPanel();

        setMinimumSize(new java.awt.Dimension(300, 300));

        jSplitPane1.setDividerLocation(200);

        jpFolderPanel.setLayout(new java.awt.BorderLayout());
        jSplitPane1.setLeftComponent(jpFolderPanel);

        jpEditorPanel.setLayout(new java.awt.BorderLayout());
        jSplitPane1.setRightComponent(jpEditorPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 682, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel jpEditorPanel;
    private javax.swing.JPanel jpFolderPanel;
    // End of variables declaration//GEN-END:variables

    @Override
    public int GetUserLevel() {
        return Common.USER_LEVEL_METHODIST;
    }

    @Override
    public String getGUIName() {
        return Mls.get("Ввод данных по стандартам");
    }
    
    boolean Inited;
    @Override
    public JPanel open() {
        if(Inited == false){
            initComponents();
            AbstractDevice dev = Common.getDevice();
            FV = new FolderViewer(getFile(null), "stdsf", "csv");
            FV.setupCallBackSelect("Комплекты стандартов", this);
            FV.setupCallBackNewFile("Новый стандарт", this);
            jpFolderPanel.add(FV,BorderLayout.CENTER);
            Mls.translate(this);
            Inited = true;
        }
        return this;
    }

    @Override
    public void close() {
        
    }

    @Override
    public File getFile(String name) {
        return StandardProbLib.getFile(name);
    }

    public String SelectedPath;
    public CSVEditor SelectedEditor;
    @Override
    public boolean treeCallBack(int option, TreePath path) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        FolderViewer.TreeNodeUserObject tnuo = (FolderViewer.TreeNodeUserObject)node.getUserObject();
        File selected_file = tnuo.Path;
        switch(option){
            case FolderViewer.OptionSelect:
                jpEditorPanel.removeAll();
                SelectedEditor = null;
                SelectedPath = null;
                JPanel p;
                if(selected_file.isDirectory()){
                    p = new JPanel();
                    p.setLayout(new BorderLayout());
                    p.add(new JLabel(Mls.get("Папка стандатров")),BorderLayout.CENTER);
                } else {
                    SelectedEditor = new CSVEditor(selected_file,"элемент","стандарт");
                    p = SelectedEditor;
                    SelectedPath = Common.getRelativeDataPath(selected_file.getPath());
                }
                jpEditorPanel.add(p,BorderLayout.CENTER);
                jpEditorPanel.revalidate();
                jpEditorPanel.repaint();
                break;
            case FolderViewer.OptionNewFile:
                String val = Dialogs.getStringMLS("Введите имя нового комплекта стандартных образцов", 
                        "Создание комплекта");
                if(val == null)
                    return false;
                if(val.endsWith(".csv") == false)
                    val += ".csv";
                File new_file = new File(selected_file.getAbsolutePath()+File.separator+val);
                try{
                    new_file.createNewFile();
                } catch(Exception ex){
                    Log.log(Level.SEVERE,"Can't create file "+new_file,ex);
                    return false;
                }
                break;
        }
        return true;
    }
}
