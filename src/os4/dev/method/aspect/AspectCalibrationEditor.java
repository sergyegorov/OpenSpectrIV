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


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.logging.Level;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.DefaultTableModel;

import os4.Common;
import os4.Mls;
import os4.dev.method.AbstractFormula;
import os4.dev.method.AbstractMethod;
import os4.dev.method.IMethodAspectEditor;
import os4.serv.Dialogs;
import os4.serv.FixedColumnTable;
import os4.task.comp.ProbLibSelector;

import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class AspectCalibrationEditor extends javax.swing.JPanel 
    implements IMethodAspectEditor{
    static final Logger Log = Common.getLogger(AspectCalibrationEditor.class);
    
    AspectCalibration calibr;
    AbstractMethod method;
    AspectCalibrationTableModel tableModel;
    AbstractFormula selectedFormula;
    FixedColumnTable fixColumnDriver;
    final void updateTableModel(AspectCalibrationTableModel model){
        jtMeasuringProb.setModel(new DefaultTableModel());
        jtMeasuringProb.setModel(model);
        //fixColumnDriver.unInstall();
        fixColumnDriver = new FixedColumnTable(1, jScrollPane1);
    }
    /**
     * Creates new form AspectCalibrationEditor
     * @param calibr
     * @param method
     * @throws java.lang.Exception
     */
    public AspectCalibrationEditor(AspectCalibration calibr,AbstractMethod method) throws Exception {
        initComponents();
        this.calibr = calibr;
        this.method = method;
        
        jspProbDetail.setResizeWeight(0.5);
        jspDetailSpectr.setResizeWeight(0.5);
        jspDetailCalibr.setResizeWeight(0.5);
        
        tableModel = (AspectCalibrationTableModel)calibr.getModel();
        jtMeasuringProb.setModel(tableModel);
        jtMeasuringProb.setSelectionMode(WIDTH);
        jtMeasuringProb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jtMeasuringProb.getColumnModel().getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            try{
                int col = jtMeasuringProb.getSelectedColumn();
                selectedFormula = tableModel.getFormula(col);
                jpFormulaPanel.removeAll();
                jpFormulaPanel.add(selectedFormula.getEditor(),BorderLayout.CENTER);
                jpFormulaPanel.revalidate();
                jpFormulaPanel.repaint();
            } catch (Exception ex){
                Log.log(Level.SEVERE, "Ошибка выбора",ex);
            }
        });
        fixColumnDriver = new FixedColumnTable(1, jScrollPane1);
        
        JMenuBar mbar = new JMenuBar();
        add(mbar,BorderLayout.NORTH);
        
        //---------- Prob menu ---------------------------------
        JMenu menu = new JMenu(Mls.get("Стандартные образцы"));
        menu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                //MIProbAddStandartd.setEnabled(false);
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                //MIProbAddStandartd.setEnabled(false);
            }

            @Override
            public void menuCanceled(MenuEvent e) {
                //MIProbAddStandartd.setEnabled(false);
            }
        });
        mbar.add(menu);
        MIProbAddStandartd = new JMenuItem(Mls.get("Добавить новый"));
        menu.add(MIProbAddStandartd);
        MIProbAddStandartd.addActionListener((ActionEvent e) -> {
            try{
                ProbLibSelector pls = new ProbLibSelector();
                pls.setVisible(true);
                if(pls.SelectedComplect != null && pls.SelectedProb != null){
                    String name = this.calibr.filterStandartName(pls.SelectedProb);
                    if(name == null)
                        return;

                    AspectCalibrationTableModel tm = (AspectCalibrationTableModel)this.calibr.getModel();
                    tm.insertProb(pls.SelectedComplect,name);
                    tm.upDate();
                    //jtMeasuringProb.setModel(new DefaultTableModel());
                    updateTableModel(tm);
                    this.calibr.commit();
                }
            }catch(Exception ex){
                Log.log(Level.SEVERE, "Add new standart exception",ex);
            }
        });
        MIProbAddMeasuring = new JMenuItem(Mls.get("Добавить ещё один прожег"));
        menu.add(MIProbAddMeasuring);
        MIProbAddMeasuring.addActionListener((ActionEvent e) -> {
            try{
                int row = jtMeasuringProb.getSelectedRow();
                if(row < 0){
                    Dialogs.errorWarnningMLS("Выберите пробу к которой надо добавить прожег");
                    return;
                }
                AspectCalibrationTableModel tm = (AspectCalibrationTableModel)this.calibr.getModel();
                tm.insertMeasuringForRow(row);
                tm.upDate();
                //jtMeasuringProb.setModel(new DefaultTableModel());
                updateTableModel(tm);
                this.calibr.commit();
            }catch(Exception ex){
                Log.log(Level.SEVERE, "Add new standart exception",ex);
            }
        });
        menu.addSeparator();
        
        
        //-------- Formula menu ---------------------------------
        menu = new JMenu(Mls.get("Измеряемые элементы"));
        menu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                //MIProbAddStandartd.setEnabled(false);
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                //MIProbAddStandartd.setEnabled(false);
            }

            @Override
            public void menuCanceled(MenuEvent e) {
                //MIProbAddStandartd.setEnabled(false);
            }
        });
        mbar.add(menu);
        MIElemAddFormula = new JMenuItem(Mls.get("Добавить новый элемент или формулу"));
        menu.add(MIElemAddFormula);
        MIElemAddFormula.addActionListener((ActionEvent e) -> {
            try{
                String element_name = Dialogs.getStringMLS("Введите имя измеряемого в формуле элемента", "Создание формулы");
                if(element_name == null)
                    return;
                element_name = this.calibr.filterElementName(element_name);
                if(element_name == null){
                    Dialogs.errorWarnningMLS("Введенный элемент не указан как измеряемый в описании методики.");
                    return;
                }
                String formula_name = Dialogs.getStringMLS("Введите краткое имя создаваемой формулы", "Создание формулы");
                if(formula_name == null)
                    return;
                AbstractFormula formula = this.method.createNewFormula(element_name, formula_name);
                AspectCalibrationTableModel tm = (AspectCalibrationTableModel)this.calibr.getModel();
                tm.insertFormula(formula);
                tm.upDate();
                //jtMeasuringProb.setModel(new DefaultTableModel());
                updateTableModel(tm);
                this.calibr.commit();
            }catch(Exception ex){
                Log.log(Level.SEVERE, "Add new formula error",ex);
            }
        });
    }
    
    JMenuItem MIProbAddStandartd,MIProbAddMeasuring;
    JMenuItem MIElemAddFormula;
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jspProbDetail = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtMeasuringProb = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jspDetailSpectr = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        jspDetailCalibr = new javax.swing.JSplitPane();
        jpFormulaPanel = new javax.swing.JPanel();
        jpCalibrGraph = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jpSpectrPanel = new javax.swing.JPanel();

        jMenu3.setText("File");
        jMenuBar2.add(jMenu3);

        jMenu4.setText("Edit");
        jMenuBar2.add(jMenu4);

        setLayout(new java.awt.BorderLayout());

        jspProbDetail.setResizeWeight(0.3);

        jtMeasuringProb.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jtMeasuringProb);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
        );

        jspProbDetail.setLeftComponent(jPanel1);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jspDetailSpectr.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jspDetailSpectr.setResizeWeight(0.6);

        jspDetailCalibr.setDividerLocation(310);
        jspDetailCalibr.setResizeWeight(0.6);

        jpFormulaPanel.setLayout(new java.awt.BorderLayout());
        jspDetailCalibr.setLeftComponent(jpFormulaPanel);

        jpCalibrGraph.setLayout(new java.awt.BorderLayout());
        jspDetailCalibr.setRightComponent(jpCalibrGraph);

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 26, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jspDetailCalibr, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jspDetailCalibr))
        );

        jspDetailSpectr.setTopComponent(jPanel3);

        jpSpectrPanel.setLayout(new java.awt.BorderLayout());
        jspDetailSpectr.setRightComponent(jpSpectrPanel);

        jPanel2.add(jspDetailSpectr, java.awt.BorderLayout.CENTER);

        jspProbDetail.setRightComponent(jPanel2);

        add(jspProbDetail, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jpCalibrGraph;
    private javax.swing.JPanel jpFormulaPanel;
    private javax.swing.JPanel jpSpectrPanel;
    private javax.swing.JSplitPane jspDetailCalibr;
    private javax.swing.JSplitPane jspDetailSpectr;
    private javax.swing.JSplitPane jspProbDetail;
    private javax.swing.JTable jtMeasuringProb;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update() {
        
    }

    @Override
    public JPanel getPanel() {
        return this;
    }
}
