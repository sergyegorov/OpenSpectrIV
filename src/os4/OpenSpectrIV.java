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

package os4;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import os4.task.AbstractTask;
import os4.task.TaskLogin;

/**
 *
 * @author root
 */
public class OpenSpectrIV extends javax.swing.JFrame {
    private static final Logger Log = Common.getLogger( OpenSpectrIV.class ); 
    
    /**
     * Creates new form OpenSpectrIV
     */
    public OpenSpectrIV(){
        Common.MainWindow = this;
        initComponents();
        setTitle(Common.getGuiName());
        jpTop.setVisible(false);
        jpMainPanel.setLayout(new CardLayout());
        jbBack.setEnabled(false);
        try{
            ShowTask(new TaskLogin());
            pack();
        } catch(OS4ExceptionPermision ex){
            Log.log(Level.SEVERE,"It is not possible. Check permission of TaskLogin!",ex);
        }
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        boolean inited = false;
        try{
            Common.init();
            inited = true;
        } 
        catch(Exception ex){
            Log.log(Level.SEVERE,"Init error...",ex);
        }
        finally 
        {
            if(inited == false){
                JOptionPane.showConfirmDialog(null, "Fatal error", "Can't init data", JOptionPane.OK_OPTION);
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
        }
        Current = getCursor();
        CurrentBackgound = getBackground();
        WaitBackground = new Color(CurrentBackgound.getRed(),
            CurrentBackgound.getGreen(),
            CurrentBackgound.getBlue());
        WaitBackground = Color.black;
        //Log.info("Gui started...");
    }
    
    Color CurrentBackgound,WaitBackground;
    Cursor Current;
    public void setEnableFull(boolean flag){
        try{
            //dispose();
            if(flag == true){
                setCursor(Current);
                setBackground(CurrentBackgound);
                setEnabled(true);
                //com.sun.awt.AWTUtilities.setWindowOpacity(this, 1f);
            }else{
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                setBackground(WaitBackground);
                setEnabled(false);
                //com.sun.awt.AWTUtilities.setWindowOpacity(this, 0.5f);
            }
            repaint();
            //pack();
        } catch(Exception ex){
            Log.log(Level.SEVERE,"MainWindow enable error",ex);
        }
    }

    ArrayList<AbstractTask> ObjectStack = new ArrayList<>();
    final public void ShowTask(AbstractTask handler) throws OS4ExceptionPermision{
        try{
            int perm = ((AbstractTask)handler).GetUserLevel();
            if(perm > Common.UserLevel)
                throw new OS4ExceptionPermision("Wrong permission level",perm);
            if(jpMainPanel.getComponentCount() > 0){
                AbstractTask prev_panel;
                prev_panel = (AbstractTask)jpMainPanel.getComponent(0);
                prev_panel.close();
            }
            JPanel new_panel = handler.open();
            ObjectStack.add(handler);
            jpMainPanel.removeAll();
            jpMainPanel.add(new_panel,"C");
            updateBackButtonName();
            updateLayout();
        } catch(Exception ex){
            Log.log(Level.SEVERE, "Show new panel error!", ex);
        }
    }
    
    void updateLayout(){
        synchronized(getTreeLock()) {
                validateTree();
                jpMainPanel.revalidate();
                jpMainPanel.repaint();
            }
    }
    
    void updateBackButtonName(){
        if(ObjectStack.size() > 1){
                jbBack.setText(ObjectStack.get(ObjectStack.size()-2).getGUIName());
                jbBack.setEnabled(true);
            } else {
                jbBack.setEnabled(false);
                jbBack.setText("-");
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

        jpTop = new javax.swing.JPanel();
        jpBottom = new javax.swing.JPanel();
        jbBack = new javax.swing.JButton();
        jpStatus = new javax.swing.JPanel();
        jlLogMsg = new javax.swing.JLabel();
        jpMainPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jpTop.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jpTopLayout = new javax.swing.GroupLayout(jpTop);
        jpTop.setLayout(jpTopLayout);
        jpTopLayout.setHorizontalGroup(
            jpTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 756, Short.MAX_VALUE)
        );
        jpTopLayout.setVerticalGroup(
            jpTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );

        jpBottom.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jbBack.setText("-");
        jbBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbBackActionPerformed(evt);
            }
        });

        jpStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jlLogMsg.setText("jLabel1");

        javax.swing.GroupLayout jpStatusLayout = new javax.swing.GroupLayout(jpStatus);
        jpStatus.setLayout(jpStatusLayout);
        jpStatusLayout.setHorizontalGroup(
            jpStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpStatusLayout.createSequentialGroup()
                .addComponent(jlLogMsg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(91, 91, 91))
        );
        jpStatusLayout.setVerticalGroup(
            jpStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpStatusLayout.createSequentialGroup()
                .addComponent(jlLogMsg)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jpBottomLayout = new javax.swing.GroupLayout(jpBottom);
        jpBottom.setLayout(jpBottomLayout);
        jpBottomLayout.setHorizontalGroup(
            jpBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpBottomLayout.createSequentialGroup()
                .addComponent(jbBack, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpBottomLayout.setVerticalGroup(
            jpBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jbBack, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
            .addComponent(jpStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jpMainPanel.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.disabledForeground")));

        javax.swing.GroupLayout jpMainPanelLayout = new javax.swing.GroupLayout(jpMainPanel);
        jpMainPanel.setLayout(jpMainPanelLayout);
        jpMainPanelLayout.setHorizontalGroup(
            jpMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 758, Short.MAX_VALUE)
        );
        jpMainPanelLayout.setVerticalGroup(
            jpMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 287, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jpBottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jpMainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jpTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpMainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbBackActionPerformed
        try{
            AbstractTask dh = ObjectStack.remove(ObjectStack.size()-1);
            dh.close();
            jpMainPanel.removeAll();
            dh = ObjectStack.get(ObjectStack.size()-1);
            jpMainPanel.add(dh.open(),"C");
            updateBackButtonName();
            updateLayout();
        }catch(Exception ex){
            Log.log(Level.SEVERE, "Back button error", ex);
        }
    }//GEN-LAST:event_jbBackActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try{
            int from = ObjectStack.size()-1;
            for(int i = from;i>=0;i--)
                ObjectStack.get(i).close();
        } catch(Exception ex){
            Log.log(Level.SEVERE, "Closing error", ex);
        }
    }//GEN-LAST:event_formWindowClosing

    static FileHandler LogFH;
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws IOException {
        File f = new File(".");
        File[] logs = f.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("os4_log");
            }
        });
        for(File file : logs)
            try{
                file.delete();
            }catch(Exception ex){
                
            }
        //Logger logger = Common.getLogger("MyLog");  
        //FileHandler fh;  

        try {  
            // This block configure the logger with handler and formatter  
            LogFH = new FileHandler("os4_log");  
            Log.addHandler(LogFH);
            //LogFH.setLevel(Level.ALL);
            SimpleFormatter formatter = new SimpleFormatter();  
            LogFH.setFormatter(formatter);  

            LogFH.setFilter(new Filter(){
                @Override
                public boolean isLoggable(LogRecord record) {
                    if(Common.MainWindow != null)
                        try{
                        Common.MainWindow.jlLogMsg.setText(record.getMessage());
                        if(record.getLevel().equals(Level.SEVERE))
                            Common.MainWindow.jlLogMsg.setForeground(Color.RED);
                        else 
                            Common.MainWindow.jlLogMsg.setForeground(Color.black);
                        } catch(Exception ex){ex.printStackTrace();}
                    return true;
                }
            });
            // the following statement is used to log any messages  
            Log.info("Started...");  
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  

        Log.info("Log Started"); 
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        /*try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Common.getLogger(OpenSpectrIV.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Common.getLogger(OpenSpectrIV.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Common.getLogger(OpenSpectrIV.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Common.getLogger(OpenSpectrIV.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }//*/
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OpenSpectrIV().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbBack;
    private javax.swing.JLabel jlLogMsg;
    private javax.swing.JPanel jpBottom;
    private javax.swing.JPanel jpMainPanel;
    private javax.swing.JPanel jpStatus;
    private javax.swing.JPanel jpTop;
    // End of variables declaration//GEN-END:variables
}
