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
import java.io.FileFilter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import os4.Mls;
import os4.serv.Dialogs;
import os4.Common;

/**
 *
 * @author root
 */
public class FolderViewer extends javax.swing.JPanel {
    private static final Logger Log = Common.getLogger( FolderViewer.class );
    
    public final static int OptionNewFile = 0;
    public final static int OptionNewFolder = 1;
    public final static int OptionRemove = 2;
    public final static int OptionSelect = 3;
    
    private File FileRootDir;
    private String FolderExt,FileExt;
    
    public class TreeNodeUserObject{
        public File Path;
        String Name;
        
        public TreeNodeUserObject(String name,File path){
            Name = name;
            Path = path;
        }
        
        public TreeNodeUserObject(File path){
            Name = getNameOnly(path.getName());
            Path = path;
        }
        
        public boolean isDirectory(){
            return Path.isDirectory();
        }
        
        @Override
        public String toString(){
            return Name;
        }
    }
    /**
     * Creates new form FolderViewer
     * @param header - title of the control
     * @param path - Base folder with file root
     * @param folder_ext - extention for all listed folders
     * @param file_ext - extention for all listed files
     */
    public FolderViewer(File path,String folder_ext,String file_ext) {
        initComponents();
        FileRootDir = path;
        if(folder_ext.charAt(0) == '.')
            FolderExt = folder_ext;
        else
            FolderExt = "."+folder_ext;
        if(file_ext.charAt(0) == '.')
            FileExt = file_ext;
        else
            FileExt = "."+file_ext;
        if(path.exists() == false)
            path.mkdir();
        reload();
    }
    
    final void reload(){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        root.setUserObject(new TreeNodeUserObject(Mls.get("Корневая папка"),
                FileRootDir));
        jtValueTree.setRootVisible(true);
        add(root,FileRootDir);
        jtValueTree.setModel(new DefaultTreeModel(root));
    }
    
    final String getNameOnly(String name){
        int ind = name.lastIndexOf('.');
        if(ind > 0)
            return name.substring(0,ind);
        return name;
    }
    
    final void add(DefaultMutableTreeNode root,File base_dir){
        File[] list = base_dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName();
                return name.endsWith(FileExt) ||
                        name.endsWith(FolderExt);
            }
        });
        for(int i = 0;i<list.length;i++){
            File f = list[i];
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(getNameOnly(f.getName()));
            node.setUserObject(new TreeNodeUserObject(f));
            root.add(node);
            if(f.isDirectory())
                add(node,f);
        }
    }
    
    OSP4TreeListener SelectListener;
    public void setupCallBackSelect(String header,OSP4TreeListener listener){
        SelectListener = listener;
        setBorder(javax.swing.BorderFactory.createTitledBorder(header));
    }
    
    OSP4TreeListener DeleteListener;
    public void setupCallBackDelete(String text,OSP4TreeListener listener){
        DeleteListener = listener;
        jbDelete.setText(text);
    }
    
    OSP4TreeListener NewFolderListener;
    public void setupCallBackNewFolder(String text,OSP4TreeListener listener){
        NewFolderListener = listener;
        jbNewFolder.setText(text);
    }
    
    OSP4TreeListener NewFileListener;
    public void setupCallBackNewFile(String text,OSP4TreeListener listener){
        jbNewFile.setText(text);
        NewFileListener = listener;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jtValueTree = new javax.swing.JTree();
        jbNewFile = new javax.swing.JButton();
        jbNewFolder = new javax.swing.JButton();
        jbDelete = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Установи обработчик select"));
        setMinimumSize(new java.awt.Dimension(254, 189));

        jtValueTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jtValueTreeValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jtValueTree);

        jbNewFile.setText("To Be Inited...");
        jbNewFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbNewFileActionPerformed(evt);
            }
        });

        jbNewFolder.setText("Новая папка");
        jbNewFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbNewFolderActionPerformed(evt);
            }
        });

        jbDelete.setText("Удалить");
        jbDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
            .addComponent(jbNewFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbNewFolder, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                    .addComponent(jbDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbNewFile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbNewFolder)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbDelete))
        );
    }// </editor-fold>//GEN-END:initComponents

    /*TreePath getFolderPath(TreePath path){
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)(path.getLastPathComponent());
        TreeNodeUserObject ret = (TreeNodeUserObject)node.getUserObject();
        if(ret.isDirectory() == false)
            return path.getParentPath();
        else
            return path;
    }*/
    
    private void jbNewFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbNewFileActionPerformed
        try{
            TreePath path = jtValueTree.getSelectionPath();
            if(path == null){
                Dialogs.errorWarnningMLS("Выберите папку в которую будут помещены результаты.");
                return;
            }
            if(NewFileListener.treeCallBack(OptionNewFile,path))//getFolderPath(path)))
                reload();
        } catch(Exception ex){
            Log.log(Level.SEVERE,"Create new file exception",ex);
        }
    }//GEN-LAST:event_jbNewFileActionPerformed

    private void jbNewFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbNewFolderActionPerformed
        try{
            TreePath tpath = jtValueTree.getSelectionPath();
            if(NewFolderListener != null && 
                    NewFolderListener.treeCallBack(OptionNewFolder,tpath) == false)
                return;
            File root = ((TreeNodeUserObject)((DefaultMutableTreeNode)tpath.getLastPathComponent()).getUserObject()).Path;
            if(root.isDirectory() == false){
                Dialogs.errorWarnningMLS("Выберите папка, где необходимо создать другую папку");
                return;
            }
            String name = Dialogs.getStringMLS("Введите имя новой папки", "Создание папки");
            File folder = new File(root.getAbsolutePath()+File.separator+name+FolderExt);
            folder.mkdir();
            reload();
        } catch(Exception ex){
            Log.log(Level.SEVERE,"Create new file exception",ex);
        }
    }//GEN-LAST:event_jbNewFolderActionPerformed

    public boolean isFolder(File file){
        return file.getName().endsWith(FolderExt) | file.equals(FileRootDir);
    }
    
    private void jbDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbDeleteActionPerformed
        try{
            TreePath tpath = jtValueTree.getSelectionPath();
            if(DeleteListener != null && 
                    DeleteListener.treeCallBack(OptionRemove,tpath) == false)
                return;
            File root = ((TreeNodeUserObject)((DefaultMutableTreeNode)tpath.getLastPathComponent()).getUserObject()).Path;
            if(isFolder(root)){ 
                if(Dialogs.getConfirmYesNoMLS("Удалить папку %s ?" , "Удаление", root.getAbsolutePath()) == false)
                    return;
            } else {
                if(Dialogs.getConfirmYesNoMLS("Удалить файл %s?", "Удаление", root.getAbsolutePath()) == false)
                    return;
            }
            if(root.delete());
                reload();
        } catch(Exception ex){
            Log.log(Level.SEVERE,"Create new file exception",ex);
        }
    }//GEN-LAST:event_jbDeleteActionPerformed

    private void jtValueTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jtValueTreeValueChanged
        try{
            if(SelectListener != null)
                SelectListener.treeCallBack(OptionSelect, evt.getPath());
        }catch(Exception ex){
            Log.log(Level.SEVERE,"Selection exception",ex);
        }
    }//GEN-LAST:event_jtValueTreeValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbDelete;
    private javax.swing.JButton jbNewFile;
    private javax.swing.JButton jbNewFolder;
    private javax.swing.JTree jtValueTree;
    // End of variables declaration//GEN-END:variables
}
