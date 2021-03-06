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
import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import javax.swing.JPanel;
import os4.Common;
import os4.Mls;
import os4.serv.Dialogs;
import os4.task.comp.FolderViewer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import os4.OS4Exception;
import os4.dev.AbstractDevice;
import os4.dev.AbstractMeasuringCondition;
import os4.dev.SpectrData;
import os4.dev.SpectrDispers;
import os4.serv.Rectangle3D;
import os4.task.comp.CustomDrawingViewer;
import os4.task.comp.OSP4TreeListener;

/**
 *
 * @author root
 */
public class TaskLinking extends javax.swing.JPanel implements AbstractTask,
        OSP4TreeListener{
    private static final Logger Log = Common.getLogger(TaskLinking.class);

    FolderViewer FV;
    CustomDrawingViewer SpectrViewer;
    /**
     * Creates new form TaskLinking
     */
    public TaskLinking() throws Exception {
        initComponents();
        CurDispers = Common.getDevice().getDispers();
        FV = new FolderViewer(getFile(null), "fl", Common.SpectrExt);
        FV.setupCallBackNewFile(Mls.get("Измереить спектр"), this);
        FV.setupCallBackSelect(Mls.get("Выбор"), this);
        jSplitPane1.setTopComponent(FV);
        SpectrViewer = new CustomDrawingViewer(getFile(null).getAbsolutePath());
        jpView.add(SpectrViewer,BorderLayout.CENTER);
    }
    
    final static String EtalonName = "etalon";
    
    @Override
    final public File getFile(String name){
        File f;
        if(name == null){
            f = new File(Common.DirData+File.separator+"Linking");
            if(f.exists() == false)
                f.mkdir();
        } else
            f = new File(Common.DirData+File.separator+"Linking"+File.separator+name); 
        return f;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaLinkingText = new javax.swing.JTextPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jbSetAsDefult = new javax.swing.JButton();
        jbCompile = new javax.swing.JButton();
        jbSetCondition = new javax.swing.JButton();
        jpView = new javax.swing.JPanel();

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setMaximumSize(new java.awt.Dimension(150, 32767));
        jPanel1.setMinimumSize(new java.awt.Dimension(150, 100));
        jPanel1.setPreferredSize(new java.awt.Dimension(150, 4));
        jPanel1.setRequestFocusEnabled(false);
        jPanel1.setLayout(new java.awt.BorderLayout());

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jtaLinkingText.setToolTipText("S1:3 - полином pixel->ly");
        jScrollPane1.setViewportView(jtaLinkingText);

        jPanel5.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setBottomComponent(jPanel5);

        jPanel1.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jSplitPane2.setLeftComponent(jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jbSetAsDefult.setText("Установить как эталонный");
        jbSetAsDefult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSetAsDefultActionPerformed(evt);
            }
        });

        jbCompile.setText("Предпросмотр");
        jbCompile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCompileActionPerformed(evt);
            }
        });

        jbSetCondition.setText("Установить условия измерений");
        jbSetCondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSetConditionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jbCompile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbSetCondition)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbSetAsDefult))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbSetAsDefult)
                    .addComponent(jbCompile)
                    .addComponent(jbSetCondition)))
        );

        jPanel2.add(jPanel3, java.awt.BorderLayout.NORTH);

        jpView.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpView.setLayout(new java.awt.BorderLayout());
        jPanel2.add(jpView, java.awt.BorderLayout.CENTER);

        jSplitPane2.setRightComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    SpectrDispers CurDispers;
    private void jbCompileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCompileActionPerformed
        try{
            SpectrDispers disp = TaskLinkingComiler.compile(jtaLinkingText.getText());
            if(disp == null){
                Dialogs.errorWarnningMLS("Неудалось от компилировать из-за следующих ошибок:\r\n"+TaskLinkingComiler.LastError);
                return;
            }
            SpectrViewer.clearMarks();
            for(ArrayList<TaskLinkingComiler.LyToPixel> marks : TaskLinkingComiler.LyToPixelRange){
                for(TaskLinkingComiler.LyToPixel link : marks)
                    SpectrViewer.addMark(link.Ly, ""+link.Pixel, Color.red);
            }
            File f = getFile(null);
            File list[] = f.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith("."+Common.SpectrExt);
                }
            });
            for (File list1 : list) {
                SpectrData data = new SpectrData(list1, SpectrData.LevelRawData);
                data.Dispers = disp;
                data.save();
            }
            BaseSpectr = null;
            SelectedSpectr = null;
            checkView();
        }catch(Exception ex){
            Log.log(Level.SEVERE,"Linking compile error",ex);
        }
    }//GEN-LAST:event_jbCompileActionPerformed

    AbstractMeasuringCondition Condition = null;
    File ConditionFile;
    private AbstractMeasuringCondition getCurrentCondition() throws FileNotFoundException, IOException{
        if(Condition == null){
            ConditionFile = getFile("condition.bin");
            if(ConditionFile.exists()){
                FileInputStream fis = new FileInputStream(ConditionFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                Condition = AbstractMeasuringCondition.loadFrom(dis);
                dis.close();
            } else {
                ConditionFile.createNewFile();
                Condition = Common.getDevice().getDefaultCondition();
                commitCondition(Condition,ConditionFile);
            }            
        }
        return Condition;
    }
    
    void commitCondition(AbstractMeasuringCondition cond,File f) throws FileNotFoundException, IOException{
        FileOutputStream fos = new FileOutputStream(f);
        DataOutputStream dos = new DataOutputStream(fos);
        cond.save(dos);
        dos.flush();
        dos.close();
        Condition = cond;
    }
    
    private void jbSetConditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSetConditionActionPerformed
        try{
            AbstractMeasuringCondition cond = getCurrentCondition();
            JPanel pan = cond.getGUIEditor();
            boolean ret = Dialogs.showDialogWithCustomPanelMLS("Условия измерений",pan);
            if(ret)
                commitCondition(cond,ConditionFile);
        }catch(Exception ex){
            Log.log(Level.SEVERE,"Setup condition exposition...",ex);
        }
    }//GEN-LAST:event_jbSetConditionActionPerformed

    private void jbSetAsDefultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSetAsDefultActionPerformed
        try{
            File fdest = TaskHardCheck.getEtalonFile();
            File fsrc = getFile(BaseName);
            Files.copy(fsrc.toPath(), fdest.toPath(), REPLACE_EXISTING);
            AbstractMeasuringCondition cond = getCurrentCondition();
            fdest = TaskHardCheck.getConditionFile();
            commitCondition(cond, fdest);
        }catch(Exception ex){
            Log.log(Level.SEVERE,"Set as default exposition...",ex);
        }
    }//GEN-LAST:event_jbSetAsDefultActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JButton jbCompile;
    private javax.swing.JButton jbSetAsDefult;
    private javax.swing.JButton jbSetCondition;
    private javax.swing.JPanel jpView;
    private javax.swing.JTextPane jtaLinkingText;
    // End of variables declaration//GEN-END:variables

    @Override
    public int GetUserLevel() {
        return Common.USER_LEVEL_SETUP;
    }

    @Override
    public String getGUIName() {
        return Mls.get("Привязка шкалы длин волн");
    }


    String DefaultTxt;
    Charset DefaultCharset = Charset.forName("UTF-8");
    @Override
    public JPanel open() {
        File f = getFile("linking.txt");
        if(f.exists())
            try {
                byte[] enc = Files.readAllBytes(Paths.get(f.getAbsolutePath()));
                DefaultTxt = new String(enc,DefaultCharset);
            } catch (IOException ex) {
                Log.log(Level.SEVERE, "Rread linking text exception", ex);
                DefaultTxt = Common.getDevice().getDefaultLinkingText();
            }
        else
            DefaultTxt = Common.getDevice().getDefaultLinkingText();
        jtaLinkingText.setText(DefaultTxt);
        try {
            checkView();
        } catch (IOException ex) {
            Log.log(Level.SEVERE, "Init spectr error", ex);
        }
        return this;
    }

    void save() throws IOException{
        byte[] enc = DefaultTxt.getBytes(DefaultCharset);
        File f = getFile("linking.txt");
        Files.write(Paths.get(f.getAbsolutePath()), enc);
    }
    
    @Override
    public void close() {
        File f = getFile("linking.txt");
        if(f.exists() == false || jtaLinkingText.getText().equals(DefaultTxt) == false){
            if(Dialogs.getConfirmYesNoMLS("Привязка была изменена. Сохранить?", "Изменения"))
            try{
                DefaultTxt = jtaLinkingText.getText();
                save();
            } catch(Exception ex){
                Log.log(Level.SEVERE,"Save linking error...",ex);
            }
        }
        SpectrViewer.save();
    }

    final static String BaseName = EtalonName+"."+Common.SpectrExt;
    File BaseSpectrFile = null;
    SpectrData BaseSpectr;
    File SelectedSpectrFile = null;
    SpectrData SelectedSpectr = null;
    Rectangle3D ViewRect = null;
    public void checkView() throws IOException{
        BaseSpectrFile = getFile(BaseName);
        if(BaseSpectrFile.exists() && BaseSpectr == null)
            BaseSpectr = new SpectrData(BaseSpectrFile);
        if(SelectedSpectrFile != null)
            SelectedSpectr = new SpectrData(SelectedSpectrFile);
        else
            SelectedSpectr = null;
        if(BaseSpectr != null){
            ViewRect = SpectrViewer.init(BaseSpectr, null);
            if(SelectedSpectr == null)
                SpectrViewer.initSecond(null);
            else
                SpectrViewer.initSecond(SelectedSpectr);
            SpectrViewer.repaint();
        }
    }
    
    @Override
    public boolean treeCallBack(int option, TreePath path) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        FolderViewer.TreeNodeUserObject tnuo = (FolderViewer.TreeNodeUserObject)node.getUserObject();
        File f = tnuo.Path;
        switch(option){
            case FolderViewer.OptionNewFile:
                {
                    String name = null;
                    if(BaseSpectr == null)
                        name = EtalonName;
                    else {
                        //name = Dialogs.getStringMLS("Введите имя измеряемой пробы", "Начало измерения");
                        if(f.isDirectory() == false){
                            if(Dialogs.getConfirmYesNoMLS("Перемерить файл %s ?", "Перемерить", f.getAbsolutePath()) == false){
                            } else{
                                name = f.getName();
                                name = name.substring(0, name.length()-Common.SpectrExt.length()-1);
                            }
                            f = f.getParentFile();
                        }
                        if(name == null)
                            name = Dialogs.getStringMLS("Введите имя измеряемой пробы", "Начало измерения");
                        if(name == null)
                            return false;
                    }
                    AbstractDevice dev = Common.getDevice();
                    if(dev == null)
                        return false;
                    try {
                        SpectrData data = dev.measuring(getCurrentCondition());
                        String file_path = f.getAbsolutePath();
                        file_path += File.separator+name;
                        file_path += "."+Common.SpectrExt;
                        data.save(new File(file_path));
                    } catch (OS4Exception ex) {
                        Log.log(Level.SEVERE, "Internal error", ex);
                    } catch (IOException ex) {
                        Log.log(Level.SEVERE, "IOException", ex);
                    }
                }
                try {
                    checkView();
                } catch (IOException ex) {
                    Log.log(Level.SEVERE, "Load file error: ", ex);
                }
                break;
            case FolderViewer.OptionSelect:
                try{
                    if(f.getName().endsWith("."+Common.SpectrExt) == false)
                        SelectedSpectrFile = null;
                    else{
                        if(BaseSpectrFile.getAbsolutePath().equals(f.getAbsolutePath()))
                            SelectedSpectrFile = null;
                        else
                            SelectedSpectrFile = f;
                    }
                    checkView();
                }catch(Exception ex){
                    Log.log(Level.SEVERE,"Selectino error",ex);
                }
                break;
        }
        return true;
    }
}
