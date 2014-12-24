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
package os4.serv.llib;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import os4.Common;
import os4.Mls;
import os4.OS4ExceptionInternalError;
import os4.serv.StreamTools;
import os4.serv.elements.ElementTable;

/**
 *
 * @author root
 */
public class LineLibFilter extends javax.swing.JDialog {
    private static final Logger Log = Common.getLogger(LineLibFilter.class);
    
    ViewPanel VP;
    /**
     * Creates new form LineLibFilter
     */
    public LineLibFilter() {
        super(Common.MainWindow,true);
        initComponents();
        Mls.translate(this);
        jtaFilterText.setText("#0<All<100000\r\n#Al>100\r\n#Cu=100\r\n#M\r\n#NM\r\n#Fe\r\nAll");
        VP = new ViewPanel(jScrollBar1);
        jpPrintPanel.add(VP,BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    class ViewPanel extends JPanel{
        String Error[];
        ArrayList<LineLibFoundedItem> Items;
        JScrollBar Sb;
        public ViewPanel(JScrollBar sb){
            Sb = sb;
        }
        
        public void init(String error){
            Error = error.split("\n");
            Items = null;
        }
        
        public void init(ArrayList<LineLibFoundedItem> items){
            Items = items;
            Error = null;
            Sb.setValue(0);
            Sb.setMaximum(items.size());
        }
        
        @Override
        public void paint(Graphics g){
            try{
                g.setColor(new Color(230,230,230));
                Dimension size = getSize();
                g.fillRect(0, 0, size.width, size.height);
                int y = 15,yh = 15;
                if(Error != null){
                    g.setColor(Color.red);
                    for(String line : Error){
                        g.drawString(line, 0, y);
                        y += yh;
                    }
                    return;
                }
                if(Items != null){
                    g.setColor(Color.black);
                    for(int i = Sb.getValue();i<Items.size();i++){
                        LineLibFoundedItem item = Items.get(i);
                        String line = item.toString();
                        g.drawString(line, 0, y);
                        y += yh;
                    }
                    return;
                }
            } catch(Exception ex){
                Log.log(Level.SEVERE,"Filtering paint error...",ex);
            }
        }
    }
    
    class Filter{
        int ElementFrom,ElementTo;
        int ValFrom,ValTo;
        int Type;
        String Error;
        
        final void parseElement(String txt) throws Exception{
            String tmp = txt.toLowerCase();
            if(tmp.equals("all") || tmp.equals("все")){
                ElementFrom = -1;
                ElementTo = 100000;
                return;
            }
            ElementFrom =  ElementTable.FindIndex(txt);
            if(ElementFrom < 0)
                throw new Exception("Wrong element name:"+txt);
            ElementTo = ElementFrom;
        }
        
        public Filter(String txt,int type){
            Type = type;
            ValFrom = -1;
            ValTo = Integer.MAX_VALUE;
            String splitter;
            if(txt.indexOf('>') > 0)
                splitter = ">";
            else{
                if(txt.indexOf('=') > 0)
                    splitter = "=";
                else {
                    if(txt.indexOf('<') > 0)
                        splitter = "<";
                    else
                        try{
                            parseElement(txt);
                            return;
                        } catch(Exception ex){
                            Error = ex.toString();
                            return;
                        }
                }
            }
            String values[] = txt.split(splitter);
            for(int i = 0;i<values.length;i++)
                values[i] = values[i].trim();
            int current = 0;
            int val1 = -1,val2 = Integer.MAX_VALUE;
            try{
                val1 = Integer.parseInt(values[current]);
                current ++; 
                try{
                    parseElement(values[current]);
                    current ++;
                } catch(Exception ex){
                    Error = ex.toString();
                    return;
                }
            } catch(Exception ex){
                try{
                    parseElement(values[current]);
                    current ++;
                } catch(Exception exe){
                    Error = exe.toString();
                    return;
                }
            }
            try{
                if(current < values.length)
                    val2 = Integer.parseInt(values[current]);
            }catch(Exception ex){
                Error = ex.toString();
                return;
            }
            if(splitter.equals("<")){
                ValFrom = val1;
                ValTo = val2;
            } else {
                if(splitter.equals(">")){
                    ValFrom = val2;
                    ValTo = Integer.MAX_VALUE;
                } else {
                    ValFrom = ValTo = val2;
                }
            }
        }
        
        public double isOk(LineLibRecord from_record) throws OS4ExceptionInternalError{
            double value;
            if(from_record.Element < ElementFrom || from_record.Element > ElementTo)
                return Double.NaN;
            switch(Type){
                case 0:
                    value = from_record.ZIntensIskra;
                    break;
                case 1:
                    value = from_record.ZIntensDuga;
                    break;
                case 2:
                    value = from_record.ZElemInt;
                    break;
                case 3:
                    value = from_record.NistIntens;
                    break;
                default:
                    throw new OS4ExceptionInternalError("Unknown source type...");
            }
            if(value < ValFrom || value > ValTo)
                return Double.NaN;
            return value;
        }
    }
    
    String lineFilter(String txt){
        int index = txt.indexOf("#");
        if(index >= 0)
            txt = txt.substring(0,index);
        txt.replace('\r', ' ');
        txt = txt.trim();
        if(txt.length() == 0)
            return null;
        return txt;
    }
    
    String CompilationErrors;
    public ArrayList<LineLibFoundedItem> find(){
        lbFoundCount.setText("-");
        ArrayList<LineLibFoundedItem> ret = new ArrayList<>();
        double ly_from = (Double)jspLyFrom.getValue();
        double ly_to = (Double)jspLyTo.getValue();
        int ion_from = (Integer)jspIonFrom.getValue();
        int ion_to = (Integer)jspIonTo.getValue();
        int type = jcbValueSourceType.getSelectedIndex();
        String filter_txt = jtaFilterText.getText();
        String filter_texts[] = filter_txt.split("\n");
        ArrayList<Filter> filters = new ArrayList<>();
        CompilationErrors = "";
        for(int i = 0;i<filter_texts.length;i++){
            String txt = lineFilter(filter_texts[i]);
            if(txt == null)
                continue;
            Filter f = new Filter(txt,type);
            if(f.Error != null){
                CompilationErrors += "Error in line: '"+txt+"'\n   "+f.Error+"\n";
                continue;
            }
            filters.add(f);
        }
        if(CompilationErrors.length() != 0){
            return null;
        }
        int using_level = cbUsing.getSelectedIndex();
        for(LineLibRecord rec:Common.LLib.DataSet){
            if(rec.Ly < ly_from || rec.Ly > ly_to ||
                    rec.IonLevel < ion_from || rec.IonLevel > ion_to ||
                    ElementTable.Elements[rec.Element].UsedFrequancy < using_level)
                continue;
            for(Filter f : filters){
                double val;
                try {
                    val = f.isOk(rec);
                    if(Double.isNaN(val) == false){
                        ret.add(new LineLibFoundedItem(rec, val));
                        break;
                    }
                } catch (OS4ExceptionInternalError ex) {
                    Common.getLogger(LineLibFilter.class).log(Level.SEVERE, null, ex);
                }
            }
        }
        lbFoundCount.setText(""+ret.size());
        return ret;
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
        jspLyFrom = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jspLyTo = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jspIonFrom = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jspIonTo = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jcbValueSourceType = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaFilterText = new javax.swing.JTextArea();
        jbOk = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jpPrintPanel = new javax.swing.JPanel();
        jScrollBar1 = new javax.swing.JScrollBar();
        jbCompile = new javax.swing.JButton();
        lbFoundCount = new javax.swing.JLabel();
        cbUsing = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabel1.setText("Длина волны от");

        jspLyFrom.setModel(new javax.swing.SpinnerNumberModel(1600.0d, 1600.0d, 9000.0d, 1.0d));

        jLabel2.setText("до");

        jspLyTo.setModel(new javax.swing.SpinnerNumberModel(9000.0d, 1600.0d, 9000.0d, 1.0d));

        jLabel3.setText("Степень ионизации от");

        jspIonFrom.setModel(new javax.swing.SpinnerNumberModel(0, 0, 16, 1));

        jLabel4.setText("до");

        jspIonTo.setModel(new javax.swing.SpinnerNumberModel(3, 0, 16, 1));

        jLabel5.setText("Источник");

        jcbValueSourceType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Зайдель. Искра", "Зайдель. Дуга", "Зайдель. По элементам", "НИСТ" }));

        jLabel6.setText("Фильтр концентраций");

        jtaFilterText.setColumns(20);
        jtaFilterText.setRows(5);
        jScrollPane1.setViewportView(jtaFilterText);

        jbOk.setText("Ок");
        jbOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOkActionPerformed(evt);
            }
        });

        jLabel7.setText("Предварительный просмотр результатов");

        jpPrintPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpPrintPanel.setLayout(new java.awt.BorderLayout());

        jScrollBar1.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                jScrollBar1AdjustmentValueChanged(evt);
            }
        });
        jpPrintPanel.add(jScrollBar1, java.awt.BorderLayout.EAST);

        jbCompile.setText(">");
        jbCompile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbCompile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCompileActionPerformed(evt);
            }
        });

        lbFoundCount.setText("-");

        cbUsing.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Все", "Экзотика", "Редкие", "Иногда", "Часто встречающиеся" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jcbValueSourceType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jspLyFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jspIonFrom)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jspLyTo, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jspIonTo, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbUsing, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbFoundCount)
                        .addContainerGap())
                    .addComponent(jbOk, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jbCompile, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpPrintPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jspLyFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jspLyTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(lbFoundCount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jpPrintPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbCompile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbOk))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jspIonFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jspIonTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jcbValueSourceType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(cbUsing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbCompileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCompileActionPerformed
        try{
            ArrayList<LineLibFoundedItem> values = find();
            if(values == null)
                VP.init(CompilationErrors);
            else
                VP.init(values);
            VP.repaint();
        }catch(Exception ex){
            Log.log(Level.SEVERE,"Filster preview error",ex);
        }
    }//GEN-LAST:event_jbCompileActionPerformed

    private void jScrollBar1AdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {//GEN-FIRST:event_jScrollBar1AdjustmentValueChanged
        try{
            VP.repaint();
        }catch(Exception ex){
            Log.log(Level.SEVERE,"Filster preview error",ex);
        }
    }//GEN-LAST:event_jScrollBar1AdjustmentValueChanged

    public ArrayList<LineLibFoundedItem> Results;
    private void jbOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOkActionPerformed
        try{
            Results = find();
            if(Results == null){
                VP.init(CompilationErrors);
                return;
            }
            save();
            setVisible(false);
        }catch(Exception ex){
            Log.log(Level.SEVERE,"Filster preview error",ex);
        }
    }//GEN-LAST:event_jbOkActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        Results = null;
    }//GEN-LAST:event_formComponentShown


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbUsing;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbCompile;
    private javax.swing.JButton jbOk;
    private javax.swing.JComboBox jcbValueSourceType;
    private javax.swing.JPanel jpPrintPanel;
    private javax.swing.JSpinner jspIonFrom;
    private javax.swing.JSpinner jspIonTo;
    private javax.swing.JSpinner jspLyFrom;
    private javax.swing.JSpinner jspLyTo;
    private javax.swing.JTextArea jtaFilterText;
    private javax.swing.JLabel lbFoundCount;
    // End of variables declaration//GEN-END:variables

    File Src;
    public void load(File f) throws IOException{
        Src = f;
        if(f.exists() == false)
            return;
        FileInputStream fis = new FileInputStream(Src);
        DataInputStream dis = new DataInputStream(fis);
        StreamTools.versionBlockBegin(dis, 1, 1);
        
        jspLyFrom.setValue(dis.readDouble());
        jspLyTo.setValue(dis.readDouble());
        jspIonFrom.setValue(dis.readInt());
        jspIonTo.setValue(dis.readInt());
        jcbValueSourceType.setSelectedIndex(dis.readInt());
        jtaFilterText.setText(StreamTools.readString(dis));
        cbUsing.setSelectedIndex(dis.readInt());
        
        StreamTools.versionBlockEnd(dis);
    }
    
    public void save() throws IOException{
        if(Src.exists() == false)
            Src.createNewFile();
        
        double ly_from = (Double)jspLyFrom.getValue();
        double ly_to = (Double)jspLyTo.getValue();
        int ion_from = (Integer)jspIonFrom.getValue();
        int ion_to = (Integer)jspIonTo.getValue();
        int type = jcbValueSourceType.getSelectedIndex();
        String filter_txt = jtaFilterText.getText();
        
        FileOutputStream fos = new FileOutputStream(Src);
        DataOutputStream dos = new DataOutputStream(fos);
        StreamTools.versionBlockBegin(dos, 1);
        dos.writeDouble(ly_from);
        dos.writeDouble(ly_to);
        dos.writeInt(ion_from);
        dos.writeInt(ion_to);
        dos.writeInt(type);
        StreamTools.writeString(filter_txt, dos);
        dos.writeInt(cbUsing.getSelectedIndex());
        
        StreamTools.versionBlockEnd(dos);        
    }
}
