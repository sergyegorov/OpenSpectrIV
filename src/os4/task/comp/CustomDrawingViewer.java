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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import os4.dev.CustomDrawing;
import os4.dev.SpectrData;
import os4.dev.SpectrDispers;
import os4.serv.MathTools;
import os4.serv.Rectangle3D;
import os4.serv.llib.LineLibFilter;
import os4.serv.llib.LineLibFoundedItem;
import os4.Common;

/**
 *
 * @author root
 */
public class CustomDrawingViewer extends javax.swing.JPanel {
    private static final Logger Log = Common.getLogger( FolderViewer.class );

    ViewPan VP;
    /**
     * Creates new form CustomDrawingViewer
     */
    public CustomDrawingViewer(String base_dir) {
        initComponents();
        jpDrawPanel.setLayout(new BorderLayout());
        VP = new ViewPan();
        jpDrawPanel.add(VP,BorderLayout.CENTER);
        initDir(base_dir);
    }
    
    final public void initDir(String base_dir){
        LineFilter = new LineLibFilter();
        try{
            LineFilter.load(new File(base_dir+File.separator+"f1.bin"));
        } catch(Exception ex){
            Log.log(Level.SEVERE,"Loading filters data error",ex);
        }
    }

    public void save(){
        try {
            LineFilter.save();
        } catch (IOException ex) {
            Log.log(Level.SEVERE, "Saving filter 1 infomatin error", ex);
        }
    }
    
    LineLibFilter LineFilter;
    public void clearMarks(){
        VP.Marks.clear();
    }
    
    public void addMark(double x,double y,String name,Color col){
        VP.Marks.add(VP.new Mark(x,y,name,col));
    }
    
    public void addMark(double ly,String name,Color col){
        VP.Marks.add(VP.new Mark(ly,name,col));
    }
    
    public void clearTextInfo(){
        TextInfo.clear();
    }
    
    public void addTextInfo(String txt){
        String lines[] = txt.split("\n");
        for(String line : lines)
            TextInfo.add(line.replace("\r", ""));
    }
    
    Color PalitraMainSpectr[] = {Color.BLACK,Color.GRAY};
    Color PalitraSecondSpectr[] = {new Color(0x000088),new Color(0x0000FF)};
    CustomDrawingConverter Converter;
    ArrayList<LineLibFoundedItem> LineLibSet = null;
    ArrayList<String> TextInfo = new ArrayList<>();
    class ViewPan extends JPanel{
        public ViewPan(){
            
        }

        public class Mark{
            public double X,Y;
            public String Name;
            public Color Col;
            public Mark(double x,double y,String name,Color col){
                X = x;
                Y = y;
                Name = name;
                Col = col;
            }
            
            public Mark(double x,String name,Color col){
                X = x;
                Y = Double.NaN;
                Name = name;
                Col = col;
            }
            
            public int draw(Graphics g,int h,int last_y){
                int x = Converter.getScreenCoodinateX(X);
                int y;
                if(Double.isNaN(Y) == false)
                    y = Converter.getScreenCoodinateY(Y);
                else {
                    y = last_y;
                    last_y -= 15;
                    if(last_y < 40)
                        last_y = h - 40;
                }
                g.setColor(Col);
                g.drawLine(x, y, x, h);
                if(Name != null)
                    g.drawString(Name, x-10, y);
                return last_y;
            }
        }
        
        public void reInitConverter(){
                if(Data == null)
                    return;
                Dimension panel_size = VP.getSize();
                Converter = Data.getConverter(View, panel_size);            
        }
        
        public ArrayList<Mark> Marks = new ArrayList<>();
        public double CursorX = 0,CursorY = 0; 
        public int CursorSn;
        @Override
        public void paint(Graphics g){
            try{
                Dimension panel_size = VP.getSize();
                g.setColor(Color.white);
                g.fillRect(0, 0, panel_size.width, panel_size.height);
                
                if(Data == null)
                    return;
                
                Converter = Data.getConverter(View, panel_size);
                
                // draw grid
                g.setColor(Color.lightGray);
                double vals_x[] = MathTools.getGoodValues(View.X, View.getRight(), panel_size.width/100);
                for(int i = 0;i<vals_x.length;i++){
                    int x = Converter.getScreenCoodinateX(vals_x[i]);
                    g.drawLine(x, 0, x, panel_size.height);
                }
                double vals_y[] = MathTools.getGoodValues(View.Y, View.getTop(), panel_size.height/50);
                for(int i = 0;i<vals_y.length;i++){
                    int y = Converter.getScreenCoodinateY(vals_y[i]);
                    g.drawLine(0, y, panel_size.width, y);
                }
                g.setColor(Color.black);
                for(int i = 0;i<vals_x.length;i++){
                    int x = Converter.getScreenCoodinateX(vals_x[i]);
                    g.drawString(MathTools.getGoodValue(vals_x[i], 1), x+2, 20);
                }
                for(int i = 0;i<vals_y.length;i++){
                    int y = Converter.getScreenCoodinateY(vals_y[i]);
                    g.drawString(MathTools.getGoodValue(vals_y[i], 0), 5, y);
                }
                         
                if(DataSecond != null)
                    DataSecond.drawView(g, Converter, DispSecond,PalitraSecondSpectr);
                if(Data != null)
                    Data.drawView(g, Converter, Disp,PalitraMainSpectr);
                
                if(LineLibSet != null)
                    for(LineLibFoundedItem item : LineLibSet)
                        item.paint(g, Converter);
                
                int last_h = panel_size.height-50;
                for(Mark mark : Marks)
                    last_h = mark.draw(g,panel_size.height,last_h);
                
                int cursor_x = Converter.getScreenCoodinateX(CursorX);
                int cursor_y = Converter.getScreenCoodinateY(CursorY);
                
                g.setColor(Color.blue);
                int y = 25;
                for(String txt : TextInfo){
                    y += 15;
                    g.drawString(txt, 50, y);
                }
                
                g.setColor(Color.green);
                g.drawLine(0, cursor_y, panel_size.width, cursor_y);
                g.drawLine(cursor_x, 0, cursor_x, panel_size.height);
            }catch(Exception ex){
                Log.log(Level.SEVERE,"Spectr painting error...",ex);
            }
        }
    };
    
    CustomDrawing Data;
    CustomDrawing DataSecond;
    SpectrDispers Disp;
    SpectrDispers DispSecond;
    Rectangle3D MinMax,View;
    public void initSecond(SpectrData data) throws IOException{
        if(data == null){
            DataSecond = null;
            DispSecond = null;
            return;
        }
        DataSecond = data.getFullView();
        DispSecond = data.Dispers;
        if(DataSecond != null){
            Rectangle3D r = DataSecond.getDimension();
            MinMax.fitIn(r);
        } else 
            MinMax = Data.getDimension();
        
        double from = DispSecond.getLyByGlobalPixel(0, 0);
        int sn = DispSecond.findSensorByGlobalPixel(MinMax.getRight());
        double to = DispSecond.getLyByGlobalPixel(sn, MinMax.getRight());
        MinMax.X = (float)from;
        MinMax.setRight((float)to);
        updateScrolls();
        VP.repaint();
    }
    
    public Rectangle3D init(SpectrData data,Rectangle3D view) throws IOException{
        if(data == null){
            Data = null;
            return view;
        }
        Data = data.getFullView();
        Disp = data.Dispers;
        MinMax = Data.getDimension();
        double from = Disp.getLyByGlobalPixel(0, 0);
        int sn = Disp.findSensorByGlobalPixel(MinMax.getRight());
        double to = Disp.getLyByGlobalPixel(sn, MinMax.getRight());
        MinMax.X = (float)from;
        MinMax.setRight((float)to);
        if(view == null)
            View = new Rectangle3D(MinMax);
        else
            View = view;
        VP.reInitConverter();
        updateScrolls();
        VP.repaint();
        return View;
    }
    
    static final double ScrollMult = 10.0;
    int getScrollValue(double val){
        return (int)(val*ScrollMult);
    }
    
    boolean IsInUpdateScroll = false;
    private void updateScrolls(){
        try{
            IsInUpdateScroll = true;
            int val = getScrollValue(View.X);
            int ext = getScrollValue(View.Width);
            int min = getScrollValue(MinMax.X);
            int max = getScrollValue(MinMax.getRight());
            jsHorizontal.setValues(val, ext, min, max);

            val = getScrollValue(View.Y);
            ext = getScrollValue(View.Height);
            min = getScrollValue(MinMax.Y);
            max = getScrollValue(MinMax.getTop());
            jsVertical.setValues(max-val-ext, ext, min, max);
        } finally {
            IsInUpdateScroll = false;
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

        jPanel4 = new javax.swing.JPanel();
        jsVertical = new javax.swing.JScrollBar();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jlInfo = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jbViewAll = new javax.swing.JButton();
        jbLibView = new javax.swing.JButton();
        jtfLyJump = new javax.swing.JTextField();
        jpDrawPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jsHorizontal = new javax.swing.JScrollBar();

        setLayout(new java.awt.BorderLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setMinimumSize(new java.awt.Dimension(16, 100));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jsVertical.setVisibleAmount(100);
        jsVertical.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                jsVerticalAdjustmentValueChanged(evt);
            }
        });
        jPanel4.add(jsVertical, java.awt.BorderLayout.CENTER);

        add(jPanel4, java.awt.BorderLayout.EAST);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setMinimumSize(new java.awt.Dimension(0, 30));
        jPanel2.setPreferredSize(new java.awt.Dimension(685, 30));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setMaximumSize(new java.awt.Dimension(2147483647, 25));
        jPanel5.setMinimumSize(new java.awt.Dimension(200, 25));
        jPanel5.setPreferredSize(new java.awt.Dimension(280, 25));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jlInfo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlInfo.setText("-");
        jPanel5.add(jlInfo, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel5, java.awt.BorderLayout.WEST);

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jbViewAll.setText("Все");
        jbViewAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbViewAllActionPerformed(evt);
            }
        });

        jbLibView.setText("Библиотека");
        jbLibView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbLibViewActionPerformed(evt);
            }
        });

        jtfLyJump.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfLyJump.setText("0");
        jtfLyJump.setToolTipText("Быстрый переход");
        jtfLyJump.setMinimumSize(new java.awt.Dimension(50, 20));
        jtfLyJump.setPreferredSize(new java.awt.Dimension(60, 20));
        jtfLyJump.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfLyJumpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 181, Short.MAX_VALUE)
                .addComponent(jtfLyJump, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbLibView)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbViewAll))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jbViewAll, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addComponent(jbLibView, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addComponent(jtfLyJump, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jpDrawPanel.setBackground(new java.awt.Color(255, 255, 255));
        jpDrawPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        jpDrawPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jpDrawPanelMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jpDrawPanelMouseMoved(evt);
            }
        });
        jpDrawPanel.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jpDrawPanelMouseWheelMoved(evt);
            }
        });
        jpDrawPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpDrawPanelMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jpDrawPanelMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jpDrawPanelLayout = new javax.swing.GroupLayout(jpDrawPanel);
        jpDrawPanel.setLayout(jpDrawPanelLayout);
        jpDrawPanelLayout.setHorizontalGroup(
            jpDrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 683, Short.MAX_VALUE)
        );
        jpDrawPanelLayout.setVerticalGroup(
            jpDrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );

        jPanel1.add(jpDrawPanel, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setMinimumSize(new java.awt.Dimension(100, 16));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jsHorizontal.setMaximum(1000);
        jsHorizontal.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        jsHorizontal.setVisibleAmount(1000);
        jsHorizontal.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                jsHorizontalAdjustmentValueChanged(evt);
            }
        });
        jPanel3.add(jsHorizontal, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    int Mx,My;
    double Vx,Vy;
    double Pixel[] = new double[128];
    int Sn[] = new int[128];
    int SnCount;
    void checkMouseCoordinates(MouseEvent evt){
        if(Converter == null)
            return;
        Mx = evt.getX();
        My = evt.getY();
        Vx = Converter.getVirtalCoodinateX(Mx);
        Vy = Converter.getVirtalCoodinateY(My);
        SnCount = Disp.findSensorByLy(Vx, Sn);
        for(int i = 0;i<SnCount;i++)
            Pixel[i] = Disp.getGlobalPixelByLy(Sn[i], Vx);
    }
    
    public int[] findSensorByLy(double ly){
        int[] sn = new int[16];
        int n = Disp.findSensorByLy(ly, sn);
        int[] ret = new int[n];
        for(int i = 0;i<n;i++)
            ret[i] = sn[i];
        return ret;
    }

    private void jpDrawPanelMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jpDrawPanelMouseWheelMoved
        try{
            checkMouseCoordinates(evt);
            double step = View.Width/10;
            int sign = evt.getWheelRotation();
            double k = Mx/(double)jpDrawPanel.getWidth();
            double left = View.X-step*sign*k;
            if(left < MinMax.X)
                left = MinMax.X;
            double right = View.getRight()+step*sign*(1-k);
            if(right > MinMax.getRight())
                right = MinMax.getRight();
            View.X = (float)left;
            View.setRight((float)right);
            reFresh(true);
        }catch(Exception ex){
            Log.log(Level.SEVERE,"Mouse wheel exception: ",ex);
        }
    }//GEN-LAST:event_jpDrawPanelMouseWheelMoved

    private void jpDrawPanelMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpDrawPanelMouseMoved
        try{
            checkMouseCoordinates(evt);
            
            String txt = String.format("Ly:%4.1f Y:%5.0f s/n_",Vx,Vy);
            for(int i = 0;i<SnCount;i++)
               txt += (Sn[i]+1)+":"+(int)(Pixel[i])+"_";
            jlInfo.setText(txt);
        }catch(Exception ex){
            Log.log(Level.SEVERE,"Mouse move exception: ",ex);
        }
    }//GEN-LAST:event_jpDrawPanelMouseMoved

    private void jpDrawPanelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpDrawPanelMouseDragged
        try{
            checkMouseCoordinates(evt);
            double dltx = PressedConverter.getVirtalCoodinateX(PressedX)-
                    PressedConverter.getVirtalCoodinateX(evt.getX());
            
            View = new Rectangle3D(PressedView);
            double min = View.X;
            double max = View.getRight();
            min += dltx;
            max += dltx;
            if(min < MinMax.X){
                double dlt = MinMax.X - min;
                min = MinMax.X;
                max += dlt;
            }
            if(max > MinMax.getRight()){
                double dlt = max-MinMax.getRight();
                min -= dlt;
                max = MinMax.getRight();
            }
            View.X = (float)min;
            View.setRight((float)max);
            
            reFresh(true);
        }catch(Exception ex){
            Log.log(Level.SEVERE,"Mouse dragge exception: ",ex);
        }
    }//GEN-LAST:event_jpDrawPanelMouseDragged

    public double getCursorX(){
        return VP.CursorX;
    }
    
    private void jpDrawPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpDrawPanelMouseClicked
        try{
            checkMouseCoordinates(evt);
            VP.CursorX = Vx;
            VP.CursorY = Vy;
            reFresh(false);
        }catch(Exception ex){
            Log.log(Level.SEVERE,"Mouse click exception: ",ex);
        }
    }//GEN-LAST:event_jpDrawPanelMouseClicked

    void reFresh(boolean update_srolls){
        if(update_srolls)
            updateScrolls();
        jpDrawPanel.repaint();
    }
    
    private void jbViewAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbViewAllActionPerformed
        try{
            View = new Rectangle3D(MinMax);
            reFresh(true);
        }catch(Exception ex){
            Log.log(Level.SEVERE,"All click exception: ",ex);
        }
    }//GEN-LAST:event_jbViewAllActionPerformed

    int PressedX,PressedY;
    Rectangle3D PressedView;
    CustomDrawingConverter PressedConverter;
    private void jpDrawPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpDrawPanelMousePressed
        try{
            //checkMouseCoordinates(evt);
            PressedX = evt.getX();
            PressedY = evt.getY();
            PressedView = new Rectangle3D(View);
            PressedConverter = new CustomDrawingConverter(Converter);
        }catch(Exception ex){
            Log.log(Level.SEVERE,"Mouse press exception ",ex);
        }
    }//GEN-LAST:event_jpDrawPanelMousePressed

    private void jsHorizontalAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {//GEN-FIRST:event_jsHorizontalAdjustmentValueChanged
        try{
            if(IsInUpdateScroll)
                return;
            double val = jsHorizontal.getValue() / ScrollMult;
            View.X = (float)val;
            reFresh(false);
        }catch(Exception ex){
            Log.log(Level.SEVERE,"HScroll moved exception ",ex);
        }
    }//GEN-LAST:event_jsHorizontalAdjustmentValueChanged

    private void jsVerticalAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {//GEN-FIRST:event_jsVerticalAdjustmentValueChanged
        try{
            if(IsInUpdateScroll)
                return;
            double val = jsVertical.getValue();
            //real = max-real_val-ext
            val = jsVertical.getMaximum() - val - jsVertical.getVisibleAmount();
            View.Y = (float)(val/ScrollMult);
            reFresh(false);
        }catch(Exception ex){
            Log.log(Level.SEVERE,"VScroll moved exception ",ex);
        }
    }//GEN-LAST:event_jsVerticalAdjustmentValueChanged

    private void jbLibViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbLibViewActionPerformed
        try{
            LineFilter.setVisible(true);
            LineLibSet = LineFilter.Results;
            reFresh(false);
        }catch(Exception ex){
            Log.log(Level.SEVERE,"VScroll moved exception ",ex);
        }
    }//GEN-LAST:event_jbLibViewActionPerformed

    private void jtfLyJumpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfLyJumpActionPerformed
        try{
            float ly = MathTools.parseFloat(jtfLyJump.getText());
            if(ly < MinMax.X || ly > MinMax.getRight())
                return;
            VP.CursorX = ly;
            float width = 50;
            if(width > View.Width/2)
                width = View.Width/2;
            float ly_from = ly-width;
            float ly_to = ly+width;
            if(ly_from < MinMax.X)
                ly_from = MinMax.X;
            if(ly_to > MinMax.getRight())
                ly_to = MinMax.getRight();
            View.X = ly_from;
            View.setRight(ly_to);
            reFresh(true);
        }catch(Exception ex){
            Log.log(Level.SEVERE,"VScroll moved exception ",ex);
        }
    }//GEN-LAST:event_jtfLyJumpActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JButton jbLibView;
    private javax.swing.JButton jbViewAll;
    private javax.swing.JLabel jlInfo;
    private javax.swing.JPanel jpDrawPanel;
    private javax.swing.JScrollBar jsHorizontal;
    private javax.swing.JScrollBar jsVertical;
    private javax.swing.JTextField jtfLyJump;
    // End of variables declaration//GEN-END:variables
}
